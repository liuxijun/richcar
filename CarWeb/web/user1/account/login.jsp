<%@ page import="cn.sh.guanghua.mediastack.user.SessionUser,
                 cn.sh.guanghua.mediastack.common.ConfigManager"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page language="java" %><%
	/*	本页面的任务有二：一是读Cookie中的信息、二是根据读到的结果显示不同的内容  */
	
	String userInfo = "";
    String sessionId = "";
    String bindingId = "";
    String redirectURL = "";
    
    SessionUser su = new SessionUser();
    /*	本页面的任务之一：读Cookie中的信息：userInfo、sessionId、bindingId */
    userInfo  = su.getUserInfo();
    sessionId = su.getSessionId();
    bindingId = su.getBindingId();
    String logoutURL = ConfigManager.getConfig().node("account").get("logoutURL","");
    String cardSelfURL = ConfigManager.getConfig().node("account").get("cardSelfURL","");
%>

<html>
<head>
<title>用户登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css"></style>
</head>

<body background="images/333.gif" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<TABLE border=0 align=center cellPadding=0 cellSpacing=2>
  <%
    /*	本页面的任务之二：二是根据读到的结果显示不同的内容
     *  如果读到有效的信息，则应提示用户已经登录（用户登出按钮），（本页面还提示的访问内容的入口） 
     *  如是没有读到信息或读到无效的信息，则显示登录按钮，提示用户进行登录
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
			            color="#000000"><B>1、在收费之前必须有授权操作 </B></FONT></a><BR>
				  	</tr>	
				  	<tr> 
				    	<TD><a href="httpService.jsp?fileID=01&action=Click&categoryId=backetball">
				    	<FONT color="#FF00FF"><B>2、授权成功后的按次计费操作</B></FONT></a><BR>
			        </tr> 
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=QonMonth&categoryId=backetball"><FONT 
			            color="#000000"><B>3、绑定的包月费率的栏目的页面打开之前应进行查询包月的操作 </B></FONT></a><BR>
			        </tr>
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=PreonMonth&categoryId=backetball">
			            <FONT color="#FF00FF"><B>4、在进行包月操作之前应有预包月操作 </B></FONT></a><BR>
			        </tr>
			        <tr>
			            <TD><a href="httpService.jsp?fileID=01&action=OnMonth&categoryId=backetball"><FONT 
			            color="#000000"><B>5、完成包月定购的操作</B></FONT></a><BR>
			        </tr>
			        <tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=OnLine&categoryId=backetball">
			            <FONT color="#FF00FF"><B>6、访问时长内容开始时，在授权通过的前提下要做上线操作 </B></FONT></a><BR>
				  	</tr>	  	
				     <tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=OffLine&categoryId=football"><FONT 
			            color="#000000"><B>7、访问时长内容结束时，在发送计费事件之后权做下线操作</B></FONT></a><BR>
				  	</tr>	
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=Time&categoryId=football"><FONT 
			            color="#FF00FF"><B>8、访问时长内容结束，在完成下线操作后再发送计费事件</B></FONT></a><BR>
				  	</tr>
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=FreeBill&categoryId=football"><FONT 
			            color="#000000"><B>9、访问已经包月定购的内容</B></FONT></a><BR>
				  	</tr>
				  	<tr> 
			            <TD><a href="httpService.jsp?fileID=01&action=Amount&categoryId=backetball"><FONT 
			            color="#FF00FF"><B>10、按金额计费</B></FONT></a><BR>
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
