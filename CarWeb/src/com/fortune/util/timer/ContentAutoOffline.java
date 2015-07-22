package com.fortune.util.timer;

import com.fortune.util.HibernateUtils;
import com.fortune.util.StringUtils;

import java.util.Date;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-13
 * Time: 15:13:14
 * To change this template use File | Settings | File Templates.
 */
public class ContentAutoOffline  extends TimerBase {

    public void run() {
        try {
            System.out.println("run timer:"+StringUtils.date2string(new Date()));
            hibernateUtils.executeUpdate( "update Content c set c.status=1 where c.id in (select c.id from Content c where c.validEndTime<?)",new Object[]{new Date()} );
            hibernateUtils.executeUpdate( "update ContentCsp cc set cc.status=1 where cc.contentId in (select c.id from Content c where c.validEndTime<?)",new Object[]{new Date()} );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
