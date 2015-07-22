package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewReportDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewReportLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewReport;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("userReviewReportLogicInterface")
public class UserReviewReportLogicImpl extends BaseLogicImpl<UserReviewReport>
		implements
			UserReviewReportLogicInterface {
	private UserReviewReportDaoInterface userReviewReportDaoInterface;
    private UserReviewLogicInterface userReviewLogicInterface;

	/**
	 * @param userReviewReportDaoInterface the userReviewReportDaoInterface to set
	 */
    @Autowired
	public void setUserReviewReportDaoInterface(
			UserReviewReportDaoInterface userReviewReportDaoInterface) {
		this.userReviewReportDaoInterface = userReviewReportDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.userReviewReportDaoInterface;
	}
    @Autowired
    public void setUserReviewLogicInterface(UserReviewLogicInterface userReviewLogicInterface) {
        this.userReviewLogicInterface = userReviewLogicInterface;
    }

    public List<UserReviewReport> getAllUserReviewReports(UserReviewReport userReviewReport, PageBean pageBean) {
        return this.userReviewReportDaoInterface.getAllUserReviewReports(userReviewReport,pageBean);
    }

    public void updateUserReviewStatus(Long userReviewId, Long userReviewStatus) {
        userReviewLogicInterface.updateUserReviewById(userReviewId,userReviewStatus);
    }
}
