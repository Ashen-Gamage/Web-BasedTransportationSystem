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

@WebServlet("/editPaymentCard")
public class EditPaymentCardServlet extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"rider".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/payment/viewcards.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            PaymentCard card = paymentService.getPaymentCardById(id);
            if (card == null || card.getUserId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/jsp/payment/viewcards.jsp");
                return;
            }
            request.setAttribute("card", card);
            request.getRequestDispatcher("/jsp/payment/editcard.jsp").forward(request, response);
        } catch (NumberFormatException | SQLException e) {
            request.setAttribute("error", "Unable to load card: " + e.getMessage());
            request.getRequestDispatcher("/jsp/payment/viewcards.jsp").forward(request, response);
        }
    }

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

            // Fetch existing card from DB to preserve the card number
            PaymentCard existingCard = paymentService.getPaymentCardById(id);
            if (existingCard == null || existingCard.getUserId() != user.getId()) {
                request.setAttribute("error", "Card not found or not owned by you.");
                request.getRequestDispatcher("/jsp/payment/viewcards.jsp").forward(request, response);
                return;
            }

            // Only update expiry date and cardholder name
            String expiryDate = request.getParameter("expiry_date");
            String cardholderName = request.getParameter("cardholder_name");

            // Validate required fields
            if (expiryDate == null || expiryDate.trim().isEmpty()) {
                throw new IllegalArgumentException("Expiry date cannot be empty.");
            }
            if (cardholderName == null || cardholderName.trim().isEmpty()) {
                throw new IllegalArgumentException("Cardholder name cannot be empty.");
            }

            PaymentCard updatedCard = new PaymentCard();
            updatedCard.setId(id);
            updatedCard.setUserId(user.getId());
            updatedCard.setCardNumber(existingCard.getCardNumber()); // keep original card number
            updatedCard.setExpiryDate(expiryDate);
            updatedCard.setCardholderName(cardholderName);

            paymentService.updatePaymentCard(updatedCard);
            response.sendRedirect(request.getContextPath() + "/jsp/payment/viewcards.jsp");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid card id.");
            request.getRequestDispatcher("/jsp/payment/editcard.jsp").forward(request, response);
        } catch (IllegalArgumentException | SQLException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/payment/editcard.jsp").forward(request, response);
        }
    }


}