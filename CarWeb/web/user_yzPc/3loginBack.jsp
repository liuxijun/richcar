<%@ page contentType="text/html; charset=UTF-8" %><%@ page import="com.huawei.itellin.spapi.service.*"%><%@ page
        import="java.net.URL,com.huawei.itellin.bcg.soap.*,com.ccit.cvb.communicator.serverside.soap.sp.*" %><%@include
        file="param.jsp"%><%@include file="queryString.jsp"%><%
    try{
        String result = getParameter(request,"Result","");
        String ssoToken = getParameter(request,"ssoToken","");
        String content_Id=request.getParameter("contentId");
        System.out.println("ssoToken:"+ssoToken);
        if ("0".equals(result)){
            response.sendRedirect("2login.jsp?"+queryStr);
            return;
        }

        SSOValidToken ssoValidToken = MainFactory.getSSOValidTokenWithCert(ssoToken, BCG_CERT);
        BSSPUserInfo bsspUInfo = ssoValidToken.getUserInfo();
        String puserId = bsspUInfo.getPseudoId();
        String nickName = bsspUInfo.getNickName();
        String userType = bsspUInfo.getUserPayType();
        session.setAttribute("userId", nickName);
        session.setAttribute("psUserId", puserId);
        session.setAttribute("userType", userType);
        response.sendRedirect("../../page/yzPc/detail.jsp?contentId="+content_Id+"&userId="+nickName);
//        if(!"".equals(sender)){
//            response.sendRedirect(sender+"?refresh="+System.currentTimeMillis());
//        }else{
//            response.sendRedirect("4checkAccount.jsp?"+queryStr);
//        }
        return;
                                                                           
    }catch(Exception e){
        e.printStackTrace();
    }
  %>