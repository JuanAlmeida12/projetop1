//Configure firebase
var config = {
	apiKey: "AIzaSyDwxt5GqFKborDsCngWP1_znK7Y8mP5SrY",
	authDomain: "worlddiscovery-f60f3.firebaseapp.com",
	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com",
	storageBucket: "worlddiscovery-f60f3.appspot.com",
};
firebase.initializeApp(config);

setBg();

setInterval(setBg, 6000);

function setBg(){
	var bg = ["bg1.jpg", "bg2.jpg", "bg3.jpg", "bg4.jpg"];
	var bgIndex = Math.floor((Math.random() * 4));
	$("body").css("background-image", "url('images/" + bg[bgIndex]+ "')");
}

function verifyUser(user) {
	console.log("Entrei na func verifyUser");
	if (user) {
		var ref = firebase.database().ref("user/" + user.uid);
		var timestamp = new Date().getTime();
		ref.update({lastLogin: timestamp});
		ref.on('value', function(snapshot){
			if(!snapshot.hasChild("createdAt")) {
				snapshot.ref.set({
					createdAt:timestamp,
					name: user.displayName,
					photoURL: user.photoURL,
					numPosts:0,
					numBadges:0,
					numPlaces:0,
					message:"",
					city: ""
				}).then(function(){
					window.location.replace("/dashboard");
				});
				var refActivity = firebase.database().ref("activity").push();
				refActivity.set({
					owner: user.uid,
					date: new Date().getTime(),
					content: user.displayName + " made his first login"
				});
			} else {
				window.location.replace("/dashboard");
			}
		});
		console.log("logado");
	}
}

firebase.auth().onAuthStateChanged(function(user) {
	verifyUser(user);
});

function authGoogle() {
	console.log("Entrei na func");
	var provider = new firebase.auth.GoogleAuthProvider();
	provider.addScope('https://www.googleapis.com/auth/plus.login');
	firebase.auth().signInWithPopup(provider).then(function(result) {
		// This gives you a Google Access Token. You can use it to access the Google API.
		var token = result.credential.accessToken;
		// The signed-in user info.
		var user = result.user;
		//verifyUser(user);
		// ...
	}).catch(function(error) {
		// Handle Errors here.
		console.log(error.message);
		var errorCode = error.code;
		var errorMessage = error.message;
		// The email of the user's account used.
		var email = error.email;
		// The firebase.auth.AuthCredential type that was used.
	  	var credential = error.credential;
	  	// ...
	});
}
