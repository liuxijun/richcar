<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.common.ConfigManager"%><%
    //��ȡ�������
    String msg = ParamTools.getParameter(request,"msg","");
    String msgHint = Constants.USER_ERROR_DEFAULT;

    if("100".equals(msg)){
        msgHint = "�û��ʺŲ�����";
    }else if("101".equals(msg)){
        msgHint = "�û��ʺű�����";
    }else if("102".equals(msg)){
        msgHint = "�û��ʺŽ���";
    }else if("103".equals(msg)){
        msgHint = "�û��Ʒ����󳬳��޶�";
    }else if("104".equals(msg)){
        msgHint = "�û��Ʒ����󳬳�����������";
    }else if("105".equals(msg)){
        msgHint = "���û����ܷ��ʴ�������";
    }else if("106".equals(msg)){
        msgHint = "���û�����Ӧ�ÿ��û�";
    }else if("200".equals(msg)){
        msgHint = "��ICP ������";
    }else if("201".equals(msg)){
        msgHint = "����Ŀ������";
    }else if("203".equals(msg)){
        msgHint = "����Ŀ���ṩ���ַ���";
    }else if("204".equals(msg)){
        msgHint = "��ICP �Ѿ�������";
    }else if("205".equals(msg)){
        msgHint = "û�и�ý�壡";
    }else if("206".equals(msg)){
        msgHint = "�û�����ȱ����Ŀ��Ϣ";
    }else if("300".equals(msg)){
        msgHint = "�û������������";
    }else if("301".equals(msg)){
        msgHint = "�û�����ȱ����Ŀ��Ϣ";
    }else if("302".equals(msg)){
        msgHint = "�û����������Ϣ����";
    }else if("303".equals(msg)){
        msgHint = "�û�����ȱ��ICP��Ϣ";
    }else if("304".equals(msg)){
        msgHint = "�û�����ȱ�ټƷ�Session";
    }else if("305".equals(msg)){
        msgHint = "�û����������Ч�����ݸ�ʽ";
    }else if("306".equals(msg)){
        msgHint = "�û��������벻��Ӧ";
    }else if("307".equals(msg)){
        msgHint = "�û����ʹ���";
    }else if("308".equals(msg)){
        msgHint = "�û�Ƶ����֤����";
    }else if("309".equals(msg)){
        msgHint = "�û�û��ȡ����֤";
    }else if("310".equals(msg)){
        msgHint = "�û���Ȩ�Ѿ���ʱ";
    }else if("311".equals(msg)){
        msgHint = "�û�û��ȡ����Ȩ";
    }else if("312".equals(msg)){
        msgHint = "�û�Ƶ����Ȩ����";
    }else if("313".equals(msg)){
        msgHint = "�û���¼״̬��ʱ";
    }else if("314".equals(msg)){
        msgHint = "�û��ظ����μƷ�����";
    }else if("315".equals(msg)){
        msgHint = "�û���KEY û�о���MD5 ����";
    }else if("316".equals(msg)){
        msgHint = "�û������MD5 ���ʧ��";
    }else if("400".equals(msg)){
        msgHint = "�û��ﵽϵͳ���ߣ��޷��ṩ����ֻ���ڵ�¼�ӿڣ�";
    }else if("99".equals(msg)){
        msgHint = "ý�����";
    }else if("98".equals(msg)){
        msgHint = "IP��ַ����";
    }else if("97".equals(msg)){
        msgHint = "û�в���Ȩ�ޣ�";
    }else if("0".equals(msg) || "8".equals(msg)){
        msgHint = "�û��Ѿ����£��������Ѿ��˶�";
    }else if("113".equals(msg)){
        msgHint = "���û����Ʒ��ʴ���Ŀ";
    }else if("114".equals(msg)){
        msgHint = "ȡ�ÿ��û��󶨹������";
    }else if("115".equals(msg)){
        msgHint = "�û������Ͳ�����";
    }else if("116".equals(msg)){
        msgHint = "�û����󶨵Ĺ��򲻴���";
    }else if("117".equals(msg)){
        msgHint = "�ʻ�״̬δ��������״̬������������";
    }else if("501".equals(msg)){
        msgHint = "���û����ڸ���Ŀ�Ѿ�����";
    }else if("502".equals(msg)){
        msgHint = "���û������Ѿ��˶�����Ŀ���²��ɶ���";
    }else if("503".equals(msg)){
        msgHint = "�û����ڸô������������ĳ������Ŀ�Ѿ�����";
    }else if("504".equals(msg)){
        msgHint = "���û��Ѿ�������ĳ��������Ѿ���������Ŀ";
    }else if("505".equals(msg)){
        msgHint = "��RAP���Ͷ����Ʒ�ʧ��";
    }else if("506".equals(msg)){
        msgHint = "���鿨�û���������������";
    }else if("599".equals(msg)){
        msgHint = "������������δ֪ԭ��";
    }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ʾ��Ϣ</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0" onload="window.resizeTo(510,305)">
<table width="500" height="305" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
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
                        <td height="140" align="left"><%=msgHint%></td>
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