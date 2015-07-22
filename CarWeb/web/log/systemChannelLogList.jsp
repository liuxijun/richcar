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
      //  needPermissions(actionHeader,"searchAll","channelSearchAll");
        needPermissions(actionHeader,"channelLogs","ChannelLogList");
     //   needPermissions(actionHeader,"list","cspSearch");
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
    var pageSize = 120000000;
    var tableWidth = 728;
    var tableHeight = 500;


    var channelStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/publish/channel!searchAllByStatus.action?obj.type=1&obj.cspId=1&obj.status=1",
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
    var actionUrl = "/log/visitLog";

    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height :100,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
        baseCls: 'x-plain',

        items: [
                new Ext.form.FieldSet({
                    title:'搜索栏',
                    width:700,
                    height:90,
                    items:[
            {
                baseCls: 'x-plain',
                layout:'table',
                layoutConfig: {columns:3},
                items: [
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 50,
                        items: [
                            {
                                name:'startDate',
                                id:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d 00:00:00',
                                labelWidth: 60,
                                fieldLabel: '开始时间',
                                width:150
                            }]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 50,
                        items: [
                            {
                                name:'endDate',
                                id:'endDate',
                                xtype: 'datefield',
                                format:'Y-m-d 23:59:59',
                                labelWidth: 60,
                                fieldLabel: '结束时间',
                                width:150
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
                    },
                    {
                        baseCls:'x-plain',
                        layout:'form',
                        labelWidth:50,
                        items:[
                            {
                                hiddenName:'channelId',
                                xtype:'combo',
                                labelWidth:50,
                                fieldLabel:'频道',
                                width:150,
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
                        baseCls: 'x-btn-over',
                        layout:"form",
                        items:[{
                            xtype:'button',
                            text: '导出数据',
                            minWidth:60,
                            handler:function(){
                                var startTime = Ext.getCmp("startDate").value;
                                var endTime = Ext.getCmp("endDate").value;
                                if(startTime==undefined||endTime==undefined){
                                    Ext.MessageBox.show({
                                        title:"标题",
                                        msg:"请选择需要查询的时间段",
                                        buttons:{"ok":"OK"},
                                        width:500,
                                        icon:Ext.MessageBox.INFO,
                                        closable:false
                                    });
                                    return ;
                                }
                                var form = searchForm.getForm().getEl().dom;
                                form.action = '/log/visitLog!channelLogs.action?start=0&limit=100000&excel=isTrue&orderBy=desc';
                                form.method = 'POST';
                                form.target = "_blank" ;
                                form.submit();
                            }
                        }]
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
            url: actionUrl + "!channelLogs.action",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'vl_name'},
                    {name:'vl_channelId'},
                    {name:'vl_count'},
                    {name:'vl_length'},
                    {name:'vl_bytesSend'}
            ],
            listeners:{
            	load: function(store){

                    var objs= store.reader.jsonData.objs;
                    if (objs){
                        var count=0;
                        var length=0;
                        var bytesSend = 0;
                        for (var i=0; i<objs.length; i++){
                            if(3==objs[i].vl_grade){
                                if(""!=objs[i].vl_count){
                                    count+=parseInt(objs[i].vl_count);
                                }
                                if(""!=objs[i].vl_length){
                                    length+=parseInt(objs[i].vl_length);
                                }
                                if(""!=objs[i].vl_bytesSend){
                                    bytesSend +=parseInt(objs[i].vl_bytesSend);
                                }
                            }
                        }
                        for (var i=0; i<objs.length; i++){
                            if(""!=objs[i].vl_count){
                            spanObj = document.getElementById('count'+objs[i].vl_channelId);
                            spanObj.innerHTML = (objs[i].vl_count/count*100+0.0).toFixed(1)+'%';
                            }
                            if(""!=objs[i].vl_length){
                            spanObj = document.getElementById('length'+objs[i].vl_channelId);
                            spanObj.innerHTML = (objs[i].vl_length/length*100+0.0).toFixed(1)+'%';
                            }
                            if(""!=objs[i].vl_bytesSend){
                                spanObj = document.getElementById('bytesSend'+objs[i].vl_bytesSend);
                                spanObj.innerHTML = (objs[i].vl_bytesSend/bytesSend*100+0.0).toFixed(1)+'%';
                            }
                        }
                    }
                }
            }
    });
    dataListStore.setDefaultSort('count(*)','desc');

    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == 2 ){
            loadData();
        }
    }

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"频道点播量统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
                {header: "频道名称", dataIndex: 'vl_name', width:140, sortable: true, align:'left'
                },
                {header: "点播次数", dataIndex: 'vl_count', width: 90, sortable: true, align:'center'},
                {header: "比重", dataIndex: '', width: 100, sortable: true, align:'center',
                     renderer:function(val,p,row){
                         if(""==row.data.vl_count){
                             return '';
                         }else{
                             return '<span style=color:#070AFF id="count'+row.data.vl_channelId+'">'+val+'</span>';
                         }

                    }
                },
                {header: "点播时长(s)", dataIndex: 'vl_length', width: 110, sortable: true, align:'center'},
                {header: "比重", dataIndex: '', width: 90, sortable: true, align:'center',
                     renderer:function(val,p,row){
                         if(""==row.data.vl_length){
                             return '';
                         }else{
                             return '<span style=color:#070AFF id="length'+row.data.vl_channelId+'">'+val+'</span>';
                         }
                    }
                },
                {header: "流量(b)", dataIndex: 'vl_bytesSend', width: 110, sortable: true, align:'center'},
                {header: "比重", dataIndex: '', width: 90, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        if(""==row.data.vl_bytesSend){
                            return '';
                        }else{
                            return '<span style=color:#070AFF id="bytesSend'+row.data.vl_bytesSend+'">'+val+'</span>';
                        }
                    }
                }
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