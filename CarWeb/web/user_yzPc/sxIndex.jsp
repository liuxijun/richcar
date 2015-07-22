<%@ page contentType="text/html; charset=GBK" %><%@page
        import="java.sql.PreparedStatement,
                java.sql.Connection,
                java.sql.ResultSet" %><%@ page
        import="com.fortune.rms.business.user.UserLogic" %><%@ include file="sxCookies.jsp" %><%@include
        file="queryString.jsp" %><%@ page import="com.fortune.rms.business.user.SessionUser" %><%@ page
        import="com.fortune.common.Constants" %>
<%@ page import="org.apache.log4j.Logger" %><%
    try {
        Logger logger = Logger.getLogger("com.fortune.rms.jsp.sxIndex.jsp");
        logger.debug("sxIndex.jsp正在访问....");
        /**
         * 本页面的任务有二：
         * 一是读Cookie中的信息、
         * 二是根据读到的结果显示不同的内容
         */

        /*  本页面的任务之一：读Cookie中的信息：userInfo、sessionId、bindingId */
        String userInfo = getCookieValue(request, "userInfo");
        String sessionId = getCookieValue(request, "sessionID");
        String bingindId = getCookieValue(request,  "bindingID");

        SessionUser su = new SessionUser();
        su.setUserInfo(userInfo);
        su.setSessionId(sessionId);

        UserLogic userLogic = new UserLogic(request);
        //检查ip是否合法
        String ip = request.getRemoteAddr();
        //String ip = "61.182.140.0";
        logger.debug("正在检查用户IP:"+ip);
        boolean isValidIp = userLogic.checkIp(ip);
        if (false) {
            logger.debug("用户"+ip+"不是我们系统的用户！");
            response.sendRedirect("account/error.jsp?msg=98");
            return;
        }


        //得到用户id
//    su.setUserId(userId);
        su.setUserId(userLogic.getSubString(userInfo, "|", 5));
        session.setAttribute(Constants.SESSION_USER, su);
        logger.debug("userInof:"+userInfo);
        logger.debug("sessionId:"+sessionId);
        if (userInfo != null
                && !userInfo.equals("")
                && !userInfo.equals("null")
                && sessionId != null
                && !sessionId.equals("")
                && !sessionId.equals("null")) {
            response.sendRedirect("sx2CheckAccount.jsp?" + queryStr);
            return;
        } else {
            String returnUrl = "sxIndex.jsp?" + queryStr;
            response.sendRedirect("sx1Login.jsp?return_url=" + java.net.URLEncoder.encode(returnUrl));
            return;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
%>