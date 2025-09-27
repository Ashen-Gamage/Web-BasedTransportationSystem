package com.example.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // 1️⃣ Invalidate session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2️⃣ Clear JSESSIONID cookie (some browsers keep it)
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setMaxAge(0); // expire immediately
        res.addCookie(cookie);

        // 3️⃣ Disable caching for logout response
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0
        res.setDateHeader("Expires", 0); // Proxies

        // 4️⃣ Redirect to login page
        res.sendRedirect(req.getContextPath() + "/login");
    }
}
