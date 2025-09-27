package com.example.servlets;

import com.example.util.DBUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/db-test")
public class DBTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            try (Connection conn = DBUtil.getConnection()) {
                out.println("<h2>✅ DB Connected Successfully!</h2>");
            } catch (Exception e) {
                out.println("<h2>❌ DB Connection Failed: " + e.getMessage() + "</h2>");
                e.printStackTrace(out);
            }
        }
    }
}
