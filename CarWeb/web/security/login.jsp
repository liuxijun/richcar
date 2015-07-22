<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page language="java" %>
<%@ include file="../inc/webroot.jsp"%><html>
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
<form method="post" name="login" action="<%=webroot%>/security/login.action">
<jsp:include page = "../inc/top.jsp" />
  <TABLE height=286 cellSpacing=0 cellPadding=0 width=1000 align=center 
background=<%=webroot%>/images/jinfen_02.jpg border=0>
    <TBODY>
      <TR> 
        <TD width=34>&nbsp;</TD>
        <TD width=936 align="center" vAlign=middle> 
          <%
         if (request.getAttribute("msg")!=null){
         	out.print("<font color=red>");
         	out.print(request.getAttribute("msg"));
         	out.print("</font><br><br>");
         	}else{
         	out.print("请重新登录");
         	}
         %>
          <div align="center"><br>
            <br>
            帐号 
            <input name="obj.login" type="text" id="login"  size=16>
            <br>
            密码 
            <input name="obj.password" type="password" id="password"  size=17>
            <br>
            <input type="submit" name="Submit2" value="登录">
          </div></TD>
        <TD width=30>&nbsp;</TD>
      </TR>
    </TBODY>
  </TABLE>
  <jsp:include page = "../inc/bottom.jsp" />
</form>
</body>
</html>
