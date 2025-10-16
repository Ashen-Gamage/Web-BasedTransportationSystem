package com.example.payment.service;

import com.example.payment.dao.PaymentDAO;
import com.example.payment.model.PaymentCard;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public void addPaymentCard(PaymentCard card) throws SQLException {
        // Validate card number: exactly 16 digits
        if (!card.getCardNumber().matches("\\d{16}")) {
            throw new SQLException("Card number must be exactly 16 digits");
        }

        // Validate expiry date: MM/YY format and not expired
        try {
            YearMonth expiry = YearMonth.parse(card.getExpiryDate(), DateTimeFormatter.ofPattern("MM/yy"));
            if (expiry.isBefore(YearMonth.now())) {
                throw new SQLException("Card has expired");
            }
        } catch (Exception e) {
            throw new SQLException("Invalid expiry date format (use MM/YY)");
        }

        paymentDAO.addPaymentCard(card);
    }

    public List<PaymentCard> getPaymentCards(int userId) throws SQLException {
        return paymentDAO.getPaymentCardsByUserId(userId);
    }
}