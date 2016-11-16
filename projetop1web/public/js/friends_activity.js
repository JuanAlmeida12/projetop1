'use strict';
let refActivity = firebase.database().ref("activity");
refActivity.orderByChild("date").startAt(1473090609560).endAt(1473098018412).on('child_added', function(activitysnap) {
  let activity = activitysnap.val();
  isFollow(activity);
});

function isFollow(activity){
  let ref = firebase.database().ref("follow");
  ref.orderByChild("following").equalTo(activity.owner).limitToFirst(1).once("value").then(function(snapshot) {
    if(snapshot.exists()){
      console.log("aqui");
      var refUser = firebase.database().ref("user/"+ activity.owner);
      refUser.once("value").then(function(usersnap){
        createActivityUI(usersnap.val(), activity, usersnap.key);
      });
    } else {
      console.log("aqui else");
    }
  });
}

function createActivityUI(user, activity, key){
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
      <div  class="message_wrapper">
        <h4  onclick="goProfile('`+ key +`')" class="heading">`+ nameFormated(user.name) +`</h4>
        <blockquote class="message">`+ activity.content +`</blockquote>
        <br />
      </div>
    </li>
    `);
}
function goProfile(profile) {
  window.location.replace("/profile/"+ profile);
}
function nameFormated(name) {
	var res = name.split(" ");
	return res[0] + " " + res[res.length -1]+" ";
}
