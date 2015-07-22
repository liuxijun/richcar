<%@ page contentType="text/html;charset=UTF-8" %><%@ page
        language="java" %><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@include
        file="../inc/webroot.jsp"%><%@ include file="../inc/webroot.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>成功信息</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=webroot%>/inc/style.css">
</head>

<body <%-- bgcolor="#D8D8D8" --%>  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<jsp:include page = "../inc/top.jsp" />
<jsp:include page = "../inc/menu.jsp"/>
<jsp:include page = "../inc/branch.jsp"/>
<table width="1000" height="286" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=webroot%>/images/jinfen_02.jpg">
         <tr>
          <td width="26">&nbsp;</td>
          <td width="752">
        <table width="388" height="152" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=webroot%>/images/bginfo.jpg">
        <tr> 
          <td width="129"> </td>
          <td width="259"><s:actionmessage></s:actionmessage></td>
        </tr>
      </table>
                
      </td>
          <td width="22">&nbsp;</td>
        </tr>
</table>
<jsp:include page = "../inc/bottom.jsp"/>
</body>
</html>
