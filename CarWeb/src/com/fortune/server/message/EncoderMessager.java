package com.fortune.server.message;

import com.fortune.util.XmlUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-10-31
 * Time: 14:28:16
 * 和编码器的接口
 */
public class EncoderMessager extends ServerMessager{
    private String message;
    private String error;
    public String sendCommand(String ip,int port,String command){
        parameters.put("static", "1");
        String commandStr = getParameterIni(command, parameters);
        return getMessage(ip, port, url, commandStr);
    }
    public int sendCommand(String ip,int port,String command,int missionId){
        int resultCode = -1;
        //Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("missionId", "" + missionId);
        String result = sendCommand(ip, port, command);
        if(result!=null && !"".equals(result.trim())){
            Element root = XmlUtils.getRootFromXmlStr(result);
            if (root != null) {
                resultCode = XmlUtils.getIntValue(root,"result-code",-1);
                message = XmlUtils.getValue(root,"message","");
                error = XmlUtils.getValue(root,"error","");
            }
        }else{
            resultCode = 1;
            message = "命令已经发出";
            error = "命令发出，未确认";
        }
        return resultCode;
    }

    public int startEncode(String ip,int port,int missionId){
        return sendCommand(ip,port,"start",missionId);
    }

    public int stopEncode(String ip,int port,int missionId){
        return sendCommand(ip,port,"stop",missionId);
    }

    public int cancelUploadEncode(String ip,int port,int missionId){
        return sendCommand(ip,port,"cancelUpload",missionId);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getParameterIni(String target, Map<String, String> parameters) {
        StringBuffer result = new StringBuffer();
        if (parameters != null) {
            result.append("command=").append(target).append("\r\n");
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                if (value == null) value = "";
                result.append(key).append("=")
                        .append(value).append("\r\n");
            }
        }
        return result.toString();
    }

}
