<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %><%@ page import="java.util.HashMap" %>
<%@ page import="com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface" %>
<%@ page import="com.fortune.util.*" %>
<%@ page import="com.fortune.common.business.security.model.Admin" %>
<%@ page import="com.fortune.common.Constants" %>
<%@ page import="com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-6-21
  Time: 下午5:22
  获取拉动推荐播放连接
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Admin admin =(Admin) session.getAttribute(Constants.SESSION_ADMIN);
    if(admin==null){
        out.println("{error:'sessionTimeout',yourIp:'"+request.getRemoteAddr()+"'}");
        return;
    }
    boolean success = false;
    long contentId = StringUtils.string2long(request.getParameter("contentId"),-1);
    String result = "";
    if(contentId>0){
        try {
            String time=StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
            String token = MD5Utils.getMD5String(contentId+time+ AppConfigurator.getInstance().getConfig("system.password.pullPwd","fortune!@#456")).substring(16);
            result = "http://tv.inhe.net/page/hbMobile/detail4phone.jsp?contentId="+contentId+"&time="+time+"&key="+token;
            success = true;
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBean("systemLogLogicInterface",session.getServletContext());
            ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface",session.getServletContext());
            Content content = contentLogicInterface.get(contentId);
            systemLogLogicInterface.saveLog(request.getRemoteAddr(),admin,"getPullUrls.jsp","获取拉动连接：contentId="+contentId+","+content.getName());
        } catch (Exception e) {
            result ="无法获取数据："+e.getMessage();
        }
    }
    Map<String,Object> map = new HashMap<String,Object>();
    map.put("success",success);
    map.put("msg",result);
    out.print(JsonUtils.getJsonString(map));
%>