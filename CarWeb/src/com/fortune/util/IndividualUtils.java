package com.fortune.util;

import com.fortune.rms.business.system.logic.logicInterface.IndividualLogicInterface;
import com.fortune.rms.business.system.model.Individual;

import javax.swing.*;
import java.util.List;

/**
 * Created by ����· on 2014/12/5.
 * ���Ի�
 */
public class IndividualUtils {
    private static IndividualUtils oneInstance;

    private String logoURL; // ���õĸ���logourl
    private String mobileLogoURL; // ���Ի����ֻ�logo
    private String name;    // ���õ�ϵͳ���ƣ����Է���ϵͳtitle��

    public static IndividualUtils getInstance() {
        oneInstance = new IndividualUtils();
        return oneInstance;
    }

    public IndividualUtils() {
        // �����������
        IndividualLogicInterface individualLogic = (IndividualLogicInterface)SpringUtils.getBeanForApp("individualLogicInterface");
        if( individualLogic != null ){
            List<Individual> l = individualLogic.getAll();
            if( l == null || l.size() == 0){
                // ����Ĭ��ֵ
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
