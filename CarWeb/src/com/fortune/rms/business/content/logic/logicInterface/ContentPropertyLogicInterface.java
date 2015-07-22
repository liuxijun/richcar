package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.content.model.EpisodeDTO;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;

import java.util.List;

public interface ContentPropertyLogicInterface
		extends
			BaseLogicInterface<ContentProperty> {
    public static long EXTRA_INT_NOT_ENCODED=2;
    public static long EXTRA_INT_HAS_ENCODED=3;
    public static long EXTRA_INT_SUCCESS=4;
    public static Long EXTRA_INT_ENCODE_FINISHED=33854718L;

    public static long EXTRA_INT_ERROR_FILE_NOT_EXISTS=404;
    public static final Byte IMAGE_TYPE=11;    //海报的类型

    public void removeByContentId(long contentId);
    public ContentProperty getContentProperties(long Id);
    public List<ContentProperty> getContentProperties(long contentId, long propertyId);
    public List<ContentProperty> getContentProperties(long contentId, long propertyId, long intValue);
    public List<ContentProperty> getContentProperties(long contentId, Long[] propertyIds);
    public List<ContentProperty> getContentPropertiesByCache(long contentId, long intValue);
    public List<ContentProperty> getContentPropertiesByCache(ContentProperty contentProperty);
    public ContentProperty getContentProperty(long contentId, long propertyId);
    public int getContentFreeCountByCache(final long contentId, final long propertyId);
    public long getContentIntValueByCache(final long contentPropertyId);
    public boolean existsSubContentId(String subContentId);
    public ContentProperty getContentPropertyBySubContentId(String subContentId);
    public void updateContentPropertyBySubContentId(ContentProperty contentProperty);
    public void removeContentPropertyBySubContentId(ContentProperty contentProperty);
    public List<ContentProperty> setContentFromContentProperties(Content content, List<ContentProperty> contentProperties, List<ContentProperty> willRemoveProperties);
    public List<ContentProperty> setContentFromContentProperties(Content content, List<ContentProperty> contentProperties);
    public Content saveClips(List<ContentProperty> clips, Content content, long encoderId);
    public List<ContentProperty> getContentPropertiesByDataType(long moduleId, long contentId, Byte dataType, boolean fillEmptyProperty);
    public List<ContentProperty> getContentPropertiesByDataType(long moduleId, long contentId, Byte dataType, boolean fillEmptyProperty, boolean initSelectOptions);
    public int updateThumbPic(ContentProperty clip);
    public int updateStringValue(String contentId, String propertyId, String stringValue);
    public  List<ContentProperty> fillEmptyPoster();
    //根据海报处理确实海报
    public List<ContentProperty> fillEmptyPoster(String handleImageFile, long moduleId, List<ContentProperty> contentProperties, long adminId);
    // added by mlwang @2014-12-23,获取选集信息
    public List<EpisodeDTO> getContentEpisodes(long contentId);

    public ContentProperty getContentDownLoadUrl(long contentId, long intValue, long propertyId);
    public long guessContentPropertyId(final long contentId, final int willClipId, final String propertyCode,
                                       final ContentPropertyLogicInterface contentPropertyLogicInterface,
                                       final PropertyLogicInterface propertyLogicInterface);
    public Long getClipId(ContentProperty sourceClip, String propertyCode, PropertyLogicInterface propertyLogicInterface,
                          ContentPropertyLogicInterface contentPropertyLogicInterface);
}
