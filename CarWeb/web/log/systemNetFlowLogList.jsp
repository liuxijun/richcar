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
        needPermissions(actionHeader,"contentLogList","contentLogList");
        //  needPermissions(actionHeader,"list","areaList");
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
    var tableWidth = 960;
    var tableHeight = 500;


    var actionUrl = "/log/visitLog";

    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 65,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:2},
        baseCls: 'x-plain',

        items: [
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:960,
                height:60,
                items:[
                    {
                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:4},
                        items: [
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 90,
                                items: [
                                    {
                                        id:'startDate',
                                        name:'startDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 00:00:00',
                                        labelWidth: 60,
                                        fieldLabel: '　选择开始日期',
                                        width:150
                                    }
                                ]
                            },  {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 90,
                                items: [
                                    {
                                        id:'endDate',
                                        name:'endDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 23:59:59',
                                        labelWidth: 60,
                                        fieldLabel: '　选择结束日期',
                                        width:150
                                    }
                                ]
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
                                        form.action = '/log/visitLog!netFlowLogs.action?excel=isTrue';
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
        if(getCmpValue("startDate")<=getCmpValue("endDate")) {
                var form1 = searchForm.getForm();
                dataListStore.removeAll();
                dataListStore.baseParams = form1.getValues();
                dataListStore.reload({params: {start:0,limit:10}});
                listGrid.getBottomToolbar().updateInfo();
            }else{
            Ext.Msg.alert("提示","结束日期大于开始日期，请重新选择");
        }
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!netFlowLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'date'},
            {name:'allNetFlow'},
            {name:'allCount'},
            {name:'allLength'},
            {name:'mobileNetFlow'},
            {name:'mobileLength'},
            {name:'mobileCount'},
            {name:'elseNetFlow'},
            {name:'elseCount'},
            {name:'elseLength'},
            {name:'allNetFlowPad'},
            {name:'padCount'},
            {name:'padLength'},
            {name:'allNetFlowPhone'},
            {name:'phoneCount'},
            {name:'phoneLength'},
            {name:'allNetFlowLive'},
            {name:'liveCount'},
            {name:'liveLength'},
            {name:'allNetFlowLivePad'},
            {name:'livePadCount'},
            {name:'livePadLength'},
            {name:'allNetFlowLivePhone'},
            {name:'livePhoneLength'},
            {name:'livePhoneCount'},
            {name:'allNetFlowContent'},
            {name:'contentLength'},
            {name:'contentCount'},
            {name:'allNetFlowContentPad'},
            {name:'contentPadLength'},
            {name:'contentPadCount'},
            {name:'allNetFlowContentPhone'},
            {name:'contentPhoneLength'},
            {name:'contentPhoneCount'},
            {name:'wasuNetFlow'},
            {name:'wasuLength'},
            {name:'wasuCount'},
            {name:'wasuLadongNetFlow'},
            {name:'vooleNetFlow'},
            {name:'vooleLength'},
            {name:'vooleCount'},
            {name:'vooleLadongNetFlow'},
            {name:'bestvNetFlow'},
            {name:'bestvLength'},
            {name:'bestvCount'},
            {name:'bestvLadongNetFlow'},
            {name:'onlineUser'},
            {name:'onlineUserNetFlow'}
        ]
    });

    var i=0;
    if(i>0){
        dataListStore.load({params: {start:0,limit:10}});
    }


    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.GridPanel({
        title:"流量使用情况统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {header: "日期", dataIndex: 'date', width: 100, sortable: true, align:'center'},
            {header: "总流量(b)", dataIndex: 'allNetFlow', width:100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'allCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'allLength', width:100, sortable: true, align:'center' },
            {header: "移动流量(b)", dataIndex: 'mobileNetFlow', width:100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'mobileCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'mobileLength', width:100, sortable: true, align:'center' },
            {header: "其他流量(b)", dataIndex: 'elseNetFlow', width:100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'elseCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'elseLength', width:100, sortable: true, align:'center' },
            {header: "Pad总流量(b)", dataIndex: 'allNetFlowPad', width:100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'padCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'padLength', width:100, sortable: true, align:'center' },
            {header: "Phone总流量(b)", dataIndex: 'allNetFlowPhone', width:100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'phoneCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'phoneLength', width:100, sortable: true, align:'center' },
            {header: "直播总流量(b)", dataIndex: 'allNetFlowLive', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'liveCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'liveLength', width:100, sortable: true, align:'center' },
            {header: "Pad直播总流量(b)", dataIndex: 'allNetFlowLivePad', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'livePadCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'livePadLength', width:100, sortable: true, align:'center' },
            {header: "Phone直播总流量(b)", dataIndex: 'allNetFlowLivePhone', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'livePhoneCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'livePhoneLength', width:100, sortable: true, align:'center' },
            {header: "点播总流量(b)", dataIndex: 'allNetFlowContent', width: 100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'contentCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'contentLength', width:100, sortable: true, align:'center' },
            {header: "Pad点播总流量(b)", dataIndex: 'allNetFlowContentPad', width: 100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'contentPadCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'contentPadLength', width:100, sortable: true, align:'center' },
            {header: "Phone点播总流量(b)", dataIndex: 'allNetFlowContentPhone', width: 100, sortable: true, align:'center' },
            {header: "次数", dataIndex: 'contentPhoneCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'contentPhoneLength', width:100, sortable: true, align:'center' },
            {header: "华数点播流量(b)", dataIndex: 'wasuNetFlow', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'wasuCount', width: 100, sortable: true, align:'center'},
            {header: "时长(b)", dataIndex: 'wasuLength', width: 100, sortable: true, align:'center'},
            {header: "华数拉动流量(b)", dataIndex: 'wasuLadongNetFlow', width: 100, sortable: true, align:'center'},
            {header: "优朋点播流量(b)", dataIndex: 'vooleNetFlow', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'vooleCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'vooleLength', width:100, sortable: true, align:'center' },
            {header: "优朋拉动流量(b)", dataIndex: 'vooleLadongNetFlow', width: 100, sortable: true, align:'center'},
            {header: "百事通点播流量(b)", dataIndex: 'bestvNetFlow', width: 100, sortable: true, align:'center'},
            {header: "次数", dataIndex: 'bestvCount', width:100, sortable: true, align:'center' },
            {header: "时长", dataIndex: 'bestvLength', width:100, sortable: true, align:'center' },
            {header: "百事通拉动流量(b)", dataIndex: 'bestvLadongNetFlow', width: 100, sortable: true, align:'center'},
            {header: "播放用户数量", dataIndex: 'onlineUser', width: 100, sortable: true, align:'center'},
            {header: "播放用户流量(b)", dataIndex: 'onlineUserNetFlow', width: 100, sortable: true, align:'center'}
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