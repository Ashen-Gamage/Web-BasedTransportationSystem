<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String role = (String) request.getAttribute("role");
  if (role == null) role = "user"; // default
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Register - RideNow</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/register.css">
  <script>
    function showForm(selectedRole) {
      document.getElementById("driverForm").style.display = "none";
      document.getElementById("userForm").style.display = "none";
      document.getElementById("adminForm").style.display = "none";

      if (selectedRole === "driver") {
        document.getElementById("driverForm").style.display = "block";
      } else if (selectedRole === "admin") {
        document.getElementById("adminForm").style.display = "block";
      } else {
        document.getElementById("userForm").style.display = "block";
      }
    }

    window.onload = function() {
      var roleFromServlet = "<%= role %>";
      showForm(roleFromServlet);
      document.getElementById("roleSelect").value = roleFromServlet;
    }
  </script>
</head>
<body>
<div class="register-container">
  <h1>Register</h1>

  <!-- Show error if exists -->
  <%
    String error = (String) request.getAttribute("error");
    if (error != null) {
  %>
  <div style="color:red;"><%= error %></div>
  <% } %>

  <!-- Role Selection -->
  <label for="roleSelect">Choose Role:</label>
  <select id="roleSelect" onchange="showForm(this.value)">
    <option value="user">User</option>
    <option value="driver">Driver</option>
    <option value="admin">Admin</option>
  </select>

  <!-- User Form -->
  <form id="userForm" action="<%=request.getContextPath()%>/register" method="post" style="display:none;">
    <input type="hidden" name="role" value="user">
    <input type="text" name="name" placeholder="Full Name" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="text" name="phone" placeholder="Phone Number" required>
    <input type="password" name="password" placeholder="Password" required>
    <button type="submit">Register as User</button>
  </form>

  <!-- Driver Form -->
  <form id="driverForm" action="<%=request.getContextPath()%>/register" method="post" style="display:none;">
    <input type="hidden" name="role" value="driver">
    <input type="text" name="name" placeholder="Full Name" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="text" name="phone" placeholder="Phone Number" required>
    <input type="text" name="licenseNumber" placeholder="Driver License No." required>
    <input type="text" name="vehicleType" placeholder="Vehicle Details" required>
    <input type="password" name="password" placeholder="Password" required>
    <button type="submit">Register as Driver</button>
  </form>

  <!-- Admin Form -->
  <form id="adminForm" action="<%=request.getContextPath()%>/register" method="post" style="display:none;">
    <input type="hidden" name="role" value="admin">
    <input type="text" name="name" placeholder="Admin Full Name" required>
    <input type="email" name="email" placeholder="Admin Email" required>
    <input type="text" name="phone" placeholder="Phone Number" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="text" name="adminCode" placeholder="Admin Authorization Code" required>
    <button type="submit">Register as Admin</button>
  </form>

</div>
</body>
</html>
