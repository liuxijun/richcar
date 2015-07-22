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
    String actionHeader = "train";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "delete", "trainManage");
        needPermissions(actionHeader, "deleteSelected", "trainManage");
        needPermissions(actionHeader, "add", "trainManage");
        needPermissions(actionHeader, "save", "trainManage");
        needPermissions(actionHeader, "list", "trainManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="train"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript" src="TrainLineSelector.js"></script>
    <script type="text/javascript">
        actionHeader = "train";
        nextUrl = "trainList.jsp";
        var trainLineStore = new Ext.data.JsonStore({
            method: 'POST',
            url: "/train/trainLine!list.action?limit=10000",
            root: 'objs',
            fields: [
                {name: 'id'},
                {name: 'name'}
            ]
        });
        function initTrainLineStore() {
            trainLineStore.load({callback: function (records, options, success) {
                if (success) {
                }
                functionDone('initTrainLineStore');
            }});
        }
        formOptions.beforePost = function() {

        };
        formOptions.afterLoad = function() {
            setCmpValue('trainLineName',getStoreText(trainLineStore,getCmpValue('obj.trainLineId'),'id','name'));
        };

        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var adViewForm = new FortuneViewForm({
                title: '<fts:text name="train"/>基本信息',
                url: 'train!save.action',
                saveUrl: 'train!save.action',
                viewUrl: 'train!view.action',
                items: [
                    {name: 'obj.id', inputType: 'hidden', value: '-1'},
                    {fieldLabel: '<fts:text name="train.name"/>', name: 'obj.name', readOnly: viewReadOnly},
                    {fieldLabel: '<fts:text name="train.sn"/>', name: 'obj.sn', readOnly: viewReadOnly},
                    {fieldLabel: '<fts:text name="train.trainCode"/>', name: 'obj.trainCode', readOnly: viewReadOnly},
                    {
                        name:'obj.trainLineId',
                        id:'obj.trainLineId',
                        inputType:'hidden'
                    },
                    new Ext.form.SelectField({
                        fieldLabel:'区域',
                        name:'trainLineName',
                        id:'trainLineName',
                        listeners:{
                            select:selectTrainLine
                        },
                        readOnly:true
                    }),
                    new FortuneCombo(
                            {
                                fieldLabel:'<fts:text name="train.type"/>',
                                hiddenName:'obj.type',
                                hiddenId:'obj.type',
                                store:getDictStore('trainType'),
                                readOnly:viewReadOnly
                            }),
                    new FortuneCombo(
                            {
                                fieldLabel:'<fts:text name="train.status"/>',
                                hiddenName:'obj.status',
                                hiddenId:'obj.status',
                                store:getDictStore('status'),
                                readOnly:viewReadOnly
                            }),
                    {fieldLabel: '<fts:text name="train.description"/>', name: 'obj.description', readOnly: viewReadOnly, xtype: 'textarea'}
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
                },
                {
                    func: initTrainLineStore,
                    done: false,
                    flag: 'initTrainLineStore'
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