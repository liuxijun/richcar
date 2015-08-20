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
        //indexҳ���ѯ���õķ���
        objs = contentNoticeLogicInterface.getOnlineContentNotice(status);
        return "list";
    }
   //�½�����ֱ������
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
       //���keyIdС�������Ϊ�գ�Ϊ������
       if( keyId < 0){
           ContentNotice cn = new ContentNotice();
           cn.setAdminId(adminId);
           cn.setContent(content);
           cn.setCreateTime(createTime);
           cn.setOfflineTime(offlineTime);
           cn.setOnlineTime(onlineTime);
           cn.setStatus(status);
           int i =contentNoticeLogicInterface.updateOnlineContentNotice(status);
           log.debug("����"+i+"��¼");
           contentNoticeLogicInterface.save(cn);
           log.debug("����һ����¼");
           logInfo +="����һ����¼��"+content+"��,��ֱ������";
       } else{
           int i = contentNoticeLogicInterface.updateOnlineContentNotice(status);
           log.debug("����"+i+"��¼");
           ContentNotice cn = contentNoticeLogicInterface.get(new Long(keyId));
           cn.setAdminId(adminId);
           cn.setContent(content);
           cn.setCreateTime(createTime);
           cn.setOfflineTime(offlineTime);
           cn.setOnlineTime(onlineTime);
           cn.setStatus(status);
           contentNoticeLogicInterface.save(cn);
           log.debug("�ɹ��޸ļ�¼,keyId="+keyId);
           logInfo += "�޸�id:"+keyId+"����Ϣ��ֱ������";
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
        //�����������߹���
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
                    infoNames +="idΪ:"+keyId+"����Ϊ��"+cn.getContent()+"��,";
                    if (status == 1 || status == 2) {
                        //����
                        if (status == 1) {
                            cn.setStatus(1L);
                            userOperation="����";
                        }
                        //����
                        if (status == 2) {
                         //�����Ѿ����ߵ����ݲ�����
                         int i = contentNoticeLogicInterface.updateOnlineContentNotice(status);
                         cn.setStatus(2L);
                         log.debug("����"+i+"����¼�������ݡ�"+cn.getContent()+"�����ߣ�");
                         userOperation="����";
                        }
                        contentNoticeLogicInterface.save(cn);
                    }
                    if(status==9){
                      userOperation="ɾ��";
                      contentNoticeLogicInterface.remove(cn);
                    }
                    dealMessage += aKey;
                } catch (Exception e) {
                    super.addActionError("�޷��޸���Դ״̬" +
                            ":" + aKey);
                }
            }
            if(infoNames!=""){
                infoNames = infoNames.substring(0,infoNames.length()-1);
            }
            logInfo +="��"+infoNames+"�������"+userOperation;
            writeSysLog(logInfo);
        }
        if ("".equals(dealMessage)) {
            super.addActionError("û�в����κ�����");
            setSuccess(false);
        } else {
            super.addActionMessage("�Ѿ��ɹ��޸���Դ״̬(" + dealMessage + ")");
            setSuccess(true);
        }
        return Constants.ACTION_DELETE;
    }
}
