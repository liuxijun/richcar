package com.fortune.rms.business.frontuser.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.frontuser.dao.daoInterface.FrontUserDaoInterface;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.frontuser.model.Organization;
import com.fortune.util.PageBean;
import com.fortune.util.TreeUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mlwang
 * Date: 2014-10-29
 * Time: 13:18:16
 * ǰ̨�û�
 */
@Repository
public class FrontUserDaoAccess  extends BaseDaoAccess<FrontUser, String>
		implements
        FrontUserDaoInterface {
    public FrontUserDaoAccess() {
        super(FrontUser.class);
    }

    /**
     * ��ѯǰ̨�û�
     * @param orgId         ��֯����Id
     * @param searchValue   ��ѯ�ִ�
     * @param pageBean      ��ҳ������
     * @return              ��������ǰ̨�û�
     */
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean){
        return searchUsers(orgId, searchValue, pageBean, -1L);
    }

    /**
     * ��ѯǰ̨�û�
     * @param orgId         ��֯����Id
     * @param searchValue   ��ѯ�ִ�
     * @param pageBean      ��ҳ������
     * @param status         �û�״̬
     * @return              ��������ǰ̨�û�
     */
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean, Long status){
        String hql = "from FrontUser u where 1=1 ";

        if(orgId > 0){
            hql += " and u.organizationId in (" + orgId;

            TreeUtils tu = TreeUtils.getInstance();
            List allChilds = tu.getAllChildOf(Organization.class, orgId, 0);
            if (allChilds != null) {
                for (Object allChild : allChilds) {
                    Organization child = (Organization) allChild;
                    hql += "," + child.getId();
                }
            }
            hql += ")";
        }

        if(searchValue!=null&&!"".equals(searchValue)){
            hql += " and ( (u.name like '%" + searchValue + "%') or (u.userId like '%" + searchValue + "%'))";
        }

        if(status >= 0){
            hql += " and u.status=" + status;
        }

        try {
            return getObjects(hql, pageBean);
        } catch (Exception e) {
            return new ArrayList<FrontUser>();
        }
    }

    public void setUserStatus(String userId, Long status){
        String hql = "update FrontUser u set u.status=" + status + " where u.userId='" +  userId + "'";
        executeUpdate(hql);
    }

    /**
     * ��ѯĳһ���û����͵��ۼ��û���
     * @param typeId  ����Id
     * @return  ǰ̨�û���
     */
    public int getUserCountByType(Long typeId){
        String hql = "from FrontUser u where u.status=" + FrontUser.USER_STATUS_NORMAL + " and u.typeId=" + typeId;
        try{
            List<FrontUser> l = getObjects(hql, new PageBean(0,
                Integer.MAX_VALUE, null, null));
            return (l != null)? l.size() : 0;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * �����û�״̬����ȡ����
     * @param status ״̬�������NULL�������
     * @return ����
     */
    public int getUserCountByStatus(Long status){
        String hql = "from FrontUser u";
        if(status != null){
            hql += " where u.status=" + status;
        }
        try{
            List<FrontUser> l = getObjects(hql, new PageBean(0,
                    Integer.MAX_VALUE, null, null));
            return (l != null)? l.size() : 0;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * ��֯�µ�����
     * @param orgId ��֯Id
     * @param includeChildren �Ƿ��������֯
     * @return ����
     */
    public int getUserCountByOrg(Long orgId, boolean includeChildren){
        String hql = "from FrontUser u where u.status=" + FrontUser.USER_STATUS_NORMAL +
                " and u.organizationId in (" + orgId;

        TreeUtils tu = TreeUtils.getInstance();
        List allChilds = tu.getAllChildOf(Organization.class, orgId, 0);
        if (allChilds != null) {
            for (Object allChild : allChilds) {
                Organization child = (Organization) allChild;
                hql += "," + child.getId();
            }
        }
        hql += ")";

        try{
            List<FrontUser> l = getObjects(hql, new PageBean(0,
                Integer.MAX_VALUE, null, null));
            return (l != null)? l.size() : 0;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkLoginName(FrontUser frontUser) {
        boolean isExisted = false;
        String hql = "from FrontUser where userId ='" + frontUser.getUserId() + "'";
        List<FrontUser> users = this.getHibernateTemplate().find(hql);
        if(users!=null && users.size()>0){
            isExisted = true;
        }

        return isExisted;
    }

}
