      var map;
      function initialize() {
        var mapOptions ={
           zoom: 15,
           center: new google.maps.LatLng(-27.477228, 153.028317),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
      }
      google.maps.event.addDomListener(window, 'load', initialize);

      function loadScript() {
       var script = document.createElement("script");
       script.type = "text/javascript";
       script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=TRUE_OR_FALSE&callback=initialize";
       document.body.appendChild(script);
     }

     window.onload = loadScript;



