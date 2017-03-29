// Return cities for the given country
function getcities(event) {
  event.preventDefault ? event.preventDefault() : event.returnValue = false

  var form = document.getElementById('city-list-form');
  var country = form.elements.namedItem('country').value; // country that was entered

  // Now we invoke the /cities endpoint as shown below
  $(function() {
    $.ajax({
      async: false,
      cache: false,
      type: 'GET',
      dataType: 'json',
      contentType: 'application/json',
      url: 'http://localhost:8080/cities/' + country
    })
    .done(function(response) {

      // Process the received response, which is a JSON array of
      // city names and corresponding population

      var cities = $('#cities'); // Get the cities div in the html
      if (response.length==0) {
        cities.html("No cities in this country");
      }
      else {
        var allcities = "<pre>"; // Preformatted text in html
        for (i = 0; i < response.length; i++) {
            allcities = allcities + response[i].name
                        + " (Population: " + response[i].population + ")"
            allcities = allcities + '<br>';
        }
        allcities = allcities + "</pre>"
        cities.html(allcities);
      }


    });
  });
}
