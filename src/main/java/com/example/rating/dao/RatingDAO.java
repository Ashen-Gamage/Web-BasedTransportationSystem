package com.example.rating.dao;

import com.example.common.utils.DBUtil;
import com.example.rating.model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {

    // Method to insert a new rating into the database
    public void insertRating(Rating rating) throws SQLException {
        System.out.println("RatingDAO.insertRating: Preparing to insert rating for ride ID " + rating.getRideRequestId());
        String sql = "INSERT INTO ratings (ride_request_id, user_id, driver_id, rating, feedback) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rating.getRideRequestId());
            stmt.setInt(2, rating.getUserId());
            stmt.setInt(3, rating.getDriverId());
            stmt.setInt(4, rating.getRating());
            stmt.setString(5, rating.getFeedback());
            int rows = stmt.executeUpdate();
            System.out.println("RatingDAO.insertRating: Inserted " + rows + " row(s)");
        } catch (SQLException e) {
            System.err.println("RatingDAO.insertRating: Error - " + e.getMessage());
            throw e;
        }
    }
    // Method to retrieve all ratings for a specific user
    public List<Rating> getRatingsByUserId(int userId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Executing getRatingsByUserId: Fetching ratings for user ID " + userId);
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rating rating = new Rating();
                    rating.setId(rs.getInt("id"));
                    rating.setRideRequestId(rs.getInt("ride_request_id"));
                    rating.setUserId(rs.getInt("user_id"));
                    rating.setDriverId(rs.getInt("driver_id"));
                    rating.setRating(rs.getInt("rating"));
                    rating.setFeedback(rs.getString("feedback"));
                    rating.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ratings.add(rating);
                }
                System.out.println("getRatingsByUserId completed: Retrieved " + ratings.size() + " ratings");
            }

        } catch (SQLException e) {
            System.err.println("Error in getRatingsByUserId: " + e.getMessage());
            throw e;
        }

        return ratings;
    }

    // Method to retrieve all ratings for a specific driver
    public List<Rating> getRatingsByDriverId(int driverId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings WHERE driver_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Executing getRatingsByDriverId: Fetching ratings for driver ID " + driverId);  // Console print to verify this part runs
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rating rating = new Rating();
                    rating.setId(rs.getInt("id"));
                    rating.setRideRequestId(rs.getInt("ride_request_id"));
                    rating.setUserId(rs.getInt("user_id"));
                    rating.setDriverId(rs.getInt("driver_id"));
                    rating.setRating(rs.getInt("rating"));
                    rating.setFeedback(rs.getString("feedback"));
                    rating.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ratings.add(rating);
                }
                System.out.println("getRatingsByDriverId completed: Retrieved " + ratings.size() + " ratings");  // Console print to verify success
            }
        } catch (SQLException e) {
            System.err.println("Error in getRatingsByDriverId: " + e.getMessage());  // Console print for error verification
            throw e;
        }
        return ratings;
    }

    // Method to calculate average rating for a driver
    public double getAverageRatingForDriver(int driverId) throws SQLException {
        String sql = "SELECT AVG(rating) AS average FROM ratings WHERE driver_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Executing getAverageRatingForDriver: Calculating average for driver ID " + driverId);  // Console print to verify this part runs
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double average = rs.getDouble("average");
                    System.out.println("getAverageRatingForDriver completed: Average rating = " + average);  // Console print to verify success
                    return average;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAverageRatingForDriver: " + e.getMessage());  // Console print for error verification
            throw e;
        }
        System.out.println("getAverageRatingForDriver: No ratings found, returning 0");  // Console print if no data
        return 0.0;
    }
    // inside com.example.rating.dao.RatingDAO (or a new RideAssignmentDAO)
    private int getDriverIdForRide(int rideId) throws SQLException {
        String sql = "SELECT d.id AS driver_id " +
                "FROM assignments a " +
                "JOIN drivers d ON a.driver_id = d.user_id " +
                "WHERE a.ride_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("driver_id"); // foreign key for ratings table
                }
            }
        }
        throw new SQLException("Driver not found for ride ID " + rideId);
    }


    public boolean isRideCompleted(int rideId) throws SQLException {
        String sql = "SELECT status FROM ride_requests WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return "completed".equalsIgnoreCase(status);
                } else {
                    throw new SQLException("Ride not found for id: " + rideId);
                }
            }
        }
    }

    public boolean hasRatingForRide(int rideId) throws SQLException {
        String sql = "SELECT COUNT(*) as cnt FROM ratings WHERE ride_request_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        }
        return false;
    }
    public List<Integer> getCompletedRidesForUser(int userId) throws SQLException {
        List<Integer> rideIds = new ArrayList<>();
        String sql = "SELECT id FROM ride_requests WHERE user_id = ? AND status = 'completed'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rideIds.add(rs.getInt("id"));
                }
            }
        }
        return rideIds;
    }

    public List<Rating> getAllRatings() throws SQLException {
        String sql = "SELECT * FROM ratings";
        List<Rating> ratings = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ratings.add(mapResultSetToRating(rs));
            }
        }
        return ratings;
    }

    // ✅ Helper method to map a ResultSet row to a Rating object
    private Rating mapResultSetToRating(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setId(rs.getInt("id"));
        rating.setRideRequestId(rs.getInt("ride_request_id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setDriverId(rs.getInt("driver_id"));
        rating.setRating(rs.getInt("rating"));
        rating.setFeedback(rs.getString("feedback"));
        // Defensive check for null timestamp
        java.sql.Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            rating.setCreatedAt(ts.toLocalDateTime());
        }
        return rating;
    }
    // Update rating
    public boolean updateRating(int id, int ratingValue, String feedback) throws SQLException {
        String sql = "UPDATE ratings SET rating = ?, feedback = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ratingValue);
            ps.setString(2, feedback);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Delete rating
    public boolean deleteRating(int id) throws SQLException {
        String sql = "DELETE FROM ratings WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

}