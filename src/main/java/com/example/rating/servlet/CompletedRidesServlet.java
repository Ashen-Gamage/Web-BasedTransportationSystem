package com.example.rating.servlet;

import com.example.rating.service.RatingService;
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

@WebServlet("/completedRides")
public class CompletedRidesServlet extends HttpServlet {
    private final RatingService ratingService = new RatingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CompletedRidesServlet.doGet: Start");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"rider".equals(user.getRole())) {
            System.out.println("CompletedRidesServlet.doGet: User not logged in or not a rider, redirecting");
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        System.out.println("CompletedRidesServlet.doGet: User logged in with ID " + user.getId());

        try {
            List<Integer> completedRides = ratingService.getCompletedRidesForUser(user.getId());
            System.out.println("CompletedRidesServlet.doGet: Retrieved " + completedRides.size() + " completed rides");

            request.setAttribute("completedRides", completedRides);
            request.getRequestDispatcher("/jsp/rating/rating.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load completed rides.");
            request.getRequestDispatcher("/jsp/rating/completedRides.jsp").forward(request, response);
        }

        System.out.println("CompletedRidesServlet.doGet: End");
    }
}
