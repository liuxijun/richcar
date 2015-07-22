package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.user.dao.daoInterface.RecommendFriendDaoInterface;
import com.fortune.rms.business.user.model.RecommendFriend;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-24
 * Time: обнГ3:32
 */
@Repository
public class RecommendFriendDaoAccess extends BaseDaoAccess<RecommendFriend, Long> implements RecommendFriendDaoInterface {
    public RecommendFriendDaoAccess() {
        super(RecommendFriend.class);
    }
}
