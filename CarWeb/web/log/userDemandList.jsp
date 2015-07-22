<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script language="javascript">
    var cpStore = new Ext.data.JsonStore({
        method:'POST',
        autoLoad:true,
        url:"/csp/csp!searchAllCp.action",
        root:'objs',
        totalProperty:'totalCount',
        fields:['id', 'name']
    });
    function initDisplay() {

        var channelViewForm = new Ext.form.FormPanel({
            title:'用户点播日志查询',
            border:true,
            url:'channel!view.action',
            saveUrl:'channel!save.action',
            width:600,
            frame:false,

            items:[
                new Ext.Toolbar({

                    items:[ new Ext.form.FieldSet({
                                title:'搜索栏',
                                width:600,
                                items:[
                                    {
                                        baseCls:'x-plain',
                                        layout:'column',
                                        items:[
                                            {
                                                columnWidth:.5,//第一列
                                                layout:"form",
                                                baseCls: 'x-plain',
                                                items:[{

                                                    xtype:"datefield",
                                                    fieldLabel: '开始时间',
                                                    name: 'startTime',
                                                    id:'startTime',
                                                    format:'Y-m-d 00:00:00',
                                                    labelWidth: 60,
                                                    width:150
                                                },{

                                                    xtype:"textfield",
                                                    fieldLabel: '资源名称',
                                                    name: 'contentName',
                                                    id:'contentName',
                                                    labelWidth: 60,
                                                    width:150
                                                },{

                                                    xtype:"textfield",
                                                    fieldLabel: '用户IP',
                                                    name: 'userIp',
                                                    labelWidth: 60 ,
                                                    width:150
                                                },{

                                                    xtype:"combo",
                                                    fieldLabel: '来源组织',
                                                    name: 'cpId',
                                                    hiddenName: 'cpId',
                                                    labelWidth: 60,

                                                    emptyText:'请选择...',
                                                    store:cpStore ,
                                                    valueField:'id',
                                                    displayField:'name',
                                                    forceSelection: true,
                                                    triggerAction: 'all',
                                                    selectOnFocus:true ,
                                                    width:150
                                                }
                                                ]
                                            },
                                            {
                                                baseCls:'x-plain',
                                                columnWidth:.5,//第二列
                                                layout:"form",
                                                items:[{

                                                    xtype:"datefield",
                                                    fieldLabel: '结束时间',
                                                    name: 'endTime',
                                                    id:'endTime',
                                                    format:'Y-m-d 23:59:59',
                                                    labelWidth: 60,
                                                    width:150
                                                },
                                                    {
                                                        fieldLabel: '频道名称',
                                                        labelWidth: 60,
                                                        width:150 ,
                                                        id: 'channelName',
                                                        name: 'channelName',
                                                        xtype:'textfield'
                                                    },{

                                                        xtype:"textfield",
                                                        fieldLabel: '用户ID',
                                                        name: 'userId',
                                                        labelWidth: 60,
                                                        width:150
                                                    },
                                                    {

                                                        fieldLabel: '点播时长≥(分钟)',
                                                        id:'playTime',
                                                        labelWidth: 60,
                                                        width:150,
                                                        id:'playTime',
                                                        name: 'playTime',
                                                        xtype:'numberfield'
                                                    }

                                                ]
                                            },{
                                                baseCls: 'x-btn-over',

                                                layout:"column",
                                                items:[{

                                                    text: '查询',
                                                    xtype:'button',
                                                    minWidth:60,
                                                    listeners:{
                                                        "click":function()
                                                        {
                                                            var startTime = Ext.getCmp("startTime").value;
                                                            var endTime = Ext.getCmp("endTime").value;
                                                            if(startTime==undefined||endTime==undefined){
                                                                Ext.MessageBox.show({
                                                                    title:"标题",
                                                                    msg:"请选择需要查询的时间段。",
                                                                    buttons:{"ok":"OK"},
                                                                    width:500,
                                                                    icon:Ext.MessageBox.INFO,
                                                                    closable:false



                                                                    // prompt:true
                                                                    // multiline:true
                                                                });

                                                                return ;
                                                            }


                                                            var form = channelViewForm.getForm().getEl().dom;
                                                            form.action = '/log/visitLog!getUserDemandCount.action?start=1&limit=12';
                                                            form.method = 'POST';
                                                            form.target = "_blank" ;
                                                            form.submit();
                                                        }
                                                    }
                                                },{

                                                },{
                                                    xtype:'button',
                                                    text: '导出数据',
                                                    handler:function(){
                                                        var startTime = Ext.getCmp("startTime").value;
                                                        var endTime = Ext.getCmp("endTime").value;
                                                        if(startTime==undefined||endTime==undefined){
                                                            Ext.MessageBox.show({
                                                                title:"标题",
                                                                msg:"请选择需要查询的时间段。",
                                                                buttons:{"ok":"OK"},
                                                                width:500,
                                                                icon:Ext.MessageBox.INFO,
                                                                closable:false



                                                                // prompt:true
                                                                // multiline:true
                                                            });

                                                            return ;
                                                        }


                                                        var form = channelViewForm.getForm().getEl().dom;
                                                        form.action = '/log/visitLog!getUserDemandCount.action?start=1&limit=100000&excel=true';
                                                        form.method = 'POST';
                                                        form.target = "_blank" ;
                                                        form.submit();
                                                    }
                                                }]
                                            }]
                                    }
                                ]
                            }
                    )]
                })
            ]
        });
        //loadFormAjax();
        channelViewForm.render(displayDiv);
//            root.expand(false,false);
    }
    Ext.onReady(function () {
        Ext.QuickTips.init();
        queueFunctions([
            {
                func:initDictStores,
                done:false,
                flag:'initDictStores'
            }
        ],
                initDisplay);
    });
    </script>
</head>
<body>
<table align="center" width="660" >
    <tr>
        <td>
            <div id="displayDiv"></div>
        </td>
    </tr>
</table>
</body>
</html>