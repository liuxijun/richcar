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
        errorMsgs.put(SGIP_RESP_SUCCESS,"�����ɹ���û�з�������");
        errorMsgs.put(SGIP_RESP_LOGIN_FAIL,"�Ƿ���¼�����¼�������������¼���������ȡ�");
        errorMsgs.put(SGIP_RESP_RE_LOGIN,"�ظ���¼������ͬһTCP/IP�����������������������¼��");
        errorMsgs.put(SGIP_RESP_LOGIN_FULL,"���ӹ��ָ࣬�����ڵ�Ҫ��ͬʱ���������������ࡣ");
        errorMsgs.put(SGIP_RESP_ERR_LOGTYPE,"��¼���ʹ�bind�����е�logintype�ֶγ���");
        errorMsgs.put(SGIP_RESP_PARAM_INVALID,"������ʽ��ָ�����в���ֵ��������Ͳ�������Э��涨�ķ�Χ������");
        errorMsgs.put(SGIP_RESP_INVALID_MOBILE,"�Ƿ��ֻ������ֶΣ����ַ�86130/186�ȺŶλ��ֻ�����ǰδ�ӡ�86����");
        errorMsgs.put(SGIP_RESP_INVALID_ID,"��ϢID��");
        errorMsgs.put(SGIP_RESP_ERR_LENGTH,"��Ϣ���ȴ���");
        errorMsgs.put(SGIP_RESP_INVALID_SN,"�Ƿ����кţ��������к��ظ������кŸ�ʽ�����");
        errorMsgs.put(SGIP_RESP_ERR_GNS,"�Ƿ�����GNS�����ǣ���ȷ��Ҫ����GNS��");
        errorMsgs.put(SGIP_RESP_BUZY,"�ڵ�æ���ڵ�洢������������ԭ����ʱ�����ṩ����");
        errorMsgs.put(SGIP_RESP_TIME_OUT,"��ʱ�����ڹ涨ʱ���ڶԷ�û�з�����Ϣȷ�ϻ����޷���ȡ���ݣ�");
        errorMsgs.put(SGIP_RESP_SYSTEM_ERR,"ϵͳ����");
        errorMsgs.put(SGIP_RPT_DEST_BREAK,"Ŀ�ĵ�ַ���ɴ·�ɱ����·������Ϣ·����ȷ����·�ɵĽڵ���ʱ�����ṩ����");
        errorMsgs.put(SGIP_RPT_ROUTE_ERR,"·�ɴ�·�ɱ����·�ɵ���Ϣ·�ɳ�����������ת��SMG��");
        errorMsgs.put(SGIP_RPT_ROUTE_INVALID,"·�ɲ����ڣ���Ϣ·�ɵĽڵ���·�ɱ��в�����");
        errorMsgs.put(SGIP_RPT_AUTH_FAIL,"�ƷѺ�����Ч����Ȩ���ɹ�ʱ�����Ĵ�����Ϣ");
        errorMsgs.put(SGIP_RPT_OUT_OF_SERVICE,"�û�����ͨ�ţ��粻�ڷ�������δ�����������");
        errorMsgs.put(SGIP_RPT_MEM_FULL,"�ֻ��ڴ治�㣡");
        errorMsgs.put(SGIP_RPT_NOT_SUPPORT_SM,"�ֻ���֧�ֶ���Ϣ��");
        errorMsgs.put(SGIP_RPT_RECV_SM_ERR,"�ֻ����ն���Ϣ���ִ���");
        errorMsgs.put(SGIP_RPT_UNKNOWN_USER,"��֪�����û���");
        errorMsgs.put(SGIP_RPT_NOT_SUPPLY,"���ṩ�˹��ܣ�");
        errorMsgs.put(SGIP_RPT_INVALID_EQUIP,"�Ƿ��豸��");
        errorMsgs.put(SGIP_RPT_SYSTEM_FAIL,"ϵͳʧ�ܣ�");
        errorMsgs.put(SGIP_RPT_SMC_QUEUE_FULL,"�������Ķ�����");
        errorMsgs.put(SGIP_RPT_TYPE_ERR,"���ʹ���");
        errorMsgs.put(SGIP_CHECK_MESSAGE_ERR,"�����Ϣ����");
        errorMsgs.put(SGIP_TIME_OUT_ERR,"��ʱ����");
        errorMsgs.put(SGIP_LINK_BREAKED_ERR,"���ӳ������⣡");
        errorMsgs.put(SGIP_LOGIN_FAIL_ERR,"��¼�������⣡");
    }
}