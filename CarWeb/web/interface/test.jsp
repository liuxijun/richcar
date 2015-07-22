<%@ page import="java.util.Date" %><%@ page import="com.fortune.util.StringUtils" %>
<%@ page import="com.fortune.rms.business.system.logic.logicImpl.SecurityUtils" %>
<%@ page import="com.fortune.server.message.ServerMessager" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 12-7-11
  Time: 下午2:25
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String url = getParameter(request,"sourceUrl","http://hls.openhe.net/vod/1/2/3/movie.mp4.m3u8?contentId=1&contentPropertyId=2&channelId=3");
    String clientIp = getParameter(request,"clientIp",request.getRemoteAddr());
    String command = request.getParameter("command");
    SecurityUtils su = SecurityUtils.getInstance();
    String registeredUrl = getParameter(request, "registeredUrl", su.regUrl(url, clientIp, null));
    String result = "";
    if("reg".equals(command)){
        registeredUrl = su.regUrl(url,clientIp,null);
    }else if("auth".equals(command)){
        int authCode = su.verifyUrlToken(registeredUrl,clientIp,null);
        switch(authCode){
            case SecurityUtils.VERIFY_RESULT_ENCRYPT_ERROR:
                result = "认证失败";
                break;
            case SecurityUtils.VERIFY_RESULT_NO_ENCRYPT:
                result="缺少token";
                break;
            case SecurityUtils.VERIFY_RESULT_PASSED:
                result = "认证通过";
                break;
        }
    }else if("authRemote".equals(command)){
        ServerMessager serverMessager = new ServerMessager();
        result = serverMessager.postToHost("http://localhost:"+request.getServerPort()+"/interface/auth.m3u8",registeredUrl+"\r\n"+clientIp);
    }else{
        result = "命令不认识："+command;
    }
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN" xml:lang="zh-CN">
<head>
    <title>测试防盗链</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css" media="screen">
        body { margin:0; padding:0; }
        .inputBox { width:800px; }
        label{width:80px;text-align: right}
    </style>
    <script type="text/javascript">
        function doCommand(command){
            testForm.command.value = command;
            testForm.submit();
        }
        function regUrl(){
            doCommand('reg');
        }
        function verifyUrl(){
            doCommand("auth");
        }
        function verifyRemoteUrl(){
            doCommand("authRemote");
        }
    </script>
</head>
<body>
   <form action="test.jsp" method="post" id="testForm">
       <table>
           <tr>
               <td><label for="sourceUrl">原始连接：</label></td>
               <td><input class="inputBox" type="text" id="sourceUrl" value="<%=url%>"></td>
           </tr>
           <tr>
               <td><label for="clientIp">客户端IP：</label></td>
               <td><input class="inputBox"  type="text" id="clientIp" value="<%=clientIp%>"></td>
           </tr>
           <tr>
               <td><label for="registeredUrl">注册连接：</label></td>
               <td><input class="inputBox"  type="text" id="registeredUrl" value="<%=registeredUrl%>"></td>
           </tr>
           <tr>
               <td colspan="2">
                   <pre><%=result%></pre>
               </td>
           </tr>
           <tr>
               <td colspan="2">
                   <input type="button" value="注册URL" onclick="regUrl()">
                   <input type="button" value="验证URL" onclick="verifyUrl()">
                   <input type="button" value="远端验证" onclick="verifyRemoteUrl()">
               </td>
           </tr>
       </table>
       <input type="hidden" name="command">
   </form>
</body>
</html>
<%!
    public String getParameter(HttpServletRequest request,String name,String def){
        String val = request.getParameter(name);
        return val==null?def:val;
    }
%>