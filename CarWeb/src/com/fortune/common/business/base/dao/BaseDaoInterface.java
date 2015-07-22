package com.fortune.common.business.base.dao;

import com.fortune.util.PageBean;
import com.fortune.common.business.security.model.Admin;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDaoInterface<E,PK extends Serializable> {

    public List<E> getAll();

    public E get(PK id);

    public E save(E obj);

    public void persist(E obj);

    public void remove(PK id);

    public void remove(E obj);

    public void update(E obj);

    public List<E> getObjects(E searchBean, PageBean pageBean, Admin admin, String hqlEx);

    public List<E> getObjects(E searchBean, PageBean pageBean, Admin admin);

    public List<E> getObjects(E object, PageBean pageBean, boolean autoLike, String sqlCondition) throws Exception ;

    public List<E> getObjects(E object, PageBean pageBean) throws Exception ;

    public List<E> getObjects(E object) throws Exception ;

    public List<E> getObjects(E object, boolean autoLike) throws Exception ;

    public List<E> getObjects(E object, PageBean pageBean, boolean autoLike) throws Exception ;

    public List<E> getObjects(String hqlStr, Object[] parameters, PageBean pageBean) throws Exception;

    public List<E> getObjects(String hqlStr, PageBean pageBean) throws Exception;

    public List<E> getObjectsByIds(String ids) throws Exception;

    public List<Object> search(String hqlStr, Object[] parameters,
                               PageBean pageBean) throws Exception;
    public boolean exists(PK id);

    public int removeAllOfTable();

    public int removeByObject(E object) throws Exception;

    public Session getHibernateSession();

    public ClassMetadata getClassMetadata(Object obj);

    public String getKeyPropertyName();

    public boolean isKeyPropertyInteger();

    public boolean isKeyPropertyLong();

    public boolean isKeyPropertyString();

    public int executeUpdate(String sql);
    public String removeRelatedData(String[] tablesName, String comment, String fieldName, Object value);
    public String removeRelatedData(String tableName, String comment, String fieldName, Object value);
    public String onRecordRemoved(String comment, String tableName, String sql);
    public List<Map<String, Object>> searchSQL(String hql, Object[] parameters, PageBean pageBean);
    public List<Object> sql(String sqlStr, Object[] parameters, PageBean pageBean) throws Exception;
}
