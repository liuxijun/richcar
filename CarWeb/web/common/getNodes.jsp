<%--

  Created by IntelliJ IDEA.

  User: xjliu

  Date: 2011-4-6

  Time: 19:51:35

  To change this template use File | Settings | File Templates.

--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="fts" uri="/WEB-INF/tlds/fortune-tags.tld" %><%@ taglib

        uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib

        uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %><%@ taglib

        prefix="s" uri="/struts-tags" %>[

<s:iterator value="objs" status="status" id="area"><s:if test="#status.index>0">,</s:if>{

    text:"<s:property value="name" escape="false"/>",id:"<s:property value="id"/>",

    cls:"<s:if test="leaf==true">file<s:else>folder</s:else></s:if>",

    leaf:<s:property value="leaf"/>,checked:false

    }</s:iterator> ]