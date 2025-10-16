package com.example.user.servlet;

import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class UserDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            // If not logged in → go to login
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        // Set user info for dashboard use (name, etc.)
        request.setAttribute("user", user);

        // Forward to main dashboard JSP
        request.getRequestDispatcher("/jsp/user/dashboard.jsp").forward(request, response);
    }
}
