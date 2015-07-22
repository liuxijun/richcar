package com.fortune.util.timer;

import com.fortune.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-13
 * Time: 17:17:23
 * To change this template use File | Settings | File Templates.
 */
public class VisitHitsStatics  extends TimerBase {

    public void run() {
        try {

            System.out.println("run timer:"+ StringUtils.date2string(new Date()));

            Calendar calendar = Calendar.getInstance();

            {
                calendar.add(Calendar.DATE,-7);
                Date startTime = calendar.getTime();

                hibernateUtils.executeUpdate("update Content c set c.weekVisitCount=0");

                List visitLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>? group by vl.contentId",new Object[]{startTime});
                List params = new ArrayList();
                for (int i=0; i<visitLogs.size(); i++){
                    Object objs[]= (Object[])visitLogs.get(i);
                    params.add(objs);
                }

                hibernateUtils.executeBatch("update Content c set c.weekVisitCount=? where c.id=?",params,500);
                hibernateUtils.executeBatch("update Content c set c.allVisitCount=c.allVisitCount+? where c.id=?",params,500);            
            }

            {
                calendar.add(Calendar.DATE,7);
                calendar.add(Calendar.MONTH,-1);
                Date startTime = calendar.getTime();

                hibernateUtils.executeUpdate("update Content c set c.monthVisitCount=0");

                List visitLogs = hibernateUtils.findAll("select count(*),vl.contentId from VisitLog vl where vl.startTime>? group by vl.contentId",new Object[]{startTime});
                List params = new ArrayList();
                for (int i=0; i<visitLogs.size(); i++){
                    Object objs[]= (Object[])visitLogs.get(i);
                    params.add(objs);
                }

                hibernateUtils.executeBatch("update Content c set c.monthVisitCount=? where c.id=?",params,500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}