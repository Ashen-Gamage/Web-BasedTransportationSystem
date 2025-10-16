package com.example.drivermanagement.dao;

import com.example.common.utils.DBUtil;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    public void saveDriver(Driver driver) throws SQLException {
        String sql = "INSERT INTO drivers (user_id, license_number, vehicle_type, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driver.getUserId());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getVehicleType());
            stmt.setString(4, driver.getStatus());
            stmt.executeUpdate();
        }
    }

    public Driver findDriverByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setUserId(rs.getInt("user_id"));
                    driver.setLicenseNumber(rs.getString("license_number"));
                    driver.setVehicleType(rs.getString("vehicle_type"));
                    driver.setStatus(rs.getString("status"));
                    return driver;
                }
            }
            return null;
        }
    }

    public List<Driver> findAvailableDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE status = 'available'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt("id"));
                driver.setUserId(rs.getInt("user_id"));
                driver.setLicenseNumber(rs.getString("license_number"));
                driver.setVehicleType(rs.getString("vehicle_type"));
                driver.setStatus(rs.getString("status"));
                drivers.add(driver);
            }
        }
        return drivers;
    }

    public void updateDriverStatus(int driverId, String status) throws SQLException {
        String sql = "UPDATE drivers SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, driverId);
            stmt.executeUpdate();
        }
    }

    public void assignDriver(Assignment assignment) throws SQLException {
        String sql = "INSERT INTO assignments (ride_request_id, driver_id, assigned_time) VALUES (?, ?, GETDATE())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assignment.getRideRequestId());
            stmt.setInt(2, assignment.getDriverId());
            stmt.executeUpdate();
        }
    }
    public List<Driver> findDriversByStatus(String status) throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE status = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status); // Set status parameter
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Driver driver = new Driver();
                    driver.setId(rs.getInt("id"));
                    driver.setUserId(rs.getInt("user_id"));
                    driver.setLicenseNumber(rs.getString("license_number"));
                    driver.setVehicleType(rs.getString("vehicle_type"));
                    driver.setStatus(rs.getString("status"));
                    drivers.add(driver);
                }
            }
        }

        return drivers;
    }
    public List<Driver> findActiveDrivers() throws SQLException {
        return findDriversByStatus("Active");
    }

}