<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.fortune.util.PageBean" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-1
  Time: 上午7:19
  重复的媒体，将只保留最后一个
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    StringBuilder sb = new StringBuilder();
    sb.append("启动："+ StringUtils.date2string(startTime)).append("\r\n");
    Long cspId = StringUtils.string2long(request.getParameter("cspId"),16241843L);
    if(cspId==null){
        cspId = 16241843L;
    }
    int count = 0;
    String firstSql ="select * from (select count(*) mc,c.name from content c where " +
            "c.csp_id="+cspId+" group by c.name) a where a.mc>1 order by mc desc";
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    List<Object> contentNames =null; /*HibernateUtils.findAll(contentLogicInterface.getSession(), "from Content c where c.name = (select cid from (select " +
            "count(*) mc,c.name cid from Content where c.cspId=" +
            " c group by c.name) a where a.mc>=2)");*/
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.repairDuplicatedContent.jsp");
    try {
        logger.debug("将执行："+firstSql);
        contentNames = executeSql(firstSql,null,null,null,null);
        //contentNames = HibernateUtils.findAll(contentLogicInterface.getSession(),firstSql);
    } catch (Exception e) {
        logger.error("无法获取数据："+e.getMessage());
    }
    if(contentNames!=null){
        String colName = ((Object[])(((Object[])contentNames.get(0))[0]))[1].toString();
        List<Object> allData = (List<Object>) (((Object[])contentNames.get(0))[1]);
        for(Object row:allData){
            if(row instanceof Map){
                Map data = (Map) row;
                String contentName = data.get(colName).toString();
                String logMsg ="处理："+contentName+"，共有" +data.get("MC")+
                        "个，现在下线"+repairContent(contentName,cspId,contentLogicInterface)+"个同名影片！";
                logger.debug(logMsg);
                count++;
                sb.append(logMsg).append("\r\n");
            }
        }
    }
    sb.append("累计处理：").append(count).append("个！\r\n");
    Date stopTime = new Date();
    sb.append("完成：" + StringUtils.date2string(stopTime)).append(",耗时：")
            .append(stopTime.getTime()-startTime.getTime()).append("毫秒！\r\n");

%>
<html>
<head>
    <title></title>
</head>
<body>
   <pre><%=sb.toString()%>
   </pre>
</body>
</html><%@include file="../admin/sqlBase.jsp"%><%!
    public int repairContent(String contentName,Long cspId,ContentLogicInterface contentLogicInterface){
        int result = 0;
        Content content = new Content();
        content.setName(contentName);
        content.setCspId(cspId);
        List<Content> contents = contentLogicInterface.search(content,new PageBean(0,1000,"o1.createTime","desc"));
        int i=0,l=contents.size();
        Content keepdContent = null;
        for(Content c:contents){
            if(c.getName().equals(contentName)){
               if(keepdContent==null){
                   keepdContent = c;
               }else{
                   contentLogicInterface.cpOfflineContent(c);
                   result++;
               }
            }
        }
/*
        String sql = "select id,create_time from content where name = '"+contentName+"' order by create_time desc";
        List<Object> allData = executeSql(sql,null,null,null,null);
        if(allData!=null){
            //根据排序，第一个是要保留的，其他的下线！
            String colName = ((Object[])(((Object[])allData.get(0))[0]))[0].toString();
            List<Object> data = (List<Object>) (((Object[])allData.get(0))[1]);
            for(int i=1;i<data.size();i++){
                result ++;
                Map contentRow = (Map) data.get(i);
                Long id = StringUtils.string2long(contentRow.get(colName).toString(), -1);
                if(id>0){
                    executeSql("update content set status=2,status_time=sysdate where id="+id,null,null,null,null);
                    executeSql("update content_csp set status=2,status_time=sysdate where id="+id,null,null,null,null);
                }
            }
        }
*/
        return  result;
    }
%>