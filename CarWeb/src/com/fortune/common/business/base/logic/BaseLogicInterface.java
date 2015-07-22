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
     * @return  ��ø�ʵ������м���
     */
    public List<E> getAll();

    /**
     *
     * @param id
     * @return ��ö�Ӧ��ʵ��
     */
    public E get(Serializable id);

    /**
     *
      * @param obj
     * @return  ���ذ���������ʵ��
     *
     */
    public E save(E obj);

    /**
     * �����ʵ�壬����������������������(����ִ��ʱ�����̽�����ͬ�������ݿ�)
     * @param obj
     */
    public void persist(E obj);

    /**
     * ����idɾ����ʵ��
     * @param id
     */
    public void remove(Serializable id);

    /**
     * ���ݴ���Ķ���ɾ����ʵ��
     * @param obj
     */
    public void remove(E obj);

    /**
     * �޸ĸ�ʵ��
     * @param obj
     */
    public void update(E obj);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param admin
     * @param hqlStr
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean, PageBean pageBean, Admin admin, String hqlStr);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param admin
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean, PageBean pageBean, Admin admin);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @param sqlCondtion
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean, PageBean pageBean, String sqlCondtion);

    /**
     *
     * @param searchBean
     * @param pageBean
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean, PageBean pageBean);

    /**
     *
     * @param searchBean
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean);

    /**
     *
     * @param searchBean
     * @param autoLike
     * @return ���ظ�ʵ��ļ���
     */
    public List<E> search(E searchBean, boolean autoLike);

    /**
     *
     * @return ��������ݿ�������ӵ�session
     */
    public Session getSession();

    /**
     *
     * @param obj
     * @return ���ݷ�����ƻ�ø�ʵ���Ԫ����
     */
    public ClassMetadata getClassMetadata(Object obj);

    /**
     *
     * @return ���ݷ�����ƻ����������
     */
    public String getKeyPropertyName();

    /**
     *
     * @return �ж������Ƿ�ΪInteger����
     */
    public boolean isKeyPropertyInteger();

    /**
     *
     * @return �ж������Ƿ�ΪLong����
     */
    public boolean isKeyPropertyLong();


    /**
     *
     * @return �жϸ��¼��Ƿ����ַ�������
     */
    public boolean isKeyPropertyString();

    /**
     *
     * @param id
     * @return �жϸ�������id�Ƿ����
     */
    public boolean exists(Serializable id);
}
