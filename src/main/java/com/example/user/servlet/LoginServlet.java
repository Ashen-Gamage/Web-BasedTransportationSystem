package com.example.user.servlet;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService(); // Service to handle user-related DB operations
    private final DriverService driverService = new DriverService(); // Service to handle driver-related DB operations
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get email and password from login form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Authenticate user
            User user = userService.loginUser(email, password);

            if (user != null) {
                // User exists, create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);  // Store user object in session

                // Redirect based on role
                if ("driver".equalsIgnoreCase(user.getRole())) {
                    Driver driver = driverService.getDriverByUserId(user.getId());
                    session.setAttribute("driver", driver);
                    // If role is driver, go to driver dashboard
                    request.getRequestDispatcher("/jsp/drivermanagement/driverDashboard.jsp").forward(request, response);
                } else {
                    // Otherwise, go to user dashboard (rider or admin)
                    request.getRequestDispatcher("/jsp/user/userDashboard.jsp").forward(request, response);
                }
            } else {
                // Invalid credentials
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // Handle DB exceptions
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Show login page if GET request
        request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
    }
}
