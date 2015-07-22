package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ÉÏÎç8:36
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPUnbind
{
    private SGIPMessageHead head = new SGIPMessageHead();

    public SGIPUnbind() {
        this.head.setCommandID(SgipCommandId.SGIP_UNBIND);
    }

    public SGIPMessageHead getHead() {
        return this.head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public byte[] toByteArray()
    {
        this.head.setMessageLength(getLength());
        ByteBuffer buffer = ByteBuffer.allocate(this.head.getLength());
        buffer.put(this.head.toByteArray());
        return buffer.array();
    }

    public static SGIPUnbind parse(ByteBuffer buffer) {
        SGIPUnbind tmp = new SGIPUnbind();
        tmp.head = SGIPMessageHead.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength();
    }

    public String toString() {
        return "SGIPUnbind message:\n" + this.head.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPUnbind" + headStr);
            return result;
        }

        result.setCode(0);
        return result;
    }
}