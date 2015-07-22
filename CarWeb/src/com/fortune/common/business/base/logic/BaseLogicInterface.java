package com.fortune.common.business.base.logic;

import com.fortune.common.business.security.model.Admin;
import com.fortune.util.PageBean;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;

import java.io.Serializable;
import java.util.List;


public interface BaseLogicInterface<E> {
    /**
     *
     * @return  获得该实体的所有集合
     */
    public List<E> getAll();

    /**
     *
     * @param id
     * @return 获得对应的实体
     */
    public E get(Serializable id);

    /**
     *
      * @param obj
     * @return  返回包含主键的实体
     *
     */
    public E save(E obj);

    /**
     * 保存该实体，不返回主键，可用于事务(事务执行时不立刻将数据同步到数据库)
     * @param obj
     */
    public void persist(E obj);

    /**
     * 根据id删除该实体
     * @param id
     */
    public void remove(Serializable id);

    /**
     * 根据传入的对象删除该实体
     * @param obj
     */
    public void remove(E obj);

    /**
     * 修改该实体
     * @param obj
     */
    public void update(E obj);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param admin
     * @param hqlStr
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean, PageBean pageBean, Admin admin, String hqlStr);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param admin
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean, PageBean pageBean, Admin admin);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param sqlCondtion
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean, PageBean pageBean, String sqlCondtion);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean, PageBean pageBean);

    /**
     *
     * @param searchBean
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean);

    /**
     *
     * @param searchBean
     * @param autoLike
     * @return 返回该实体的集合
     */
    public List<E> search(E searchBean, boolean autoLike);

    /**
     *
     * @return 获得与数据库进行连接的session
     */
    public Session getSession();

    /**
     *
     * @param obj
     * @return 根据反射机制获得该实体的元数据
     */
    public ClassMetadata getClassMetadata(Object obj);

    /**
     *
     * @return 根据反射机制获得主键名称
     */
    public String getKeyPropertyName();

    /**
     *
     * @return 判断主键是否为Integer类型
     */
    public boolean isKeyPropertyInteger();

    /**
     *
     * @return 判断主键是否为Long类型
     */
    public boolean isKeyPropertyLong();


    /**
     *
     * @return 判断该事件是否是字符串类型
     */
    public boolean isKeyPropertyString();

    /**
     *
     * @param id
     * @return 判断该主键的id是否存在
     */
    public boolean exists(Serializable id);
}
