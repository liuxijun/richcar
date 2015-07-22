<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"cpSearch","cpContentList,contentCpSearch");
        needPermissions(actionHeader,"cpChangeStatus","contentCpChangeStatus");
         needPermissions(actionHeader,"cpContentView","cpContentManage");
        needPermissions(actionHeader,"add","contentAdd");
    }
    boolean useNewVersion = true;// AppConfigurator.getInstance().getBoolConfig("system.compactMode",false);
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
<script language="javascript">
var viewWin;
Ext.onReady(function(){
<%
    long cpId = 0;
    if (admin != null && admin.getCspId()!=null){
        cpId = admin.getCspId();
    }
%>
    var cpId=<%=cpId%>;

    var pageSize = 13;
    var tableWidth = 800;
    var tableHeight = 460;


    var actionUrl = "/content/content";
    //刷新列表
    //刷新列表

    function loadData(){
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params:{start:listGrid.toolbars[1].cursor,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }
    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!cpSearch.action?c_cspId="+cpId,
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'c_id'},
                    {name:'c_name'},
                    {name:'c_actors'},
                    {name:'c_directors'},
                    {name:'c_moduleId'},
                    {name:'c_createTime'},
                    {name:'c_validEndTime'},
                    {name:'c_onlineTime'},
                    {name:'c_cspId'},
                    {name:'c_status'},
                    {name:'c_deviceId'}                    
            ]
    });
    dataListStore.setDefaultSort('c_createTime','desc');


    var deviceStore = new Ext.data.JsonStore({
         method:'POST',
         url:"/system/device!list.action",
         baseParams:{limit:1000000},
         totalProperty:'totalCount',
         root:'objs',
         fields:[
             {name:'id'},
             {name:'name'}
         ]
     });
    deviceStore.load({
        callback :
                function(records,options,success){
                    if(success){
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
             {name:'id'},
             {name:'name'}
         ]
     });
    moduleStore.load({
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
            dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }

    var moduleComboStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: "/module/module!getCspModuleList.action",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'id'},
                    {name:'name'}
            ]
    });
    moduleComboStore.load({params:{cpId:cpId, start:0, limit:10000000}});

    var toolForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '10px','margin-right':'0px','margin-bottom':'0px'},
        id:'toolForm',
        width: '100%',
        height : 33,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:3},
        baseCls: 'x-plain',

        items: [
            {
                baseCls: 'x-plain',
                layout: 'form',
                items: [
                    {
                        xtype: 'label',
                        labelSeparator : '',
                        fieldLabel: '工具栏:'
                    }]
            }
            ,
            {
                baseCls: 'x-plain',
                layout: 'form',
                labelWidth: 60,
                items: [

                    {
                        hiddenName:'moduleId',
                        xtype: 'combo',
                        labelWidth: 50,
                        fieldLabel: '资源模版',
                        width:110,
                        //allowBlank:false,
                        triggerAction: 'all',
                        emptyText:'请选择...',
                        //originalValue:'',
                        store: moduleComboStore,
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
                                //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                            }
                        }

                    }]
            },
            {
                baseCls: 'x-btn-over',
                layout:'column',
                layoutConfig: {columns:6},
                items:[
                    {
                        text: '添加',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var moduleId = toolForm.getForm().findField("moduleId").getValue();
                                if (moduleId==''){
                                    Ext.Msg.alert("提示","请选择资源模版！");
                                    return;
                                }
                                viewObj('add','',moduleId);
                                //刷新列表
                                //loadData();
                            }
                        }
                    },
                    {
                        text: '申请上线',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length ==0){
                                    Ext.Msg.alert("提示","未选择记录！");
                                    return;
                                }
                                var keyIds="";
                                for(var i=0; i<row.length; i++){
                                    keyIds +=  row[i].get('c_id') + ",";
                                    if (row[i].get('c_status')=='2'){
                                        Ext.Msg.alert("提示","选中资源部分已上线！");
                                        return;
                                    }
                                }
                                keyIds = keyIds.substr(0,keyIds.length-1);
                                Ext.MessageBox.confirm("请您确认操作", " 申请上线 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!cpChangeStatus.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,status:3},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                                        }

                                                    }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '申请下线',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length ==0){
                                    Ext.Msg.alert("提示","未选择记录！");
                                    return;
                                }
                                var keyIds="";
                                for(var i=0; i<row.length; i++){
                                    keyIds +=  row[i].get('c_id') + ",";
                                    if (row[i].get('c_status')!='2'){
                                        Ext.Msg.alert("提示","选中资源部分已下线！");
                                        return;
                                    }
                                }
                                keyIds = keyIds.substr(0,keyIds.length-1);
                                Ext.MessageBox.confirm("请您确认操作", " 申请下线 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!cpChangeStatus.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,status:6},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                                        }

                                                    }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text:'移至回收站',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length ==0){
                                    Ext.Msg.alert("提示","未选择记录！");
                                    return;
                                }
                                var keyIds="";
                                for(var i=0; i<row.length; i++){
                                    keyIds +=  row[i].get('c_id') + ",";
                                    if (row[i].get('c_status')=='2'){
                                        Ext.Msg.alert("提示","选中资源部分已上线,请先申请下线！");
                                        return;
                                    }
                                }
                                keyIds = keyIds.substr(0,keyIds.length-1);
                                Ext.MessageBox.confirm("请您确认操作", " 移至回收站 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!cpChangeStatus.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,status:8},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                                        }

                                                    }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: '删除',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length ==0){
                                    Ext.Msg.alert("提示","未选择记录！");
                                    return;
                                }
                                var keyIds="";
                                for(var i=0; i<row.length; i++){
                                    keyIds +=  row[i].get('c_id') + ",";
                                    if (row[i].get('c_status')=='2'){
                                        Ext.Msg.alert("提示","选中资源部分已上线,请先申请下线！");
                                        return;
                                    }
                                }
                                keyIds = keyIds.substr(0,keyIds.length-1);
                                Ext.MessageBox.confirm("请您确认操作", " 删除记录 ",function(btn){
                                    if(btn=="yes"){

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                                method:'POST',
                                                url: actionUrl + "!cpChangeStatus.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,status:9},
                                            callback :
                                                    function(records,options,success){
                                                        var returnData = this.reader.jsonData;
                                                        if (returnData.success){
                                                            //刷新列表
                                                            loadData();
                                                            Ext.MessageBox.alert('提示','操作成功');
                                                        } else {
                                                             Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                                        }

                                                    }
                                        });
                                    }
                                });

                            }
                        }
                    }






                ]
            }
        ]
    });


    var searchForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        id:'searchForm',
        width: '100%',
        height : 90,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:4},
        baseCls: 'x-plain',

        items: [
                 new Ext.form.FieldSet({
                     title:'搜索栏',
                     width:700,
                     height:79,
                     items:[
            {
                baseCls: 'x-plain',
                layout:'table',
                layoutConfig: {columns:4},
                items: [
       /*                 {
                baseCls: 'x-plain',
                layout: 'form',
                items: [
                    {
                        xtype: 'label',
                        labelSeparator : '',
                        fieldLabel: '搜索栏:'
                    }]
            }
            ,  */   {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth:75,
                        items: [
                            {
                                name:'startDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　开始时间',
                                width:100
                            }]
                    }, {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 90,
                        items: [
                            {
                                hiddenName:'c_status',
                                xtype: 'combo',
                                labelWidth: 90,
                                fieldLabel: '　　　资源状态',
                                width:100,
                                //allowBlank:false,
                                triggerAction: 'all',
                                emptyText:'请选择...',
                                //originalValue:'',
                                store:  new Ext.data.ArrayStore({
                                        fields: ['id', 'name'],
                                        data: [['1', '下线'], ['2', '上线'],['202','转码失败']]
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
                                        //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                    }
                                }

                            }]
                    }, {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 90,
                        items: [
                            {
                                name:'c_name',
                                xtype: 'textfield',
                                labelWidth: 60,
                                fieldLabel: '　　　资源名称',
                                width:140
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
                            }
                        ]
                    },
                 

                    {
                        baseCls: 'x-plain',
                        layout: 'form',
                        labelWidth: 75,
                        items: [
                            {
                                name:'endDate',
                                xtype: 'datefield',
                                format:'Y-m-d',
                                labelWidth: 60,
                                fieldLabel: '　　结束时间',
                                width:100
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



    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });
    var listGrid = new Ext.grid.GridPanel({
        title:"资源管理",
        width:tableWidth,
        height:tableHeight+59   ,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
                {header: "ID", dataIndex: 'c_id', width: 50,hidden:true, sortable: true, align:'left'},
                {header: "名称", dataIndex: 'c_name', width: 150, sortable: true, align:'left',
                    renderer:
                        function (val,p,row){
                            if(canDoThisAction('cpContentView'))    {
                            return '<a href=\'javascript:viewObj("view","'+row.data.c_id+'",'+row.data.c_moduleId+','+row.data.c_status+',"'+row.data.c_name+'",'+row.data.c_deviceId+')\'>'+ val +'</a>';
                        }}
                },

                {header: "导演", dataIndex: 'c_actors', width: 125, sortable: true, align:'center'},
                {header: "副导演", dataIndex: 'c_directors', width: 100, sortable: true, align:'center'},
                {header: "资源模版", dataIndex: 'c_moduleId', width: 105, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        var objs = moduleStore.reader.jsonData.objs;
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
                {header: "入库时间", dataIndex: 'c_createTime', width: 120, sortable: true, align:'center'},
                {header: "CP状态", dataIndex: 'c_status', width:55, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
                            case 1:return '下线';
                            case 2:return '上线';
                            case 3:return '申请上线';
                            case 5:return '审核拒绝上线';
                            case 6:return '申请下线';
                            case 7:return '审核拒绝下线';
                            case 202:return '转码失败';
                            case 8:return '回收站';                        
                        }
                    }
                },
                {header: "海报修改", dataIndex: 'c_deviceId', width: 120, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        return "<a target='_blank' href='../content/uploadContentImage.jsp?deviceId="+val+
                                "&contentId="+row.data.c_id+"&contentName="+encodeURI(encodeURI(row.data.c_name))+
                                "&moduleId="+row.data.c_moduleId+"'>修改海报</a>";
                    }
                }
        ],

        tbar:new Ext.Toolbar({items:[

                        new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:135,
                            labelWidth: 60,
                            frame:true,
                            layout:'table',
                            layoutConfig: {columns:1},
                            baseCls: 'x-plain',

                            items: [
                                    searchForm,
                                    toolForm                                    
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

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action,id,moduleId,status,name,deviceId){
        var viewHtml="";
        var url;
        <%
        if(useNewVersion){
%>      url = "../../V5/media/mediaView.jsp?iframe=true&keyId="+id+"&moduleId="+moduleId+"&channelId="+deviceId;
        if (status && status==2){
            //url+="&readOnly=true";
            url = 'contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId;
        }
        <%
        }else{
%>
        if (status && status==2){
            url = 'contentViewOnly.jsp?action=' + action + '&id=' + id + '&moduleId=' + moduleId;
        }else{
            url = 'contentAddV2.jsp?action='+ action +'&contentId='+ id +'&moduleId='+moduleId+'&contentName='+encodeURI(encodeURI(name))+'&deviceId='+deviceId;
        }
        <%
        }
        %>
        window.open(url);
/*
        viewHtml = '<iframe scrolling="yes" frameborder="0" width="100%" height="100%" src="'+url+'"></iframe>';
        var cmpId = 'viewMediaWin';
        try {
            viewWin = Ext.getCmp(cmpId);
            if(viewWin!=null){
                viewWin.close();
            }
        } catch (e) {
        }
        viewWin = new Ext.Window({
            //title:"文件列表",
            width:950,
            height:tableHeight+200,
            closeAction:"hide",
            closable:true,
            id:cmpId,
            bodyStyle:"padding:0px",
            plain:true,
            html : viewHtml,
            listeners:{
                "show":function(){//alert("显示");
                },
                "hide":function(){
                    loadData();
                },
                "close":function(){  //alert("关闭");
                }
            }
        });
        viewWin.show();
*/
    }
});

function closeMediaViewWin() {
    if(viewWin!=null){
        viewWin.close();
    }
}
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