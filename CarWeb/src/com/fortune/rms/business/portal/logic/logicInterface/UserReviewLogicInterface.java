package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.portal.model.UserReview;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.Date;
import java.util.List;

public interface UserReviewLogicInterface
		extends
			BaseLogicInterface<UserReview> {

    public List<UserReview> getAllUserReviews(UserReview userReview, PageBean pageBean);
    public void updateUserReviewById(Long id, Long status);
    public SearchResult<UserReview> getAllUserReviewsByContentId(UserReview userReview, PageBean pageBean, long contentId);
    public void saveUserReviewByKeyWord(UserReview userReview);
    public List<UserReview> searchUserReviewByContentIdAndCspId(long contentId, long cspId);
    public List<UserReview> searchUserReviewByContentIdAndCspId1(long contentId, long cspId);
    public UserReview searchLastUserReviewsByContentIdAndCspId(long contentId, long cspId);
    public void changeUserReviewStatus(long userReviewId, long status, String desp, String time);
     public UserReview getUserReview(Long userReviewId);
}
