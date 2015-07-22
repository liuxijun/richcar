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
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"cpLogs","cspLogList");
    //    needPermissions(actionHeader,"list","cspSearch");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
<meta http-equiv=content-type content="text/html; charset=UTF-8">
<title></title>
<%@include file="../inc/jsCssLib.jsp" %>
<%@include file="../inc/extBase.jsp" %>
<script language="javascript">
Ext.onReady(function() {
    var pageSize = 120000000;
    var tableWidth = 680;
    var tableHeight = 500;


    var actionUrl = "/log/visitLog";

    var searchForm = new Ext.FormPanel({
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
                    width:650,
                    height:50,
                    items:[
/*            {
                baseCls: 'x-plain',
                layout: 'form',
                width:50,
                items: [
                    {
                        xtype: 'label',
                        labelSeparator : '',
                        fieldLabel: '搜索栏:'
                    }
                ]
            }
            ,*/
            {
                baseCls: 'x-plain',
                layout:'table',
                layoutConfig: {columns:4},
                items: [
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 30,
                        items: [
                            {
                                hiddenName:'vl_spId',
                                xtype: 'combo',
                                labelWidth: 60,
                                fieldLabel: '　SP',
                                width:135,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store: new Ext.data.ArrayStore({
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

                                    }
                                }

                            }
                        ]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 70,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth:70,
                                fieldLabel: '　开始时间',
                                width:100
                            }
                        ]
                    },
                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth:70,
                        items: [
                            {
                                name:'endDate',
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
                    }
                ]
            }
                   ]
              })

        ]
    });

    //刷新列表
    this.loadData = function() {
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params: {start:0,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!cpLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'vl_cpId'
            },
            {
                name:'vl_count'
            },
            {
                name:'vl_length'
            }
        ],
        listeners:{
            load: function(store) {

                var objs = store.reader.jsonData.objs;
                if (objs) {
                    var count = 0;
                    var length = 0;
                    for (var i = 0; i < objs.length; i++) {
                        count += parseInt(objs[i].vl_count);
                        length += parseInt(objs[i].vl_length);
                    }
                    for (var i = 0; i < objs.length; i++) {
                        spanObj = document.getElementById('count' + objs[i].vl_cpId);
                        spanObj.innerHTML = (objs[i].vl_count / count * 100 + 0.0).toFixed(1) + '%';
                        spanObj = document.getElementById('length' + objs[i].vl_cpId);
                        spanObj.innerHTML = (objs[i].vl_length / length * 100 + 0.0).toFixed(1) + '%';
                    }
                }
            }
        }
    });
    dataListStore.setDefaultSort('count(*)', 'desc');

    var cspStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/csp/csp!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'name'
            },
            {
                name:'isCp'
            },
            {
                name:'isSp'
            }
        ]
    });
    cspStore.load({
        callback :
                function(records, options, success) {
                    if (success) {
                        sequenceDo();
                    }
                }
    });

    var sequence = 0;

    function sequenceDo() {
        sequence++;
        if (sequence == 1) {
            loadData();
            initSpCombo();
        }
    }

    function initSpCombo() {
        var datas = [];
        for (var i = 0; i < cspStore.getCount(); i++) {
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isSp == 1) {
                datas[datas.length] = [storeRecord.data.id,storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('vl_spId');
        obj.clearValue();
        obj.store.loadData(datas);
    }

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"CP点播量统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {
                header: "SP名称",
                dataIndex: 'vl_cpId',
                width:180,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    var objs = cspStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return val;
                }
            },
            {
                header: "点播次数",
                dataIndex: 'vl_count',
                width: 100,
                sortable: true,
                align:'center'
            },
            {
                header: "比重",
                dataIndex: 'vl_cpId',
                width: 100,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    return '<span id="count' + row.data.vl_cpId + '">' + val + '</span>';
                }
            },
            {
                header: "点播时长",
                dataIndex: 'vl_length',
                width: 100,
                sortable: true,
                align:'center'
            },
            {
                header: "比重",
                dataIndex: 'vl_cpId',
                width: 100,
                sortable: true,
                align:'center',
                renderer:function(val, p, row) {
                    return '<span id="length' + row.data.vl_cpId + '">' + val + '</span>';
                }
            }
        ],

        tbar:new Ext.Toolbar({
          items:[
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
        ]
    }),

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