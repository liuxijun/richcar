<%@ page contentType="text/html; charset=UTF-8" %>
<%@include file="param.jsp"%>
<%
    try{

        String ip = getParameter(request,"ip","");
        String userId = (String)session.getAttribute("userId");
        if (userId==null){
            response.sendRedirect("ssoToken.jsp?return_url="+java.net.URLEncoder.encode(ccitCheckLoginBack));
            return;
        }

        out.println(userId+"用户已经登陆");
    }catch(Exception e){
        e.printStackTrace();
    }
%>