package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JA
 * Date: 13-1-29
 * Time: 下午1:10
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionErrorCode {
    public static Map<Integer,String> subscriptionErrorCode = new HashMap<Integer, String>();
    static {
        subscriptionErrorCode.put(101,"发送端设备类型非法");
        subscriptionErrorCode.put(102,"发送端设备ID非法");
        subscriptionErrorCode.put(103,"用户号码非法");
        subscriptionErrorCode.put(104,"用户类型非法");
        subscriptionErrorCode.put(105,"密码非法");
        subscriptionErrorCode.put(106,"邮件非法");
        subscriptionErrorCode.put(107,"语言类型非法");
        subscriptionErrorCode.put(108,"用户信息不存在");
        subscriptionErrorCode.put(109,"用户已在WEB门户注册");
        subscriptionErrorCode.put(110,"用户未在WEB门户注册");
        subscriptionErrorCode.put(111,"旧密码错误");
        subscriptionErrorCode.put(112,"新密码非法");
        subscriptionErrorCode.put(113,"密码非法");
        subscriptionErrorCode.put(114,"用户终端类型非法");
        subscriptionErrorCode.put(115,"用户已注销");
        subscriptionErrorCode.put(116,"产品ID非法");
        subscriptionErrorCode.put(117,"套餐ID非法");
        subscriptionErrorCode.put(118,"定购方用户号码非法");
        subscriptionErrorCode.put(119,"定购方用户类型非法");
        subscriptionErrorCode.put(120,"使用方用户号码非法");
        subscriptionErrorCode.put(121,"使用方用户类型非法");
        subscriptionErrorCode.put(122,"付费方用户号码非法");
        subscriptionErrorCode.put(123,"付费方用户类型非法");
        subscriptionErrorCode.put(124,"受赠方不接受");
        subscriptionErrorCode.put(125,"用户状态不正常");
        subscriptionErrorCode.put(121,"用户余额不足");
        subscriptionErrorCode.put(126,"使用方用户类型非法");
        subscriptionErrorCode.put(127,"业务状态不正常");
        subscriptionErrorCode.put(128,"产品状态不正常");
        subscriptionErrorCode.put(129,"套餐状态不正常");
        subscriptionErrorCode.put(130,"定购关系不存在");
        subscriptionErrorCode.put(131,"定购关系已存在");
        subscriptionErrorCode.put(132,"定购关系不正常");
        subscriptionErrorCode.put(133,"用户没有开通该业务能力，要求省门户收到该错误码后，提示用户到CRM开通该业务能力，若全国门户收到该错误码后，将该信息转到省门户，提示用户到CRM开通该业务能力。");
        subscriptionErrorCode.put(134,"起始时间非法");
        subscriptionErrorCode.put(135,"结束时间非法");
        subscriptionErrorCode.put(136,"用户已处于黑名单中");
        subscriptionErrorCode.put(999,"其他");

    }

    public static String getSubscriptionErrorMessage(Integer code) {
         return subscriptionErrorCode.get(code);
    }
}
