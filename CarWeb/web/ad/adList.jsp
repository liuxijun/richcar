<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/permissionLimits.jsp"%><%
    //初始化本页权限需求
    String actionHeader = "ad";
    {
        session.setAttribute("actionHeader",actionHeader);
        needPermissions(actionHeader,"view","adManage");
        needPermissions(actionHeader,"delete","adManage");
        needPermissions(actionHeader,"deleteSelected","adManage");
        needPermissions(actionHeader,"add","adManage");
        needPermissions(actionHeader,"save","adManage");
        needPermissions(actionHeader,"list","adManage");
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="ad"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "adList.jsp";
        actionHeader = "ad";
        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','name','desp','startTime','endTime','adLength','adPlayType','playTime','isForce'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'},
                {header: "<fts:text name="ad.name"/>",hidden:false, align:'center', width: 140, sortable: true,dataIndex: 'name'},
                {header: "<fts:text name="ad.desp"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'desp'},
                {header: "<fts:text name="ad.startTime"/>",hidden:false, align:'center', width: 130, sortable: true,dataIndex: 'startTime'},
                {header: "<fts:text name="ad.endTime"/>",hidden:false, align:'center', width: 130, sortable: true,dataIndex: 'endTime'},
                {header: "<fts:text name="ad.adLength"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'adLength'},
                {header: "<fts:text name="ad.adPlayType"/>",hidden:false, align:'center', width: 60, 
                    sortable: true,dataIndex: 'adPlayType',renderer:function (val,p,row){return getDictStoreText('adPlayType',val);}},
                {header: "<fts:text name="ad.url"/>",hidden:true, align:'center', width: 160, sortable: true,dataIndex: 'url'},
                {header: "<fts:text name="ad.playTime"/>",hidden:true, align:'center', width: 50, sortable: true,dataIndex: 'playTime'},
                {header: "<fts:text name="ad.isForce"/>",hidden:true, align:'center', width: 60, sortable: true,dataIndex: 'isForce'},
                {header: "管理", align:'center', width: 70, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var adGrid = new FortuneSearchListGrid({
                title:'<fts:text name="ad"/>列表',
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
            adGrid.render(displayDiv);
            defaultGrid = adGrid;
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