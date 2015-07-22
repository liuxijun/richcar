<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "areaIpRange";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title><fts:text name="areaIpRange"/>管理</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "areaIpRangeList.jsp";
        actionHeader = "areaIpRange";
        checkAllFunctions();  // 检查用户权限
         var areaId=<%=request.getParameter("obj.areaId")%>

      function deleteCallBackFunction(){
            nextUrl = "areaIpRangeList.jsp?obj.areaId="+areaId;
            gotoNextUrl();
        }

        function initDisplay(){
            storeConfig.fields = ['id','areaName','ipRangeName'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: getActionUrl('list')});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
    
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'}                ,
                {header: "<fts:text name="areaIpRange.areaId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'areaName'}                ,
                {header: "<fts:text name="areaIpRange.ipRangeId"/>",hidden:false, align:'center', width: 60, sortable: true,dataIndex: 'ipRangeName'},
                {header: "管理", align:'center', width: 100, sortable: false,dataIndex: 'id',renderer:
                        function (val,p,row){
                            return '<a href=\'areaIpRangeView.jsp?keyId='+val+'&obj.areaId='+areaId+'\'>查看</a>&nbsp;';
                        }
                }
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var areaIpRangeGrid = new FortuneSearchListGrid({
                title:'<fts:text name="areaIpRange"/>列表',
                sm : sm,
                cm:columns,
                store:searchStore,
          /*      tbar:new Ext.Toolbar({items:[
                    {text:'搜索: '}, {text:'名称'},
                    new Ext.ux.form.SearchField({
                            store: searchStore,
                            paramName:'obj.name',
                            width:320
                        })
                    ]}),*/
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据",
                        items:defaultBottomButtons
                })
            });
            areaIpRangeGrid.render(displayDiv);
            defaultGrid = areaIpRangeGrid;
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
            ],initDisplay);
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