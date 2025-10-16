/*
package com.example.drivermanagement.servlet;

import com.example.admin.dao.AdminDAO;
import com.example.drivermanagement.model.Driver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addDriver")
public class AddDriverServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String licenseNumber = request.getParameter("licenseNumber");
            String vehicleType = request.getParameter("vehicleType");
            String status = request.getParameter("status");

            Driver driver = new Driver();
            driver.setUserId(userId);
            driver.setLicenseNumber(licenseNumber);
            driver.setVehicleType(vehicleType);
            driver.setStatus(status);

            adminDAO.saveDriver(driver);

            // Redirect to manage page
            response.sendRedirect(request.getContextPath() + "/jsp/drivermanagement/manage.jsp");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to add driver: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/addDriver.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/drivermanagement/addDriver.jsp").forward(request, response);
    }
}
*/
