<html>

<head>
    <meta charset="UTF-8">
</head>

<body>
<%  String aName = request.getAttribute("accName").toString();    %>
<%=aName%>
<br />
<%  String aType = request.getAttribute("accType").toString();    %>
<%=aType%>
<br />
<%  String aTime = request.getAttribute("accTime").toString();    %>
<%=aTime%>
<br />
Accident Location:
<div id="loc" style="width: 100px; height: 20px; background: black;"></div>
<br />
<%  String aObs = request.getAttribute("accObsLvl").toString();%>
<%=aObs%>
<br /> <br />
    <img src="http://www.firstaidforfree.com/wp-content/uploads/2013/12/accident2.jpg" alt="Img1" height="180" width="300">

    <img src="http://b.fastcompany.net/multisite_files/coexist/article_feature/bike-accident-report-card-main.jpg" alt="Img2" height="180" width="300">

    <img src="http://www.slatergordon.co.uk/media/2250683/car-accident-header.jpg" alt="Img3" height="180" width="300">

</body>

</html>