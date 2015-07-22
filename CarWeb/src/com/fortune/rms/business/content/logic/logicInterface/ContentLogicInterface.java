package com.fortune.rms.business.content.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.model.*;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.PageBean;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ContentLogicInterface extends BaseLogicInterface<Content> {
//    public static final Long STATUS_SP_PUBLISHED =2L;
//    public static final Long STATUS_SP_OFFLINE = 1L;
    public static final Long STATUS_CP_OFFLINE = 1L;
    public static final Long STATUS_CP_ONLINE = 2L;
    public static final Long STATUS_WAITING=100L;
    public static final Long STATUS_WAIT_TO_ONLINE=3L;
    public static final Long STATUS_WAIT_TO_OFFLINE=6L;
    public static final Long STATUS_RECYCLE=8L;
    public static final Long STATUS_DELETE=9L;
    public static final Long STATUS_LOST_MEDIA_SOURCE=10L;
    public static final Long IS_SPECIAL=1L;
    public static final Long STATUS_WAITING_FOR_ENCODE=200L;
    public static final Long STATUS_ENCODING=201L;
    public static final Long STATUS_ENCODE_ERROR=202L;
    public static final Long STATUS_WAITING_FOR_AUDIT=300L;
    public static final Long STATUS_AUDIT_REJECTED=301L;    // added by mlwang, @2014-10-23，审核未通过

    public Content initContent(Long contentId);
    public String getStatusString(Long status);
    public Content getCachedContent(Long contentId);
    public Content getCachedContent(Content content);
    public int getBookMarkCountOfUser(String userTel);
    public long getContentIdBySubContentId(String subContentId);
    public long getContentIdByContentChannelId(long contentChannelId);
    public List<Content> getRelatedContents(Long contentId, Long cspId, Long relateId);
    public List<Content> getSameViewContents(Long contentId, Long relateId);
    public Device getDevice(Long deviceId);
    public boolean downloadFile(Device device, String fileName, String desertFile);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean);
    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean);

    public List<Content> list(Long contentCspStatus, String contentName, String directors, String actors, List<ContentProperty> searchValues, PageBean pageBean);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean);
    public List<Content> list(Long contentCspStatus, String searchValue, PageBean pageBean);
    /*added by mlwang @2014-11-11,为list方法增加前台用户可观看的频道和用户类型过滤条件*/
    public List<Content> list(Long contentCspStatus, String searchValue, PageBean pageBean, List<Long> channelIdList, FrontUser user);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, List<Long> channelIdList, FrontUser user);
    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, FrontUser user);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, FrontUser user);
    public List<Content> top10(List<Long> channelIdList, FrontUser user);
    /*就到这里了*/
    public Channel getContentBindChannel(Long channelId, Long contentId);
    public Property getPropertyByCode(String code);
    public String getPropertySelectValue(final Long propertyId, final String value);
    public Session getSession();
    public String getContentChannel(Long contentId);
    public String getContentRecommend(Long contentId);
    public String getContentServiceProduct(Long contentId);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean);
    public boolean existContentId(String contentId);
    public List<Content> getContent(String name, String contentId, Long cspId);
    public boolean cpOfflineContent(Content content);
    public void updateContentByContentId(Content content);
    public void removeContentByContentId(Content content);
    public List<Map> searchContentParameter(String name, long channelId, Long cspId, long contentCspStatus, PageBean pageBean);
    public Map<String,Object> scanXml(String fileName, Long moduleId, List<String> response, String xmlFileEncoding);
    public void saveToXml(String fileName, List<Content> contents, Long moduleId);
    public List<String> importXmlV2(String xmlFileName, Long cspId, Integer adminId, Long deviceId,
                                    Long moduleId, String xmlFileEncoding, String webAppPath, String cspFilePath, String file,
                                    boolean checkOnly);
    public List<String> importXml(String xmlFileName, Long cspId, Integer adminId,
                                  Long deviceId, Long moduleId, String xmlFileEncoding, String webAppPath, String cspFilePath, String file, boolean checkOnly);
    public Content saveContent(Content content);
    public List<Content> getContentsOfVipChannels(long channelId);
    public List<Content> getContentsOfExpired(Long cspId);
    public List<Content> getContentsByCspId(Long cspId);

    //仅用于沃爱上英超的媒体列表收集
    public List<Content> getContentsByCspId(Long cspId, PageBean pageBean);
    //仅用于获取沃爱上英超媒体总数
    public long getCountByCspId(Long cspId);

    public List<Content> getContentsBySpecial(PageBean pageBean);
    List<Content> getContentsOfChannelAndCp(long channelId, Long cspId, String name, String actors, String directors, Long status, int publishStatus, PageBean pageBean);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, Long type);
    public List<Content> getContentByName(String homeClubName, String guestClubName, long channelId, PageBean pageBean, long cspId);
    public List<Channel> checkPublishChannels(Content content, List<ContentProperty> contentProperties);
    public List<Channel> checkPublishChannels(Content content, String channelIds);
    public List<Channel> checkPublishChannels(Content content, String channelIds, boolean willCheckAuditFlag);
    public List<Content> getContents(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, String code, String code_1);

    void onEncodeTaskFinished(Content content, int allTaskCount, int unFinishedCount);
    public String setContentStatus(Content content, long status);

    // add by mlwang @2014-10-16，获得待审内容
    public List<RedexAuditContent> getUnAudtiContent(Long channelId, String searchValue, PageBean pageBean);
    public List<Content> getContentList(Long channelId, String searchValue, Date startTime, Date stopTime, PageBean pageBean);
    public List<Content> getContentList(Long channelId, String searchValue, Date startTime, Date stopTime, PageBean pageBean, Admin admin);
    public List<ContentAbnormal> getAbnormalContentList(Integer id);
    public int incAllVisitCount(long contentId);
    public List<ContentDTO> getNewest(List<Long> channelIdList, Long userType, int count);
    public List<ContentTidyDTO> getRedexContents(Long channelId, String searchWord, List<Long> channelIdList, Long userType, PageBean pageBean);
    public List<ContentTidyDTO> getSimilarContents(Content content, int priority, List<Long> channelIdList, Long userType);
    public ContentDetailDTO getContentDetail(Long contentId, Long channelId);
    public List<Content> getPicContent(long channelId, long modelId);
    // added by mlwang @2015-3-16，获取直播列表
    public List<ContentTidyDTO> getLivingList(Long channelId, String searchWord, List<Long> channelIdList, Long userType, PageBean pageBean);
    // -----------------------------------------------------

    public List<Content> getContentsByChannelId(long channelId, PageBean pageBean);
    public List<Content> getContentsByStatus(long status, String searchValue, Date startTime, Date stopTime, PageBean pageBean);
    public Map<String,List<Content>> getContentsOfChannelIds(String ids, PageBean pageBean);
}


