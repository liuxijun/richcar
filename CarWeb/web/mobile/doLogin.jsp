<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fortune.util.JsonUtils" %>
<%@ page import="com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface" %>
<%@ page import="com.fortune.util.SpringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/8/31
  Time: 6:41
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userId=request.getParameter("userId");
    String pwd =  request.getParameter("pwd");
    Map<String,Object> result = new HashMap<String,Object>();
    String msg =null;
    boolean success=false;
    if(userId==null||userId.equals("")||pwd==null||"".equals(pwd.trim())){
        msg = "没有输入有效的登录id和口令："+userId+","+pwd;
    }else{
        //输入的是手机号码
        CarLogicInterface carLogicInterface = (CarLogicInterface) SpringUtils.getBean("carLogicInterface",session.getServletContext());
        success = carLogicInterface.login(userId,pwd);
    }
    result.put("success",success);
    result.put("msg",msg);
    out.print(JsonUtils.getJsonString(result));
%>