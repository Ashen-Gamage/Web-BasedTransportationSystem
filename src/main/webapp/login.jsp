<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Transport System - Login</title>
  <link href="https://cdn.jsdelivr.net/npm/remixicon@4.2.0/fonts/remixicon.css" rel="stylesheet">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login.css">
</head>
<body>
<%
  String role = request.getParameter("role");
  String title = "Login";
  String subtitle = "Sign in to continue";
  String iconClass = "ri-login-box-line";

  if ("driver".equals(role)) {
    title = "Driver Login";
    subtitle = "Manage your rides and availability";
    iconClass = "ri-steering-2-line";
  } else if ("passenger".equals(role)) {
    title = "Passenger Login";
    subtitle = "Book rides and view history";
    iconClass = "ri-user-line";
  } else if ("admin".equals(role)) {
    title = "Admin Login";
    subtitle = "Manage users and system data";
    iconClass = "ri-shield-user-line";
  }
%>

<div class="login-container">
  <div class="login-header">
    <div class="logo">
      <i class="<%= iconClass %>"></i> Transport System
    </div>
    <h1><%= title %></h1>
    <p><%= subtitle %></p>
  </div>

  <form action="<%=request.getContextPath()%>/login" method="post">
    <div class="form-group">
      <label for="username">Username</label>
      <i class="ri-user-line"></i>
      <input type="text" id="username" name="username" placeholder="Enter username" required>
    </div>

    <div class="form-group">
      <label for="password">Password</label>
      <i class="ri-lock-line"></i>
      <input type="password" id="password" name="password" placeholder="Enter password" required>
    </div>

    <input type="hidden" name="role" value="<%= role != null ? role : "passenger" %>">

    <button type="submit" class="login-btn">
      <i class="ri-login-circle-line"></i> Sign In
    </button>
  </form>

  <% String error = (String) request.getAttribute("error"); if (error != null) { %>
  <div class="error-message">
    <i class="ri-alert-line"></i> <%= error %>
  </div>
  <% } %>

  <div class="links">
    <a href="<%=request.getContextPath()%>/forgot-password.jsp">Forgot Password?</a>
    <a href="<%=request.getContextPath()%>/register.jsp?role=<%= role != null ? role : "passenger" %>">Create Account</a>
  </div>
</div>
</body>
</html>
