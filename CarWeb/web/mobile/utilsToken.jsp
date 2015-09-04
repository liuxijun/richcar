<%@ page import="java.security.NoSuchAlgorithmException" %><%@ page import="com.fortune.util.MD5Utils" %><%@ page import="java.util.Date" %><%@ page import="com.fortune.util.StringUtils" %><%@ page import="com.fortune.util.AppConfigurator" %><%@ page import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/9/3
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%!
  Logger __logger4tokenUtils = Logger.getLogger("com.fortune.car.utilsToken.jsp");
  public boolean verifyToken(String token,String phone){
    String pwd = AppConfigurator.getInstance().getConfig("mobileTokenPassword","fortune!@#456");
    boolean result = false;
    if(token!=null&&phone!=null&&pwd!=null){
      if(token.length()==46){
        String timeStr = token.substring(0,14);
        String lastToken = token.substring(14);
        __logger4tokenUtils.debug("lastToken="+lastToken+",timeStr="+timeStr);
        try {
          String calToken = MD5Utils.getMD5String(timeStr + phone + pwd);
          if(lastToken.equals(calToken)){
            result = true;
          }else{
            __logger4tokenUtils.error("校验token失败："+calToken+"!="+lastToken);
          }
        } catch (NoSuchAlgorithmException e) {
        }
      }else{
        __logger4tokenUtils.error("Token长度异常：token的长度是"+token.length()+"，不是46,token="+token);
      }
    }else{
      __logger4tokenUtils.error("参数缺失：token="+token+",phone="+phone+",pwd="+pwd);
    }
    return result;
  }
  public String createToken(String phone){
    String pwd = AppConfigurator.getInstance().getConfig("mobilePhoneTokenPassword","fortune!@#456");
    String timeStr = StringUtils.date2string(new Date(), "yyyyMMddHHmmss");
    try {
      return timeStr+ MD5Utils.getMD5String(timeStr + phone + pwd);
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }
%>