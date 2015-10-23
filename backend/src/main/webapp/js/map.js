<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta charset="utf-8">
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=false">">
    </script>
    <script>
      var map;
      function initialize() {
        var mapOptions ={
          zoom: 8,
          center: new google.maps.LatLng(-34.397,150.644),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
      }
      google.maps.event.addDomListener(window, 'load', initialize);



//      function loadScript() {
//       var script = document.createElement("script");
//       script.type = "text/javascript";
//       script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=TRUE_OR_FALSE&callback=initialize";
//       document.body.appendChild(script);
//     }

//     window.onload = loadScript;

    </script>
  </head>
  <body onload="initialize()">
    <div id="map-canvas" style="height:300px; width:500px"></div>
  </body>
</html>


