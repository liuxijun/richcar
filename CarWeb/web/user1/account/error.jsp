<%@ page contentType="text/html; charset=GBK" %><%@page
import="cn.sh.guanghua.util.tools.ParamTools,
        cn.sh.guanghua.mediastack.common.Constants,
        cn.sh.guanghua.mediastack.common.ConfigManager"%><%
    //获取错误参数
    String msg = ParamTools.getParameter(request,"msg","");
    String msgHint = Constants.USER_ERROR_DEFAULT;

    if("100".equals(msg)){
        msgHint = "用户帐号不存在";
    }else if("101".equals(msg)){
        msgHint = "用户帐号被冻结";
    }else if("102".equals(msg)){
        msgHint = "用户帐号金额不足";
    }else if("103".equals(msg)){
        msgHint = "用户计费请求超出限额";
    }else if("104".equals(msg)){
        msgHint = "用户计费请求超出最多请求次数";
    }else if("105".equals(msg)){
        msgHint = "该用户不能访问此类内容";
    }else if("106".equals(msg)){
        msgHint = "该用户不是应用卡用户";
    }else if("200".equals(msg)){
        msgHint = "该ICP 不存在";
    }else if("201".equals(msg)){
        msgHint = "该栏目不存在";
    }else if("203".equals(msg)){
        msgHint = "该栏目不提供此种费率";
    }else if("204".equals(msg)){
        msgHint = "该ICP 已经被冻结";
    }else if("205".equals(msg)){
        msgHint = "没有该媒体！";
    }else if("206".equals(msg)){
        msgHint = "用户请求缺少栏目信息";
    }else if("300".equals(msg)){
        msgHint = "用户请求参数不足";
    }else if("301".equals(msg)){
        msgHint = "用户请求缺少栏目信息";
    }else if("302".equals(msg)){
        msgHint = "用户请求加密信息错误";
    }else if("303".equals(msg)){
        msgHint = "用户请求缺少ICP信息";
    }else if("304".equals(msg)){
        msgHint = "用户请求缺少计费Session";
    }else if("305".equals(msg)){
        msgHint = "用户请求存在无效的数据格式";
    }else if("306".equals(msg)){
        msgHint = "用户名和密码不对应";
    }else if("307".equals(msg)){
        msgHint = "用户类型错误";
    }else if("308".equals(msg)){
        msgHint = "用户频繁认证请求";
    }else if("309".equals(msg)){
        msgHint = "用户没有取得认证";
    }else if("310".equals(msg)){
        msgHint = "用户授权已经超时";
    }else if("311".equals(msg)){
        msgHint = "用户没有取得授权";
    }else if("312".equals(msg)){
        msgHint = "用户频繁授权请求";
    }else if("313".equals(msg)){
        msgHint = "用户登录状态超时";
    }else if("314".equals(msg)){
        msgHint = "用户重复按次计费请求";
    }else if("315".equals(msg)){
        msgHint = "用户的KEY 没有经过MD5 处理";
    }else if("316".equals(msg)){
        msgHint = "用户请求的MD5 检测失败";
    }else if("400".equals(msg)){
        msgHint = "用户达到系统上线，无法提供服务（只用于登录接口）";
    }else if("99".equals(msg)){
        msgHint = "媒体错误！";
    }else if("98".equals(msg)){
        msgHint = "IP地址错误！";
    }else if("97".equals(msg)){
        msgHint = "没有播放权限！";
    }else if("0".equals(msg) || "8".equals(msg)){
        msgHint = "用户已经包月，但本月已经退订";
    }else if("113".equals(msg)){
        msgHint = "卡用户限制访问此栏目";
    }else if("114".equals(msg)){
        msgHint = "取得卡用户绑定规则出错";
    }else if("115".equals(msg)){
        msgHint = "用户卡类型不存在";
    }else if("116".equals(msg)){
        msgHint = "用户所绑定的规则不存在";
    }else if("117".equals(msg)){
        msgHint = "帐户状态未处于正常状态，不允许消费";
    }else if("501".equals(msg)){
        msgHint = "该用户对于该栏目已经订购";
    }else if("502".equals(msg)){
        msgHint = "该用户本月已经退订此栏目本月不可订购";
    }else if("503".equals(msg)){
        msgHint = "用户对于该大包月所包含的某个子栏目已经包月";
    }else if("504".equals(msg)){
        msgHint = "该用户已经订购的某个大包月已经包含此栏目";
    }else if("505".equals(msg)){
        msgHint = "向RAP发送订购计费失败";
    }else if("506".equals(msg)){
        msgHint = "体验卡用户不能做订购操作";
    }else if("599".equals(msg)){
        msgHint = "订购错误：其它未知原因";
    }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>提示信息</title>
<link href="../../css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0" onload="window.resizeTo(510,305)">
<table width="500" height="305" border="0" cellpadding="0" cellspacing="0" background="../../images/account/123.gif">
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
                        <td height="140" align="left"><%=msgHint%></td>
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