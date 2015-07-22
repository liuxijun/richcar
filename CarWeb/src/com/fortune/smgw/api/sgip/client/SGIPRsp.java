package com.fortune.smgw.api.sgip.client;

import com.fortune.smgw.api.sgip.message.SGIPSubmitResp;
import org.apache.log4j.Logger;

public class SGIPRsp
{
    private SGIPSubmitResp rsp = null;
    private Logger log = Logger.getLogger(SGIPRsp.class);

    public synchronized void handleResponse(SGIPSubmitResp rsp)
    {
        this.rsp = rsp;
        notify();
    }

    public synchronized SGIPSubmitResp waitForSGIPSubmitResp()
    {
        while (this.rsp == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                this.log.error("SGIPRsp is InterruptedException,method:waitForSGIPSubmitResp ", e);
            }
        }
        return this.rsp;
    }
}