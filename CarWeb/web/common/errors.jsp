<%@ taglib uri="/struts-tags" prefix="s" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>�쳣����:
<br />
<h3 style="color: red">
<!-- ����쳣���� --> 
<s:property	value="exception" /></h3>
<br />
�쳣��ջ��Ϣ:
<br />
<!-- �쳣��ջ��Ϣ -->
<pre>
    <s:property value="exceptionStack" />
</pre>

