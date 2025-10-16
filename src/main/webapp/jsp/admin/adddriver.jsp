<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Add Driver - RideNow</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>

<div class="container">
  <h2>Add New Driver</h2>

  <% if (request.getAttribute("error") != null) { %>
  <p class="error-message"><%= request.getAttribute("error") %></p>
  <% } %>

  <form class="add-driver-form" action="<%=request.getContextPath()%>/addDriver" method="post">
    <label for="userId">User ID:</label>
    <input type="number" id="userId" name="userId" required>

    <label for="licenseNumber">License Number:</label>
    <input type="text" id="licenseNumber" name="licenseNumber" required>

    <label for="vehicleType">Vehicle Type:</label>
    <input type="text" id="vehicleType" name="vehicleType" required>

    <label for="status">Status:</label>
    <select id="status" name="status" required>
      <option value="Active">Active</option>
      <option value="Inactive">Inactive</option>
    </select>

    <input type="submit" value="Add Driver">
  </form>

  <div class="back-link">
    <a href="<%=request.getContextPath()%>/jsp/drivermanagement/manage.jsp">Back to Driver Management</a>
  </div>
</div>

</body>
</html>
