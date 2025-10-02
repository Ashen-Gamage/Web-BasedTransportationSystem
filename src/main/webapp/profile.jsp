<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Prevent caching
    response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader("Expires",0);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Profile</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/profile.css">
</head>
<body>

<div class="profile-container">
    <aside class="sidebar">
        <div class="brand">
            <div class="logo"></div>
            <div class="name">Profile</div>
        </div>
        <ul>
            <li class="active">
                <a href="<%=request.getContextPath()%>/profile">
                    <span class="dot"></span><span class="label">Overview</span>
                </a>
            </li>
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
            <li>
                <a href="<%=request.getContextPath()%>/logout">
                    <span class="dot"></span><span class="label">Logout</span>
                </a>
            </li>
        </ul>
    </aside>

    <main class="profile-content">
        <div class="card header-card">
            <div class="header-left">
                <div class="avatar-wrap">
                    <img class="avatar" src="<%=request.getContextPath()%>/assets/images/default-avatar.png" alt="avatar"
                         onerror="this.src='<%=request.getContextPath()%>/assets/img/default-avatar.png'">
                </div>
                <div>
                    <h1 class="title">${user.name}</h1>
                    <div class="subtitle">
                        <span class="muted">Role:</span>
                        <c:choose>
                            <c:when test="${user.role == 'ADMIN'}"><span class="badge admin">ADMIN</span></c:when>
                            <c:when test="${user.role == 'DRIVER'}"><span class="badge driver">DRIVER</span></c:when>
                            <c:otherwise><span class="badge user"><c:out value="${user.role}"/></span></c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div>
                <a class="btn primary" href="<%=request.getContextPath()%>/messages/new?to=${user.id}">Send Message</a>
                <a class="btn" href="<%=request.getContextPath()%>/profile">Contacts</a>
            </div>
        </div>

        <div class="grid two">
            <div class="card">
                <div class="section-title">Contact Information</div>
                <div class="kv"><span>Phone</span><span>${user.phone}</span></div>
                <div class="kv"><span>Email</span><span>${user.email}</span></div>

                <div class="section-title mt16">Basic Information</div>
                <div class="kv"><span>User ID</span><span>${user.id}</span></div>
                <div class="kv"><span>Name</span><span>${user.name}</span></div>
                <div class="kv"><span>Role</span><span>${user.role}</span></div>
            </div>

            <div class="card info-card">
                <div class="section-title">Role Panel</div>
                <c:choose>
                    <c:when test="${user.role == 'ADMIN'}">
                        <div class="group-title">Admin Overview</div>
                        <p class="muted">You are signed in as <strong>ADMIN</strong>. Use your admin dashboard for system controls.</p>
                    </c:when>
                    <c:when test="${user.role == 'DRIVER'}">
                        <div class="group-title">Driver Overview</div>
                        <p class="muted">You are signed in as <strong>DRIVER</strong>. Vehicle/license details can be added later.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="group-title">User Overview</div>
                        <p class="muted">You are signed in as a regular user.</p>
                    </c:otherwise>
                </c:choose>
                <div class="kv"><span>Name</span><span>${user.name}</span></div>
                <div class="kv"><span>Email</span><span>${user.email}</span></div>
                <div class="kv"><span>Phone</span><span>${user.phone}</span></div>
            </div>
        </div>
    </main>
</div>

</body>
</html>
