<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-6-13
  Time: 10:32:00
  投票问卷列表
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.JsonUtils" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %><%
    int start = StringUtils.string2int(request.getParameter("start"), 0);
    int limit = StringUtils.string2int(request.getParameter("limit"),10);
    long channelId = StringUtils.string2long(request.getParameter("channelId"),474431645L);
    if(limit<=0){
        limit = 10;
    }

    String callBackFunction = request.getParameter("callback");
    String listData;

    PageBean pageBean = new PageBean((start+limit)/limit,limit,request.getParameter("orderBy"),request.getParameter("orderDir"));
    ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
    List<Content> list = contentLogicInterface.getContentsByChannelId(channelId,pageBean);
    listData = JsonUtils.getListJsonString("objs",list,"total",pageBean.getRowCount());
    if(callBackFunction!=null){
        out.println("\r\n"+callBackFunction+"(");
    }


%>{
"listData":<%=listData%>
}<%
    if(callBackFunction!=null){
        out.print(");");
    }
%>