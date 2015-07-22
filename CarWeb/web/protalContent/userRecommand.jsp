<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>推荐</title></head>
<body>
<script src="../js/jquery.js" type="text/javascript">

</script>
<script type="text/javascript">
$(document).ready(function() {
    var cspId =$("#cspId").val();
    var contentId = $('#contentId').val();
    var userId = $('#userId').val();

 $("input[name='status']").click(function(){
        var statusSelect = $(this).val();
        $.post("/portal/userRecommand!save.action",
            {   'obj.cspId':cspId ,
                'obj.contentId':contentId,
                'obj.userId':userId,
                'obj.status':statusSelect
            },
                function(json) {
                    alert("感谢您的推荐!");
                    $("input[name='status']").attr("checked",false);
                });
    });
          })     ;


</script>

<s:form action="userScoring" id="userScoringForm" namespace="/portal" method="get">
    <s:hidden name="obj.userId" value="test" id="userId"/>
    <s:hidden name="obj.cspId" value="2145" id="cspId"/>
    <s:hidden name="obj.contentId" value="3136" id="contentId"/>

    <h2> 影片名称：<input name="obj.contentName" type="text" value="华夏视联-飞女芝芝" readonly="true">
        </h2>

    状态：
  <input type="radio" name="status" value="0">顶
  <input type="radio" name="status" value="1"> 踩
</s:form>


</body>
</html>