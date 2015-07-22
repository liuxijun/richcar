<%@page
contentType="text/html;charset=gb2312" %>
 <%@ page import="cn.sh.guanghua.util.tools.ParamTools,
                  cn.sh.guanghua.mediastack.business.normal.PlayLogic"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link rel="stylesheet" href="../../css/font_1.css"/>
<title>用户登陆</title>
 <script src="../../js/changePic.js" language="JavaScript"></script>
</head>
<body onload="window.resizeTo(560,395)" background="../../images/account/b.gif" leftmargin="00" topmargin="0" ><div align="center">
<br/>
<%
    String msg = ParamTools.getParameter(request,"msg","");
    if (msg.equals("session_unvalid")){
        //out.print("登陆时间超长，请重新登陆");
    }
    String returnUrl = ParamTools.getParameter(request,"return_url","");
    returnUrl = java.net.URLDecoder.decode(returnUrl);


%>

        <script language="javascript">
            function checkAll(){
                if (form1.user_id.value==""){
                    alert("请输入用户名");
                    return false;
                }
                if (form1.user_pas.value==""){
                    alert("请输入密码");
                    return false;
                }
            if (form1.rand.value==""){
                alert("请输入认证码");
                return false;
            }

                form1.submit();
                return false;
            }
        </script>
        <script language="javascript">
            function next(){
                if(window.event.keyCode==13){
                     checkAll();
                }
            }
            document.onkeydown= next ;
        </script>


<form method="post" action="UserLogin.jsp" name="form1">
<br/>
<br/>
<br/>
<br/>
<br/>

<table width="200">
<!--<tr>
<td colspan="2" align="center" class="red_b_font">
登陆时间过长，请重新登陆
</td>
</tr>
-->
<tr>
<td>
<nobr>用户名：</nobr>
</td>
<td><input type="text" name="user_id" size="20"></td>
</tr>
<tr>
<td>
密　码：
</td>
<td><input type="password" name="user_pas" size="20"></td>
</tr>
<tr>
<td>
认证码：
</td>
<td><input type="text" name="rand" size="6"><img border="0" src="image.jsp"/></td>
</tr>


<input type="hidden" name="return_url" value="<%=returnUrl%>"/>
<input type="hidden" name="flag" value="play"/>
<tr>
<td align="center" colspan="2">
<br/>
<nobr>
<a href="javascript:checkAll()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image12','','../images/user/button_queding_red.gif',1)"><img name="Image12" border="0" src="../images/user/button_queding.gif" width="73" height="21"/></a>
<a href="#" onClick="javascript:reset()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image13','','../images/user/button_quxiao_red.gif',1)"><img name="Image13" border="0" src="../images/user/button_quxiao.gif" width="73" height="21"/></a>
<a href="register_info.jsp" target="_blank" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image12','','../images/user/button_yhzhc_red.gif',1)"><img name="Image12" border="0" src="../images/user/button_yhzhc.gif" width="73" height="21"/></a>
</nobr>
</td>
</tr>

</table>
</form>
</div>
</body>
</html>
