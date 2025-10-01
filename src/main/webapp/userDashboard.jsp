<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Prevent caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Session check
    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");

    if (userName == null || userEmail == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>User Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/dashboard.css">
</head>
<body>
<div class="dashboard-container">
    <h1>Welcome, <%= (userName != null ? userName : userEmail) %>!</h1>

    <div class="dashboard-links">
        <a href="<%=request.getContextPath()%>/profile.jsp">Profile</a>
        <a href="<%=request.getContextPath()%>/rides.jsp">My Rides</a>
        <a href="<%=request.getContextPath()%>/settings.jsp">Settings</a>
        <a href="<%=request.getContextPath()%>/logout">Logout</a>
    </div>
</div>
</body>
</html>
