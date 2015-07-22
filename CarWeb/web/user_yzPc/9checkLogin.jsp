<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-5-14
  Time: 下午1:25
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
%>
//用户未登录，需要进行用户的认证
///*
    window.top.location.href="/user/index.jsp?sender="+encodeURI(encodeURI(window.top.location.href));
//*/
<%
    }
%>