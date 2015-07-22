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
 * ��ʱִ��ָ������
 */
public class DelayRunner extends Thread {
    //��ʼ����̬�߳�ʵ��
    private static DelayRunner instance = new DelayRunner();
    //����һ�����ڴ洢��ʱ�̵߳ļ���
    private static HashMap<String, PortalStackFileProcess> sessions;
    //�����߳�ֹͣ�Ŀ��ر���
    private boolean shouldStop = false;

    //Ĭ�Ϲ��캯������ʼ���̼߳���
    private DelayRunner() {
        if (sessions == null) {
            sessions = new HashMap<String, PortalStackFileProcess>();
        }
    }
    //�����߳�ֹͣ�ķ���
    public void stopDelayRunner() {
        shouldStop = true;
    }

    //�����ȡ���̵߳ľ�̬ʵ���������߳�û������ʱ��������������
    public static DelayRunner getInstance() {
        if (!instance.isAlive()) {
            instance.start();
        }
        return instance;
    }

    //���̼߳�����ע���߳�
    public void startSession(String id, long cspId, String fullFilePath) {
        //�����̵߳�id�õ����̶߳���
        PortalStackFileProcess process = sessions.get(id);
        //�����߳���ʱִ�е�ʱ��
        Integer delaySeconds = AppConfigurator.getInstance().getIntConfig("rms.delayWorker.delaySeconds",2);
        //����������Ѵ��ڸ��̣߳����ʼ���̵߳�ִ��ʱ��
        if (process != null) {
            process.setDelaySeconds(delaySeconds);
        } else {
            //���򣬾�ʵ�����µ��̣߳������뵽�̼߳�����
            process = new PortalStackFileProcess(id, cspId, fullFilePath);
            process.setDelaySeconds(delaySeconds);
            sessions.put(id, process);
        }

    }

    //�����߳�����ʱ ���õķ���
    public void run() {
        ExecutorService es = Executors.newFixedThreadPool(AppConfigurator.getInstance().getIntConfig("rms.delayWorker.sessionCount", 5));
        while (true) {
            try {
                //���߳�������1����
                sleep(1000L);
                //�����߳̿������ж��Ƿ����ִ��
                if (shouldStop) break;
                //�������̼߳��ϣ�ȡ��ÿ���߳�
                Iterator<Map.Entry<String, PortalStackFileProcess>> it = sessions.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, PortalStackFileProcess> m = it.next();
                    //���ÿһ���̶߳���
                    PortalStackFileProcess process = m.getValue();
                    //����ö���Ϊ�գ��򽫸��̵߳���ʱʱ���һ����
                    if (process != null) {
                        process.decDelaSeconds();
                        //�жϸ��߳��Ƿ����ִ���ˣ����Ϊtrue������̳߳���ɾ�����̣߳�������ִ�и��߳�
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
