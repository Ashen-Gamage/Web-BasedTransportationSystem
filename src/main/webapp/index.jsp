<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RideWithUs - Uber Style</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/index.css">
</head>
<body>
<!-- Navigation -->
<nav class="navbar">
    <div class="logo">RideWithUs</div>
    <ul class="nav-links">
        <li><a href="<%=request.getContextPath()%>/index.jsp">Home</a></li>
        <li><a href="<%=request.getContextPath()%>/login.jsp">Login</a></li>

        <!-- Register Dropdown -->
        <li class="dropdown">
            <a href="javascript:void(0)" class="dropbtn">Register ▾</a>
            <div class="dropdown-content">
                <a href="<%=request.getContextPath()%>/RegisterServlet?role=driver">Driver</a>
                <a href="<%=request.getContextPath()%>/RegisterServlet?role=customer">Customer</a>
                <a href="<%=request.getContextPath()%>/RegisterServlet?role=admin">Admin</a>
            </div>
        </li>
    </ul>
</nav>



<!-- Hero Section -->
<section class="hero">
    <div class="hero-content">
        <h1>Move with Safety & Reliability</h1>
        <p>Your journey, your choice. Book your ride now.</p>
        <div class="cta-buttons">
            <a href="<%=request.getContextPath()%>/login.jsp" class="btn">Login</a>
            <a href="<%=request.getContextPath()%>/register.jsp" class="btn btn-secondary">Register</a>
        </div>
    </div>
</section>

<!-- Booking Form -->
<section class="booking">
    <form action="BookRideServlet" method="post">
        <input type="text" name="pickup" placeholder="Pickup Location" required>
        <input type="text" name="dropoff" placeholder="Dropoff Location" required>
        <button type="submit">Book Ride</button>
    </form>
</section>

<!-- Footer -->
<footer>
    <p>© 2025 RideWithUs. Inspired by Uber.</p>
</footer>
</body>
</html>
