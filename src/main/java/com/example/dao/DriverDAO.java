package com.example.dao;

import com.example.util.DBUtil;

import java.sql.*;

public class DriverDAO {

    public void createDriver(int userId, String licenseNumber, String vehicleType) throws SQLException {
        String sql = "INSERT INTO drivers (user_id, license_number, vehicle_type) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, licenseNumber);
            ps.setString(3, vehicleType);
            ps.executeUpdate();
        }
    }
}
