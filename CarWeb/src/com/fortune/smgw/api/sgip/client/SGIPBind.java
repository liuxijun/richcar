package com.fortune.smgw.api.sgip.client;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-15
 * Time: ÏÂÎç5:24
 * To change this template use File | Settings | File Templates.
 */
import java.nio.ByteBuffer;

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPBindBody;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;

public class SGIPBind
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPBindBody body = new SGIPBindBody();

    public SGIPBind() {
        head.setCommandID(SgipCommandId.SGIP_BIND);
    }

    public SGIPMessageHead getHead() {
        return head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public SGIPBindBody getBody() {
        return body;
    }

    public void setBody(SGIPBindBody body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        head.setMessageLength(getLength());
        ByteBuffer buffer
                = ByteBuffer.allocate(head.getLength() + body.getLength());
        buffer.put(head.toByteArray());
        buffer.put(body.toByteArray());
        return buffer.array();
    }

    public static SGIPBind parse(ByteBuffer buffer) {
        SGIPBind tmp = new SGIPBind();
        tmp.head = SGIPMessageHead.parse(buffer);
        tmp.body = SGIPBindBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return head.getLength() + body.getLength();
    }

    public String toString() {
        return ("SGIPBind message:\n" + head.toString() + "\n"
                + body.toString() + "\n");
    }

    public ValidityResult checkValidity() {
        ValidityResult result = new ValidityResult();
        result.setCode(5);
        String headStr = ValidityFunction.checkSGIPHead(head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPBind" + headStr);
            return result;
        }
        if (!ValidityFunction.checkInteger(body.getLoginType(), 1)) {
            result.setDescribe
                    ("SGIPBind\u6d88\u606f\u4f53Login Type\u5b57\u6bb5\u68c0\u9a8c\u51fa\u9519!");
            return result;
        }
        if (!ValidityFunction.checkString(body.getLoginName(), 16)) {
            result.setDescribe
                    ("SGIPBind\u6d88\u606f\u4f53Login Name\u5b57\u6bb5\u68c0\u9a8c\u51fa\u9519!");
            return result;
        }
        if (!ValidityFunction.checkString(body.getLoginPassword(), 16)) {
            result.setDescribe
                    ("SGIPBind\u6d88\u606f\u4f53Login Passowrd\u5b57\u6bb5\u68c0\u9a8c\u51fa\u9519!");
            return result;
        }
        if (!ValidityFunction.checkString(body.getReserve(), 8)) {
            result.setDescribe
                    ("SGIPBind\u6d88\u606f\u4f53Reserve\u5b57\u6bb5\u68c0\u9a8c\u51fa\u9519!");
            return result;
        }
        result.setCode(0);
        return result;
    }
}
