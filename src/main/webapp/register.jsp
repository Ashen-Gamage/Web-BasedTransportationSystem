<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  // Get role from servlet (if user clicked from navbar dropdown)
  String role = (String) request.getAttribute("role");
  if (role == null) {
    role = ""; // default if nothing selected
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Register - RideWithUs</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/register.css">
  <script>
    // Show form based on role dynamically
    function showForm(selectedRole) {
      document.getElementById("driverForm").style.display = "none";
      document.getElementById("customerForm").style.display = "none";
      document.getElementById("adminForm").style.display = "none";

      if (selectedRole === "driver") {
        document.getElementById("driverForm").style.display = "block";
      } else if (selectedRole === "customer") {
        document.getElementById("customerForm").style.display = "block";
      } else if (selectedRole === "admin") {
        document.getElementById("adminForm").style.display = "block";
      }
    }

    // Run on page load if role comes from servlet
    window.onload = function() {
      var roleFromServlet = "<%= role %>";
      if(roleFromServlet) {
        document.getElementById("roleSelect").value = roleFromServlet;
        showForm(roleFromServlet);
      }
    }
  </script>
</head>
<body>
<div class="register-container">
  <h1>Register</h1>

  <!-- Role Selection Dropdown -->
  <label for="roleSelect">Choose your role:</label>
  <select id="roleSelect" name="role" onchange="showForm(this.value)">
    <option value="">-- Select --</option>
    <option value="driver">Driver</option>
    <option value="customer">Customer</option>
    <option value="admin">Admin</option>
  </select>

  <!-- Driver Form -->
  <form id="driverForm" action="<%=request.getContextPath()%>/RegisterServlet" method="post" style="display:none;">
    <input type="hidden" name="role" value="driver">
    <input type="text" name="name" placeholder="Full Name" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="text" name="license" placeholder="Driver License No." required>
    <input type="text" name="vehicle" placeholder="Vehicle Details" required>
    <button type="submit">Register as Driver</button>
  </form>

  <!-- Customer Form -->
  <form id="customerForm" action="<%=request.getContextPath()%>/RegisterServlet" method="post" style="display:none;">
    <input type="hidden" name="role" value="customer">
    <input type="text" name="name" placeholder="Full Name" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="text" name="phone" placeholder="Phone Number" required>
    <button type="submit">Register as Customer</button>
  </form>

  <!-- Admin Form -->
  <form id="adminForm" action="<%=request.getContextPath()%>/RegisterServlet" method="post" style="display:none;">
    <input type="hidden" name="role" value="admin">
    <input type="text" name="username" placeholder="Admin Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="text" name="securityCode" placeholder="Security Code" required>
    <button type="submit">Register as Admin</button>
  </form>
</div>
</body>
</html>
