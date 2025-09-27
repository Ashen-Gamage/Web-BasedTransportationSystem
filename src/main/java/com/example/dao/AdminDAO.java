package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;

public class AdminDAO {

    public void createAdmin(int userId, String adminCode) throws SQLException {
        String sql = "INSERT INTO admins (user_id, admin_code) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, adminCode);
            ps.executeUpdate();
        }
    }
}
