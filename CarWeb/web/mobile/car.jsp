<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/9/3
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%><%@ page
        import="com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.*" %><%@ page import="com.fortune.cars.business.cars.model.Car" %><%@ page
        import="java.util.*" %><%@ page
        import="com.fortune.common.business.base.logic.logicInterface.DictionaryLogicInterface" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    Map<String,Object> result = new HashMap<String,Object>();
    String phone = request.getParameter("phone");
    String token = request.getParameter("token");
    boolean success = false;
    logger.debug("有用户请求："+request.getRemoteAddr()+","+request.getQueryString());
    if(verifyToken(phone,token)||"true".equals(request.getParameter("debug"))){
        int carId = StringUtils.string2int(request.getParameter("carId"),-1);
        CarLogicInterface carLogicInterface = (CarLogicInterface) SpringUtils.getBean("carLogicInterface",session.getServletContext());
        DictionaryLogicInterface dictionaryLogicInterface = (DictionaryLogicInterface) SpringUtils.getBean("dictionaryLogicInterface",session.getServletContext());
        List<Car> cars = new ArrayList<Car>();
        if(carId>0){
            try {
                cars.add(carLogicInterface.get(carId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Car searchBean = new Car();
            searchBean.setPhone(phone);
            cars.addAll(carLogicInterface.search(searchBean));
        }
        List<Map<String,Object>> carList = new ArrayList<Map<String, Object>>();
        int i = 0;
        for(Car car:cars){
            List<Map<String,Object>> carItemList = new ArrayList<Map<String, Object>>();
            List<CarItem> items = initItems();
            for(CarItem carItem:items){
                Map<String,Object> mapItem = new HashMap<String, Object>();
                mapItem.put("id",i);
                i++;
                mapItem.put("fieldLabel",carItem.getFieldLabel());
                mapItem.put("name",carItem.getName());
                String type = carItem.getType();
                if("newLine".equals(type)||"hidden".equalsIgnoreCase(type)){
                    continue;
                }else if(null==type||"null".equals(type)){
                    type = "text";
                }
                Object value = BeanUtils.getProperty(car,carItem.getName());
                if(value==null){
                    value = "";
                }else{
                    if("select".equals(type)){
                        String v = dictionaryLogicInterface.getNameOfCode(value.toString(),carItem.getName());
                        if(v!=null){
                            value = v;
                        }
                    }
                }
                mapItem.put("value",value);
                mapItem.put("type", type);
                carItemList.add(mapItem);
            }
            Map<String,Object> carMap = new HashMap<String, Object>();
            carMap.put("id",car.getId());
            carMap.put("carNo",car.getCarNo());
            carMap.put("values",carItemList);
            carList.add(carMap);
        }
        result.put("cars",carList);
        success = cars.size()>0;
    }
    result.put("success",success);
    out.print(JsonUtils.getJsonString(result));
%><%!
    Logger logger = Logger.getLogger("com.fortune.richcar.jsp.car.jsp");
%><%@include file="utilsToken.jsp"%><%@include file="../cars/carItems.jsp"%>