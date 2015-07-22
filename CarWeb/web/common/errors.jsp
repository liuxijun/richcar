<%@ taglib uri="/struts-tags" prefix="s" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>异常对象:
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

