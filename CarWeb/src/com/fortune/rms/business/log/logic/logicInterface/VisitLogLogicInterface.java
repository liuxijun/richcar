package com.fortune.rms.business.log.logic.logicInterface;

import cn.sh.guanghua.mediastack.dataunit.UserLog;
import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.content.model.ContentDTO;
import com.fortune.rms.business.log.model.*;
import com.fortune.rms.business.user.model.UserLogin;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VisitLogLogicInterface extends BaseLogicInterface<VisitLog> {
    public void JDBCSaveVisitLogs(List<VisitLog> visitLogs);
    public Map getOrganizationContributionCount(long spId, String startTime, String endTime, String channelsAndLeafs);
    public Map getResourceContributionCount(long spId, long cpId, String startTime, String endTime, String channelsAndLeafs, String contentName, String channelSelect, String playTimeSelect, PageBean pageBean);
    public Map getAreaContributionCount(long spId, String startTime, String endTime);
    public List getOnLineUserAnalysisCount(long spId, long cpId, String startTime, String endTime);
    public List getActivityUserAnalysisCount(long spId, String startTime, String endTime, PageBean pageBean);
    public Map getUserDemandCount(long spId, long cpId, String startTime, String endTime, String endTime1, String contentName, String channelName, String userIp, String userId, String playTime, PageBean pageBean);
    public List getChannelDemandCount(long spId, String startTime, String endTime, String channelsAndLeafs);
    public Map getChannelOnDemandCount(long spId, String startTime, String endTime, long isFree, PageBean pageBean);
    public Map getResourceOnDemandCount(long spId, String startTime, String endTime, String channelName, String playTime, PageBean pageBean);
    public int getMaxConcurrentOfDate(String startTime, String endTime);
    public List getAllPhoneNotIsNull(String startTime, String endTime);
    public Long getOnlineUserOfDate(Date startDate, Date endDate);
    public List getLoginedUserInfo(String startDate, String endDate);
    public List getAreaLogsFromAreaDemandLog(Date startDate, Date endDate);
    public List getAllNetFlowFromDailyStaticsLog(Date startDate, Date endDate);
    public List getAllBingFaFromDailyStaticsLog(Date startDate, Date endDate);
    public List getUserLoginLogs(Date startDate, Date endDate);
    public List getSpLogsFromVisitLog(Date startDate, Date endDate);
    public List getChannelLogsFromChannelDemandLog(Date startDate, Date endDate, long channelId, String isExport);
    public List getContentByDateAndType(Date startDate, Date endDate, String type);
    public List getContentFilmAndTvLog(Date startDate, Date endDate, long type, int pageSize);
    public String createExcelFile(List basicInfoList, List<Object[]> areaLogList, List<Object[]> spLogList, List<Object[]> contentLogList, List<Object[]> contentLiveLogList, List<Object[]> contentLadongLogList, List<Object[]> bingfaList, List<Object[]> netFlowList,
                                  List<Object[]> channelLogList, List<Object[]> contentByLengthLogList, List<Object[]> contentByNetFlowList, List<Object[]> userLoginLogList, List<Object[]> contentFilmLogList, List<Object[]> contentTvLogList,
                                  List<Map<String, String>> countLogList, List<Object[]> systemPlayedLogList, List<Object[]> searchHotLogList);
    public String createExcelFile(Date startDate, Date endDate, long contentType, List<Object[]> list, String excelName);
    public Map getUserLoginCount(String startTime, String endTime, String userIp, String userId, PageBean pageBean);

    public List getTotalHotRanked();
    public List getFileOrTvHotRanked(long contentType);

    public List<ContentDemandLog> getContentDemandLogByDate(String startDate, String endDate);

    public List<DailyStaticsLog> getDailyStaticsLogByDate(String startDate, String endDate);

    public List<AreaDemandLog> getAreaDemandLogByDate(String startDate, String endDate);

    public List<ChannelDemandLog> getChannelDemandLogByDate(String startDate, String endDate);

    public List<VisitLog> getVisitLogByDate(String startDate, String endDate);

    public List getUserLoginByDate(String startDate, String endDate);

    /*Author : mlwang*/
    public List<RedexStatVisit> getRedexContentVisitStat(Long channelId, Date startTime, Date endTime, PageBean pageBean);
    public List<RedexStatChannelVisit> getRedexChannelVisitStat(Date startTime, Date endTime);
    public List<RedexStatConcurrent> getRedexConcurrentStat(Date startTime, Date endTime);
    public List<ContentDTO> getTop(List<Long> channelIdList, Long userType, int days, int count);
    /*end of mlwang block*/

    public List getDemandCountLogs(Date startDate, Date endDate);
    public List getCountList(Date startDate, Date endDate);
    public String createExcelFile1(Date startDate, Date endDate, long contentType, List<Map<String, String>> list, String excelName);
    public  List getSearchHotLogs(Date startDate, Date endDate);
}
