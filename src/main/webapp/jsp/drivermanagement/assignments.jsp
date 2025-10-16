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
<%@ page import="java.util.List, com.example.drivermanagement.model.Assignment" %>
<html>
<head>
    <title>Driver Assignments - RideNow</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>
<h2>My Assignments</h2>
<% if (request.getAttribute("error") != null) { %>
<p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>
<% List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments"); %>
<% if (assignments != null && !assignments.isEmpty()) { %>
<table border="1">
    <tr>
        <th>Assignment ID</th>
        <th>Ride Request ID</th>
        <th>Assigned Time</th>
    </tr>
    <% for (Assignment assignment : assignments) { %>
    <tr>
        <td><%= assignment.getId() %></td>
        <td><%= assignment.getRideRequestId() %></td>
        <td><%= assignment.getAssignedTime() %></td>
    </tr>
    <% } %>
</table>
<% } else { %>
<p>No assignments found.</p>
<% } %>
<nav>
    <a href="<%=request.getContextPath()%>/driverProfile">Profile</a> |
    <a href="<%=request.getContextPath()%>/driverRideRequests">View Ride Requests</a> |
    <a href="<%=request.getContextPath()%>/index.jsp">Back to Home</a>
</nav>
</body>
</html>