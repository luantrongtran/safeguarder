<html>
<!--References: https://github.com/SheetJS/js-xlsx-->
    <meta charset="UTF-8">
    <title></title>
    <script src="/js/jszip.js"></script>
    <script src="/js/xlsx.js"></script>

    <script src="/js/jquery.js"></script>
    <script src="resources/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/bootstrap/css/bootstrap.min.css">
    <script>

        $(document).ready(function () {
            $("#excelfile").change(function(e) {
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

                to_json(workbook);
                /* DO SOMETHING WITH workbook HERE */
            };
            reader.readAsBinaryString(f);

        }

        function to_json(workbook) {
            workbook.SheetNames.forEach(function(sheetName) {
                var roa = XLS.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);

                if(roa.length > 0){
                    for(var i = 0; i < roa.length; i++) {
                        var acc = roa[i];
                        addNewRow(acc);
                    }
                }
            });
        }

        function clearTable() {
            $("#excel_output_body>tr").empty();
        }

        function addNewRow(acc) {
            var name = acc.name;
            var type = acc.type;
            var time = acc.time;
            var lat = acc.lat;
            var lon = acc.lon;
            var observation = acc.observation;
            var description = acc.description;

            var newRow = "<tr>";
            newRow += "<td><input readonly value='" + name + "'></td>";
            newRow += "<td><input readonly value='" + type + "'></td>";
            newRow += "<td><input readonly value='" + time + "'></td>";
            newRow += "<td><input readonly value='" + lat + "'></td>";
            newRow += "<td><input readonly value='" + lon + "'></td>";
            newRow += "<td><input readonly value='" + observation + "'></td>";
            newRow += "<td><input readonly value='" + description + "'></td>";
            newRow += "<td><input type='file'></td>"
            newRow += "<td><input type='file'></td>"
            newRow += "<td><input type='file'></td>"
            newRow += "<td>Not uploaded</td>";
            newRow += "</tr>";

            $("#excel_output_body").append(newRow);
        }
    </script>

</head>

<body>

<h1>Auto load events</h1>

<input type="file" id="excelfile">

<%
    String uploadUrl = request.getAttribute("uploadUrl").toString();
%>

<form method="POST" action="<%=uploadUrl%>">
    <div>
        <table class="table table-condensed" id="excel_output">
            <thead>
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Time</th>
                <th>Lat</th>
                <th>Lon</th>
                <th>Observation</th>
                <th>Description</th>

                <th>Image 1</th>
                <th>Image 2</th>
                <th>Image 3</th>

                <th>Status</th>
            </tr>
            </thead>
            <tbody id="excel_output_body">

            </tbody>
        </table>
    </div>
</form>

<div>
    <button type="submit">Submit</button>
</div>

</body>

</html>