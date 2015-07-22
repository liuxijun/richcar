package com.fortune.rms.business.log.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.log.model.*;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VisitLogDaoInterface extends BaseDaoInterface<VisitLog, Long> {
     public void JDBCSaveVisitLogs(List<VisitLog> visitLogs);
     public List getOrganizationContributionCount(long spId, String startTime, String endTime, String channelsAndLeafs);
     public Map getResourceContributionCount(long spId, long cpId, String startTime, String endTime, String channelsAndLeafs, String contentName, String channelSelect, String playTimeSelect, PageBean pageBean);
     public List getAreaContributionCount(long spId, String startTime, String endTime);
     public Long getOnLineUserAnalysisCount(long spId, long cpId, String culTime);
     public List getActivityUserAnalysisCount(long spId, String startTime, String endTime, PageBean pageBean);
     public List getUserDemandCount(long spId, long cpId, String startTime, String endTime, String endTime1, String contentName, String channelName, String userIp, String userId, String playTime, PageBean pageBean);
     public List getChannelDemandCount(long spId, String startTime, String endTime, String channelsAndLeafs);
     public List getChannelOnDemandCount(long spId, String startTime, String endTime, Long isFree, PageBean pageBean);
     public List getResourceOnDemandCount(long spId, String startTime, String endTime, String channelName, String playTime, PageBean pageBean);
     public int getMaxConcurrentOfDate(String startTime, String endTime);
     public List getAllPhoneNotIsNull(String startTime, String endTime);
     public Long getOnlineUserOfDate(Date startDate, Date endDate);
     public List getLoginedUserInfo(String startDate, String endDate);
     public List getAreaLogsFromAreaDemandLog(Date startDate, Date endDate);
    public List getAllNetFlowFromDailyStaticsLog(Date startDate, Date endDate);
    public List getAllBingFaFromDailyStaticsLog(Date startDate, Date endDate);
    public List getContentByDateAndType(Date startDate, Date endDate, String type);
    public List getUserLoginLogs(Date startDate, Date endDate);
    public List getUserCountLogs(Date startDate, Date endDate);
    public List getUserLoginCountLogs(Date startDate, Date endDate);
    public List getUserDayCountLogs(Date startDate, Date endDate);
    public List getSpLogsFromVisitLog(Date startDate, Date endDate);
    public List getChannelLogsFromChannelDemandLog(Date startDate, Date endDate, String channelIds);
    public Map getUserLoginCount(String startTime, String endTime, String userIp, String userId, PageBean pageBean);
    public List checkChannelFilmOrTv(long channelId);
    //有的频道既有电影又有电视剧。
    public List getContentFilmAndTvLog(Date startTime, Date endDate, long contentType, String channelIds, String contentIds, int pageSize);

    public List getTotalHotRanked();
    public List getFileOrTvHotRanked(long contentType, String channelIds, String contentIds);
    public List<ContentDemandLog> getContentDemandLogByDate(String startDate, String endDate);
    public List<DailyStaticsLog> getDailyStaticsLogByDate(String startDate, String endDate);

    public List<AreaDemandLog> getAreaDemandLogByDate(String startDate, String endDate);
    public List<ChannelDemandLog> getChannelDemandLogByDate(String startDate, String endDate);
    public List<VisitLog> getVisitLogByDate(String startDate, String endDate);
    public List getUserLoginByDate(String startDate, String endDate);

    /*author: mlwang*/
    public List<Object[]> getRedexStatVisitCount(String channelIds, Date startTime, Date endTime, PageBean pageBean);
    public List<Object[]> getRedexChannelVisitCount(Date startTime, Date endTime);
    public int getTimeSpotAverageConcurrent(Date timeSpot, Date startTime, Date endTime) throws java.text.ParseException;
    public int getTimeSpotPeakConcurrent(Date timeSpot, Date startTime, Date endTime) throws java.text.ParseException;
    public List<Object[]> getRedexTop(String channelIds, Long userType, Date startTime, int count);
    /*end of mlwang block*/

   public List getDemandCountLogs(Date startDate, Date endDate);
    public List getSearchHotLogs(Date startDate, Date endDate);

}