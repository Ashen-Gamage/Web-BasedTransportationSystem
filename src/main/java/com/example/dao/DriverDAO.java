package com.example.dao;

import com.example.model.Ride;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    // Create a driver row
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

    // Check if driver already exists
    public boolean driverExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM drivers WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Fetch rides assigned to this driver (including open states)
    public List<Ride> getAssignedRides(int driverId) throws SQLException {
        List<Ride> rides = new ArrayList<>();
        String sql = """
            SELECT id, rider_id, driver_id, pickup, dropoff, status, fare, distance_km
            FROM rides
            WHERE driver_id = ? AND status IN ('ASSIGNED','ACCEPTED','ON_THE_WAY','IN_RIDE')
            ORDER BY id DESC
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, driverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ride ride = new Ride();
                    ride.setId(rs.getInt("id"));
                    ride.setRiderId((Integer) rs.getObject("rider_id"));
                    ride.setDriverId((Integer) rs.getObject("driver_id"));
                    ride.setPickup(rs.getString("pickup"));
                    ride.setDropoff(rs.getString("dropoff"));
                    ride.setStatus(rs.getString("status"));
                    ride.setFare(rs.getBigDecimal("fare"));
                    ride.setDistanceKm(rs.getBigDecimal("distance_km"));
                    rides.add(ride);
                }
            }
        }
        return rides;
    }

    // Accept ride (set ACCEPTED + timestamp)
    public void acceptRide(int rideId, int driverId) throws SQLException {
        String sql = "UPDATE rides SET status = 'ACCEPTED', accepted_at = NOW() WHERE id = ? AND driver_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rideId);
            ps.setInt(2, driverId);
            ps.executeUpdate();
        }
    }

    // Update ride status to ON_THE_WAY / IN_RIDE / COMPLETED
    public void updateRideStatus(int rideId, String status) throws SQLException {
        String sql = "UPDATE rides SET status = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, rideId);
            ps.executeUpdate();
        }
    }
}
