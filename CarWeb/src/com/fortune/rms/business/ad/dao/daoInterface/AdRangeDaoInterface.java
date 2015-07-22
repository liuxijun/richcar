package com.fortune.rms.business.ad.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.ad.model.AdRange;

import java.util.List;

public interface AdRangeDaoInterface extends BaseDaoInterface<AdRange, Long> {
    public List<AdRange> getAdRangeOfContent(Long contentId, Long channelId);
}