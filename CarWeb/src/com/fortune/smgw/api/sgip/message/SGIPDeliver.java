package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午8:31
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPDeliverBody;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPDeliver
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPDeliverBody body = new SGIPDeliverBody();

    public SGIPDeliver() {
        this.head.setCommandID(SgipCommandId.SGIP_DELIVER);
    }

    public SGIPMessageHead getHead() {
        return this.head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public SGIPDeliverBody getBody() {
        return this.body;
    }

    public void setBody(SGIPDeliverBody body) {
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

    public static SGIPDeliver parse(ByteBuffer buffer) {
        SGIPDeliver tmp = new SGIPDeliver();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPDeliverBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString() {
        return "SGIPDeliver message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPDeliver" + headStr);
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getUserNumber(), 21)) {
            result.setDescribe("SGIPDeliver消息体UserNumber字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getSPNumber(), 21)) {
            result.setDescribe("SGIPDeliver消息体SPNumber字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getTP_pid(), 1)) {
            result.setDescribe("SGIPDeliver消息体TP_pid字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getTP_udhi(), 1)) {
            result.setDescribe("SGIPDeliver消息体TP_udhi字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getMessageCoding(), 1)) {
            result.setDescribe("SGIPDeliver消息体MessageCoding字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getMessageLength(), 4)) {
            result.setDescribe("SGIPSubmit消息体MessageLength字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPDeliver消息体Reserve字段检验出错!");
            return result;
        }

        result.setCode(0);
        return result;
    }
}