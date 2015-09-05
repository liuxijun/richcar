<%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.util.JsonUtils" %><%@ page
        import="com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.*" %><%@ page import="com.fortune.cars.business.cars.model.Car" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/9/5
  Time: 21:24
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Map<String,Object> result = new HashMap<String, Object>();
    boolean success=false;
    String message = "";
    String phone = request.getParameter("phone");
    if(phone==null){
        phone = request.getParameter("userId");
    }
    String token = request.getParameter("token");
    if(token==null||token.isEmpty()||phone==null||phone.isEmpty()){
        message = "输入参数不够，缺少必要的参数：phone或token："+request.getQueryString();
    }else{
        if(verifyToken(token,phone)){
            String command = request.getParameter("command");
            if("changePwd".equals(command)){
                CarLogicInterface carLogicInterface = (CarLogicInterface) SpringUtils.getBean("carLogicInterface",session.getServletContext());
                String oldPwd = request.getParameter("oldPwd");
                String newPwd = request.getParameter("newPwd");
                message = carLogicInterface.updatePwd(phone,oldPwd,newPwd);
                if(message == null){
                    message = "口令修改成功！";
                    success = true;
                }else{
                    success = false;
                }
            }else{
                message = "尚未支持的命令："+command;
            }
        }
    }
    result.put("success",success);
    result.put("message",message);
    out.print(JsonUtils.getJsonString(result));
%><%@include file="utilsToken.jsp"%>