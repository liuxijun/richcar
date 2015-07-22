package com.fortune.rms.business.live.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.live.dao.daoInterface.LiveChannelDaoInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.LiveChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ����· on 2015/2/26.
 */
@Repository
public class LiveChannelDaoAccess  extends BaseDaoAccess<LiveChannel, Long>
        implements
        LiveChannelDaoInterface {
    public LiveChannelDaoAccess() {
        super(LiveChannel.class);
    }

    /**
     * ����ֱ��ɾ��ֱ����Ŀ����
     * @param live ֱ��
     */
    public void removeByLive(Live live){
        if( live == null ) return;

        String hql = "delete from LiveChannel lc where lc.liveId=" + live.getId();
        executeUpdate(hql);
    }

    /**
     * ����ֱ����ȡ��Ŀ��Ϣ
     * @param live ֱ������ֻʹ�����е�Id
     * @return ֱ����Ŀ�б�
     */
    public List<LiveChannel> getListByLive(Live live){
        if(live == null) return null;
        String hql = "from LiveChannel lc where lc.liveId=" + live.getId();
        return this.getHibernateTemplate().find(hql);
    }
}
