package com.fortune.test.Testor;

import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.system.model.SystemLog;
import com.fortune.rms.business.user.logic.logicImpl.UserLogicImpl;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.*;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-18
 * Time: 下午5:52
 * 扫描测试WEB服务器
 */
public class TestSystem {
    private Logger logger = Logger.getLogger(this.getClass());
    public void doTest(){
        logger.info("启动测试！");
        DeviceLogicInterface deviceLogicInterface = (DeviceLogicInterface) SpringUtils.getBeanForApp("deviceLogicInterface");
        Device device = new Device();
        device.setType(DeviceLogicInterface.DEVICE_TYPE_WEB);
        device.setStatus((long)DeviceLogicInterface.DEVICE_ONLINE);
        List<Device> webServers = deviceLogicInterface.search(device);
        ServerMessager messager = new ServerMessager();
        String result = "";
        long resultCode = 0x0;
        for(Device webServer :webServers){
            String ip = TcpUtils.getHostFromUrl(webServer.getUrl());
            int serverPort = TcpUtils.getPortFromUrl(webServer.getUrl());
            logger.info("正在测试："+webServer.getName()+","+ip+","+serverPort);
            String testResult = messager.getMessage(ip,serverPort,
                   "/test/system.jsp?startTime="+System.currentTimeMillis(),null);
            if("".equals(testResult.trim())){
                result+=webServer.getName()+"测试失败！";
                resultCode |=0x80000000;
            }else{
                Element root = XmlUtils.getRootFromXmlStr(testResult);
                if(root!=null){
                    Node resultNode = root.selectSingleNode("result");
                    if(resultNode!=null){
                        long testCode = XmlUtils.getLongValue(resultNode,"@code",-1);
                        if(testCode==0){
                            logger.info("服务器"+webServer.getName()+"测试没有任何问题："+resultNode.getText());
                         }else{
                            resultCode|=testCode;
                            String text = resultNode.getText();
                            logger.error("服务器"+webServer.getName()+"测试发生错误："+text);
                            if(!result.contains(text)){
                                result+= text;
                            }
                        }
                    }else{
                        resultCode|=0x80000000;
                        result +="数据错误：xml解析异常:没有result节点";
                    }
                }else{
                    resultCode|=0x80000000;
                    result +="数据错误：xml解析异常，可能是格式错误！";
                    logger.error(webServer.getName()+"测试时XML格式有问题："+testResult);
                }
            }
        }
        AppConfigurator config = AppConfigurator.getInstance();
        String fileName =  config.getConfig("test.errorFlagFileName","D:\\fortune\\error.txt");
        String phoneNumber = config.getConfig("system.test.errorFeedBackPhone","15631127974");
        if(resultCode!=0){
            FileUtils.writeNew(fileName,StringUtils.date2string(new Date())+" - 测试发生错误，信息为："+result);
            logger.error("发生了测试错误，将发送到手机短信"+phoneNumber);
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBeanForApp("systemLogLogicInterface");
            SystemLog systemLog = new SystemLog();
            systemLog.setAdminId(1L);
            systemLog.setAdminIp("本机");
            systemLog.setAdminName("测试机器人");
            systemLog.setLog(result);
            systemLog.setSystemLogAction(this.getClass().getName());
            systemLogLogicInterface.save(systemLog);
            result = "我是复全网络自动测试机器人，目前系统有些异常："+result;
            result = result.replaceAll("Tomcat运行正常！","");
            result = result.replaceAll("数据库测试正常！","");
            result = result.replaceAll("\r","");
            result = result.replaceAll("\n","");
            UserLogicInterface userLogicInterface = (UserLogicInterface)SpringUtils.getBeanForApp("userLogicInterface");
            String[] phones = phoneNumber.split(",");
            for(String mobilePhone:phones){
                if(result.length()>70){
                    //分成多条传送
                    int length = result.length();
                    int sendEveryTime = 63;
                    //每次发送64个字符
                    int sendTimes = (length+sendEveryTime-1)/sendEveryTime;
                    for(int i=0;i<sendTimes;i++){
                        int endIndex = (i*sendEveryTime)+sendEveryTime;
                        if(endIndex>=length){
                            endIndex = length-1;
                        }
                        String message = "("+(i+1)+"/"+sendTimes+")"+result.substring(i*sendEveryTime,endIndex);
                        userLogicInterface.sendSMS(mobilePhone, message);
                    }
                }else{
                    userLogicInterface.sendSMS(mobilePhone, result);
                }
            }
        }else{
            File testFile = new File(fileName);
            if(testFile.exists()&&!testFile.isDirectory()){
                if(testFile.delete()){

                }
                UserLogicInterface userLogicInterface = (UserLogicInterface)SpringUtils.getBeanForApp("userLogicInterface");
                String[] phones = phoneNumber.split(",");
                for(String mobilePhone:phones){
                    userLogicInterface.sendSMS(mobilePhone, "系统服务已经恢复。");
                }
            }
            logger.info("真棒，所有测试都是正常的！");
        }
        logger.info("测试完毕！");
    }
    public static void main(String args[]){
        TestSystem testor = new TestSystem();
        testor.doTest();
    }
}
