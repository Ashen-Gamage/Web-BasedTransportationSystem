<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.drivermanagement.model.Assignment" %>
<%
    System.out.println("🧩 [JSP] allAssignments.jsp loaded successfully.");
%>
<html>
<head>
    <title>All Assignments</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/jsp/drivermanagement/assignments.css">
</head>
<body>
<h2>Driver Assignments Overview</h2>

<form method="get" action="<%=request.getContextPath()%>/allAssignments" style="margin-bottom: 20px;">
    <button type="submit" class="btn-primary">Refresh Pending Rides</button>
</form>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

<%
    List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
%>

<% if (assignments != null && !assignments.isEmpty()) { %>
<table border="1" class="assignment-table">
    <tr>
        <th>Assignment ID</th>
        <th>Ride ID</th>
        <th>Pickup</th>
        <th>Dropoff</th>
        <th>Status</th>
        <th>Assigned Time</th>
        <th>Rider Name</th>
        <th>Driver Name</th>
        <th>Vehicle Type</th>
        <th>Action</th>
    </tr>
    <% for (Assignment a : assignments) { %>
    <tr>
        <td><%= a.getAssignmentId() %></td>
        <td><%= a.getRideId() %></td>
        <td><%= a.getPickupLocation() %></td>
        <td><%= a.getDropoffLocation() %></td>
        <td><%= a.getRideStatus() %></td>
        <td><%= a.getAssignedTime() %></td>
        <td><%= a.getRiderName() %></td>
        <td><%= a.getDriverName() %></td>
        <td><%= a.getVehicleType() %></td>
        <td>
            <% if (!"completed".equalsIgnoreCase(a.getRideStatus())) { %>
            <form action="<%=request.getContextPath()%>/finishRide" method="post" style="display:inline;">
                <input type="hidden" name="rideId" value="<%= a.getRideId() %>">
                <button type="submit" class="btn-finish">Finish Ride</button>
            </form>

            <form action="<%=request.getContextPath()%>/deleteRideDriver" method="post" style="display:inline;"
                  onsubmit="return confirm('If u sure that rider update the location ?');">
                <input type="hidden" name="rideId" value="<%= a.getRideId() %>">
                <button type="submit" class="btn-delete">Delete Ride</button>
            </form>
            <% } else { %>
            ✅ Completed
            <% } %>
        </td>

    </tr>
    <% } %>
</table>
<% } else { %>
<p>No assignments found.</p>
<% } %>
</body>
</html>
