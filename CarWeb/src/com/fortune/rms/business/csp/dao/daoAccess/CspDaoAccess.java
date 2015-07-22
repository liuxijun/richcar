package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.csp.dao.daoInterface.CspDaoInterface;
import com.fortune.rms.business.csp.model.Csp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CspDaoAccess extends BaseDaoAccess<Csp, Long>
        implements
        CspDaoInterface {

    public CspDaoAccess() {
        super(Csp.class);
    }

    public List<Csp> getAllSp() {
        String hql = "from Csp where isSp=1";

        return this.getHibernateTemplate().find(hql);
    }

    public List<Csp> getAllCp() {
        String hql = "from Csp where isCp=1";
        return this.getHibernateTemplate().find(hql);
    }

    public boolean existSpId(String spId) {
        boolean exist = false;
        String hql = "from Csp where spId=" + spId + "";
        List<Csp> csps = this.getHibernateTemplate().find(hql);
        if (csps != null && csps.size() > 0) {
            exist = true;
        }
        return exist;
    }

    public Csp getCspBySpId(String spId) {
        String hql = "from Csp where spId=" + spId + "";
        List<Csp> csps = this.getHibernateTemplate().find(hql);
        if (csps != null && csps.size() > 0) {
            return csps.get(0);
        }
        return null;
    }

    public List<Csp> getCspByCpCode(String cpCode) {
        String hql = "from Csp where spId like ?";
        List<Csp> csps = this.getHibernateTemplate().find(hql, new Object[]{cpCode});

        return csps;
    }

    public List<Csp> getCpsOfStatus(int status) {
        String hqlStr = "from Csp c1 where c1.isCp =1 and c1.status=" + status + "";
        return this.getHibernateTemplate().find(hqlStr);
    }

    public Csp getCspIdByName(String name) {
        String hqlStr = "from Csp c1 where c1.name like ?";
        List<Csp> csps = this.getHibernateTemplate().find(hqlStr, new Object[]{name});
        if (csps != null && csps.size() == 1) {
            return csps.get(0);
        }
        return null;
    }

    public Csp getSpIdByContentId(long contentId) {
        String hqlStr = "from Csp c where c.id = (select ct.cspId from Content ct where ct.id =" + contentId + ")";
        List<Csp> list = this.getHibernateTemplate().find(hqlStr);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    public Csp getSpIdByProductId(long productId) {
        String hqlStr = "from Csp c where c.id = (select sp.cspId from ServiceProduct sp where sp.productId =" + productId + ")";
        List<Csp> list = this.getHibernateTemplate().find(hqlStr);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    public void remove(Csp csp) {
        if (csp == null || csp.getId() <= 0) {
            return;
        }
        /**
         * 删除时，需要把关联的数据也同步删除掉
         */
        long cspId = csp.getId();
        String cspName = csp.getName();
        logger.debug(onRecordRemoved(cspName,"CONTENT_CHANNEL","delete from CONTENT_CHANNEL " +
                " where CHANNEL_ID in (select ID from CHANNEL where CSP_ID=" +cspId+") or " +
                " CHANNEL_ID in (select CHANNEL_ID from CSP_CHANNEL where CSP_ID="+cspId+")"));
        logger.debug(onRecordRemoved(cspName,"CSP_CHANNEL","delete from CSP_CHANNEL  where CSP_ID="+cspId+" or" +
                " CHANNEL_ID in (select ID from CHANNEL where CSP_ID=" +cspId+")"));
        logger.debug(onRecordRemoved(cspName,"CONTENT","update CONTENT set CSP_ID=-" + cspId + ",STATUS="
                + ContentLogicInterface.STATUS_DELETE + " where CSP_ID=" + cspId));
        logger.debug(removeRelatedData(new String[]{"USER_REVIEW_KEYWORD","USER_SCORING","CONTENT_NOTICE",
                "CONTENT_ZT_LOG","CSP_AUDITOR","CSP_CSP","CONTENT_RECOMMEND","SERVICE_PRODUCT","RECOMMEND",
                "CSP_DEVICE","CSP_MODULE","CSP_PRODUCT","CSP_TEMPLATE","ADMIN_CSP","ADMIN_ROLE","CHANNEL",
                //"CONTENT","CSP_CHANNEL","CONTENT_CSP",//这三个单独处理
                "RELATED","RELATED_PROPERTY_CONTENT","TEMPLATE","USER_FAVORITES","USER_RECOMMAND",
                "USER_REVIEW"},cspName,"CSP_ID",cspId));
        super.remove(csp);
    }
}
