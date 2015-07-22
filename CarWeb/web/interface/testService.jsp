<%@ page import="com.fortune.server.message.ServerMessager" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-4-23
  Time: 8:40:52
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String serverMsg = request.getParameter("serverMsg");
    String resultMsg = "";
    if(serverMsg == null){
        serverMsg = "<?xml version=\"1.0\"?>\n" +
"<req version=\"1.0\">\n" +
"<task type=\"encoder\" target=\"onHeart\">\n" +
" <param n=\"macAddr\" v=\"00225F6FCC99\"/>\n" +
" <param n=\"deviceId\" v=\"14\"/>\n" +
" <param n=\"clientVersion\" v=\"2.16.5.0\"/>\n" +
" <param n=\"allMacAddrs\" v=\"00225F6FCC99;0023AE0B4828\"/>\n" +
" <param n=\"downloadSessionCount\" v=\"0\"/>\n" +
"</task>\n" +
"</req>";
    }
    {
        ServerMessager messager = new ServerMessager();
        resultMsg = messager.postToHost("http://61.55.144.81/user/saveLog.jsp",serverMsg);
    }
%><html>
  <head><title>Simple jsp page</title></head>
  <body>
  <form action="testService.jsp" method="post">
    <textarea name="serverMsg" style="width:600px;height:300px">
<%=serverMsg%>
        </textarea>
      <br/>
      <input type="submit">
      <pre>执行结果：
<%=resultMsg%>
      </pre>
  </form></body>
</html><%
%>