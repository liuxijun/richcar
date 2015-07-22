package com.fortune.rms.business.portal.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewReportDaoInterface;
import com.fortune.rms.business.portal.model.UserReviewReport;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserReviewReportDaoAccess
		extends
			BaseDaoAccess<UserReviewReport, Long>
		implements
			UserReviewReportDaoInterface {




    public UserReviewReportDaoAccess() {
		super(UserReviewReport.class);
	}

    public List<UserReviewReport> getAllUserReviewReports(UserReviewReport userReviewReport, PageBean pageBean) {
        String hql = "userReviewId in (select id from UserReview where status=5)";
        return this.getObjects(userReviewReport,pageBean,null,hql);  
    }
}
