<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List, com.example.drivermanagement.model.Driver" %>


<%
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires", 0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RideNow Admin Dashboard</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@4.2.0/fonts/remixicon.css" rel="stylesheet">
    <style>
        /* Dashboard styling */
        .admin-actions { display:flex; gap:12px; margin-bottom:16px; }
        .card { padding:16px; border-radius:8px; box-shadow:0 1px 6px rgba(0,0,0,0.06); background:#fff; text-decoration:none; color:black; }
        table { width:100%; border-collapse:collapse; }
        th, td { padding:8px 10px; border-bottom:1px solid #eee; text-align:left; }
        a.btn { display:inline-block; padding:8px 12px; border-radius:4px; background:#007BFF; color:#fff; text-decoration:none; }
        a.btn.secondary { background:#6c757d; }
    </style>
</head>
<body>

<header class="navbar">
    <div class="logo"><a href="<%=request.getContextPath()%>/index.jsp">RideNow</a></div>
    <nav>
        <ul class="nav-actions">
            <li><a href="<%=request.getContextPath()%>/jsp/admin/adminDashboard.jsp">Dashboard</a></li>
            <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
        </ul>
    </nav>
</header>

<main class="container" style="padding:24px;">
    <h1>Admin Dashboard</h1>

    <!-- Admin Actions -->
    <div class="admin-actions">
        <a class="card" href="<%=request.getContextPath()%>/jsp/drivermanagement/assign.jsp">Assign Driver</a>
        <a class="card" href="<%=request.getContextPath()%>/jsp/drivermanagement/manage.jsp">Manage Drivers</a>
        <a class="card" href="<%=request.getContextPath()%>/jsp/drivermanagement/adddriver.jsp">Add Drivers</a>
    </div>

    <!-- Active Drivers Table -->
    <section class="card">
        <h2>Active Drivers</h2>

        <%
            List<Driver> drivers = (List<Driver>) request.getAttribute("drivers");
            if (drivers == null || drivers.isEmpty()) {
        %>
        <p>No drivers found. Use Manage Drivers to add or import.</p>
        <%
        } else {
        %>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>User ID</th>
                <th>License</th>
                <th>Vehicle</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% for (Driver d : drivers) { %>
            <tr>
                <td><%= d.getId() %></td>
                <td><%= d.getUserId() %></td> <!-- safe: int, no null check -->
                <td><%= d.getLicenseNumber() %></td>
                <td><%= d.getVehicleType() %></td>
                <td><%= d.getStatus() %></td>
                <td>
                    <a href="<%=request.getContextPath()%>/admin/manageDriver.jsp?driverId=<%=d.getId()%>">Edit</a> |
                    <a href="<%=request.getContextPath()%>/admin/assignDriver.jsp?rideId=&driverId=<%=d.getId()%>">Assign</a>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <%
            }
        %>
    </section>
</main>

<footer style="padding:16px;text-align:center;">
    <p>© 2025 RideNow — Admin Panel</p>
</footer>

</body>
</html>
