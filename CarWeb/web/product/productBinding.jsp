<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "product";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"searchProductBySpId","cspProductList,cspManage");
      //  needPermissions(actionHeader,"list","cspManage,cspSearch");
        needPermissions(actionHeader,"saveProductToCsp","cspSaveProductToCsp,cspManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="device"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
   <script type="text/javascript">
        actionHeader = "/csp/csp";
        nextUrl = "/csp/cspList.jsp";
        function loadFormFromServerData(){
            Ext.Ajax.request({
                
               url:getActionUrl('searchProductBySpId',"keyId="+keyId),
                callback : function(opt, success, response) {
                    if(success){
                        var serverData = Ext.util.JSON.decode(response.responseText);
                        if(serverData.success){
                            initData(serverData);
                        }else{
                            Ext.MessageBox.alert('发生异常','服务器返回错误信息：'+serverData.error)
                        }
                    }
                }
            });
        }
        function initData(serverData){
            var productStoreData = [];
            var selectedStoreData = [];
            var products=serverData.data['obj.products'];
            if(products!=null){
                for(var i=0;i<products.length;i++){
                    var product = products[i];
                    var item=[product.id,product.name];
                    if(product.selected){
                       selectedStoreData.push(item);
                    }else{
                       productStoreData.push(item);
                    }
                }
            }
            var productStore = new Ext.data.ArrayStore({
                data:productStoreData,
                fields: ['value','text'],
                sortInfo: {
                    field: 'text',
                    direction: 'ASC'
                }
            });
            var selectedStore = new Ext.data.ArrayStore({
                data:selectedStoreData,
                fields: ['value','text'],
                sortInfo: {
                    field: 'text',
                    direction: 'ASC'
                }
            });
            var cspViewForm = new FortuneViewForm({
                title:'SP绑定产品',
                url:'/csp/csp!saveProductToCsp.action',
                saveUrl:'/csp/csp!saveProductToCsp.action',  //'../ext-test/saveData.jsp',//
                viewUrl:'/csp/csp!view.action',
                bodyStyle:'padding:10px 10px 0',
                align:'center',
                layout:'fit',
                width:350,
                height:362,
                items:[
                    {
                        xtype:'fieldset',
                        collapsible: true,
                        title: 'CSP基本信息',
                        autoHeight:true,
                        labelWidth:100,
                        defaults: {width: 150},
                        defaultType: 'textfield',
                        items:[
                            {
                                name:'obj.id',
                                inputType:'hidden',
                                value:serverData.data["obj.id"]
                            }                     ,
                            {
                                fieldLabel:'<fts:text name="SP名称"/>',
                                name:'obj.name',
                                allowBlank:false,
                                value:serverData.data["obj.name"],
                                readOnly:"true"
                            }
                            <%--{  ,--%>    
                                <%--fieldLabel:'<fts:text name="role.memo"/>',--%>
                                <%--name:'obj.memo',--%>
                                <%--allowBlank:true,--%>
                                <%--value:serverData.data["obj.memo"],--%>
                                <%--xtype:'textarea',--%>
                                <%--readOnly:viewReadOnly--%>
                            <%--}--%>
                        ]
                    },
                    {
                        collapsible: true,
                        title: '基本信息',
                        xtype: 'itemselector',
                        name: 'productIdsString',
                            fieldLabel: '权限设置',
                        imagePath: '../ext/ux/images/',
                        width:590,
                        emptyText: '请选择...',
                        autoHeight:true,
                        multiselects: [
                            {
                                legend:'可选产品',
                                width: 150,
                                height: 200,
                                store: productStore,
                                displayField: 'text',
                                valueField: 'value'
                            },
                            {
                                legend:'已选产品',
                                width: 150,
                                height: 200,
                                displayField: 'text',
                                valueField: 'value',
                                store: selectedStore,
                                name:'cpIds',
                                tbar:[
                                    {
                                        text: '清空',
                                        handler:function() {
                                            cspViewForm.getForm().findField('productIdsString').reset();
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                ]
            });
            displayDiv.innerHTML = '';
            cspViewForm.render(displayDiv);
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            //除了基本操作（删除，查看），还要添加的链接
            //defaultViewFormButtons.push({text:'锁定',handler:doAction,action:'lock'});
            checkAllFunctions(); //检查权限
            loadFormFromServerData();
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