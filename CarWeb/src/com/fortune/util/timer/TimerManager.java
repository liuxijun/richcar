package com.fortune.util.timer;

import com.fortune.util.HibernateUtils;
import com.fortune.util.config.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-12
 * Time: 17:42:12
 * To change this template use File | Settings | File Templates.
 */
public class TimerManager implements ServletContextListener {
    protected Log log = LogFactory.getLog(this.getClass());

    public void contextInitialized(ServletContextEvent event) {
        try{
            TimerBase tb = (TimerBase)Class.forName("com.fortune.util.timer.ContentAutoOffline").newInstance();
            HibernateUtils hibernateUtils = new HibernateUtils(event.getServletContext());
            tb.setHibernateUtils(hibernateUtils);
            tb.setTimer(tb,"day 16:22:00");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}