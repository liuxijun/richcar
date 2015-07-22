package com.fortune.rms.business.publish.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.publish.dao.daoInterface.ChannelDaoInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.TreeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ChannelDaoAccess extends BaseDaoAccess<Channel, Long>
		implements
			ChannelDaoInterface {

	public ChannelDaoAccess() {
		super(Channel.class);
	}

    @SuppressWarnings("unchecked")
    public List<Channel> getChannelsByCspId(long cspId) {
        String hql = "from Channel c where c.cspId="+cspId+"";
        return this.getHibernateTemplate().find(hql);
    }

    @SuppressWarnings("unchecked")
    public List<Channel> getCspChannel(long cspId,int status) {
        String hql = "from Channel c where c.cspId="+cspId+" and c.status = "+status;
        return this.getHibernateTemplate().find(hql);
    }

    public List getSonChannelId(Long channelId){
        Session session =  getSession();
        try{
            String sql =  "select ID from CHANNEL where PARENT_ID ="+channelId;
            Query query =  session.createSQLQuery(sql);
            return query.list();
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
          session.close();
        }
        return  null;
    }

    @SuppressWarnings("unchecked")
    public boolean hasPrivilegeToChannelSX(long cspId,long channelId) {
        String hql = "from Channel c where c.cspId="+cspId+" and c.id in("+channelId+"";
        List<Channel> allParents = TreeUtils.getInstance().getParents(Channel.class,channelId);
        if(allParents==null){
            allParents = new ArrayList<Channel>();
        }
        //此行于20130326 17：35被刘喜军注释掉。考虑授权情况下，不应该包含子节点。否则会出现子节点被授权，父节点都会被默认授权的现象
        //allParents.addAll(TreeUtils.getInstance().getAllChildOf(Channel.class,channelId,0));
        for(Channel channel:allParents){
            hql+=","+channel.getId();
        }
        hql+=")";
        List<Channel> list = getHibernateTemplate().find(hql);
        return (list!=null&&list.size()>0);
    }

}
