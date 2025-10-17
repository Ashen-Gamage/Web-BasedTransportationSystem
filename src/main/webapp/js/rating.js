function refreshCompletedRides() {
    console.log("Refreshing completed rides...");
    window.location.href = "/completedRides";
}

function selectStar(rating) {
    console.log("Star selected: " + rating);
    var stars = document.querySelectorAll('.star');
    for (var i=0; i<stars.length; i++){
        if(i<rating) stars[i].classList.add('selected');
        else stars[i].classList.remove('selected');
    }
    document.getElementById('rating').value = rating;
}

function showRatingPopup(rideId) {
    console.log("Showing rating popup for rideId: " + rideId);
    document.getElementById('rideId').value = rideId;
    document.getElementById('selectedRideId').innerText = rideId;
    document.getElementById('overlay').classList.add('active');
    document.getElementById('ratingPopup').classList.add('active');
}

function closeRatingPopup() {
    console.log("Closing rating popup");
    document.getElementById('overlay').classList.remove('active');
    document.getElementById('ratingPopup').classList.remove('active');
}
