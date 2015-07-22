<%@ page import="com.fortune.util.StringUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-9
  Time: 下午7:54
  截图
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String url = request.getParameter("url");
    long deviceId = StringUtils.string2long(request.getParameter("deviceId"),-1);
    int  pos = StringUtils.string2int(request.getParameter("pos"),10);

%>