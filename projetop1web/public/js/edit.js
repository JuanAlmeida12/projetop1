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

function verifyUser(user) {
	if (user) {
	    // User is signed in
			console.log(user.uid);
			$("#nav_user_profile").append(nameFormated(user.displayName));
			$("#nav_user_profile").append("<span class=' fa fa-angle-down'></span>");
	    $("#profile_image").attr("src",user.photoURL);
			$("#nav_user_image").attr("src",user.photoURL);
			$("#profile_name").append(nameFormated(user.displayName));
	} else {
	    // No user is signed in
	    window.location.replace("/");
	}
}

function cancel(){
	window.location.replace("/profile/"+ id);
}

function save() {
	var user = firebase.auth().currentUser;
	var update = {};
  var mgender = $('input[name="gender"]:checked').val();
  var mjob = $('#user_job').val();
  var mmessage = $('#message').val();
  var maddress = $('#user_address').val();
	if(mgender != null) {
		update["gender"] = mgender;
	}
	if(mjob != ""){
		update["job"] = mjob;
	}
	if(mmessage != "") {
		update["message"] = mmessage;
	}
	if(maddress != ""){
		update["city"] = maddress;
	}
  var ref = firebase.database().ref("user/"+id);
	var refActivity = firebase.database().ref("activity").push();
	refActivity.set({
		date: new Date().getTime(),
		owner: id,
		content: nameFormated(user.displayName)+" update their profile"
	});
  ref.update(update).then(function(){
		window.location.replace("/profile/"+id);
	});
}
function nameFormated(name) {
	var res = name.split(" ");
	return res[0] + " " + res[res.length -1]+" ";
}
