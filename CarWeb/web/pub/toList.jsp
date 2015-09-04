<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-20
  Time: 15:48:28
  
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="s" uri="/struts-tags" %><%
    int pageIndex = Integer.parseInt(session.getAttribute("redirect_index").toString());
    switch(pageIndex){
        case 1:
            response.sendRedirect("../pub/pubList.jsp");
            break;
        case 2:
            response.sendRedirect("../man/contentMan.jsp");
            break;
        case 3:
            response.sendRedirect("../pub/channelContent.jsp");
            break;
        default:
            response.sendRedirect("../pub/pubList.jsp");
    }
%>