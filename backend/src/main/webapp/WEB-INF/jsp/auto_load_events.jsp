<html>
<!--References: https://github.com/SheetJS/js-xlsx-->
    <meta charset="UTF-8">
    <title></title>
    <script src="/js/jszip.js"></script>
    <script src="/js/xlsx.js"></script>
    <script src="/js/ssf.js"></script>

    <script src="/js/jquery.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/bootstrap/css/bootstrap.min.css">
    <script>

        <%--https://github.com/SheetJS/js-harb/blob/master/bits/22_helpers.js#L7-L17--%>
        function numdate(v) {
            var date = SSF.parse_date_code(v);
            var val = new Date();
            val.setUTCDate(date.d);
            val.setUTCMonth(date.m-1);
            val.setUTCFullYear(date.y);
            val.setUTCHours(date.H);
            val.setUTCMinutes(date.M);
            val.setUTCSeconds(date.S);
            return val;
        }

        $(document).ready(function () {
            $("#zip_file").change(function(e) {
                var files = e.target.files;
                var f = files[0];
                $("#zip_filename").val(f.name);
            });
            $("#excelfile").change(function(e) {
                var files = e.target.files;
                var f = files[0];
                $("#excel_filename").val(f.name);

                clearTable();
                handleFile(e);
            });
        });

        function handleFile(e) {
            var files = e.target.files;
            var f = files[0];
            var reader = new FileReader();
            var name = f.name;
            reader.onload = function(e) {
                var data = e.target.result;

                var workbook = XLSX.read(data, {type: 'binary'});
                var sum = populateIntoTable(workbook);

                $("#numOfItems").val(sum);
            };
            reader.readAsBinaryString(f);

        }

        function populateIntoTable(workbook) {
            var sum = 0;
            workbook.SheetNames.forEach(function(sheetName) {
                var worksheet = workbook.Sheets[sheetName];
                var roa = XLS.utils.sheet_to_row_object_array(worksheet);

                if(roa.length > 0){
                    for(var i = 0; i < roa.length; i++) {
                        var cell_index = i + 2;
                        var cell_date_address = "D" + cell_index;
                        var date_code = worksheet[cell_date_address].v;
                        var d = numdate(date_code, true);
                        var day = d.getDate();
                        var month = d.getMonth() + 1;
                        var year = d.getFullYear();


                        var acc = roa[i];

                        var strDate = day + "/" + month + "/" + year;
                        acc.strDate = strDate;

                        if(acc.time.split(':')[0].length == 1) {
                            acc.time = '0' + acc.time;
                        }
                        var strDate1 = year + "-" + month + "-" + day + "T" + acc.time;
                        d = new Date(strDate1);
                        acc.millisecond = d.getTime();

                        addNewRow(acc, i);

                        sum++;
                    }
                }

//                var worksheet = workbook.Sheets[sheetName];
//
//                for (z in worksheet) {
//                    alert('1');
//                    /* all keys that do not begin with "!" correspond to cell addresses */
//                    if(z[0] === '!') continue;
//                    console.log(z + "=" + JSON.stringify(worksheet[z].v));
//                }
            });

            return sum;
        }

        function clearTable() {
            $("#excel_output_body>tr").empty();
        }

        /**
         *
         * @param acc accident json object
         * @param order order in the table
         */
        function addNewRow(acc, order) {
            var name = acc.name;
            var type = acc.type;
            var date = acc.date;
            var time = acc.time;
            var lat = acc.lat;
            var lon = acc.lon;
            var observation = acc.observation;
            var description = acc.description;

            var image1 = '';
            if (acc.image1 + '' != 'undefined') {
                image1 = acc.image1;
            }

            var image2 = '';
            if(acc.image2+'' != 'undefined'){
               image2 = acc.image2;
            }

            var image3 = '';
            if(acc.image3+'' != 'undefined'){
                image3 = acc.image3;
            }


            var newRow = "<tr>";
            newRow += "<td><input name='" + "name" + order + "'  name='" + "" + "' readonly value='" + name + "'></td>";
            newRow += "<td><input name='" + "type" + order + "'  readonly value='" + type + "'></td>";
            newRow += "<td><input name='" + "date" + order + "'  readonly value='" + date + "'></td>";
            newRow += "<td><input name='" + "time" + order + "'  readonly value='" + time + "'></td>";
            newRow += "<td><input name='" + "lat"  + order + "'  readonly value='" + lat + "'></td>";
            newRow += "<td><input name='" + "lon" + order + "'  readonly value='" + lon + "'></td>";
            newRow += "<td><input name='" + "observation" + order + "'  readonly value='" + observation + "'></td>";
            newRow += "<td><input name='" + "description" + order + "'  readonly value='" + description + "'></td>";
            newRow += "<input name='millisecond" + order + "' type='hidden' value='" + acc.millisecond + "' />";
            newRow += "<td><input value='" + image1 + "' name='image1" + order + "'/></td>";
            newRow += "<td><input value='" + image2 + "' name='image2" + order + "'></td>";
            newRow += "<td><input value='" + image3 + "'name='image3" + order + "'></td>"

            newRow += "</tr>";

            $("#excel_output_body").append(newRow);
        }

        /**
         * Validate if excel file or zip file has been selected
         */
        function validateData() {
            if($("#excel_filename").val() == null || $("#excel_filename").val() == '') {
                alert('Please select an excel file');
                return false;
            } else if ($("#zip_filename").val() == null || $("#zip_filename").val() == '') {
                var result = confirm("No zip file selected! Do you want to continue?");
                return result
            } else if ($("#numOfItems").val() == 0) {
                alert("No data in the selected excel file");
                return false;
            }

            return true;
        }
    </script>

    <style>
        .btn-file {
            position: relative;
            overflow: hidden;
        }
        .btn-file input[type=file] {
            position: absolute;
            top: 0;
            right: 0;
            min-width: 100%;
            min-height: 100%;
            font-size: 100px;
            text-align: right;
            filter: alpha(opacity=0);
            opacity: 0;
            outline: none;
            background: white;
            cursor: inherit;
            display: block;
        }
    </style>
</head>

<body >

<h1>Auto load events</h1>

<div style="width: 400px">
    <h4>Please select an excel file (.xlsx only)</h4>
    <div class="input-group">
                <span class="input-group-btn">
                    <span class="btn btn-primary btn-file">
                        Browse&hellip; <input type="file" id="excelfile" class="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                    </span>
                </span>
        <input type="text" id="excel_filename" class="form-control" readonly>
    </div>
</div>



<%
    String uploadUrl = request.getAttribute("uploadUrl").toString();
%>

<form method="POST" action="<%=uploadUrl%>" enctype="multipart/form-data" onsubmit="return validateData()">
    <div style="width: 400px">
        <h4>Please select the zip file containing images</h4>
        <div class="input-group">
                <span class="input-group-btn">
                    <span class="btn btn-primary btn-file">
                        Browse&hellip; <input id="zip_file" type="file" name="images" accept="application/zip"/>
                    </span>
                </span>
            <input type="text" id="zip_filename" class="form-control" readonly>
        </div>
    </div>

    <div style="margin-top: 50px;">
        <h2>Data Table</h2>
        Number of Items: <input id="numOfItems" name="numOfItems" />
        <table class="table table-condensed" id="excel_output">
            <thead>
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Date</th>
                <th>Time</th>
                <th>Lat</th>
                <th>Lon</th>
                <th>Observation</th>
                <th>Description</th>

                <th>Image 1</th>
                <th>Image 2</th>
                <th>Image 3</th>
            </tr>
            </thead>
            <tbody id="excel_output_body">

            </tbody>
        </table>
    </div>

    <button type="submit">Submit</button>
</form>

</body>

</html>