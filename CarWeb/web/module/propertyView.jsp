<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
       // needPermissions(actionHeader,"search","propertyManage,propertySearch");
        needPermissions(actionHeader,"view","propertyManage,propertyView");
        needPermissions(actionHeader,"save","propertyManage,propertySave");
    }
%><%@include file="../inc/checkHeader.jsp"%>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
<script language="javascript">
<%
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    String nowTime = com.fortune.util.StringUtils.date2string(new java.util.Date());
%>
Ext.onReady(function(){

    var moduleId = <%=request.getParameter("moduleId")%>;

    var action = '<%=action%>';
    var id = '<%=id%>';
    var tableWidth = 540;


    var actionUrl = "/module/property";

    var dataViewStore = new Ext.data.JsonStore({
        method:'POST',
        url: actionUrl + "!view.action"
    });

    function dataFresh(){
        dataViewStore.load({
            params:{keyId:id},
            callback :
                    function(records,options,success){
                        viewForm.getForm().setValues( this.reader.jsonData.data );
                    }
        });
    }



    var columnWidth = 180;

    var viewForm = new Ext.FormPanel({
        style: {'margin-left':'75px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'viewForm',
        width: '80%',

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
                width:380,
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
                            name:'obj.moduleId',
                            xtype: 'hidden',
                            value: moduleId
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
                            name:'obj.code',
                            xtype: 'textfield',
                            fieldLabel: 'code',
                            value: '',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                            hiddenName:'obj.dataType',
                            xtype: 'combo',
                            fieldLabel: '数据类型',
                            width:columnWidth,
                            //allowBlank:false,
                            triggerAction: 'all',
                            emptyText:'请选择...',
                            //originalValue:'',
                            store: new Ext.data.ArrayStore({
                                        fields: ['value', 'display'],
                                        data: [
                                            ['1', '文本_单行'],
                                            ['2', '文本_多行'],
                                            ['3', '文本_数字'],
                                            ['4', '文本_日期'],
                                            ['5', '选择项_下拉框'],
                                            ['6', '选择项_单选框'],
                                            ['7', '选择项_复选框'],
                                            ['8', '文件_WMV'],
                                            ['9', '文件_FLV'],
                                            ['10', '文件_MP4'],
                                            ['11', '文件_图片'],
                                            ['12', '文件_网页'],
                                            ['13', '文件_ZIP文件']
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
                        },

                     {
                         name:'obj.isMultiLine',
                         xtype:'radiogroup',
                         fieldLabel:'是否多行',
                         width:columnWidth,
                         items:[
                             {boxLabel:'单行',name:'obj.isMultiLine',inputValue:"0",checked: true },
                             {boxLabel:'多行',name:'obj.isMultiLine',inputValue:"1"}

                         ]
                     },
                     {
                         name:'obj.isMerge',
                         xtype:'radiogroup',
                         fieldLabel:'合并保存',
                         width:columnWidth,
                         items:[
                             {boxLabel:'是',name:'obj.isMerge',inputValue:"1"},
                             {boxLabel:'否',name:'obj.isMerge',inputValue:"0",checked: true }

                         ]
                     },

                        {
                            name:'obj.maxSize',
                            xtype: 'textfield',
                            fieldLabel: '字数限制',
                            value: '',
                            vtype: 'number',
                            width:columnWidth,
                            allowBlank:true
                        },

                        {
                            name:'obj.isNull',
                            xtype:'radiogroup',
                            fieldLabel:'可空',
                            width:columnWidth,                            
                            items:[
                                {boxLabel:'是',name:'obj.isNull',inputValue:"1",checked: true },
                                {boxLabel:'否',name:'obj.isNull',inputValue:"0"}

                            ]
                        },
                        {
                            name:'obj.isMain',
                            xtype:'radiogroup',
                            fieldLabel:'保存主表',
                            width:columnWidth,
                            items:[
                                {boxLabel:'是',name:'obj.isMain',inputValue:"1",checked: true },
                                {boxLabel:'否',name:'obj.isMain',inputValue:"0"}
                            ]
                        },

                        {
                            name:'obj.columnName',
                            xtype: 'textfield',
                            fieldLabel: '主表列名',
                            value: '',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                            name:'obj.colSpan',
                            xtype: 'textfield',
                            fieldLabel: '所占列数',
                            value: '1',
                            vtype:'number',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                            name:'obj.rowSpan',
                            xtype: 'textfield',
                            fieldLabel: '所占行数',
                            vtype:'number',
                            value: '1',
                            width:columnWidth,
                            allowBlank:true
                        },
                        {
                         name:'obj.relatedTable',
                         xtype:'radiogroup',
                         fieldLabel:'外表相关',
                         width:columnWidth,
                         items:[
                             {boxLabel:'否',name:'obj.relatedTable',inputValue:"0",checked: true },
                             {boxLabel:'设备',name:'obj.relatedTable',inputValue:"1" },
                             {boxLabel:'栏目',name:'obj.relatedTable',inputValue:"2" },
                             {boxLabel:'字典',name:'obj.relatedTable',inputValue:"3" }
                         ]
                     },
                        {
                            name:'obj.status',
                            xtype:'radiogroup',
                            fieldLabel:'状态',
                            width:columnWidth,                       
                            items:[
                                {boxLabel:'有效',name:'obj.status',inputValue:"1",checked: true },
                                {boxLabel:'失效',name:'obj.status',inputValue:"99"}
                            ]
                        },

                        {
                            name:'obj.desp',
                            fieldLabel: '简　　介',
                            xtype:'textarea',
                            minWidth:145,
                            width:250,
                            height:38,
                            listeners:{
                                "click":function()
                                {
                                }
                            }
                        }


                    ]}
            } ,

            {
                width:460,
                height:30,
                baseCls: 'x-btn-over',
                layout: 'table',
                layoutConfig: {columns:4},
                items: [
                    {
                        width:100,
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
                        width:100,
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
                                        if (viewForm.getForm().findField('obj.name').getValue()==''){
                                            msg += '名称必填<br>';
                                        }

                                        if (msg!=''){
                                            Ext.Msg.alert("提示",msg);
                                            return;
                                        }
                                        if(viewForm.getForm().isValid()){
                                            viewForm.getForm().submit({
                                                params:{action:action},
                                                //baseParams:{action:action},
                                                url: actionUrl + '!save.action', //处理修改后台地址
                                                method: 'post',
                                                waitMsg: '正在处理数据，请稍后……',
                                                success: function (form,returnMsg){
                                                    var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                                     Ext.Msg.alert("提示","操作成功！",showResult);
                                                    function showResult(){
                                                              parent.viewWin.hide();
                                                    }
                                                    action = 'view';
                                                    id = serverData.data['obj.id'] ;
                                                    dataFresh();
                                                    //dataForm.reset();           //表单中所有数据置空
                                                },
                                                failure: function (form,returnMsg){
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
                        width:100,
                        baseCls: 'x-plain',
                        layout:'form',
                        items:[
                            {
                                text:'重新加载',
                                xtype:'button',
                                minWidth:80,
                                handler:function(){
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

    var  viewPanel = new Ext.Panel({
        title:"属性管理",
        width:tableWidth-30,
        style: {'margin-left':'5px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        //height:tableHeight,
        items:[
            new Ext.form.FieldSet({
                title: '',
                border:false,
                autoHeight: true,
                //autoWidth: true,
                width:tableWidth-30,
                style: {'margin-left':'12px','margin-top': '10px','margin-right':'12px','margin-bottom':'12px'},

                items: [
                    viewForm
                ]
            })
        ]
    })

    viewPanel.render('display');

            if (action=='view'){
                dataFresh();
            }

})

</script>
</head>
<body>
<table align="center" width="560">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>