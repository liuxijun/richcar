package com.fortune.rms.business.content.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.content.dao.daoInterface.ContentPropertyDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentChannelLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentChannel;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.content.model.EpisodeDTO;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertySelectLogicInterface;
import com.fortune.rms.business.module.model.Property;
//import com.fortune.rms.web.content.ContentAction;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.*;
import com.fortune.util.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("contentPropertyLogicInterface")
public class ContentPropertyLogicImpl extends BaseLogicImpl<ContentProperty>
        implements
        ContentPropertyLogicInterface {
    private ContentPropertyDaoInterface contentPropertyDaoInterface;
    private EncoderTaskLogicInterface encoderTaskLogicInterface;
    private PropertyLogicInterface propertyLogicInterface;
    private PropertySelectLogicInterface propertySelectLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private ContentChannelLogicInterface contentChannelLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;

/*
    private ContentAction contentAction;

    public ContentAction getContentAction() {
        return contentAction;
    }

    public void setContentAction(ContentAction contentAction) {
        this.contentAction = contentAction;
    }
*/

    /**
     * @param contentPropertyDaoInterface the contentPropertyDaoInterface to set
     */
    @Autowired
    public void setContentPropertyDaoInterface(
            ContentPropertyDaoInterface contentPropertyDaoInterface) {
        this.contentPropertyDaoInterface = contentPropertyDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentPropertyDaoInterface;

    }

    @Autowired
    public void setEncoderTaskLogicInterface(EncoderTaskLogicInterface encoderTaskLogicInterface) {
        this.encoderTaskLogicInterface = encoderTaskLogicInterface;
    }

    @Autowired
    public void setPropertyLogicInterface(PropertyLogicInterface propertyLogicInterface) {
        this.propertyLogicInterface = propertyLogicInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    @Autowired
    public void setPropertySelectLogicInterface(PropertySelectLogicInterface propertySelectLogicInterface) {
        this.propertySelectLogicInterface = propertySelectLogicInterface;
    }

    @Autowired
    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }

    public void removeByContentId(long contentId) {
        contentPropertyDaoInterface.executeUpdate("delete from ContentProperty cp where cp.contentId=" + contentId);
    }

    public List<ContentProperty> getContentProperties(long contentId, long propertyId) {
        ContentProperty cp = new ContentProperty();
        cp.setContentId(contentId);
        cp.setPropertyId(propertyId);
        return search(cp);
    }

    public ContentProperty getContentProperties(long Id) {
        return baseDaoInterface.get(Id);
    }

    public List<ContentProperty> getContentProperties(long contentId, long propertyId, long intValue) {
        return contentPropertyDaoInterface.getContentProperties(contentId, propertyId, intValue);
    }

    public List<ContentProperty> getContentProperties(long contentId, Long[] propertyIds) {
        return this.contentPropertyDaoInterface.getContentPropertiesByContentIdAndPropertyIds(contentId, propertyIds);
    }

    /**
     * 获取所有的content对应的媒体信息
     *
     * @param contentId         内容id
     * @param dataType          数据类型
     * @param fillEmptyProperty 是否填充没有数据的属性，例如某一个模板里定义的属性，但是没有保存，
     *                          返回列表中是否包含这个属性
     * @return 结果列表
     */
    public List<ContentProperty> getContentPropertiesByDataType(long moduleId, long contentId, Byte dataType, boolean fillEmptyProperty) {
        return getContentPropertiesByDataType(moduleId, contentId, dataType, fillEmptyProperty, false);
    }

    public boolean isSelectType(Property property) {
        Byte propertyDataType = property.getDataType();
        return (PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(propertyDataType) || PropertyLogicInterface.DATA_TYPE_COMBO.equals(propertyDataType) ||
                PropertyLogicInterface.DATA_TYPE_RADIO.equals(propertyDataType));
    }

    public String getDeviceName(Content content) {
        try {
            Device device = deviceLogicInterface.get(content.getDeviceId());
            return device.getName();
        } catch (Exception e) {
            logger.error("无法设置服务器信息，没有找到服务器：" + content.getDeviceId());
        }
        return "" + content.getDeviceId() + "(未找到)";
    }

    public String getSelectedValues(String idStr, Property property) {
        if (idStr == null) {
            return null;
        }
        String[] ids = idStr.split(";");
        String result = "";
        for (String val : ids) {
            if (val == null) continue;
            val = val.trim();
            if ("".equals(val)) {
                continue;
            }
            long selectId = StringUtils.string2long(val.trim(), -1);
            if (selectId > 0) {
                try {
                    PropertySelect select = propertySelectLogicInterface.get(selectId);
                    if (select != null) {
                        String name = select.getName();
                        if (name != null) {
                            result += name + ",";
                        }
                    }
                } catch (Exception e) {
                    logger.error("无法设置PropertySelect信息：" + e.getMessage());
                }
            }
        }
        if (result.length() > 2) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private void clonePropertyToContentProperty(ContentProperty cp,Property property){
        if(cp!=null&&property!=null){
            cp.setProperty(property);
            cp.setPropertyType(property.getDataType());
            cp.setColSpan(property.getColSpan());
            cp.setRowSpan(property.getRowSpan());
        }
    }
    public List<ContentProperty> getContentPropertiesByDataType(long moduleId, long contentId, Byte dataType, boolean fillEmptyProperty, boolean initSelectOptions) {
        List<ContentProperty> result = new ArrayList<ContentProperty>();
        Content content = null;

        if (contentId > 0) {
            try {
                content = contentLogicInterface.get(contentId);
            } catch (Exception e) {
                logger.error("无法找到媒体：id=" + contentId + "," +
                        "" + e.getMessage());
                //return result;
            }
        }
        Property property;
        if (moduleId <= 0) {
            if (content != null) {
                if(content.getModuleId()!=null){
                    moduleId = content.getModuleId();
                }
            }
        }
        if (moduleId <= 0) {
            moduleId = ConfigManager.getInstance().getConfig("system.default.moduleId",10000);
        }
        List<Property> properties = propertyLogicInterface.getPropertiesOfModule(moduleId,PropertyLogicInterface.STATUS_ON,dataType
                , new PageBean(0, 1000, "o1.displayOrder asc,o1.name", "asc"));
        int propertyCount = properties.size();
        Long[] propertyIds = new Long[propertyCount];
        for (int i = 0; i < propertyCount; i++) {
            property = properties.get(i);
            propertyIds[i] = property.getId();
        }
        List<ContentProperty> allData = contentPropertyDaoInterface.getContentPropertiesByContentIdAndPropertyIds(contentId, propertyIds);
        //遍历属性表，装配数据
        try {
            int nullDataId = -1;
            for (Property p : properties) {
                boolean hasFoundData = false;
                for (ContentProperty cp : allData) {
                    if (cp.getPropertyId() != null && p.getId() == cp.getPropertyId()) {
                        hasFoundData = true;
                        if (cp.getName() == null) cp.setName(p.getName());
                        setPropertyInfoToThumbPic(cp, p);
                        if (initSelectOptions) {
                            if ("DEVICE".equals(p.getCode())) {
                                cp.setStringValue(getDeviceName(content));
                            } else if (isSelectType(p)) {
                                cp.setStringValue(getSelectedValues(cp.getStringValue(), p));
                            }
                        }
                        clonePropertyToContentProperty(cp,p);
                        result.add(cp);
                    }
                }
                if (!hasFoundData) {
                    //先找找看，这个属性是否是主表中的
                    String value;
                    Byte isMain = p.getIsMain();
                    String columnName = p.getColumnName();
                    if (isMain != null && isMain == 1 && columnName != null
                            && !"".equals(columnName.trim())&&
                            !"CHANNEL_ID".equalsIgnoreCase(p.getCode())) {//如果是在主表中，必须要进行填充，而且不是channelId
                        try {
                            Object valueObj = BeanUtils.getProperty(content, p.getColumnName());
                            value = null == valueObj ? "" : valueObj.toString();
                            if (initSelectOptions) {
                                if ("DEVICE".equals(p.getCode())) {
                                    value = getDeviceName(content);
                                } else if (isSelectType(p)) {
                                    value = getSelectedValues(value, p);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("无法获取属性值：propertyName=" + p.getName() + "->colName=" + p.getColumnName());
                            value = "";
                        }
                    } else {
                        if("CHANNEL_ID".equalsIgnoreCase(p.getCode())&&contentId>0){//频道的数据来自ContentChannel
                            List<ContentChannel> channels = contentChannelLogicInterface.getContentPublishedChannels(contentId);
                            value = "";
                            for(ContentChannel cc:channels){
                                if(!"".equals(value)){
                                    value +=",";
                                }
                                value+=cc.getChannelId();
                            }
                        }else{
                            value = fillEmptyProperty ? "" : null;
                        }
                    }
                    if (value != null) {//无需填充，就放弃这个property
                        Byte propertyDataType = p.getDataType();
                        if(PropertyLogicInterface.DATA_TYPE_MP4.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_WMV.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FLV.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FILE_HTML.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FILE_ZIP.equals(propertyDataType)){
                            if("".equals(value)){
                                continue;//如果不是基础数据形态而是点播、外链资源，如果数据为空就放弃填充
                            }
                        }
                        ContentProperty cp = new ContentProperty();
                        cp.setId(nullDataId);
                        nullDataId--;
                        cp.setPropertyId(p.getId());
                        cp.setName(p.getName());
                        cp.setContentId(contentId);
                        cp.setStringValue(value);
                        cp.setIntValue(0L);
                        cp.setExtraData(p.getName());
                        setPropertyInfoToThumbPic(cp, p);
                        clonePropertyToContentProperty(cp,p);
                        result.add(cp);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public String removeParameters(String url,String[] parameters){
        String[] begins = new String[]{"&","?","&amp;"};
        for(String parameter:parameters){
            for(String begin:begins){
                String fullParameter = begin+parameter+"=";
                int p=url.indexOf(fullParameter);
                if(p>=0){
                    int p1=p+parameter.length()+1;
                    int l=url.length();
                    while(p1<l){
                        char ch = url.charAt(p1);
                        if(ch=='&'){
                            break;
                        }
                        p1++;
                    }
                    String temp = url;
                    if("?".equals(begin)){
                        p++;
                    }
                    url = temp.substring(0,p);
                    if(p1>=l){
                        continue;
                    }
                    url = url+temp.substring(p1);
                }
            }
        }
        return url.replace("?&","?");
    }

    public String setPropertyInfoToThumbPic(ContentProperty contentProperty, Property property) {
        if (contentProperty != null) {
            String thumbPic = contentProperty.getThumbPic();
            if (thumbPic == null) {
                thumbPic = "";
            }
            if (thumbPic.contains("?")) {
                thumbPic += "&";
            } else {
                thumbPic += "?";
            }
            thumbPic = removeParameters(thumbPic,new String[]{
                    "propertyId","propertyCode","propertyName","propertyType"
            });
            thumbPic += "propertyId=" + property.getId() + "&propertyCode=" + property.getCode() +
                    "&propertyName=" + property.getName() + "&propertyType=" + property.getDataType();
            contentProperty.setThumbPic(thumbPic);
            return thumbPic;
        }
        return null;
    }

    public int updateThumbPic(ContentProperty clip) {
        return contentPropertyDaoInterface.updateThumbPic(clip);
    }

    //修改海报的路径
    public int updateStringValue(String contentId, String propertyId, String stringValue) {
        return contentPropertyDaoInterface.updateStringValue(contentId, propertyId, stringValue);
    }

    public List<ContentProperty> getContentPropertiesByCache(long contentId, long intValue) {

//        List<ContentProperty> contentProperties = (List<ContentProperty>) CacheUtils.get(contentId+"_"+intValue,"countPropertiesCache",new DataInitWorker(){
//
//            public Object init(Object objKey, String cacheName) {
//                if(objKey != null){
//                    String[] str = objKey.toString().split("_");
//                    if(str.length == 2){
//                        long str1;
//                        long str2;
//                        try{
//                            str1 = Long.valueOf(str[0]);
//                            str2 = Long.valueOf(str[1]);
//                        }catch(Exception e){
//                            str1 = -1;
//                            str2 = -1;
//                        }
//                        if(str1 != -1 &&str2 != -1 ){
//                            ContentProperty cp = new ContentProperty();
//                            cp.setContentId(str1);
//                            cp.setPropertyId(str2);
//
//                            try {
//                                return contentPropertyDaoInterface.getObjects(cp);
//                            } catch (Exception e) {
//                                logger.error("获取contentProperty 失败！");
//                            }
//                        }
//                    }
//                }
//                return null;
//            }
//        });
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<ContentProperty> getContentPropertiesByCache(ContentProperty contentProperty) {
        Long contentId = contentProperty.getContentId();
        Long intValue = contentProperty.getIntValue();
        return (List<ContentProperty>) CacheUtils.get(contentId + "_" + intValue, "contentPropertiesCountCache", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                if (objKey != null) {
                    String[] objKeyString = objKey.toString().split("_");
                    if (objKeyString.length == 2) {
                        long contentId = StringUtils.string2long(objKeyString[0], -1);
                        long clipNumber = StringUtils.string2long(objKeyString[1], -1);
                        if (contentId > 0) {
                            ContentProperty cp = new ContentProperty();
                            cp.setContentId(contentId);
                            cp.setIntValue(clipNumber);
                            try {
                                return search(cp);
                            } catch (Exception e) {
                                logger.error("发生错误！");
                            }
                        }

                    }
                }
                return null;
            }
        });
    }

    public ContentProperty getContentProperty(long contentId, long propertyId) {
        List<ContentProperty> cps = getContentProperties(contentId, propertyId);
        if (cps != null && cps.size() > 0) {
            return cps.get(0);
        }
        return null;
    }

    public int getContentFreeCountByCache(final long contentId, final long propertyId) {
        Integer freeCount = (Integer) CacheUtils.get(contentId + "_" + propertyId, "contentFreeCountCache", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                if (objKey != null) {
                    String[] str = objKey.toString().split("_");
                    if (str.length == 2) {
                        long str1;
                        long str2;
                        try {
                            str1 = Long.valueOf(str[0]);
                            str2 = Long.valueOf(str[1]);
                        } catch (Exception e) {
                            str1 = -1;
                            str2 = -1;
                        }
                        if (str1 != -1 && str2 != -1) {
                            ContentProperty cp = getContentProperty(str1, str2);
                            if (cp != null) {
                                return StringUtils.string2int(cp.getStringValue(), 0);
                            }
                        }

                    }
                }
                return null;
            }
        });
        if (freeCount == null) {
            freeCount = -1;
        }
        return freeCount;
    }

    public long getContentIntValueByCache(final long contentPropertyId) {
        Long intValue = (Long) CacheUtils.get(contentPropertyId, "contentIntValueCache", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                if (objKey != null) {
                    try {
                        ContentProperty contentProperty = get((Long) objKey);
                        if (contentProperty != null) {
                            return contentProperty.getIntValue();
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }


                }
                return null;
            }
        });
        if (intValue == null) {
            intValue = -1l;
        }
        return intValue;
    }

    public boolean existsSubContentId(String subContentId) {
        return this.contentPropertyDaoInterface.existsSubContentId(subContentId);
    }

    public ContentProperty getContentPropertyBySubContentId(String subContentId) {
        return this.contentPropertyDaoInterface.getContentPropertyBySubContentId(subContentId);
    }

    public void updateContentPropertyBySubContentId(ContentProperty contentProperty) {
        this.contentPropertyDaoInterface.updateContentPropertyBySubContentId(contentProperty);
    }

    public void removeContentPropertyBySubContentId(ContentProperty contentProperty) {
        this.contentPropertyDaoInterface.removeContentPropertyBySubContentId(contentProperty);
    }

    public List<ContentProperty> setContentFromContentProperties(Content content, List<ContentProperty> contentProperties, List<ContentProperty> willRemoveProperties) {
        List<ContentProperty> tempList = new ArrayList<ContentProperty>();
        //扫描一遍，把主表中的数据设置好
        for (ContentProperty cp : contentProperties) {
            Long propertyId = cp.getPropertyId();
            boolean shouldCopy = true;
            if (propertyId != null) {
                Property property = propertyLogicInterface.getPropertyByCache(propertyId);
                if (property != null) {
                    Byte isMain = property.getIsMain();
                    if (isMain != null && isMain.intValue() == 1) {
                        String colName = property.getColumnName();
                        if (colName != null) {
                            String val = cp.getStringValue();
                            if("".equals(val)){
                                val = null;
                            }
                            shouldCopy = !com.fortune.util.BeanUtils.setProperty(content, colName, val);
                        }
                    }
                }
            }
            if (shouldCopy) {
                tempList.add(cp);
            } else {
                willRemoveProperties.add(cp);
            }
        }
        return tempList;
    }

    public Content saveClips(List<ContentProperty> clips, Content content, long encoderId) {
//        long isContentId=content.getId();
        long contentId = content.getId();
        String resourceKey = "contentId=" + contentId;
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        try {
            threadUtils.acquire(resourceKey);
            List<ContentProperty> oldData;
            if (contentId > 0) {
                oldData = getContentProperties(content.getId(), -1);
            } else {
                logger.error("媒体保存意外发生：" + content.getName() + ",请检查！");
                oldData = new ArrayList<ContentProperty>();
            }
            if(content.getDeviceId()==null||content.getDeviceId()<=0){
                List<Device> devices = deviceLogicInterface.getDevicesOfType(DeviceLogicInterface.DEVICE_TYPE_HLS_VOD,DeviceLogicInterface.DEVICE_ONLINE,-1);
                if(devices!=null&&devices.size()>0){
                    content.setDeviceId(devices.get(0).getId());
                }
            }
            if (contentId > 0) {
                try {
                    Content oldContent = contentLogicInterface.get(contentId);
                    if (oldContent != null) {
                        //cspId一直保留老的，防止发生意外情况被覆盖掉。
                        Long cspId = oldContent.getCspId();
                        if(cspId==null){
                            cspId = content.getCspId();
                        }
                        if(cspId == null){
                            cspId= 1L;
                        }
                        BeanUtils.copyPropertiesIfDesertIsNull(oldContent, content);
                        content.setCspId(cspId);
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
            if (content.getCreateTime() == null) {
                logger.debug("新建媒体，设置createTime");
                content.setCreateTime(new Date());
            }
            if (content.getValidEndTime() == null) {
                Date endDate = new Date(System.currentTimeMillis() + 3650 * 24 * 3600 * 1000L);
                logger.debug("没有版权信息，设置版权截至日期：" + StringUtils.date2string(endDate));
                content.setValidEndTime(endDate);
            }
            if (content.getValidStartTime() == null) {
                Date beginDate = new Date();
                logger.debug("没有版权信息，设置版权起始日期：" + StringUtils.date2string(beginDate));
                content.setValidStartTime(beginDate);
            }
            List<ContentProperty> willSaveProperties = setContentFromContentProperties(content, clips);
            //content.setDeviceId(deviceId);
            content = contentLogicInterface.save(content);
            for (ContentProperty old : oldData) {
                if (old == null) continue;
                boolean shouldRemove = true;
                if (clips != null) {
                    for (ContentProperty cp : willSaveProperties) {
                        if (cp != null) {
                            if (old.getId() == cp.getId()) {
                                shouldRemove = false;
                                BeanUtils.copyPropertiesIfDesertIsNull(old, cp);
                                break;
                            }
                        }
                    }
                }
                if (shouldRemove) {
                    //如果数据在新提交上来的数据中没有，就要考虑删除。如果这个是转码后的媒体链接，就应该保存
                    //但这个机会只有这一次，下次要删除的话，就真的删了。
                    // 如果数据真的要删除，要考虑一起删除媒体文件。
                    Long propertyId = old.getPropertyId();
                    Property property = null;
                    if(EXTRA_INT_ENCODE_FINISHED.equals(old.getExtraInt())){
                        if(property!=null){
                            logger.warn("《" +content.getName()+"》(" +content.getId()+")提交上来的数据中没有这个属性："+property.getName()+"(" +propertyId+
                                    "),但我发现这个属性是刚刚转码出来的，可能在转码期间，管理员编辑了这个媒体！所以不删除这个媒体！");
                        }else{
                            logger.warn("《" +content.getName()+"》(" +content.getId()+")提交上来的数据中没有这个属性：propertyId=(" +propertyId+
                                    "),但我发现这个属性是刚刚转码出来的，可能在转码期间，管理员编辑了这个媒体！所以不删除这个媒体！");
                        }
                        old.setExtraInt(EXTRA_INT_HAS_ENCODED);
                        save(old);
                    }else{
                        //
                        if(property!=null){
                            logger.warn("《" +content.getName()+"》(" +content.getId()+")提交上来的数据中没有这个属性了："+property.getName()+"(" +propertyId+
                                    "),将执行删除操作："+old.getSimpleJson());
                        }else{
                            propertyId=null;
                        }
                        if(propertyId==null){
                            logger.warn("《" +content.getName()+"》(" +content.getId()+")提交上来的数据中没有这个属性了：propertyId="+old.getPropertyId()+
                                    ",将执行删除操作："+old.getSimpleJson());
                        }
                        remove(old);
                    }
                }
            }
            boolean startEncodeTask = AppConfigurator.getInstance().getBoolConfig("system.content.startEncodeTaskWhenSaveContent", true);
            boolean encodingTaskAdded = false;
            long clipNo = 1;
            for (ContentProperty cp : willSaveProperties) {
                String oldStringValue = "";
                if (cp.getId() > 0) {
                    try {
                        ContentProperty oldClipInfo = getContentProperties(cp.getId());
                        oldStringValue = oldClipInfo.getStringValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                cp.setContentId(content.getId());
                cp = save(cp);
                //如果不需要启动转码任务，就跳过后面的逻辑
                if (!startEncodeTask) {
                    logger.debug("根据设置，在保存时无需进行转码任务的自动添加！");
                    continue;
                }
                Long propertyId = cp.getPropertyId();
                if (propertyId != null) {
                    Property property = null;
                    if(propertyId!=null){
                        try {
                            property = propertyLogicInterface.getPropertyByCache(propertyId);
                        } catch (Exception e) {
                            logger.error("无法通过propertyID获取Property,尝试通过约定的ID来获取："+propertyId);
                            if(propertyId==1L){
                                property = propertyLogicInterface.getByCode("Media_Url_Source");
                            }else if(propertyId==2L){
                                property = propertyLogicInterface.get("CHANNEL_ID");
                            }
                        }
                    }
                    if (property != null) {
                        if (cp.getExtraInt() == null) {
                            cp.setExtraInt(-1L);
                        }
                        if (("Media_Url_Source".equals(property.getCode()))){
                            //20141029加入检查是否是直播，如果是直播，就放弃转码
                            Device device = deviceLogicInterface.get(content.getDeviceId());
                            boolean shouldStartTask;
                            if(device!=null&&DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE == device.getType()){
                                shouldStartTask = false;
                            }else{
                                String fileName = cp.getStringValue();
                                if(device!=null){
                                    fileName=device.getUrl()+"/"+fileName;
                                }
                                if(fileName.contains(".m3u8")||fileName.contains(".M3U8")||fileName.contains("z.m3u8")||fileName.contains("live/")||fileName.contains("hls/")){
                                    shouldStartTask = false;
                                }else{
                                    fileName = FileUtils.extractFileName(fileName,"/");
                                    shouldStartTask = false;
                                    for(int i=0,l=fileName.length();i<l;i++){
                                        char ch = fileName.charAt(i);
                                        if(ch<'0'||ch>'9'){
                                            //直播的连接都是数字，如果不是数字，就有问题了
                                            shouldStartTask = true;
                                            break;
                                        }
                                    }
                                }

                            }
                            //20141029加入结束
                            if(cp.getIntValue()==null){
                                cp.setIntValue(clipNo);
                            }
                            clipNo++;
                            //得到以前的视频地址和现在的对比
                            if (shouldStartTask&&(!(EXTRA_INT_HAS_ENCODED == cp.getExtraInt()) || !oldStringValue.equals(cp.getStringValue()))){
                                FileType fileType = FileUtils.getFileType(cp.getStringValue());
                                if(FileType.video.equals(fileType)||FileType.sound.equals(fileType)){
                                    logger.debug("媒体文件：《" + content.getName() + "(" + cp.getIntValue() +
                                            ":" + cp.getName() +
                                            ")》连接《" + cp.getStringValue() +
                                            "》视频地址发生变化，或者视频没有转码重新启动转码");
                                    cp.setExtraInt(EXTRA_INT_HAS_ENCODED);
                                    encodingTaskAdded = true;
                                    encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content, cp);
                                }else{
                                    logger.warn("媒体文件：《" + content.getName() + "(" + cp.getIntValue() +
                                            ":" + cp.getName() +
                                            ")》连接《" + cp.getStringValue() +
                                            "》文件类型是："+fileType+",无需进行转码！");
                                }
                                //save(cp);//20140912，在encoderTaskLogicInterface.createEncoderTaskForAllTemplate方法里已经保存了，这里就不再保存
                            }
                        }
                    }
                }
            }
            if(encodingTaskAdded){
                //如果不播放源，就要等源都已经转码完毕才能播
                if(!AppConfigurator.getInstance().getBoolConfig("system.playMediaUrlSource",true)){
                    content.setStatus(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE);
                    contentLogicInterface.setContentStatus(content,ContentLogicInterface.STATUS_WAITING_FOR_ENCODE);
                }
            }
        } catch (Exception e) {
            logger.error("保存影片信息是发生异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        return content;
    }

    public List<ContentProperty> setContentFromContentProperties(Content content, List<ContentProperty> contentProperties) {
        List<ContentProperty> willRemoveProperties = new ArrayList<ContentProperty>();
        return setContentFromContentProperties(content, contentProperties, willRemoveProperties);
/*
        if(result!=null){
            for(ContentProperty cp:willRemoveProperties){
                cp.setContentId(0-content.getId());
                result.add(cp);
            }
        }
*/
        //return result;
    }

    public List<ContentProperty> fillEmptyPoster() {
        List<ContentProperty> cutImage = new ArrayList<ContentProperty>();
        return cutImage;
    }
    //根据海报处理确实海报

    public List<ContentProperty> fillEmptyPoster(String handleImageFile, long moduleId, List<ContentProperty> contentProperties, long cspId) {
        HttpServletRequest request = ServletActionContext.getRequest();
        List<ContentProperty> allPosterProperties;

        //根据模版得到海报信息
        byte dataType = ContentPropertyLogicInterface.IMAGE_TYPE;
        Boolean shouldAddThisPoster;
        allPosterProperties = getContentPropertiesByDataType(moduleId, -1, dataType, true, false);
        //得到所有当前模版的海报内容，对比前台页面那些还好是没有值的
        for (ContentProperty posterProperty : allPosterProperties) {
            shouldAddThisPoster = true; //判断完后要把值该回来不然会出问题
            for (ContentProperty dataFromClientSideSaving : contentProperties) {
                if (posterProperty.getPropertyId().equals(dataFromClientSideSaving.getPropertyId())) {
                    logger.warn("有相同的数据跳出");
                    shouldAddThisPoster = false;
                    break;
                }
                //5050是影片的名字
//              if(dataFromClientSideSaving.getPropertyId()==5050){
//                  moveName=dataFromClientSideSaving.getStringValue();
//              }
            }
            if (shouldAddThisPoster) {
                //如果这个海报没有就要切割海报 （首先要判断海报是否带有海报大写(100*100)）
                Long propertyId = posterProperty.getPropertyId();
                String posterPropertyName = posterProperty.getName();
                if (propertyId <= 0) {
                    logger.error("属性信息设置错误，无法进行海报自动生成：" + posterProperty.getName());
                    continue;
                }
                Property property = propertyLogicInterface.getPropertyByCache(propertyId);
                if (property != null) {
                    posterPropertyName = property.getName();
                }
                //判断海报名字后面是否存在分割海报的大小
                String s_str = "[*]";
                Pattern p = Pattern.compile(s_str);
                Matcher m = p.matcher(posterPropertyName);
                //boolean rs = m.find();
                if (m.find()) {
                    logger.debug("存在处理海报的大小格式");
                    String[] posterPropertyNameArray = posterPropertyName.split("[（|(]");
                    String str2 = posterPropertyNameArray[1];
                    String[] str3 = str2.split("[*|*]");
                    String str4 = str3[0];//高
                    String str5 = str3[1];
                    String str6[] = str5.split("[)|）]");
                    String str7 = str6[0];//宽
                    int width = Integer.parseInt(str4);
                    int height = Integer.parseInt(str7);
                    File handleImage = new File(request.getRealPath(handleImageFile));
                    //得到分割海报的名字
                    String[] strImage = handleImageFile.split("[.]");
                    String postfix = strImage[1];
                    //创建生成海报的位置
                    String targetFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" +
                            Math.round(Math.random() * 100000) + "_" + new Date().getTime() % 10000 + "_" + width + "." + postfix;
                    File saveToFileName = new File(request.getRealPath(targetFilePath));
                    try {
                        //根据目标海报创建海报
                        FileUtils.createFixedBoundImg(handleImage.toString(), saveToFileName.toString(), height, width);
                        //ImageControl imageControl = new ImageControl();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //上传成功后，需要同步到其他的几台机器上 （这地方值需要添加数据）
                    try {
                        JsUtils syncUtils = new JsUtils();
                        syncUtils.saveAndPushSynFile(saveToFileName.getName(), saveToFileName.getAbsolutePath(), targetFilePath, cspId);
                    } catch (Exception e) {
                        logger.error("无法同步文件：" + saveToFileName);
                    }
                    SimpleFileInfo fileInfo = new SimpleFileInfo(saveToFileName.getAbsolutePath(), saveToFileName.length(),
                            new Date(saveToFileName.lastModified()), false, FileType.image);
                    FileUtils.setFileMediaInfo(saveToFileName.getAbsolutePath(), fileInfo);
                    posterProperty.setStringValue(targetFilePath);
                    contentProperties.add(posterProperty);
                }
            }
        }
        return contentProperties;
    }

    /**
     * 获取视频的分集信息
     * @param contentId 视频Id
     * @return 分集信息
     */
    public List<EpisodeDTO> getContentEpisodes(long contentId){
        ContentProperty cp = new ContentProperty();
        Property property = propertyLogicInterface.getByCode("Media_Url_Source");
        cp.setContentId(contentId);
        if(property!=null){
            cp.setPropertyId(property.getId());     // 只取源的信息，url同样在播放时再获取
        }else{
            cp.setPropertyId(AppConfigurator.getInstance().getLongConfig("system.default.mediaUrlPropertyId",1L));
        }

        List<ContentProperty> propertyList = null;
        try {
            propertyList = contentPropertyDaoInterface.getObjects(cp,
                    new PageBean(1, 9999, "o1.intValue", "asc"));
        }catch(Exception e){e.printStackTrace();}
        if( propertyList == null) return  null;

        ArrayList<EpisodeDTO> episodeList = new ArrayList<EpisodeDTO>();
        for(ContentProperty p : propertyList){
            episodeList.add(new EpisodeDTO(p));
        }

        return episodeList;
    }

    public ContentProperty getContentDownLoadUrl(long contentId,long intValue,long propertyId) {
         return contentPropertyDaoInterface.getContentDownLoadUrl(contentId,intValue,propertyId);
    }

    public long guessContentPropertyId(final long contentId,final int willClipId,String propertyCodeParameter,
                                       final ContentPropertyLogicInterface contentPropertyLogicInterface,
                                       final PropertyLogicInterface propertyLogicInterface){
        if(propertyCodeParameter==null||propertyCodeParameter.equals("null")){
            propertyCodeParameter = "Media_Url_Source";
        }
        final String propertyCode = propertyCodeParameter;
        if(contentId<=0||willClipId<0){
            logger.error("输入参数不正常：contentId="+contentId+",clipId="+willClipId+",propertyCode="+propertyCode);
            return -1L;
        }
        String key = "contentId"+contentId+"_clipId"+willClipId+"_p"+propertyCode;
        logger.debug("尝试获取contentPropertyId，参数为："+key);
        Object result =  CacheUtils.get(key,"contentPropertyIdCache",new DataInitWorker(){
            public long scanList(List<ContentProperty> sourceClips,int willClipId,ContentProperty cp,boolean isResultList){
                logger.debug("查询clips结果：contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount="+sourceClips.size());
                int clipCount = sourceClips.size();
                int clipIntValue = willClipId;
                Long firstIntValue=-1L;
                if(clipCount>0){
                    //如果影片的第一集从0开始，那么，就要将clipId减去1，因为默认是从1开始的
                    ContentProperty firstClip = sourceClips.get(0);
                    if(firstClip!=null){
                        firstIntValue = firstClip.getIntValue();
                        if(firstIntValue!=null){
                            if(firstIntValue==0){
                                if(clipIntValue>0){
                                    logger.warn("第一集从0开始的，应该从1开始");
                                    clipIntValue--;
                                }else if(clipIntValue==0){
                                    logger.debug("找第一集的来了");
                                }else{
                                    clipIntValue = 0;
                                }
                            }else{
                                logger.debug("第一集的起始集数是："+firstIntValue);
                            }
                        }else{
                            logger.error("第一集的起始集数为空，这是错误的！");
                        }
                    }else{
                        logger.error("第一集是空的，发生严重问题！");
                    }
                    //如果只有一集
                    if(clipIntValue<0){
                        logger.debug("集数居然出现负值，重置为0");
                        clipIntValue = 0;
                    }
                    if(firstIntValue==null){
                        firstIntValue = 0L;
                    }
                    if(clipIntValue<firstIntValue.intValue()){
                        clipIntValue = firstIntValue.intValue();
                    }
                    if(clipCount==1){
                        if(isResultList){
                            return sourceClips.get(0).getId();
                        }else{
                            return getClipId(sourceClips.get(0), propertyCode,propertyLogicInterface,contentPropertyLogicInterface);
                        }
                    }
                    //扫描一遍所有的影片源片段，防止在缺集的情况下出现异常
                    for(ContentProperty clip:sourceClips){
                        Long intValue = clip.getIntValue();
                        if(intValue==null){
                            intValue = 1L;
                        }
                        if(clipCount<intValue){
                            clipCount = intValue.intValue();
                        }
                        if(clipIntValue==intValue.intValue()){
                            if(isResultList){
                                return clip.getId();
                            }else{
                                return getClipId(clip, propertyCode,propertyLogicInterface,contentPropertyLogicInterface);
                            }
                        }
                    }
                }else{
                    logger.error("没有找到任何的影片播放源数据：contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount=0");
                }
                return -1;
            }
            public Object init(Object key,String cacheName){
                Long result = -1L;
                logger.debug("准备查询属性：Media_Url_Source");
                Property sourceProperty = propertyLogicInterface.getByCode("Media_Url_Source");
                if(sourceProperty==null||sourceProperty.getId()<=0){
                    sourceProperty = propertyLogicInterface.getByCode("Media_Url_768K");
                }
                if(sourceProperty!=null&&sourceProperty.getId()>0){
                    ContentProperty cp = new ContentProperty();
                    cp.setContentId(contentId);
                    cp.setPropertyId(sourceProperty.getId());
                    PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.intValue","asc");
                    logger.debug("准备查询clips：contentId="+contentId+",propertyId="+cp.getPropertyId());
                    List<ContentProperty> sourceClips = contentPropertyLogicInterface.search(cp, pageBean);
                    if(sourceClips!=null&&sourceClips.size()>0){
                        return scanList(sourceClips,willClipId,cp,false);
                    }else{
                        logger.debug("没有找到任何的影片播放源数据：contentId=" + contentId + ",propertyId=" + cp.getPropertyId()+","+sourceProperty.getCode());
                    }
                }
                {
                    //尝试从bandwidth这个参数再搜索
                    logger.debug("尝试从bandwidth="+propertyCode+"来查找影片片短....");
                    Property property = propertyLogicInterface.getByCode(propertyCode);
                    if(property!=null){
                        ContentProperty cp = new ContentProperty();
                        cp.setContentId(contentId);
                        cp.setPropertyId(property.getId());
                        PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.intValue","asc");
                        List<ContentProperty> clips = contentPropertyLogicInterface.search(cp, pageBean);
                        if(clips!=null&&clips.size()>0){
                            logger.debug("找到了"+clips.size()+"条记录！");
                            return scanList(clips,willClipId,cp,true);

/*
                            if(willClipId>0){
                                return clips.get(0).getId();
                            }else{
                                for(ContentProperty clip:clips){
                                    if(clip!=null&&clip.getIntValue().intValue()==willClipId){
                                        return clip.getId();
                                    }
                                }
                            }
*/
                        }else{
                            logger.error("没有找到影片片段信息：contentId="+contentId+",propertyCode="+propertyCode+",propertyId="+property.getId());
                        }
                    }else{
                        logger.error("没有找到影片片段对应的属性：contentId="+contentId+",code="+propertyCode);
                    }
                }
                return result;
            }
        });
        if(result ==null){
            return -1L;
        }
        if(result instanceof Long){
            return (Long) result;
        }
        if(result instanceof Integer){
            return ((Integer)result).longValue();
        }
        return StringUtils.string2long(result.toString(), -1L);
    }
    public Long getClipId(ContentProperty sourceClip,String propertyCode,PropertyLogicInterface propertyLogicInterface,
                          ContentPropertyLogicInterface contentPropertyLogicInterface){
        if(sourceClip!=null){
            if("Media_Url_Source".equals(propertyCode)){
                logger.debug("这是找源播放链接："+propertyCode);
                return sourceClip.getId();
            }else{
                Property property = propertyLogicInterface.getByCode(propertyCode);
                if(property!=null){
                    logger.debug("准备查找播放链接："+propertyCode+","+property.getName());
                    ContentProperty searchBean = new ContentProperty();
                    searchBean.setPropertyId(property.getId());
                    searchBean.setIntValue(sourceClip.getIntValue());
                    searchBean.setContentId(sourceClip.getContentId());
                    List<ContentProperty> clips = contentPropertyLogicInterface.search(searchBean);
                    if(clips!=null&&clips.size()>0){
                        ContentProperty clip = clips.get(0);
                        if(clip!=null){
                            return clip.getId();
                        }
                    }
                }else{
                    logger.error("准备查找播放链接"+propertyCode+"时发生异常，没找到对应的Property");
                }
            }
        }
        return -1L;
    }

}
