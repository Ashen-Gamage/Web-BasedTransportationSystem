package com.example.drivermanagement.dao;

import com.example.common.utils.DBUtil;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;
import com.example.ridebooking.model.RideRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

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

    public void updateDriver(Driver driver) throws SQLException {
        String sql = "UPDATE drivers SET license_number = ?, vehicle_type = ?, status = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getLicenseNumber());
            stmt.setString(2, driver.getVehicleType());
            stmt.setString(3, driver.getStatus());
            stmt.setInt(4, driver.getUserId());
            stmt.executeUpdate();
        }
    }

    public List<Assignment> getAssignmentsByDriverId(int driverId) throws SQLException {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignments WHERE driver_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setId(rs.getInt("id"));
                    assignment.setRideRequestId(rs.getInt("ride_request_id"));
                    assignment.setDriverId(rs.getInt("driver_id"));
                    assignment.setAssignedTime(rs.getTimestamp("assigned_time").toLocalDateTime());
                    assignments.add(assignment);
                }
            }
        }
        return assignments;
    }

    public List<RideRequest> getRideRequestsByDriverId(int driverId) throws SQLException {
        List<RideRequest> requests = new ArrayList<>();
        String sql = "SELECT rr.* FROM ride_requests rr JOIN assignments a ON rr.id = a.ride_request_id WHERE a.driver_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
 ждать
                while (rs.next()) {
                    RideRequest request = new RideRequest();
                    request.setId(rs.getInt("id"));
                    request.setUserId(rs.getInt("user_id"));
                    request.setPickupLocation(rs.getString("pickup_location"));
                    request.setDropoffLocation(rs.getString("dropoff_location"));
                    request.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());
                    request.setStatus(rs.getString("status"));
                    requests.add(request);
                }
            }
        }
        return requests;
    }
}