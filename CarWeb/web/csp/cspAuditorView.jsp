<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="cspAuditor"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "cspAuditor";
        nextUrl = "cspAuditorList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var cspAuditorViewForm = new FortuneViewForm({
                title:'<fts:text name="cspAuditor"/>基本信息',
                url:'cspAuditor!save.action',
                saveUrl:'cspAuditor!save.action',
                viewUrl:'cspAuditor!view.action',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.cspId"/>',name:'obj.cspId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.adminId"/>',name:'obj.adminId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.cpOnline"/>',name:'obj.cpOnline',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.cpOffline"/>',name:'obj.cpOffline',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.spOnline"/>',name:'obj.spOnline',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="cspAuditor.spOffline"/>',name:'obj.spOffline',readOnly:viewReadOnly}
                ]
            });
            loadFormAjax();
            cspAuditorViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }/*,{
                    func:initAreaStore,
                    done:false,
                    flag:'initAreaStore'
                }*/
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