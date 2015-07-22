<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "permission";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","permissionManage,permissionDelete");
        needPermissions(actionHeader,"view","permissionManage,permissionView");
        needPermissions(actionHeader,"list","permissionManage,permissionList");
        needPermissions(actionHeader,"save","permissionManage,permissionSave");
        needPermissions(actionHeader,"add","permissionManage");
        needPermissions(actionHeader,"deleteSelected","permissionManage,permissionDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="permission"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "permissionList.jsp";
        actionHeader = "permission";

        defaultPageSize=17;
        Ext.onReady(function() {
            Ext.QuickTips.init();
            storeConfig.fields = ['permissionId','name','target','classname','methodName','permissionDesc'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "permissionId";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'permissionId'}                ,
                {header: "<fts:text name="permission.name"/>",hidden:false, align:'center', width: 200, sortable: true,dataIndex: 'name'}                ,
                {header: "<fts:text name="permission.target"/>",hidden:false, align:'center', width: 130, sortable: true,dataIndex: 'target'}                ,
                {header: "<fts:text name="permission.methodname"/>",hidden:true, align:'center', width: 80, sortable: true,dataIndex: 'methodName'}                ,
                {header: "<fts:text name="permission.classname"/>",hidden:false, align:'center', width: 190, sortable: true,dataIndex: 'classname'}                ,
                {header: "<fts:text name="permission.permissiondesc"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'permissionDesc'},
                {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'permissionId'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var permissionGrid = new FortuneSearchListGrid({
                title:'<fts:text name="permission"/>列表',
                sm : sm,
                cm:columns,
                height:500,
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
            permissionGrid.render(displayDiv);
            defaultGrid = permissionGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
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