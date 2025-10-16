package com.example.drivermanagement.service;

import com.example.drivermanagement.dao.DriverDAO;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;
import com.example.drivermanagement.model.RideRequest;

import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final DriverDAO driverDAO = new DriverDAO();

    public Driver getDriverByUserId(int userId) throws SQLException {
        return driverDAO.findDriverByUserId(userId);
    }

    public Driver getAllPendingRides(int userId) throws SQLException {
        return driverDAO.findDriverByUserId(userId);
    }

    public void updateDriverProfile(Driver driver) throws SQLException {
        driverDAO.updateDriver(driver);
    }

    public List<Assignment> getDriverAssignments(int driverId) throws SQLException {
        return driverDAO.getAssignmentsByDriverId(driverId);
    }

    /*public List<RideRequest> getDriverRideRequests(int driverId) throws SQLException {
        return driverDAO.getRideRequestsByDriverId(driverId);
    }*/

    public void createDriver(Driver driver) throws SQLException {
        DriverDAO dao = new DriverDAO();
        dao.createDriver(driver); // insert into drivers table
    }

    public List<com.example.drivermanagement.model.RideRequest> getPendingRideRequests() throws SQLException {
        return driverDAO.getPendingRideRequests();
    }

    public List<RideRequest> getDriverRideRequests(int driverId) throws SQLException {
        System.out.println("🔍 STEP 3: === DRIVER SERVICE STARTED === driverId=" + driverId);
        long startTime = System.currentTimeMillis();

        List<RideRequest> rides = driverDAO.getRideRequestsByDriverId(driverId);

        long endTime = System.currentTimeMillis();
        System.out.println("✅ STEP 5: === SERVICE FINISHED === Found " + rides.size() + " rides in " + (endTime - startTime) + "ms");
        return rides;
    }
    public void acceptRide(int rideId, int driverId, int userId) throws SQLException {
        driverDAO.acceptRideRequest(rideId, driverId, userId);
    }



}