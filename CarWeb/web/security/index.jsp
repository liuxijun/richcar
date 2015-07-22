<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2009-4-5
  Time: 22:28:04
  To change this template use File | Settings | File Templates.
--%><%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %>
        <%@include file="../inc/webroot.jsp"%>
<html>
  <head>
      <title>WELCOME</title>
      <s:head theme="xhtml"/><link rel="stylesheet" type="text/css" href="<%=webroot%>/inc/style.css">
      <script type="text/javascript" src="../js/common.js"></script>
      <script type="text/javascript" src="../js/treeSelector.js"></script>
      <script type="text/javascript" src="../js/nodeSelector.js"></script>
  </head>
  <body <%-- bgcolor="#D8D8D8" --%>  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <jsp:include page = "../inc/top.jsp" />
 <jsp:include page = "../inc/menu.jsp"/>
  <jsp:include page = "../inc/branch.jsp"/>
<TABLE height=22 cellSpacing=0 cellPadding=0 width=1000 align=center
  background=<%=webroot%>/images/jinfen_02.jpg border=0>
  <tr>
                        <td height="300" colspan="2">
                        <div align="center">
                        欢迎访问管理系统。
                        </div>
                        </td>
                      </tr>
                    </table>
  <jsp:include page = "../inc/bottom.jsp"/>
  </body>
</html>
