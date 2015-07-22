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
import com.fortune.smgw.api.sgip.SGIPConfig;
import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;
import com.fortune.smgw.api.sgip.message.body.SGIPSubmitBody;
import com.fortune.smgw.api.sgip.message.body.SgipCommandId;
import java.nio.ByteBuffer;

public class SGIPSubmit
{
    private SGIPMessageHead head = new SGIPMessageHead();
    private SGIPSubmitBody body = new SGIPSubmitBody();

    public SGIPSubmit()
    {
        this.head.setCommandID(SgipCommandId.SGIP_SUBMIT);
    }

    public SGIPMessageHead getHead()
    {
        return this.head;
    }

    public void setHead(SGIPMessageHead head)
    {
        this.head = head;
    }

    public SGIPSubmitBody getBody()
    {
        return this.body;
    }

    public void setBody(SGIPSubmitBody body) {
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

    public static SGIPSubmit parse(ByteBuffer buffer)
    {
        SGIPSubmit tmp = new SGIPSubmit();
        tmp.head = SGIPMessageHead.parse(buffer);

        tmp.body = SGIPSubmitBody.parse(buffer);
        return tmp;
    }

    public int getLength()
    {
        return this.head.getLength() + this.body.getLength();
    }

    public String toString()
    {
        return "SGIPSubmit message:\n" + this.head.toString() + "\n" + this.body.toString() + "\n";
    }

    public ValidityResult checkValidity()
    {
        ValidityResult result = new ValidityResult();
        result.setCode(5);

        String headStr = ValidityFunction.checkSGIPHead(this.head);
        if (!headStr.equals("true")) {
            result.setDescribe("SGIPSubmit" + headStr);
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getSPNumber(), 21)) {
            result.setDescribe("SGIPSubmit��Ϣ��SPNumber�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getChargeNumber(), 21)) {
            result.setDescribe("SGIPSubmit��Ϣ��ChargeNumber�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getUserCount(), 0, 100)) {
            result.setDescribe("SGIPSubmit��Ϣ��UserCount�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getUserNumber(), 21)) {
            result.setDescribe("SGIPSubmit��Ϣ��UserNumber�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getCorpId(), 5)) {
            result.setDescribe("SGIPSubmit��Ϣ��CorpId�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getServiceType(), 10)) {
            result.setDescribe("SGIPSubmit��Ϣ��ServiceType�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getFeeType(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��FeeType�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getFeeValue(), 6))
        {
            result.setDescribe("SGIPSubmit��Ϣ��FeeValue�ֶμ������!");
            return result;
        }
        if ((Integer.parseInt(this.body.getFeeValue()) < SGIPConfig.SGIP_MIN_FEEVALUE) ||
                (Integer.parseInt(this.body.getFeeValue()) > SGIPConfig.SGIP_MAX_FEEVALUE))
        {
            result.setDescribe("SGIPSubmit��Ϣ��FeeValue�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkStringNumber(this.body.getGivenValue(), 6))
        {
            result.setDescribe("SGIPSubmit��Ϣ��GivenValue�ֶμ������!");
            return result;
        }
        if ((Integer.parseInt(this.body.getGivenValue()) < SGIPConfig.SGIP_MIN_FEEVALUE) ||
                (Integer.parseInt(this.body.getGivenValue()) > SGIPConfig.SGIP_MAX_FEEVALUE))
        {
            result.setDescribe("SGIPSubmit��Ϣ��GivenValue�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getAgentFlag(), 0, 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��AgentFlag�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getMorelatetoMTFlag(), 0, 3)) {
            result.setDescribe("SGIPSubmit��Ϣ��MorelatetoMTFlag�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkIntegerRange(this.body.getPriority(), 0, 9)) {
            result.setDescribe("SGIPSubmit��Ϣ��Priority�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkDate(this.body.getExpireTime()))
        {
            this.body.setExpireTime("");
        }
        if (!ValidityFunction.checkDate(this.body.getScheduleTime())) {
            result.setDescribe("SGIPSubmit��Ϣ��ScheduleTime�ֶμ������!");
            return result;
        }
        if (!ValidityFunction.checkInteger(this.body.getReportFlag(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��ReportFlag�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getTP_pid(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��TP_pid�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getTP_udhi(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��TP_udhi�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getMessageCoding(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��MessageCoding�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getMessageType(), 1)) {
            result.setDescribe("SGIPSubmit��Ϣ��MessageType�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkInteger(this.body.getMessageLength(), 4)) {
            result.setDescribe("SGIPSubmit��Ϣ��MessageLength�ֶμ������!");
            return result;
        }

        if (!ValidityFunction.checkString(this.body.getReserve(), 8)) {
            result.setDescribe("SGIPSubmit��Ϣ��Reserve�ֶμ������!");
            return result;
        }

        result.setCode(0);
        return result;
    }
}