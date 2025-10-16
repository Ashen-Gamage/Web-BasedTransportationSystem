package com.example.ridebooking.servlet;

import com.example.ridebooking.dao.RideBookingDAO;
import com.example.ridebooking.model.RideRequest;
import com.example.ridebooking.service.RideBookingService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.ridebooking.dao.RideBookingDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/bookRide")
public class BookRideServlet extends HttpServlet {
    private final RideBookingService service = new RideBookingService();
    RideBookingDAO dao = new RideBookingDAO();
    // ✅ Load Book Ride page + show pending rides
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {

            // ✅ Fetch pending rides before showing page
            List<RideRequest> pendingRides = service.getPendingRidesByUser(user.getId());
            request.setAttribute("pendingRides", pendingRides);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to load pending rides.");
        }

        request.getRequestDispatcher("/jsp/ridebooking/bookride.jsp").forward(request, response);
        request.getRequestDispatcher("/jsp/drivermanagement/rideRequest.jsp").forward(request, response);
    }

    // ✅ Handle ride booking form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        RideRequest ride = new RideRequest();
        ride.setUserId(user.getId());
        ride.setPickupLocation(request.getParameter("pickup"));
        ride.setDropoffLocation(request.getParameter("dropoff"));
        ride.setStatus("pending");

        try {
            service.bookRide(ride);
            // ✅ After booking, redirect to show updated rides
            response.sendRedirect(request.getContextPath() + "/bookRide");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to book ride: " + e.getMessage());
            doGet(request, response); // show page again with error
        }
    }

}
