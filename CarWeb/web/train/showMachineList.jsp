<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "showMachine";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","trainAdDeviceManage");
        needPermissions(actionHeader,"delete","trainAdDeviceManage");
        needPermissions(actionHeader,"deleteSelected","trainAdDeviceManage");
        needPermissions(actionHeader,"add","trainAdDeviceManage");
        needPermissions(actionHeader,"save","trainAdDeviceManage");
        needPermissions(actionHeader,"list","trainAdDeviceManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="showMachine"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "adList.jsp";
        actionHeader = "showMachine";
        checkAllFunctions();  // 检查用户权限

        function initDisplay(){
            storeConfig.fields = ['id','name','position','sn','trainName','trainId','type','status'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'},
                {header: "<fts:text name="showMachine.name"/>",hidden:false, align:'center', width: 120, sortable: true,dataIndex: 'name'},
                {header: "<fts:text name="showMachine.position"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'position'},
                {header: "<fts:text name="showMachine.sn"/>",hidden:false, align:'center', width: 120, sortable: true,dataIndex: 'sn'},
                {header: "<fts:text name="showMachine.trainName"/>",hidden:false, align:'center', width: 120, sortable: true,dataIndex: 'trainName'},
                {header: "<fts:text name="showMachine.type"/>",hidden:false, align:'center', width: 100, sortable: true,dataIndex: 'type',renderer:function(val){
                    return getDictStoreText('showMachineType',val);
                }},
                {header: "<fts:text name="showMachine.status"/>",hidden:false, align:'center', width: 80, sortable: true,dataIndex: 'status',renderer:function(val){
                    return getDictStoreText('status',val);
                }},
                {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var trainGrid = new FortuneSearchListGrid({
                title:'<fts:text name="showMachine"/>列表',
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
            trainGrid.render('displayDiv');
            defaultGrid = trainGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize}});
        }
        Ext.onReady(function() {
            Ext.QuickTips.init();
            queueFunctions([
                {
                    func:initDictStores,
                    done:false,
                    flag:'initDictStores'
                }
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