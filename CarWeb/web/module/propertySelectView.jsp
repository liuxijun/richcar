<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"view","propertySelectView");
        needPermissions(actionHeader,"save","propertySelectSave");
        needPermissions(actionHeader,"search","propertySelectSearch");
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
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    String nowTime = com.fortune.util.StringUtils.date2string(new java.util.Date());
%>
Ext.onReady(function() {

    var propertyId = <%=request.getParameter("propertyId")%>;

    var action = '<%=action%>';
    var id = '<%=id%>';
    var tableWidth = 500;

    var actionUrl = "/module/propertySelect";

    var dataViewStore = new Ext.data.JsonStore({
        method:'POST',
        url: actionUrl + "!view.action"
    });

    function dataFresh() {
        dataViewStore.load({
            params:{keyId:id},
            callback :
                    function(records, options, success) {
                        viewForm.getForm().setValues(this.reader.jsonData.data);
                    }
        });
    }


    var columnWidth = 180;

    var viewForm = new Ext.FormPanel({
        style: {'margin-left':'55px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'viewForm',
        width:400,

        labelWidth: 60,
        frame:true,
        //layout:'column',
        layout:'table',
        layoutConfig: {columns:1},
        baseCls: 'x-plain',
        waitMsgTarget: true,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        fileUpload:true,

        items: [
            {
                width:350,
                baseCls: 'x-plain',
                items: {
                    baseCls: 'x-plain',
                    layout: 'form',
                    items: [
                        {
                            name:'obj.id',
                            xtype: 'hidden'
                        },
                        {
                            name:'obj.propertyId',
                            xtype: 'hidden',
                            value: propertyId
                        },
                        {
                            name:'obj.displayOrder',
                            xtype: 'hidden'
                        },

                        {
                            name:'obj.name',
                            xtype: 'textfield',
                            fieldLabel: '名称',
                            value: '',
                            width:columnWidth,
                            allowBlank:false
                        },
                        {
                            name:'obj.value',
                            xtype: 'textfield',
                            fieldLabel: '值',
                            value: '',
                            vtype:'number',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                            name:'obj.code',
                            xtype: 'textfield',
                            fieldLabel: 'code',
                            value: '',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                            hiddenName:'obj.status',
                            xtype: 'combo',
                            fieldLabel: '状态',
                            width:columnWidth,
                            //allowBlank:false,
                            triggerAction: 'all',
                            emptyText:'请选择...',
                            //originalValue:'',
                            store: new Ext.data.ArrayStore({
                                fields: ['value', 'display'],
                                data: [
                                    ['1', '有效'],
                                    ['0', '失效']
                                ]
                            }),
                            valueField: 'value',
                            displayField: 'display',
                            value:'1',
                            //mode: 'remote',
                            mode:'local',
                            loadingText:'加载中...',
                            selectOnFocus:true,
                            editable: false,
                            typeAheadDelay:1000,
                            //pageSize:5,
                            forceSection: true,
                            typeAhead: false,
                            allowBlank:false,
                            listeners:{
                                select: function(combo, record, index) {
                                    //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                }
                            }
                        }


                    ]}
            } ,

            {
                width:300,
                height:30,
                baseCls: 'x-btn-over',
                layout: 'table',
                layoutConfig: {columns:4},
                items: [
                    {
                        width:75,
                        baseCls: 'x-plain',
                        layout:'form',
                        items:[
                            {
                                xtype: 'label',
                                labelSeparator : '',
                                fieldLabel: '　'
                            }
                        ]
                    },
                    {
                        width:80,
                        baseCls: 'x-plain',
                        layout:'form',
                        items:[
                            {
                                text: '保　存',
                                xtype:'button',
                                minWidth:80,
                                listeners:{
                                    "click":function()
                                    {
                                        //todo 输入校验
                                        var msg = '';
                                        if (viewForm.getForm().findField('obj.name').getValue() == '') {
                                            msg += '名称必填<br>';
                                        }

                                        if (msg != '') {
                                            Ext.Msg.alert("提示", msg);
                                            return;
                                        }
                                        if (viewForm.getForm().isValid()) {
                                            viewForm.getForm().submit({
                                                params:{action:action},
                                                //baseParams:{action:action},
                                                url: actionUrl + '!save.action', //处理修改后台地址
                                                method: 'post',
                                                waitMsg: '正在处理数据，请稍后……',
                                                success: function (form, returnMsg) {
                                                    var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                                    Ext.Msg.alert("提示", "操作成功！", showResult);
                                                    function showResult() {
                                                        parent.viewWin.close();
                                                    }

                                                    action = 'view';
                                                    id = serverData.data[0].id;
                                                    dataFresh();
                                                    //dataForm.reset();           //表单中所有数据置空
                                                },
                                                failure: function (form, returnMsg) {
                                                    Ext.MessageBox.show({
                                                        title: '提示',
                                                        msg: '操作失败，原因是:' + returnMsg.result.error,
                                                        buttons: Ext.MessageBox.OK,
                                                        icon: Ext.MessageBox.ERROR
                                                    });
                                                },
                                                scope: this
                                            });
                                        }
                                    }
                                }
                            }
                        ]
                    } ,

                    {
                        width:80,
                        baseCls: 'x-plain',
                        layout:'form',
                        items:[
                            {
                                text:'重新加载',
                                xtype:'button',
                                minWidth:80,
                                handler:function() {
                                    dataFresh();
                                },
                                action:'view'
                            }
                        ]
                    }
                ]
            }

        ]
    })

    var viewPanel = new Ext.Panel({
        title:"模板管理",
        width:tableWidth - 95,
        style: {'margin-left':'5px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
        //height:tableHeight,
        items:[
            new Ext.form.FieldSet({
                title: '',
                border:false,
                autoHeight: true,
                //autoWidth: true,
                width:tableWidth-80,
                style: {'margin-left':'0px','margin-top': '10px','margin-right':'0px','margin-bottom':'0px'},

                items: [
                    viewForm
                ]
            })
        ]
    })

    viewPanel.render('display');

    if (action == 'view') {
        dataFresh();
    }

})

</script>
</head>
<body>
<table align="center" width="580">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>