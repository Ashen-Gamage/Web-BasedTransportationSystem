<%@ page import="com.example.user.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard - RideNow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link href="https://cdn.jsdelivr.net/npm/remixicon/fonts/remixicon.css" rel="stylesheet">
</head>
<body>

<div class="dashboard">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <h2>RideNow</h2>
        </div>
        <ul class="menu">
            <li><a href="<%=request.getContextPath()%>/jsp/user/profile.jsp"><i class="ri-user-line"></i> Profile</a></li>
            <li><a href="<%=request.getContextPath()%>/jsp/ridebooking/bookride.jsp" class="active"><i class="ri-car-line"></i> Book Ride</a></li>
            <li><a href="<%=request.getContextPath()%>/jsp/ridetracking/track.jsp"><i class="ri-map-pin-line"></i> Track Ride</a></li>
            <li><a href="<%=request.getContextPath()%>/jsp/payment/payment.jsp"><i class="ri-wallet-line"></i> Payments</a></li>
            <li><a href="<%=request.getContextPath()%>/jsp/rating/rating.jsp"><i class="ri-star-line"></i> Ratings</a></li>
            <li><a href="<%=request.getContextPath()%>/logout"><i class="ri-logout-box-line"></i> Logout</a></li>
        </ul>
    </aside>

    <!-- Main content -->
    <main></main>
</div>

</body>
</html>
