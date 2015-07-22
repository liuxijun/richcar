<%@ page import="java.util.List" %><%@ page import="com.fortune.util.BeanUtils" %><%@ page
        import="com.fortune.util.StringUtils" %><%@ page
        import="com.fortune.vac.*" %><%@ page
        import="org.apache.log4j.Logger" %><%@ page
        import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.util.MD5Utils" %><%@ page import="com.fortune.vac.socket.client.ClientSocket" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 13-4-24
  Time: 下午5:50
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String command = request.getParameter("command");
    String resultStr = "";
    Logger logger = Logger.getLogger("com.fortune.vac.jsp.index.jsp");
    if("checkPriceInterface".equals(command)){
        ClientSocket worker = ClientSocket.getInstance();
        String phoneNumber = request.getParameter("phoneNumber");
        String productId = request.getParameter("productId");
        String spId = request.getParameter("spId");
        long operateType = StringUtils.string2long(request.getParameter("operateType"),-1);
        int resultCode = 404;
        logger.debug("接口调用：来自"+request.getRemoteAddr()+",参数：phoneNumber="+phoneNumber+",productId="+
                productId+",spId="+spId+",operateId="+operateType);
        if(phoneNumber!=null&&productId!=null&&spId!=null&&operateType>=0&&!"".equals(phoneNumber)&&!"0".equals(phoneNumber)){
           resultCode = worker.checkPrice(phoneNumber, productId, operateType,spId);
        }else if("0".equals(phoneNumber)){
            //如果是0，是拉动默认号码，直接允许其进行播放
            resultCode = 0;
        }
        if("json".equals(request.getParameter("format"))){
            out.println("{resultCode:" +resultCode+
                    "}");
        }else{
            out.println("<?xml version=\"1.0\"?><result resultCode=\"" +resultCode+
                    "\"><time>" +System.currentTimeMillis()+
                    "</time></result>");
        }
        return;
    }else if("test".equals(command)){

    }
    AppConfigurator appConfig = AppConfigurator.getInstance();
    CheckPriceMessage checkPriceMessage = null;
    try {
        VacWorker worker = VacWorker.getInstance();
        checkPriceMessage = worker.getBaseCheckPriceMessage(appConfig.getConfig("vac.spId","14613"),"8618631184734","8018000402",1);
        {
            List<Tag> tags = checkPriceMessage.getTags();
            for(Tag tag:tags){
                String tagName  = tag.getName();
                String tagInputValue = request.getParameter(tag.getName());
                if(tagInputValue!=null){
                    int tagType = tag.getType();
                    Object tagValue = null;
                    if(tagType==CheckPriceMessage.TAG_TYPE_INT){
                        tagValue = StringUtils.string2int(tagInputValue,0);
                    }else if(tagType==CheckPriceMessage.TAG_TYPE_LONG){
                        tagValue = StringUtils.string2long(tagInputValue, 0);
                    }else if(tagType == CheckPriceMessage.TAG_TYPE_STRING){
                        tagValue = tagInputValue;
                    }else if(tagType==CheckPriceMessage.TAG_TYPE_TLV){
                        Tlv tlv = new Tlv();
                        tlv.setLength(tagInputValue.length());
                        tlv.setTag(Tlv.getTagByName(tagName));
                        tlv.setValue(tagInputValue);
                        tagValue = tlv;
                    }
                    BeanUtils.setProperty(checkPriceMessage,tagName,tagValue);
                }
            }
        }
        logger.debug("command="+command);
        //VacManager.getInstance();
        if(null==command){
            command = "bind";
        }
        if("test".equals(command)){
            logger.debug("准备进行测试");
            int resultCode = worker.checkPrice(checkPriceMessage.getDA(),checkPriceMessage.getServiceId(),checkPriceMessage.getOperationType(),
                    checkPriceMessage.getSpId());
            resultStr+="远程调用返回结果："+resultCode+"\r\n";
        }else if("checkPrice".equals(command)){
            logger.debug(checkPriceMessage.toString());
            if(!worker.isBindSuccess()){
                logger.debug("第一次啊，还没bind到VAC呢，赶紧bind到VAC!");
                worker.bind();
            }else{
                logger.debug("已经bind到VAC" +
                        "，放心用吧！");
            }
            if(!worker.isBindSuccess()){
                resultStr = "无法bind到VAC中心，操作失败！";
                logger.error(resultStr);
            }else{
                byte[] result = worker.sendToServer(checkPriceMessage);
                if(result!=null&&result.length>0){
                    String arrayStr = MD5Utils.bufferToHex(result);
                    resultStr += "\r\n接收到："+arrayStr+"\r\n";
                    logger.debug("接收到的数据："+arrayStr);
                    try {
                        MessageHeader header = new MessageHeader(result);
                        CheckPriceResp resp = new CheckPriceResp(result);
                        int respResult = (int) resp.getResultCode();
                        if(resp != null) {
                            resultStr += "执行结果："+ErrorCode.getErrorMessage(respResult)+"(0x" +Long.toHexString(respResult)+
                                    ")\r\n"+ header.toString()+resp.toString();
                        } else {
                            resultStr += "执行结果：CheckPriceResp为null！";
                        }
                    } catch (Exception e) {
                        resultStr +="处理数据过程中发生异常："+e.getMessage();
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }else{
                    resultStr = "发送数据到服务器发生异常，返回长度为0";
                }
                logger.debug(resultStr);
            }
        }else if("bind".equals(command)){
            if(worker.isBindSuccess()){
                if(worker.unBind()){
                    resultStr = "已经bind了，先解bind成功！\r\n";
                }else{
                    resultStr = "标志是已经bind了，但是无法解除bind状态，可能会有问题！\r\n";
                }
            }
            if(worker.bind()){
                resultStr += "Bind执行成功！";
            }else{
                resultStr +="bind执行失败！";
            }
        }else if("unBind".equals(command)){
            if(worker.unBind()){
                resultStr += "unBind运行成功！";
            }else{
                resultStr += "unBind运行失败！";
            }
        }else if("handset".equals(command)){
            if(worker.handset()){
                resultStr += "Handset运行成功！";
            }else{
                resultStr += "Handset运行失败！";
            }
        }
    } catch (Exception e) {
        logger.debug(e.getMessage());
        e.printStackTrace();
    }
%>
<html>
<head>
    <title></title>
</head>
<body>
<div align="center">
    <form action="?" method="post">
        <table border="1" cellspacing="0">
            <tr>
                <td>VAC</td>
                <td><%=AppConfigurator.getInstance().getConfig("vac.hostIp")%>（如果是在线服务，应该是10.17.170.200）</td>
            </tr>
            <tr>
                <td>Port</td>
                <td><%=AppConfigurator.getInstance().getConfig("vac.port")%>(如果是在线服务，应该是9999）</td>
            </tr>
            <tr bgcolor="#DEDEDE">
                <td width="100" align="right">名称</td>
                <td width="600">值</td>
            </tr>
            <%
                List<Tag> tags = checkPriceMessage.getTags();
                for(Tag tag:tags){
                    String tagName = tag.getName();
                    Object value = BeanUtils.getProperty(checkPriceMessage,tagName);
                    if(value==null){
                        value = "";
                    }else if(value instanceof Tlv){
                        Tlv tlv = (Tlv) value;
                        value = tlv.getValue();
                    }
            %>
            <tr>
                <td align="right"><label for="<%=tagName%>"><%=tagName%>：</label></td>
                <td><input id="<%=tagName%>" style="width:600px;" type="text" name="<%=tagName%>" value="<%=value.toString()%>"></td>
            </tr>
            <%
                }
            %>
            <tr>
                <td align="right">
                    命令：
                </td>
                <td><select style="width:300px" name="command" value="checkPrice">
                    <option value="bind" <%="bind".equals(command)?"selected":""%>>BIND</option>
                    <option value="checkPrice" <%="checkPrice".equals(command)?"selected":""%>>CheckPrice</option>
                    <option value="handset" <%="handset".equals(command)?"selected":""%>>handset</option>
                    <option value="unBind" <%="unBind".equals(command)?"selected":""%>>unbind</option>
                    <option value="test" <%="test".equals(command)?"selected":""%>>代理访问测试</option>
                </select></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input style="width:100px" type="submit" value="提交">&nbsp;&nbsp;<input type="reset" value="恢复" style="width:100px" />
                </td>
            </tr>
            <tr>
                <td align="right">运行结果：</td>
                <td>
                    <textarea style="width:600px;height:300px;"><%=resultStr%></textarea>
                </td>
            </tr>
        </table>

    </form>
</div>
</body>
</html><!-- 201304261930 -->