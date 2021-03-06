<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"contentView","contentViewOnly");
        needPermissions(actionHeader,"spAuditSearch","systemContentSpAudit,systemSpContentAuditOnlineList");
        needPermissions(actionHeader,"contentAudit","systemContentSpAudit,contentContentAudit");
    //    needPermissions(actionHeader,"getCspByAuditAdmin","cspGetCspByAuditAdmin");
     //   needPermissions(actionHeader,"getContentRegUrl","contentGetContentRegUrl");
      //  needPermissions(actionHeader,"list","contentModuleList");
        //needPermissions(actionHeader,"list","deviceList");
      //  needPermissions(actionHeader,"list","cspSearch");
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
Ext.onReady(function() {
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
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:0,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }

    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!spAuditSearch.action?ca_type=3&cadmin_spOffline=1&cadmin_adminId=" + adminId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'ca_id'
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
                name:'cc_cspId'
            },
            {
                name:'cc_status'
            },
            {
                name:'cc_statusTime'
            }
        ]
    });
    dataListStore.setDefaultSort('c_id', 'desc');


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
    deviceStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });

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
    moduleStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });
    var cspStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/csp/csp!list.action",
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
    cspStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });
    var sequence = 0;

    function sequenceDo() {
        sequence++;
        if (sequence == 3) {
            dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }


    var toolForm = new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '10px','margin-right':'0px','margin-bottom':'0px'},
        id:'toolForm',
        width: '100%',
        height : 30,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:3},
        baseCls: 'x-plain',

        items: [
            {
                baseCls: 'x-plain',
                layout: 'form',
                items: [
                    {
                        xtype: 'label',
                        labelSeparator : '',
                        fieldLabel: '工具栏:'
                    }
                ]
            },
            {
                baseCls: 'x-btn-over',
                layout:'table',
                layoutConfig: {columns:6},
                items:[
                    {
                        text: '通过',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                var keyIds = "";
                                for (var i = 0; i < row.length; i++) {
                                    keyIds += row[i].get('ca_id') + ",";
                                }
                                keyIds = keyIds.substr(0, keyIds.length - 1);
                                Ext.MessageBox.confirm("请您确认操作", " 通过审核 ", function(btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url: actionUrl + "!contentAudit.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,result:1,adminId:adminId},
                                            callback :
                                                    function(records, options, success) {
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
                    },

                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth:70,
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'resultMsg',
                                fieldLabel: '　拒绝理由'
                            }
                        ]
                    },

                    {
                        baseCls: 'x-btn-over',
                        layout:'column',
                        items:[
                            {
                                text: '拒绝',
                                xtype:'button',
                                minWidth:60,
                                listeners:{
                                    "click":function()
                                    {
                                        var row = listGrid.getSelectionModel().getSelections();
                                        if (row.length == 0) {
                                            Ext.Msg.alert("提示", "未选择记录！");
                                            return;
                                        }
                                        var resultMsg = toolForm.getForm().findField('resultMsg').getValue();
                                        if (resultMsg == '') {
                                            Ext.Msg.alert("提示", "未填写拒绝理由！");
                                            return;
                                        }
                                        var keyIds = "";
                                        for (var i = 0; i < row.length; i++) {
                                            keyIds += row[i].get('ca_id') + ",";
                                        }
                                        keyIds = keyIds.substr(0, keyIds.length - 1);
                                        Ext.MessageBox.confirm("请您确认操作", " 拒绝审核 ", function(btn) {
                                            if (btn == "yes") {

                                                var remoteRequestStore = new Ext.data.JsonStore({
                                                    method:'POST',
                                                    url: actionUrl + "!contentAudit.action"
                                                });
                                                remoteRequestStore.reload({
                                                    params:{keyIds:keyIds,result:2,resultMsg:resultMsg,adminId:adminId},
                                                    callback :
                                                            function(records, options, success) {
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
            }
        ]
    });


    var cspComboStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/csp/csp!getCspByAuditAdmin.action",
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
    cspComboStore.load({params:{adminId:adminId,auditType:'spOffline',cspType:'isSp', start:0, limit:10000000}});

    var searchForm = new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 95,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:1},
        baseCls: 'x-plain',

          items: [
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:700,
                height:80,
                items:[
            {
                baseCls: 'x-plain',
                layout:'table',
                layoutConfig: {columns:4},
                items: [
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 60,
                        items: [
                            {
                                hiddenName:'cc_cspId',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　　SP',
                                width:140,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store: cspComboStore,
                                valueField: 'id',
                                displayField: 'name',
                                //value:'1',
                                //mode: 'remote',
                                mode:'local',
                                loadingText:'加载中...',
                                selectOnFocus:true,
                                editable: false,
                                typeAheadDelay:1000,
                                //pageSize:5,
                                forceSection: true,
                                typeAhead: false,
                                //allowBlank:false,
                                listeners:{
                                    select: function(combo, record, index) {
                                        //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                    }
                                }

                            }
                        ]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 90,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　　开始时间',
                                width:100
                            }
                        ]
                    },    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 90,
                        items: [
                            {
                                name:'endDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　　结束时间',
                                width:100
                            }
                        ]
                    },
                       {
                        baseCls: 'x-plain',
                        layout: 'form',
                        items: [
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }
                        ]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 60,
                        items: [
                            {
                                name:'c_name',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '资源名称',
                                width:140
                            }
                        ]
                    },

                    {
                        baseCls: 'x-btn-over',
                        layout:'form',
                        items:[
                            {
                                text: '查询',
                                xtype:'button',
                                minWidth:60,
                                listeners:{
                                    "click":function()
                                    {
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
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"SP下线审核",
        width:tableWidth,
        height:tableHeight+20,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
            {
                header: "ID",
                dataIndex: 'c_id',
                hidden:true,
                width: 50,
                sortable: true,
                align:'left'
            },
            {
                header: "名称",
                dataIndex: 'c_name',
                width: 140,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            if(canDoThisAction('contentView')){
                            return '<a href=\'javascript:viewObj("view","' + row.data.c_id + '",' + row.data.c_moduleId + ')\'>' + val + '</a>';
                        }       }
            },

            {
                header: "主演",
                dataIndex: 'c_actors',
                width: 100,
                sortable: true,
                align:'center'
            },
            {
                header: "导演",
                dataIndex: 'c_directors',
                width: 100,
                sortable: true,
                align:'center'
            },
            {
                header: "资源模版",
                dataIndex: 'c_moduleId',
                width: 110,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
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
                header: "入库时间",
                dataIndex: 'c_createTime',
                width: 120,
                sortable: true,
                align:'center'
            },
            {
                header: "CP",
                dataIndex: 'c_cspId',
                width: 105,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    var objs = cspStore.reader.jsonData.objs;
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
                header: "SP",
                dataIndex: 'cc_cspId',
                width: 105,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    var objs = cspStore.reader.jsonData.objs;
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
                header: "设备",
                dataIndex: 'c_deviceId',
                width:100,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    var objs = deviceStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return "";
                }
            }

        ],

        tbar:new Ext.Toolbar({items:[

            new Ext.Panel({
                style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                id:'searchForm',
                width: '100%',
                height:140,
                labelWidth: 60,
                frame:true,
                layout:'table',
                layoutConfig: {columns:1},
                baseCls: 'x-plain',

                items: [
                    searchForm,
                    toolForm
                ]
            })
        ]}
                ),

        bbar:new Ext.PagingToolbar({
            pageSize: pageSize,
            store: dataListStore,
            displayInfo: true,
            displayMsg: '结果数据 {0} - {1} of {2}',
            emptyMsg: "没有数据",
            items:[

            ]
        })
    });


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