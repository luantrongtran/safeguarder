<html>

<head>
    <meta charset="UTF-8">

    <style>
        h2 {
            padding: 0;
            margin: 0;
            font-weight: bold;
            text-transform: uppercase;
        }

        #images {
            text-align: center;
        }

        #loc {
            display: inline-block;
            width: 80px;
            height: 15px;
            border-bottom: solid 2px black;
        }

        img {
            margin-top: 20px;
            margin-left: 20px;
        }

        #acc {
            -moz-border-radius: 10px;
            -webkit-border-radius: 10px;
            border-radius: 10px;
            width: 400px;
            height: 480px;
            padding: 10px 10px 25px 40px;
            background-color: lightblue;
            font-size: 20px;
            line-height: 60px;
        }

        .att {
            font-weight: bold;
        }
    </style>

    <%@include file="../templates/header.html"%>
    <%@include file="../templates/navigation.html"%>
</head>

<body>
    <%
        String aName = request.getAttribute("accName").toString();
        String aType = request.getAttribute("accType").toString();
        String aTime = request.getAttribute("accTime").toString();
        String aObs = request.getAttribute("accObsLvl").toString();
        String aDesc = request.getAttribute("accDesc").toString();

        String image1 = request.getAttribute("Image1").toString();
        String image2 = request.getAttribute("Image2").toString();
        String image3 = request.getAttribute("Image3").toString();
    %>

    <div class="row" id="container">
        <div class="col-md-2"></div>

        <div class="col-md-4" id="details">
            <h2><%=aName%></h2>
            <div id="acc">
                <br />
                <span class="att">Type  </span>:&nbsp;<%=aType%>
                <br />
                <span class="att">Time </span>:&nbsp;<%=aTime%>
                <br />
                <span class="att">Location </span>:&nbsp;<div id="loc"></div>
                <br />
                <span class="att">Severity </span>:&nbsp;<%=aObs%>
                <br />
                <span class="att">Description </span>:&nbsp;<%=aDesc%>
            </div>
        </div>

        <div class="col-md-4" id="images">
            <%
                if(!image1.isEmpty()) {
            %>
            <img src="<%=image1%>" height="170" width="280">
            <br />
            <%
                }
                if(!image2.isEmpty()) {
            %>
            <img src="<%=image2%>" height="170" width="280">
            <br />
            <%
                }
                if(!image3.isEmpty()) {
            %>
            <img src="<%=image3%>" height="170" width="280">
            <%
                }
            %>
        </div>

        <div class="col-md-2"></div>
    </div>
    <div class="row" id="footer"><%@include file="../templates/footer.html"%></div>
</body>
</html>