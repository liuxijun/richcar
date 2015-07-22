<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp" %><%
    //初始化本页权限需求
    String actionHeader = "area";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"AreaIpRange","areaManage");
        needPermissions(actionHeader,"add","areaManage");
     //   needPermissions(actionHeader,"save","areaManage,areaSave");
        needPermissions(actionHeader,"list","areaManage,areaList");
        needPermissions(actionHeader,"view","areaManage,areaView");
        needPermissions(actionHeader,"deleteSelected","areaManage,areaDeleteSelected");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="area"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "areaList.jsp";
        actionHeader = "area";
        checkAllFunctions();  // 检查用户权限

        defaultPageSize=13;
        function initDisplay(){
            storeConfig.fields = ['id','name','desp'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="area.name"/>",hidden:false, align:'center', width: 100,renderer:displayName,sortable: true,dataIndex: 'name'}                ,
                {header: "<fts:text name="area.desp"/>",hidden:false, align:'center', width: 150, sortable: true,dataIndex: 'desp',
                    renderer:
                        function(val,p,row){
                            if(canDoThisAction()){

                            }
                        }},
                {header: "管理IP", align:'center', width: 100, sortable: false,dataIndex: 'id',
                    renderer:
                        function (val,p,row){
                            if(canDoThisAction('AreaIpRange'))  {
                            return '<a href=\'ipRangeList.jsp?obj.areaId='+val+'\'>管理</a>';
                        }}     },
                {header: "管理手机归属地", align:'center', width: 100, sortable: false,dataIndex: 'id',
                    renderer:
                            function (val,p,row){
                                if(canDoThisAction('AreaIpRange'))  {
                                    if(val<1000&&val>0){
                                        return '<a href=\'phoneRangeList.jsp?obj.areaId='+val+'\'>管理</a>';
                                    }
                                }}     }
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var areaGrid = new FortuneSearchListGrid({
                width:525,
                title:'区域列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.name',
                            width:210
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
            areaGrid.render(displayDiv);
            defaultGrid = areaGrid;
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