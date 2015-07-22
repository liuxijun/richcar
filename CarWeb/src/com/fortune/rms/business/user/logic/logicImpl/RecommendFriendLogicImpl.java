package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.user.dao.daoInterface.RecommendFriendDaoInterface;
import com.fortune.rms.business.user.logic.logicInterface.RecommendFriendLogicInterface;
import com.fortune.rms.business.user.model.RecommendFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-24
 * Time: ÏÂÎç3:31
 */
@Service("friendRecommendFriendLogicInterface")

public class RecommendFriendLogicImpl extends BaseLogicImpl<RecommendFriend> implements RecommendFriendLogicInterface {
    private RecommendFriendDaoInterface recommendFriendDaoInterface;

    @Autowired
    public void setRecommendFriendDaoInterface(RecommendFriendDaoInterface recommendFriendDaoInterface) {
        this.recommendFriendDaoInterface = recommendFriendDaoInterface;
        baseDaoInterface = (BaseDaoInterface) recommendFriendDaoInterface;

    }
}
