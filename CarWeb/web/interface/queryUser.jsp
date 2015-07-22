<%@ page import="java.util.Date" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="java.util.Enumeration" %><%@ page
        import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-7-10
  Time: 下午5:34
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    String userIp = request.getParameter("userIp");
    String userPort = request.getParameter("userPort");
    if(userPort==null){
        userPort = ""+request.getRemotePort();
    }
    if(userIp == null){
        userIp = request.getRemoteAddr();
    }
    Map<String,String> headers = new HashMap<String, String>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while(headerNames.hasMoreElements()){
        String headerName = headerNames.nextElement();
        headers.put(headerName,request.getHeader(headerName));
    }
%><?xml version="1.0" encoding="gbk"?>
<result startTime="<%=StringUtils.date2string(startTime)%>">
    <ip><%=userIp%></ip>
    <port><%=userPort%></port>
    <date><%=StringUtils.date2string(new Date())%></date>
    <%
        for(String headerName:headers.keySet()){
        out.println("\t<"+headerName+">"+headers.get(headerName)+"</"+headerName+">");
        }
    %>
</result>
<%!
    private Logger logger = Logger.getLogger("com.fortune.jsp.queryUserServer.jsp");
%>