<%@ page contentType="text/html;charset=gb2312"%><%@ page
    language="java" %><%@ page
    import = "com.runway.race.ipm.*,
              cn.sh.guanghua.mediastack.user.SessionUser,
              cn.sh.guanghua.util.tools.ParamTools,
              cn.sh.guanghua.util.tools.StringTools,
              cn.sh.guanghua.mediastack.common.Constants,
              cn.sh.guanghua.mediastack.common.ConfigManager" %><%@ include file="../sxCookies.jsp" %>
<%
    /*
     * 本页面主要做三件事：
     * 判断返回是否有参数（如果没有参数则要重定向到门户，否则就开放访问页面）、
     * 如果有参数就从服务器那儿读取用户登录的信息
     * 如果读到了用户登录信息就写Cookie（上述如果都成功的情况下，则开放访问页面）
     *                    
     */
    //System.out.println("erter");
    String paramString = "";
    String ispURL = ConfigManager.getConfig().node("account").get("ISPURL","");
    String resultChecker = ConfigManager.getConfig().node("account").get("resultChecker","");
    //重定向到门户登录
    String redirectURL = ispURL  + "&resultChecker=" + java.net.URLEncoder.encode(resultChecker)
			+ "&bindingId=" +  System.currentTimeMillis()+"123456";

    String bindingID = request.getParameter("bindingId"); //从resultGetter.jsp传过来的参数

    /* 判断返回是否有参数（如果没有参数则要重定向到门户，否则就开放访问页面）*/
    if(null == bindingID || bindingID.equals("") || bindingID.equals("null")){
        //跳到门户
        //response.sendRedirect(redirectURL);
    } else {	
        
        /* 向ICP服务器读取用户登录的信息*/    
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	LoginUserInfo loginUserInfo = icpLoginInfoContainer.getLoginUserInfo(bindingID);
        icpLoginInfoContainer.removeLoginInfo(bindingID);
    	
    	/* 判断是否已经读到用户登录信息量 */
    	if(loginUserInfo == null){
    	   //没有，则重定向到门户
		   //response.sendRedirect(redirectURL);
    	} else {
    	    //成功：获得具体的信息，并写Cookie
    	    String userInfo = loginUserInfo.getUserInfo();
	        //System.out.println("成功：获得具体的信息，userInfo     = " + userInfo);
	        //System.out.println("成功：获得具体的信息，sessionId = " + sessionId);
            SessionUser su = new SessionUser();
            su.setBindingId(loginUserInfo.getBindingID());
            su.setSessionId(loginUserInfo.getSessionID());
            su.setUserInfo(userInfo);
            //得到用户id
            su.setUserId(StringTools.getSubString(userInfo,"|",5));
            session.setAttribute(Constants.SESSION_USER,su);
            paramString = (String)session.getAttribute("PARAM_STRING");
		    response.sendRedirect("../Play.jsp?"+paramString + "&binding_id="+bindingID);
            return;
    	}
    }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>提示信息</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0" onload="window.resizeTo(560,395)">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/b.gif">
  <tbody>
  <tr>
    <td align="left" valign="top"><table width="559" height="290" border="0" cellpadding="0" cellspacing="0">
        <tbody>
        <tr>
          <td height="72">　</td>
        </tr>
        <tr>
          <td align="center" valign="top"><table width="559" height="218" border="0" cellpadding="0" cellspacing="0">
              <tbody>
              <tr>
                <td width="86">　</td>
                <td width="473" align="left" valign="top"><table width="377" height="201" border="0" cellpadding="2" cellspacing="0" class="black">
                      <tbody>
                      <tr valign="middle" bgcolor="#E8E8E8">
                        <td align="left"> <div align="center"><strong><font color="#FF3300">用户点播出现异常！</font></strong></div></td>
                      </tr>
                      <tr valign="middle">
                      <script language="javascript">
                              function openWin(){
                                window.close();
                                window.open('<%=redirectURL%>','_blank','resizable=yes');
                              }
                      </script>
                        <td height="140" align="left">您还没有登录，点击<a href="javascript:openWin()">这里</a>登录</td>
                      </tr>
                      <tr align="center" valign="middle">
                        <td>[ <a href="javascript:window.close();">关闭窗口</a> ]</td>
                      </tr>
                    </tbody>
                  </table></td>
              </tr>
            </tbody>
            </table></td>
        </tr>
      </tbody>
      </table></td>
  </tr>
</tbody>
</table>
</body>
</html>