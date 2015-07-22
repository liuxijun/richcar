package com.fortune.rms.business.log.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.log.dao.daoInterface.DailyStaticsLogDaoInterface;
import com.fortune.rms.business.log.logic.logicInterface.DailyStaticsLogLogicInterface;
import com.fortune.rms.business.log.model.DailyStaticsLog;
import com.fortune.util.SearchResult;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("dailyStaticsLogLogicInterface")
public class DailyStaticsLogLogicImpl extends BaseLogicImpl<DailyStaticsLog> implements DailyStaticsLogLogicInterface{
    private DailyStaticsLogDaoInterface dailyStaticsLogDaoInterface;
    /**
     * @param dailyStaticsLogDaoInterface the DailyStaticsLogLogicInterface to set
     */
    @Autowired
    public void setDailyStaticsLogDaoInterface(
            DailyStaticsLogDaoInterface dailyStaticsLogDaoInterface) {
        this.dailyStaticsLogDaoInterface = dailyStaticsLogDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.dailyStaticsLogDaoInterface;
    }

    public Map getNetFlowLogsFromVisitLog(Date startDate,Long liveChannelId,List cspIdList){
        return dailyStaticsLogDaoInterface.getNetFlowLogsFromVisitLog(startDate,liveChannelId,cspIdList);
    }

    public List getBingFaLogsFromVisitLog(Date startDate){
        try {
            List list = dailyStaticsLogDaoInterface.getAllTimeDataFromVisitLog(startDate);
//            Date startTime = StringUtils.string2date(startDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            //每15分钟一次，即一天分成96份
            Date date = StringUtils.string2date(StringUtils.date2string(startDate,"yyyy-MM-dd"),"yyyy-MM-dd");
            long totalCount = 96;
            long period = 900000;

            HashMap timeHm = new HashMap();
            long curTime = date.getTime();

            for (int i = 0; i < totalCount; i++) {
                for (int j = 0; j < list.size(); j++) {
                    Object objs[] = (Object[]) list.get(j);
                    long oneStartTime = (StringUtils.string2date(objs[0].toString())).getTime();
                    long oneEndTime = (StringUtils.string2date(objs[1].toString())).getTime();

                    if (oneStartTime < curTime && oneEndTime >= curTime) {
                        if (timeHm.get("" + curTime) == null) {
                            timeHm.put("" + curTime, new Long(0));
                        }
                        long count = ((Long) timeHm.get("" + curTime)).longValue();
                        count++;
                        timeHm.put("" + curTime, new Long(count));
                    }
                }
                curTime += period;
            }

            curTime = date.getTime();
            long maxValueTime = 0;
            List list2 = new ArrayList();

            long maxValue = 0;
            long count = 0;
            //如果日期格式为dd-HH:mm则为查询多个日期的，只获取每天的最大并发量。
                for (int i = 0; i < totalCount; i++) {
                    if (timeHm.get("" + curTime) != null) {
                        count = ((Long) timeHm.get("" + curTime)).longValue();
                    }else{
                        //如果 timeHm.get("" + curTime) ==null 则说明还未到这个时刻，那么这个时刻的并发值应该为0；
                        count = 0;
                    }
                    if (count > maxValue) {
                        maxValueTime = curTime;
                        maxValue = count;
                    }
                    Object objs[] = new Object[]{StringUtils.date2string(new Date(curTime), "HH:mm"), new Long(count)};
                    list2.add(objs);
                    curTime += period;
                }
              String maxValueDate = StringUtils.date2string(new Date(maxValueTime), "dd-HH:mm");
              Object objs1[] = new Object[]{StringUtils.date2string(startDate,"yyyy-MM-dd")+" "+maxValueDate,new Long(maxValue)};
              list2.add(objs1);

          return list2;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
