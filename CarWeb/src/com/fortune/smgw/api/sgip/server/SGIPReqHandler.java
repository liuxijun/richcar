package com.fortune.smgw.api.sgip.server;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
import com.fortune.rms.business.system.logic.logicInterface.ShortMessageLogLogicInterface;
import com.fortune.smgw.api.common.TypeConvert;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.common.timeout.TimeoutEvent;
import com.fortune.smgw.api.common.timeout.TimeoutListener;
import com.fortune.smgw.api.common.timeout.TimeoutManager;
import com.fortune.smgw.api.common.timeout.TimeoutObject;
import com.fortune.smgw.api.sgip.SGIPConfig;
import com.fortune.smgw.api.sgip.message.*;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import com.fortune.smgw.socket.iinterface.IReqHandler;
import com.fortune.smgw.socket.server.ReceiveObj;
import com.fortune.smgw.socket.server.ServerData;
import com.fortune.smgw.socket.server.SocketServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fortune.util.SpringUtils;
import org.apache.log4j.Logger;

public class SGIPReqHandler
        implements Runnable, IReqHandler
{
    private Logger log = Logger.getLogger(SGIPReqHandler.class);
    private List queue = new LinkedList();
    TimeoutManager delivertm;
    TimeoutManager reporttm;
    private Thread curThread;

    public SGIPReqHandler()
    {
        this.curThread = new Thread(this);
        this.curThread.start();
        this.delivertm = new TimeoutManager(SGIPConfig.getInstance().SGIP_SERVER_TIMEOUT);

        this.delivertm.addTimeoutListener(new TimeoutListener()
        {
            public void timeoutPerformed(TimeoutEvent event) {
                List list = event.getKeys();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String msgid = (String)it.next();
                    ReceiveObj obj = (ReceiveObj)SGIPServer.MSG_QUEUE.remove(msgid);
                    SGIPServer.MSGID_QUEUE.remove(msgid);
                    if (obj != null) {
                        SGIPReqHandler.this.log.error("Report message timeout. SequenceNo:" + msgid);
                        SGIPDeliver deliver = (SGIPDeliver)obj.obj;

                        SGIPDeliverResp resp = new SGIPDeliverResp();

                        resp.getHead().setSequenceNo(deliver.getHead().getSequenceNo());
                        resp.getBody().setResult(19);
                        obj.server.send(obj.channel, resp.toByteArray());
                        SGIPReqHandler.this.log.debug("send DeliverResp message:");
                        SGIPReqHandler.this.log.debug(resp.toString());

                        SGIPReqHandler.this.log.debug("finish process Deliver message.  SequenceNo:" + msgid);
                    }
                }
            }
        });
        this.delivertm.start();

        this.reporttm = new TimeoutManager(SGIPConfig.getInstance().SGIP_CLIENT_TIMEOUT);

        this.reporttm.addTimeoutListener(new TimeoutListener()
        {
            public void timeoutPerformed(TimeoutEvent event) {
                List list = event.getKeys();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String msgid = (String)it.next();
                    ReceiveObj obj = (ReceiveObj)SGIPServer.MSG_QUEUE.remove(msgid);
                    SGIPServer.MSGID_QUEUE.remove(msgid);
                    if (obj != null) {
                        SGIPReqHandler.this.log.error("Report message timeout. SequenceNo:" + msgid);
                        SGIPReport report = (SGIPReport)obj.obj;
                        SGIPReportResp resp = new SGIPReportResp();

                        resp.getHead().setSequenceNo(report.getHead().getSequenceNo());
                        resp.getBody().setResult(19);

                        obj.server.send(obj.channel, resp.toByteArray());
                        SGIPReqHandler.this.log.debug("send ReportResp message:");
                        SGIPReqHandler.this.log.debug(resp.toString());

                        SGIPReqHandler.this.log.debug("finish process Report message.  SequenceNo:" + msgid);
                    }
                }
            }
        });
        this.reporttm.start();
    }

    public void run()
    {
        for (;;) {
            ServerData data;
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException interruptedexception) {
                        /* empty */
                    }
                }
                data = (ServerData) queue.remove(0);
            }
            processServerData(data);
        }
    }

    public void Request(SocketServer server, SocketChannel channel, byte[] data)
    {
        byte[] dataCopy = new byte[data.length];
        System.arraycopy(data, 0, dataCopy, 0, data.length);
        synchronized (this.queue) {
            this.queue.add(new ServerData(server, channel, dataCopy));
            this.queue.notify();
        }
    }

    private void processServerData(ServerData serverData) {
        String ip = serverData.socket.socket().getRemoteSocketAddress().toString();
        try {
            int readLength = 0;
            byte[] data = serverData.data;

            this.log.debug(ip + " :message data total length:" + data.length);
            int dataLength = data.length;
            if(dataLength<20){
               //这不是SGIP的头
                log.debug("数据长度太小，忽略这个数据！");
                serverData.server.send(serverData.socket, ("Report Listener Is Waiting!\n\rWho are you? \r\nCome from "+ip+".").getBytes());
                serverData.socket.close();
            }else{
                while (dataLength - readLength > 0){
                    byte[] length = new byte[4];
                    System.arraycopy(data, readLength, length, 0,dataLength-readLength>length.length?dataLength-readLength:length.length);
                    int msgLength = TypeConvert.byte2int(length);
                    byte[] commonid = new byte[4];
                    System.arraycopy(data, readLength + 4, commonid, 0, commonid.length);
                    int msgType = TypeConvert.byte2int(commonid);
                    if (data.length - readLength < msgLength) {
                        this.log.error(ip + " :message is length:" + msgLength + ".but just " + (data.length - readLength) +
                                " left!");
                        break;
                    }
                    byte[] msg = new byte[msgLength];
                    System.arraycopy(data, readLength, msg, 0, msg.length);
                    this.log.debug(ip + " :process " + msgLength + " length message.");
                    process(msgType, msg, serverData.server, serverData.socket);
                    readLength += msgLength;
                }
            }
            this.log.debug(ip + " :finish process message data!");
        }
        catch (Exception e) {
            this.log.error(ip + " :request message exception：", e);
        }
    }

    private void process(int msgType, byte[] msg, SocketServer server, SocketChannel channel) {
        if (msgType == SgipCommandId.SGIP_BIND) {
            this.log.debug(channel.socket().getRemoteSocketAddress().toString() + " :it`s a Bind message.");
            processBind(msg, server, channel);
        } else if (msgType == SgipCommandId.SGIP_DELIVER) {
            this.log.debug(channel.socket().getRemoteSocketAddress().toString() + " :it`s a Deliver message.");
            processDeliver(msg, server, channel);
        } else if (msgType == SgipCommandId.SGIP_REPORT) {
            this.log.debug(channel.socket().getRemoteSocketAddress().toString() + " :it`s a Report message.");
            processReport(msg, server, channel);
        } else if(msgType == SgipCommandId.SGIP_SUBMIT){
            log.debug(channel.socket().getRemoteSocketAddress().toString() + " :it`s a Submit message.");
            processSubmit(msg,server,channel);
        } else {
            this.log.error(channel.socket().getRemoteSocketAddress().toString() + " :message is unknown to type:" + msgType);
        }
    }

    private void processBind(byte[] msg, SocketServer server, SocketChannel channel)
    {
        this.log.debug("process Bind message.");
        SGIPBind bind = SGIPBind.parse(ByteBuffer.wrap(msg));

        this.log.debug(bind.toString());
        String msgid = bind.getHead().getSequenceNo().toString();
        SGIPMessageHead head = bind.getHead();
        SGIPBindResp bindResp = new SGIPBindResp();
        bindResp.getHead().setSequenceNo(head.getSequenceNo());
        bindResp.getBody().setResult(1);

        if (bind.checkValidity().getCode() != 0) {
            bindResp.getBody().setResult(5);
            this.log.error("repeat login.  SequenceNo:" + msgid);
        }
        if ((bind.getBody().getLoginName().equals(SGIPServer.getInstance().getUserName())) &&
                (bind.getBody().getLoginPassword().equals(SGIPServer.getInstance().getPassWord()))) {
            bindResp.getBody().setResult(0);
            if (SGIPServer.LoginedChannel.containsKey(channel))
            {
                bindResp.getBody().setResult(2);
                this.log.error("repeat login.  SequenceNo:" + msgid);
            } else {
                SGIPServer.LoginedChannel.put(channel, channel);
            }
        }
        else {
            bindResp.getBody().setResult(1);
            this.log.error("Bind error:username or password is not correct. SequenceNo:" + msgid);
        }
        server.send(channel, bindResp.toByteArray());
        this.log.debug("send BindResp message:SequenceNo:" + msgid);
        this.log.debug(bindResp.toString());
        this.log.debug("finish process Bind message. SequenceNo:" + msgid);
    }

    private void processDeliver(byte[] msg, SocketServer server, SocketChannel channel) {
        this.log.debug("process Deliver message.");
        if (SGIPServer.LoginedChannel.containsKey(channel))
        {
            SGIPDeliver deliver = SGIPDeliver.parse(ByteBuffer.wrap(msg));
            this.log.debug(deliver.toString());
            String msgid = deliver.getHead().getSequenceNo().toString();
            SGIPDeliverResp resp = new SGIPDeliverResp();
            resp.getHead().setSequenceNo(deliver.getHead().getSequenceNo());
            ValidityResult vr = deliver.checkValidity();
            if (vr.getCode() != 0) {
                resp.getBody().setResult(5);
                this.log.error("message error.  SequenceNo:" + msgid + ",error reasons:" + vr.getDescribe());
                server.send(channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Deliver message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.size() >= SGIPConfig.getInstance().QUEUE_NUM) {
                resp.getBody().setResult(11);
                this.log.error("message queue is full.  SequenceNo:" + msgid);

                server.send(channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Deliver message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.containsKey(msgid))
            {
                resp.getBody().setResult(7);
                this.log.error("message SequenceNo is repeat.  SequenceNo:" + msgid);

                server.send(channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Deliver message.  SequenceNo:" + msgid);
                return;
            }
            ReceiveObj obj = new ReceiveObj(server, channel, msgid, SgipCommandId.SGIP_DELIVER, deliver);
            this.delivertm.addTimeoutObject(new TimeoutObject(msgid));
            SGIPServer.MSG_QUEUE.put(msgid, obj);
            SGIPServer.MSGID_QUEUE.add(msgid);
        }
        else {
            this.log.error(channel.socket().getRemoteSocketAddress().toString() +
                    " :not login ,can`t process this Deliver message.");
        }
    }

    private void processSubmit(byte[] msg, SocketServer server, SocketChannel channel) {
        this.log.debug("process Submit message.");
        if (SGIPServer.LoginedChannel.containsKey(channel))
        {
            SGIPSubmit submit = SGIPSubmit.parse(ByteBuffer.wrap(msg));
            this.log.debug(submit.toString());
            String msgid = submit.getHead().getSequenceNo().toString();
            SGIPSubmitResp resp = new SGIPSubmitResp();
            resp.getHead().setSequenceNo(submit.getHead().getSequenceNo());
            ValidityResult vr = submit.checkValidity();
            if (vr.getCode() != 0) {
                resp.getBody().setResult(5);
                this.log.error("message error.  SequenceNo:" + msgid + ",error reasons:" + vr.getDescribe());
                server.send(channel, resp.toByteArray());
                this.log.debug("send DeliverResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Deliver message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.size() >= SGIPConfig.getInstance().QUEUE_NUM) {
                resp.getBody().setResult(11);
                this.log.error("message queue is full.  SequenceNo:" + msgid);

                server.send(channel, resp.toByteArray());
                this.log.debug("send SubmitResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Submit message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.containsKey(msgid))
            {
                resp.getBody().setResult(7);
                this.log.error("message SequenceNo is repeat.  SequenceNo:" + msgid);
                server.send(channel, resp.toByteArray());
                this.log.debug("send SubmitResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Submit message.  SequenceNo:" + msgid);
            }else{
                resp.getBody().setResult(0);
                this.log.error("短信发送请求已经收到： SequenceNo:" + msgid);
                server.send(channel, resp.toByteArray());
                this.log.debug("发送回馈消息"+resp.toString());
                this.log.debug("finish process Submit message.  SequenceNo:" + msgid);
            }
/*
            ReceiveObj obj = new ReceiveObj(server, channel, msgid, SgipCommandId.SGIP_SUBMIT, submit);
            this.delivertm.addTimeoutObject(new TimeoutObject(msgid));
            SGIPServer.MSG_QUEUE.put(msgid, obj);
            SGIPServer.MSGID_QUEUE.add(msgid);
*/
        }
        else {
            this.log.error(channel.socket().getRemoteSocketAddress().toString() +
                    " :not login ,can`t process this Submit message.");
        }
    }

    private void processReport(byte[] msg, SocketServer server, SocketChannel channel) {
        this.log.debug("process Report message.");
        if (SGIPServer.LoginedChannel.containsKey(channel)) {
            SGIPReport report = SGIPReport.parse(ByteBuffer.wrap(msg));
            String msgid = report.getHead().getSequenceNo().toString();
            this.log.debug(report.toString());
            int reportType = report.getBody().getReportType();
            if(report.getBody().getReportType()==0){
                //这是一个submit的report
                ShortMessageLogLogicInterface shortMessageLogLogicInterface = (ShortMessageLogLogicInterface)
                        SpringUtils.getBeanForApp("shortMessageLogLogicInterface");
                String sn = report.getBody().getSubmitSequenceNumber().toString();
                log.debug("收到Submit的Report消息："+sn);
                shortMessageLogLogicInterface.updateSubmitReport(sn,report.getBody().getState(),new Date(),channel.socket().getRemoteSocketAddress().toString());
            }else{
                log.debug("收到非Submit的Report消息，reportType="+reportType);
            }
            SGIPReportResp resp = new SGIPReportResp();
            resp.getHead().setSequenceNo(report.getHead().getSequenceNo());
            ValidityResult vr = report.checkValidity();
            if (vr.getCode() != 0) {
                resp.getBody().setResult(5);
                this.log.error("message error.  SequenceNo:" + msgid + ",error reasons:" + vr.getDescribe());
                server.send(channel, resp.toByteArray());
                this.log.debug("send ReportResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Report message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.size() >= SGIPConfig.getInstance().QUEUE_NUM) {
                resp.getBody().setResult(11);
                this.log.error("message queue is full.  SequenceNo:" + msgid);

                server.send(channel, resp.toByteArray());
                this.log.debug("send ReportResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Report message.  SequenceNo:" + msgid);
                return;
            }

            if (SGIPServer.MSG_QUEUE.containsKey(msgid))
            {
                resp.getBody().setResult(7);
                this.log.error("message SequenceNo is repeat.  SequenceNo:" + msgid);

                server.send(channel, resp.toByteArray());
                this.log.debug("send ReportResp message:");
                this.log.debug(resp.toString());
                this.log.debug("finish process Report message.  SequenceNo:" + msgid);
                return;
            }
            ReceiveObj obj = new ReceiveObj(server, channel, msgid, SgipCommandId.SGIP_REPORT, report);
            this.reporttm.addTimeoutObject(new TimeoutObject(msgid));
            SGIPServer.MSG_QUEUE.put(msgid, obj);
            SGIPServer.MSGID_QUEUE.add(msgid);
        }
        else {
            this.log.error(channel.socket().getRemoteSocketAddress().toString() +
                    " :not login ,can`t process this Report message.");
        }
    }
}