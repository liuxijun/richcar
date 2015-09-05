<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/9/5
  Time: 8:13
  To change this template use File | Settings | File Templates.
  返回用户车辆检查信息
--%><%@ page
        import="com.fortune.cars.business.conduct.logic.logicInterface.ConductLogicInterface" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.*" %><%@ page import="com.fortune.cars.business.conduct.model.Conduct" %><%@ page
        import="java.util.*" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
  Map<String,Object> result = new HashMap<String,Object>();
  String phone = request.getParameter("phone");
  String token = request.getParameter("token");
  boolean success = false;
  String msg;
  logger.debug("有用户请求："+request.getRemoteAddr()+","+request.getQueryString());
  List<Conduct> conducts = new ArrayList<Conduct>();
  if(verifyToken(token,phone)||"true".equals(request.getParameter("debug"))){
    ConductLogicInterface conductLogicInterface = (ConductLogicInterface) SpringUtils.getBean("conductLogicInterface",session.getServletContext());
    String command = request.getParameter("command");
    int carId = StringUtils.string2int(request.getParameter("carId"), -1);
    if("list".equals(command)){
      if(carId>0){
        Conduct conduct = new Conduct();
        conduct.setCarId(carId);
        conducts = conductLogicInterface.search(conduct);
        msg = "成功获取一组数据！";
      }else{
        msg = "没有输入足够的参数：carId！"+request.getQueryString();
      }
    }else if("view".equals(command)){
      int conductId = StringUtils.string2int(request.getParameter("conductId"),-1);
      if(conductId>0){
        try {
          Conduct conduct = conductLogicInterface.get(conductId);
          conduct.setItems(conductLogicInterface.getItems(conductId,carId));
          conducts.add(conduct);
          msg = "成功获取一个数据";
        } catch (Exception e) {
          msg = "无法获取指定的检查结果："+e.getLocalizedMessage()+","+request.getQueryString();
        }
      }else{
        msg = "没有输入足够的参数：conductId！"+request.getQueryString();
      }
    }else{
      msg = "命令不能被识别："+command;
    }
    result.put("conducts",conducts);
    success = conducts.size()>0;
  }else{
    msg = "用户登录信息过期！需要重新登录！";
  }
  result.put("success",success);
  result.put("msg",msg);
  logger.debug("处理结果："+msg);
  out.print(JsonUtils.getJsonString(result));
%><%!
  Logger logger = Logger.getLogger("com.fortune.richcar.jsp.car.jsp");
%><%@include file="utilsToken.jsp"%>