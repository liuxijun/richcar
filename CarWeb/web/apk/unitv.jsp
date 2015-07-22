<%@ page import="com.fortune.common.Constants" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-5-8
  Time: 下午2:45
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String userAgent = request.getHeader("user-agent");
    String phone = (String) session.getAttribute(Constants.USER_PHONE_NUMBER);
    if(phone==null){
        phone = "18631118565";
        session.setAttribute(Constants.USER_PHONE_NUMBER, phone);
/*
        String token = request.getParameter("token");
        phone = request.getParameter("phone");
        if(verifyToken(token, phone)){
            session.setAttribute(Constants.USER_PHONE_NUMBER,phone);
        }else{
            String returnUrl = "/apk/player.jsp";
            session.setAttribute("returnUrl",returnUrl);
            response.sendRedirect("../page/hbMobile/userLogin.jsp?returnUrl="+returnUrl);
            return;
        }
*/
    }
    boolean fromClient = "true".equals(request.getParameter("fromClient"));
    String apkFileName = "unitv_1.2.9.8.apk";
    String apkVersion = "1.2.9.8";
    String downloadUrl = "href=\"download.jsp?fileName="+apkFileName+"\"";

%><%@include file="cssJsLib.jsp"%><!DOCTYPE html><html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=0;">
<meta name="format-detection" content="telephone=no">
<link rel="apple-touch-icon-precomposed" href="resource/12783546.png" sizes="72x72"> <!-- Android 3.0+ -->
<link type="text/css" rel="stylesheet" href="resource/15002588.0.css">
<script type="text/javascript" src="resource/3749131.touchslider.min.js"></script>
<title>联通电视</title>
</head>
<body style="background: white">
<div class="100%"><img src="../page/unitv/images/unitv_logo.png" width="100%" height="52"></div>
<div class="wrapper">
    <header class="sub-hd">
        <div class="tit">应用简介</div>
        <span class="rt-btn"><a href="#" onclick="history.go(-1); return false"></a>返回</span>
        <span class="home"><a href="/">首页</a></span>
    </header>
    <div id="screenshot-slider" class="slider-img" style="padding: 0;height: 3px">
        <ul class="touchslider-viewport"
            style="-webkit-tap-highlight-color: transparent; height: 2px; position: relative; ">
        </ul>
    </div>
    <ul class="v-soft-list down-page">
        <li>
            <div class="icon"><img src="resource/unitv.png"><i class=""></i></div>
            <div class="info">
                <h2>联通电视  V<%=apkVersion%></h2>
                <p class="txt d-icon">                12000+人推荐下载
                </p>
                <p class="txt">视频应用软件 | 2.0MB</p>
                <div class="down-btn free"><a <%=downloadUrl%>>免费下载</a></div>
            </div>
        </li>
    </ul>
    <div class="payment">
        <p><strong>安全检测：</strong><span class="safe">已通过安全检测</span></p>
        <p><strong>资费说明：免费+部分收费</strong></p>
    </div>
    <div class="soft-info">
        <div class="introduce J_info" style="height: 57px;margin-top: 15px;">
            “联通电视手机版”是河北联通为4G手机用户全新打造的手机电视业务，让您随时随地观看电影、电视剧、娱乐、综艺、资讯、音乐和时尚等特色节目，并可与电视多屏互动，为您带来一个全新的视界，体验无处不在的精彩。</div>
    </div>
    <%
        if(fromClient){
%>
    <div id="screenshot-slider" class="slider-img" style="padding: 0;height: 3px">
        <ul class="touchslider-viewport"
            style="-webkit-tap-highlight-color: transparent; height: 2px; position: relative; ">
        </ul>
    </div>
    <ul class="v-soft-list down-page">
        <li <%--style="padding: 10px"--%>>
            <div class="icon"><img src="images/logo_woxin.png"><i class=""></i></div>
            <div class="info">
                <h2>沃随信</h2>
                <p class="txt d-icon">                3600+人推荐下载
                </p>
                <p class="txt">短彩信应用软件 | 2.0MB</p>
                <div class="down-btn free"><a href="download.jsp?fileName=woxin.apk">免费下载</a></div>
            </div>
        </li>
    </ul>
    <div class="payment">
        <p><strong>安全检测：</strong><span class="safe">已通过安全检测</span></p>
        <p><strong>资费说明：免费+部分收费</strong></p>
    </div>
    <div class="soft-info">
        <div class="introduce J_info" style="height: 57px;margin-top: 15px;">“沃随信”系河北联通为安卓手机用户提供的一款免费使用的短彩信软件，该软件简单易用，不仅有丰富多彩的短彩信内容供大家查看和转发给好友，而且还可自己编辑上传优质短信供其他人使用。</div>
    </div>
    <div id="screenshot-slider" class="slider-img" style="padding: 0;height: 3px">
        <ul class="touchslider-viewport"
            style="-webkit-tap-highlight-color: transparent; height: 2px; position: relative; ">
        </ul>
    </div>
    <ul class="v-soft-list down-page">
        <li <%--style="padding: 10px"--%>>
            <div class="icon"><img src="images/logo_woxuan.png"><i class=""></i></div>
            <div class="info">
                <h2>沃炫</h2>
                <p class="txt d-icon">                4800+人推荐下载
                </p>
                <p class="txt">炫铃应用软件 | 2.0MB</p>
                <div class="down-btn free"><a href="download.jsp?fileName=woxuan.apk">免费下载</a></div>
            </div>
        </li>
    </ul>
    <div class="payment">
        <p><strong>安全检测：</strong><span class="safe">已通过安全检测</span></p>
        <p><strong>资费说明：免费+部分收费</strong></p>
    </div>
    <div class="soft-info">
        <div class="introduce J_info" style="height: 57px;margin-top: 15px;">“沃炫”系河北联通为广大手机客户提供的一款方便管理炫铃和DIY炫铃内容的软件。通过安卓客户端软件可以实现搜索设置喜欢的炫铃、随心录制炫铃、复制好友炫铃、按来电群组设置不同炫铃，本软件还提供免费情景炫铃铃音供客户使用。</div>
    </div>

    <%
        }
    %>
    <div id="screenshot-slider" class="slider-img" style="padding: 0;height: 3px">
        <ul class="touchslider-viewport"
            style="-webkit-tap-highlight-color: transparent; height: 2px; position: relative; ">
        </ul>
    </div>
    <div style="width:100%"><img src="../page/unitv/images/copyright_phone_b.jpg" width="100%" height="16%"></div>
</div>


</body></html>