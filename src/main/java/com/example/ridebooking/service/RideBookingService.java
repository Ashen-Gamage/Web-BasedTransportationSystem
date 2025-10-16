package com.example.ridebooking.service;

import com.example.ridebooking.dao.RideBookingDAO;
import com.example.ridebooking.model.RideRequest;

import java.sql.SQLException;
import java.util.List;

public class RideBookingService {
    private final RideBookingDAO dao = new RideBookingDAO();

    public RideRequest getRideById(int id) throws SQLException {
        return dao.getRideById(id);
    }
    public void bookRide(RideRequest ride) throws SQLException {
        dao.saveRideRequest(ride);
    }
    // ✅ New method to fetch pending rides
    public List<RideRequest> getPendingRidesByUser(int userId) throws SQLException {
        return dao.getPendingRidesByUser(userId);
    }
    public void deleteRide(int rideId, int userId) throws SQLException {
        int rowsDeleted = dao.deleteRide(rideId, userId);
        if (rowsDeleted == 0) {
            throw new SQLException("Ride not found or not owned by this user.");
        }
    }
    /**
     * Update ride and ensure ownership.
     * Throws SQLException if no row was updated (not found or not owned).
     */
    public void updateRide(RideRequest ride) throws SQLException {
        int updated = dao.updateRide(ride);
        if (updated == 0) {
            throw new SQLException("Update failed — ride not found or not owned by user.");
        }
    }


}