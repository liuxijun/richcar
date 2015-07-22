<%@ page import="com.fortune.util.AppConfigurator" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2010-9-22
  Time: 16:28:12
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    AppConfigurator appConfig = AppConfigurator.getInstance();
    //String defaultLogin;
    String clientIp = request.getRemoteAddr();
    String defaultLogin = "",defaultPwd="",defaultVerifyCode="";
    // System.out.println(clientIp);
    if("127.0.0.1".equals(clientIp)||"localhost".equals(clientIp)||
            "0:0:0:0:0:0:0:1".equals(clientIp)//win7的localhost
            ){
        defaultLogin = appConfig.getConfig("defaultLogin","");
        defaultPwd = appConfig.getConfig("defaultPwd","");
        defaultVerifyCode = appConfig.getConfig("defaultVerifyCode","");
    }
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="FORTUNE NETWORK"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>FORTUNE NETWORK</title>
<script src="images/index/AC_RunActiveContent.js" type="text/javascript"></script>
<link href="images/index/css.css" rel="stylesheet" type="text/css" />

    <link rel="stylesheet" type="text/css" href="resources/css/ext-all.css"/>
    <script type="text/javascript" src="ext/ext-base.js"></script>
    <script type="text/javascript" src="ext/ext-all.js"></script>
    <script type="text/javascript" src="js/ExtUtils.js"></script>
    <script type="text/javascript" src="js/FortuneUtils.js"></script>
    <script src="ext/ext-lang-zh_CN.js" type="text/javascript"></script>
    <link href="images/css.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="js/md5.js"></script>
    <script type="text/javascript">
        <!--
        function $(eleId){
            return document.getElementById(eleId);
        }



        function doLogin(){
            //var passwordObj = userPassword.value;
            var passwordValue = "";
            var loginObj = $("userLogin");
            var pwdObj = $("userPassword");
            var verifyCodeValue = $("verifyCode").value;
            var loginValue = loginObj?loginObj.value:"";
            if(loginValue==null||loginValue==""){
                alert("请输入帐号！");
                return;
            }
            if(pwdObj!=null&&pwdObj.value!=""){
                passwordValue = hex_md5(pwdObj.value);
            }else{
                alert("请输入口令！");
                return;
            }
            //alert(passwordValue);
     //       if(isIE9){

     //       }else{
                Ext.MessageBox.show({
                   msg: '正在登陆，请稍候...',
                   progressText: '正在登录...',
                   width:300,
                   wait:true,
                   waitConfig: {interval:500},
                   icon:'ext-mb-info' //custom class in msg-box.html
                   //,animEl: 'mb7'
                });
      //      }
            Ext.Ajax.request({
                method:'POST',
                waitMsg:'正在提交',
                waitTitle:'链接中...',
                params:{
                   "obj.login":loginValue,
                    "obj.password":passwordValue,
                    "verifyCode":verifyCodeValue

                },
                url:'security/login.action',
                //waitTitle:'链接中...',
                //waitMsg:'发送数据...',
                callback : function(opt, success, response) {
                    Ext.MessageBox.hide();
                    if (success) {
                        var serverData = Ext.util.JSON.decode(response.responseText);
                        if (serverData.success) {
                            window.location = "admin/index.jsp";
                        } else {
                            alert('登陆失败，请仔细检查输入是否正确！\n错误提示：'+serverData.error[0]);
                        }
                    }
                }
            });
        }
        function MM_preloadImages() { //v3.0
            var d = document;
            if (d.images) {
                if (!d.MM_p) d.MM_p = new Array();
                var i,j = d.MM_p.length,a = MM_preloadImages.arguments;
                for (i = 0; i < a.length; i++)
                    if (a[i].indexOf("#") != 0) {
                        d.MM_p[j] = new Image;
                        d.MM_p[j++].src = a[i];
                    }
            }
        }
        function refreshVerifyPic(){
            $("verifyPic").src="security/verifyPic.jsp?dc="+new Date().getTime();
            // alert("refreshed");
        }
        //-->
        function onKeyDown(e){
            if((window.event)&&(window.event.keyCode==13)){
                doLogin();
            }else if((e.which)&&(e.which==13)){
                doLogin();
            }
        }
    </script>


</head>

<body onkeydown="return onKeyDown(event)">
<table width="980" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="90"><a href="#"><img src="images/index/01_1.jpg" width="980" height="90" /></a></td>
  </tr>
  <tr>
    <td height="320" valign="top" background="images/index/01_2.jpg"><table width="869" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="610" height="99">&nbsp;</td>
        <td width="50">&nbsp;</td>
        <td width="209">&nbsp;</td>
        </tr>

 <!--     <tr>
        <td>&nbsp;</td>
        <td height="40">快速通道</td>
        <td>
                  <script type="text/javascript">
                            function selectUser(){
                                var s1 = document.getElementById("s1");
                                if (s1.value ==1){
                                    var userName = 'splt01';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==2){
                                    var userName = 'cpjdgq';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==3){
                                    var userName = 'cpzlkp';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==4){
                                    var userName = 'cpyd01';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==5){
                                    var userName = 'cpkp01';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==6){
                                    var userName = 'cpgycmxq';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==7){
                                    var userName = 'cphxsl';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==8){
                                    var userName = 'cpyppl';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                if (s1.value ==9){
                                    var userName = 'cpbjws';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                  if (s1.value ==10){
                                    var userName = 'spyd01';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                  if (s1.value ==11){
                                    var userName = 'cpydszn';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                  if (s1.value ==12){
                                    var userName = 'cpydkp';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                  if (s1.value ==13){
                                    var userName = 'cpydkpdy';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                  if (s1.value ==14){
                                    var userName = 'cpyd01';
                                    document.getElementById("userLogin").value= userName;
                                    document.getElementById("userPassword").value= userName;
                                }
                                doLogin();
                            }
                        </script>
                        <select id="s1" name="s1" onchange="selectUser()">
                            <option value="0">选择帐号</option>
                            <option value="1">SP联通影视</option>
                            <option value="2">　CP激动高清</option>
                            <option value="3">　CP中录宽频</option>
                            <option value="4">　CP优度</option>
                            <option value="5">　CP激动宽频</option>
                            <option value="6">　CP国艺传媒戏曲</option>
                            <option value="7">　CP华夏视联</option>
                            <option value="8">　CP优朋普乐</option>
                            <option value="9">SP优度宽频影视</option>
                            <option value="10">　CP优度电视专栏</option>
                            <option value="11">　CP优度宽频</option>
                            <option value="12">　CP优度宽频电影</option>
                            <option value="13">　CP优度</option>
                    </select>
        </td>
        </tr> -->

      <tr>
        <td>&nbsp;</td>
        <td height="40">用户名</td>
        <td><input name="userLogin" type="text" id="userLogin"  style="width:180px;"/></td>
        </tr>
      <tr>
        <td>&nbsp;</td>
        <td height="40">密&nbsp;&nbsp;&nbsp;码</td>
        <td><input name="userPassword" type="password" id="userPassword" style="width:180px;"/></td>
        </tr>
      <tr>
        <td>&nbsp;</td>
        <td height="40">验证码</td>
        <td><input name="verifyCode" type="text" id="verifyCode" style="width:120px;"/><a href="#" title="点击更新验证码！"><img src="security/verifyPic.jsp"
                                        id="verifyPic"
                                        onclick="refreshVerifyPic()"
                                        alt="验证码" style="vertical-align:middle" width="50" height="22"/></a></td>
        </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="34">&nbsp;</td>
        <td>&nbsp;</td>
        <td valign="top"><a href="#" onclick="doLogin()"><img src="images/index/b_1.jpg" width="79" height="29" /></a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100">&nbsp;</td>
  </tr>
<!--
  <tr>
    <td height="80"><img src="images/index/copyright.gif" width="980" height="80" /></td>
  </tr>
-->
</table>

<table width="980" border="0" align="center" cellpadding="0" cellspacing="0" background="images/index/copy.gif">
  <tr>
    <td width="300" height="80">&nbsp;</td>
    <td width="680">©Copyright © 2010-2014上海复全网络 
    高铁娱乐综合业务管理平台 021-55239116<br /></td>
  </tr>
</table>

</body>
</html>
