package com.fortune.rms.business.portal.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.portal.model.UserFavorites;
import com.fortune.util.PageBean;
import com.fortune.util.SearchResult;

import java.util.List;

public interface UserFavoritesDaoInterface
		extends
			BaseDaoInterface<UserFavorites, Long> {
      public SearchResult<UserFavorites> getUserFavoritesCount(UserFavorites userFavorites, PageBean pageBean);
      public List<Object[]> getAllUserFavorites(UserFavorites userFavorites, PageBean pageBean);
    public List<UserFavorites> redexGetUserFavoriteList(String userId, PageBean pageBean);
    public Long redexGetContentFavoriteCount(Long contentId);
}