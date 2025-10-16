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

@WebServlet("/manageDriver")
public class ManageDriverServlet extends HttpServlet {
    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            Driver driver = driverDAO.findDriverByUserId(user.getId());
            if (driver == null) {
                driver = new Driver();
                driver.setUserId(user.getId());
            }
            request.setAttribute("driver", driver);
            request.getRequestDispatcher("/jsp/drivermanagement/manage.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to load driver: " + e.getMessage());
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

        Driver driver = new Driver();
        driver.setUserId(user.getId());
        driver.setLicenseNumber(request.getParameter("licenseNumber"));
        driver.setVehicleType(request.getParameter("vehicleType"));
        driver.setStatus(request.getParameter("status"));

        try {
            Driver existingDriver = driverDAO.findDriverByUserId(user.getId());
            if (existingDriver == null) {
                driverDAO.saveDriver(driver);
            } else {
                driverDAO.updateDriverStatus(driver.getId(), driver.getStatus());
            }
            request.setAttribute("message", "Driver profile updated successfully");
            request.setAttribute("driver", driver);
            request.getRequestDispatcher("/jsp/drivermanagement/manage.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to update driver: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/manage.jsp").forward(request, response);
        }
    }
}