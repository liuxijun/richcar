package com.fortune.rms.business.user.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.user.model.UserType;
import com.fortune.rms.business.user.model.UserTypeDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: обнГ2:15
 * To change this template use File | Settings | File Templates.
 */
public interface UserTypeLogicInterface extends BaseLogicInterface<UserType> {
    public List<UserTypeDetail> getUserType();
    public void removeUserType(Long id, Long altId);
    public boolean referenceChannels(long typeId, String channels);
}
