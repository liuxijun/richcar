package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.publish.model.Recommend;
import net.sf.ehcache.Cache;

import java.util.List;
import java.util.Map;

public interface ContentRecommendLogicInterface
		extends
			BaseLogicInterface<ContentRecommend> {
    public List<Content> getContents(String recommendCode);
    /*added by mlwang @2014-11-11，增加栏目过滤和用户类型过滤*/
    public List<Content> getContents(String recommendCode, List<Long> channelIdList, FrontUser user);
    /*end*/
    public Map<String,Object> getRecommendMovies(String id);
    public Recommend getRecommendByCode(String recommendCode);
    public Map<String,Object> getFromCache(String id);
    public void putToCache(String id, Map<String, Object> result);
    public Cache getCache();
    public void clearAllCache();
    public String getRecommendCodeById(long recommendId);
    public List<Map<String,Object>>getContentRecommendListByRecommendCode(String recommendCodes);
    public List<ContentRecommend> getContentRecommendByDisplayOrder(long displayOrder, long recommendId);
    public List<ContentRecommend> saveContentRecommendOfChannel(List<Content> contents, long channelId);
    public List<ContentRecommend> saveContentRecommend(List<Content> contents, String recommendCode);
    public List<ContentRecommend> saveContentRecommend(List<Content> contents, Recommend recommend);
    public List<ContentRecommend> getContentIdsOfRecommend(Long recommendId);
}
