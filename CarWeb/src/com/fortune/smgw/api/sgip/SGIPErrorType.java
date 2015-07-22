package com.fortune.smgw.api.sgip;

import java.util.HashMap;
import java.util.Map;

public class SGIPErrorType
{
  public static final int SGIP_RESP_SUCCESS = 0;
  public static final int SGIP_RESP_LOGIN_FAIL = 1;
  public static final int SGIP_RESP_RE_LOGIN = 2;
  public static final int SGIP_RESP_LOGIN_FULL = 3;
  public static final int SGIP_RESP_ERR_LOGTYPE = 4;
  public static final int SGIP_RESP_PARAM_INVALID = 5;
  public static final int SGIP_RESP_INVALID_MOBILE = 6;
  public static final int SGIP_RESP_INVALID_ID = 7;
  public static final int SGIP_RESP_ERR_LENGTH = 8;
  public static final int SGIP_RESP_INVALID_SN = 9;
  public static final int SGIP_RESP_ERR_GNS = 10;
  public static final int SGIP_RESP_BUZY = 11;
  public static final int SGIP_RESP_TIME_OUT = 19;
  public static final int SGIP_RESP_SYSTEM_ERR = 20;
  public static final int SGIP_RPT_DEST_BREAK = 21;
  public static final int SGIP_RPT_ROUTE_ERR = 22;
  public static final int SGIP_RPT_ROUTE_INVALID = 23;
  public static final int SGIP_RPT_AUTH_FAIL = 24;
  public static final int SGIP_RPT_OUT_OF_SERVICE = 25;
  public static final int SGIP_RPT_MEM_FULL = 26;
  public static final int SGIP_RPT_NOT_SUPPORT_SM = 27;
  public static final int SGIP_RPT_RECV_SM_ERR = 28;
  public static final int SGIP_RPT_UNKNOWN_USER = 29;
  public static final int SGIP_RPT_NOT_SUPPLY = 30;
  public static final int SGIP_RPT_INVALID_EQUIP = 31;
  public static final int SGIP_RPT_SYSTEM_FAIL = 32;
  public static final int SGIP_RPT_SMC_QUEUE_FULL = 33;
  public static final int SGIP_RPT_TYPE_ERR = 34;
  public static final int SGIP_CHECK_MESSAGE_ERR = -1;
  public static final int SGIP_TIME_OUT_ERR = -2;
  public static final int SGIP_LINK_BREAKED_ERR = -3;
  public static final int SGIP_LOGIN_FAIL_ERR = -4;
  public static Map<Integer,String> errorMsgs = new HashMap<Integer,String>();
    static{
        errorMsgs.put(SGIP_RESP_SUCCESS,"操作成功！没有发生错误！");
        errorMsgs.put(SGIP_RESP_LOGIN_FAIL,"非法登录，如登录名、口令出错、登录名与口令不符等。");
        errorMsgs.put(SGIP_RESP_RE_LOGIN,"重复登录，如在同一TCP/IP连接中连续两次以上请求登录。");
        errorMsgs.put(SGIP_RESP_LOGIN_FULL,"连接过多，指单个节点要求同时建立的连接数过多。");
        errorMsgs.put(SGIP_RESP_ERR_LOGTYPE,"登录类型错，bind命令中的logintype字段出错。");
        errorMsgs.put(SGIP_RESP_PARAM_INVALID,"参数格式错，指命令中参数值与参数类型不符或与协议规定的范围不符。");
        errorMsgs.put(SGIP_RESP_INVALID_MOBILE,"非法手机号码字段，出现非86130/186等号段或手机号码前未加“86”。");
        errorMsgs.put(SGIP_RESP_INVALID_ID,"消息ID错。");
        errorMsgs.put(SGIP_RESP_ERR_LENGTH,"消息长度错误。");
        errorMsgs.put(SGIP_RESP_INVALID_SN,"非法序列号，包括序列号重复、序列号格式错误等");
        errorMsgs.put(SGIP_RESP_ERR_GNS,"非法操作GNS！但是，你确定要操作GNS吗？");
        errorMsgs.put(SGIP_RESP_BUZY,"节点忙，节点存储队列满或其他原因，暂时不能提供服务。");
        errorMsgs.put(SGIP_RESP_TIME_OUT,"超时错误，在规定时间内对方没有发送信息确认或者无法获取数据！");
        errorMsgs.put(SGIP_RESP_SYSTEM_ERR,"系统错误！");
        errorMsgs.put(SGIP_RPT_DEST_BREAK,"目的地址不可达，路由表存在路由且消息路由正确但被路由的节点暂时不能提供服务。");
        errorMsgs.put(SGIP_RPT_ROUTE_ERR,"路由错，路由表存在路由但消息路由出错的情况，如转错SMG等");
        errorMsgs.put(SGIP_RPT_ROUTE_INVALID,"路由不存在，消息路由的节点在路由表中不存在");
        errorMsgs.put(SGIP_RPT_AUTH_FAIL,"计费号码无效，鉴权不成功时反馈的错误信息");
        errorMsgs.put(SGIP_RPT_OUT_OF_SERVICE,"用户不能通信（如不在服务区、未开机等情况）");
        errorMsgs.put(SGIP_RPT_MEM_FULL,"手机内存不足！");
        errorMsgs.put(SGIP_RPT_NOT_SUPPORT_SM,"手机不支持短消息！");
        errorMsgs.put(SGIP_RPT_RECV_SM_ERR,"手机接收短消息出现错误！");
        errorMsgs.put(SGIP_RPT_UNKNOWN_USER,"不知道的用户！");
        errorMsgs.put(SGIP_RPT_NOT_SUPPLY,"不提供此功能！");
        errorMsgs.put(SGIP_RPT_INVALID_EQUIP,"非法设备！");
        errorMsgs.put(SGIP_RPT_SYSTEM_FAIL,"系统失败！");
        errorMsgs.put(SGIP_RPT_SMC_QUEUE_FULL,"短信中心队列满");
        errorMsgs.put(SGIP_RPT_TYPE_ERR,"类型错误！");
        errorMsgs.put(SGIP_CHECK_MESSAGE_ERR,"检查消息错误！");
        errorMsgs.put(SGIP_TIME_OUT_ERR,"超时错误！");
        errorMsgs.put(SGIP_LINK_BREAKED_ERR,"连接出现问题！");
        errorMsgs.put(SGIP_LOGIN_FAIL_ERR,"登录出现问题！");
    }
}