package com.example.admin.servlet;

import com.example.admin.dao.AdminDAO;
import com.example.drivermanagement.model.Assignment;
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

@WebServlet("/assignDriver")
public class AssignDriverServlet extends HttpServlet {
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            List<Driver> availableDrivers = adminDAO.findAvailableDrivers();
            request.setAttribute("drivers", availableDrivers);
            request.setAttribute("rideId", request.getParameter("rideId"));
            request.getRequestDispatcher("/jsp/drivermanagement/assign.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load drivers: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        int rideId = Integer.parseInt(request.getParameter("rideId"));
        int driverId = Integer.parseInt(request.getParameter("driverId"));

        Assignment assignment = new Assignment();
        assignment.setRideRequestId(rideId);
        assignment.setDriverId(driverId);

        try {
            adminDAO.assignDriver(assignment);
            adminDAO.updateDriverStatus(driverId, "busy");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to assign driver: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/assign.jsp").forward(request, response);
        }
    }
}