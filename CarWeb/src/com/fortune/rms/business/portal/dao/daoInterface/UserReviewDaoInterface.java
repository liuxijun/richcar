package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.model.UserReview;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.Date;
import java.util.List;

public interface UserReviewDaoInterface
		extends
			BaseDaoInterface<UserReview, Long> {

    public List<Object[]> getAllUserReviews(UserReview userReview, PageBean pageBean);

    public void updateUserReviewById(Long id, Long status);

    public SearchResult<UserReview> getAllUserReviewsByContentId(UserReview userReview, PageBean pageBean, long contentId);

    public List<UserReview> searchUserReviewByContentIdAndCspId(long contentId, long cspId);

    public List<UserReview> searchUserReviewByContentIdAndCspId1(long contentId, long cspId);

    public UserReview searchLastUserReviewsByContentIdAndCspId(long contentId, long cspId);

    public void changeUserReviewStatus(long userReviewId, long status, String desp, String time);

}