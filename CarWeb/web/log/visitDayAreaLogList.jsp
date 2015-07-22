<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "visitDayAreaLog";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="visitDayAreaLog"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "visitDayAreaLogList.jsp";
        actionHeader = "visitDayAreaLog";
        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','spId','cpId','channelId','areaId','day','count','length'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="visitDayAreaLog.spId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'spId'}                ,
                {header: "<fts:text name="visitDayAreaLog.cpId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'cpId'}                ,
                {header: "<fts:text name="visitDayAreaLog.channelId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'channelId'}                ,
                {header: "<fts:text name="visitDayAreaLog.areaId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'areaId'}                ,
                {header: "<fts:text name="visitDayAreaLog.day"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'day'}                ,
                {header: "<fts:text name="visitDayAreaLog.count"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'count'}                ,
                {header: "<fts:text name="visitDayAreaLog.length"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'length'},
                {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var visitDayAreaLogGrid = new FortuneSearchListGrid({
                title:'<fts:text name="visitDayAreaLog"/>列表',
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
            visitDayAreaLogGrid.render(displayDiv);
            defaultGrid = visitDayAreaLogGrid;
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