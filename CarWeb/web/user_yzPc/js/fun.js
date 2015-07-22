
function MM_swapImgRestore() { 
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { 
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { 
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { 
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

    // 获取门户首页的URL，如果本页面的URL部署改变的话，请更改该函数的返回值
    function getPortalURL() {
        return "http://www.kdsj2.sx.cn/100/index.html";
    }
    
    // 获取登录错误显示页面的名称, 如果错误信息显示页面名称改变的话，请修改返回值
    function getErrorProcessName() {
        return "loginErrorProcess.html";
    }

    // ------------------------------------------------------------------------
    //本函数用于获取某个cookie的值
    // ------------------------------------------------------------------------
    function GetCookie(sName) {
        // cookies are separated by semicolons
        
        var aCookie = document.cookie.split("; ");
        for (var i=0; i < aCookie.length; i++) {
            // a name/value pair (a crumb) is separated by an equal sign
            var aCrumb = aCookie[i].split("=");
            if (sName == aCrumb[0]) {
                return unescape(aCrumb[1]);
            }
        }
    
        // a cookie with the requested name does not exist
        return "";
    }

    // ------------------------------------------------------------------------
    //本函数用于检查用户名和密码的输入情况
    // ------------------------------------------------------------------------
    function checkLoginForm1() {
	    //check log_name 
	    var loginName;
        loginName = trim(document.frmLogin.userAccount.value);
        document.frmLogin.userAccount.value=loginName;
    	if (loginName=="") {
    	    alert("请输入用户名!");
	        document.frmLogin.userAccount.focus();
    	    document.frmLogin.userAccount.select();
    	    return false;
        }
    
        //check password
        if (document.frmLogin.password.value=="")
        {
            alert("请输入密码!");
            document.frmLogin.password.focus();
            document.frmLogin.password.select();
            return false;
        }
        
        // check if logged
        var userInfo = GetCookie("userInfo");
        if ( (userInfo != null) && (userInfo != "") )
        {
            alert("您已经登录过,请刷新页面！");
            return false;
        }
	
      	return true;
    }    
    
    // ------------------------------------------------------------------------
    //本函数用于去掉字符串左右两边的空格
    // ------------------------------------------------------------------------
    function trim(str) {
        var strTmp ;
        strTmp = rightTrim(leftTrim(str)) ;
        return strTmp ;
    }
        
    // ------------------------------------------------------------------------
    //本函数用于去掉字符串左边的空格
    // ------------------------------------------------------------------------
    function leftTrim(str) {
        if (str.charAt(0) == " ") {
            str = str.slice(1);
            str = leftTrim(str); 
        }
        
        return str;
    }
    
    // ------------------------------------------------------------------------
    //本函数用于去掉字符串右边的空格
    // ------------------------------------------------------------------------
    function rightTrim(str) {
        if (str.length - 1 >= 0 && str.charAt(str.length - 1) == " ") {
            str = str.slice(0, str.length - 1);
            str = rightTrim(str);
        }
        
        return str;
    }
     	
    // ------------------------------------------------------------------------
    //解析是否有参数，如果参数的名字是LoginError的话，则重定向到出错页面
    // ------------------------------------------------------------------------
    function redirectToLoginErrorPage(errorID) {
    	if (errorID == null || errorID == "") {
    	    // Do nothing
    	}
    	else {
            var portalURL = getPortalURL();
            var lastIndex1 = portalURL.lastIndexOf("/");
            var lastIndex2 = portalURL.lastIndexOf("//");
            var portal;
            if (lastIndex1 == -1 || lastIndex1 == 0) {   // 没有"/"
                portal = "http://www.kdsj2.sx.cn/100/";
            }
            else if (lastIndex2 == lastIndex1 - 1) {   // 只有"//"
                portal = portalURL + "/";
            }
            else {                                     // 有"/"
                portal = portalURL.substring(0, lastIndex1 + 1);
            }
            
            window.parent.navigate(portal + getErrorProcessName()
                                   + "?LoginError=" + errorID);
        }
	}
    // ------------------------------------------------------------------------
    //本函数用于显示用户名、密码输入框
    // ------------------------------------------------------------------------
    function print_name_password_field() {  
    
    	
        document.write("                                        <tr>");
        document.write("                                                <td><span class='style1'>用户类型");
        document.write("                                                <select name='typer' style='background-color:#1f5abf;border:1px solid #79d0ff;width:120px; color:#FFFFFF'>");
        document.write("                                                        <option value='1' selected=''>注册用户</option>");
        document.write("                                                                <option value='11'>接入用户</option>");
        document.write("                                                                <option value='2'>卡用户</option> ");
        document.write("                                                         </select>");
        document.write("                                                                 </span></td>");
        document.write("                                        </tr>"); 
        
    	document.write("					<tr>");
    	document.write("						<td><span class='style1'>用&nbsp;&nbsp;&nbsp;&nbsp;户");
    	document.write("						<input name='userAccount' type='text' style='background-color:#1F5ABF;border:1px solid #79D0FF;width:120px;color:#ffffff; ' />");
    	document.write("						  </span></td>");
    	document.write("						</td>");
    	document.write("					</tr>");
    	document.write("					<tr>");
    	document.write("						<td><span class='style1'>密&nbsp;&nbsp;&nbsp;&nbsp;码");
    	document.write("						<input name='password' type='password' style='background:background-color:#1f5abf;border:1px solid #79d0ff;width:120px;color:#ffffff; ' />");
    	document.write("						</td>");
    	document.write("					</tr>");
    }
    
    // ------------------------------------------------------------------------
    //本函数用于显示“用户名”信息
    // ------------------------------------------------------------------------
    function print_username_message() {
        var userInfo = GetCookie("userInfo");
        var last_separator_index = userInfo.lastIndexOf("|");
        var userName = userInfo.substr(last_separator_index + 1);
        
        document.write("                    <tr>");
    	document.write("                      <td align=center><span class='style1'>");
    	document.write("                        <div align=center><b>" + userName + " 您已经成功登录</b></div>");
    	document.write("                      </span></td>");
    	document.write("                    </tr>");
    }
   
    // ------------------------------------------------------------------------
    //本函数用于获取ICP传递过来的参数，并把这些参数放到hidden字段里
    // ------------------------------------------------------------------------
	function writeICPParameter() {
	    // 解析GET请求参数
    	var urlQuery = location.href.split("?");
    	
    	if (urlQuery[1].indexOf("&") == -1) {//只有一个参数
    	    // 一个参数的情况处理过了，这里Do nothing
    	}
    	else {
        	// 参数值对的数组
    	    var urlTerms = urlQuery[1].split("&");
    	    
    	    for (var i = 0; i < urlTerms.length; i++) {
    	        var keyValue = urlTerms[i].split("=");
    	        var key      = keyValue[0];
    	        var value    = keyValue[1];
    	        
    	        if (key == "LoginError") {
    	            document.write("<input name=resultChecker type=HIDDEN value='" + portalURL + "' >");
    	            return;
    	        }
    	        
    	        if (key == "resultChecker") {
    	            writeResultChecker(value);
    	        }
    	        else if (key == "icpId") {
    	            writeIcpId(value);
    	        }
    	        else if (key == "bindingId") {
    	            writeBindingId(value);
    	        }
    	    }
	    }
	}
	
	// ------------------------------------------------------------------------
    //本函数用于把resultChecker参数写入hidden字段里，便于向race server提交
    // ------------------------------------------------------------------------
	function writeResultChecker(icpResultChecker) {
	    if (icpResultChecker == null
            || icpResultChecker == "") 
        {
	        document.write("<input name='resultChecker' type='HIDDEN' value='" + portalURL + "' >");
	    }
	    else {
	        icpResultChecker = unescape(icpResultChecker);
	        document.write("<input name='resultChecker' type='HIDDEN'  value=" + icpResultChecker + ">");
	    }
	}
	    
	// ------------------------------------------------------------------------
    //本函数用于把icpId参数写入hidden字段里，便于向race server提交
    // ------------------------------------------------------------------------
	function writeIcpId(icpid) {
        if (icpid != null 
            && (! icpid == ""))
        {
           document.write("<input name='icpId' type='HIDDEN' value='" + icpid + "'>");
        }
    }
       
	// ------------------------------------------------------------------------
    //本函数用于把bindingId参数写入hidden字段里，便于向race server提交
    // ------------------------------------------------------------------------
	function writeBindingId(bindingid) {
        if (bindingid != null 
            && (! bindingid == ""))
        {
           document.write("<input name='bindingId' type='HIDDEN' value='" + bindingid + "'>");
        }
    }
     //-------------------------------------------------------------------
    //根据帐户类型
    //本函数用于把普通注册的帐号加上CNUS后缀
    //-------------------------------------------------------------------
    function checkLoginForm(){
    	       
        	   if (document.frmLogin.userAccount.value=="")
                {
                    alert("请输入帐号!");
               	    document.frmLogin.userAccount.focus();
                    return false;
                }
                if(document.frmLogin.typer.value == 1)
                {	
                 		 document.frmLogin.userAccount.value = document.frmLogin.userAccount.value+"CNUS";	
                } 
                if(document.frmLogin.typer.value != 1){
                 document.frmLogin.userAccount.value = document.frmLogin.userAccount.value;
                 }      	
              
                if (document.frmLogin.password.value=="")
                {
                    alert("请输入密码!");
                    document.frmLogin.password.focus();
                    return false;
                }
                //alert(document.frmLogin.typer.value+":"+document.frmLogin.userAccount.value+":"+document.frmLogin.password.value);
		frmLogin.submit();
                return true;
    }

    // ------------------------------------------------------------------------
    //还没有登录时，显示登录按钮
    // ------------------------------------------------------------------------
    function print_login_button() {    	
        document.write("                        <td><div align='center'><img src='../images/iptv/dl_dl.gif' width='37' height='17' onclick='checkLoginForm();' /></div></td>");
        document.write("                        <td><div align='center'><a href='http://221.204.252.33:8087/webapps/page/netBusiness/register/licence.jsp'><img src='../images/iptv/dl_zc.gif' width='37' height='17' border='0' /></a></div></td>");
        document.write("                        <td><div align='center'><a href='http://221.204.252.33:8087/webapps/page/netBusiness/findPassword.jsp'><img src='../images/iptv/dl_mmzh.gif' width='71' height='17' border='0'/></a></div></td>");
    }
    
    // ------------------------------------------------------------------------
    //已经登录了，显示退出登录按钮
    // ------------------------------------------------------------------------
    function print_logout_button() {
        document.write("                        <td><div align='center'><a href='http://race.kdsj2.sx.cn:8087/race/Logout' target='_parent'>");
    	document.write("		                  <img src=images/tuichu.gif border=0 width=37 height=16>");
    	document.write("		                </a></div></td>");

	    document.write("                        <td><div align='center'><a href='http://221.204.252.33:8087/webapps/page/netBusiness/ssoLogin.jsp' target='_parent'>");
    	document.write("		                  <img src=images/service.gif border=0 width=85 height=16>");
    	document.write("		                </a></div></td>");


   
    } 
    function print_server(){
    	    // ------------------------------------------------------------------------
    //下面是要传递给race server的参数
    // ------------------------------------------------------------------------
    
    document.write("<input name='loginType' type='HIDDEN' value='C' />");
    
    var portalURL = getPortalURL();
    document.write("<input name='returnUrl' type='HIDDEN' value='" + portalURL + "' />");

    if (location.href.indexOf("?") == -1) {
		// 没有参数，则Do nothing.
	}
	else {
	    // 获取ICP的页面传递过来的参数.
	    writeICPParameter();
	}
}