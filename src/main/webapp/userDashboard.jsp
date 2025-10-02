<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Prevent caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Session check
    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");

    if (userName == null || userEmail == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>User Dashboard</title>

    <!-- Leaflet CSS & JS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>

    <!-- Axios for reverse geocoding -->
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>

    <style>
        /* Reset */
        * { box-sizing:border-box; margin:0; padding:0; }
        html, body { height:100%; font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#0b0c0f; color:#eef2f7; line-height:1.5; }

        /* Container */
        .dashboard-container { max-width:1200px; margin:30px auto; padding:0 20px; }
        .dashboard-container h1 { font-size:28px; margin-bottom:6px; }

        /* Quick Links */
        .dashboard-links { display:flex; flex-wrap:wrap; gap:10px; padding:12px; background:#1f1f2e; border-radius:12px; box-shadow:0 2px 6px rgba(0,0,0,0.2); }
        .dashboard-links a { text-decoration:none; color:#fff; background:#2563eb; padding:8px 14px; border-radius:999px; transition:transform .1s ease, filter .2s ease; }
        .dashboard-links a:hover { transform:translateY(-2px); filter:brightness(1.1); }

        /* Sections */
        .section { background:#1f1f2e; border-radius:12px; padding:16px; margin-top:16px; }
        .section h2 { color:#2563eb; font-size:20px; margin-bottom:12px; }

        /* Form */
        .ride-form .form-group { margin-bottom:10px; }
        .ride-form label { display:block; margin-bottom:5px; }
        .ride-form input { width:100%; padding:8px; border-radius:8px; border:1px solid #2a2a3c; background:#1f1f2e; color:#eef2f7; }
        .btn { background:#2563eb; color:#fff; border:none; padding:8px 14px; border-radius:8px; cursor:pointer; font-weight:600; margin-right:5px; }
        .btn:hover { filter:brightness(1.1); }

        /* Table */
        table { width:100%; border-collapse:collapse; margin-top:12px; border-radius:12px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.3); }
        thead th { text-align:left; font-size:13px; font-weight:700; padding:12px; background:#2a2a3c; color:#9aa4b2; }
        tbody td { padding:12px; border-bottom:1px solid #2a2a3c; color:#eef2f7; }
        tbody tr:hover { background:#262636; }

        /* Status pills */
        .status { display:inline-block; padding:4px 10px; border-radius:999px; font-size:12px; font-weight:bold; text-transform:uppercase; }
        .status.completed { background:#bbf7d0; color:#14532d; }

        /* Map modal */
        .map-modal { display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.6); justify-content:center; align-items:center; z-index:9999; }
        .map-modal-content { background:#fff; padding:15px; width:80%; height:80%; border-radius:10px; display:flex; flex-direction:column; }
        .close-btn { font-size:22px; cursor:pointer; align-self:flex-end; }
        #map { flex:1; border-radius:8px; min-height:300px; }

        /* Responsive table */
        @media (max-width:900px) {
            thead { display:none; }
            table, tbody, tr, td { display:block; width:100%; }
            tbody tr { margin:10px 0; border:1px solid #2a2a3c; border-radius:10px; padding:10px; }
            tbody td { border:none; display:grid; grid-template-columns:120px 1fr; gap:6px; padding:10px; }
            tbody td::before { content:attr(data-label); font-weight:700; color:#9aa4b2; text-transform:uppercase; font-size:11px; }
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h1>Welcome, <%= (userName != null ? userName : userEmail) %>!</h1>

    <!-- Quick links -->
    <div class="dashboard-links">
        <a href="<%=request.getContextPath()%>/profile.jsp">Profile</a>
        <a href="#rides-section">My Rides</a>
        <a href="<%=request.getContextPath()%>/settings.jsp">Settings</a>
        <a href="<%=request.getContextPath()%>/logout">Logout</a>
    </div>

    <!-- My Rides Section -->
    <div class="section" id="rides-section">
        <h2>Book a Ride</h2>
        <form action="<%=request.getContextPath()%>/RideServlet" method="post" class="ride-form">
            <div class="form-group">
                <label for="pickup">Pickup Location:</label>
                <input type="text" id="pickup" name="pickup" readonly required>
            </div>
            <div class="form-group">
                <label for="dropoff">Drop-off Location:</label>
                <input type="text" id="dropoff" name="dropoff" readonly required>
            </div>
            <button type="button" class="btn" onclick="openMap('pickup')">Select Pickup on Map</button>
            <button type="button" class="btn" onclick="openMap('dropoff')">Select Drop-off on Map</button>
            <button type="submit" class="btn">Request Ride</button>
        </form>
    </div>

    <!-- My Ride History Section -->
    <div class="section">
        <h2>My Ride History</h2>
        <table>
            <thead>
            <tr>
                <th>Pickup</th>
                <th>Drop-off</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td data-label="Pickup">Colombo</td>
                <td data-label="Drop-off">Kandy</td>
                <td data-label="Status"><span class="status completed">Completed</span></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<!-- Map Modal -->
<div id="mapModal" class="map-modal">
    <div class="map-modal-content">
        <span class="close-btn" onclick="closeMap()">&times;</span>
        <h3 id="mapTitle">Select Location</h3>
        <div id="map"></div>
    </div>
</div>

<script>
    let currentField = null;
    let map, marker;

    function openMap(field) {
        currentField = field;
        document.getElementById("mapModal").style.display = "flex";
        document.getElementById("mapTitle").innerText =
            field === "pickup" ? "Select Pickup Location" : "Select Drop-off Location";

        if (!map) {
            map = L.map('map').setView([7.8731, 80.7718], 7); // Sri Lanka center
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap'
            }).addTo(map);

            map.on('click', function(e) {
                if (marker) map.removeLayer(marker);
                marker = L.marker(e.latlng).addTo(map);

                axios.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${e.latlng.lat}&lon=${e.latlng.lng}`)
                    .then(response => {
                        let place = response.data.display_name || (e.latlng.lat.toFixed(5)+", "+e.latlng.lng.toFixed(5));
                        document.getElementById(currentField).value = place;
                        closeMap();
                    })
                    .catch(() => {
                        document.getElementById(currentField).value = e.latlng.lat.toFixed(5)+", "+e.latlng.lng.toFixed(5);
                        closeMap();
                    });
            });
        }

        setTimeout(() => {
            map.invalidateSize();
            map.setView([7.8731, 80.7718], 7);
        }, 200);
    }

    function closeMap() {
        document.getElementById("mapModal").style.display = "none";
    }
</script>
</body>
</html>
