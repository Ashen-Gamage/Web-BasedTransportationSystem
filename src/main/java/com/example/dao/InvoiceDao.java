package com.example.dao;
import java.sql.*;
import com.example.util.DBUtil;


public class InvoiceDao {
    public int create(Connection c, int rideId, int userId, long totalCents, long driverCents, long feeCents, String html) throws Exception {
        String invNo = "INV-" + System.currentTimeMillis();
        try (PreparedStatement ps = c.prepareStatement("""
      INSERT INTO invoices(invoice_no, ride_id, user_id, amount_cents, driver_amount_cents, platform_fee_cents, status, html)
      VALUES(?,?,?,?,?,?, 'PAID', ?);
      SELECT SCOPE_IDENTITY();
    """)) {
            ps.setString(1, invNo);
            ps.setInt(2, rideId);
            ps.setInt(3, userId);
            ps.setLong(4, totalCents);
            ps.setLong(5, driverCents);
            ps.setLong(6, feeCents);
            ps.setString(7, html);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
    // Read by ID
    public InvoiceRow findById(int id) throws Exception {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM invoices WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                InvoiceRow inv = new InvoiceRow();
                inv.id = rs.getInt("id");
                inv.invoiceNo = rs.getString("invoice_no");
                inv.userId = rs.getInt("user_id");
                inv.status = rs.getString("status");
                return inv;
            }
        }
    }

    // Update invoice status
    public void updateStatus(int id, String status) throws Exception {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE invoices SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // Delete invoice (admin only)
    public void delete(int id) throws Exception {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM invoices WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Helper DTO
    public static class InvoiceRow {
        public int id, userId;
        public String invoiceNo, status;
    }

}
