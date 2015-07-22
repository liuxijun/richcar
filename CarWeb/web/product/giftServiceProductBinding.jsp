<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"searchServiceProduct","searchServiceProduct");
        needPermissions(actionHeader,"searchServiceProductGift","searchServiceProductGift");
        needPermissions(actionHeader,"saveServiceProductGift","saveServiceProductGift");
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="../resources/css/ext-all.css" />
<script type="text/javascript" src="../ext/ext-base.js"></script>
<script type="text/javascript" src="../ext/ext-all.js"></script>
<script type="text/javascript" src="../ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="../ext/ux/ux-all.js"></script>
<script language="javascript">
 var keyId = <%=request.getParameter("keyId")%>;

 function confirmRadio(id){
    var checkBoxObj= document.getElementById("c"+id);
    if(checkBoxObj.checked){
       document.getElementById("m"+id).checked=true;
    }else{
       document.getElementById("m"+id).checked=false;
    }
 }
 function confirmCheckBox(id){
     var checkBoxObj= document.getElementById("c"+id);
     if(checkBoxObj.checked){

     }else{
         document.getElementById("m"+id).checked=false;
     }
 }

 function setCheckButton(serviceProductGiftString){
      for(var i=0;i<serviceProductGiftString.length;i++){

            var serviceProductGift=serviceProductGiftString[i];
            var giftServiceProductId=serviceProductGift.giftServiceProductId;

            var serviceProductId= serviceProductGift.serviceProductId;

            var startTime = serviceProductGift.startTime;

            var endTime = serviceProductGift.endTime;

            var obj =  document.getElementById("c"+giftServiceProductId);
            if(obj!=null){
                 obj.checked=true;
            }

//            if(isDefault==1){
//                document.getElementById("m"+moduleId).checked=true;
//            }

      }

 }
Ext.onReady(function(){

    var keyId = <%=request.getParameter("keyId")%>;
   // alert(keyId);
    var pageSize = 12;
    var tableWidth = 650;
    var tableHeight = 410;

    var actionUrl = "/product/serviceProduct";


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
     var serviceProductGiftStore = new Ext.data.JsonStore({

                            method:'POST',
                            url: "/product/serviceProductGift!searchServiceProductGift.action",
                            root:'serviceProductGift'
                        });
    var dataListStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url: actionUrl + "!searchServiceProduct.action?keyId="+keyId+"",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {name:'id'},
            {name:'name'},
            {name:'selectStatus'},
            {name:'startTime'},
            {name:'endTime'}
        ],
        listeners:{
             beforeload:function(){
                   serviceProductGiftStore.load({
                    params:{keyId:keyId }

                });
             },
            load:function(){
                  serviceProductGiftStore.load({
                    params:{keyId:keyId},
                    callback:

                            function(records,options,success){
                              var serviceProductGiftData = serviceProductGiftStore.reader.jsonData;
                              if(serviceProductGiftData.totalCount!=0){
                              serviceProductGiftString=serviceProductGiftData.serviceProductGifts;
                              setCheckButton(serviceProductGiftString);
                              }
                            }

                });
            }
  }

    });
   // dataListStore.setDefaultSort('id','asc');

//serviceProductGiftStore.load();
//dataListStore.load({ params:{start:0, limit:pageSize}})
   serviceProductGiftStore.load({
        params:{keyId:keyId},
        callback:
                function(records,options,success){
                     dataListStore.load({
                    params:{start:0, limit:pageSize},
                    callback:
                            function(records,options,success){
                              var serviceProductGiftData = serviceProductGiftStore.reader.jsonData;
                              if(serviceProductGiftData.totalCount!=0){
                              serviceProductGiftString=serviceProductGiftData.serviceProductGifts;
                              setCheckButton(serviceProductGiftString);
                              }
                            }
                     });
                }
    });
//    dataListStore.load({
//        params:{start:0, limit:pageSize},
//        callback :
//            function(records,options,success){
//                serviceProductGiftStore.reload({
//                    params:{keyId:keyId},
//                    callback :
//                            function(records,options,success){
//                                    var serviceProductGiftData = this.reader.jsonData;
//                                    if(serviceProductGiftData.totalCount!=0){
//                                       serviceProductGiftString=serviceProductGiftData.serviceProductGifts;
                                      // setCheckButton(serviceProductGiftString);
//                                        for (var i=0;i<dataListStore.getCount();i++){
//                                            var record = dataListStore.getAt(i);
//                                            for (var j=0;j<serviceProductGiftString.length;j++){
//                                                var record1= serviceProductGiftString[j];
//                                                if (record1.serviceProductId==record.data.id){
//                                                    record.data.startTime=record1.startTime;
//                                                    record.data.endTime=record1.endTime;
//                                                }
//                                            }
//                                        }
//                                    }
//                                    dataListStore.reload();
//                                        if (returnData.success){
//                                            //刷新列表
//                                           // loadData();
//                                            Ext.MessageBox.alert('提示','操作成功');
//                                        } else {
//                                            Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
//                                        }

//                            }
//                });

//            }
//    });


     function formatDate(value){
        return value ? value.dateFormat('Y-m-d') : '';
    }


    var columnWidth = 180;
    var checkSelect = new Ext.grid.CheckboxSelectionModel ({
        singleSelect : false,
        checkOnly: true  //true点击行不选中, false为点击行也选中checkbox

    });
    var listGrid = new Ext.grid.EditorGridPanel({
        title:"赠送服务产品添加",
        width:tableWidth,
        height:tableHeight+40,
        store: dataListStore,

        clicksToEdit:1,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        iconCls:'icon-grid',
        viewConfig: { forceFit: true },

        sm:checkSelect,
        columns: [

            {header: "ID", dataIndex: 'id',width: 30,hidden:true, sortable: true, align:'left',
                renderer:
                          function(val,p,row){
                              
                               return '<input name="id" style= "border:0px " readonly= "true " value="'+row.data.id+'" type="input" />';
                          }
            },
            {header: "服务产品名称", dataIndex: 'name', width: 150, sortable: true, align:'left'},
            {header: "选中状态", dataIndex: 'selectStatus', width: 90, sortable: true, align:'center',
                renderer:
                        function (val,p,row){
                             return '<input name="c'+row.data.id+'" id="c'+row.data.id+'" type="checkbox" value="1"/>';
                        }
            },
            {header: "开始时间", dataIndex: 'startTime', width: 70, sortable: true, align:'center',
                editor: new Ext.form.DateField({
                        format : 'Y-m-d',
                        xtype: 'textfield'
 	                }),
                renderer:
                    function(val,p,row){

                    if(!row.dirty){
                    var serviceProductGiftStoreString = serviceProductGiftStore.reader.jsonData;
                        var serviceProductGifts = serviceProductGiftStoreString.serviceProductGifts;
                              for(var i=0;i<serviceProductGiftStoreString.totalCount;i++){
                                  if(row.data.id==serviceProductGifts[i].giftServiceProductId){
                                      row.data.startTime=serviceProductGifts[i].startTime.split(" ")[0];
                                      
                                      return serviceProductGifts[i].startTime.split(" ")[0];
                                      break;
                                  }

                              }
                     }else{
                        return val.dateFormat('Y-m-d');
                    }
            }
            },
            {header: "结束时间", dataIndex: 'endTime', width: 70, sortable: true, align:'center',
                editor: new Ext.form.DateField({
                      format : 'Y-m-d',
                      xtype: 'textfield'
 	                }),
                renderer: function(val,p,row){
                      if(!row.dirty){
                    var serviceProductGiftStoreString = serviceProductGiftStore.reader.jsonData;
                        var serviceProductGifts = serviceProductGiftStoreString.serviceProductGifts;
                              for(var i=0;i<serviceProductGiftStoreString.totalCount;i++){
                                  if(row.data.id==serviceProductGifts[i].giftServiceProductId){
                                      row.data.endTime=serviceProductGifts[i].endTime.split(" ")[0];
                                      return serviceProductGifts[i].endTime.split(" ")[0];
                                      break;
                                  }

                              }
                      }else{
                              return val.dateFormat("Y-m-d");
                      }
            }
            },
//            {header: "默认模板", dataIndex: 'isDefault', width: 90, sortable: true, align:'left',
//                renderer:
//                        function (val,p,row){
//                             return '<input name="isDefault"  type="radio" id="m'+row.data.id+'" onchange="confirmRadio('+row.data.id+');" value="'+row.data.id+'"/>';
//                        }
//            },
            {header: "审核人确认", dataIndex: 'id', width: 90,hidden:true, sortable: true, align:'left',
                renderer:
                        function (val,p,row){

                             return '<a href=\'javascript:test1('+row.data.id+');\'>修改</a>';
                        }
            }
        ],

        tbar:new Ext.Toolbar({items:[
            {text:'搜索: '}, {text:'服务产品名称'},
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

                {text:'提交数据',handler:function(row){

                    var serviceProductGiftString ="";
                    Ext.MessageBox.confirm("请您确认操作", " 更新记录 ",function(btn){
                            if(btn=="yes"){

                            for (var i=0; i<listGrid.getStore().getCount(); i++){
                               var storeRecord = listGrid.getStore().getAt(i);
                                var keyId1 = storeRecord.data.id;
                                if(serviceProductGiftString==""){
                                    serviceProductGiftString = keyId1;
                                }else{
                                    serviceProductGiftString +=keyId1;
                                }
                                var selected = document.getElementById("c"+keyId1);
                                if(selected.checked){
                                     if(storeRecord.data.startTime==""||storeRecord.data.endTime==""){
                                          alert("请为选中的服务设置有效期");
                                          return;
                                     }else{
                                         var startTime;
                                         var endTime;
                                         if(storeRecord.data.startTime.length!=10||storeRecord.data.endTime.length!=10){
                                              startTime=formatDate(storeRecord.data.startTime);
                                              endTime=formatDate(storeRecord.data.endTime);
                                         }else{
                                             startTime=storeRecord.data.startTime;
                                             endTime=storeRecord.data.endTime ;

                                         }
                                         serviceProductGiftString +="_"+startTime+"_"+endTime;
                                     }

                                }
                                serviceProductGiftString+=",";

                            }
                                serviceProductGiftString = serviceProductGiftString.substr(0,serviceProductGiftString.length-1);
                            
                                var remoteRequestStore = new Ext.data.JsonStore({
                                    method:'POST',
                                    url: "/product/serviceProductGift!saveServiceProductGift.action"
                                });
                                remoteRequestStore.reload({
                                params:{serviceProductGiftString:serviceProductGiftString,keyId:keyId},
                                callback :
                                        function(records,options,success){
                                            var returnData = this.reader.jsonData;
                                            if (returnData.success){
//                                             for (var i=0; i<listGrid.getStore().getCount(); i++){
//                                                 var storeRecord = listGrid.getStore().getAt(i);
//                                                 if (storeRecord.dirty)
//                                                      storeRecord.commit();
//                                             }
                                                //刷新列表
                                                //loadData();
                                                Ext.MessageBox.alert('提示','操作成功');
                                            } else {
                                                Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+returnData.error);
                                            }

                                        }
                                });
                            }
                        });
                }},

                {text:'返回',handler:function(){
                    document.location='systemServiceProductList.jsp';
                }}
            ]
        })
    });


    listGrid.render('display');
    //listGrid.render(Ext.getBody());

    //这样定义函数, 就可以在extjs外调用了
    this.viewObj = function (action,id){
        var viewHtml =  '<iframe  frameborder="0" width="100%" height="100%" src="propertyView.jsp?action='+ action +'&id='+ id +'&moduleId='+ moduleId +'"></iframe>';
        var viewWin = new Ext.Window({
            //title:"文件列表",
            //x:0,
            y:20,
            width:tableWidth-50,
            height:tableHeight-50,
            closeAction:"hide",
            closable:true,
            bodyStyle:"padding:0px",
            plain:true,
            //layout:'fit',
            //collapsible:true,
            //plain: false,
            //resizable: true,

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
<body onload="">
<table align="center" width="660">
    <tr>
        <td>
            <div id="display"></div>
        </td>
    </tr>
</table>
</body>
</html>