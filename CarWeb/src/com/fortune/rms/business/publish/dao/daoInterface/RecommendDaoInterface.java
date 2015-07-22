package com.fortune.rms.business.publish.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.PageBean;

import java.util.List;

public interface RecommendDaoInterface
		extends
			BaseDaoInterface<Recommend, Long> {
       public List<Recommend> getRecommendsByCspId(long cspId);
       public long getCspIdByCode(String code);

    public List<Recommend> getRecommend(Long cspId, Long type, String name, String code, PageBean pageBean);

    public Recommend getRecommendByChannelId(Long channelId);
    // added by mlwang @2014-12-12
    public List<Recommend> getChannelRecommendByChannelList(List<Long> channelIdList);
}