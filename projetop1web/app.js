var express = require('express');
var app = express();
var path    = require("path");
var gcm = require('node-gcm');

var request = require('request');
var cheerio = require('cheerio');
var URL = require('url-parse');

var sender = new gcm.Sender('AIzaSyAlibYmw1xGx_JjADozWZyb_-kAghd18CE');

var firebase = require("firebase");
firebase.initializeApp({
	serviceAccount: "./WorldDiscovery-9631a52bfc29.json",
  	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com"
});

app.use(express.static(__dirname + '/public'));
app.set('views', __dirname + '/views');
app.engine('html', require('ejs').renderFile);
app.engine('js', require('ejs').renderFile);

app.get('/', function (req, res) {
  res.render("login.html");
});

app.get('/dashboard', function (req, res) {
  res.render("index.html");
});

app.get('/profile/:id', function (req, res) {
  res.render("profile.html",{profile:req.params.id});
});

app.get('/profile/:id/edit', function (req, res) {
  res.render("form.html",{profile:req.params.id});
});

app.get('/search/:user', function (req, res) {
  res.render("search.html",{user:req.params.user});
});

app.listen(process.env.PORT || 5000, function () {
  console.log('Listening on port 5000!');
});

// Database Triggers

var db = firebase.database();
var ref = db.ref("follow");

// Attach an asynchronous callback to read the data at our posts reference
ref.on("child_added", function(snapshot) {
	var newFollow = snapshot.val();
	console.log(snapshot.val());
	console.log(newFollow.token);
	var message = new gcm.Message({
    data: {
			sender: newFollow.follower,
			action: 'FOLLOW'
		}
	});
	var regTokens = [newFollow.token];
	sender.send(message, { registrationTokens: regTokens }, function (err, response) {
	    if(err) console.error(err);
	    else    console.log(response);
	});
});
