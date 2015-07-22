<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"changeDisplayOrder","propertySelectChangeDisplayOrder");
        needPermissions(actionHeader,"search","propertySelectSearch");
        needPermissions(actionHeader,"view","propertySelectView");
        needPermissions(actionHeader,"save","propertySelectSave");
        needPermissions(actionHeader,"deleteSelected","propertySelectDeleteSelected");
        needPermissions(actionHeader,"propertySelectView","propertyManage");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
var viewWin ;
Ext.onReady(function(){
    var moduleId = <%=request.getParameter("moduleId")%>;
    var propertyId = <%=request.getParameter("propertyId")%>;

    var pageSize = 13;
    var tableWidth = 500;
    var tableHeight = 430;

    var actionUrl = "/module/propertySelect";

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
        url: actionUrl + "!search.action?obj.propertyId="+propertyId,
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'code'},
            {name:'status'},
            {name:'value'},
            {name:'displayOrder'}
        ]
    });
    dataListStore.setDefaultSort('displayOrder','asc');
    dataListStore.load({params:{start:0, limit:pageSize}});

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });
    var listGrid = new Ext.grid.EditorGridPanel({
        title:"属性可选项管理",
        width:tableWidth+150,
        height:tableHeight,
        store: dataListStore,

        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
       viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
            {header: "ID", dataIndex: 'id',hidden:true, width: 30, sortable: true, align:'left'},
            {header: "名称", dataIndex: 'name', width: 90, sortable: true, align:'left',
                renderer:
                        function (val,p,row){
                            if(canDoThisAction('propertySelectView')){
                            return '<a href=\'javascript:viewObj("view","'+row.data.id+'")\'>'+ val +'</a>';
                        }          }
            },

            {header: "CODE", dataIndex: 'code', width: 50, sortable: true, align:'left'},

            {header: "状态", dataIndex: 'status', width: 30, sortable: true, align:'center',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 0:return '失效';
                                case 1:return '有效';
                            }
                        }
            },
            {header: "显示顺序", dataIndex: 'displayOrder', width: 50, sortable: true, align:'center',
                editor: new Ext.form.TextField({
 	                    allowBlank: false
 	                })
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
                }},
                {text:'更改显示顺序',handler:function(){

                    var uploadData = "";
                    for (i=0; i<listGrid.getStore().getCount(); i++){
                        storeRecord = listGrid.getStore().getAt(i);
                        if (storeRecord.dirty){
                            uploadData += storeRecord.data.id + "_" + storeRecord.data.displayOrder + ",";
                        }
                    }

                    if (uploadData!=""){
                        uploadData = uploadData.substring(0,uploadData.length-1);
                        var remoteRequestStore = new Ext.data.JsonStore({
                            method:'POST',
                            url: actionUrl + "!changeDisplayOrder.action"
                        });
                        remoteRequestStore.reload({
                            params:{uploadData:uploadData},
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
                }},
                {text:'返回',handler:function(){
                    document.location='propertyList.jsp?moduleId='+moduleId;
                }}
            ]
        })
    });

    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action,id){
        var viewHtml =  '<iframe  frameborder="0" scrolling="no" width="100%" height="100%" src="propertySelectView.jsp?action='+ action +'&id='+ id +'&propertyId='+ propertyId +'"></iframe>';
        viewWin = new Ext.Window({
            //title:"文件列表",
            //x:0,
            y:70,
           width:tableWidth-65,
            height:tableHeight-218,
            closeAction:"hide",
          closable:true,
          bodyStyle:"padding:0px",   
            plain:true,
            //layout:'fit',
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