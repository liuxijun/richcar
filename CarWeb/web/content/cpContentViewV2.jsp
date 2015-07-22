<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"getCspDevice","deviceGetDeviceRegUrl");
//        needPermissions(actionHeader,"list","propertyList");
//        needPermissions(actionHeader,"list","propertySelectList");
  //      needPermissions(actionHeader,"uploadFile","contentManage,contentUploadFile");
        needPermissions(actionHeader,"save","contentManage,contentSave");
        needPermissions(actionHeader,"getFtpList","contentManage,contentGetFtpList");
        needPermissions(actionHeader,"getDeviceRegUrl","deviceGetDeviceRegUrl");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script type="text/javascript" src="/js/common.js"></script>      
<script type="text/javascript" src="contentViewV2.js"></script>
<script language="javascript">
    var moduleId = '<%=request.getParameter("moduleId")%>';
    var contentId = '<%=request.getParameter("contentId")%>';
    initSystem(contentId,moduleId);

</script>
    <style>
        .imgStyle{
            float:left;
            width:220px;
        }
        .posterImg{
            float:left;
            width:120px;
            height:60px;
        }
        .textInfo{
            float:left;
            width:100px
        }
        .textBox{
            width:50px
        }
    </style>
</head>
<body>
<div id="display">
    <ul class="imgStyle">
        <li class="poster"><img src="/upload/2011/05/13/132707_images.jpg" alt="海报"></li>
        <li class="textInfo"><label for="url_0">连接</label><input class="textBox" type="text" name="url" id="url_0"></li>
        <li class="textInfo"><label for="clipSize_0">大小</label><input class="textBox"  type="text" name="size" id="clipSize_0"></li>
    </ul>
    <ul class="imgStyle">
        <li class="poster"><img src="/upload/2011/05/13/132707_images.jpg" alt="海报"></li>
        <li class="textInfo"><label for="url_1">连接</label><input class="textBox"  type="text" name="url" id="url_1"></li>
        <li class="textInfo"><label for="clipSize_1">大小</label><input class="textBox"  type="text" name="size" id="clipSize_1"></li>
    </ul>
</div>
</body>
</html>