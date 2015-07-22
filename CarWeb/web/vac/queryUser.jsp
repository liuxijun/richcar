<%@ page contentType="text/html;charset=GBK" language="java" %><%@ page
        import="java.net.URL" %><%@ page
        import="java.util.Date" %><%@ page import="com.fortune.vac.ip2phone.*" %><%@ page
        import="org.apache.axis.AxisFault" %><%@ page
        import="java.net.MalformedURLException" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="com.fortune.util.*" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.IP2PhoneLogLogicInterface" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-3-19
  Time: 上午10:49
  To change this template use File | Settings | File Templates.
--%><%
    String command=request.getParameter("command");
    String result;
    int resultCode;
    String userIp = request.getParameter("userIp");
    String userPort = request.getParameter("userPort");
    if(userIp == null){
        userIp = request.getRemoteAddr();
    }
    if(userPort == null){
        userPort = ""+request.getRemotePort();
    }
    if("ip2phone".equals(command)){
        if(userIp==null){
            result = "无法获取用户IP，不能继续！";
            resultCode = 405;
        }else{
            if(userPort==null){
                result = "没有用户端口，不能继续！";
                resultCode=406;
            }else{
                result = getUserTelphoneByIpNative(userIp,userPort,request);
                resultCode = result!=null&&"".equals(result.trim())?0:2;
            }
        }

%><%
    }else if("test".equals(command)){
%>testOnly<%
    return;
}else{
        result = "参数似乎一个都不见了"+request.getQueryString();
        resultCode=404;
    }
    logger.debug("请求来自：" +request.getRemoteAddr()+
            "，请求信息："+userIp+":"+userPort+",结果="+resultCode+","+result);
    String format = request.getParameter("format");
    if("json".equals(format)){
        Map<String,Object> json = new HashMap<String,Object>();
        Element root = XmlUtils.getRootFromXmlStr(result);
        String phone = "";
        if(root!=null){
            Node serverInfo = root.selectSingleNode("serverInfo");
            resultCode = XmlUtils.getIntValue(serverInfo,"@resultCode",-1);
            result = XmlUtils.getValue(serverInfo,"@description","");
            if(resultCode!=0){
            }
            result ="("+userIp+":"+userPort+")"+result;
            Node userInfo = root.selectSingleNode("userInfo");
            phone = XmlUtils.getValue(userInfo,"@userName","");
        }
        json.put("resultCode",resultCode);
        json.put("result",result);
        json.put("success",true);
        json.put("phone",phone);
        String callBack = request.getParameter("callBack");
        if(callBack!=null){
            out.print(callBack+"(");
        }
        out.print(JsonUtils.getJsonString(json));
        if(callBack!=null){
            out.print(");\r\n");
        }
        return;
    }else{
%><?xml version="1.0" encoding="gbk"?>
<root><result-code><%=resultCode%></result-code><result><%=result%></result></root><%
    }
%><%!
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.queryUser.jsp");
    public String getUserTelphoneByIpNative(String userIp,String userPort,HttpServletRequest httpRequest){
        String serviceId = "18000012";
        AppConfigurator config = AppConfigurator.getInstance();
        serviceId = config.getConfig("QUERY_USER_INFO_SERVICE_ID",serviceId);
        String phone = "";
        String logs = "";
        int resultCode = -999;
        String QUERY_USER_INFO_SERVICE_URL=config.getConfig("QUERY_USER_INFO_SERVICE_URL","http://61.55.156.205:8090/webservices/services/QueryUserInfoServiceApplyHttpPort");
        try {
            QueryUserInfoServiceApplyHttpPortSoapBindingStub queryInterface = new QueryUserInfoServiceApplyHttpPortSoapBindingStub(new URL(QUERY_USER_INFO_SERVICE_URL),null);
            QueryUserInfoRequestUserInfo userInfo=new QueryUserInfoRequestUserInfo(userIp,userPort,"");
            QueryUserInfoRequestServerInfo serverInfo = new QueryUserInfoRequestServerInfo(serviceId, StringUtils.date2string(new Date(), "yyyyMMddHHmmss"));
            QueryUserInfoRequest queryUserInfoRequest = new QueryUserInfoRequest(userInfo,serverInfo);
            QueryUserInfoRespone queryResponse = queryInterface.queryUserInfoServiceApply(queryUserInfoRequest);
            if(queryResponse!=null){
                resultCode = StringUtils.string2int(queryResponse.getServerInfo().getResultCode(),-1);
                if(resultCode != 0){
                    logs +=("无法获取IP，错误代码："+queryResponse.getServerInfo().getResultCode()+","+queryResponse.getServerInfo().getDescription());
                    logger.error(logs);
                    phone =  queryResponse.toString();
                }else {
                    phone = queryResponse.getUserInfo().getUserName();
                }
            }


/*
            com.fortune.vac.ip2phone.QueryUserInfoRespone queryResponse = queryInterface.queryUserInfoServiceApply(queryUserInfoRequest);

            if(queryResponse.getServerInfo().hashCode()!=0){
                logger.error("无法获取IP，错误代码："+queryResponse.getServerInfo().getResultCode()+","+queryResponse.getServerInfo().getDescription());
                return null;
            }else {
                return queryResponse.getUserInfo().getUserName();
            }
*/
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            logger.error("无法获取IP对应的手机号："+axisFault.getMessage());
        } catch (MalformedURLException e) {
            logger.error("无法获取IP对应的手机号：" + e.getMessage());
        } catch(java.rmi.RemoteException e){
            logger.error("无法获取IP对应的手机号："+e.getMessage());
        }
        HttpSession session = httpRequest.getSession();
        IP2PhoneLogLogicInterface ip2PhoneLogLogicInterface = (IP2PhoneLogLogicInterface) SpringUtils.getBean("ip2PhoneLogLogicInterface", session.getServletContext());
        ip2PhoneLogLogicInterface.createLog(httpRequest.getRemoteAddr(), "hbUnicomIp2PhoneInterface", "hbUnicomIp2PhoneInterface",phone, resultCode,logs,new Date(),
                httpRequest.getHeader("user-agent"));
        return phone;
    }
    //201304272037
%>