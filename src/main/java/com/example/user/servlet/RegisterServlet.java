package com.example.user.servlet;

import com.example.user.model.User;
import com.example.user.service.UserService;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final DriverService driverService = new DriverService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String licenseNumber = request.getParameter("license_number");
        String vehicleType = request.getParameter("vehicle_type");

        // Basic validation
        if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "Username, email, password and role are required.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        // Validate driver-specific fields
        if ("driver".equalsIgnoreCase(role)) {
            if (licenseNumber == null || licenseNumber.trim().isEmpty() ||
                    vehicleType == null || vehicleType.trim().isEmpty()) {
                request.setAttribute("error", "License number and vehicle type are required for drivers.");
                request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
                return;
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        // IMPORTANT: hash the password before saving; do it in service/DAO ideally.
        user.setPassword(password);
        user.setRole(role);

        try {
            int userId = -1;
            // Preferred: if driver, attempt a single transactional method that creates both user + driver
            if ("driver".equalsIgnoreCase(role)) {
                try {
                    // This method should create user and driver in one transaction and return generated user id
                    userId = userService.registerUserWithDriver(user, licenseNumber, vehicleType);
                } catch (UnsupportedOperationException | NoSuchMethodError ex) {
                    // Service doesn't provide atomic method — fall back to two-step approach
                    // (You can catch a more specific exception if your service indicates "not implemented")
                    userId = userService.registerUser(user);
                    Driver driver = new Driver();
                    driver.setUserId(userId);
                    driver.setLicenseNumber(licenseNumber);
                    driver.setVehicleType(vehicleType);
                    driver.setStatus("available");
                    driverService.createDriver(driver);
                }
            } else {
                // Non-driver: simple user registration
                userId = userService.registerUser(user);
            }

            // Fetch the complete user from DB (so session has all DB-populated fields)
            User registeredUser = userService.findUserByEmail(email);
            if (registeredUser == null) {
                // Unexpected: created id but couldn't load user
                request.setAttribute("error", "Registration succeeded but failed to load user profile. Please log in.");
                request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
                return;
            }

            // Create session and store the user object
            HttpSession session = request.getSession();
            session.setAttribute("user", registeredUser);

            // Optionally set message in session (since we are redirecting)
            session.setAttribute("success", "Registration successful! Welcome, " + registeredUser.getUsername());

            // Redirect to an appropriate page
            if ("driver".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/jsp/drivermanagement/driverDashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            }

        } catch (SQLException e) {
            // Log the exception (use a logger in real apps)
            e.printStackTrace();
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
        } catch (Exception e) {
            // Catch other unexpected exceptions
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
    }
}