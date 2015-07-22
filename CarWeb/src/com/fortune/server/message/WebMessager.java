package com.fortune.server.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-3-4
 * Time: 10:14:47
 * 与Web服务器的信息通讯
 */
public class WebMessager extends ServerMessager {

    public String noticeLiveToWeb(String ip, int port, String serverSn,
                                  String mediaId, String nodeId, String liveUrl,
                                  String author, String title, String interfaceUrl, String command) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("serverSn", serverSn);
        parameters.put("mediaId", mediaId);
        parameters.put("nodeId", nodeId);
        parameters.put("url", liveUrl);
        parameters.put("author", author);
        parameters.put("title", title);
        String commandStr = getParameterXml(command, command, parameters);
        return getMessage(ip, port, interfaceUrl, commandStr);

    }

    public String liveStarted(String ip, int port, String serverSn,
                              String mediaId, String nodeId, String liveUrl,
                              String author, String title, String interfaceUrl) {
        return noticeLiveToWeb(ip, port, serverSn, mediaId, nodeId, liveUrl, author, title, interfaceUrl, "start-server");
    }

    public String liveStoped(String ip, int port, String serverSn,
                             String mediaId, String nodeId, String liveUrl,
                             String author, String title, String interfaceUrl) {
        return noticeLiveToWeb(ip, port, serverSn, mediaId, nodeId, liveUrl, author, title, interfaceUrl, "stop-server");
    }

}