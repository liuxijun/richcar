<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","propertyManage,propertyView");
        needPermissions(actionHeader,"search","propertyManage,propertySearch");
        needPermissions(actionHeader,"save","propertyManage,propertySave");
        needPermissions(actionHeader,"changeDisplayOrder","propertyManage,propertyChangeDisplayOrder");
        needPermissions(actionHeader,"deleteSelected","propertyManage,propertyDeleteSelected");
        needPermissions(actionHeader,"modulePropertySelect","propertyManage");
        needPermissions(actionHeader,"propertyView","propertyManage");
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

    var moduleId = <%=request.getParameter("moduleId")%>;

    var pageSize = 15;
    var tableWidth = 650;
    var tableHeight = 500;

    var actionUrl = "/module/property";

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
        url: actionUrl + "!search.action?obj.moduleId="+moduleId,
        totalProperty:"totalCount",
         baseParams:{limit:pageSize},
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
            {name:'columnName'},
            {name:'status'},{name:'relatedTable'},
            {name:'displayOrder'}
        ]
    });
    dataListStore.setDefaultSort('displayOrder','asc');
    dataListStore.load({params:{start:0}});

    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox
    });
    var listGrid = new Ext.grid.EditorGridPanel({
        title:"属性管理",
        width:tableWidth+52,
        height:tableHeight-10,
        store: dataListStore,

        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        //viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [
            checkSelect,
            {header: "ID", dataIndex: 'id',hidden:true, width: 30, sortable: true, align:'left'},
            {header: "名称", dataIndex: 'name', width: 150, sortable: true, align:'left',
                renderer:
                        function (val,p,row){
                            if(canDoThisAction('propertyView'))    {
                            return '<a href=\'javascript:viewObj("view","'+row.data.id+'")\'>'+ val +'</a>';
                        }          }
            },

            {header: "CODE", dataIndex: 'code', width: 150, sortable: true, align:'left'},
            {header: "数据类型", dataIndex: 'dataType', width: 100, sortable: true, align:'left',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '文本_单行';
                                case 2:return '文本_多行';
                                case 3:return '文本_数字';
                                case 4:return '文本_日期';
                                case 5:return '选择项_下拉框';
                                case 6:return '选择项_单选框';
                                case 7:return '选择项_复选框';
                                case 8:return '文件_WMV';
                                case 9:return '文件_FLV';
                                case 10:return '文件_MP4';
                                case 11:return '文件_图片';
                                case 12:return '文件_网页';
                                case 13:return '文件_ZIP文件';                            
                            }
                        }
            },
            {header: "是否多行",hidden:true, dataIndex: 'isMultiLine', width: 62,align:'center', sortable: true,
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 0:return '单行';
                                case 1:return '多行';
                            }
                        }
            },
            {header: "合并保存",hidden:true, dataIndex: 'isMerge',align:'center', width: 66, sortable: true, align:'left',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 0:return '否';
                                case 1:return '是';
                            }
                        }
            },
            {header: "可空",hidden:true, dataIndex: 'isNull', width: 42, sortable: true, align:'left',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 0:return '否';
                                case 1:return '是';
                            }
                        }
            },
            {header: "存在主表", dataIndex: 'isMain', width: 65, sortable: true, align:'center',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 0:return '否';
                                case 1:return '是';
                            }
                        }
            },
            {header: "主表列名",hidden:true, dataIndex: 'columnName', width: 63, sortable: true, align:'left'},
            {header: "状态", dataIndex: 'status', width: 45, sortable: true, align:'center',
                renderer:
                        function (val,p,row){
                            switch(val){
                                case 99:return '失效';
                                case 1:return '有效';
                            }
                        }
            },
            {header: "显示顺序", dataIndex: 'displayOrder', width: 81, sortable: true, align:'center',
                editor: new Ext.form.TextField({
 	                    allowBlank: false
 	                })

            },
            {header: "管理可选项", dataIndex: 'id', width: 80, sortable: true, align:'center',
                renderer:
                        function (val,p,row){
                            var dataType = row.data.dataType;
                            var relatedTable = parseInt(row.data.relatedTable);
                            if (canDoThisAction('modulePropertySelect')) {
                            if ((dataType==5|| dataType==6|| dataType==7)&&
                                    relatedTable==0){
                                return '<a href=\'propertySelectList.jsp?moduleId='+moduleId+'&propertyId='+val+'\'>管理</a>';
                            }
                                return "";
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
                    document.location='moduleList.jsp';
                }}
            ]
        })
    });

    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action,id){
        var viewHtml =  '<iframe  frameborder="0" scrolling="no" width="100%" height="100%" src="propertyView.jsp?action='+ action +'&id='+ id +'&moduleId='+ moduleId +'"></iframe>';
         viewWin = new Ext.Window({
            //title:"文件列表",
            //x:0,
            y:20,
            width:tableWidth-108,
            height:tableHeight+28    , 
            closeAction:"hide",
            closable:true,
            bodyStyle:"padding:0px",
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