<%@ page import="com.example.user.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.ridebooking.model.RideRequest" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
        return;
    }

    // Retrieve rides passed from the servlet
    List<RideRequest> pendingRides = (List<RideRequest>) request.getAttribute("pendingRides");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Ride - RideNow</title>

    <!-- ✅ CSS Reference -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ridebooking.css">
</head>

<body>
<!-- ===== Dashboard Header Section ===== -->
<div class="dashboard-container">
    <h1>Welcome, <%= user.getUsername() %> </h1>
    <p class="tagline">Plan your next trip effortlessly and track your rides in one place.</p>

    <form action="<%=request.getContextPath()%>/bookRide" method="get" class="top-button-form">
        <button type="submit" class="btn-primary">Check Pending Rides</button>
    </form>
</div>

<!-- ===== Ride Booking Form Section ===== -->
<div class="container">
    <h2>Book a Ride</h2>

    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="<%=request.getContextPath()%>/bookRide" method="post" class="ride-form">
        <label for="pickup">Pickup Location:</label>
        <input type="text" id="pickup" name="pickup" placeholder="Enter pickup point" required>

        <label for="dropoff">Dropoff Location:</label>
        <input type="text" id="dropoff" name="dropoff" placeholder="Enter destination" required>

        <input type="submit" value="Book Now">
    </form>
</div>

<!-- ===== Pending Rides Section ===== -->
<div class="container">


    <% if (pendingRides != null && !pendingRides.isEmpty()) { %>
    <h2>Pending Rides</h2>
    <table class="ride-table">
        <thead>
        <tr>
            <th>Ride ID</th>
            <th>Pickup</th>
            <th>Dropoff</th>
            <th>Status</th>
            <th>Requested Time</th>
        </tr>
        </thead>
        <tbody>
        <% for (RideRequest ride : pendingRides) { %>
        <tr>
            <td><%= ride.getId() %></td>
            <td><%= ride.getPickupLocation() %></td>
            <td><%= ride.getDropoffLocation() %></td>
            <td><%= ride.getStatus() %></td>
            <td><%= ride.getRequestTime() %></td>
            <td>
                <div class="ride-actions">
                    <a class="btn-secondary" href="<%=request.getContextPath()%>/editRide?id=<%= ride.getId() %>">Update</a>

                    <form method="post" action="<%=request.getContextPath()%>/deleteRide"
                          onsubmit="return confirm('Delete this ride request? This action cannot be undone.');"
                          style="display:inline;">
                        <input type="hidden" name="id" value="<%= ride.getId() %>">
                        <button type="submit" class="btn-danger">Delete</button>
                    </form>
                </div>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p class="no-rides">Check Pending Rides / Book Now.</p>
    <% } %>

    <p class="back-home">
        <a href="<%=request.getContextPath()%>/jsp/user/userDashboard.jsp" class="btn-secondary">Back to Home</a>
    </p>
</div>
</body>
</html>
