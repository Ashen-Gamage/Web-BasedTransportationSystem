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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.getRequestDispatcher("/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            User u = dao.findByEmail(email.trim().toLowerCase());

            // ✅ Verify user exists and password matches
            if (u == null || !HashUtil.verify(password, u.getPasswordHash())) {
                req.setAttribute("error", "Invalid credentials.");
                req.getRequestDispatcher("/login.jsp").forward(req, res);
                return;
            }

            // ✅ Set session attributes
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", u.getId());
            session.setAttribute("userRole", u.getRole());
            session.setAttribute("userName", u.getName());
            session.setAttribute("userEmail", u.getEmail());

            // 🔹 Redirect based on role stored in DB
            String role = u.getRole() != null ? u.getRole().toUpperCase() : "USER";

            switch (role) {
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
