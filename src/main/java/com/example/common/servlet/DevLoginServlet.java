package com.example.common.servlet;

import com.example.user.model.User;
import com.example.user.service.UserService;
import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/devLogin")
public class DevLoginServlet extends HttpServlet {

    private boolean isDevMode() {
        String v = System.getenv("APP_DEV_MODE");
        if (v == null) v = System.getProperty("app.dev.mode", "true");
        return "true".equalsIgnoreCase(v);
    }

    private final UserService userService = new UserService();
    private final DriverService driverService = new DriverService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isDevMode()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            // 1) Find or create dev user
            String devEmail = "dev.driver@example.com";
            User user = userService.findUserByEmail(devEmail);
            if (user == null) {
                user = new User();
                user.setUsername("DevDriver");
                user.setEmail(devEmail);
                user.setPassword("devpass"); // simple placeholder, hashed if needed
                user.setRole("driver");
                userService.createUser(user);
                user = userService.findUserByEmail(devEmail);
            }

            // 2) Find or create dev driver
            Driver driver = driverService.getDriverByUserId(user.getId());
            if (driver == null) {
                driver = new Driver();
                driver.setUserId(user.getId());
                driver.setLicenseNumber("DEV-1234");
                driver.setVehicleType("Car");
                driver.setStatus("available");
                driverService.createDriver(driver);
                driver = driverService.getDriverByUserId(user.getId());
            }

            // 3) Put user and driver in session
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("driver", driver);

            // 4) Redirect to profile or dashboard
            resp.sendRedirect(req.getContextPath() + "/jsp/drivermanagement/profile.jsp");
        } catch (SQLException e) {
            throw new ServletException("Dev login failed", e);
        }
    }
}
