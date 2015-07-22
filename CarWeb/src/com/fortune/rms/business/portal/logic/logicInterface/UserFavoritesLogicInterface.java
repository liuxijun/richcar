package com.fortune.rms.business.portal.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.portal.model.RedexFavorite;
import com.fortune.rms.business.portal.model.UserContentStatus;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserFavoritesLogicInterface
		extends
			BaseLogicInterface<UserFavorites> {

    public SearchResult<UserFavorites> getUserFavoritesCount(UserFavorites userFavorites, PageBean pageBean);
    public List<UserFavorites> getAllUserFavorites(UserFavorites userFavorites, PageBean pageBean);
    public List<Content> getContentsOfUser(String userTel, PageBean pageBean);
    public boolean hasFavoriteIt(String userId, Long contentId);
    public boolean favoriteIt(String userId, Long contentId, String ip);
    public boolean removeFavorite(String userId, Long contentId);
    public List<RedexFavorite> redexGetUserFavoriteList(String userId, PageBean pageBean);
    public RedexFavorite redexDoFavorite(String userId, Long contentId, String ip);
    public UserContentStatus getContentStatus(String userId, Long contentId);
    public Long redexGetContentFavoriteCount(Long contentId);
}
