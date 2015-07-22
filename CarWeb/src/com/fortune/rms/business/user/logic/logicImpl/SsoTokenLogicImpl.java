package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.rms.business.user.logic.logicInterface.SsoTokenLogicInterface;
import com.fortune.rms.business.user.model.SsoUserToken;
import com.fortune.util.Base64;
import com.fortune.util.SHA1Util;
import com.fortune.util.ThreeDesUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-5-9
 * Time: 18:27:14
 *
 */
@Service("ssoTokenLogicInterface")
public class SsoTokenLogicImpl implements SsoTokenLogicInterface {

    public String userBindSso(String userId,String url,String key){
        String ssoToken = null;
        SsoUserToken ssoUserBind = new SsoUserToken();
        ssoUserBind.setUserId(userId);
        ssoUserBind.setReturnUrl(url);
        Date dt= new Date();
        Long time= dt.getTime();
        ssoUserBind.setExpiredTimeStamp_SSO(""+time);
        ssoUserBind.setExpiredTimeStamp_UE(""+time);
        ssoUserBind.setProvinceNo(GenerateGUID());
        String hash_sha1 = SHA1Util.HashBase64(ssoUserBind.getExpiredTimeStamp_SSO()+ssoUserBind.getUserId());
        String userToken = GenerateGUID();
//        System.out.println(hash_sha1);
        String data =ssoUserBind.getUserId()+"$"+ssoUserBind.getProvinceNo()+"$"+userToken+"$"+ssoUserBind.getReturnUrl()+"$"+ssoUserBind.getExpiredTimeStamp_SSO()+"$"+ssoUserBind.getExpiredTimeStamp_UE()+"$"+hash_sha1;
        String s = new String(ThreeDesUtil.byte2Hex(ThreeDesUtil.encryptMode(key.getBytes(),data.getBytes())));
//        System.out.println("加密后："+s);
        ssoToken = new String(Base64.encode((ssoUserBind.getReturnUrl()+"$"+s).getBytes()));
        return ssoToken;
    }

    public String userLogicSso(String userId,String url,String key){
        String ssoToken = null;
        SsoUserToken ssoUserBind = new SsoUserToken();
        ssoUserBind.setUserId(userId);
        ssoUserBind.setReturnUrl(url);
        Date dt= new Date();
        Long time= dt.getTime();
        ssoUserBind.setExpiredTimeStamp_SSO(""+time);
        ssoUserBind.setExpiredTimeStamp_UE(""+time);
        ssoUserBind.setProvinceNo(GenerateGUID());
        String hash_sha1 = SHA1Util.HashBase64(ssoUserBind.getExpiredTimeStamp_SSO()+ssoUserBind.getUserId());
        String userToken = GenerateGUID();
//        System.out.println(hash_sha1);
        String data =ssoUserBind.getUserId()+"$"+ssoUserBind.getProvinceNo()+"$"+userToken+"$"+ssoUserBind.getReturnUrl()+"$"+ssoUserBind.getExpiredTimeStamp_SSO()+"$"+ssoUserBind.getExpiredTimeStamp_UE()+"$"+hash_sha1;
        String s = new String(ThreeDesUtil.byte2Hex(ThreeDesUtil.encryptMode(key.getBytes(),data.getBytes())));
//        System.out.println("加密后："+s);
        ssoToken = new String(Base64.encode((ssoUserBind.getReturnUrl()+"$"+s).getBytes()));
        return ssoToken;
    }

    public static final String GenerateGUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
