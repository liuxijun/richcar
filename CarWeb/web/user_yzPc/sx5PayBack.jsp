<%@ page
contentType="text/html; charset=GBK" %><%@ page
import="java.util.List,
        java.net.InetAddress,
        com.runway.race.ipm.IpmResult,
        com.runway.race.ipm.Ipm,
        com.runway.race.ipm.IpmImpl"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fortune.rms.business.content.model.Content" %>
<%@ page import="com.fortune.rms.business.product.model.Product" %>
<%@ page import="com.fortune.rms.business.user.CacheLogic" %>
<%@ page import="com.fortune.rms.business.csp.logic.logicImpl.CspLogicImpl" %>
<%@ page import="com.fortune.rms.business.csp.model.Csp" %>
<%@ include file="param.jsp"%>
<%@ include file="queryString.jsp"%>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="java.util.*,java.text.DecimalFormat"%>
<%@ page import="com.fortune.rms.business.user.SessionUser" %>
<%@ page import="com.fortune.common.Constants" %>
<%
    //�����ռ���������ɽ���������۷�
    SessionUser su = (SessionUser) session.getAttribute(Constants.SESSION_USER);
    String userId = su.getUserId();
    String sessionId = su.getSessionId();
    String userInfo = su.getUserInfo();

    //session���
    if (userId == null && userId.equals("")) {
        response.sendRedirect("account/error.jsp?msg=avalid_session");
        return;
    }

    UserLogic userLogic = new UserLogic(request);

//    DecimalFormat df = new DecimalFormat("#0.00");//�������ָ�ʽת��
    //��ȡӰƬ����
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.PayMediaRequest_bak");

    CacheLogic cacheLogic = new CacheLogic(request);
    Product product = cacheLogic.getProduct(serviceProductId);
    Content content = cacheLogic.getContent(contentId);

    Csp csp = cacheLogic.getCsp(content.getCspId());

    try{
        String type = getParameter(request, "type", "");

        String price =  getParameter(request, "price", "0");
        double fee = Double.valueOf(price);

        String categoryId;
        String clientIp = request.getRemoteAddr();
        String userIP = InetAddress.getLocalHost().getHostAddress();
        userIP = userIP + " from " + request.getRemoteAddr();

        //ɽ����Ŀ�����ݲ�ͬ�Ĺ������ͣ����ò�ͬ�Ľӿڣ�
        try{
            if(type.equals("media")){//���򵥸�ý�壬�������İ��μƷѽӿ�
                logger.debug("���˹��򵥸�ý���ļ�");

                fileID = content.getName()+" ["+contentPropertyId+"]";
//����Ҳ��spID����
//                /*
                try {
                    if(csp!=null && csp.getType() == 1){
                       fileID = fileID+"|"+csp.getSpId();
                        logger.debug(csp.getName()+" �Ǵ�����ṩ�̣�SPID="+csp.getSpId());
                    }else{
                        logger.debug(csp.getName()+" ���Ǵ�����ṩ�̣�����SPID��Ϣ��");
                    }
                } catch (Exception e) {
                    logger.error("�޷���ȡIMP��Ϣ��"+e.getMessage());
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
//                */

                Ipm ipm = new IpmImpl();

                categoryId = product.getPayProductNo();
                logger.debug("��ѯ��������ӿ�AAProcess��fileID="+fileID+",categoryId="+categoryId);
                IpmResult result = ipm.AAProcess(userInfo, sessionId, categoryId, fileID, "0", "0", clientIp);
                if(result.getResultID() == 0){
                    logger.debug("��Ҫ���򣬵��ù���ӿ�AccountProcessClick��fileID="+fileID+",categoryId="+categoryId);
                    IpmResult ap = ipm.AccountProcessClick(userInfo, sessionId, categoryId, fileID, "0", "0", userIP);
                    if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5 || ap.getErrorInfo().equals("314")){//�Ʒѳɹ�,���,icp���
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"����ɹ�,����ֵ="+ap.getResultID());
                         session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                         userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);
                    }else{
                        logger.debug("fileID="+fileID+",categoryId="+categoryId+"����ʧ��,����ֵ="+ap.getResultID());
                        response.sendRedirect("account/error.jsp?msg="+ap.getResultID());
                    }
                }else if(result.getResultID() == 314){
                    logger.debug("fileID="+fileID+",categoryId="+categoryId+"�Ѿ����򣬷���="+result.getResultID());
                    logger.debug("���Բ��ţ�pramString="+queryStr);
                    //paramString +="&fileID="+fileID;
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    response.sendRedirect("sx6Player.jsp?" + queryStr);
                    return;
                }else{
                    response.sendRedirect("account/error.jsp?msg="+result.getResultID());
                }
            }else if (type.equals("channel")){//����Ƶ�����������İ��½ӿ�
                categoryId = product.getPayProductNo();
                Ipm ipm = new IpmImpl();
                IpmResult ap = ipm.OrderMonthService(userInfo, sessionId,categoryId, "0", userIP);
                if(ap.getResultID() == 0 || ap.getResultID() == 4 || ap.getResultID() == 5){//�Ʒѳɹ�,���,icp���
                    session.setAttribute("play_" + contentId + "_" + contentPropertyId, "true");
                    userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);
                }else{
                    response.sendRedirect("account/error.jsp?msg="+ ap.getResultID());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }catch(Exception e){
        session.setAttribute("ERROR", e.toString());
        e.printStackTrace();
        response.sendRedirect("account/error.jsp?msg=unexpect_error");
        return;
    }
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����ɹ�</title>
<link href="css/win.css" rel="stylesheet" type="text/css" />
</head>

<body leftmargin="00" topmargin="0">
<table width="559" height="394" border="0" cellpadding="0" cellspacing="0" background="images/account/123.gif">
  <tbody>
        <tr>
          <td height="72">��</td>
        </tr>
  		<tr>
    		<td align="left" valign="top">
					<table width="100%" class="black" height="110" border="0" bordercolor="#111111" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
							  <td align="center" width="100%" valign="middle" height="90">
								  <p><b><font color="#0000FF">��ϲ��������֧���Ѿ��ɹ�!!</font></b></p>
  							  </td>
							</tr>
						</tbody>
					</table>
			</td>
		</tr>
	</tbody>
</table>
<script language="javascript" type="text/javascript">
        alert("��ϲ��������֧���Ѿ��ɹ�!!");
        location.replace("sx6Player.jsp?<%=queryStr%>");
        document.location.href = "sx6Player.jsp?<%=queryStr%>";
</script>

</body>
</html>
