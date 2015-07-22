<%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/12/10
  Time: 17:56
  前台用户获取的内容列表
--%>
<%@taglib prefix="s" uri="/struts-tags" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %>{
"totalCount":"${totalCount}",
"contents":<s:if test="contentList == null">[]</s:if><s:else></s:else> }
