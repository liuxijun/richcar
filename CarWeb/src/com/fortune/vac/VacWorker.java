package com.fortune.vac;

import com.fortune.server.message.ServerMessager;
import com.fortune.util.AppConfigurator;
import com.fortune.util.XmlUtils;
import com.fortune.vac.socket.client.ClientSocket;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: 下午12:23
 * AVC认证
 */
public class VacWorker {
    private Logger logger = Logger.getLogger(getClass());
    private static VacWorker instance = new VacWorker();
    private boolean nativeCall = false;

    ClientSocket clientSocket = null;
    public static VacWorker getInstance(){
        return instance;
    }
    private VacWorker(){
        nativeCall = AppConfigurator.getInstance().getBoolConfig("vac.native.call", false);
        if(nativeCall){
            clientSocket = ClientSocket.getInstance();
        }
    }

    public boolean startup(){
        return clientSocket.startup();
    }

    public boolean shutdown(){
        return clientSocket.shutdown();
    }

    public  synchronized byte[] sendToServer(MessageBody messageBody){
        return clientSocket.sendToServer(messageBody);
    }

    public boolean bind(){
        return clientSocket.bind();
    }

    public boolean unBind(){
        return clientSocket.unBind();
    }

    public boolean handset(){
        return clientSocket.handset();
    }

    @SuppressWarnings("unused")
    public int checkPriceConfirm(String phoneNumber,String productId){
        return clientSocket.checkPriceConfirm(phoneNumber,productId);
    } 
    public CheckPriceMessage getBaseCheckPriceMessage(String spId,String phoneNumber,String productId,long operationType){
        return clientSocket.getBaseCheckPriceMessage(spId, phoneNumber, productId,operationType);
    }

    public int checkPrice(String phoneNumber,String productId,long operationType,String spId){
        AppConfigurator config = AppConfigurator.getInstance();
        if(nativeCall){
            logger.debug("启用内置购买流程，直接访问VAC...");
            return clientSocket.checkPrice(phoneNumber,productId,operationType,spId);
        }else{
            String serverUrl = config.getConfig("vac.visit.url","http://61.55.144.86/vac/index.jsp?command=checkPriceInterface");
            logger.debug("启用外置购买流程，代理访问VAC："+serverUrl);
            ServerMessager messager = new ServerMessager();
            String postData = "phoneNumber="+phoneNumber+"&productId="+productId+"&spId="+spId+"&operateType="+operationType;
            String result = messager.postToHost(serverUrl,postData);
            if(result!=null&&!"".equals(result.trim())){
                Element root = XmlUtils.getRootFromXmlStr(result);
                if(root!=null){
                    return XmlUtils.getIntValue(root,"@resultCode",-99);
                }else{
                    logger.error("xml解析出现问题！无法继续："+result);
                }
            }else{
                logger.error("接口放回参数为空，可能是访问错误："+serverUrl+","+postData);
            }
        }
        return -999;
    }

    public boolean isBindSuccess() {
        return clientSocket.isBindSuccess();
    }

    public int operateOrder(String phoneNumber,String productId,Long operationType,String spId) {
        logger.debug("购买流程启动：phoneNumber="+phoneNumber+",productId="+productId+",operationType="+operationType+",cspId="+spId);
        int result =  checkPrice(phoneNumber,productId,operationType,spId);
        String log ="购买结果："+result+","+ErrorCode.getErrorMessage(result);
        if(result!=0){
            logger.error(log);
        }else{
            logger.debug(log);
        }
        return result;
    }

    public boolean checkBuy(String phoneNumber,List<String> productIds,Long operationType,String spId) {
         if(productIds != null && productIds.size() > 0)  {
             for (String productId : productIds) {
                 long startTime = System.currentTimeMillis();
                 int result = checkPrice(phoneNumber,productId,operationType,spId);
                 long stopTime = System.currentTimeMillis();
                 logger.debug("检查是否购买：phoneNumber="+phoneNumber+",productId="+productId+
                         ",operationType="+operationType+",cspId="+spId+",result="+result+",耗时："+(stopTime-startTime)+
                         "毫秒"
                 );
                 if(result == 0) {
                      return true;
                  }
             }
         }
        return false;
    }
}
