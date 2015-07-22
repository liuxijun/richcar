package com.fortune.server.message;

import com.fortune.util.AppConfigurator;
import com.fortune.util.XmlUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-3-4
 * Time: 10:14:47
 * 与Transocder服务器的信息通讯
 */
public class TranscoderMessager extends ServerMessager {

    public void stopAllTransfer(String ip, int port) {
        String allTransferInfo = this.listInputTransferSessions(ip, port);
        if (allTransferInfo != null) {
            Element root = XmlUtils.getRootFromXmlStr(allTransferInfo);
            if (root != null) {
                List sessions = root.selectNodes("params/item");
                if (sessions != null && sessions.size() > 0) {
                    for (Object session : sessions) {
                        Node item = (Node) session;
                        if (item != null) {
                            List parameters = item.selectNodes("param");
                            if (parameters != null) {
                                String ttid = null;
                                boolean ttidTransferStarted = false;
                                for (Object parameter : parameters) {
                                    Node param = (Node) parameter;
                                    if (param != null) {
                                        String name = XmlUtils.getValue(param, "@n", null);
                                        if (name != null) {
                                            String value = XmlUtils.getValue(param, "@v", null);
                                            if ("input_transfer".equals(name)) {
                                                if (value == null || "".equals(value)) {
                                                    //不用停这个，可以继续
                                                    //continue;
                                                } else {
                                                    ttidTransferStarted = true;
                                                }
                                            } else if ("TTID".equals(name)) {
                                                ttid = value;
                                            }
                                        }
                                    }
                                }
                                if (ttid != null && !"".equals(ttid) && ttidTransferStarted) {
                                    logger.debug("发现TTID正在输出源监控：" + ttid + ",尝试停止它.....");
                                    this.stopTransferSession(ip, port, ttid);
                                    logger.debug("TTID：" + ttid + ",停止输出源监控命令发送完毕");
                                } else {
                                }
                            } else {
                                //参数为空
                            }
                        }
                    }
                }
            } else {
                logger.error("xml解析过程出现问题：" + allTransferInfo);
            }
        } else {
            logger.debug(ip + ":" + port + " 数据返回为空，无法继续！");
        }
    }

    public String getTranscoderStatus(String ip, int port) {
        Map<String, String> parameters = new HashMap<String, String>();
        String commandStr = getParameterXml("transc", "get", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String startTransferSession(String ip, int port, String ttid, String toIp, int toPort) {
        logger.debug("准备启动服务器(" + ip + ":" + port +
                ")编码TTID:" + ttid +
                "源输出到观看主机：" + toIp + ":" + toPort);
        return setTransferSession(ip, port, ttid, toIp + ":" + toPort);
    }

    public String setTransferSession(String ip, int port, String ttid, String to) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("TTID", ttid);
        parameters.put("input_transfer", to);
        String commandStr = getParameterXml("1.1", "transc", "set", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String stopTransferSession(String ip, int port, String ttid) {
        return setTransferSession(ip, port, ttid, "");
    }

    public String listInputTransferSessions(String ip, int port) {
        logger.debug("罗列所有的源输出情况！");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("listall", "1");
        parameters.put("input_transfer", "1");
        String commandStr = getParameterXml("transc", "get", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String listTranscoderTask(String ip, int port) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("listall", "1");
//        parameters.put("update", "1");
        String commandStr = getParameterXml("transc", "list", parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String doTranscoder(String ip, int port, String src, String dst, String command) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("src", "" + src);
        parameters.put("dst", "" + dst);
        parameters.put("static", "1");
        String commandStr = getParameterXml("transc", command, parameters);
        return getMessage(ip, port, url, commandStr);
    }

    public String startTranscoder(String ip, int port, String src, String dst) {
        return doTranscoder(ip, port, src, dst, "start");
    }

    public String stopTranscoder(String ip, int port, String src, String dst) {
        return doTranscoder(ip, port, src, dst, "stop");
    }

    public TranscoderMessager() {
        url = AppConfigurator.getInstance().getConfig("server.message.interface.transcoderUrl","/ep-get/xmlcommand");
    }

}