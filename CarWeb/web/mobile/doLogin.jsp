<%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="com.fortune.cars.business.cars.logic.logicInterface.CarLogicInterface" %><%@ page
        import="java.security.NoSuchAlgorithmException" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/8/31
  Time: 6:41
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userId=request.getParameter("userId");
    String pwd =  request.getParameter("pwd");
    String token = request.getParameter("token");
    Map<String,Object> result = new HashMap<String,Object>();
    logger.debug("有用户发起了登录请求，用户来自："+request.getRemoteAddr()+","+request.getHeader("user-agent"));
    String msg =null;
    boolean success=false;
    if(token!=null){
        if(verifyToken(token,userId)){
            success = true;
            result.put("token",createToken(userId));
        }else{
            msg = "Token校验失败";
        }
    }else{
        if(userId==null||userId.equals("")||pwd==null||"".equals(pwd.trim())){
            msg = "没有输入有效的登录id和口令："+userId+","+pwd;
        }else{
            //输入的是手机号码
            CarLogicInterface carLogicInterface = (CarLogicInterface) SpringUtils.getBean("carLogicInterface",session.getServletContext());
            success = carLogicInterface.login(userId,pwd);
            if(success){
                result.put("token",createToken(userId));
                msg = "登录请求通过！";
            }else{
                msg="登录请求被拒绝：userId="+userId+",pwd="+pwd;
            }
        }
    }
    logger.debug("登录结果："+success+",msg="+msg);
    result.put("userId",userId);
    result.put("success",success);
    result.put("msg",msg);
    out.print(JsonUtils.getJsonString(result));
%><%!
    Logger logger = Logger.getLogger("com.fortune.richcar.jsp.doLogin.jsp");
    public boolean verifyToken(String token,String phone){
        String pwd = AppConfigurator.getInstance().getConfig("mobileTokenPassword","fortune!@#456");
        boolean result = false;
        if(token!=null&&phone!=null&&pwd!=null){
            if(token.length()==46){
                String timeStr = token.substring(0,14);
                String lastToken = token.substring(14);
                logger.debug("lastToken="+lastToken+",timeStr="+timeStr);
                try {
                    String calToken = MD5Utils.getMD5String(timeStr + phone + pwd);
                    if(lastToken.equals(calToken)){
                        result = true;
                    }else{
                        logger.error("校验token失败："+calToken+"!="+lastToken);
                    }
                } catch (NoSuchAlgorithmException e) {
                }
            }else{
                logger.error("Token长度异常："+token.length()+"!=46");
            }
        }else{
            logger.error("参数缺失：token="+token+",phone="+phone+",pwd="+pwd);
        }
        return result;
    }
    public String createToken(String phone){
        String pwd = AppConfigurator.getInstance().getConfig("mobilePhoneTokenPassword","fortune!@#456");
        String timeStr = StringUtils.date2string(new Date(), "yyyyMMddHHmmss");
        try {
            return timeStr+MD5Utils.getMD5String(timeStr+phone+pwd);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
%>