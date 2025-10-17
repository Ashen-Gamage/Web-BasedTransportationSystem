<%@ page import="java.util.List" %>
<%@ page import="com.example.rating.model.Rating" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Driver Feedback</title>
    <style>
        /* your styles */
    </style>
</head>
<body>
<button class="refresh-button" onclick="refreshCompletedRides()">⟳ Refresh</button>
<div class="container">
    <h2>Driver Feedback</h2>

    <% String error = (String) request.getAttribute("error");
        if (error != null) { %>
    <p style="color:red;"><%= error %></p>
    <% } %>

    <% Integer driverId = (Integer) request.getAttribute("driverId");
        if (driverId != null) { %>
    <p><strong>Driver ID: <%= driverId %></strong></p>
    <% } %>

    <% List<Rating> ratings = (List<Rating>) request.getAttribute("ratings");
        Double averageRating = (Double) request.getAttribute("averageRating");
        if (averageRating != null) { %>
    <p><strong>Average Rating: <%= String.format("%.2f", averageRating) %> / 5</strong></p>
    <% } %>

    <% if (ratings != null && !ratings.isEmpty()) { %>
    <table>
        <tr>
            <th>Ride ID</th><th>User ID</th><th>Rating</th><th>Feedback</th><th>Date</th>
        </tr>
        <% for (Rating r : ratings) { %>
        <tr>
            <td><%= r.getRideRequestId() %></td>
            <td><%= r.getUserId() %></td>
            <td><%= r.getRating() %></td>
            <td><%= r.getFeedback() != null ? r.getFeedback() : "" %></td>
            <td><%= r.getCreatedAt() %></td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p>No feedback available for this driver.</p>
    <% } %>

    <p><a href="<%=request.getContextPath()%>/index.jsp">Back to Home</a></p>
</div>
</body>
</html>
