package com.fortune.smgw.api.sgip.client;

import com.fortune.smgw.api.sgip.message.SGIPBind;
import com.fortune.smgw.socket.client.SocketClientLink;
import com.fortune.smgw.socket.iinterface.ILoginHandler;
import org.apache.log4j.Logger;

public class SGIPLoginHandler
        implements ILoginHandler
{
    private Logger log = Logger.getLogger(SGIPLoginHandler.class);

    public void login(int linkId) {
        sendLoginMsg(com.fortune.smgw.socket.client.SocketClient.Links[linkId]);
        this.log.debug("SGIPBind send , linkid:" + linkId);
    }

    public void logout(int linkId)
    {
    }

    private SGIPBind getLoginMsg() {
        SGIPBind bind = new SGIPBind();
        bind.getBody().setLoginName(SGIPClient.getInstance().getUserName());
        bind.getBody().setLoginPassword(SGIPClient.getInstance().getPassWord());
        bind.getBody().setLoginType((byte)1);

        return bind;
    }

    private void sendLoginMsg(SocketClientLink link) {
        try {
            SGIPBind bind = getLoginMsg();
            link.send(bind.toByteArray());
            this.log.debug(bind.toString());
        } catch (Exception e) {
            this.log.error("sendLoginMsg is  exception,method:sendLoginMsg", e);
        }
    }
}