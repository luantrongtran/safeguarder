<!DOCTYPE html>
<html lang="en">

<%@include file="../templates/header.html"%>
<link href="../stylesheets/main.css" rel="stylesheet">
<body onload="initialize()">
<%@include file="../templates/navigation.html"%>
    <div class="container">
        <h1>Google Map</h1>
        <p>You can view the maps and events that are reported in your surroundings here!</p>

        <div id="accidentList">
            <table class="table table-condensed" id="excel_output">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Time</th>
                    <th>Observation Level</th>
                    <th>Description</th>
                    <th>Location</th>
                </tr>
                </thead>
                <tbody id="accident_table_body">

                </tbody>
            </table>
        </div>

        <div class="map-container">
        </div>
        <div id="map-canvas" style="height:700px; width:1200px"></div>
    </div>
  <hr>

<%@include file="../templates/scripts.html"%>

<script>


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

        $.getJSON("/controlpanel/getaccidents", function(list) {
            var table = $('#accident_table_body');
            $.each(list, function(index, accident) {
                var d = new Date(accident.time);
                var day = addZero(d.getDate(), 2);
                var month = addZero(d.getMonth() + 1,2 );
                var year = d.getFullYear();
                var hours = addZero(d.getHours(), 2);
                var mins = addZero(d.getMinutes(), 2);

                var strDate = day + "/" + month + "/" + year + " " + hours + ":" + mins;

                $('<tr>').appendTo(table)
                        .append($('<td>').text(accident.name))
                        .append($('<td>').text(accident.type))
                        .append($('<td>').text(strDate))
                        .append($('<td>').text(accident.observation_level))
                        .append($('<td>').text(accident.description))
                        .append($('<td>').html("<a onclick='panTo(" + accident.lat + "," + accident.lon + ")'>Location</a>"));

                var latLng = new google.maps.LatLng(accident.lat, accident.lon);

                var marker = new google.maps.Marker({
                    position: latLng,
                    map: map,
                    title: 'Hello World!'
                });

                var windowInfoContent = "<h3>" + accident.name + "</h3>";
                windowInfoContent += "<b>Type:</b>" + accident.type;
                windowInfoContent += "<br>";
                windowInfoContent += "<b>Time:</b>" + strDate;
                if(accident.image1) {
                    windowInfoContent += "<img src='" +  accident.image1 + "'";
                }

                var infowindow = new google.maps.InfoWindow({
                    content: windowInfoContent
                });

                marker.addListener('click', function() {
                    infowindow.open(map, marker);
                });
            });
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
</script>

</body>
<%@include file="../templates/footer.html"%>
</html>