package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentChannelDaoInterface;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ContentChannelDaoAccess
		extends
			BaseDaoAccess<ContentChannel, Long>
		implements
			ContentChannelDaoInterface {

	public ContentChannelDaoAccess() {
		super(ContentChannel.class);
	}

    public List<ContentChannel> list(Long cspId,Long channelId, String contentName,
                                     String directors, String actors,
                                     List<ContentProperty> searchValues,PageBean pageBean) {
        String hql = "from ContentChannel cc";
        String conditionHql = "";
        if(channelId!=null&&channelId>0){
            conditionHql += " and cc.channelId="+channelId;
        }
        List<String> parameters = new ArrayList<String>();
        if(contentName!=null||actors!=null||directors!=null||(cspId!=null&&cspId>0)){
            conditionHql += " and cc.contentId in( select c.id from Content c " +
                    "where 1=1";
            if(cspId!=null&&cspId>0){
                conditionHql += " and c.cspId="+cspId;
            }
            if(contentName!=null&&!"".equals(contentName.trim())){
                conditionHql += " and c.name like ?";
                parameters.add("%"+contentName+"%");
            }
            if(directors!=null&&!"".equals(directors.trim())){
                conditionHql += " and c.directors like ?";
                parameters.add("%"+directors+"%");
            }
            if(actors!=null&&!"".equals(actors.trim())){
                conditionHql += " and c.actors like ?";
                parameters.add("%"+actors+"%");
            }
            conditionHql += ")";
        }else{

        }
        if(!"".equals(conditionHql)){
            hql+=" where "+conditionHql.substring(5);
        }
        try {
            return this.getObjects(hql,parameters.toArray(),pageBean);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<ContentChannel>();
    }

    public List<Channel> getChannelsByContentId(Long contentId) {
        Session session = null;
        List<Channel> channels = null;
        try{
            session = getSession();
            String hql = "from Channel c where c.id in (select cc.channelId from ContentChannel cc where cc.contentId="+contentId+")";
            Query query = session.createQuery(hql);
            channels = query.list();
            
        }catch(Exception e){
           logger.error(e.getMessage());
        }finally {
            if(session!=null){
                session.close();
            }
        }
       
            return channels;
    }
}
