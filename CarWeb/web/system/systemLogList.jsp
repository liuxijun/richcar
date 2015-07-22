<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "systemLog";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "list", "systemLogManage,systemLogList");
        needPermissions(actionHeader,"view","systemLogManage");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="systemLog"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        nextUrl = "systemLogList.jsp";
        actionHeader = "systemLog";
        //   checkAllFunctions();  // 检查用户权限
        defaultPageSize = 13;

        function loadData() {
            searchStore.baseParams["adminName"]=Ext.getCmp("adminName").getValue();
            searchStore.baseParams["log"]=Ext.getCmp("log").getValue();
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }

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
                    width:560,
                    height:50,
                    items:[
                        {
                            baseCls: 'x-plain',
                            layout:'table',
                            layoutConfig: {columns:3},
                            items: [
                                {
                                    baseCls: 'x-plain',
                                    layout: 'form',
                                    labelWidth: 60,
                                    items: [
                                        {
                                            name:'adminName',
                                            xtype: 'textfield',
                                            id:'adminName',
                                            labelWidth: 60,
                                            fieldLabel: '　　操作员',
                                            width:135
                                        }]
                                },
                                {
                                    baseCls: 'x-plain',
                                    layout: 'form',
                                    labelWidth: 63,
                                    items: [
                                        {
                                            name:'log',
                                            id:'log',
                                            xtype: 'textfield',
                                            labelWidth: 60,
                                            fieldLabel: '　　　内容',
                                            width:160
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

        function initDisplay() {
            storeConfig.fields = ['id', 'systemLogAction', 'adminName', 'adminIp', 'logTime', 'log'];
            storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id', hidden:true, align:'center', header:"序号", width:60, sortable:true, dataIndex:'id'}                ,
                 {header:"<fts:text name="systemLog.systemLogAction"/>", hidden:true, align:'center', width:165, sortable:true, dataIndex:'systemLogAction'}                ,
                {header:"<fts:text name="systemLog.adminId"/>", hidden:false, align:'center', width:90, sortable:true, dataIndex:'adminName'}                ,
                {header:"<fts:text name="systemLog.adminIp"/>", hidden:false, align:'center', width:105, sortable:true, dataIndex:'adminIp'}                ,
                {header:"<fts:text name="systemLog.logTime"/>", hidden:false, align:'center', width:118, sortable:true, dataIndex:'logTime'}                ,
                {header:"<fts:text name="systemLog.log"/>", hidden:false, align:'center', width:208, sortable:true, dataIndex:'log'},
                {header:"管理", align:'center', width:75, sortable:false,
                    renderer:function (val, p, row) {
                       if (canDoThisAction('view')) {
                            return '<a href=\'systemLogView.jsp?obj.id=' + val + '\'>查看</a>&nbsp;';
                        }
                    }, dataIndex:'id'
        }
        ])
        ;
        //除了基本操作（删除，查看），还要添加的链接
        //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
        //工具栏中的按钮，还需要添加的
        //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
        checkAllFunctions();  // 检查用户权限
        var systemLogGrid = new FortuneSearchListGrid({
            title:'<fts:text name="systemLog"/>列表',
            sm:sm,
            cm:columns,
            width:650,
            height:460,
            store:searchStore,
            tbar:new Ext.Toolbar({items:[
                        new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:73,
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
                pageSize:defaultPageSize,
                store:searchStore,
                displayInfo:true,
                displayMsg:'结果数据 {0} - {1} of {2}',
                emptyMsg:"没有数据",
                items:defaultBottomButtons
            })
        });
        systemLogGrid.render(displayDiv);
        defaultGrid = systemLogGrid;
//        searchStore.store('id','desc');
        searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function () {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }/*,{
                 func:initAreaStore,
                 done:false,
                 flag:'initAreaStore'
                 }*/
            ],
                    initDisplay);
        });

    </script>
</head>
<body>
<table align="center" width="660">
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>