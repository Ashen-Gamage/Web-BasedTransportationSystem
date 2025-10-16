package com.example.drivermanagement.servlet;

import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.service.DriverService;
import com.example.ridebooking.model.RideRequest;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"driver".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            Driver driver = driverService.getDriverByUserId(user.getId());
            List<RideRequest> rideRequests = driverService.getDriverRideRequests(driver.getId());
            request.setAttribute("rideRequests", rideRequests);
            request.getRequestDispatcher("/jsp/drivermanagement/riderequests.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load ride requests: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/riderequests.jsp").forward(request, response);
        }
    }
}