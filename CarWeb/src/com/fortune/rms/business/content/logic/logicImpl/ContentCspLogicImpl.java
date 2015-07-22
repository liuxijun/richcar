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
        STATUS_CODES.put(STATUS_WAIT_TO_ONLINE,"���ߴ���");
        STATUS_CODES.put(STATUS_WAIT_TO_OFFLINE,"���ߴ���");
        STATUS_CODES.put(STATUS_ONLINE_PUBLISHED,"���߲�����");
        STATUS_CODES.put(STATUS_ONLINE,"����˵�δ����");
        STATUS_CODES.put(STATUS_WAIT_AUDIT,"����");
        STATUS_CODES.put(STATUS_OFFLINE,"����");
        STATUS_CODES.put(STATUS_DELETE,"�Ѿ�ɾ��");
        STATUS_CODES.put(STATUS_RECYCLE,"����վ");
        STATUS_CODES.put(STATUS_APPLY_OFFLINE,"����Ӧ��");
        STATUS_CODES.put(STATUS_HIDDEN,"����״̬");
        STATUS_CODES.put(STATUS_NEW,"����Դ");

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
            result = "[δ֪("+status+")]";
        }
        return result;
    }
    public boolean setStatus(long contentId,long cspId,long channelId,long status){
        return setStatus(contentId,cspId,channelId,status,-1, false);
    }

    /**
     * ����contentCsp��״̬
     * @param contentId  ����
     * @param cspId       sp
     * @param channelId  Ƶ��
     * @param status      ״̬
     * @param checkIt     ����Ƿ���ڣ�������ھͲ��޸ģ���������ھͱ���
     * @return            �Ƿ����
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
                //�������Զ��csp��ͳһ����
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
                }else{//���ֻ�����һ��csp�ģ����������Ҫɾ��
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

/*      //���´�����2013��05��06ע�ͱ���ϲ����û�б�Ҫ��sp���ߵ�����£��޸�cp���ߡ��ⲻ��ѧ
        Content content = contentLogicInterface.get(contentId);
        content.setStatus(status);
        contentLogicInterface.save(content);
*/
        if(channelId>0){
            //���浽contentChannel��
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
        //����һ�������2014��11��13������ϲ��ע��ɾ������Ϊ����ӰƬʱ�����Բ�����ɾ��Ƶ����Ŀ��Ϣ��
        //contentChannelLogicInterface.unPublishContent(contentId,channelId);
        //ע��ɾ������
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
