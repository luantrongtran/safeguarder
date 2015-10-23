<html>

<head>
    <meta charset="UTF-8">
</head>

<body>

hello world!
<%
    String msg = request.getAttribute("msg").toString();
%>
<%=msg%>

</body>

</html>