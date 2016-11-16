var map;
var mMarker = {};
var indice = 0;
function initMap() {
	map = new google.maps.Map(document.getElementById('map_profile'), {
    center: {lat: 0, lng: 0},
    zoom: 2,
    styles: [{
      stylers: [{ visibility: 'simplified' }]
    }, {
      elementType: 'labels',
      stylers: [{ visibility: 'off' }]
    }]
  });

  // Referência ao DIV que recebe o conteúdo da infowindow recorrendo ao jQuery
  var iwOuter = $('.gm-style-iw');

  /* Uma vez que o div pretendido está numa posição anterior ao div .gm-style-iw.
   * Recorremos ao jQuery e criamos uma variável iwBackground,
   * e aproveitamos a referência já existente do .gm-style-iw para obter o div anterior com .prev().
   */
  var iwBackground = iwOuter.prev();

  // Remover o div da sombra do fundo
  iwBackground.children(':nth-child(2)').css({'display' : 'none'});

  // Remover o div de fundo branco
  iwBackground.children(':nth-child(4)').css({'display' : 'none'});

    handlePhotosOnMap();
}

google.maps.event.addDomListener(window, 'load', initMap());

function handlePhotosOnMap() {
  var refPhoto = firebase.database().ref("photo");
  refPhoto.orderByChild("owner").equalTo(id).on("child_added", function(snapshot){
    var photo = snapshot.val();
    var refStorage = firebase.storage().ref(photo.content);
    refStorage.getDownloadURL().then(function(url){
      createMarker(
        photo,
        url,
        indice
     );
     indice = indice +1;
    });
  });
}


function createMarker(photo, url,indice) {
    var marker_type;
    var colors = ["#FFA500",'#00CCBB', '#00FF00', '#D3D3D3','#FF69B4'];
    var colorMarker = Math.floor((Math.random() * 5));
    var icon = {
    url: "../images/pin_map_size.png", // url
    origin: new google.maps.Point(0,0), // origin
    anchor: new google.maps.Point(3, 34) // anchor
    };
    var marker = new google.maps.Marker({
      map: map,
      position: {lat: photo.lat, lng: photo.lng},
      icon: icon,
      id:indice
    });

    mMarker[marker.id] = indice;

    $("#carousel_indice").append(`
      <li data-target="#myCarousel" data-slide-to="`+ indice +`"></li>
      `);
    $("#carousel_images").append(`
      <div style="width: 600px; height:350px" class="item image view view-first">
        <div class="carousel-caption">
          <div class="mask">
            <p>`+ photo.description +`</p>
          </div>
        </div>
        <img class="img-responsive center-block"style="height:350px" src="` + url +`" alt="`+ marker.id +`"/>
      </div>
        `);
    $('.item').first().addClass('active');
    $('.carousel-indicators > li').first().addClass('active');
    $('#myCarousel').carousel();

    google.maps.event.addListener(marker, 'click', function() {
      console.log(mMarker);
      $('.carousel').carousel(mMarker[marker.id]);
    });
}
