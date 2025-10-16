package com.example.drivermanagement.servlet;

import com.example.drivermanagement.dao.DriverDAO;
import com.example.drivermanagement.model.Driver;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Only admin can access
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            // Fetch drivers by status
            List<Driver> activeDrivers = driverDAO.findDriversByStatus("Active");
            List<Driver> inactiveDrivers = driverDAO.findDriversByStatus("Inactive");
            List<Driver> busyDrivers = driverDAO.findDriversByStatus("busy"); // Match your DB values

            // Set attributes for JSP
            request.setAttribute("activeDrivers", activeDrivers);
            request.setAttribute("inactiveDrivers", inactiveDrivers);
            request.setAttribute("busyDrivers", busyDrivers);

            // Forward to admin dashboard JSP
            request.getRequestDispatcher("/jsp/drivermanagement/adminDashboard.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load drivers: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
