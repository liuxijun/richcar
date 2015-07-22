package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.portal.dao.daoInterface.UserRecommandDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserRecommandLogicInterface;
import com.fortune.rms.business.portal.model.UserRecommand;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("userRecommandLogicInterface")
public class UserRecommandLogicImpl extends BaseLogicImpl<UserRecommand>
        implements
        UserRecommandLogicInterface {
    private UserRecommandDaoInterface userRecommandDaoInterface;

    /**
     * @param userRecommandDaoInterface the userRecommandDaoInterface to set
     */
    @Autowired
    public void setUserRecommandDaoInterface(
            UserRecommandDaoInterface userRecommandDaoInterface) {
        this.userRecommandDaoInterface = userRecommandDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.userRecommandDaoInterface;
    }

    public List<UserRecommand> getAllUserRecommand(UserRecommand userRecommand, PageBean pageBean) {
        List<Object[]> resultContentId=userRecommandDaoInterface.getAllUserRecommand(userRecommand, pageBean);
        List<UserRecommand> result = new ArrayList<UserRecommand>();
        for(Object[] objs :resultContentId){
            UserRecommand ur = (UserRecommand)objs[0];
            Content c = (Content) objs[1];
            ur.setContentName(c.getName());
            Csp csp=(Csp)objs[2];
            ur.setCspName(csp.getName());
            result.add(ur);
        }
        return result;
    }

    public SearchResult<UserRecommand> getUserRecommandCount(UserRecommand userRecommand, PageBean pageBean) {
        return this.userRecommandDaoInterface.getUserRecommandCount(userRecommand, pageBean);
    }

}
