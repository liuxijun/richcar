package com.fortune.rms.business.user.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.user.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12-9-11
 * Time: обнГ2:21
 */
public interface UserDaoInterface extends BaseDaoInterface<User, Long> {
    public boolean checkLoginName(User user);
    public User getUserById(Long id);
}
