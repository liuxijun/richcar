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
    String actionHeader = "ad";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "delete", "adManage");
        needPermissions(actionHeader, "deleteSelected", "adManage");
        needPermissions(actionHeader, "add", "adManage");
        needPermissions(actionHeader, "save", "adManage");
        needPermissions(actionHeader, "list", "adManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="ad"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        actionHeader = "ad";
        nextUrl = "adList.jsp";
        var cspDeviceStore = new Ext.data.JsonStore({
            method: 'POST',
            url: "/csp/cspDevice!getDevicesOfCsp.action",
            root: 'objs',
            fields: [
                {name: 'id'},
                {name: 'name'}
            ]
        });
        function initCspDeviceStore() {
            cspDeviceStore.load({callback: function (records, options, success) {
                if (success) {
                }
                functionDone('initCspDeviceStore');
            }});
        }
        formOptions.beforePost=function(){
            var start = (getCmpValue("startTime"));
            var stop = (getCmpValue("stopTime"));
            //alert(start+"->"+stop);
            var now = new Date();
            if(stop.getTime()<now.getTime()){
                if(!window.confirm("截至日期已过，您确认要保留此次修改么？\n截至时间："+stop+"\n当前时间："+now)){
                    return false;
                }
            }
            if(stop.getTime()<start.getTime()){
                if(!window.confirm("截至日期小于起始时间，您确认要保留此次修改么？\n起始时间："+start
                        +"\n截至时间："+stop)){
                    return false;
                }
            }
            return true;
        };
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var adViewForm = new FortuneViewForm({
                title: '<fts:text name="ad"/>基本信息',
                url: 'ad!save.action',
                saveUrl: 'ad!save.action',
                viewUrl: 'ad!view.action',
                items: [
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {name: 'obj.id', inputType: 'hidden', value: '-1'},
                    {fieldLabel: '广告名称', name: 'obj.name', readOnly: viewReadOnly},
                    new Ext.form.ComboBox({
                        fieldLabel: '媒体服务器',
                        hiddenName: 'obj.deviceId',
                        mode: 'local',
                        value: 1,
                        forceSelection: true,
                        triggerAction: "all",
                        editable: false,
                        store: cspDeviceStore,
                        valueField: 'id', displayField: 'name',
                        emptyText: '请选择...'/* ,
                         listeners: {
                         load:
                         function(combo) {
                         combo.setValue(1);
                         *//*this.combobox.selectIndex=1;*//*
                         }
                         }*/
                    }),
                    {fieldLabel: '播放连接', name: 'obj.url', readOnly: viewReadOnly},
                    {fieldLabel: '起始时间',id:'startTime', name: 'obj.startTime', readOnly: viewReadOnly,
                        format:'Y-m-d 00:00:00',xtype: 'datefield'},
                    {fieldLabel: '截至时间',id:'stopTime', name: 'obj.endTime', readOnly: viewReadOnly,
                        format:'Y-m-d 23:59:59',xtype: 'datefield'},
                    {fieldLabel: '广告时长', name: 'obj.adLength', readOnly: viewReadOnly},
                    new FortuneCombo({
                        fieldLabel: '广告类型',
                        hiddenName: 'obj.adPlayType',
                        mode: 'local',
                        value: 1,
                        forceSelection: true,
                        triggerAction: "all",
                        editable: false,
                        store: getDictStore('adPlayType'),
                        valueField: 'value', displayField: 'name',
                        emptyText: '请选择...'
                    }),

                    {fieldLabel: '<fts:text name="ad.playTime"/>', inputType: 'hidden', name: 'obj.playTime',
                        readOnly: viewReadOnly},
                    {fieldLabel: '<fts:text name="ad.isForce"/>', inputType: 'hidden', name: 'obj.isForce',
                        readOnly: viewReadOnly},
                    {fieldLabel: '广告描述', name: 'obj.desp', readOnly: viewReadOnly, xtype: 'textarea'}
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
                    func: initCspDeviceStore,
                    done: false,
                    flag: 'initCspDeviceStore'
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