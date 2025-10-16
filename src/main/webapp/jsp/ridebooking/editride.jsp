<%@ page import="com.example.ridebooking.model.RideRequest, java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>
<%
    RideRequest ride = (RideRequest) request.getAttribute("ride");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    // If ride exists, keep original request time; otherwise, use current time
    String requestTimeVal = ride != null && ride.getRequestTime() != null
            ? ride.getRequestTime().format(dtf)
            : LocalDateTime.now().format(dtf);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Ride</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <style>
        .form-container { max-width:600px; margin:2rem auto; padding:1rem; border:1px solid #ddd; border-radius:6px; }
        .form-row { margin-bottom:0.75rem; }
        label { display:block; margin-bottom:0.25rem; font-weight:600; }
        input[type="text"] { width:100%; padding:0.5rem; box-sizing:border-box; }
        .actions { display:flex; gap:0.5rem; margin-top:1rem; }
        .btn-primary { background:#007bff;color:#fff;padding:0.45rem 0.8rem;border:none;border-radius:4px;text-decoration:none; cursor:pointer; }
        .btn-secondary { background:#6c757d;color:#fff;padding:0.45rem 0.8rem;border:none;border-radius:4px;text-decoration:none; cursor:pointer; }
        .error { color:#c00; margin-bottom:1rem; }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Edit Ride Request</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="<%=request.getContextPath()%>/editRide">
        <input type="hidden" name="id" value="<%= ride != null ? ride.getId() : "" %>">
        <input type="hidden" name="request_time" value="<%= requestTimeVal %>">

        <div class="form-row">
            <label for="pickup">Pickup Location</label>
            <input id="pickup" name="pickup" type="text" maxlength="100" required
                   value="<%= ride != null && ride.getPickupLocation() != null ? ride.getPickupLocation() : "" %>">
        </div>

        <div class="form-row">
            <label for="dropoff">Dropoff Location</label>
            <input id="dropoff" name="dropoff" type="text" maxlength="100" required
                   value="<%= ride != null && ride.getDropoffLocation() != null ? ride.getDropoffLocation() : "" %>">
        </div>

        <div class="actions">
            <button type="submit" class="btn-primary">Save</button>
            <a class="btn-secondary" href="<%=request.getContextPath()%>/bookRide">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
