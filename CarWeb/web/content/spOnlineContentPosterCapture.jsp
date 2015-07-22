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
        needPermissions(actionHeader, "OnlineView", "SpOnlineContentManage");
        needPermissions(actionHeader, "spApplyOffline", "contentSpApplyOffline,SpOnlineContentManage");
        needPermissions(actionHeader, "spSearch", "systemContentSpSearch,SpOnlineContentManage");
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
    <%
        long spId = 0;
        if (admin != null && admin.getCspId()!=null){
            spId = admin.getCspId();
        }
    %>
    var spId =<%=spId%>;

    var pageSize = 14;
    var tableWidth = 750;
    var tableHeight = 500;


    var actionUrl = "/content/content";
</script>

<%--
<script type="text/javascript" src="contentPublish.js"></script>
--%>

<script language="javascript">
var listGrid={};
var searchForm={};
var moduleId=-1;
var propertyStore = new Ext.data.JsonStore({
    method:'POST',
    url:"/module/property!list.action?obj.moduleId="+moduleId+"&obj.dataType=11&pageBean.orderBy=o1.name",
    baseParams:{limit:1000000},
    totalProperty:'totalCount',
    root:'objs',
    fields:[
        {name:'id'},
        {name:'name'},
        {name:'code'},
        {name:'dataType'},
        {name:'isMultiLine'},
        {name:'isMerge'},
        {name:'isNull'},
        {name:'isMain'},
        {name:'maxSize'},
        {name:'columnName'},
        {name:'relatedTable'},
        {name:'status'},
        {name:'displayOrder'}
    ]
});
propertyStore.load();
function startCapture(){
    dealPoster("capture");
}
function startReplace(){
    dealPoster("replace");
}
function dealPoster(command) {
    var selectAll = Ext.getCmp("selectAllRecords");
    var url = "contentPosterAutoCreator.jsp?command=" +command+
            "&c_status=2&cc_status=2&cc_cspId=" + spId+"&";
    var selected = false;
    if(selectAll!=null){
        if(selectAll.checked){
            selected = true;
            url +="createAll=true&"
        }
    }
    if(Ext.getCmp("replaceOldIfExists").checked){
        url += "replaceOldIfExists=true&";
    }
    var row = listGrid.getSelectionModel().getSelections();
    if (row.length == 0&&(!selected)) {
        Ext.Msg.alert("提示", "未选择记录！请选择至少一条记录或者勾选“选择所有符合条件的影片”");
        return;
    }
    var propertyId = getCmpValue("propertyId");
    if(propertyId == null||propertyId<=0||propertyId == "0"||propertyId == "-1"){
        Ext.Msg.alert("提示", "未选海报条目！");
        return;
    }
    url+="propertyId="+propertyId+"&";
    var i= 0,l=row.length;
    for(;i<l;i++){
        url +="contentId="+row[i].get("c_id")+"&";
    }
    url += "picSize="+getCmpValue("picSize")+"&";
    url += "startTime="+getCmpValue("startTime")+"&";
    var picUrl = getCmpValue("picUrl");
    if(picUrl==""||picUrl==null){
        if(command == "replace"){
            Ext.Msg.alert("提示", "必须输入已有海报地址");
            return;
        }
    }
    url+="picUrl="+picUrl+"&";
    Ext.MessageBox.show({
        msg: '正在提交，请稍候...',
        progressText: '正在提交...',
        width:300,
        wait:true,
        waitConfig: {interval:500},
        icon:'ext-mb-info' //custom class in msg-box.html
        //,animEl: 'mb7'
    });
    Ext.Ajax.request({
        url:url,
        params:searchForm.getForm().getValues(),
        callback:function(opt, success, response){
            Ext.MessageBox.hide();
            if (success) {
                var serverData = Ext.util.JSON.decode(response.responseText);
                if (serverData.success) {
                    Ext.Msg.alert("提示", "命令已经接受，正在处理，请稍候查看结果");
                } else {
                    Ext.Msg.alert("提示", "发生异常，请检查："+serverData.message);
                }
            }
        }
    });
}
Ext.onReady(function () {

    //刷新列表
    this.loadData = function () {
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params:{start:listGrid.toolbars[1].cursor, limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:"/content/content!spSearch.action?c_status=2&cc_status=2&cc_cspId=" + spId,
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
    var sequence = 0;

    function sequenceDo() {
        sequence++;
        if (sequence == 3) {
            dataListStore.load({params:{start:0, limit:pageSize}});
        }
    }


    var toolForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'10px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'toolForm',
        width:'100%',
        height:70,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:7},
        baseCls:'x-plain',
        items:[
            {
                xtype:'checkbox',
                boxLabel:'所有符合条件影片',
                id:'selectAllRecords',
                name:'selectAllRecords',
                checked:false
            },
            {
                xtype:'label',
                text:'画面大小：'
            },
            {
                xtype:'textfield',
                id:'picSize',
                name:'picSize',
                width:100,
                value:'450x360'
            },
            {
                xtype:'label',
                text:'，启动（秒）:'
            },
            {
                xtype:'textfield',
                id:'startTime',
                name:'startTime',
                width:70,
                value:'300'
            },{
                xtype:'label',
                text:'，海报图类型：',
                width:40
            },
            {
                hiddenName:'propertyId',
                hiddenId:'propertyId',
                xtype:'combo',
                labelWidth:100,
                fieldLabel:'海报',
                width:150,
                //allowBlank:false,
                triggerAction:'all',
                emptyText:'请选择...',
                //originalValue:'',
                store:propertyStore,
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

            },{
                xtype:'checkbox',
                boxLabel:'替换已经存在的海报',
                id:'replaceOldIfExists',
                name:'replaceOldIfExists',
                checked:false
            },{
                xtype:'label',
                align:'right',
                text:'图片连接：'
            },{
                xtype:'textfield',
                width:250,
                id:'picUrl',
                colspan:3,
                name:'picUrl'
            },{
                text:'使用图片连接',
                xtype:'button',
                handler:startReplace
            },
            {
                text:'使用视频截图',
                xtype:'button',
                minWidth:60,
                listeners:{
                    "click":startCapture
                }
            }
        ]
    });


    var cpComboStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:"/csp/csp!getCspBySp.action",
        totalProperty:"totalCount",
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
    cpComboStore.load({params:{spId:spId, start:0, limit:10000000}});

    var channelStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        url:"/publish/channel!searchAll.action?obj.type=1&obj.cspId=" + spId,
        totalProperty:"totalCount",
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
    channelStore.setDefaultSort("id", "ASC");
    channelStore.load({params:{start:0, limit:1000000},
        callback:function (r, options, success) {
            if (success == true) {
                channelStore.insert(0, new Ext.data.Record({id:'', name:'全部'}, ''));
            }
        }
    });

    searchForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'5px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'searchForm',
        width:'100%',
        height:95,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:2},
        baseCls:'x-plain',

        items:[
            new Ext.form.FieldSet({
                title:'搜索栏',
                width:650,
                height:80,
                items:[
                    {
                        baseCls:'x-plain',
                        layout:'table',
                        layoutConfig:{columns:5},
                        items:[
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelAlign:'right',
                                labelWidth:60,
                                items:[
                                    {
                                        hiddenName:'c_cspId',
                                        xtype:'combo',
                                        labelWidth:90,
                                        fieldLabel:'　　　CP',
                                        width:130,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:cpComboStore,
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
                                labelWidth:90,
                                items:[
                                    {
                                        name:'startDate',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　　　开始时间',
                                        width:100
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:90,
                                items:[
                                    {
                                        name:'endDate',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　　　结束时间',
                                        width:100
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
                                labelWidth:60,
                                items:[
                                    {
                                        name:'c_name',
                                        xtype:'textfield',
                                        labelWidth:60,
                                        fieldLabel:'资源名称',
                                        width:130
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:90,
                                labelAlign:'left',
                                items:[
                                    {
                                        hiddenName:'channelId',
                                        xtype:'combo',
                                        labelWidth:100,
                                        fieldLabel:'      资源频道',
                                        width:130,
                                        //allowBlank:false,
                                        triggerAction:'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:channelStore,
                                        valueField:'id',
                                        displayField:'name',
                                        //value:'1',
                                        //mode: 'remote',
                                        colspan:2,
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


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect:false,
        checkOnly:true  //true点击行不选中, false为点击行也选中checkbox
    });


    listGrid = new Ext.grid.GridPanel({
        title:"上线资源",
        width:tableWidth - 30,
        height:tableHeight + 60,
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
                hidden:true,
                width:50,
                sortable:true,
                align:'left'
            },
            {
                header:"名称",
                dataIndex:'c_name',
                width:130,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    if (canDoThisAction('OnlineView')) {
                        return '<a href=\'javascript:viewObj("view","' + row.data.c_id + '",' + row.data.c_moduleId + ')\'>' + val + '</a>';
                    }
                }
            },

            {
                header:"主演",
                dataIndex:'c_actors',
                width:125,
                sortable:true,
                align:'center'
            },
            {
                header:"导演",
                dataIndex:'c_directors',
                width:100,
                hidden:true,
                sortable:true,
                align:'center'
            },
            {
                header:"资源模版",
                dataIndex:'c_moduleId',
                width:105,
                sortable:true,
                align:'center',
                hidden:true,
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
                header:"CP",
                dataIndex:'c_cspId',
                width:120,
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
                header:"设备",
                dataIndex:'c_deviceId',
                width:100,
                hidden:true,
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
            },{
                header:'',
                width:100,
                align:'center',
                dataIndex:'c_id',
                renderer:function(val,p,row){
                    return "<a target='_blank' href='../content/uploadContentImage.jsp?deviceId="+val+"&contentId="+val+"&contentName="+encodeURI(encodeURI(row.data.c_name))+"&moduleId="+row.data.c_moduleId+"'>修改海报</a>";
                }
            }

        ],

        tbar:new Ext.Toolbar({items:[

                    new Ext.Panel({
                        style:{'margin-left':'0px', 'margin-top':'0px', 'margin-right':'0px', 'margin-bottom':'0px'},
                        id:'searchForm',
                        width:'100%',
                        height:178,
                        labelWidth:60,
                        frame:true,
                        layout:'table',
                        layoutConfig:{columns:1},
                        baseCls:'x-plain',

                        items:[
                            searchForm,
                            toolForm
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
        document.location = 'contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId;
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