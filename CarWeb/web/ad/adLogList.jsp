<%@ page import="com.fortune.util.StringUtils" %>
<%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    //初始化本页权限需求
    String actionHeader = "adLog";
    {
        session.setAttribute("actionHeader",actionHeader);
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8">
    <title>日志列表</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script type="text/javascript">
        nextUrl = "adLogList.jsp";
        actionHeader = "adLog";
        defaultPageSize = 13;
        var adId = "<%=StringUtils.string2int(request.getParameter("adId"),-1)%>";
        var trainId = "<%=StringUtils.string2int(request.getParameter("trainId"),-1)%>";
        function doSearch(){
            var startTime = getCmpValue("startTime");
            if(startTime!=null&&startTime!=""){
                startTime = startTime+" 00:00:00";
            }
            var stopTime = getCmpValue("stopTime");
            if(stopTime!=null&&stopTime!=""){
                stopTime = stopTime + " 23:59:59";
            }
            var baseParams = searchStore.baseParams;
            baseParams["startTime"]=startTime;
            baseParams["stopTime"]=stopTime;
            baseParams["trainId"]=trainId;
            baseParams["adId"]=adId;
            baseParams["adName"] = getCmpValue("adName");
            searchStore.baseParams = baseParams;
            searchStore.load();
        }
        checkAllFunctions();  // 检查用户权限
        function initDisplay(){
            storeConfig.fields = ['id','adId','adName','trainId','contentId','contentName','visitTime','channelName'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url: '../ad/adLog!listLogs.action'});
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'id'},
                {id:'adName',hidden:false,align:'center',header: "广告", width: 180, sortable: true, dataIndex: 'adName'}
                ,{id:'contentName',hidden:false,align:'center',header: "投放影片", width: 220, sortable: true, dataIndex: 'contentName'}
                ,{id:'channelName',hidden:false,align:'center',header: "投放栏目", width: 80, sortable: true, dataIndex: 'channelName'}
                ,{id:'visitTime',hidden:false,align:'center',header: "投放时间", width: 120, sortable: true, dataIndex: 'visitTime'}
                //,{header: "管理", align:'center', width: 100, sortable: false,renderer:displayManage,dataIndex: 'id'}
            ]);
            //除了基本操作（删除，查看），还要添加的链接
            //defaultManageLinks.push({text:'锁定',action:'lock',type:'onclick'});
            //工具栏中的按钮，还需要添加的
            //defaultBottomButtons.push({text:'锁定所选',handler:doSelected,action:'lock'});
            checkAllFunctions();  // 检查用户权限
            var adLogGrid = new FortuneSearchListGrid({
                title:'广告投放统计',
                sm : sm,
                cm:columns,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {text:'广告名称'},
                    {xtype:'textfield',id:'adName'},
                    {text:'起始日期：'},
                    {xtype:'datefield',id:'startTime',format:'Y-m-d'},
                    {text:'截至日期：'},
                    {xtype:'datefield',id:'stopTime',format:'Y-m-d'},
                    {text:'搜索',handler:doSearch}]}),
                bbar:new Ext.PagingToolbar({
                        pageSize: defaultPageSize,
                        store: searchStore,
                        displayInfo: true,
                        displayMsg: '结果数据 {0} - {1} of {2}',
                        emptyMsg: "没有数据",
                        items:defaultBottomButtons
                })
            });
            adLogGrid.render('displayDiv');
            defaultGrid = adLogGrid;
            searchStore.load({params:{start:0, limit:defaultPageSize,adId:adId,trainId:trainId}});
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