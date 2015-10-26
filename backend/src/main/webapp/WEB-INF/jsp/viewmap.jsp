<!DOCTYPE html>
<html lang="en">

<%@include file="../templates/header.html"%>
<link href="../stylesheets/main.css" rel="stylesheet">
<body>
<%@include file="../templates/navigation.html"%>
    <div class="container">
        <h1>Google Map</h1>
        <p>You can view the maps and events that are reported in your surroundings here!</p>

        <div id="accidentList" style="overflow-y: auto; width: 1200px; max-height: 300px; margin-bottom: 5px;">
            <table class="table table-condensed" id="excel_output">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Time</th>
                    <th>Severity</th>
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

    function initializeMap() {
        var myLatLng = {lat: -27.477228, lng: 153.028317};
        var mapOptions ={
            zoom: 15,
            center: myLatLng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };


        map = new google.maps.Map(document.getElementById("map-canvas"),
                mapOptions);

        refreshAccidentList();
    }
    google.maps.event.addDomListener(window, 'load', initializeMap);

    function loadScript() {
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyDO4n9AFniMpOQS3wkWH0pRUsmLxoqcxZ4&sensor=TRUE_OR_FALSE&callback=initializeMap";
        document.body.appendChild(script);
    }

    window.onload = loadScript;

    function panTo(lat, lon) {
        var latLng = new google.maps.LatLng(lat, lon);
        map.panTo(latLng);
    }

    function refreshAccidentList() {
        var icons = {};
        icons["aviation accident"] = "../resources/img/aviation_marker.png";
        icons["criminal"] = "../resources/img/crime_marker.png";
        icons["earthquake"] = "../resources/img/earthquake_marker.png"
        icons["ferry accident"] = "../resources/img/ferry_marker.png";
        icons["industry accident"] = "../resources/img/industry_accident_marker.png";
        icons["traffic accident"] = "../resources/img/traffic_accident_marker.png";
        icons["weather accident"] = "../resources/img/weather_marker.png";

        var observationLevel = {};
        observationLevel[1] = "Low";
        observationLevel[2] = "Medium";
        observationLevel[3] = "High";
        observationLevel[4] = "Highest";

        $.getJSON("/controlpanel/getaccidents", function(list) {
            clearAccidentTable();

            var table = $('#accident_table_body');
            $.each(list, function(index, accident) {
                var d = new Date(accident.time);
                var day = addZero(d.getDate(), 2);
                var month = addZero(d.getMonth() + 1,2 );
                var year = d.getFullYear();
                var hours = addZero(d.getHours(), 2);
                var mins = addZero(d.getMinutes(), 2);

                var obsLvl = observationLevel[accident.observation_level];

                var strDate = day + "/" + month + "/" + year + " " + hours + ":" + mins;

                $('<tr>').appendTo(table)
                        .append($('<td>').text(accident.name))
                        .append($('<td>').text(accident.type))
                        .append($('<td>').text(strDate))
                        .append($('<td>').text(obsLvl))
                        .append($('<td>').text(accident.description))
                        .append($('<td>').html("<a onclick='panTo(" + accident.lat + "," + accident.lon + ")'>Location</a>"));

                var latLng = new google.maps.LatLng(accident.lat, accident.lon);

                var markerIcon = icons[accident.type.toLowerCase()];

                var marker = new google.maps.Marker({
                    position: latLng,
                    map: map,
                    icon: markerIcon
                });

                var windowInfoContent = "<h3>" + accident.name + "</h3>";
                windowInfoContent += "<div style='display: inline-block;'>";
                windowInfoContent += "<b style='margin-right:3px;'>Type:</b> " + accident.type;
                windowInfoContent += "<br>";
                windowInfoContent += "<b style='margin-right:3px;'>Time:</b> " + strDate;
                windowInfoContent += "<br>";
                windowInfoContent += "<b style='margin-right:3px;'>Severity:</b> " + obsLvl;
                windowInfoContent += "</div>";
                
                if(accident.image1 != '') {
                    windowInfoContent += "<div style='display: inline-block;'>";
                    windowInfoContent += "<img style='width: 200px; height: 200px;' src='" +  accident.image1 + "'>";
                    windowInfoContent += "</div>";
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

    function clearAccidentTable() {
        $("#accident_table_body>tr").empty();
    }
</script>

</body>
<%@include file="../templates/footer.html"%>
</html>