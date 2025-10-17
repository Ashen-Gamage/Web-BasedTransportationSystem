package com.example.rating.model;

import java.time.LocalDateTime;

public class Rating {
    private int id;
    private int rideRequestId;
    private int userId;  // Rider ID
    private int driverId;  // Driver ID
    private int rating;  // 1-5 stars
    private String feedback;
    private LocalDateTime createdAt;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRideRequestId() { return rideRequestId; }
    public void setRideRequestId(int rideRequestId) { this.rideRequestId = rideRequestId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}