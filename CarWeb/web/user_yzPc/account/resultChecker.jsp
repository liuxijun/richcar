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
     * ��ҳ����Ҫ�������£�
     * �жϷ����Ƿ��в��������û�в�����Ҫ�ض����Ż�������Ϳ��ŷ���ҳ�棩��
     * ����в����ʹӷ������Ƕ���ȡ�û���¼����Ϣ
     * ����������û���¼��Ϣ��дCookie������������ɹ�������£��򿪷ŷ���ҳ�棩
     *                    
     */
    //System.out.println("erter");
    String paramString = "";
    String ispURL = ConfigManager.getConfig().node("account").get("ISPURL","");
    String resultChecker = ConfigManager.getConfig().node("account").get("resultChecker","");
    //�ض����Ż���¼
    String redirectURL = ispURL  + "&resultChecker=" + java.net.URLEncoder.encode(resultChecker)
			+ "&bindingId=" +  System.currentTimeMillis()+"123456";

    String bindingID = request.getParameter("bindingId"); //��resultGetter.jsp�������Ĳ���

    /* �жϷ����Ƿ��в��������û�в�����Ҫ�ض����Ż�������Ϳ��ŷ���ҳ�棩*/
    if(null == bindingID || bindingID.equals("") || bindingID.equals("null")){
        //�����Ż�
        //response.sendRedirect(redirectURL);
    } else {	
        
        /* ��ICP��������ȡ�û���¼����Ϣ*/    
	    ICPLoginInfoContainer icpLoginInfoContainer = ICPLoginInfoContainer.getInstance();
    	LoginUserInfo loginUserInfo = icpLoginInfoContainer.getLoginUserInfo(bindingID);
        icpLoginInfoContainer.removeLoginInfo(bindingID);
    	
    	/* �ж��Ƿ��Ѿ������û���¼��Ϣ�� */
    	if(loginUserInfo == null){
    	   //û�У����ض����Ż�
		   //response.sendRedirect(redirectURL);
    	} else {
    	    //�ɹ�����þ������Ϣ����дCookie
    	    String userInfo = loginUserInfo.getUserInfo();
	        //System.out.println("�ɹ�����þ������Ϣ��userInfo     = " + userInfo);
	        //System.out.println("�ɹ�����þ������Ϣ��sessionId = " + sessionId);
            SessionUser su = new SessionUser();
            su.setBindingId(loginUserInfo.getBindingID());
            su.setSessionId(loginUserInfo.getSessionID());
            su.setUserInfo(userInfo);
            //�õ��û�id
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
<title>��ʾ��Ϣ</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0" onload="window.resizeTo(560,395)">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="../../images/account/b.gif">
  <tbody>
  <tr>
    <td align="left" valign="top"><table width="559" height="290" border="0" cellpadding="0" cellspacing="0">
        <tbody>
        <tr>
          <td height="72">��</td>
        </tr>
        <tr>
          <td align="center" valign="top"><table width="559" height="218" border="0" cellpadding="0" cellspacing="0">
              <tbody>
              <tr>
                <td width="86">��</td>
                <td width="473" align="left" valign="top"><table width="377" height="201" border="0" cellpadding="2" cellspacing="0" class="black">
                      <tbody>
                      <tr valign="middle" bgcolor="#E8E8E8">
                        <td align="left"> <div align="center"><strong><font color="#FF3300">�û��㲥�����쳣��</font></strong></div></td>
                      </tr>
                      <tr valign="middle">
                      <script language="javascript">
                              function openWin(){
                                window.close();
                                window.open('<%=redirectURL%>','_blank','resizable=yes');
                              }
                      </script>
                        <td height="140" align="left">����û�е�¼�����<a href="javascript:openWin()">����</a>��¼</td>
                      </tr>
                      <tr align="center" valign="middle">
                        <td>[ <a href="javascript:window.close();">�رմ���</a> ]</td>
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