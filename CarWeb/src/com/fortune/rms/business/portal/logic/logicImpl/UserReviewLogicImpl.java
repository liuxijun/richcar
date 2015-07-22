package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.csp.dao.daoInterface.CspDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewDaoInterface;
import com.fortune.rms.business.portal.dao.daoInterface.UserReviewKeywordDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserReviewLogicInterface;
import com.fortune.rms.business.portal.model.UserReview;
import com.fortune.rms.business.portal.model.UserReviewKeyword;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("userReviewLogicInterface")
public class UserReviewLogicImpl extends BaseLogicImpl<UserReview>
        implements
        UserReviewLogicInterface {
    private UserReviewDaoInterface userReviewDaoInterface;
    private UserReviewKeywordDaoInterface userReviewKeywordDaoInterface;
    private CspDaoInterface cspDaoInterface;
    private ContentDaoInterface contentDaoInterface;

    /**
     * @param userReviewDaoInterface the userReviewDaoInterface to set
     */
    @Autowired
    public void setUserReviewDaoInterface(
            UserReviewDaoInterface userReviewDaoInterface) {
        this.userReviewDaoInterface = userReviewDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.userReviewDaoInterface;
    }
    @Autowired
    public void setUserReviewKeywordDaoInterface(UserReviewKeywordDaoInterface userReviewKeywordDaoInterface) {
        this.userReviewKeywordDaoInterface = userReviewKeywordDaoInterface;
    }
    @Autowired
    public void setCspDaoInterface(CspDaoInterface cspDaoInterface) {
        this.cspDaoInterface = cspDaoInterface;
    }
    @Autowired
    public void setContentDaoInterface(ContentDaoInterface contentDaoInterface) {
        this.contentDaoInterface = contentDaoInterface;
    }

    public List<UserReview> getAllUserReviews(UserReview userReview, PageBean pageBean) {
        List<Object[]> tempResult = userReviewDaoInterface.getAllUserReviews(userReview, pageBean);
        List<UserReview> result = new ArrayList<UserReview>();
        for (Object[] objs : tempResult) {
            UserReview ur = (UserReview) objs[0];
            Content c = (Content) objs[1];
            ur.setContentName(c.getName());
            result.add(ur);
        }
        return result;
    }


    public void updateUserReviewById(Long id, Long status) {
        userReviewDaoInterface.updateUserReviewById(id, status);
    }

    public SearchResult<UserReview> getAllUserReviewsByContentId(UserReview userReview, PageBean pageBean, long contentId) {
        return userReviewDaoInterface.getAllUserReviewsByContentId(userReview, pageBean, contentId);
    }

    public void saveUserReviewByKeyWord(UserReview userReview) {
        List<UserReviewKeyword> keywords = userReviewKeywordDaoInterface.getAllUserReviewwordsByCspId(userReview.getCspId());
        String content = userReview.getDesp();
        for (UserReviewKeyword urk : keywords) {
            String oldword = urk.getWord();
            if (content.indexOf(oldword) != -1) {
                content = content.replaceAll(oldword, "***");
            }
        }
        userReview.setDesp(content);
        userReviewDaoInterface.save(userReview);
    }

    public List<UserReview> searchUserReviewByContentIdAndCspId(long contentId, long cspId) {
        return userReviewDaoInterface.searchUserReviewByContentIdAndCspId(contentId, cspId);
    }

    public List<UserReview> searchUserReviewByContentIdAndCspId1(long contentId, long cspId) {
        return userReviewDaoInterface.searchUserReviewByContentIdAndCspId1(contentId, cspId);
    }

    public void changeUserReviewStatus(long userReviewId, long status,String desp,String time) {
        this.userReviewDaoInterface.changeUserReviewStatus(userReviewId, status,desp,time);
    }

    public UserReview searchLastUserReviewsByContentIdAndCspId(long contentId, long cspId) {
        return this.userReviewDaoInterface.searchLastUserReviewsByContentIdAndCspId(contentId,cspId);
    }

    public UserReview getUserReview(Long userReviewId) {
        UserReview ur = userReviewDaoInterface.get(userReviewId);
        if (ur != null) {
            String cspName = "";
            String contentName = "";
            if (ur.getCspId() != null) {
                Long cspId = ur.getCspId();
                Csp csp = cspDaoInterface.get(cspId);
                if (csp != null) {
                    cspName = csp.getName();
                }
            }
            if (ur.getContentId() != null) {
                Long contentId = ur.getContentId();
                Content content = contentDaoInterface.get(contentId);
                if (content != null) {
                    contentName = content.getName();
                }
            }
            ur.setCspName(cspName);
            ur.setContentName(contentName);
        }
        return ur;
    }
}
