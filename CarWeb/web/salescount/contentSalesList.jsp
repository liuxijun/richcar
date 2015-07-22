<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "product";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"searchContentSalesCount","userBuySearchContentSalesCount");
       // needPermissions(actionHeader,"searchAllSp","spList");
       // needPermissions(actionHeader,"searchAllCp","cspSearchAllCp");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
  <head>
      <meta http-equiv=content-type content="text/html; charset=UTF-8">
      <title>资源销售统计</title>
      <%@include file="../inc/jsCssLib.jsp"%>
      <%@include file="../inc/extBase.jsp"%>
      <script type="text/javascript">
          nextUrl = "productSalesList.jsp";
          actionHeader = "../product/userBuy";
          checkAllFunctions();  // 检查用户权限
         defaultPageSize=16;


          var spStore = new Ext.data.JsonStore({
             method:'POST',
             url:"/csp/csp!searchAllSp.action",
             root:'objs',
             fields:[
                 {name:'id'},
                 {name:'name'}
             ]
         });
        spStore.load();
        var cpStore = new Ext.data.JsonStore({
             method:'POST',
             url:'/csp/csp!searchAllCp.action',
             root:'objs',
             fields:[
                 {name:'id'},
                 {name:'name'}
             ]
        });
        cpStore.load();

          function initDisplay(){
              storeConfig.fields = ['contentId','contentName','salesAmount','buyNum','cpId','cpName','spId','spName'];
              storeConfig.proxy = new Ext.data.HttpProxy({method:'POST',url:getActionUrl('searchContentSalesCount')});
              searchStore = new FortuneSearchStore(storeConfig);
              keyFieldId = "id";

              var columns = new Ext.grid.ColumnModel([
                  {header:'SP名称',align:'center',width:160,sortable:true,dataIndex:'spName'},
                  {header:'CP名称',align:'center',width:125,sortable:true,dataIndex:'cpName'},
                  {header:'内容',align:'center',width:100,sortable:true,dataIndex:'contentName'},
                  {header:'销售金额',align:'center',width:90,sortable:true,dataIndex:'salesAmount'},
                  {header:'购买次数',align:'center',width:90,sortable:true,dataIndex:'buyNum'}

              ]);

                 //刷新列表
                 this.loadData = function(){
                        var form1 = searchForm.getForm();
                        searchStore.removeAll();
                        searchStore.baseParams = form1.getValues();
                        searchStore.reload({params: {start:0,limit:12}});
                        spSalesGrid.getBottomToolbar().updateInfo();
                    }
              var searchForm =  new Ext.FormPanel({
                    style: {'margin-left':'10px','margin-top': '5px','margin-right':'0px','margin-bottom':'0px'},
                    id:'searchForm',
                    width: '100%',
                    height : '100%',
                    labelWidth: 50,
                    frame:true,
                    layout:'table',
                    layoutConfig: {columns:2},
                    baseCls: 'x-plain',

                    items: [
                        new Ext.form.FieldSet({
                            title:'搜索栏',
                            width:540,
                            height:80,
                            items:[
                        {
                            baseCls: 'x-plain',
                            layout:'table',
                            layoutConfig: {columns:4},
                            items: [
                  /*              {
                            baseCls: 'x-plain',
                            layout: 'form',
                            items: [
                                {
                                    xtype: 'label',
                                    labelSeparator : '',
                                    fieldLabel: '搜索栏:'
                                }
                            ]
                        }
                        , */  {
                                    baseCls: 'x-plain',
                                    layout: 'form',
                                    labelWidth: 60,
                                    items: [
                                        {
                                            hiddenName:'userBuy.spId',
                                            xtype: 'combo',
                                            labelWidth: 90,
                                            fieldLabel: '　　　SP',
                                            width:140,
                                            //allowBlank:false,
                                            triggerAction: 'all',
                                            emptyText:'请选择...',
                                            //originalValue:'',
                                            store:spStore,
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
                                            typeAhead: false

                                        }]
                                },
                                     {
                                    baseCls: 'x-plain',
                                    layout: 'form',
                                    labelWidth: 75,
                                    items: [
                                        {
                                            name:'userBuy.startTime',
                                            xtype: 'datefield',
                                            format:'Y-m-d',
                                            labelWidth: 60,
                                            fieldLabel: '　　开始时间',
                                            width:130
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
                                                                }          ,    {
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
                                    labelWidth: 60,
                                    items: [
                                        {
                                            hiddenName:'userBuy.cpId',
                                            xtype: 'combo',
                                            labelWidth: 90,
                                            fieldLabel: '　　　CP',
                                            width:140,
                                            //allowBlank:false,
                                            triggerAction: 'all',
                                            emptyText:'请选择...',
                                            //originalValue:'',
                                            store:cpStore,
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
                                            typeAhead: false

                                        }]
                                },




                                {
                                    baseCls: 'x-plain',
                                    layout: 'form',
                                    labelWidth: 75,
                                    items: [
                                        {
                                            name:'userBuy.endTime',
                                            xtype: 'datefield',
                                            format:'Y-m-d',
                                            labelWidth: 60,
                                            fieldLabel: '　　结束时间',
                                            width:130
                                        }]
                                },

                                {
                                    baseCls: 'x-btn-over',
                                    layout:'form',
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



              var spSalesGrid =  new FortuneSearchListGrid({
                  title:'资源销售统计',
                  cm:columns,
                  height:500,
                  width:600,
                  store:searchStore,
                  tbar:new Ext.Toolbar({items:[
                              new Ext.Panel({
                            style: {'margin-left':'0px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
                            id:'searchForm',
                            width: '100%',
                            height:'100%',
                            labelWidth: 100,
                            frame:true,
                            layout:'table',
                            layoutConfig: {columns:1},
                            baseCls: 'x-plain',

                            items: [
                                    searchForm
                            ]
                        })
                  ]}),
                  bbar:new Ext.PagingToolbar({
                      pageSize:defaultPageSize,
                      store:searchStore,
                      displayInfo:true,
                      displayMsg:'结果数据 {0} - {1} of {2}',
                      emptyMsg:'没有数据'
                  })
              });

              spSalesGrid.render(displayDiv);
              defaultGrid = spSalesGrid;
              searchStore.load({
                  params:{start:0,limit:defaultPageSize}
              });
          }

          Ext.onReady(function(){
              Ext.QuickTips.init();
              initDisplay();
          });

      </script>
  </head>
  <body>
    <table align="center" width="660">
        <tr>
            <td>
                <div id="displayDiv"></div>
            </td>
        </tr>
    </table>
 </body>
</html>