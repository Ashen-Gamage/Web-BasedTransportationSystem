// src/main/java/com/example/rating/service/RatingService.java
package com.example.rating.service;

import com.example.rating.dao.RatingDAO;
import com.example.rating.model.Rating;

import java.sql.SQLException;
import java.util.List;

public class RatingService {
    private final RatingDAO ratingDAO = new RatingDAO();

    // Service method to add a rating, with basic validation
    public void addRating(Rating rating) throws SQLException {
        System.out.println("Executing addRating in service: Validating rating value");  // Console print to verify service layer entry
        if (rating.getRating() < 1 || rating.getRating() > 5) {
            System.err.println("Invalid rating: Must be between 1 and 5");  // Console print for validation error
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratingDAO.insertRating(rating);
        System.out.println("addRating in service completed: Rating added successfully");  // Console print to verify success
    }

    // Service method to get ratings for a driver
    public List<Rating> getRatingsForDriver(int driverId) throws SQLException {
        System.out.println("Executing getRatingsForDriver in service: Fetching ratings");  // Console print to verify service layer entry
        List<Rating> ratings = ratingDAO.getRatingsByDriverId(driverId);
        System.out.println("getRatingsForDriver in service completed: " + ratings.size() + " ratings retrieved");  // Console print to verify success
        return ratings;
    }
    // Service method to get ratings for a user
    public List<Rating> getRatingsByUserId(int userId) throws SQLException {
        System.out.println("Executing getRatingsByUserId in service: Fetching ratings for user " + userId);
        List<Rating> ratings = ratingDAO.getRatingsByUserId(userId); // ✅ correct DAO method
        System.out.println("getRatingsByUserId in service completed: " + ratings.size() + " ratings retrieved");
        return ratings;
    }

    // Service method to get average rating
    public double getAverageRating(int driverId) throws SQLException {
        System.out.println("Executing getAverageRating in service: Calculating average");  // Console print to verify service layer entry
        double average = ratingDAO.getAverageRatingForDriver(driverId);
        System.out.println("getAverageRating in service completed: Average = " + average);  // Console print to verify success
        return average;
    }
    private int getDriverIdForRide(int rideId) throws SQLException {
        return 1;  // Placeholder; replace with actual query
    }
    public List<Integer> getCompletedRidesForUser(int userId) throws SQLException {
        return ratingDAO.getCompletedRidesForUser(userId);
    }
    public boolean updateRating(int id, int ratingValue, String feedback) {
        try {
            return ratingDAO.updateRating(id, ratingValue, feedback);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRating(int id) {
        try {
            return ratingDAO.deleteRating(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}