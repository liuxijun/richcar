package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.UserHotSearchDaoInterface;
import com.fortune.rms.business.publish.model.UserHotSearch;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserHotSearchDaoAccess
        extends
        BaseDaoAccess<UserHotSearch,Long>
        implements
        UserHotSearchDaoInterface{

    public UserHotSearchDaoAccess() {
        super(UserHotSearch.class);
    }

    public List<UserHotSearch> getUserHotSearch(){
        Session session =  getSession();
        //搜索前 5条
//        String hql = "select t.type,t.createTime, t.rowid from UserHotSearch t where rownum < 6 order by id desc";
        try{
        Query query   =   session.createQuery("from UserHotSearch Order by searchCount desc");
        query.setMaxResults(5);  //查询出来的记录数
        List<UserHotSearch> list = query.list();
        return list;
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }
   //客户端修改的前五条
    public List<UserHotSearch> getUpdateHotSearch(){
        Session session =  getSession();
        //搜索前 5条
//        String hql = "select t.type,t.createTime, t.rowid from UserHotSearch t where rownum < 6 order by id desc";
        try{
        Query query   =   session.createQuery("from UserHotSearch where searchCountStatus=1 Order by updateCount desc");
        query.setMaxResults(5);  //查询出来的记录数
        List<UserHotSearch> list = query.list();
        return list;
        }catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
}
