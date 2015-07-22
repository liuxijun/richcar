package com.fortune.util;

import com.fortune.rms.business.system.logic.logicInterface.IndividualLogicInterface;
import com.fortune.rms.business.system.model.Individual;

import javax.swing.*;
import java.util.List;

/**
 * Created by 王明路 on 2014/12/5.
 * 个性化
 */
public class IndividualUtils {
    private static IndividualUtils oneInstance;

    private String logoURL; // 设置的个性logourl
    private String mobileLogoURL; // 个性化的手机logo
    private String name;    // 设置的系统名称，可以放在系统title中

    public static IndividualUtils getInstance() {
        oneInstance = new IndividualUtils();
        return oneInstance;
    }

    public IndividualUtils() {
        // 获得现有配置
        IndividualLogicInterface individualLogic = (IndividualLogicInterface)SpringUtils.getBeanForApp("individualLogicInterface");
        if( individualLogic != null ){
            List<Individual> l = individualLogic.getAll();
            if( l == null || l.size() == 0){
                // 设置默认值
                logoURL = "/page/redex/assets/images/logo.png";
                mobileLogoURL = "/page/redex/assets/images/logo2.png";
                name = "Redex";
            }else{
                logoURL = l.get(0).getLogoPath();
                name = l.get(0).getName();
            }
        }
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileLogoURL() {
        return mobileLogoURL;
    }

    public void setMobileLogoURL(String mobileLogoURL) {
        this.mobileLogoURL = mobileLogoURL;
    }
}
