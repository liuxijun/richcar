package com.fortune.smgw.socket.client;
import org.apache.log4j.Logger;

public class WaitHandler
        implements Runnable
{
    private Logger log = Logger.getLogger(WaitHandler.class);
    private Thread handler;
    private boolean waitFlag = true;
    private Object obj = new Object();

    public WaitHandler() {
        this.handler = new Thread(this);
        this.handler.start();
    }

    public void run() {
        try {
            while (true)
                Thread.sleep(50L);
        }
        catch (Exception e) {
            this.log.error("WaitHandler is Exception,method:run", e);
        }
    }

    public synchronized void waitResponse()
    {
        try
        {
            while (this.waitFlag) {
                wait();
            }

            return;
        } catch (Exception e) {
            this.log.error("WaitHandler is Exception,method:waitResponse", e);
        }
    }

    public synchronized void changeWaitFlag(boolean flag)
    {
        this.waitFlag = flag;
        if (!this.waitFlag)
            notify();
    }
}