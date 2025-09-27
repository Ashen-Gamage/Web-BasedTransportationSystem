package com.example.servlets;

import com.example.dao.UserDAO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // 1️⃣ Session check
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2️⃣ Prevent browser caching
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0
        res.setDateHeader("Expires", 0); // Proxies

        // 3️⃣ Fetch user info
        int userId = (int) session.getAttribute("userId");
        try {
            User u = new UserDAO().findById(userId);
            if (u == null) {
                // If user not found in DB, force logout
                session.invalidate();
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("user", u);

            // 4️⃣ Forward to JSP
            req.getRequestDispatcher("/profile.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            // On error, redirect to login
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
