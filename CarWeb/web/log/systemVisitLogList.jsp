<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        //needPermissions(actionHeader,"list","cspcspList");
      //  needPermissions(actionHeader,"list","deviceList");
       // needPermissions(actionHeader,"list","cspSearch");
      // needPermissions(actionHeader,"list","contentModuleList");
        needPermissions(actionHeader,"visitLogs","visitLogList");
   //     needPermissions(actionHeader,"searchAll","channelSearchAll");
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
    var pageSize = 14;
    var tableWidth = 890;
    var tableHeight = 500;


    var actionUrl = "/log/visitLog";

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
                 width:825,
                 height:80,
                 items:[
            {
                baseCls: 'x-plain',
                layout:'table',
                layoutConfig: {columns:6},
                items: [
/*                       {
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
            , */  {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 60,
                        items: [
                            {
                                name:'vl_userId',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '　　用户ID',
                                width:100
                            }]
                    },  {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 70,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　开始时间',
                                width:100
                            }]
                    },{
                       baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 45,
                        items: [
                            {
                                hiddenName:'vl_spId',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　SP',
                                width:145,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store: new Ext.data.ArrayStore( {
                                    fields: ["id",'name'],
                                    data:[]
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
                                        var spId = this.getValue();
                                        var datas = [];
                                        for (var i=0; i<cspStore.getCount(); i++){
                                            var storeRecord = cspStore.getAt(i);
                                            if (storeRecord.data.isCp==1){
                                                var cpId = storeRecord.data.id;
                                                if (cpId == spId){
                                                    datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
                                                    continue;
                                                }
                                                for (var j=0;j<cspCspStore.getCount();j++){
                                                    var cspCspStoreRecord = cspCspStore.getAt(j);
                                                    if (cspCspStoreRecord.data.masterCspId==spId && cspCspStoreRecord.data.cspId==cpId){
                                                        datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
                                                    }
                                                }
                                            }
                                        }
                                        var obj = searchForm.getForm().findField('vl_cpId');
                                        obj.clearValue();
                                        obj.store.loadData(datas);

                                        datas=[];
                                        for (var i=0; i<channelStore.getCount(); i++){
                                            var storeRecord = channelStore.getAt(i);
                                            if (storeRecord.data.cspId==spId){
                                                 datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
                                            }
                                        }
                                        obj = searchForm.getForm().findField('vl_channelId');
                                        obj.clearValue();
                                        obj.store.loadData(datas);

                                    }
                                }

                            }]
                    },
                       {
                       baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 70,
                        items: [
                            {
                                hiddenName:'vl_channelId',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　SP频道',
                                width:120,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store: new Ext.data.ArrayStore( {
                                    fields: ["id",'name'],
                                    data:[]
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
                        items: [
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }]
                    },    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        items: [
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }]
                    },   {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 60,
                        items: [
                            {
                                name:'vl_userIp',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '　　用户IP',
                                width:100
                            }]
                    },     {
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
                            }]
                    },
                  {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth:45,
                        items: [
                            {
                                hiddenName:'vl_cpId',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　CP',
                                width:145,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store: new Ext.data.ArrayStore( {
                                    fields: ["id",'name'],
                                    data:[]
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
                        labelWidth:70,
                        items: [
                            {
                                name:'contentName',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '　资源名称',
                                width:120
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

    //刷新列表
    this.loadData = function(){
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:0,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!visitLogs.action",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'vl_id'},
                    {name:'vl_spId'},
                    {name:'vl_cpId'},
                    {name:'vl_channelId'},
                    {name:'vl_contentId'},
                    {name:'vl_contentPropertyId'},
                    {name:'vl_url'},
                    {name:'vl_userId'},
                    {name:'vl_userIp'},
                    {name:'vl_areaId'},
                    {name:'vl_isFree'},
                    {name:'vl_startTime'},
                    {name:'vl_endTime'},
                    {name:'vl_length'}
            ],
            listeners:{
            	load: function(store){

                    var objs= store.reader.jsonData.objs;
                    if (objs){
                        var ids = '';
                        for (var i=0; i<objs.length; i++){
                            ids += objs[i].vl_contentId + ',';
                        }
                        if (ids!=''){
                            ids = ids.substring(0,ids.length-1);

                            var remoteRequestStore = new Ext.data.JsonStore({
                                method:'POST',
                                url: "/content/content!getContents.action"
                            });
                            remoteRequestStore.reload({
                                params:{keyIds:ids},
                                callback :
                                        function(records,options,success){
                                            var names = this.reader.jsonData.objs;
                                            var ids = dataListStore.reader.jsonData.objs;
                                            if (names && ids){
                                                for (var i=0; i<ids.length; i++){
                                                    for (var j=0;j<names.length; j++){
                                                        if (ids[i].vl_contentId==names[j].c_id){
                                                            var spanObj = document.getElementById(ids[i].vl_id);
                                                            spanObj.innerHTML=names[j].c_name;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                            });

                        }
                    }
                }
            }
    });
    dataListStore.setDefaultSort('vl_startTime','desc');

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
    var cspStore = new Ext.data.JsonStore({
         method:'POST',
         url:"/csp/csp!list.action",
         baseParams:{limit:1000000},
         totalProperty:'totalCount',
         root:'objs',
         fields:[
             {name:'id'},
             {name:'name'},
             {name:'isCp'},
             {name:'isSp'}
         ]
     });
    cspStore.load({
        callback :
                function(records,options,success){
                    if(success){
                        sequenceDo();
                    }
                }
    });
    var cspCspStore = new Ext.data.JsonStore({
         method:'POST',
         url:"/csp/cspCsp!list.action",
         baseParams:{limit:1000000},
         totalProperty:'totalCount',
         root:'objs',
         fields:[
             {name:'masterCspId'},
             {name:'cspId'}
         ]
     });
    cspCspStore.load({
        callback :
                function(records,options,success){
                    if(success){
                        sequenceDo();
                    }
                }
    });

    var channelStore = new Ext.data.JsonStore({
         method:'POST',
         url:"/publish/channel!searchAll.action?obj.type=1",
         baseParams:{limit:1000000},
         totalProperty:'totalCount',
         root:'objs',
         fields:[
             {name:'id'},
             {name:'name'},
             {name:'cspId'}
         ]
     });
    channelStore.load({
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
        if (sequence == 5){
            //dataListStore.load({params: {start:0,limit:pageSize}});
            initSpCombo();
            initCpCombo();
            loadData();
        }
    }

    function initSpCombo(){
            var datas = [];
            for (var i=0; i<cspStore.getCount(); i++){
                var storeRecord = cspStore.getAt(i);
                if (storeRecord.data.isSp==1){
                    datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
                }
            }
            var obj = searchForm.getForm().findField('vl_spId');
            obj.clearValue();
            obj.store.loadData(datas);
    }
    function initCpCombo(){
            var datas = [];
            for (var i=0; i<cspStore.getCount(); i++){
                var storeRecord = cspStore.getAt(i);
                if (storeRecord.data.isCp==1){
                    datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
                }
            }
            var obj = searchForm.getForm().findField('vl_cpId');
            obj.clearValue();
            obj.store.loadData(datas);
    }






    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.GridPanel({
        title:"点播日志",
        width:tableWidth+10,
        height:tableHeight+30,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
                {header: "名称", dataIndex: 'vl_contentId', width: 130, sortable: true, align:'center',
                     renderer:function(val,p,row){
                        return '<span id="'+row.data.vl_id+'">'+val+'</span>';
                    }
                },
                {header: "SP", dataIndex: 'vl_spId', width:100, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = cspStore.reader.jsonData.objs;
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
                {header: "CP", dataIndex: 'vl_cpId', width: 100, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = cspStore.reader.jsonData.objs;
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
                {header: "SP频道", dataIndex: 'vl_channelId', width: 80, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = channelStore.reader.jsonData.objs;
                        if (objs){
                            for (var i=0; i<objs.length; i++){
                                if (objs[i].id == val && objs[i].name){
                                    return objs[i].name.replace('　','');
                                }
                            }
                        }
                        return "";
                    }
                },
                {header: "开始时间", dataIndex: 'vl_startTime', width: 120, sortable: true, align:'center'},
                {header: "结束时间", dataIndex: 'vl_endTime', width: 120, sortable: true, align:'center'},
                {header: "时长(秒)", dataIndex: 'vl_length', width: 60, sortable: true, align:'center'},
                {header: "用户ID", dataIndex: 'vl_userId', width: 75, sortable: true, align:'center'},
                {header: "用户IP", dataIndex: 'vl_userIp', width: 100, sortable: true, align:'center'}

        ],

        tbar:new Ext.Toolbar({items:[

                        new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:110,
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