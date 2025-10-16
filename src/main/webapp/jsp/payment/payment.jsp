<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 40px; background:#f6f7fb; }
        .card { background: white; padding: 24px; border-radius: 12px; box-shadow: 0 6px 18px rgba(0,0,0,0.06); max-width:480px; margin: auto; text-align:center; }
        h1 { margin-top:0; }
        .btn { display:inline-block; padding:10px 18px; margin:10px; border-radius:8px; text-decoration:none; border: none; cursor:pointer; font-weight:600; }
        .btn-primary { background:#2563eb; color:white; }
        .btn-secondary { background:#e6e9f2; color:#111827; }
    </style>
</head>
<body>
<div class="card">
    <h1>Payment Methods</h1>
    <p>Manage your saved payment cards.</p>

    <!-- Buttons linking to Add / View pages -->
    <a href="<%=request.getContextPath()%>/jsp/payment/addcard.jsp" class="btn btn-primary">Add Card</a>
    <a href="<%=request.getContextPath()%>/viewPaymentCards" class="btn btn-secondary">View Cards</a>

</div>
</body>
</html>
