package com.example.payment.servlet;

import com.example.payment.model.PaymentCard;
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

@WebServlet("/addPaymentCard")
public class AddPaymentCardServlet extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"rider".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }
        request.getRequestDispatcher("/jsp/payment/addcard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"rider".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        PaymentCard card = new PaymentCard();
        card.setUserId(user.getId());
        card.setCardNumber(request.getParameter("card_number"));
        card.setExpiryDate(request.getParameter("expiry_date"));
        card.setCardholderName(request.getParameter("cardholder_name"));

        try {
            paymentService.addPaymentCard(card);
            response.sendRedirect(request.getContextPath() + "/jsp/payment/viewcards.jsp");
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/payment/addcard.jsp").forward(request, response);
        }
    }
}