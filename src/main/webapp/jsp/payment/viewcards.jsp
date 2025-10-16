<!— language: html -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.payment.model.PaymentCard" %>
<html>
<head>
    <title>My Payment Cards - RideNow</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <style>
        .card-list { display: flex; flex-direction: column; gap: 1rem; }
        .card-item { border: 1px solid #ddd; padding: 1rem; border-radius: 6px; display: flex; justify-content: space-between; align-items: center; }
        .card-info { display: flex; flex-direction: column; gap: 0.25rem; }
        .card-actions { display: flex; gap: 0.5rem; }
        .btn-primary, .btn-secondary, .btn-danger { padding: 0.4rem 0.8rem; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; color: #fff; }
        .btn-primary { background: #007bff; }
        .btn-secondary { background: #6c757d; }
        .btn-danger { background: #dc3545; }
    </style>
</head>
<body>
<h2>My Payment Cards</h2>

<div class="container">

    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <% List<PaymentCard> cards = (List<PaymentCard>) request.getAttribute("cards"); %>

    <% if (cards != null && !cards.isEmpty()) { %>
    <div class="card-list">
        <% for (PaymentCard card : cards) { %>
        <div class="card-item">
            <div class="card-info">
                <div><strong>Card</strong>: **** **** **** <%= card.getCardNumber().substring(Math.max(0, card.getCardNumber().length() - 4)) %></div>
                <div><strong>Expiry</strong>: <%= card.getExpiryDate() %></div>
                <div><strong>Cardholder</strong>: <%= card.getCardholderName() %></div>
                <div><strong>Added On</strong>: <%= card.getCreatedAt() %></div>
            </div>

            <div class="card-actions">
                <a class="btn-secondary" href="<%=request.getContextPath()%>/editPaymentCard?id=<%=card.getId()%>">Update</a>

                <form method="post" action="<%=request.getContextPath()%>/deletePaymentCard" onsubmit="return confirm('Delete this card? This action cannot be undone.');" style="display:inline;">
                    <input type="hidden" name="id" value="<%= card.getId() %>">
                    <button type="submit" class="btn-danger">Delete</button>
                </form>
            </div>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <p class="no-rides">No payment cards added.</p>
    <% } %>

    <p class="back-home">
        <a href="<%=request.getContextPath()%>/jsp/user/userDashboard.jsp" class="btn-secondary">Back to Home</a>
        <a href="<%=request.getContextPath()%>/addPaymentCard" class="btn-primary">Add New Card</a>
    </p>

</div>
</body>
</html>
