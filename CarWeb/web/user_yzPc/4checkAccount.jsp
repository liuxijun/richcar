<%@ page contentType="text/html; charset=gb2312" %><%@ page
        import="com.fortune.rms.business.user.UserLogic" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@include
        file="param.jsp"%><%@include file="queryString.jsp"%><%
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
        response.sendRedirect("1login.jsp?"+queryStr);
        return;
    }

    userId = (String)session.getAttribute("psUserId");
    UserLogic userLogic= new UserLogic(request);

   
    if (userLogic.checkAccount(userId, contentId, contentPropertyId, spId)
        || userLogic.checkCcit(userId, contentId, spId, platformId, SP_PRIVATE_KEY, bsspExtendServiceAddress,productIdss)
            ){
        session.setAttribute("play_"+contentId+"_"+contentPropertyId,"true");
        response.sendRedirect("8player.jsp?"+queryStr);
        return;
    }
    if (userLogic.checkAccount(userId, contentId, contentPropertyId, spId)){
        session.setAttribute("play_"+contentId+"_"+contentPropertyId,"true");
        response.sendRedirect("8player.jsp?"+queryStr);
        return;
    }


    response.sendRedirect("5buylist.jsp?"+queryStr);
    return;
%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.HbCheckAccount.jsp");
%>