<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.huawei.itellin.spapi.service.*"%>
<%@ page import="java.net.URL,com.huawei.itellin.bcg.soap.*,com.ccit.cvb.communicator.serverside.soap.sp.*" %>
<%@include file="param.jsp"%>
<%
    try{

        String flag = getParameter(request,"flag","");
        if ("checklogin".equals(flag)){
            String result = getParameter(request,"Result","");
            String ssoToken = getParameter(request,"ssoToken","");
            if ("0".equals(result)){
                response.sendRedirect("login.jsp?return_url="+java.net.URLEncoder.encode(ccitLoginBack));
                return;
            }

            SSOValidToken ssoValidToken = MainFactory.getSSOValidTokenWithCert(ssoToken, BCG_CERT);
            BSSPUserInfo bsspUInfo = ssoValidToken.getUserInfo();
            String puserId = bsspUInfo.getPseudoId();
            String nickName = bsspUInfo.getNickName();
            String userType = bsspUInfo.getUserPayType();
            session.setAttribute("userId", nickName);
            session.setAttribute("psUserId", puserId);
            session.setAttribute("userType", userType);

            response.sendRedirect("checklogin.jsp");
            return;

        }


        if ("onepay".equals(flag)){

            String responseValue = request.getParameter("SPItemResponse");
            SPItemResponse spItemResponse = MainFactory.decryptSPItemResponseValueWithCert(responseValue, BCG_CERT, SP_PRIVATE_KEY);

            int result = spItemResponse.getResult();

            String payNo = "";
            String transactionID = "";
            int fee = 0;

            if (result==0){
                spid = spItemResponse.getSpId();
                payNo = spItemResponse.getTransparentString();
                transactionID = spItemResponse.getTransactionId();
                fee = spItemResponse.getFee();
                out.print("锁定成功，价格为："+fee/100+"元<br/>");
            }else{
                spid = spItemResponse.getSpId();
                payNo = spItemResponse.getTransparentString();
                transactionID = spItemResponse.getTransactionId();
                fee = spItemResponse.getFee();
                out.print("锁定失败"+fee);
            }

            if (result==0){
                String timeStamp = InteriorTools.generateTimeStamp();
                BSSPStringBuffer reqSignStringBuffer = new BSSPStringBuffer();
                reqSignStringBuffer.append(spid);
                reqSignStringBuffer.append(timeStamp);
                reqSignStringBuffer.append(payNo);
                reqSignStringBuffer.append(transactionID);
                String reqSignString = reqSignStringBuffer.toString();
                String reqSign = InteriorTools.generateSign(reqSignString, SP_PRIVATE_KEY);

                BSSPServiceServiceLocator locator = new BSSPServiceServiceLocator();
                BSSPServiceSoapBindingStub _stub = new BSSPServiceSoapBindingStub(new URL(bsspServiceAddress), locator);
                SPPayByItemConfirmResponse payByItemConfirmResponse = _stub.spPayByItemConfirm(spid, timeStamp, payNo, transactionID, reqSign);

                int soapResult = payByItemConfirmResponse.getResult();
                if (soapResult==0){
                    out.println("购买成功");
                }else{
                    out.println("购买失败"+soapResult);                    
                }
            }
        }

        if ("periodpay".equals(flag)){

            String responseValue = request.getParameter("SPServiceOrderByPeriodResponse");
            SPServiceOrderByPeriodResponse  spServiceOrderByPeriodResponse  =MainFactory.decryptSPServiceOrderByPeriodResponseValueWithCert(responseValue, BCG_CERT, SP_PRIVATE_KEY);

            int result = spServiceOrderByPeriodResponse.getResult();

            String payNo = "";
            String transactionID = "";
            int fee = 0;

            if (result==0){
                payNo = spServiceOrderByPeriodResponse.getTransparentString();
                transactionID = spServiceOrderByPeriodResponse.getTransactionId();
                fee = spServiceOrderByPeriodResponse.getFee();

                out.println("包月购买成功");
            }else{
                out.println("包月购买失败，错误码："+result);

            }

        }

        if ("checkpay".equals(flag)){

            String psuserId = (String)session.getAttribute("psUserId");
            String payNo="05@000openhe@12935080";

            String timeStamp = InteriorTools.generateTimeStamp();

            BSSPStringBuffer reqSignStringBuffer = new BSSPStringBuffer();
            reqSignStringBuffer.append(spid);
            reqSignStringBuffer.append(timeStamp);
            reqSignStringBuffer.append(psuserId);
            reqSignStringBuffer.append(payNo);
            reqSignStringBuffer.append("");
            reqSignStringBuffer.append("");
            String reqSignString = reqSignStringBuffer.toString();
            String reqSign = InteriorTools.generateSign(reqSignString, SP_PRIVATE_KEY);

            QueryUserProductInfoRequest userProductInfo = new QueryUserProductInfoRequest();
            userProductInfo.setSpID(spid);
            userProductInfo.setTimeStamp(timeStamp);
            userProductInfo.setPseudoID(psuserId);
            userProductInfo.setProductID(payNo);
            userProductInfo.setSpSignData(reqSign);
            userProductInfo.setSpUserID("");
            userProductInfo.setRemark("");

            BSSPExtendService_ServiceLocator locator = new BSSPExtendService_ServiceLocator();
            BSSPExtendServiceSoapBindingStub _stub = new BSSPExtendServiceSoapBindingStub(new URL(bsspExtendServiceAddress), locator);
            QueryUserProductInfoResponse queryUserProduct = _stub.queryUserProductInfo(userProductInfo);

            int result = queryUserProduct.getResult();
            //System.out.print(queryUserProduct.getResult());
            if(result==1){  //购买过该产品
                out.println("购买过该产品");
            }else{
                out.println("未购买该产品"+result);                
            }

        }

 /*
        if ("login".equals(flag)){
            String result = getParameter(request,"Result","");
            String ssoToken = getParameter(request,"ssoToken","");
            if ("1".equals(result)){
                SSOValidToken ssoValidToken = MainFactory.getSSOValidTokenWithCert(ssoToken, BCG_CERT);
                BSSPUserInfo bsspUInfo = ssoValidToken.getUserInfo();
                String puserId = bsspUInfo.getPseudoId();
                String nickName = bsspUInfo.getNickName();
                String userType = bsspUInfo.getUserPayType();
                session.setAttribute("userId", nickName);
                session.setAttribute("psUserId", puserId);
                session.setAttribute("userType", userType);
            }

        }
*/

    }catch(Exception e){
        e.printStackTrace();
    }
%>end