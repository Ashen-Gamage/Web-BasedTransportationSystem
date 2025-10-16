package com.example.drivermanagement.service;

import com.example.drivermanagement.dao.DriverDAO;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;
import com.example.ridebooking.model.RideRequest;

import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final DriverDAO driverDAO = new DriverDAO();

    public Driver getDriverByUserId(int userId) throws SQLException {
        return driverDAO.findDriverByUserId(userId);
    }

    public void updateDriverProfile(Driver driver) throws SQLException {
        driverDAO.updateDriver(driver);
    }

    public List<Assignment> getDriverAssignments(int driverId) throws SQLException {
        return driverDAO.getAssignmentsByDriverId(driverId);
    }

    public List<RideRequest> getDriverRideRequests(int driverId) throws SQLException {
        return driverDAO.getRideRequestsByDriverId(driverId);
    }
}