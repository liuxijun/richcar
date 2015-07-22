package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.ContentServiceProduct;

import java.util.List;

public interface ContentServiceProductDaoInterface
		extends
			BaseDaoInterface<ContentServiceProduct, Long> {
    public List<ContentServiceProduct> getContentServiceProductsByContentIdAndCspId(Long contentId, Long cspId);

}