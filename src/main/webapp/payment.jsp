<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.dao.PaymentDao.PaymentRow" %>
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
    <!-- ================= USER SECTION ================= -->
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
    <!-- ================= DRIVER SECTION ================= -->
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
    <!-- ================= ADMIN SECTION ================= -->
    <section class="admin-section">
      <h2>Admin & Payouts</h2>
      <p>View all payments, invoices, and driver balances.</p>
      <p>Create, update, or delete wallet entries if validation fails or refunds are needed.</p>

      <!-- Create Payment Form (TOP of table) -->
      <h3>Create New Payment</h3>
      <form action="<%=request.getContextPath()%>/admin" method="post">
        <input type="hidden" name="action" value="createPayment">
        <input type="text" name="rideId" placeholder="Ride ID" required>
        <input type="text" name="payerId" placeholder="Payer ID" required>
        <input type="text" name="amount" placeholder="Amount in cents" required>
        <input type="text" name="method" placeholder="Method" required>
        <button type="submit">Create Payment</button>
      </form>


      <!-- Payment Table -->
      <table border="1" style="margin-top:15px; border-collapse: collapse; width:100%;">
        <tr>
          <th>ID</th>
          <th>Ride</th>
          <th>Payer</th>
          <th>Amount</th>
          <th>Status</th>
          <th>Method</th>
          <th>Action</th>
        </tr>
        <%
          List<PaymentRow> payments = (List<PaymentRow>) request.getAttribute("payments");
          if (payments != null) {
            for (PaymentRow p : payments) {
        %>
        <tr>
          <td><%= p.getId() %></td>
          <td><%= p.getRideId() %></td>
          <td><%= p.getPayerId() %></td>
          <td><%= p.getAmount() %></td>
          <td><%= p.getStatus() %></td>
          <td><%= (p.getMethod() != null ? p.getMethod() : "") %></td>
          <td>
            <!-- Delete -->
            <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
              <input type="hidden" name="action" value="deleteRefund">
              <input type="hidden" name="paymentId" value="<%= p.getId() %>">
              <button type="submit">Delete</button>
            </form>

            <!-- Update -->
            <form action="<%=request.getContextPath()%>/admin" method="post" style="display:inline-block;">
              <input type="hidden" name="action" value="updatePayment">
              <input type="hidden" name="paymentId" value="<%= p.getId() %>">
              <input type="text" name="status" placeholder="Status">
              <input type="text" name="method" placeholder="Method">
              <button type="submit">Update</button>
            </form>
          </td>
        </tr>
        <% }} %>
      </table>
    </section>
    <% } %>


  </main>
</div>
</body>
</html>
