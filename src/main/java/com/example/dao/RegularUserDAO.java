package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;

public class RegularUserDAO {

    public void createRegularUser(int userId, String paymentInfo) throws SQLException {
        String sql = "INSERT INTO regular_users (user_id, payment_info) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, paymentInfo);
            ps.executeUpdate();
        }
    }
}
