package com.fortune.rms.timer;

import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.timer.base.TimerBase;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;
import java.util.Date;
import java.util.List;
/**
 * ��Ȩ�����Զ����� �� ÿ������һ��
 * User: Administrator
 * Date: 2011-7-13
 * Time: 15:13:14
 * To change this template use File | Settings | File Templates.
 */
public class ContentAutoOffline  extends TimerBase {

    public void run() {
        try {
            logger.debug("��Ȩ�����Զ����� ContentAutoOffline run timer:"+StringUtils.date2string(new Date()));
            ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBeanForApp("contentLogicInterface");
            ContentCspLogicInterface contentCspLogicInterface = (ContentCspLogicInterface) SpringUtils.getBeanForApp("contentCspLogicInterface");
            SystemLogLogicInterface systemLogLogicInterface = (SystemLogLogicInterface)SpringUtils.getBeanForApp("systemLogLogicInterface");
            List<Content> contents = contentLogicInterface.getContentsOfExpired(-1L);
            Admin admin = new Admin();
            admin.setId(1);
            admin.setLogin("root");
            admin.setRealname("��������Ա");
            if(contents!=null&&contents.size()>0){
                int count = contents.size();
                Date now = new Date();
                String nowStr =  StringUtils.date2string(now);
                int start = 1;
                int end=0;
                String logMsg = "";
                for(Content content:contents){
//                    contentLogicInterface.cpOfflineContent(content);
                    end++;
                    content.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);
                    content.setStatusTime(now);
                    contentLogicInterface.save(content);
                    contentCspLogicInterface.setStatus(content.getId(), -1,-1,ContentCspLogicInterface.STATUS_OFFLINE);
                    String tempLog = "��"+content.getName()+"(id="+content.getId()+")����Ȩ�Ѿ�����(��Ȩ���ޣ�" +
                            StringUtils.date2string(content.getValidStartTime())+"->"+StringUtils.date2string(content.getValidEndTime())+
                            ",��ǰ��" +nowStr+
                            ")���Զ����ߡ�";
                    int allLogLen = logMsg.length();
                    int tempLogLen = tempLog.length();
                    if(allLogLen+tempLogLen>1990){
                        logMsg = "��Ȩ�����������߲���(" +start+
                                "-" +end+
                                "/" + count+")��"+logMsg;
                        start=end+1;
                        logger.debug(logMsg);
                        systemLogLogicInterface.saveLog("����",admin,this.getClass().getName(),logMsg);
                        logMsg = "";
                    }
                    logMsg +="\r\n"+tempLog;
                }
                logger.debug(logMsg);
                logMsg = "��Ȩ�����������߲���(" +start+
                        "-" +end+
                        "/" + count+")��"+logMsg;
                systemLogLogicInterface.saveLog("����",admin,this.getClass().getName(),logMsg);
            }else{
                String logInfo = "����ɨ���Ȩ���ڣ�δ�����й��ڵ�ý�壡";
                systemLogLogicInterface.saveLog("����",admin,this.getClass().getName(),logInfo);
                logger.debug(logInfo);
            }
//            hibernateUtils.executeUpdate( "update Content c set c.status=1 where c.id in (select c.id from Content c where c.validEndTime<?)",new Object[]{new Date()} );
//            hibernateUtils.executeUpdate( "update ContentCsp cc set cc.status=1 where cc.contentId in (select c.id from Content c where c.validEndTime<?)",new Object[]{new Date()} );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("�����쳣���޷�׼ȷ�������ý�壺"+e.getMessage());
        }
    }
}
