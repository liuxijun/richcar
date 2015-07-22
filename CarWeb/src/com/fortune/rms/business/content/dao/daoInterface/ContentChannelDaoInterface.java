package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;

import java.util.List;

public interface ContentChannelDaoInterface
		extends
			BaseDaoInterface<ContentChannel, Long> {
    public List<ContentChannel> list(Long cspId, Long channelId, String contentName, String directors,
                                     String actors, List<ContentProperty> searchValues, PageBean pageBean);

    public List<Channel> getChannelsByContentId(Long contentId);
}