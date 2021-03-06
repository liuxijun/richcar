<%@ page import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.common.Constants" %><%@ page
        import="com.fortune.rms.business.frontuser.model.FrontUser" %><%@ page
        import="com.fortune.rms.business.frontuser.logic.logicInterface.FrontUserLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="java.util.List" %>
<%@ page import="com.fortune.rms.business.publish.model.Channel" %>
<%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    String[] parameterNames = new String[]{"id","parentId","channelId","userId"};
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.login_pad.jsp");
    FrontUser frontUser =(FrontUser)session.getAttribute(Constants.SESSION_FRONT_USER);
    logger.debug("fromUser in session ="+frontUser);
    String userName = request.getParameter("userName");
    String pwd = request.getParameter("password");
    String message=null;
    if(userName!=null&&pwd!=null&&!"".equals(userName.trim())&&!"".equals(pwd)){
        FrontUserLogicInterface frontUserLogicInterface=(FrontUserLogicInterface) SpringUtils.getBean(
                "frontUserLogicInterface",session.getServletContext());
        FrontUser u = new FrontUser();
        u.setUserId(userName);
        List<FrontUser> l = frontUserLogicInterface.search(u);
        if(l!=null&&l.size()>0){
            u = l.get(0);
            if(u.getUserId().equals(userName)&&u.getPassword().equals(pwd)){
                String url = request.getParameter("nextUrl");
                if(url==null){
                    url = "index_pad.html";
                }else{
                    for(String p:parameterNames){
                        if(url.contains("?")){
                            url+="&";
                        }else{
                            url+="?";
                        }
                        String v=request.getParameter(p);
                        if(v!=null){
                            url+=p+"="+v;
                        }
                    }
                }
                if( FrontUser.USER_STATUS_NORMAL.equals(u.getStatus())){
                    // 登录
                    session.setAttribute(Constants.SESSION_FRONT_USER, u);
                    // 解析用户可以观看的所有频道，保存在Session中
                    session.setAttribute(Constants.SESSION_FRONT_USER_CHANNEL, frontUserLogicInterface.getUserAuthorizedChannel( u) );
                    //解析用户可以观看的一级频道,保存在Session中
                    List<Channel> visibleChannelList = frontUserLogicInterface.
                            getTopLevelChannel(frontUserLogicInterface.getUserAuthorizedChannel( u));
                    session.setAttribute(Constants.SESSION_FRONT_USER_TOP_CHANNEL,visibleChannelList);
                    response.sendRedirect(url);
                    return;
                }else{
                    message=("您的账号被锁定，请联系管理员！");
                }
            }else{
                message = "错误的帐号或者口令！请查证后再试试！^_^";
            }
        }else{
            message = "错误的帐号或者口令！请查证后再试试！^o^";
        }
    }else{

    }
%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black"> 

<title>WEICHAI</title>
<link href="css/ipad.css" rel="stylesheet">
<link href="css/user_pad.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script src="js/md5.js"></script>
    <script src="js/utilsParameters.js"></script>
    <script src="js/login.js"></script>
    <script type="text/javascript">
        nextUrl = "index_pad";
    </script>
  <style>
/* Swipe 2 required styles */
.swipe {
  overflow: hidden;
  visibility: hidden;
  position: relative;
  width: 100%;
  margin: 0 auto;
}
.swipe-wrap {
  overflow: hidden;
  position: relative;
}
.swipe-wrap div {
  float:left;
  width:100%;
  position: relative;
}

.swipe-wrap img{ width:65%; padding:0px; margin:0px; float:left; overflow:hidden;}
.swipe-wrap a{ display:block;  overflow:hidden;  padding:0px; margin:0px; overflow:hidden; background-color:#e61c24;}
.swipe-wrap .ad_font{ display:block; width:35%; height:100%; overflow:hidden; float:right;}
.swipe-wrap .ad_font strong{ font-size:22px; font-weight:normal; display:block; margin:33px 40px 15px 26px;}
.swipe-wrap .ad_font em{ font-size:13px; line-height:160%; font-weight:normal; display:block; margin:0 40px 18px 26px;}

#pager{ position:absolute; right:30px; bottom:20px;}
#pager em{display: inline-block; width:9px; height:9px; overflow:hidden; vertical-align: middle; margin:0 4px; color: #333; font-style: normal; background-color:#fff; background: rgba(255, 255, 255, 0.6) !important; filter:alpha(opacity=60); -moz-border-radius: 90px; -webkit-border-radius:90px; border-radius:90px; text-indent:-999em; cursor:pointer;}
#pager em.on{color: #f00; background-color:#fff;  width:9px; height:9px; overflow:hidden; background: rgba(255, 255, 255, 1) !important; filter:alpha(opacity=100); -moz-border-radius: 90px; -webkit-border-radius:90px; border-radius:90px; text-indent:-999em;  cursor:pointer;}
/* END required styles */

</style>
    <script>
    function doLogin(){
        var username = $("#username").val();
        var inputPwd = $("#password_");
        var password = inputPwd.val();
        if(username == null || trim(username) == "" || password == null || trim(password) == "") {
            alert("请输入用户名与密码！");
            return;
        }
        $("#password").val( hex_md5(password));
        inputPwd.val('');
        document.forms[0].submit();
    }
        function onLoaded(){
            <%
            if(null!=message){
            %>alert('<%=message%>');<%
            }
            %>
        }
    </script>
</head>

<body id="tbody" onload="onLoaded()">
<header class="globalheader">
<section class="top_cont">
<div class="logoarea"><a href="index_pad.html">WEICHAI</a></div>
<!--<div class="searcharea"><form><input type="button"  class="btn" onClick="location.href='search.html'"><input type="text" class="sc_input"></form></div>
<div class="userarea">
<div class="no_login"><b class="i_user"></b><a href="login.html">登录</a><span>|</span><a href="reg.html">注册</a></div>
<div class="record"><b class="i_rec"></b><a href="#">我看过的</a><b class="i_arw"></b></div>
<div class="fav"><b class="i_favs"></b><a href="#">收藏</a><b class="i_arw"></b></div>
</div>-->
</section>
</header>

<section class="section fixed">
<div class="login_t fixed">
<form id="loginForm" name="loginForm" action="login_pad.jsp" method="post">
<div class="login_username"><input type="text" name="userName" id="username"></div>
<div class="login_password"><input type="password" name="password_" id="password_"></div>
<div class="login_btn">
<button type="button" class="button" onclick="doLogin('index_pad')">登录</button>
</div>
<!--<div class="login_link"><input checked="true" name="rem" id="rem" type="checkbox"><label for="rem">记住我</label><span>|</span><a href="reg.html">注册账号></a></div>-->
    <input type="hidden" name="password" id="password">
    <input type="hidden" name="command" value="login">
    <%
        for(String p:parameterNames){
            String v = request.getParameter(p);
            if(v!=null){
                %><input type="hidden" name="<%=p%>" value="<%=v%>"><%
            }
        }
    %>
</form>
</div>





</section>




<!--<footer class="nav clearfix">
  <div class="navarea">
<ul>
<li><a href="index.html"><em>首页</em></a></li>
<li><a href="list.html"><em>潍柴动态</em></a></li>
<li><a href="list.html"><em>各地纵横</em></a></li>
<li><a href="list.html"><em>企业文化</em></a></li>
<li><a href="list.html"><em>行业信息</em></a></li>
<li><a href="list.html"><em>党建特色</em></a></li>
<li><a href="list.html"><em>原创专区</em></a></li>
</ul>
</div>
</footer>-->

<script type="text/javascript" src="js/swipe.js"></script>

<script>
var mySwipe = Swipe(document.getElementById('mySwipe'), {
  // startSlide: 4,
  auto: 3000,
  // continuous: true,
  // disableScroll: true,
  // stopPropagation: true,
  callback: function(index, element) {
    slideTab(index);
  }
  // transitionEnd: function(index, element) {}
});
var bullets = document.getElementById('pager').getElementsByTagName('em');
for (var i=0; i < bullets.length; i++) {
  var elem = bullets[i];
  elem.setAttribute('data-tab', i);
  elem.onclick = function(){
    mySwipe.slide(parseInt(this.getAttribute('data-tab'), 10), 500);
  }
}
function slideTab(index){
  var i = bullets.length;
  while (i--) {
    bullets[i].className = bullets[i].className.replace('on',' ');
  }
  bullets[index].className = 'on';
};

// with jQuery
// window.mySwipe = $('#mySwipe').Swipe().data('Swipe');

// url bar hiding
(function() {
    
  var win = window,
      doc = win.document;

  // If there's a hash, or addEventListener is undefined, stop here
  if ( !location.hash || !win.addEventListener ) {

    //scroll to 1
    window.scrollTo( 0, 1 );
    var scrollTop = 1,

    //reset to 0 on bodyready, if needed
    bodycheck = setInterval(function(){
      if( doc.body ){
        clearInterval( bodycheck );
        scrollTop = "scrollTop" in doc.body ? doc.body.scrollTop : 1;
        win.scrollTo( 0, scrollTop === 1 ? 0 : 1 );
      } 
    }, 15 );

    if (win.addEventListener) {
      win.addEventListener("load", function(){
        setTimeout(function(){
          //reset to hide addr bar at onload
          win.scrollTo( 0, scrollTop === 1 ? 0 : 1 );
        }, 0);
      }, false );
    }
  }

})();
</script>
</body>
</html>
