function searchButton() {
  var user = $("#search_box").val();
  window.location.replace("/search/"+ user);
}
