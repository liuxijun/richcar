package com.fortune.rms.business.encoder.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.encoder.dao.daoInterface.EncoderTaskDaoInterface;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class EncoderTaskDaoAccess extends BaseDaoAccess<EncoderTask,Long> implements EncoderTaskDaoInterface {

    public EncoderTaskDaoAccess() {
        super(EncoderTask.class);
    }

    public List searchTask(String taskName,String sourceFileName, String contentName,
                           Long encoderId,Long templateId,Long cspId,
                           Integer status,Date beginDate,Date endDate, PageBean pageBean) {
/*
        String hql = "from EncoderTask et,Device d" +
                " where et.clipId=cp.id and cp.contentId=c.id and et.encoderId=d.id and et.templateId=ep.id";
*/
        String fromTables = "from EncoderTask et,Device d";
        String whereStr = " where et.encoderId=d.id";
        List<Object> parameters = new ArrayList<Object>();
        if(taskName!=null&&!taskName.trim().equals("")){
            whereStr +=" and et.name like ?";
            parameters.add("%"+taskName+"%");
        }
        if(contentName!=null&&!"".equals(contentName.trim())){
            fromTables +=",Content c,ContentProperty cp";
            whereStr+=" and et.clipId=cp.id and cp.contentId=c.id and c.name like ?";
            parameters.add("%"+contentName+"%");
        }
        if(cspId!=null&&cspId>0){
            whereStr += " and et.clipId in(select cp_1.id from ContentProperty cp_1 where" +
                              " cp_1.contentId in (select c_1.id from Content c_1 where c_1.cspId = "+cspId+")" +
                                            ")";
        }
        if(status!=null&&status>0){
            whereStr+=" and et.status="+status;
        }
        if(sourceFileName!=null&&!"".equals(sourceFileName.trim())){
            whereStr+=" and et.sourceFileName like ?";
            parameters.add("%"+sourceFileName+"%");
        }
        if(beginDate!=null){
            whereStr+=" and et.startTime>?";
            parameters.add(beginDate);
        }
        if(endDate!=null){
            whereStr += " and et.startTime <= ?";
            parameters.add(endDate);
        }
        if(encoderId!=null&&encoderId>0){
            whereStr+=" and d.id="+encoderId;
        }
        if(templateId!=null&&templateId>0){
            fromTables+=",EncoderTemplate ep";
            whereStr+=" and et.templateId=ep.id and et.templateId="+templateId;
        }
        //this.getHibernateTemplate().find(hql);
        try {
            String hql = fromTables+whereStr;
            return getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            return new ArrayList();
        }

    }

    public String updateLog(EncoderTask task,boolean updateStatus) {
        String tempSql = "update EncoderTask et set et.encodeLog = ? ";
        if(updateStatus){
            tempSql +=" ,et.status="+task.getStatus();
        }
        tempSql +=" where et.id="+task.getId();
        final String encodeLog = task.getEncodeLog();
        Session session = null;
        try {
            session = this.getHibernateSession();
            Query query = session.createQuery(tempSql);
            query.setString(0,encodeLog);
            query.executeUpdate();
        } catch (HibernateException e) {
            e.printStackTrace();
            logger.error("更新日志时发生异常："+e.getMessage());
        } finally {
            if(session!=null){
                session.close();
            }
        }
        return task.getEncodeLog();
    }

    public int getUnFinishedTaskCountOfContent(long contentId) {
        return getTaskCountOfContentWithStatusCondition(contentId, EncoderTaskLogicInterface.STATUS_FINISHED,true);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getTaskCount(Long encoderId,Long streamServerId,Long cspId){
        String hql = "select et.status,count(*) from EncoderTask et";
        String condition="";
        if(encoderId!=null&&encoderId>0){
            condition+=" and et.encoderId="+encoderId;
        }
        if(streamServerId!=null&&streamServerId>0){
            condition+=" and et.streamServerId="+streamServerId;
        }
        if(cspId!=null&&cspId>0){
            condition +=" and et.clipId in (select id from ContentProperty cp where cp.contentId in " +
                    "(select id from Content c where c.cspId="+cspId+"))";
        }
        if(!"".equals(condition)){
            hql += " where "+condition.substring(5);
        }
        hql+=" group by et.status";
        return getHibernateTemplate().find(hql);
    }
    public int getTaskCountOfContentWithStatusCondition(long contentId, int taskStatus,boolean exclude) {
        if(contentId<=0){
            return -1;
        }
        //要考虑到除了clipId和contentProperty的ID对应外，source_file_name 和string_value也要对应。主要是为了防止出现
        //url变动后重新生成的任务与老任务的冲突
        String hql = "select count(*) from EncoderTask et where et.clipId in " +
                "(select cp.id from ContentProperty cp where cp.contentId="+contentId+") and et.sourceFileName" +
                " in (select cp.stringValue from ContentProperty cp where cp.contentId =" +contentId+
                ")";
        if(taskStatus>=0){
            hql+=" and et.status ";
            if(exclude){
                hql+="<>";
            }else {
                hql+="=";
            }
            hql+=""+ taskStatus;
        }
        return StringUtils.string2int(getHibernateTemplate().find(hql).get(0).toString(),-1);
    }

    public int getTaskCountOfContentExcludeStatus(long contentId, int excludeTaskStatus) {
        return getTaskCountOfContentWithStatusCondition(contentId,excludeTaskStatus,true);
    }
    public int getTaskCountOfContent(long contentId, int taskStatus) {
        return getTaskCountOfContentWithStatusCondition(contentId,taskStatus,false);
    }
}
