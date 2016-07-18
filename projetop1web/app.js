var express = require('express');
var app = express();
var path    = require("path");

var request = require('request');
var cheerio = require('cheerio');
var URL = require('url-parse');

var firebase = require("firebase");
firebase.initializeApp({
	serviceAccount: "./WorldDiscovery-9631a52bfc29.json",
  	databaseURL: "https://worlddiscovery-f60f3.firebaseio.com"
});

app.use(express.static(__dirname + '/public'));

app.get('/', function (req, res) {
  res.sendFile(path.resolve("views/index.html"));
});

app.listen(8081, function () {
  console.log('Example app listening on port 3000!');
});