package com.example.drivermanagement.dao;

import com.example.common.utils.DBUtil;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;
import com.example.drivermanagement.model.RideRequest;

import java.sql.*;
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
                    assignment.setAssignmentId(rs.getInt("id"));
                    assignment.setRideId(rs.getInt("ride_id")); // make sure the column alias is 'ride_id'
                    assignment.setDriverId(rs.getInt("driver_id"));
                    assignment.setAssignedTime(Timestamp.valueOf(rs.getTimestamp("assigned_time").toLocalDateTime()));
                    assignments.add(assignment);
                }
            }
        }
        return assignments;
    }

    public List<RideRequest> getRideRequestsByDriverId(int driverId) throws SQLException {
        List<RideRequest> requests = new ArrayList<>();
        String sql = """
        SELECT rr.*, u.username
        FROM ride_requests rr
        JOIN users u ON rr.user_id = u.id
        WHERE rr.status='pending'
        ORDER BY rr.request_time DESC
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RideRequest request = new RideRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setUsername(rs.getString("username"));
                request.setPickupLocation(rs.getString("pickup_location"));
                request.setDropoffLocation(rs.getString("dropoff_location"));
                request.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        }
        return requests;
    }


    public void createDriver(Driver driver) throws SQLException {
        String sql = "INSERT INTO drivers (user_id, license_number, vehicle_type, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, driver.getUserId());
            ps.setString(2, driver.getLicenseNumber());
            ps.setString(3, driver.getVehicleType());
            ps.setString(4, driver.getStatus());
            ps.executeUpdate();
        }
    }

    public List<RideRequest> getPendingRideRequests() throws SQLException {
        List<RideRequest> requests = new ArrayList<>();
        String sql = """
            SELECT rr.*, u.username 
            FROM ride_requests rr 
            JOIN users u ON rr.user_id = u.id 
            WHERE rr.status = 'pending'
            ORDER BY rr.request_time DESC
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RideRequest request = new RideRequest();
                request.setId(rs.getInt("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setUsername(rs.getString("username"));
                request.setPickupLocation(rs.getString("pickup_location"));
                request.setDropoffLocation(rs.getString("dropoff_location"));
                request.setRequestTime(rs.getTimestamp("request_time").toLocalDateTime());
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        }
        return requests;
    }
    public int getDriverUserId(int driverId) throws SQLException {
        String sql = "SELECT user_id FROM drivers WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id"); // this is the users.id
                } else {
                    throw new SQLException("Driver not found with id " + driverId);
                }
            }
        }
    }

    public void acceptRideRequest(int rideId, int driverId, int riderId) throws SQLException {
        // SQL statements
        String updateRideSql = "UPDATE ride_requests SET status = 'accepted' WHERE id = ?";
        String insertAssignmentSql = "INSERT INTO assignments (ride_id, rider_id, driver_id, assigned_time) VALUES (?, ?, ?, GETDATE())";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // start transaction
            try (
                    PreparedStatement updateStmt = conn.prepareStatement(updateRideSql);
                    PreparedStatement insertStmt = conn.prepareStatement(insertAssignmentSql)
            ) {
                // 1️⃣ Update ride status
                updateStmt.setInt(1, rideId);
                updateStmt.executeUpdate();

                // 2️⃣ Get the driver's user_id (needed for assignments.driver_id)
                int driverUserId = getDriverUserId(driverId); // <-- use your existing getDriverUserId method

                // 3️⃣ Insert into assignments
                insertStmt.setInt(1, rideId);         // ride_id
                insertStmt.setInt(2, riderId);        // rider_id
                insertStmt.setInt(3, driverUserId);   // driver_id (must be users.id)
                insertStmt.executeUpdate();

                conn.commit(); // commit transaction
            } catch (SQLException e) {
                conn.rollback(); // rollback on error
                throw e;
            } finally {
                conn.setAutoCommit(true); // reset auto-commit
            }
        }
    }
    // this methord for driver to delete ride if user change the d
// Inside DriverDAO.java
    public List<Assignment> getFullAssignmentDetails() throws SQLException {
        System.out.println("📡 [DAO] Fetching full assignment details from database...");

        List<Assignment> list = new ArrayList<>();

        String sql = """
        SELECT 
            a.id AS assignment_id,
            rr.id AS ride_id,
            rr.pickup_location,
            rr.dropoff_location,
            rr.status AS ride_status,
            a.assigned_time,
            
            rider.id AS rider_id,
            rider.username AS rider_name,
            rider.phone AS rider_phone,
            
            driverU.id AS driver_user_id,
            driver.id AS driver_id,
            driverU.username AS driver_name,
            driverU.phone AS driver_phone,
            driver.license_number,
            driver.vehicle_type,
            driver.status AS driver_status

        FROM assignments a
        JOIN ride_requests rr ON a.ride_id = rr.id
        JOIN users rider ON a.rider_id = rider.id
        JOIN drivers driver ON a.driver_id = driver.user_id
        JOIN users driverU ON driver.user_id = driverU.id
        ORDER BY a.assigned_time DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("🟡 [DAO] SQL executed successfully. Reading results...");

            while (rs.next()) {
                Assignment a = new Assignment();
                a.setAssignmentId(rs.getInt("assignment_id"));
                a.setRideId(rs.getInt("ride_id"));
                a.setPickupLocation(rs.getString("pickup_location"));
                a.setDropoffLocation(rs.getString("dropoff_location"));
                a.setRideStatus(rs.getString("ride_status"));
                a.setAssignedTime(rs.getTimestamp("assigned_time"));

                a.setRiderId(rs.getInt("rider_id"));
                a.setRiderName(rs.getString("rider_name"));
                a.setRiderPhone(rs.getString("rider_phone"));

                a.setDriverId(rs.getInt("driver_id"));
                a.setDriverName(rs.getString("driver_name"));
                a.setDriverPhone(rs.getString("driver_phone"));
                a.setLicenseNumber(rs.getString("license_number"));
                a.setVehicleType(rs.getString("vehicle_type"));
                a.setDriverStatus(rs.getString("driver_status"));

                list.add(a);
            }

            System.out.println("✅ [DAO] Data fetch complete. Total rows found: " + list.size());

        } catch (SQLException e) {
            System.out.println("❌ [DAO ERROR] SQL Exception during getFullAssignmentDetails(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public boolean updateRideStatus(int rideId, String newStatus) {
        String sql = "UPDATE ride_requests SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, rideId);
            int rows = stmt.executeUpdate();

            System.out.println("✅ [DAO] Ride status updated for Ride ID " + rideId + ", rows affected: " + rows);
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteRideAndAssignment(int rideId) {
        String deleteAssignmentSQL = "DELETE FROM assignments WHERE ride_id = ?";
        String deleteRideSQL = "DELETE FROM ride_requests WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Delete from assignments first
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteAssignmentSQL)) {
                stmt1.setInt(1, rideId);
                stmt1.executeUpdate();
            }

            // Delete from ride_requests table
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteRideSQL)) {
                stmt2.setInt(1, rideId);
                int rows = stmt2.executeUpdate();

                conn.commit(); // Commit both
                System.out.println("✅ [DAO] Deleted ride_request and assignment for Ride ID " + rideId);
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("⚠️ [DAO] Transaction rollback for Ride ID " + rideId);
            } catch (Exception ignored) {}
            return false;
        }
    }



}
