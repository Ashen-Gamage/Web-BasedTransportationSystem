package com.example.servlets;

import com.example.dao.UserDAO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        int userId = (int) session.getAttribute("userId");
        try {
            User u = new UserDAO().findById(userId);
            if (u == null) {
                session.invalidate();
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("user", u);
            req.getRequestDispatcher("/settings.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
