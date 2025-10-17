package com.example.rating.servlet;

import com.example.rating.service.RatingService;

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

@WebServlet("/deleteRating")
public class DeleteRatingServlet extends HttpServlet {
    private final RatingService ratingService = new RatingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int ratingId = Integer.parseInt(request.getParameter("ratingId"));

        boolean success = ratingService.deleteRating(ratingId);
        if (success) {
            response.sendRedirect("http://localhost:8080/WebBaseTransportationSystem/viewFeedback?driverId=null");
        } else {
            request.setAttribute("error", "Failed to delete rating.");
            response.sendRedirect("http://localhost:8080/WebBaseTransportationSystem/viewFeedback?driverId=null");
        }
    }
}
