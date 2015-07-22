package com.fortune.vac;

import com.fortune.vac.webservice.GetSubscriptionReq;
import com.fortune.vac.webservice.SubInfo;

/**
 * Created with IntelliJ IDEA.
 * User: JA
 * Date: 13-2-1
 * Time: ����1:42
 * To change this template use File | Settings | File Templates.
 *
 */
public class VacSubscription {

    //�鿴�û��Ƿ��йۿ��㲥ý���Ȩ��
    public boolean checkSubscription(SubInfo[] subInfo) {
        boolean  effective = false;
        if(subInfo == null || subInfo.length < 1) {
            return effective;
        } else {
            for(SubInfo s : subInfo) {
                if(s.getStatus() == 0) {
                    //�ж�״̬���ͼ�����Ч���ڣ�������break������true
                    effective = true;
                    break;
                }
            }
        }
        return effective;
    }
}
