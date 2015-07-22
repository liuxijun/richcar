<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "save", "systemLogManage,systemLogSave");
        //  needPermissions(actionHeader,"list","systemLogManage,systemLogList");
        needPermissions(actionHeader, "view", "systemLogManage,systemLogView");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="systemLog"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "systemLog";
        nextUrl = "systemLogList.jsp";
        var keyId=<%=request.getParameter("obj.id")%>    ;

        function initDisplay() {
            var systemLogViewForm = new FortuneViewForm({
                title:'<fts:text name="systemLog"/>基本信息',
                bodyStyle:'padding:5px 5px 0',
                url:'systemLog!view.action',
                viewUrl:'systemLog!view.action',
                buttonAlign:'center',
                width: 750,
                defaults: {width: 630},
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {name:'obj.id', inputType:'hidden', value:'-1'}                     ,
                    {fieldLabel:'<fts:text name="systemLog.systemLogAction"/>', name:'obj.systemLogAction', readOnly:true}                     ,
                    {fieldLabel:'<fts:text name="systemLog.adminId"/>', name:'obj.adminId',xtype:'hidden', readOnly:true}                     ,
                    {fieldLabel:'<fts:text name="systemLog.adminId"/>', name:'obj.adminName',readOnly:true}                     ,
                    {fieldLabel:'<fts:text name="systemLog.adminIp"/>', name:'obj.adminIp', readOnly:true}                     ,
                    {fieldLabel:'<fts:text name="systemLog.logTime"/>', name:'obj.logTime', readOnly:true}                     ,
                    {fieldLabel:'<fts:text name="systemLog.log"/>', name:'obj.log',height:198, xtype:'textarea',readOnly:true}
                ],
                buttons:[
                    {
                        xtype:'button',
                        text:'返回',
                        minWidth:90,
                        listeners:{
                            "click":function () {
                                window.location.href = "systemLogList.jsp";
                            }
                        }
                    }
                ]
            });

            loadFormAjax();
            systemLogViewForm.render(displayDiv);
        }

        Ext.onReady(function () {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],
                    initDisplay);
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>