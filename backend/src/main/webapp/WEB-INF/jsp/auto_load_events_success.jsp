<html lang="en">
<%@include file="../templates/header.html"%>
<%
    String msg = request.getAttribute("msg").toString();
%>
<body style="margin-left: 20px;">
<%@include file="../templates/navigation.html"%>
<h2 style="color: green;">Uploaded Successfully.</h2>
<h3 style="color: blue;"><%=msg%></h3>
<a href="/">Go back to homepage</a>
</body>
</html>