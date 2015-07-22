<%@ page contentType="text/html; charset=GB2312" %><%@ page import="cn.sh.guanghua.mediastack.common.ConfigManager,
                 cn.sh.guanghua.mediastack.user.SessionUser,
                 cn.sh.guanghua.mediastack.common.Constants"%><%

    //session¼ì²é
SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
String userId = "";
if (su != null){
    userId = su.getUserId();
}else{
    response.sendRedirect("error.jsp?msg=avalid_session");
    return;
}
session.setAttribute("bind_status","usercenter");
response.sendRedirect("../verify/selectAccountType.jsp?status=usercenter");
return;

%>