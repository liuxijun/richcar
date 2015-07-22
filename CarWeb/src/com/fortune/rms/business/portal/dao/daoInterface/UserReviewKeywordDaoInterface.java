package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserReviewKeywordDaoInterface
		extends
			BaseDaoInterface<UserReviewKeyword, Long> {

    public boolean isExistUserReviewKeyword(String word);

    public  List<Object[]> getAllUserReviewwords(UserReviewKeyword userReviewKeyword, PageBean pageBean);

    public List<UserReviewKeyword> getAllUserReviewwordsByCspId(long cspId);

}