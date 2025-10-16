    package com.example.payment.dao;

    import com.example.common.utils.DBUtil;
    import com.example.payment.model.PaymentCard;

    import java.sql.*;
    import java.time.LocalDateTime;
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
            String sql = "SELECT id, user_id, card_number, expiry_date, cardholder_name, created_at " +
                    "FROM payment_cards WHERE user_id = ? ORDER BY created_at DESC";

            List<PaymentCard> cards = new ArrayList<>();

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

                        Timestamp ts = rs.getTimestamp("created_at");
                        if (ts != null) {
                            card.setCreatedAt(ts.toLocalDateTime());
                        }

                        cards.add(card);
                    }
                }
            }

            return cards;
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

                        Timestamp ts = rs.getTimestamp("created_at");
                        if (ts != null) {
                            c.setCreatedAt(ts.toLocalDateTime());
                        }

                        return c;
                    }
                }
            }
            return null;
        }

        /**
         * Update card fields. The WHERE clause ensures the user owns the card.
         * Returns number of rows updated (0 if not found or not owned).
         */
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

        /**
         * Delete a card only if it belongs to the provided userId.
         * Returns number of rows deleted (0 if not found or not owned).
         */
        public int deleteByIdAndUser(int id, int userId) throws SQLException {
            String sql = "DELETE FROM payment_cards WHERE id = ? AND user_id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, userId);
                return ps.executeUpdate();
            }
        }
    }