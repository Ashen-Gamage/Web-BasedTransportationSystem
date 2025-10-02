package com.example.servlets;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.HashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // don’t create new session
        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Username and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            User user = dao.findByUsername(username.trim());

            if (user == null || !HashUtil.verify(password, user.getPasswordHash())) {
                req.setAttribute("error", "Invalid username or password.");
                req.getRequestDispatcher("/login.jsp").forward(req, res);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole());

            switch (user.getRole().toUpperCase()) {
                case "ADMIN":
                    res.sendRedirect(req.getContextPath() + "/adminDashboard.jsp");
                    break;
                case "DRIVER":
                    res.sendRedirect(req.getContextPath() + "/driverDashboard.jsp");
                    break;
                default:
                    res.sendRedirect(req.getContextPath() + "/userDashboard.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Server error. Try again.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }
}
