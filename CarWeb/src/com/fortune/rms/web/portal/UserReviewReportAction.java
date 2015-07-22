package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewReportLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewReport;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.SearchResult;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
@Namespace("/portal")
@ParentPackage("default")
@Action(value="userReviewReport")
public class UserReviewReportAction extends BaseAction<UserReviewReport> {
	private static final long serialVersionUID = 3243534534534534l;
	private UserReviewReportLogicInterface userReviewReportLogicInterface;
    private UserReviewReport userReviewReport;
	@SuppressWarnings("unchecked")
	public UserReviewReportAction() {
		super(UserReviewReport.class);
	}
	/**
	 * @param userReviewReportLogicInterface the userReviewReportLogicInterface to set
	 */
    @Autowired
	public void setUserReviewReportLogicInterface(
			UserReviewReportLogicInterface userReviewReportLogicInterface) {
		this.userReviewReportLogicInterface = userReviewReportLogicInterface;
		setBaseLogicInterface(userReviewReportLogicInterface);
	}

    public String list(){
        objs = this.userReviewReportLogicInterface.getAllUserReviewReports(userReviewReport,pageBean);
        return Constants.ACTION_LIST;
    }

    public String save(){
        userReviewReportLogicInterface.updateUserReviewStatus(obj.getUserReviewId(),obj.getStatus());
        writeSysLog("影评报告保存： "+obj.getId()+",userReviewId="+obj.getUserReviewId()+",状态："+obj.getStatus());
        return Constants.ACTION_SAVE;
    }

    public UserReviewReport getUserReviewReport() {
        return userReviewReport;
    }

    public void setUserReviewReport(UserReviewReport userReviewReport) {
        this.userReviewReport = userReviewReport;
    }
}
