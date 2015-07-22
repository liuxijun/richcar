<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "encoderTask";
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
        actionHeader = "encoderTask";
        nextUrl = "encoderTaskListCanOrder.jsp";
        viewReadOnly = true;
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var contentViewForm = new FortuneViewForm({
                title:'转码信息',
                width:500,
                defaults: {width: 400},
                url:'encoderTask!save.action',
                saveUrl:'encoderTask!save.action',
                viewUrl:'encoderTask!view.action',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                    {fieldLabel:'名字',name:'obj.name',readOnly:viewReadOnly},
                    {fieldLabel:'启动时间',name:'obj.startTime',readOnly:viewReadOnly},
                    {fieldLabel:'结束时间',name:'obj.stopTime',readOnly:viewReadOnly},
                    {fieldLabel:'当前进度',name:'obj.process',readOnly:viewReadOnly},
                    {fieldLabel:'源文件',name:'obj.sourceFileName',readOnly:viewReadOnly},
                    {fieldLabel:'目标文件',name:'obj.desertFileName',readOnly:viewReadOnly},
                    {fieldLabel:'日志信息',name:'obj.encodeLog',xtype:'textarea',height:400,readOnly:viewReadOnly}
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