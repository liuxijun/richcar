<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2011-1-28
  Time: 17:21:47
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    session.invalidate();
%><html>
  <head><title>登出系统</title>
  <script type="text/javascript">
      function refreshPage(){
         frm.submit(); 
      }
  </script></head>
  <body onload="refreshPage()">
  <form name="frm" method="post" target="_top" action="../login.jsp">

  </form>
  </body>
</html>