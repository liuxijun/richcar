<%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 13-11-19
  Time: 上午10:22
  展现当前用户上传影片的推荐信息
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
var viewWinZT;
Ext.onReady(function(){
<%
    long adminId = 0;
    if (admin != null && admin.getCspId()!=null){
        adminId = admin.getId();
    }
%>
    var adminId = <%=adminId%>;
    var recommendId=123;
    var pageSize = 12;
    var tableWidth = 750;
    var tableHeight = 500;
    var cspId=<%=admin.getCspId()%> ;

    var actionUrl = "/content/content";

    //刷新列表
    function loadData(){
        dataListStore.removeAll();
        dataListStore.reload({params: {start:0,limit:pageSize}});
        listGrid.getBottomToolbar().updateInfo();
    }

    var cpComboStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort:true,
        baseParams:{start:0, limit:10000000},
        url:"/csp/csp!list.action",
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
    cpComboStore.load({
        callback:function (records, options, success) {
            if (success) {
                sequenceDo();
            }
        }
    });

    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!spZipSearch.action?cc_cspId="+cspId+"&cc_status=2",
            totalProperty:"totalCount",
            root:'objs',
            fields:[
                    {name:'cr_id'},
                    {name:'c_id'},
                    {name:'c_name'},
                    {name:'c_post1Url'},
                    {name:'c_post2Url'},
                    {name:'c_moduleId'},
                    {name:'c_createTime'},
                    {name:'c_validEndTime'},
                    {name:'c_onlineTime'},
                    {name:'c_cspId'},
                    {name:'c_deviceId'},
                    {name:'c_status'},
                    {name:'cc_status'}
            ]
    });

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
//    暂时注册后面如果要查询数据再显示出来
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
        if (sequence == 3){
            dataListStore.load({params: {start:0,limit:pageSize}});
        }
    }


    var toolForm =  new Ext.FormPanel({
        style: {'margin-left':'10px','margin-top': '10px','margin-right':'0px','margin-bottom':'0px'},
        id:'toolForm',
        width: '100%',
        height : 30,
        labelWidth: 50,
        frame:true,
        layout:'table',
        layoutConfig: {columns:6},
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
            },
            {
                baseCls: 'x-btn-over',
                layout:'table',
                layoutConfig: {columns:6},
                items:[
                    {
                        text:'添加专题',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {

                                var viewHtml =  '<iframe  frameborder="0" scrolling="no" width="100%" height="100%"  src="cpRecommendAdd.jsp"></iframe>';
                                viewWinZT = new Ext.Window({
                                    //title:"文件列表",
                                    x:200,
                                    y:200,
                                    width:450,
                                    height:260,
                                    closeAction:"hide",
                                    closable:true,
                                    bodyStyle:"padding:0px",
                                    plain:true,
                                    html : viewHtml,
                                    listeners:{
                                        "show":function(){//alert("显示");
                                        },
                                        "hide":function(){
                                            loadData();
                                        },
                                        "close":function(){//alert("关闭");
                                        }
                                    }
                                })
                                viewWinZT.show();
                            }
                        }
                    }
                ]
            },
            {
                baseCls: 'x-btn-over',
                layout:'table',
                layoutConfig: {columns:6},
                items:[
                    {
                        text:'添加外链',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function()
                            {

                                var viewHtml =  '<iframe  frameborder="0" scrolling="no" width="100%" height="100%"  src="../publish/systemRecommendAdd.jsp"></iframe>';
                                viewWinZT = new Ext.Window({
                                    //title:"文件列表",
                                    x:200,
                                    y:200,
                                    width:450,
                                    height:260,
                                    closeAction:"hide",
                                    closable:true,
                                    bodyStyle:"padding:0px",
                                    plain:true,
                                    html : viewHtml,
                                    listeners:{
                                        "show":function(){//alert("显示");
                                        },
                                        "hide":function(){
                                            loadData();
                                        },
                                        "close":function(){//alert("关闭");
                                        }
                                    }
                                })
                                viewWinZT.show();
                            }
                        }
                    }
                ]
            },
            {
                baseCls: 'x-btn-over',
                layout:'table',
                layoutConfig: {columns:6},
                items:[
            {
                text:'删除',
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
                        }
                        keyIds = keyIds.substr(0,keyIds.length-1);
                        Ext.MessageBox.confirm("请您确认操作", " 删除推荐资源 ",function(btn){
                            if(btn=="yes"){

                                var remoteRequestStore = new Ext.data.JsonStore({
                                        method:'POST',
                                        url: actionUrl + "!deleteZipRecord.action"
                                });
                                remoteRequestStore.reload({
                                    params:{keyIds:keyIds},
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


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });



    var listGrid = new Ext.grid.EditorGridPanel({
        title:"推荐设置",
        width:tableWidth+30,
        height:tableHeight,
        store: dataListStore,
        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },
        sm:checkSelect,
        columns: [
                checkSelect,
                {header: "ID", dataIndex: 'c_id',hidden:true, width: 50, sortable: true, align:'left'},
                {header: "名称", dataIndex: 'c_name', width: 100, sortable: true, align:'center'
//                    ,
//                    renderer:
//                        function (val,p,row){
//                            if(canDoThisAction('recommendView')) {
//                            return '<a href=\'javascript:viewObj("view","'+row.data.c_id+'",'+row.data.c_moduleId+')\'>'+ val +'</a>';
//                        }        }
                },

                {header: "Html地址", dataIndex: 'c_post1Url', width: 170, sortable: true, align:'center'},
                {header: "海报地址", dataIndex: 'c_post2Url', width: 110, sortable: true, align:'center'},
                { header: "SP名称", dataIndex: 'c_cspId', width: 80, sortable: true, align:'center',
                  renderer:function (val, p, row) {
                    var objs = cpComboStore.data.items;
                      if(null == val){
                          return "专题海报"
                      }
                    if(objs){
                        for(var i =0;i< objs.length;i++){
                            if(objs[i].id == val){
                                return objs[i].data.name;
                             }
                          }
                      }
                  }
                },
                {header: "资源模版", dataIndex: 'c_moduleId', width: 60, sortable: true, align:'center'
                },
                {header: "入库时间", dataIndex: 'c_createTime', width: 120, sortable: true, align:'center'},
                {header: "CP状态", dataIndex: 'c_status', width: 55, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
                            case 1:return '下线';
                            case 2:return '上线';
                            case 3:return '申请上线';
                            case 5:return '审核拒绝上线';
                            case 6:return '申请下线';
                            case 7:return '审核拒绝下线';
                            case 8:return '回收站';
                            case 9:return '已删除';
                        }
                    }
                },
                {header: "SP状态", dataIndex: 'c_status', width: 55, sortable: true, align:'center',
                    renderer:function(val,p,row){
                        switch(parseInt(val)){
                            case 0:return '新资源';
                            case 1:return '下线';
                            case 2:return '上线';
                            case 3:return '申请上线';
                            case 4:return '已审核上线';
                            case 5:return '审核拒绝上线';
                            case 6:return '申请下线';
                            case 7:return '审核拒绝下线';
                            case 8:return '回收站';
                            case 9:return '已删除';
                        }
                    }
                }
        ],

        tbar:new Ext.Toolbar({items:[

                        new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:40,
                            labelWidth: 60,
                            frame:true,
                            layout:'table',
                            layoutConfig: {columns:1},
                            baseCls: 'x-plain',

                            items: [
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
    this.viewObj = function (action,id,moduleId){
        document.location='/content/contentViewOnly.jsp?action='+ action +'&id='+ id +'&moduleId='+moduleId;
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