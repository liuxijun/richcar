package com.fortune.threeScreen.webservice;

import com.fortune.rms.business.user.model.SsoKeyResp;
import com.fortune.util.AppConfigurator;
import com.fortune.util.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-4-27
 * Time: 16:00:06
 *
 */
public class SsoKeyService {

    public SsoKeyResp updateSsoKey(int seqNo,String Key,String effectTime){
        Configuration config = new Configuration();
        config.setValue("ssoKey.key",Key);
        config.setValue("ssoKey.effectTime",effectTime);
        AppConfigurator.getInstance(effectTime);
        int i = seqNo;
        SsoKeyResp ssoKeyResp = new SsoKeyResp();
        if(seqNo != 0){
            if(Key != null || effectTime != null){
                ssoKeyResp.setSeqNo(i);
                ssoKeyResp.setResult(0);
                ssoKeyResp.setResultDesc("³É¹¦");
            }else{
                ssoKeyResp.setSeqNo(seqNo);
                ssoKeyResp.setResult(1);
                ssoKeyResp.setResultDesc("error");
            }
            return ssoKeyResp;
        }
        return null;
    }
}

