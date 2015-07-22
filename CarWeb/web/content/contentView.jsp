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
    <title><fts:text name="content"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "content";
        nextUrl = "contentList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var contentViewForm = new FortuneViewForm({
                title:'<fts:text name="content"/>基本信息',
                url:'content!save.action',
                saveUrl:'content!save.action',
                viewUrl:'content!view.action',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'<fts:text name="content.name"/>',name:'obj.name',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.actors"/>',name:'obj.actors',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.directors"/>',name:'obj.directors',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.creatorAdminId"/>',name:'obj.creatorAdminId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.createTime"/>',name:'obj.createTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.cspId"/>',name:'obj.cspId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.moduleId"/>',name:'obj.moduleId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.deviceId"/>',name:'obj.deviceId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.status"/>',name:'obj.status',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.statusTime"/>',name:'obj.statusTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.contentAuditId"/>',name:'obj.contentAuditId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.digiRightUrl"/>',name:'obj.digiRightUrl',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.validStartTime"/>',name:'obj.validStartTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.validEndTime"/>',name:'obj.validEndTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.allVisitCount"/>',name:'obj.allVisitCount',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.monthVisitCount"/>',name:'obj.monthVisitCount',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.weekVisitCount"/>',name:'obj.weekVisitCount',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.intro"/>',name:'obj.intro',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.post1Url"/>',name:'obj.post1Url',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.post2Url"/>',name:'obj.post2Url',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property1"/>',name:'obj.property1',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property2"/>',name:'obj.property2',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property3"/>',name:'obj.property3',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property4"/>',name:'obj.property4',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property5"/>',name:'obj.property5',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property6"/>',name:'obj.property6',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property7"/>',name:'obj.property7',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="content.property8"/>',name:'obj.property8',readOnly:viewReadOnly}
                ]
            });
            loadFormAjax();
            contentViewForm.render(displayDiv);
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