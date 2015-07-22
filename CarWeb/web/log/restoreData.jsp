<%@ page import="java.util.List" %><%@ page import="com.fortune.util.BeanUtils" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.vac.*" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.util.MD5Utils" %><%@ page import="com.fortune.vac.socket.client.ClientSocket" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-24
  Time: 下午5:50
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div align="center">
    <form action="../log/visitLog!insertLogDate.action" method="post">
        <table border="1" cellspacing="0">
            <tr>
                <td align="right"><label for="startDate">开始日期：</label></td>
                <td><input id="startDate" style="width:300px;" type="text" name="startDate"></td>
            </tr>
            <tr>
                <td align="right"><label for="endDate">结束日期：</label></td>
                <td><input id="endDate" style="width:300px;" type="text" name="endDate"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input style="width:100px" type="submit" value="提交">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>