package com.fortune.rms.business.product.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.product.dao.daoInterface.ServiceProductChannelDaoInterface;
import com.fortune.rms.business.product.model.ServiceProductChannel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceProductChannelDaoAccess extends
        BaseDaoAccess<ServiceProductChannel, Long>
implements
        ServiceProductChannelDaoInterface {


    public ServiceProductChannelDaoAccess() {
        super(ServiceProductChannel.class);
    }

    public List<ServiceProductChannel> getSelectedChannel(long serviceProductId) {
        String hql = "from ServiceProductChannel spc where spc.serviceProductId ="+serviceProductId;
        return this.getHibernateTemplate().find(hql);
    }


    public void deleteByServiceProductIds(String serviceProductIds) {
        String hql = "delete from ServiceProductChannel spc where spc.serviceProductId in (?)";
        Session session = getSession();
        try {
            Query query = session.createQuery(hql);
            query.setString(0,serviceProductIds);
            query.executeUpdate();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
