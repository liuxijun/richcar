<%@ page import="cn.sh.guanghua.mediastack.user.SessionUser,
                 cn.sh.guanghua.mediastack.common.Constants"%><%
    //session¼ì²é
    SessionUser su = (SessionUser)session.getAttribute(Constants.SESSION_USER);
    String userId = "";
    if (su != null){
        userId = su.getUserId();
    }else{
        response.sendRedirect("../Login.jsp?msg=session_unvalid");
        return;
    }
%>