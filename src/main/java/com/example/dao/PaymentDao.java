package com.example.dao;

import com.example.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

    // Inner DTO class
    public static class PaymentRow {
        public int id, rideId, payerId;
        public long amountCents;
        public String status;
        public String method;  // <-- add this

        public int getId() { return id; }
        public int getRideId() { return rideId; }
        public int getPayerId() { return payerId; }
        public long getAmount() { return amountCents; }
        public String getStatus() { return status; }
        public String getMethod() { return method; }  // <-- add getter
    }

    // Get all payments
    public List<PaymentRow> findAll() throws Exception {
        List<PaymentRow> list = new ArrayList<>();
        String sql = "SELECT id, ride_id, payer_id, CAST(amount*100 AS BIGINT) AS amount_cents, status, method FROM payments"; // include method
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PaymentRow p = new PaymentRow();
                p.id = rs.getInt("id");
                p.rideId = rs.getInt("ride_id");
                p.payerId = rs.getInt("payer_id");
                p.amountCents = rs.getLong("amount_cents");
                p.status = rs.getString("status");
                p.method = rs.getString("method"); // <-- populate method
                list.add(p);
            }
        }
        return list;
    }


    // Update payment
    public void updatePayment(int paymentId, String status, String method) throws Exception {
        String sql = "UPDATE payments SET status=?, method=?, paid_at=GETDATE() WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, method);
            ps.setInt(3, paymentId);
            ps.executeUpdate();
        }
    }

    // Delete payment
    public void deletePayment(int paymentId) throws Exception {
        String sql = "DELETE FROM payments WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        }
    }

    // Create payment
    public void createPayment(int rideId, int payerId, long amountCents, String method) throws Exception {
        String sql = "INSERT INTO payments(ride_id, payer_id, amount, method, status, currency) VALUES (?, ?, ?, ?, 'PENDING', 'LKR')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            ps.setInt(2, payerId);
            ps.setBigDecimal(3, new java.math.BigDecimal(amountCents).movePointLeft(2));
            ps.setString(4, method);
            ps.executeUpdate();
        }
    }
}
