package com.example.servlets;

import com.example.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/ ")
public class DBTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {
            out.println("<h2>✅ DB Connected Successfully!</h2>");
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<h2>❌ DB Connection Failed: " + e.getMessage() + "</h2>");
        }
    }
}
