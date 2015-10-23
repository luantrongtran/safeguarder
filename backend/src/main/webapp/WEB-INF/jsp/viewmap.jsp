<!DOCTYPE html>
<html lang="en">
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
    </script>
    </head>
<%@include file="../templates/header.html"%>
<link href="../stylesheets/main.css" rel="stylesheet">
<body onload="initialize()">
<%@include file="../templates/navigation.html"%>
    <div class="container">
        <h1>Google Map</h1>
        <p>You can view the maps and events that are reported in your surroundings here!</p>

    <div class="map-container">
    </div>
    <div id="map-canvas" style="height:700px; width:1200px"></div>
    </div>
  <hr>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<%@include file="../templates/scripts.html"%>
</body>
<%@include file="../templates/footer.html"%>
</html>