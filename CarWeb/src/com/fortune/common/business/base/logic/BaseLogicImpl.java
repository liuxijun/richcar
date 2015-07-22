package com.fortune.common.business.base.logic;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.util.PageBean;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseLogicImpl<E> implements BaseLogicInterface<E> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected BaseDaoInterface<E, Serializable> baseDaoInterface;
    public void setBaseDaoInterface(BaseDaoInterface<E, Serializable> baseDaoInterface) {
        this.baseDaoInterface = baseDaoInterface;
    }

    public List<E> getAll() {
        return baseDaoInterface.getAll();
    }

    public E get(Serializable id) {
        return baseDaoInterface.get(id);
    }

    public E save(E obj) {
        return baseDaoInterface.save(obj);
    }

    public void persist(E obj) {
        baseDaoInterface.persist(obj);
    }

    public void remove(Serializable id) {
        baseDaoInterface.remove(id);
    }

    public void remove(E obj){
        baseDaoInterface.remove(obj);
    }

    public void update(E object) {
        baseDaoInterface.update(object);
    }

    public List<E> search(E searchBean, PageBean pageBean, Admin admin,String hqlStr) {
        return baseDaoInterface.getObjects(searchBean,pageBean, admin,hqlStr);
    }

    public List<E> search(E searchBean, PageBean pageBean, Admin admin){
        return baseDaoInterface.getObjects(searchBean,pageBean, admin);
    }

    public List<E> search(E searchBean, PageBean pageBean, String sqlCondtion) {
        try {
            return baseDaoInterface.getObjects(searchBean, pageBean, true, sqlCondtion);
        } catch (Exception e) {
            logger.error("调用baseDaoInterface的getObjects(searchBean,pageBean)方法出错：" + e.getMessage());
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

    public List<E> search(E searchBean, PageBean pageBean) {
        return search(searchBean, pageBean, "");
    }

    public List<E> search(E searchBean) {
        try {
            return baseDaoInterface.getObjects(searchBean);
        } catch (Exception e) {
            logger.error("调用baseDaoInterface的getObjects(searchBean)方法出错：" + e.getMessage());
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

    public List<E> search(E searchBean, boolean autoLike) {
        try {
            return baseDaoInterface.getObjects(searchBean, autoLike);
        } catch (Exception e) {
            logger.error("调用baseDaoInterface的getObjects(searchBean)方法出错：" + e.getMessage());
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

    public Session getSession(){
        return baseDaoInterface.getHibernateSession();
    }

    public ClassMetadata getClassMetadata(Object obj){
        return baseDaoInterface.getClassMetadata(obj);
    }

    public String getKeyPropertyName(){
        return baseDaoInterface.getKeyPropertyName();
    }

    public boolean isKeyPropertyLong(){
        return baseDaoInterface.isKeyPropertyLong();
    }
    public boolean isKeyPropertyInteger(){
        return baseDaoInterface.isKeyPropertyInteger();
    }

    public boolean isKeyPropertyString(){
        return baseDaoInterface.isKeyPropertyString();
    }

    public boolean exists(Serializable id){
        return baseDaoInterface.exists(id);
    }

}
