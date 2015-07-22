package com.fortune.rms.business.ad.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.ad.model.AdRange;

import java.util.List;

public interface AdRangeLogicInterface extends BaseLogicInterface<AdRange> {
    public static final Long AD_RANGE_TYPE_CHANNEL=1L;
    public static final Long AD_RANGE_TYPE_CONTENT=2L;
    public static final Long AD_RANGE_POS_BEFORE=1L;
    public static final Long AD_RANGE_POS_AFTER=2L;
    public List<AdRange> getAdRangeOfContent(Long contentId, Long channelId);
}
