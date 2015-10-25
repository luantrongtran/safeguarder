      var map;

      function initialize() {
      var myLatLng = {lat: -27.477228, lng: 153.028317};
        var mapOptions ={
           zoom: 15,
           center: myLatLng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };


        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);

        var marker = new google.maps.Marker({
           position: myLatLng,
           map: map,
           title: 'You are here!'
           });
      }
      google.maps.event.addDomListener(window, 'load', initialize);

      function loadScript() {
       var script = document.createElement("script");
       script.type = "text/javascript";
       script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=TRUE_OR_FALSE&callback=initialize";
       document.body.appendChild(script);
     }

     window.onload = loadScript;

    function panTo(lat, lon) {
        var latLng = new google.maps.LatLng(lat, lon);
        map.panTo(latLng);
    }

