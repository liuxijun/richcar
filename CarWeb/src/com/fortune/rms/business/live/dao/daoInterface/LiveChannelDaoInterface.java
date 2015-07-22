package com.fortune.rms.business.live.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.LiveChannel;

import java.util.List;

/**
 * Created by ����· on 2015/2/26.
 * ֱ��Ƶ��Dao
 */
public interface LiveChannelDaoInterface   extends BaseDaoInterface<LiveChannel, Long> {
    public void removeByLive(Live live);
    public List<LiveChannel> getListByLive(Live live);
}
