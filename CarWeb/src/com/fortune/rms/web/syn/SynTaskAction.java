package com.fortune.rms.web.syn;

import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.syn.logic.logicImpl.SynFileLogicImpl;
import com.fortune.rms.business.syn.logic.logicImpl.SynTaskLogicImpl;
import com.fortune.rms.business.syn.logic.logicInterface.SynTaskLogicInterface;
import com.fortune.rms.business.syn.model.SynTask;
import com.fortune.util.config.Config;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 12:03:35
 * 
 */
@Namespace("/syn")
@ParentPackage("default")
@Action(value="synTask")
public class SynTaskAction extends BaseAction<SynTask> {
    private static final long serialVersionUID = 3243534534534534l;

    @SuppressWarnings("unchecked")
    public SynTaskAction() {
        super(SynTask.class);
    }

    private SynTaskLogicInterface synTaskLogicInterface;

    private String fileUrl;
    private String md5;
    private String type;
    private long synId;
    private String deviceIp;
    private String masterIp;

    @Autowired
    public void setSynTaskLogicInterface(SynTaskLogicInterface synTaskLogicInterface) {
        this.synTaskLogicInterface = synTaskLogicInterface;
        setBaseLogicInterface(synTaskLogicInterface);
    }

    public String list(){
        objs = this.synTaskLogicInterface.searchSynTaskByPage(obj,pageBean,admin.getCspId());
        return "list";
    }

    public String reSync() throws Exception {
        synTaskLogicInterface.reSync(keyId);
        return list();
    }


    public String pullSynTask(){
        Config config = new Config();
        HttpServletRequest request = ServletActionContext.getRequest();
        String remoteAddr = request.getRemoteAddr();
        if(remoteAddr.equals(Config.getStrValue("master.ip","61.55.144.81"))){
            String encoding = config.getStrValue("web.encoding", "UTF-8");
            this.synTaskLogicInterface.updateSynTaskStatus(synId,SynTaskLogicImpl.SYNTASK_START_SYN);

            try{
                fileUrl = URLDecoder.decode(fileUrl,encoding);
            }catch(UnsupportedEncodingException e){

            }
            if(type.equals(SynFileLogicImpl.SYNFILE_ADD)){
                this.synTaskLogicInterface.downFile(fileUrl,md5,synId,masterIp);
            }else if(type.equals(SynFileLogicImpl.SYNFILE_DEL)){
                this.synTaskLogicInterface.delFile(fileUrl,synId);
            }
        }else{
            log.error("错误的IP发来的命令请求，拒绝这个命令："+remoteAddr+",type="+type);
        }

        return null;
    }

    public String pushSlaveSynTask() throws Exception {
        List<SynTask> synTasks = this.synTaskLogicInterface.searchSlaveSynTask(deviceIp);
        if(synTasks.size()!=0){
            this.synTaskLogicInterface.pushSynTask(synTasks);
        }
        return null;
    }



    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSynId() {
        return synId;
    }

    public void setSynId(long synId) {
        this.synId = synId;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }
}
