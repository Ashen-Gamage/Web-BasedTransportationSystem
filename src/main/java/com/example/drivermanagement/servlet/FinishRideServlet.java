package com.example.drivermanagement.servlet;

import com.example.drivermanagement.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/finishRide")
public class FinishRideServlet extends HttpServlet {
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
            boolean success = assignmentService.markRideAsCompleted(rideId);

            if (success) {
                System.out.println("✅ [Servlet] Ride ID " + rideId + " marked as completed.");
            } else {
                System.out.println("⚠️ [Servlet] Could not update status for Ride ID " + rideId);
            }

            // Redirect back to allAssignments page
            response.sendRedirect(request.getContextPath() + "/allAssignments");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating ride status: " + e.getMessage());
            request.getRequestDispatcher("/jsp/drivermanagement/allAssignments.jsp").forward(request, response);
        }
    }
}
