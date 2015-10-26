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
            height: 450px;
            padding: 10px 10px 25px 40px;
            background-color: #F0F0F0;
            font-size: 20px;
            line-height: 60px;
        }

        .att {
            font-weight: bold;
        }

        #delete {
            width: 400px;
            text-align: center;
            margin-top: 20px;
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
        String id = request.getAttribute("accId").toString();
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

            <div id="delete">
                <a href="/deleteAccidentById?accidentId=<%=id%>" class="btn btn-info" id="dltBtn" onClick="return confirm('Delete <%=aName%>?');">
                Delete Accident</a>
            </div>
        </div>

        <div class="col-md-4" id="images">
            <img src="http://www.firstaidforfree.com/wp-content/uploads/2013/12/accident2.jpg" alt="Img1" height="170" width="280">
            <br />
            <img src="http://b.fastcompany.net/multisite_files/coexist/article_feature/bike-accident-report-card-main.jpg" alt="Img2" height="170" width="280">
            <br />
            <img src="http://www.slatergordon.co.uk/media/2250683/car-accident-header.jpg" alt="Img3" height="170" width="280">
        </div>

        <div class="col-md-2"></div>
    </div>
    <div class="row" id="footer"><%@include file="../templates/footer.html"%></div>
</body>
</html>