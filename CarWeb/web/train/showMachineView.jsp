<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "showMachine";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "delete", "trainAdDeviceManage");
        needPermissions(actionHeader, "deleteSelected", "trainAdDeviceManage");
        needPermissions(actionHeader, "add", "trainAdDeviceManage");
        needPermissions(actionHeader, "save", "trainAdDeviceManage");
        needPermissions(actionHeader, "list", "trainAdDeviceManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="showMachine"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript" src="TrainLineSelector.js"></script>
    <script type="text/javascript">
        actionHeader = "showMachine";
        nextUrl = "showMachineList.jsp";
        formOptions.beforePost = function() {

        };
        formOptions.afterLoad = function() {

        };

        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var adViewForm = new FortuneViewForm({
                title: '<fts:text name="showMachine"/>基本信息',
                url: 'showMachine!save.action',
                saveUrl: 'showMachine!save.action',
                viewUrl: 'showMachine!view.action',
                items: [
                    {name: 'obj.id', inputType: 'hidden', value: '-1'},
                    {fieldLabel: '<fts:text name="showMachine.name"/>', name: 'obj.name', readOnly: viewReadOnly},
                    {fieldLabel: '<fts:text name="showMachine.sn"/>', name: 'obj.sn', readOnly: viewReadOnly},
                    {
                        name:'obj.trainId',
                        id:'obj.trainId',
                        inputType:'hidden'
                    },
                    new Ext.form.SelectField({
                        fieldLabel:'车辆',
                        name:'obj.trainName',
                        id:'obj.trainName',
                        listeners:{
                            select:selectTrain
                        },
                        readOnly:true
                    }),
                    new FortuneCombo(
                            {
                                fieldLabel:'<fts:text name="showMachine.type"/>',
                                hiddenName:'obj.type',
                                hiddenId:'obj.type',
                                store:getDictStore('showMachineType'),
                                readOnly:viewReadOnly
                            }),
                    new FortuneCombo(
                            {
                                fieldLabel:'<fts:text name="showMachine.status"/>',
                                hiddenName:'obj.status',
                                hiddenId:'obj.status',
                                store:getDictStore('status'),
                                readOnly:viewReadOnly
                            }),
                    {fieldLabel: '<fts:text name="showMachine.position"/>', name: 'obj.position', readOnly: viewReadOnly, xtype: 'textarea'}
                ]
            });
            loadFormAjax();
            adViewForm.render('displayDiv');
        }
        Ext.onReady(function () {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            queueFunctions([
                {
                    func: initDictStores,
                    done: false,
                    flag: 'initDictStores'
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