<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="queryString.jsp"%>
<%@include file="param.jsp"%>
<%
    String returnUrl = getParameter(request,"returnUrl","");
    String resultChecker = getParameter(request,"resultChecker","");

    String userAccount = getParameter(request,"userAccount","");
    String password = getParameter(request,"password","");
    System.out.println("userAccount-->"+userAccount);
    System.out.println("password-->"+password);

    if(userAccount != null && !userAccount.trim().isEmpty()) {
        if(password != null && ! password.trim().isEmpty()) {
            if("dujin".equals(userAccount) && password.equals("175571")) {
                session.setAttribute("userId",userAccount);
                response.sendRedirect(resultChecker);
                return;
            }
        }
    }
    response.sendRedirect(returnUrl);
    return;
%>