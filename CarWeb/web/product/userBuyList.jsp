<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "userBuy";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="userBuy"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "userBuyList.jsp";
        actionHeader = "userBuy";
        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','userId','userIp','spId','channelId','contentId','serviceProductId','isGift','price','startTime','endTime','buyTime'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="userBuy.userId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'userId'}                ,
                {header: "<fts:text name="userBuy.userIp"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'userIp'}                ,
                {header: "<fts:text name="userBuy.spId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'spId'}                ,
                {header: "<fts:text name="userBuy.channelId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'channelId'}                ,
                {header: "<fts:text name="userBuy.contentId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'contentId'}                ,
                {header: "<fts:text name="userBuy.serviceProductId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'serviceProductId'}                ,
                {header: "<fts:text name="userBuy.isGift"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'isGift'}                ,
                {header: "<fts:text name="userBuy.price"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'price'}                ,
                {header: "<fts:text name="userBuy.startTime"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'startTime'}                ,
                {header: "<fts:text name="userBuy.endTime"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'endTime'}                ,
                {header: "<fts:text name="userBuy.buyTime"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'buyTime'},
                {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var userBuyGrid = new FortuneSearchListGrid({
                title:'<fts:text name="userBuy"/>列表',
                sm : sm,
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
            userBuyGrid.render(displayDiv);
            defaultGrid = userBuyGrid;
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