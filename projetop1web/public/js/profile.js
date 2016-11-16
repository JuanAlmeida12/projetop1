//Configure firebase
var config = {
	apiKey: "AIzaSyDwxt5GqFKborDsCngWP1_znK7Y8mP5SrY",
	authDomain: "worlddiscovery-f60f3.firebaseapp.com",
	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com",
	storageBucket: "worlddiscovery-f60f3.appspot.com",
};
firebase.initializeApp(config);

var following = false;
var refFollow = null;

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

function buttonState(follow){
	if(follow){
		$("#bt_follow").attr("class","btn btn-danger");
		$("#bt_follow").text("");
		$("#bt_follow").append("<i class='fa fa-user-times m-right-xs'></i> Unfollow");
	} else {
		$("#bt_follow").attr("class","btn btn-info");
		$("#bt_follow").text("");
		$("#bt_follow").append("<i class='fa fa-user-plus m-right-xs'></i> Follow");
	}
}

function followUser() {
	if(!following){
		var ref = firebase.database().ref("follow");
		following = true;
		var user = firebase.auth().currentUser;
		var newFollow = ref.push();
		newFollow.set({
  		'follower': user.uid,
  		'following': id,
			'date': new Date().getTime()
		});
		refFollow = newFollow;
	} else {
		following = false;
		refFollow.remove();
	}
	buttonState(following)
}

function isFollower(userid) {
	var ref = firebase.database().ref("follow");
	ref.orderByChild("following").equalTo(id).once("child_added").then(function(snapshot) {
		var followData = snapshot.val();
		if(followData.follower == userid) {
			console.log(snapshot.val());
			following = true;
			refFollow = snapshot.ref;
			buttonState(following);
		}
		/*
		if(snapshot.exists()){
				var dataFollow = snapshot.
				console.log(snapshot.val());
				following = true;
			refFollow = snapshot.ref;
		}
		getProfileInfo();
		buttonState(following);*/
	});
}

function verifyUser(user) {
	if (user) {
			isFollower(user.uid);
			getProfileInfo();
	    // User is signed in
			console.log(user.uid);
			$("#nav_user_profile").append(nameFormated(user.displayName));
			$("#nav_user_profile").append("<span class=' fa fa-angle-down'></span>");
	    $("#profile_image").attr("src",user.photoURL);
			$("#nav_user_image").attr("src",user.photoURL);
			$("#profile_name").append(nameFormated(user.displayName));
			if(user.uid != id) {
				$("#bt_edit").remove();
			} else {
				$("#bt_follow").remove();
			}

	} else {
	    // No user is signed in
	    window.location.replace("/");
	}
}

function handleBadges(user) {
	var refBadgeUser = firebase.database().ref("user-badge");
	refBadgeUser.orderByChild("user").equalTo(id).on("child_added", function(snapshot){
		var userbadge = snapshot.val();
		var refBadge = firebase.database().ref("badge");
		refBadge.orderByChild("codbadge").equalTo(userbadge.codbadge).once("child_added").then(function(badgesnap){
			var badge = badgesnap.val();
			var mprogress = Math.floor((userbadge.progress/badge.meta)*100);
			console.log(mprogress);
			$('#tableBadge > tbody:last-child').append(`
				<tr>
					<td>`+ badge.name +`</td>
					<td>`+ badge.description +`</td>
					<td class="hidden-phone">`+ badge.meta +`</td>
					<td class="vertical-align-mid">
						<div class="progress">
							<div class="progress-bar progress-bar-success" role="progressbar" style="width:`+ mprogress +`%" aria-valuenow="`+ mprogress +`" aria-valuemin="0" aria-valuemax="100">
								<span class="sr-only">`+ mprogress +`% Complete</span>
							</div>
						</div>
					</td>
				</tr>
				`);
		});
	});
}

function edit(){
	window.location.replace("/profile/"+ id+"/edit");
}

function handlePlaces() {
	var refPlacesUser = firebase.database().ref("user-place");
	refPlacesUser.orderByChild("user").equalTo(id).on("child_added", function(snapshot){
		var userplace = snapshot.val();
		var refPlace = firebase.database().ref("posts");
		refPlace.orderByChild("codplace").equalTo(userplace.codplace).once("child_added").then(function(placesnap){
			var place = placesnap.val();
			console.log(place);
			var date = new Date(userplace.date);
			$('#tablePlaces > tbody:last-child').append(`
				<tr>
					<td><i class="fa fa-flag"></i></td>
					<td>`+ place.placeName +`</td>
					<td>`+ place.description.en +`</td>
					<td>`+ date.toLocaleDateString("en-US") +`</td>
				</tr>
				`);
		});
	});
}

function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 32; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

function handleActivity(user){
	var refActivity = firebase.database().ref("activity");
	refActivity.orderByChild("owner").equalTo(id).limitToLast(50).on("child_added", function(snapshot){
		var activity = snapshot.val();
		var monthNames = ["January", "February", "March", "April", "May", "June",
  		"July", "August", "September", "October", "November", "December"
		];
		var date = new Date(activity.date);
		$("#activities").prepend(`
			<li>
				<img src="`+user.photoURL+`" class="avatar" alt="Avatar">
				<div class="message_date">
					<h3 class="date text-info">`+ date.getDate() +`</h3>
					<p class="month">`+ monthNames[date.getMonth()] +`</p>
				</div>
				<div class="message_wrapper">
					<h4 class="heading">`+ nameFormated(user.name) +`</h4>
					<blockquote class="message">`+ activity.content +`</blockquote>
					<br />
				</div>
			</li>
			`);
	});
}

function getProfileInfo() {
	var ref = firebase.database().ref("user/"+id);
	ref.once('value').then(function(snapshot){
		var user = snapshot.val();
		handleActivity(user);
		handleBadges(user);
		handlePlaces();
		$("#profilepage_name").text(user.name);
		$("#profile_picture").attr("src",user.photoURL);
		if(snapshot.hasChild('job') && user.job != ""){
			$("#user_job").append(" "+user.job);
		} else {
			$("#user_job").remove();
		}
		if(snapshot.hasChild('city') && user.city != ""){
			$("#user_location").append(" "+user.city);
		} else {
			$("#user_location").remove();
		}
		if(snapshot.hasChild('gender') && user.gender != ""){
			$("#user_gender").append(" "+user.gender);
		} else {
			$("#user_gender").remove();
		}
		if(snapshot.hasChild('message') && user.message != ""){
			$("#user_message").append(" "+formatMessage(user.message));
		} else {
			$("#user_message").remove();
		}
	});
}
function htmlDecode(input){
    var e = document.createElement('div');
    e.innerHTML = input;
    return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
  }

function formatMessage(message) {
	var index = 0;
	var tmp = "";
	tmp +="<br/>";
	for (var i = 0; i < message.length; i++) {
		tmp += message.charAt(i);
		index = index +1;
		if(index == 25){
			tmp +="\n";
			index = 0;
		}
	}
	console.log(tmp);
	return tmp;
}

function nameFormated(name) {
	var res = name.split(" ");
	return res[0] + " " + res[res.length -1]+" ";
}
