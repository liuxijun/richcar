<%@ page import="com.fortune.util.StringUtils" %><%@ page
        import="java.util.*" %>
<%@ page import="com.fortune.util.JsonUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 14-5-8
  Time: 上午10:09
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    {
        Admin _opLogined =
                (Admin)session.getAttribute(
                        com.fortune.common.Constants.SESSION_ADMIN);
        if(_opLogined == null){
%><html><body onload="jumpForm.submit()">
<form name="jumpForm" target="_top" action="../index.jsp"></form></body></html>
<%
            return;
        }
    }
    String startDate = request.getParameter("startDate");
    Date startTime;
    if(startDate == null||"".equals(startDate)){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //星期日是1，星期一是2，星期六是7。按照我朝习惯，星期一是第一天，所以要转换一下
        dayOfWeek=dayOfWeek-1;
        if(dayOfWeek==0){
            dayOfWeek = 7;
        }
        startTime = new Date(System.currentTimeMillis()-(7+dayOfWeek-1)*24*3600*1000L);
        startDate = StringUtils.date2string(startTime,"yyyy-MM-dd")+" 00:00:00";
    }else{
        startTime = StringUtils.string2date(startDate);
    }

    String stopDate = request.getParameter("stopDate");
    if(stopDate==null||"".equals(stopDate)){
        stopDate = StringUtils.date2string(new Date(startTime.getTime()+7*24*3600*1000L),"yyyy-MM-dd")+" 00:00:00";
    }
    if("true".equals(request.getParameter("jsonFormat"))){
        String sql = "select a.NAME,a.ID,b.CC1 DOWNLOAD_COUNT,b.CC2 ACTIVE_COUNT from area a left join (select " +
                "count(case status when 1 then status end) cc1," +
                "count(case status when 2 then status end) cc2," +
                "area_id " +
                "from CLIENT_LOG where time > to_date('"+startDate+"','YYYY-MM-DD HH24:MI:SS')" +
                "  and time<=to_date('"+stopDate+"','YYYY-MM-DD HH24:MI:SS')  group by area_id ) b" +
                " on a.id = b.area_id where a.id>300 and a.id<1000";
        long areaId = StringUtils.string2long(request.getParameter("areaId"),-1);
        if(areaId>0){
            sql += " and b.area_id="+areaId;
        }
        sql += " order by b.cc1 desc";
        logger.debug(sql);
        Map<String,Object> result = new HashMap<String,Object>();
        List areaLoginStatics=executeSql(sql,jdbcDriver,jdbcUrl,jdbcUser,jdbcPwd);
        int allCount = 0;
        int allActiveCount=0;
        for (Object r : areaLoginStatics){
            if (r instanceof Object[]){
                Object[] dataResult = (Object[]) r;
                List<Map<String,String>> dataRows = (List<Map<String,String>>) dataResult[1];
                int i=0;
                for(Map<String,String> row:dataRows){
                    i++;
                    //row.put("id",""+i);
                    String downloadCount =row.get("DOWNLOAD_COUNT");
                    if(downloadCount == null){
                        downloadCount = row.get("cc1");
                        if(downloadCount!=null){
                            row.remove("cc1");
                            row.put("DOWNLOAD_COUNT",downloadCount);
                        }
                    }
                    int count =StringUtils.string2int(downloadCount,0);
                    String activeCountStr = row.get("ACTIVE_COUNT");
                    if(activeCountStr==null){
                        activeCountStr = row.get("cc2");
                        if(activeCountStr!=null){
                            row.remove("cc2");
                            row.put("ACTIVE_COUNT",activeCountStr);
                        }
                    }
                    int activeCount=StringUtils.string2int(row.get("ACTIVE_COUNT"),0);
                    allCount+=count;
                    allActiveCount+=activeCount;
                }
                Map<String,String> row = new HashMap<String,String>();
                row.put("NAME","累计");
                row.put("ID","0");
                row.put("DOWNLOAD_COUNT",allCount+"");
                row.put("ACTIVE_COUNT",allActiveCount+"");
                dataRows.add(row);
                result.put("objs",dataRows);
                result.put("total",dataRows.size());
            }
        }

        int acitveCountOnSearch=getIntSqlResult("select count(*) from CLIENT_LOG where status=2 and time > to_date('"+
                startDate+"','YYYY-MM-DD HH24:MI:SS') and time<=to_date('"+
                stopDate+"','YYYY-MM-DD HH24:MI:SS')");
        int allClientCountOfActive =getIntSqlResult("select count(*) from CLIENT_LOG where status=2");
        result.put("acitveCountOnSearch",acitveCountOnSearch);
        result.put("allClientCountOfActive",allClientCountOfActive);
        //result.put("objs",areaLoginStatics);
        String jsonFormat = JsonUtils.getJsonString(result);
        out.print(jsonFormat);
        return;
    }
%><%@include file="../inc/checkHeader.jsp"%><html>
<head>
    <title>登过燕赵视界平台手机号码列表</title>
    <%@include file="../inc/jsCssLib.jsp"%>
    <%@include file="../inc/extBase.jsp"%>
    <script language="javascript">

        var panel;
        function doSearch(){
            var baseParams = searchStore.baseParams;
            baseParams["startDate"]=getCmpValue("startDate").format('Y-m-d H:i:s');
            baseParams["stopDate"]=getCmpValue("stopDate").format('Y-m-d H:i:s');
            searchStore.baseParams = baseParams;
            searchStore.load();
        }
        function initDisplay(){
            storeConfig.fields = ['ID','NAME','DOWNLOAD_COUNT','ACTIVE_COUNT'];
            storeConfig.proxy = new Ext.data.HttpProxy({method: 'POST',url:'downloadLogList.jsp?jsonFormat=true' });
            searchStore = new FortuneSearchStore(storeConfig);
            keyFieldId = "id";
            var columns = new Ext.grid.ColumnModel([
                //sm,
                {id:'Id',hidden:true,align:'center',header: "序号", width: 60, sortable: true, dataIndex: 'ID'}
                ,{header:'区域名称',width:180,align:'center',dataIndex:'NAME'}
                ,{header:'下载次数',width:120,align:'center',dataIndex:'DOWNLOAD_COUNT'}
                ,{header:'激活个数',width:120,align:'center',dataIndex:'ACTIVE_COUNT'}
            ]);
            var adGrid = new FortuneSearchListGrid({
                title:'下载激活统计',
                sm : sm,
                cm:columns,
                store:searchStore,
                tbar:new Ext.Toolbar({items:[
                    {xtype:'label',text:'起始时间：'}
                    ,{xtype:'datefield',format:'Y-m-d H:i:s',id:'startDate',value:'<%=startDate%>',width:140}
                    ,{xtype:'label',text:'截至时间'}
                    ,{xtype:'datefield',format:'Y-m-d H:i:s',id:'stopDate',value:'<%=stopDate%>',width:140}
                    ,{text:'搜索',handler:doSearch}
                ]}),
                bbar:new Ext.PagingToolbar({
                    pageSize: defaultPageSize,
                    store: searchStore,
                    displayInfo: true,
                    displayMsg: '结果数据 {0} - {1} of {2}',
                    emptyMsg: "没有数据"
                    //,items:[{xtype:'label',text:'历史累计激活数量：',id:'historyActive'}]
                })
            });
            adGrid.render(displayDiv);
            defaultGrid = adGrid;
            //doSearch();
        }

        Ext.onReady(function(){
/*
            panel = new Ext.grid.GridPanel({
                        title:'下载激活统计',
                        width:640,
                        autoHeight:true,
                        store:searchStore,
                        trackMouseOver:false,
                        disableSelection:true,
                        loadMask: true,
                        height:480,
                        columns:columns,
                        tbar:new Ext.Toolbar({items:[{xtype:'label',text:'起始时间：'}
                            ,{xtype:'datefield',format:'Y-m-d H:i:s',id:'startTime',value:'<%=startDate%>'}
                            ,{xtype:'label',text:'截至时间'}
                            ,{xtype:'datefield',format:'Y-m-d H:i:s',id:'stopTime',value:'<%=startDate%>'}
                                ,{text:'搜索',handler:doSearch}
                        ]})
                    }
            );
            panel.render('displayDiv');
*/
            initDisplay();
            doSearch();
        });
    </script>
</head>
<body>
<table align="center">
    <tr>
        <td><div id="displayDiv"></div></td>
    </tr>
</table>
<%--
   <form action="downloadLogList.jsp" method="post">
       <table width="500">
           <tr><td>从<%=startDate%>到<%=stopDate%>下载情况统计：</td></tr>
           &lt;%&ndash;<tr><td>
               <%=sql%>
           </td></tr>&ndash;%&gt;
           <tr>
               <td>
                   <table border="0" cellpadding="0" cellspacing="0" style="background: #E0E6E8">
<%
%>               <tr>
                       <td align='center' style="background: #F0F0F0;width:100px">累计</td>
                       <td align='center' style='width:400px'><%=allCount%></td>
                       <td align='center' ><%=allActiveCount%></td>
                   </tr>
                   </table>
               </td>
           </tr>
           <tr>
               <td>激活总量：<%=acitveCountOnSearch%></td>
           </tr>
           <tr>
               <td>累计激活：<%=allClientCountOfActive%></td>
           </tr>
           <tr>
               <td><label for="startDate">起始时间：</label><input id="startDate" type="text" name="startDate" value="<%=startDate%>">
                   <label for="stopDate">截至时间：</label><input id="stopDate" type="text" name="stopDate" value="<%=stopDate%>">
                   <input type="submit" value="搜索"/>
               </td>
           </tr>
       </table>
   </form>
--%>
</body>
</html><%@include file="../admin/sqlBase.jsp"%>
<%!
    public int getIntSqlResult(String sql){
        List<Map<String,String>> dataRows = getSqlResult(sql);
        if(dataRows!=null&&dataRows.size()>0){
            return StringUtils.string2int(dataRows.get(0).values().iterator().next(),-1);
        }
        return -1;
    }

    public List<Map<String,String>> getSqlResult(String sql){
//        logger.debug("准备执行："+sql);
        List<Object> executeResult = executeSql(sql,null,null,null,null);
        if(executeResult!=null){
            for(Object result:executeResult){
                if(result instanceof Object[]){
                    Object[] dataResult = (Object[]) result;
                    return (List<Map<String,String>>) dataResult[1];
                }
            }
        }
        return new ArrayList<Map<String, String>>(0);
    }
%>