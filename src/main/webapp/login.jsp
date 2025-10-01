<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  // Prevent caching
  response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
  response.setHeader("Pragma","no-cache");
  response.setDateHeader("Expires",0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login.css">
</head>
<body>
<div class="login-container">
  <form action="<%=request.getContextPath()%>/login" method="post" autocomplete="off">
    <div class="form-group">
      <label>Email</label>
      <input type="email" name="email" required autocomplete="off">
    </div>
    <div class="form-group">
      <label>Password</label>
      <input type="password" name="password" required autocomplete="new-password">
    </div>
    <button class="login-btn" type="submit">Sign In</button>

    <%
      String error = (String) request.getAttribute("error");
      if (error != null) {
    %>
    <div class="error-message"><%= error %></div>
    <% } %>
  </form>
</div>
</body>
</html>
