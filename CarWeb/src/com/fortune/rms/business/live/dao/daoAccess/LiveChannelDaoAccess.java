package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.LiveChannelDaoInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.LiveChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 王明路 on 2015/2/26.
 */
@Repository
public class LiveChannelDaoAccess  extends BaseDaoAccess<LiveChannel, Long>
        implements
        LiveChannelDaoInterface {
    public LiveChannelDaoAccess() {
        super(LiveChannel.class);
    }

    /**
     * 根据直播删除直播栏目设置
     * @param live 直播
     */
    public void removeByLive(Live live){
        if( live == null ) return;

        String hql = "delete from LiveChannel lc where lc.liveId=" + live.getId();
        executeUpdate(hql);
    }

    /**
     * 根据直播获取栏目信息
     * @param live 直播对象，只使用其中的Id
     * @return 直播栏目列表
     */
    public List<LiveChannel> getListByLive(Live live){
        if(live == null) return null;
        String hql = "from LiveChannel lc where lc.liveId=" + live.getId();
        return this.getHibernateTemplate().find(hql);
    }
}
