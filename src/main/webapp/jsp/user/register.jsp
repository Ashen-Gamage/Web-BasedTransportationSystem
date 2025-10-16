<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/register.css">
    <style>
        .register-container { max-width: 400px; margin: 50px auto; padding: 20px; background: #fff; border-radius: 8px; }
        label { display: block; margin: 10px 0 5px; font-weight: bold; }
        input, select { width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px; }
        button { width: 100%; padding: 10px; background: #007bff; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background: #0056b3; }
        .error-text { color: red; }
        .success-text { color: green; }
        .driver-fields { display: none; }
    </style>
    <script>
        function toggleDriverFields() {
            var role = document.getElementById("roleSelect").value;
            var driverFields = document.getElementById("driverFields");
            if (role === "driver") {
                driverFields.style.display = "block";
            } else {
                driverFields.style.display = "none";
            }
        }
    </script>
</head>
<body onload="toggleDriverFields()">
<div class="register-container">
    <h1>Create Your Account</h1>

    <form action="<%=request.getContextPath()%>/register" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" placeholder="Enter your username" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="Enter your email" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Enter your password" required>

        <label for="roleSelect">Role</label>
        <select id="roleSelect" name="role" onchange="toggleDriverFields()">
            <option value="rider">Rider</option>
            <option value="driver">Driver</option>
        </select>

        <div id="driverFields" class="driver-fields">
            <label for="license_number">License Number</label>
            <input type="text" id="license_number" name="license_number" placeholder="Enter your license number">

            <label for="vehicle_type">Vehicle Type</label>
            <input type="text" id="vehicle_type" name="vehicle_type" placeholder="Enter your vehicle type (e.g., Sedan, SUV)">
        </div>

        <button type="submit">Register</button>
    </form>

    <% if (request.getAttribute("error") != null) { %>
    <p class="error-text"><%= request.getAttribute("error") %></p>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <p class="success-text"><%= request.getAttribute("success") %></p>
    <% } %>
</div>
</body>
</html>