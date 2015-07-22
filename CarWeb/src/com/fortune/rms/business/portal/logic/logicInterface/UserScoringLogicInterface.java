package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;
import java.util.Map;

public interface UserScoringLogicInterface
		extends
			BaseLogicInterface<UserScoring> {
      public SearchResult<UserScoring> getAllUserScoringCount(UserScoring userScoring, PageBean pageBean);
      public List<UserScoring> getUserScoringByContentIdAndCspId(UserScoring userScoring, PageBean pageBean) ;

      public Map<String,String> getUserScoringCountByContentIdAndCspId(long contentId, long cspId);

     public UserScoring getUserScoring(Long id);
    public Long redexGetScoreRangeCount(long contentId, int min, int max);
    public UserScoring doScore(String userId, long contentId, int score, String ip);

}
