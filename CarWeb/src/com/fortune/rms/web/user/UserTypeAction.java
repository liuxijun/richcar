package com.fortune.rms.web.user;
import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.user.logic.logicInterface.UserTypeLogicInterface;
import com.fortune.rms.business.user.model.UserType;
import com.fortune.rms.business.user.model.UserTypeDetail;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ����10:25
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/user")
@ParentPackage("default")
@Action(value="userType")
@Results({
        @Result(name = "typeList",location = "/sys/typeDetailList.jsp")
})

public class UserTypeAction extends BaseAction<UserType> {
    private String typeListJson;
    private Long altType;       // ɾ���û�����ʱ��ԭ�е��û����������id
    private String channels;

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public Long getAltType() {
        return altType;
    }

    public void setAltType(Long altType) {
        this.altType = altType;
    }

    public String getTypeListJson() {
        return typeListJson;
    }

    public UserTypeAction() {
        super(UserType.class);
    }

    private UserTypeLogicInterface userTypeLogicInterface;


    public void setUserTypeLogicInterface(UserTypeLogicInterface userTypeLogicInterface) {
        this.userTypeLogicInterface = userTypeLogicInterface;
        setBaseLogicInterface(userTypeLogicInterface);
    }

    // added by mlwang @2014-10-27
    // ��ȡ�û����ͣ�������
    public String userTypeList(){
        List<UserTypeDetail> userTypeList = userTypeLogicInterface.getUserType();
        typeListJson = getJsonArray(userTypeList);
        return "typeList";
    }

    public String remove(){
        userTypeLogicInterface.removeUserType(obj.getId(), altType);
        writeSysLog("ɾ���û�����ID��" + obj.getId() + " ��ԭ���û�����IdΪ" + altType + "���û�����");
        setSuccess(true);
        return "delete";
    }

    /**
     * Ϊ�û����͹�����Ŀ����
     * @return SUCCESS
     */
    public String referenceChannel(){
        if( obj != null && channels != null){
           setSuccess(userTypeLogicInterface.referenceChannels(obj.getId(), channels));
        }
        return SUCCESS;
    }
}
