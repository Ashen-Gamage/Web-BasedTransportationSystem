package com.example.payment.service;

import com.example.common.utils.DBUtil;
import com.example.payment.dao.PaymentDAO;
import com.example.payment.model.PaymentCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public void addPaymentCard(PaymentCard card) throws SQLException {
        // Validate card number: exactly 16 digits
        if (!card.getCardNumber().matches("\\d{16}")) {
            throw new SQLException("Card number must be exactly 16 digits");
        }

        // Validate expiry date: MM/YY format and not expired
        try {
            YearMonth expiry = YearMonth.parse(card.getExpiryDate(), DateTimeFormatter.ofPattern("MM/yy"));
            if (expiry.isBefore(YearMonth.now())) {
                throw new SQLException("Card has expired");
            }
        } catch (Exception e) {
            throw new SQLException("Invalid expiry date format (use MM/YY)");
        }

        paymentDAO.addPaymentCard(card);
    }

    public PaymentCard getPaymentCardById(int id) throws SQLException {
        return paymentDAO.findById(id);
    }

    public void updatePaymentCard(PaymentCard card) throws SQLException {
        // dao.update ensures the update only happens for the correct id and user_id
        int updated = paymentDAO.update(card);
        if (updated == 0) {
            throw new SQLException("Update failed — card not found or not owned by user.");
        }
    }

    public void deletePaymentCard(int cardId, int userId) throws SQLException {
        int deleted = paymentDAO.deleteByIdAndUser(cardId, userId);
        if (deleted == 0) {
            throw new SQLException("Delete failed — card not found or not owned by user.");
        }
    }

    public PaymentCard findById(int id) throws SQLException {
        String sql = "SELECT id, user_id, card_number, expiry_date, cardholder_name, created_at FROM payment_cards WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaymentCard c = new PaymentCard();
                    c.setId(rs.getInt("id"));
                    c.setUserId(rs.getInt("user_id"));
                    c.setCardNumber(rs.getString("card_number"));
                    c.setExpiryDate(rs.getString("expiry_date"));
                    c.setCardholderName(rs.getString("cardholder_name"));
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return c;
                }
                return null;
            }
        }
    }

    public int update(PaymentCard card) throws SQLException {
        String sql = "UPDATE payment_cards SET card_number = ?, expiry_date = ?, cardholder_name = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getCardNumber());
            ps.setString(2, card.getExpiryDate());
            ps.setString(3, card.getCardholderName());
            ps.setInt(4, card.getId());
            ps.setInt(5, card.getUserId());
            return ps.executeUpdate();
        }
    }

    public int deleteByIdAndUser(int id, int userId) throws SQLException {
        String sql = "DELETE FROM payment_cards WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        }
    }

    public List<PaymentCard> getPaymentCards(int userId) throws SQLException {
        return paymentDAO.getPaymentCardsByUserId(userId);
    }
}