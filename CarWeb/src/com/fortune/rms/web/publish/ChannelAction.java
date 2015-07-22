package com.fortune.rms.web.publish;

import com.fortune.common.Constants;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.csp.logic.logicInterface.AdminCspLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspChannelLogicInterface;
import com.fortune.rms.business.csp.model.AdminCsp;
import com.fortune.rms.business.csp.model.CspChannel;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
//import com.fortune.util.AppConfigurator;
import com.fortune.util.JsonUtils;
import com.fortune.util.TreeData;
import com.fortune.util.TreeUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.convention.annotation.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Namespace("/publish")
@ParentPackage("default")
@Action(value = "channel")
@Results({
        @Result(name = "channelTree",location = "/publish/channelTree.jsp"),
        @Result(name = "success",location = "/common/jsonMessages.jsp")
})
public class ChannelAction extends BaseAction<Channel> {
    private static final long serialVersionUID = 3243534534534534l;
    private ChannelLogicInterface channelLogicInterface;
    private AdminCspLogicInterface adminCspLogicInterface;
    private CspChannelLogicInterface cspChannelLogicInterface;
    private long channelId;
    private int type;
    private int cspId;
    private int status;
    private Channel channel;


    @SuppressWarnings("unchecked")
    public ChannelAction() {
        super(Channel.class);
    }

    /**
     * @param channelLogicInterface the channelLogicInterface to set
     */
    public void setChannelLogicInterface(
            ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
        setBaseLogicInterface(channelLogicInterface);
    }

    public void setAdminCspLogicInterface(AdminCspLogicInterface adminCspLogicInterface) {
        this.adminCspLogicInterface = adminCspLogicInterface;
    }

    public void setCspChannelLogicInterface(CspChannelLogicInterface cspChannelLogicInterface) {
        this.cspChannelLogicInterface = cspChannelLogicInterface;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCspId(int cspId) {
        this.cspId = cspId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public TreeData<TreeData> formatTree(long parentId, List<Channel> list1) {
        TreeData<TreeData> treeData = new TreeData<TreeData>();
        try {
            for (int i = 0; i < list1.size(); i++) {
                Channel c = list1.get(i);
                if (c.getParentId().equals(parentId)) {
                    treeData.set(c, formatTree(c.getId(), list1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeData;
    }

    public String getChannelName(int dec,String channelName) {
        String result = "";
        for (int i = 0; i < dec; i++) {
            result += "　";
        }
        if(channelName.contains(result)){

        }else{
           channelName = result+channelName;
        }
        return channelName;
    }
    //判断频道是否有叶子节点，若无则为叶子，有则为父目录
    public List<Channel> formatChannel(long parentId, List<Channel> list1, int dec) {
        List<Channel> result = new ArrayList<Channel>();
        try {
            for (int i = 0,l=list1.size(); i < l; i++) {
                Channel c = list1.get(i);
                if (c.getParentId().equals(parentId)) {
                    c.setName(getChannelName(dec, c.getName()));
                    //c.setLevel(dec);
                    result.add(c);
                    List<Channel> children = formatChannel(c.getId(), list1, dec + 1);
                    if(children.size()>=1){
                        c.setLeaf(false);
                    }else{
                        c.setLeaf(true);
                    }
                   result.addAll(children);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @SuppressWarnings("unused")
    public String searchAll() {
        log.debug("in searchAll");
        if(type==0&&obj.getType()!=null){
            type = obj.getType().intValue();
        }
        if(cspId<=0&&obj.getCspId()!=null){
            cspId = obj.getCspId().intValue();
        }
        objs = channelLogicInterface.getAvailableChannelOfCsp(type, cspId);
        objs = formatChannel(-1, objs, 0);
        pageBean.setRowCount(objs.size());
        return Constants.ACTION_LIST;
    }

    @SuppressWarnings("unused")
    public String searchAllByStatus() {
        log.debug("in searchAll");
        if(type==0&&obj.getType()!=null){
            type = obj.getType().intValue();
        }
        if(cspId<=0&&obj.getCspId()!=null){
            cspId = obj.getCspId().intValue();
        }
        if(status<=0&&obj.getStatus()!=null){
            status = obj.getStatus().intValue();
        }
        objs = channelLogicInterface.getAvailableChannelOfCsp(type, cspId,status);
        objs = formatChannel(-1, objs, 0);
        pageBean.setRowCount(objs.size());
        return Constants.ACTION_LIST;
    }

    public String search() {
        log.debug("in search");
        long parentId = getRequestIntParam("anode", -1);
        pageBean.setPageSize(1000000);
        super.search();
        TreeData<TreeData> treeData = formatTree(parentId, objs);
        String result = "";
        try {
            result += "{\"success\":true,\"total\":" + treeData.size() + ",\"data\":[";
            for (int i = 0; i < treeData.size(); i++) {
                Object o = treeData.getKey(i);
                result += "{\"name\":\"" + BeanUtils.getProperty(o, "name") + "\"," +
                        "\"_id\":\"" + BeanUtils.getProperty(o, "id") + "\",";
                if ("-1".equals(BeanUtils.getProperty(o, "parentId"))) {
                    result += "\"_parent\":null,";
                } else {
                    result += "\"_parent\":\"" + BeanUtils.getProperty(o, "parentId") + "\",";
                }
                if (treeData.getValue(o).size() == 0) {
                    result += "\"_is_leaf\":true,";
                } else {
                    result += "\"_is_leaf\":false,";
                }
                if ("null"==(BeanUtils.getProperty(o, "grade"))||null==(BeanUtils.getProperty(o, "grade"))) {
                    result += "\"_grade\":1,";
                } else {
                    result += "\"_grade\":\"" + BeanUtils.getProperty(o, "grade") + "\",";
                }
                if ("".equals(BeanUtils.getProperty(o, "status"))) {
                    result += "\"_status\":1,";
                } else {
                    result += "\"_status\":\"" + BeanUtils.getProperty(o, "status") + "\"},";
                }
            }
            result = result.substring(0, result.length() - 1);
            result = result + "]}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        directOut(result);
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getChannels() {
        String parentId = getRequestParam("parentId", null);
        Map<String, Object> session = ActionContext.getContext().getSession();
        long cspId;
        TreeUtils tu = TreeUtils.getInstance();
        tu.initCache(Channel.class);
        if (parentId == null || "-1".equals(parentId) || parentId.equals("xnode-11")) {
            parentId = "-1";
        }
        if (session != null) {
            //Admin admin = (Admin) session.get("sessionOperator");
            if (admin != null && admin.getIsRoot() != 1) {
                AdminCsp adminCsp = new AdminCsp();
                adminCsp.setAdminId(admin.getId());
                List<AdminCsp> adminCsps = adminCspLogicInterface.search(adminCsp);
                if (adminCsps != null && adminCsps.size() > 0) {
                    objs = new ArrayList<Channel>();
                    for (int i = 0; i < adminCsps.size(); i++) {
                        cspId = adminCsps.get(i).getCspId();
                        if (cspId != 0 && cspId != -1) {
                            objs.addAll(tu.getSonOf(Channel.class, parentId, "cspId", String.valueOf(cspId)));
                        }
                    }
                }
            } else {
                objs = tu.getSonOf(Channel.class, parentId, null);
            }
        }
        for (Channel channel : objs) {
            List childs = tu.getSonOf(Channel.class, channel.getId(), "");
            if (childs != null && childs.size() > 0) {
                channel.setLeaf(false);
            } else {
                channel.setLeaf(true);
            }
        }
        if (objs == null) {
            objs = new ArrayList<Channel>();
        }
        return "getNodes";
    }

    public String updateChannelStatus(){
        log.debug("updateChannelStatus");
        String logInfo = admin.getLogin()+"("+admin.getRealname()+")";
        long status = getRequestIntParam("status",-1);
        long channelId = getRequestIntParam("channelId",-1);
        long grade = getRequestIntParam("grade",-1);
        if(channelId != -1){
            Channel cn  = channelLogicInterface.get(new Long(channelId));
            cn.setStatus(status);
            cn.setGrade(grade);
            channelLogicInterface.save(cn);
            logInfo += "将频道《"+cn.getName()+"》的状态改为"+status+"。将显示等级改为"+grade;
            log.debug("成功修改状态");
            writeSysLog(logInfo);
        }
      return Constants.ACTION_SAVE;
    }
    public String getSonOfChannelId() {
        String parentId = getRequestParam("parentChannelId", null);
        long cspId = getRequestIntParam("cspId", 0);
        Map<String, Object> session = ActionContext.getContext().getSession();
        TreeUtils tu = TreeUtils.getInstance();
        tu.initCache(Channel.class);
        objs = tu.getSonOf(Channel.class, parentId, null);
        if (objs == null) {
           objs = new ArrayList<Channel>();
         }
//
//       directOut(JsonUtils.getListJsonString("objs", objs, "totalCount", pageBean.getRowCount()));
        // directOut("123");
        return "list";
    }

    @SuppressWarnings("unused")
    public String getParent() {
        log.debug("in getParent");
        long parentId = getRequestIntParam("parentId", -1);
        if (parentId == -1) {
            obj.setId(-1);
            obj.setName("??");
        } else {
            obj = baseLogicInterface.get(parentId);
        }
        return Constants.ACTION_VIEW;
    }


    @SuppressWarnings("unused")
    public String searchChannelName() {
        obj = channelLogicInterface.get(channelId);
        return "view";
    }

    //查询出(管理员有的频道)的频道
    public String getChooseChannels() {
        String parentId = getRequestParam("parentId", null);
        String cspId_key = getRequestParam("keyId", null);
        Long cspId_Chaneel = Long.parseLong(cspId_key);
        Map<String, Object> session = ActionContext.getContext().getSession();
        TreeUtils tu = TreeUtils.getInstance();
        int cspId = 0;
        tu.initCache(Channel.class);

        if (parentId == null || "-1".equals(parentId) || parentId.equals("xnode-11")) {

            parentId = "-1";

        }
        //得到cspId下面的频道信息

        List<CspChannel> cspChannels = cspChannelLogicInterface.getCspChannelByCspId(cspId_Chaneel,-1);
        if (session != null) {
            objs = tu.getSonOf(Channel.class, parentId, "cspId", "1");
        }

        //获得叶子标示
        for (Channel channel : objs) {
            List childs = tu.getSonOf(Channel.class, channel.getId(), "");
            if (childs != null && childs.size() > 0) {
                channel.setLeaf(false);
            } else {
                channel.setLeaf(true);
            }
        }
        //获得选中标示 (需要的属性可以在实体类里面加)
        if (cspChannels.size() > 0) {
            for (int i = 0; i < objs.size(); i++) {
                Channel channel = objs.get(i);
                long channelId = channel.getId();
                for (int j = 0; j < cspChannels.size(); j++) {
                    long channelIdSelected = cspChannels.get(j).getChannelId();
                    if (channelId == channelIdSelected) {
                        channel.setChecked(true);
                        break;
                    } else {
                        channel.setChecked(false);
                    }
                }
            }
        } else {
            //由于数据库没有信息的时候可能他会知道去找缓存（避免缓存,不同的业务勾选会一样）
            for (int i = 0; i < objs.size(); i++) {
                Channel channel = objs.get(i);
                channel.setChecked(false);
            }
        }
        if (objs == null) {
            objs = new ArrayList<Channel>();
        }
        return "getCheckNodes";
    }

    private String channelTree;
    public String channelTree(){
        long parentId = obj.getId();
        if(parentId<=0){
            parentId = ConfigManager.getInstance().getConfig("system.defaultChannelRootId",-1L);
        }
        channelTree = JsonUtils.getJsonString(getTree(parentId));
        return "channelTree";
    }

    public List<Map<String,Object>> getTree(long parentId){
        List<Map<String,Object>> all = new ArrayList<Map<String, Object>>();

        List<Channel> channels = channelLogicInterface.getChannelList(parentId);
        for(Channel channel:channels){
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("id",channel.getId());
            result.put("name",channel.getName());
            result.put("auditFlag", channel.getAuditFlag());
            List<Map<String,Object>> children = getTree(channel.getId());
            if(children!=null&&children.size()>0){
                result.put("children",children);
            }
            all.add(result);
        }
        return all;
    }

    public String getChannelTree(){
        return channelTree;
    }

    // added by mlwang @2014-10-22
    // 同步客户端编辑的频道信息
    private String serialisedChannel;

    public String getSerialisedChannel() {
        return serialisedChannel;
    }

    public void setSerialisedChannel(String serialisedChannel) {
        this.serialisedChannel = serialisedChannel;
    }

    List<Channel> channelList;      // 要提交给Logic处理的频道信息
    @SuppressWarnings("unchecked")
    public String synchronize(){
        if(serialisedChannel != null){
            // 解析成json对象，放到数组中和数据库同步
            // serialisedChannel是一个以[]包裹的json数组
            log.debug(serialisedChannel);
            try{
                channelList = new ArrayList();
                //JSONObject channelJson = new JSONObject("{\"channels\":" + serialisedChannel + "}");
                JSONArray jsonArray = new JSONArray(serialisedChannel);
                log.debug("array length:" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    parseChannel(-1L, (JSONObject) jsonArray.get(i));
                }
                Integer cspId = admin.getCspId();
                if(cspId==null||cspId<=0){
                    cspId = 1;
                }
                String logs = channelLogicInterface.saveChannels(channelList,cspId);
                setSuccess(true);

                writeSysLog("成功保存了栏目："+logs);

                addActionError("搞定!");
            }catch(JSONException e){
                setSuccess(false);
                addActionError(e.getMessage());
            }
        }

        return ActionSupport.SUCCESS;
    }

    private void parseChannel(Long parentId, JSONObject channelObj){
        if( channelObj == null ) return;

        try{
            Long channelId = Long.parseLong(channelObj.get("id").toString());
            String channelName = "";
            try {
                channelName = java.net.URLDecoder.decode((String) channelObj.get("name"), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            int auditFlag = Integer.parseInt(channelObj.get("flag").toString());

            Channel channel = new Channel();
            channel.setId(channelId);
            channel.setName(channelName);
            channel.setAuditFlag(auditFlag);
            channel.setParentId(parentId);
            channelList.add(channel);

//            log.debug("把【" + channelName + "】添加到列表中了， 现在有：" + channelList.size() + " 个频道了。");

            // 解析子频道
            if(channelObj.has("children")){
                JSONArray jsonArray = new JSONArray(channelObj.get("children").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    parseChannel(channelId, (JSONObject) jsonArray.get(i));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
