package com.fortune.rms.web.portal;

import com.fortune.common.Constants;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserScoringLogicInterface;
import com.fortune.rms.business.portal.model.RedexFavorite;
import com.fortune.rms.business.portal.model.UserContentStatus;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.util.JsonUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import java.util.List;

/**
 * Created by ����· on 2015/1/20.
 * Redexʹ�õ��û��ղ�Action
 */
@Namespace("/portal")
@ParentPackage("default")
@Action(value = "favorite")
public class RedexFavoriteAction  extends BaseAction<UserFavorites> {
    private UserFavoritesLogicInterface userFavoritesLogicInterface;
    private UserScoringLogicInterface userScoringLogicInterface;

    public void setUserScoringLogicInterface(UserScoringLogicInterface userScoringLogicInterface) {
        this.userScoringLogicInterface = userScoringLogicInterface;
    }

    private Long contentId;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public void setUserFavoritesLogicInterface(UserFavoritesLogicInterface userFavoritesLogicInterface) {
        this.userFavoritesLogicInterface = userFavoritesLogicInterface;
    }

    public RedexFavoriteAction() {
        super(UserFavorites.class);
    }

    /**
     * ��ȡ�û����ղ��б�
     */
    public void favoriteList() {
        if (obj.getUserId() == null && session.get(Constants.SESSION_FRONT_USER) != null) {
            FrontUser user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            obj.setUserId(user.getUserId());
        }
        List<RedexFavorite> redexFavoriteList = userFavoritesLogicInterface.redexGetUserFavoriteList(obj.getUserId(), pageBean);
        directOut(com.fortune.util.JsonUtils.getListJsonString("favorites", redexFavoriteList, "totalCount", pageBean.getRowCount()));
    }

    /**
     * ����ղ�
     */
    public void addFavorite() {
        RedexFavorite fav = null;
        if ( obj.getContentId() != null) {
            if(obj.getUserId() == null || obj.getUserId().isEmpty()){
                FrontUser user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
                obj.setUserId(user.getUserId());
            }
            fav = userFavoritesLogicInterface.redexDoFavorite(obj.getUserId(),
                    obj.getContentId(),
                    ServletActionContext.getRequest().getRemoteAddr());
        }
        if (fav == null)
            fav = new RedexFavorite();
        fav.setFavoriteId(-1L);
        directOut(com.fortune.util.JsonUtils.getJsonString(fav, ""));
     }

    /**
     * ����û���Ƶ״̬�������Ƿ��Ѿ��ղء��Ƿ��Ѿ�����ϲ��/��ϲ��
     */
    @Action(value = "contentStatus")
    public void getUserContentStatus(){
        UserContentStatus status = null;
        if (session.get(Constants.SESSION_FRONT_USER) != null) {
            FrontUser user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
            status = userFavoritesLogicInterface.getContentStatus(user.getUserId(), contentId);
        }

        if(status == null){
            status = new UserContentStatus();
        }

        directOut(com.fortune.util.JsonUtils.getJsonString(status, ""));
    }

    /**
     * ɾ���ղ�
     * @return SUCCESS
     */
    public String removeFav(){
        setSuccess(false);
        if(contentId > 0){
            if (session.get(Constants.SESSION_FRONT_USER) != null) {
                FrontUser user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
                setSuccess(userFavoritesLogicInterface.removeFavorite(user.getUserId(), contentId));
            }else{
                addActionError("��¼�ѹ��ڣ������µ�¼");
            }
        }else{
            addActionError("��Ч�Ĳ���");
        }

        if(obj == null){
            obj = new UserFavorites();
        }

        return SUCCESS;
    }

    /**
     * Ϊ�������֣� contentIdΪid scoreΪ����
     * @return success�� ֻ��success��Ч
     */
    public String doScore(){
        if(contentId > 0){
            if (session.get(Constants.SESSION_FRONT_USER) != null) {
                FrontUser user = (FrontUser) session.get(Constants.SESSION_FRONT_USER);
                setSuccess(userScoringLogicInterface.doScore(user.getUserId(), contentId, score,
                        ServletActionContext.getRequest().getRemoteAddr()) != null);
            }else{
                addActionError("��¼�ѹ��ڣ������µ�¼");
            }
        }else{
            addActionError("��Ч�Ĳ���");
        }

        return SUCCESS;
    }
}

