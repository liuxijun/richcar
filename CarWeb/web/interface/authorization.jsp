<%@ page import="java.io.InputStream,
    org.apache.log4j.Logger" %><%@ page
        import="java.net.URLDecoder" %><%@ page import="sun.misc.BASE64Decoder" %><%@ page
        contentType="text/html;charset=UTF-8" language="java" %><%
    InputStream is = request.getInputStream();
    int dataLength = request.getContentLength();
    if(dataLength>0){
        byte[] dataBuffer = new byte[dataLength];
        int readedLength = is.read(dataBuffer);
        String readResult = "";

        int readed = 0;
        try {
            while(readed<dataLength&&readedLength>0){
                readResult = readResult + new String(dataBuffer,0,readedLength);
        //        gbkString += new String(dataBuffer,0,readedLength,"GBK");
                readedLength = is.read(dataBuffer);
                readed+=readedLength;
            }
            if(readedLength>0){
                readResult = readResult + new String(dataBuffer,0,readedLength);
                readed+=readedLength;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // logger.debug("客户端(" +request.getRemoteAddr()+")传来数据长度：" + readResult.length() + ",readResult=\n" + readResult);
        String[] dataArray = readResult.split("\n");
        if(dataArray.length>0){
            String url=dataArray[0].trim();
            int p = url.indexOf(".ts?dur=");
            if(p>0){
                //这是个新版的url，要做个替换
                String tempStr = url.substring(0,p);
                int p1 = tempStr.lastIndexOf("_");
                if(p1>0){
                    tempStr = tempStr.substring(0,p1);
                }
                int p0 = tempStr.lastIndexOf("/");
                if(p0>0){
                    BASE64Decoder decoder = new BASE64Decoder();
                    String decodeStr = tempStr.substring(p0+1);
                   // logger.debug("尝试解码："+decodeStr);
                    decodeStr = new String(decoder.decodeBuffer(decodeStr),"UTF-8");
                   // logger.debug("解码结果："+decodeStr);
                    tempStr = tempStr.substring(0,p0)+ decodeStr;
                }
                tempStr = tempStr+url.substring(p);
                //logger.debug("修正后的URL="+tempStr);
                url = tempStr;
            }else{
                url = URLDecoder.decode(url,"UTF-8");
            }
            String ipData = null;
            for(String pName :new String[]{"userIp","userip","clientIp","clientip"}){
                ipData = StringUtils.getParameter(url,pName);
                if(ipData!=null&&!"".equals(ipData.trim())){
                    break;
                }
            }

            if((ipData==null||"".equals(ipData.trim()))&&dataArray.length>1){
                ipData = dataArray[1].trim();
                if(ipData.startsWith("ip=")){
                    ipData = ipData.substring(3);
                }
            }
            //url = url.replaceAll(";","%3B");
            //String gbkUrl = URLDecoder.decode(gbkString,"GBK");
            boolean authPassed = false;
/*
            //暂时关闭对优鹏的防盗链
            if(url.contains("/voole/")){
                logger.warn("优朋普乐的播放连接，因为有汉字，暂时放行："+url);
                out.println("success");
                return;
            }
*/
            int result = verifyUrlToken(url,ipData,null);
            if(result == SecurityUtils.VERIFY_RESULT_PASSED){
                authPassed = true;
            }else if(result == SecurityUtils.VERIFY_RESULT_ENCRYPT_ERROR){
/*              //已经放弃对GBK编码的猜测
                //可能是编码错误，发送过来的是GBK编码，而我们当成了UTF-8
                logger.debug("尝试用gbk编码进行解码："+gbkUrl);
                result = verifyUrlToken(gbkUrl,ipData,null);
                */

/*
                //已经放弃了对URL中IP的校验，因为盗链中可能会包含这个IP
                String userIp = StringUtils.getParameter(url,"userIp");
                if(userIp!=null&&!userIp.equals(ipData)){
                    //logger.debug("验证失败，尝试用URL中IP！");
                    result = verifyUrlToken(url,userIp,null);
                    authPassed = result ==VERIFY_RESULT_PASSED;
                }
                if(result == VERIFY_RESULT_ENCRYPT_ERROR){
                }
*/
            }
            if(ipData==null){
                ipData = "";
            }
            if((!authPassed)&&("140.206.48.130".equals(ipData)||ipData.startsWith("61.55.144.")
                    ||ipData.startsWith("127.0.0.1")||ipData.startsWith("0:0:"))
                    ||ipData.contains("192.168.")||url.contains("fromEditorPlatform")){
                logger.warn("服务器："+request.getRemoteAddr()+
                        " 发过来的认证请求，虽然校验失败，但根据IP判断是复全内部测试，放行：\n"+readResult);
                authPassed = true;
            }
            if(authPassed){
                //logger.debug("验证通过:"+request.getRemoteAddr()+",data="+readResult);
                out.println("success");
                return;
            }else{
                logger.debug("验证失败:"+ipData+","+url);
            }
        }else{
            logger.error("数据为空，无法进行验证！");
        }
    }else{
        logger.error(request.getRemoteAddr()+"没有数据输入");
    }
%>fail
<%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.authorization.jsp");
%><%@include file="../user/urlUtils.jsp"%>