package com.example.drivermanagement.servlet;

import com.example.drivermanagement.model.Driver;
import com.example.drivermanagement.model.Assignment;
import com.example.drivermanagement.service.DriverService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/allAssignments")
public class DriverAssignmentsServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🚀 [Servlet] /allAssignments endpoint hit — Starting process...");
        try {
            System.out.println("🟡 [Servlet] Calling service layer -> getAllAssignments()");
            List<Assignment> assignments = driverService.getAllAssignments();

            if (assignments == null) {
                System.out.println("❌ [Servlet] assignments is NULL");
            } else {
                System.out.println("✅ [Servlet] assignments received. Total: " + assignments.size());
            }

            request.setAttribute("assignments", assignments);
            request.getRequestDispatcher("/jsp/drivermanagement/allAssignments.jsp")
                    .forward(request, response);
            System.out.println("📦 [Servlet] Forwarded to JSP successfully.");

        } catch (SQLException e) {
            System.out.println("❌ [Servlet] SQL Exception: " + e.getMessage());
            request.setAttribute("error", "Failed to load assignments: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/allAssignments.jsp")
                    .forward(request, response);
        }
    }

}


