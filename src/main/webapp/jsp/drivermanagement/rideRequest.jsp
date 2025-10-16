<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.drivermanagement.model.RideRequest, com.example.drivermanagement.model.Driver" %>

<%
    // Get driver from session
    Driver driver = (Driver) session.getAttribute("driver");
    int driverId = (driver != null) ? driver.getId() : 0;

    // Get pending rides from request attribute
    List<RideRequest> pendingRides = (List<RideRequest>) request.getAttribute("pendingRides");
%>

<html>
<head>
    <title>Driver Ride Requests</title>
    <style>
        body { font-family: Arial; margin: 30px; background-color: #f7f7f7; }
        h2 { color: #333; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; background: white; border-radius: 8px; overflow: hidden; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
        th { background-color: #222; color: #fff; }
        .btn-primary, .btn-danger, .btn-secondary { padding: 8px 14px; border: none; border-radius: 5px; color: #fff; cursor: pointer; text-decoration: none; font-weight: bold; }
        .btn-primary { background-color: #007bff; }
        .btn-danger { background-color: #28a745; }
        .btn-secondary { background-color: #6c757d; }
        .no-rides { text-align: center; margin-top: 20px; font-size: 18px; color: #666; }
    </style>
</head>
<body>

<h2>Pending Ride Requests</h2>

<!-- Refresh button -->
<form method="get" action="<%=request.getContextPath()%>/driverRideRequests" style="margin-bottom: 20px;">
    <button type="submit" class="btn-primary">Refresh Pending Rides</button>
</form>

<div class="container">
    <% if (pendingRides != null && !pendingRides.isEmpty()) { %>
    <table class="ride-table">
        <thead>
        <tr>
            <th>Ride ID</th>
            <th>User ID</th>
            <th>Pickup</th>
            <th>Dropoff</th>
            <th>Status</th>
            <th>Requested Time</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (RideRequest ride : pendingRides) { %>
        <tr>
            <td><%= ride.getId() %></td>
            <td><%= ride.getUserId() %></td>
            <td><%= ride.getPickupLocation() %></td>
            <td><%= ride.getDropoffLocation() %></td>
            <td><%= ride.getStatus() %></td>
            <td><%= ride.getRequestTime() %></td>
            <td>
                <form method="post" action="<%=request.getContextPath()%>/acceptRide"
                      onsubmit="return confirm('Accept this ride?');" style="display:inline;">
                    <input type="hidden" name="rideId" value="<%= ride.getId() %>">
                    <input type="hidden" name="userId" value="<%= ride.getUserId() %>">
                    <input type="hidden" name="driverId" value="<%= driverId %>">
                    <button type="submit" class="btn-danger">Accept</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p class="no-rides">🚫 No pending rides available.</p>
    <% } %>

    <p class="back-home">
        <a href="<%=request.getContextPath()%>/jsp/drivermanagement/driverDashboard.jsp" class="btn-secondary">Back to Home</a>
    </p>
</div>

</body>
</html>
