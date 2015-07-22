package com.fortune.rms.timer;

import com.fortune.util.AppConfigurator;
import com.fortune.util.JsUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-9-23
 * Time: 17:01:36
 * 延时执行指定任务
 */
public class DelayRunner extends Thread {
    //初始化静态线程实例
    private static DelayRunner instance = new DelayRunner();
    //定义一个用于存储定时线程的集合
    private static HashMap<String, PortalStackFileProcess> sessions;
    //定义线程停止的开关变量
    private boolean shouldStop = false;

    //默认构造函数，初始化线程集合
    private DelayRunner() {
        if (sessions == null) {
            sessions = new HashMap<String, PortalStackFileProcess>();
        }
    }
    //定义线程停止的方法
    public void stopDelayRunner() {
        shouldStop = true;
    }

    //定义获取该线程的静态实例，当该线程没有启动时，则立即启动它
    public static DelayRunner getInstance() {
        if (!instance.isAlive()) {
            instance.start();
        }
        return instance;
    }

    //向线程集合中注入线程
    public void startSession(String id, long cspId, String fullFilePath) {
        //根据线程的id得到该线程对象
        PortalStackFileProcess process = sessions.get(id);
        //设置线程延时执行的时间
        Integer delaySeconds = AppConfigurator.getInstance().getIntConfig("rms.delayWorker.delaySeconds",2);
        //如果集合中已存在该线程，则初始化线程的执行时间
        if (process != null) {
            process.setDelaySeconds(delaySeconds);
        } else {
            //否则，就实例化新的线程，并加入到线程集合中
            process = new PortalStackFileProcess(id, cspId, fullFilePath);
            process.setDelaySeconds(delaySeconds);
            sessions.put(id, process);
        }

    }

    //定义线程启动时 调用的方法
    public void run() {
        ExecutorService es = Executors.newFixedThreadPool(AppConfigurator.getInstance().getIntConfig("rms.delayWorker.sessionCount", 5));
        while (true) {
            try {
                //该线程先休眠1分钟
                sleep(1000L);
                //根据线程开发，判断是否继续执行
                if (shouldStop) break;
                //迭代该线程集合，取出每个线程
                Iterator<Map.Entry<String, PortalStackFileProcess>> it = sessions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, PortalStackFileProcess> m = it.next();
                    //获得每一个线程对象
                    PortalStackFileProcess process = m.getValue();
                    //如果该对象不为空，则将该线程的延时时间减一分钟
                    if (process != null) {
                        process.decDelaSeconds();
                        //判断该线程是否可以执行了，如果为true，则从线程池中删除该线程，并立刻执行该线程
                        if (process.isTime()) {
                            it.remove();
                            es.execute(process);
                        }
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        es.shutdown();
    }


    public static void main(String[] arg) {
        DelayRunner.getInstance().start();
//        DelayRunner.getInstance().startSession("recommend_id",);
//        DelayRunner.getInstance().startSession("list_channel_id");
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
        DelayRunner.getInstance().stopDelayRunner();
    }

    public class PortalStackFileProcess implements Runnable {
        private String id;
        private Integer delaySeconds;
        private long cspId;
        private String fullFilePath;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setDelaySeconds(Integer delaySeconds) {
            this.delaySeconds = delaySeconds;
        }

        public void decDelaSeconds(){
            delaySeconds--;
        }

        public boolean isTime(){
            return (delaySeconds<=0);
        }
        public PortalStackFileProcess(String id, long cspId, String fullFilePath) {
            this.id = id;
            this.cspId = cspId;
            this.fullFilePath = fullFilePath;
        }

        public void run() {

            if (id != null) {
                if (id.startsWith("list_")) {
                    try {
                        new JsUtils().createListJsFile(id, cspId, fullFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(id.startsWith("index_")) {
                    try {
                        new JsUtils().createRecommendJsFile(id, cspId, fullFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
