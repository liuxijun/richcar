
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

    // ��ȡ�Ż���ҳ��URL�������ҳ���URL����ı�Ļ�������ĸú����ķ���ֵ
    function getPortalURL() {
        return "http://www.kdsj2.sx.cn/100/index.html";
    }
    
    // ��ȡ��¼������ʾҳ�������, ���������Ϣ��ʾҳ�����Ƹı�Ļ������޸ķ���ֵ
    function getErrorProcessName() {
        return "loginErrorProcess.html";
    }

    // ------------------------------------------------------------------------
    //���������ڻ�ȡĳ��cookie��ֵ
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
    //���������ڼ���û�����������������
    // ------------------------------------------------------------------------
    function checkLoginForm1() {
	    //check log_name 
	    var loginName;
        loginName = trim(document.frmLogin.userAccount.value);
        document.frmLogin.userAccount.value=loginName;
    	if (loginName=="") {
    	    alert("�������û���!");
	        document.frmLogin.userAccount.focus();
    	    document.frmLogin.userAccount.select();
    	    return false;
        }
    
        //check password
        if (document.frmLogin.password.value=="")
        {
            alert("����������!");
            document.frmLogin.password.focus();
            document.frmLogin.password.select();
            return false;
        }
        
        // check if logged
        var userInfo = GetCookie("userInfo");
        if ( (userInfo != null) && (userInfo != "") )
        {
            alert("���Ѿ���¼��,��ˢ��ҳ�棡");
            return false;
        }
	
      	return true;
    }    
    
    // ------------------------------------------------------------------------
    //����������ȥ���ַ����������ߵĿո�
    // ------------------------------------------------------------------------
    function trim(str) {
        var strTmp ;
        strTmp = rightTrim(leftTrim(str)) ;
        return strTmp ;
    }
        
    // ------------------------------------------------------------------------
    //����������ȥ���ַ�����ߵĿո�
    // ------------------------------------------------------------------------
    function leftTrim(str) {
        if (str.charAt(0) == " ") {
            str = str.slice(1);
            str = leftTrim(str); 
        }
        
        return str;
    }
    
    // ------------------------------------------------------------------------
    //����������ȥ���ַ����ұߵĿո�
    // ------------------------------------------------------------------------
    function rightTrim(str) {
        if (str.length - 1 >= 0 && str.charAt(str.length - 1) == " ") {
            str = str.slice(0, str.length - 1);
            str = rightTrim(str);
        }
        
        return str;
    }
     	
    // ------------------------------------------------------------------------
    //�����Ƿ��в��������������������LoginError�Ļ������ض��򵽳���ҳ��
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
            if (lastIndex1 == -1 || lastIndex1 == 0) {   // û��"/"
                portal = "http://www.kdsj2.sx.cn/100/";
            }
            else if (lastIndex2 == lastIndex1 - 1) {   // ֻ��"//"
                portal = portalURL + "/";
            }
            else {                                     // ��"/"
                portal = portalURL.substring(0, lastIndex1 + 1);
            }
            
            window.parent.navigate(portal + getErrorProcessName()
                                   + "?LoginError=" + errorID);
        }
	}
    // ------------------------------------------------------------------------
    //������������ʾ�û��������������
    // ------------------------------------------------------------------------
    function print_name_password_field() {  
    
    	
        document.write("                                        <tr>");
        document.write("                                                <td><span class='style1'>�û�����");
        document.write("                                                <select name='typer' style='background-color:#1f5abf;border:1px solid #79d0ff;width:120px; color:#FFFFFF'>");
        document.write("                                                        <option value='1' selected=''>ע���û�</option>");
        document.write("                                                                <option value='11'>�����û�</option>");
        document.write("                                                                <option value='2'>���û�</option> ");
        document.write("                                                         </select>");
        document.write("                                                                 </span></td>");
        document.write("                                        </tr>"); 
        
    	document.write("					<tr>");
    	document.write("						<td><span class='style1'>��&nbsp;&nbsp;&nbsp;&nbsp;��");
    	document.write("						<input name='userAccount' type='text' style='background-color:#1F5ABF;border:1px solid #79D0FF;width:120px;color:#ffffff; ' />");
    	document.write("						  </span></td>");
    	document.write("						</td>");
    	document.write("					</tr>");
    	document.write("					<tr>");
    	document.write("						<td><span class='style1'>��&nbsp;&nbsp;&nbsp;&nbsp;��");
    	document.write("						<input name='password' type='password' style='background:background-color:#1f5abf;border:1px solid #79d0ff;width:120px;color:#ffffff; ' />");
    	document.write("						</td>");
    	document.write("					</tr>");
    }
    
    // ------------------------------------------------------------------------
    //������������ʾ���û�������Ϣ
    // ------------------------------------------------------------------------
    function print_username_message() {
        var userInfo = GetCookie("userInfo");
        var last_separator_index = userInfo.lastIndexOf("|");
        var userName = userInfo.substr(last_separator_index + 1);
        
        document.write("                    <tr>");
    	document.write("                      <td align=center><span class='style1'>");
    	document.write("                        <div align=center><b>" + userName + " ���Ѿ��ɹ���¼</b></div>");
    	document.write("                      </span></td>");
    	document.write("                    </tr>");
    }
   
    // ------------------------------------------------------------------------
    //���������ڻ�ȡICP���ݹ����Ĳ�����������Щ�����ŵ�hidden�ֶ���
    // ------------------------------------------------------------------------
	function writeICPParameter() {
	    // ����GET�������
    	var urlQuery = location.href.split("?");
    	
    	if (urlQuery[1].indexOf("&") == -1) {//ֻ��һ������
    	    // һ�����������������ˣ�����Do nothing
    	}
    	else {
        	// ����ֵ�Ե�����
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
    //���������ڰ�resultChecker����д��hidden�ֶ��������race server�ύ
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
    //���������ڰ�icpId����д��hidden�ֶ��������race server�ύ
    // ------------------------------------------------------------------------
	function writeIcpId(icpid) {
        if (icpid != null 
            && (! icpid == ""))
        {
           document.write("<input name='icpId' type='HIDDEN' value='" + icpid + "'>");
        }
    }
       
	// ------------------------------------------------------------------------
    //���������ڰ�bindingId����д��hidden�ֶ��������race server�ύ
    // ------------------------------------------------------------------------
	function writeBindingId(bindingid) {
        if (bindingid != null 
            && (! bindingid == ""))
        {
           document.write("<input name='bindingId' type='HIDDEN' value='" + bindingid + "'>");
        }
    }
     //-------------------------------------------------------------------
    //�����ʻ�����
    //���������ڰ���ͨע����ʺż���CNUS��׺
    //-------------------------------------------------------------------
    function checkLoginForm(){
    	       
        	   if (document.frmLogin.userAccount.value=="")
                {
                    alert("�������ʺ�!");
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
                    alert("����������!");
                    document.frmLogin.password.focus();
                    return false;
                }
                //alert(document.frmLogin.typer.value+":"+document.frmLogin.userAccount.value+":"+document.frmLogin.password.value);
		frmLogin.submit();
                return true;
    }

    // ------------------------------------------------------------------------
    //��û�е�¼ʱ����ʾ��¼��ť
    // ------------------------------------------------------------------------
    function print_login_button() {    	
        document.write("                        <td><div align='center'><img src='../images/iptv/dl_dl.gif' width='37' height='17' onclick='checkLoginForm();' /></div></td>");
        document.write("                        <td><div align='center'><a href='http://221.204.252.33:8087/webapps/page/netBusiness/register/licence.jsp'><img src='../images/iptv/dl_zc.gif' width='37' height='17' border='0' /></a></div></td>");
        document.write("                        <td><div align='center'><a href='http://221.204.252.33:8087/webapps/page/netBusiness/findPassword.jsp'><img src='../images/iptv/dl_mmzh.gif' width='71' height='17' border='0'/></a></div></td>");
    }
    
    // ------------------------------------------------------------------------
    //�Ѿ���¼�ˣ���ʾ�˳���¼��ť
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
    //������Ҫ���ݸ�race server�Ĳ���
    // ------------------------------------------------------------------------
    
    document.write("<input name='loginType' type='HIDDEN' value='C' />");
    
    var portalURL = getPortalURL();
    document.write("<input name='returnUrl' type='HIDDEN' value='" + portalURL + "' />");

    if (location.href.indexOf("?") == -1) {
		// û�в�������Do nothing.
	}
	else {
	    // ��ȡICP��ҳ�洫�ݹ����Ĳ���.
	    writeICPParameter();
	}
}