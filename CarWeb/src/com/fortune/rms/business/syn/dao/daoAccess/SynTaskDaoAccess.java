package com.fortune.rms.business.syn.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.syn.dao.daoInterface.SynTaskDaoInterface;
import com.fortune.rms.business.syn.model.SynTask;
import com.fortune.util.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liupeng
 * Date: 2011-6-20
 * Time: 10:32:23
 */
@Repository
public class SynTaskDaoAccess extends BaseDaoAccess<SynTask, Long> implements SynTaskDaoInterface {
    public SynTaskDaoAccess() {
        super(SynTask.class);
    }

    public void updateTaskSynStatus(long synId, long synStatus) {
        String sql = "update syn_task set syn_status =" + synStatus + " where id=" + synId + "";
        this.executeSQLUpdate(sql);
    }

    public void updateTaskSynProcess(long synTaskId, long startPos, long endPos) {
        String sql = "update syn_task set start_pos =" + startPos + ",end_pos = " + endPos + " where id=" + synTaskId + "";
        this.executeSQLUpdate(sql);
    }

    public List<Object[]> searchSynTask() {
        String hql = "from SynTask s_t,Device d,SynFile s_f " +
                "where s_t.deviceId=d.id and s_t.synFileId=s_f.id";
        return this.getHibernateTemplate().find(hql);
    }

    public List<Object[]> searchCurrentSynTask(long synFileId) {
        String hql = "from SynTask s_t,Device d,SynFile s_f "+
                "where s_t.deviceId=d.id and s_t.synFileId=s_f.id and s_f="+synFileId+"";
        return this.getHibernateTemplate().find(hql);
    }

    public List<Object[]> searchSynTaskByPage(SynTask synTask,PageBean pageBean,long spId) {
        List<Object[]> objects = new ArrayList<Object[]>();
        Session session = null;
        Query query;
        try{
            session = getSession();
            String hql = "from SynTask s_t,Device d,SynFile s_f " +
                    "where s_t.deviceId=d.id and s_t.synFileId=s_f.id and s_f.spId ="+spId+"";
            if(synTask!=null){
                if(synTask.getSynFile()!=null&&synTask.getSynFile().getName()!=null){
                        hql+=" and s_f.name like '%"+synTask.getSynFile().getName().trim()+"%'";
                }
            }
            String hql_count = "select count(*) " +hql;
            hql += " order by s_t.id desc";
            Query query_count = session.createQuery(hql_count);
            String count =query_count.list().get(0).toString();
            pageBean.setRowCount(new Integer(count));
            query = session.createQuery(hql);
            query.setFirstResult(pageBean.getStartRow());
            query.setMaxResults(pageBean.getPageSize());
            objects = query.list();
        }catch(Exception e){
             logger.error(e.getMessage());
        }finally {
            if(session!=null){
                session.close();
            }
        }
        return objects;
    }

    public List<Object[]> searchMasterSynTask() {
        String hql = "from SynTask s_t,Device d,SynFile s_f " +
                "where s_t.deviceId=d.id and s_t.synFileId=s_f.id and s_t.synStatus=1";
        return this.getHibernateTemplate().find(hql);
    }


    public List<Object[]> searchSlaveSynTask(String deviceIp) {

//        String hql = "from SynTask s_t,Device d,SynFile s_f " +
//                "where s_t.deviceId=d.id and s_t.synFileId=s_f.id and s_t.synStatus=1 and d.ip='"+deviceIp+"'";
        String hql = "from SynTask s_t,Device d,SynFile s_f " +
                "where s_t.deviceId=d.id and s_t.synFileId=s_f.id and s_t.synStatus=1";

        return this.getHibernateTemplate().find(hql);
    }

}
