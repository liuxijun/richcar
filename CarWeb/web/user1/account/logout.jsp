<%@ page contentType="text/html;charset=gb2312"%>
<%@ page language="java" %>
<%@ page import = "com.runway.race.ipm.*,
                   cn.sh.guanghua.mediastack.user.SessionUser,
                   cn.sh.guanghua.mediastack.common.ConfigManager" %><%
    /* 
     * 这个页面做三件事：
     *                 通知ICP服务器清除服务器上的用户登录信息
     *                 清除当前IE窗口中的ICP域内的cookie
     *                 将用户退出登录的操作通知CNUS服务器
     */
    
    /* 从cookie中读bingdingId信息,如果存在就做以下的三件事 */
    SessionUser su = new SessionUser();
    String bindingID = su.getBindingId();
	if (null != bindingID && !bindingID.equals("") && !bindingID.equalsIgnoreCase("null") ) {
	    //通知ICP服务器清除服务器上的用户登录信息
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	icpLoginInfoContainer.removeLoginInfo(bindingID);
		
		//清除当前IE窗口中的ICP域内的cookie
		su.setBindingId("");
        su.setSessionId("");
        su.setSessionId("");
				
	    //将用户退出登录的操作通知CNUS服务器
        String ispLogoutURL = ConfigManager.getConfig().node("account").get("ispLogoutURL","");
		response.sendRedirect(ispLogoutURL);
	}
	
%>

