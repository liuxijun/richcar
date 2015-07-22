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
        needPermissions(actionHeader,"areaLogs","areaLogList");
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
    var tableWidth = 760;
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
                height:70,
                items:[
                    {
                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:4},
                        items: [
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 70,
                                items: [
                                    {
                                        name:'startDate',
                                        id:'startDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 00:00:00',
                                        labelWidth: 60,
                                        fieldLabel: '　开始时间',
                                        width:120
                                    }]
                            },
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 78,
                                items: [
                                    {
                                        id:'endDate',
                                        name:'endDate',
                                        xtype: 'datefield',
                                        format:'Y-m-d 23:59:59',
                                        labelWidth: 60,
                                        fieldLabel: '　　结束时间',
                                        width:120
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
                                        form.action = '/log/visitLog!userLoginLogs.action?excel=isTrue';
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
        url: actionUrl + "!userLoginLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'vl_areaId'},
            {name:'vl_oneLogin'},
            {name:'vl_twoLogin'},
            {name:'vl_threeLogin'},
            {name:'vl_fourLogin'},
            {name:'vl_fiveLogin'},
            {name:'vl_countLogin'},
            {name:'vl_timeCountLogin'},
            {name:'vl_dayCountLogin'}
        ]
    });
    dataListStore.setDefaultSort('count(*)','desc');

    var areaStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/system/area!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'}
        ]
    });
    areaStore.load({
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
            loadData();
        }
    }

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.GridPanel({
        title:"用户登陆情况统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {header: "区域名称", dataIndex: 'vl_areaId', width:100, sortable: true, align:'center',
                renderer:function(val,p,row){
                    var objs = areaStore.reader.jsonData.objs;
                        if (objs){
                            for (var i=0; i<objs.length; i++){
                                if (objs[i].id == val){
                                    return objs[i].name
                                }
                                if(-1==val){
                                    return '其他'
                                }
                                if(1==val){
                                    return '总量'
                                }
                            }
                    }
                    return val;
                }
            },
            {header: "联通总部取号", dataIndex: 'vl_oneLogin', width: 80, sortable: true, align:'center'},
            {header: "Wap取号", dataIndex: 'vl_twoLogin', width: 80, sortable: true, align:'center'},
            {header: "河北联通取号", dataIndex: 'vl_threeLogin', width: 80, sortable: true, align:'center'},
            {header: "验证码登陆", dataIndex: 'vl_fourLogin', width: 80, sortable: true, align:'center'},
            {header: "客户端登陆", dataIndex: 'vl_fiveLogin', width: 80, sortable: true, align:'center'},
            {header: "总量", dataIndex: 'vl_countLogin', width: 80, sortable: true, align:'center'},
            {header: "登陆次数", dataIndex: 'vl_timeCountLogin', width: 80, sortable: true, align:'center'},
            {header: "登陆天次", dataIndex: 'vl_dayCountLogin', width: 80, sortable: true, align:'center'}
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