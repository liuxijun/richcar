package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserRecommandLogicInterface
		extends
			BaseLogicInterface<UserRecommand> {

       public List<UserRecommand> getAllUserRecommand(UserRecommand userRecommand, PageBean pageBean);

     public SearchResult<UserRecommand> getUserRecommandCount(UserRecommand userRecommand, PageBean pageBean);
}
