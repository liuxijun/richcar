package com.fortune.rms.web.log;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.log.logic.logicInterface.ClientLogLogicInterface;
import com.fortune.rms.business.log.model.ClientLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-6
 * Time: ÏÂÎç3:00
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/log")
@ParentPackage("default")
@Action(value="clientLog")
public class ClientLogAction extends BaseAction<ClientLog> {
    private static final long serialVersionUID = 3243534534534534l;
    private ClientLogLogicInterface clientLogLogicInterface;
    private String clientVersion;
    private String userAgent;

    public ClientLogAction() {
        super(ClientLog.class);
    }

    @SuppressWarnings("unchecked")
    @Autowired
    public void setClientLogLogicInterface(ClientLogLogicInterface clientLogLogicInterface) {
        this.clientLogLogicInterface = clientLogLogicInterface;
        setBaseLogicInterface(clientLogLogicInterface);
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String save(){
        log.debug("in clientLog");
        ClientLog clientLog = new ClientLog();
        clientLog.setTime(new Date());
        clientLog.setStatus(ClientLogLogicInterface.STATUS_DOWNLOAD);
        clientLog.setClientVersion(clientVersion);
        clientLog.setUserAgent(userAgent);
        clientLogLogicInterface.save(clientLog);
        return "success";
    }
}
