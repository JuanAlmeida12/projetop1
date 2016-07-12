//Configure firebase
var config = {
	apiKey: "AIzaSyDwxt5GqFKborDsCngWP1_znK7Y8mP5SrY",
	authDomain: "worlddiscovery-f60f3.firebaseapp.com",
	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com",
	storageBucket: "worlddiscovery-f60f3.appspot.com",
};
firebase.initializeApp(config);

//verify user is signed in
function verifyUser(user) {
	console.log("Entrei na func verifyUser");
	if (user) {
		console.log("logado");
		$("#login").remove();
		$("#nav-mobile").append(`
			<li id='useriden'>
				<div class="chip">
		    		<img src="`+ user.photoURL +`" alt="Contact Person"> `
		    		+ user.displayName +
		  		`</div>
	  		</li>
			`);
		$("#nav-mobile").append("<li id='chat'><a href='#'><i class='material-icons'>chat_bubble</i></a></li>");
		$("#nav-mobile").append("<li id='logout'><a href='javascript:signOut()'>Sair</a></li>");
		$("#user-image-slide-menu")
        	.mouseover(function() { 
	            $(this).attr("src", user.photoURL);
        })
	} else {
		console.log("Nao logado");
		$("#nav-mobile").append("<li id='login'><a href='javascript:authGoogle()'>Login</a></li>")
		$("#logout").remove();
		$("#useriden").remove();
		$("#chat").remove();
	}
}

$('.button-collapse').sideNav();

function showSlideMenu() {
	$('.button-collapse').sideNav('show');
}

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

function authGoogle() {
	console.log("Entrei na func");
	var provider = new firebase.auth.GoogleAuthProvider();
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