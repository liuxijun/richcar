<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "contentView", "systemContentManage");
        needPermissions(actionHeader, "spSearch", "systemContentManage,systemContentSpSearch");
        // needPermissions(actionHeader,"cspcspList","cspcspList");
     //   needPermissions(actionHeader, "cspSearch", "cspSearch");
        //    needPermissions(actionHeader,"getContentRegUrl","contentGetContentRegUrl");
    //    needPermissions(actionHeader, "contentModuleList", "contentModuleList");
        //      needPermissions(actionHeader,"deviceList","deviceList");
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
Ext.onReady(function () {
    var pageSize = 15;
    var tableWidth = 885;
    var tableHeight = 500;


    var actionUrl = "/content/content";

    var searchForm = new Ext.FormPanel({
        style:{'margin-left':'15px', 'margin-top':'5px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'searchForm',
        width:'100%',
        height:110,
        labelWidth:70,
        frame:true,
        layout:'table',
        layoutConfig:{columns:1},
        baseCls:'x-plain',

        items:[
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:790,
                height:90,
                items:[
                    {
                        baseCls:'x-plain',
                        layout:'table',
                        layoutConfig:{columns:5},
                        items:[
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:50,
                                items:[
                                    {
                                        hiddenName:'cc_cspId',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　SP',
                                        width:135,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:["id", 'name'],
                                            data:[]
                                        }),
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
                                                var spId = this.getValue();
                                                var datas = [];
                                                for (var i = 0; i < cspStore.getCount(); i++) {
                                                    var storeRecord = cspStore.getAt(i);
                                                    if (storeRecord.data.isCp == 1) {
                                                        var cpId = storeRecord.data.id;
                                                        for (var j = 0; j < cspCspStore.getCount(); j++) {
                                                            var cspCspStoreRecord = cspCspStore.getAt(j);
                                                            if (cspCspStoreRecord.data.masterCspId == spId && cspCspStoreRecord.data.cspId == cpId) {
                                                                datas[datas.length] = [storeRecord.data.id, storeRecord.data.name];
                                                            }
                                                        }
                                                    }
                                                }
                                                var obj = searchForm.getForm().findField('c_cspId');
                                                obj.clearValue();
                                                obj.store.loadData(datas);

                                            }
                                        }

                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:60,
                                items:[
                                    {
                                        hiddenName:'cc_status',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　SP状态',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:['id', 'name'],
                                            data:[
                                                ['1', '下线'],
                                                ['2', '上线']
                                            ]
                                        }),
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
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:70,
                                items:[
                                    {
                                        name:'startDate',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　开始时间',
                                        width:100
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:70,
                                items:[
                                    {
                                        name:'c_name',
                                        xtype:'textfield',
                                        labelWidth:60,
                                        fieldLabel:'　资源名称',
                                        width:140
                                    }
                                ]
                            },

                            {
                                baseCls:'x-plain',
                                layout:'form',
                                items:[
                                    {
                                        xtype:'label',
                                        labelSeparator:'',
                                        fieldLabel:'　'
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:50,
                                items:[
                                    {
                                        hiddenName:'c_cspId',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　CP',
                                        width:135,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:["id", 'name'],
                                            data:[]
                                        }),
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
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:60,
                                items:[
                                    {
                                        hiddenName:'c_status',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　CP状态',
                                        width:100,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:new Ext.data.ArrayStore({
                                            fields:['id', 'name'],
                                            data:[
                                                ['1', '下线'],
                                                ['2', '上线']
                                            ]
                                        }),
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
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:70,
                                items:[
                                    {
                                        name:'endDate',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　结束时间',
                                        width:100
                                    }
                                ]
                            },
                            {
                                baseCls:'x-btn-over',
                                layout:'form',
                                items:[
                                    {
                                        text:'查询',
                                        xtype:'button',
                                        minWidth:60,
                                        listeners:{
                                            "click":function () {
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
    this.loadData = function () {
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params:{start:0, limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:"/content/content!spSearch.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'cc_id'
            },
            {
                name:'c_id'
            },
            {
                name:'c_name'
            },
            {
                name:'c_actors'
            },
            {
                name:'c_directors'
            },
            {
                name:'c_moduleId'
            },
            {
                name:'c_createTime'
            },
            {
                name:'c_validEndTime'
            },
            {
                name:'c_onlineTime'
            },
            {
                name:'c_cspId'
            },
            {
                name:'c_deviceId'
            },
            {
                name:'c_status'
            },
            {
                name:'cc_cspId'
            },
            {
                name:'cc_status'
            },
            {
                name:'cc_statusTime'
            }
        ]
    });
    dataListStore.setDefaultSort('c_createTime', 'desc');

    var deviceStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/system/device!list.action",
        baseParams:{limit:1000000},
        totalProperty:'totalCount',
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
    deviceStore.load({
        callback:function (records, options, success) {
            if (success) {
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
            {
                name:'id'
            },
            {
                name:'name'
            }
        ]
    });
    moduleStore.load({
        callback:function (records, options, success) {
            if (success) {
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
        callback:function (records, options, success) {
            if (success) {
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
            {
                name:'masterCspId'
            },
            {
                name:'cspId'
            }
        ]
    });
    cspCspStore.load({
        callback:function (records, options, success) {
            if (success) {
                sequenceDo();
            }
        }
    });
    var sequence = 0;

    function sequenceDo() {
        sequence++;
        if (sequence == 4) {
            //dataListStore.load({params: {start:0,limit:pageSize}});
            initSpCombo();
            initCpCombo();
            loadData();
        }
    }

    function initSpCombo() {
        var datas = [];
        for (var i = 0; i < cspStore.getCount(); i++) {
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isSp == 1) {
                datas[datas.length] = [storeRecord.data.id, storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('cc_cspId');
        obj.clearValue();
        obj.store.loadData(datas);
    }

    function initCpCombo() {
        var datas = [];
        for (var i = 0; i < cspStore.getCount(); i++) {
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isCp == 1) {
                datas[datas.length] = [storeRecord.data.id, storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('c_cspId');
        obj.clearValue();
        obj.store.loadData(datas);
    }


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect:false,
        checkOnly:true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"浏览资源",
        width:tableWidth,
        height:tableHeight + 55,
        store:dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns:[
            checkSelect,
            {
                header:"ID",
                dataIndex:'c_id',
                width:50,
                hidden:true,
                sortable:true,
                align:'center'
            },
            {
                header:"名称",
                dataIndex:'c_name',
                width:298,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    if (canDoThisAction('contentView')) {
                        return '<a target="_blank" href="contentViewOnly.jsp?action=view&id=' + row.data.c_id + '&moduleId=' + row.data.c_moduleId + '&isSystem=true">' + val + '</a>';
                    }
                }
            },

            {
                header:"主演",
                dataIndex:'c_actors',
                width:110,
                sortable:true,
                align:'center'
            },
            {
                header:"导演",
                dataIndex:'c_directors',
                width:100,
                sortable:true,
                align:'center'
            },
            {
                header:"资源模版",
                dataIndex:'c_moduleId',
                width:106,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = moduleStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return "";
                }
            },
            {
                header:"入库时间",
                dataIndex:'c_createTime',
                width:120,
                sortable:true,
                align:'center'
            },
            {
                header:"SP",
                dataIndex:'cc_cspId',
                width:128,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = cspStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return "";
                }
            },
            {
                header:"SP状态",
                dataIndex:'c_status',
                width:55,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    switch (parseInt(val)) {
                        case 0:
                            return '新资源';
                        case 1:
                            return '下线';
                        case 2:
                            return '上线';
                        case 3:
                            return '申请上线';
                        case 4:
                            return '已审核上线';
                        case 5:
                            return '审核拒绝上线';
                        case 6:
                            return '申请下线';
                        case 7:
                            return '审核拒绝下线';
                        case 8:
                            return '回收站';
                        case 9:
                            return '已删除';
                    }
                }
            },
            {
                header:"CP",
                dataIndex:'c_cspId',
                width:111,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = cspStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].name
                            }
                        }
                    }
                    return "";
                }
            },
            {
                header:"CP状态",
                dataIndex:'c_status',
                width:55,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    switch (parseInt(val)) {
                        case 1:
                            return '下线';
                        case 2:
                            return '上线';
                        case 3:
                            return '申请上线';
                        case 5:
                            return '审核拒绝上线';
                        case 6:
                            return '申请下线';
                        case 7:
                            return '审核拒绝下线';
                        case 8:
                            return '回收站';
                        case 9:
                            return '已删除';
                    }
                }
            },
            {
                header:"设备",
                dataIndex:'c_deviceId',
                width:100,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = deviceStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
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
                        style:{'margin-left':'0px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                        id:'searchForm',
                        width:'100%',
                        height:110,
                        labelWidth:60,
                        frame:true,
                        layout:'table',
                        layoutConfig:{columns:1},
                        baseCls:'x-plain',

                        items:[
                            searchForm
                        ]
                    })
                ]}
        ),

        bbar:new Ext.PagingToolbar({
            pageSize:pageSize,
            store:dataListStore,
            displayInfo:true,
            displayMsg:'结果数据 {0} - {1} of {2}',
            emptyMsg:"没有数据",
            items:[

            ]
        })
    });


    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action, id, moduleId) {
        document.location = 'contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId+'&isSystem=true';
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