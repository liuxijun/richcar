package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentRelated;

import java.util.List;

public interface ContentRelatedLogicInterface
		extends
			BaseLogicInterface<ContentRelated> {
    public List<Content> getRelatedContents(Long contentId);
}
