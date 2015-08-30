<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/7/22
  Time: 8:43
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
  String userAgent = request.getHeader("user-agent");
  if(userAgent == null){
    userAgent = "";
  }
  if(userAgent.contains("iOS")||userAgent.contains("Android")){
    response.sendRedirect("mobile/car.html");
    return;
  }else{
    response.sendRedirect("login.jsp");
  }
%>