package com.fortune.rms.web.content;

import com.fortune.common.Constants;
import com.fortune.rms.business.content.logic.logicInterface.ContentNoticeLogicInterface;
import com.fortune.rms.business.content.model.ContentNotice;
import com.fortune.common.web.base.BaseAction;
import com.fortune.util.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentNotice")
public class ContentNoticeAciton extends BaseAction<ContentNotice> {
    private static final long serialVersionUID = 3243534534534534l;
    private ContentNoticeLogicInterface contentNoticeLogicInterface;
    private long spId;
    private long status;
    private String content;
    private String createTime;
    private String offlineTime;
    private String onlineTime;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
    }

    @SuppressWarnings("unchecked")
    public ContentNoticeAciton() {
        super(ContentNotice.class);
    }
    /**
     * @param contentNoticeLogicInterface the contentRelatedLogicInterface to set
     */
    @Autowired
    public void setContentNoticeLogicInterface(
            ContentNoticeLogicInterface contentNoticeLogicInterface) {
        this.contentNoticeLogicInterface = contentNoticeLogicInterface;
        setBaseLogicInterface(contentNoticeLogicInterface);
    }

    public String getOnlineContentNotice(){
        log.debug("in getOnlineContentNotice");
        //index页面查询调用的方法
        objs = contentNoticeLogicInterface.getOnlineContentNotice(status);
        return "list";
    }
   //新建公告直接上线
   public String saveAllData(){
       log.debug("in saveAllData");
       String logInfo = admin.getLogin()+"("+admin.getRealname()+")";
       Long keyId = StringUtils.string2long(this.getKeyId(), -1);
       String content = obj.getContent();
       Long adminId = obj.getAdminId();
       Date createTime = obj.getCreateTime();
       Date onlineTime = obj.getOnlineTime();
       Date offlineTime = obj.getOfflineTime();
       Long status = obj.getStatus();
       //如果keyId小于零或者为空，为新增。
       if( keyId < 0){
           ContentNotice cn = new ContentNotice();
           cn.setAdminId(adminId);
           cn.setContent(content);
           cn.setCreateTime(createTime);
           cn.setOfflineTime(offlineTime);
           cn.setOnlineTime(onlineTime);
           cn.setStatus(status);
           int i =contentNoticeLogicInterface.updateOnlineContentNotice(status);
           log.debug("下线"+i+"记录");
           contentNoticeLogicInterface.save(cn);
           log.debug("新增一条记录");
           logInfo +="新增一条记录《"+content+"》,并直接上线";
       } else{
           int i = contentNoticeLogicInterface.updateOnlineContentNotice(status);
           log.debug("下线"+i+"记录");
           ContentNotice cn = contentNoticeLogicInterface.get(new Long(keyId));
           cn.setAdminId(adminId);
           cn.setContent(content);
           cn.setCreateTime(createTime);
           cn.setOfflineTime(offlineTime);
           cn.setOnlineTime(onlineTime);
           cn.setStatus(status);
           contentNoticeLogicInterface.save(cn);
           log.debug("成功修改记录,keyId="+keyId);
           logInfo += "修改id:"+keyId+"的信息并直接上线";
       }
       writeSysLog(logInfo);
       return Constants.ACTION_SAVE;
   }

    public String contentNotice_offline() {
        return setContentNoticeStatus(status, keys);
    }

    public String contentNotice_online(){
        return setContentNoticeStatus(status,keys);
    }
    public String contentNotice_delete(){
        return setContentNoticeStatus(status,keys);
    }

    private String setContentNoticeStatus( Long status, List<String> keyIds) {
        //公告上线下线管理
        log.debug("in setContentCspStatus");
        String dealMessage = "";
        if (keyIds != null) {
            String infoNames="";
            String userOperation="";
            String logInfo = admin.getLogin()+"("+admin.getRealname()+")";
            for (String aKey : keyIds) {
                try {
                    long keyId = StringUtils.string2long(aKey,-1);
                    if(keyId<=0){
                        continue;
                    }
                    ContentNotice cn = contentNoticeLogicInterface.get(keyId);
                    infoNames +="id为:"+keyId+"内容为《"+cn.getContent()+"》,";
                    if (status == 1 || status == 2) {
                        //下线
                        if (status == 1) {
                            cn.setStatus(1L);
                            userOperation="下线";
                        }
                        //上线
                        if (status == 2) {
                         //查找已经上线的内容并下线
                         int i = contentNoticeLogicInterface.updateOnlineContentNotice(status);
                         cn.setStatus(2L);
                         log.debug("下线"+i+"条记录，将内容《"+cn.getContent()+"》上线！");
                         userOperation="上线";
                        }
                        contentNoticeLogicInterface.save(cn);
                    }
                    if(status==9){
                      userOperation="删除";
                      contentNoticeLogicInterface.remove(cn);
                    }
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("无法修改资源状态" +
                            ":" + aKey);
                }
            }
            if(infoNames!=""){
                infoNames = infoNames.substring(0,infoNames.length()-1);
            }
            logInfo +="将"+infoNames+"的走马灯"+userOperation;
            writeSysLog(logInfo);
        }
        if ("".equals(dealMessage)) {
            super.addActionError("没有操作任何数据");
            setSuccess(false);
        } else {
            super.addActionMessage("已经成功修改资源状态(" + dealMessage + ")");
            setSuccess(true);
        }
        return Constants.ACTION_DELETE;
    }
}
