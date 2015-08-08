<%@ page import="com.fortune.util.AppConfigurator" %>
<%@ page import="com.fortune.util.IndividualUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: mlwang
  Date: 2014-10-10
  Time: 15:02:50
  新的登录页面
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    if(!AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
        response.sendRedirect("login_.jsp");
        return;
    }
    String logoUrl = AppConfigurator.getInstance().getConfig("system.loginPageLogo","images/logo2.png");
    /*String logoUrl = IndividualUtils.getInstance().getLogoURL();*/
%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>管理员登录 - <%=IndividualUtils.getInstance().getName()%></title>
    <meta name="description" content="User login page"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="style/bootstrap.min.css"/>
    <link rel="stylesheet" href="style/font-awesome.min.css"/>
    <!-- text fonts -->
    <link rel="stylesheet" href="style/ace-fonts.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="style/ace.min.css"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="style/ace-part2.min.css"/>
    <![endif]-->
    <link rel="stylesheet" href="style/ace-rtl.min.css"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="style/ace-ie.min.css"/>
    <![endif]-->
    <link rel="stylesheet" href="style/ace.onpage-help.css"/>
    <link rel="stylesheet" href="style/doc.min.css?v=11.12"/>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="scripts/html5shiv.js"></script>
    <script src="scripts/respond.min.js"></script>
    <![endif]-->
</head>

<body class="login-layout light-login">
<div class="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center logo2">
                        <img src="<%=logoUrl%>"/>
                    </div>

                    <div class="space-6"></div>

                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="heade">
                                        用户登录
                                    </h4>

                                    <div class="space-6"></div>

                                    <form action=" " id="loginForm">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-left">
                                                            <input type="text" class="form-control" placeholder="用户名"
                                                                   data-rel="tooltip" data-original-title="用户名不存在"
                                                                   name="username"/>
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-left">
                                                            <input type="password" class="form-control" placeholder="密码"
                                                                   data-rel="tooltip" data-original-title="密码不正确"
                                                                   name="userpassword"/>
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>
                                            <label id="verify" class="block clearfix"><span
                                                    class="block input-icon input-icon-left">
                                                            <input type="text" data-original-title="验证码错误"
                                                                   data-rel="tooltip" placeholder="验证码"
                                                                   class="form-control" name="verify"><i
                                                    class="ace-icon fa fa-code"></i> </span> </label>
                                            <label class="block clearfix">
                                                <img src="security/verifyPic.jsp"
                                                                                  style="cursor:pointer;width:330px;height:70px;"
                                                                                  class="verifyimg reloadverify"
                                                                                  id="verifyPic"
                                                                                  onclick="refreshVerifyPic()"
                                                                                  alt="验证码，点击刷新"/>
                                            </label>

                                            <div class="space"></div>

                                            <!--
                                                               <div class="clearfix">
                                                                   <label class="inline">
                                                                       <input type="checkbox" class="ace" />
                                                                       <span class="lbl"> 记住我</span>
                                                                   </label>

                                                               </div>
                                                               // -->
                                            <div class="clearfix">
                                                <a class="btn btn-blue btn-block" id="doLogin">登录</a>
                                            </div>

                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>


                                    <div class="space-6"></div>


                                </div>
                                <!-- /.widget-main -->

                                <div class="toolbar clearfix">
                                    <div>
                                        <!--
                                                          <a href="#" data-target="#forgot-box" class="forgot-password-link">
                                                              <i class="ace-icon fa fa-arrow-left"></i>
                                                              忘记密码？
                                                          </a>
                                                          //-->
                                        &nbsp;
                                    </div>

                                    <div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.widget-body -->
                        </div>
                        <!-- /.login-box -->

                    </div>
                    <!-- /.position-relative -->
                </div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.main-content -->
</div>
<!-- /.main-container -->
<div id="loading_container" class="small_mask">
    <div id="loading" class="central_loading">
        <i class="fa fa-spin fa-spinner bigger-300"></i>
        <i style="display:block;margin:10px auto">正在加载</i>
        <!--<img src="images/Spinner.gif" alt="获取数据"/>//-->
    </div>
</div>

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='scripts/jquery.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='scripts/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='scripts/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="scripts/bootstrap.min.js"></script>

<script src="scripts/jquery-ui.custom.min.js"></script>
<script src="scripts/jquery.ui.touch-punch.min.js"></script>
<script src="scripts/bootbox.min.js"></script>
<script src="scripts/jquery.easypiechart.min.js"></script>
<script src="scripts/jquery.gritter.min.js"></script>
<script src="scripts/spin.min.js"></script>
<script type="text/javascript" src="js/md5.js"></script>

<!-- ace scripts -->
<script src="scripts/ace.min.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function($) {
        $("#loginForm input[name='username']").focus();
        
        $(document).on('click', '.toolbar a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');//hide others
            $(target).addClass('visible');//show target
        });

        $(document).keydown(function( event ) {
            if ( event.which == 13 ) {
                event.preventDefault();
                logon();
            }
        });

        $("#doLogin").click(function() {
           logon();
        });
        $(document).ajaxStart(function(){
            $("#loading_container").show();
        });

        $(document).ajaxStop(function(){
            setTimeout(function(){$("#loading_container").hide();}, 200);
        });
    });


    $(document).ready(function() {
        $('[data-rel=tooltip]').tooltip({
            trigger: 'manual',
            placement: 'right'
        });
        $('[data-rel=tooltip]').on('shown.bs.tooltip', function() {
            $('.tooltip').css({
                left: 'auto',
                right: "0"
            });
        })
        $('[data-rel=tooltip]').click(function() {
            $(this).tooltip('hide');
        });
    })

    function logon() {
        if($.trim($("#loginForm input[name='username']").val()) == ""){
            $("#loginForm input[name='username']").attr("data-original-title","请输入登录名");
             $("#loginForm input[name='username']").addClass('tooltip-error').tooltip('show');
            return;
        }
        
        if($.trim($("#loginForm input[name='userpassword']").val()) == ""){
            $("#loginForm input[name='userpassword']").attr("data-original-title","请输入密码");
             $("#loginForm input[name='userpassword']").addClass('tooltip-error').tooltip('show');
            return;
        }

        if($.trim($("#loginForm input[name='verify']").val()) == ""){
            $("#loginForm input[name='verify']").attr("data-original-title","请输入验证码");
             $("#loginForm input[name='verify']").addClass('tooltip-error').tooltip('show');
            return;
        }

        submitLogon();
    }

    function submitLogon(){
        var uname = $.trim($("#loginForm input[name='username']").val());
        var upwd = $.trim($("#loginForm input[name='userpassword']").val());
        var ver = $.trim($("#loginForm input[name='verify']").val());

        $.ajax({
            type: "POST",
            url: "/security/login.action",
            //dataType: "json",
            dataType: "text",
            data: {'obj.login':uname,'obj.password':hex_md5(upwd),verifyCode:ver},
            //data: {name: ver},
            success: function(msg){
                //var response = $.parseJSON(msg);
                var response = eval("(function(){return " + msg + ";})()");
                //var response = JSON.parse(msg);
                if (response.success) {
                    window.location = "man.jsp";
                } else {
                    //alert('登陆失败，请仔细检查输入是否正确！\n错误提示：'+data.error[0]);
                    var errorMsg = response.error[0];
                    if(errorMsg.indexOf("验证码") >= 0){
                        $("#loginForm input[name='verify']").attr("data-original-title","验证码错误");
                         $("#loginForm input[name='verify']").addClass('tooltip-error').tooltip('show');
                    }else{
                        $("#loginForm input[name='userpassword']").attr("data-original-title","帐号或者口令输入错误");
                         $("#loginForm input[name='userpassword']").addClass('tooltip-error').tooltip('show');
                    }
                }
             }
        });
    }
    
    function refreshVerifyPic() {
        //$("#verifyPic").src = "security/verifyPic.jsp?dc=" + new Date().getTime();
        $("#verifyPic").attr("src","security/verifyPic.jsp?dc=" + new Date().getTime());
    }
</script>
</body>
</html>
