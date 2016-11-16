//Configure firebase
var config = {
	apiKey: "AIzaSyDwxt5GqFKborDsCngWP1_znK7Y8mP5SrY",
	authDomain: "worlddiscovery-f60f3.firebaseapp.com",
	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com",
	storageBucket: "worlddiscovery-f60f3.appspot.com",
};
firebase.initializeApp(config);
search_user();
function search_user() {
  console.log(user);
  var ref = firebase.database().ref("user");
  ref.orderByChild("name").startAt(user).on("child_added", function(snapshot) {
    var userget = snapshot.val();
    console.log(userget);
    var template =
    `<div class="col-md-3 col-xs-12 widget widget_tally_box">
      <div class="x_panel fixed_height_390">
        <div class="x_content">

          <div class="flex">
            <ul class="list-inline widget_profile_box">
              <li>
                <a>
                  <i class="fa fa-google"></i>
                </a>
              </li>
              <li>
                <img src="`+ userget.photoURL +`" alt="..." class="img-circle profile_img">
              </li>
              <li>
                <a>
                  <i class="fa fa-user"></i>
                </a>
              </li>
            </ul>
          </div>

          <h3 class="name">`+ nameFormated(userget.name) +`</h3>

          <div class="flex">
            <ul class="list-inline count2">
              <li>
                <h3>`+ userget.numBadges +`</h3>
                <span>Badges</span>
              </li>
              <li>
                <h3>`+ userget.numPlaces +`</h3>
                <span>Places</span>
              </li>
              <li>
                <h3>`+ userget.numPosts +`</h3>
                <span>Posts</span>
              </li>
            </ul>
          </div>
          <p>`+ userget.message.substring(0,25)+"..." +`</p>
          <a id="go_profile" onclick="goProfile('`+ snapshot.key +`')"class="btn btn-info"><i class="fa fa-arrow-right  m-right-xs"></i> Go Profile</a>
        </div>
      </div>
    </div>`;
    $("#content_users").append(template);
  });
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


function goProfile(profile) {
  window.location.replace("/profile/"+ profile);
}

function nameFormated(name) {
	var res = name.split(" ");
	return res[0] + " " + res[res.length -1]+" ";
}
