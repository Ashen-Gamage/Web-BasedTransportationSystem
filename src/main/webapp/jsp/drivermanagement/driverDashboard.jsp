<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.user.model.User, com.example.drivermanagement.model.Driver" %>
<%
  // === GET USER/DRIVER ===
  User user = (User) request.getAttribute("user");
  Driver driver = (Driver) request.getAttribute("driver");

  if (user == null) user = (User) session.getAttribute("user");
  if (driver == null) driver = (Driver) session.getAttribute("driver");

  // Redirect to login if user is null or not driver
  if (user == null || !"driver".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
    return;
  }

  // Save driver in session for use in other JSPs
  if (driver != null) {
    session.setAttribute("driver", driver);
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Driver Dashboard - RideNow</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/drivermanagement/driverDashboard.css">
  <link href="https://cdn.jsdelivr.net/npm/remixicon/fonts/remixicon.css" rel="stylesheet">
</head>
<body>

<div class="dashboard">
  <!-- Sidebar -->
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2>RideNow Driver</h2>
    </div>
    <ul class="menu">
      <li><a href="<%=request.getContextPath()%>/driverProfile">Profile</a></li>
      <li><a href="<%=request.getContextPath()%>/jsp/drivermanagement/rideRequest.jsp"><i class="ri-car-line"></i> Ride Requests</a></li>
      <li><a href="<%=request.getContextPath()%>/jsp/drivermanagement/assignments.jsp"><i class="ri-clipboard-line"></i> Assignments</a></li>
      <li><a href="<%=request.getContextPath()%>/logout"><i class="ri-logout-box-line"></i> Logout</a></li>
    </ul>
  </aside>

  <!-- Main Content -->
  <main class="main-content">
    <h1>Welcome, <%= user.getUsername() %> 👋</h1>
    <p>This is your driver dashboard. View ride requests, manage assignments, and update your profile.</p>

    <div class="dashboard-cards">
      <div class="card">
        <i class="ri-user-settings-line"></i>
        <h3>Profile</h3>
        <p>Update your personal and vehicle information.</p>
        <a href="<%=request.getContextPath()%>/jsp/drivermanagement/profile.jsp" class="btn">View Profile</a>
      </div>

      <div class="card">
        <i class="ri-taxi-line"></i>
        <h3>Ride Requests</h3>
        <p>Check new ride requests assigned to you.</p>
        <a href="<%=request.getContextPath()%>/jsp/drivermanagement/rideRequest.jsp" class="btn">View Requests</a>
      </div>

      <div class="card">
        <i class="ri-briefcase-line"></i>
        <h3>Assignments</h3>
        <p>Manage your current and past ride assignments.</p>
        <a href="<%=request.getContextPath()%>/jsp/drivermanagement/assignments.jsp" class="btn">View Assignments</a>
      </div>
    </div>
  </main>
</div>

</body>
</html>
