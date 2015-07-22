package com.fortune.rms.business.syn.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.syn.dao.daoInterface.SynTaskDaoInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynFileLogicInterface;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynFile;
import com.fortune.rms.business.syn.model.SynTask;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import com.fortune.util.config.Config;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-6-20
 * Time: 10:44:13
 */
@Service("synTaskLogicInterface")
public class SynTaskLogicImpl extends BaseLogicImpl<SynTask> implements SynTaskLogicInterface {
    private SynTaskDaoInterface synTaskDaoInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private SynFileLogicInterface synFileLogicInterface;
    public static long SYNTASK_WAIT_SYN = 1;
    public static long SYNTASK_START_SYN = 2;
    public static long SYNTASK_DOWN_ING = 3;
    public static long SYNTASK_DOWN_SUCCESS = 4;
    public static long SYNTASK_DOWN_FALSE = 5;
    public static long SYNTASK_DEL_ING = 6;
    public static long SYNTASK_DEL_SUCCESS = 7;
    public static long SYNTASK_DEL_FALSE = 8;
    @Autowired
    public void setSynTaskDaoInterface(SynTaskDaoInterface synTaskDaoInterface) {
        this.synTaskDaoInterface = synTaskDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.synTaskDaoInterface;
    }
    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }
    @Autowired
    public void setSynFileLogicInterface(SynFileLogicInterface synFileLogicInterface) {
        this.synFileLogicInterface = synFileLogicInterface;
    }

    /*
   * 添加任务
   *
   * */
    public void addSynTask(long synFileId,long synLevel) {
        Config config = new Config();
        String masterIp = config.getStrValue("master.ip", "127.0.0.1:8080");
        List<Device> devices = deviceLogicInterface.getDevicesExceptMasterDevice(masterIp);
        for (Device device : devices) {
            SynTask synTask = new SynTask();
            synTask.setSynFileId(synFileId);
            synTask.setDeviceId(device.getId());
            synTask.setSynStatus(SynTaskLogicImpl.SYNTASK_WAIT_SYN);
            synTask.setStartTime(new Date());
            synTask.setEndPos(-1l);
            synTask.setSynLevel(synLevel);
            this.synTaskDaoInterface.save(synTask);
        }

        try {
            pushCurrentTask(synFileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pushCurrentTask(long synFileId) throws Exception{
        List<SynTask> synTasks = searchCurrentSynTask(synFileId);
        Config config = new Config();
        String masterIp = config.getStrValue("master.ip", "127.0.0.1:8080");
        String encoding = config.getStrValue("web.encoding", "UTF-8");
        ExecutorService es = Executors.newFixedThreadPool(6);
//        ExecutorService es = Executors.newFixedThreadPool(1);
        String testIp = "192.168.1.114:8080";
        if (synTasks.size() != 0) {

            for (SynTask synTask : synTasks) {

                String url = "http://" +synTask.getDevice().getIp()+ "/syn/synTask!pullSynTask.action";
                Map<String, String> params = new HashMap<String, String>();
                String fileUrl = synTask.getSynFile().getUrl();
                fileUrl = com.fortune.util.net.URLEncoder.encode(com.fortune.util.net.URLEncoder.encode(fileUrl,encoding),encoding);
                String md5 = synTask.getSynFile().getMd5();
                String type = synTask.getSynFile().getType();
                String synId = String.valueOf(synTask.getId());
                params.put("fileUrl", fileUrl);
                params.put("md5", md5);
                params.put("type", type);
                params.put("synId", synId);
                params.put("masterIp", masterIp);
                DownloadWorker worker = new DownloadWorker(url, params);
                es.execute(worker);

            }
        }
    }

    /*
    * 推送任务
    *
    * */
    public void pushSynTask() throws Exception {

        List<SynTask> taskSyns = searchMasterSynTask();
        Config config = new Config();
        String masterIp = config.getStrValue("master.ip", "127.0.0.1:8080");
        String encoding = config.getStrValue("web.encoding", "UTF-8");
        ExecutorService es = Executors.newFixedThreadPool(5);
        if (taskSyns.size() != 0) {

            for (SynTask synTask : taskSyns) {
                String url = "http://" + synTask.getDevice().getIp() + "/syn/synTask!pullSynTask.action";
                Map<String, String> params = new HashMap<String, String>();
                String fileUrl = synTask.getSynFile().getUrl();
                fileUrl = com.fortune.util.net.URLEncoder.encode(com.fortune.util.net.URLEncoder.encode(fileUrl,encoding),encoding);
                String md5 = synTask.getSynFile().getMd5();
                String type = synTask.getSynFile().getType();
                String synId = String.valueOf(synTask.getId());
                params.put("fileUrl", fileUrl);
                params.put("md5", md5);
                params.put("type", type);
                params.put("synId", synId);
                params.put("masterIp", masterIp);
                DownloadWorker worker = new DownloadWorker(url, params);
                es.execute(worker);

            }
        }

    }

    public void pushSynTask(List<SynTask> synTasks) throws Exception {

        List<SynTask> taskSyns = synTasks;
        Config config = new Config();
        String masterIp = config.getStrValue("master.ip", "127.0.0.1:8080");
        String encoding = config.getStrValue("web.encoding", "UTF-8");
        if (taskSyns.size() != 0) {

            for (SynTask synTask : taskSyns) {
                String url = "http://" + synTask.getDevice().getIp() + "/syn/synTask!pullSynTask.action";
                Map<String, String> params = new HashMap<String, String>();
                String fileUrl = synTask.getSynFile().getUrl();
                fileUrl = com.fortune.util.net.URLEncoder.encode(com.fortune.util.net.URLEncoder.encode(fileUrl,encoding),encoding);
                String md5 = synTask.getSynFile().getMd5();
                String type = synTask.getSynFile().getType();
                String synId = String.valueOf(synTask.getId());
                params.put("fileUrl", fileUrl);
                params.put("md5", md5);
                params.put("type", type);
                params.put("synId", synId);
                params.put("masterIp", masterIp);
                ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5);
                DownloadWorker worker = new DownloadWorker(url, params);
                es.execute(worker);
            }
        }

    }

    public void updateSynTaskProcess(long synTaskId, long nStartPos, long nEndPos) {
        this.synTaskDaoInterface.updateTaskSynProcess(synTaskId, nStartPos, nEndPos);
    }

    public void downFile(String fileUrl, String md5, long synTaskId, String masterIp) {
        this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DOWN_ING);

        ServletContext context = ServletActionContext.getServletContext();
        String fullName = context.getRealPath(fileUrl);
        DownloadCallBack caller = new DownloadCallBack(synTaskId);
        HttpUtil.HttpDownFile(fileUrl, masterIp, caller);
        String filePath = FileUtils.extractFilePath(fullName, File.separator);
        String fileName = FileUtils.extractFileName(fullName, File.separator);
        try {
            String newMd5 = MD5Utils.getFileMD5String(fullName);

            if (md5.equals(newMd5)) {
                String fileTypes[] = fileName.split("\\.");
                String fileType = "";
                if(fileTypes.length==2){
                    fileType = fileTypes[1];
                }
                if (fileType.equals("zip")) {
                     ZipUtils.unZip(fullName.toString(), filePath+"\\");
                }

                this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DOWN_SUCCESS);
                SynTask synTask = this.synTaskDaoInterface.get(synTaskId);
                if(synTask.getSynLevel() == 1){
                    TomcatUtils.reBoot();
                }
            } else {
                File file = new File(fullName);
                if (file.isFile() && file.exists()) {

                    file.delete();
                }

                this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DOWN_FALSE);
            }

        } catch (Exception e) {
            this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DOWN_FALSE);
            e.printStackTrace();
            

        }
    }

    public void delFile(String fileUrl, long synTaskId) {
        this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DEL_ING);
        ServletContext context = ServletActionContext.getServletContext();
        String fullName = context.getRealPath(fileUrl);
        File file = new File(fullName);
        if(!file.exists()){
            logger.debug("删除文件失败:"+fullName+"不存在!");
            this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DEL_FALSE);
        }else{
            if(file.isFile()){
               file.delete();
               this.synTaskDaoInterface.updateTaskSynStatus(synTaskId, SynTaskLogicImpl.SYNTASK_DEL_SUCCESS);
            }
        }
       
    }

    public void updateSynTaskStatus(long synId, long synStatus) {
        this.synTaskDaoInterface.updateTaskSynStatus(synId, synStatus);
    }

    public List<SynTask> searchCurrentSynTask(long synFileId){
        List<Object[]> tempResult = this.synTaskDaoInterface.searchCurrentSynTask(synFileId);
        List<SynTask> result = new ArrayList<SynTask>();
        for (Object[] objs : tempResult) {
            SynTask taskSyn = (SynTask) objs[0];
            Device device = (Device) objs[1];
            taskSyn.setDevice(device);
            SynFile synFile = (SynFile) objs[2];
            taskSyn.setSynFile(synFile);
            result.add(taskSyn);
        }
        return result;
    }

    public List<SynTask> searchSynTask() {
        List<Object[]> tempResult = this.synTaskDaoInterface.searchSynTask();
        List<SynTask> result = new ArrayList<SynTask>();
        for (Object[] objs : tempResult) {
            SynTask taskSyn = (SynTask) objs[0];
            Device device = (Device) objs[1];
            taskSyn.setDevice(device);
            SynFile synFile = (SynFile) objs[2];
            taskSyn.setSynFile(synFile);
            result.add(taskSyn);
        }
        return result;
    }

    public List<SynTask> searchMasterSynTask() {
        List<Object[]> tempResult = this.synTaskDaoInterface.searchMasterSynTask();
        List<SynTask> result = new ArrayList<SynTask>();
        for (Object[] objs : tempResult) {
            SynTask taskSyn = (SynTask) objs[0];
            Device device = (Device) objs[1];
            taskSyn.setDevice(device);
            SynFile synFile = (SynFile) objs[2];
            taskSyn.setSynFile(synFile);
            result.add(taskSyn);
        }
        return result;
    }

    public List<SynTask> searchSlaveSynTask(String deviceIp) {
        List<Object[]> tempResult = this.synTaskDaoInterface.searchSlaveSynTask(deviceIp);
        List<SynTask> result = new ArrayList<SynTask>();
        for (Object[] objs : tempResult) {
            SynTask taskSyn = (SynTask) objs[0];
            Device device = (Device) objs[1];
            taskSyn.setDevice(device);
            SynFile synFile = (SynFile) objs[2];
            taskSyn.setSynFile(synFile);
            result.add(taskSyn);
        }
        return result;
    }

    public class DownloadCallBack implements HttpProcessCaller {
        private long taskId;

        public DownloadCallBack(long taskId) {
            this.taskId = taskId;
        }

        public int callBack(long downloaded, long totalSize) {
            updateSynTaskProcess(taskId, downloaded, totalSize);
            return 0;
        }
        public int finished(long downloaded, long totalSize) {
            return 0;
        }
        public int error(int n,String message) {
            return 0;
        }
        public void beforeStart(long totalSize){

        }

    }

    public List<SynTask> searchSynTaskByPage(SynTask synTask, PageBean pageBean,long spId) {

        List<Object[]> tempResult = this.synTaskDaoInterface.searchSynTaskByPage(synTask,pageBean,spId);
        List<SynTask> result = new ArrayList<SynTask>();
        for (Object[] objs : tempResult) {
            SynTask taskSyn = (SynTask) objs[0];
            Device device = (Device) objs[1];
            taskSyn.setDevice(device);
            SynFile synFile = (SynFile) objs[2];
            taskSyn.setSynFile(synFile);
            result.add(taskSyn);
        }
        return result;
    }

    public void reSync(long synTaskId) throws Exception {
        SynTask synTask = synTaskDaoInterface.get(synTaskId);
        synTask.setSynFile(synFileLogicInterface.get(synTask.getSynFileId()));
        synTask.setDevice(deviceLogicInterface.get(synTask.getDeviceId()));
        Config config = new Config();
        String masterIp = config.getStrValue("master.ip", "127.0.0.1:8080");
        String encoding = config.getStrValue("web.encoding", "UTF-8");
//        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5);
//        ExecutorService es = Executors.newFixedThreadPool(1);
        String testIp = "192.168.1.114:8080";
        if (synTask!=null) {
                String url = "http://" +synTask.getDevice().getIp()+ "/syn/synTask!pullSynTask.action";
                Map<String, String> params = new HashMap<String, String>();
                String fileUrl = synTask.getSynFile().getUrl();
                fileUrl = com.fortune.util.net.URLEncoder.encode(com.fortune.util.net.URLEncoder.encode(fileUrl,encoding),encoding);
                String md5 = synTask.getSynFile().getMd5();
                String type = synTask.getSynFile().getType();
                String synId = String.valueOf(synTask.getId());
                params.put("fileUrl", fileUrl);
                params.put("md5", md5);
                params.put("type", type);
                params.put("synId", synId);
                params.put("masterIp", masterIp);
                DownloadWorker worker = new DownloadWorker(url, params);
                worker.start();
        }
    }
}
