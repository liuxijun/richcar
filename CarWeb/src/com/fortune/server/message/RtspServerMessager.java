package com.fortune.server.message;

import com.fortune.util.AppConfigurator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-3-4
 * Time: 10:14:47
 * 与RTSP服务器的信息通讯
 */
public class RtspServerMessager extends ServerMessager {
    private String url = AppConfigurator.getInstance().getConfig("live.message.rtspInterface.url", "ep-get/xmlcommand");

    public String queryServerChannel(String ip, int port) {
        Map<String, String> parameters = new HashMap<String, String>();
        String commandStr = getParameterXml("query", "channels", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String queryServerClient(String ip, int port, int start, int unit) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("start", "" + start);
        parameters.put("unit", "" + unit);
        String commandStr = getParameterXml("query", "clients", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String queryServerGen(String ip, int port, int clientCount, int channleCount) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("channels", "" + channleCount);
        parameters.put("clients", "" + clientCount);
        String commandStr = getParameterXml("query", "general", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public boolean saveOrStop(String ip, int port,String tubeId, String saveCmd, String recordFileUrl) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("channel", tubeId);
        parameters.put("path", recordFileUrl);
        String commandStr = getParameterXml("file_save", saveCmd, parameters);
        String result = getMessage(ip, port, url, commandStr);
        if(result!=null){
            result = result.trim();
        }
        return !("fail".equals(result));
    }

    public boolean stopSave(String ip, int port,  String tubeId, String recordFileUrl) {
        return saveOrStop(ip, port, tubeId, "stop", recordFileUrl);
    }

    public boolean startSave(String ip, int port, String tubeId, String recordFileUrl) {
        return saveOrStop(ip, port, tubeId, "start", recordFileUrl);
    }

}
