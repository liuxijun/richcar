package com.fortune.rms.business.user.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.user.dao.daoInterface.StbDaoInterface;
import com.fortune.rms.business.user.model.Stb;

import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: long
 * Date: 12-10-30
 * Time: ����10:06
 *    selectStb ����һ��ֵ�����жϸ�ֵ�ڻ��������Ƿ����
 */
@Repository
public class StbDaoAccess extends BaseDaoAccess<Stb,Long> implements StbDaoInterface {

    public StbDaoAccess() {
        super(Stb.class);
    }
    public boolean selectStb(String srialNo){
        String hql="from Stb where serialNo='"+srialNo+"'";
        return this.getHibernateTemplate().find(hql).size() > 0;
    }
}
