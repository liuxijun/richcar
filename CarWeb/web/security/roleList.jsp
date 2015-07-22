<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "role";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","roleManage,roleDelete");
        needPermissions(actionHeader,"view","roleManage,roleView");
        needPermissions(actionHeader,"add","roleManage");
        needPermissions(actionHeader,"list","roleManage,roleList");
        needPermissions(actionHeader,"save","roleManage,roleSave");
        needPermissions(actionHeader,"deleteSelected","roleManage,roleDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="role"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "roleList.jsp";
        actionHeader = "role";
        defaultPageSize=13;
        function displayRoleType(val){
            return getDictStoreText('roleType',val);
        }
        function afterLoad(){
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            storeConfig.fields = ['roleid','name','memo','type'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
             storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "roleid";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'roleid'}                ,
                {header: "角色名称",hidden:false, align:'center', width: 160, sortable: true,dataIndex: 'name'}                ,
                {header: "角色描述",hidden:false, align:'center', width: 160, sortable: true,dataIndex: 'memo'},
                {header: "角色类型",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'type',renderer:displayRoleType},
                {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'roleid'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var roleGrid = new FortuneSearchListGrid({
                title:'<fts:text name="role"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:528,
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
            roleGrid.render(displayDiv);
            defaultGrid = roleGrid;
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
            ],
            afterLoad);
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