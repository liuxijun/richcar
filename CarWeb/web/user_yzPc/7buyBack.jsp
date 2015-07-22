<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="com.fortune.rms.business.user.UserLogic" %>
<%@ page import="com.huawei.itellin.bcg.soap.SPPayByItemConfirmResponse" %>
<%@ page import="java.net.URL" %>
<%@ page import="com.huawei.itellin.bcg.soap.BSSPServiceSoapBindingStub" %>
<%@ page import="com.huawei.itellin.bcg.soap.BSSPServiceServiceLocator" %>
<%@ page import="com.huawei.itellin.spapi.service.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@include file="param.jsp"%>
<%@include file="queryString.jsp"%>
<%
     UserLogic userLogic= new UserLogic(request);

    String userIP = request.getRemoteAddr();    
    //String userId = (String)session.getAttribute("userId");
    String userId = (String)session.getAttribute("psUserId");
    if (userId==null){
        response.sendRedirect("1login.jsp?"+queryStr);
        return;
    }

    if (productType==1){
            String responseValue = request.getParameter("SPServiceOrderByPeriodResponse");
            SPServiceOrderByPeriodResponse spServiceOrderByPeriodResponse  =MainFactory.decryptSPServiceOrderByPeriodResponseValueWithCert(responseValue, BCG_CERT, SP_PRIVATE_KEY);

            int result = spServiceOrderByPeriodResponse.getResult();

            String payNo = "";
            String transactionID = "";
            int fee = 0;                    

            if (result==0){
                payNo = spServiceOrderByPeriodResponse.getTransparentString();
                transactionID = spServiceOrderByPeriodResponse.getTransactionId();
                fee = spServiceOrderByPeriodResponse.getFee();

                out.println("包月购买成功");

                userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);

                session.setAttribute("play_"+contentId+"_"+contentPropertyId,"true");    
                response.sendRedirect("8player.jsp?"+queryStr);
                return;

            }else{
                out.println("包月购买失败，错误码："+result);

            }
    }

    if (productType==2){

            String responseValue = request.getParameter("SPItemResponse");
            SPItemResponse spItemResponse = MainFactory.decryptSPItemResponseValueWithCert(responseValue, BCG_CERT, SP_PRIVATE_KEY);

            int result = spItemResponse.getResult();

            String payNo = "";
            String transactionID = "";
            int fee = 0;

            if (result==0){
                platformId = spItemResponse.getSpId();
                payNo = spItemResponse.getTransparentString();
                transactionID = spItemResponse.getTransactionId();
                fee = spItemResponse.getFee();
                out.print("锁定成功，价格为："+(float)fee/100+"元<br/>");
            }else{
                platformId = spItemResponse.getSpId();
                payNo = spItemResponse.getTransparentString();
                transactionID = spItemResponse.getTransactionId();
                fee = spItemResponse.getFee();
                out.print("锁定失败"+fee);
            }

            if (result==0){
                String timeStamp = InteriorTools.generateTimeStamp();
                BSSPStringBuffer reqSignStringBuffer = new BSSPStringBuffer();
                reqSignStringBuffer.append(platformId);
                reqSignStringBuffer.append(timeStamp);
                reqSignStringBuffer.append(payNo);
                reqSignStringBuffer.append(transactionID);
                String reqSignString = reqSignStringBuffer.toString();
                String reqSign = InteriorTools.generateSign(reqSignString, SP_PRIVATE_KEY);

               try{

                BSSPServiceServiceLocator locator = new BSSPServiceServiceLocator();
                BSSPServiceSoapBindingStub _stub = new BSSPServiceSoapBindingStub(new URL(bsspServiceAddress), locator);
                SPPayByItemConfirmResponse payByItemConfirmResponse = _stub.spPayByItemConfirm(platformId, timeStamp, payNo, transactionID, reqSign);


                int soapResult = payByItemConfirmResponse.getResult();
                if (soapResult==0){
                    out.println("购买成功");

                    userLogic.saveUserBuy(spId, channelId, contentId, contentPropertyId, serviceProductId, fee, userId,userIP);

                    session.setAttribute("play_"+contentId+"_"+contentPropertyId,"true");            
                    response.sendRedirect("8player.jsp?"+queryStr);
                    return;


                }else{
                    out.println("购买失败"+soapResult);
                }
               }catch(Exception e){
                   logger.error("按次购买出错:" + e.getMessage());
               }
            }
    }

%>
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.7buyBack.jsp");
%>