<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
//        needPermissions(actionHeader,"list","deviceList");
//        needPermissions(actionHeader,"list","channelRecommendList");
//        needPermissions(actionHeader,"list","contentModuleList");
//        needPermissions(actionHeader,"searchAll","channelSearchAll");
        needPermissions(actionHeader, "deleteRecommendContent", "SpRecommendContentManage,contentDeleteRecommendContent");
        needPermissions(actionHeader, "recommendView", "SpRecommendContentManage");
        needPermissions(actionHeader, "spRecommendSearch", "SpRecommendContentManage,contentSpRecommendSearch");
        needPermissions(actionHeader, "addRecommendContent", "SpRecommendContentManage,contentAddRecommendContent");
        needPermissions(actionHeader, "changeRecommendDisplayOrder", "ChangeRecommendDisplayOrder，SpRecommendContentManage");
        needPermissions(actionHeader, "changeRecommendChannel", "contentChangeRecommendChannel，SpRecommendContentManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script language="javascript">
Ext.onReady(function () {
    <%
        long spId = 0;
        long adminId = 0;
        if (admin != null && admin.getCspId()!=null){
            spId = admin.getCspId();
            adminId = admin.getId();
        }

    %>
    var adminId = <%=adminId%>;
    var spId =<%=spId%>;

    var pageSize = 12;
    var tableWidth = 750;
    var tableHeight = 500;


    var actionUrl = "/content/content";

    //刷新列表
    function loadData() {
        var form1 = searchForm.getForm();
        //dataListStore.removeAll();
/*
        Ext.Ajax.request({
            url:actionUrl + "!spRecommendSearch.action?cc_cspId=" + spId,
            params:form1.getValues(),
            callback : function(opt, success, response) {
                if (success) {
                    alert(response.responseText);
                    var serverResult = Ext.util.JSON.decode(response.responseText);
                    if (serverResult) {

                    } else {
                        Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + serverResult.error)
                    }
                }
            }
        });
*/
///*
        dataListStore.baseParams = form1.getValues();
        dataListStore.load({params:{start:listGrid.toolbars[1].cursor, limit:pageSize},callback:function (record, options, success) {
            if (success) {
                //alert(dataListStore.getCount());
            }
        }});
//*/
        //listGrid.getBottomToolbar().updateInfo();
    }

    var channelCom;

    function channelData() {
       channelStore.load({params:{start:0, limit:1000000},
           callback:function (r, options, success) {
                if (success) {
                    channelStore.insert(0, new Ext.data.Record({id:'', name:'全部'}, ''));
                    channelCom.setValue(channelStore.getAt(0).get("id"))      ;
                    loadData();
                }
            }
        })

}
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:actionUrl + "!spRecommendSearch.action?cc_cspId=" + spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'cr_id'
            },
            {
                name:'c_id'
            },
            {
                name:'c_name'
            },
            {
                name:'c_actors'
            },
            {
                name:'c_directors'
            },
            {
                name:'c_moduleId'
            },
            {
                name:'c_createTime'
            },
            {
                name:'c_validEndTime'
            },
            {
                name:'c_onlineTime'
            },
            {
                name:'c_cspId'
            },
            {
                name:'c_deviceId'
            },
            {
                name:'c_status'
            },
            {
                name:'cc_status'
            },
            {
                name:'cr_recommendId'
            },
            {
                name:'cr_channelId'
            },
            {
                name:'cr_displayOrder'
            }

        ]
    });
    dataListStore.setDefaultSort('c_id', 'desc');



    var channelStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:"/publish/channel!searchAll.action?type=1&cspId=" + spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            }
        ]
    });
    channelStore.setDefaultSort("id", "ASC");
    channelStore.load({params:{start:0, limit:1000000},
        callback:function (r, options, success) {
            if (success == true) {
                channelStore.insert(0, new Ext.data.Record({id:'', name:'全部'}, ''));
            }

        }
    });

    var recommendStore = new Ext.data.JsonStore({
        method:'POST',
        baseParams:{limit:1000000},
        url:"/publish/recommend!list.action?obj.cspId=" + spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            },
            {
                name:'type'
            }
        ]
    });
    recommendStore.load();

    var deviceStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/system/device!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            }
        ]
    });
    deviceStore.load();

    var moduleStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/module/module!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            }
        ]
    });
    moduleStore.load();


    var toolForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'10px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'toolForm',
        width:'100%',
        height:30,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:5},
        baseCls:'x-plain',

        items:[
            {
                baseCls:'x-plain',
                layout:'form',
                items:[
                    {
                        xtype:'label',
                        labelSeparator:'',
                        fieldLabel:'工具栏:'
                    }
                ]
            },
            {
                baseCls:'x-btn-over',
                layout:'table',
                layoutConfig:{columns:6},
                items:[
                    {
                        text:'更改显示顺序',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var uploadData = "";
                                for (i = 0; i < listGrid.getStore().getCount(); i++) {
                                    storeRecord = listGrid.getStore().getAt(i);
                                    if (storeRecord.dirty) {
                                        uploadData += storeRecord.data.cr_id + "_" + storeRecord.data.cr_displayOrder + ",";
                                    }
                                }

                                if (uploadData != "") {
                                    uploadData = uploadData.substring(0, uploadData.length - 1);
                                    var remoteRequestStore = new Ext.data.JsonStore({
                                        method:'POST',
                                        url:actionUrl + "!changeRecommendDisplayOrder.action"
                                    });
                                    remoteRequestStore.reload({
                                        params:{uploadData:uploadData},
                                        callback:function (records, options, success) {
                                            var returnData = this.reader.jsonData;
                                            if (returnData.success) {
                                                //刷新列表
                                                loadData();

                                            } else {
                                                Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    }

                ]
            },
            {
                baseCls:'x-btn-over',
                layout:'table',
                layoutConfig:{columns:6},
                items:[
                    {
                        text:'更改频道',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var uploadData = "";
                                for (i = 0; i < listGrid.getStore().getCount(); i++) {
                                    storeRecord = listGrid.getStore().getAt(i);
                                    if (storeRecord.dirty) {
                                        uploadData += storeRecord.data.cr_id + "_" + storeRecord.data.cr_channelId + ",";
                                    }
                                }

                                if (uploadData != "") {
                                    uploadData = uploadData.substring(0, uploadData.length - 1);
                                    var remoteRequestStore = new Ext.data.JsonStore({
                                        method:'POST',
                                        url:actionUrl + "!changeRecommendChannel.action"
                                    });
                                    remoteRequestStore.reload({
                                        params:{uploadData:uploadData},
                                        callback:function (records, options, success) {
                                            var returnData = this.reader.jsonData;
                                            if (returnData.success) {
                                                //刷新列表
                                                channelData();

                                            } else {
                                                Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    }

                ]
            },
            {
                baseCls:'x-btn-over',
                layout:'table',
                layoutConfig:{columns:6},
                items:[
                    {
                        text:'添加',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var recommendType = searchForm.getForm().findField('recommendType').getValue();
                                var recommendId = searchForm.getForm().findField('cr_recommendId').getValue();
                                var channelId = searchForm.getForm().findField('cr_channelId').getValue();

                                if (recommendType == '') {
                                    Ext.MessageBox.alert('提示', '请选择"推荐类型"');
                                    return;
                                }

                                if (recommendId == '') {
                                    Ext.MessageBox.alert('提示', '请选择"推荐"');
                                    return;
                                }
                                if (channelId == '') {
                                    Ext.MessageBox.alert('提示', '请选择"频道"');
                                    return;
                                }

                                var viewHtml = '<iframe  frameborder="0" scorlling="no" width="100%" height="100%" src="spRecommendSearch.jsp?recommendType=' + recommendType + '&recommendId=' + recommendId + '&channelId=' + channelId + '"></iframe>';
                                var viewWin = new Ext.Window({
                                    //title:"文件列表",
                                    x:120,
                                    y:5,
                                    width:tableWidth + 43,
                                    height:tableHeight + 118,
                                    closeAction:"hide",
                                    closable:true,
                                    bodyStyle:"padding:0px",
                                    plain:true,
                                    resizable:false,
                                    html:viewHtml,
                                    listeners:{
                                        "show":function () {//alert("显示");
                                        },
                                        "hide":function () {
                                            loadData();
                                        },
                                        "close":function () {//alert("关闭");
                                        }
                                    }
                                })
                                viewWin.show();
                            }
                        }
                    }
                ]
            },
            {
                baseCls:'x-btn-over',
                layout:'table',
                layoutConfig:{columns:6},
                items:[
                    {
                        text:'删除',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var recommendId = searchForm.getForm().findField('cr_recommendId').getValue();
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                var keyIds = "";
                                for (var i = 0; i < row.length; i++) {
                                    keyIds += row[i].get('cr_id') + ",";
                                }
                                keyIds = keyIds.substr(0, keyIds.length - 1);
                                Ext.MessageBox.confirm("请您确认操作", " 删除推荐资源 ", function (btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url:actionUrl + "!deleteRecommendContent.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds, recommendId:recommendId},
                                            callback:function (records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    loadData();
                                                    Ext.MessageBox.alert('提示', '操作成功');
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                                }

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                ]
            }
        ]
    });


    var searchForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'5px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'searchForm',
        width:'100%',
        height:95,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:4},
        baseCls:'x-plain',

        items:[
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:750,
                height:80,
                items:[
                    {
                        baseCls:'x-plain',
                        layout:'table',
                        layoutConfig:{columns:4},
                        items:[
                            /*                  {
                             baseCls: 'x-plain',
                             layout: 'form',
                             items: [
                             {
                             xtype: 'label',
                             labelSeparator : '',
                             fieldLabel: '搜索栏:'
                             }
                             ]
                             }
                             ,*/
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:65,
                                items:[
                                    {
                                        hiddenName:'recommendType',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　推荐类型',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:['id', 'name'],
                                            data:[
                                                ['1', '常规推荐'],
                                                ['2', '频道推荐']
                                            ]
                                        }),
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable:false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection:true,
                                        typeAhead:false,
                                        //allowBlank:false,
                                        listeners:{
                                            select:function (combo, record, index) {
                                                var datas = [];
                                                for (var i = 0; i < recommendStore.getCount(); i++) {
                                                    var storeRecord = recommendStore.getAt(i);
                                                    if (storeRecord.data.type == this.getValue()) {
                                                        datas[datas.length] = [storeRecord.data.id, storeRecord.data.name];
                                                    }
                                                }
                                                var obj = searchForm.getForm().findField('cr_recommendId');
                                                obj.clearValue();
                                                obj.store.loadData(datas);
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:60,
                                items:[
                                    channelCom = new Ext.form.ComboBox({
                                        hiddenName:'cr_channelId',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　频道',
                                        width:140,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:channelStore,
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable:false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection:true,
                                        typeAhead:false,
                                        //allowBlank:false,
                                        listeners:{
                                            select:function (combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    })
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:65,
                                items:[
                                    {
                                        hiddenName:'cr_recommendId',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　　推荐',
                                        width:205,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:["id", 'name'],
                                            data:[]
                                        }),
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable:false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection:true,
                                        typeAhead:false,
                                        //allowBlank:false,
                                        listeners:{
                                            select:function (combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                items:[
                                    {
                                        xtype:'label',
                                        labelSeparator:'',
                                        fieldLabel:'　'
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:65,
                                items:[
                                    {
                                        hiddenName:'cc_status',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　SP状态',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:['id', 'name'],
                                            data:[
                                                ['1', '下线'],
                                                ['2', '上线']
                                            ]
                                        }),
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable:false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection:true,
                                        typeAhead:false,
                                        //allowBlank:false,
                                        listeners:{
                                            select:function (combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:60,
                                items:[
                                    {
                                        hiddenName:'c_status',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　CP状态',
                                        width:140,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:['id', 'name'],
                                            data:[
                                                ['1', '下线'],
                                                ['2', '上线']
                                            ]
                                        }),
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        mode:'local',
                                        loadingText:'加载中...',
                                        selectOnFocus:true,
                                        editable:false,
                                        typeAheadDelay:1000,
                                        //pageSize:5,
                                        forceSection:true,
                                        typeAhead:false,
                                        //allowBlank:false,
                                        listeners:{
                                            select:function (combo, record, index) {
                                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:65,
                                items:[
                                    {
                                        xtype:'textfield',
                                        fieldLabel:'　资源名称',
                                        labelWidth:60,
                                        name:'c_name',
                                        width:205
                                    }
                                ]
                            },

                            {
                                baseCls:'x-btn-over',
                                layout:'form',
                                items:[
                                    {
                                        text:'查询',
                                        xtype:'button',
                                        minWidth:60,

                                        listeners:{
                                            "click":function () {
                                                loadData();
                                            }
                                        }
                                    }
                                ]
                            }


                        ]
                    }
                ]
            })

        ]
    });


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect:false,
        checkOnly:true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.EditorGridPanel({
        title:"推荐设置",
        width:tableWidth + 35,
        height:tableHeight + 25,
        store:dataListStore,
        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },
        sm:checkSelect,
        columns:[
            checkSelect,
            {
                header:"ID",
                dataIndex:'c_id',
                hidden:true,
                width:50,
                sortable:true,
                align:'center'
            },
            {
                header:"名称",

                dataIndex:'c_name',
                width:130,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    if (canDoThisAction('recommendView')) {
                        return '<a href=\'javascript:viewObj("view","' + row.data.c_id + '",' + row.data.c_moduleId + ')\'>' + val + '</a>';
                    }
                }
            },

            {
                header:"主演",
                dataIndex:'c_actors',
                width:100,
                sortable:true,
                align:'center'
            },
            {
                header:"导演",
                dataIndex:'c_directors',
                width:100,
                sortable:true,
                align:'center'
            },
            {
                header:"频道",
                width:140,
                sortable:true,
                align:'center',
                dataIndex:'cr_channelId',
                editor:new Ext.form.ComboBox({
                    hiddenName:'cr_channelId',
                    width:140,
                    store:channelStore,
                    emptyText:'请选择',
                    triggerAction:'all', // 显示所有下列数据
                    mode:'local',
                    valueField:'id',
                    displayField:'name',
                    selectOnFocus:true, //获取焦点时自动选择字段
                    editable:false //是否允许输入

                }),
                renderer:function (v, p, r) {
                    var index = channelStore.find('id', v);
                    if (index != -1) {
                        return channelStore.getAt(index).data.name;      //返回记录集中的value字段的值
                    }

                }
            },
            {
                header:"资源模版",
                dataIndex:'c_moduleId',
                width:105,
                hidden:true,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = moduleStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return "";
                }
            },
            {
                header:"入库时间",
                dataIndex:'c_createTime',
                width:120,
                sortable:true,
                align:'center'
            },
            {
                header:"CP状态",
                dataIndex:'c_status',
                width:60,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    switch (parseInt(val)) {
                        case 1:
                            return '下线';
                        case 2:
                            return '上线';
                        case 3:
                            return '申请上线';
                        case 5:
                            return '审核拒绝上线';
                        case 6:
                            return '申请下线';
                        case 7:
                            return '审核拒绝下线';
                        case 8:
                            return '回收站';
                        case 9:
                            return '已删除';
                    }
                }
            },
            {
                header:"SP状态",
                dataIndex:'cc_status',
                width:60,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    switch (parseInt(val)) {
                        case 0:
                            return '新资源';
                        case 1:
                            return '下线';
                        case 2:
                            return '上线';
                        case 3:
                            return '申请上线';
                        case 4:
                            return '已审核上线';
                        case 5:
                            return '审核拒绝上线';
                        case 6:
                            return '申请下线';
                        case 7:
                            return '审核拒绝下线';
                        case 8:
                            return '回收站';
                        case 9:
                            return '已删除';
                    }
                }
            },
            {
                header:"显示顺序",
                dataIndex:'cr_displayOrder',
                width:65,
                sortable:true,
                align:'center',
                editor:new Ext.form.TextField({
                    allowBlank:false
                })
            }


        ],

        tbar:new Ext.Toolbar({items:[

                    new Ext.Panel({
                        style:{'margin-left':'0px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                        id:'searchForm',
                        width:'100%',
                        height:139,
                        labelWidth:60,
                        frame:true,
                        layout:'table',
                        layoutConfig:{columns:1},
                        baseCls:'x-plain',

                        items:[
                            searchForm, toolForm
                        ]
                    })
                ]}
        ),

        bbar:new Ext.PagingToolbar({
            pageSize:pageSize,
            store:dataListStore,
            displayInfo:true,
            displayMsg:'结果数据 {0} - {1} of {2}',
            emptyMsg:"没有数据",
            items:[

            ]
        })
    });
    //dataListStore.load();


    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action, id, moduleId) {
        document.location = 'contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId;
    }
})
</script>
</head>
<body>
<table align="center" width="760">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>