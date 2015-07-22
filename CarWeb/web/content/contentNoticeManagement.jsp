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
        long adminId = 0;
        if (admin != null && admin.getCspId()!=null){
            adminId = admin.getId();
        }
    %>
    var adminId = <%=adminId%>;
    var pageSize = 10;
    var tableWidth = 750;
    var tableHeight = 500
    var actionHeader="contentNotice";

    var actionUrl = "/content/contentNotice";
</script>

<script language="javascript">
Ext.onReady(function () {
    //刷新列表
    this.loadData = function () {
        var form1 = searchForm.getForm();
        dataListStore.removeAll();
        dataListStore.baseParams = form1.getValues();
        dataListStore.reload({params:{start:0, limit:pageSize}});
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
        remoteSort:true,
        url:"/content/contentNotice!list.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'content'
            },
            {
                name:'status'
            },
            {
                name:'createTime'
            },
            {
                name:'onlineTime'
            },
            {
                name:'offlineTime'
            },
            {
                name:'adminId'
            }
        ]
    });
    dataListStore.setDefaultSort('createTime', 'desc');


    var adminStore = new Ext.data.JsonStore({
        method:'POST',
        baseParams:{limit:1000000},
        url:"/security/admin!list.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'id'
            },
            {
                name:'login'
            } ,
            {
                name:'realName'
            }
        ]
    });
    adminStore.load({
        callback:function (records, options, success) {
            if (success) {
                sequenceDo();
            }
        }
    });

    var sequence = 0;
    function sequenceDo() {
        sequence++;
        if (sequence == 2) {
            dataListStore.load({params:{start:0, limit:pageSize}});
        }
    }

     defaultBottomButtons = [
        {
            text:'添加记录',
            handler:addRecord,
            action:'add'
        }
    ];

    var toolForm = new Ext.FormPanel({
        style:{'margin-left':'10px', 'margin-top':'10px', 'margin-right':'0px', 'margin-bottom':'0px'},
        id:'toolForm',
        width:'100%',
        height:35,
        labelWidth:50,
        frame:true,
        layout:'table',
        layoutConfig:{columns:3},
        baseCls:'x-plain',

        items:[
            {
                baseCls:'x-plain',
                layout:'form',
                items:[
                    {
                        xtype:'label',
                        labelSeparator:'',
                        fieldLabel:'工具栏:'
                    }
                ]
            },
            {
                baseCls:'x-btn-over',
                layout:'column',
                layoutConfig:{columns:6},
                items:[
                    {
                        text:'上线',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                if(row.length >1){
                                    Ext.Msg.alert("提示","不能选择多条记录！");
                                    return;
                                }
                                var keyIds = "";
                                var status = "";
                                keyIds = row[0].get('id');
                                status = row[0].get('status');
                                if(status==2){
                                    Ext.Msg.alert("错误","内容已上线，不能重复上线！");
                                    return;
                                }
                                Ext.MessageBox.confirm("请您确认操作", " 是否上线？ ", function (btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url:actionUrl + "!contentNotice_online.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds, status:2},
                                            callback:function (records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    loadData();
                                                    Ext.MessageBox.alert('提示', '操作成功');
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                                }

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text:'下线',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                var keyIds = "";
                                for (var i = 0; i < row.length; i++) {
                                    keyIds += row[i].get('id') + ",";
                                }
                                keyIds = keyIds.substr(0, keyIds.length - 1);
                                Ext.MessageBox.confirm("请您确认操作", "是否下线？", function (btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url:actionUrl + "!contentNotice_offline.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds,status:1},
                                            callback:function (records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    loadData();
                                                    Ext.MessageBox.alert('提示', '操作成功');
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    },
                    {
                        text:'删除',
                        xtype:'button',
                        minWidth:60,
                        listeners:{
                            "click":function () {
                                var row = listGrid.getSelectionModel().getSelections();
                                if (row.length == 0) {
                                    Ext.Msg.alert("提示", "未选择记录！");
                                    return;
                                }
                                var keyIds = "";
                                for (var i = 0; i < row.length; i++) {
                                    keyIds += row[i].get('id') + ",";
                                }
                                keyIds = keyIds.substr(0, keyIds.length - 1);
                                Ext.MessageBox.confirm("请您确认操作", " 删除记录？ ", function (btn) {
                                    if (btn == "yes") {

                                        var remoteRequestStore = new Ext.data.JsonStore({
                                            method:'POST',
                                            url:actionUrl + "!contentNotice_delete.action"
                                        });
                                        remoteRequestStore.reload({
                                            params:{keyIds:keyIds, status:9},
                                            callback:function (records, options, success) {
                                                var returnData = this.reader.jsonData;
                                                if (returnData.success) {
                                                    //刷新列表
                                                    loadData();
                                                    Ext.MessageBox.alert('提示', '操作成功');
                                                } else {
                                                    Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
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

    var searchForm = new Ext.FormPanel({
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
                        layoutConfig:{columns:4},
                        items:[
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:60,
                                items:[
                                    {
                                        name:'content',
                                        xtype:'textfield',
                                        labelWidth:60,
                                        fieldLabel:'内容',
                                        width:115
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:90,
                                items:[
                                    {
                                        name:'createTime',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　　　创建时间',
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
                                        name:'onlineTime',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        fieldLabel:'上线时间',
                                        width:130
                                    }
                                ]
                            },
                            {
                                baseCls:'x-plain',
                                layout:'form',
                                labelWidth:90,
                                items:[
                                    {
                                        name:'offlineTime',
                                        xtype:'datefield',
                                        format:'Y-m-d',
                                        labelWidth:60,
                                        fieldLabel:'　　　下线时间',
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

    var checkSelect = new Ext.grid.CheckboxSelectionModel({
        singleSelect:false,
        checkOnly:true  //true点击行不选中, false为点击行也选中checkbox
    });


    var listGrid = new Ext.grid.GridPanel({
        title:"走马灯内容管理",
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
                dataIndex:'id',
                hidden:true,
                width:50,
                sortable:true,
                align:'left'
            },
            {
                header:"内容",
                dataIndex:'content',
                width:200,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                   return '<a href=\'javascript:viewObj("' + row.data.id + '")\'>' + val + '</a>';
                }
            },

            {
                header:"状态",
                dataIndex:'status',
                width:50,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    if(val==1){
                       return "下线";
                     }else{
                       return "上线";

                     }
                }
            },
            {
                header:"创建时间",
                dataIndex:'createTime',
                width:105,
                sortable:true,
                align:'center'
            },
            {
                header:"上线时间",
                dataIndex:'onlineTime',
                width:105,
                sortable:true,
                align:'center'
            },
            {
                header:"下线时间",
                dataIndex:'offlineTime',
                width:105,
                sortable:true,
                align:'center'
            },
            {
                header:"操作员",
                dataIndex:'adminId',
                width:60,
                sortable:true,
                align:'center',
                renderer:function (val, p, row) {
                    var objs = adminStore.reader.jsonData.objs;
                    if (objs) {
                        for (var i = 0; i < objs.length; i++) {
                            if (objs[i].id == val) {
                                return objs[i].realname
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
                        height:139,
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
            items:defaultBottomButtons
        })
    });
    listGrid.render('display');

    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (keyId) {
        document.location = 'contentNoticeView.jsp?keyId=' + keyId + '';
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