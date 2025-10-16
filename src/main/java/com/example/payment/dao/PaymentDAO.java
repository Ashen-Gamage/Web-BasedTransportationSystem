package com.example.payment.dao;

import com.example.common.utils.DBUtil;
import com.example.payment.model.PaymentCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void addPaymentCard(PaymentCard card) throws SQLException {
        String sql = "INSERT INTO payment_cards (user_id, card_number, expiry_date, cardholder_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, card.getUserId());
            stmt.setString(2, card.getCardNumber());
            stmt.setString(3, card.getExpiryDate());
            stmt.setString(4, card.getCardholderName());
            stmt.executeUpdate();
        }
    }

    public List<PaymentCard> getPaymentCardsByUserId(int userId) throws SQLException {
        List<PaymentCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM payment_cards WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaymentCard card = new PaymentCard();
                    card.setId(rs.getInt("id"));
                    card.setUserId(rs.getInt("user_id"));
                    card.setCardNumber(rs.getString("card_number"));
                    card.setExpiryDate(rs.getString("expiry_date"));
                    card.setCardholderName(rs.getString("cardholder_name"));
                    card.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    cards.add(card);
                }
            }
        }
        return cards;
    }
}