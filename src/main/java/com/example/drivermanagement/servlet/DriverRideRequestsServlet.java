package com.example.drivermanagement.servlet;

import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.RideRequest;
import com.example.drivermanagement.service.DriverService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/driverRideRequests")
public class DriverRideRequestsServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        System.out.println("🔍 STEP 1: === DRIVER RIDE REQUESTS SERVLET STARTED ===");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Driver driver = (Driver) session.getAttribute("driver");

        System.out.println("🔍 STEP 1: User=" + (user != null ? user.getUsername() + "(" + user.getRole() + ")" : "NULL"));
        System.out.println("🔍 STEP 1: Driver=" + (driver != null ? driver.getId() : "NULL"));

        if (user == null || !"driver".equals(user.getRole())) {
            System.out.println("❌ STEP 1: REDIRECTING TO LOGIN (Not driver)");
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }
        System.out.println("✅ STEP 1: Driver authenticated! Proceeding...");

        try {
            System.out.println("🔍 STEP 2: Calling DriverService.getDriverRideRequests(" + driver.getId() + ")");
            long startTime = System.currentTimeMillis();

            List<RideRequest> pendingRides = driverService.getDriverRideRequests(driver.getId());

            long endTime = System.currentTimeMillis();
            System.out.println("⏱️  STEP 2: Service call took " + (endTime - startTime) + "ms");

            System.out.println("✅ STEP 6: Setting 'pendingRides' attribute. Size=" + pendingRides.size());
            request.setAttribute("pendingRides", pendingRides);

        } catch (SQLException e) {
            System.out.println("❌ STEP 2: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Unable to load pending rides.");
        }

        System.out.println("🔍 STEP 7: Forwarding to bookride.jsp");
        request.getRequestDispatcher("/jsp/drivermanagement/rideRequest.jsp").forward(request, response);
        System.out.println("✅ COMPLETE: === SERVLET FLOW FINISHED SUCCESSFULLY ===");

    }
}
