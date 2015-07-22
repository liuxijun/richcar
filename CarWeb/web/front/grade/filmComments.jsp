<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>影片评论</title>
<link rel="stylesheet" type="text/css" href="../css/grade.css">
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/grade.js"></script>
<script type="text/javascript">
    var contentId = request("contentId");
    var cspId = request("cspId");
    $(document).ready(function() {
        receiveComments();
    });

</script>


</head>

<body>
<div class="box_con" id="List_showCm_0">
    <ul class="commentdetail" id="commentsDiv">

    </ul>
</div>
</body>
</html>
