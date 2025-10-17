<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.rating.model.Rating" %>

<%
    List<Rating> ratings = (List<Rating>) request.getAttribute("ratings");
    Double averageRating = (Double) request.getAttribute("averageRating");
    String error = (String) request.getAttribute("error");
%>

<html>
<head>
    <title>RideNow - Driver Feedback</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; margin: 0; padding: 20px; }
        h2 { text-align: center; color: #333; }
        .feedback-summary { text-align: center; background: #fff; padding: 15px; border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 350px; margin: 0 auto 20px; }
        .average-rating { font-size: 24px; color: #ffcc00; font-weight: bold; }
        .feedback-list { display: flex; flex-wrap: wrap; justify-content: center; gap: 20px; }
        .feedback-card { background: #fff; width: 300px; padding: 15px; border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: left; transition: transform 0.2s; }
        .feedback-card:hover { transform: scale(1.02); }
        .rating-stars { color: #ffcc00; margin-bottom: 5px; }
        .feedback-text { font-size: 15px; color: #333; margin-top: 5px; }
        .ride-info { font-size: 13px; color: #666; margin-top: 8px; }
        .dashboard-buttons { margin-top: 10px; display: flex; gap: 10px; flex-wrap: wrap; }
        .dashboard-buttons button { padding: 6px 12px; border-radius: 4px; border: none; background: #007bff; color: white; cursor: pointer; }
        .dashboard-buttons button:hover { background: #0056b3; }
        .back-button { display: block; width: fit-content; margin: 20px auto; background: #007bff; color: white; border: none; padding: 10px 16px; border-radius: 6px; cursor: pointer; text-decoration: none; }
        .back-button:hover { background: #0056b3; }
        .error { text-align: center; color: red; font-weight: bold; }

        /* Popup styles */
        .overlay { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 999; }
        .rating-popup { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: #fff; padding: 20px; border-radius: 12px; z-index: 1000; width: 300px; box-shadow: 0 4px 10px rgba(0,0,0,0.3); }
        .rating-popup h3 { margin-top: 0; text-align: center; }
        .rating-popup .stars { font-size: 30px; color: #ccc; text-align: center; margin-bottom: 10px; }
        .rating-popup .stars .star { cursor: pointer; transition: color 0.2s; }
        .rating-popup .stars .star.selected { color: #ffcc00; }
        .rating-popup textarea { width: 100%; border-radius: 6px; border: 1px solid #ccc; padding: 6px; resize: none; }
        .rating-popup input[type="submit"], .rating-popup input[type="button"] { margin-top: 10px; width: 48%; }
    </style>
</head>
<body>

<h2>Driver Feedback Overview</h2>

<% if (error != null) { %>
<div class="error"><%= error %></div>
<% } else if (ratings == null || ratings.isEmpty()) { %>
<div class="feedback-summary">
    <p>No feedback available for this driver yet.</p>
</div>
<% } else { %>
<div class="feedback-summary">
    <p><strong>Average Driver Rating:</strong></p>
    <div class="average-rating">
        ⭐ <%= String.format("%.2f", averageRating) %> / 5.0
    </div>
    <p>Total Reviews: <%= ratings.size() %></p>
</div>

<div class="feedback-list">
    <% for (Rating r : ratings) { %>
    <div class="feedback-card">
        <div class="rating-stars">
            <% for (int i = 1; i <= 5; i++) { %>
            <%= (i <= r.getRating()) ? "★" : "☆" %>
            <% } %>
        </div>
        <div class="feedback-text">
            "<%= r.getFeedback() != null && !r.getFeedback().trim().isEmpty() ? r.getFeedback() : "No written feedback." %>"
        </div>
        <div class="ride-info">
            Ride ID: <%= r.getRideRequestId() %><br>
            Given on: <%= r.getCreatedAt() %>
        </div>

        <div class="dashboard-buttons">
            <!-- Open Uber-style rating popup -->
            <button type="button"
                    onclick="openRatingPopup(
                        <%= r.getId() %>,
                        <%= r.getRating() %>,
                            '<%= r.getFeedback() != null ? r.getFeedback().replaceAll("'", "\\\\'").replaceAll("\n","\\n").replaceAll("\r","") : "" %>'
                            )">Update Rating</button>

            <!-- Delete Rating Form -->
            <form action="<%=request.getContextPath()%>/deleteRating" method="post">
                <input type="hidden" name="ratingId" value="<%= r.getId() %>">
                <button type="submit">Delete Rating</button>
            </form>
        </div>
    </div>
    <% } %>
</div>
<% } %>

<a href="<%=request.getContextPath()%>/completedRides" class="back-button">← Back to Dashboard</a>

<!-- Rating Popup -->
<div id="overlay" class="overlay" onclick="closeRatingPopup()"></div>
<div id="ratingPopup" class="rating-popup">
    <h3>Rate Your Ride</h3>
    <p>Ride ID: <span id="selectedRideId">None</span></p>
    <form action="<%=request.getContextPath()%>/updateRating" method="post">
        <input type="hidden" name="ratingId" id="rideId" value="">
        <div class="stars">
            <span class="star" onclick="selectStar(this,1)">★</span>
            <span class="star" onclick="selectStar(this,2)">★</span>
            <span class="star" onclick="selectStar(this,3)">★</span>
            <span class="star" onclick="selectStar(this,4)">★</span>
            <span class="star" onclick="selectStar(this,5)">★</span>
        </div>
        <input type="hidden" id="rating" name="ratingValue" value="0">
        <label for="feedback">Feedback:</label>
        <textarea id="feedbackInput" name="feedback" rows="3"></textarea><br><br>
        <input type="submit" value="Submit Rating">
        <input type="button" value="Cancel" onclick="closeRatingPopup()">
    </form>
</div>

<script>
    function openRatingPopup(ratingId, currentRating, feedback) {
        document.getElementById('overlay').style.display = 'block';
        document.getElementById('ratingPopup').style.display = 'block';
        document.getElementById('selectedRideId').innerText = ratingId;
        document.getElementById('rideId').value = ratingId;
        document.getElementById('rating').value = currentRating;
        document.getElementById('feedbackInput').value = feedback;

        const stars = document.querySelectorAll('#ratingPopup .star');
        stars.forEach((star, index) => {
            star.classList.toggle('selected', index < currentRating);
        });
    }

    function closeRatingPopup() {
        document.getElementById('overlay').style.display = 'none';
        document.getElementById('ratingPopup').style.display = 'none';
    }

    function selectStar(el, rating) {
        document.getElementById('rating').value = rating;
        const stars = document.querySelectorAll('#ratingPopup .star');
        stars.forEach((star, index) => {
            star.classList.toggle('selected', index < rating);
        });
    }
</script>

</body>
</html>
