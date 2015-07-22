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
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"auditView","CpAuditResultManage");
       needPermissions(actionHeader,"cpAuditResultSearch","CpAuditResultManage,contentCpAuditResultSearch");
//        needPermissions(actionHeader,"list","deviceList");
//        needPermissions(actionHeader,"list","contentModuleList");
//        needPermissions(actionHeader,"list","systemAdminList");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
Ext.onReady(function(){
<%
    long cpId = 0;
    long adminId = 0;
    if (admin != null && admin.getCspId()!=null){
        cpId = admin.getCspId();
        adminId = admin.getId();
    }

%>
    var adminId = <%=adminId%>;
    var cpId=<%=cpId%>;

    var pageSize = 14;
    var tableWidth = 750;
    var tableHeight = 500;


    var actionUrl = "/content/content";
    
    //刷新列表
    function loadData(){
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:0,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    
    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!cpAuditResultSearch.action",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'ca_id'},
                    {name:'c_id'},
                    {name:'c_name'},
                    {name:'c_actors'},
                    {name:'c_directors'},
                    {name:'c_moduleId'},
                    {name:'c_createTime'},
                    {name:'c_validEndTime'},
                    {name:'c_onlineTime'},
                    {name:'c_cspId'},
                    {name:'c_deviceId'},
                    {name:'c_status'},
                    {name:'ca_type'},
                    {name:'ca_applyTime'},
                    {name:'ca_result'},
                    {name:'ca_resultMsg'},
                    {name:'ca_auditAdminId'},
                    {name:'ca_auditTime'}

            ]
    });
    dataListStore.setDefaultSort('c_id','desc');

    var deviceStore = new Ext.data.JsonStore({
         method:'POST',
         url:"/system/device!list.action",
         baseParams:{limit:1000000},
         totalProperty:'totalCount',
         root:'objs',
         fields:[
             {name:'id'},
             {name:'name'}
         ]
     });
    deviceStore.load({
        callback :
                function(records,options,success){
                    if(success){
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
             {name:'id'},
             {name:'name'}
         ]
     });
    moduleStore.load({
        callback :
                function(records,options,success){
                    if(success){
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
             {name:'id'},
             {name:'login'},
             {name:'realName'}
         ]
     });
    adminStore.load({
        callback :
                function(records,options,success){
                    if(success){
                        sequenceDo();
                    }
                }
    });

    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == 3){
            loadData();
            //dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }


    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 95,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
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
                layoutConfig: {columns:5},
                items: [
    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 82,
                        items: [
                            {
                                hiddenName:'ca_type',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　审核类型',
                                width:100,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store:  new Ext.data.ArrayStore({
                                        fields: ['id', 'name'],
                                        data: [['', '全部'], ['1', 'CP下线'], ['2', 'CP上线']]
                                    }),
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

                            }]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 83,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　开始时间',
                                width:100
                            }]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 89,
                        items: [
                            {
                                name:'c_name',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '　　　资源名称',
                                width:145
                            }]
                    },  {
                        baseCls: 'x-plain',
                        layout: 'form',
                        items: [
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }]
                    }  ,    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        items: [
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }]
                    }  ,
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 82,
                        items: [
                            {
                                hiddenName:'ca_result',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　审核结果',
                                width:100,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store:  new Ext.data.ArrayStore({
                                        fields: ['id', 'name'],
                                        data: [['0', '待审'], ['1', '通过'], ['2', '拒绝']]
                                    }),
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

                            }]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 83,
                        items: [
                            {
                                name:'endDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　结束时间',
                                width:100
                            }]
                    }   ,   {
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
            } ]
                       })
        ]
    });



    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
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

        columns: [
                {header: "ID", dataIndex: 'c_id', width: 50, hidden:true,sortable: true, align:'left'},
                {header: "名称", dataIndex: 'c_name', width: 130, sortable: true, align:'left',
                    renderer:
                        function (val,p,row){
                            if(canDoThisAction('auditView'))     {
                            return '<a href=\'javascript:viewObj("view","'+row.data.c_id+'",'+row.data.c_moduleId+')\'>'+ val +'</a>';
                        }}
                },

                {header: "主演", dataIndex: 'c_actors', width: 120, sortable: true, align:'center'},
                {header: "导演", dataIndex: 'c_directors', width: 100, sortable: true, align:'center'},
                {header: "资源模版", dataIndex: 'c_moduleId', width: 100, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = moduleStore.reader.jsonData.objs;
                        if (objs){
                            for (var i=0; i<objs.length; i++){
                                if (objs[i].id == val){
                                    return objs[i].name
                                }
                            }
                        }
                        return "";
                    }
                },
                {header: "入库时间", dataIndex: 'c_createTime', width: 120, sortable: true, align:'center'},
                {header: "审核类型", dataIndex: 'ca_type', width: 65, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
                            case 1:return 'CP下线';
                            case 2:return 'CP上线';
                            case 3:return 'SP下线';
                            case 4:return 'SP上线';
                        }
                    }
                },
                {header: "申请时间", dataIndex: 'ca_applyTime', width: 120, sortable: true, align:'center'},
                {header: "审核结果", dataIndex: 'ca_result', width: 65, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
                            case 0:return '待审';
                            case 1:return '通过';
                            case 2:return '拒绝';
                        }
                    }
                },
                {header: "拒绝理由", dataIndex: 'ca_resultMsg', width: 70, sortable: true, align:'center'},
                {header: "审核人", dataIndex: 'ca_auditAdminId', width: 60, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = adminStore.reader.jsonData.objs;
                        if (objs){
                            for (var i=0; i<objs.length; i++){
                                if (objs[i].id == val){
                                    return objs[i].login
                                }
                            }
                        }
                        return "";
                    }
                },
                {header: "审核时间", dataIndex: 'ca_auditTime', width: 118, sortable: true, align:'center'}

        ],

        tbar:new Ext.Toolbar({items:[

                        new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:105,
                            labelWidth: 60,
                            frame:true,
                            layout:'table',
                            layoutConfig: {columns:1},
                            baseCls: 'x-plain',

                            items: [
                                    searchForm
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
    this.viewObj = function (action,id,moduleId){
        document.location='contentViewOnly.jsp?action='+ action +'&id='+ id +'&moduleId='+moduleId;
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