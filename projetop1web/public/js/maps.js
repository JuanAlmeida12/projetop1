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
    var mposition = new google.maps.LatLng(
        position.coords.latitude, position.coords.longitude);

    map.setCenter(mposition);

    var request = {
        location: mposition,
        radius: '5000',
        types: ['natural_feature','stadium', 'park','city_hall', 'art_gallery','library']
    };

    service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, callback);

}

function callback(results, status, pagination) {
    getMyPlaces(function(places) {
        if (status == google.maps.places.PlacesServiceStatus.OK) {
            for (var i = 0; i < results.length; i++) {
                var visited = places.hasOwnProperty(results[i].id);
                createMarker(results[i], visited);
            }
        if (pagination.hasNextPage) {
            pagination.nextPage();
        }
        }
    });
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
    var placeLoc = place.geometry.location;
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
        infowindow.setContent(place.name);
        infowindow.open(map, this);
    });
}