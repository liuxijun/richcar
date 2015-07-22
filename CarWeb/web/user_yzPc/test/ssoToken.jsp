<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="param.jsp"%>

<%!
public String getCookie(javax.servlet.http.HttpServletRequest request,String cookieName){
    try{
        Cookie[]  cks  =  request.getCookies();
        if(cks!=null){
            for(int j=0;j<cks.length;j++){
                if (cookieName.equals(cks[j].getName())){
                    String middle=java.net.URLDecoder.decode(cks[j].getValue());//重点！
                    return new String(middle.getBytes("GB2312"),"iso-8859-1");//进行编码，使内容在网页中显示正常。
                }
            }
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return "";
}

%>
<%

String homeEntry = getCookie(request,"homeEntry");
if ("".equals(homeEntry)){
	homeEntry = "02";
}
 
    String returnURL = getParameter(request,"return_url","");
%>
<HTML>
<HEAD>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <META name="GENERATOR" content="IBM WebSphere Studio">
    <META http-equiv="Content-Style-Type" content="text/css">
    <LINK href="theme/Master.css" rel="stylesheet" type="text/css">
    <TITLE>requestvaluetobcg.jsp</TITLE>
</HEAD>
<BODY>
<table>
    <tr>
        <td>
            请稍候...
        </td>
    </tr>
</table>
<!--设置重定向的目标地址-->                                     
<form method="post" action="<%=ccitCheckLogin%>" name="form1">

    <!-- 设置requestName和requestValue -->
    <input type="hidden" name="SPID" value="<%=spid%>" >
    <input type="hidden" name="type" value="<%=homeEntry%>" >
    <input type="hidden" name="ReturnURL" value="<%=returnURL%>" >
</form>

<script language="JavaScript" >
    document.form1.submit();
</script>
</BODY>
</HTML>