package com.example.drivermanagement.servlet;

import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.service.DriverService;
import com.example.user.model.User;
import com.example.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/driverProfile")
public class DriverProfileServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final UserService userService = new UserService();

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
            request.setAttribute("driver", driver);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/jsp/drivermanagement/profile.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load profile: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"driver".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            // Update user details
            user.setUsername(request.getParameter("username"));
            user.setPhone(request.getParameter("phone"));
            userService.updateUserProfile(user);

            // Update driver details
            Driver driver = driverService.getDriverByUserId(user.getId());
            driver.setLicenseNumber(request.getParameter("license_number"));
            driver.setVehicleType(request.getParameter("vehicle_type"));
            driverService.updateDriverProfile(driver);

            session.setAttribute("user", user); // Update session
            request.setAttribute("driver", driver);
            request.setAttribute("user", user);
            request.setAttribute("message", "Profile updated successfully");
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        request.getRequestDispatcher("/jsp/drivermanagement/profile.jsp").forward(request, response);
    }
}