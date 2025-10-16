package com.example.drivermanagement.servlet;

import com.example.drivermanagement.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/acceptRide")
public class AcceptRideServlet extends HttpServlet {

    private final DriverService driverService = new DriverService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🚗 === AcceptRideServlet STARTED ===");

        String rideIdStr = request.getParameter("rideId");
        String driverIdStr = request.getParameter("driverId");
        String userIdStr = request.getParameter("userId"); // NEW: get rider ID


        System.out.println("🔍 rideId param = " + rideIdStr);
        System.out.println("🔍 driverId param = " + driverIdStr);
        System.out.println("🔍 userId param = " + userIdStr);


        if (rideIdStr == null || driverIdStr == null) {
            System.out.println("❌ ERROR: Missing rideId or driverId parameter!");
            response.sendRedirect(request.getContextPath() + "/driverRideRequests");
            return;
        }

        try {
            int rideId = Integer.parseInt(rideIdStr);
            int driverId = Integer.parseInt(driverIdStr);
            int userId = Integer.parseInt(userIdStr); // NEW: parse rider ID


            System.out.println("✅ Parsed rideId=" + rideId + ", driverId=" + driverId);

            // Pass all three parameters to service layer
            driverService.acceptRide(rideId, driverId, userId);

            System.out.println("✅ Ride accepted successfully!");
            response.sendRedirect(request.getContextPath() + "/driverRideRequests");

        } catch (NumberFormatException e) {
            System.out.println("❌ ERROR: Invalid numeric format -> " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/driverRideRequests");
        } catch (SQLException e) {
            System.out.println("❌ ERROR: Database issue -> " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to accept ride: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
