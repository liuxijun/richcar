package com.fortune.common.business.base.dao;

import com.fortune.common.business.security.model.Admin;
import com.fortune.util.*;
import org.hibernate.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.math.BigInteger;
import java.math.BigDecimal;

public class BaseDaoAccess<E, PK extends Serializable> extends
        HibernateDaoSupport implements BaseDaoInterface<E, PK> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Class<E> persistentClass;

    @Resource(name = "sessionFactory")
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    public BaseDaoAccess(Class<E> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @SuppressWarnings("unchecked")
    public List<E> getAll() {
        return super.getHibernateTemplate().loadAll(this.persistentClass);
    }

    @SuppressWarnings("unchecked")
    public E get(PK id) {
        E entity = (E) super.getHibernateTemplate().get(this.persistentClass,
                id);

        if (entity == null) {
            logger.warn("Uh oh, '" + this.persistentClass + "' object with id '"
                    + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public E save(E object) {
        return (E) super.getHibernateTemplate().merge(object);
    }

    @SuppressWarnings("unchecked")
    public void persist(E object) {
        super.getHibernateTemplate().save(object);
    }

    public void remove(PK id) {
        super.getHibernateTemplate().delete(this.get(id));
    }

    public void remove(E obj) {

        super.getHibernateTemplate().delete(obj);
    }

    public void update(E object) {
        super.getHibernateTemplate().update(object);
    }

    public List<E> getObjects(E searchBean, PageBean pageBean, Admin admin, String hqlStrEx) {
        try {
            String hqlStr = "";
            if (admin != null) {
/*
                Integer areaId=admin.getAreaId();
                if(areaId!=null&&areaId>0&&BeanUtils.hasProperty(searchBean,"areaId")){
                    TreeUtils tu = TreeUtils.getInstance();
                    hqlStr=""+areaId;
                    List areas = tu.getAllChildOf(Area.class,areaId,0);
                    for(Object obj:areas){
                        Area area = (Area)obj;
                        if(!hqlStr.equals("")){
                            hqlStr+=",";
                        }
                        hqlStr+=area.getId();
                    }
                    if(!"".equals(hqlStr)){
                        hqlStr = " o1.areaId in("+hqlStr+")";
                    }else{
                        hqlStr = " ";
                    }
                }
*/
            }
            if (hqlStrEx != null && (!"".equals(hqlStrEx.trim()))) {
                hqlStr += hqlStrEx + " ";
            }
            return getObjects(searchBean, pageBean, true, hqlStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

    public List<E> getObjects(E searchBean, PageBean pageBean, Admin admin) {
        return getObjects(searchBean, pageBean, admin, "");
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjects(E searchBeanValue, PageBean pageBean, boolean autoLike,
                              String sqlCondition) throws Exception {
        SearchCondition searchCondition;
        String keyPropertyName;
        if (searchBeanValue != null) {
            ClassMetadata metadata = super.getSessionFactory()
                    .getClassMetadata(searchBeanValue.getClass());
            keyPropertyName = metadata.getIdentifierPropertyName();
            searchCondition = HibernateUtils.getSearchCondition(metadata,
                    searchBeanValue, autoLike);
            String orderBy = pageBean.getOrderBy();
            if (orderBy == null || "".equals(orderBy.trim())) {
                pageBean.setOrderBy("o1." + keyPropertyName);
                //orderBy = " order by "+pageBean.getOrderBy();
                pageBean.setOrderDir("desc");
            }
        } else {
            searchCondition = new SearchCondition();
        }

        String hqlStr = searchCondition.getSqlStr();
        if (hqlStr == null || "".equals(hqlStr)) {
            String className = this.persistentClass.getSimpleName();
            if (searchBeanValue != null) {
                className = searchBeanValue.getClass().getSimpleName();
            }
            hqlStr = "from " + className;
        }
        if (sqlCondition != null && (!"".equals(sqlCondition.trim()))) {
            if (hqlStr.indexOf("where") > 0) {
                hqlStr += " and ";
            } else {
                hqlStr += " where ";
            }
            hqlStr += sqlCondition;
        }
        Object[] params = searchCondition.getObjectArrayParamValues();
        //Type[] types = searchCondition.getTypeArray();
        try {
            return getObjects(hqlStr, params, pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjects(E object, PageBean pageBean) throws Exception {
        return this.getObjects(object, pageBean, true);
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjects(E object) throws Exception {
        return this.getObjects(object, true);
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjects(E object, boolean autoLike) throws Exception {
        return getObjects(object, new PageBean(0,
                Integer.MAX_VALUE, null, null), autoLike);
    }


    public List<E> getObjects(E searchBeanValue, PageBean pageBean,
                              boolean autoLike) {
        try {
            return getObjects(searchBeanValue, pageBean, autoLike, null);
        } catch (Exception e) {
            return new ArrayList<E>();
        }
    }
    @SuppressWarnings("unchecked")
    public List<E> getObjects(String hqlStr, Object[] parameters,
                              PageBean pageBean) throws Exception {
        return (List<E>)search(hqlStr,parameters,pageBean);
    }
    @SuppressWarnings("unchecked")
    public List<Object> search(String hqlStr, Object[] parameters,
                              PageBean pageBean) throws Exception {
        List<Object> result;
        Session session = getSession();
        try {
            if(pageBean!=null){
                String countSql = "select count(*) ";
                if (hqlStr.toLowerCase().trim().indexOf("from") > 0) {
                    countSql = countSql + "from (" + hqlStr + ")";
                } else {
                    countSql = countSql + hqlStr;
                }
                Query countQuery = session.createQuery(countSql);
                countQuery = setQueryParameter(countQuery, parameters);
                //countQuery.setCacheable(true);
                pageBean.setRowCount(new Integer(countQuery.list().get(0).toString()));
            }

            if ((pageBean != null) && (pageBean.getOrderBy() != null)
                    && (!"".equals(pageBean.getOrderBy().trim()))) {
                String orderBy = pageBean.getOrderBy();
                orderBy = orderBy.replace('\'',' ').replace('(',' ').replace(';',' ').replace('"',' ')
                        .replace('=',' ').replace(')',' ');
                hqlStr += " order by " + orderBy;
                if (pageBean.getOrderDir() != null) {
                    if ("desc".equals(pageBean.getOrderDir().toLowerCase())) {
                        hqlStr += " desc ";
                    } else {
                        hqlStr += " asc ";
                    }
                }
            }
            Query query = session.createQuery(hqlStr);
            if(parameters!=null&&parameters.length>0){
                query = setQueryParameter(query, parameters);
            }
            //query.setCacheable(true);
            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            result = query.list();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return new ArrayList<Object>(0);
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjects(String hqlStr,
                              PageBean pageBean) throws Exception {
        return getObjects(hqlStr,null,pageBean);
    }

    @SuppressWarnings("unchecked")
    public List<E> getObjectsByIds(String ids) throws Exception {
        String className = this.persistentClass.getSimpleName();
        StringBuffer queryString = new StringBuffer("FROM " + className
                + " WHERE ");
        queryString.append(" id in (").append(ids).append(")");
        return super.getHibernateTemplate().find(queryString.toString());
    }

    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        E entity = (E) super.getHibernateTemplate().get(this.persistentClass,
                id);
        return entity != null;
    }

    @SuppressWarnings("unused")
    public int removeAllOfTable() {
        String hql = "DELETE ";
        hql += persistentClass.getName();
        return executeUpdate(hql);
    }


    @SuppressWarnings("unchecked")
    public int removeByObject(E object) throws Exception {

        List<E> list = this.getObjects(object);
        super.getHibernateTemplate().deleteAll(list);
        return list.size();

    }

    public Session getHibernateSession() {
        return super.getSessionFactory().openSession();
    }

    public ClassMetadata getClassMetadata(Object obj) {
        return super.getSessionFactory().getClassMetadata(obj.getClass());
    }

    public String getKeyPropertyName() {
        ClassMetadata metadata = super.getSessionFactory()
                .getClassMetadata(persistentClass);
        return metadata.getIdentifierPropertyName();
    }

    public boolean isKeyPropertyInteger() {
        return getKeyType() instanceof IntegerType;
    }

    public boolean isKeyPropertyLong() {
        return getKeyType() instanceof LongType;
    }

    public boolean isKeyPropertyString() {
        return getKeyType() instanceof StringType;
    }

    public int executeUpdate(String hql) {
        final String tempSql = hql;
        Object obj = this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {
                        Query query = session.createQuery(tempSql);
                        int result = query.executeUpdate();
                        return String.valueOf(result);
                    }
                });
        return Integer.parseInt(obj.toString());
    }


    public String removeRelatedData(String[] tablesName, String comment, String fieldName, Object value) {
        String result="";
        for(String tableName:tablesName){
            result+=removeRelatedData(tableName,comment,fieldName,value)+"\r\n";
        }
        return result;
    }

    public String removeRelatedData(String tableName,String comment, String fieldName, Object value) {
        return onRecordRemoved(comment,tableName,"delete from "+tableName+" where "+fieldName+"="+value);
    }

    public String onRecordRemoved(String comment,String tableName,String sql) {
        return "删除“"+comment+"”时处理关联数据表"+tableName+"时处理的数据条数为："+
                executeSQLUpdate(sql);
    }
    @SuppressWarnings("unused")
    public int executeSQLUpdate(String sql) {
        final String tempSql = sql;
        Object obj = this.getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException {
                        SQLQuery query = session.createSQLQuery(tempSql);
                        int result = query.executeUpdate();
                        return String.valueOf(result);
                    }
                });
        return Integer.parseInt(obj.toString());
    }

    public Query setQueryParameter(Query query, Object[] parameters) {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                Object param = parameters[i];
                Type type;
                if (param instanceof Integer) {
                    type = IntegerType.INSTANCE;
                } else if (param instanceof String) {
                    type = StringType.INSTANCE;
                } else if (param instanceof Date) {
                    type = DateType.INSTANCE;
                } else if (param instanceof Long) {
                    type = LongType.INSTANCE;
                } else if (param instanceof BigInteger) {
                    type = BigIntegerType.INSTANCE;
                } else if (param instanceof BigDecimal) {
                    type = BigDecimalType.INSTANCE;
                } else if (param instanceof Float) {
                    type = FloatType.INSTANCE;
                } else {
                    type = ObjectType.INSTANCE;
                }
                query.setParameter(i, parameters[i], type);
            }
        }
        return query;
    }

    public Type getKeyType() {
        ClassMetadata metadata = super.getSessionFactory()
                .getClassMetadata(persistentClass);
        String keyPropertyName = metadata.getIdentifierPropertyName();
        return metadata.getPropertyType(keyPropertyName);
    }

    public List<Object> sql(String sqlStr, Object[] parameters,
                            PageBean pageBean) throws Exception {
        List<Object> result;
        Session session = getSession();
        try {
            String countSql = "select count(*) ";
            if (sqlStr.toLowerCase().trim().indexOf("from") > 0) {
                countSql = countSql + "from (" + sqlStr + ") tableCountAlias";
            } else {
                countSql = countSql + sqlStr;
            }
            Query countQuery = session.createSQLQuery(countSql);
            countQuery = setQueryParameter(countQuery, parameters);
            //countQuery.setCacheable(true);
            pageBean.setRowCount(new Integer(countQuery.list().get(0).toString()));

            if ((pageBean != null) && (pageBean.getOrderBy() != null)
                    && (!"".equals(pageBean.getOrderBy().trim()))) {
                String orderBy = pageBean.getOrderBy();
                if(orderBy.startsWith("o1.")){
                    //如果是自动添加上的o1.，就查查看sql中有没有o1
                    if(!sqlStr.contains(" o1 ")){
                        orderBy = orderBy.substring(3);
                    }
                }
                orderBy = orderBy.replace('\'',' ').replace('(',' ').replace(';',' ').replace('"',' ')
                        .replace('=',' ').replace(')',' ');
                sqlStr += " order by " + orderBy;
                if (pageBean.getOrderDir() != null) {
                    if ("desc".equals(pageBean.getOrderDir().toLowerCase())) {
                        sqlStr += " desc ";
                    } else {
                        sqlStr += " asc ";
                    }
                }
            }
            Query query = session.createSQLQuery(sqlStr);
            if(parameters!=null&&parameters.length>0){
                query = setQueryParameter(query, parameters);
            }
            //query.setCacheable(true);
            if (pageBean != null) {
                query.setFirstResult(pageBean.getStartRow());
                query.setMaxResults(pageBean.getPageSize());
            }
            result = query.list();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return new ArrayList<Object>(0);
    }

    public List<Map<String, Object>> searchSQL(String hql,Object[] parameters,PageBean pageBean){
        hql = hql.trim();
        int p=hql.indexOf(" from ");
        List<String> fields = new ArrayList<String>();
        if(p>0){
            String selectFieldName = hql.substring(0,p);
            if(selectFieldName.toLowerCase().startsWith("select")){
                selectFieldName = selectFieldName.substring("select".length());
            }
            String[] fieldNames = selectFieldName.split(",");
            for(String field:fieldNames){
                field=field.trim();
                p = field.indexOf(" ");
                if(p>0){
                    field = field.substring(p);
                }
                field = field.trim();
                while (field.startsWith("\"")&&field.length()>1){
                    field = field.substring(1);
                }
                while (field.endsWith("\"")&&field.length()>1){
                    field = field.substring(0,field.length()-1);
                }
                fields.add(field);
            }
        }
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        try {
            List<Object> list = sql(hql,parameters,pageBean);
            int id=1;
            for(Object data:list){
                Object[] row = (Object[]) data;
                Map<String,Object> rowMap = new HashMap<String,Object>();
                boolean idFound = false;
                for(int i=0,l=fields.size();i<l;i++){
                    String fieldName = fields.get(i);
                    if("id".equals(fieldName)){
                        idFound = true;
                    }
                    rowMap.put(fieldName,row[i]);
                }
                if(!idFound){
                    rowMap.put("id",id);
                }
                result.add(rowMap);
                id++;
            }
        } catch (Exception e) {
            logger.error("无法获取数据："+e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }

}
