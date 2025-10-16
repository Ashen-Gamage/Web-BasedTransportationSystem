<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Add Payment Card - RideNow</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">

    <!-- Optional XLSX helper (fixed syntax) -->
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
                    var filteredData = jsonData.filter(function(row) {
                        return row.some(filledCell);
                    });

                    // Heuristic to find the header row by ignoring rows with fewer filled cells than the next row
                    var headerRowIndex = filteredData.findIndex(function(row, index) {
                        return row.filter(filledCell).length >= (filteredData[index + 1] ? filteredData[index + 1].filter(filledCell).length : 0);
                    });

                    // Fallback
                    if (headerRowIndex === -1 || headerRowIndex > 25) {
                        headerRowIndex = 0;
                    }

                    // Convert filtered JSON back to CSV
                    var sheet = XLSX.utils.aoa_to_sheet(filteredData.slice(headerRowIndex)); // Create a new sheet from filtered array of arrays
                    var csv = XLSX.utils.sheet_to_csv(sheet, { header: 1 });
                    return csv;
                } catch (e) {
                    console.error(e);
                    return "";
                }
            }
            return gk_fileData[filename] || "";
        }
    </script>

    <style>
        body{ font-family: Arial, sans-serif; background:#f6f7fb; padding:24px; }
        .wrap{ max-width:640px; margin:auto; background:white; padding:20px; border-radius:10px; box-shadow:0 6px 18px rgba(0,0,0,0.06); }
        label{ display:block; margin-top:12px; font-weight:600; }
        input[type=text]{ width:100%; padding:10px; margin-top:6px; border-radius:6px; border:1px solid #d1d5db; box-sizing:border-box; }
        .actions{ margin-top:16px; text-align:right; }
        .btn{ padding:10px 14px; border-radius:8px; border:none; cursor:pointer; font-weight:600; }
        .btn-primary{ background:#2563eb; color:white; }
        .error{ color:#ef4444; margin-top:8px; font-size:0.95rem; }
        .note{ font-size:0.9rem; color:#6b7280; margin-top:10px; }
    </style>

    <script>
        // Normalize input: remove spaces and dashes
        function normalizeCardNumber(num) {
            return (num || "").replace(/[\s-]/g, "");
        }

        // Luhn algorithm
        function passesLuhn(number) {
            number = number.replace(/\D/g,'');
            var sum = 0;
            var alt = false;
            for (var i = number.length - 1; i >= 0; i--) {
                var n = parseInt(number.charAt(i), 10);
                if (alt) {
                    n *= 2;
                    if (n > 9) n -= 9;
                }
                sum += n;
                alt = !alt;
            }
            return (sum % 10) === 0;
        }

        function showError(id, msg) {
            document.getElementById(id).textContent = msg || "";
        }

        function validateCardNumber() {
            var raw = document.getElementById("card_number").value || "";
            var num = normalizeCardNumber(raw);
            // Accept common card lengths 13..19
            if (!/^\d{13,19}$/.test(num)) {
                showError("card_error", "Card number must be 13 to 19 digits (spaces/dashes allowed).");
                return false;
            }
            if (!passesLuhn(num)) {
                showError("card_error", "Card number failed validation (Luhn). Check the number.");
                return false;
            }
            showError("card_error", "");
            // store normalized number in hidden input so server receives digits-only
            document.getElementById("card_number_norm").value = num;
            return true;
        }

        function validateExpiryDate() {
            var expiry = (document.getElementById("expiry_date").value || "").trim();
            var regex = /^(0[1-9]|1[0-2])\/\d{2}$/;
            if (!regex.test(expiry)) {
                showError("expiry_error", "Expiry date must be in MM/YY format.");
                return false;
            }
            var parts = expiry.split("/");
            var month = parseInt(parts[0], 10); // 1..12
            var year = 2000 + parseInt(parts[1], 10);

            // expiryEnd = last millisecond of the expiry month
            // new Date(year, month, 0) => last day of month (because day 0 gives last day of previous month if month is next month),
            // so using month as-is yields next month, day 0 => last day of expiry month
            var expiryEnd = new Date(year, month, 0, 23, 59, 59, 999);
            var now = new Date();

            if (expiryEnd < now) {
                showError("expiry_error", "Card has expired.");
                return false;
            }
            showError("expiry_error", "");
            return true;
        }

        function validateForm() {
            // run both and return combined result
            var a = validateCardNumber();
            var b = validateExpiryDate();
            return a && b;
        }

        // Optional: format card input as user types (group by 4)
        function formatCardInput(e) {
            var el = e.target;
            var val = normalizeCardNumber(el.value);
            val = val.replace(/(.{4})/g, '$1 ').trim();
            el.value = val;
        }

        window.addEventListener('DOMContentLoaded', function() {
            var cn = document.getElementById("card_number");
            if (cn) {
                cn.addEventListener('input', formatCardInput);
            }
        });
    </script>
</head>
<body>
<div class="wrap">
    <h2>Add Payment Card</h2>

    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="<%=request.getContextPath()%>/addPaymentCard" method="post" onsubmit="return validateForm();">
        <label for="card_number">Card Number:</label>
        <!-- visible field allows spaces/dashes for readability -->
        <input type="text" id="card_number" name="card_number_display" inputmode="numeric" maxlength="23" placeholder="4111 1111 1111 1111" required>
        <!-- hidden normalized numeric-only value sent to server -->
        <input type="hidden" id="card_number_norm" name="card_number">

        <div id="card_error" class="error" aria-live="polite"></div>

        <label for="expiry_date">Expiry Date (MM/YY):</label>
        <input type="text" id="expiry_date" name="expiry_date" placeholder="MM/YY" required maxlength="5">
        <div id="expiry_error" class="error" aria-live="polite"></div>

        <label for="cardholder_name">Cardholder Name:</label>
        <input type="text" id="cardholder_name" name="cardholder_name" required>

        <div class="note">
            Demo only: CVV should never be stored. In production use a payment gateway and store tokens only.
        </div>

        <div class="actions">
            <a href="<%=request.getContextPath()%>/viewPaymentCards" class="btn">Cancel</a>
            <button type="submit" class="btn btn-primary">Add Card</button>
        </div>
    </form>
</div>
</body>
</html>
