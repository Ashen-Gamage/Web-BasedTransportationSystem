package com.example.drivermanagement.servlet;

import com.example.drivermanagement.dao.DriverDAO;
import com.example.drivermanagement.model.Driver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/activeDrivers")
public class ActiveDriversServlet extends HttpServlet {
    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Driver> activeDrivers = driverDAO.findActiveDrivers();
            request.setAttribute("drivers", activeDrivers);
            request.getRequestDispatcher("/jsp/drivermanagement/activeDrivers.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load active drivers: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
