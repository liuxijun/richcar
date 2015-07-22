<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
<%@include
        file="../inc/permissionLimits.jsp"%>
<%
    //初始化本页权限需求
    String actionHeader = "stb";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"delete","stbManage");
        needPermissions(actionHeader,"view","stbManage,stbView");
        needPermissions(actionHeader,"list","stbManage,stbList");
        needPermissions(actionHeader,"save","stbManage,stbSave");
        needPermissions(actionHeader,"add","stbManage");
        needPermissions(actionHeader,"deleteSelected","stbManage,stbDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>机顶盒管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
        <script type="text/javascript">
        nextUrl = "stbList.jsp";
        actionHeader = "stb";
        checkAllFunctions();  // 检查用户权限
        defaultPageSize=17;
        function initDisplay(){
            storeConfig.fields = ['id','userId','serialNo','stbType','location','status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "关联用户",hidden:false, align:'center', width: 135, sortable: true,dataIndex: 'userId'}                ,
                {header: "系列号",hidden:false, align:'center', width: 90, sortable: true,dataIndex: 'serialNo'}                ,
                {header: "机顶盒类型",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'stbType',
                        renderer:
                        function (val,p,row){
                            return getDictStoreText('stbType',val);
                        }
                }                ,

                {header: "安装位置",hidden:false, align:'center', width: 175, sortable: true,dataIndex: 'location'}                ,
                {header: "状态",hidden:false, align:'center', width: 70, sortable: true,dataIndex: 'status'
                    ,
                    renderer:
                        function (val,p,row){
                            return getDictStoreText('stbStatus',val);
                        }
                }  ,
                {header: "管理", align:'center', width: 80, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var stbGrid = new FortuneSearchListGrid({
                title:'机顶盒列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:650,
                height:400,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'系列号'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.serialNo',
                            width:360
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
            stbGrid.render(displayDiv);
            defaultGrid = stbGrid;
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