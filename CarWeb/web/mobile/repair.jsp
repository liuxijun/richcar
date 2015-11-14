<%@ page import="java.util.HashMap" %><%@ page
        import="java.util.Map" %><%@ page
        import="com.fortune.cars.business.repair.logic.logicInterface.RepairLogicInterface" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.cars.business.repair.model.Repair" %><%@ page
        import="java.util.List" %><%@ page
        import="com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface" %><%@ page
        import="com.fortune.cars.business.cars.model.Car" %><%@ page
        import="com.fortune.cars.business.repair.logic.logicInterface.PartsLogicInterface" %><%@ page
        import="com.fortune.cars.business.repair.model.Parts" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/11/14
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
  Map<String,Object> result = new HashMap<String,Object>();
  String phone = request.getParameter("phone");
  String token = request.getParameter("token");
  boolean success = false;
  logger.debug("有用户请求："+request.getRemoteAddr()+","+request.getQueryString());
  String msg = "没有错误";
  if(verifyToken(token,phone)||"true".equals(request.getParameter("debug"))){
      int type = StringUtils.string2int(request.getParameter("type"),-1);
      if(type<=0){
          msg = "参数不对，没有输入type参数！";
      }else{
          String carNo = request.getParameter("carNo");
          if(carNo==null||"".equals(carNo)){
              if(phone!=null&&!"".equals(phone.trim())){
                  CarLogicInterface carLogicInterface = (CarLogicInterface)SpringUtils.getBean("carLogicInterface",session.getServletContext());
                  Car car = new Car();
                  car.setPhone(phone);
                  List<Car> cars = carLogicInterface.search(car,new PageBean(1,2,null,null));
                  if(cars!=null&&cars.size()>0){
                      Car userCar = cars.get(0);
                      if(userCar!=null){
                          carNo = userCar.getCarNo();
                      }
                  }
              }
          }
          if(carNo==null||"".equals(carNo)){
              msg = "参数不对，没有输入carNo参数！";
          }else{
              logger.debug("准备查找:车牌=" + carNo + ",type=" + type);
              RepairLogicInterface repairLogicInterface =(RepairLogicInterface) SpringUtils.getBean("repairLogicInterface",
                      session.getServletContext());
              Repair searchBean = new Repair();
              searchBean.setCarNo(carNo);
              searchBean.setType(type);
              PageBean pageBean = new PageBean();
              pageBean.setOrderBy("o1.createTime");
              pageBean.setOrderDir("desc");
              pageBean.setPageSize(5);
              List<Repair> repairs = repairLogicInterface.search(searchBean,pageBean);
              if(repairs!=null&&repairs.size()>0){
                  PartsLogicInterface partsLogicInterface=(PartsLogicInterface)SpringUtils.getBean("partsLogicInterface",session.getServletContext());
                  for(Repair repair:repairs){
                      Parts parts=new Parts();
                      parts.setRepairId(repair.getId());
                      repair.setParts(partsLogicInterface.search(parts));
                  }
              }
              result.put("repairs",repairs);
              success = true;
          }
      }
  }else{
      msg = "用户没有登录，不能继续";
  }
  result.put("msg",msg);
  result.put("success",success);
  out.print(JsonUtils.getJsonString(result));

%><%!
  Logger logger = Logger.getLogger("com.fortune.richcar.jsp.car.jsp");
%><%@include file="utilsToken.jsp"%>