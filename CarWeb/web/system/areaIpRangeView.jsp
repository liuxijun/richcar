<%@ page import="com.fortune.util.StringUtils" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title><fts:text name="areaIpRange"/>管理</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript">
    Ext.onReady(function() {
        Ext.QuickTips.init();
        var areaId = <%=request.getParameter("obj.areaId")%>;
        var keyId = <%=request.getParameter("keyId")%>;

        var actionUrl = "/system/areaIpRange";
        var ipRangeCom;
        var ipRangeId1;
        var ipRange_ids = "";

        /*
         function getAreaNameById(id){
         return "工区";
         }
         formOptions.afterLoad=function(){
         setLabelText('areaName',areaId);
         };
         */

        Ext.Ajax.request({
            url:'area!searchAreaById.action?areaId= ' + areaId,
            method:'GET',
            callback:function(opt, success, response) {
                var serverData = Ext.util.JSON.decode(response.responseText);
                if (serverData.success) {
                    var areaName = serverData.data['obj.name'];
                    setLabelText('areaName', areaName);
                }
            }
        });

        var ipRangeStore = new Ext.data.JsonStore({
            fields:['id','name'],
            root: 'objs',
            totalProperty: 'totalCount',
            //  autoLoad:true,
            proxy:new Ext.data.HttpProxy({method:'GET',url:'ipRange!list.action?limit=10000'}),
            listeners:{
                load:function() {
                    Ext.Ajax.request({
                        url:actionUrl + '!view.action?keyId=' + keyId,
                        method:'GET',
                        callback:function(opt, success, response) {
                            var serverData = Ext.util.JSON.decode(response.responseText);
                            if (serverData.success) {
                                ipRangeId1 = serverData.data['obj.ipRangeId'];
                                var objId = serverData.data['obj.id'];
                                if (objId != null && objId != "") {
                                    viewPanel.getForm().findField('objId').setValue(objId);
                                }
                                var ipRangeId = ipRangeStore.getAt(0).get("id");
                                if (ipRangeId1 == 0) {
                                    ipRangeCom.setValue(ipRangeId);
                                } else {
                                    ipRangeCom.setValue(ipRangeId1);
                                }
                            }
                        }
                    });
                }
            }
        });

        var viewPanel = new Ext.FormPanel({
            title:"AreaIpRange基本信息",
            id:'viewForm',
            width: 350,
            labelWidth: 60,
            bodyStyle: 'padding:10px ',
            defaultType: 'textfield',
            layout:'form',
            items: [
                {
                    name:'obj.id',
                    inputType:'hidden',
                    id:'objId',
                    value:'-1'
                }                     ,
                {
                    inputType:'hidden',
                    name:'obj.areaId',
                    value:areaId,
                    id:'areaIds',
                    readOnly:viewReadOnly
                },
                {
                    fieldLabel:'所属地区',
                    name:'obj.areaName',
                    id:'areaName',
                    xtype:'label'
                } ,
                ipRangeCom = new Ext.form.ComboBox({
                    fieldLabel:'ipRangeId',
                    hiddenName: 'obj.ipRangeId',
                    mode:'remote',
                    valueField:'id',
                    displayField:'name',
                    store:ipRangeStore,
                    width:180,
                    forceSelection:true,
                    emptyText:'请选择...',
                    triggerAction:"all",
                    editable:false,
                    allowBlank : false,
                    readOnly:viewReadOnly
                })
            ],  buttons:[
                {
                    text: '保存数据',
                    xtype:'button',
                    minWidth:90,
                    listeners:{
                        "click":function()
                        {
                            if (viewPanel.getForm().isValid()) {
                                  viewPanel.getForm().submit({
                                        url:actionUrl + '!save.action',
                                        method: 'post',
                                        waitMsg: '正在处理数据，请稍后……',
                                        success: function (form, returnMsg) {
                                            Ext.Msg.alert("提示", "操作成功！", showResult);
                                            function showResult() {
                                                window.location.href = "areaIpRangeList.jsp?obj.areaId=" + areaId;
                                            }
                                        },
                                        failure: function (form, returnMsg) {
                                            Ext.MessageBox.show({
                                                title: '提示',
                                                msg: '操作失败，原因是:' + returnMsg.result.error,
                                                buttons: Ext.MessageBox.OK,
                                                icon: Ext.MessageBox.ERROR
                                            });
                                        },
                                        scope: this
                                    });
                                }
                               

                            }
                        }

                },
                {
                    xtype : 'button',
                    text:'重新加载',
                    minWidth:90,
                    handler:function() {
                        if (keyId == "-1") {   //添加时
                            viewPanel.getForm().reset();
                            var ipRangeIds = ipRangeCom.getValue();
                            if (ipRangeIds == null || ipRangeIds == 0)
                            {
                                var ipRangeId = ipRangeStore.getAt(0).get("id");
                                ipRangeCom.setValue(ipRangeId);
                            }
                        }
                        else {  //查看时
                            ipRangeCom.setValue(ipRangeId1);
                        }
                    }
                }
            ]
        })

        ipRangeStore.load();

        viewPanel.render('displayDiv');

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