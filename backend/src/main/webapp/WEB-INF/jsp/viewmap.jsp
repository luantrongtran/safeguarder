<!DOCTYPE html>
<html lang="en">


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

<script src="/js/map.js"></script>
<%@include file="../templates/scripts.html"%>
</body>
<%@include file="../templates/footer.html"%>
</html>