<%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="java.util.UUID" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.wo.sdk.message.APIRequest" %><%@ page
        import="com.wo.sdk.message.AuthorizationHeader" %><%@ page
        import="com.wo.sdk.openapi.auth.domain.GetTokenResponse" %><%@ page
        import="com.wo.sdk.openapi.auth.impl.AuthenticateAPIImpl" %><%@ page
        import="com.wo.sdk.openapi.net.impl.NetAPIimpl" %><%@ page
        import="com.wo.sdk.openapi.net.domain.GetphonenumberResponse" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-7-12
  Time: 下午2:13
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date startTime = new Date();
    logs="";
    String uniKey = request.getParameter("unikey");
    if(uniKey==null){
        response.sendRedirect("index.jsp?unikey="+generateUniKey(request));
        return;
    }
    String token = getToken();
    String phoneNumber=null;
    if(token!=null&&!"".equals(token.trim())){
        session.setAttribute("uniToken",token);
        int tryTimes = 0;
        statusCode = 0;
        while(statusCode!=200&&tryTimes<8&&phoneNumber==null){
            tryTimes++;
            logs += "\n第" +tryTimes+
                    "次尝试";
            phoneNumber= getUserMobilePhone(uniKey,token);
            if(statusCode!=200){
                Thread.sleep(Math.round(Math.random()*1000));
                token = getToken();
                statusCode = 0;
            }
        }
    }else{
        phoneNumber = "调用接口获取token失败！";
    }
    Date stopTime = new Date();

%><html>
<head>
    <title>测试用户手机号码</title>
    <meta content="True" name="HandheldFriendly">
    <meta name="viewport" content="width=320"/>
</head>
<body>
<table border="1" cellspacing="0" width="320">
    <tr>
        <td bgcolor="#808080" align="right" width="160">IP:</td><td width="160"><%=request.getRemoteAddr()%></td>
    </tr>
    <tr>
        <td bgcolor="#808080" align="right">Port:</td><td><%=request.getRemotePort()%></td>
    </tr>
    <tr>
        <td bgcolor="#808080" align="right">Token:</td><td><%=token%></td>
    </tr>
    <tr>
        <td bgcolor="#808080" align="right">unikey:</td><td><%=uniKey%></td>
    </tr>
    <tr>
        <td bgcolor="#808080" align="right">status:</td><td><%=statusCode%></td>
    </tr>
    <tr>
        <td  bgcolor="#808080" align="right">结果:</td><td><%=phoneNumber%></td>
    </tr>
    <tr>
        <td  bgcolor="#808080" align="right">启动:</td><td><%=StringUtils.date2string(startTime)%></td>
    </tr>
    <tr>
        <td  bgcolor="#808080" align="right">完成:</td><td><%=StringUtils.date2string(stopTime)%></td>
    </tr>
    <tr>
        <td colspan="2" align="center"><a href="index.jsp?createToken=true">重头再来</a></td>
    </tr>
    <tr>
        <td colspan="2" align="center"><textarea style="width:320px;height:300px;"><%=logs%></textarea></td>
    </tr>
</table>
</body>
</html>
<%!
    private int statusCode=-1;
    private String logs = "";
    private Logger logger = Logger.getLogger("com.fortune.rms.jsp.hbMobile.uniLogin.jsp");
    private AppConfigurator config = AppConfigurator.getInstance();
    private String appKey = config.getConfig("system.security.unicomCenter.appKey","f7142508e0d869afae6b847da770d40eee154eb9");//,"23ab78dee4b73c5d92e528a2788ac127e118e087");
    private String appSecurity = config.getConfig("system.security.unicomCenter.appSecret","f9d093d8fea100bccd7fea228ea02982d1965763");//,"d68eedb334b2273ff73d138fa7a803d08d70ce35");
    //https://open.wo.com.cn/openapi/authenticate/v1.0
/*
    private String host = config.getConfig("system.security.unicomCenter.host","open.wo.com.cn");
    private int port = config.getIntConfig("system.security.unicomCenter.port", 443);
*/
    public String generateUniKey(HttpServletRequest request){
/*
        String result = "";
        String dateStr = StringUtils.date2string(new Date(),"yyyyMMddHHmmss");
        String pwd = AppConfigurator.getInstance().getConfig("uniKeyPwd","fortune!@#456");
        String randomNumber = ""+Math.round(Math.random()*100000000);
*/
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
    public boolean validUniKey(HttpServletRequest request,String uniKey){
        return true;
    }

    public String getToken(){
        String result;
        String url = config.getConfig("system.security.unicomCenter.authenticateUrl","https://open.wo.com.cn/openapi/authenticate/v1.0");
        Date startTime = new Date();
        APIRequest request = new APIRequest();
        AuthorizationHeader header = new AuthorizationHeader();
        header.setAppKey(appKey);
        header.setAppSecret(appSecurity);
        request.setAuthHeader(header);
        request.setAccept("application/json");
        request.setContentType("application/json");
        request.setUri(url);
        AuthenticateAPIImpl auth = new AuthenticateAPIImpl();
        logger.debug("即将发起请求："+url);
        logs +="\n" +StringUtils.date2string(new Date(),"yyyy-MM-dd HH:mm:ss,sss")+
                "即将访问："+url;
        GetTokenResponse response = auth.getToken(request);
        statusCode = response.getStatusCode();
        logs +="\n返回状态："+statusCode;
        result = response.getToken();
        logs +="\n返回Token："+result;
        logger.debug("返回代码：statusCode="+statusCode);
        logger.debug("返回token:"+response.getToken());
        logger.debug("返回数据：\n"+result);
        Date stopTime = new Date();
        logs +="\n此次获取Token花费时间："+(stopTime.getTime()-startTime.getTime())+"毫秒";
        return result;
    }

    public String getUserMobilePhone(String uniKey,String token){
        String url = config.getConfig("system.security.unicomCenter.getPhoneNumberUrl","https://open.wo.com.cn/openapi/netplus/getphonenumber/v1.0/")+uniKey;
        Date startTime = new Date();
        APIRequest request = new APIRequest();
        AuthorizationHeader header = new AuthorizationHeader();
        header.setAppKey(appKey);
        //header.setAppSecret(appSecurity);
        header.setToken(token);
        request.setAuthHeader(header);
        request.setAccept("text/plain");
        request.setContentType("text/plain");
        request.setUri(url);
        NetAPIimpl netApi = new NetAPIimpl();
        logger.debug("即将发起请求："+url);
        logs +="\n即将访问："+url;
        GetphonenumberResponse response = netApi.getphonenumber(request);
        statusCode = response.getStatusCode();
        logs +="\n返回状态："+statusCode;
        String result = null;
        if(statusCode!=200){
            logger.error("无法获取号码："+statusCode+",返回信息如下：\n"+response.getResponseData());
            logs += "\n返回信息如下：\n"+response.getResponseData();
        }else{
            result =  response.getResponseData();
            logs +="\n返回号码："+result;
            logger.debug("返回数据：\n"+result);
            if(result!=null){
                result = result.trim();
                int p = result.indexOf(",");
                if(p>0){
                    result = result.substring(0,p);
                }
                if("".equals(result)||"0".equals(result)){
                    result = null;
                }
            }
        }
        Date stopTime = new Date();
        logs +="\n此次获取手机号码花费时间："+(stopTime.getTime()-startTime.getTime())+"毫秒";
        return result;
    }
%>