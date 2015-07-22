<%@ page import="java.io.File" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fortune.util.FileUtils" %>
<%@ page import="com.fortune.util.StringUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/1/19
  Time: 10:41
  列本目录下的日志文件
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
  List<File> files = FileUtils.listDir(application.getRealPath("/logs/"),"utils*.*",false);
%>
<html>
<head>
    <title></title>
</head>
<body>
<table width="100%" border="1" cellspacing="0">
  <tr>
    <td width="60%">文件名</td>
    <td width="20%">大小</td>
    <td width="20%">日期</td>
  </tr>
   <%
     if(files!=null){
       for(File file:files){
         %>  <tr>
    <td><a href="<%=file.getName()%>"><%=file.getName()%></a></td>
    <td><%=file.length()%></td>
    <td><%=StringUtils.date2string(file.lastModified())%></td>
  </tr>
<%
       }
     }
   %>
</table>
</body>
</html>
