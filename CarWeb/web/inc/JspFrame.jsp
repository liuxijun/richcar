<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ include file="../inc/webroot.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><%=webname%></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=webroot%>/inc/style.css">
	<SCRIPT language=javascript src="<%=webroot%>/inc/fade.js"></SCRIPT>
	<SCRIPT language=javascript src="<%=webroot%>/inc/pz_chromeless.js" type=text/javascript></SCRIPT>
	
</head>

<body <%-- bgcolor="#D8D8D8" --%>  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<jsp:include page = "../inc/top.jsp" />
<jsp:include page = "../inc/menu.jsp"/>
<jsp:include page = "../inc/branch.jsp"/>
<table width="800" height="286" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=webroot%>/images/jinfen_02.jpg">
         <tr>
          <td width="26">&nbsp;</td>
          <td width="752" valign="top"><div align="center">


<table id="Table_01" width="751" height="44" border="0" cellpadding="0" cellspacing="0">
                    <tr> 
                      <td> <img src="<%=webroot%>/images/title2_01.jpg" width="35" height="44" alt=""></td>
                      <td> 
                      <table width="716" height="44" border="0" cellpadding="0" cellspacing="0" background="<%=webroot%>/images/title2_02.jpg">
                          <tr>
                            <td width="716"><font color="#FD9802"><strong>&#25805;&#20316;&#25552;&#31034;</strong></font></td>
                          </tr>
                        </table>
                        </td>
                    </tr>
                  </table>

						
						<form method="post" name="xxxForm" action="<%=webroot%>/xxx.do" >
						<table width="718" border="0" align="center" cellpadding="2" cellspacing="0" bgcolor="#666666">
						                          <tr> 
						                            <td width="400" bgcolor="#999999"> <div align="center"> 
						                                
						                               
						                                <table width="718" border="0" align="center" cellpadding="5" cellspacing="1">
						                                  <tr bgcolor="#F5F5F5"> 
						                                    <td width="250" height="22"> <div align="right">&#21517;&#31216;</div></td>
						                                    <td height="30"><input name="name" type="text" id="name"  size=38><html:errors property="name"/></td>
						                                  </tr>					                                  
						                                  
						                                  <tr bgcolor="#F5F5F5"> 
						                                    <td width="250" height="22"> <div align="right">??????</div></td>
						                                    <td height="30">
						                                  <select name="superId" >
						                                  <option value=0>---------- ? ? ? ----------</option>

						                                    <logic:iterate id="it" name="menuTree"> 
						                                    	<option value=<bean:write name="it" property="id" />>
						                                    	<%
							                                    		try{
								                                    		long i = 0;
								                                    		while (i>1){
								                                    			out.print(" ");
								                                    			i--;
								                                    		}
								                                    		out.print("+-");
							                                    		}catch(Exception ex){
																            ex.printStackTrace();
																        }
						                                    	%>
						                                    	<bean:write name="it" property="name" />
						                                    	</option>
				           									</logic:iterate>
				           									</select><html:errors property="superId"/>						                                    
						                                    </td>
						                                  </tr>		
		                                  
						                                  <tr bgcolor="#F5F5F5"> 
						                                    <td colspan="2" height="30">
						                                    <div align="center"><input type="submit" name="Submit2" value="????">
						                                    </div></td>
						                                  </tr>						                                  
														</table>
														
						</div></td>
						</tr>
						</table>	
						</form>						
      </div></td>
          <td width="22">&nbsp;</td>
        </tr>
</table>
<jsp:include page = "../inc/bottom.jsp"/>
</body>
</html>
