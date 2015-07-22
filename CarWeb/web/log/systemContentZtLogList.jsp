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
    var pageSize = 12;
    var tableWidth = 650;
    var tableHeight = 500;


    var actionUrl = "/log/visitLog";

    var store = new Ext.data.SimpleStore({
        fields : ['value', 'name'],
        data : [['1', '专题页面统计'], ['0', '专题页面资源点播']]
    });

    var searchForm =  new Ext.FormPanel({
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
                                labelWidth: 70,
                                items: [
                                    {
                                        hiddenName:'vl_cspId',
                                        xtype: 'combo',
                                        labelWidth: 90,
                                        fieldLabel: '　　　　SP',
                                        width:140,
                                        //allowBlank:false,
                                        triggerAction: 'all',
                                        emptyText:'请选择...',
                                        //originalValue:'',
                                        store:  new Ext.data.ArrayStore( {
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

                                    }]
                            },
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
                                labelWidth: 70,
                                items: [
                                    {
                                        hiddenName : 'vl_isSpecial',
                                        xtype: 'combo',
                                        labelWidth: 60,
                                        fieldLabel: '　查询类型',
                                        emptyText:'请选择...',
                                        width:140,
                                        mode : 'local',
                                        triggerAction : 'all',
                                        store:store,
                                        valueField: 'value',
                                        displayField: 'name'
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
        url: actionUrl + "!contentZtLogs.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'vl_count'},
            {name:'vl_contentId'},
            {name:'vl_type'},
            {name:'vl_cspId'},
            {name:'vl_isSpecial'}
        ],
        listeners:{
            load: function(store){

                var objs= store.reader.jsonData.objs;
                var ids='';
                if (objs){
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
                            params:{keyIds:ids,limit:12},
                            callback :
                                    function(records,options,success){
                                        var names = this.reader.jsonData.objs;
                                        var ids = dataListStore.reader.jsonData.objs;
                                        if (names && ids){
                                            for (var i=0; i<ids.length; i++){
                                                for (var j=0;j<names.length; j++){
                                                    if (ids[i].vl_contentId==names[j].c_id){
                                                        if(ids[i].vl_type==1){
                                                            var spanObj = document.getElementById(ids[i].vl_contentId+"_1");
                                                        }else if(ids[i].vl_type==2){
                                                            var spanObj = document.getElementById(ids[i].vl_contentId+"_2");
                                                        }else{
                                                            var spanObj = document.getElementById(ids[i].vl_contentId);
                                                        }
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

    var sequence = 0;
    function sequenceDo(){
        sequence++;
        if (sequence == 2){
            loadData();
        }
        if(sequence == 1){
            initSpCombo();
        }
    }
    function initSpCombo(){
        var datas = [];
        for (var i=0; i<cspStore.getCount(); i++){
            var storeRecord = cspStore.getAt(i);
            if (storeRecord.data.isSp==1){
                datas[datas.length]=[storeRecord.data.id,storeRecord.data.name];
            }
        }
        var obj = searchForm.getForm().findField('vl_cspId');
        obj.clearValue();
        obj.store.loadData(datas);
    }

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.GridPanel({
        title:"专题点播量统计",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            {header: "SP名称", dataIndex: 'vl_cspId', width:100, sortable: true, align:'center',
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
            {header: "资源名称", dataIndex: 'vl_contentId', width:120, sortable: true, align:'center',
                renderer:function(val,p,row){
                    if(row.data.vl_type==2){
                        return '<span id="'+row.data.vl_contentId+'_2">'+val+'</span>';
                    }else if(row.data.vl_type==1){
                        return '<span id="'+row.data.vl_contentId+'_1">'+val+'</span>';
                    }else{
                        return '<span id="'+row.data.vl_contentId+'">'+val+'</span>';
                    }
                }
            },
            {header: "资源类型", dataIndex: 'vl_isSpecial', width:120, sortable: true, align:'center',
                renderer:function(val,p,row){
                    if("0" == val){
                        return '普通资源';
                    }
                    if("1" == val){
                        return '专题页面';
                    }
                }
            },
            {header: "点播方式", dataIndex: 'vl_type', width: 120, sortable: true, align:'center',
                renderer:function(val,p,row){
                    if("0" == val){
                        return '专题页面点播';
                    }
                    if("1" == val){
                        return '首页推荐点击';
                    }
                    if("2" == val){
                        return '专题频道页点击';
                    }
                }},
            {header: "点播次数", dataIndex: 'vl_count', width: 120, sortable: true, align:'center'}
        ],

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