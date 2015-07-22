<%@ page import="java.io.UnsupportedEncodingException" %>
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
    //初始化本页权限需求
    String actionHeader = "phoneRange";
    {
        session.setAttribute("actionHeader", actionHeader);
        needPermissions(actionHeader,"save","phoneRangeManage,phoneRangeSave");
        needPermissions(actionHeader,"list","phoneRangeManage,phoneRangeList");
        needPermissions(actionHeader,"view","phoneRangeManage,phoneRangeView");
        needPermissions(actionHeader,"add","areaManage");
        needPermissions(actionHeader,"deleteSelected","phoneRangeManage,phoneRangeDeleteSelected");
    }
%>
<%@include file="../inc/checkHeader.jsp" %>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="PhoneRange"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp" %>
    <%@include file="../inc/extBase.jsp" %>
    <script type="text/javascript">
        nextUrl = "phoneRangeList.jsp";
        actionHeader = "phoneRange";
        defaultPageSize=13;
        checkAllFunctions();  // 检查用户权限



        var areaId =<%=request.getParameter("obj.areaId")%>;
        function deleteCallBackFunction() {
            nextUrl = "phoneRangeList.jsp?obj.areaId=" + areaId;
            gotoNextUrl();
        }

        function displayName(val, meta, record) {
            var id = record.get("id");
            return '<a href=\'phoneRangeView.jsp?keyId='+id+'&obj.areaId='+areaId+'\'>'+val+'</a>';
        }

        function initDisplay(){
            storeConfig.fields = ['id','name','desp','phoneFrom','phoneTo'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            storeConfig.baseParams={limit:defaultPageSize};
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {
                    id:'Id',
                    hidden:true,
                    align:'center',
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id'
                }
                ,
                {
                    header: "<fts:text name="ipRange.name"/>",
                    hidden:false,
                    align:'center',
                    width: 100,
                    sortable: true,
                    dataIndex: 'name',
                    renderer: displayName
                }
                ,
                {
                    header: "<fts:text name="起始号段"/>",
                    hidden:false,
                    align:'center',
                    width: 100,
                    sortable: true,
                    dataIndex: 'phoneFrom'
                }
                ,
                {
                    header: "<fts:text name="结束号段"/>",
                    hidden:false,
                    align:'center',
                    width: 100,
                    sortable: true,
                    dataIndex: 'phoneTo'
                }
                ,
                {
                    header: "<fts:text name="描述信息"/>",
                    hidden:false,
                    align:'center',
                    width: 220,
                    sortable: true,
                    dataIndex: 'desp'
                }
                //  {header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var PhoneRangeGrid = new FortuneSearchListGrid({
                title:'<fts:text name="PhoneRange"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
                width:560,
                tbar:new Ext.Toolbar({items:[
                    {
                        text:'搜索: '
                    },
                    {
                        text:'名称'
                    },
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
            PhoneRangeGrid.render(displayDiv);
            defaultGrid = PhoneRangeGrid;
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