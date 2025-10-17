package com.example.rating.servlet;

import com.example.rating.service.RatingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/updateRating")
public class UpdateRatingServlet extends HttpServlet {
    private final RatingService ratingService = new RatingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int ratingId = Integer.parseInt(request.getParameter("ratingId"));
            int ratingValue = Integer.parseInt(request.getParameter("ratingValue"));
            String feedback = request.getParameter("feedback");

            boolean success = ratingService.updateRating(ratingId, ratingValue, feedback);

            // Forcefully redirect to predefined path
            response.sendRedirect("http://localhost:8080/WebBaseTransportationSystem/viewFeedback?driverId=null");

        } catch (NumberFormatException e) {
            // If ratingId or ratingValue is invalid
            response.sendRedirect("http://localhost:8080/WebBaseTransportationSystem/viewFeedback?driverId=null");
        }
    }
}
