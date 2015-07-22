package com.fortune.rms.timer.base;

import com.fortune.rms.business.encoder.logic.logicImpl.EncodeTaskManager;
import com.fortune.rms.business.encoder.logic.logicImpl.EncodeTaskManagerInterface;
import com.fortune.rms.business.encoder.logic.logicImpl.EncodeTaskRemoteManager;
import com.fortune.rms.business.system.logic.logicImpl.MediaServerMonitor;
import com.fortune.util.*;
import com.fortune.util.config.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-12
 * Time: 17:42:12
 * 
 */
public class TimerManager implements ServletContextListener {
    protected Log log = LogFactory.getLog(this.getClass());
    private static final String configName = "/timer.xml";

    public void contextInitialized(ServletContextEvent event) {
        try{
            final AppConfigurator appConfig = AppConfigurator.getInstance();
            if(appConfig.getBoolConfig("system.encoder.startEncoder",false)){
                log.info("30秒后启动任务请求和扫描！");
                (new Thread(){
                    public void run(){
                        try {
                            sleep(30000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EncodeTaskManagerInterface taskManager;
                        if(appConfig.getBoolConfig("system.encoder.nativeQuery",false)){
                            log.debug("数据库方式启动！");
                            taskManager = EncodeTaskManager.getInstance();
                        }else{
                            log.debug("远端请求方式启动！");
                            taskManager = EncodeTaskRemoteManager.getInstance();
                        }
                        taskManager.onSystemStartup();
                    }
                }).start();

            }else{
                log.info("无需启动转码服务");
            }
            String configPath = TimerManager.class.getResource(configName).getFile();
            String config = FileUtil.readFileInfo(configPath);

            org.dom4j.Element rootElement = DocumentHelper.parseText(config).getRootElement();
            List nodes = rootElement.selectNodes("/timers/timer");
            for (int i=0; i<nodes.size(); i++){
                Element element = (Element)nodes.get(i);
                String className = element.selectSingleNode("class").getText();
                String runtime = element.selectSingleNode("runtime").getText();
               // System.out.println(className);
               // System.out.println(runtime);

                TimerBase tb = (TimerBase)Class.forName(className).newInstance();
                HibernateUtils hibernateUtils = new HibernateUtils(event.getServletContext());
                tb.setHibernateUtils(hibernateUtils);
                tb.setTimer(tb,runtime);

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        EncodeTaskManager.getInstance().shutdown();
        TimerBase.shutdown();
        MediaServerMonitor.getInstance().shutdown();
    }

    public static void main(String args[]){
        TimerManager timerManager = new TimerManager();
        timerManager.contextInitialized(null);
    }
}