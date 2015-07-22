<%@ page import="com.fortune.util.CacheUtils" %><%@ page import="com.fortune.util.StringUtils" %><%@ page import="java.util.Date" %><%@ page import="java.net.URLDecoder" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-15
  Time: 上午8:47
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    CacheUtils.clearAll();
    Date now = new Date();
    String name = request.getParameter("name");
    while(name!=null&&name.contains("%")){
        name = URLDecoder.decode(name,"UTF-8");
    }
    String callBackFunction = request.getParameter("callBack");
    if(callBackFunction==null){
        callBackFunction = "refreshFinished";
    }
%><%=callBackFunction%>(true,{success:true,name:"<%=name%>",now:'<%=StringUtils.date2string(now)%>',time:<%=now.getTime()%>});
