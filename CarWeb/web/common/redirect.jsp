<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/8/4
  Time: 8:36
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
  String location =(String) request.getAttribute("location");
  if(location!=null){
    response.sendRedirect(location);
    return;
  }
%><html>
<head>
  <title>没有参数</title>
</head>
<body>
<p>没有参数进行Redirect</p>
</body>
</html>