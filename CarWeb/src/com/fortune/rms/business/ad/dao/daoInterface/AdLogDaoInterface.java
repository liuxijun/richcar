package com.fortune.rms.business.ad.dao.daoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.rms.business.ad.model.AdLog;
import com.fortune.util.PageBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdLogDaoInterface extends BaseDaoInterface<AdLog, Long> {
    public List<Map<String, Object>> stateForAd(String adName, Date startTime, Date stopTime, Integer trainId, PageBean pageBean);
    public List<Map<String, Object>> stateForTrain(String lineName, Date startTime, Date stopTime, Integer adId, PageBean pageBean);
    public List<Map<String, Object>> listLogs(String adName, String contentName, Long channelId, String lineName,
                                              Date startTime, Date stopTime, Integer adId, Integer trainId, PageBean pageBean);
}