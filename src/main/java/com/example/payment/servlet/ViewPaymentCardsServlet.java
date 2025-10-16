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
import java.util.List;

@WebServlet("/viewPaymentCards")
public class ViewPaymentCardsServlet extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"rider".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        try {
            // ✅ Fetch payment cards before showing page (like pending rides)
            List<PaymentCard> cards = paymentService.getPaymentCards(user.getId());
            request.setAttribute("cards", cards);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load payment cards.");
        }

        // ✅ Forward once at the end
        request.getRequestDispatcher("/jsp/payment/viewcards.jsp").forward(request, response);
    }
}
