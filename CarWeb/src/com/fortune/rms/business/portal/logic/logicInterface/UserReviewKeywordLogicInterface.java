package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserReviewKeywordLogicInterface
		extends
			BaseLogicInterface<UserReviewKeyword> {
    public boolean isExistUserReviewKeyword(String word);

    public List<UserReviewKeyword> getAllUserReviewKeywords(UserReviewKeyword userReviewKeyword, PageBean pageBean);

    public List<UserReviewKeyword> getAllUserReviewKeywordsByCspId(long cspId);
    
}
