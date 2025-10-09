package com.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import com.example.dao.PaymentDao;
import com.example.dao.PaymentDao.PaymentRow;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            List<PaymentRow> payments = paymentDao.findAll();
            req.setAttribute("payments", payments);
            req.getRequestDispatcher("/WEB-INF/adminDashboard.jsp").forward(req, res);
        } catch (Exception e) {
            throw new ServletException("Error loading payments", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");

        try {
            if ("updatePayment".equals(action)) {
                int paymentId = Integer.parseInt(req.getParameter("paymentId"));
                String status = req.getParameter("status");
                String method = req.getParameter("method");
                paymentDao.updatePayment(paymentId, status, method);

            } else if ("deleteRefund".equals(action)) {
                int paymentId = Integer.parseInt(req.getParameter("paymentId"));
                paymentDao.deletePayment(paymentId);

            } else if ("createPayment".equals(action)) {   // <-- handle create
                int rideId = Integer.parseInt(req.getParameter("rideId"));
                int payerId = Integer.parseInt(req.getParameter("payerId"));
                long amount = Long.parseLong(req.getParameter("amount"));
                String method = req.getParameter("method");
                paymentDao.createPayment(rideId, payerId, amount, method);
            }

            resp.sendRedirect(req.getContextPath() + "/admin");
        } catch (Exception e) {
            throw new ServletException("Admin action failed: " + action, e);
        }
    }
}
