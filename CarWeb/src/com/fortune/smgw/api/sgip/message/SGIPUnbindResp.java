package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ÉÏÎç8:37
 * To change this template use File | Settings | File Templates.
 */
import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPUnbindResp
{
    private SGIPMessageHead head = new SGIPMessageHead();

    public SGIPUnbindResp() {
        this.head.setCommandID(SgipCommandId.SGIP_UNBIND_RESP);
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
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(this.head.toByteArray());
        return buffer.array();
    }

    public static SGIPUnbindResp parse(ByteBuffer buffer) {
        SGIPUnbindResp tmp = new SGIPUnbindResp();
        tmp.head = SGIPMessageHead.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength();
    }

    public String toString() {
        return "SGIPUnbindResp message:\n" + this.head.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPUnbindResp" + headStr);
            return result;
        }

        result.setCode(0);
        return result;
    }
}