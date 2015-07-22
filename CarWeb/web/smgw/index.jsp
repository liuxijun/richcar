<%@ page import="com.fortune.smgw.api.sgip.client.SGIPClientInitInfo" %><%@ page
        import="com.fortune.smgw.api.sgip.client.SGIPClient" %><%@ page
        import="com.fortune.smgw.api.sgip.message.SGIPSubmit" %><%@ page
        import="com.fortune.smgw.api.sgip.client.SGIPRsp" %><%@ page
        import="com.fortune.smgw.api.sgip.message.body.SGIPSequenceNo" %><%@ page
        import="com.fortune.smgw.api.sgip.message.SGIPSubmitResp" %><%@ page
        import="java.util.Date" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="java.text.SimpleDateFormat" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.smgw.api.sgip.client.SGIPClientWorker" %><%@ page
        import="com.fortune.rms.business.system.logic.logicInterface.ShortMessageLogLogicInterface" %><%@ page
        import="com.fortune.util.SpringUtils" %><%@ page
        import="com.fortune.rms.business.system.model.ShortMessageLog" %><%@ page import="com.fortune.util.StringUtils" %><%--<%@ page
import="com.zte.smgw.api.sgip.client.SGIPClientInitInfo" %><%@ page
import="com.zte.smgw.api.sgip.client.SGIPClient" %><%@ page
import="com.zte.smgw.api.sgip.message.SGIPSubmit" %><%@ page
import="com.zte.smgw.api.sgip.client.SGIPRsp" %><%@ page
import="com.zte.smgw.api.sgip.message.body.SGIPSequenceNo" %><%@ page
import="com.zte.smgw.api.sgip.message.SGIPSubmitResp" %>--%><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-3-16
  Time: 上午11:13
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    Date now = new Date();
    String command = getParameter(request,"command","");
    String result = date2string(now)+" - 尚未开始";
    String message = request.getParameter("message");
    String phoneNumber = request.getParameter("phoneNumber");
    AppConfigurator sysConfig = AppConfigurator.getInstance();
    String sgipIp =getParameter(request,"sgipIp",sysConfig.getConfig("SGIP_IP","127.0.0.1"));//"221.192.140.33");
    int sgipPort = (int)getLongParameter(request,"sgipPort",sysConfig.getIntConfig("SGIP_PORT",9001));
    String sgipUser = getParameter(request,"sgipUser",sysConfig.getConfig("SGIP_USER","openhe"));
    String sgipPassword = getParameter(request,"sgipPassword",sysConfig.getConfig("SGIP_PASSWORD","123456"));
    String spCorpId = getParameter(request,"spCorpId",sysConfig.getConfig("SGIP_CORP_ID","14294"));
    String spNumber = getParameter(request,"spNumber",sysConfig.getConfig("SGIP_SP_NUMBER","10655910010"));
    long expireMinutes = sysConfig.getIntConfig("SGIP_EXPIRED_MINUTES",-1);

    log.debug("访问请求来自"+request.getRemoteAddr()+"，command="+command);
    if("send".equals(command)){
        if(message!=null&&!"".equals(message.trim())){
            if(phoneNumber!=null&&!"".equals(phoneNumber.trim())){
                try {
                    log.debug("尝试通过(" +sgipIp+":"+sgipPort+",usr:"+sgipUser+",pwd:"+sgipPassword+",corpId:"+spCorpId+",spNumber:"+spNumber+
                            ")向"+phoneNumber+"发送“" +message+"”");
                    ShortMessageLogLogicInterface shortMessageLogLogicInterface = (ShortMessageLogLogicInterface) SpringUtils.getBean("shortMessageLogLogicInterface",session.getServletContext());
                    SGIPClientInitInfo info = new SGIPClientInitInfo();
                    info.IP = sgipIp;
                    info.port = sgipPort;
                    info.userName = sgipUser;
                    info.passWord = sgipPassword;
                    info.maxLink =1;

//            info.IP = "10.130.83.207";
//            info.port = 5577;
//            info.userName = "zhjec";
//            info.passWord = "zhjec";
//            info.maxLink =1;
                    SGIPClientWorker client = SGIPClientWorker.getInstance();
                    //client.init(info);
                    SGIPSubmit submit = new SGIPSubmit();
//                    Date expireTime = new Date(now.getTime()+5*60*1000L);//过期时间，设定为5分钟
                    submit.getBody().setSPNumber(spNumber);//接入号
                    submit.getBody().setChargeNumber("000000000000000000000");//该费用由SP支付//phoneNumber);
                    submit.getBody().setUserCount(1);
                    if(phoneNumber.startsWith("+86")||phoneNumber.startsWith("086")){
                        phoneNumber = phoneNumber.substring(1);
                    }
                    if(phoneNumber.startsWith("0086")){
                        phoneNumber = phoneNumber.substring(2);
                    }
                    if(!phoneNumber.startsWith("86")){
                        phoneNumber= "86"+phoneNumber;
                    }
                    submit.getBody().setUserNumber(phoneNumber);
                    submit.getBody().setCorpId(spCorpId);//企业代码
                    submit.getBody().setFeeType(1);
                    submit.getBody().setFeeValue("0");
                    submit.getBody().setGivenValue("0");//赠送的话费
                    submit.getBody().setAgentFlag(0);
                    submit.getBody().setMorelatetoMTFlag(0);
                    submit.getBody().setPriority(0);
                    if(expireMinutes>0){
                        Date expireTime = new Date(now.getTime()+expireMinutes*60*1000L);//过期时间，设定为5分钟
                        submit.getBody().setExpireTime(date2string(expireTime,"yyMMddHHmmss"));
                    }else{
                        submit.getBody().setExpireTime(null);
                    }
                    //submit.getBody().setScheduleTime(date2string(now,"yymmddhhmmss")+"032+");//"090621010101"
                    submit.getBody().setScheduleTime(null);//立即发送。\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0");//"090621010101"
                    submit.getBody().setReportFlag(1); /*该条消息无论最后是否成功都要返回状态报告
                    状态报告标记
                            0-该条消息只有最后出错时要返回状态报告
                            1-该条消息无论最后是否成功都要返回状态报告
                            2-该条消息不需要返回状态报告
                            3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告

                    */
                    submit.getBody().setTP_pid(0);
                    submit.getBody().setTP_udhi(0);
                    submit.getBody().setMessageCoding(15);//GBK编码
                    /*
                    短消息的编码格式。
                            0：纯ASCII字符串
                            3：写卡操作
                            4：二进制编码
                            8：UCS2编码
                            15: GBK编码
                            其它参见GSM3.38第4节：SMS Data Coding Scheme

                    * */

                    result +="\r\n将要发送的内容："+message+"\r\n";
                    submit.getBody().setMessageContent(message.getBytes("GBK"));
                    submit.getBody().setReserve("");
                    submit.getBody().setMessageType(0);
                    Date date1=new Date();
/*
                    SGIPRsp rsphandler6;
                    rsphandler6 = new SGIPRsp();
*/
                    submit.getHead().setSequenceNo(new SGIPSequenceNo());
                    log.debug("准备发送.....");
                    //client.sendSubmit(submit, rsphandler6);
                    //SGIPSubmitResp rsp6 = rsphandler6.waitForSGIPSubmitResp();
                    int tryTimes = sysConfig.getIntConfig("SGIP_IF_FAIL_TRY_TIMES",5);

                    int respResult = 1;
                    ShortMessageLog shortMessageLog = new ShortMessageLog();
                    shortMessageLog.setMessage(message);
                    shortMessageLog.setStartTime(new Date());
                    shortMessageLog.setSn(submit.getHead().getSequenceNo().toString());
                    int nowTryTimes = tryTimes;
                    while(respResult!=0&&nowTryTimes>0){
                        try {
                            SGIPSubmitResp rsp6 = client.sendSubmit(submit);
                            respResult = rsp6.getBody().getResult();
                            if(respResult!=0){
                                String tempStr= StringUtils.date2string(new Date())+"发生异常（" +respResult+
                                        "），再试一次发送！还可以尝试"+(nowTryTimes-1)+"次！";
                                log.error(tempStr);
                                result+=tempStr+"\r\n";
                            }else{
                            }
                        } catch (Exception e) {
                            log.error("发送时发生异常："+e.getMessage());
                        }
                        nowTryTimes --;
                    }
                    //client.close();
                    if(nowTryTimes<tryTimes){
                        String tempStr= StringUtils.date2string(new Date())+
                                "尝试了"+(tryTimes-nowTryTimes)+"次后发送成功！";
                        log.warn(tempStr);
                        result+=tempStr+"\r\n";
                    }else{

                    }
                    shortMessageLog.setStatus((long) respResult);
                    shortMessageLog.setSmsIp(sysConfig.getConfig("SGIP_IP","127.0.0.1"));
                    shortMessageLog.setLog(result+"\n发送消息体：\n"+submit.getBody());
                    shortMessageLog.setPhoneNumber(phoneNumber);
                    shortMessageLogLogicInterface.save(shortMessageLog);
                    if(respResult!=0){
                        log.error("发送短信失败！");
                    }
                    result += "\r\nsendResult:"+respResult;
                    log.info("启动时间：" +date2string(date1));
                    log.info(result);
                    log.info("结束时间：" +date2string(new Date()));
                    if("true".equals(request.getParameter("resultXmlFormat"))){
                        //暂时全部返回发送成功！
                        if(respResult!=0){
                            respResult = 0;
                        }
%><?xml version="1.0" encoding="utf-8"?>
<root>
    <result-code><%=respResult%></result-code>
<%--
    <result-str><%=StringUtils.escapeXMLTags(result)%></result-str>
--%>
</root><%
                        return;
                    }
                } catch (Exception e) {
                    result = "发生异常："+ e.getMessage();
                    log.error(result);
                }
            }else{
                result = "error:no phone number!";
                log.error("出错：短信接收方号码不能为空");
            }
        }else{
            result = "error:no message!";
            log.error(result);
        }

    }else{
        result = "command can not be execute:"+command;
        log.debug("目前还不能处理的命令："+command+",直接放弃。");
    }
    if(phoneNumber==null){
        phoneNumber = "15631127974";
    }
    if(message==null){
        message = "JustForTest测试而已，不要认真！";
    }
    String remoteAddr = request.getRemoteAddr();
    if(!("140.206.48.130".equals(remoteAddr)||
            "127.0.0.1".equals(remoteAddr)||
            "0:0:0:0:0:0:0:1".equals(remoteAddr)||
            remoteAddr.startsWith("192.168.")||
            "testForFortuneDeveloper".equals(command)
    )){
        out.println("<html><body>您好，来自" +remoteAddr+
                "的朋友</body></html>");
        return;
    }
%><html>
<head>
    <title>测试短信信息是否正常</title>
    <style type="text/css">
        .allBox{
            width:455px;
            border:1px solid gray;
        }
        .aLine{

        }
        .fieldLabel{
            text-align: right;
            width:150px;
            font-weight: bold;
        }
        .textField{
            width:300px;
        }
        .textArea{
            width:300px;
            height:100px;
        }
    </style>

</head>
<body>

<form action="?time=<%=now.getTime()%>" method="post">
    <div align="center">
        <h1>短信网关接口测试页面</h1>
        <table class="allBox">
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="sgipIp">短信网关地址：</label></td>
                <td><input id="sgipIp" type="text" class="textField" name="sgipIp" value="<%=sgipIp%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="sgipPort">短信网关端口：</label></td>
                <td><input id="sgipPort" type="text" class="textField" name="sgipPort" value="<%=sgipPort%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="sgipUser">短信网关用户：</label></td>
                <td><input type="text" class="textField" name="sgipUser" id="sgipUser" value="<%=sgipUser%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="sgipPassword">短信网关口令：</label></td>
                <td><input type="text" class="textField" name="sgipPassword" id="sgipPassword" value="<%=sgipPassword%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="spCorpId">短信SP企业代码：</label></td>
                <td><input type="text" class="textField" name="spCorpId" id="spCorpId" value="<%=spCorpId%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="spNumber">SP接入号码：</label></td>
                <td><input type="text" class="textField" name="spNumber" id="spNumber" value="<%=spNumber%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="phoneNumber">发送到手机号：</label></td>
                <td><input type="text" class="textField" name="phoneNumber" id="phoneNumber" value="<%=phoneNumber%>"></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="message">消息内容：</label></td>
                <td><textarea cols="80" rows="20"  class="textArea" name="message" id="message"><%=message%></textarea></td>
            </tr>
            <tr class="aLine">
                <td align="right" class="fieldLabel"><label for="result">处理结果：</label></td>
                <td><textarea cols="80" readonly  rows="20" class="textArea" name="result" id="result"><%=result%></textarea></td>
            </tr>
            <tr class="aLine">
                <td colspan="2" align="center"><input type="submit" value="提交请求">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset"
                                                                                                                value="回复数据">
                </td>
            </tr>
        </table>
    </div>
    <%
        if(command == null ||"".equals(command.trim())){
            command = "send";
        }
    %>
    <input type="hidden" name="command" value="<%=command%>"/>
</form>
</body>
</html><%!
    Logger log = Logger.getLogger("com.fortune.rms.jsp.smgw.index.jsp");
    public static void initClient(){
        AppConfigurator sysConfig = AppConfigurator.getInstance();
        String sgipIp =sysConfig.getConfig("SGIP_IP", "127.0.0.1");//"221.192.140.33");
        int sgipPort = sysConfig.getIntConfig("SGIP_PORT", 9001);
        String sgipUser = sysConfig.getConfig("SGIP_USER", "openhe");
        String sgipPassword = sysConfig.getConfig("SGIP_PASSWORD", "123456");
        SGIPClientInitInfo info = new SGIPClientInitInfo();
        info.IP = sgipIp;
        info.port = sgipPort;
        info.userName = sgipUser;
        info.passWord = sgipPassword;
        info.maxLink =1;

//            info.IP = "10.130.83.207";
//            info.port = 5577;
//            info.userName = "zhjec";
//            info.passWord = "zhjec";
//            info.maxLink =1;
        SGIPClient client = SGIPClient.getInstance();
        client.init(info);
    }
    public String getParameter(HttpServletRequest request,String name,String defaultValue){
        String result = request.getParameter(name);
        if(result==null){
            return defaultValue;
        }
        return result;
    }
    public long getLongParameter(HttpServletRequest request,String name,long defaultValue){
        String value = getParameter(request, name,""+defaultValue);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public static String date2string(Date date) {
        return date2string(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2string(long date) {
        java.util.Date theTime = new java.util.Date(date);
        return date2string(theTime);
    }

    public static String date2string(Date date, String format) {
        if (date == null) {
            date = new java.util.Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }
  //build:201308081044
%>