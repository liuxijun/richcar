<%@ page import="cn.sh.guanghua.util.tools.ParamTools"%>
<%@ page contentType="text/html; charset=GBK" %>
<%
    String returnUrl = ParamTools.getParameter(request,"return_url","");
%>
        <html>
        <head>
        <meta http-equiv='Content-Language' content='zh-cn'>
        <meta http-equiv='Content-Type' content='text/html; charset=gb2312'>
        <title></title>
        <style>td{font-size:14px;}</style>
        </head>
        <body topmargin='0' leftmargin='0' onload='window.resizeTo(515,335)'>
        <table border='0' width='550' cellpadding='0' style='border-collapse: collapse' height='400' background='../../images/account/123.gif' align='center'>
        	<tr><td height='10'></td></tr>
        	<tr>
        		<td align='right' width='94'>
		<img src='http://kdsj2.sx.cn/yqts.jpg' width='94' height='87'>
        		</td>
        		<td width='400' style='line-height:200%;'>
��¼ʧ�ܣ��û������ڣ�������󣬲���ɽ����ͨ�����û�
<br><a href="Login.jsp?return_url=<%=java.net.URLEncoder.encode(returnUrl)%>">�����µ�¼</a>
        		</td>
        	</tr>
        </table>
        </body>
        </html>
