<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "product";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"searchSpSalesCount","spSalesList");
      //  needPermissions(actionHeader,"searchAllSp","spList");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title>SP销售统计</title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script type="text/javascript">
nextUrl = "spSalesList.jsp";
actionHeader = "../product/userBuy";
checkAllFunctions();  // 检查用户权限


var cspStore = new Ext.data.JsonStore({
    method:'POST',
    url:"/csp/csp!searchAllSp.action",
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
cspStore.load();

function initDisplay() {
    storeConfig.fields = ['spId','spName','salesAmount','buyNum'];
    storeConfig.proxy = new Ext.data.HttpProxy({method:'POST',url:getActionUrl('searchSpSalesCount')});
    searchStore = new FortuneSearchStore(storeConfig);
    keyFieldId = "id";

    var columns = new Ext.grid.ColumnModel([
        {
            header:'SP名称',
            align:'center',
            width:180,
            sortable:true,
            dataIndex:'spName'
        },
        {
            header:'销售金额',
            align:'center',
            width:120,
            sortable:true,
            dataIndex:'salesAmount'
        },
        {
            header:'购买次数',
            align:'center',
            width:120,
            sortable:true,
            dataIndex:'buyNum'
        }

    ]);

    //刷新列表
    this.loadData = function() {
        var form1 = searchForm.getForm();
        searchStore.removeAll();
        searchStore.baseParams = form1.getValues();
        searchStore.reload({params: {start:0,limit:12}});
        spSalesGrid.getBottomToolbar().updateInfo();
    }
    var searchForm = new Ext.FormPanel({
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
                width:425,
                height:80,
                items:[
                    {

                        baseCls: 'x-plain',
                        layout:'table',
                        layoutConfig: {columns:3},
                        items: [
                            /*                 {
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
                             ,
                             */
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 70,
                                items: [
                                    {
                                        name:'userBuy.startTime',
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
                                labelWidth: 45,
                                items: [
                                    {
                                        hiddenName:'userBuy.spId',
                                        xtype: 'combo',
                                        labelWidth: 90,
                                        fieldLabel: '　　SP',
                                        width:152,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:cspStore,
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
                                        typeAhead: false

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
                            }

                            ,
                            {
                                baseCls: 'x-plain',
                                layout: 'form',
                                labelWidth: 70,
                                items: [
                                    {
                                        name:'userBuy.endTime',
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
                            }


                        ]
                    }


                ]})

        ]
    });


    var spSalesGrid = new FortuneSearchListGrid({
        title:'SP销售业绩统计',
        cm:columns,
        store:searchStore,
        height:500,
        width:490,
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
        ]}),
        bbar:new Ext.PagingToolbar({
            pageSize:2,
            store:searchStore,
            displayInfo:true,
            displayMsg:'结果数据 {0} - {1} of {2}',
            emptyMsg:'没有数据'
        })
    });

    spSalesGrid.render(displayDiv);
    defaultGrid = spSalesGrid;
    searchStore.load({
        params:{start:0,limit:defaultPageSize}
    });
}

Ext.onReady(function() {
    Ext.QuickTips.init();
    initDisplay();
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