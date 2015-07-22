<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.product.model.ServiceProduct" %>
<%@ page import="com.fortune.rms.business.product.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@include file="param.jsp"%>
<%@include file="queryString.jsp"%>
<%
    String userId = (String)session.getAttribute("userId");
    if (userId==null){
        response.sendRedirect("1login.jsp?"+queryStr);
        return;
    }    

    CacheLogic cacheLogic= new CacheLogic(request);

    List serviceProducts = cacheLogic.getAllServiceProduct(contentId, spId);

%>
<HTML>
<HEAD>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <META name="GENERATOR" content="IBM WebSphere Studio">
    <META http-equiv="Content-Style-Type" content="text/css">
    <LINK href="css/style.css" rel="stylesheet" type="text/css">
    <TITLE>�����б�</TITLE>
</HEAD>

<BODY bgcolor=#8888FF>
<div class="wapper">
    <div class="t"></div>
    <div class="c">
        <h2 class="f_y">��ѡ�����Ĺ���ʽ</h2>                                                                                                                                                                                                                                                       
    </div>
    <div class="b">
        <center>
            <table width="456" border="0" cellpadding="3" cellspacing="1" bgcolor="#D0E22C" class="black">
                <tr>
                    <td bgcolor="#FFFFFF">

                       <table align="center" width="430"  class="black">

                            <%
                                for (Object serviceProduct1 : serviceProducts) {
                                    ServiceProduct serviceProduct = (ServiceProduct) serviceProduct1;
                                    Product product = cacheLogic.getProduct(serviceProduct.getId());
                                    if (product == null) {
                                        logger.error("��Ӧ�Ĺ����ƷΪ�գ�"+serviceProduct.getId()+":"+serviceProduct.getName());
                                        continue;
                                    }
                            %>
                           <tr>
                               <td align="left" colspan="3"><strong>��</strong></td>
                           </tr>
                           <tr>
                               <td width="250">
                                   <img src="images/unit1.gif"/> &nbsp;
                                   <%
                                       switch (serviceProduct.getType().intValue()) {
                                           case 2:
                                               out.print("���򵥴ε㲥");
                                               break;
                                           case 1:
                                               out.print("�������");
                                               break;
                                           case 3:
                                               out.print("������Ʒ��");
                                               break;
                                       }
                                   %>
                                   <b><font color="red">
                                       <%=serviceProduct.getName()%>
                                   </font></b></td>
                               <td width="90">ʱ�䣺<b><font color="red">
                                   <%
                                       switch (serviceProduct.getType().intValue()) {
                                           case 2:
                                               out.print("24Сʱ");
                                               break;
                                           case 1:
                                               out.print("һ����");
                                               break;
                                           case 3:
                                               out.print("24Сʱ");
                                               break;
                                       }
                                   %>
                               </font></b>
                               </td>
                               <td>
                                   <a href="6buy.jsp?<%=queryStr%>&serviceProductId=<%=serviceProduct.getId()%>&productId=<%=product.getId()%>&productType=<%=product.getType()%>"><b><font
                                           color="red">����(<%=product.getPrice()%>Ԫ)</font></b></a>
                               </td>
                           </tr>
                           <%}%>
                            <tr>
                                <td align="left" colspan="3"><strong> </strong></td>
                            </tr>
                        </table>


                    </td>
                </tr>

                <tr>
                    <td align="center" valign="middle" bgcolor="#F1F5DC"><a href="javascript:jumpToHelp()">��ô�����</a></td>
                </tr>
                <tr>
                    <td align="center">
                        <p class="but" align="center"><a  href="javascript:window.close();" target="_self"><font color="#3366CC">���رմ��ڡ�</font></a></p>
                    </td>
                </tr>
            </table>
        </center>
    </div>
</div>
</BODY>
</HTML><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.HbBuyList.jsp");
%>