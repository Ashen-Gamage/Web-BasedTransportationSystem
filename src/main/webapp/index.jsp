<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <title>RideNow - Move Your Way</title>

    <!-- CSS -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@4.2.0/fonts/remixicon.css" rel="stylesheet">
</head>
<body>

<!-- ===================== Navbar ===================== -->
<header class="navbar">
    <div class="logo">
        <a href="<%=request.getContextPath()%>/index.jsp">RideNow</a>
    </div>
        <div class="cta-buttons">
            <a class="btn secondary" href="<%=request.getContextPath()%>/jsp/admin/adminDashboard.jsp">
                <i class="ri-user-add-line"></i> Admin
            </a>
        </div>
    <div class="cta-buttons">
        <a class="btn secondary" href="<%=request.getContextPath()%>/jsp/drivermanagement/driverDashboard.jsp">
            <i class="ri-user-add-line"></i> Driver
        </a>
    </div>

    <nav>
        <ul class="nav-actions">
            <li><a href="<%=request.getContextPath()%>/login">Login</a></li>
            <li class="dropdown">
                <a class="dropbtn">Register <i class="ri-arrow-down-s-line"></i></a>
                <div class="dropdown-content">
                    <a href="<%=request.getContextPath()%>/register?role=user">As User</a>
                    <a href="<%=request.getContextPath()%>/register?role=driver">As Driver</a>
                    <a href="<%=request.getContextPath()%>/register?role=admin">As Admin</a>
                </div>
            </li>
        </ul>
    </nav>
</header>

<!-- ===================== Hero Section ===================== -->
<section class="hero">
    <div class="overlay"></div>
    <div class="hero-content">
        <h1>Go Anywhere with <span>RideNow</span></h1>
        <p>Book your ride in seconds and travel with comfort, style, and safety.</p>
        <div class="cta-buttons">
            <a class="btn primary" href="<%=request.getContextPath()%>/login">
                <i class="ri-login-box-line"></i> Get Started
            </a>
            <a class="btn secondary" href="<%=request.getContextPath()%>/register?role=user">
                <i class="ri-user-add-line"></i> Sign Up
            </a>
        </div>
    </div>
</section>

<!-- ===================== Info Section ===================== -->
<section class="info">
    <div class="info-box">
        <i class="ri-taxi-line icon"></i>
        <h2>Book a Ride</h2>
        <p>Instant booking at your fingertips. Travel with verified drivers anytime, anywhere.</p>
    </div>
    <div class="info-box">
        <i class="ri-steering-line icon"></i>
        <h2>Drive & Earn</h2>
        <p>Join our driver community and earn on your schedule with fair commissions.</p>
    </div>
    <div class="info-box">
        <i class="ri-wallet-3-line icon"></i>
        <h2>Secure Payments</h2>
        <p>Enjoy seamless payments with cards, wallets, or cash — safe and transparent.</p>
    </div>
</section>

<!-- ===================== Footer ===================== -->
<footer>
    <p>© 2025 RideNow — Web-Based Transportation System | Built for the Future of Mobility</p>
</footer>

<!-- ===================== JavaScript: Dropdown ===================== -->
<script>
    const dropBtn = document.querySelector('.dropbtn');
    const dropdown = document.querySelector('.dropdown-content');
    if (dropBtn) {
        dropBtn.addEventListener('click', () => dropdown.classList.toggle('show'));
    }
    window.onclick = function(event) {
        if (!event.target.matches('.dropbtn')) {
            if (dropdown && dropdown.classList.contains('show')) dropdown.classList.remove('show');
        }
    };
</script>

</body>
</html>
