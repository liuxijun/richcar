package com.fortune.rms.timer;

import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.StringUtils;

import java.util.*;

/**
 *  整理内容 每周 每月 全部 点播量          每天凌晨4点30运行一次
 * User: admin
 * Date: 13-6-30
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
public class VisitHitsStaticsOfDay extends TimerBase {
    public void run(){
        try {

            logger.debug("整理内容 每周 每月 全部 点播量  VisitHitsStaticsOfDay run timer:"+ StringUtils.date2string(new Date()));
//            logger.debug("第一次运行，清空总点击量，重新记录。");
//            List visitLogCount = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl group by vl.contentId") ;
//            hibernateUtils.executeBatch("update Content c set c.allVisitCount=? where c.id = ?",visitLogCount,visitLogCount.size());
//            logger.debug("重新记录总点击量完成。");

            Calendar cl = Calendar.getInstance();
//            hibernateUtils.executeUpdate("update Content c set c.weekVisitCount=0,c.monthVisitCount=0 where c.visitCountStatus is null or c.visitCountStatus != 1");
//            logger.debug("清空周，月点击量。");

            cl.add(Calendar.DATE,-7);
            Date startTime = cl.getTime();

//            List visitCountLogs = hibernateUtils.findAll("select count(*),contentId from VisitLog  where contentId in (select contentId from VisitLog  where startTime >=? and contentId not in (select c.id from Content c where c.visitCountStatus = 1 )) group by contentId",new Object[]{startTime});
            List visitCountLogs = hibernateUtils.findAll("select count(*),contentId from VisitLog where contentId in (select id from Content) group by contentId");
            hibernateUtils.executeBatch("update Content c set c.allVisitCount=? where c.id = ?",visitCountLogs,500);
            logger.debug("更新资源的总点击量，共"+visitCountLogs.size()+"个。");

//            List visitWeekLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>=? and vl.contentId not in (select c.id from Content c where c.visitCountStatus = 1 ) group by vl.contentId",new Object[]{startTime});
            hibernateUtils.executeUpdate("update Content c set c.weekVisitCount=0,c.monthVisitCount=0");
            List visitWeekLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>=? and contentId in (select id from Content) group by vl.contentId",new Object[]{startTime});
            hibernateUtils.executeBatch("update Content c set c.weekVisitCount = ? where c.id = ? ",visitWeekLogs,500);
            logger.debug("更新一周内被点击资源的周点击量，共"+visitWeekLogs.size()+"个。");
            //这地方的逻辑有问题！！！

            Calendar cl1 = Calendar.getInstance();
            cl1.add(Calendar.MONTH,-1);
            Date startTime1 = cl1.getTime();

            {

                List visitMonthLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>? and contentId in (select id from Content) group by vl.contentId",new Object[]{startTime1});
                hibernateUtils.executeBatch("update Content c set c.monthVisitCount=? where c.id=?",visitMonthLogs,500);
                logger.debug("更新一月内被点击资源的月点击量，共"+visitMonthLogs.size()+"个。");
            }

            logger.debug("数据更新完毕");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
