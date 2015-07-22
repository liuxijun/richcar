package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午8:30
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPBindBody;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPBind
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPBindBody body = new SGIPBindBody();

    public SGIPBind() {
        this.head.setCommandID(SgipCommandId.SGIP_BIND);
    }

    public SGIPMessageHead getHead() {
        return this.head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public SGIPBindBody getBody() {
        return this.body;
    }

    public void setBody(SGIPBindBody body) {
        this.body = body;
    }

    public byte[] toByteArray()
    {
        this.head.setMessageLength(getLength());
        ByteBuffer buffer = ByteBuffer.allocate(this.head.getLength() + this.body.getLength());
        buffer.put(this.head.toByteArray());
        buffer.put(this.body.toByteArray());
        return buffer.array();
    }

    public static SGIPBind parse(ByteBuffer buffer) {
        SGIPBind tmp = new SGIPBind();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPBindBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString() {
        return "SGIPBind message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPBind" + headStr);
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getLoginType(), 1)) {
            result.setDescribe("SGIPBind消息体Login Type字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getLoginName(), 16)) {
            result.setDescribe("SGIPBind消息体Login Name字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getLoginPassword(), 16)) {
            result.setDescribe("SGIPBind消息体Login Passowrd字段检验出错!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPBind消息体Reserve字段检验出错!");
            return result;
        }

        result.setCode(0);
        return result;
    }
}