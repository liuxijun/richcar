package com.fortune.smgw.api.sgip.message;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ����8:32
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.common.ValidityFunction;
import com.fortune.smgw.api.common.ValidityResult;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SGIPReportBody;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPReport
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPReportBody body = new SGIPReportBody();

    public SGIPReport() {
        this.head.setCommandID(SgipCommandId.SGIP_REPORT);
    }

    public SGIPMessageHead getHead() {
        return this.head;
    }

    public void setHead(SGIPMessageHead head) {
        this.head = head;
    }

    public SGIPReportBody getBody() {
        return this.body;
    }

    public void setBody(SGIPReportBody body) {
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

    public static SGIPReport parse(ByteBuffer buffer) {
        SGIPReport tmp = new SGIPReport();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPReportBody.parse(buffer);
        return tmp;
    }

    public int getLength() {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString() {
        return "SGIPReport message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPReport" + headStr);
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getSubmitSequenceNumber().getNode(), 4)) {
            result.setDescribe("SGIPReport��Ϣ��SubmitSequenceNumber�ֶεĵ�һ���ּ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getSubmitSequenceNumber().getTime(), 4)) {
            result.setDescribe("SGIPReport��Ϣ��SubmitSequenceNumber�ֶεĵڶ����ּ������!");
            return result;
        }
        if (!ValidityFunction.checkInteger(this.body.getSubmitSequenceNumber().getNumber(), 4)) {
            result.setDescribe("SGIPReport��Ϣ��SubmitSequenceNumber�ֶεĵ������ּ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getReportType(), 0, 1)) {
            result.setDescribe("SGIPReport��Ϣ��ReportType�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getUserNumber(), 21)) {
            result.setDescribe("SGIPReport��Ϣ��UserNumber�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getState(), 0, 2)) {
            result.setDescribe("SGIPReport��Ϣ��State�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getErrorCode(), 1)) {
            result.setDescribe("SGIPReport��Ϣ��ErrorCode�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPReport��Ϣ��Reserve�ֶμ������!");
            return result;
        }
        result.setCode(0);
        return result;
    }
}