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
            // Fetch fresh user data from database to ensure latest values
            User freshUser = userService.findUserByEmail(user.getEmail());
            if (freshUser == null) {
                request.setAttribute("error", "User not found in database.");
                response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
                return;
            }

            // Fetch driver info from database every time
            Driver driver = driverService.getDriverByUserId(freshUser.getId());
            if (driver == null) {
                driver = new Driver(); // Initialize empty driver to avoid null pointer
                driver.setUserId(freshUser.getId());
            }

            // Update session with fresh user data
            session.setAttribute("user", freshUser);
            request.setAttribute("user", freshUser);
            request.setAttribute("driver", driver);

            // Debugging: Log fetched data
            System.out.println("Fetched User: " + freshUser);
            System.out.println("Fetched Driver: " + driver);

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
            user.setAddressLine(request.getParameter("address_line"));
            user.setCity(request.getParameter("city"));
            user.setPostalCode(request.getParameter("postal_code"));
            userService.updateUserProfile(user);

            // Fetch fresh user data after update
            User freshUser = userService.findUserByEmail(user.getEmail());
            if (freshUser == null) {
                request.setAttribute("error", "User not found after update.");
                response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
                return;
            }

            // Update or insert driver details
            String licenseNumber = request.getParameter("license_number");
            String vehicleType = request.getParameter("vehicle_type");
            Driver driver = driverService.getDriverByUserId(freshUser.getId());

            if (driver == null) {
                driver = new Driver();
                driver.setUserId(freshUser.getId());
                driver.setLicenseNumber(licenseNumber);
                driver.setVehicleType(vehicleType);
                driver.setStatus("available");
                driverService.createDriver(driver);
            } else {
                driver.setLicenseNumber(licenseNumber);
                driver.setVehicleType(vehicleType);
                driverService.updateDriverProfile(driver);
            }

            // Fetch the latest driver data after update
            driver = driverService.getDriverByUserId(freshUser.getId());

            // Update session and request attributes
            session.setAttribute("user", freshUser);
            request.setAttribute("user", freshUser);
            request.setAttribute("driver", driver);
            request.setAttribute("message", "Profile updated successfully");

            // Debugging: Log updated data
            System.out.println("Updated User: " + freshUser);
            System.out.println("Updated Driver: " + driver);

        } catch (SQLException e) {
            request.setAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/drivermanagement/profile.jsp").forward(request, response);
    }
}