package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRecommend;
import com.fortune.util.PageBean;

import java.util.List;

public interface ContentRecommendDaoInterface
		extends
			BaseDaoInterface<ContentRecommend, Long> {
    public List<ContentRecommend> getContentIds(String recommendCode, PageBean pageBean);
    /*added by mlwang, @2014-11-11*/
    public List<ContentRecommend> getContentIds(String recommendCode, PageBean pageBean, List<Long> channelIdList, long userType);
    // end of added
    public String getRecommendCodeById(long recommendId);
    public List<ContentRecommend> getContentRecommendByDisplayOrder(long displayOrder, long recommendId);
}