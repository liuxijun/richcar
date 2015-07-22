package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.csp.dao.daoInterface.CspDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.portal.dao.daoInterface.UserScoringDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserScoringLogicInterface;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("userScoringLogicInterface")
public class UserScoringLogicImpl extends BaseLogicImpl<UserScoring>
        implements
        UserScoringLogicInterface {
    private UserScoringDaoInterface userScoringDaoInterface;
    private CspDaoInterface cspDaoInterface;
    private ContentDaoInterface contentDaoInterface;

    /**
     * @param userScoringDaoInterface the userScoringDaoInterface to set
     */
    @Autowired
    public void setUserScoringDaoInterface(
            UserScoringDaoInterface userScoringDaoInterface) {
        this.userScoringDaoInterface = userScoringDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.userScoringDaoInterface;
    }
    @Autowired
    public void setCspDaoInterface(CspDaoInterface cspDaoInterface) {
        this.cspDaoInterface = cspDaoInterface;
    }
    @Autowired
    public void setContentDaoInterface(ContentDaoInterface contentDaoInterface) {
        this.contentDaoInterface = contentDaoInterface;
    }

    public SearchResult<UserScoring> getAllUserScoringCount(UserScoring userScoring, PageBean pageBean) {
        return userScoringDaoInterface.getAllUserScoringCount(userScoring, pageBean);
    }

    public List<UserScoring> getUserScoringByContentIdAndCspId(UserScoring userScoring, PageBean pageBean) {
        List<Object[]> tempResult = userScoringDaoInterface.getUserScoringByContentIdAndCspId(userScoring, pageBean);
        List<UserScoring> result = new ArrayList<UserScoring>();
        for (Object[] objs : tempResult) {
            UserScoring us = (UserScoring) objs[0];
            Content c = (Content) objs[1];
            us.setContentName(c.getName());
            Csp csp = (Csp) objs[2];
            us.setCspName(csp.getName());
            result.add(us);
        }
        return result;
    }

    public Map<String, String> getUserScoringCountByContentIdAndCspId(long contentId, long cspId) {
        return this.userScoringDaoInterface.getUserScoringCountByContentIdAndCspId(contentId, cspId);
    }


    public UserScoring getUserScoring(Long id) {
        UserScoring us = userScoringDaoInterface.get(id);
        if (us != null) {
            String cspName = "";
            String contentName = "";
            if (us.getCspId() != null) {
                Long cspId = us.getCspId();
                Csp csp = cspDaoInterface.get(cspId);
                if (csp != null) {
                    cspName = csp.getName();
                }
            }
            if (us.getContentId() != null) {
                Long contentId = us.getContentId();
                Content content = contentDaoInterface.get(contentId);
                if (content != null) {
                    contentName = content.getName();
                }
            }
            us.setCspName(cspName);
            us.setContentName(contentName);
        }
        return us;
    }

    /**
     * 评分在范围内的次数
     * @param contentId 内容Id
     * @param min   最小分数（包含）
     * @param max   最大分数（包含）
     * @return 次数
     */
    public Long redexGetScoreRangeCount(long contentId, int min, int max){
        return userScoringDaoInterface.redexScoreRangeCount(contentId, min, max);
    }

    /**
     * 为内容评分
     * @param userId        用户Id
     * @param contentId     内容Id
     * @param score         分数
     * @param ip            操作地址
     * @return      评分对象
     */
    public UserScoring doScore(String userId, long contentId, int score,String ip){
        UserScoring userScoring = new UserScoring();
        userScoring.setContentId(contentId);
        userScoring.setUserId(userId);
        List<UserScoring> l = null;
        try {
            l = userScoringDaoInterface.getObjects(userScoring);
        }catch (Exception e){};
        if(l != null && l.size() > 0) return null;

        userScoring.setScore(score);
        userScoring.setTime(new Date());
        userScoring.setUserIp(ip);
        userScoring.setStatus(1L);
        return userScoringDaoInterface.save(userScoring);
    }
}
