<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RideNow Dashboard - Completed Rides & Ratings</title>
    <style>
        body { font-family:'Segoe UI', sans-serif; background:#f0f2f5; margin:0; padding:0; }
        .container { width:90%; margin:20px auto; }
        h2 { text-align:center; color:#333; }

        .completed-rides { display:flex; flex-wrap:wrap; gap:20px; justify-content:center; margin-top:20px; }
        .ride-card { background:#fff; padding:15px; border-radius:12px; box-shadow:0 4px 8px rgba(0,0,0,0.1); width:220px; text-align:center; }
        .ride-card button { margin:5px 0; background:#007bff; color:#fff; border:none; padding:6px 12px; border-radius:6px; cursor:pointer; }
        .ride-card button:hover { background:#0056b3; }

        .refresh-button { position:fixed; bottom:20px; right:20px; background:#28a745; color:white; border:none; padding:12px 18px; border-radius:50px; cursor:pointer; box-shadow:0 4px 6px rgba(0,0,0,0.2); }
        .refresh-button:hover { background:#1e7e34; }

        .dashboard-buttons { text-align:center; margin-top:20px; }
        .dashboard-buttons button { margin:5px; padding:10px 15px; border:none; border-radius:6px; cursor:pointer; background:#17a2b8; color:#fff; }
        .dashboard-buttons button:hover { background:#117a8b; }

        .overlay { display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:900; }
        .overlay.active { display:block; }
        .rating-popup { display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:#fff; padding:25px; border-radius:15px; box-shadow:0 8px 16px rgba(0,0,0,0.3); z-index:1000; width:400px; }
        .rating-popup.active { display:block; }
        .stars { display:inline-block; margin-bottom:10px; }
        .star { font-size:30px; cursor:pointer; color:#ccc; }
        .star.selected { color:#ffcc00; }
    </style>
    <script>
        function refreshCompletedRides() {
            window.location.href = "<%=request.getContextPath()%>/completedRides";
        }

        function selectStar(rating) {
            var stars = document.querySelectorAll('.star');
            for(var i=0;i<stars.length;i++){
                stars[i].classList.toggle('selected', i<rating);
            }
            document.getElementById('rating').value = rating;
        }

        function showRatingPopup(rideId) {
            document.getElementById('rideId').value = rideId;
            document.getElementById('selectedRideId').innerText = rideId;
            document.getElementById('overlay').classList.add('active');
            document.getElementById('ratingPopup').classList.add('active');
        }

        function closeRatingPopup() {
            document.getElementById('overlay').classList.remove('active');
            document.getElementById('ratingPopup').classList.remove('active');
        }

        function showDriverFeedback(driverId) {
            window.open('<%=request.getContextPath()%>/viewFeedback?driverId=' + driverId, '_blank');
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Completed Rides</h2>

    <div class="dashboard-buttons">
        <button onclick="refreshCompletedRides()">⟳ Refresh</button>
    </div>

    <div class="completed-rides">
        <%
            List<Integer> completedRidesList = (List<Integer>) request.getAttribute("completedRides");
            Integer driverId = (Integer) request.getAttribute("driverId");
            if(completedRidesList != null && !completedRidesList.isEmpty()){
                for(Integer rideId : completedRidesList){
        %>
        <div class="ride-card">
            <p><strong>Ride ID: <%= rideId %></strong></p>
            <button type="button" onclick="showRatingPopup(<%= rideId %>)">Rate this ride</button>
            <button type="button" onclick="showDriverFeedback(<%= driverId %>)">View Driver Feedback</button>
        </div>
        <%
            }
        } else {
        %>
        <p>No completed rides found.</p>
        <%
            }
        %>
    </div>
</div>

<button class="refresh-button" onclick="refreshCompletedRides()">⟳ Refresh</button>

<!-- Rating popup -->
<div id="overlay" class="overlay" onclick="closeRatingPopup()"></div>
<div id="ratingPopup" class="rating-popup">
    <h3>Rate Your Ride</h3>
    <p>Ride ID: <span id="selectedRideId">None</span></p>
    <form action="<%=request.getContextPath()%>/submitRating" method="post">
        <input type="hidden" name="rideId" id="rideId" value="">
        <div class="stars">
            <span class="star" onclick="selectStar(1)">★</span>
            <span class="star" onclick="selectStar(2)">★</span>
            <span class="star" onclick="selectStar(3)">★</span>
            <span class="star" onclick="selectStar(4)">★</span>
            <span class="star" onclick="selectStar(5)">★</span>
        </div>
        <input type="hidden" id="rating" name="rating" value="0">
        <br>
        <label for="feedback">Feedback:</label>
        <textarea id="feedback" name="feedback" rows="3"></textarea><br><br>
        <input type="submit" value="Submit Rating">
        <input type="button" value="Cancel" onclick="closeRatingPopup()">
    </form>
</div>
</body>
</html>
