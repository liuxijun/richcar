package com.fortune.rms.business.content.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.dao.daoInterface.ContentCspDaoInterface;
import com.fortune.rms.business.content.model.ContentCsp;
import org.springframework.stereotype.Repository;

@Repository
public class ContentCspDaoAccess extends BaseDaoAccess<ContentCsp, Long>
		implements
			ContentCspDaoInterface {

	public ContentCspDaoAccess() {
		super(ContentCsp.class);
	}
    //删除常规推荐
    public int deleteContentRecommend(String contentId,Long cspId){
        String hql ="delete from ContentRecommend cr where cr.contentId in (" + contentId+ ") and cr.recommendId in (select r.id from Recommend r where r.cspId=" + cspId + ")";
        return executeUpdate(hql);
    }
    //删除产品
    public int deleteContentServiceProduct(String contentId,Long cspId){
        String hql="delete from ContentServiceProduct csp where csp.contentId in (" + contentId + ") and csp.serviceProductId in (select sp.id from ServiceProduct sp where sp.cspId in(" + cspId + ",543737280))";//增加联通产品删除，联通CSPID为543737280
        return executeUpdate(hql);
    }
//    public int deleteContentChannel(String contentId,Long cspId){
//        String hql="delete from ContentChannel cc where cc.contentId in (" + contentId + ") and " +  "cc.channelId in (select c.id from Channel c where c.cspId=" + cspId+ ")";
//        return executeUpdate(hql);
//    }
//    public int deleteAvailableChannelOfCsp(String contentId,Long cspId){
//        String hql="delete from ContentChannel cc where cc.contentId in (" + contentId + ") and " +  "cc.channelId in (select cc1.channelId from CspChannel cc1 where cc1.cspId=" + cspId+ ")";
//        return executeUpdate(hql);
//    }
//    public int deleteChannelChildOfCsp(String contentId,Long cspId){
//        String hql="delete from ContentChannel cc where cc.contentId in (" + contentId +") and" + "cc.channelId in (select id from Channel where parentId in (select cc1.channelId from CspChannel cc1 where cc1.csp_Id="+cspId+"))";
//       return executeUpdate(hql);
//    }
    public int deleteAllContentChannel(String contentId,Long cspId,String result){
        String hql="delete from ContentChannel cc where cc.contentId in (" + contentId + ") and "+"cc.channelId in (" + result + ")";
        return executeUpdate(hql);
    }
}