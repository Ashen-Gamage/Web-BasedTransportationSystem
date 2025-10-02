<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Fetch the role from session
  String role = (String) session.getAttribute("userRole");
  if (role == null) {
    // User not logged in
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Payments Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/payment.css">
</head>
<body>
<div class="dashboard-container">

  <header class="dashboard-header">
    <h1>
      <%
        switch(role.toUpperCase()) {
          case "USER": out.print("Invoice Generation"); break;
          case "DRIVER": out.print("Driver Wallet"); break;
          case "ADMIN": out.print("Admin & Payouts"); break;
          default: out.print("Payments Dashboard");
        }
      %>
    </h1>
    <p class="role-label">Logged in as: <%= role %></p>
  </header>

  <main class="dashboard-main">

    <% if ("USER".equalsIgnoreCase(role)) { %>
    <section class="user-section">
      <h2>Invoice Generation</h2>
      <p>Step 1: View your invoices.</p>
      <p>Step 2: Make payment for pending invoices.</p>
      <p>Step 3: On successful payment, confirmation is shown.</p>
      <p>Admin can see all your invoices.</p>
      <form action="<%=request.getContextPath()%>/payment" method="post">
        <input type="hidden" name="action" value="createPayment">
        <button class="btn primary" type="submit">Pay Now</button>
      </form>
    </section>

    <% } else if ("DRIVER".equalsIgnoreCase(role)) { %>
    <section class="driver-section">
      <h2>Driver Wallet</h2>
      <p>After a user completes payment, your wallet is credited automatically.</p>
      <p>View your current balance and recent wallet transactions below.</p>
      <form action="<%=request.getContextPath()%>/driver" method="post">
        <input type="hidden" name="action" value="requestPayout">
        <button class="btn primary" type="submit">Request Payout</button>
      </form>
    </section>

    <% } else if ("ADMIN".equalsIgnoreCase(role)) { %>
    <section class="admin-section">
      <h2>Admin & Payouts</h2>
      <p>View all payments, invoices, and driver balances.</p>
      <p>Create, update, or delete wallet entries if validation fails or refunds are needed.</p>
      <div class="admin-buttons">
        <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
          <input type="hidden" name="action" value="debitWallet">
          <button class="btn danger" type="submit">Debit Wallet</button>
        </form>
        <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
          <input type="hidden" name="action" value="createPayout">
          <button class="btn primary" type="submit">Create Payout</button>
        </form>
        <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
          <input type="hidden" name="action" value="updatePayment">
          <button class="btn primary" type="submit">Update Payment</button>
        </form>
        <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
          <input type="hidden" name="action" value="deleteRefund">
          <button class="btn warning" type="submit">Delete / Refund</button>
        </form>
      </div>
    </section>

    <% } %>

  </main>
</div>
</body>
</html>
