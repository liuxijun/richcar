<%
/**
 * Copyright ?2002 Shanghai Fudan Ghuanghua Co. Ltd.
 * All right reserved.
 *
 * @�ļ����ƣ�AccountDo.jsp
 * @��Ҫ���ܣ�
 * ��ʾ�����Ľ����Ϣ��
 * @���������
 * 1. useracount_id���������Ϣ
 * 2. command(add��delete)
 * @���������
 * �����ɹ����
 * @��    �ߣ�zbxue
 * @��    �ڣ�
 *    @��ʼʱ�䣺 2004-09-02
 *    @����ʱ�䣺
 *
 *    @�޸���Ա��
 *    @�޸�������ԭ��
 *
 */
%><%@page
contentType="text/html;charset=gb2312" %><%@page
import="java.util.*,
        cn.sh.guanghua.mediastack.common.ConfigManager,
        cn.sh.guanghua.mediastack.user.UserLogic,
        cn.sh.guanghua.mediastack.user.SessionUser" %><%@page
import="cn.sh.guanghua.mediastack.dataunit.*" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools" %><%@page
import="cn.sh.guanghua.util.tools.MD5" %><%@page
import="cn.sh.guanghua.mediastack.common.Constants"%><%@page
import="cn.sh.guanghua.mediastack.common.Db"%><%@page
import="cn.sh.guanghua.mediastack.common.Logger"%><%@page
import="cn.sh.guanghua.util.tools.*" %><%

        String msg = ParamTools.getParameter(request,"msg","ע����ԭ�ɹ���");
        String flag =  ParamTools.getParameter(request,"flag","");
        String returnUrl =  ParamTools.getParameter(request,"return_url","");
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
                        <td align="left"> <div align="center"><strong><font color="blue">�١�����״��̬���ᡡʾ</font></strong></div></td>
                      </tr>
                      <tr valign="middle">
                        <td height="140" align="left"><%=msg%>
                        <%
                        if (!"".equals(returnUrl) ){
                             out.print("<a href='"+returnUrl+"'>����</a>");
                        }

                        %>
                        </td>
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