package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: 下午12:53
 * 错误代码表
 */
public class ErrorCode {
    public static final int ERROR_NOT_BINDED=10000;
    public static final int ERROR_CAN_NOT_CONNECT=10001;
    public static final int ERROR_SEND_ERROR=10002;
    public static Map<Integer,String> errorCode = new HashMap<Integer,String>();
    static{
        errorCode.put(0,"执行结果成功！");
        errorCode.put(1,"PDU长度无效（大于30K或者小于0）");
        errorCode.put(2,"解码PDU失败（缺少域或域类型无效）");
        errorCode.put(3,"绑定的帐号无效");
        errorCode.put(4,"绑定的密码不匹配");
        errorCode.put(5,"业务引擎已经被绑定");
        errorCode.put(6,"业务引擎还没有被绑定");
        errorCode.put(7,"等待响应消息超时");
        errorCode.put(8,"等待确认消息超时");
        errorCode.put(100,"域值无效(notExpectedOrError)");
        errorCode.put(101,"源端设备类型超出范围");
        errorCode.put(102,"源端设备ID不正确");
        errorCode.put(103,"目的端设备类型超出范围");
        errorCode.put(104,"目的端设备ID不正确");
        errorCode.put(105,"时间戳格式不正确");
        errorCode.put(106,"订购用户ID无效");
        errorCode.put(107,"MSISDN无效");
        errorCode.put(108,"SP 标识无效");
        errorCode.put(109,"业务标识无效");
        errorCode.put(110,"业务接入码无效");
        errorCode.put(500,"硬盘读写错误");
        errorCode.put(501,"网络连接不正常");
        errorCode.put(502,"网络错误");
        errorCode.put(503,"LICENSE文件不合法");
        errorCode.put(504,"注册用户数目超出LICENSE限制");
        errorCode.put(505,"系统内部错误");
        errorCode.put(506,"数据库错误 (数据库连接断掉,SQL语法无效)");
        errorCode.put(600,"运营商不存在");
        errorCode.put(601,"角色不存在");
        errorCode.put(602,"雇员不存在");
        errorCode.put(603,"管理员不存在");
        errorCode.put(604,"运营商没有足够权限");
        errorCode.put(1001,"订购用户不存在");
        errorCode.put(1002,"订购用户状态被停止");
        errorCode.put(1003,"订购用户欠费，预付费用户计费失败");
        errorCode.put(1004,"订购用户在黑名单");
        errorCode.put(1005,"无效用户，用户不再当前平台终");
        errorCode.put(1006,"此业务能力已被屏蔽");
        errorCode.put(1007,"用户无法屏蔽/恢复业务能力");
        errorCode.put(1100,"指定的业务不对用户开放");
        errorCode.put(1103,"用户已注册");
        errorCode.put(1104,"用户不存在");
        errorCode.put(1105,"用户状态不正常");
        errorCode.put(1106,"用户密码检验错误");
        errorCode.put(1107,"产生伪码失败");
        errorCode.put(1200,"订购关系已存在，指同一产品用户已定购");
        errorCode.put(1201,"订购关系不存在");
        errorCode.put(1202,"订购关系状态不正常");
        errorCode.put(1203,"同步订购关系给SP失败");
        errorCode.put(1204,"用户不能订购两个此类业务，指互斥类产品用户已定购");
        errorCode.put(1205,"剩余的订购关系数目不足");
        errorCode.put(1206,"ALS/Fax数字为空");
        errorCode.put(1600,"XX表示CRM返回错误码");
        errorCode.put(2000,"SP不存在");
        errorCode.put(2001,"SP状态不正常");
        errorCode.put(2002,"SP信用度低");
        errorCode.put(2100,"业务不存在");
        errorCode.put(2101,"业务不开放（状态不正常）");
        errorCode.put(2200,"没有此类型业务(SMS,MMS,etc.)");
        errorCode.put(2201,"业务不能订购");
        errorCode.put(2202,"剩余可用帐户不足");
        errorCode.put(2203,"业务信用度低");
        errorCode.put(2204,"业务URL检验失败");
        errorCode.put(2205,"代收费业务，不允许定购、点播");
        errorCode.put(2209,"产品不能从终端侧订购、退订");
        errorCode.put(3000,"CDR格式错误");
        errorCode.put(3001,"价格为负");
        errorCode.put(3002,"价格格式错误");
        errorCode.put(3003,"价格超出范围");
        errorCode.put(3100,"用户不是一个预付费用户");
        errorCode.put(3101,"用户余额不足");
        errorCode.put(3102,"压缩余额失败");
        errorCode.put(3103,"没有需要的计费信息");
        errorCode.put(3104,"写CDR失败");
        errorCode.put(3105,"CDR被复制");
        errorCode.put(3106,"插入CDR进数据库失败");
        errorCode.put(3107,"CDR价格太高");
        errorCode.put(3108,"等候SCP响应失败");
        errorCode.put(3109,"重新载入计费矩阵进内存失败");
        errorCode.put(4001,"BSS内没有用户信息");
        errorCode.put(4002,"从BSS同步用户信息失败");
        errorCode.put(4003,"从其他管理平台同步用户信息失败");
        errorCode.put(4004,"同步SP给其他管理平台失败");
        errorCode.put(4005,"同步业务数据给其他管理平台失败");
        errorCode.put(4100,"同步订购关系给SP失败");
        errorCode.put(4200,"删除MSISDN失败");
        errorCode.put(4201,"改变MSISDN失败");
        errorCode.put(ERROR_NOT_BINDED,"尚未Bind到VAC");
        errorCode.put(ERROR_CAN_NOT_CONNECT,"无法连接到服务器");
        errorCode.put(ERROR_SEND_ERROR,"发送数据时发生错误");
    }

    public static String getErrorMessage(int code){
        return errorCode.get(code);
    }
}
