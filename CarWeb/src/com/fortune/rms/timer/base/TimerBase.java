package com.fortune.rms.timer.base;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.fortune.util.HibernateUtils;
import com.fortune.util.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-12
 * Time: 17:41:56
 * m
 */
public class TimerBase implements Runnable{
    protected Logger logger = Logger.getLogger(this.getClass());
    public static ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(20);
    public static void shutdown(){
        scheduExec.shutdown();
        scheduExec.shutdownNow();       
    }


    public HibernateUtils hibernateUtils;
    public void setHibernateUtils(HibernateUtils hibernateUtils){
        this.hibernateUtils = hibernateUtils;
    }


    //minute 10,50
    //hour 10:00,20:00,30:00,40:00,50:00
    //day 10:20:30,15:50:00,20:23:12
    //week 1:10:33:12,2:20:22:50              1是从星期日开始
    //month 1:10:33:12,2:20:22:50,13:15:08:00

    public void setTimer(final Runnable task,String timerStr){
        
        String timeStrs[] = timerStr.split(" ");

        if (timeStrs==null || timeStrs.length!=2){
            return;
        }
        String timeUnit = timeStrs[0];
        String times[] = timeStrs[1].split(",");

        if ("minute".equals(timeUnit)){
            for (int i=0; i<times.length; i++){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND,Integer.parseInt( times[i] ));
                if (calendar.getTime().getTime()<System.currentTimeMillis()){
                    calendar.add(Calendar.MINUTE,1);
                }
                logger.debug("minute:"+StringUtils.date2string(calendar.getTime()));
                long delay = (calendar.getTime().getTime() - System.currentTimeMillis())/1000;
                scheduExec.scheduleWithFixedDelay(task, delay, 60, TimeUnit.SECONDS);
            }
        }

        if ("hour".equals(timeUnit)){
            for (int i=0; i<times.length; i++){
                Calendar calendar = Calendar.getInstance();
                String subTimes[] = times[i].split(":");
                calendar.set(Calendar.MINUTE,Integer.parseInt( subTimes[0] ));
                calendar.set(Calendar.SECOND,Integer.parseInt( subTimes[1] ));
                if (calendar.getTime().getTime()<System.currentTimeMillis()){
                    calendar.add(Calendar.HOUR,1);
                }
                logger.debug("hour:"+StringUtils.date2string(calendar.getTime()));
                long delay = (calendar.getTime().getTime() - System.currentTimeMillis())/1000;
                scheduExec.scheduleWithFixedDelay(task, delay, 3600, TimeUnit.SECONDS);
            }
        }

        if ("day".equals(timeUnit)){
            for (int i=0; i<times.length; i++){
                Calendar calendar = Calendar.getInstance();
                String subTimes[] = times[i].split(":");
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt( subTimes[0] ));
                calendar.set(Calendar.MINUTE,Integer.parseInt( subTimes[1] ));
                calendar.set(Calendar.SECOND,Integer.parseInt( subTimes[2] ));
                if (calendar.getTime().getTime()<System.currentTimeMillis()){
                    calendar.add(Calendar.DATE,1);
                }
                logger.debug("day:"+StringUtils.date2string(calendar.getTime()));
                long delay = (calendar.getTime().getTime() - System.currentTimeMillis())/1000;
                scheduExec.scheduleWithFixedDelay(task, delay, 86400, TimeUnit.SECONDS);
            }
        }

        if ("week".equals(timeUnit)){
            for (int i=0; i<times.length; i++){
                Calendar calendar = Calendar.getInstance();
                String subTimes[] = times[i].split(":");
                calendar.set(Calendar.DAY_OF_WEEK,Integer.parseInt( subTimes[0]));
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt( subTimes[1] ));
                calendar.set(Calendar.MINUTE,Integer.parseInt( subTimes[2] ));
                calendar.set(Calendar.SECOND,Integer.parseInt( subTimes[3] ));
                if (calendar.getTime().getTime()<System.currentTimeMillis()){
                    calendar.add(Calendar.DATE,7);
                }
                logger.debug("week:"+StringUtils.date2string(calendar.getTime()));
                long delay = (calendar.getTime().getTime() - System.currentTimeMillis())/1000;
                scheduExec.scheduleWithFixedDelay(task, delay, 604800, TimeUnit.SECONDS);
            }
        }

        if ("month".equals(timeUnit)){
            for (int i=0; i<times.length; i++){
                Calendar calendar = Calendar.getInstance();
                String subTimes[] = times[i].split(":");
                calendar.set(Calendar.DATE,Integer.parseInt( subTimes[0]));
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt( subTimes[1] ));
                calendar.set(Calendar.MINUTE,Integer.parseInt( subTimes[2] ));
                calendar.set(Calendar.SECOND,Integer.parseInt( subTimes[3] ));
                if (calendar.getTime().getTime()<System.currentTimeMillis()){
                    calendar.add(Calendar.MONTH,1);
                }
                logger.debug("month:"+StringUtils.date2string(calendar.getTime()));
                long delay = (calendar.getTime().getTime() - System.currentTimeMillis())/1000;

                Runnable runCommand = new Runnable() {
                    public void run() {
                        task.run();
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.add(Calendar.MONTH, 1);
                        //calendar1.add(Calendar.SECOND, 3);
                        logger.debug("month:"+StringUtils.date2string(calendar1.getTime()));
                        long delay = (calendar1.getTime().getTime() - System.currentTimeMillis())/1000;
                        scheduExec.schedule(this, delay, TimeUnit.SECONDS);
                    }};
                scheduExec.schedule(runCommand, delay, TimeUnit.SECONDS);
            }
        }
    }

    public void run(){}
}
