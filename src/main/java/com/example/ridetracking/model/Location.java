package com.example.ridetracking.model;

import java.time.LocalDateTime;

public class Location {
    private int id;
    private int rideRequestId;
    private double latitude;
    private double longitude;
    private LocalDateTime updateTime;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRideRequestId() {
        return rideRequestId;
    }

    public void setRideRequestId(int rideRequestId) {
        this.rideRequestId = rideRequestId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}