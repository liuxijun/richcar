<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title></title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <style type= "text/css" >
        .x-selectable, .x-selectable * {
            -moz-user-select: text! important ;
            -khtml-user-select: text! important ;
        }
    </style>
    <script type="text/javascript">
        <%
            long spId = 0;
            if (admin != null && admin.getCspId()!=null){
                spId = admin.getCspId();
            }
        %>
        var spId =<%=spId%>;
      /*  var spId =11054540;*/

        var pageSize = 20;

         Ext.onReady(function() {
             var channelStore = new Ext.data.JsonStore({
                 method:'POST',
                 remoteSort:true,
                 url:"/publish/channel!searchAll.action?obj.type=1&obj.cspId=" + spId,
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
             channelStore.setDefaultSort("id", "ASC");
             channelStore.load({params:{start:0, limit:1000000}});


              var viewForm = new Ext.FormPanel({
                  style:{'margin-left':'15px','margin-top':'0px','margin-right':'0px','margin-bottom':'0px'},
                  id:'viewFrom',
                  title:'媒体名称录入',
                  width:'500px',
                  frame:true,
                  layout:'form',
                  waitMsgTarget:true,
                  loadMask:{msg:'正在加载数据，请稍候……'},
                  fileUpload:true,
                  items:[
                      {
                          hiddenName:'channelId',
                          xtype:'combo',
                          fieldLabel:'媒体频道',
                          width:200,
                          //allowBlank:false,
                          triggerAction:'all',
                          emptyText:'请选择...',
                          //originalValue:'',
                          store:channelStore,
                          valueField:'id',
                          displayField:'name',
                          //value:'1',
                          //mode: 'remote',
                          mode:'local',
                          loadingText:'加载中...',
                          selectOnFocus:true,
                          editable:false,
                          typeAheadDelay:1000,
                          //pageSize:5,
                          forceSection:true,
                          typeAhead:false,
                          //allowBlank:false,
                          listeners:{
                              select:function (combo, record, index) {
                                  //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                              }
                          }
                      },
                      {
                          fieldLabel:'媒体名称',
                          xtype: 'textarea',
                          name:'obj.name',
                          width:300,
                          height:'145'
                      },
                      {
                          text: '保　存',
                          xtype:'button',
                          style : 'margin-left:325px',
                          minWidth:80,
                          listeners:{
                              "click":function()
                              {
                                  var msg = '';
                                  if (viewForm.getForm().findField('channelId').getValue()==''){
                                      msg += '请选择媒体平道！<br>';
                                  }
                                  if (viewForm.getForm().findField('obj.name').getValue()==''){
                                      msg += '请填写媒体名称！<br>';
                                  }
                                  if (msg!=''){
                                      var message =  Ext.MessageBox.show({
                                          title:'提示',
                                          msg: msg,
                                          modal:false,
                                          buttons: Ext.MessageBox.OK,
                                          icon: Ext.MessageBox.ERROR
                                      });
                                      return;
                                  }
                                  viewForm.getForm().submit({
                                      url: 'content!inputContentName.action?obj.cspId='+spId,
                                      method: 'post',
                                      waitMsg: '正在处理数据，请稍后……',
                                      success: function (form, returnMsg) {
                                          //msg中存放的是新插入的媒体ID，即data为新媒体ID拼接成的字符窜
                                         var data = Ext.util.JSON.decode(returnMsg.response.responseText).msg;
                                        /*  var message = Ext.MessageBox.show({
                                              title:'提示',
                                              msg:"录入成功！",
                                              model:false,
                                              buttons: Ext.MessageBox.OK,
                                              icon: Ext.MessageBox.INFO
                                          });*/
                                          //dataForm.reset();           //表单中所有数据置空
                                          document.getElementById("parameterValue").innerHTML = data;
                                      },
                                      failure: function (form, returnMsg) {
                                          var message = Ext.MessageBox.show({
                                              title: '提示',
                                              msg: '操作失败，原因是:' + returnMsg.result.error,
                                              modal:false,
                                              buttons: Ext.MessageBox.OK,
                                              icon: Ext.MessageBox.ERROR
                                          });
                                      },
                                      scope: this
                                  });
                              }
                          }
                      },
                      {
                          html:'<table align="center" width="760">'+
                                  '<tr><td>媒体链接参数：</td></tr><tr><td colspan="2" id="parameterValue"></tr>'
                      }
                  ]
              });


             function addStoreBaseParam(cmpId,store){
                 var cmpVal = getCmpValue(cmpId);
                 if(cmpVal!=null){
                     if(store!=null){
                         store.baseParams[cmpId]=cmpVal;
                     }
                 }
             }

             function loadDate() {
                 addStoreBaseParam('obj.name',dataListStore);
                 addStoreBaseParam('searchChannelId',dataListStore);
                 addStoreBaseParam('contentCspStatus',dataListStore);
                 dataListStore.load({params:{start:0, limit:pageSize}});
             }

             var dataListStore = new Ext.data.JsonStore({
                 method:'POST',
                 remoteSort: true,
                 url:"content!searchContentParameter.action?obj.cspId="+spId,
                 totalProperty:"totalCount",
                 root:'objs',
                 fields:[
                     {name:'c_DOT_name'},
                     {name:'parameter'},
                     {name:'cc_DOT_channelId'}
                 ]
             });
             dataListStore.setDefaultSort('cc.channelId',"desc");

             /*var dataListStore = new Ext.data.JsonStore({
                 data:contentIds,
                 fields:['name','parameter']
             });*/

             var listGrid = new Ext.grid.GridPanel({
                 style:{'margin-left':'15px','margin-top':'0px','margin-right':'0px','margin-bottom':'0px'},
                 title:"媒体链接查询",
                 width:'500px',
                 height:400,
                 store: dataListStore,
                 loadMask:{msg:'正在加载数据，请稍侯……'},
                 iconCls:'icon-grid',
                 //viewConfig: { forceFit: true },
                 columns: [
                     {header: "媒体名称", dataIndex: 'c_DOT_name', width: 180, sortable: true, align:'left' },
                     {header: "媒体频道", dataIndex: 'cc_DOT_channelId', width: 60, sortable: true, align:'left' },
                     {header: "链接参数", dataIndex: 'parameter', width: 280, sortable: false, align:'left' }
                 ],
                 tbar:new Ext.Toolbar({
                     items : [
                         {
                             text:'媒体名称'
                         },
                         {
                             xtype:'textfield',
                             id:'obj.name',
                             name:'obj.name',
                             width:110
                         },
                         {
                             text:'频道'
                         },
                         {
                             id:'searchChannelId',
                             name:'channelId',
                             xtype: 'combo',
                             width:130,
                             store: channelStore,
                             valueField:'id',
                             displayField:'name',
                             mode:'local',
                             triggerAction: 'all',
                             loadingText:'加载中...',
                             emptyText:'请选择...',
                             selectOnFocus:true,
                             editable: false,
                             typeAheadDelay:1000,
                             //pageSize:5,
                             forceSection: true,
                             typeAhead: false,
                             listeners:{

                             }
                         },
                         {
                             text:'状态'
                         },{
                             id:'contentCspStatus',
                             name:'status',
                             xtype: 'combo',
                             width:80,
                             fieldLabel: '发布状态',
                             store: new Ext.data.ArrayStore({
                                 fields: ['value', 'display'],
                                 data: [['', '全部'],['2', '上线'] , ['1', '下线']]
                             }),
                             valueField: 'value',
                             displayField: 'display',
                             mode:'local',
                             triggerAction: 'all',
                             loadingText:'加载中...',
                             emptyText:'请选择...',
                             selectOnFocus:true,
                             editable: false,
                             typeAheadDelay:1000,
                             //pageSize:5,
                             forceSection: true,
                             typeAhead: false,
                             listeners:{
                                 select: function(combo, record, index) {
                                     //alert(index +":"+ record.get("deviceId") +":"+ record.get("name"));
                                 }
                             }

                         },
                         {
                             text:'搜索',
                             handler:function() {
                                loadDate();
                             }
                         }
                     ]
                 }),
                 bbar:new Ext.PagingToolbar({
                     pageSize: pageSize,
                     store: dataListStore,
                     displayInfo: true,
                     displayMsg: '结果数据 {0} - {1} of {2}',
                     emptyMsg: "没有数据"
                 })
             });

             /*var indexForm = new Ext.Panel({
                 style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                 id:'indexForm',
                 width:550,
                 height:610,
                 frame:true,
                 layout:'table',
                 layoutConfig:{columns:1},
                 //baseCls: 'x-plain',
                 waitMsgTarget: true,
                 loadMask:{msg:'正在加载数据，请稍侯……'},
                 fileUpload:true,
                 items:[
                     {
                         items:[
                             viewForm
                         ]
                     }
                 ]
             });*/

             viewForm.render('display');
             listGrid.render('display1')
         })
    </script>
<script type="text/javascript">
    if (!Ext.grid.GridView.prototype.templates) {
        Ext.grid.GridView.prototype.templates = {};
    }
    Ext.grid.GridView.prototype.templates.cell = new Ext.Template(
            '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,
            '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,
            '</td>'
    );
</script>
</head>
<body>
<table align="center" width="760">
    <tr>
        <td colspan="2">
            <div id="display"></div>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <div id="display1"></div>
        </td>
    </tr>
</table>
</body>
</html>