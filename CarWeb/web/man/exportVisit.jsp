<%@ page import="java.net.URLEncoder" %><%@ page
        import="com.fortune.util.IndividualUtils" %><%@ taglib
        prefix="s" uri="/struts-tags" %><%--
  Created by IntelliJ IDEA.
  User: 王明路
  Date: 2014/11/21
  Time: 10:34
  导出媒体访问量报表
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
  String ua = request.getHeader( "User-Agent" );

  String fileName = session.getAttribute("exportFileName").toString();
  String contentDisposition = contentDisposition = "attachment; filename=" + URLEncoder.encode(fileName, "UTF8");

  if( ua != null){
    String userAgent = ua.toLowerCase();
    if( userAgent.contains("mozilla")){
      contentDisposition = "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8");
    }
  }

  response.setContentType("application/ms-excel; charset=UTF-8");
  response.setCharacterEncoding("UTF-8");
  response.setHeader("Content-Disposition", contentDisposition);
%><!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>访问统计 - <%=IndividualUtils.getInstance().getName()%></title>
</head>

<body>
<table style="border:1px">
  <tbody>
  <tr><td colspan="2"><h1>访问量统计报表</h1></td></tr>
  <tr>
    <td>统计频道：<s:if test="channel != null"><s:property value="channel.name"/></s:if><s:else>全部</s:else> </td>
    <td>统计时段：<s:if test="startTime == null && endTime == null">全部</s:if>
      <s:else>
        <s:if test="startTime != null"><s:date name="startTime" format="YYYY-MM-dd"/></s:if> -
        <s:if test="endTime != null"><s:date name="endTime" format="YYYY-MM-dd"/></s:if>
      </s:else>
    </td>
  </tr>
  </tbody>
</table>
<table class="table table-striped table-bordered table-hover table-30">
  <thead>
  <tr>
    <th style="width: 150px;" align="center">排名</th>
    <th width="50%">名称</th>
    <th align="right">访问量</th>
  </tr>
  </thead>

  <tbody>
  <s:iterator value="statVisitList" status="statStatus">
    <tr>
      <td align="center">
        <s:if test="pageBean != null">
          <s:property value="new java.lang.Integer(pageBean.pageSize) * (new java.lang.Integer(pageBean.pageNo) - 1) + #statStatus.index + 1"/>
        </s:if>
        <s:else><s:property value="#statStatus.index + 1"/></s:else>
      </td>
      <td><s:property value="name"/></td>
      <td align="right"><s:property value="count"/></td>
    </tr>
  </s:iterator>

  </tbody>
</table>
</body>
</html>
