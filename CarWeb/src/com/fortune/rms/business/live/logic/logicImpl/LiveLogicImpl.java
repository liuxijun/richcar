package com.fortune.rms.business.live.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.content.logic.logicInterface.ContentCspLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.*;
import com.fortune.rms.business.live.dao.daoInterface.LiveChannelDaoInterface;
import com.fortune.rms.business.live.dao.daoInterface.LiveDaoInterface;
import com.fortune.rms.business.live.logic.logicInterface.LiveLogicInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.LiveChannel;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.system.dao.daoInterface.DeviceDaoInterface;
import com.fortune.rms.business.system.logic.logicImpl.DeviceLogicImpl;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * Created by ����· on 2015/2/26.
 *
 */
@Service("liveLogicInterface")
public class LiveLogicImpl extends BaseLogicImpl<Live>
        implements
        LiveLogicInterface {
    public LiveLogicImpl() {
    }

    private LiveDaoInterface liveDaoInterface;
    private LiveChannelDaoInterface liveChannelDaoInterface;
    //private LiveLogDaoInterface liveLogDaoInterface;
    private DeviceDaoInterface deviceDaoInterface;
    //private ContentDaoInterface contentDaoInterface;
    //private ContentChannelLogicInterface contentChannelLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    //private AdminLogicInterface adminLogicInterface;
    private SystemLogLogicInterface systemLogLogicInterface;
    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    @Autowired
    public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
        this.systemLogLogicInterface = systemLogLogicInterface;
    }

    private ContentLogicInterface contentLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;

/*
    @Autowired
    public void setAdminLogicInterface(AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
    }

    @Autowired
    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }

*/
    @Autowired
    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    /*
    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }
*/

    @Autowired
    public void setDeviceDaoInterface(DeviceDaoInterface deviceDaoInterface) {
        this.deviceDaoInterface = deviceDaoInterface;
    }

    @Autowired
    public void setLiveDaoInterface(LiveDaoInterface liveDaoInterface) {
        this.liveDaoInterface = liveDaoInterface;
        this.baseDaoInterface = (BaseDaoInterface)liveDaoInterface;
    }
    @Autowired
    public void setLiveChannelDaoInterface(LiveChannelDaoInterface liveChannelDaoInterface) {
        this.liveChannelDaoInterface = liveChannelDaoInterface;
    }

/*
    @Autowired
    public void setLiveLogDaoInterface(LiveLogDaoInterface liveLogDaoInterface) {
        this.liveLogDaoInterface = liveLogDaoInterface;
    }

*/
    /**
     * ��ѯֱ��
     * @param channels      ��ѯ��Χ������Ա���Թ����Ƶ���б����ŷָ���Ϊ��ʱ����
     * @param searchValue   ��ѯ�ؼ��ʣ�Ϊ��ʱ����
     * @param pageBean       ��ҳ��������Ϣ
     * @return  ֱ���б�
     */
    public List<Live> searchLive(String channels, String searchValue, PageBean pageBean){
        return  liveDaoInterface.searchLive(channels, searchValue, pageBean);
    }

    /**
     * ��ѯ¼��
     * @param channels          ��ѯ��Χ������Ա���Թ����Ƶ���б����ŷָ���Ϊ��ʱ����
     * @param searchValue       ��ѯ�ؼ��ʣ�Ϊ��ʱ����
     * @param pageBean          ��ҳ��������Ϣ
     * @return live�����б�
     */
    public List<Live> searchRecord(String channels, String searchValue, PageBean pageBean){
        return liveDaoInterface.searchRecord(channels, searchValue, pageBean);
    }


    /**
     * ����ֱ����Ϣ���½����޸�
     * @param live      ֱ������
     * @param channelIdList ֱ����ĿId����
     * @param channelName ֱ��Ƶ����
     * @param serverIp ֱ����������ַ
     * @return  ����ɹ����Live����
     */
    public Live saveLive(Live live, List<Long> channelIdList,String channelName, String serverIp){
        if(live == null) return null;

        Live oldLive;
        if(live.getId() > 0){
            oldLive = get(live.getId());
            if( oldLive != null ){
                live.setCreateTime(oldLive.getCreateTime());
                live.setStatus(oldLive.getStatus());
                live.setCreator(oldLive.getCreator());
                live.setChannel(oldLive.getChannel());
                live.setCspId(oldLive.getCspId());
                live.setModuleId(oldLive.getModuleId());
                // ����ϵĺ��������ã�ɾ��
                if(oldLive.getPoster() != null && !oldLive.getPoster().equals(live.getPoster())){
                    logger.debug("�������ˣ��ϵĺ����Ƿ�ɾ���أ�"+oldLive.getPoster());
                }
            }
        }else{
            live.setCreateTime(new Date());
            live.setStatus(Live.LIVE_STATUS_NORMAL);
        }

        // ����serverIpƥ��server_id
        if(serverIp != null && !serverIp.isEmpty()) {
            Device device = deviceDaoInterface.getDeviceByIp(serverIp,DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE);
            if (device != null) {
                live.setServerId(device.getId());
            }
        }else{
            live.setServerId(-1L);
        }
        live.setChannel(channelName);
        Long moduleId = live.getModuleId();
        if(moduleId==null||moduleId<=0){
            moduleId = AppConfigurator.getInstance().getLongConfig("system.live.defaultModuleId",503754003L);
            live.setModuleId(moduleId);
        }

        live = save(live);

        // ���ԭ�е���Ŀ����
        liveChannelDaoInterface.removeByLive(live);
        if( live != null && channelIdList != null ){
            for (Long aChannelIdList : channelIdList) {
                LiveChannel liveChannel = new LiveChannel();
                liveChannel.setLiveId(live.getId());
                liveChannel.setChannelId(aChannelIdList);
                liveChannelDaoInterface.save(liveChannel);
            }
        }
        return live;
    }

    /**
     * ɾ��ֱ��
     * @param live ֱ������ֻʹ�����е�id
     * @return �ɹ�ɾ��������ֱ��������id,ʧ�ܷ���-1
     */
    public long removeLive(Live live){
        if(live == null) return ERROR_CODE_LIVE_NOT_EXISTS;
        /*��ɾ��ֱ������Ŀ����*/
        Live l = null;
        try {
            l = get(live.getId());
        } catch (Exception e) {
            logger.error("�޷���ȡlive��������Ϣ�ǣ�"+e.getMessage());
        }
        if( l == null) return ERROR_CODE_LIVE_NOT_EXISTS;
        if(l.getStatus()==Live.LIVE_STATUS_WORKING){
            return ERROR_CODE_LIVE_HAS_STARTED;
        }
        liveChannelDaoInterface.removeByLive(live);
        remove(live.getId());
        return l.getTaskId();
    }

    /**
     * װ��ֱ����Ϣ������������Ϣ��
     * @param id ֱ��Id
     * @return ֱ�������null
     */
    public Live loadLive(Long id){
        if(id <=0) return null;

        Live l = get(id);
        if(l != null) {
            l.setChannelList(liveChannelDaoInterface.getListByLive(l));
        }
        return l;
    }

    /**
     * ��ȡ��������������ƣ�����Ƿ�����е�ֱ���ظ�
     * @return �ַ���
     */
    public String getRandomStreamName(){
        boolean found = false;
        String streamName;
        do{
            streamName = RandomStringUtils.randomAlphanumeric(8);
            if(liveDaoInterface.getLiveByStreamName( streamName) == null){
                found = true;
            }
        }while (!found);
        return streamName;
    }

    /**
     * ����taskId��Ӧ��ֱ��
     * @param taskId ����Id
     * @return �������
     * -1 ����Id��Ч
     * -2 û���ҵ���Ӧ��ֱ��
     * >0 ������ֱ������
     */
    public synchronized int startLive(Long taskId,Long liveId){
        String key = "startLive:taskId="+taskId+",liveId="+liveId;
        int resultCode = 0;
        try {
            ThreadUtils.getInstance().acquire(key);
            String logs = "ֱ��������������Ϣ��taskId="+taskId+",liveId="+liveId+",";
            if( taskId < 0) return ERROR_CODE_INVALID_TASK_ID;
            long[] statusArray = {Live.LIVE_STATUS_NORMAL, Live.LIVE_STATUS_EXPIRED, Live.LIVE_STATUS_WORKING, Live.LIVE_STATUS_STARTING};
            List<Live> liveList = new ArrayList<Live>();
            if(liveId!=null&&liveId>0){
                liveList.add(get(liveId));
            }else{
                liveList.addAll(liveDaoInterface.getLiveByTaskId(taskId, statusArray));
            }
            if(liveList.size() == 0) return ERROR_CODE_LIVE_NOT_EXISTS;
            boolean hasPublished = false;
            for(Live l : liveList){
                if(l == null) continue;
                List<LiveChannel> channelList = liveChannelDaoInterface.getListByLive(l);
                if(l.getStatus()==Live.LIVE_STATUS_WORKING){
                    logs+="ֱ������������״̬�������ǰ�Ƿ����Ѿ��������ߵ����ݣ�";
                    //������Ѿ�����״̬��ֱ���������ҿ��Ƿ��з��������ݣ����û�У����ٷ���һ�£�����У��ͷŹ�
                    List<Content> contents = contentLogicInterface.getContent(null,getLiveIdForContent(l),null);
                    if(contents!=null&&contents.size()>0){
                        for(Content content:contents){
                            long id = content.getId();
                            //ֻ�����ߵ�
                            if(ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())){
                                ContentCsp cc = new ContentCsp();
                                cc.setContentId(id);
                                cc.setCspId(l.getCspId());
                                cc.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                List<ContentCsp> data = contentCspLogicInterface.search(cc);
                                if(data.size()>0){
                                    //���ż��Ƶ���������
                                    for(LiveChannel c : channelList) {
                                        contentCspLogicInterface.publishContent(content.getId(), l.getCspId(), c.getChannelId());
                                    }
                                    logs+="��������ǰ�������ߵ�ý�壺"+content.getName()+",�ظ�һ�����߲�����";
                                    hasPublished = true;
                                    break;
                                }else{
                                    logs+="δ�ҵ�SP���ߵ����ݣ�";
                                }
                            }else{
                                logs+="ý��δ���ߣ�"+content.getName()+",";
                            }
                        }
                    }else{
                        logs+="δ������ǰ�������ߵ����ݣ�";
                    }
                }
                if(hasPublished){
                    continue;
                }
                // ����Ϊ����ֱ��
                l.setStatus( Live.LIVE_STATUS_WORKING );
                l=save(l);

                // �����ֱ��
                if(l.getIsLive() == 0){
                    logs+=l.getTitle()+"����Ҫ�������ߣ�����ֻ��¼�ƣ�";
                    continue;
                }

                // ��ֱ����Ϣ����Content��
                Content content = new Content();
                content.setName(regTitle(l.getTitle(), new Date()));
                logs+="���·������ߣ�����Ϊ��"+content.getName()+"��";
                content.setActors(l.getActor());
                content.setIntro(l.getIntro());
                content.setPost1Url(l.getPoster());
                content.setCreateTime(new Date());

                //content.setCspId();
                content.setDeviceId(l.getServerId());
                content.setUserTypes(l.getUserTypes());
                content.setCreatorAdminId(l.getCreator());
                content.setCspId(l.getCspId());
                Long moduleId = l.getModuleId();
                if(moduleId==null||moduleId<=0){
                    moduleId = AppConfigurator.getInstance().getLongConfig("system.live.defaultModuleId",503754003L);
                }
                content.setModuleId(moduleId);
                // ��ȡ����Ա��cspId
        /*    if( l.getCreator() != null) {
                Admin admin = adminLogicInterface.get(l.getCreator().intValue());
                if( admin != null ){
                    content.setCspId(admin.getCspId().longValue());
                }
            }*/
                // ��ֱ��id��¼��Property1��
                content.setContentId(getLiveIdForContent(l));
                //��������Ϊֱ��
                //content.setType()
                // ״̬����Ϊ����
                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                logger.debug("׼������content��id=" +content.getId()+
                        ",name="+content.getName()+",cspId="+content.getCspId());
                content = contentLogicInterface.save(content);
                logger.debug("�������content��id=" +content.getId()+
                        ",name="+content.getName()+",cspId="+content.getCspId());
                // ������Ŀ����
                int channelCount = 0;
                for(LiveChannel c : channelList) {
                    //contentChannelLogicInterface.save(new ContentChannel(-1, content.getId(), c.getChannelId()));
                    Channel channel;
                    Long channelId = c.getChannelId();
                    if(channelId!=null&&channelId>0){
                        channel = (Channel)TreeUtils.getInstance().getObject(Channel.class,channelId);
                        if(channel!= null){
                            contentCspLogicInterface.publishContent(content.getId(), l.getCspId(), channelId);
                            logs+="�������ߵ�Ƶ����"+channel.getName()+",";
                            channelCount++;
                        }else{
                            logs+="���޷�������Ƶ������Ϊû���ҵ����Ƶ����"+channelId+",";
                        }
                    }else{
                        logs+="û�з������κ�Ƶ������Ϊ�趨��Ƶ��ID����ȷ��channelId="+channelId+",";
                    }
                }
                if(channelCount>0){
                    logs+="�ۼƷ�����Ƶ��"+channelCount+"����";
                }else{
                    logs+="δ�������κ�Ƶ����";
                }
                ContentProperty clip = new ContentProperty();
                Property property = contentLogicInterface.getPropertyByCode("Media_Url_Source");
                clip.setPropertyId(property.getId());
                clip.setName("ֱ��Դ���ӣ�" + property.getName());
                clip.setIntValue(1L);
                clip.setDesp(l.getTitle()+"��ֱ����������");
                clip.setContentId(content.getId());
                clip.setStringValue(l.getChannel());
                contentPropertyLogicInterface.save(clip);
                resultCode++;
            }
            if(hasPublished&&resultCode==0){
                //���ֱ���Ѿ��������ˣ��·������ֵ����㣬������״̬��Ϊ�Ѿ�����
                logs+="ֱ���Ѿ��������ˣ����Բ����ٽ�����������";
                resultCode = ERROR_CODE_LIVE_HAS_STARTED;
            }
            systemLogLogicInterface.saveMachineLog(logs);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ThreadUtils.getInstance().release(key);
        }
        return resultCode;
    }

    public String getLiveIdForContent(Live l){
        return "live:"+l.getId()+":live";
    }
    /**
     * ֹͣtaskId��Ӧ��ֱ��
     * @param taskId ����Id
     * @param filePath �ļ�·����transcenter���ص�·��
     * @return ͣ����ֱ������
     * -1 taskId��Ч
     * -2 û���ҵ���Ӧֱ��
     * >0 �ɹ�������ֱ������
     */
    public int stopLive(Long taskId, String filePath,Long liveId){
        if( taskId < 0) return ERROR_CODE_INVALID_TASK_ID;
        int result = 0;
        long[] statusArray = {Live.LIVE_STATUS_WORKING, Live.LIVE_STATUS_STOPPING,Live.LIVE_STATUS_NORMAL};
        List<Live> liveList = new ArrayList<Live>();
        if(liveId!=null&&liveId>0){
            liveList.add(get(liveId));
        }else{
            liveList.addAll(liveDaoInterface.getLiveByTaskId(taskId, statusArray));
        }
        if(liveList.size() == 0) return ERROR_CODE_LIVE_NOT_EXISTS;
        String logs = "ֱֹͣ���ص���";

        // ��Device���ж�ȡһ��VOD����ȡ·�����û��滻ͳһת�뷵��·����ǰ׺
        String transcoderBasePath = "";
        Device d = new Device();
        d.setType(DeviceLogicImpl.DEVICE_TYPE_HLS_VOD);
        try {
            List<Device> deviceList = deviceDaoInterface.getObjects(d);
            if(deviceList != null && deviceList.size() > 0) transcoderBasePath = deviceList.get(0).getLocalPath();
        }catch (Exception e){
            e.printStackTrace();
        }
        // ·��ʹ��ͳһ����
        transcoderBasePath = transcoderBasePath.replace("\\", File.separator).replace("/", File.separator);

        for(Live l : liveList) {
            if (l == null) continue;
            // ����״̬Ϊһ��״̬
            logs+=l.getTitle()+"��";
            if(Live.LIVE_STATUS_NORMAL!=l.getStatus()){
                l.setStatus(Live.LIVE_STATUS_NORMAL);
                result++;
                l=save(l);
                logs+="״̬�޸�ΪNormal(����)��";
            }else{
                logs+="״̬�����޸ģ�";
            }

            if(l.getNeedRecord()>0){
                logs+="����¼�ƣ�";
            }
            if(filePath==null||"".equals(filePath.trim())){
                logs+="����β���¼��ֹͣ������ֱ��ֹͣ��";
                // ��Content���еļ�¼���Ϊ����
                Content c = new Content();
                c.setContentId(getLiveIdForContent(l));
                c.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                try {
                    List<Content> contentList = contentLogicInterface.search(c);
                    if(contentList != null&&contentList.size()>0) {
                        for (Content content : contentList) {
                            content.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);
                            String name = content.getName();
                            if(name==null){
                                name = l.getTitle();
                            }
                            logs+="����"+name;
                            if(!name.contains("[ֱ���Ѿ�����")){
                                name +="[ֱ���Ѿ�����"+ StringUtils.date2string(new Date())+"]";
                                logs+="��������Ϊ"+name;
                            }
                            content.setName(name);
                            contentCspLogicInterface.unPublishContent(content.getId(),l.getCspId(),-1);
                            contentLogicInterface.save(content);
                            logs+="�����߲�����ɣ�";
                            logger.debug("ֱ���������Ѿ���ý�����ߣ�"+name);
                        }
                    }else{
                        String tempLog ="û�з��ֺ�ֱ����¼����"+l.getTitle()+"���й������Ѿ��������ߵ�ý�壡";
                        logs+=tempLog;
                        logger.debug(tempLog);
                    }
                }catch (Exception e){
                    logs+="�����쳣��"+e.getMessage();
                    e.printStackTrace();
                }
            }else{
                filePath = filePath.replace("\\", File.separator).replace("/", File.separator);
                String relPath = filePath.replace(transcoderBasePath, "");
                // ����Content���ݺ�ContentProperties���ύת��
                logs+="¼����ɺ����ý�岢����ת������";
                liveToContent(l, relPath);
            }
            // ���ֱ��������¼�ƣ��������ת�����̣���ӵ�Content����
            if(l.getNeedRecord() > 0){
                // ��¼�Ƶ��ļ���λ����Ҫ���Ӵ�tc���صľ���λ�ã��ظ�����������Ҫ�ο�startLive�е��滻����
                if(filePath == null || filePath.isEmpty()){
                    String log = "ֱ������¼�ƽ�����" + l.getTitle() + ")�����ļ�����·��Ϊ�գ���������ֱ��ֹͣ��������¼�ƣ�";
                    logs+=log;
                    logger.debug(log);
                }
            }
        }
        if(result==0){
            logs+="ֱ���Ѿ�ֹͣ���ˣ����ܽ��������Ĳ����ˡ�";
            result = ERROR_CODE_LIVE_HAS_STOPPED;
        }
        systemLogLogicInterface.saveMachineLog(logs);
        return result;
    }

    /**
     * �����������򣬽������滻
     * {YYYY}-�� {MM}-�� {DD}-�� {E}-�ܼ�
     * @param title Ҫ�滻������
     * @param date ���ڼ�������ڣ����Ϊ�գ�ʹ�õ�ǰʱ��
     * @return �滻�������
     */
    private String regTitle(String title, Date date){
        if(title == null) return "";

        Calendar cal = Calendar.getInstance();
        if(date == null) date = new Date();
        cal.setTime(date);
        cal.get(Calendar.YEAR);
        int monthVal = cal.get(Calendar.MONTH)+1;
        String month = monthVal < 10? "0"+monthVal : monthVal+"";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10? "0"+cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH)+"";
        String[] dayOfWeekName = {"����", "��һ", "�ܶ�", "����", "����", "����", "����"};
        return title.replace("{YYYY}", cal.get(Calendar.YEAR)+"")
                .replace("{MM}", month)
                .replace("{DD}", day)
                .replace("{E}", dayOfWeekName[cal.get(Calendar.DAY_OF_WEEK)-1]);
    }

    /**
     * ��ȡֱ����������
     * @param liveId ֱ��Id
     * @return ����URL����������ַ
     */
    public String getLiveURL(Long liveId){
        Live live = get(liveId);
        if (live == null) return null;

        // ֱ��������
        Device device = deviceDaoInterface.get(live.getServerId());
        if(device == null) return null;

        String url = device.getUrl();
        if(!url.endsWith("/")) url += "/";
        //url += "";
        // ƴ������������
        return url + live.getChannel() + ".m3u8";
    }

    /**
     * �ֶ�����ֱ��
     * @param id ֱ��Id
     * @return ֱ���������
     * >0 �����ɹ���taskId
     * -1 û���ҵ�ֱ��
     * -2 ��ǰ״̬��������
     */
    public Long start(Long id){
        Live live = get(id);
        if (live == null) return -1L;

        if(live.getStatus() != Live.LIVE_STATUS_NORMAL){
            return -2L;
        }
        live.setStatus(Live.LIVE_STATUS_STARTING);
        live = save(live);

        return live.getTaskId();
    }

    /**
     * �ֶ�ֱֹͣ��
     * @param id ֱ��Id
     * @return ֱ��ֹͣ���
     */
    public Long stop(Long id){
        Live live = get(id);
        if (live == null) return -1L;

        if(live.getStatus() != Live.LIVE_STATUS_WORKING){
            return -2L;
        }
        live.setStatus(Live.LIVE_STATUS_STOPPING);
        live = save(live);

        return live.getTaskId();
    }

    /**
     * ��ֱ��תΪContent���ύת��
     * @param live ֱ������
     * @param filePath �ļ����·������ֱ�ӱ��浽Content_property���е�
     */
    private void liveToContent(Live live, String filePath){
        if(live == null || filePath == null) return;

        Content content = new Content();
        // ����Ĭ��ֵ
        BeanUtils.setDefaultValue(content,"createTime",new Date());
        BeanUtils.setDefaultValue(content,"validStartTime",new Date());
        BeanUtils.setDefaultValue(content,"validEndTime",new Date(System.currentTimeMillis()+10*365*24*3600*1000L));
        BeanUtils.setDefaultValue(content, "cspId", live.getCspId());
        BeanUtils.setDefaultValue(content, "contentAuditId", 1L);
        BeanUtils.setDefaultValue(content, "creatorAdminId", live.getCreator());
        BeanUtils.setDefaultValue(content,"allVisitCount",0L);
        BeanUtils.setDefaultValue(content,"monthVisitCount",0L);
        BeanUtils.setDefaultValue(content,"weekVisitCount", 0L);
        // live�е���Ϣ
        content.setModuleId(live.getModuleId());
        String name = live.getTitle();
        if( live.getType() != Live.LIVE_TYPE_REPEAT||!name.contains("{")) {
            name+=AppConfigurator.getInstance().getConfig("system.record.extInfo","[¼]{YYYY}-{MM}-{DD}");
        }
        name = regTitle(name,new Date());
        content.setName(name);
        content.setActors(live.getActor());
        content.setIntro(live.getIntro());
        content.setPost1Url(live.getPoster());

        // ѡ��һ���㲥������
        try {
            List<Device> deviceList = deviceDaoInterface.getDevicesByCspId(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD,
                    DeviceLogicInterface.DEVICE_ONLINE, -1);
            if(deviceList != null && deviceList.size() > 0) {
                // ���ѡ��һ��
                Random r = new Random((new Date()).getTime());
                Device device = deviceList.get(r.nextInt() % deviceList.size());
                content.setDeviceId(device.getId());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        // ���Թۿ����û�����
        content.setUserTypes(live.getUserTypes());
        content.setStatus(ContentLogicInterface.STATUS_WAITING);

        List<ContentProperty> contentProperties = new ArrayList<ContentProperty>();
        // ���ļ�Դ��ӵ�property��
        ContentProperty p = new ContentProperty();
        p.setName(live.getTitle()+"¼��������ԭʼ����");
        p.setIntValue(1L);
        p.setStringValue(filePath);
        p.setPropertyId(1L);    // must be 1
        contentProperties.add(p);

        // ����㲥����
        content = contentLogicInterface.save(content);
        //���Ƶ��������Ϣ
        String channelIds = "";
        if(live.getRecordChannels() == null || live.getRecordChannels().isEmpty()) {
            List<LiveChannel> liveChannels = liveChannelDaoInterface.getListByLive(live);
            if (liveChannels != null) {
                for (LiveChannel liveChannel : liveChannels) {
                    channelIds += channelIds.isEmpty() ? liveChannel.getChannelId() : "," + liveChannel.getChannelId();
                }
            }
        }else{
            channelIds = live.getRecordChannels();
        }
        contentLogicInterface.checkPublishChannels(content,channelIds,true);
        // �����ļ���Ϣ
        contentPropertyLogicInterface.saveClips(contentProperties, content, -1);
    }
}
