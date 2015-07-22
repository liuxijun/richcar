package com.fortune.util.timer;

import com.fortune.util.StringUtils;
import java.util.Date;
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-12
 * Time: 18:27:27
 * To change this template use File | Settings | File Templates.
 */
public class Test extends TimerBase implements Runnable{
    public void run() {
        try {
            System.out.println(StringUtils.date2string(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String args[]){
        Test test = new Test();
        //test.setTimer(test,"minute 10,50");
        //test.setTimer(test,"hour 10:00,20:00,30:00,40:00,50:00");
        //test.setTimer(test,"day 10:20:30,15:50:00,20:23:12");
        //test.setTimer(test,"week 1:10:33:12,2:20:22:50");
        //test.setTimer(test,"month 1:10:33:12,2:20:22:50,13:15:08:00");
        VisitHitsStatics hts = new VisitHitsStatics();
        

    }
}
