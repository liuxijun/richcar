<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"getDefaultModule","cspModuleGetDefaultModule");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
Ext.onReady(function(){

    var dataViewStore = new Ext.data.JsonStore({
            method:'POST',
            url: "/csp/cspModule!getDefaultModule.action"
    });
    //dataListStore.setDefaultSort('m_mediaId','desc');
    dataViewStore.load({
        callback :
            function(records,options,success){
                if (typeof(this.reader.jsonData)=='undefined'){
                    Ext.Msg.alert("提示","CSP未设置默认资源模版！");   
                }else{
                    document.location='cpContentView.jsp?action=add&moduleId='+this.reader.jsonData.obj.moduleId;
                }
            }
    });
})
</script>
</head>
<body>
</body>
</html>