package com.example.drivermanagement.servlet;

import com.example.drivermanagement.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteRideDriver")
public class DeleteRideServlet extends HttpServlet {
    private DriverService assignmentService;

    @Override
    public void init() {
        assignmentService = new DriverService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rideIdStr = request.getParameter("rideId");

        if (rideIdStr == null || rideIdStr.isEmpty()) {
            request.setAttribute("error", "Invalid Ride ID");
            request.getRequestDispatcher("/jsp/drivermanagement/allAssignments.jsp").forward(request, response);
            return;
        }

        try {
            int rideId = Integer.parseInt(rideIdStr);
            boolean success = assignmentService.deleteRideAndAssignment(rideId);

            if (success) {
                System.out.println("🗑️ [Servlet] Ride ID " + rideId + " deleted successfully.");
            } else {
                System.out.println("⚠️ [Servlet] Could not delete Ride ID " + rideId);
            }

            // Redirect back to assignments list
            response.sendRedirect(request.getContextPath() + "/allAssignments");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error deleting ride: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/allAssignments.jsp").forward(request, response);
        }
    }
}
