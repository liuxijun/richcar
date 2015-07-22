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
    <title><fts:text name="visitLog"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "visitLog";
        nextUrl = "visitLogList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var visitLogViewForm = new FortuneViewForm({
                title:'<fts:text name="visitLog"/>基本信息',
                url:'visitLog!save.action',
                saveUrl:'visitLog!save.action',
                viewUrl:'visitLog!view.action',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'<fts:text name="visitLog.spId"/>',name:'obj.spId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.cpId"/>',name:'obj.cpId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.channelId"/>',name:'obj.channelId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.contentId"/>',name:'obj.contentId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.contentPropertyId"/>',name:'obj.contentPropertyId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.url"/>',name:'obj.url',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.userId"/>',name:'obj.userId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.userIp"/>',name:'obj.userIp',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.areaId"/>',name:'obj.areaId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.isFree"/>',name:'obj.isFree',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.startTime"/>',name:'obj.startTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.endTime"/>',name:'obj.endTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="visitLog.length"/>',name:'obj.length',readOnly:viewReadOnly}
                ]
            });
            loadFormAjax();
            visitLogViewForm.render(displayDiv);
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