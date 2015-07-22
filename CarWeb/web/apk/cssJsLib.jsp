<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-3-7
  Time: 下午3:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="utf-8"/><%@include file="../page/hbMobile/baseFunction.jsp"%><%
    String userAgentForViewPort = request.getHeader("user-agent");
    String viewPorts="";
 //   String viewPortInfo = "<meta name=\"viewport\" content=\"width=640px,user-scalable=yes\" />";
//    {
//
//        if(userAgentForViewPort.contains("Android")){
//           viewPortInfo = "<meta name=\"viewport\" content=\"width=640\" />";
//        }
//    }
    float androidVersions = getAndroidVersion(userAgentForViewPort);
    if(androidVersions>0||"true".equals(request.getParameter("isAndroid"))){
        if(userAgentForViewPort==null)userAgentForViewPort="";
        boolean isAndroids =(androidVersions<3.0f)||"true".equals(request.getParameter("isAndroid"));
        if(!isAndroids){
            //控制获取输入文本框防止页面变大。小结 mate标签的属性控制分辨率
            //user-scalable : 指定用户是否可以手动缩放网页（放大或缩小）
            //initial-scale : 设置可视窗口的初始缩放比例
            // <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
            viewPorts = "<meta name=\"viewport\" content=\"user-scalable=yes,minimum-scale=1,maximum-scale=0.5\" />";
        }else{
            viewPorts = "<meta content=\"width=640px,user-scalable=yes,initial-scale=0.5, maximum-scale=0.1\" name=\"viewport\" />";
        }
    }
%>
<%--<%=viewPortInfo%>--%>
<meta name="viewport" content="width=640px,user-scalable=yes" />
<%=viewPorts%>
<meta content="True" name="HandheldFriendly"/>

<script src="../page/hbMobile/js/mobilePortal.jsp"></script>
<script type="text/javascript" src="../page/hbMobile/js/jquery.js"></script>
<script type="text/javascript" src="../page/hbMobile/js/common1.js" charset="gbk"></script>
