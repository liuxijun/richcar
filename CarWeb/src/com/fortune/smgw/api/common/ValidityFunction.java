package com.fortune.smgw.api.common;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: 上午8:27
 * To change this template use File | Settings | File Templates.
 */

import com.fortune.smgw.api.sgip.message.body.SGIPMessageHead;

import java.text.SimpleDateFormat;

public class ValidityFunction
{
    public static String checkSGIPHead(SGIPMessageHead head)
    {
        String result = "true";
        if (!checkInteger(head.getMessageLength(), 4)) {
            result = "消息头Message Length字段检验出错!";
            return result;
        }
        if (!checkInteger(head.getCommandID(), 4)) {
            result = "消息头Command ID字段检验出错!";
            return result;
        }
        if (!checkInteger(head.getSequenceNo().getNode(), 4)) {
            result = "消息头Sequence Number字段的第一部分检验出错!";
            return result;
        }
        if (!checkInteger(head.getSequenceNo().getTime(), 4)) {
            result = "消息头Sequence Number字段的第二部分检验出错!";
            return result;
        }
        if (!checkInteger(head.getSequenceNo().getNumber(), 4)) {
            result = "消息头Sequence Number字段的第三部分检验出错!";
            return result;
        }
        return result;
    }

    public static boolean checkInteger(int value, int length)
    {
        switch (length) {
            case 1:
                if ((value > 255) || (value < 0)) {
                    return false;
                }
                break;
            case 2:
                if ((value > 65535) || (value < 0)) {
                    return false;
                }
                break;
            case 3:
            case 4:
        }
        return true;
    }

    public static boolean checkIntegerRange(int value, int min, int max)
    {
        if ((value > max) || (value < min)) {
            return false;
        }
        return true;
    }

    public static boolean checkString(String value, int length)
    {
        if ((value == null) || (value.equals(""))) {
            return true;
        }

        int charset = 0;
        int strLen = 0;
        try {
            strLen = value.getBytes("GBK").length;
            charset = 1;
        } catch (Exception ex) {
            try {
                strLen = value.getBytes("UTF-8").length;
                charset = 2;
            }
            catch (Exception localException1) {
            }
        }
        switch (charset) {
            case 0:
                if (value.getBytes().length > length) {
                    return false;
                }
                break;
            case 1:
                if (strLen > length) {
                    return false;
                }

                break;
            case 2:
                int oraLen = value.length();
                int zhLen = strLen - oraLen;
                strLen = oraLen - zhLen / 2 + zhLen;
                if (strLen > length) {
                    return false;
                }
                break;
            default:
                if (value.getBytes().length > length) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static boolean checkStringNumber(String value, int length)
    {
        if ((value == null) || (value.equals(""))) {
            return true;
        }

        if (!value.matches("\\d+")) {
            return false;
        }
        if (value.length() > length) {
            return false;
        }
        return true;
    }

    public static boolean checkDate(String value)
    {
        if ((value == null) || (value.equals(""))) {
            return true;
        }
        SimpleDateFormat myFmt = new SimpleDateFormat("yyMMddHHmmss");

        if (value.length() != 12)
            return false;
        try
        {
            myFmt.parse(value);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
