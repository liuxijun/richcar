package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewReport;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserReviewReportLogicInterface
		extends
			BaseLogicInterface<UserReviewReport> {

    public List<UserReviewReport> getAllUserReviewReports(UserReviewReport userReviewReport, PageBean pageBean);

    public void updateUserReviewStatus(Long userReviewId, Long userReviewStatus);

}
