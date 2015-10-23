<html>

<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="/js/xlsx.js"></script>

    <script>
        function to_json(workbook) {
            var result = {};
            workbook.SheetNames.forEach(function(sheetName) {
                var roa = XLS.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
                if(roa.length > 0){
                    result[sheetName] = roa;
                }
            });
            return result;
        }
    </script>
</head>

<body>

hello world!
<%
String msg = request.getAttribute("msg").toString();
%>
<%=msg%>
<input type="file" />

</body>

</html>