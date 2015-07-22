package com.fortune.rms.business.vote.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.vote.dao.daoInterface.VoteDaoInterface;
import com.fortune.rms.business.vote.model.Vote;
import com.fortune.util.PageBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ����· on 2014/12/24.
 */
@Repository
public class VoteDaoAccess extends BaseDaoAccess<Vote, Long> implements VoteDaoInterface {
    public VoteDaoAccess() {
        super(Vote.class);
    }

    /**
     * ����ͶƱ
     * @param searchWord ��ѯ�ؼ���
     * @param pageBean    ��ҳ��Ϣ
     * @return ��������������ͶƱ
     */
    public List<Vote> searchVote(String searchWord, PageBean pageBean){
        String hql = "from Vote v";
        if( searchWord != null && !searchWord.isEmpty() ){
            hql += " where v.title like '%" + searchWord + "%'";
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<Vote>();
        }
    }

    /**
     * ״̬������ͶƱ�б�״̬Ϊ���ߣ�����Чʱ�䷶Χ��
     * @return �����������б�
     */
    public List<Vote> normalVotes(){
        String hql = "from Vote v where v.status=" + Vote.VOTE_STATUS_ONLINE + " and v.startTime<=:NOW and v.endTime>=:NOW";
        Session session = getSession();
        Query q = session.createQuery(hql);
        q.setTimestamp("NOW", new Date());
        List<Vote> list = q.list();
        if( session != null && session.isOpen() ){
            session.close();
        }

        return list;
    }

    /**
     * ����Id����ɾ��ͶƱ
     * @param idArray
     */
    public void removeByIdArray(String idArray){
        String hql = "delete from Vote v where v.id in (" + idArray + ")";
        executeUpdate(hql);
    }


    public List getVoteAndInvestigationList(PageBean pageBean) {
        Session session = getSession();
        try {
            String countSql = "select count(*) from (select v.id,v.title,1 from vote v union select i.id,i.title,2 from investigation i) as count";
            Query countQuery = session.createSQLQuery(countSql);
            pageBean.setRowCount(new Integer(countQuery.list().get(0).toString()));

            String sql = "select v.id,v.title,1,v.create_Time from vote v union select i.id,i.title,2,i.create_Time from investigation i order by id";
            Query query = session.createSQLQuery(sql);
            query.setFirstResult(pageBean.getStartRow());
            query.setMaxResults(pageBean.getPageSize());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            session.close();
        }

        return null;
    }

    public Vote getLastVote() {
        String hql = "from Vote order by createTime desc";
        List list = this.getHibernateTemplate().find(hql);
        return (list!=null&&list.size()>0)?(Vote)list.get(0):null;
    }
}
