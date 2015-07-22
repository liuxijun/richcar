package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserRecommandDaoInterface
		extends
			BaseDaoInterface<UserRecommand, Long> {


   public List<Object[]> getAllUserRecommand(UserRecommand userRecommand, PageBean pageBean);

   public SearchResult<UserRecommand> getUserRecommandCount(UserRecommand userRecommand, PageBean pageBean);
}