package com.example.rating.servlet;

import com.example.rating.dao.RatingDAO;
import com.example.rating.model.Rating;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/viewFeedback")
public class ViewFeedbackServlet extends HttpServlet {
    private final RatingDAO ratingDAO = new RatingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String driverIdParam = request.getParameter("driverId");
            String userIdParam = request.getParameter("userId");

            List<Rating> ratings;

            if (driverIdParam != null && !driverIdParam.equals("null") && !driverIdParam.isEmpty()) {
                int driverId = Integer.parseInt(driverIdParam);
                ratings = ratingDAO.getRatingsByDriverId(driverId);
                request.setAttribute("type", "driver");
            } else if (userIdParam != null && !userIdParam.equals("null") && !userIdParam.isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                ratings = ratingDAO.getRatingsByUserId(userId);
                request.setAttribute("type", "user");
            } else {
                ratings = ratingDAO.getAllRatings(); // fallback
                request.setAttribute("type", "all");
            }

            request.setAttribute("ratings", ratings);
            request.getRequestDispatcher("/jsp/rating/feedback.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching ratings: " + e.getMessage());
        }
    }
}
