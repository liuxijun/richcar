<%@ page import="com.fortune.common.business.security.model.Admin" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-4-26
  Time: 19:32:03
  To change this template use File | Settings | File Templates.
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
    String agent = request.getHeader("user-agent");
    boolean isFirefox = false;
    if(agent!=null&&agent.indexOf("Firefox")>=0){
        isFirefox = true;
        //System.out.println("is firefox!!!user-agent===="+agent);
    }
    if(!isFirefox){
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd"><%
    }else{
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"><%        
    }
%>