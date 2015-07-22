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
     * ��ȡ���е�content��Ӧ��ý����Ϣ
     *
     * @param contentId         ����id
     * @param dataType          ��������
     * @param fillEmptyProperty �Ƿ����û�����ݵ����ԣ�����ĳһ��ģ���ﶨ������ԣ�����û�б��棬
     *                          �����б����Ƿ�����������
     * @return ����б�
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
            logger.error("�޷����÷�������Ϣ��û���ҵ���������" + content.getDeviceId());
        }
        return "" + content.getDeviceId() + "(δ�ҵ�)";
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
                    logger.error("�޷�����PropertySelect��Ϣ��" + e.getMessage());
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
                logger.error("�޷��ҵ�ý�壺id=" + contentId + "," +
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
        //�������Ա�װ������
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
                    //�����ҿ�����������Ƿ��������е�
                    String value;
                    Byte isMain = p.getIsMain();
                    String columnName = p.getColumnName();
                    if (isMain != null && isMain == 1 && columnName != null
                            && !"".equals(columnName.trim())&&
                            !"CHANNEL_ID".equalsIgnoreCase(p.getCode())) {//������������У�����Ҫ������䣬���Ҳ���channelId
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
                            logger.error("�޷���ȡ����ֵ��propertyName=" + p.getName() + "->colName=" + p.getColumnName());
                            value = "";
                        }
                    } else {
                        if("CHANNEL_ID".equalsIgnoreCase(p.getCode())&&contentId>0){//Ƶ������������ContentChannel
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
                    if (value != null) {//������䣬�ͷ������property
                        Byte propertyDataType = p.getDataType();
                        if(PropertyLogicInterface.DATA_TYPE_MP4.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_WMV.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FLV.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FILE_HTML.equals(propertyDataType)||
                                PropertyLogicInterface.DATA_TYPE_FILE_ZIP.equals(propertyDataType)){
                            if("".equals(value)){
                                continue;//������ǻ���������̬���ǵ㲥��������Դ���������Ϊ�վͷ������
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
            logger.error("�����쳣��" + e.getMessage());
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

    //�޸ĺ�����·��
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
//                                logger.error("��ȡcontentProperty ʧ�ܣ�");
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
                                logger.error("��������");
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
        //ɨ��һ�飬�������е��������ú�
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
                logger.error("ý�屣�����ⷢ����" + content.getName() + ",���飡");
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
                        //cspIdһֱ�����ϵģ���ֹ����������������ǵ���
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
                logger.debug("�½�ý�壬����createTime");
                content.setCreateTime(new Date());
            }
            if (content.getValidEndTime() == null) {
                Date endDate = new Date(System.currentTimeMillis() + 3650 * 24 * 3600 * 1000L);
                logger.debug("û�а�Ȩ��Ϣ�����ð�Ȩ�������ڣ�" + StringUtils.date2string(endDate));
                content.setValidEndTime(endDate);
            }
            if (content.getValidStartTime() == null) {
                Date beginDate = new Date();
                logger.debug("û�а�Ȩ��Ϣ�����ð�Ȩ��ʼ���ڣ�" + StringUtils.date2string(beginDate));
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
                    //������������ύ������������û�У���Ҫ����ɾ������������ת����ý�����ӣ���Ӧ�ñ���
                    //���������ֻ����һ�Σ��´�Ҫɾ���Ļ��������ɾ�ˡ�
                    // ����������Ҫɾ����Ҫ����һ��ɾ��ý���ļ���
                    Long propertyId = old.getPropertyId();
                    Property property = null;
                    if(EXTRA_INT_ENCODE_FINISHED.equals(old.getExtraInt())){
                        if(property!=null){
                            logger.warn("��" +content.getName()+"��(" +content.getId()+")�ύ������������û��������ԣ�"+property.getName()+"(" +propertyId+
                                    "),���ҷ�����������Ǹո�ת������ģ�������ת���ڼ䣬����Ա�༭�����ý�壡���Բ�ɾ�����ý�壡");
                        }else{
                            logger.warn("��" +content.getName()+"��(" +content.getId()+")�ύ������������û��������ԣ�propertyId=(" +propertyId+
                                    "),���ҷ�����������Ǹո�ת������ģ�������ת���ڼ䣬����Ա�༭�����ý�壡���Բ�ɾ�����ý�壡");
                        }
                        old.setExtraInt(EXTRA_INT_HAS_ENCODED);
                        save(old);
                    }else{
                        //
                        if(property!=null){
                            logger.warn("��" +content.getName()+"��(" +content.getId()+")�ύ������������û����������ˣ�"+property.getName()+"(" +propertyId+
                                    "),��ִ��ɾ��������"+old.getSimpleJson());
                        }else{
                            propertyId=null;
                        }
                        if(propertyId==null){
                            logger.warn("��" +content.getName()+"��(" +content.getId()+")�ύ������������û����������ˣ�propertyId="+old.getPropertyId()+
                                    ",��ִ��ɾ��������"+old.getSimpleJson());
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
                //�������Ҫ����ת�����񣬾�����������߼�
                if (!startEncodeTask) {
                    logger.debug("�������ã��ڱ���ʱ�������ת��������Զ���ӣ�");
                    continue;
                }
                Long propertyId = cp.getPropertyId();
                if (propertyId != null) {
                    Property property = null;
                    if(propertyId!=null){
                        try {
                            property = propertyLogicInterface.getPropertyByCache(propertyId);
                        } catch (Exception e) {
                            logger.error("�޷�ͨ��propertyID��ȡProperty,����ͨ��Լ����ID����ȡ��"+propertyId);
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
                            //20141029�������Ƿ���ֱ���������ֱ�����ͷ���ת��
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
                                            //ֱ�������Ӷ������֣�����������֣�����������
                                            shouldStartTask = true;
                                            break;
                                        }
                                    }
                                }

                            }
                            //20141029�������
                            if(cp.getIntValue()==null){
                                cp.setIntValue(clipNo);
                            }
                            clipNo++;
                            //�õ���ǰ����Ƶ��ַ�����ڵĶԱ�
                            if (shouldStartTask&&(!(EXTRA_INT_HAS_ENCODED == cp.getExtraInt()) || !oldStringValue.equals(cp.getStringValue()))){
                                FileType fileType = FileUtils.getFileType(cp.getStringValue());
                                if(FileType.video.equals(fileType)||FileType.sound.equals(fileType)){
                                    logger.debug("ý���ļ�����" + content.getName() + "(" + cp.getIntValue() +
                                            ":" + cp.getName() +
                                            ")�����ӡ�" + cp.getStringValue() +
                                            "����Ƶ��ַ�����仯��������Ƶû��ת����������ת��");
                                    cp.setExtraInt(EXTRA_INT_HAS_ENCODED);
                                    encodingTaskAdded = true;
                                    encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content, cp);
                                }else{
                                    logger.warn("ý���ļ�����" + content.getName() + "(" + cp.getIntValue() +
                                            ":" + cp.getName() +
                                            ")�����ӡ�" + cp.getStringValue() +
                                            "���ļ������ǣ�"+fileType+",�������ת�룡");
                                }
                                //save(cp);//20140912����encoderTaskLogicInterface.createEncoderTaskForAllTemplate�������Ѿ������ˣ�����Ͳ��ٱ���
                            }
                        }
                    }
                }
            }
            if(encodingTaskAdded){
                //���������Դ����Ҫ��Դ���Ѿ�ת����ϲ��ܲ�
                if(!AppConfigurator.getInstance().getBoolConfig("system.playMediaUrlSource",true)){
                    content.setStatus(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE);
                    contentLogicInterface.setContentStatus(content,ContentLogicInterface.STATUS_WAITING_FOR_ENCODE);
                }
            }
        } catch (Exception e) {
            logger.error("����ӰƬ��Ϣ�Ƿ����쳣��" + e.getMessage());
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
    //���ݺ�������ȷʵ����

    public List<ContentProperty> fillEmptyPoster(String handleImageFile, long moduleId, List<ContentProperty> contentProperties, long cspId) {
        HttpServletRequest request = ServletActionContext.getRequest();
        List<ContentProperty> allPosterProperties;

        //����ģ��õ�������Ϣ
        byte dataType = ContentPropertyLogicInterface.IMAGE_TYPE;
        Boolean shouldAddThisPoster;
        allPosterProperties = getContentPropertiesByDataType(moduleId, -1, dataType, true, false);
        //�õ����е�ǰģ��ĺ������ݣ��Ա�ǰ̨ҳ����Щ������û��ֵ��
        for (ContentProperty posterProperty : allPosterProperties) {
            shouldAddThisPoster = true; //�ж����Ҫ��ֵ�û�����Ȼ�������
            for (ContentProperty dataFromClientSideSaving : contentProperties) {
                if (posterProperty.getPropertyId().equals(dataFromClientSideSaving.getPropertyId())) {
                    logger.warn("����ͬ����������");
                    shouldAddThisPoster = false;
                    break;
                }
                //5050��ӰƬ������
//              if(dataFromClientSideSaving.getPropertyId()==5050){
//                  moveName=dataFromClientSideSaving.getStringValue();
//              }
            }
            if (shouldAddThisPoster) {
                //����������û�о�Ҫ�и�� ������Ҫ�жϺ����Ƿ���к�����д(100*100)��
                Long propertyId = posterProperty.getPropertyId();
                String posterPropertyName = posterProperty.getName();
                if (propertyId <= 0) {
                    logger.error("������Ϣ���ô����޷����к����Զ����ɣ�" + posterProperty.getName());
                    continue;
                }
                Property property = propertyLogicInterface.getPropertyByCache(propertyId);
                if (property != null) {
                    posterPropertyName = property.getName();
                }
                //�жϺ������ֺ����Ƿ���ڷָ���Ĵ�С
                String s_str = "[*]";
                Pattern p = Pattern.compile(s_str);
                Matcher m = p.matcher(posterPropertyName);
                //boolean rs = m.find();
                if (m.find()) {
                    logger.debug("���ڴ������Ĵ�С��ʽ");
                    String[] posterPropertyNameArray = posterPropertyName.split("[��|(]");
                    String str2 = posterPropertyNameArray[1];
                    String[] str3 = str2.split("[*|*]");
                    String str4 = str3[0];//��
                    String str5 = str3[1];
                    String str6[] = str5.split("[)|��]");
                    String str7 = str6[0];//��
                    int width = Integer.parseInt(str4);
                    int height = Integer.parseInt(str7);
                    File handleImage = new File(request.getRealPath(handleImageFile));
                    //�õ��ָ��������
                    String[] strImage = handleImageFile.split("[.]");
                    String postfix = strImage[1];
                    //�������ɺ�����λ��
                    String targetFilePath = "/upload/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/" +
                            Math.round(Math.random() * 100000) + "_" + new Date().getTime() % 10000 + "_" + width + "." + postfix;
                    File saveToFileName = new File(request.getRealPath(targetFilePath));
                    try {
                        //����Ŀ�꺣����������
                        FileUtils.createFixedBoundImg(handleImage.toString(), saveToFileName.toString(), height, width);
                        //ImageControl imageControl = new ImageControl();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //�ϴ��ɹ�����Ҫͬ���������ļ�̨������ ����ط�ֵ��Ҫ������ݣ�
                    try {
                        JsUtils syncUtils = new JsUtils();
                        syncUtils.saveAndPushSynFile(saveToFileName.getName(), saveToFileName.getAbsolutePath(), targetFilePath, cspId);
                    } catch (Exception e) {
                        logger.error("�޷�ͬ���ļ���" + saveToFileName);
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
     * ��ȡ��Ƶ�ķּ���Ϣ
     * @param contentId ��ƵId
     * @return �ּ���Ϣ
     */
    public List<EpisodeDTO> getContentEpisodes(long contentId){
        ContentProperty cp = new ContentProperty();
        Property property = propertyLogicInterface.getByCode("Media_Url_Source");
        cp.setContentId(contentId);
        if(property!=null){
            cp.setPropertyId(property.getId());     // ֻȡԴ����Ϣ��urlͬ���ڲ���ʱ�ٻ�ȡ
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
            logger.error("���������������contentId="+contentId+",clipId="+willClipId+",propertyCode="+propertyCode);
            return -1L;
        }
        String key = "contentId"+contentId+"_clipId"+willClipId+"_p"+propertyCode;
        logger.debug("���Ի�ȡcontentPropertyId������Ϊ��"+key);
        Object result =  CacheUtils.get(key,"contentPropertyIdCache",new DataInitWorker(){
            public long scanList(List<ContentProperty> sourceClips,int willClipId,ContentProperty cp,boolean isResultList){
                logger.debug("��ѯclips�����contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount="+sourceClips.size());
                int clipCount = sourceClips.size();
                int clipIntValue = willClipId;
                Long firstIntValue=-1L;
                if(clipCount>0){
                    //���ӰƬ�ĵ�һ����0��ʼ����ô����Ҫ��clipId��ȥ1����ΪĬ���Ǵ�1��ʼ��
                    ContentProperty firstClip = sourceClips.get(0);
                    if(firstClip!=null){
                        firstIntValue = firstClip.getIntValue();
                        if(firstIntValue!=null){
                            if(firstIntValue==0){
                                if(clipIntValue>0){
                                    logger.warn("��һ����0��ʼ�ģ�Ӧ�ô�1��ʼ");
                                    clipIntValue--;
                                }else if(clipIntValue==0){
                                    logger.debug("�ҵ�һ��������");
                                }else{
                                    clipIntValue = 0;
                                }
                            }else{
                                logger.debug("��һ������ʼ�����ǣ�"+firstIntValue);
                            }
                        }else{
                            logger.error("��һ������ʼ����Ϊ�գ����Ǵ���ģ�");
                        }
                    }else{
                        logger.error("��һ���ǿյģ������������⣡");
                    }
                    //���ֻ��һ��
                    if(clipIntValue<0){
                        logger.debug("������Ȼ���ָ�ֵ������Ϊ0");
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
                    //ɨ��һ�����е�ӰƬԴƬ�Σ���ֹ��ȱ��������³����쳣
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
                    logger.error("û���ҵ��κε�ӰƬ����Դ���ݣ�contentId="+contentId+",propertyId="+cp.getPropertyId()+",clipCount=0");
                }
                return -1;
            }
            public Object init(Object key,String cacheName){
                Long result = -1L;
                logger.debug("׼����ѯ���ԣ�Media_Url_Source");
                Property sourceProperty = propertyLogicInterface.getByCode("Media_Url_Source");
                if(sourceProperty==null||sourceProperty.getId()<=0){
                    sourceProperty = propertyLogicInterface.getByCode("Media_Url_768K");
                }
                if(sourceProperty!=null&&sourceProperty.getId()>0){
                    ContentProperty cp = new ContentProperty();
                    cp.setContentId(contentId);
                    cp.setPropertyId(sourceProperty.getId());
                    PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.intValue","asc");
                    logger.debug("׼����ѯclips��contentId="+contentId+",propertyId="+cp.getPropertyId());
                    List<ContentProperty> sourceClips = contentPropertyLogicInterface.search(cp, pageBean);
                    if(sourceClips!=null&&sourceClips.size()>0){
                        return scanList(sourceClips,willClipId,cp,false);
                    }else{
                        logger.debug("û���ҵ��κε�ӰƬ����Դ���ݣ�contentId=" + contentId + ",propertyId=" + cp.getPropertyId()+","+sourceProperty.getCode());
                    }
                }
                {
                    //���Դ�bandwidth�������������
                    logger.debug("���Դ�bandwidth="+propertyCode+"������ӰƬƬ��....");
                    Property property = propertyLogicInterface.getByCode(propertyCode);
                    if(property!=null){
                        ContentProperty cp = new ContentProperty();
                        cp.setContentId(contentId);
                        cp.setPropertyId(property.getId());
                        PageBean pageBean = new PageBean(0,Integer.MAX_VALUE,"o1.intValue","asc");
                        List<ContentProperty> clips = contentPropertyLogicInterface.search(cp, pageBean);
                        if(clips!=null&&clips.size()>0){
                            logger.debug("�ҵ���"+clips.size()+"����¼��");
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
                            logger.error("û���ҵ�ӰƬƬ����Ϣ��contentId="+contentId+",propertyCode="+propertyCode+",propertyId="+property.getId());
                        }
                    }else{
                        logger.error("û���ҵ�ӰƬƬ�ζ�Ӧ�����ԣ�contentId="+contentId+",code="+propertyCode);
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
                logger.debug("������Դ�������ӣ�"+propertyCode);
                return sourceClip.getId();
            }else{
                Property property = propertyLogicInterface.getByCode(propertyCode);
                if(property!=null){
                    logger.debug("׼�����Ҳ������ӣ�"+propertyCode+","+property.getName());
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
                    logger.error("׼�����Ҳ�������"+propertyCode+"ʱ�����쳣��û�ҵ���Ӧ��Property");
                }
            }
        }
        return -1L;
    }

}
