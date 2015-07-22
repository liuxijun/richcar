<%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="java.util.List" %><%@ page
        import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%@ page import="com.fortune.util.PageBean" %>
<%@ page import="com.fortune.util.JsonUtils" %>

<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/10/16
  Time: 11:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.page.js.top10.jsp");
    logger.debug("用户请求："+request.getRemoteAddr()+",开始初始化......");

    ContentLogicInterface contentLogic = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface", session.getServletContext());

    Content searchBean = new Content();
    List<Content> top10 = contentLogic.search(searchBean,new PageBean(0,10,"o1.allVisitCount","desc"));
    logger.debug("查询排行完成，准备输出");
    String callback = request.getParameter("callback");
    if(callback==null){
        out.println("var top10Contents=");
    }else{
        out.println("\r\n"+callback+"(");
    }
    out.print(JsonUtils.getJsonString(top10));
    if(callback==null){
        out.println(";");
    }else{
        out.println(");");
    }
%><%@include file="jsUtils.jsp"%>