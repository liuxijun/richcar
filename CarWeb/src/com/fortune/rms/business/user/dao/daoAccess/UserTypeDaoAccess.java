package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.user.dao.daoInterface.UserTypeDaoInterface;
import com.fortune.rms.business.user.model.UserType;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: xy
 * Date: 13-12-4
 * Time: ����2:21
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class UserTypeDaoAccess extends BaseDaoAccess<UserType,Long> implements UserTypeDaoInterface{

    public UserTypeDaoAccess() {
        super(UserType.class);
    }

    /**
     * ���û����ʹ�typeIdָ�����͸�ΪaltTypeId
     * @param typeId    ԭid
     * @param altTypeId �������Id
     */
    public void changeUserType(long typeId, long altTypeId){
        String hql = "update FrontUser fu set fu.typeId=" + altTypeId + " where typeId=" + typeId;
        executeUpdate(hql);
    }
}
