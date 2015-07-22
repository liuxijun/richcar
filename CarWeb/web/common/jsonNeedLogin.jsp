<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-10-21
  Time: 13:00:53
  To change this template use File | Settings | File Templates.
--%><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%@ taglib
        uri="/struts-tags" prefix="s" %><%@ page
        import="com.opensymphony.xwork2.ActionContext,
        com.opensymphony.xwork2.util.ValueStack,
        org.apache.log4j.Logger" %><%

    request.setAttribute("exception","登录已经超时！请重新登录！谢谢！");
%>{
    success:false,
    msg:"${exception}",error:"${exception}",actionError:"${exception}"}<%--
<br />
<h3 style="color: red">
<!-- 获得异常对象 -->
<s:property	value="exception" /></h3>
<br />
异常堆栈信息:
<br />
<!-- 异常堆栈信息 -->
<pre>
    <s:property value="exceptionStack" />
</pre>

--%>