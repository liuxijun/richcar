<%@ page contentType="text/html;charset=gb2312"%>
<%@ page language="java" %>
<%@ page import = "com.runway.race.ipm.*,
                   cn.sh.guanghua.mediastack.common.ConfigManager" %><%@
 include file="./cookies.inc" %><%@ include file="./fj.inc" %>
<%
    /*
     * 本页面主要做三件事：
     * 读取请求中的参数
     * 如果有参数就将用户的登录信息通知服务器来存放用户登录的信息      *
     */
     
    // 读取请求中的参数，从SsoLogin接口传来
	String userInfo = request.getParameter("userInfo");
	String bindingId = request.getParameter("bindingId");
	String sessionId = request.getParameter("sessionId");
    //String resultChecker = ConfigManager.getConfig().node("account").get("resultChecker","");

	// 判断是否有有效的用户登录的参数
	if  (userInfo != null && !userInfo.equals("") && !userInfo.equals("null")
	     && bindingId != null && !bindingId.equals("") && !bindingId.equals("null")
	     && sessionId != null && !sessionId.equals("") && !sessionId.equals("null")){
	     
	    // 如果有参数就将用户的登录信息通知服务器来存放用户登录的信息
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	icpLoginInfoContainer.setLoginInfo(userInfo, sessionId, bindingId);

        //将用户重定向至resultChecker，带参数bindingId
        //response.sendRedirect(resultChecker +"bindingId="+bindingId);
	} else {
	    out.println("Failed to write Cookie for param losting!");
	}
%>