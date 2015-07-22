package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.ContentServiceProduct;

import java.util.List;

public interface ContentServiceProductLogicInterface
		extends
			BaseLogicInterface<ContentServiceProduct> {

    public List<ContentServiceProduct> getContentServiceProductsByContentIdAndCspId(Long contentId, Long cspId);
}
