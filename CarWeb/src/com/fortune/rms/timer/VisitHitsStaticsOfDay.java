package com.fortune.rms.timer;

import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.StringUtils;

import java.util.*;

/**
 *  �������� ÿ�� ÿ�� ȫ�� �㲥��          ÿ���賿4��30����һ��
 * User: admin
 * Date: 13-6-30
 * Time: ����2:57
 * To change this template use File | Settings | File Templates.
 */
public class VisitHitsStaticsOfDay extends TimerBase {
    public void run(){
        try {

            logger.debug("�������� ÿ�� ÿ�� ȫ�� �㲥��  VisitHitsStaticsOfDay run timer:"+ StringUtils.date2string(new Date()));
//            logger.debug("��һ�����У�����ܵ���������¼�¼��");
//            List visitLogCount = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl group by vl.contentId") ;
//            hibernateUtils.executeBatch("update Content c set c.allVisitCount=? where c.id = ?",visitLogCount,visitLogCount.size());
//            logger.debug("���¼�¼�ܵ������ɡ�");

            Calendar cl = Calendar.getInstance();
//            hibernateUtils.executeUpdate("update Content c set c.weekVisitCount=0,c.monthVisitCount=0 where c.visitCountStatus is null or c.visitCountStatus != 1");
//            logger.debug("����ܣ��µ������");

            cl.add(Calendar.DATE,-7);
            Date startTime = cl.getTime();

//            List visitCountLogs = hibernateUtils.findAll("select count(*),contentId from VisitLog  where contentId in (select contentId from VisitLog  where startTime >=? and contentId not in (select c.id from Content c where c.visitCountStatus = 1 )) group by contentId",new Object[]{startTime});
            List visitCountLogs = hibernateUtils.findAll("select count(*),contentId from VisitLog where contentId in (select id from Content) group by contentId");
            hibernateUtils.executeBatch("update Content c set c.allVisitCount=? where c.id = ?",visitCountLogs,500);
            logger.debug("������Դ���ܵ��������"+visitCountLogs.size()+"����");

//            List visitWeekLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>=? and vl.contentId not in (select c.id from Content c where c.visitCountStatus = 1 ) group by vl.contentId",new Object[]{startTime});
            hibernateUtils.executeUpdate("update Content c set c.weekVisitCount=0,c.monthVisitCount=0");
            List visitWeekLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>=? and contentId in (select id from Content) group by vl.contentId",new Object[]{startTime});
            hibernateUtils.executeBatch("update Content c set c.weekVisitCount = ? where c.id = ? ",visitWeekLogs,500);
            logger.debug("����һ���ڱ������Դ���ܵ��������"+visitWeekLogs.size()+"����");
            //��ط����߼������⣡����

            Calendar cl1 = Calendar.getInstance();
            cl1.add(Calendar.MONTH,-1);
            Date startTime1 = cl1.getTime();

            {

                List visitMonthLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>? and contentId in (select id from Content) group by vl.contentId",new Object[]{startTime1});
                hibernateUtils.executeBatch("update Content c set c.monthVisitCount=? where c.id=?",visitMonthLogs,500);
                logger.debug("����һ���ڱ������Դ���µ��������"+visitMonthLogs.size()+"����");
            }

            logger.debug("���ݸ������");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
