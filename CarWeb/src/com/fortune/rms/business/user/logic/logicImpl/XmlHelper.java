package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.rms.business.user.model.AllResp;
import com.fortune.threeScreen.serverMessage.ServerMessager;
import com.fortune.util.XmlUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;


/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-6-21
 * Time: 11:54:08
 * 基础类
 */
public class XmlHelper {
    protected Logger logger = Logger.getLogger(this.getClass());
    public AllResp getResult(String xmlContent){
        return getResult(com.fortune.util.XmlUtils.getRootFromXmlStr(xmlContent));
    }

    public AllResp getResult(Element root){
        AllResp result = new AllResp();
        if(root!=null){
            result.setResultCode(com.fortune.util.XmlUtils.getIntValue(root, "ResultCode", -1));
            result.setReason(com.fortune.util.XmlUtils.getValue(root, "Reason", "【没有原因，不行吗？】"));
        }else{
            result.setResultCode(500);
            result.setReason(com.fortune.util.XmlUtils.getValue(root, "Reason", "接口调用异常"));
        }
        return result;
    }

    public AllResp getSimpleResult(String url,String xmlStr){
        ServerMessager serverMessage = new ServerMessager();
        return getResult(serverMessage.postToHost(url,xmlStr));
    }
}
