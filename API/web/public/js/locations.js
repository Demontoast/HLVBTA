// Return cities for the given country
function getLocations(event) {
  event.preventDefault ? event.preventDefault() : event.returnValue = false

  var form = document.getElementById('locations-list');
  var latitude = form.elements.namedItem('latitude').value; // latitude that was entered
   var longitude = form.elements.namedItem('longitude').value; // longitude that was entered
   var speed = form.elements.namedItem('speed').value; // speed that was entered

  // Now we invoke the /locations endpoint as shown below
  $(function() {
    $.ajax({
      async: false,
      cache: false,
      type: 'GET',
      dataType: 'json',
      contentType: 'application/json',
      url: 'http://localhost:8080/locations/' + latitude +'/'+longitude+'/'+speed
    })
    .done(function(response) {

      // Process the received response, which is a JSON array of
      // city names and corresponding population

      var locations = $('#ADs'); // Get the ADs div in the html
      if (response.length==0) {
        locations.html("No ADs around");
      }
      else {
        var allAds = "<pre>"; // Preformatted text in html
        for (i = 0; i < response.length; i++) {
            allAds = allAds +" Name: "+ response[i].title +" Description: "+ response[i].description
            allAds = allAds + '<br>';
        }
        allAds = allAds + "</pre>"
        locations.html(allAds);
      }


    });
  });
}
