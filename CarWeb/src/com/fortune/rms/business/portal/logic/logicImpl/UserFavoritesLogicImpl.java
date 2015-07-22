package com.fortune.rms.business.portal.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentTidyDTO;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.portal.dao.daoInterface.UserFavoritesDaoInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserScoringLogicInterface;
import com.fortune.rms.business.portal.model.RedexFavorite;
import com.fortune.rms.business.portal.model.UserContentStatus;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.rms.business.portal.model.UserScoring;
import com.fortune.rms.business.user.logic.logicInterface.BookMarkLogicInterface;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("userFavoritesLogicInterface")
public class UserFavoritesLogicImpl extends BaseLogicImpl<UserFavorites>
		implements
			UserFavoritesLogicInterface {
	private UserFavoritesDaoInterface userFavoritesDaoInterface;
    private ContentLogicInterface contentLogicInterface;
    private UserScoringLogicInterface userScoringLogicInterface;
    private BookMarkLogicInterface bookMarkLogicInterface ;
    @Autowired
    public void setUserScoringLogicInterface(UserScoringLogicInterface userScoringLogicInterface) {
        this.userScoringLogicInterface = userScoringLogicInterface;
    }

    @Autowired
    public void setBookMarkLogicInterface(BookMarkLogicInterface bookMarkLogicInterface) {
        this.bookMarkLogicInterface = bookMarkLogicInterface;
    }

    /**
	 * @param userFavoritesDaoInterface the userFavoritesDaoInterface to set
	 */
    @Autowired
	public void setUserFavoritesDaoInterface(
			UserFavoritesDaoInterface userFavoritesDaoInterface) {
		this.userFavoritesDaoInterface = userFavoritesDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.userFavoritesDaoInterface;
	}

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface){
        this.contentLogicInterface = contentLogicInterface;
    }
    public SearchResult<UserFavorites> getUserFavoritesCount(UserFavorites userFavorites, PageBean pageBean){
        return userFavoritesDaoInterface.getUserFavoritesCount(userFavorites,pageBean);
    }

      public List<UserFavorites> getAllUserFavorites(UserFavorites userFavorites, PageBean pageBean){
          List<Object[]> tempResult= userFavoritesDaoInterface.getAllUserFavorites(userFavorites,pageBean);
          List<UserFavorites> result=new ArrayList<UserFavorites>();
           for(Object[] objs :tempResult){
            UserFavorites uf = (UserFavorites)objs[0];
            Content c = (Content) objs[1];
            uf.setContentName(c.getName());
            Csp csp=(Csp)objs[2];
            uf.setCspName(csp.getName());
            result.add(uf);
        }
        return result;
      }

    public List<Content> getContentsOfUser(String userTel,PageBean pageBean){
        UserFavorites bean = new UserFavorites();
        bean.setUserId(userTel);
        List<UserFavorites> favorites = search(bean,pageBean);
        List<Content> contents = new ArrayList<Content>();
        for(UserFavorites favorite:favorites){
            Long contentId = favorite.getContentId();
            Content content = contentLogicInterface.getCachedContent(contentId);
            if(content!=null){
                contents.add(content);
            }
        }
        return contents;
    }

    public UserFavorites getFavorite(String userId,Long contentId){
        UserFavorites bean = new UserFavorites();
        bean.setUserId(userId);
        bean.setContentId(contentId);
        List<UserFavorites> favorites = search(bean);
        if(favorites!=null&&favorites.size()>0){
            return favorites.get(0);
        }
        return null;
    }
    public boolean hasFavoriteIt(String userId, Long contentId) {
        return getFavorite(userId,contentId)!=null;
    }

    public boolean favoriteIt(String userId, Long contentId,String ip) {
        if(userId==null||contentId==null||"".equals(userId.trim())||contentId<=0){
            return false;
        }
        if(hasFavoriteIt(userId,contentId)){
            return false;
        }
        UserFavorites bean = new UserFavorites();
        bean.setContentId(contentId);
        bean.setUserId(userId);
        bean.setTime(new Date());
        bean.setUserIp(ip);
        save(bean);
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    //ȡ���ղ�
    public boolean removeFavorite(String userId, Long contentId){
        boolean flog;
        UserFavorites bean = getFavorite(userId,contentId);
        if(bean!=null){
            remove(bean);
            flog=true;
        }else{
            logger.warn("û���ҵ���("+userId+")��Ӧ���ղأ�"+contentId+"��");
            flog=false;
        }
        return flog;
    }

    /**
     * ��ѯ�û��ղ��б�����RedexFavorite�����б�
     * @param userId �û�Id
     * @param pageBean ��ҳ��Ϣ
     * @return RedexFavorite�б�
     */
    public List<RedexFavorite> redexGetUserFavoriteList(String userId, PageBean pageBean){
        List<RedexFavorite>  userFavList = new ArrayList<RedexFavorite>();

        if(userId == null || userId.isEmpty()) return userFavList;

        List<UserFavorites> favList = userFavoritesDaoInterface.redexGetUserFavoriteList(userId, pageBean);
        if(favList == null) return userFavList;

        for(UserFavorites fav : favList){
            RedexFavorite redexFav = new RedexFavorite(fav);
            redexFav.setContentDTO(new ContentTidyDTO(contentLogicInterface.get(fav.getContentId())));
            userFavList.add(redexFav);
        }

        return  userFavList;
    }

    /**
     * redex�ղ�
     * @param userId        �û�id
     * @param contentId     ����Id
     * @param ip             ip��ַ
     * @return  RedexFavorite����
     */
    public RedexFavorite redexDoFavorite(String userId, Long contentId, String ip){
        if(hasFavoriteIt(userId,contentId)){
            return null;
        }

        UserFavorites fav = new UserFavorites();
        fav.setUserId(userId);
        fav.setContentId(contentId);
        fav.setUserIp(ip);
        fav.setTime(new Date());
        fav = userFavoritesDaoInterface.save(fav);
        RedexFavorite redexFavorite = new RedexFavorite(fav);
        redexFavorite.setContentDTO(new ContentTidyDTO(contentLogicInterface.get(contentId)));
        return redexFavorite;
    }

    /**
     * ��ȡ�û����ݵ��ղغ�����״̬
     * @param userId        �û�Id
     * @param contentId     ����Id
     * @return UserContentStatus����
     */
    public UserContentStatus getContentStatus(String userId, Long contentId){
        if(userId == null || userId.isEmpty() || contentId == null || contentId <= 0) return null;

        UserContentStatus status = new UserContentStatus();
        status.setUserId(userId);
        status.setContentId(contentId);
        // �Ƿ��ղ�
        UserFavorites fav = getFavorite(userId,contentId);
        status.setFaved(fav != null);
        status.setFavTime(fav == null ? new Date() : fav.getTime());
        // �Ƿ�����
        UserScoring scoring = new UserScoring();
        scoring.setUserId(userId);
        scoring.setContentId(contentId);
        List<UserScoring> l = userScoringLogicInterface.search(scoring);
        if(l != null && l.size() > 0){
            status.setScore(l.get(0).getScore());
            status.setScoreTime(l.get(0).getTime());
        }
        status.setBookMark(bookMarkLogicInterface.getBookMarkOfUser(userId,contentId));
        return status;
    }

    /**
     * ��ѯ���ݱ��ղص��ܴ���
     * @param contentId ����Id
     * @return �ղ��ܴ���
     */
    public Long redexGetContentFavoriteCount(Long contentId){
        return userFavoritesDaoInterface.redexGetContentFavoriteCount(contentId);
    }
}
