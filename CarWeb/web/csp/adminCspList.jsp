<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //初始化本页权限需求
    String actionHeader = "adminCsp";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader, "add", "adminManage");
        needPermissions(actionHeader, "view", "adminCspView");
        needPermissions(actionHeader, "delete", "adminCspDelete");
        needPermissions(actionHeader, "deleteSelected", "adminCspDeleteSelected");

    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="adminCsp"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "adminCspList.jsp";
        actionHeader = "adminCsp";
       defaultPageSize=17; 
      //checkAllFunctions();  // 检查用户权限

        var adminId =<%=request.getParameter("obj.adminId")%>;
        function deleteCallBackFunction() {
            nextUrl = "adminCspList.jsp?obj.adminId=" + adminId;
            gotoNextUrl();
        }

        defaultManageLinks = [
            {text:'解除绑定',action:'delete',type:'onclick',messageInfo:''},
            {text:'绑定角色',action:'view',type:'href'}
        ];
        defaultBottomButtons = [
            {text:'添加绑定',handler:addRecord,action:'add'},
            {text:'删除所选',handler:deleteSelectedRows,action:'deleteSelected'}
        ];
        function displayIsDefault(val,meta,rec){
            if(val==1){
                return "是"
            }
            return "否";
        }
        function displayAdminName(val,meta,rec){
            if(val==''||val==null){
                val = rec.get("admin.login");
            }
            return val;
        }
        function initDisplay(){
            storeConfig.fields = ['id','adminId','admin.login','admin.realname','csp.name','cspId','isDefaultCsp'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "管理员",hidden:false, align:'center', width: 180,
                    renderer:displayAdminName,
                    sortable: false,dataIndex: 'admin.realname'}                ,
                {header: "CSP",hidden:false, align:'center', width: 180, sortable: false,dataIndex: 'csp.name'}                ,
                {header: "默认",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'isDefaultCsp',renderer:displayIsDefault},
                {header: "管理", align:'center', width: 160, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var adminCspGrid = new FortuneSearchListGrid({
                title:'管理员绑定到CSP的列表',
                sm : sm,
                cm:columns,
                height:500,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'CSP'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.csp.name',
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
            adminCspGrid.render(displayDiv);
            defaultGrid = adminCspGrid;

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