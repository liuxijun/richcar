package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.UserType;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: обнГ2:19
 * To change this template use File | Settings | File Templates.
 */

public interface UserTypeDaoInterface extends BaseDaoInterface<UserType,Long> {
    public void changeUserType(long typeId, long altTypeId);
}
