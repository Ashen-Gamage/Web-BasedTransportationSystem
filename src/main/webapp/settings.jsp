<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires",0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Settings</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/profile.css">
</head>
<body>

<div class="profile-container">

    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="brand">
            <div class="logo"></div>
            <div class="name">Settings</div>
        </div>
        <ul>
            <!-- Profile -->
            <li>
                <a href="<%=request.getContextPath()%>/profile">
                    <span class="dot"></span><span class="label">Profile</span>
                </a>
            </li>

            <!-- Dashboard -->
            <li>
                <c:choose>
                    <c:when test="${user.role == 'ADMIN'}">
                        <a href="<%=request.getContextPath()%>/adminDashboard.jsp"><span class="dot"></span><span class="label">Dashboard</span></a>
                    </c:when>
                    <c:when test="${user.role == 'DRIVER'}">
                        <a href="<%=request.getContextPath()%>/driverDashboard.jsp"><span class="dot"></span><span class="label">Dashboard</span></a>
                    </c:when>
                    <c:otherwise>
                        <a href="<%=request.getContextPath()%>/userDashboard.jsp"><span class="dot"></span><span class="label">Dashboard</span></a>
                    </c:otherwise>
                </c:choose>
            </li>

            <!-- Rides (example for drivers or users) -->
            <li>
                <c:choose>
                    <c:when test="${user.role == 'DRIVER'}">
                        <a href="<%=request.getContextPath()%>/driverRides">
                            <span class="dot"></span><span class="label">Rides</span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="<%=request.getContextPath()%>/userRides">
                            <span class="dot"></span><span class="label">Rides</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </li>

            <!-- Logout -->
            <li>
                <a href="<%=request.getContextPath()%>/logout">
                    <span class="dot"></span><span class="label">Logout</span>
                </a>
            </li>
        </ul>

    </aside>

    <!-- Main Content -->
    <main class="profile-content">
        <div class="card header-card">
            <h1 class="title">Settings</h1>
            <span class="subtitle">Manage your account and preferences</span>
        </div>

        <!-- Common Settings for All Users -->
        <div class="card">
            <div class="section-title">Account Settings</div>
            <ul>
                <li>Profile Info (Name, Email, Phone)</li>
                <li>Change Password</li>
                <li>Update Personal Info</li>
                <li>Delete/Deactivate Account</li>
            </ul>
        </div>

        <!-- Role-Specific Settings -->

        <!-- Admin Settings -->
        <!-- Driver Settings -->
        <!-- Rider Settings -->
        <c:choose>
            <c:when test="${user.role == 'ADMIN'}">
                <div class="card">
                    <div class="section-title">Admin Settings</div>
                    <ul>
                        <li>User Management (Add/Remove Drivers/Riders, Reset Passwords)</li>
                        <li>System Settings (Ride Pricing, Commission Rates)</li>
                        <li>Reports & Analytics (Earnings Reports, Monitor Rides)</li>
                        <li>Admin Profile (Update Admin Code/Credentials)</li>
                    </ul>
                </div>
            </c:when>

            <c:when test="${user.role == 'DRIVER'}">
                <div class="card">
                    <div class="section-title">Driver Settings</div>
                    <ul>
                        <li>Vehicle Info (Type, Plate No, Model)</li>
                        <li>License Info (Driving License, Upload Docs)</li>
                        <li>Availability (Toggle Online/Offline)</li>
                        <li>Earnings & Payouts (Bank, PayPal)</li>
                        <li>Ride Preferences (Short/Long rides, Max pickup distance)</li>
                    </ul>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card">
                    <div class="section-title">Rider Settings</div>
                    <ul>
                        <li>Payment Settings (Add/Remove Credit Card/PayPal, Default Payment)</li>
                        <li>Ride Preferences (Favorite Pickup Locations, Vehicle Type)</li>
                        <li>Notification Preferences (SMS, Email, Push)</li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>

    </main>
</div>

</body>
</html>
