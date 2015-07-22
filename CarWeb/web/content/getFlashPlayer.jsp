<%--
  Created by IntelliJ IDEA.
  User: long
  Date: 13-5-9
  Time: 下午6:04
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String pidUrl=request.getParameter("urlPlay");
    session.setAttribute("pid",pidUrl);
    session.setAttribute("contentId",request.getParameter("contentId"));
    session.setAttribute("deviceId",request.getParameter("deviceId"));
//    System.out.print("进行播放时所调用的页面+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+pidUrl+"=============="+session.getAttribute("pid"));
%>{success:true}