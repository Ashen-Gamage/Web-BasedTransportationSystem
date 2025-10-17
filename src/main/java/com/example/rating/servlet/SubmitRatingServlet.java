package com.example.rating.servlet;

import com.example.common.utils.DBUtil;
import com.example.rating.model.Rating;
import com.example.rating.service.RatingService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/submitRating")
public class SubmitRatingServlet extends HttpServlet {
    private final RatingService ratingService = new RatingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SubmitRatingServlet.doGet: Start");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"rider".equals(user.getRole())) {
            System.out.println("SubmitRatingServlet.doGet: User not logged in or not a rider, redirecting");
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String rideIdStr = request.getParameter("rideId");
        if (rideIdStr == null) {
            System.out.println("SubmitRatingServlet.doGet: rideId parameter missing, redirecting to completedRides");
            response.sendRedirect(request.getContextPath() + "/completedRides");
            return;
        }

        int rideId = Integer.parseInt(rideIdStr);
        System.out.println("SubmitRatingServlet.doGet: rideId = " + rideId);

        try {
            if (!isRideCompleted(rideId)) {
                System.out.println("SubmitRatingServlet.doGet: Ride not completed, redirecting to completedRides");
                response.sendRedirect(request.getContextPath() + "/completedRides");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error checking ride completion", e);
        }

        request.setAttribute("rideId", rideId);
        request.getRequestDispatcher("/jsp/rating/rating.jsp").forward(request, response);
        System.out.println("SubmitRatingServlet.doGet: Forwarded to rating.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SubmitRatingServlet.doPost: Start");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"rider".equals(user.getRole())) {
            System.out.println("SubmitRatingServlet.doPost: User not logged in or not a rider, redirecting");
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        int rideRequestId = Integer.parseInt(request.getParameter("rideId"));
        int ratingValue = Integer.parseInt(request.getParameter("rating"));
        String feedback = request.getParameter("feedback");

        System.out.println("SubmitRatingServlet.doPost: rideRequestId = " + rideRequestId + ", rating = " + ratingValue);

        try {
            if (!isRideCompleted(rideRequestId)) {
                System.out.println("SubmitRatingServlet.doPost: Ride not completed");
                request.setAttribute("error", "You can only rate completed rides.");
                request.getRequestDispatcher("/jsp/rating/rating.jsp").forward(request, response);
                return;
            }

            if (hasAlreadyRated(rideRequestId, user.getId())) {
                System.out.println("SubmitRatingServlet.doPost: Ride already rated by this user");
                request.setAttribute("error", "You have already rated this ride.");
                request.getRequestDispatcher("/jsp/rating/rating.jsp").forward(request, response);
                return;
            }

            int driverId = getDriverIdForRide(rideRequestId);
            System.out.println("SubmitRatingServlet.doPost: driverId = " + driverId);

            Rating rating = new Rating();
            rating.setRideRequestId(rideRequestId);
            rating.setUserId(user.getId());
            rating.setDriverId(driverId);
            rating.setRating(ratingValue);
            rating.setFeedback(feedback);

            ratingService.addRating(rating);
            System.out.println("SubmitRatingServlet.doPost: Rating added successfully");

            response.sendRedirect(request.getContextPath() + "/viewFeedback?driverId=" + driverId);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to submit rating: " + e.getMessage());
            request.getRequestDispatcher("/jsp/rating/rating.jsp").forward(request, response);
        }
    }

    private boolean isRideCompleted(int rideId) throws SQLException {
        System.out.println("SubmitRatingServlet.isRideCompleted: Checking ride ID " + rideId);
        String sql = "SELECT status FROM ride_requests WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean completed = "completed".equalsIgnoreCase(rs.getString("status"));
                    System.out.println("SubmitRatingServlet.isRideCompleted: status = " + rs.getString("status") + ", completed = " + completed);
                    return completed;
                }
            }
        }
        return false;
    }

    private boolean hasAlreadyRated(int rideId, int userId) throws SQLException {
        System.out.println("SubmitRatingServlet.hasAlreadyRated: rideId = " + rideId + ", userId = " + userId);
        String sql = "SELECT COUNT(*) as cnt FROM ratings WHERE ride_request_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean alreadyRated = rs.getInt("cnt") > 0;
                    System.out.println("SubmitRatingServlet.hasAlreadyRated: alreadyRated = " + alreadyRated);
                    return alreadyRated;
                }
            }
        }
        return false;
    }

    private int getDriverIdForRide(int rideId) throws SQLException {
        System.out.println("SubmitRatingServlet.getDriverIdForRide: rideId = " + rideId);
        String sql = "SELECT d.id AS driver_id FROM assignments a JOIN drivers d ON a.driver_id = d.user_id WHERE a.ride_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int driverId = rs.getInt("driver_id");
                    System.out.println("SubmitRatingServlet.getDriverIdForRide: driverId = " + driverId);
                    return driverId;
                }
            }
        }
        throw new SQLException("Driver not found for ride ID " + rideId);
    }
}
