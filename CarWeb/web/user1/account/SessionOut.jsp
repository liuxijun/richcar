<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools"%><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%><%
  session.removeAttribute(Constants.USER_LISTENER);
  session.invalidate();
  response.sendRedirect("http://61.55.138.4/netvirst/cytd/jsp/ssos/logout/logout.jsp");
  return;
/*
  String msgHint = "账号注销成功";
%>
<html>
	<head>
		<title>提示信息</title>
	</head>
	<body>
		<center>
        <table width="400">
          <tr>
            <td height="100"></td>
          </tr>
        </table>
        <table align="center" width="400">
          <tr>
             <td align="center"><b><font color="#990000"><%=msgHint%></font></b></td>
          </tr>
          <tr>
             <td align="center" height="50"> </td>
           </tr>
          <tr>
             <td align="center"><a href="#" onclick="javascript:window.close();"><img border="0" src="../../images/account/bt_close.gif" width="111" height="23" /></a></td>
           </tr>
        </table>
        </center>
	</body>
</html>
<%
*/
%>