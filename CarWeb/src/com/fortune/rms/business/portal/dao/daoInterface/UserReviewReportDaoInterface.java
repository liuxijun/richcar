package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.model.UserReviewReport;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserReviewReportDaoInterface
		extends
			BaseDaoInterface<UserReviewReport, Long> {

    public List<UserReviewReport> getAllUserReviewReports(UserReviewReport userReviewReport, PageBean pageBean);

}