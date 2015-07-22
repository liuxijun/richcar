<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>收藏</title></head>
<body>
<script src="../js/jquery.js" type="text/javascript">

</script>
<script type="text/javascript">
    $(document).ready(function() {
        var cspId = $("#cspId").val();
        var contentId = $('#contentId').val();
        var userId = $('#userId').val();

        $("#userFavorites").submit(function() {
            $.post("/portal/userFavorites!save.action",
            {   'obj.cspId':cspId ,
                'obj.contentId':contentId,
                'obj.userId':userId
            },
                    function(data) {
                        alert("保存成功");
                    });

            return false;
        });
    });

</script>

<s:form id="userFavorites">
    <s:hidden name="obj.userId" value="test" id="userId"/>
    <s:hidden name="obj.cspId" value="2145" id="cspId"/>
    <s:hidden name="obj.contentId" value="3136" id="contentId"/>

    <h2> 影片名称：<input name="obj.contentName" type="text" value="华夏视联-飞女芝芝" readonly="true">
    </h2>

    <s:submit value="收藏"></s:submit>
</s:form>


</body>
</html>