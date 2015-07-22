<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp" %>
<%
    //设置页面显示基本信息
    String actionHeader = "admin";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "delete", "adminManage,adminDelete");
        needPermissions(actionHeader, "add", "adminManage,adminSave");
        needPermissions(actionHeader, "view", "adminManage");
        needPermissions(actionHeader, "list", "adminManage");
        needPermissions(actionHeader, "save", "adminManage");
        needPermissions(actionHeader, "deleteSelected", "adminManage");
        needPermissions(actionHeader, "unlock", "adminManage");
        needPermissions(actionHeader, "lock", "adminManage");
        needPermissions(actionHeader, "bindCsp", "adminManage,adminBindCsp");
        needPermissions(actionHeader, "lockSelected", "adminManage,adminLockSelected");
        needPermissions(actionHeader, "unlockSelected", "adminManage,adminUnlockSelected");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>管理员管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        <%
            long adminId = 0;
            if (admin != null){
                adminId = admin.getId();
            }
        %>
        var adminId = <%=adminId%>;

        nextUrl = "adminList.jsp";
        actionHeader = "admin";

        defaultPageSize = 17;

        function deleteAdminIdFunction(actionUrl, parameters) {
            for (var i = 0; i < searchStore.getCount(); i++) {
                if (adminId == searchStore.getAt(i).id) {
                    Ext.MessageBox.alert('操作提示','  不能删除已登录用户！ ');
                    break;
                } else {
                    requestActionAjax(actionUrl, parameters);
                    break;
                }
            }

        }


        function displayOperatorManage(val, meta, record) {
            var manageLink = [];
            var i = 0;
            for (i = 0; i < defaultManageLinks.length; i++) {
                manageLink.push(defaultManageLinks[i]);
            }
            if (record.get("status") == "1") {
                manageLink.push({text:'锁定', action:'lock', type:'onclick', messageInfo:'请确认锁定该用户'});
            } else {
                manageLink.push({text:'解锁', action:'unlock', type:'onclick', messageInfo:'请确认解锁该用户'});
            }
            manageLink.push({text:'绑定CSP', action:'bindCsp', type:'link', href:'../csp/adminCspList.jsp', parameter:'obj.adminId'});
            return displayControl(val, meta, record, manageLink);
        }


        Ext.onReady(function () {
            Ext.QuickTips.init();
            storeConfig.fields = ['id', 'login', 'realname', 'modifydate', 'lastlogintime', 'telephone', 'status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:getActionUrl('list')});
            storeConfig.baseParams = {limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id', hidden:true, align:'center', header:"序号", width:60, sortable:true, dataIndex:'id'},
                {header:"账号", align:'center', width:70, sortable:true, renderer:displayName, dataIndex:'login'},
                {header:"姓名", align:'center', width:100, sortable:true, dataIndex:'realname'},
                {header:"电话", hidden:true, align:'center', width:80, sortable:true, dataIndex:'telephone'},
                {header:"最后登录", align:'center', width:120, sortable:true, dataIndex:'lastlogintime'},
                {header:"修改日期", align:'center', width:120, sortable:true, dataIndex:'modifydate'},
                {header:"状态", align:'center', width:50, sortable:false, renderer:displayStatus, dataIndex:'status'},
                {header:"管理", align:'center', width:150, sortable:false, renderer:displayOperatorManage, dataIndex:'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的

            defaultBottomButtons.push({text:'锁定所选', handler:doSelected, action:'lockSelected'});
            defaultBottomButtons.push({text:'解锁所选', handler:doSelected, action:'unlockSelected'});

            checkAllFunctions();
            var adminGrid = new FortuneSearchListGrid({
                title:'管理员列表',
                sm:sm,
                cm:columns,
                width:645,
                height:500,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '},
                    {text:'名称'},

                    new Ext.ux.form.SearchField({
                        store:searchStore,
                        paramName:'obj.login',
                        width:320
                    })
                ]
                }),
                bbar:new Ext.PagingToolbar({
                    pageSize:defaultPageSize,
                    store:searchStore,
                    displayInfo:true,
                    displayMsg:'结果数据 {0} - {1} of {2}',
                    emptyMsg:"没有数据",
                    items:defaultBottomButtons
                })
            });

            adminGrid.render(displayDiv);
            defaultGrid = adminGrid;
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