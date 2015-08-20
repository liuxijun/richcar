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
                                        form.action = '/log/visitLog!getDemandCountLogs.action?excel=isTrue';
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
        url: actionUrl + "!getDemandCountLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'Count'},
            {name:'date'},
            {name:'0'},
            {name:'1'},
            {name:'2'},
            {name:'3'},
            {name:'4'},
            {name:'5'},
            {name:'6'},
            {name:'7'},
            {name:'8'},
            {name:'9'},
            {name:'10'},
            {name:'11'},
            {name:'12'},
            {name:'13'},
            {name:'14'},
            {name:'15'},
            {name:'16'},
            {name:'17'},
            {name:'18'},
            {name:'19'},
            {name:'20'},
            {name:'21'},
            {name:'22'},
            {name:'23'}
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
        title:"直播趋势统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {header: "日期", dataIndex: 'date', width: 100, sortable: true, align:'center'},

            {header: "00:00", dataIndex: '0', width: 100, sortable: true, align:'center'},

            {header: "01:00", dataIndex: '1', width: 100, sortable: true, align:'center'},

            {header: "02:00", dataIndex: '2', width: 100, sortable: true, align:'center'},

            {header: "03:00", dataIndex: '3', width: 100, sortable: true, align:'center'},

            {header: "04:00", dataIndex: '4', width: 100, sortable: true, align:'center'},

            {header: "05:00", dataIndex: '5', width: 100, sortable: true, align:'center'},

            {header: "06:00", dataIndex: '6', width: 100, sortable: true, align:'center'},

            {header: "07:00", dataIndex: '7', width: 100, sortable: true, align:'center'},

            {header: "08:00", dataIndex: '8', width: 100, sortable: true, align:'center'},

            {header: "09:00", dataIndex: '9', width: 100, sortable: true, align:'center'},

            {header: "10:00", dataIndex: '10', width: 100, sortable: true, align:'center'},

            {header: "11:00", dataIndex: '11', width: 100, sortable: true, align:'center'},

            {header: "12:00", dataIndex: '12', width: 100, sortable: true, align:'center'},

            {header: "13:00", dataIndex: '13', width: 100, sortable: true, align:'center'},

            {header: "14:00", dataIndex: '14', width: 100, sortable: true, align:'center'},

            {header: "15:00", dataIndex: '15', width: 100, sortable: true, align:'center'},

            {header: "16:00", dataIndex: '16', width: 100, sortable: true, align:'center'},

            {header: "17:00", dataIndex: '17', width: 100, sortable: true, align:'center'},

            {header: "18:00", dataIndex: '18', width: 100, sortable: true, align:'center'},

            {header: "19:00", dataIndex: '19', width: 100, sortable: true, align:'center'},

            {header: "20:00", dataIndex: '20', width: 100, sortable: true, align:'center'},

            {header: "21:00", dataIndex: '21', width: 100, sortable: true, align:'center'},

            {header: "22:00", dataIndex: '22', width: 100, sortable: true, align:'center'},

            {header: "23:00", dataIndex: '23', width: 100, sortable: true, align:'center'},

            {header: "按天统计", dataIndex: 'Count', width: 100, sortable: true, align:'center'}
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