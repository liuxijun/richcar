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
    <title><fts:text name="userBuy"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        actionHeader = "userBuy";
        nextUrl = "userBuyList.jsp";
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var userBuyViewForm = new FortuneViewForm({
                title:'<fts:text name="userBuy"/>基本信息',
                url:'userBuy!save.action',
                saveUrl:'userBuy!save.action',
                viewUrl:'userBuy!view.action',
                items:[
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                     {name:'obj.id',inputType:'hidden',value:'-1'}                     ,
                     {fieldLabel:'<fts:text name="userBuy.userId"/>',name:'obj.userId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.userIp"/>',name:'obj.userIp',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.spId"/>',name:'obj.spId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.channelId"/>',name:'obj.channelId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.contentId"/>',name:'obj.contentId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.serviceProductId"/>',name:'obj.serviceProductId',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.isGift"/>',name:'obj.isGift',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.price"/>',name:'obj.price',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.startTime"/>',name:'obj.startTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.endTime"/>',name:'obj.endTime',readOnly:viewReadOnly}                     ,
                     {fieldLabel:'<fts:text name="userBuy.buyTime"/>',name:'obj.buyTime',readOnly:viewReadOnly}
                ]
            });
            loadFormAjax();
            userBuyViewForm.render(displayDiv);
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