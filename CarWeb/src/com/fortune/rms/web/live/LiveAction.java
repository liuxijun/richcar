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
 * Created by ����· on 2015/2/26.
 * ֱ��Action
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

    private String searchValue;         // ��ѯ�ؼ���
    private String liveChannels;        // ���ŷָ���ֱ��Ƶ������
    private String posterData;          // base64��ĺ�������
    private String liveChannel;         // ֱ��������Ƶ������
    private Long taskId;                 // ��ȡֱ����Ӧ����������ʱʹ��
    private Task task;                   // ��������ʹ��

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;              // ͳһת��ص�������

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

    private String serverIp;            // ֱ����������ַ����transcenter��֪��������server_id
    private String source;              // Դ��ַ��transcenter�ص�ʹ��

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
     * װ��ֱ����Ϣ������������Ϣ����Ŀ��Ϣ
     * @return SUCCESS
     */
    public String loadLive(){
        obj = liveLogicInterface.loadLive(obj.getId());
        return SUCCESS;
    }

    /**
     * ͳһת��Ļص���������ֱ����ͳһת�봫�ݲ���taskId���ҵ���Ӧ��ֱ�����Ϊ����״̬
     */
    public void startLive(){
        int code = liveLogicInterface.startLive(taskId,obj.getId());
        CacheUtils.clearAll();
        String message = "" ;
        if(code > 0){
            message = "�ɹ�����" + code + "��ֱ��";
        }else {
            switch (code) {
                case -1:
                    message = "��Ч��taskId";
                    break;
                case -2:
                    message = "û���ҵ�������ֱ��";
                    break;
            }
        }
        directOut("{\"code\":" + code + ", \"message\":\"" + message + "\"}");
    }

    /**
     * ͳһת��Ļص�����ֱֹͣ����ͳһת�봫�ݲ���taskId���ҵ���Ӧ��ֱ�����Ϊֹͣ
     * @param filePath �ļ�����·��������Ϊ��
     */
    public void stopLive(String filePath){
        int code = liveLogicInterface.stopLive(taskId, filePath,obj.getId());
        CacheUtils.clearAll();
        String message = "" ;
        if(code > 0){
            message = "�ɹ�ֹͣ" + code + "��ֱ��";
        }else {
            switch (code) {
                case -1:
                    message = "��Ч��taskId";
                    break;
                case -2:
                    message = "û���ҵ�������ֱ��";
                    break;
            }
        }
        directOut("{\"code\":" + code + ", \"message\":\"" + message + "\"}");
    }

    /**
     * ��ȡֱ���б����ڹ����ֱ̨�����������в�ѯ������ֻ��ѯ����Ա���Թ������Ŀ��Χ�ڵ�ֱ��
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
     * ¼���б�
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

    // ɾ��ֱ��
    public String removeLive(){
        // ɾ��ָ����ֱ��
        long taskId = liveLogicInterface.removeLive(obj);
        if(taskId > 0){
            // ֪ͨtranscenter���������Ѿ�������
            transcenterRemoveTask(taskId);
            CacheUtils.clearAll();
            writeSysLog("ɾ��ֱ����ID��"+obj.getId());
            setSuccess(true);
        }else{
            String errorLogs ="ɾ��ֱ��ʧ�ܣ�ID��"+obj.getId()+",";
            if(taskId==LiveLogicInterface.ERROR_CODE_LIVE_HAS_STARTED){
                errorLogs+="���������񣬲���ֱ��ɾ��������ֹͣ������";
            }else if(taskId==LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS){
                errorLogs+="���񲻴��ڣ�����ϸ��飬�Ƿ��Ѿ���ɾ���ˣ�";
            }else if(taskId==LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID){
                errorLogs+="��Ч��ת������ID��";
            }else {
                errorLogs +="��ʱ����֪�������ԭ��";
            }
            writeSysLog(errorLogs);
            setSuccess(false);
        }
        return SUCCESS;
    }

    // �ֶ�����ֱ�������޵������񣬷�����ͣ״̬���ȴ��ص����
    public void start(){
        Long r = liveLogicInterface.start(obj.getId());
        CacheUtils.clearAll();
        String message = "�����ɹ���";
        switch (r.intValue()){
            case -1: message = "��Ч��ֱ����"; break;
            case -2: message = "ֱ����ǰ״̬����������";
        }

        // ֪ͨtranscenter����ֱ��
        if( r > 0 ) transcenterStartTask(r);
        writeSysLog("����ֱ����ID��"+obj.getId()+","+message);

        directOut("{\"success\":" + (r > 0) + ", \"message\":\"" + message + "\"}");
    }

    // �ֶ�ֱֹͣ�������޵�������
    public void stop(){
        Long r = liveLogicInterface.stop(obj.getId());
        CacheUtils.clearAll();
        String message = "ֹͣ�ɹ���";
        switch (r.intValue()){
            case -1: message = "��Ч��ֱ����"; break;
            case -2: message = "ֱ����ǰ״̬����ֹͣ��";
        }

        // ֪ͨtranscenter����ֱ��
        if( r > 0 ) transcenterStopTask(r);
        writeSysLog("ֱֹͣ����ID��"+obj.getId()+","+message);
        directOut("{\"success\":" + (r > 0) + ", \"message\":\"" + message + "\"}");
    }

    /**
     * ����ֱ��
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
        // ���溣������
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
        // ������������
        String[] liveChannelIds = liveChannels.split(",");
        List<Long> channelIdArray = new ArrayList<Long>();
        for (String liveChannelId : liveChannelIds) {
            Long channelId = StringUtils.string2long(liveChannelId, -1);
            if (channelId > 0) channelIdArray.add(channelId);
        }
        // ��¼������
        if( live.getId() <= 0){
            live.setCreator( admin != null? admin.getId(): -1L);
            live.setCspId(admin.getCspId() != null? admin.getCspId().longValue(): -1L);
        }
        live = liveLogicInterface.saveLive(live, channelIdArray, liveChannel, serverIp);
        updateCallbackUrl(live.getTaskId(),live.getId());
        writeSysLog("����" +(live.getIsLive()==1L?"ֱ��":"¼��")+
                "��ID��"+live.getId()+","+live.getTitle());
        return SUCCESS;
    }
    /**
     * ����¼��
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
        // ����ͷ�ϵ�data:image/png;base64����ȡ��չ��
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

    // ��ȡֱ������url�����ڹ���Ա����ֱ��
    public void liveURL(){
        String url = liveLogicInterface.getLiveURL(obj.getId());
        directOut("{\"succeed\":" + (url != null) + ", \"url\":\"" + url + "\"}");
    }

    // ͳһת��ص����
    // ��������action=[start,error,finished,queue,waiting,scanning]&taskId=x&obId=y&transcoderId=r&source&desert&startTime=xxx&stopTime=xxx
    @Action(value = "tc_callback")
    public void transcenterCallback(){
        if(action == null || action.isEmpty()){
            directOut("{\"succeed\":false, \"message\":\"actionΪ��\"}");
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
                message = "�ɹ�����" + code + "��ֱ��";
            }else {
                switch (code) {
                    case LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID:
                        message = "��Ч��taskId";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS:
                        message = "û���ҵ�������ֱ��";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_HAS_STARTED:
                        message="ֱ���Ѿ�����";
                        break;
                }
            }
            CacheUtils.clearAll();
            if(code<=0){
                writeSysLog("����ֱ����ת�����Ļص���ID��" + taskId + "," + message);
            }
            log.debug("start �����" + message);
            directOut("{\"succeed\":" + (code > 0) + ", \"message\":\"" + message + "\"}");
        }else if(p.equals("finished")){
            // �����ļ�����λ�ã����������
            String type = request.getParameter("l");
            String path = request.getParameter("save2file");
            if(path != null && !path.isEmpty() && !"file".equals(type)){
                path = "";
            }
            code = liveLogicInterface.stopLive(taskId, path,obj.getId());
            if(code > 0){
                message = "�ɹ�ֹͣ" + code + "��ֱ��";
            }else {
                switch (code) {
                    case LiveLogicInterface.ERROR_CODE_INVALID_TASK_ID:
                        message = "��Ч��taskId";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_NOT_EXISTS:
                        message = "û���ҵ�������ֱ��";
                        break;
                    case LiveLogicInterface.ERROR_CODE_LIVE_HAS_STOPPED:
                        message = "�Ѿ�ֱֹͣ��";
                        break;
                }
            }
            if(code<=0){
                writeSysLog("����ֱ����ת�����Ļص���ID��" + taskId + "," + message);
            }
            log.debug("����ֱ�� �����" + message);
            directOut("{\"succeed\":" + (code > 0) + ", \"message\":\"" + message + "\"}");
            CacheUtils.clearAll();
        }else{
            directOut("{\"succeed\": true, \"message\":\"��" + p + "���������յ�����δ������\"}");
        }
    }
    /**
     * ��ͳһת���ȡһ���������Ϣ��id��taskid
     * ��
     */
    @Action(value = "getTask")
    public void getTranscenterTask(){
        // httpClient����
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
            directOut("{\"total\":-1, \"message\":\"����ͳһת��(" + url + ")ʧ��\"}");
        }
    }

    @Action(value = "synchroTask")
    public void synchronizeTaskWithTranscenter(){
        // ��������Ϣ�����ͬ����ͳһת��ƽ̨
        String url = AppConfigurator.getInstance().getConfig("redex.transcenter.address").trim();
        if(!url.endsWith("/")){
            url += "/";
        }
        url += TRANS_CENTER_SAVE_TASK_URL;

        String message = "�ɹ�";
        Long taskId = 0L;
        if(task != null && (task.getId() == null || task.getId() < 0)){
            // Ϊֱ��ѡ��ֱ��������������������������
            if(task.getNeedLive()) {
                task.setStreamName(liveLogicInterface.getRandomStreamName());

                Device device = deviceLogicInterface.getRandomLiveServer();
                if (device != null) {
                    // mod by mlwang, ʹ��url�еĵ�ַ�Ͷ˿�
                    //task.setServerIp(device.getIp());
                    String deviceUrl = device.getUrl();
                    task.setServerIp(device.getIp());
                    task.setServerPort(TcpUtils.getPortFromUrl(deviceUrl));
                    log.debug("task��Ӧ�ķ�������Ϣ�ǣ�" + task.getServerIp() + ":" + task.getServerPort() + ",��Ϣ���ԣ�" + device.getName() + "," + deviceUrl);
                } else {
                    taskId = -99L;
                }
            }else{
                task.setServerIp("");
            }
        }


        if(task!= null && task.getNeedRecord() && (task.getFilePath() == null || task.getFilePath().isEmpty())){
            //��ַ
            String filePath = StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" + task.getStreamName() + ".mp4";
            task.setFilePath(filePath);
        }else if(task!=null){
            task.setFilePath("");
        }

        if( taskId != -99) {
            taskId = saveTask(url, task);
        }

        log.debug("ͳһת�����񱣴�����" + taskId);
        if(taskId < 0) {
            /* -1 ����Ϊ��
                    * -2 Э�����
                    * -3 IO����
                    * -4 JSON��ʽ����
                    * -9 δ֪����
                    */
            switch (taskId.intValue()) {
                case -1: message = "�������Ϊ��"; break;
                case -2: message = "����ͳһת�룬����Э�����"; break;
                case -3: message = "����ͳһת�룬����IO����"; break;
                case -4: message = "ͳһת�뷵��JSON��ʽ����"; break;
                case -9: message = "����������δ֪����"; break;
                case -99: message = "û��ֱ���������ɹ�ѡ���������÷�����";break;
            }
        }
        writeSysLog("��������Ϣ�����ͬ����ͳһת��ƽ̨taskId=" +taskId+
                ":"+task.getTitle()+","+message);
        directOut("{\"failed\":" + ((taskId < 0) ? "true" : "false") + ", \"message\":\"" + message +
                "\", \"taskId\":\"" + taskId + "\", \"serverIp\":\"" + task.getServerIp() +
                "\",\"streamName\":\"" + task.getStreamName() + "\"}");
    }

    /**
     * ��ͳһת��ƽ̨��ȡ���������б������ֱ��directOut�����������action������Ϊ�˷�ֹͳһת��ip�����⿪��
     */
    @Action(value = "taskList")
    public void getTranscenterTasks(){
        // httpClient����
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
            directOut("{\"total\":-1, \"message\":\"����ͳһת��(" + url + ")ʧ��\"}");
        }
    }

    /**
     * ����ͳһת����ź�Դ�б�
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
            directOut("{\"failed\":true, \"message\":\"����ͳһת��(" + url + ")ʧ��\"}");
        }
    }

    /**
     * ����ͳһת��ƽ̨��
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
            directOut("{\"failed\":true, \"message\":\"����ͳһת��(" + url + ")ʧ��\"}");
        }
    }

    /**
     * ֪ͨͳһת�룬��������
     * @param taskId ����Id
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
        writeSysLog("֪ͨת����������ת�룺taskId="+taskId);
    }

    /**
     * ֪ͨͳһת�룬ֹͣ����
     * @param taskId ����Id
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
        writeSysLog("֪ͨת������ֹͣת�룺taskId="+taskId);
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
    * ֪ͨtranscenterɾ������
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
        writeSysLog("֪ͨת������ɾ��ת������taskId="+taskId);

    }

    public Task getTask() {
        return task;
    }

    /**
     * ��transcenterͨ�ţ���������
     * @param url ͳһת�뱣������url
     * @param task �������
     * @return ����ɹ�������Id�����ʧ�ܷ���<0�ĳ�������
     * -1 ����Ϊ��
     * -2 Э�����
     * -3 IO����
     * -4 JSON��ʽ����
     * -9 δ֪����
     */
    public long saveTask(String url, Task task){
        if(task == null) return -1;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // Ҫ���ݵĲ���
        // ��Ҫ¼��ʱ��file�����򱣳�Ϊ��
        // ��Ҫֱ��ʱ��serverIp�����򱣳�Ϊ��
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
        params.add(new BasicNameValuePair("sourceId", task.getSourceId().toString()));// Դ
        params.add(new BasicNameValuePair("templateIds", task.getTemplateIds()));   // ����
        params.add(new BasicNameValuePair("streamName", task.getStreamName())); // ����������
        params.add(new BasicNameValuePair("serverIp", task.getServerIp()+":"+task.getServerPort())); // ����������ַ
        params.add(new BasicNameValuePair("type", task.getType().toString())); // ʱ������
        params.add(new BasicNameValuePair("startTime", StringUtils.date2string(task.getStartTime()))); // ��ʼʱ��
        params.add(new BasicNameValuePair("endTime", StringUtils.date2string(task.getEndTime()))); // ����ʱ��
        params.add(new BasicNameValuePair("startDate", StringUtils.date2string(task.getStartDate()))); // ��ʼ����
        params.add(new BasicNameValuePair("endDate", StringUtils.date2string(task.getEndDate()))); // ��������
        params.add(new BasicNameValuePair("weekDay", task.getWeekDay())); // �ظ�����
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
                // ����json����
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
     * http����url
     * @param url   ����url������ƴ���ں���
     * @return  ���ص�����
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
