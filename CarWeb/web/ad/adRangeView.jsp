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
    String actionHeader = "adRange";
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
    <title><fts:text name="adRange"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript" src="AdDesertSelector.js"></script>
    <script type="text/javascript" src="../train/TrainLineSelector.js"></script>
    <script type="text/javascript">
        actionHeader = "adRange";
        nextUrl = "adRangeList.jsp";
        var adStore = new Ext.data.JsonStore({
            method: 'POST',
            url: "ad!list.action",
            root: 'objs',
            fields: [
                {name: 'id'},
                {name: 'name'}
            ]
        });
        function playDesertTypeChanged() {

        }
        function adSelected() {

        }
        var desertTypeDescription = "";
        function onDesertSelected(id, name) {
            setCmpValue("obj.cid", id);
            setCmpValue("obj.desertName",desertTypeDescription+name);
        }
        function selectDesert() {
            var url;
            var desertType = getCmpValue('obj.type');
            if(desertType == "2"){
                url = "../content/content!list.action";
                desertTypeDescription = "媒体：";
            }else{
                url = "../publish/channel!searchAll.action?cspId=1";
                desertTypeDescription = "栏目：";
            }
            openSelectDesertWin({url: url, selectedFunc: onDesertSelected});
        }
        formOptions.beforePost=function(){
            var adId = getCmpValue("obj.adId");
            if(adId==null||adId==""||parseInt(adId)<=0){
                alert("请选择广告素材");
                return false;
            }
            var type = getCmpValue("obj.type");
            if(type==null||parseInt(type)<=0){
                alert("请选择投放目标方式");
                return false;
            }
            var desertName = getCmpValue("obj.desertName");
            if(desertName==null||desertName==""){
                alert("请选择投放目标内容");
                return false;
            }
            var desertId = getCmpValue("obj.cid");
            if(desertId==null||desertId==""||parseInt(desertId)<=0){
                alert("请选择投放目标内容");
                return false;
            }
            for(var i= 0,l=adStore.getCount();i<l;i++){
                var rec = adStore.getAt(i);
                if(rec.get("id")==adId){
                    setCmpValue("obj.adName",rec.get("name"));
                    break;
                }
            }
            return true;
        };
        function initAllStore() {
            adStore.load({callback: function () {
                functionDone('initAllStore');
            }});
        }
        function initDisplay() {
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            var adRangeViewForm = new FortuneViewForm({
                title: '<fts:text name="adRange"/>基本信息',
                url: 'adRange!save.action',
                saveUrl: 'adRange!save.action',
                viewUrl: 'adRange!view.action',
                items: [
                    //{name:'keyId',value:keyId,inputType:'hidden'},
                    {name: 'obj.id', inputType: 'hidden', value: '-1'},
                    new FortuneCombo({
                        fieldLabel: '<fts:text name="adRange.adName"/>',
                        hiddenName: 'obj.adId',
                        hiddenId: 'obj.adId',
                        allowBlank:false,
                        valueField:'id',displayField:'name',
                        listeners: {
                            select: adSelected
                        },
                        store: adStore
                    }),
                    new FortuneCombo({
                        fieldLabel: '<fts:text name="adRange.pos"/>',
                        hiddenName: 'obj.pos',
                        store: getDictStore('adPlayPos')
                    }),

                    new FortuneCombo({
                        fieldLabel: '<fts:text name="adRange.type"/>',
                        hiddenName: 'obj.type',
                        listeners: {
                            select: playDesertTypeChanged
                        },
                        allowBlank:false,
                        value:'1',
                        store: getDictStore('adPlayDesert')
                    }),
                    new Ext.form.SelectField({
                        allowBlank:false,
                        fieldLabel: '<fts:text name="adRange.desertName"/>',
                        name: 'obj.desertName',
                        id: 'obj.desertName',
                        listeners: {
                            select: selectDesert
                        },
                        readOnly: true
                    }),
                    new Ext.form.SelectField({
                        fieldLabel:'<fts:text name="adRange.lineName"/>',
                        name:'obj.lineName',
                        id:'obj.lineName',
                        listeners:{
                            select:selectTrainLineForAd
                        },
                        readOnly:true
                    }),
                    {name: 'obj.cid',id:'obj.cid', inputType: 'hidden'},
                    {name: 'obj.lineId',id:'obj.lineId', inputType: 'hidden'},
                    {name: 'obj.adName',id:'obj.adName', inputType: 'hidden'}
                ]
            });
            loadFormAjax();
            adRangeViewForm.render('displayDiv');
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
                            func: initAllStore,
                            done: false,
                            flag: 'initAllStore'
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