<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
   //     needPermissions(actionHeader,"list","deviceList");
  //      needPermissions(actionHeader,"getCspModuleList","moduleGetModuleList");
        needPermissions(actionHeader,"cpSearch","contentCpSearch");
  //      needPermissions(actionHeader,"save","contentSave");
        needPermissions(actionHeader,"digitalRightView","cpDigitalRightManage");
    //    needPermissions(actionHeader,"list","contentModuleList");
   //     needPermissions(actionHeader,"getCspDevice","deviceGetCspDevice");
        needPermissions(actionHeader,"updateDigitalRight","cpDigitalRightManage,contentUpdateDigitalRight");
        needPermissions(actionHeader,"updateDigitalRightBySearch","cpDigitalRightManage,contentUpdateDigitalRightBySearch");

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
    if (admin != null && admin.getCspId()!=null){
        cpId = admin.getCspId();
    }
%>
    var cpId=<%=cpId%>;

    var pageSize = 13;
    var tableWidth = 750;
    var tableHeight = 500;

    var actionUrl = "/content/content";

    //刷新列表
    //刷新列表

    function loadData(){
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params:{start:listGrid.toolbars[1].cursor,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }

    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!cpSearch.action?status12=3&c_cspId="+cpId,
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'c_id'},
                    {name:'c_name'},
                    {name:'c_actors'},
                    {name:'c_directors'},
                    {name:'c_moduleId'},
                    {name:'c_createTime'},
                    {name:'c_validStartTime'},                    
                    {name:'c_validEndTime'},
                    {name:'c_digiRightUrl'},                    
                    {name:'c_onlineTime'},
                    {name:'c_cspId'},
                    {name:'c_status'},
                    {name:'c_deviceId'}
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

    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == 2){
            dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }

    var moduleComboStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: "/module/module!getCspModuleList.action",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'id'},
                    {name:'name'}
            ]
    });
    moduleComboStore.load({params:{cpId:cpId, start:0, limit:10000000}});

    var toolForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '10px','margin-right':'0px','margin-bottom':'0px'},
        id:'toolForm',
        width: '100%',
        height : 45,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:6},
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
                    }]
            },
            {
                baseCls: 'x-plain',
                layout: 'form',
                labelWidth: 60,
                items: [
                    {
                        name:'rightStartTime',
                        xtype: 'datefield',
                        format:'Y-m-d',
                        labelWidth: 60,
                        fieldLabel: '版权开始',
                        width:100
                    }]
            },
            {
                baseCls: 'x-plain',
                layout: 'form',
                labelWidth: 70,
                items: [
                    {
                        name:'rightEndTime',
                        xtype: 'datefield',
                        format:'Y-m-d',
                        labelWidth: 60,
                        fieldLabel: '　版权到期',
                        width:100
                    }]
            },
            {
                baseCls: 'x-plain',
                layout: 'form',
                labelWidth: 70,
                items: [
                    {
                        name:'rightUrl',
                        xtype: 'textfield',
                        format:'Y-m-d',
                        labelWidth: 60,
                        fieldLabel: '　版权链接',
                        width:100
                    }]
            },

            {
                baseCls: 'x-btn-over',
                layout:'column',
                layoutConfig: {columns:6},
                items:[
                    {
                        text: '更新',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length ==0){
                                    Ext.Msg.alert("提示","未选择记录！");
                                    return;
                                }
                                var rightStartTime = toolForm.getForm().findField("rightStartTime").getValue();
                                var rightEndTime = toolForm.getForm().findField("rightEndTime").getValue();
                                var rightUrl = toolForm.getForm().findField("rightUrl").getValue();
                                if (rightStartTime==''){
                                    Ext.Msg.alert("提示","请填写版权开始！");
                                    return;
                                }
                                if (rightEndTime==''){
                                    Ext.Msg.alert("提示","请填写版权到期！");
                                    return;
                                }
                                var keyIds="";
                                for(var i=0; i<row.length; i++){
                                    keyIds +=  row[i].get('c_id') + ",";
                                }
                                keyIds = keyIds.substr(0,keyIds.length-1);
                                Ext.MessageBox.confirm("请您确认操作", " 更新版权 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!updateDigitalRight.action"
                                        });                                        
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,rightStartTime:rightStartTime,rightEndTime:rightEndTime,rightUrl:rightUrl},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                                        }

                                                    }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '按查询结果更新',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var rightStartTime = toolForm.getForm().findField("rightStartTime").getValue();
                                var rightEndTime = toolForm.getForm().findField("rightEndTime").getValue();
                                var rightUrl = toolForm.getForm().findField("rightUrl").getValue();
                                if (rightStartTime==''){
                                    Ext.Msg.alert("提示","请填写版权开始！");
                                    return;
                                }
                                if (rightEndTime==''){
                                    Ext.Msg.alert("提示","请填写版权到期！");
                                    return;
                                }
                                Ext.MessageBox.confirm("请您确认操作", " 更新版权 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!updateDigitalRightBySearch.action"
                                        });

                                        remoteRequestStore.baseParams = searchForm.getForm().getValues();

                                        remoteRequestStore.reload({
                                            params:{rightStartTime:rightStartTime,rightEndTime:rightEndTime,rightUrl:rightUrl},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
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


    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 92,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:4},
        baseCls: 'x-plain',

        items: [
                 new Ext.form.FieldSet({
                     title:'搜索栏'
                    ,width:700,
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
                        labelWidth: 75,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　开始时间',
                                width:100
                            }]
                    },     {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 90,
                        items: [
                            {
                                hiddenName:'c_status',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　　资源状态',
                                width:100,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store:  new Ext.data.ArrayStore({
                                        fields: ['id', 'name'],
                                        data: [['1', '下线'], ['2', '上线']]
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
                    },       {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 75,
                        items: [
                            {
                                name:'c_name',
                                xtype: 'textfield',
                                labelWidth:60,
                                fieldLabel: '　　资源名称',
                                width:140
                            }]
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
                        labelWidth: 75,
                        items: [
                            {
                                name:'endDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　结束时间',
                                width:100
                            }]
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
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });
    var listGrid = new Ext.grid.GridPanel({
        title:"资源管理",
        width:tableWidth+20,
        height:tableHeight+40,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
                {header: "ID", dataIndex: 'c_id', width: 50,hidden:true, sortable: true, align:'left'},
                {header: "名称", dataIndex: 'c_name', width: 150, sortable: true, align:'left',
                    renderer:
                        function (val,p,row){
                            if(canDoThisAction('digitalRightView')){
                            return '<a href=\'javascript:viewObj("view","'+row.data.c_id+'",'+row.data.c_moduleId+')\'>'+ val +'</a>';
                        }}
                },

                {header: "主演", dataIndex: 'c_actors', width: 128, sortable: true, align:'center'},
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
                {header: "版权开始", dataIndex: 'c_validStartTime', width: 120, sortable: true, align:'center'},
                {header: "版权到期", dataIndex: 'c_validEndTime', width: 120, sortable: true, align:'center'},
                {header: "版权链接", dataIndex: 'c_digiRightUrl', width: 80, sortable: true, align:'center'},
                {header: "CP状态", dataIndex: 'c_status', width: 60, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
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
                {header: "设备", dataIndex: 'c_deviceId', width:100, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = deviceStore.reader.jsonData.objs;
                        if (objs){
                            for (var i=0; i<objs.length; i++){
                                if (objs[i].id == val){
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
    this.viewObj = function (action,id,moduleId){
        document.location='cpContentView.jsp?action='+ action +'&id='+ id +'&moduleId='+moduleId;
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