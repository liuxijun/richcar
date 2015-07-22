<%@ page import="com.fortune.util.AppConfigurator" %><%@ page import="com.fortune.server.message.ServerMessager" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fortune.util.JsonUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/7/5
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command= request.getParameter("command");
    AppConfigurator appConfigurator = AppConfigurator.getInstance();
    ServerMessager messager = new ServerMessager();
    Map<String,Object> result = new HashMap<String,Object>();
    if("save".equals(command)){
        String name = request.getParameter("name");
        String addr = request.getParameter("addr");
        String pid  = request.getParameter("pid");
        if(pid!=null&&!"".equals(pid)){
            addr +="/"+pid;
        }
        String localAddr = appConfigurator.getConfig("redex.transcenter.localAddr","eth1");
        if(!"".equals(localAddr)){
            addr+="@"+localAddr;
        }
        String transcenterUrl = appConfigurator.getConfig("redex.transcenter.address","http://127.0.0.1/transcenter")+"/interface/service.jsp";
        out.println(messager.postToHost(transcenterUrl, "command=saveSource&name=" + name + "&addr=" + addr, "UTF-8"));
        return;
    }else{
        result.put("error","参数不错！命令不能识别："+command);
    }
    out.print(JsonUtils.getJsonString(result));
%>