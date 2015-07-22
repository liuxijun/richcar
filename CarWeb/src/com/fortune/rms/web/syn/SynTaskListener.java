package com.fortune.rms.web.syn;


import com.fortune.util.DownloadWorker;
import com.fortune.util.HttpUtil;
import com.fortune.util.config.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-24
 * Time: 15:13:48
 * 
 */
public class  SynTaskListener implements ServletContextListener {
    public ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
    protected Log log = LogFactory.getLog(this.getClass());

    public void contextInitialized(ServletContextEvent event) {
        Runnable task = new Runnable() {
            public void run() {
                try {
                    log.debug("start...");
                    try {
                        Config config = new Config();
                        String masterIp = config.getStrValue("master.ip", null);


                        String url = "http://" + masterIp + "/syn/synTask!pushSlaveSynTask.action";
                        Map<String, String> params = new HashMap<String, String>();
                        InetAddress address = InetAddress.getLocalHost();
                        String deviceIp = address.getHostAddress();
                        log.debug("slave device ip :"+deviceIp);
                        params.put("deviceIp", deviceIp);
//                        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*5);
                        DownloadWorker worker = new DownloadWorker(url,params);
                        worker.start();
//                        es.execute(worker);

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                    log.debug("end...");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        scheduExec.scheduleWithFixedDelay(task, 10, 300, TimeUnit.SECONDS);

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        scheduExec.shutdown();
        scheduExec.shutdownNow();
    }
}