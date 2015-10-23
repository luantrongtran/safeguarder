<!DOCTYPE html>
<html lang="en">
<%@include file="../templates/header.html"%>
<body>
  <div class="container">
       < class="jumbotron">
      <h2>Welcome to SafeGuarder</h2>
      <p>SafeGuarder is a web service that will allow users to report any incidents they witness or are aware of
        the events and the other users will be notified about the reported events in their surroundings. </p>
        <br>
        <h3>Features</h3>
        <br>

        <a href="./HelloWorld?name=Luan" class="btn btn-info">Hello World</a>
        <a href="/view-map"><button class="btn btn-info">View Map</button></a>
        <a href="./autoloadevents?name=Luan" class="btn bn-info">Auto load events</a>
    </div>
  </div>
  <%@include file="../templates/scripts.html"%>
</body>
<%@include file="../templates/footer.html"%>
</html>