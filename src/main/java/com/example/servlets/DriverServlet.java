package com.example.servlets;

import com.example.dao.DriverDAO;
import com.example.model.Ride;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DriverServlet", urlPatterns = {"/driver"})
public class DriverServlet extends HttpServlet {

    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        Integer userIdObj = (Integer) session.getAttribute("userId");
        if (role == null || !"DRIVER".equalsIgnoreCase(role) || userIdObj == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        int driverId = userIdObj;

        String action = req.getParameter("action");
        if (action == null || action.isBlank()) action = "dashboard";

        try {
            switch (action) {
                case "acceptRide": {
                    int rideId = Integer.parseInt(req.getParameter("rideId"));
                    driverDAO.acceptRide(rideId, driverId);
                    req.getSession().setAttribute("flash", "Ride accepted successfully.");
                    resp.sendRedirect(req.getContextPath() + "/driver?action=dashboard");
                    return;
                }
                case "updateStatus": {
                    int rideId = Integer.parseInt(req.getParameter("rideId"));
                    String status = req.getParameter("status"); // ON_THE_WAY | IN_RIDE | COMPLETED
                    driverDAO.updateRideStatus(rideId, status);
                    req.getSession().setAttribute("flash", "Ride status updated to " + status + ".");
                    resp.sendRedirect(req.getContextPath() + "/driver?action=dashboard");
                    return;
                }
                case "dashboard":
                default: {
                    List<Ride> assignedRides = driverDAO.getAssignedRides(driverId);
                    req.setAttribute("assignedRides", assignedRides);

                    // Flash message
                    Object flash = session.getAttribute("flash");
                    if (flash != null) {
                        req.setAttribute("flash", flash);
                        session.removeAttribute("flash");
                    }

                    req.getRequestDispatcher("/WEB-INF/views/driverDashboard.jsp").forward(req, resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Server error. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/driverDashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp); // Handle POST forms
    }
}
