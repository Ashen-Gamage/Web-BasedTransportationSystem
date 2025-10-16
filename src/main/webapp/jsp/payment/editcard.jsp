<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.payment.model.PaymentCard" %>
<html>
<head>
    <title>Edit Payment Card</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <style>
        .form-container { max-width: 600px; margin: 2rem auto; padding: 1rem; border: 1px solid #ddd; border-radius: 6px; }
        .form-row { margin-bottom: 0.75rem; }
        label { display: block; margin-bottom: 0.25rem; font-weight: bold; }
        input[type="text"], input[type="tel"] { width: 100%; padding: 0.5rem; box-sizing: border-box; }
        .actions { display:flex; gap:0.5rem; margin-top:1rem; }
        .btn-primary { background:#007bff; color:#fff; padding:0.5rem 0.8rem; border:none; border-radius:4px; text-decoration:none; }
        .btn-secondary { background:#6c757d; color:#fff; padding:0.5rem 0.8rem; border:none; border-radius:4px; text-decoration:none; }
        .error { color: #c00; margin-bottom: 1rem; }
        .note { font-size:0.9rem; color:#555; }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Edit Payment Card</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% PaymentCard card = (PaymentCard) request.getAttribute("card"); %>

    <form method="post" action="<%=request.getContextPath()%>/editPaymentCard">
        <input type="hidden" name="id" value="<%= card != null ? card.getId() : "" %>">

<%--        <div class="form-row">--%>
<%--            <label for="card_number">Card Number</label>--%>
<%--            <input id="card_number" name="card_number" type="tel" maxlength="16" pattern="[0-9]{16}" required--%>
<%--                   value="<%= card != null && card.getCardNumber() != null ? card.getCardNumber() : "" %>">--%>
<%--            <div class="note">Enter 16 digits (numbers only).</div>--%>
<%--        </div>--%>

        <div class="form-row">
            <label for="expiry_date">Expiry Date (MM/YY)</label>
            <input id="expiry_date" name="expiry_date" type="text" maxlength="5"
                   pattern="(0[1-9]|1[0-2])/[0-9]{2}" required placeholder="MM/YY"
                   value="<%= card != null && card.getExpiryDate() != null ? card.getExpiryDate() : "" %>">
        </div>

        <div class="form-row">
            <label for="cardholder_name">Cardholder Name</label>
            <input id="cardholder_name" name="cardholder_name" type="text" maxlength="100" required
                   value="<%= card != null && card.getCardholderName() != null ? card.getCardholderName() : "" %>">
        </div>

        <% if (card != null && card.getCreatedAt() != null) { %>
        <div class="form-row">
            <label>Added On</label>
            <div class="note"><%= card.getCreatedAt().toString() %></div>
        </div>
        <% } %>

        <div class="actions">
            <button type="submit" class="btn-primary">Save</button>
            <a class="btn-secondary" href="<%=request.getContextPath()%>/jsp/payment/viewcards.jsp">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>