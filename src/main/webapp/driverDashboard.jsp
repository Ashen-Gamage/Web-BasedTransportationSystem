<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*, com.example.model.Ride" %>

<%
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
  response.setHeader("Pragma", "no-cache");
  response.setDateHeader("Expires", 0);

  String userRole = (String) session.getAttribute("userRole");
  String userName = (String) session.getAttribute("userName");
  Integer userId = (Integer) session.getAttribute("userId");

  if (!"DRIVER".equalsIgnoreCase(userRole) || userName == null || userId == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }

  List<Ride> assignedRides = (List<Ride>) request.getAttribute("assignedRides");
  String flash = (String) request.getAttribute("flash");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Driver Dashboard</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/dashboard.css">
</head>
<body>
<div class="dashboard-container">
  <h1>Welcome, <%= userName %>!</h1>
  <p>Role: Driver</p>

  <div class="dashboard-links">
    <a href="<%=request.getContextPath()%>/profile">Profile</a>
    <a href="<%=request.getContextPath()%>/driver?action=dashboard">Refresh Assigned Rides</a>
    <a href="<%=request.getContextPath()%>/rideTracking">Track Rides</a>
    <a href="<%=request.getContextPath()%>/payment.jsp">payment</a>
    <a href="<%=request.getContextPath()%>/settings">Settings</a>
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
  </div>

  <hr/>

  <c:if test="${not empty flash}">
    <p style="color:green;">${flash}</p>
  </c:if>

  <c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
  </c:if>

  <h2>Assigned Rides</h2>
  <c:if test="${not empty assignedRides}">
    <table border="1" cellpadding="5">
      <tr>
        <th>Ride ID</th>
        <th>Pickup</th>
        <th>Dropoff</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      <c:forEach var="ride" items="${assignedRides}">
        <tr>
          <td>${ride.id}</td>
          <td>${ride.pickup}</td>
          <td>${ride.dropoff}</td>
          <td>${ride.status}</td>
          <td>
            <c:choose>
              <c:when test="${ride.status == 'ASSIGNED'}">
                <form action="${pageContext.request.contextPath}/driver?action=acceptRide" method="post">
                  <input type="hidden" name="rideId" value="${ride.id}">
                  <button type="submit">Accept</button>
                </form>
              </c:when>
              <c:when test="${ride.status == 'ACCEPTED'}">
                <form action="${pageContext.request.contextPath}/driver?action=updateStatus" method="post">
                  <input type="hidden" name="rideId" value="${ride.id}">
                  <select name="status">
                    <option value="ON_THE_WAY">On the Way</option>
                    <option value="IN_RIDE">In Ride</option>
                    <option value="COMPLETED">Completed</option>
                  </select>
                  <button type="submit">Update</button>
                </form>
              </c:when>
              <c:otherwise>
                <span>N/A</span>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
  <c:if test="${empty assignedRides}">
    <p>No assigned rides yet.</p>
  </c:if>
</div>
</body>
</html>
