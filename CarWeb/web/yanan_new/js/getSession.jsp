<%@ page import="com.fortune.common.Constants" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    FrontUser user = (FrontUser)(session.getAttribute(Constants.SESSION_FRONT_USER));
    String result = null;
    if(user!=null){
        result = "{userId:'"+user.getUserId()+"',name:'" +user.getName()+"'}";
    }
    out.println("var user ="+result+";");

%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.getSession.jsp");
%>