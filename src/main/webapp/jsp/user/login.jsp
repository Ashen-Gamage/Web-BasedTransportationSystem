<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Login</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
</head>
<body>

<div class="login-container">
    <h2>User Login</h2>

    <form action="<%=request.getContextPath()%>/login" method="post">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <input type="submit" value="Login">
    </form>

    <% if (request.getAttribute("error") != null) { %>
    <p class="error-message"><%= request.getAttribute("error") %></p>
    <% } %>

    <p style="text-align:center; margin-top:1rem;">
        Don't have an account? <a href="<%=request.getContextPath()%>/jsp/user/register.jsp">Register here</a>
    </p>
</div>

</body>
</html>
