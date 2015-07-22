package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.dao.daoInterface.ContentCspDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.ContentCsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("contentCspLogicInterface")
public class ContentCspLogicImpl extends BaseLogicImpl<ContentCsp>
		implements
			ContentCspLogicInterface {
	private ContentCspDaoInterface contentCspDaoInterface;
    private ContentChannelLogicInterface contentChannelLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    public static Map<Long,String> STATUS_CODES = new HashMap<Long, String>();
    static{
        STATUS_CODES.put(STATUS_WAIT_TO_ONLINE,"上线待审");
        STATUS_CODES.put(STATUS_WAIT_TO_OFFLINE,"下线待审");
        STATUS_CODES.put(STATUS_ONLINE_PUBLISHED,"上线并发布");
        STATUS_CODES.put(STATUS_ONLINE,"已审核但未发布");
        STATUS_CODES.put(STATUS_WAIT_AUDIT,"待审");
        STATUS_CODES.put(STATUS_OFFLINE,"下线");
        STATUS_CODES.put(STATUS_DELETE,"已经删除");
        STATUS_CODES.put(STATUS_RECYCLE,"回收站");
        STATUS_CODES.put(STATUS_APPLY_OFFLINE,"下线应用");
        STATUS_CODES.put(STATUS_HIDDEN,"隐藏状态");
        STATUS_CODES.put(STATUS_NEW,"新资源");

    }
    /**
	 * @param contentCspDaoInterface the contentCspDaoInterface to set
	 */
    @Autowired
	public void setContentCspDaoInterface(
			ContentCspDaoInterface contentCspDaoInterface) {
		this.contentCspDaoInterface = contentCspDaoInterface;
		baseDaoInterface = (BaseDaoInterface) this.contentCspDaoInterface;
	}

    @Autowired
    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }


    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public String getStatusString(Long status) {
        String result = STATUS_CODES.get(status);
        if(result==null){
            result = "[未知("+status+")]";
        }
        return result;
    }
    public boolean setStatus(long contentId,long cspId,long channelId,long status){
        return setStatus(contentId,cspId,channelId,status,-1, false);
    }

    /**
     * 设置contentCsp的状态
     * @param contentId  内容
     * @param cspId       sp
     * @param channelId  频道
     * @param status      状态
     * @param checkIt     检查是否存在，如果存在就不修改，如果不存在就保存
     * @return            是否存在
     */
    public boolean setStatus(long contentId,long cspId,long channelId,long status,long auditId,boolean checkIt){
        if(contentId<=0){
            return false;
        }
        ContentCsp bean = new ContentCsp();
        bean.setContentId(contentId);
        bean.setCspId(cspId);
        List<ContentCsp> hasData = search(bean);
        boolean result = true;
        if(hasData!=null&&hasData.size()>0){
            if(!checkIt){
                //如果是针对多个csp的统一调整
                if(cspId<=0){
                    for(ContentCsp cc:hasData){
                        cc.setStatus(status);
                        if(auditId>=0){
                            cc.setContentAuditId(auditId);
                        }
                        cc.setStatusTime(new Date());
                        //cc.setContentAuditId(0L);
                        save(cc);
                    }
                }else{//如果只是针对一个csp的，多余的数据要删掉
                    bean = hasData.get(0);
                    bean.setStatus(status);
                    bean.setContentAuditId(0L);
                    if(auditId>=0){
                        bean.setContentAuditId(auditId);
                    }
                    bean.setStatusTime( new Date());
                    save(bean);
                    for(int i=1,l=hasData.size();i<l;i++){
                        bean = hasData.get(i);
                        if(bean!=null){
                            remove(bean);
                        }
                    }
                }
            }
        }else{
            bean.setContentAuditId(auditId);
            bean.setStatus(status);
            bean.setStatusTime(new Date());
            save(bean);
            result = false;
        }

/*      //以下代码于2013、05、06注释被刘喜军。没有必要在sp下线的情况下，修改cp下线。这不科学
        Content content = contentLogicInterface.get(contentId);
        content.setStatus(status);
        contentLogicInterface.save(content);
*/
        if(channelId>0){
            //保存到contentChannel中
            result = result || contentChannelLogicInterface.setPublishStatus(contentId, channelId, status);
        }
        return result;
    }

    public boolean checkStatus(long contentId, long cspId, long channelId, long defaultStatus) {
        return setStatus(contentId,cspId,channelId,defaultStatus,-1,true);
    }

    public boolean publishContent(long contentId, long cspId, long channelId) {
        contentChannelLogicInterface.publishContent(contentId,channelId);
        return setStatus(contentId,cspId,channelId,ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
    }

    public boolean unPublishContent(long contentId, long cspId, long channelId) {
        //下面一句代码在2014年11月13日由刘喜军注释删除，因为下线影片时，可以不考虑删除频道栏目信息。
        //contentChannelLogicInterface.unPublishContent(contentId,channelId);
        //注释删除结束
        return setStatus(contentId,cspId,channelId,ContentCspLogicInterface.STATUS_OFFLINE);
    }
    public int deleteContentRecommend(String contentId,Long cspId){
        return  this.contentCspDaoInterface.deleteContentRecommend(contentId,cspId);
    }
    public int deleteContentServiceProduct(String contentId,Long cspId){
        return this.contentCspDaoInterface.deleteContentServiceProduct(contentId,cspId);
    }
//    public int deleteContentChannel(String contentId,Long cspId){
//        return this.contentCspDaoInterface.deleteContentChannel(contentId,cspId);
//    }
//    public int deleteAvailableChannelOfCsp(String contentId,Long cspId){
//        return this.contentCspDaoInterface.deleteAvailableChannelOfCsp(contentId,cspId);
//    }
//    public int deleteChannelChildOfCsp(String contentId,Long cspId){
//        return this.contentCspDaoInterface.deleteChannelChildOfCsp(contentId,cspId);
//    }
   public int deleteAllContentChannel(String contentId,Long cspId,String result){
       return this.contentCspDaoInterface.deleteAllContentChannel(contentId,cspId,result);
    }
}
