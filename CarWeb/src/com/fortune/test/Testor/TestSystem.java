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
 * Time: ����5:52
 * ɨ�����WEB������
 */
public class TestSystem {
    private Logger logger = Logger.getLogger(this.getClass());
    public void doTest(){
        logger.info("�������ԣ�");
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
            logger.info("���ڲ��ԣ�"+webServer.getName()+","+ip+","+serverPort);
            String testResult = messager.getMessage(ip,serverPort,
                   "/test/system.jsp?startTime="+System.currentTimeMillis(),null);
            if("".equals(testResult.trim())){
                result+=webServer.getName()+"����ʧ�ܣ�";
                resultCode |=0x80000000;
            }else{
                Element root = XmlUtils.getRootFromXmlStr(testResult);
                if(root!=null){
                    Node resultNode = root.selectSingleNode("result");
                    if(resultNode!=null){
                        long testCode = XmlUtils.getLongValue(resultNode,"@code",-1);
                        if(testCode==0){
                            logger.info("������"+webServer.getName()+"����û���κ����⣺"+resultNode.getText());
                         }else{
                            resultCode|=testCode;
                            String text = resultNode.getText();
                            logger.error("������"+webServer.getName()+"���Է�������"+text);
                            if(!result.contains(text)){
                                result+= text;
                            }
                        }
                    }else{
                        resultCode|=0x80000000;
                        result +="���ݴ���xml�����쳣:û��result�ڵ�";
                    }
                }else{
                    resultCode|=0x80000000;
                    result +="���ݴ���xml�����쳣�������Ǹ�ʽ����";
                    logger.error(webServer.getName()+"����ʱXML��ʽ�����⣺"+testResult);
                }
            }
        }
        AppConfigurator config = AppConfigurator.getInstance();
        String fileName =  config.getConfig("test.errorFlagFileName","D:\\fortune\\error.txt");
        String phoneNumber = config.getConfig("system.test.errorFeedBackPhone","15631127974");
        if(resultCode!=0){
            FileUtils.writeNew(fileName,StringUtils.date2string(new Date())+" - ���Է���������ϢΪ��"+result);
            logger.error("�����˲��Դ��󣬽����͵��ֻ�����"+phoneNumber);
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface) SpringUtils.getBeanForApp("systemLogLogicInterface");
            SystemLog systemLog = new SystemLog();
            systemLog.setAdminId(1L);
            systemLog.setAdminIp("����");
            systemLog.setAdminName("���Ի�����");
            systemLog.setLog(result);
            systemLog.setSystemLogAction(this.getClass().getName());
            systemLogLogicInterface.save(systemLog);
            result = "���Ǹ�ȫ�����Զ����Ի����ˣ�Ŀǰϵͳ��Щ�쳣��"+result;
            result = result.replaceAll("Tomcat����������","");
            result = result.replaceAll("���ݿ����������","");
            result = result.replaceAll("\r","");
            result = result.replaceAll("\n","");
            UserLogicInterface userLogicInterface = (UserLogicInterface)SpringUtils.getBeanForApp("userLogicInterface");
            String[] phones = phoneNumber.split(",");
            for(String mobilePhone:phones){
                if(result.length()>70){
                    //�ֳɶ�������
                    int length = result.length();
                    int sendEveryTime = 63;
                    //ÿ�η���64���ַ�
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
                    userLogicInterface.sendSMS(mobilePhone, "ϵͳ�����Ѿ��ָ���");
                }
            }
            logger.info("��������в��Զ��������ģ�");
        }
        logger.info("������ϣ�");
    }
    public static void main(String args[]){
        TestSystem testor = new TestSystem();
        testor.doTest();
    }
}
