<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map-canvas { height: 100% }
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=false">
    </script>
    <script type="text/javascript">

      function initialize() {
        var mapOptions =true
        {
          center: new google.maps.LatLng(-27.477228, 153.028317),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("map-canvas"),
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

    </script>
  </head>
  <body onload="initialize()">
    <div id="map-canvas"/>
  </body>
</html>


//Just a reference for Maps
//var map; // Global declaration of the map
//var iw = new google.maps.InfoWindow(); // Global declaration of the infowindow
//var lat_longs = new Array();
//var markers = new Array();
//var drawingManager;
//
//function initialize() {
//
//
//
//    var myLatlng = new google.maps.LatLng(-27.477228, 153.028317);
//    var myOptions = {
//        zoom: 13,
//        center: myLatlng,
//        mapTypeId: google.maps.MapTypeId.ROADMAP}
//    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
//    drawingManager = new google.maps.drawing.DrawingManager({
//        drawingMode: google.maps.drawing.OverlayType.POLYGON,
//        drawingControl: true,
//        drawingControlOptions: {
//            position: google.maps.ControlPosition.TOP_CENTER,
//            drawingModes: [google.maps.drawing.OverlayType.POLYGON]
//        },
//        polygonOptions: {
//            editable: true,
//            draggable: true
//        }
//    });
//
//    coords = $('#old-coords').text()
//    draw_coords = []
//
//    if(coords != "null") {
//        //Regex to obtain the Longitudes and Latitudes
//        var lat = coords.match(/[+-]?\d+\.\d+\,/g);
//        var long = coords.match(/[+-]?\d+\.\d+\)/g);
//
//        //Now lets create a object with latitude and longitude
//        for (i = 0; i < lat.length; i++) {
//            draw_coords.push({
//                lat: parseFloat(lat[i]),
//                lng: parseFloat(long[i].replace(/\)$/g, ''))
//            })
//        }
//
//
//        //Code to display the Old Fence
//        var oldFence = new google.maps.Polygon({
//            paths: draw_coords,
//            strokeColor: '#FF0000',
//            strokeOpacity: 0.8,
//            strokeWeight: 2,
//            fillColor: '#FF0000',
//            fillOpacity: 0.35
//        });
//        oldFence.setMap(map);
//    }
//
//    drawingManager.setMap(map);
//
//
//
//
//
//    google.maps.event.addListener(drawingManager, "overlaycomplete", function(event) {
//        var newShape = event.overlay;
//        newShape.type = event.type;
//    });
//
//    google.maps.event.addListener(drawingManager, "overlaycomplete", function(event){
//        overlayClickListener(event.overlay);
//        $('#coords').val(event.overlay.getPath().getArray());
//    });
//
//    //Initialize the DataPicker library function
//    $('#start-time').datetimepicker({
//        format: 'HH:mm'
//    });
//
//    $('#end-time').datetimepicker({
//        format: 'HH:mm'
//    });
//}
//
//function overlayClickListener(overlay) {
//    google.maps.event.addListener(overlay, "mouseup", function(event){
//        $('#coords').val(overlay.getPath().getArray());
//    });
//}
