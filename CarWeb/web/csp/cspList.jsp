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
    //初始化本页权限需求
    String actionHeader = "csp";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader, "delete", "cspManage,cspDelete");
        needPermissions(actionHeader, "view", "cspManage,cspView");
        needPermissions(actionHeader, "list", "cspManage,cspSearch");
      //  needPermissions(actionHeader, "save", "cspManage,cspSave");
        needPermissions(actionHeader,"add","cspManage");
        needPermissions(actionHeader, "bindDevice", "cspManage");
        needPermissions(actionHeader, "binCP", "cspManage");
        needPermissions(actionHeader, "binProduct", "cspManage");
        needPermissions(actionHeader, "binModule", "cspManage");
        needPermissions(actionHeader, "binAuditor", "cspManage");
        needPermissions(actionHeader, "deleteSelected", "cspManage,cspDeleteSelected");
        needPermissions(actionHeader,"binChannel","cspManage");
    }

%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="csp"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        nextUrl = "cspList.jsp";
        actionHeader = "csp";
        defaultPageSize = 17;
        checkAllFunctions();  // 检查用户权限
        function initDisplay() {
            storeConfig.fields = ['id', 'name', 'address', 'phone', 'email', 'status', 'isCp', 'isSp', 'isCpOnlineAudit', 'isCpOfflineAudit', 'isSpOnlineAudit', 'isSpOfflineAudit', 'alias', 'spId'];
            storeConfig.proxy = new Ext.data.HttpProxy({method:'POST', url:getActionUrl('list')});
            storeConfig.baseParams = {limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id', hidden:true, align:'center', header:"序号", width:60, sortable:true, dataIndex:'id'}                ,
                {header:"<fts:text name="csp.name"/>", hidden:false, align:'center', width:145, sortable:true, dataIndex:'name'}                ,
                {header:"<fts:text name="csp.isCp"/>", hidden:false, align:'center', width:60, sortable:true, dataIndex:'isCp', renderer:function (val, p, row) {
                    switch (val) {
                        case 1:
                            return '是';
                        case 0:
                            return '否';
                    }
                }
                }                ,
                {header:"<fts:text name="csp.isSp"/>", hidden:false, align:'center', width:60, sortable:true, dataIndex:'isSp', renderer:function (val, p, row) {
                    switch (val) {
                        case 1:
                            return '是';
                        case 0:
                            return '否';
                    }
                }
                }                ,
                {header:"<fts:text name="绑定设备"/>", hidden:false, align:'center', width:70, sortable:false,
                    renderer:function (val, p, rec) { //d
                        if (canDoThisAction('bindDevice')) {
                            return '<a href="/system/deviceBinding.jsp?keyId=' + val + '">绑定</a>';
                        }
                    }, dataIndex:"id"},
                {header:"<fts:text name="绑定CP"/>", hidden:false, align:'center', width:60, sortable:false,
                    renderer:function (val, p, row) { //d
                        if (canDoThisAction('binCP')) {
                            if (row.data.isSp == 1) {
                                return '<a href="/csp/cspBinding.jsp?keyId=' + val + '">绑定</a>';
                            }
                        }

                    }, dataIndex:"id"},
                {header:"<fts:text name="绑定产品"/>", hidden:false, align:'center', width:70, sortable:false,
                    renderer:function (val, p, row) { //d
                        if (canDoThisAction('binProduct')) {
                            if (row.data.isSp == 1) {
                                return '<a href="/product/productBinding.jsp?keyId=' + val + '">绑定</a>';
                            }
                        }

                    }, dataIndex:"id"},
                {header:"<fts:text name="绑定频道"/>", hidden:false, align:'center', width:70, sortable:false,
                    renderer:function (val, p, row) { //d
                        if (canDoThisAction('binChannel')) {
                            if(row.data.isSp==1){
                            return '<a href="/publish/cspChannel.jsp?keyId=' + val + '">绑定</a>';
                            }
                        }

                    }, dataIndex:"id"},
                {header:"<fts:text name="管理模板"/>", hidden:false, align:'center', width:70, sortable:false,
                    renderer:function (val, p, row) { //d
                        if (canDoThisAction('binModule')) {
                            //if(row.data.isCp==1){
                            return '<a href="/module/moduleBinding.jsp?keyId=' + val + '">管理</a>';
                            //}
                        }

                    }, dataIndex:"id"},
                {header:"<fts:text name="审核人"/>", hidden:false, align:'center', width:70, sortable:false,
                    renderer:function (val, p, row) { //d
                        if (canDoThisAction('binAuditor')) {
                            //                           if(row.data.isSpOnlineAudit==1){
                            return '<a href="/security/auditorBinding.jsp?keyId=' + val + '">管理</a>';
//                           }
                        }


                    }, dataIndex:"id"},

                {header:"管理", align:'center', width:70, sortable:false, renderer:displayManage, dataIndex:'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var cspGrid = new FortuneSearchListGrid({
                title:'<fts:text name="csp"/>列表',
                sm:sm,
                cm:columns,
                height:505,
                width:700,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '},
                    {text:'名称'},
                    new Ext.ux.form.SearchField({
                        store:searchStore,
                        paramName:'obj.name',
                        width:320
                    })
                ]}),
                bbar:new Ext.PagingToolbar({
                    pageSize:defaultPageSize,
                    store:searchStore,
                    displayInfo:true,
                    displayMsg:'结果数据 {0} - {1} of {2}',
                    emptyMsg:"没有数据",
                    items:defaultBottomButtons
                })
            });
            cspGrid.render(displayDiv);
            defaultGrid = cspGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function () {
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