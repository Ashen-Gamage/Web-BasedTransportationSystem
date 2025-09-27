package com.example.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String role = request.getParameter("role");   // driver, customer, admin

        // Debug check
        System.out.println("Role received in doGet: " + role);

        request.setAttribute("role", role);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String role = request.getParameter("role");

        if ("driver".equals(role)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String license = request.getParameter("license");
            String vehicle = request.getParameter("vehicle");
            // TODO: Save driver into DB
            System.out.println("Driver Registered: " + name + " - " + license);
        }
        else if ("customer".equals(role)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            // TODO: Save customer into DB
            System.out.println("Customer Registered: " + name + " - " + phone);
        }
        else if ("admin".equals(role)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String code = request.getParameter("securityCode");
            // TODO: Validate & save admin
            System.out.println("Admin Registered: " + username);
        }

        // Redirect after successful registration
        response.sendRedirect("login.jsp");
    }
}
