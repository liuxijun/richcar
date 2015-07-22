package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;
import java.util.Map;

public interface UserScoringDaoInterface
		extends
			BaseDaoInterface<UserScoring, Long> {
      public SearchResult<UserScoring> getAllUserScoringCount(UserScoring userScoring, PageBean pageBean) ;

      public List<Object[]> getUserScoringByContentIdAndCspId(UserScoring userScoring, PageBean pageBean);

      public Map<String,String> getUserScoringCountByContentIdAndCspId(long contentId, long cspId);
      public Long redexScoreRangeCount(long contentId, int min, int max);
}