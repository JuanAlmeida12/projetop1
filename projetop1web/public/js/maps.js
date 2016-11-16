var map;
var infowindow;

var database = firebase.database();

var myPlaces = {}

var colors = ["#FFA500",'#00CCBB', '#00FF00', '#D3D3D3','#FF69B4'];

var placesLabel = {'park':'map-icon-park', 'parking':'map-icon-park', 'cemetery':'map-icon-cemetery',
    'place_of_worship':'map-icon-place-of-worship','art_gallery':'map-icon-art-gallery',
        'city_hall':'map-icon-city-hall','church':'map-icon-church', 'subway_station':'map-icon-subway-station'}

function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.397, lng: 150.644},
    zoom: 18,
    styles: [{
      stylers: [{ visibility: 'simplified' }]
    }, {
      elementType: 'labels',
      stylers: [{ visibility: 'off' }]
    }]
  });
    infowindow = new google.maps.InfoWindow();
    getLocation();
}

google.maps.event.addDomListener(window, 'load', initMap());

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
    }
}

function getMyPlaces(callback) {
    var user = firebase.auth().currentUser;
    if (user) {
        console.log('snapshot.val()');
        firebase.database().ref('user-places/' +
            firebase.auth().currentUser.uid).on('value', function(snapshot) {
            console.log(snapshot.val());
            var places = {};
            for (key in snapshot.val()){
                places[snapshot.val()[key].pid] = snapshot.val()[key];
            }
            callback(places)
        });
    }
}

function showPosition(position) {
    getWeather(position.coords.latitude,position.coords.longitude);
    var mposition = new google.maps.LatLng(
        position.coords.latitude, position.coords.longitude);

    map.setCenter(mposition);
    var request = {
      location: mposition,
      radius: '5000',
      types: ['natural_feature','library','airport','art_gallery','campground','stadium','zoo','place_of_worship']
    };
    var service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, callback);
    // var placeData = {
    //     placeName: "Embedded",
    //     description: {
    //         en: "Embedded Lab",
    //         pt: "Embedded Lab"
    //     },
    //     lat: -7.2123825,
    //     lng: -35.9082391,
    //     tags: ["lab"]
    // };
    // var newPlaceKey = firebase.database().ref().child('places').push().key;
    // var updates = {};
    // updates['/posts/' + newPlaceKey] = placeData;
    // firebase.database().ref().update(updates);

    // firebase.database().ref('posts/').on('value', function(snapshot) {
    //         //console.log(snapshot.val());
    //         var places = {};
    //         for (key in snapshot.val()){
    //             //console.log(snapshot.val()[key]);
    //             createMarker(snapshot.val()[key],false);
    //         }
    //     });
}

function createMarkers(places) {
  var bounds = new google.maps.LatLngBounds();
  var placesList = document.getElementById('places');

  for (var i = 0, place; place = places[i]; i++) {
    var image = {
      url: place.icon,
      size: new google.maps.Size(71, 71),
      origin: new google.maps.Point(0, 0),
      anchor: new google.maps.Point(17, 34),
      scaledSize: new google.maps.Size(25, 25)
    };

    var marker = new google.maps.Marker({
      map: map,
      icon: image,
      title: place.name,
      position: place.geometry.location
    });
  }
}

function callback(results, status, pagination) {
        if (status == google.maps.places.PlacesServiceStatus.OK) {
          console.log("aqui place");
          createMarkers(results);
            // for (var i = 0; i < results.length; i++) {
            //     var visited = places.hasOwnProperty(results[i].id);
            //     createMarker(results[i], visited);
            // }
        if (pagination.hasNextPage) {
            pagination.nextPage();
        }
        }
}

function createMarker(place, visited) {
    var marker_type,map_icon;
    if(visited) {
        marker_type = SQUARE_PIN;
        map_icon = placesLabel[place.types[0]];
        console.log(map_icon);
    } else {
        marker_type = ROUTE;
        map_icon = 'map-icon-point-of-interest';
    }
    var colorMarker = Math.floor((Math.random() * 5));
    var placeLoc = new google.maps.LatLng( place.lat, place.lng ) ;
    var marker = new Marker({
        map: map,
        position: placeLoc,
        icon: {
            path: marker_type,
            fillColor: colors[colorMarker],
            fillOpacity: 1,
            strokeColor: '',
            strokeWeight: 0
        },
        map_icon_label: '<span class="map-icon '+ map_icon +'"></span>'
    });

    google.maps.event.addListener(marker, 'click', function() {
        infowindow.setContent(place.placeName);
        infowindow.open(map, this);
    });
}
