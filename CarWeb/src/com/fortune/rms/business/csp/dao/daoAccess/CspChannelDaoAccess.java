package com.fortune.rms.business.csp.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.csp.dao.daoInterface.CspChannelDaoInterface;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.TreeUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wang
 * Date: 13-1-29
 * Time: 下午3:12
 */
@Repository
public class CspChannelDaoAccess extends BaseDaoAccess<CspChannel, Long>
        implements
        CspChannelDaoInterface {

    public CspChannelDaoAccess() {
        super(CspChannel.class);
    }
    //得到csp相关的频道
    @SuppressWarnings("unchecked")
    public List<CspChannel> getCspBindChannels(long cspId,int type){
        String hqlStr="from CspChannel cc where cc.cspId="+cspId+"";
        if(type>0){
            hqlStr +=" and cc.channelId in (select id from Channel c where c.type=" +type+")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    @SuppressWarnings("unchecked")
    public List<CspChannel> getCspBindChannels(long cspId,int type,int status){
        String hqlStr="from CspChannel cc where cc.cspId="+cspId+"";
        if(type>0){
            hqlStr +=" and cc.channelId in (select id from Channel c where c.type=" +type+" and c.status = "+status+")";
        }
        return this.getHibernateTemplate().find(hqlStr);
    }

    @SuppressWarnings("unchecked")
     public List<Channel> getChannel(Long parentId){
         String hqlStr="from Channel c where c.parentId="+parentId+" and (c.status is null or c.status =1) order by c.grade";
//         String hqlStr="from Channel where parentId="+parentId;
         return this.getHibernateTemplate().find(hqlStr);
     }
    //删除相关csp的频道
    @SuppressWarnings("unchecked")
    public void deleteCspChannel(long cspId) {
        String hqlStr ="delete from CspChannel cc where cc.cspId="+cspId;
        executeUpdate(hqlStr);
    }

    @SuppressWarnings("unchecked")
    public boolean  hasPrivilegeToChannel(Long cspId,Long channelId){
        String hql = "from CspChannel cc where cc.cspId="+cspId+" and cc.channelId in("+channelId+"";
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
        List<CspChannel> list = getHibernateTemplate().find(hql);
        return (list!=null&&list.size()>0);
    }

}
