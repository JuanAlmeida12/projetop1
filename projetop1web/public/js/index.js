//Configure firebase
var config = {
	apiKey: "AIzaSyDwxt5GqFKborDsCngWP1_znK7Y8mP5SrY",
	authDomain: "worlddiscovery-f60f3.firebaseapp.com",
	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com",
	storageBucket: "worlddiscovery-f60f3.appspot.com",
};
firebase.initializeApp(config);

firebase.auth().onAuthStateChanged(function(user) {
	verifyUser(user);
});

function signOut() {
	firebase.auth().signOut().then(function() {
	  	// Sign-out successful.
	}, function(error) {
	  	// An error happened.
	});
}

function myProfile(){
	var user = firebase.auth().currentUser;
	window.location.replace("/profile/" + user.uid);
}

function verifyUser(user) {
	if (user) {
	    // User is signed in
			console.log(user.uid);
			$("#nav_user_profile").append(nameFormated(user.displayName));
			$("#nav_user_profile").append("<span class=' fa fa-angle-down'></span>");
	    $("#profile_image").attr("src",user.photoURL);
			$("#nav_user_image").attr("src",user.photoURL);
			$("#profile_name").text(nameFormated(user.displayName));
			var ref = firebase.database().ref("user/" + user.uid);
			ref.once('value').then(function(snapshot){
				var user_info = snapshot.val();
				var createdAt = new Date(user_info.createdAt);
				var lastLogin = new Date(user_info.lastLogin);
				var numPosts = user_info.numPosts;
				var lastPost = new Date(user_info.lastPost);
				var numBadges = user_info.numBadges;
				var lastBadge = new Date(user_info.lastBadge);
				var numPlaces = user_info.numPlaces;
				var lastPlace = new Date(user_info.lastPlace);
				$("#user_since").append(dateFormated(createdAt));
				$("#user_since_span").append(createdAt.toLocaleDateString("en-US"));
				$("#user_last_login").append(dateFormated(lastLogin));
				$("#user_last_login_span").append(lastLogin.toLocaleDateString("en-US"));

				$("#user_activities").append(numPosts);
				$("#user_badges").append(numBadges);
				$("#user_places").append(numPlaces);
				if(numPosts == 0){
					$("#user_last_activity").append("Never");
				} else {
					$("#user_last_activity").append(lastPost.toLocaleDateString("en-US"));
				}
				if(numBadges == 0){
					$("#user_last_badge").append("Never");
				} else {
					$("#user_last_badge").append(lastBadge.toLocaleDateString("en-US"));
				}
				if(numPlaces == 0){
					$("#user_last_place").append("Never");
				} else {
					$("#user_last_place").append(lastPlace.toLocaleDateString("en-US"));
				}
			});
	} else {
	    // No user is signed in
	    window.location.replace("/");
	}
}
function dateFormated(dateTemp) {
	var currDate = new Date();
	var timeDiff = Math.abs(currDate.getTime() - dateTemp.getTime());
	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
	if(diffDays > 7 && diffDays < 30){
		weeks = Math.flow(diffDays/7);
		if(weeks === 1) {
			return weeks +" week";
		} else {
			return weeks +" weeks";
		}
	} else if (diffDays > 30) {
		months = Math.flow(diffDays/30);
		if(months === 1) {
			return months +" month";
		} else {
			return months +" month";
		}
	} else {
		if(diffDays === 1) {
			return diffDays +" day";
		} else {
			return diffDays +" days";
		}
	}
}

function nameFormated(name) {
	var res = name.split(" ");
	return res[0] + " " + res[res.length -1]+" ";
}
