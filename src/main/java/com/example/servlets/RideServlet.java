package com.example.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class RideServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pickup = request.getParameter("pickup");
        String dropoff = request.getParameter("dropoff");

        // ✅ Save these values to DB in future
        System.out.println("Pickup: " + pickup);
        System.out.println("Drop-off: " + dropoff);

        // redirect back to dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
    }
}
