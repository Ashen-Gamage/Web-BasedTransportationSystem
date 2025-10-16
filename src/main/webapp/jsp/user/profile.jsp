<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.example.user.model.User" %>
<%
    User user = (User) request.getAttribute("user");
    if (user == null) {
        user = (User) session.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/user/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
    <style>
        .container { max-width:700px; margin:2rem auto; padding:1.25rem; border:1px solid #e0e0e0; border-radius:6px; }
        label { display:block; margin-top:0.75rem; font-weight:600; }
        input[type="text"], input[type="email"] { width:100%; padding:0.5rem; box-sizing:border-box; }
        .actions { margin-top:1rem; display:flex; gap:0.5rem; }
        .btn { padding:0.45rem 0.8rem; border:none; border-radius:4px; cursor:pointer; }
        .btn-primary { background:#007bff; color:#fff; }
        .btn-danger { background:#dc3545; color:#fff; }
        .message { margin-bottom:0.75rem; padding:0.5rem; border-radius:4px; }
        .message.success { background:#e6ffed; color:#046a0b; border:1px solid #b7f0c5; }
        .message.error { background:#ffecec; color:#a60000; border:1px solid #ffbcbc; }
        .note { font-size:0.9rem; color:#555; margin-top:0.25rem; }
    </style>
    <script>
        function confirmDelete() {
            return confirm('Delete your profile? This action is irreversible.');
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Profile</h2>

    <% if (request.getAttribute("message") != null) { %>
    <div class="message success"><%= request.getAttribute("message") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
    <div class="message error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/profile" method="post" novalidate>
        <label for="username">Username</label>
        <input id="username" name="username" type="text" maxlength="100" required
               value="<%= user.getUsername() != null ? user.getUsername() : "" %>">

        <label for="email">Email</label>
        <input id="email" name="email" type="email" value="<%= user.getEmail() != null ? user.getEmail() : "" %>" disabled>

        <label for="phone">Phone</label>
        <input id="phone" name="phone" type="text" pattern="07[0-9]{8}" maxlength="10" inputmode="numeric"
               title="Sri Lanka mobile format: 07XXXXXXXX"
               value="<%= user.getPhone() != null ? user.getPhone() : "" %>">
        <div class="note">Sri Lanka mobile numbers should be 10 digits and start with <code>07</code>. Leave blank to remove.</div>

        <label for="address_line">Address</label>
        <input id="address_line" name="address_line" type="text" maxlength="255"
               value="<%= user.getAddressLine() != null ? user.getAddressLine() : "" %>">

        <label for="city">City</label>
        <input id="city" name="city" type="text" maxlength="100"
               value="<%= user.getCity() != null ? user.getCity() : "" %>">

        <label for="postal_code">Postal Code</label>
        <input id="postal_code" name="postal_code" type="text" maxlength="20"
               value="<%= user.getPostalCode() != null ? user.getPostalCode() : "" %>">

        <div class="actions">
            <button type="submit" class="btn btn-primary">Update Profile</button>
            <a class="btn" href="<%= request.getContextPath() %>/jsp/user/userDashboard.jsp" style="background:#6c757d;color:#fff;text-decoration:none;padding:0.45rem 0.8rem;border-radius:4px;">Back</a>
        </div>
    </form>

    <form action="<%= request.getContextPath() %>/profile" method="post" onsubmit="return confirmDelete();" style="margin-top:1rem;">
        <input type="hidden" name="action" value="delete">
        <button type="submit" class="btn btn-danger">Delete Profile</button>
    </form>

</div>
</body>
</html>
