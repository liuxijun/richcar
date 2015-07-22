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
 * 前台用户
 */
@Repository
public class FrontUserDaoAccess  extends BaseDaoAccess<FrontUser, String>
		implements
        FrontUserDaoInterface {
    public FrontUserDaoAccess() {
        super(FrontUser.class);
    }

    /**
     * 查询前台用户
     * @param orgId         组织机构Id
     * @param searchValue   查询字串
     * @param pageBean      分页和排序
     * @return              符合条件前台用户
     */
    public List<FrontUser> searchUsers(Long orgId, String searchValue, PageBean pageBean){
        return searchUsers(orgId, searchValue, pageBean, -1L);
    }

    /**
     * 查询前台用户
     * @param orgId         组织机构Id
     * @param searchValue   查询字串
     * @param pageBean      分页和排序
     * @param status         用户状态
     * @return              符合条件前台用户
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
     * 查询某一个用户类型的累计用户数
     * @param typeId  类型Id
     * @return  前台用户数
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
     * 根据用户状态，获取数量
     * @param status 状态，如果是NULL，则忽略
     * @return 数量
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
     * 组织下的人数
     * @param orgId 组织Id
     * @param includeChildren 是否包含子组织
     * @return 人数
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
