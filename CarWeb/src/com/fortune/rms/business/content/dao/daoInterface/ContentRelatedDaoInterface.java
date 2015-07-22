package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRelated;

import java.util.List;

public interface ContentRelatedDaoInterface
		extends
			BaseDaoInterface<ContentRelated, Long> {
    public List<ContentRelated> getRelatedContents(Long contentId);
}