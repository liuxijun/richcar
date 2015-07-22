<%@ page contentType="text/html;charset=gb2312"%><%@ page language="java" %><%@ page
        import = "com.runway.race.ipm.*" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@include file="../sxCookies.jsp"%><%
    /* 
     * 这个页面做三件事：
     *                 通知ICP服务器清除服务器上的用户登录信息
     *                 清除当前IE窗口中的ICP域内的cookie
     *                 将用户退出登录的操作通知CNUS服务器
     */
    
    /* 从cookie中读bingdingId信息,如果存在就做以下的三件事 */
//    SessionUser su = new SessionUser();
    String bindingID =getCookieValue(request, "bindingID");
	if (null != bindingID && !bindingID.equals("") && !bindingID.equalsIgnoreCase("null") ) {
	    //通知ICP服务器清除服务器上的用户登录信息
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	icpLoginInfoContainer.removeLoginInfo(bindingID);
		
		//清除当前IE窗口中的ICP域内的cookie
/*
		su.setBindingId("");
        su.setSessionId("");
        su.setSessionId("");
				
*/
	    //将用户退出登录的操作通知CNUS服务器
        String ispLogoutURL = AppConfigurator.getInstance().getConfig("account.ispLogoutURL","");
		response.sendRedirect(ispLogoutURL);
	}
	
%>

