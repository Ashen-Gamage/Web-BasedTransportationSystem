<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.model.User" %>

<%
    // 1️⃣ Prevent browser caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // 2️⃣ Ensure user object exists
    User user = (User) request.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/profile.css">
</head>
<body>
<div class="profile-container">
    <h2>Welcome, <%= user.getName() %>!</h2>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>
    <p><strong>Phone:</strong> <%= user.getPhone() != null ? user.getPhone() : "-" %></p>
    <p><strong>Role:</strong> <%= user.getRole() %></p>
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</div>
</body>
</html>
