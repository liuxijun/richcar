<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-6-15
  Time: 15:12:07
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.runway.race.ipm.Ipm" %><%@ page
        import="com.runway.race.ipm.IpmImpl" %><%@ page
        import="com.fortune.rms.business.user.SessionUser" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.runway.race.ipm.IpmResult" %><%
    String errorInfo = "ipmFlag为空";

    int resultID = -1;
    Ipm ipm = new IpmImpl();
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    //String userId = "";
    String userInfo ;
    String sessionId  ;
    String clientIp = request.getRemoteAddr();
    boolean  result = false;
    logger.debug(request.getQueryString());
    String ipmFlag = request.getParameter("ipmFlag");
    if(su==null){
        userInfo = request.getParameter("userInfo");
        sessionId = request.getParameter("userSessionId");
    }else{
        userInfo = su.getUserInfo();
        sessionId = su.getSessionId();
    }
    if (sessionId != null){
        //userId = su.getUserId();
        String fileID =request.getParameter("fileID");
        if(fileID!=null){
            fileID = java.net.URLDecoder.decode(fileID,"UTF-8");
        }
        String rootCategoryID = request.getParameter("rootCategoryID");

        if(ipmFlag!=null){
            try {
                logger.debug("准备通知润汇：rootCategoryID="+rootCategoryID+",fileID="+fileID+",ipmFlag="+ipmFlag+",clientIp="+clientIp);
                IpmResult ir = ipm.AccountProcessFree(userInfo,sessionId,
                        rootCategoryID,fileID,ipmFlag,clientIp);
                resultID = ir.getResultID();
                errorInfo = ir.getErrorInfo();
                result = true;
            } catch (Exception e) {
                logger.error("通知润汇发生异常："+e.getMessage());
                result = false;
                errorInfo = e.getMessage();
                resultID = 501;
            }
            if(errorInfo!=null){
                errorInfo = errorInfo.replace('\'','"');
                errorInfo = errorInfo.replaceAll("\r","\\\r");
                errorInfo = errorInfo.replaceAll("\n","\\\n");
            }else{
                errorInfo = "没有任何返回值";
            }
        }else{
            logger.debug("ipmFlag为空，不用发送消息："+request.getQueryString());
        }
    }else{
        //访客无权观看
        logger.debug("用户没登陆，不用发送消息："+request.getQueryString());
        errorInfo = "session timeout!";
        resultID = 500;
    }
    StringBuffer resultBuf = new StringBuffer();
    resultBuf.append("{\r\nsuccess:").append(result).append(",\r\nresult:")
            .append(resultID).append(",\r\nerrorInfo:'").append(errorInfo).append("',\r\n");
    resultBuf.append("userId:'").append(userInfo).append("',sessionId:'").append(sessionId).append("'}\r\n");
    logger.debug("返回给客户端消息：\r\n"+resultBuf.toString());
%><%=resultBuf.toString()%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.noticeRunway.jsp");
%>