package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewKeywordDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewKeywordLogicInterface;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("userReviewKeywordLogicInterface")
public class UserReviewKeywordLogicImpl extends BaseLogicImpl<UserReviewKeyword>
		implements
			UserReviewKeywordLogicInterface {
	private UserReviewKeywordDaoInterface userReviewKeywordDaoInterface;

	/**
	 * @param userReviewKeywordDaoInterface the userReviewKeywordDaoInterface to set
	 */
    @Autowired
	public void setUserReviewKeywordDaoInterface(
			UserReviewKeywordDaoInterface userReviewKeywordDaoInterface) {
		this.userReviewKeywordDaoInterface = userReviewKeywordDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.userReviewKeywordDaoInterface;
	}

    public boolean isExistUserReviewKeyword(String word) {
        return this.userReviewKeywordDaoInterface.isExistUserReviewKeyword(word);
    }

    public List<UserReviewKeyword> getAllUserReviewKeywords(UserReviewKeyword userReviewKeyword, PageBean pageBean) {
        List<Object[]> tempResult = userReviewKeywordDaoInterface.getAllUserReviewwords(userReviewKeyword,pageBean);
        List<UserReviewKeyword> result = new ArrayList<UserReviewKeyword>();
        for(Object[] objs :tempResult ){
            UserReviewKeyword  urk = (UserReviewKeyword) objs[0];
            Csp csp = (Csp)objs[1];
            urk.setCspName(csp.getName());
            result.add(urk);
        }
        return result;

    }

    public List<UserReviewKeyword> getAllUserReviewKeywordsByCspId(long cspId) {
        return this.userReviewKeywordDaoInterface.getAllUserReviewwordsByCspId(cspId);
    }
}
