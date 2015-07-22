<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "product";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","productManage,productDelete");
        needPermissions(actionHeader,"view","productManage,productView");
        needPermissions(actionHeader,"list","productManage,productSearch");
        needPermissions(actionHeader,"save","productManage,productSave");
        needPermissions(actionHeader,"add","productManage");
        needPermissions(actionHeader,"deleteSelected","productManage,productDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="product"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "productList.jsp";
        actionHeader = "product";
        defaultPageSize=17;
        checkAllFunctions();  // 检查用户权限

        function displayType(val){
                  return getDictStoreText("productType",val);
        }

        function displayLengthUnit(val){
                  return getDictStoreText("productLengthUnit",val);
        }

       /* function displayStatus(val){
            return getDictStoreText("productStatus",val);
        }*/
        function initDisplay(){
            storeConfig.fields = ['id','name','payProductNo','price','type','validLength','lengthUnit','autoPay','searchExtra','status'];
             storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url:  getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="product.name"/>",hidden:false, align:'center', width: 150, sortable: true,dataIndex: 'name'}                ,
                {header: "<fts:text name="product.payProductNo"/>",hidden:false, align:'center', width: 155, sortable: true,dataIndex: 'payProductNo'}                ,
                {header: "<fts:text name="product.price"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'price'}                ,
                {header: "<fts:text name="product.type"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'type',renderer:displayType}                ,
                {header: "<fts:text name="product.validLength"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'validLength'}                ,
                {header: "<fts:text name="product.lengthUnit"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'lengthUnit',renderer:displayLengthUnit}                ,
                {header: "<fts:text name="product.autoPay"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'autoPay',renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '是';
                                case 2:return '否';
                            }
                        }}                ,
                {header: "<fts:text name="product.searchExtra"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'searchExtra',renderer:
                        function (val,p,row){
                            switch(val){
                                case 1:return '是';
                                case 2:return '否';
                            }
                        }}           ,
                {header: "<fts:text name="product.status"/>",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'status',renderer:
                    function (val,p,row){
                        switch(val){
                           case 0:return "失效";
                           case 1:return "有效";
                           case 11:return "可订";
                           case 10:return "可退";
                           case 12:return "展示";
                        }
                    }
                },

                {header: "管理", align:'center', width: 90, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var productGrid = new FortuneSearchListGrid({
                title:'产品列表',
                sm : sm,
                width:670,
                height:505,
                cm:columns,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.name',
                            width:320
                        })
                    ]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据",
                        items:defaultBottomButtons

                })
            });
            productGrid.render(displayDiv);
            defaultGrid = productGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }/*,{
                    func:initAreaStore,
                    done:false,
                    flag:'initAreaStore'
                }*/
            ],
                   initDisplay);
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