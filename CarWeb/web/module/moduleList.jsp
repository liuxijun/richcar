<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","moduleManage,moduleView");
        needPermissions(actionHeader,"search","moduleManage,moduleSearch");
        needPermissions(actionHeader,"add","moduleManage");
        needPermissions(actionHeader,"save","moduleManage,moduleSave");
        needPermissions(actionHeader,"deleteSelected","moduleDeleteSelected");
        needPermissions(actionHeader,"moduleProperty","moduleManage");
        needPermissions(actionHeader,"moduleView","moduleManage");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
    var viewWin;
Ext.onReady(function(){

    var pageSize = 14;
    var tableWidth = 620;
    var tableHeight = 430;

    var actionUrl = "/module/module";

    //刷新列表
    function loadData(){
        var searchField = Ext.getCmp('searchField');
        if (searchField.getValue()==''){
            dataListStore.removeAll();
            dataListStore.reload({params:{start:0,limit:pageSize}});
            listGrid.getBottomToolbar().updateInfo();
        }else{
            searchField.onTrigger2Click();
        }
    }

    var dataListStore = new Ext.data.JsonStore({
            method:'POST',
            remoteSort: true,
            url: actionUrl + "!search.action",
            totalProperty:"totalCount",
            baseParams:{limit:pageSize},
            root:'objs',
            fields:[
                    {name:'id'},
                    {name:'name'},
                    {name:'code'},
                    {name:'status'}
            ]
    });
    //dataListStore.setDefaultSort('m_mediaId','desc');
    dataListStore.load({params:{start:0, limit:pageSize}});

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });
    var listGrid = new Ext.grid.GridPanel({
        title:"资源模板管理",
        width:tableWidth,
        height:tableHeight,
        store: dataListStore,

        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
                {header: "ID", dataIndex: 'id', width: 65,hidden:true, sortable: true, align:'left'},
                {header: "名称", dataIndex: 'name', width: 140, sortable: true, align:'left',
                    renderer:
                        function (val,p,row){
                            if(canDoThisAction('moduleView'))   {
                            return '<a href=\'javascript:viewObj("view","'+row.data.id+'")\'>'+ val +'</a>';
                        }           }
                },

                {header: "CODE", dataIndex: 'code', width: 85, sortable: true, align:'center'},
                {header: "管理属性", dataIndex: 'id', width: 80, sortable: true, align:'center',
                    renderer:
                        function (val,p,row){
                            if (canDoThisAction('moduleProperty')) {
                              return '<a href=\'propertyList.jsp?moduleId='+val+'\'>管理</a>';
                            }
                        }
                }
        ],

        tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            id:'searchField',
                            store: dataListStore,
                            paramName:'obj.name',
                            width:320
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
                    {text:'添加记录',handler:function(){
                        viewObj('add','');
                        //viewWin.show();
                        //window.location.href='mediaView.jsp?action=add';
                    }},
                    {text:'删除所选',handler:function(){
                        var row = listGrid.getSelectionModel().getSelections();
                        if (row.length ==0){
                            Ext.Msg.alert("提示","未选择记录！");
                            return;
                        }
                        var keyIds="";
                        for(var i=0; i<row.length; i++){
                            keyIds +=  row[i].get('id') + ",";
                        }
                        keyIds = keyIds.substr(0,keyIds.length-1);
                        Ext.MessageBox.confirm("请您确认操作", " 删除记录 ",function(btn){
                            if(btn=="yes"){

                                var remoteRequestStore = new Ext.data.JsonStore({
                                        method:'POST',
                                        url: actionUrl + "!deleteSelected.action"
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
                    }}
                ]
        })
    });

    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action,id){
        var viewHtml =  '<iframe  frameborder="0" scrolling="no" width="100%" height="100%" src="moduleView.jsp?action='+ action +'&id='+ id +'"></iframe>';
         viewWin = new Ext.Window({
            //title:"文件列表",
            //x:0,
            y:20,
            width:tableWidth-98,
            height:tableHeight-75,
            closeAction:"hide",
            bodyStyle:"padding:0px",
            closable:true,
            plain:true,
            //layout:'fit',
            //collapsible:true,
            //plain: false,
            resizable: false,
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
        viewWin.show();
    }
})
</script>
</head>
<body>
<table align="center" width="660">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>