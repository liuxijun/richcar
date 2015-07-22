<%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.common.business.security.model.Admin" %><%@ taglib
        prefix="s" uri="/struts-tags" %><%@ taglib
    prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ page
    contentType="text/html;charset=UTF-8" language="java" %><%
    {
        //todo 测试用户
        Admin testAdmin = new Admin();
        testAdmin.setId(1);
        //优度
        testAdmin.setCspId(2134);

        //网通大包月
        //testAdmin.setCspId(1039);

        //session.setAttribute(Constants.SESSION_ADMIN,testAdmin);
        //todo 结束

        Admin _opLogined =(Admin)session.getAttribute(Constants.SESSION_ADMIN);
        if(_opLogined == null){
            response.sendRedirect("../index.jsp");
            return;
        }
    }
%><%@include file="../inc/permissionLimits.jsp"%><html
        xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv='Content-Language' content='zh-cn'/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <title>综合媒资管理系统 V5</title>
    <link href="../../images/favicon.ico" rel="Shortcut Icon"/>
</head>
<frameset border="0" framespacing="0" rows="*,0" frameborder="no" cols="*">
    <FRAMESET border="0" name="right" frameSpacing="0" rows="65,*" frameBorder="no">
        <FRAME name="top" src="adminHeader.jsp" scrolling="no">
<%--
        <FRAME name="controltop" src="controlTop.htm" scrolling="no">
--%>
        <FRAMESET border="0" name="main1" frameSpacing="0" frameBorder="no" cols="240,*">
            <frame name='left' target='main' src='adminLeftMenu.jsp' noresize scrolling="auto" frameborder='NO'>
<%--
            <FRAME name="control" src="control.htm" scrolling="no">
--%>
            <frame name='main' noresize scrolling="auto">
        </FRAMESET>
    </FRAMESET>
    <FRAME name="none" src="none.htm" scrolling="no">
</frameset>
<noframes>
    <body bgcolor='#FFFFFF' text='#000000'>
   管理系统提示：您的浏览器不支持框架！
    </body>
</noframes>
</frameset>
</html>
