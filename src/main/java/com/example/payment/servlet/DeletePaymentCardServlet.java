package com.example.payment.servlet;

import com.example.payment.service.PaymentService;
import com.example.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deletePaymentCard")
public class DeletePaymentCardServlet extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"rider".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            // optional: verify ownership in service/DAO
            paymentService.deletePaymentCard(id, user.getId());
            response.sendRedirect(request.getContextPath() + "/jsp/payment/viewcards.jsp");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid card id.");
            request.getRequestDispatcher("/jsp/payment/viewcards.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/payment/viewcards.jsp").forward(request, response);
        }
    }
}