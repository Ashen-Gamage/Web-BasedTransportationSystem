package com.example.ridebooking.dao;

import com.example.common.utils.DBUtil;
import com.example.ridebooking.model.RideRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List; // ✅ Correct import for List

public class RideBookingDAO {

    // Save a new ride request
    public void saveRideRequest(RideRequest ride) throws SQLException {
        String sql = "INSERT INTO ride_requests (user_id, pickup_location, dropoff_location, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ride.getUserId());
            stmt.setString(2, ride.getPickupLocation());
            stmt.setString(3, ride.getDropoffLocation());
            stmt.setString(4, ride.getStatus());
            stmt.executeUpdate();
        }
    }

    // Retrieve all pending rides for a user
    public List<RideRequest> getPendingRidesByUser(int userId) throws SQLException {
        String sql = "SELECT id, user_id, pickup_location, dropoff_location, status, request_time " +
                "FROM ride_requests WHERE user_id = ? ORDER BY request_time DESC";

        List<RideRequest> rides = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RideRequest ride = new RideRequest();
                    ride.setId(rs.getInt("id"));
                    ride.setUserId(rs.getInt("user_id"));
                    ride.setPickupLocation(rs.getString("pickup_location"));
                    ride.setDropoffLocation(rs.getString("dropoff_location"));
                    ride.setStatus(rs.getString("status"));

                    Timestamp ts = rs.getTimestamp("request_time");
                    if (ts != null) {
                        ride.setRequestTime(ts.toLocalDateTime());
                    }

                    rides.add(ride);
                }
            }
        }
        return rides;
    }
    // Retrieve a single ride by id
    public RideRequest getRideById(int id) throws SQLException {
        String sql = "SELECT id, user_id, pickup_location, dropoff_location, status, request_time FROM ride_requests WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RideRequest ride = new RideRequest();
                    ride.setId(rs.getInt("id"));
                    ride.setUserId(rs.getInt("user_id"));
                    ride.setPickupLocation(rs.getString("pickup_location"));
                    ride.setDropoffLocation(rs.getString("dropoff_location"));
                    ride.setStatus(rs.getString("status"));

                    Timestamp ts = rs.getTimestamp("request_time");
                    if (ts != null) {
                        ride.setRequestTime(ts.toLocalDateTime());
                    }
                    return ride;
                }
            }
        }
        return null;
    }

    /**
     * Update pickup_location, dropoff_location and request_time.
     * Uses WHERE id = ? AND user_id = ? to ensure the user owns the ride.
     * Returns number of rows updated (0 if not found or not owned).
     */
    public int updateRide(RideRequest ride) throws SQLException {
        String sql = "UPDATE ride_requests SET pickup_location = ?, dropoff_location = ?, request_time = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ride.getPickupLocation());
            ps.setString(2, ride.getDropoffLocation());
            ps.setTimestamp(3, ride.getRequestTime() != null ? Timestamp.valueOf(ride.getRequestTime()) : null);
            ps.setInt(4, ride.getId());
            ps.setInt(5, ride.getUserId());
            return ps.executeUpdate();
        }
    }

    /**
     * Delete a ride only if it belongs to the provided userId.
     * Returns number of rows deleted (0 if not found or not owned).
     */
    public int deleteRide(int id, int userId) throws SQLException {
        String sql = "DELETE FROM ride_requests WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        }
    }
}
