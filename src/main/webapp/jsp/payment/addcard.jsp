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
<html>
<head>
    <title>Add Payment Card - RideNow</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    <script>
        function validateCardNumber() {
            const cardNumber = document.getElementById("card_number").value;
            if (!/^\d{16}$/.test(cardNumber)) {
                alert("Card number must be exactly 16 digits");
                return false;
            }
            return true;
        }

        function validateExpiryDate() {
            const expiryDate = document.getElementById("expiry_date").value;
            const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;
            if (!regex.test(expiryDate)) {
                alert("Expiry date must be in MM/YY format");
                return false;
            }
            const [month, year] = expiryDate.split("/");
            const expiry = new Date(`20${year}`, month - 1);
            const now = new Date();
            if (expiry < now) {
                alert("Card has expired");
                return false;
            }
            return true;
        }

        function validateForm() {
            return validateCardNumber() && validateExpiryDate();
        }
    </script>
</head>
<body>
<h2>Add Payment Card</h2>
<% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<form action="<%=request.getContextPath()%>/addPaymentCard" method="post" onsubmit="return validateForm()">
    <label for="card_number">Card Number:</label>
    <input type="text" id="card_number" name="card_number" required><br><br>
    <label for="expiry_date">Expiry Date (MM/YY):</label>
    <input type="text" id="expiry_date" name="expiry_date" placeholder="MM/YY" required><br><br>
    <label for="cardholder_name">Cardholder Name:</label>
    <input type="text" id="cardholder_name" name="cardholder_name" required><br><br>
    <input type="submit" value="Add Card">
</form>
<a href="<%=request.getContextPath()%>/viewPaymentCards">View Cards</a>
</body>
</html>