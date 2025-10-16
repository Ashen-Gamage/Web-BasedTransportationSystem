<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:out value="${pageTitle != null ? pageTitle : 'WebBase Transportation System'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<h1>Welcome to WebBase Transportation System</h1>

<c:choose>
<c:when test="${not empty sessionScope.user}">
<p>Hello, ${sessionScope.user.username}! Role: ${sessionScope.user.role}</p>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
</c:when>
</c:choose>
<hr>
