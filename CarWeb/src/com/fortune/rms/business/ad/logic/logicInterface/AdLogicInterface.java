package com.fortune.rms.business.ad.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.ad.model.Ad;

public interface AdLogicInterface extends BaseLogicInterface<Ad> {
    public static final Long AD_TYPE_VIDEO=1L;
    public static final Long AD_TYPE_IMAGE=2L;
}
