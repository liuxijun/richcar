package com.fortune.vac;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JA
 * Date: 13-1-29
 * Time: ����1:10
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionErrorCode {
    public static Map<Integer,String> subscriptionErrorCode = new HashMap<Integer, String>();
    static {
        subscriptionErrorCode.put(101,"���Ͷ��豸���ͷǷ�");
        subscriptionErrorCode.put(102,"���Ͷ��豸ID�Ƿ�");
        subscriptionErrorCode.put(103,"�û�����Ƿ�");
        subscriptionErrorCode.put(104,"�û����ͷǷ�");
        subscriptionErrorCode.put(105,"����Ƿ�");
        subscriptionErrorCode.put(106,"�ʼ��Ƿ�");
        subscriptionErrorCode.put(107,"�������ͷǷ�");
        subscriptionErrorCode.put(108,"�û���Ϣ������");
        subscriptionErrorCode.put(109,"�û�����WEB�Ż�ע��");
        subscriptionErrorCode.put(110,"�û�δ��WEB�Ż�ע��");
        subscriptionErrorCode.put(111,"���������");
        subscriptionErrorCode.put(112,"������Ƿ�");
        subscriptionErrorCode.put(113,"����Ƿ�");
        subscriptionErrorCode.put(114,"�û��ն����ͷǷ�");
        subscriptionErrorCode.put(115,"�û���ע��");
        subscriptionErrorCode.put(116,"��ƷID�Ƿ�");
        subscriptionErrorCode.put(117,"�ײ�ID�Ƿ�");
        subscriptionErrorCode.put(118,"�������û�����Ƿ�");
        subscriptionErrorCode.put(119,"�������û����ͷǷ�");
        subscriptionErrorCode.put(120,"ʹ�÷��û�����Ƿ�");
        subscriptionErrorCode.put(121,"ʹ�÷��û����ͷǷ�");
        subscriptionErrorCode.put(122,"���ѷ��û�����Ƿ�");
        subscriptionErrorCode.put(123,"���ѷ��û����ͷǷ�");
        subscriptionErrorCode.put(124,"������������");
        subscriptionErrorCode.put(125,"�û�״̬������");
        subscriptionErrorCode.put(121,"�û�����");
        subscriptionErrorCode.put(126,"ʹ�÷��û����ͷǷ�");
        subscriptionErrorCode.put(127,"ҵ��״̬������");
        subscriptionErrorCode.put(128,"��Ʒ״̬������");
        subscriptionErrorCode.put(129,"�ײ�״̬������");
        subscriptionErrorCode.put(130,"������ϵ������");
        subscriptionErrorCode.put(131,"������ϵ�Ѵ���");
        subscriptionErrorCode.put(132,"������ϵ������");
        subscriptionErrorCode.put(133,"�û�û�п�ͨ��ҵ��������Ҫ��ʡ�Ż��յ��ô��������ʾ�û���CRM��ͨ��ҵ����������ȫ���Ż��յ��ô�����󣬽�����Ϣת��ʡ�Ż�����ʾ�û���CRM��ͨ��ҵ��������");
        subscriptionErrorCode.put(134,"��ʼʱ��Ƿ�");
        subscriptionErrorCode.put(135,"����ʱ��Ƿ�");
        subscriptionErrorCode.put(136,"�û��Ѵ��ں�������");
        subscriptionErrorCode.put(999,"����");

    }

    public static String getSubscriptionErrorMessage(Integer code) {
         return subscriptionErrorCode.get(code);
    }
}
