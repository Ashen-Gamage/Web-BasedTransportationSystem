<script type="text/javascript">
        var gk_isXlsx = false;
        var gk_xlsxFileLookup = {};
        var gk_fileData = {};
        function filledCell(cell) {
          return cell !== '' && cell != null;
        }
        function loadFileData(filename) {
        if (gk_isXlsx && gk_xlsxFileLookup[filename]) {
            try {
                var workbook = XLSX.read(gk_fileData[filename], { type: 'base64' });
                var firstSheetName = workbook.SheetNames[0];
                var worksheet = workbook.Sheets[firstSheetName];

                // Convert sheet to JSON to filter blank rows
                var jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1, blankrows: false, defval: '' });
                // Filter out blank rows (rows where all cells are empty, null, or undefined)
                var filteredData = jsonData.filter(row => row.some(filledCell));

                // Heuristic to find the header row by ignoring rows with fewer filled cells than the next row
                var headerRowIndex = filteredData.findIndex((row, index) =>
                  row.filter(filledCell).length >= filteredData[index + 1]?.filter(filledCell).length
                );
                // Fallback
                if (headerRowIndex === -1 || headerRowIndex > 25) {
                  headerRowIndex = 0;
                }

                // Convert filtered JSON back to CSV
                var csv = XLSX.utils.aoa_to_sheet(filteredData.slice(headerRowIndex)); // Create a new sheet from filtered array of arrays
                csv = XLSX.utils.sheet_to_csv(csv, { header: 1 });
                return csv;
            } catch (e) {
                console.error(e);
                return "";
            }
        }
        return gk_fileData[filename] || "";
        }
        </script><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.example.payment.model.PaymentCard" %>
<html>
<head>
    <title>My Payment Cards - RideNow</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>
<h2>My Payment Cards</h2>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<% List<PaymentCard> cards = (List<PaymentCard>) request.getAttribute("cards"); %>
<% if (cards != null && !cards.isEmpty()) { %>
    <table border="1">
        <tr>
            <th>Card Number</th>
            <th>Expiry Date</th>
            <th>Cardholder Name</th>
            <th>Added On</th>
        </tr>
        <% for (PaymentCard card : cards) { %>
            <tr>
                <td><%= card.getCardNumber().substring(12) %> <!-- Show last 4 digits for security --></td>
                <td><%= card.getExpiryDate() %></td>
                <td><%= card.getCardholderName() %></td>
                <td><%= card.getCreatedAt() %></td>
            </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No payment cards added.</p>
<% } %>
<a href="<%=request.getContextPath()%>/addPaymentCard">Add New Card</a>
<a href="<%=request.getContextPath()%>/index.jsp">Back to Home</a>
</body>
</html>