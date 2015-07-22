package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.log.dao.daoInterface.ClientLogDaoInterface;
import com.fortune.rms.business.log.model.ClientLog;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ClientLogDaoAccess extends BaseDaoAccess<ClientLog,Long>
        implements ClientLogDaoInterface{

    public ClientLogDaoAccess() {
        super(ClientLog.class);
    }

    @SuppressWarnings("unchecked")
    public List<ClientLog> checkPhoneCode(String phoneCode){
        String sql = "from ClientLog c where c.phoneCode = ?";
        return this.getHibernateTemplate().find(sql,new Object[]{phoneCode});
    }
}
