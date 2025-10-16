package com.example.admin.servlet;

import com.example.admin.dao.AdminDAO;
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
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Driver> activeDrivers = adminDAO.findActiveDrivers();
            request.setAttribute("drivers", activeDrivers);
            request.getRequestDispatcher("/jsp/drivermanagement/activeDrivers.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load active drivers: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
}
