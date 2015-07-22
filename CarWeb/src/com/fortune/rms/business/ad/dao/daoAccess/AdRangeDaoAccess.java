package com.fortune.rms.business.ad.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.ad.dao.daoInterface.AdRangeDaoInterface;
import com.fortune.rms.business.ad.logic.logicInterface.AdRangeLogicInterface;
import com.fortune.rms.business.ad.model.Ad;
import com.fortune.rms.business.ad.model.AdRange;
import com.fortune.rms.business.publish.model.Channel;
import org.springframework.stereotype.Repository;
import com.fortune.util.TreeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AdRangeDaoAccess extends BaseDaoAccess<AdRange, Long>
        implements
        AdRangeDaoInterface {

    public AdRangeDaoAccess() {
        super(AdRange.class);
    }

    @SuppressWarnings("unchecked")
    public List<AdRange> getAdRangeOfContent(Long contentId,Long channelId) {
        if (contentId == null || contentId <= 0) {
            return new ArrayList<AdRange>(0);
        }
        TreeUtils treeUtils = TreeUtils.getInstance();
        List<Channel> parents = treeUtils.getParents(Channel.class,channelId);
        String channelIds =""+channelId;
        for(Channel channel:parents){
            channelIds += ","+channel.getId();
        }
        String hql = "from AdRange  ar where ar.adId in (" +
                "select a.id from Ad a where a.startTime<? and a.endTime>?) and ((" +
                " ar.type=" + AdRangeLogicInterface.AD_RANGE_TYPE_CONTENT+
                " and ar.cid=" +contentId+
                ") or (" +
                " ar.type=" + AdRangeLogicInterface.AD_RANGE_TYPE_CHANNEL+
                " and ar.cid in (" +channelIds+
                ")))";
        return this.getHibernateTemplate().find(hql,new Object[]{new Date(),new Date()});
    }
}
