<%@ page import="cn.sh.guanghua.mediastack.user.SessionUser,
                 cn.sh.guanghua.mediastack.common.ConfigManager"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page language="java" %><%
	/*	��ҳ��������ж���һ�Ƕ�Cookie�е���Ϣ�����Ǹ��ݶ����Ľ����ʾ��ͬ������  */
	
	String userInfo = "";
    String sessionId = "";
    String bindingId = "";
    String redirectURL = "";
    
    SessionUser su = new SessionUser();
    /*	��ҳ�������֮һ����Cookie�е���Ϣ��userInfo��sessionId��bindingId */
    userInfo  = su.getUserInfo();
    sessionId = su.getSessionId();
    bindingId = su.getBindingId();
    String logoutURL = ConfigManager.getConfig().node("account").get("logoutURL","");
    String cardSelfURL = ConfigManager.getConfig().node("account").get("cardSelfURL","");
%>

<html>
<head>
<title>�û���¼</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css"></style>
</head>

<body background="images/333.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<TABLE border=0 align=center cellPadding=0 cellSpacing=2>
  <%
    /*	��ҳ�������֮�������Ǹ��ݶ����Ľ����ʾ��ͬ������
     *  ���������Ч����Ϣ����Ӧ��ʾ�û��Ѿ���¼���û��ǳ���ť��������ҳ�滹��ʾ�ķ������ݵ���ڣ� 
     *  ����û�ж�����Ϣ�������Ч����Ϣ������ʾ��¼��ť����ʾ�û����е�¼
     */
     
	if  (userInfo != null && !userInfo.equals("") && !userInfo.equals("null") && sessionId != null && !sessionId.equals("") && !sessionId.equals("null")){
	    %>
	    <tr>
	    	<td>
				<table>
					<TR>
						<TD align="center" valign="bottom"><a  href="<%=logoutURL%>"><img src="images/logout_button.gif" width="80" height="22" border="0"></a></TD>
					</TR>
					<TR>
						<TD align="center" valign="bottom"><a href="<%=cardSelfURL%>" target="_blank"><img src="images/zi_button.gif" width="91" height="22" border="0"></a></TD>
					</TR>
				</table>
			</td>
			
			
			<td>
				<table width="778" height="18" border="0" align="center"  cellpadding="0" cellspacing="0" background="images/fenzhan-bg.jpg">
					<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=Authorize&categoryId=backetball"><FONT 
			            color="#000000"><B>1�����շ�֮ǰ��������Ȩ���� </B></FONT></a><BR>
				  	</tr>	
				  	<tr> 
				    	<TD><a href="httpService.jsp?fileID=01&action=Click&categoryId=backetball">
				    	<FONT color="#FF00FF"><B>2����Ȩ�ɹ���İ��μƷѲ���</B></FONT></a><BR>
			        </tr> 
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=QonMonth&categoryId=backetball"><FONT 
			            color="#000000"><B>3���󶨵İ��·��ʵ���Ŀ��ҳ���֮ǰӦ���в�ѯ���µĲ��� </B></FONT></a><BR>
			        </tr>
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=PreonMonth&categoryId=backetball">
			            <FONT color="#FF00FF"><B>4���ڽ��а��²���֮ǰӦ��Ԥ���²��� </B></FONT></a><BR>
			        </tr>
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=OnMonth&categoryId=backetball"><FONT 
			            color="#000000"><B>5����ɰ��¶����Ĳ���</B></FONT></a><BR>
			        </tr>
			        <tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=OnLine&categoryId=backetball">
			            <FONT color="#FF00FF"><B>6������ʱ�����ݿ�ʼʱ������Ȩͨ����ǰ����Ҫ�����߲��� </B></FONT></a><BR>
				  	</tr>	  	
				     <tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=OffLine&categoryId=football"><FONT 
			            color="#000000"><B>7������ʱ�����ݽ���ʱ���ڷ��ͼƷ��¼�֮��Ȩ�����߲���</B></FONT></a><BR>
				  	</tr>	
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=Time&categoryId=football"><FONT 
			            color="#FF00FF"><B>8������ʱ�����ݽ�������������߲������ٷ��ͼƷ��¼�</B></FONT></a><BR>
				  	</tr>
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=FreeBill&categoryId=football"><FONT 
			            color="#000000"><B>9�������Ѿ����¶���������</B></FONT></a><BR>
				  	</tr>
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=Amount&categoryId=backetball"><FONT 
			            color="#FF00FF"><B>10�������Ʒ�</B></FONT></a><BR>
				  	</tr>	  	
				</table>
			</td>
		</tr>   
<%
	} else {
	    String bindingID = "" + System.currentTimeMillis();
        String ispAuthenticationURL = ConfigManager.getConfig().node("account").get("ispAuthenticationURL","");
        String resultChecker = ConfigManager.getConfig().node("account").get("resultChecker","");
        redirectURL = ispAuthenticationURL + "resultChecker="
			+ java.net.URLEncoder.encode(resultChecker)
			+ "&bindingId="	+ bindingID;
%>
	<TBODY>
		<TR>
		    <TD align="center"><a href="<%=redirectURL%>"><img src="images/login_button.gif" width="70" height="22" border="0"></a></TD>
		</TR>
		<TR>
		    <TD align="center"><a href="<%=cardSelfURL%>" target="_parent"><img src="images/zi_button.gif" width="91" height="22" border="0"></a></TD>
		</TR>
	</TBODY>
 <%
	}
 %>
  
</TABLE>
</body>
</html>
