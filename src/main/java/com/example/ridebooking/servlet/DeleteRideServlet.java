package com.example.ridebooking.servlet;

import com.example.ridebooking.service.RideBookingService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteRide")
public class DeleteRideServlet extends HttpServlet {
    private final RideBookingService service = new RideBookingService();



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            service.deleteRide(id, user.getId());
            response.sendRedirect(request.getContextPath() + "/bookRide");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ride id.");
            request.getRequestDispatcher("/jsp/ridebooking/bookride.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to delete ride: " + e.getMessage());
            request.getRequestDispatcher("/jsp/ridebooking/bookride.jsp").forward(request, response);
        }
    }
}