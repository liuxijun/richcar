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
 * Created by 王明路 on 2015/2/26.
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
     * 查询直播
     * @param channels      查询范围，管理员可以管理的频道列表，逗号分隔，为空时忽略
     * @param searchValue   查询关键词，为空时忽略
     * @param pageBean       分页和排序信息
     * @return  直播列表
     */
    public List<Live> searchLive(String channels, String searchValue, PageBean pageBean){
        return  liveDaoInterface.searchLive(channels, searchValue, pageBean);
    }

    /**
     * 查询录制
     * @param channels          查询范围，管理员可以管理的频道列表，逗号分隔，为空时忽略
     * @param searchValue       查询关键词，为空时忽略
     * @param pageBean          分页和排序信息
     * @return live类型列表
     */
    public List<Live> searchRecord(String channels, String searchValue, PageBean pageBean){
        return liveDaoInterface.searchRecord(channels, searchValue, pageBean);
    }


    /**
     * 保存直播信息，新建或修改
     * @param live      直播对象
     * @param channelIdList 直播栏目Id序列
     * @param channelName 直播频道号
     * @param serverIp 直播服务器地址
     * @return  保存成功后的Live对象
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
                // 如果老的海报不再用，删掉
                if(oldLive.getPoster() != null && !oldLive.getPoster().equals(live.getPoster())){
                    logger.debug("海报变了，老的海报是否删除呢？"+oldLive.getPoster());
                }
            }
        }else{
            live.setCreateTime(new Date());
            live.setStatus(Live.LIVE_STATUS_NORMAL);
        }

        // 根据serverIp匹配server_id
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

        // 清除原有的栏目设置
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
     * 删除直播
     * @param live 直播对象，只使用其中的id
     * @return 成功删除，返回直播的任务id,失败返回-1
     */
    public long removeLive(Live live){
        if(live == null) return ERROR_CODE_LIVE_NOT_EXISTS;
        /*先删除直播的栏目设置*/
        Live l = null;
        try {
            l = get(live.getId());
        } catch (Exception e) {
            logger.error("无法获取live，错误信息是："+e.getMessage());
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
     * 装载直播信息，包括基本信息和
     * @param id 直播Id
     * @return 直播对象或null
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
     * 获取随机的智能流名称，检查是否和已有的直播重复
     * @return 字符串
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
     * 启动taskId对应的直播
     * @param taskId 任务Id
     * @return 启动结果
     * -1 任务Id无效
     * -2 没有找到对应的直播
     * >0 启动的直播个数
     */
    public synchronized int startLive(Long taskId,Long liveId){
        String key = "startLive:taskId="+taskId+",liveId="+liveId;
        int resultCode = 0;
        try {
            ThreadUtils.getInstance().acquire(key);
            String logs = "直播启动，输入信息：taskId="+taskId+",liveId="+liveId+",";
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
                    logs+="直播是正在运行状态，检查以前是否有已经发布上线的内容，";
                    //如果是已经启动状态的直播，就找找看是否有发布的内容，如果没有，就再发布一下，如果有，就放过
                    List<Content> contents = contentLogicInterface.getContent(null,getLiveIdForContent(l),null);
                    if(contents!=null&&contents.size()>0){
                        for(Content content:contents){
                            long id = content.getId();
                            //只找上线的
                            if(ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())){
                                ContentCsp cc = new ContentCsp();
                                cc.setContentId(id);
                                cc.setCspId(l.getCspId());
                                cc.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                List<ContentCsp> data = contentCspLogicInterface.search(cc);
                                if(data.size()>0){
                                    //接着检查频道发布情况
                                    for(LiveChannel c : channelList) {
                                        contentCspLogicInterface.publishContent(content.getId(), l.getCspId(), c.getChannelId());
                                    }
                                    logs+="发现了以前发布上线的媒体："+content.getName()+",重复一下上线操作！";
                                    hasPublished = true;
                                    break;
                                }else{
                                    logs+="未找到SP上线的内容！";
                                }
                            }else{
                                logs+="媒体未上线："+content.getName()+",";
                            }
                        }
                    }else{
                        logs+="未发现以前发布上线的内容！";
                    }
                }
                if(hasPublished){
                    continue;
                }
                // 设置为正在直播
                l.setStatus( Live.LIVE_STATUS_WORKING );
                l=save(l);

                // 如果是直播
                if(l.getIsLive() == 0){
                    logs+=l.getTitle()+"不需要发布上线，可能只是录制！";
                    continue;
                }

                // 将直播信息导入Content表
                Content content = new Content();
                content.setName(regTitle(l.getTitle(), new Date()));
                logs+="重新发布上线，名称为："+content.getName()+"，";
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
                // 获取管理员的cspId
        /*    if( l.getCreator() != null) {
                Admin admin = adminLogicInterface.get(l.getCreator().intValue());
                if( admin != null ){
                    content.setCspId(admin.getCspId().longValue());
                }
            }*/
                // 将直播id记录在Property1里
                content.setContentId(getLiveIdForContent(l));
                //类型设置为直播
                //content.setType()
                // 状态设置为正常
                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                logger.debug("准备保存content，id=" +content.getId()+
                        ",name="+content.getName()+",cspId="+content.getCspId());
                content = contentLogicInterface.save(content);
                logger.debug("保存完毕content，id=" +content.getId()+
                        ",name="+content.getName()+",cspId="+content.getCspId());
                // 保存栏目设置
                int channelCount = 0;
                for(LiveChannel c : channelList) {
                    //contentChannelLogicInterface.save(new ContentChannel(-1, content.getId(), c.getChannelId()));
                    Channel channel;
                    Long channelId = c.getChannelId();
                    if(channelId!=null&&channelId>0){
                        channel = (Channel)TreeUtils.getInstance().getObject(Channel.class,channelId);
                        if(channel!= null){
                            contentCspLogicInterface.publishContent(content.getId(), l.getCspId(), channelId);
                            logs+="发布上线到频道："+channel.getName()+",";
                            channelCount++;
                        }else{
                            logs+="但无法发布到频道，因为没有找到这个频道："+channelId+",";
                        }
                    }else{
                        logs+="没有发布到任何频道，因为设定的频道ID不正确：channelId="+channelId+",";
                    }
                }
                if(channelCount>0){
                    logs+="累计发布到频道"+channelCount+"个。";
                }else{
                    logs+="未发布到任何频道！";
                }
                ContentProperty clip = new ContentProperty();
                Property property = contentLogicInterface.getPropertyByCode("Media_Url_Source");
                clip.setPropertyId(property.getId());
                clip.setName("直播源链接：" + property.getName());
                clip.setIntValue(1L);
                clip.setDesp(l.getTitle()+"的直播播放链接");
                clip.setContentId(content.getId());
                clip.setStringValue(l.getChannel());
                contentPropertyLogicInterface.save(clip);
                resultCode++;
            }
            if(hasPublished&&resultCode==0){
                //如果直播已经被发布了，新发布的又等于零，就设置状态码为已经发布
                logs+="直播已经被启动了，所以不用再进行其他操作";
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
     * 停止taskId对应的直播
     * @param taskId 任务Id
     * @param filePath 文件路径，transcenter传回的路径
     * @return 停掉的直播个数
     * -1 taskId无效
     * -2 没有找到对应直播
     * >0 成功操作的直播个数
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
        String logs = "停止直播回调，";

        // 从Device表中读取一个VOD，获取路径，用户替换统一转码返回路径的前缀
        String transcoderBasePath = "";
        Device d = new Device();
        d.setType(DeviceLogicImpl.DEVICE_TYPE_HLS_VOD);
        try {
            List<Device> deviceList = deviceDaoInterface.getObjects(d);
            if(deviceList != null && deviceList.size() > 0) transcoderBasePath = deviceList.get(0).getLocalPath();
        }catch (Exception e){
            e.printStackTrace();
        }
        // 路径使用统一规则
        transcoderBasePath = transcoderBasePath.replace("\\", File.separator).replace("/", File.separator);

        for(Live l : liveList) {
            if (l == null) continue;
            // 设置状态为一般状态
            logs+=l.getTitle()+"，";
            if(Live.LIVE_STATUS_NORMAL!=l.getStatus()){
                l.setStatus(Live.LIVE_STATUS_NORMAL);
                result++;
                l=save(l);
                logs+="状态修改为Normal(正常)，";
            }else{
                logs+="状态不用修改，";
            }

            if(l.getNeedRecord()>0){
                logs+="包含录制，";
            }
            if(filePath==null||"".equals(filePath.trim())){
                logs+="但这次不是录制停止，而是直播停止，";
                // 将Content表中的记录标记为下线
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
                            logs+="下线"+name;
                            if(!name.contains("[直播已经结束")){
                                name +="[直播已经结束"+ StringUtils.date2string(new Date())+"]";
                                logs+="，并改名为"+name;
                            }
                            content.setName(name);
                            contentCspLogicInterface.unPublishContent(content.getId(),l.getCspId(),-1);
                            contentLogicInterface.save(content);
                            logs+="，下线操作完成，";
                            logger.debug("直播结束后，已经将媒体下线："+name);
                        }
                    }else{
                        String tempLog ="没有发现和直播或录播“"+l.getTitle()+"”有关联的已经发布上线的媒体！";
                        logs+=tempLog;
                        logger.debug(tempLog);
                    }
                }catch (Exception e){
                    logs+="发生异常："+e.getMessage();
                    e.printStackTrace();
                }
            }else{
                filePath = filePath.replace("\\", File.separator).replace("/", File.separator);
                String relPath = filePath.replace(transcoderBasePath, "");
                // 构造Content数据和ContentProperties，提交转码
                logs+="录制完成后，添加媒体并增加转码任务。";
                liveToContent(l, relPath);
            }
            // 如果直播进行了录制，这里进入转码流程，添加到Content表中
            if(l.getNeedRecord() > 0){
                // 将录制的文件，位置需要增加从tc传回的具体位置，重复任务名称需要参考startLive中的替换规则
                if(filePath == null || filePath.isEmpty()){
                    String log = "直播包含录制结束（" + l.getTitle() + ")，但文件保存路径为空，所以这是直播停止，而不是录制！";
                    logs+=log;
                    logger.debug(log);
                }
            }
        }
        if(result==0){
            logs+="直播已经停止过了，不能进行其他的操作了。";
            result = ERROR_CODE_LIVE_HAS_STOPPED;
        }
        systemLogLogicInterface.saveMachineLog(logs);
        return result;
    }

    /**
     * 根据命名规则，将标题替换
     * {YYYY}-年 {MM}-月 {DD}-日 {E}-周几
     * @param title 要替换的内容
     * @param date 用于计算的日期，如果为空，使用当前时间
     * @return 替换后的内容
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
        String[] dayOfWeekName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return title.replace("{YYYY}", cal.get(Calendar.YEAR)+"")
                .replace("{MM}", month)
                .replace("{DD}", day)
                .replace("{E}", dayOfWeekName[cal.get(Calendar.DAY_OF_WEEK)-1]);
    }

    /**
     * 获取直播播放链接
     * @param liveId 直播Id
     * @return 播放URL，智能流地址
     */
    public String getLiveURL(Long liveId){
        Live live = get(liveId);
        if (live == null) return null;

        // 直播服务器
        Device device = deviceDaoInterface.get(live.getServerId());
        if(device == null) return null;

        String url = device.getUrl();
        if(!url.endsWith("/")) url += "/";
        //url += "";
        // 拼接智能流名称
        return url + live.getChannel() + ".m3u8";
    }

    /**
     * 手动启动直播
     * @param id 直播Id
     * @return 直播启动结果
     * >0 启动成功，taskId
     * -1 没有找到直播
     * -2 当前状态不能启动
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
     * 手动停止直播
     * @param id 直播Id
     * @return 直播停止结果
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
     * 将直播转为Content，提交转码
     * @param live 直播对象
     * @param filePath 文件相对路径，可直接保存到Content_property表中的
     */
    private void liveToContent(Live live, String filePath){
        if(live == null || filePath == null) return;

        Content content = new Content();
        // 设置默认值
        BeanUtils.setDefaultValue(content,"createTime",new Date());
        BeanUtils.setDefaultValue(content,"validStartTime",new Date());
        BeanUtils.setDefaultValue(content,"validEndTime",new Date(System.currentTimeMillis()+10*365*24*3600*1000L));
        BeanUtils.setDefaultValue(content, "cspId", live.getCspId());
        BeanUtils.setDefaultValue(content, "contentAuditId", 1L);
        BeanUtils.setDefaultValue(content, "creatorAdminId", live.getCreator());
        BeanUtils.setDefaultValue(content,"allVisitCount",0L);
        BeanUtils.setDefaultValue(content,"monthVisitCount",0L);
        BeanUtils.setDefaultValue(content,"weekVisitCount", 0L);
        // live中的信息
        content.setModuleId(live.getModuleId());
        String name = live.getTitle();
        if( live.getType() != Live.LIVE_TYPE_REPEAT||!name.contains("{")) {
            name+=AppConfigurator.getInstance().getConfig("system.record.extInfo","[录]{YYYY}-{MM}-{DD}");
        }
        name = regTitle(name,new Date());
        content.setName(name);
        content.setActors(live.getActor());
        content.setIntro(live.getIntro());
        content.setPost1Url(live.getPoster());

        // 选择一个点播服务器
        try {
            List<Device> deviceList = deviceDaoInterface.getDevicesByCspId(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD,
                    DeviceLogicInterface.DEVICE_ONLINE, -1);
            if(deviceList != null && deviceList.size() > 0) {
                // 随机选择一个
                Random r = new Random((new Date()).getTime());
                Device device = deviceList.get(r.nextInt() % deviceList.size());
                content.setDeviceId(device.getId());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        // 可以观看的用户类型
        content.setUserTypes(live.getUserTypes());
        content.setStatus(ContentLogicInterface.STATUS_WAITING);

        List<ContentProperty> contentProperties = new ArrayList<ContentProperty>();
        // 把文件源添加到property中
        ContentProperty p = new ContentProperty();
        p.setName(live.getTitle()+"录制下来的原始链接");
        p.setIntValue(1L);
        p.setStringValue(filePath);
        p.setPropertyId(1L);    // must be 1
        contentProperties.add(p);

        // 保存点播内容
        content = contentLogicInterface.save(content);
        //检查频道发布信息
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
        // 保存文件信息
        contentPropertyLogicInterface.saveClips(contentProperties, content, -1);
    }
}
