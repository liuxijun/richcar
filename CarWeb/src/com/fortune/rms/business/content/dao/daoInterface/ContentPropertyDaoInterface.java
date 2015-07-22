package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.ContentProperty;

import java.util.List;

public interface ContentPropertyDaoInterface
		extends
			BaseDaoInterface<ContentProperty, Long> {
    public boolean existsSubContentId(String subContentId);
    public ContentProperty getContentPropertyBySubContentId(String subContentId);
    public void updateContentPropertyBySubContentId(ContentProperty contentProperty);
    public void removeContentPropertyBySubContentId(ContentProperty contentProperty);
    public List<ContentProperty> getContentPropertiesByContentIdAndPropertyIds(long contentId, Long[] propertyIds);
    public List<ContentProperty> getContentProperties(long contentId, long propertyId, long intValue);
    public int updateThumbPic(ContentProperty clip);
    public int updateStringValue(String contentId, String propertyId, String stringValue);
    public ContentProperty getContentDownLoadUrl(long contentId, long intValue, long propertyId);
}