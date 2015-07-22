<%@ page contentType="text/html; charset=gb2312" %><%@ page
        import="com.fortune.rms.business.user.CacheLogic" %><%@ page
        import="com.fortune.rms.business.user.UserLogic" %><%@ page
        import="com.fortune.rms.business.content.model.Content" %><%@ page
        import="org.apache.log4j.Logger" %>
<%@ page import="com.huawei.itellin.spapi.service.*"%>
<%@include
        file="param.jsp"%><%@include file="queryString.jsp"%><%
    CacheLogic cacheLogic= new CacheLogic(request);
    //目前是只要播放就要登录 所以这个地方暂时不要
//    if("".equals(sender)){
//        Content content = cacheLogic.getContent(contentId);
//        UserLogic userLogic= new UserLogic(request);
//        if (userLogic.checkFree(contentId,spId,contentPropertyId)){
//            //System.out.println("免费");
//            logger.debug("影片免费：contentId:spId="+contentId+":"+spId);
//            session.setAttribute("play_free_"+contentId+"_"+contentPropertyId,"true");
////            response.sendRedirect("8player.jsp?"+queryStr);
//           //播放进入详情页面
////            response.sendRedirect("../../page/yzPc/detail.jsp?isPlay=yes");
//            session.setAttribute("isPlay","yes");
//            return;
//        }
//    }else{
//        //logger.debug("sender="+sender);
//    }

    String userId = (String)session.getAttribute("userId");
    String content_Id=request.getParameter("contentId");
    if (userId==null||userId=="null"){

        response.sendRedirect("1login.jsp?"+queryStr);
        return;
    }else{
        session.setAttribute("userId", userId);
        response.sendRedirect("../../page/yzPc/detail.jsp?contentId="+content_Id+"&userId="+userId);
    }
//    if("".equals(sender)){
//        response.sendRedirect("4checkAccount.jsp?"+queryStr);
//    }else{
//        int i=sender.indexOf("refresh");
//        if(i>0){
//            sender = sender.substring(0,i);
//        }
//        response.sendRedirect(sender+"refresh="+System.currentTimeMillis());
//    }
 %><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.HbIndex.jsp");
%>