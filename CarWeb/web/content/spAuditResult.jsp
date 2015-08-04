<%@ page import="com.fortune.common.business.security.model.Admin" %><%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
      //  needPermissions(actionHeader,"list","deviceList");
    //    needPermissions(actionHeader,"getCspBySp","cspGetCspBySp");
      //  needPermissions(actionHeader,"list","contentModuleList");
     //   needPermissions(actionHeader,"list","systemAdminList");
        needPermissions(actionHeader,"spAuditResultSearch","spAuditResultContentManage,contentSpAuditResultSearch");
        needPermissions(actionHeader,"auditResultView","spAuditResultContentManage");
    }
%><%@include file="../inc/checkHeader.jsp" %>
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

    String caType = request.getParameter("ca_type");
    if (caType==null) caType="";

    String caResult = request.getParameter("ca_result");
    if (caResult==null) caResult="";


%>
    var adminId = <%=adminId%>;
    var spId =<%=spId%>;

    var pageSize = 14;
    var tableWidth = 750;
    var tableHeight = 500;


    var actionUrl = "/content/content";

    //刷新列表
    function loadData() {
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:listGrid.toolbars[1].cursor,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }

    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!spAuditResultSearch.action?cc_status=-1&cc_cspId=" + spId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'cc_id'},
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
                name:'cc_status'
            },
            {
                name:'cc_statusTime'
            },
            {
                name:'ca_type'
            },
            {
                name:'ca_applyTime'
            },
            {
                name:'ca_result'
            },
            {
                name:'ca_resultMsg'
            },
            {
                name:'ca_auditAdminId'
            },
            {
                name:'ca_auditTime'
            }

        ]
    });
    dataListStore.setDefaultSort('ca_applyTime', 'desc');


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

    var adminStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/security/admin!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'login'
            },
            {
                name:'realName'
            }
        ]
    });
    adminStore.load({
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
            loadData();
            //dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }

    var cpComboStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: "/csp/csp!getCspBySp.action",
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
    cpComboStore.load({params:{spId:spId, start:0, limit:10000000}});

    var toolForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'10px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'toolForm',
        width:'100%',
        height:35,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:3},
        baseCls:'x-plain',

        items:[
            {
                baseCls:'x-btn-over',
                layout:'table',
                layoutConfig:{columns:6},
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
                        text:'删除',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                var keyIds = "";
                                for (var i = 0; i < row.length; i++) {
                                    keyIds += row[i].get('cc_id') + ",";
                                }
                                keyIds = keyIds.substr(0, keyIds.length - 1);
                                Ext.MessageBox.confirm("请您确认操作", " 删除记录 ", function (btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url:actionUrl + "!spWait_audit.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds, spId:spId, status:-1},
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
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 92,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
        baseCls: 'x-plain',

        items: [
            new Ext.form.FieldSet({
                title:"搜索栏",
                width:698,
                height:80,
                items:[
                    {
                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:5},
                        items: [
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 90,
                                items: [
                                    {
                                        hiddenName:'c_cspId',
                                        xtype: 'combo',
                                        labelWidth: 90,
                                        fieldLabel: '　　　　　　CP',
                                        width:130,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store: cpComboStore,
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
                                labelWidth: 70,
                                items: [
                                    {
                                        hiddenName:'ca_type',
                                        xtype: 'combo',
                                        labelWidth: 90,
                                        fieldLabel: '　审核类型',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:  new Ext.data.ArrayStore({
                                            fields: ['id', 'name'],
                                            data: [
                                                ['3', 'SP下线'],
                                                ['4', 'SP上线']
                                            ]
                                        }),
                                        valueField: 'id',
                                        displayField: 'name',
                                        value:'<%=caType%>',
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
                                labelWidth:70,
                                items: [
                                    {
                                        name:'startDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d',
                                        labelWidth: 60,
                                        fieldLabel: '　开始时间',
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
                                labelWidth: 90,
                                items: [
                                    {
                                        name:'c_name',
                                        xtype: 'textfield',
                                        labelWidth: 60,
                                        fieldLabel: '　　　资源名称',
                                        width:130
                                    }
                                ]
                            },

                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 70,
                                items: [
                                    {
                                        hiddenName:'ca_result',
                                        xtype: 'combo',
                                        labelWidth: 90,
                                        fieldLabel: '　审核结果',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:  new Ext.data.ArrayStore({
                                            fields: ['id', 'name'],
                                            data: [
                                                ['0', '待审'],
                                                ['1', '通过'],
                                                ['2', '拒绝']
                                            ]
                                        }),
                                        valueField: 'id',
                                        displayField: 'name',
                                        value:'<%=caResult%>',
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
                                labelWidth: 70,
                                items: [
                                    {
                                        name:'endDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d',
                                        labelWidth: 60,
                                        fieldLabel: '　结束时间',
                                        width:100
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
                ]})

        ]
    });


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"审核情况查询",
        width:tableWidth,
        height:tableHeight+30,
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
                width: 50,
                hidden:true,
                sortable: true,
                align:'left'
            },
            {
                header: "名称",
                dataIndex: 'c_name',
                width: 150,
                sortable: true,
                align:'center',
                renderer:
                        function (val, p, row) {
                            if(canDoThisAction('auditResultView')){
                            return '<a href=\'javascript:viewObj("view","' + row.data.c_id + '",' + row.data.c_moduleId + ')\'>' + val + '</a>';
                        }  }
            },

            {
                header: "主演",
                dataIndex: 'c_actors',
                width: 120,
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
                width: 105,
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
                header: "CP状态",
                dataIndex: 'c_status',
                width: 55,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    switch (parseInt(val)) {
                        case 1:return '下线';
                        case 2:return '上线';
                        case 3:return '申请上线';
                        case 5:return '审核拒绝上线';
                        case 6:return '申请下线';
                        case 7:return '审核拒绝下线';
                        case 8:return '回收站';
                    }
                }
            },
            {
                header: "审核类型",
                dataIndex: 'ca_type',
                width: 65,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    switch (parseInt(val)) {
                        case 1:return 'CP下线';
                        case 2:return 'CP上线';
                        case 3:return 'SP下线';
                        case 4:return 'SP上线';
                    }
                }
            },
            {
                header: "申请时间",
                dataIndex: 'ca_applyTime',
                width: 120,
                sortable: true,
                align:'center'
            },
            {
                header: "审核结果",
                dataIndex: 'ca_result',
                width: 68,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    switch (parseInt(val)) {
                        case 0:return '待审';
                        case 1:return '通过';
                        case 2:return '拒绝';
                    }
                }
            },
            {
                header: "拒绝理由",
                dataIndex: 'ca_resultMsg',
                width:80,
                sortable: true,
                align:'center'
            },
            {
                header: "审核人",
                dataIndex: 'ca_auditAdminId',
                width:70,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    var objs = adminStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].login
                            }
                        }
                    }
                    return "";
                }
            },
            {
                header: "审核时间",
                dataIndex: 'ca_auditTime',
                width: 120,
                sortable: true,
                align:'center'
            }

        ],

        tbar:new Ext.Toolbar({items:[

            new Ext.Panel({
                style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                id:'searchForm',
                width: '100%',
                height:139,
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