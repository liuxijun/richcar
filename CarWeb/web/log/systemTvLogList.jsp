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
        needPermissions(actionHeader,"contentLogs","contentLogList");
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
    var tableWidth = 700;
    var tableHeight = 500;


    var actionUrl = "/log/visitLog";

    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 120,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
        baseCls: 'x-plain',

        items: [
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:960,
                height:90,
                items:[
                    {
                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:4},
                        items: [
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 82,
                                items: [
                                    {
                                        id:'startDate',
                                        name:'startDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 00:00:00',
                                        labelWidth: 60,
                                        fieldLabel: '　　开始时间',
                                        width:150
                                    }]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 70,
                                items: [
                                    {
                                        name:'limit',
                                        id:'limit',
                                        xtype: 'textfield',
                                        labelWidth: 60,
                                        fieldLabel: '　查询数量',
                                        emptyText:'20',
                                        width:140
                                    }]
                            },
                            {
                                baseCls: 'x-btn-over',
                                layout:'column',
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
                                baseCls: 'x-plain',
                                layout: 'form',
                                items: [
                                    {
                                        xtype: 'label',
                                        labelSeparator : '',
                                        fieldLabel: '　'
                                    }]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 82,
                                items: [
                                    {
                                        id:'endDate',
                                        name:'endDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 23:59:59',
                                        labelWidth: 60,
                                        fieldLabel: '　　结束时间',
                                        width:150
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
                                        form.action = '/log/visitLog!contentFilmAndTvLogs.action?contentType=2&excel=isTrue';
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

    //刷新列表
    this.loadData = function(){
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:0}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!contentFilmAndTvLogs.action?contentType=2",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'vl_spId'},
            {name:'vl_contentType'},
            {name:'vl_contentId'},
            {name:'vl_channelId'},
            {name:'vl_count'},
            {name:'vl_length'},
            {name:'vl_bytesSend'}
        ] ,
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
                            params:{keyIds:ids,limit:20},
                            callback :
                                    function(records,options,success){
                                        var names = this.reader.jsonData.objs;
                                        var ids = dataListStore.reader.jsonData.objs;
                                        if (names && ids){
                                            for (var i=0; i<ids.length; i++){
                                                for (var j=0;j<names.length; j++){
                                                    if (ids[i].vl_contentId==names[j].c_id){
                                                        var spanObj = document.getElementById(ids[i].vl_contentId);
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
    dataListStore.setDefaultSort('count(*)','desc');

    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == 3){
            loadData();
        }
    }

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.GridPanel({
        title:"电视剧点播排行",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {header: "SP名称", dataIndex: 'vl_spId', width:100, sortable: true, align:'center',
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
            {header: "资源类型", dataIndex: 'vl_contentType', width:70, sortable: true, align:'center',
                renderer:function(val,p,row){
                    if("1"==val){
                        return "电影";
                    }
                    if("2"==val){
                        return "电视剧"
                    }
                    return "";
                }
            },
            {header: "资源名称", dataIndex: 'vl_contentId', width:120, sortable: true, align:'center',
                renderer:function(val,p,row){
                    return '<span id="'+row.data.vl_contentId+'">'+val+'</span>';
                }
            },
            {header: "频道", dataIndex: 'vl_channelId', width:120, sortable: true, align:'center',
                renderer:function(val,p,row){
                    var objs = channelStore.reader.jsonData.objs;
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
            {header: "点播次数", dataIndex: 'vl_count', width: 100, sortable: true, align:'center'},
            {header: "点播时长", dataIndex: 'vl_length', width: 100, sortable: true, align:'center'},
            {header: "总流量(b)", dataIndex: 'vl_bytesSend', width: 100, sortable: true, align:'center'}
        ],

        tbar:new Ext.Toolbar({items:[
                    new Ext.Panel({
                        style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                        id:'searchForm',
                        width: '100%',
                        height:80,
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
            pageSize: 100000000,
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