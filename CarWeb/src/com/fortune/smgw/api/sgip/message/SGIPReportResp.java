package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ����8:40
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPCommonRespBody;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPReportResp
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPCommonRespBody body = new SGIPCommonRespBody();

    public SGIPReportResp() {
        this.head.setCommandID(SgipCommandId.SGIP_REPORT_RESP);
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
        ByteBuffer buffer = ByteBuffer.allocate(this.head.getLength() + this.body.getLength());
        buffer.put(this.head.toByteArray());
        buffer.put(this.body.toByteArray());
        return buffer.array();
    }

    public static SGIPReportResp parse(ByteBuffer buffer) {
        SGIPReportResp tmp = new SGIPReportResp();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPCommonRespBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString() {
        return "SGIPReportResp message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPReportResp" + headStr);
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getResult(), 1)) {
            result.setDescribe("SGIPReportResp��Ϣ��Result�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPReportResp��Ϣ��Reserve�ֶμ������!");
            return result;
        }

        result.setCode(0);
        return result;
    }
}