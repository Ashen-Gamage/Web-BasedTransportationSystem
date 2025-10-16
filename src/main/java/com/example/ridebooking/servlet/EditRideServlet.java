package com.example.ridebooking.servlet;

import com.example.ridebooking.model.RideRequest;
import com.example.ridebooking.service.RideBookingService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/editRide")
public class EditRideServlet extends HttpServlet {
    private final RideBookingService service = new RideBookingService();
    private final DateTimeFormatter[] formatters = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,               // 2025-10-13T14:30
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),    // 2025-10-13 14:30
            DateTimeFormatter.ofPattern("yyyy-MM-dd")           // 2025-10-13 (will be treated as midnight)
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/bookRide");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            RideRequest ride = service.getRideById(id);
            if (ride == null || ride.getUserId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/bookRide");
                return;
            }
            request.setAttribute("ride", ride);
            request.getRequestDispatcher("/jsp/ridebooking/editride.jsp").forward(request, response);
        } catch (NumberFormatException | SQLException e) {
            request.setAttribute("error", "Unable to load ride: " + e.getMessage());
            request.getRequestDispatcher("/jsp/ridebooking/bookride.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            // fetch existing ride to validate ownership and keep other fields
            RideRequest ride = service.getRideById(id);
            if (ride == null || ride.getUserId() != user.getId()) {
                request.setAttribute("error", "Ride not found or not owned by you.");
                request.getRequestDispatcher("/jsp/ridebooking/bookride.jsp").forward(request, response);
                return;
            }

            String pickup = request.getParameter("pickup");
            String dropoff = request.getParameter("dropoff");
            String requestTimeParam = request.getParameter("request_time");

            if (pickup != null) ride.setPickupLocation(pickup.trim());
            if (dropoff != null) ride.setDropoffLocation(dropoff.trim());

            if (requestTimeParam != null && !requestTimeParam.trim().isEmpty()) {
                LocalDateTime parsed = tryParseLocalDateTime(requestTimeParam.trim());
                if (parsed != null) {
                    ride.setRequestTime(parsed);
                }
            }

            service.updateRide(ride);
            response.sendRedirect(request.getContextPath() + "/bookRide");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ride id.");
            request.getRequestDispatcher("/jsp/ridebooking/editride.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to update ride: " + e.getMessage());
            request.getRequestDispatcher("/jsp/ridebooking/editride.jsp").forward(request, response);
        }
    }

    private LocalDateTime tryParseLocalDateTime(String input) {
        for (DateTimeFormatter f : formatters) {
            try {
                return LocalDateTime.parse(input, f);
            } catch (DateTimeException ignored) {}
        }
        // fallback: if input contains 'T' or space attempt ISO_LOCAL_DATE_TIME parsing again
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeException e) {
            return null;
        }
    }
}