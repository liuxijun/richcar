package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: ����1:04
 * ����
 */
public class Command {
    public static final int CMDID_Bind=0x10000001;//��������
    public static final int CMDID_BindResp=0x80000001;//	����Ӧ��
    public static final int CMDID_UnBind=0x10000002;//	ȥ��������
    public static final int CMDID_UnBindResp=0x80000002;//	ȥ����Ӧ��
    public static final int CMDID_Handset=0x10000003;//	������������
    public static final int CMDID_HandsetResp=0x80000003;//	��������Ӧ��
    public static final int CMDID_CheckPrice=0x10000005;//	��Ȩ��������
    public static final int CMDID_CheckPriceResp=0x80000005;//	��Ȩ����Ӧ��
    public static final int CMDID_CheckPriceConfirm=0x10000006;//	��Ȩ����ȷ������
    public static final int CMDID_CheckPriceConfirmResp=0x80000006;//	��Ȩ����ȷ��Ӧ��
    public static final int CMDID_TrafficPrice=0x10000007;//	������������
    public static final int CMDID_TrafficPriceResp=0x80000007;//	��������Ӧ��
    public static final int CMDID_ContentAbstractReq=0x10000008;//	����������ժҪ����
    public static final int CMDID_ContentAbstractResp=0x80000008;//	����������ժҪ��Ӧ
    public static final Long OPERATE_TYPE_SUBSCRIBE = 1L;
    public static final Long OPERATE_TYPE_DIS_SUBSCRIBE = 2L;
    public static final Long OPERATE_TYPE_DIS_SUBSCRIBE_SERVICE_ID = 3L;
    public static final Long OPERATE_TYPE_VOD = 4L;
    public static final Long OPERATE_TYPE_CHECK = 6L;

    public static Map<Integer,String> commandNames=new HashMap<Integer, String>();
    static{
        commandNames.put(CMDID_Bind,"bind");
        commandNames.put(CMDID_BindResp,"bindResp");
        commandNames.put(CMDID_UnBind,"unBind");
        commandNames.put(CMDID_UnBindResp,"unBindResp");
        commandNames.put(CMDID_Handset,"handset");
        commandNames.put(CMDID_HandsetResp,"handsetResp");
        commandNames.put(CMDID_CheckPrice,"checkPrice");
        commandNames.put(CMDID_CheckPriceResp,"checkPriceResp");
        commandNames.put(CMDID_CheckPriceConfirm,"checkPriceConfirm");
        commandNames.put(CMDID_CheckPriceConfirmResp,"checkPriceConfirmResp");
        commandNames.put(CMDID_TrafficPrice,"trafficPrice");
        commandNames.put(CMDID_TrafficPriceResp,"trafficPriceResp");
        commandNames.put(CMDID_ContentAbstractReq,"contentAbstractReq");
        commandNames.put(CMDID_ContentAbstractResp,"contentAbstractResp");
    }
}
