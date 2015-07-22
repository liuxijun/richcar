package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午8:35
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPCommonRespBody;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPBindResp
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPCommonRespBody body = new SGIPCommonRespBody();

    public SGIPBindResp() {
        this.head.setCommandID(SgipCommandId.SGIP_BIND_RESP);
    }

    public SGIPMessageHead getHead() {
        return this.head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public SGIPCommonRespBody getBody() {
        return this.body;
    }

    public void setBody(SGIPCommonRespBody body) {
        this.body = body;
    }

    public byte[] toByteArray()
    {
        this.head.setMessageLength(getLength());
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(this.head.toByteArray());
        buffer.put(this.body.toByteArray());
        return buffer.array();
    }

    public static SGIPBindResp parse(ByteBuffer buffer) {
        SGIPBindResp tmp = new SGIPBindResp();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPCommonRespBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString() {
        return "SGIPBindResp message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPBindResp" + headStr);
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getResult(), 1)) {
            result.setDescribe("SGIPBindResp消息体Result字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPBindResp消息体Reserve字段检验出错!");
            return result;
        }

        result.setCode(0);
        return result;
    }
}