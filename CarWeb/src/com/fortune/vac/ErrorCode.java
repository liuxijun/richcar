package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-9-23
 * Time: ����12:53
 * ��������
 */
public class ErrorCode {
    public static final int ERROR_NOT_BINDED=10000;
    public static final int ERROR_CAN_NOT_CONNECT=10001;
    public static final int ERROR_SEND_ERROR=10002;
    public static Map<Integer,String> errorCode = new HashMap<Integer,String>();
    static{
        errorCode.put(0,"ִ�н���ɹ���");
        errorCode.put(1,"PDU������Ч������30K����С��0��");
        errorCode.put(2,"����PDUʧ�ܣ�ȱ�������������Ч��");
        errorCode.put(3,"�󶨵��ʺ���Ч");
        errorCode.put(4,"�󶨵����벻ƥ��");
        errorCode.put(5,"ҵ�������Ѿ�����");
        errorCode.put(6,"ҵ�����滹û�б���");
        errorCode.put(7,"�ȴ���Ӧ��Ϣ��ʱ");
        errorCode.put(8,"�ȴ�ȷ����Ϣ��ʱ");
        errorCode.put(100,"��ֵ��Ч(notExpectedOrError)");
        errorCode.put(101,"Դ���豸���ͳ�����Χ");
        errorCode.put(102,"Դ���豸ID����ȷ");
        errorCode.put(103,"Ŀ�Ķ��豸���ͳ�����Χ");
        errorCode.put(104,"Ŀ�Ķ��豸ID����ȷ");
        errorCode.put(105,"ʱ�����ʽ����ȷ");
        errorCode.put(106,"�����û�ID��Ч");
        errorCode.put(107,"MSISDN��Ч");
        errorCode.put(108,"SP ��ʶ��Ч");
        errorCode.put(109,"ҵ���ʶ��Ч");
        errorCode.put(110,"ҵ���������Ч");
        errorCode.put(500,"Ӳ�̶�д����");
        errorCode.put(501,"�������Ӳ�����");
        errorCode.put(502,"�������");
        errorCode.put(503,"LICENSE�ļ����Ϸ�");
        errorCode.put(504,"ע���û���Ŀ����LICENSE����");
        errorCode.put(505,"ϵͳ�ڲ�����");
        errorCode.put(506,"���ݿ���� (���ݿ����Ӷϵ�,SQL�﷨��Ч)");
        errorCode.put(600,"��Ӫ�̲�����");
        errorCode.put(601,"��ɫ������");
        errorCode.put(602,"��Ա������");
        errorCode.put(603,"����Ա������");
        errorCode.put(604,"��Ӫ��û���㹻Ȩ��");
        errorCode.put(1001,"�����û�������");
        errorCode.put(1002,"�����û�״̬��ֹͣ");
        errorCode.put(1003,"�����û�Ƿ�ѣ�Ԥ�����û��Ʒ�ʧ��");
        errorCode.put(1004,"�����û��ں�����");
        errorCode.put(1005,"��Ч�û����û����ٵ�ǰƽ̨��");
        errorCode.put(1006,"��ҵ�������ѱ�����");
        errorCode.put(1007,"�û��޷�����/�ָ�ҵ������");
        errorCode.put(1100,"ָ����ҵ�񲻶��û�����");
        errorCode.put(1103,"�û���ע��");
        errorCode.put(1104,"�û�������");
        errorCode.put(1105,"�û�״̬������");
        errorCode.put(1106,"�û�����������");
        errorCode.put(1107,"����α��ʧ��");
        errorCode.put(1200,"������ϵ�Ѵ��ڣ�ָͬһ��Ʒ�û��Ѷ���");
        errorCode.put(1201,"������ϵ������");
        errorCode.put(1202,"������ϵ״̬������");
        errorCode.put(1203,"ͬ��������ϵ��SPʧ��");
        errorCode.put(1204,"�û����ܶ�����������ҵ��ָ�������Ʒ�û��Ѷ���");
        errorCode.put(1205,"ʣ��Ķ�����ϵ��Ŀ����");
        errorCode.put(1206,"ALS/Fax����Ϊ��");
        errorCode.put(1600,"XX��ʾCRM���ش�����");
        errorCode.put(2000,"SP������");
        errorCode.put(2001,"SP״̬������");
        errorCode.put(2002,"SP���öȵ�");
        errorCode.put(2100,"ҵ�񲻴���");
        errorCode.put(2101,"ҵ�񲻿��ţ�״̬��������");
        errorCode.put(2200,"û�д�����ҵ��(SMS,MMS,etc.)");
        errorCode.put(2201,"ҵ���ܶ���");
        errorCode.put(2202,"ʣ������ʻ�����");
        errorCode.put(2203,"ҵ�����öȵ�");
        errorCode.put(2204,"ҵ��URL����ʧ��");
        errorCode.put(2205,"���շ�ҵ�񣬲����������㲥");
        errorCode.put(2209,"��Ʒ���ܴ��ն˲ඩ�����˶�");
        errorCode.put(3000,"CDR��ʽ����");
        errorCode.put(3001,"�۸�Ϊ��");
        errorCode.put(3002,"�۸��ʽ����");
        errorCode.put(3003,"�۸񳬳���Χ");
        errorCode.put(3100,"�û�����һ��Ԥ�����û�");
        errorCode.put(3101,"�û�����");
        errorCode.put(3102,"ѹ�����ʧ��");
        errorCode.put(3103,"û����Ҫ�ļƷ���Ϣ");
        errorCode.put(3104,"дCDRʧ��");
        errorCode.put(3105,"CDR������");
        errorCode.put(3106,"����CDR�����ݿ�ʧ��");
        errorCode.put(3107,"CDR�۸�̫��");
        errorCode.put(3108,"�Ⱥ�SCP��Ӧʧ��");
        errorCode.put(3109,"��������ƷѾ�����ڴ�ʧ��");
        errorCode.put(4001,"BSS��û���û���Ϣ");
        errorCode.put(4002,"��BSSͬ���û���Ϣʧ��");
        errorCode.put(4003,"����������ƽ̨ͬ���û���Ϣʧ��");
        errorCode.put(4004,"ͬ��SP����������ƽ̨ʧ��");
        errorCode.put(4005,"ͬ��ҵ�����ݸ���������ƽ̨ʧ��");
        errorCode.put(4100,"ͬ��������ϵ��SPʧ��");
        errorCode.put(4200,"ɾ��MSISDNʧ��");
        errorCode.put(4201,"�ı�MSISDNʧ��");
        errorCode.put(ERROR_NOT_BINDED,"��δBind��VAC");
        errorCode.put(ERROR_CAN_NOT_CONNECT,"�޷����ӵ�������");
        errorCode.put(ERROR_SEND_ERROR,"��������ʱ��������");
    }

    public static String getErrorMessage(int code){
        return errorCode.get(code);
    }
}
