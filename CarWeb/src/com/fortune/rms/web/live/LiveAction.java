package com.fortune.rms.web.live;

import com.fortune.common.Constants;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.live.logic.logicInterface.LiveLogicInterface;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.Task;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.AppConfigurator;
import com.fortune.util.CacheUtils;
//import com.fortune.util.FileUtils;
import com.fortune.util.StringUtils;
//import com.fortune.util.net.URLEncoder;
import com.fortune.util.TcpUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
//import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.apache.http.protocol.HTTP;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONException;
import org.json.JSONObject;
import com.fortune.server.message.ServerMessager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Enumeration;
import java.util.List;

/**
 * Created by 王明路 on 2015/2/26.
 * 直播Action
 */
@Namespace("/live")
@ParentPackage("default")
@Action(value = "live")
public class LiveAction extends BaseAction<Live> {
    private static final String TRANS_CENTER_TASK_URL = "interface/service.jsp?command=tasks";
    private static final String TRANS_CENTER_SOURCE_URL = "interface/service.jsp?command=sources";
    private static final String TRANS_CENTER_TEMPLATE_URL = "interface/service.jsp?command=templates";
    private static final String TRANS_CENTER_REMOVE_TASK_URL = "interface/service.jsp?command=delete";
    private static final String TRANS_CENTER_GET_TASK_URL = "interface/service.jsp?command=getTask";
    private static final String TRANS_CENTER_SAVE_TASK_URL = "interface/service.jsp?command=save";
    private static final String TRANS_CENTER_START_TASK_URL = "interface/service.jsp?command=start";
    private static final String TRANS_CENTER_STOP_TASK_URL = "interface/service.jsp?command=stop";
    private static final String TRANS_CENTER_UPDATE_TASK_CALLBACK_URL = "interface/service.jsp?command=updateCallback";
    public LiveAction() {
        super(Live.class);
    }

    private AdminLogicInterface adminLogicInterface;
    private LiveLogicInterface liveLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;

    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    public void setLiveLogicInterface(LiveLogicInterface liveLogicInterface) {
        this.liveLogicInterface = liveLogicInterface;
    }

    private String searchValue;         // 查询关键字
    private String liveChannels;        // 逗号分隔的直播频道设置
    private String posterData;          // base64后的海报数据
    private String liveChannel;         // 直播智能流频道名称
    private Long taskId;                 // 获取直播对应的任务详情时使用
    private Task task;                   // 保存任务使用

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;              // 统一转码回调主参数

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getLiveChannel() {
        return liveChannel;
    }

    public void setLiveChannel(String liveChannel) {
        this.liveChannel = liveChannel;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    private String serverIp;            // 直播服务器地址，由transcenter告知，解析成server_id
    private String source;              // 源地址，transcenter回调使用

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPosterData() {
        return posterData;
    }

    public void setPosterData(String posterData) {
        this.posterData = posterData;
    }

    public String getLiveChannels() {
        return liveChannels;
    }

    public void setLiveChannels(String liveChannels) {
        this.liveChannels = liveChannels;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setAdminLogicInterface(AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
    }

    /**
     * 装载直播信息，包括基本信息和栏目信息
     * @return SUCCESS
     */
    public String loadLive(){
        obj = liveLogicInterface.loadLive(obj.getId());
        return SUCCESS;
    }

    /**
     * 统一转码的回调处理，启动直播，统一转码传递参数taskId，找到对应的直播标记为启动状态
     */
    public void startLive(){
        int code = liveLogicInterface.startLive(taskId,obj.getId());
        CacheUtils.clearAll();
        String message = "" ;
        if(code > 0){
            message = "成功启动" + code + "个直播";
        }else {
            switch (code) {
                case -1:
                    message = "无效的taskId";
                    break;
                case -2:
                    message = "没有找到关联的直播";
                    break;
            }
        }
        directOut("{\"code\":" + code + ", \"message\":\"" + message + "\"}");
    }

    /**
     * 统一转码的回调处理，停止直播，统一转码传递参数taskId，找到对应的直播标记为停止
     * @param filePath 文件保存路径，可能为空
     */
    public void stopLive(String filePath){
        int code = liveLogicInterface.stopLive(taskId, filePath,obj.getId());
        CacheUtils.clearAll();
        String message = "" ;
        if(code > 0){
            message = "成功停止" + code + "个直播";
        }else {
            switch (code) {
                case -1:
                    message = "无效的taskId";
                    break;
                case -2:
                    message = "没有找到关联的直播";
                    break;
            }
        }
        directOut("{\"code\":" + code + ", \"message\":\"" + message + "\"}");
    }

    /**
     * 获取直播列表，用于管理后台直播管理，可能有查询条件，只查询管理员可以管理的栏目范围内的直播
     */
    public void getLiveList(){
        Admin admin = (Admin)session.get(Constants.SESSION_ADMIN);
        if(admin == null){
            directOut(com.fortune.util.JsonUtils.getListJsonString("liveList", new ArrayList<Live>(), "totalCount", -1));
        }else {
            String channels = "";
            if( admin.getIsRoot() != 1 ) {
                channels = adminLogicInterface.getAdminGrantChannel(admin);
            }
            List<Live> liveList = liveLogicInterface.searchLive(channels, searchValue, pageBean);

            directOut(com.fortune.util.JsonUtils.getListJsonString("liveList", liveList, "totalCount", pageBean.getRowCount()));
        }
    }

    /**
     * 录制列表
     */
    public void getRecordList(){
        Admin admin = (Admin)session.get(Constants.SESSION_ADMIN);
        if(admin == null){
            directOut(com.fortune.util.JsonUtils.getListJsonString("recordList", new ArrayList<Live>(), "totalCount", -1));
        }else {
            String channels = "";
            if( admin.getIsRoot() != 1 ) {
                channels = adminLogicInterface.getAdminGrantChannel(admin);
            }
            List<Live> recordList = liveLogicInterface.searchRecord(channels, searchValue, pageBean);

            directOut(com.fortune.util.JsonUtils.getListJsonString("recordList", recordList, "totalCount", pageBean.getRowCount()));
        }
    }

    // 删除直播
    public String removeLive(){
        // 删除指定的直播
        long taskId = liveLogicInterface.removeLive(obj);
        if(taskId > 0){
            // 通知transcenter，该任务已经被废弃
            transcenterRemoveTask(taskId);
            CacheUtils.clearAll();
            writeSysLog("删除直播，ID是"+obj.getId());
            setSuccess(true);
        }else{
            String errorLogs ="删除直播失败，ID是"+obj.getId()+",";
            if(taskId==LiveLogicInterface.ERROR_CODE_LIVE_HAS_STARTED){
                errorLogs+="启动的任务，不能直接删除！请先停止该任务！";
            }else if(taskId==LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS){
                errorLogs+="任务不存在，请仔细检查，是否已经被删除了？";
            }else if(taskId==LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID){
                errorLogs+="无效的转码任务ID！";
            }else {
                errorLogs +="暂时还不知道具体的原因！";
            }
            writeSysLog(errorLogs);
            setSuccess(false);
        }
        return SUCCESS;
    }

    // 手动启动直播，仅限单次任务，返回启停状态，等待回调结果
    public void start(){
        Long r = liveLogicInterface.start(obj.getId());
        CacheUtils.clearAll();
        String message = "启动成功。";
        switch (r.intValue()){
            case -1: message = "无效的直播。"; break;
            case -2: message = "直播当前状态不能启动。";
        }

        // 通知transcenter启动直播
        if( r > 0 ) transcenterStartTask(r);
        writeSysLog("启动直播，ID是"+obj.getId()+","+message);

        directOut("{\"success\":" + (r > 0) + ", \"message\":\"" + message + "\"}");
    }

    // 手动停止直播，仅限单次任务
    public void stop(){
        Long r = liveLogicInterface.stop(obj.getId());
        CacheUtils.clearAll();
        String message = "停止成功。";
        switch (r.intValue()){
            case -1: message = "无效的直播。"; break;
            case -2: message = "直播当前状态不能停止。";
        }

        // 通知transcenter启动直播
        if( r > 0 ) transcenterStopTask(r);
        writeSysLog("停止直播，ID是"+obj.getId()+","+message);
        directOut("{\"success\":" + (r > 0) + ", \"message\":\"" + message + "\"}");
    }

    /**
     * 保存直播
     * @return SUCCESS
     */
    public String saveLive(){
        if(obj != null){
            obj.setIsLive(1L);
        }
        return doSave(obj);
    }
    public String doSave(Live live){
        CacheUtils.clearAll();
        // 保存海报数据
        String posterFilePath = "";
        if(posterData != null && !posterData.isEmpty()){
            try {
                posterFilePath = savePoster(posterData);
            }catch (Exception e){
                posterFilePath = "";
                e.printStackTrace();
            }
        }
        live.setPoster(posterFilePath);
        // 保存其他数据
        String[] liveChannelIds = liveChannels.split(",");
        List<Long> channelIdArray = new ArrayList<Long>();
        for (String liveChannelId : liveChannelIds) {
            Long channelId = StringUtils.string2long(liveChannelId, -1);
            if (channelId > 0) channelIdArray.add(channelId);
        }
        // 记录创建者
        if( live.getId() <= 0){
            live.setCreator( admin != null? admin.getId(): -1L);
            live.setCspId(admin.getCspId() != null? admin.getCspId().longValue(): -1L);
        }
        live = liveLogicInterface.saveLive(live, channelIdArray, liveChannel, serverIp);
        updateCallbackUrl(live.getTaskId(),live.getId());
        writeSysLog("保存" +(live.getIsLive()==1L?"直播":"录制")+
                "，ID是"+live.getId()+","+live.getTitle());
        return SUCCESS;
    }
    /**
     * 保存录制
     * @return SUCCESS
     */
    public String saveRecord(){
        obj.setIsLive(0L);
        return doSave(obj);
    }

    private String savePoster(String posterData) throws IOException {
        //HttpServletRequest request = ServletActionContext.getRequest();
        ServletContext context = ServletActionContext.getServletContext();
        String saveToFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
        String fullFileName;
        File posterFile;
        // 根据头上的data:image/png;base64，获取扩展名
        String postfix = posterData.substring(posterData.indexOf("image/")+6,posterData.indexOf(";base64"));
        do{
            fullFileName = String.format("%s%s.%s", saveToFilePath, RandomStringUtils.randomAlphanumeric(8), postfix);
            posterFile = new File(context.getRealPath(fullFileName));
        }while(posterFile.exists());

        String base64 = posterData.substring(posterData.indexOf(",")+1);
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(base64 .getBytes());
        org.apache.commons.io.FileUtils.writeByteArrayToFile(posterFile, decoded);
        return fullFileName;
    }

    // 获取直播播放url，用于管理员播放直播
    public void liveURL(){
        String url = liveLogicInterface.getLiveURL(obj.getId());
        directOut("{\"succeed\":" + (url != null) + ", \"url\":\"" + url + "\"}");
    }

    // 统一转码回调入口
    // 参数类似action=[start,error,finished,queue,waiting,scanning]&taskId=x&obId=y&transcoderId=r&source&desert&startTime=xxx&stopTime=xxx
    @Action(value = "tc_callback")
    public void transcenterCallback(){
        if(action == null || action.isEmpty()){
            directOut("{\"succeed\":false, \"message\":\"action为空\"}");
            return;
        }

        HttpServletRequest request = ServletActionContext.getRequest();
/*
        Enumeration pNames=request.getParameterNames();
        while(pNames.hasMoreElements()){
            String name=(String)pNames.nextElement();
            String value=request.getParameter(name);
            System.out.println(name + "=" + value);
        }
*/
        //System.out.println("transcenter callback action:" + action + " taskId:" + taskId + " source:" + source);

        String p = action.toLowerCase();
        int code;
        String message = "";
        if(p.equals("start")){
            code = liveLogicInterface.startLive(taskId,obj.getId());
            message = "" ;
            if(code > 0){
                message = "成功启动" + code + "个直播";
            }else {
                switch (code) {
                    case LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID:
                        message = "无效的taskId";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS:
                        message = "没有找到关联的直播";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_HAS_STARTED:
                        message="直播已经启动";
                        break;
                }
            }
            CacheUtils.clearAll();
            if(code<=0){
                writeSysLog("启动直播后，转码中心回调，ID是" + taskId + "," + message);
            }
            log.debug("start 结果：" + message);
            directOut("{\"succeed\":" + (code > 0) + ", \"message\":\"" + message + "\"}");
        }else if(p.equals("finished")){
            // 解析文件保存位置，如果保存了
            String type = request.getParameter("l");
            String path = request.getParameter("save2file");
            if(path != null && !path.isEmpty() && !"file".equals(type)){
                path = "";
            }
            code = liveLogicInterface.stopLive(taskId, path,obj.getId());
            if(code > 0){
                message = "成功停止" + code + "个直播";
            }else {
                switch (code) {
                    case LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID:
                        message = "无效的taskId";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS:
                        message = "没有找到关联的直播";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_HAS_STOPPED:
                        message = "已经停止直播";
                        break;
                }
            }
            if(code<=0){
                writeSysLog("结束直播后，转码中心回调，ID是" + taskId + "," + message);
            }
            log.debug("结束直播 结果：" + message);
            directOut("{\"succeed\":" + (code > 0) + ", \"message\":\"" + message + "\"}");
            CacheUtils.clearAll();
        }else{
            directOut("{\"succeed\": true, \"message\":\"【" + p + "】命令已收到，但未做处理\"}");
        }
    }
    /**
     * 从统一转码获取一个任务的信息，id在taskid
     * 中
     */
    @Action(value = "getTask")
    public void getTranscenterTask(){
        // httpClient访问
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_GET_TASK_URL;
        url += "&id=" + taskId;
        HTTPResult result = httpGet(url);
        if(result.getCode() == 200){
            directOut(result.getResult());
        }else{
            directOut("{\"total\":-1, \"message\":\"访问统一转码(" + url + ")失败\"}");
        }
    }

    @Action(value = "synchroTask")
    public void synchronizeTaskWithTranscenter(){
        // 将任务信息保存或同步到统一转码平台
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_SAVE_TASK_URL;

        String message = "成功";
        Long taskId = 0L;
        if(task != null && (task.getId() == null || task.getId() < 0)){
            // 为直播选择直播服务器，并生成智能流名称
            if(task.getNeedLive()) {
                task.setStreamName(liveLogicInterface.getRandomStreamName());

                Device device = deviceLogicInterface.getRandomLiveServer();
                if (device != null) {
                    // mod by mlwang, 使用url中的地址和端口
                    //task.setServerIp(device.getIp());
                    String deviceUrl = device.getUrl();
                    task.setServerIp(device.getIp());
                    task.setServerPort(TcpUtils.getPortFromUrl(deviceUrl));
                    log.debug("task对应的服务器信息是：" + task.getServerIp() + ":" + task.getServerPort() + ",信息来自：" + device.getName() + "," + deviceUrl);
                } else {
                    taskId = -99L;
                }
            }else{
                task.setServerIp("");
            }
        }


        if(task!= null && task.getNeedRecord() && (task.getFilePath() == null || task.getFilePath().isEmpty())){
            //地址
            String filePath = StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" + task.getStreamName() + ".mp4";
            task.setFilePath(filePath);
        }else if(task!=null){
            task.setFilePath("");
        }

        if( taskId != -99) {
            taskId = saveTask(url, task);
        }

        log.debug("统一转码任务保存结果：" + taskId);
        if(taskId < 0) {
            /* -1 对象为空
                    * -2 协议错误
                    * -3 IO错误
                    * -4 JSON格式错误
                    * -9 未知错误
                    */
            switch (taskId.intValue()) {
                case -1: message = "任务对象为空"; break;
                case -2: message = "访问统一转码，发生协议错误"; break;
                case -3: message = "访问统一转码，发生IO错误"; break;
                case -4: message = "统一转码返回JSON格式错误"; break;
                case -9: message = "保存任务发生未知错误"; break;
                case -99: message = "没有直播服务器可供选择，请先配置服务器";break;
            }
        }
        writeSysLog("将任务信息保存或同步到统一转码平台taskId=" +taskId+
                ":"+task.getTitle()+","+message);
        directOut("{\"failed\":" + ((taskId < 0) ? "true" : "false") + ", \"message\":\"" + message +
                "\", \"taskId\":\"" + taskId + "\", \"serverIp\":\"" + task.getServerIp() +
                "\",\"streamName\":\"" + task.getStreamName() + "\"}");
    }

    /**
     * 从统一转码平台获取现有任务列表，将结果直接directOut给浏览器，从action访问是为了防止统一转码ip不对外开放
     */
    @Action(value = "taskList")
    public void getTranscenterTasks(){
        // httpClient访问
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_TASK_URL;
        if(pageBean != null){
            url += "&pageNo=" + pageBean.getPageNo();
            if(pageBean.getPageSize() > 0){
                url += "&pageSize=" + pageBean.getPageSize();
            }
            if(searchValue != null && !searchValue.isEmpty()){
                try {
                    url += "&name=" + java.net.URLEncoder.encode(searchValue, "UTF-8");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        HTTPResult result = httpGet(url);
        if(result.getCode() == 200){
            directOut(result.getResult());
        }else{
            directOut("{\"total\":-1, \"message\":\"访问统一转码(" + url + ")失败\"}");
        }
    }

    /**
     * 请求统一转码的信号源列表
     */
    @Action(value = "sourceList")
    public void getTranscenterSources(){
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_SOURCE_URL;

        HTTPResult result = httpGet(url);
        if(result.getCode() == 200){
            directOut(result.getResult());
        }else{
            directOut("{\"failed\":true, \"message\":\"访问统一转码(" + url + ")失败\"}");
        }
    }

    /**
     * 请求统一转码平台的
     */
    @Action(value = "templateList")
    public void getTranscenterTemplates(){
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_TEMPLATE_URL;

        HTTPResult result = httpGet(url);
        if(result.getCode() == 200){
            directOut(result.getResult());
        }else{
            directOut("{\"failed\":true, \"message\":\"访问统一转码(" + url + ")失败\"}");
        }
    }

    /**
     * 通知统一转码，启动任务
     * @param taskId 任务Id
     */
    public void transcenterStartTask(Long taskId){
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_START_TASK_URL;

        HTTPResult result = httpGet(url + "&id=" + taskId);
        if(result.getCode() == 200) {
            log.debug(result.getResult());
        }
        writeSysLog("通知转码中心启动转码：taskId="+taskId);
    }

    /**
     * 通知统一转码，停止任务
     * @param taskId 任务Id
     */
    public void transcenterStopTask(Long taskId){
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_STOP_TASK_URL;

        HTTPResult result = httpGet(url + "&id=" + taskId);
        if(result.getCode() == 200) {
            log.debug(result.getResult());
        }
        writeSysLog("通知转码中心停止转码：taskId="+taskId);
    }

    public void updateCallbackUrl(Long taskId,Long liveId){
        AppConfigurator appConfigurator = AppConfigurator.getInstance();
        String url = appConfigurator.getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_UPDATE_TASK_CALLBACK_URL;

        HTTPResult result = httpPost(url + "&id=" + taskId, "callback=" + appConfigurator.getConfig(
                "system.transcenter.callbackUrl", "http://127.0.0.1/live/tc_callback.action") + "?obj.id=" + liveId + "&");
        if(result.getCode() == 200) {
            log.debug(result.getResult());
        }
    }
    /*
    * 通知transcenter删除任务
    * */
    public void transcenterRemoveTask(Long taskId){
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_REMOVE_TASK_URL;

        HTTPResult result = httpGet(url + "&id=" + taskId);
        if(result.getCode() == 200) {
            log.debug(result.getResult());
        }
        writeSysLog("通知转码中心删除转码任务：taskId="+taskId);

    }

    public Task getTask() {
        return task;
    }

    /**
     * 和transcenter通信，保存任务
     * @param url 统一转码保存任务url
     * @param task 任务对象
     * @return 保存成功的任务Id，如果失败返回<0的长型数字
     * -1 对象为空
     * -2 协议错误
     * -3 IO错误
     * -4 JSON格式错误
     * -9 未知错误
     */
    public long saveTask(String url, Task task){
        if(task == null) return -1;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 要传递的参数
        // 需要录制时传file，否则保持为空
        // 需要直播时传serverIp，否则保持为空
        if(task.getId() == null){ task.setId(-1L); }
        if(obj==null){
            obj = new Live();
            obj.setId(-1L);
        }
        Long id;
        if(obj!=null){
            id = obj.getId();
            if(id!=null&&id>0){
                String callback=AppConfigurator.getInstance().getConfig(
                        "system.transcenter.callbackUrl","http://127.0.0.1/live/tc_callback.action");
                callback+="?obj.id="+id;
                params.add(new BasicNameValuePair("callback", callback));  // id
            }
        }
        params.add(new BasicNameValuePair("id", task.getId().toString()));  // id
        params.add(new BasicNameValuePair("title", task.getTitle()));  // title
        params.add(new BasicNameValuePair("sourceId", task.getSourceId().toString()));// 源
        params.add(new BasicNameValuePair("templateIds", task.getTemplateIds()));   // 配置
        params.add(new BasicNameValuePair("streamName", task.getStreamName())); // 智能流名称
        params.add(new BasicNameValuePair("serverIp", task.getServerIp()+":"+task.getServerPort())); // 流服务器地址
        params.add(new BasicNameValuePair("type", task.getType().toString())); // 时间类型
        params.add(new BasicNameValuePair("startTime", StringUtils.date2string(task.getStartTime()))); // 开始时间
        params.add(new BasicNameValuePair("endTime", StringUtils.date2string(task.getEndTime()))); // 结束时间
        params.add(new BasicNameValuePair("startDate", StringUtils.date2string(task.getStartDate()))); // 开始日期
        params.add(new BasicNameValuePair("endDate", StringUtils.date2string(task.getEndDate()))); // 结束日期
        params.add(new BasicNameValuePair("weekDay", task.getWeekDay())); // 重复星期
        params.add(new BasicNameValuePair("file", task.getFilePath()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
/*
 * Execute the HTTP Request
 */
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                // EntityUtils to get the response content
                String content =  EntityUtils.toString(respEntity);
                JSONObject jsonObject = new JSONObject(content);
                log.debug("save task return:" + content);
                // 解析json数据
                JSONObject taskObj = jsonObject.getJSONObject("task");
                if(taskObj == null) return 0;
                return StringUtils.string2long(taskObj.get("id").toString(), 0);
            }
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
            return -2;
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
            return -3;
        } catch( JSONException e){
            e.printStackTrace();
            return -4;
        }

        return -9;
    }

    /**
     * http访问url
     * @param url   访问url，参数拼接在后面
     * @return  返回的数据
     */
    public HTTPResult httpGet(String url){
        return httpPost(url,null);
    }
    public HTTPResult httpPost(String url,String postData){
        //URL httpURL;
        HTTPResult result = new HTTPResult();
        try {
            ServerMessager messager = new ServerMessager();
            String transResult = messager.postToHost(url, postData, "UTF-8");
            /*
            httpURL = new URL(url);
            System.out.println("url:" + url);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            result.setResult("");
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                result.setResult(result.getResult() + line);
            }
            in.close();
            */
            result.setResult(transResult);
            result.setCode(200);
        }catch (Exception e){
            result.setCode(-1);
            return result;
        }
        return result;
    }

    private class HTTPResult{
        private String result;
        private Integer code;

        public HTTPResult(){

        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
}
