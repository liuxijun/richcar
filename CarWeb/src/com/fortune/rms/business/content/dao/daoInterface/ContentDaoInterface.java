package com.fortune.rms.business.content.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;

public interface ContentDaoInterface extends BaseDaoInterface<Content, Long> {
    public List<Content> recommendList(Long channelId);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean);
    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId);
    public List<Content> list(Long contentCspStatus, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean);
    public List<Content> list(Long contentCspStatus, String searchValue, PageBean pageBean);
    /*新增用户可访问栏目列表和用户类型过滤条件*/
    public List<Content> list(Long contentCspStatus, String searchValue, PageBean pageBean, List<Long> channelIdList, long userType);
    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId,
                              List<Long> channelIdList, long userType);
    public List<Content> list(Long cspId, Long contentCspStatus, String channelIds, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, String systemChannelId,
                              List<Long> channelIdList, long userType, String excludeIds);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, List<Long> channelIdList, long userType);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, long userType);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String contentName, String directors,
                              String actors,
                              List<ContentProperty> searchValues, PageBean pageBean, List<Long> channelIdList, long userType, String excludeIds);
    public List<Content> top10(List<Long> channelIdList, long userType);
    /* 好了，休息休息一下 */
    public Long getContentIdBySubContentId(String subContentId, Long propertyId);
    public Long getContentIdByContentChannelId(Long contentChannelId);
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean);
    public List<Object[]> list2(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean);
    public boolean existContentId(String contentId, Long cspId);
    public List<Content> getContent(String contentId, Long cspId);
    public List<Content> getContent(String name, String contentId, Long cspId);
    public void updateContentByContentId(Content content);
    public String getContentChannel(Long contentId);
    public String getContentRecommend(Long contentId);
    public String getContentServiceProduct(Long contentId);
    public void removeContentByContentId(Content content);
    public int setContentStatus(Long id, Long status);
    public List searchContentParameter(String name, long channelId, Long cspId, long contentCspStatus, PageBean pageBean);
    public List getContentsOfVipChannels(long channelId);
    public List<Content> getContentsOfExpired(Long cspId);
    public List<Content> getContentsByCspId(Long cspId);
    public List<Content> getContentsByCspId(Long cspId, PageBean pageBean);
    public long getCountByCspId(Long cspId);
    public List<Content> getContentsBySpecial(PageBean pageBean);
    public List<Content> getContentsOfChannelAndCp(long channelId, Long cspId, String name, String actors,
                                                   String directors, Long status, int publishStatus, PageBean pageBean);

    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, Long type);
    public List<Content> getContentByName(String homeClubName, String guestClubName, long channelId, PageBean pageBean, long cspId);
    public List<Content> getContents(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, String code, String code_1);
    // add by mlwang @2014-10-16
    public List<Content> getUnAuditContents(Long channelId, String searchValue, PageBean pageBean);
    // add by mlwang @2014-10-23 查询内容
    public List<Content> getContents(Long channelId, String searchValue, Date startTime, Date stopTime, PageBean pageBean, Integer adminId);
    public List<Object[]> getTransAbnormalList(Integer adminId);
    public List<Content> getRejectContents(Integer adminId);
    public void addUserType(long userTypeId, String channels);
    public List<Content> getRedexNewest(String channelIds, Long userType, int count);
    public List<Object[]> getRedexContents(Long channelId, String searchWord, String channelIdString, Long userType, PageBean pageBean);
    // added by mlwang @2015-3-16，检索内容，新增类型，默认为忽略
    public List<Object[]> getRedexContents(Long channelId, String searchWord, String channelIdString, Long userType, PageBean pageBean, long type);
    // ============
    public int incAllVisitCount(long contentId);

    public List<Content> getPicContent(long channelId, long modelId);

    public List<Content> getContentsByChannelId(long channelId, PageBean pageBean);

    public List<Content> getContentsByStatus(long status, String searchValue, Date startTime, Date endTime, PageBean pageBean);
}