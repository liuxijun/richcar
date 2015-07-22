<%@ page import="com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.publish.model.Channel" %><%@ page
        import="com.fortune.util.MD5Utils" %><%@ page
        import="com.fortune.rms.business.user.model.UserLogin" %><%@ page
        import="java.util.Date" %><%@ page
        import="com.fortune.rms.business.user.logic.logicInterface.UserLoginLogicInterface" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2014/11/24
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userName = request.getParameter("user");
    String pwd = request.getParameter("pwd");
    String message = "未输入任何参数！";
    if(userName!=null&&pwd!=null&&!"".equals(userName.trim())&&!"".equals(pwd)){
        FrontUserLogicInterface frontUserLogicInterface=(FrontUserLogicInterface) SpringUtils.getBean(
                "frontUserLogicInterface", session.getServletContext());
        FrontUser u = new FrontUser();
        u.setUserId(userName);
        List<FrontUser> l = frontUserLogicInterface.search(u);
        if(l!=null&&l.size()>0){
            u = l.get(0);
            if(u.getUserId().equals(userName)&&u.getPassword().equals(MD5Utils.getMD5String(pwd))){
                String url = request.getParameter("nextUrl");
                if(url==null){
                    url = "http://" +request.getServerName()+":"+request.getServerPort()+
                            "/index.html";
                }
                if( FrontUser.USER_STATUS_NORMAL.equals(u.getStatus())){
                    // 登录
                    session.setAttribute(Constants.SESSION_FRONT_USER, u);
                    // 解析用户可以观看的所有频道，保存在Session中
                    session.setAttribute(Constants.SESSION_FRONT_USER_CHANNEL, frontUserLogicInterface.getUserAuthorizedChannel( u) );
                    //解析用户可以观看的一级频道,保存在Session中
                    List<Channel> visibleChannelList = frontUserLogicInterface.
                            getTopLevelChannel(frontUserLogicInterface.getUserAuthorizedChannel( u));
                    session.setAttribute(Constants.SESSION_FRONT_USER_TOP_CHANNEL,visibleChannelList);
                    response.sendRedirect(url);
                    UserLoginLogicInterface userLoginLogicInterface = (UserLoginLogicInterface)SpringUtils.getBean("userLoginLogicInterface",session.getServletContext());
                    userLoginLogicInterface.save(new UserLogin(-1,u.getUserId(),new Date(),
                            UserLoginLogicInterface.LOGIN_STATUS_FROM_SMS_VERIFY_CODE,request.getRemoteAddr(),u.getName(),
                            "用户通过一键登录接口登录",u.getOrganizationId()));
                    return;
                }else{
                    message=("您的账号被锁定，请联系管理员！");
                }
            }else{
                message = "错误的帐号或者口令！请查证后再试试！";
            }
        }else{
            message = "错误的帐号或者口令！请查证后再试试！^o^";
        }
    }else{

    }
%><html>
<head>
    <title>潍柴网络电视台温馨提示：登录异常</title>
</head>
<body>
<br/><br/>
<p align="center">
    <%=message%>
</p>

</body>
</html>
