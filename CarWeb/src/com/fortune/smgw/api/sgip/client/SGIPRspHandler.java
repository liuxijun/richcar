package com.fortune.smgw.api.sgip.client;

import com.fortune.smgw.api.common.TypeConvert;
import com.fortune.smgw.api.sgip.message.SGIPBindResp;
import com.fortune.smgw.api.sgip.message.SGIPSubmitResp;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import com.fortune.smgw.socket.iinterface.IRspHandler;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

public class SGIPRspHandler
        implements IRspHandler {
    private Logger log = Logger.getLogger(SGIPRspHandler.class);

    public void Response(int linkId, byte[] data) {
        int readLength = 0;
        this.log.debug("message data total length:" + data.length);
        while (data.length - readLength > 0) {
            byte[] length = new byte[4];
            System.arraycopy(data, readLength, length, 0, length.length);
            int msgLength = TypeConvert.byte2int(length);

            byte[] commonid = new byte[4];
            System.arraycopy(data, readLength + 4, commonid, 0, commonid.length);
            int msgType = TypeConvert.byte2int(commonid);

            if (data.length - readLength < msgLength) {
                this.log.error("message data length error:" + msgLength);
            } else {
                this.log.debug("message data length:" + msgLength);
                byte[] msg = new byte[msgLength];
                System.arraycopy(data, readLength, msg, 0, msg.length);
                process(msgType, msg, linkId);
                readLength += msgLength;
            }
        }
        this.log.info("finish process message data!");
    }

    private void process(int msgType, byte[] msg, int linkId) {
        if (msgType == SgipCommandId.SGIP_BIND_RESP) {
            processBindResp(msg, linkId);
        } else if (msgType == SgipCommandId.SGIP_SUBMIT_RESP) {
            processSubmitResp(msg, linkId);
        } else
            this.log.error("message is unknown to type:" + msgType);
    }

    private void processBindResp(byte[] msg, int linkId) {
        SGIPBindResp resp = SGIPBindResp.parse(ByteBuffer.wrap(msg));
        this.log.debug("rev SGIPBind  from linkId:" + linkId);
        this.log.debug(resp.toString());
        if (resp.getBody().getResult() == 0) {
            com.fortune.smgw.socket.client.SocketClient.Links[linkId].loginSuccess();
            this.log.debug("BindResp process success");
        } else {
            com.fortune.smgw.socket.client.SocketClient.Links[linkId].loginFail();
            this.log.debug("BindResp process fail");
        }
    }

    private void processSubmitResp(byte[] msg, int linkId) {
        SGIPSubmitResp resp = SGIPSubmitResp.parse(ByteBuffer.wrap(msg));

        String msgid = resp.getHead().getSequenceNo().toString();
        this.log.debug("rev msgid:" + msgid);
        this.log.debug(resp.toString());
        if (SGIPClient.rspHandlers.containsKey(msgid)) {
            SGIPRsp rsphandler = (SGIPRsp) SGIPClient.rspHandlers.get(msgid);
            rsphandler.handleResponse(resp);
            SGIPClient.rspHandlers.remove(msgid);
            this.log.debug("SubmitResp process sucess");
        } else {
            this.log.error("SubmitResp message is unknown SequenceNo:" + msgid);
        }
    }
}