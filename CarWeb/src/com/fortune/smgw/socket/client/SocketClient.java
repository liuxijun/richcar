/* 
 * socketͨѶ�ͻ���
 */
package com.fortune.smgw.socket.client;
import com.fortune.smgw.socket.iinterface.ILoginHandler;
import com.fortune.smgw.socket.iinterface.IRspHandler;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class SocketClient
{
    private Logger log = Logger.getLogger(SocketClient.class);
    private static InetAddress hostAddress;
    private static int port;
    public static SocketClientLink[] Links;
    private int maxLink;
    private Map rspHandlers = Collections.synchronizedMap(new HashMap());
    private static int currentLinkId;
    private Object obj = new Object();
    private Class rspHandlerClass;
    private Class loginHandlerClass;

    public SocketClient(InetAddress hostAddress, int port, int maxLink, String rsphandler, String loginHandler)
    {
        SocketClient.hostAddress = hostAddress;
        SocketClient.port = port;
        this.maxLink = maxLink;
        Links = new SocketClientLink[maxLink];
        try
        {
            this.rspHandlerClass = Class.forName(rsphandler);
            this.loginHandlerClass = Class.forName(loginHandler);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized SocketClientLink getLink()
    {
        currentLinkId %= this.maxLink;
        log.debug("getLink ׼����ȡlink����ǰlinkID:"+currentLinkId);
        if (Links[currentLinkId] == null) {
            Links[currentLinkId] = initlink(currentLinkId);
        }

        int linkId = currentLinkId;
        currentLinkId += 1;
        SocketClientLink link = Links[linkId];
        boolean flag = true;
        if ((link.getStatus() == -1) || (link.getStatus() == -2)){//||(!link.getSocket().isConnected())) {
            link.setStatus(0);
        }
        while (flag) {
            int status = link.getStatus();
            if (status == 0) {
                log.debug("��δ���ӣ�׼����ʼ������....");
                link.initiateConnection();
                link.connectHandler.waitResponse();
                log.debug("��ʼ�����ӹ�����ɣ�");
            } else if (status == 1) {
                log.debug("׼��Login���̣�");
                link.login();
                link.connectHandler.waitResponse();
                log.debug("Login������ϣ�");
            } else if (status != 2)
            {
                log.debug("��ǰ����״̬��"+status);
                if (status == 3)
                    return link;
                if (status == -2)
                    return link;
                if (status == -1) {
                    if (linkId > 0) {
                        this.maxLink = (linkId + 1);
                    }
                    return link;
                }
            }
        }
        this.log.debug("++==Get SocketClientLink,linkid:" + linkId + ", maxLink:" + this.maxLink);
        return link;
    }

    public SocketClientLink initlink(int linkId)
    {
        log.debug("��ʼ������initLink��,linkId="+linkId);
        SocketClientLink link = null;
        try {
            return new SocketClientLink(hostAddress, port, (IRspHandler)this.rspHandlerClass.newInstance(), linkId,
                    (ILoginHandler)this.loginHandlerClass.newInstance());
        }
        catch (Exception e) {
            this.log.error("Init SocketClientLink Exception,method :initlink ", e);
        }
        return link;
    }
}