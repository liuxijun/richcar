/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-4-7
 * Time: 9:47:36
 * Hibernate操作的一些助手
 */
package com.fortune.util;

import org.hibernate.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class HibernateUtils extends HibernateDaoSupport {

    public HibernateUtils() {
    }

    public HibernateUtils(HttpServletRequest request) {
        try {
            WebApplicationContext springContext = WebApplicationContextUtils
                    .getWebApplicationContext(request.getSession().getServletContext());
            this.setSessionFactory((SessionFactory) springContext.getBean("sessionFactory"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HibernateUtils(ServletContext servletContext) {
        try {
            WebApplicationContext springContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            this.setSessionFactory((SessionFactory) springContext.getBean("sessionFactory"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasThisDAOProperty(ClassMetadata classMetadata, String propertyName) {
        try {
            if (classMetadata != null) {
                String[] propertyNames = classMetadata.getPropertyNames();
                for (String propName : propertyNames) {
                    if (propName.equals(propertyName)) {
                        return true;
                    }
                }
            }
        } catch (MappingException e) {
            return false;
        }
        return false;
    }

    public static SearchCondition getSearchCondition(ClassMetadata classMapping, Object searchBeanValues, boolean autoLike) {
        SearchCondition result = new SearchCondition();
        if (searchBeanValues != null) {
            result.setSqlStr("from " + searchBeanValues.getClass().getSimpleName() + " o1 ");
            String keyPropertyName = classMapping.getIdentifierPropertyName();
            Type keyType = classMapping.getPropertyType(keyPropertyName);
            if (keyType instanceof StringType) {
                String paramValue = (String) BeanUtils.getProperty(searchBeanValues, keyPropertyName);
                if (paramValue != null && (!"".equals(paramValue))) {
                    String value = paramValue;
                    if (autoLike) {
                        value = "%" + paramValue + "%";
                    }
                    result.appendAndSqlCondition("o1." + keyPropertyName + " like ?", value,
                            keyType);
                }
            }
            for (String propertyName : classMapping.getPropertyNames()) {
                Type paramType = classMapping.getPropertyType(propertyName);
                if (paramType instanceof SetType || paramType instanceof ListType || paramType instanceof MapType
                        || paramType instanceof BagType) {
                    continue;
                }

                Object pValue = BeanUtils.getProperty(searchBeanValues, propertyName);

                boolean shouldAddThisParam = false;
                String valuePos = "?";
                if (pValue != null) {
                    if (paramType instanceof LongType) {
                        pValue = new Long(pValue.toString());
                        if (((Long) pValue) >= 0) {
                            shouldAddThisParam = true;
                        }
//                        paramType = Hibernate.LONG;
                    } else if (paramType instanceof IntegerType) {
                        pValue = new Integer(pValue.toString());
                        if ((Integer) pValue >= 0) {
                            shouldAddThisParam = true;
                        }
//                        paramType = Hibernate.INTEGER;
                    } else if (paramType instanceof ByteType) {
                        pValue = new Integer(pValue.toString());
                        if ((Integer) pValue >= 0) {
                            shouldAddThisParam = true;
                        }
//                        paramType = Hibernate.BYTE;
                    } else if (paramType instanceof BigDecimalType) {
                        pValue = new BigDecimal(pValue.toString());
                        if (((BigDecimal) pValue).compareTo(BigDecimal.ZERO) >= 0) {
                            shouldAddThisParam = true;
                        }
                    } else if (paramType instanceof DateType || paramType instanceof TimestampType) {
                        shouldAddThisParam = true;
                    } else if (paramType instanceof StringType) {
                        if (!"".equals(((String) pValue).trim())) {
                            shouldAddThisParam = true;
                            if (autoLike) {
                                pValue = "%" + pValue + "%";
                            }
                        }
                    } else if (paramType instanceof BigIntegerType) {
                        pValue = new BigInteger(pValue.toString());
                        if (((BigInteger) pValue).compareTo(BigInteger.ZERO) >= 0) {
                            shouldAddThisParam = true;
                        }
/*                      }else if(paramType instanceof FloatType){
                        shouldAddThisParam = true;
*/
                    } else {
                        shouldAddThisParam = false;
                    }
                }
                if (shouldAddThisParam) {
                    String operator = " = ";
                    if (paramType instanceof StringType && autoLike) {
                        operator = " like ";
                    }
                    result.appendAndSqlCondition("o1." + propertyName + operator + valuePos, pValue, paramType);
                }
            }
        }
        return result;
    }


    /**
     * 得到查询的条数，格式为select * from User,或者是from User
     */
    public static int findCount(Session session, String hql) throws Exception {

        try {
            if (hql.indexOf("select") == 0 || hql.indexOf("SELECT") == 0) {
                hql = hql.substring(hql.indexOf("from"));
            }
            if (hql.indexOf("order") > -1) {
                hql = hql.substring(0, hql.indexOf("order"));
            }
            Query query = session.createQuery("select count(*) " + hql);
            int i = Integer.parseInt(query.iterate().next().toString());
            return i;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到查询的条数，格式为select * from User,或者是from User
     */
    public static int findCount(Session session, String hql, Object paramValues[], Type paramTypes[]) throws Exception {

        try {
            if (hql.indexOf("select") == 0 || hql.indexOf("SELECT") == 0) {
                hql = hql.substring(hql.indexOf(" from "));
            }
            if (hql.indexOf("order") > -1) {
                hql = hql.substring(0, hql.indexOf("order"));
            }
            Query query = session.createQuery("select count(*) " + hql);
            query.setParameters(paramValues, paramTypes);
            int i = Integer.parseInt(query.iterate().next().toString());
            return i;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到起始行，结束行的数据库记录
     */
    public static List findList(Session session, String hql, int startNo, int maxCount) throws Exception {
        try {
            Query query = session.createQuery(hql);
            query.setFirstResult(startNo);
            query.setMaxResults(maxCount);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到起始行，结束行的数据库记录
     */
    public static List findList(Session session, String hql, Object paramValues[], Type paramTypes[], int startNo, int maxCount) throws Exception {
        try {
            Query query = session.createQuery(hql);
            query.setParameters(paramValues, paramTypes);
            query.setFirstResult(startNo);
            query.setMaxResults(maxCount);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    /**
     * 得到起始行，结束行的数据库记录
     */
    public static List findSQLList(Session session, String hql, Object paramValues[], Type paramTypes[], int startNo, int maxCount) throws Exception {
        try {
            Query query = session.createSQLQuery(hql);
            query.setParameters(paramValues, paramTypes);
            query.setFirstResult(startNo);
            query.setMaxResults(maxCount);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到的查询的所有数据库记录
     */
    public static List findAll(Session session, String hql) throws Exception {
        try {
            Query query = session.createQuery(hql);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Object save(Object object) {
        return super.getHibernateTemplate().merge(object);
    }

    /**
     * 得到的查询的所有数据库记录
     */
    public List findAll(String hql) throws Exception {
        Session session = null;
        try {
            session = getSession();
            Query query = session.createQuery(hql);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到的查询的所有数据库记录
     */
    public List findAll(String hql, Object params[]) throws Exception {
        Session session = null;
        try {
            session = getSession();
            Query query = session.createQuery(hql);
            setQueryParameter(query, params);

            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    /**
     * 按主键得到某个表的一条记录
     */
    public int sqlFindCount(String sql) throws Exception {
        Session session = getSession();
        try {
            Query query_count = session.createSQLQuery(sql);
            String count = query_count.list().get(0).toString();
            return Integer.parseInt(count);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }

    /**
     * 得到的查询的所有数据库记录
     */
    public static List sqlFindAll(Session session, String sql) throws Exception {
        try {
            List list = session.createSQLQuery(sql).list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 得到某个对象的所有的数据库记录
     */
    public static List findAll(Session session, Object obj) throws Exception {
        try {
            String hql = "from " + obj.getClass().getName();
            Query query = session.createQuery(hql);
            List list = query.list();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 按主键得到某个表的一条记录
     */
    public static Object findByPK(Session session, Class cls, Serializable sl) throws Exception {
        try {
            Object obj = session.load(cls, sl);
            return obj;
        } catch (ObjectNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 按主键得到某个表的一条记录
     */
    public Object findByPK(Class cls, Serializable sl) throws Exception {
        return this.getHibernateTemplate().get(cls, sl);
    }

    /**
     * 删除记录
     */
    public static int deleteAll(Session session, String hql) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //int i = session.delete(hql);hibernate2方法
            Query query = session.createQuery(hql);
            int i = query.executeUpdate();
            tx.commit();
            return i;
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 删除一条记录记录
     */
    public static void delete(Session session, Object obj) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 更新数据库记录
     */
    public static void update(Session session, Object obj) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 更新数据库记录
     * HibernateMgr.executeUpdate("update Item i set i.isAudit=1 where i.id="+item.getId());
     */
    public int executeUpdate(String hql) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            session = getSession();
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            int i = query.executeUpdate();
            tx.commit();
            return i;
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 更新数据库记录
     * HibernateMgr.executeUpdate("update Item i set i.isAudit=1 where i.id="+item.getId());
     */
    public int executeUpdate(String hql, Object params[]) throws Exception {
        Session session = null;
        Transaction tx = null;
        try {
            session = getSession();
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            setQueryParameter(query, params);
            int i = query.executeUpdate();
            tx.commit();
            return i;
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    /**
     * 批量 执行,batchSize为每多少条数据一提交
     */
    public void executeBatch(String hql, List params, int batchSize) throws Exception {
        if (params == null || params.size() == 0) {
            return;
        }
        Session session = getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (int i = 0; i < params.size(); i++) {
                Object param[] = (Object[]) params.get(i);
                Query query = session.createQuery(hql);
                setQueryParameter(query, param);
                query.executeUpdate();
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                    //System.out.println(""+i+":"+(System.currentTimeMillis()-mi)/1000+"秒");
                    //mi = System.currentTimeMillis();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 更新数据库记录
     * HibernateMgr.executeUpdate("update Item i set i.isAudit=1 where i.id="+item.getId());
     */
    public static int executeUpdate(Session session, String hql) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            int i = query.executeUpdate();
            tx.commit();
            return i;
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 插入一条数据库的记录
     */
    public static Serializable insert(Session session, Object obj) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Serializable sl = session.save(obj);
            tx.commit();
            return sl;
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 批量插入数据库记录,objs为要插入的对象,batchSize为每多少条数据一提交
     */
    public static void insertBatch(Session session, List objs, int batchSize) throws Exception {
        if (objs == null || objs.size() == 0) {
            return;
        }

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (int i = 0; i < objs.size(); i++) {
                session.save(objs.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                    //System.out.println(""+i+":"+(System.currentTimeMillis()-mi)/1000+"秒");
                    //mi = System.currentTimeMillis();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    /**
     * 批量插入数据库记录,objs为要插入的对象,batchSize为每多少条数据一提交
     */
    public void insertBatch(List objs, int batchSize) throws Exception {
        if (objs == null || objs.size() == 0) {
            return;
        }
        Session session = getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            for (int i = 0; i < objs.size(); i++) {
                session.save(objs.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                    //System.out.println(""+i+":"+(System.currentTimeMillis()-mi)/1000+"秒");
                    //mi = System.currentTimeMillis();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 批量修改数据库记录,objs为要修改的对象,batchSize为每多少条数据一提交
     */
    public static void updateBatch(Session session, List objs, int batchSize) throws Exception {
        if (objs == null || objs.size() == 0) {
            return;
        }
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            for (int i = 0; i < objs.size(); i++) {
                session.update(objs.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
            tx.commit();
        } catch (Exception e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
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
}
