package com.fortune.vac;

import com.fortune.vac.webservice.GetSubscriptionReq;
import com.fortune.vac.webservice.SubInfo;

/**
 * Created with IntelliJ IDEA.
 * User: JA
 * Date: 13-2-1
 * Time: 下午1:42
 * To change this template use File | Settings | File Templates.
 *
 */
public class VacSubscription {

    //查看用户是否有观看点播媒体的权限
    public boolean checkSubscription(SubInfo[] subInfo) {
        boolean  effective = false;
        if(subInfo == null || subInfo.length < 1) {
            return effective;
        } else {
            for(SubInfo s : subInfo) {
                if(s.getStatus() == 0) {
                    //判断状态，和检验有效日期，符合则break，返回true
                    effective = true;
                    break;
                }
            }
        }
        return effective;
    }
}
