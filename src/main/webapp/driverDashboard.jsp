<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
  response.setHeader("Pragma", "no-cache");
  response.setDateHeader("Expires", 0);

  String userRole = (String) session.getAttribute("userRole");
  String userName = (String) session.getAttribute("userName");

  if (!"DRIVER".equalsIgnoreCase(userRole) || userName == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Driver Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/dashboard.css">
</head>
<body>
<div class="dashboard-container">
  <h1>Welcome, <%= userName %>!</h1>
  <p>Role: Driver</p>

  <div class="dashboard-links">
    <a href="<%=request.getContextPath()%>/profile">Profile</a>
    <a href="<%=request.getContextPath()%>/assignedRides">Assigned Rides</a>
    <a href="<%=request.getContextPath()%>/rideTracking">Track Rides</a>
    <a href="<%=request.getContextPath()%>/settings">Settings</a>
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
  </div>
</div>
</body>
</html>
