<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.user.model.User, com.example.drivermanagement.model.Driver" %>
<%
    User user = (User) request.getAttribute("user");
    if (user == null) {
        user = (User) session.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
        return;
    }

    Driver driver = (Driver) request.getAttribute("driver");
    if (driver == null) {
        driver = new Driver(); // avoid null pointer
    }



%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Driver Profile - RideNow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/jsp/drivermanagement/driverDashboard.css">
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4; }
        h2 { color: #333; }
        form { background: #fff; padding: 20px; border-radius: 8px; max-width: 500px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { width: 100%; padding: 8px; margin-top: 5px; border-radius: 4px; border: 1px solid #ccc; }
        button { margin-top: 15px; padding: 10px 15px; background: #007bff; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background: #0056b3; }
        p { margin: 10px 0; }
    </style>
</head>
<body>
<h2>Driver Profile</h2>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

<% if (request.getAttribute("message") != null) { %>
<p style="color:green;"><%= request.getAttribute("message") %></p>
<% } %>

<form action="<%= request.getContextPath() %>/driverProfile" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="<%= user.getUsername() %>" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="<%= user.getEmail() %>" disabled>

    <label for="phone">Phone:</label>
    <input type="text" id="phone" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>">

    <label for="address_line">Address Line:</label>
    <input type="text" id="address_line" name="address_line" value="<%= user.getAddressLine() != null ? user.getAddressLine() : "" %>">

    <label for="city">City:</label>
    <input type="text" id="city" name="city" value="<%= user.getCity() != null ? user.getCity() : "" %>">

    <label for="postal_code">Postal Code:</label>
    <input type="text" id="postal_code" name="postal_code" value="<%= user.getPostalCode() != null ? user.getPostalCode() : "" %>">

    <label for="license_number">License Number:</label>
    <input type="text" id="license_number" name="license_number"
           value="<%= driver.getLicenseNumber() != null ? driver.getLicenseNumber() : "" %>" required>

    <label for="vehicle_type">Vehicle Type:</label>
    <input type="text" id="vehicle_type" name="vehicle_type"
           value="<%= driver.getVehicleType() != null ? driver.getVehicleType() : "" %>" required>

    <button type="submit">Update Profile</button>
</form>
</body>
</html>
