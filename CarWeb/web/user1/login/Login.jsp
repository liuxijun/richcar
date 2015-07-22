<%@ page import="cn.sh.guanghua.util.tools.ParamTools"%>
<%@ page contentType="text/html; charset=GBK" %>
<%
    String returnUrl = ParamTools.getParameter(request,"return_url","");
    String bindingId = ""+System.currentTimeMillis();
    String icpId = "12548";
%>

<html>
<head>
<title>登录窗口</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.STYLE1 {font-size: 12px}
.STYLE2 {color: #7D3A27}
.STYLE4 {font-size: 12px; color: #783827; }
-->
</style>
</head>

<body  topmargin='0' leftmargin='0'  onkeydown="OnLogin()"  onload='window.resizeTo(515,343)'>
<table width="510" height="280" border="0" cellpadding="0" cellspacing="0" background="/images/newuser/dlck.gif">
	<tr>
		<td colspan="5" width="510" height="244">
		<table width="100%" border="0">
  <tr>
    <td width="132" height="109"></td>
    <td width="238" height="109"></td>
    <td width="140" height="109"></td>
  </tr>
  <tr>
    <td width="132" height="127"></td>
    <td width="238" height="127">

<script language="javascript">
    function OnLogin(){
        if(window.event.keyCode==13){
            checkData();
        }
    }
	function checkData(){
		if (form1.user_type.value=='0'){
			alert('请选择用户类型');
            return false;
		}
		if (form1.userAccount.value==''){
			alert('请输入用户名');
            return false;
		}
		if (form1.password.value==''){
			alert('请输入密码');
            return false;
		}
        if (form1.user_type.value=='2'){
            form1.userAccount.value=form1.userAccount.value+'CNUS';
        }
        form1.submit();
        return true;
	}
</script>

<form name="form1" action="http://race.kdsj2.sx.cn:8087/race/UnifiedLogin" method="post" onSubmit="return checkData()">
<input name=loginType type="HIDDEN" value="C">
<input name=bindingId type="HIDDEN" value="<%=bindingId%>">
<input name=icpId type="HIDDEN" value="<%=icpId%>">
<input name=resultChecker type="HIDDEN" value="<%=returnUrl%>">
<input name=returnUrl  type="HIDDEN" value="http://mediastack.kdsj2.sx.cn/user/login/LoginError.jsp?return_url=<%=java.net.URLEncoder.encode(returnUrl)%>">


      <table width="100%" border="0">
        <tr>
          <td width="33%" height="30"><div align="left"><span class="STYLE4">用户类型：</span></div></td>
          <td width="67%"><label>
            <select name="user_type">
              <option value="0">选择用户类型</option>
              <option value="1">联通接入用户</option>
              <option value="2">注册用户</option>
              <option value="3">卡用户</option>
            </select>
          </label></td>
        </tr>
        <tr>
          <td height="30"><div align="left"><span class="STYLE4">用 户 名：</span></div>
          </div></td>
          <td><label>
            <input name="userAccount" type="text" size="13">
          </label></td>
        </tr>
        <tr>
          <td height="30"><div align="left"><span class="STYLE4">密　　码：</span></div>
          </div></td>
          <td><input name="password" type="password" size="13"></td>
        </tr>
      </table>
        </form>
    </td>
    <td width="140" height="127"></td>
  </tr>
  <tr>
    <td width="132" height="8"></td>
    <td width="238" height="8"></td>
    <td width="140" height="8"></td>
  </tr>
</table>
	   </td>
	</tr>
	<tr>
		<td width="132" height="36"></td>
		<td>
			<img src="/images/newuser/dl.gif" width="72" height="36" alt="" onClick="checkData()"></td>
		<td>
			<a href="http://221.204.252.33:8087/webapps/page/netBusiness/register/licence.jsp" target="_blank"><img src="/images/newuser/zc.gif" alt="" width="71" height="36" border="0"></a></td>
		<td>
			<a href="http://221.204.252.33:8087/webapps/page/netBusiness/findPassword.jsp" target="_blank"><img src="/images/newuser/mm.gif" alt="" width="95" height="36" border="0"></a></td>
		<td width="140" height="36"></td>
	</tr>
</table>
</body>
</html>