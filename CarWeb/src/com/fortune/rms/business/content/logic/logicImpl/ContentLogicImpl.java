package com.fortune.rms.business.content.logic.logicImpl;

//import com.fortune.common.business.base.dao.BaseDaoInterface;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.content.dao.daoInterface.ContentDaoInterface;
import com.fortune.rms.business.content.logic.logicInterface.*;
import com.fortune.rms.business.content.model.*;
import com.fortune.rms.business.csp.logic.logicInterface.CspChannelLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspCspLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspDeviceLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspCsp;
import com.fortune.rms.business.encoder.logic.logicInterface.EncoderTaskLogicInterface;
import com.fortune.rms.business.encoder.model.EncoderTask;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.logic.logicInterface.PropertySelectLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.module.model.PropertySelect;
import com.fortune.rms.business.portal.logic.logicInterface.UserFavoritesLogicInterface;
import com.fortune.rms.business.portal.logic.logicInterface.UserScoringLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RelatedPropertyContentLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.logic.logicInterface.SystemLogLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.user.logic.logicInterface.BookMarkLogicInterface;
import com.fortune.rms.business.user.model.BookMark;
import com.fortune.tags.TagUtils;
import com.fortune.util.*;
import com.fortune.util.net.URLEncoder;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Node;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service("contentLogicInterface")
public class ContentLogicImpl extends BaseLogicImpl<Content>
        implements
        ContentLogicInterface {
    private ContentDaoInterface contentDaoInterface;
    private RelatedPropertyContentLogicInterface relateContentLogic;
    private ContentRelatedLogicInterface contentRelatedLogic;
    private ChannelLogicInterface channelLogic;
    private ContentChannelLogicInterface contentChannelLogic;
    private PropertyLogicInterface propertyLogic;
    private PropertySelectLogicInterface propertySelectLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private ContentAuditLogicInterface contentAuditLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private CspDeviceLogicInterface cspDeviceLogicInterface;
    private CspCspLogicInterface cspCspLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private CspChannelLogicInterface cspChannelLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private EncoderTaskLogicInterface encoderTaskLogicInterface;
    private AdminLogicInterface adminLogic;
    private SystemLogLogicInterface systemLogLogicInterface;
    private UserFavoritesLogicInterface userFavoritesLogicInterface;
    private UserScoringLogicInterface userScoringLogicInterface;
    private BookMarkLogicInterface bookMarkLogicInterface;
    @Autowired
    public void setUserScoringLogicInterface(UserScoringLogicInterface userScoringLogicInterface) {
        this.userScoringLogicInterface = userScoringLogicInterface;
    }

    @Autowired
    public void setUserFavoritesLogicInterface(UserFavoritesLogicInterface userFavoritesLogicInterface) {
        this.userFavoritesLogicInterface = userFavoritesLogicInterface;
    }

    @Autowired
    public void setBookMarkLogicInterface(BookMarkLogicInterface bookMarkLogicInterface) {
        this.bookMarkLogicInterface = bookMarkLogicInterface;
    }

    @Autowired
    public void setAdminLogic(AdminLogicInterface adminLogic) {
        this.adminLogic = adminLogic;
    }
    public static Map<Long, String> STATUS_CODES = new HashMap<Long, String>();
    static {
        STATUS_CODES.put(STATUS_CP_ONLINE, "CP上线");
        STATUS_CODES.put(STATUS_CP_OFFLINE, "CP下线");
        STATUS_CODES.put(STATUS_WAIT_TO_ONLINE, "上线待审");
        STATUS_CODES.put(STATUS_WAIT_TO_OFFLINE, "下线待审");
        //STATUS_CODES.put(STATUS_SP_PUBLISHED,"SP已发布到频道");
//        STATUS_CODES.put(STATUS_SP_ONLINE,"SP上线，待发布");
//        STATUS_CODES.put(STATUS_SP_OFFLINE,"SP下线");
        STATUS_CODES.put(STATUS_WAITING, "等待进一步操作");
        STATUS_CODES.put(STATUS_RECYCLE, "回收站");
        STATUS_CODES.put(STATUS_DELETE, "删除");
        STATUS_CODES.put(STATUS_LOST_MEDIA_SOURCE, "源文件丢失");
        STATUS_CODES.put(STATUS_WAITING_FOR_ENCODE, "等待转码");
        STATUS_CODES.put(STATUS_ENCODE_ERROR, "转码失败");
        STATUS_CODES.put(STATUS_WAITING_FOR_AUDIT, "等待审核");
        STATUS_CODES.put(STATUS_ENCODING, "正在转码");
        STATUS_CODES.put(STATUS_AUDIT_REJECTED, "审核被拒");
    }

    //private List<String> logMessage;
    @Autowired
    @SuppressWarnings("unchecked")
    public void setContentDaoInterface(ContentDaoInterface contentDaoInterface) {
        this.contentDaoInterface = contentDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.contentDaoInterface;

    }

    @Autowired
    public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
        this.systemLogLogicInterface = systemLogLogicInterface;
    }

    @Autowired
    public void setCspCspLogicInterface(CspCspLogicInterface cspCspLogicInterface) {
        this.cspCspLogicInterface = cspCspLogicInterface;
    }

    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    @Autowired
    public void setRelateContentLogic(RelatedPropertyContentLogicInterface relateContentLogic) {
        this.relateContentLogic = relateContentLogic;
    }

    @Autowired
    public void setContentRelatedLogic(ContentRelatedLogicInterface contentRelatedLogic) {
        this.contentRelatedLogic = contentRelatedLogic;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    @Autowired
    public void setChannelLogic(ChannelLogicInterface channelLogic) {
        this.channelLogic = channelLogic;
    }

    @Autowired
    public void setContentChannelLogic(ContentChannelLogicInterface contentChannelLogic) {
        this.contentChannelLogic = contentChannelLogic;
    }

    @Autowired
    public void setPropertyLogic(PropertyLogicInterface propertyLogic) {
        this.propertyLogic = propertyLogic;
    }

    @Autowired
    public void setContentPropertyLogicInterface(ContentPropertyLogicInterface contentPropertyLogicInterface) {
        this.contentPropertyLogicInterface = contentPropertyLogicInterface;
    }

    @Autowired
    public void setContentAuditLogicInterface(ContentAuditLogicInterface contentAuditLogicInterface) {
        this.contentAuditLogicInterface = contentAuditLogicInterface;
    }

    @Autowired
    public void setCspDeviceLogicInterface(CspDeviceLogicInterface cspDeviceLogicInterface) {
        this.cspDeviceLogicInterface = cspDeviceLogicInterface;
    }

    @Autowired
    public void setPropertySelectLogicInterface(PropertySelectLogicInterface propertySelectLogicInterface) {
        this.propertySelectLogicInterface = propertySelectLogicInterface;
    }

    @Autowired
    public void setEncoderTaskLogicInterface(EncoderTaskLogicInterface encoderTaskLogicInterface) {
        this.encoderTaskLogicInterface = encoderTaskLogicInterface;
    }

    /*
        @Autowired
        public void setSystemLogLogicInterface(SystemLogLogicInterface systemLogLogicInterface) {
            this.systemLogLogicInterface = systemLogLogicInterface;
        }


        @Autowired
        public void setPropertySelectLogicInterface(PropertySelectLogicInterface propertySelectLogicInterface) {
            this.propertySelectLogicInterface = propertySelectLogicInterface;
        }
    */

    @Autowired
    public void setCspChannelLogicInterface(CspChannelLogicInterface cspChannelLogicInterface) {
        this.cspChannelLogicInterface = cspChannelLogicInterface;
    }

    @Autowired
    public void setContentCspLogicInterface(ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
    }

    public String getStatusString(Long status) {
        String result = STATUS_CODES.get(status);
        if (result == null) {
            result = "[未知(" + status + ")]";
        }
        return result;
    }

    public Content initContent(Long contentId) {
        Content content = null;
        try {
            content = contentDaoInterface.get(contentId);
            return initContent(content);
        } catch (Exception e) {
            logger.error("初始化Content时发生异常：id="+contentId+",err="+e.getMessage());
            e.printStackTrace();
        }
        return content;
    }
    public String appendParameters(String url,String name,Object val){
        if(val!=null){
            String strVal = val.toString();
            if(!strVal.equals("")){
                if(url.contains("?")){
                    url+="&";
                }else{
                    url+="?";
                }
                try {
                    url+=name+"="+ URLEncoder.encode(strVal,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    url+=name+"="+ val;
                }
            }
        }
        return url;
    }
    @SuppressWarnings("unchecked")
    public Content initContent(Content content) {
        long contentId;
        if(content==null){
            logger.error("输入的Content是空！无法继续操作！");
            return null;
        }
        contentId = content.getId();
        try {
            Map<String, Property> playUrlTypes = new HashMap<String, Property>();
            long moduleId = content.getModuleId();
            //找出所有的模版属性，准备进行装配
            List<Property> propertyList = (List<Property>) CacheUtils.get(moduleId,"propertiesOfModule",new com.fortune.util.DataInitWorker(){
                 public Object init(Object key,String cacheName){
                     return propertyLogic.getPropertiesOfModule((Long)key,PropertyLogicInterface.STATUS_ON,null,new PageBean(0,10000,"displayOrder","asc"));
                 }
            });
            Map<Long, Property> propertyMap = new HashMap<Long, Property>();
            //将其保存到属性缓存中备用
            long devicePropertyId = 0;
            for (Property p : propertyList) {
                propertyMap.put(p.getId(), p);
                if (p.getRelatedTable() != null && p.getRelatedTable() == 1) {
                    devicePropertyId = p.getId();
                }
            }
            //将属性的可选值进行缓存
            final long propertyIdOfDeviceType = devicePropertyId;
            Map<String, String> psMap = (Map<String,String>) CacheUtils.get(moduleId,"propertySelectAndDeviceOfModule",new com.fortune.util.DataInitWorker(){
                public Object init(Object key,String cacheName){
                    Map<String,String> result = new HashMap<String, String>();
                    try {
                        List<PropertySelect> propertySelectList = HibernateUtils.findAll(getSession(),
                                "from PropertySelect ps where ps.propertyId in (select propertyId from ModuleProperty mp where mp.moduleId=" + key + ")");
                        for (PropertySelect ps : propertySelectList) {
                            //PropertySelect ps = (PropertySelect) aPropertySelectList;
                            result.put(ps.getPropertyId() + "_" + ps.getValue(), ps.getName());
                            result.put(ps.getPropertyId() + "_" + ps.getValue()+"_code", ps.getCode());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //查找设备，放入缓存
                    if (propertyIdOfDeviceType > 0) {
                        try {
                            List<Device> deviceList = HibernateUtils.findAll(getSession(), "from Device");
                            for (Device device : deviceList) {
                                result.put(propertyIdOfDeviceType + "_" + device.getId(), device.getName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return result;
                }
            });
            int no = 0;

            //获取所有的资源相关的属性的值
            List contentPropertyList = HibernateUtils.findAll(getSession(), "from ContentProperty cp where cp.contentId=" + content.getId() + " order by id asc");
            List<ContentProperty> outputContentProperty = new ArrayList<ContentProperty>();
            //遍历属性列表，初始化值 。这个for循环主要是扫主表，也就是在content表本身的属性值 。另一部分数据在contentProperty表中。
            for (Property p : propertyList) {
                String str=null;
                //如果属性是在主表里，并且对应列名不空，就从主记录Content对象里取值
                String fieldName = p.getColumnName();
                if (p.getIsMain() != null && p.getIsMain() == 1 && fieldName != null) {
                    if("".equals(fieldName)){
                        logger.error("错误发生了：属性“" +p.getName()+
                                "”对应的Content的Field名居然是空！做不下去了！");
                        continue;
                    }
                    try {
                        str = BeanUtils.getProperty(content, fieldName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if (str != null && !"".equals(str)) {
                        Byte dataType = p.getDataType();
                        if (PropertyLogicInterface.DATA_TYPE_RADIO.equals(dataType) || PropertyLogicInterface.DATA_TYPE_COMBO.equals(dataType) ||
                                PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(dataType)) {
                            if (p.getIsMerge() != null && p.getIsMerge() == 1) {
                                String ss[] = str.split(";");
                                if (ss.length > 0) {
                                    for (String s : ss) {
                                        ContentProperty cp = new ContentProperty();
                                        cp.setId(no++);
                                        cp.setPropertyId(p.getId());
                                        cp.setDesp(p.getName());
                                        cp.setIntValue(p.getDataType().longValue());
                                        cp.setStringValue("" + psMap.get(p.getId() + "_" + s));
                                        outputContentProperty.add(cp);
                                    }
                                }
                            } else {
                                Object v = psMap.get(p.getId()+"_"+str);
                                Object code = psMap.get(p.getId()+"_"+str+"_code");
                                if(v==null){
                                    Byte tableType = p.getRelatedTable();
                                    if(tableType!=null){
                                        if(tableType==1){//devcie
                                            //
                                            long id = StringUtils.string2long(str,-1);
                                            if(id>0){
                                                Device device = getDevice(id);
                                                if(device!=null){
                                                    v = device.getName();
                                                }
                                            }
                                        }else if(tableType == 2){//channel
                                            long id = StringUtils.string2long(str,-1);
                                            if(id>0){
                                                try {
                                                    Channel channel = channelLogic.get(id);
                                                    if(channel!=null){
                                                        v = channel.getName();
                                                    }
                                                } catch (Exception e) {
                                                    logger.error("无法找到频道：channelId="+id);
                                                }
                                            }
                                        }else if(tableType == 3){
                                            v = TagUtils.getInstance().getDictName(p.getCode(),str);
                                        }
                                    }
                                }else{

                                }
                                if(v==null){
                                    v=str;
                                }
                                ContentProperty cp = new ContentProperty();
                                cp.setId(no++);
                                cp.setPropertyId(p.getId());
                                cp.setDesp(p.getName());
                                cp.setIntValue(p.getDataType().longValue());
                                if(code!=null){
                                   cp.setPropertyName(code.toString());
                                }
                                cp.setStringValue(""+v);
                                outputContentProperty.add(cp);
                            }
                        } else {
                            ContentProperty cp = new ContentProperty();
                            cp.setId(no++);
                            cp.setPropertyId(p.getId());
                            cp.setDesp(p.getName());
                            cp.setIntValue(p.getDataType().longValue());
                            cp.setStringValue(str);
                            outputContentProperty.add(cp);
                        }
                    }
                }
            }
            //主表扫描完毕，扫描contentProperty表
            for (Object aContentPropertyList : contentPropertyList) {
                ContentProperty cp = (ContentProperty) aContentPropertyList;
                /* todo intvalue
                                    if (cp.getStringValue()==null || "".equals(cp.getStringValue())){
                                        cp.setStringValue(""+cp.getIntValue());
                                    }
                */
                String str = cp.getStringValue();
                Property p = propertyMap.get(cp.getPropertyId());
                if (p != null) {
                    //Byte byteMain = p.getIsMain();
                    //boolean isInTheMainTable = byteMain!=null&&byteMain==1;
//                    if(isInTheMainTable){
//                        logger.warn("属性“" +p.getName()+
//                                "”[" +p.getCode()+
//                                "]在主表中，但在从表ContentProperty中发现了！应该是个垃圾数据！");
//                        continue;
//                    }
                    if (str != null && !"".equals(str)) {
                        Byte dataType = p.getDataType();
                        if (PropertyLogicInterface.DATA_TYPE_COMBO.equals(dataType)
                                || PropertyLogicInterface.DATA_TYPE_RADIO.equals(dataType)
                                || PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(dataType)) {
                            if (p.getIsMerge() != null && p.getIsMerge() == 1) {
                                String ss[] = str.split(";");
                                if (ss.length > 0) {
                                    for (String s : ss) {
                                        ContentProperty cp1 = new ContentProperty();
                                        cp1.setId(cp.getId());
                                        cp1.setContentId(contentId);
                                        cp1.setPropertyId(p.getId());
                                        cp1.setDesp(p.getName());
                                        cp1.setIntValue(p.getDataType().longValue());
                                        cp1.setStringValue("" + psMap.get(p.getId() + "_" + s));
                                        cp1.setExtraData(cp.getExtraData());
                                        outputContentProperty.add(cp1);
                                    }
                                }
                            } else {
                                ContentProperty cp1 = new ContentProperty();
                                cp1.setContentId(contentId);
                                cp1.setId(cp.getId());
                                cp1.setPropertyId(p.getId());
                                cp1.setDesp(p.getName());
                                cp1.setIntValue(p.getDataType().longValue());
                                cp1.setStringValue("" + psMap.get(p.getId() + "_" + str));
                                cp1.setExtraData(cp.getExtraData());
                                outputContentProperty.add(cp1);
                            }
                        } else {
                            ContentProperty cp1;
                            try {
                                cp1 = (ContentProperty) com.fortune.util.BeanUtils.clone(cp);
                            } catch (Exception e) {
                                logger.error(e.getLocalizedMessage());
                                e.printStackTrace();
                                cp1 = new ContentProperty();
                                cp1.setContentId(contentId);
                                cp1.setId(cp.getId());
                                cp1.setPropertyId(p.getId());
                                cp1.setDesp(p.getName());//这个做法有问题
                                cp1.setIntValue(cp.getIntValue());
                                cp1.setStringValue(str);
                                cp1.setExtraData(cp.getExtraData());
                            }
                            outputContentProperty.add(cp1);
                        }
                    }
                } else {
                    logger.error("有个属性已经被删除，但依然存在于数据中，这是个风险，也是垃圾数据：" + cp.toString());
                }

            }
            Map<Long, List<ContentProperty>> properties = new HashMap<Long, List<ContentProperty>>();
            for (ContentProperty cp : outputContentProperty) {
                Long propertyId = cp.getPropertyId();
                List<ContentProperty> values = properties.get(propertyId);
                if (values == null) {
                    values = new ArrayList<ContentProperty>();
                }
                values.add(cp);
                properties.put(propertyId, values);
            }
            Map<String, Object> contentProperties = content.getProperties();
            for (Long propertyId : properties.keySet()) {
                List<ContentProperty> ps = properties.get(propertyId);
                Property p = propertyMap.get(propertyId);
                if (ps != null && p != null) {
                    Byte dataType = p.getDataType();
                    boolean isMediaUrl = dataType != null && (PropertyLogicInterface.DATA_TYPE_WMV.equals(dataType)
                            || PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType)
                            || PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType));
                    if(PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType)){
                        if(ps.size()>0){
                            for(ContentProperty pic:ps){
                                String name = pic.getName();
                                String extraData = pic.getExtraData();
                                String desp = pic.getDesp();
                                Long intValue = pic.getIntValue();
                                String extraLinks = appendParameters(pic.getStringValue(),"name",name);
                                extraLinks = appendParameters(extraLinks,"desp",desp);
                                extraLinks = appendParameters(extraLinks,"extra",extraData);
                                extraLinks = appendParameters(extraLinks,"intValue",intValue);
                                pic.setStringValue(extraLinks);
                            }
                        }
                    }

                    if (isMediaUrl && ps.size() > 0 && !playUrlTypes.containsKey(p.getCode())) {
                        playUrlTypes.put(p.getCode(), p);
                    }
                    if (ps.size() > 1) {
                        List<Object> values = new ArrayList<Object>();
                        if (isMediaUrl) {
                            ps = SortUtils.sortArray(ps, "intValue", "asc");
                        }
                        for (ContentProperty cp : ps) {
                            values.add(getClipInfo(p, cp));
                        }
                        contentProperties.put(p.getCode(), values);
                    } else if (ps.size() == 1) {
                        ContentProperty cp = ps.get(0);
                        contentProperties.put(p.getCode(), getClipInfo(p, cp));
                        //在前面的psMap中保留的PropertySelect中的value对应的code，后来在扫描contentProperty时，保存在
                        //propertyName中。注意，如果以后逻辑上有调整，可能会有问题。
                        String selectValueCode = cp.getPropertyName();
                        if(selectValueCode!=null){
                            contentProperties.put(p.getCode()+"_code",selectValueCode);
                        }
                    }
                }
            }
            List<Map<String, String>> types = new ArrayList<Map<String, String>>();
            for (Property property : playUrlTypes.values()) {
                Map<String, String> p = new HashMap<String, String>();
                p.put("type", "" + property.getDataType());
                p.put("code", property.getCode());
                p.put("name", property.getName());
                p.put("desc", property.getDesp());
                p.put("column", property.getColumnName());
                types.add(p);
            }
            contentProperties.put("playUrlTypes", types);
        } catch (Exception e) {
            logger.error("初始化Content数据时发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        return content;
    }

    public Content getCachedContent(Long contentId) {
        return (Content) CacheUtils.get(contentId, contentCacheName, new com.fortune.util.DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return initContent((Long) key);
            }
        });
    }
    public Content getCachedContent(final Content content) {
        return (Content) CacheUtils.get(content.getId(), contentCacheName, new com.fortune.util.DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return initContent(content);
            }
        });
    }

    public Object getClipInfo(Property property, ContentProperty cp) {
        String value = cp.getStringValue();
        String code = property.getCode();
        Byte dataType = property.getDataType();
        if (dataType == null) {
            dataType = -1;
        }
        if ("MEDIA_CLIP".equals(code) || "MEDIA_URL".equals(code) || dataType.equals(PropertyLogicInterface.DATA_TYPE_FLV) ||
                dataType.equals(PropertyLogicInterface.DATA_TYPE_MP4) ||
                dataType.equals(PropertyLogicInterface.DATA_TYPE_WMV)) {
            value += "?contentPropertyId=" + cp.getId() + "&contentId=" + cp.getContentId()
                    + "&propretyId=" + cp.getPropertyId() + "&propertyCode=" + code;
            Map<String, String> result = new HashMap<String, String>();
            result.put("no", "" + cp.getIntValue());
            result.put("name", "" + cp.getName() + "");
            result.put("clipPoster", cp.getThumbPic());
            result.put("playUrl", value);
            result.put("desp", cp.getDesp());
            result.put("length", "" + cp.getLength());
            result.put("extraInt", "" + cp.getExtraInt());
            result.put("extraData", "" + cp.getExtraData());
            result.put("subContentId", cp.getSubContentId());
            return result;
        } else {
            return value;
        }
    }

    public long getContentIdByContentChannelId(long contentChannelId) {
        Long result = (Long) CacheUtils.get(contentChannelId, "contentChannelIdCache", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                return contentDaoInterface.getContentIdByContentChannelId(new Long(key.toString()));
            }
        });
        if (result == null) return -1;
        return result;
    }

    public int getBookMarkCountOfUser(String userTel) {
        return bookMarkLogicInterface.getBookMarkCountOfUser(userTel);
    }

    public long getContentIdBySubContentId(String subContentId) {
        Property p = this.getPropertyByCode("SUB_CONTENT_ID");
        if (p != null) {
            final long propertyId = p.getId();
            Long result = (Long) CacheUtils.get(subContentId, "subcontentIdCache", new DataInitWorker() {
                public Object init(Object key, String cacheName) {
                    return contentDaoInterface.getContentIdBySubContentId(key.toString(), propertyId);
                }
            });
            if (result == null) return -1;
            return result;
        } else {
            return -1;
        }
    }

    public List<Map> searchContentParameter(String name, long channelId, Long cspId, long contentCspStatus, PageBean pageBean) {
        List list = contentDaoInterface.searchContentParameter(name, channelId, cspId, contentCspStatus, pageBean);

        List<Map> cps = new ArrayList<Map>();
        if (list != null && list.size() > 0) {
            for (Object aList : list) {
                Map<String, String> cParameters = new HashMap<String, String>();
                Object[] o = (Object[]) aList;
                if (o[0] == null || o[0].toString().trim().isEmpty()) {
                    cParameters.put("c_DOT_name", "名称不详");
                } else {
                    cParameters.put("c_DOT_name", o[0].toString());
                }

                String urlParameter = "?cid=";
                urlParameter += o[1].toString();
                urlParameter += "&mid=";
                urlParameter += o[2].toString();
                urlParameter += "&spId=";
                urlParameter += o[3].toString();
                cParameters.put("parameter", urlParameter);

                if (o[4] == null || o[4].toString().trim().isEmpty()) {
                    cParameters.put("cc_DOT_channelId", "未知频道");
                } else {
                    cParameters.put("cc_DOT_channelId", o[4].toString());
                }
                cps.add(cParameters);
            }
        }
        return cps;
    }


    public static final String contentCacheName = "contentCache";
    public static final String relatedCacheName = "relatedCache";
    public static final String sameViewCacheName = "saveViewCache";

    public String appendNames(String orgStr, String newNames) {
        if (orgStr == null || "".equals(orgStr.trim())) {
            return newNames;
        }
        return orgStr + ";" + newNames;
    }

    @SuppressWarnings("unchecked")
    public List<Content> getRelatedContents(final Long contentId, final Long cspId, final Long relateId) {
        return (List<Content>) CacheUtils.get(contentId, relatedCacheName, new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                List<Content> result;
                Long keyContentId = (Long) key;
                RelatedPropertyContent bean = new RelatedPropertyContent();
                bean.setContentId(keyContentId);
                bean.setCspId(cspId);
                bean.setRelatedPropertyId(relateId);
                List<RelatedPropertyContent> tempResult = relateContentLogic.search(bean);
                result = new ArrayList<Content>();
                for (RelatedPropertyContent rc : tempResult) {
                    Long rcContentId = rc.getContentId();
                    if (contentId != null && !contentId.equals(rcContentId)) {
                        result.add(getCachedContent(rcContentId));
                    }
                }
                if (result.size() < 5) {
                    Content content = getCachedContent(contentId);
                    long willCheckCspId = cspId;
                    if (willCheckCspId <= 0) {
                        willCheckCspId = content.getCspId();
                    }
                    if (content != null) {
                        //分别按演员、导演和影片名进行匹配相似的影片。加入result。
                        String names = content.getActors(); //演员
                        //String ContentNames;
                        names = appendNames(names, content.getDirectors());//导演
                        //截取影片名的前4个字进行匹配查找
                        String contentName = content.getName();
                        if (contentName.length() > 6) {
                            if (contentName.substring(0, 1).contains("《") || contentName.substring(0, 1).contains("【")) {
                                contentName = contentName.substring(1, 5);
                                //logger.debug("查询的影片名字为————————————————————————————————————————————" + contentName);
                            } else {
                                contentName = contentName.substring(0, 4);
                                //logger.debug("查询的影片名字为————————————————————————————————————————————" + contentName);
                            }
                        }
                        names = appendNames(names, contentName);    //影片名称
                        if (names != null && !"".equals(names)) {
//                            int contentSize=4-result.size();
                            PageBean pageBean = new PageBean(0, 4, null, null);
                            for (String name : names.split(";")) {
                                List<Content> data = list(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, -1L, name, pageBean);
                                for (Content c : data) {
                                    //只有不是同一个片子才加进去
                                    if (!keyContentId.equals(c.getId())) {
                                        boolean notAdded = true;
                                        for (Content hasBeenAddContent : result) {
                                            if (hasBeenAddContent.getId() == c.getId()) {
                                                notAdded = false;
                                                break;
                                            }
                                        }
                                        if (notAdded) {
                                            result.add(c);
                                        }
                                    }
                                }
                            }
                        }
                        if (result.size() < 5) {
                            //如果按导演，演员，影片名称都未匹配到，则在当前影视频道内搜索。
                            List<Channel> channelList = contentChannelLogic.getChannelsByContentId(content.getId());  //得到当前影视的频道
                            List<Content> contentList = new ArrayList<Content>();
                            if (channelList != null) {
                                for (Channel channel : channelList) {
                                    PageBean pageBean = new PageBean(0, 10, null, null);
//                                    List<Content> data = list(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, channel.getId(), pageBean);
                                    //随机选取10个数据。
                                    List<Object[]> data2 = list2(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, channel.getId(), pageBean);
                                    //装配到Content里。
                                    for (Object[] o : data2) {
                                        Content content1 = new Content();
                                        content1.setId(Long.parseLong(o[0].toString()));
                                        content1.setName(o[1].toString());
                                        content1.setProperties(null);
                                        contentList.add(content1);
                                    }
                                    for (Content c : contentList) {
                                        if (!keyContentId.equals(c.getId())) {
                                            boolean notAdded = true;
                                            for (Content hasBeenAddContent : result) {
                                                if (hasBeenAddContent.getId() == c.getId()) {
                                                    notAdded = false;
                                                    break;
                                                }
                                            }
                                            if (notAdded) {
                                                //装配Propertys属性。
                                                result.add(getCachedContent(c.getId()));
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                return result;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Content> getSameViewContents(final Long contentId, final Long relateId) {
        return (List<Content>) CacheUtils.get(contentId, sameViewCacheName,
                new DataInitWorker() {
                    public Object init(Object key, String cacheName) {
                        ContentRelated bean = new ContentRelated();
                        bean.setContentId(contentId);
                        bean.setRelatedId(relateId);
                        List<ContentRelated> tempResult = contentRelatedLogic.search(bean);
                        List<Content> result = new ArrayList<Content>();
                        for (ContentRelated cr : tempResult) {
                            if (contentId != null && !contentId.equals(cr.getContentId())) {
                                result.add(getCachedContent(cr.getContentId()));
                            }
                        }
                        return result;
                    }
                });
    }

    /**
     *
     * @param cspId csp的id
     * @param contentCspStatus 发布状态，一般情况都是上线发布：2
     * @param channelId 频道id
     * @param contentName 媒体名称
     * @param directors  导演
     * @param actors  演员
     * @param searchValues  搜索值
     * @param pageBean 分页信息
     * @return                   搜索结果
     */
    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId, final Long contentCspStatus, final Long channelId, final String contentName,
                              final String directors, final String actors,
                              final List<ContentProperty> searchValues, final PageBean pageBean) {
        //得到栏目推荐的数量看有没有
        List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
        if(pageBean!=null){
            if (pageBean.getStartRow() == 0) {
                //说明有推荐(见识查询的条数)
                pageBean.setPageSize(pageBean.getPageSize() - recommendResult.size());
            } else {
                //如果不是第一页减去推荐的数目
                pageBean.setStartRow(pageBean.getStartRow() - recommendResult.size());
            }
        }
        String key = cspId + "_ch" + channelId + "_name" + contentName + "_director" + directors + "_actor" + actors;
        if (searchValues != null) {
            for (ContentProperty cp : searchValues) {
                key += "_prop" + cp.getPropertyId() + "_v" + cp.getStringValue();
            }
        }
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //对searchValues做一个遍历，重新整理一下数据，如果是整合在主表里，要对主表进行搜索
                if (searchValues != null) {
                    for (ContentProperty cp : searchValues) {
                        Property property = propertyLogic.get(cp.getPropertyId());
                        Byte isMain = property.getIsMain();
                        if (isMain != null && 1 == isMain) {
                            cp.setExtraInt(1L);
                            cp.setExtraData(property.getColumnName());
                        } else {
                            cp.setExtraInt(0L);
                            cp.setExtraData(null);
                        }
//                        cp.setExtraData("");
                    }
                }
                List<Content> result = new ArrayList<Content>();
                logger.debug("准备进行搜索："+objKey);
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues, pageBean);
                if (pageBean != null && pageBean.getStartRow() == 0) {
                    List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
                    for (Content c : recommendResult) {
                        result.add(getCachedContent(c.getId()));
                    }
                    pageBean.setRowCount(pageBean.getRowCount() + recommendResult.size());
                    logger.debug("搜索结束，记录共有"+pageBean.getRowCount()+",当前获取到了"+tempResult.size());
                }else{
                    logger.debug("数据有误，pageBean=null，所以搜索出来的数据不一定正确，当前获取到了"+tempResult.size());
                }
                for (Content c : tempResult) {
                    logger.debug("初始化"+c.getName());
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                logger.debug("搜索结束，返回数据条数："+result.size());
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId, final Long contentCspStatus, final String channelIds, final String contentName, final String directors, final String actors, final List<ContentProperty> searchValues, final PageBean pageBean) {
        String key = cspId + "_ch" + channelIds + "_name" + contentName + "_director" + directors + "_actor" + actors;
        if (searchValues != null) {
            for (ContentProperty cp : searchValues) {
                key += "_prop" + cp.getPropertyId() + "_v" + cp.getStringValue();
            }
        }
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //对searchValues做一个遍历，重新整理一下数据，如果是整合在主表里，要对主表进行搜索
                if (searchValues != null) {
                    for (ContentProperty cp : searchValues) {
                        Property property = propertyLogic.get(cp.getPropertyId());
                        Byte isMain = property.getIsMain();
                        if (isMain != null && 1 == isMain) {
                            cp.setExtraInt(1L);
                            cp.setExtraData(property.getColumnName());
                        } else {
                            cp.setExtraInt(0L);
                            cp.setExtraData(null);
                        }
//                        cp.setExtraData("");
                    }
                }
                Channel channel = new Channel();
                channel.setType(1l);
                channel.setCspId(1l);
                channel.setParentId(-1l);
                //channel.setName("根目录");
                String systemChannelId = null;
                List<Channel> channels = channelLogic.search(channel);
                if (channels != null && channels.size() > 0) {
                    for (Channel c : channels) {
                        if ("根目录".equals(c.getName()) || "首页".equals(c.getName())) {
                            systemChannelId = "" + c.getId();
                            break;
                        }
                    }
                }
                if (systemChannelId == null) {
                    systemChannelId = "-1";
                }
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelIds, contentName, directors, actors, searchValues, pageBean, systemChannelId);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Content> list(final Long contentCspStatus, final String contentName, final String directors, final String actors, final List<ContentProperty> searchValues, final PageBean pageBean) {
        String key = "name" + contentName + "_director" + directors + "_actor" + actors;
        if (searchValues != null) {
            for (ContentProperty cp : searchValues) {
                key += "_prop" + cp.getPropertyId() + "_v" + cp.getStringValue();
            }
        }
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                List<Content> tempResult = contentDaoInterface.list(contentCspStatus, contentName, directors, actors, searchValues, pageBean);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }

    public List<ContentProperty> getContentPropertiesFromXmlNode(Content content, Node contentNode,
                                                                 Map<String, String> elementIdMaps,
                                                                 long intValue, List<String> saveResult,
                                                                 ContentProperty source, OutputStreamWriter os,Map<String,Property> properties) throws IOException {
        List<ContentProperty> result = new ArrayList<ContentProperty>();
        AppConfigurator config = AppConfigurator.getInstance();
        org.dom4j.Element contentEle = (org.dom4j.Element) contentNode;
        List attributes = contentEle.attributes();
        if(attributes!=null){
            for(Object o:attributes){
                Attribute attribute = (Attribute)o;
                String propertyCode = attribute.getName();
                String value = attribute.getValue();
                if(value==null||"".equals(value)){
                    continue;
                }
                Property property;
                if(propertyCode.startsWith("%")){//这不需要数据库支持的一个属性
                    property = new Property();
                    property.setCode(propertyCode);
                    property.setId(10000);
                    property.setIsMain(null);
                    property.setDataType(PropertyLogicInterface.DATA_TYPE_MP4);
                }else{
                    property = properties.get(propertyCode);
                    if(property==null){
                        propertyCode = elementIdMaps.get(propertyCode);
                        if(propertyCode==null){
                            continue;
                        }
                        property=properties.get(propertyCode);
                    }
                }
                if(property==null){
                    saveResult.add("" + propertyCode + "处理失败");
                    logger.error("Can't found property of code '" + propertyCode + "'");
                    continue;
                }
                Byte dataType = property.getDataType();
                if (PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(dataType) ||
                        PropertyLogicInterface.DATA_TYPE_COMBO.equals(dataType) ||
                        PropertyLogicInterface.DATA_TYPE_RADIO.equals(dataType)) {
                    String selectValue = getPropertySelectValue(property.getId(), value);
                    if (selectValue == null) {
                        //没有找到属性值，那就创建一个
                        String error = "无法找到《" + content.getName() + "》的属性：" + property.getName() + ",值：" + value;
                        if (config.getBoolConfig("importXml.createSelectValueIfNotExists", true)) {
                            error += ",按照配置文件，创建这个属性值！";
                            PropertySelect select = new PropertySelect();
                            select.setDisplayOrder(-1L);
                            select.setPropertyId(property.getId());
                            select.setName(value);
                            select.setCode(value);
                            select = propertySelectLogicInterface.save(select);
                            value = "" + select.getId();
                            saveResult.add(error);
                            if (os != null) {
                                os.write(error + "\r\n");
                            }
                        } else {
                            saveResult.add(error);
                            if (os != null) {
                                os.write(error + "\r\n");
                            }
                            continue;
                        }
                    } else {
                        value = selectValue;
                    }
                    //value = propertyLogic.getSelectId(property.getId(), value);
                    //ps=propertySelectLogicInterface.getPropertyId(property.getId(), value);
                    // value=""+ps.getId();
                }
                if(config.getBoolConfig("import.default.autoPutToContentTable",false)){
                    //如果在数据库里进行操作，就不用在这时进行归数据并操作
                    Byte isMain = property.getIsMain();
                    if(isMain!=null&&isMain==1){
                        //如果在Content主表，则尝试为Content赋值
                        String columnName = property.getColumnName();
                        if(columnName!=null){
                            if(com.fortune.util.BeanUtils.setProperty(content,columnName,value)){
                                logger.debug(propertyCode+"->"+columnName+"设置到主表成功！");
                                continue;
                                //break;
                            }
                        }
                    }
                }
                //如果不在Content主表，则尝试为ContentProperty赋值
                ContentProperty cp;
                if (source != null) {
                    cp = (ContentProperty) com.fortune.util.BeanUtils.clone(source);
                } else {
                    cp = new ContentProperty();
                }
                com.fortune.util.BeanUtils.setDefaultValue(cp,"desp",propertyCode);
                com.fortune.util.BeanUtils.setDefaultValue(cp,"name",propertyCode);
                com.fortune.util.BeanUtils.setDefaultValue(cp,"extraData",propertyCode);
                cp.setPropertyId(property.getId());
                cp.setStringValue(value);
                cp.setContentId(null);
                cp.setIntValue(intValue);
                // contentPropertyLogicInterface.save(cp);
                result.add(cp);
            }
        }
        return result;
    }

    public Content save(Content content) {
        long id = content.getId();
        //content = super.save(content);
        boolean isCreateNew = false;
        if (id <= 0) {
            //是新建媒体，保存绑定关系
            logger.debug("媒体《" + content.getName() + "》是新建媒体，需要保存其相对其他的SP的绑定关系");
            isCreateNew = true;
        }
        com.fortune.util.BeanUtils.setDefaultValue(content,"allVisitCount",0L);
        com.fortune.util.BeanUtils.setDefaultValue(content,"monthVisitCount",0L);
        com.fortune.util.BeanUtils.setDefaultValue(content,"weekVisitCount",0L);
        List<CspCsp> allCspIds = new ArrayList<CspCsp>();
        Long cspId = content.getCspId();
        Csp csp = null;
        if(cspId!=null){
            try {
                csp = cspLogicInterface.get(cspId);
            } catch (Exception e) {
                //e.printStackTrace();
                logger.error("无法获取CSP!id="+e.getMessage());
            }
        }
        if(cspId==null||cspId<=0||csp==null){
            //紧凑模式下会自动设置为csp表中的第一个csp
            if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
                List<Csp> csps = cspLogicInterface.getAll();
                if(csps!=null&&csps.size()>0){
                    content.setCspId(csps.get(0).getId());
                    cspId = content.getCspId();
                }
            }
        }
        if (cspId != null && cspId > 0) {
            CspCsp cc = new CspCsp();
            cc.setCspId(content.getCspId());
            allCspIds.addAll(cspCspLogicInterface.search(cc));
            Csp own;
            try {
                own = cspLogicInterface.get(content.getCspId());
                if (CspLogicInterface.TRUE.equals(own.getIsSp())) {
                    boolean foundSelf = false;
                    for (CspCsp cspCsp : allCspIds) {
                        Long spId = cspCsp.getMasterCspId();
                        if (spId != null && spId == own.getId()) {
                            foundSelf = true;
                            break;
                        }
                    }
                    if (!foundSelf) {
                        CspCsp self = new CspCsp();
                        self.setMasterCspId(own.getId());
                        self.setCspId(own.getId());
                        allCspIds.add(self);
                    }
                }
                if (isCreateNew && !CspLogicInterface.TRUE.equals(own.getIsCpOnlineAudit())) {
                    //新建，而且是免审，就直接上线
                    logger.debug("新建媒体，而且CP设置了免审，就直接上线：媒体：" + content.getName() + ",csp=" + own.getName());
                    content.setStatus(STATUS_CP_ONLINE);
                }
            } catch (Exception e) {
                logger.error("无法处理有关CSP的绑定：" + e.getMessage());
            }
        }
        String post=content.getPost1Url();
        if(post!=null&&"".equals(post.trim())){
            content.setPost1Url(null);
        }
        post=content.getPost2Url();
        if(post!=null&&"".equals(post.trim())){
            content.setPost2Url(null);
        }
        content = super.save(content);
        for (CspCsp cspCsp : allCspIds) {
            long defaultStatus;
            try {
                csp = cspLogicInterface.get(cspCsp.getMasterCspId());
                if (!CspLogicInterface.TRUE.equals(csp.getIsSp())) {
                    //如果不是SP，不用绑定
                    continue;
                }
                if (CspLogicInterface.TRUE.equals(csp.getIsSpOnlineAudit())) {
                    defaultStatus = 0L;
                } else {
                    defaultStatus = ContentCspLogicInterface.STATUS_ONLINE;
                }
                if (isCreateNew) {
                    logger.debug("添加绑定关系：" + content.getName() + "(id=" + content.getId() + ")绑定到" + csp.getName() + "(id=" + csp.getId() + ")");
                    contentCspLogicInterface.setStatus(content.getId(), cspCsp.getMasterCspId(), -1, defaultStatus);
                } else {
                    logger.debug("检查绑定关系：" + content.getName() + "(id=" + content.getId() + ")绑定到" + csp.getName() + "(id=" + csp.getId() + ")");
                    contentCspLogicInterface.checkStatus(content.getId(), cspCsp.getMasterCspId(), -1, defaultStatus);
                }
            } catch (Exception e) {
                logger.error("无法获取对应SP的信息：" + e.getMessage());
            }

        }
        return content;
    }

    public Content saveContent(Content content, List<ContentProperty> contentProperties, List<ContentProperty> oldContentProperties, List<String> saveResult,
                               String picPath, String webAppPath, String cspFilePath, String fileNameHere, OutputStreamWriter os, boolean checkOnly) {
        try {
            if (!checkOnly) {
                contentProperties = contentPropertyLogicInterface.setContentFromContentProperties(content, contentProperties);
                content = save(content);
            } else {
                if (content.getId() <= 0) {
                    content.setId(999999);
                }
            }
        } catch (Exception e) {
            logger.error("无法保存Content：" + content.toString() + "\n错误信息：" + e.getMessage());
            return null;
        }
        String dateDir = "/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
        AppConfigurator config = AppConfigurator.getInstance();
        boolean shouldCopyPoster = config.getBoolConfig("importXml.default.copyPoster", true);
        boolean shouldSyncPoster = config.getBoolConfig("importXml.default.syncPoster", true);
        boolean checkMediaFile = config.getBoolConfig("importXml.default.checkMediaFile", true);
        boolean autoEncode = config.getBoolConfig("importXml.autoEncode", true);
        int fileId = 1;
        for (ContentProperty cp : contentProperties) {
            cp.setContentId(content.getId());
            int idx = 0, len = oldContentProperties.size();
            for (; idx < len; idx++) {
                ContentProperty old = oldContentProperties.get(idx);
                Long propertyId = old.getPropertyId();
                if (propertyId != null && propertyId.equals(cp.getPropertyId())) {
                    Long oldIntValue = old.getIntValue();
                    if (oldIntValue == null) {
                        oldIntValue = 0L;
                    }
                    Long intValue = cp.getIntValue();
                    if ((oldIntValue == null && intValue == null) || (oldIntValue.equals(intValue))) {
                        //找到旧的匹配数据！复制过来
                        cp.setDesp(old.getDesp());
                        cp.setExtraData(old.getExtraData());
                        cp.setExtraInt(old.getExtraInt());
                        cp.setId(old.getId());
                        cp.setName(cp.getName() == null ? old.getName() : cp.getName());
                        cp.setSubContentId(old.getSubContentId());
                        cp.setThumbPic(old.getThumbPic());
                        oldContentProperties.remove(idx);
                        break;
                    }
                }
            }
            Property property = null;
            try {
                property = propertyLogic.getPropertyByCache(cp.getPropertyId());
            } catch (Exception e) {
                logger.error("无法查找到属性：" + cp.getPropertyId() + "，这个数据不能导入：" + cp.toString());
            }
            if (property == null) {
                continue;
            }
            String name = cp.getName();
            if (name == null || "".equals(name)) {
                name = property.getName();
                cp.setName(name);
            }
            Byte dataType = property.getDataType();
            //如果是海报图片，需要将其进行同步到所有的WEB服务器
            if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType) && shouldCopyPoster) {
                //   if picExist then begin
                //       CopyFileFromNasToMasterHost81PostDir
                //       SyncFileToAllWebHost86_87_89
                //   else begin
                //       Warning
                //   end if
                File picFile = new File(picPath + "/" + cp.getStringValue());
                if (picFile.exists() && !picFile.isDirectory() && picFile.length() > 0) {
                    String newFileUrl = "/upload" + dateDir + cspFilePath + "/" + content.getId() + "_" + fileId + "." +
                            FileUtils.getFileExtName(picFile.getAbsolutePath());
                    fileId++;
                    File newPicFile = new File((webAppPath + newFileUrl).replace('/', File.separatorChar));
                    File newPicPath = newPicFile.getParentFile();
                    cp.setStringValue(newFileUrl);
                    if (!checkOnly) {
                        logger.debug("复制海报文件" + picFile.getAbsolutePath() + " --> " + newPicFile.getAbsolutePath());
                        FileUtils.copy(picFile, newPicPath.getAbsolutePath(), FileUtils.extractFileName(newPicFile.getAbsolutePath(), File.separator));
                        //todo 分发内容
                        if (shouldSyncPoster) {
                            JsUtils jsUtils = new JsUtils();
                            try {
                                jsUtils.saveAndPushSynFile(fileNameHere, newPicFile.getAbsolutePath(), newFileUrl, content.getCspId());
                            } catch (Exception e) {
                                e.getMessage();
                                logger.error("同步信息出错");
                            }
                        }
                        //然后保存到数据库
                        cp = contentPropertyLogicInterface.save(cp);
                        if (cp == null) {
                            logger.error("无法保存数据！");
                        }
                    }
                } else if (picFile.isDirectory()) {
                    logger.warn("海报连接有问题，url=" + picFile.getAbsolutePath());
                } else {
                    //存储上没有，就在本地找找，如果还没有，就报错
                    File newPicFile = new File(webAppPath + "/" + cp.getStringValue());
                    if (!newPicFile.exists()) {
                        String errorMsg = "海报文件缺失：《" + content.getName() +
                                "》的：" + picFile.getAbsolutePath();
                        logger.error(errorMsg);
                        try {
                            os.write(errorMsg + "\r\n");
                        } catch (IOException e) {
                            logger.error("无法输出结果到输出文件：" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else if (checkMediaFile && (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                    PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType))) {
                //如果是媒体文件
                String errorMsg = "";
                Device device = getDevice(content.getDeviceId());
                cp.setExtraInt(404L);
                File mediaFile = new File(device.getLocalPath() + "/" + cp.getStringValue());
                if (mediaFile.exists()) {
                    SimpleFileInfo fileInfo = new SimpleFileInfo(mediaFile.getAbsolutePath(), mediaFile.length(), new Date(mediaFile.lastModified()), false,
                            FileType.video);
                    if (FileUtils.setFileMediaInfo(mediaFile.getAbsolutePath(), fileInfo)) {
                        cp.setLength((long) fileInfo.getLength());
                        cp.setThumbPic("?" + fileInfo.getWidth() + "x" + fileInfo.getHeight());
                        cp.setExtraInt(200L);
                    } else {
                        cp.setExtraInt(500L);
                        errorMsg = "媒体文件无法进行播放检查:《" + content.getName() +
                                "》的：" + mediaFile.getAbsolutePath();

                    }
                } else {
                    errorMsg = "媒体文件缺失：《" + content.getName() +
                            "》的" + cp.getName() + "(" + cp.getIntValue() + ")" +
                            "：" + mediaFile.getAbsolutePath();
                    cp.setExtraInt(404L);
                }
                if (!checkOnly) {
                    cp = contentPropertyLogicInterface.save(cp);
                    if ("Media_Url_Source".equals(property.getCode())) {
                        if (cp.getExtraInt() == 200L) {
                            if (autoEncode) {
                                //加入自动转码队列，只有是源才做转码
                                //cp.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_HAS_ENCODED);
                                List<EncoderTask> tasks = encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content, cp);
                                String taskInfo = "";
                                for (EncoderTask task : tasks) {
                                    taskInfo += "" + task.getName() + ",";
                                }
                                if (!"".equals(taskInfo)) {
                                    if(!config.getBoolConfig("system.playMediaUrlSource",true)){
                                        content.setStatus(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE);
                                        content = save(content);
                                    }
                                    taskInfo = taskInfo.substring(0, taskInfo.length() - 1);
                                }
                                saveResult.add("按照配置已经添加转码任务：" + taskInfo);
                            } else {
                                saveResult.add("因为配置要求，不启动转码任务：" + cp.getStringValue());
                            }
                        } else {
                            saveResult.add("因为文件不正确，不启动转码任务：" + cp.getStringValue() + "，如果后期补充视频文件，请在补充完成后通知系统管理员手工启动转码。");
                        }
                    }
                }
                if (errorMsg != null && !"".equals(errorMsg)) {
                    logger.error(errorMsg);
                    //saveResult.add(errorMsg);
                    try {
                        os.write(errorMsg + "\r\n");
                    } catch (IOException e) {
                        logger.error("无法输出结果到输出文件：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                if (!checkOnly) {
                    cp = contentPropertyLogicInterface.save(cp);
                    if (cp == null) {
                        logger.error("无法保存数据！");
                    }
                }
            }
        }
        return content;
    }

    public boolean downloadFile(Device device,String fileName,String desertFile){
        String url = device.getUrl()+"/"+fileName;
        if(url.startsWith("http://hls.")){
            //这是需要调度的，就用IP代替主机
            url = "http://"+device.getIp()+":"+TcpUtils.getPortFromUrl(url)+("/"+StringUtils.getClearURL(url).replace("//","/"));
        }else if(url.contains("serverIP")){
            url = url.replace("serverIP",device.getIp());
        }else if(url.contains("serverIp")){
            url = url.replace("serverIp",device.getIp());
        }
        File localPath = new File(device.getLocalPath());
        logger.debug("尝试下载："+url+",保存到："+localPath.getAbsolutePath());
        HttpDownloadWorker httpDownloadWorker = new HttpDownloadWorker(url,
                localPath.getAbsolutePath(),desertFile,null);
        return httpDownloadWorker.download();
    }

    /**
     * 保存媒体信息
     * @param content 要保存的媒体对象
     * @param contentProperties 属性结果
     * @param oldContentProperties 以前保存过的属性结果队列
     * @param saveResult 保存中的日至信息
     * @param picPath 图片路径
     * @param webAppPath web的根目录
     * @param cspFilePath csp的文件木记录
     * @param fileNameHere 文件名
     * @param os 输出日志文件流
     * @param checkOnly 是否是检查
     * @return 保存后的content对象
     */
    public Content saveContentV2(Content content, List<ContentProperty> contentProperties, List<ContentProperty> oldContentProperties, List<String> saveResult,
                               String picPath, String webAppPath, String cspFilePath, String fileNameHere, OutputStreamWriter os, boolean checkOnly) {
        String dateDir = "/" + StringUtils.date2string(new Date(), "yyyy/MM/dd") + "/";
        AppConfigurator config = AppConfigurator.getInstance();
        boolean shouldCopyPoster = config.getBoolConfig("importXml.default.copyPoster", true);
        boolean shouldSyncPoster = config.getBoolConfig("importXml.default.syncPoster", true);
        boolean checkMediaFile = config.getBoolConfig("importXml.default.checkMediaFile", true);
        boolean autoEncode = config.getBoolConfig("importXml.autoEncode", true);
        int fileId = 1;
        List<ContentProperty> willSaveProperties = new ArrayList<ContentProperty>();
        //遍历检查所有的图片和视频属性
        Device device = getDevice(content.getDeviceId());
        for (ContentProperty cp : contentProperties) {

            int idx = 0, len = oldContentProperties.size();
            for (; idx < len; idx++) {
                ContentProperty old = oldContentProperties.get(idx);
                Long propertyId = old.getPropertyId();

                if (propertyId != null && propertyId.equals(cp.getPropertyId())) {
                    Long oldIntValue = old.getIntValue();
                    if (oldIntValue == null) {
                        oldIntValue = 0L;
                    }
                    Long intValue = cp.getIntValue();
                    if (intValue == null || (oldIntValue.equals(intValue))) {
                        //找到旧的匹配数据！复制过来
                        cp.setDesp(old.getDesp());
                        cp.setExtraData(old.getExtraData());
                        cp.setExtraInt(old.getExtraInt());
                        cp.setId(old.getId());
                        cp.setName(cp.getName() == null ? old.getName() : cp.getName());
                        cp.setSubContentId(old.getSubContentId());
                        cp.setThumbPic(old.getThumbPic());
                        oldContentProperties.remove(idx);
                        break;
                    }
                }
            }
            Property property = null;
            try {
                property = propertyLogic.getPropertyByCache(cp.getPropertyId());
            } catch (Exception e) {
                logger.error("无法查找到属性：" + cp.getPropertyId() + "，这个数据不能导入：" + cp.toString());
            }
            if (property == null) {
                continue;
            }
            String name = cp.getName();
            if (name == null || "".equals(name)) {
                name = property.getName();
                cp.setName(name);
            }
            Byte dataType = property.getDataType();
            //如果是海报图片，需要将其进行同步到所有的WEB服务器
            if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType) && shouldCopyPoster) {
                //   if picExist then begin
                //       CopyFileFromNasToMasterHost81PostDir
                //       SyncFileToAllWebHost86_87_89
                //   else begin
                //       Warning
                //   end if
                File picFile = new File(picPath + "/" + cp.getStringValue());
                if(!picFile.exists()){
                    //如果文件不存在，就尝试进行下载。
                    if(config.getBoolConfig("importXml.picPath.downloadIfNotExists",false)){
                        downloadFile(device,picFile.getAbsolutePath().substring(device.getLocalPath().length()),picFile.getAbsolutePath());
                    }
                }
                if (picFile.exists() && !picFile.isDirectory() && picFile.length() > 0) {
                    String contentId = ""+content.getId();
                    if(content.getId()<=0){
                        contentId = content.getContentId()+"_"+StringUtils.date2string(new Date(),"yyyyMMddHHmmss")
                                +"_"+Math.round(Math.random()*100000);
                    }
                    String newFileUrl = "/upload" + dateDir + cspFilePath + "/" + contentId + "_" + fileId + "." +
                            FileUtils.getFileExtName(picFile.getAbsolutePath());
                    fileId++;
                    File newPicFile = new File((webAppPath + newFileUrl).replace('/', File.separatorChar));
                    File newPicPath = newPicFile.getParentFile();
                    cp.setStringValue(newFileUrl.replace("//","/"));
                    if (!checkOnly) {
                        logger.debug("复制海报文件" + picFile.getAbsolutePath() + " --> " + newPicFile.getAbsolutePath());
                        FileUtils.copy(picFile, newPicPath.getAbsolutePath(), FileUtils.extractFileName(newPicFile.getAbsolutePath(), File.separator));
                        //todo 分发内容
                        //然后保存到数据库
                        willSaveProperties.add(cp);
/*
                        cp = contentPropertyLogicInterface.save(cp);
                        if (cp == null) {
                            logger.error("无法保存数据！");
                        }
*/
                    }
                } else if (picFile.isDirectory()) {
                    logger.warn("海报连接有问题，url=" + picFile.getAbsolutePath());
                } else {
                    //存储上没有，就在本地找找，如果还没有，就报错
                    File newPicFile = new File(webAppPath + "/" + cp.getStringValue());
                    if (!newPicFile.exists()) {
                        String errorMsg = "海报文件缺失：《" + content.getName() +
                                "》的：" + picFile.getAbsolutePath();
                        logger.error(errorMsg);
                        try {
                            os.write(errorMsg + "\r\n");
                        } catch (IOException e) {
                            logger.error("无法输出结果到输出文件：" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else if (checkMediaFile && (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                    PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType))) {
                //如果是媒体文件
                String errorMsg = "";
                cp.setExtraInt(404L);
                File mediaFile = new File(device.getLocalPath() + "/" + cp.getStringValue());
                if (mediaFile.exists()) {
                    SimpleFileInfo fileInfo = new SimpleFileInfo(mediaFile.getAbsolutePath(), mediaFile.length(), new Date(mediaFile.lastModified()), false,
                            FileType.video);
                    if (FileUtils.setFileMediaInfo(mediaFile.getAbsolutePath(), fileInfo)) {
                        cp.setLength((long) fileInfo.getLength());
                        cp.setThumbPic("?" + fileInfo.getWidth() + "x" + fileInfo.getHeight());
                        cp.setExtraInt(200L);
                    } else {
                        cp.setExtraInt(500L);
                        errorMsg = "媒体文件无法进行播放检查:《" + content.getName() +
                                "》的：" + mediaFile.getAbsolutePath();

                    }
                } else {
                    errorMsg = "媒体文件缺失：《" + content.getName() +
                            "》的" + cp.getName() + "(" + cp.getIntValue() + ")" +
                            "：" + mediaFile.getAbsolutePath();
                    cp.setExtraInt(404L);
                }
                if (!checkOnly) {
                    willSaveProperties.add(cp);
                }
                if (!"".equals(errorMsg)) {
                    logger.error(errorMsg);
                    //saveResult.add(errorMsg);
                    try {
                        os.write(errorMsg + "\r\n");
                    } catch (IOException e) {
                        logger.error("无法输出结果到输出文件：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                if (!checkOnly) {
                    willSaveProperties.add(cp);
/*
                    cp = contentPropertyLogicInterface.save(cp);
                    if (cp == null) {
                        logger.error("无法保存数据！");
                    }
*/
                }
            }
        }
        try {
            if (!checkOnly) {
                willSaveProperties = contentPropertyLogicInterface.setContentFromContentProperties(content, willSaveProperties,oldContentProperties);
                content = save(content);
                //把老的数据删了
                for(ContentProperty cp:oldContentProperties){
                    logger.debug("准备删除《"+content.getName()+"》的"+cp.getName()+","+cp.getStringValue());
                    contentPropertyLogicInterface.remove(cp);
                }
                for(ContentProperty cp:willSaveProperties){
                    cp.setContentId(content.getId());
                    logger.debug("准备保存《"+content.getName()+"》的"+cp.getName()+","+cp.getStringValue());
                    cp = contentPropertyLogicInterface.save(cp);
                    Property property = propertyLogic.getPropertyByCache(cp.getPropertyId());
                    Byte dataType = property.getDataType();
                    if (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                            PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType)){
                        //准备启动转码
                        if ("Media_Url_Source".equals(property.getCode())) {
                            //原来的逻辑里，需要检查文件存在才会进行转码，现在忽略这个检查
                            if (autoEncode) {
                                //加入自动转码队列，只有是源才做转码
                                //cp.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_HAS_ENCODED);
                                List<EncoderTask> tasks = encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content, cp);
                                String taskInfo = "";
                                for (EncoderTask task : tasks) {
                                    taskInfo += "" + task.getName() + ",";
                                }
                                if (!"".equals(taskInfo)) {
                                    taskInfo = taskInfo.substring(0, taskInfo.length() - 1);
                                }
                                saveResult.add("按照配置已经添加转码任务：" + taskInfo);
                            } else {
                                saveResult.add("因为配置要求，不启动转码任务：" + cp.getStringValue());
                            }
/*
                            if (cp.getExtraInt() == 200L) {
                            } else {
                                saveResult.add("因为文件不正确，不启动转码任务：" + cp.getStringValue() + "，如果后期补充视频文件，请在补充完成后通知系统管理员手工启动转码。");
                            }
*/
                        }
                    }else if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType) && shouldCopyPoster) {
                        //准备同步
                        if (shouldSyncPoster) {
                            JsUtils jsUtils = new JsUtils();
                            try {
                                String newFileUrl = cp.getStringValue();
                                File newPicFile = new File((webAppPath + newFileUrl).replace('/', File.separatorChar));
                                jsUtils.saveAndPushSynFile(fileNameHere, newPicFile.getAbsolutePath(), newFileUrl, content.getCspId());
                            } catch (Exception e) {
                                e.getMessage();
                                logger.error("同步信息出错");
                            }
                        }

                    }
                }
            } else {
                if (content.getId() <= 0) {
                    content.setId(999999);
                }
            }
        } catch (Exception e) {
            logger.error("无法保存Content：" + content.toString() + "\n错误信息：" + e.getMessage());
            return null;
        }
        return content;
    }


    public Device getDevice(Long deviceId) {
        return (Device) CacheUtils.get(deviceId, "device", new com.fortune.util.DataInitWorker() {
            public Object init(Object keyId, String cacheName) {
                try {
                    return deviceLogicInterface.get((Long) keyId);
                } catch (Exception e) {
                    logger.error("无法找到设备:" + keyId + "," + e.getMessage(), e);
                    return null;
                }
            }
        });
    }
    public Map<String,String> initMaps(boolean xmlToProperty,Long moduleId){
        Map<String, String> elementIdMaps = new HashMap<String, String>();
        Configuration propertyConfig = new Configuration("/propertyIds.properties");
        Properties properties = propertyConfig.getProperties();
        if (properties != null) {
            String header = "M" + moduleId + "_";
            for (Object keyObj : properties.keySet()) {
                String key = keyObj.toString();
                if (key.startsWith(header)) {
                    String value = properties.get(keyObj).toString();
                    key = key.substring(header.length());
                    if(xmlToProperty){
                        elementIdMaps.put(key, value);
                    }else{
                        elementIdMaps.put(value,key);
                    }
                }
            }
        }
        return elementIdMaps;
    }

    /**
     * 保存到xml时，序列化播放连接时按照集数来获取播放连接信息
     * @param clips  所有集
     * @param episodes 集数
     * @return 结果
     */
    public Map<String,String> getClipOfEpisodes(List<Map<String,String>> clips,String episodes){
        if(episodes==null){
            return null;
        }
        for(Map<String,String> clip:clips){
            if(clip!=null){
                if(episodes.equals(clip.get("Episodes"))){
                    return clip;
                }
            }
        }
        return null;
    }
    /**
     * 将一个content列表格式化到xml中。
     * @param fileName xml文件名
     * @param contents 媒体列表
     * @param moduleId 模版id
     */
    @SuppressWarnings("unchecked")
    public void saveToXml(String fileName,List<Content> contents,Long moduleId){
        if(moduleId==null||moduleId<=0){
            moduleId = 10000L;
        }
        Map<String,String> ids = initMaps(false,moduleId);
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            fw.write("<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n");
            fw.write("<Contents TimeStamp=\"" +StringUtils.date2string(new Date(),"yyyyMMddHHmmss")+
                    "\" CPCode=\"1\">\r\n");
            for(Content content:contents){
                Map<String,Object> properties = content.getProperties();
                Map<String,List<Map<String,String>>> allUrls = new HashMap<String,List<Map<String,String>>>();
                String contentId = content.getContentId();
                if(contentId==null||"".equals(contentId.trim())){
                    contentId = ""+content.getId();
                }
                fw.write("\t<content ContentID=\"" + contentId +
                        "\" ");
                int i=0;
                for(String key:ids.keySet()){
                    List<Map<String,String>> clips = null;
                    String attributeName = ids.get(key);
                    String attributeValue = null;
                    //如果是自身的一个属性
                    if(key.startsWith("@")){
                        key = key.substring(1);
                        try {
                            attributeValue = BeanUtils.getProperty(content,key);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }if(key.startsWith("%")){
                        key = key.substring(1);
                    }
                    if(attributeValue==null){
                        Object propertyValue = properties.get(key);
                        if(propertyValue!=null){
                            //检查属性是否是播放连接
                            List<Map<String,String>> playUrlTypes =(List<Map<String,String>>) properties.get("playUrlTypes");
                            boolean isNotClips = true;
                            if(playUrlTypes!=null){
                                for(Map<String,String> type:playUrlTypes){
                                    String code = type.get("code");
                                    if(key.equals(code)){
                                        if(propertyValue instanceof List){
                                            //这是点播连接
                                            clips = (List)propertyValue;
                                            isNotClips = false;
                                        }else if(propertyValue instanceof Map){
                                            clips = new ArrayList<Map<String,String>>();
                                            clips.add((Map<String,String>)propertyValue);
                                            isNotClips = false;
                                        }else{
                                            logger.error("无法找到正确的数据类型："+key+",name="+content.getName());
                                            isNotClips = true;
                                        }
                                        break;
                                    }
                                }
                            }
                            if(isNotClips){
                                attributeValue = propertyValue.toString();
                            }
                        }
                    }
                    if(attributeValue!=null){
                        i++;
                        if(i%5==0){
                            fw.write("\r\n\t\t\t");
                        }
                        fw.write(" "+attributeName+"=\""+StringUtils.escapeXMLTags(attributeValue).replace('"','\'')+"\"");
                    }else if(clips!=null){
                        allUrls.put(attributeName,clips);
                    }
                }
                fw.write(">\r\n");
                //以集数为关键索引的播放连接
                List<Map<String,String>> playUrls = new ArrayList<Map<String,String>>();
                //准备输出影片视频片段信息
                for(String propertyCode:allUrls.keySet()){
                    List<Map<String,String>> clips = allUrls.get(propertyCode);
                    if(clips!=null){

                        try {
                            for(Map<String,String> clip:clips){
                                String playUrl = clip.get("playUrl");
                                if(playUrl==null){
                                    continue;
                                }
                                int p=playUrl.indexOf("?");
                                if(p>0){
                                    playUrl = playUrl.substring(0,p);
                                }
                                String episodes= clip.get("no");
                                Map<String,String> url = getClipOfEpisodes(playUrls,episodes);
                                if(url==null){
                                    url = new HashMap<String,String>();
                                    url.put("Episodes",episodes);
                                    playUrls.add(url);
                                }
                                url.put(propertyCode,playUrl);
                            }
                        } catch (Exception e) {
                            logger.error("导出《" +content.getName()+
                                    "》片段信息时发生异常："+e.getMessage());
                        }
                    }
                }
                fw.write("\t\t<subcontents>\r\n");
                for(Map<String,String> playUrl:playUrls){
                    fw.write("\t\t\t<subcontent");
                    for(String key:playUrl.keySet()){
                        String value = playUrl.get(key);
                        if(value!=null){
                            fw.write(" "+key+"=\""+value+"\"");
                        }
                    }
                    fw.write("/>\r\n");
                }
                fw.write("\t\t</subcontents>\r\n");
                //输出频道信息
                List<Channel> channels = (List<Channel>) content.getProperties().get("channels");
                if (channels == null || channels.size() <= 0) {
                    logger.error("");
                } else {
                    fw.write("\t\t<channels>\r\n");
                    for (Channel channel: channels) {
                        fw.write("\t\t\t<channel ChannelId=\""+channel.getId()+"\" ChannelName=\"" +
                                channel.getName()+"\"/>\r\n");
                    }
                    fw.write("\t\t</channels>\r\n");
                }
                        fw.write("\t</content>\r\n");
            }
            fw.write("</Contents>");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Map<String,Property> getPropertyMap(Long moduleId){
        Map<String,Property> propertyMap = new HashMap<String,Property>();
        List<Property> propertiesOfModule = propertyLogic.getPropertiesOfModule(moduleId,-1L,null,new PageBean(1,10000,null,null));
        for(Property p:propertiesOfModule){
            propertyMap.put(p.getCode(),p);
        }
        return propertyMap;
    }
    /**
     * 扫描一个xml文件，返回一个媒体列表
     *
     * @param fileName xml文件名
     * @return 扫描结果，媒体列表
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> scanXml(String fileName, Long moduleId, List<String> response, String xmlFileEncoding) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String,Property> propertyMap = getPropertyMap(moduleId);
        List<Content> result = new ArrayList<Content>();
        logger.debug("启动扫描xml文件："+fileName);
        map.put("contents", result);
        //控制不能同时操作一个文件
        String xmlName;
        xmlName = fileName;
        String resourceKey = "xmlName=" + xmlName;
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        try {
            threadUtils.acquire(resourceKey);
            Date startTime = new Date();
            map.put("startTime", startTime);
            AppConfigurator config = AppConfigurator.getInstance();
            if (moduleId == null || moduleId <= 0) {
                moduleId = config.getLongConfig("importXml.default.moduleId", 5000L);
            }
            int importContentCount = 0;
            int importErrorCount = 0;
            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                response.add("没有找到这个xml文件，请与管理员联系！");
                logger.error("无法找到XML文件：" + xmlFile.getAbsolutePath());
                return map;
            }
            org.dom4j.Element root = XmlUtils.getRootFromXmlFile(fileName, xmlFileEncoding);
            //org.dom4j.Element root = XmlUtils.getRootFromXmlStr(xmlContent);
            Map<String, String> elementIdMaps = initMaps(true,moduleId);//
            String importContentId;
            String mediaName;
            Long channelId;
            String tempStr;
            boolean ignoreCopyRight = config.getBoolConfig("importXml.default.ignoreCopyRight", false);
            try {
                if (ignoreCopyRight) {
                    response.add("忽略版权信息，所有导入影片均设置为10年后过期！");
                } else {
                    response.add("严格按照XML中的版权信息导入到系统中！");
                }
                List allContentNodes = root.selectNodes("content");
                long cpCode = XmlUtils.getLongValue(root, "@CPCode", -1);
                map.put("cpCode", cpCode);
                int i = 0, nodeCount = allContentNodes.size();
                for (Object node : allContentNodes) {
                    i++;
                    if (i % 50 == 0) {
                        logger.debug("处理了" + i + "/" + nodeCount + ",百分比" + Math.round(i * 100.0 / nodeCount) + "%...");
                    }
                    Node contentNode = (Node) node;
                    importContentId = XmlUtils.getValue(contentNode, "@ContentID", "");
                    //判断  ContentID 在数据库中是否存在
                    mediaName = XmlUtils.getValue(contentNode, "@MediaName", "");

                    String directors = XmlUtils.getValue(contentNode, "@MediaDirectors", "");
                    String actors = XmlUtils.getValue(contentNode, "@MediaActors", "");
                    String intro = XmlUtils.getValue(contentNode, "@MediaIntro", "");
                    Content content = newContent(-1, importContentId, mediaName, cpCode,
                            directors,
                            actors,
                            -1L, moduleId,
                            intro);
                    result.add(content);
                    String contentName = content.getName();
                    List<ContentProperty> cps = getContentPropertiesFromXmlNode(content, contentNode, elementIdMaps, 0L, response, null, null,propertyMap);
                    cps = contentPropertyLogicInterface.setContentFromContentProperties(content,cps);
                    content.getProperties().put("cps", cps);
                    if (moduleId == 1) {
                        List<ContentProperty> clips = new ArrayList<ContentProperty>();
                        for (Object subN : contentNode.selectNodes("MediaClips/MediaClip")) {
                            Property property = getPropertyByCode("MEDIA_CLIP");
                            if (property != null) {
                                Node subContentNode = (Node) subN;

                                int episodes = XmlUtils.getIntValue(subContentNode, "@MediaClipEpisodes", 1);
                                int MediaClipLength = XmlUtils.getIntValue(subContentNode, "@MediaClipLength", 1);
                                int MediaClipWidth = XmlUtils.getIntValue(subContentNode, "@MediaClipWidth", 1);
                                String MediaClipSource = XmlUtils.getValue(subContentNode, "@MediaClipSource", "");

                                ContentProperty clip = new ContentProperty();
                                clip.setIntValue((long) episodes);
                                clip.setPropertyId(property.getId());
                                clip.setStringValue(MediaClipSource);
                                clip.setExtraData(episodes + "###" + MediaClipWidth + "###" + MediaClipLength);
                                clip.setContentId(null);

                                clips.add(clip);

                            }
                        }

                        if (clips != null && clips.size() > 0) {
                            cps.addAll(clips);
                        }
                    }else{
                        for (Object subN : contentNode.selectNodes("subcontents/subcontent")) {
                            Node subContentNode = (Node) subN;
                            int episodes = XmlUtils.getIntValue(subContentNode, "@Episodes", 1);
                            ContentProperty clip = new ContentProperty();
                            clip.setIntValue((long) episodes);
                            clip.setName(XmlUtils.getValue(subContentNode, "@SubContentName", contentName + "第" + episodes + "集"));
                            clip.setSubContentId(XmlUtils.getValue(subContentNode, "@SubContentID", ""));
                            List<ContentProperty> clips = getContentPropertiesFromXmlNode(content, subContentNode, elementIdMaps, episodes, response, clip, null,propertyMap);
                            cps.addAll(clips);
                            //cps.addAll(getContentPropertiesFromXmlNode(subContentNode, elementIdMaps, episodes,result));
                        }
                    }

                    Node copyrightNode = contentNode.selectSingleNode("copyrights/copyright");
                    String validStartTimeStr;
                    String validEndTimeStr;
                    if(copyrightNode!=null){
                        validStartTimeStr = XmlUtils.getValue(copyrightNode, "@EffectiveTime", "20130101").trim();
                        validEndTimeStr = XmlUtils.getValue(copyrightNode, "@ExpireTime", "20230101").trim();
                    }else{
                        validEndTimeStr = "20230101";
                        validStartTimeStr = "20130101";
                    }
                    String formatStr = "yyyyMMdd";
                    if (ignoreCopyRight || "".equals(validEndTimeStr)||null==validEndTimeStr) {
                        validStartTimeStr = StringUtils.date2string(new Date(), "yyyyMMdd");//设置成现在
                        validEndTimeStr = StringUtils.date2string(new Date(System.currentTimeMillis() + 10 * 365 * 24 * 3600 * 1000L), "yyyyMMdd");//设置十年后
                    }
                    int p = validEndTimeStr.indexOf(" ");
                    if (p > 0) {
                        validEndTimeStr = validEndTimeStr.substring(0, p).trim();
                        validStartTimeStr = validStartTimeStr.substring(0, p).trim();
                        formatStr = "yyyy-MM-dd";
                        if (validEndTimeStr.contains("/")) {
                            formatStr = "yyyy/MM/dd";
                        }
                    }
                    Date validStartTime = StringUtils.string2date(validStartTimeStr, formatStr);
                    content.setValidStartTime(validStartTime);
                    Date validEndTime = StringUtils.string2date(validEndTimeStr, formatStr);
                    content.setValidEndTime(validEndTime);
                    Date now = new Date();
                    if (validEndTime.before(new Date(now.getTime() + 7 * 24 * 3600 * 1000L))) {
                        importErrorCount++;

                        String warnStr = "已经过期了" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                        if (validEndTime.after(now)) {
                            warnStr = "再过" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                    "就会过期";
                        }
                        tempStr = "警告：导入媒体《" + content.getName() + "(id=" + content.getId() + ")" +
                                "》的版权到期日期可能有问题，" + warnStr +
                                "。如果已经过期，会被系统自动下线：起始日期" +
                                StringUtils.date2string(validStartTime) + "->结束日期" +
                                StringUtils.date2string(validEndTime) + "，当前日期：" + StringUtils.date2string(new Date()) +
                                "(xml中数据：" + validStartTimeStr + "->" + validEndTimeStr + ")";
                        logger.error(tempStr);
                        response.add(tempStr);
                    }
                    if (content == null) {
                        importErrorCount++;
                        tempStr = "导入媒体是发生异常，导入的XML为：\n" + contentNode.asXML();
                        logger.error(tempStr);
                        response.add(tempStr);
                        importErrorCount++;
                    }                    //如果不是老的数据，那就要新建对应的审计和csp表
                    List channels = contentNode.selectNodes("channels/channel");
                    if (channels == null || channels.size() <= 0) {
                        tempStr = content.getName() + "媒体没有频道节点!";
                        response.add(tempStr);
                        importErrorCount++;
                        logger.error(tempStr);
                    } else {
                        List<Channel> channelsList = (List<Channel>) content.getProperties().get("channels");
                        if (channelsList == null) {
                            channelsList = new ArrayList<Channel>();
                        }
                        for (Object node5 : channels) {
                            Node channelNode = (Node) node5;
                            String channelIdStr = XmlUtils.getValue(channelNode, "@ChannelId", null);
                            channelId = StringUtils.string2long(channelIdStr, -1);
                            String channelName = XmlUtils.getValue(channelNode, "@Name", "");
                            Channel channel = new Channel();
                            channel.setId(channelId);
                            channel.setName(channelName);
                            channelsList.add(channel);
                        }
                        content.getProperties().put("channels", channelsList);
                    }
                    importContentCount++;
                }
                String resultStr = "处理选中的xml文件完毕，累计";
                resultStr += "媒体：" + importContentCount + "个！错误" + importErrorCount + "个！";
                response.add(resultStr);
                logger.debug(resultStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("发生异常，无法继续执行导入操作：" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("扫描XML时发生异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        map.put("endTime", new Date());
        logger.debug("完成扫描xml文件："+fileName);
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<String> importXmlV2(String xmlFileName, Long cspId, Integer adminId, Long deviceId,
                                  Long moduleId, String xmlFileEncoding, String webAppPath, String cspFilePath, String fileNameHere,
                                  boolean checkOnly) {
        //控制不能同时操作一个文件
        //String xmlName = xmlFileName;
        String resourceKey = "xmlName=FunctionImportXmlV2_" + xmlFileName;
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        try {
            threadUtils.acquire(resourceKey);
            Date startTime = new Date();
            AppConfigurator config = AppConfigurator.getInstance();
            if (moduleId == null || moduleId <= 0) {
                moduleId = config.getLongConfig("importXml.default.moduleId", 5000L);
            }
            if (deviceId == null || deviceId <= 0) {
                //找到和这个csp绑定的服务器，并选出第一个
                List<Device> devicesOfCsp = cspDeviceLogicInterface.getDeviceOfCsp(cspId);
                if (devicesOfCsp != null && devicesOfCsp.size() > 0) {
                    deviceId = devicesOfCsp.get(0).getId();
                } else {
                    deviceId = config.getLongConfig("importXml.default.deviceId", 11054546l);//默认的一个，三屏互动的设备id
                }
            }
            int importContentCount = 0;
            int importErrorCount = 0;
            List<String> result = new ArrayList<String>();
            File xmlFile = new File(xmlFileName);
            if (!xmlFile.exists()) {
                result.add("没有找到这个xml文件，请与管理员联系！");
                logger.error("无法找到XML文件：" + xmlFile.getAbsolutePath());
                return result;
            }
            String importContentId;
            String mediaName;

            Long channelId;
            Device device = null;
            try {
                device = deviceLogicInterface.get(deviceId);
            } catch (Exception e) {
                String info = "无法找到流媒体服务器，ID=" + deviceId;
                logger.error(info);
                result.add(info);
                importErrorCount++;
            }

            File importResultFile = new File(webAppPath + "/import/logs/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss_") + cspId + "_" + xmlFile.getName() +
                    Math.round(Math.random() * 1000) + ".txt");
            File pathFile = importResultFile.getParentFile();
            if (!pathFile.exists()) {
                if (!pathFile.mkdirs()) {
                    logger.error("无法创建日志目录！日志记录可能会有问题！");
                }
            }
            //将导入的xml做一个备份
            File backupXmlFile = new File(webAppPath + "/import/backup/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss") + "_" + xmlFile.getName());
            FileUtils.copy(xmlFile, backupXmlFile.getParentFile().getAbsolutePath(), backupXmlFile.getName());
            result.add("xml文件已经备份到：" + backupXmlFile.getAbsolutePath());
            OutputStreamWriter os = null;
            String tempStr;
            boolean ignoreCopyRight = config.getBoolConfig("importXml.default.ignoreCopyRight", false);
            try {
                if (ignoreCopyRight) {
                    result.add("忽略版权信息，所有导入影片均设置为10年后过期！");
                } else {
                    result.add("严格按照XML中的版权信息导入到系统中！");
                }
                Map<String, Object> xmlResult = scanXml(xmlFileName, moduleId, result, xmlFileEncoding);
                List<Content> contentsFromXml = (List<Content>) xmlResult.get("contents");
                os = new OutputStreamWriter(new FileOutputStream(importResultFile));
                os.write(StringUtils.date2string(startTime) +
                        "-" + "启动处理xml文件：" + xmlFile.getAbsolutePath() + "\r\n");
                Long cpCode = (Long) xmlResult.get("cpCode");

                if (cspId.equals(cpCode)) {
                    int i = 0, nodeCount = contentsFromXml.size();
                    for (Content contentFromXml : contentsFromXml) {
                        int startErrorCount = importErrorCount;
                        i++;
                        if (i % 50 == 0) {
                            logger.debug("处理了" + i + "/" + nodeCount + ",百分比" + Math.round(i * 100.0 / nodeCount) + "%...");
                            os.flush();
                        }
                        importContentId = contentFromXml.getContentId();
                        //判断  ContentID 在数据库中是否存在
                        mediaName = contentFromXml.getName();
                        List<Content> contents = getContent(mediaName, null, cspId);

                        int oldCount = contents != null ? contents.size() : 0;
                        Content content = null;
                        if (oldCount > 0) {
                            content = contents.get(0);
                            String msg = "媒体《" + mediaName +
                                    "》重复导入，原平台ID为" + importContentId + "，在本平台ID为" +
                                    content.getId() +
                                    "，在本平台影片名为：《" + content.getName() +
                                    "》，入库时间：" + StringUtils.date2string(content.getCreateTime()) +
                                    "，当前状态：" + getStatusString(content.getStatus()) +
                                    "，尝试修改替换原有数据！";
                            result.add(msg);
                            logger.warn(msg);
                            //保留第一个并覆盖第一个数据的内容
                            //其他的下线
                            if (oldCount > 1) {
                                for (int idx = 1; idx < oldCount; idx++) {
                                    Content c = contents.get(idx);
                                    if (ContentLogicInterface.STATUS_CP_ONLINE.equals(c.getStatus())) {
                                        msg = "媒体《" + c.getName() +
                                                "》重复导入，而且，在本平台也重复了！在原CP平台的ID" +
                                                "为" + c.getContentId() + "，在本平台ID为" + c.getId() +
                                                "，入库时间为：" + StringUtils.date2string(c.getCreateTime()) +
                                                "，导入结束后，将会将此条记录下线！";
                                        result.add(msg);
                                        logger.warn(msg);
                                        if (!checkOnly) {
                                            cpOfflineContent(c);
                                        }
                                    }
                                }
                            }
                        }
                        boolean hasOldData = false;
                        boolean importFromXml = true;
                        contentFromXml.setDeviceId(deviceId);
                        contentFromXml.setModuleId(moduleId);
                        List<ContentProperty> oldCps = new ArrayList<ContentProperty>();
                        if (content != null) {
                            hasOldData = true;
                            contentFromXml.setId(content.getId());
                            oldCps = contentPropertyLogicInterface.getContentProperties(content.getId(), -1);
                        } else {
                            content = contentFromXml;
                        }
                        if (importFromXml) {
                            List<ContentProperty> cps = (List<ContentProperty>) contentFromXml.getProperties().get("cps");
                            String picPath = device.getLocalPath();// xmlFile.getParentFile().getAbsolutePath();
                            if(config.getBoolConfig("importXml.picPath.relateFromXmlPath",true)){
                                picPath = xmlFile.getParentFile().getAbsolutePath();
                            }
                            content = saveContentV2(contentFromXml, cps, oldCps, result, picPath, webAppPath, cspFilePath, fileNameHere, os, checkOnly);
                            int posterCount = 0;
                            int posterCanUseCount = 0;
                            int clipCount = 0;
                            int clipCanUseCount = 0;
                            for (ContentProperty cp : cps) {
                                Property property = propertyLogic.getPropertyByCache(cp.getPropertyId());
                                if (property != null) {
                                    Byte dataType = property.getDataType();
                                    if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType)) {
                                        posterCount++;
                                        File picFile = new File(webAppPath + "/" + cp.getStringValue());
                                        if (picFile.exists() && (!picFile.isDirectory())) {
                                            posterCanUseCount++;
                                        } else {
                                            String info = "媒体《" + content.getName() +
                                                    "》的海报“" + cp.getName() + "," + cp.getIntValue() + ",xml中数据：" + cp.getStringValue() + ",文件位置：" + picFile.getAbsolutePath() +
                                                    "”没有找到！";
                                            os.write(info + "\r\n");
                                            result.add(info);
                                            importErrorCount++;
                                        }
                                    } else if (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) || PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType) ||
                                            PropertyLogicInterface.DATA_TYPE_WMV.equals(dataType)) {
                                        if (device != null) {
                                            clipCount++;
                                            File mediaFile = new File(device.getLocalPath() + "/" + cp.getStringValue());
                                            if (mediaFile.exists() && (!mediaFile.isDirectory())) {
                                                clipCanUseCount++;
                                            } else {
                                                String info = "媒体《" + content.getName() +
                                                        "》的片段“" + cp.getName() + "," + cp.getIntValue() + "," + mediaFile.getAbsolutePath() +
                                                        "”没有找到！";
                                                os.write(info + "\r\n");
                                                result.add(info);
                                                importErrorCount++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (posterCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》在XML数据中没有任何海报！");
                            } else if (posterCanUseCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》没有任何可用海报！");
                            }
                            if(config.getBoolConfig("importXml.default.checkMediaFile", true)){
                                if (clipCount == 0) {
                                    result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                            ")》没有在XML中找到任何播放片段！");
                                } else if (clipCanUseCount == 0) {
                                    result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                            ")》没有任何可用播放片段！");
                                }
                            }else{
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》系统设置不检查媒体文件，所谓不知道媒体文件情况！！");
                            }
                            Date now = new Date();
                            Date validEndTime = content.getValidEndTime();
                            Date validStartTime = content.getValidStartTime();
                            if (validEndTime.before(new Date(now.getTime() + 7 * 24 * 3600 * 1000L))) {
                                importErrorCount++;

                                String warnStr = "已经过期了" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                                if (validEndTime.after(now)) {
                                    warnStr = "再过" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                            "就会过期";
                                }
                                tempStr = "警告：导入媒体《" + content.getName() + "(id=" + content.getId() + ")" +
                                        "》的版权到期日期可能有问题，" + warnStr +
                                        "。如果已经过期，会被系统自动下线：起始日期" +
                                        StringUtils.date2string(validStartTime) + "->结束日期" +
                                        StringUtils.date2string(validEndTime) + "，当前日期：" + StringUtils.date2string(new Date());
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                            }
                            if (contentFromXml == null) {
                                importErrorCount++;
                                tempStr = "导入媒体是发生异常，导入数据为空";
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                                importErrorCount++;
                            }
                        }
                        //如果不是老的数据，那就要新建对应的审计和csp表
                        if (!hasOldData) {
                            if (!checkOnly) {
                                ContentAudit contentAudit = saveContentAudit(content.getId());
                                saveContentCsp(content.getId(), cpCode, contentAudit.getId(), ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                            }
                        }
                        List<Channel> channels = (List<Channel>) contentFromXml.getProperties().get("channels");
                        if (channels == null || channels.size() <= 0) {
                            tempStr = content.getName() + "媒体没有频道节点!";
                            result.add(tempStr);
                            os.write(tempStr + "\r\n");
                            importErrorCount++;
                            logger.error(tempStr);
                        } else {
                            for (Channel channelFromXml : channels) {
                                channelId = channelFromXml.getId();
                                if (channelId < 0) {
                                    tempStr = content.getName() + "频道ID没有正确输入!";
                                    result.add(tempStr);
                                    os.write(tempStr + "\r\n");
                                    logger.error(tempStr);
                                    continue;
                                }
                                String channelName = channelFromXml.getName();
                                //查看当前用户是否有这个频道或者权限
                                Channel channel = (Channel) TreeUtils.getInstance().getObject(Channel.class, channelId);
                                if (channel == null) {
                                    //频道ID不对。尝试进行猜测，能否找到一个名字上只是多了括号的
                                    tempStr = content.getName() + ",无法通过ChannelID=" + channelId + "来查找频道，尝试通过频道名反查：" + channelName + "....";
                                    channel = (Channel) CacheUtils.get(channelName, "channelByNameCache", new com.fortune.util.DataInitWorker() {
                                        public Object init(Object key, String cacheName) {
                                            Channel bean = new Channel();
                                            String searchName = key.toString();
                                            bean.setName(searchName);

                                            List<Channel> channelList = channelLogic.search(bean);
                                            for (Channel ch : channelList) {
                                                String name = ch.getName();
                                                if (name.equals(searchName) || name.startsWith(searchName + "(") ||
                                                        name.startsWith(searchName + "（")) {
                                                    return ch;
                                                }
                                            }
                                            return null;
                                        }
                                    });
                                    if (channel == null) {
                                        importErrorCount++;
                                        tempStr += "《" + content.getName() +
                                                "》导入时，无法找到频道，无论是通过ID=" + channelId + ",还是通过Name=" + channelName;
                                        logger.error(tempStr);
                                        os.write(tempStr + "\r\n");
                                        importErrorCount++;
                                        result.add(tempStr);
                                        continue;
                                    } else {
                                        channelName = channel.getName();
                                        channelId = channel.getId();
                                    }
                                } else {
                                    channelName = channel.getName();
                                }

                                boolean hasPrivilegeToChannel = false;
                                boolean allowPublishToUnLeafChannel = config.getBoolConfig("importXml.allowPublishToUnLeafChannel", true);  //
                                boolean isLeafChannel = channelLogic.isLeafChannel(channelId);
                                if(cspId==1||cspId==2){
                                    //如果是root进行导入，不考虑授权信息
                                    hasPrivilegeToChannel = true;
                                    allowPublishToUnLeafChannel = true;
                                    isLeafChannel = true;
                                }else{
                                    if (moduleId == 5000) {
                                        hasPrivilegeToChannel = cspChannelLogicInterface.hasPrivilegeToChannel(cspId, channelId);
                                    } else if (moduleId == 1) {
                                        hasPrivilegeToChannel = channelLogic.hasPrivilegeToChannelSX(cspId, channelId);
                                    }
                                }
                                if (hasPrivilegeToChannel) {
                                    //判断是否为叶子节点。
                                    if (isLeafChannel || ((!isLeafChannel) && allowPublishToUnLeafChannel)) {
                                        //把频道和content绑定起来
                                        boolean shouldCreateContentChannel = true;
                                        //如果这个影片已经有了，那就要检查是否已经发布到这个频道。如果没有，就发布。如果已经发布，就放弃
                                        if (hasOldData && contentChannelLogic.isExists(channelId, content.getId())) {
                                            shouldCreateContentChannel = false;
                                            tempStr = "媒体《" + content.getName() + "》已经存在，已经设置过频道绑定，不再发布：" + channelName;
                                            //os.write(tempStr+"\r\n");
                                            result.add(tempStr);
                                            logger.debug(tempStr);
                                        }
                                        if (shouldCreateContentChannel) {
                                            if (hasOldData) {
                                                tempStr = "媒体《" + content.getName() + "》已经存在，重新发布到频道：" + channelName;
                                                logger.debug(tempStr);
                                                os.write(tempStr + "\r\n");
                                            }
                                            if (!checkOnly) {
                                                saveContentChannel(content.getId(), channelId);
                                                //

                                            }
                                        }
                                    } else {
                                        importErrorCount++;
                                        tempStr = "频道“" + channelName + "”不是叶子节点，批量导入时会拒绝《" + content.getName() + "》发布媒体到这个频道！";
                                        os.write(tempStr + "\r\n");
                                        logger.error(tempStr);
                                    }
                                } else {
                                    tempStr = "《" + content.getName() + "》导入时发生错误：没有权限绑定到id为" + channelId + "名字为《" + channelName + "》这个频道，请创建或者管理员给频道赋权";
                                    result.add(tempStr);
                                    logger.error(tempStr);
                                    importErrorCount++;
                                    os.write(tempStr + "\r\n");
                                    os.flush();
                                }
                            }
                        }
                        int contentErrorCount = importErrorCount - startErrorCount;
                        if (contentErrorCount == 0) {
                            //没有错误，如果原媒体没有上线，则尝试将其上线发布
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                                content.setStatusTime(new Date());
                                content = save(content);
                                contentCspLogicInterface.setStatus(content.getId(), content.getCspId(), -1, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                result.add("媒体《" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")》没有错，尝试将其直接上线发布！");
                            }
                        } else {
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                result.add("媒体《" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")》信息中有" + contentErrorCount +
                                        "个错误，现在状态是" + getStatusString(content.getStatus()) +
                                        "，请您手工操作补充数据后上线！");
                            }
                        }
                        importContentCount++;
                    }
                } else {
                    tempStr = "当前用户不能操作这xml,xml的cpCode应改为" + cspId;
                    result.add(tempStr);
                    os.write(tempStr + "\r\n");
                    importErrorCount++;
                    logger.error("当前你没有这个权限！不能操作这xml");
                }
                String resultStr = "处理选中的xml文件完毕，累计";
                if (checkOnly) {
                    resultStr += "检查";
                } else {
                    resultStr += "添加";
                }
                resultStr += "媒体：" + importContentCount + "个！错误" + importErrorCount + "个！";
                os.write(resultStr + "\r\n");
                result.add(resultStr);
                logger.debug(resultStr);
                Date stopTime = new Date();
                os.write("-启动时间：" + StringUtils.date2string(startTime) + "\n");
                os.write("-完成时间：" + StringUtils.date2string(stopTime));
            } catch (IOException e) {
                logger.error("无法找到文件：" + importResultFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("发生异常，无法继续执行导入操作：" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("无法关闭文件：" + importResultFile.getAbsolutePath());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("保存影片信息是发生异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public List<String> importXml(String xmlFileName, Long cspId, Integer adminId, Long deviceId,
                                    Long moduleId, String xmlFileEncoding, String webAppPath, String cspFilePath, String file,
                                    boolean checkOnly) {
/*
        String xmlContent = "";
        xmlContent = FileUtils.readFileInfo(xmlFileName, "UTF-8");
        if (!xmlContent.subSequence(0, 1).equals("<")) {
            xmlContent = xmlContent.substring(1);
        }
*/
        //控制不能同时操作一个文件
        //String xmlName = xmlFileName;
        AppConfigurator config = AppConfigurator.getInstance();
        if(config.getBoolConfig("system.compactMode",false)){
            return importXmlV2(xmlFileName,cspId,adminId,deviceId,moduleId,xmlFileEncoding,webAppPath,cspFilePath,file,checkOnly);
        }
        String resourceKey = "xmlName=" + xmlFileName;
        ThreadUtils threadUtils = ThreadUtils.getInstance();
        try {
            threadUtils.acquire(resourceKey);
            Date startTime = new Date();
            if (moduleId == null || moduleId <= 0) {
                moduleId = config.getLongConfig("importXml.default.moduleId", 5000L);
            }
            if (deviceId == null || deviceId <= 0) {
                //找到和这个csp绑定的服务器，并选出第一个
                List<Device> devicesOfCsp = cspDeviceLogicInterface.getDeviceOfCsp(cspId);
                if (devicesOfCsp != null && devicesOfCsp.size() > 0) {
                    deviceId = devicesOfCsp.get(0).getId();
                } else {
                    deviceId = config.getLongConfig("importXml.default.deviceId", 11054546l);//默认的一个，三屏互动的设备id
                }
            }
            int importContentCount = 0;
            int importErrorCount = 0;
            List<String> result = new ArrayList<String>();
/*
        if(xmlFileEncoding==null){
            xmlFileEncoding = config.getConfig("importXml.default.fileEncoding","UTF-8");
        }
*/
            File xmlFile = new File(xmlFileName);
            if (!xmlFile.exists()) {
                result.add("没有找到这个xml文件，请与管理员联系！");
                logger.error("无法找到XML文件：" + xmlFile.getAbsolutePath());
                return result;
            }
            org.dom4j.Element root = XmlUtils.getRootFromXmlFile(xmlFileName, xmlFileEncoding);
            //org.dom4j.Element root = XmlUtils.getRootFromXmlStr(xmlContent);
            Map<String, String> elementIdMaps = initMaps(true,moduleId);
            Map<String,Property> propertyMap = getPropertyMap(moduleId);

            String importContentId;
            String mediaName;

            Long channelId;
            Device device = null;
            try {
                device = deviceLogicInterface.get(deviceId);
            } catch (Exception e) {
                String info = "无法找到流媒体服务器，ID=" + deviceId;
                logger.error(info);
                result.add(info);
                importErrorCount++;
            }

            File importResultFile = new File(webAppPath + "/import/logs/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss_") + cspId + "_" + xmlFile.getName() +
                    Math.round(Math.random() * 1000) + ".txt");
            File pathFile = importResultFile.getParentFile();
            if (!pathFile.exists()) {
                if (!pathFile.mkdirs()) {
                    logger.error("无法创建日志目录！日志记录可能会有问题！");
                }
            }
            //将导入的xml做一个备份
            File backupXmlFile = new File(webAppPath + "/import/backup/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss") + "_" + xmlFile.getName());
            FileUtils.copy(xmlFile, backupXmlFile.getParentFile().getAbsolutePath(), backupXmlFile.getName());
            result.add("xml文件已经备份到：" + backupXmlFile.getAbsolutePath());
            OutputStreamWriter os = null;
            String tempStr;
            boolean ignoreCopyRight = config.getBoolConfig("importXml.default.ignoreCopyRight", false);
            try {
                if (ignoreCopyRight) {
                    result.add("忽略版权信息，所有导入影片均设置为10年后过期！");
                } else {
                    result.add("严格按照XML中的版权信息导入到系统中！");
                }
                os = new OutputStreamWriter(new FileOutputStream(importResultFile));
                os.write("-" + StringUtils.date2string(startTime) +
                        "启动处理xml文件：" + xmlFile.getAbsolutePath() + "\r\n");
                List allContentNodes = root.selectNodes("content");
                long cpCode = XmlUtils.getLongValue(root, "@CPCode", -1);

                if (cspId == cpCode) {
                    int i = 0, nodeCount = allContentNodes.size();
                    for (Object node : allContentNodes) {
                        int startErrorCount = importErrorCount;
                        i++;
                        if (i % 50 == 0) {
                            logger.debug("处理了" + i + "/" + nodeCount + ",百分比" + Math.round(i * 100.0 / nodeCount) + "%...");
                            os.flush();
                        }
                        Node contentNode = (Node) node;
                        importContentId = XmlUtils.getValue(contentNode, "@ContentID", "");
                        //判断  ContentID 在数据库中是否存在
                        mediaName = XmlUtils.getValue(contentNode, "@MediaName", "");
                        Content content = null;
                        List<Content> contents = getContent(mediaName, null, cspId);
                        int oldCount = contents != null ? contents.size() : 0;
                        if (oldCount > 0) {
                            content = contents.get(0);
                            String msg = "媒体《" + mediaName +
                                    "》重复导入，原平台ID为" + importContentId + "，在本平台ID为" +
                                    content.getId() +
                                    "，在本平台影片名为：《" + content.getName() +
                                    "》，入库时间：" + StringUtils.date2string(content.getCreateTime()) +
                                    "，当前状态：" + getStatusString(content.getStatus()) +
                                    "，尝试修改替换原有数据！";
                            result.add(msg);
                            logger.warn(msg);
                            //保留第一个并覆盖第一个数据的内容
                            //其他的下线
                            if (oldCount > 1) {
                                for (int idx = 1; idx < oldCount; idx++) {
                                    Content c = contents.get(idx);
                                    if (ContentLogicInterface.STATUS_CP_ONLINE.equals(c.getStatus())) {
                                        msg = "媒体《" + c.getName() +
                                                "》重复导入，而且，在本平台也重复了！在原CP平台的ID" +
                                                "为" + c.getContentId() + "，在本平台ID为" + c.getId() +
                                                "，入库时间为：" + StringUtils.date2string(c.getCreateTime()) +
                                                "，导入结束后，将会将此条记录下线！";
                                        result.add(msg);
                                        logger.warn(msg);
                                        if (!checkOnly) {
                                            cpOfflineContent(c);
                                        }
                                    }
                                }
                            }
                        }
                        boolean hasOldData = false;
                        boolean importFromXml = true;
                        String directors = XmlUtils.getValue(contentNode, "@MediaDirectors", "");
                        String actors = XmlUtils.getValue(contentNode, "@MediaActors", "");
                        String intro = XmlUtils.getValue(contentNode, "@MediaIntro", "");
                        List<ContentProperty> oldCps = new ArrayList<ContentProperty>();
                        if (content != null) {
/*
                        String msg = "媒体《" + mediaName +
                                "》重复导入，ID为" + importContentId +"跳过导入资源，但需要检查频道发布！";
                        logger.info(msg);
*/
                            //String msg = "媒体《" + mediaName +"》重复导入，导入的ID为" + importContentId +"，先从本平台加载影片数据，用来进行修改和替换！";
                            hasOldData = true;
                            content.setActors(actors);
                            content.setDirectors(directors);
                            content.setIntro(intro);
                            content.setDeviceId(deviceId);
                            content.setModuleId(moduleId);
                            oldCps = contentPropertyLogicInterface.getContentProperties(content.getId(), -1);
                            //os.write(msg+"\r\n");
                            //result.add(msg);
                        } else {
                            //result.add("导入xml数据开始。影片为："+mediaName+"");
                            content = newContent(adminId, importContentId, mediaName, cpCode,
                                    directors,
                                    actors,
                                    deviceId, moduleId,
                                    intro);
                        }
                        if (importFromXml) {
                            String contentName = content.getName();
                            List<ContentProperty> cps = getContentPropertiesFromXmlNode(content, contentNode, elementIdMaps, 0L, result, null, os,propertyMap);
                            if (moduleId == 5000) {
                                for (Object subN : contentNode.selectNodes("subcontents/subcontent")) {
                                    Node subContentNode = (Node) subN;
                                    int episodes = XmlUtils.getIntValue(subContentNode, "@Episodes", 1);
                                    ContentProperty clip = new ContentProperty();
                                    clip.setIntValue((long) episodes);
                                    clip.setName(XmlUtils.getValue(subContentNode, "@SubContentName", contentName + "第" + episodes + "集"));
                                    clip.setSubContentId(XmlUtils.getValue(subContentNode, "@SubContentID", ""));
                                    List<ContentProperty> clips = getContentPropertiesFromXmlNode(content, subContentNode, elementIdMaps, episodes, result, clip, os,propertyMap);
                                    cps.addAll(clips);
                                    //cps.addAll(getContentPropertiesFromXmlNode(subContentNode, elementIdMaps, episodes,result));
                                }
                            } else if (moduleId == 1) {
                                List<ContentProperty> clips = new ArrayList<ContentProperty>();
                                for (Object subN : contentNode.selectNodes("MediaClips/MediaClip")) {
                                    Property property = getPropertyByCode("MEDIA_CLIP");
                                    if (property != null) {
                                        Node subContentNode = (Node) subN;

                                        int episodes = XmlUtils.getIntValue(subContentNode, "@MediaClipEpisodes", 1);
                                        int MediaClipLength = XmlUtils.getIntValue(subContentNode, "@MediaClipLength", 1);
                                        int MediaClipWidth = XmlUtils.getIntValue(subContentNode, "@MediaClipWidth", 1);
                                        String MediaClipSource = XmlUtils.getValue(subContentNode, "@MediaClipSource", "");

                                        ContentProperty clip = new ContentProperty();
                                        clip.setIntValue((long) episodes);
                                        clip.setPropertyId(property.getId());
                                        clip.setStringValue(MediaClipSource);
                                        clip.setExtraData(episodes + "###" + MediaClipWidth + "###" + MediaClipLength);
                                        clip.setContentId(null);

                                        clips.add(clip);

                                    }
                                }

                                if (clips != null && clips.size() > 0) {
                                    cps.addAll(clips);
                                }
                            }

                            Node copyrightNode = contentNode.selectSingleNode("copyrights/copyright");
                            String validStartTimeStr;
                            String validEndTimeStr;
                            if(copyrightNode!=null){
                                validStartTimeStr = XmlUtils.getValue(copyrightNode, "@EffectiveTime", "20130101").trim();
                                validEndTimeStr = XmlUtils.getValue(copyrightNode, "@ExpireTime", "20230101").trim();
                            }else{
                                validEndTimeStr = "20230101";
                                validStartTimeStr = "20130101";
                            }
                            String formatStr = "yyyyMMdd";
                            if (ignoreCopyRight||validEndTimeStr==null||"".equals(validEndTimeStr.trim())) {
                                validStartTimeStr = StringUtils.date2string(new Date(), "yyyyMMdd");//设置成现在
                                validEndTimeStr = StringUtils.date2string(new Date(System.currentTimeMillis() + 10 * 365 * 24 * 3600 * 1000L), "yyyyMMdd");//设置十年后
                            }
                            int p = validEndTimeStr.indexOf(" ");
                            if (p > 0) {
                                validEndTimeStr = validEndTimeStr.substring(0, p).trim();
                                validStartTimeStr = validStartTimeStr.substring(0, p).trim();
                                formatStr = "yyyy-MM-dd";
                                if (validEndTimeStr.contains("/")) {
                                    formatStr = "yyyy/MM/dd";
                                }
                            }
                            Date validStartTime = StringUtils.string2date(validStartTimeStr, formatStr);
                            content.setValidStartTime(validStartTime);
                            Date validEndTime = StringUtils.string2date(validEndTimeStr, formatStr);
                            content.setValidEndTime(validEndTime);
                            String picPath = xmlFile.getParentFile().getAbsolutePath();
                            content = saveContent(content, cps, oldCps, result, picPath, webAppPath, cspFilePath, file, os, checkOnly);
                            int posterCount = 0;
                            int posterCanUseCount = 0;
                            int clipCount = 0;
                            int clipCanUseCount = 0;
                            for (ContentProperty cp : cps) {
                                Property property = propertyLogic.getPropertyByCache(cp.getPropertyId());
                                if (property != null) {
                                    Byte dataType = property.getDataType();
                                    if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType)) {
                                        posterCount++;
                                        File picFile = new File(webAppPath + "/" + cp.getStringValue());
                                        if (picFile.exists() && (!picFile.isDirectory())) {
                                            posterCanUseCount++;
                                        } else {
                                            String info = "媒体《" + content.getName() +
                                                    "》的海报“" + cp.getName() + "," + cp.getIntValue() + ",xml中数据：" + cp.getStringValue() + ",文件位置：" + picFile.getAbsolutePath() +
                                                    "”没有找到！";
                                            os.write(info + "\r\n");
                                            result.add(info);
                                            importErrorCount++;
                                        }
                                    } else if (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) || PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType) ||
                                            PropertyLogicInterface.DATA_TYPE_WMV.equals(dataType)) {
                                        if (device != null) {
                                            clipCount++;
                                            File mediaFile = new File(device.getLocalPath() + "/" + cp.getStringValue());
                                            if (mediaFile.exists() && (!mediaFile.isDirectory())) {
                                                clipCanUseCount++;
                                            } else {
                                                String info = "媒体《" + content.getName() +
                                                        "》的片段“" + cp.getName() + "," + cp.getIntValue() + "," + mediaFile.getAbsolutePath() +
                                                        "”没有找到！";
                                                os.write(info + "\r\n");
                                                result.add(info);
                                                importErrorCount++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (posterCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》在XML数据中没有任何海报！");
                            } else if (posterCanUseCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》没有任何可用海报！");
                            }
                            if (clipCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》没有在XML中找到任何播放片段！");
                            } else if (clipCanUseCount == 0) {
                                result.add("媒体《" + content.getName() + "(id=" + content.getId() +
                                        ")》没有任何可用播放片段！");
                            }
                            Date now = new Date();
                            if (validEndTime.before(new Date(now.getTime() + 7 * 24 * 3600 * 1000L))) {
                                importErrorCount++;

                                String warnStr = "已经过期了" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                                if (validEndTime.after(now)) {
                                    warnStr = "再过" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                            "就会过期";
                                }
                                tempStr = "警告：导入媒体《" + content.getName() + "(id=" + content.getId() + ")" +
                                        "》的版权到期日期可能有问题，" + warnStr +
                                        "。如果已经过期，会被系统自动下线：起始日期" +
                                        StringUtils.date2string(validStartTime) + "->结束日期" +
                                        StringUtils.date2string(validEndTime) + "，当前日期：" + StringUtils.date2string(new Date()) +
                                        "(xml中数据：" + validStartTimeStr + "->" + validEndTimeStr + ")";
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                            }
                            if (content == null) {
                                importErrorCount++;
                                tempStr = "导入媒体是发生异常，导入的XML为：\n" + contentNode.asXML();
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                                importErrorCount++;
                            }
                        }
                        //如果不是老的数据，那就要新建对应的审计和csp表
                        if (!hasOldData) {
                            if (!checkOnly) {
                                ContentAudit contentAudit = saveContentAudit(content.getId());
                                saveContentCsp(content.getId(), cpCode, contentAudit.getId(), ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                            }
                        }
                        List channels = contentNode.selectNodes("channels/channel");
                        if (channels == null || channels.size() <= 0) {
                            tempStr = content.getName() + "媒体没有频道节点!";
                            result.add(tempStr);
                            os.write(tempStr + "\r\n");
                            importErrorCount++;
                            logger.error(tempStr);
                        } else {
                            for (Object node5 : channels) {
                                Node channelNode = (Node) node5;
                                String channelIdStr = XmlUtils.getValue(channelNode, "@ChannelId", null);
                                channelId = StringUtils.string2long(channelIdStr, -1);
                                if (channelId < 0) {
                                    tempStr = content.getName() + "频道ID没有正确输入：" + channelIdStr;
                                    result.add(tempStr);
                                    os.write(tempStr + "\r\n");
                                    logger.error(tempStr);
                                    continue;
                                }
                                String channelName = XmlUtils.getValue(channelNode, "@Name", "");
                                //查看当前用户是否有这个频道或者权限
                                Channel channel = (Channel) TreeUtils.getInstance().getObject(Channel.class, channelId);
                                if (channel == null) {
                                    //频道ID不对。尝试进行猜测，能否找到一个名字上只是多了括号的
                                    tempStr = content.getName() + ",无法通过ChannelID=" + channelId + "来查找频道，尝试通过频道名反查：" + channelName + "....";
                                    channel = (Channel) CacheUtils.get(channelName, "channelByNameCache", new com.fortune.util.DataInitWorker() {
                                        public Object init(Object key, String cacheName) {
                                            Channel bean = new Channel();
                                            String searchName = key.toString();
                                            bean.setName(searchName);

                                            List<Channel> channelList = channelLogic.search(bean);
                                            for (Channel ch : channelList) {
                                                String name = ch.getName();
                                                if (name.equals(searchName) || name.startsWith(searchName + "(") ||
                                                        name.startsWith(searchName + "（")) {
                                                    return ch;
                                                }
                                            }
                                            return null;
                                        }
                                    });
                                    if (channel == null) {
                                        importErrorCount++;
                                        tempStr += "《" + content.getName() +
                                                "》导入时，无法找到频道，无论是通过ID=" + channelId + ",还是通过Name=" + channelName;
                                        logger.error(tempStr);
                                        os.write(tempStr + "\r\n");
                                        importErrorCount++;
                                        result.add(tempStr);
                                        continue;
                                    } else {
                                        channelName = channel.getName();
                                        channelId = channel.getId();
                                    }
                                } else {
                                    channelName = channel.getName();
                                }

                                boolean hasPrivilegeToChannel = false;
                                if (moduleId == 5000) {
                                    hasPrivilegeToChannel = cspChannelLogicInterface.hasPrivilegeToChannel(cspId, channelId);
                                } else if (moduleId == 1) {
                                    hasPrivilegeToChannel = channelLogic.hasPrivilegeToChannelSX(cspId, channelId);
                                }
                                if (hasPrivilegeToChannel) {
                                    //判断是否为叶子节点。
//                                if (channelLogic.isLeafChannel(channelId)) {
                                    boolean allowPublishToUnLeafChannel = config.getBoolConfig("importXml.allowPublishToUnLeafChannel", true);  //
                                    boolean isLeafChannel = channelLogic.isLeafChannel(channelId);
                                    if (isLeafChannel || ((!isLeafChannel) && allowPublishToUnLeafChannel)) {
                                        //把频道和content绑定起来
                                        boolean shouldCreateContentChannel = true;
                                        //如果这个影片已经有了，那就要检查是否已经发布到这个频道。如果没有，就发布。如果已经发布，就放弃
                                        if (hasOldData && contentChannelLogic.isExists(channelId, content.getId())) {
                                            shouldCreateContentChannel = false;
                                            tempStr = "媒体《" + content.getName() + "》已经存在，已经设置过频道绑定，不再发布：" + channelName;
                                            //os.write(tempStr+"\r\n");
                                            result.add(tempStr);
                                            logger.debug(tempStr);
                                        }
                                        if (shouldCreateContentChannel) {
                                            if (hasOldData) {
                                                tempStr = "媒体《" + content.getName() + "》已经存在，重新发布到频道：" + channelName;
                                                logger.debug(tempStr);
                                                os.write(tempStr + "\r\n");
                                            }
                                            if (!checkOnly) {
                                                saveContentChannel(content.getId(), channelId);
                                            }
                                        }
                                    } else {
                                        importErrorCount++;
                                        tempStr = "频道“" + channelName + "”不是叶子节点，批量导入时会拒绝《" + content.getName() + "》发布媒体到这个频道！";
                                        os.write(tempStr + "\r\n");
                                        logger.error(tempStr);
                                    }
                                } else {
                                    tempStr = "《" + content.getName() + "》导入时发生错误：没有权限绑定到id为" + channelId + "名字为《" + channelName + "》这个频道，请创建或者管理员给频道赋权";
                                    result.add(tempStr);
                                    logger.error(tempStr);
                                    importErrorCount++;
                                    os.write(tempStr + "\r\n");
                                    os.flush();
                                }
                            }
                        }
                        int contentErrorCount = importErrorCount - startErrorCount;
                        if (contentErrorCount == 0) {
                            //没有错误，如果原媒体没有上线，则尝试将其上线发布
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                                content.setStatusTime(new Date());
                                content = save(content);
                                contentCspLogicInterface.setStatus(content.getId(), content.getCspId(), -1, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                result.add("媒体《" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")》没有错，尝试将其直接上线发布！");
                            }
                        } else {
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                result.add("媒体《" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")》信息中有" + contentErrorCount +
                                        "个错误，现在状态是" + getStatusString(content.getStatus()) +
                                        "，请您手工操作补充数据后上线！");
                            }
                        }

                        importContentCount++;
                    }
                } else {
                    tempStr = "当前用户不能操作这xml,xml的cpCode应改为" + cspId;
                    result.add(tempStr);
                    os.write(tempStr + "\r\n");
                    importErrorCount++;
                    logger.error("当前你没有这个权限！不能操作这xml");
                }
                String resultStr = "处理选中的xml文件完毕，累计";
                if (checkOnly) {
                    resultStr += "检查";
                } else {
                    resultStr += "添加";
                }
                resultStr += "媒体：" + importContentCount + "个！错误" + importErrorCount + "个！";
                os.write(resultStr + "\r\n");
                result.add(resultStr);
                logger.debug(resultStr);
                Date stopTime = new Date();
                os.write("-启动时间：" + StringUtils.date2string(startTime) + "\n");
                os.write("-完成时间：" + StringUtils.date2string(stopTime));
            } catch (IOException e) {
                logger.error("无法找到文件：" + importResultFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("发生异常，无法继续执行导入操作：" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("无法关闭文件：" + importResultFile.getAbsolutePath());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("保存影片信息是发生异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        return null;
    }

    //添加成功要加一条contentCsp绑定关系
    public void saveContentCsp(Long contentId, Long cspId, long auditId, long status) {
/*      以下代码由刘喜军在2013年6月13日修改，不能直接添加。因为添加操作已经在save方法中做过了。这里只能是修改状态
        ContentCsp cc = new ContentCsp();
        cc.setCspId(cspId);
        cc.setContentId(contentId);
        cc.setStatus(2l);
        cc.setStatusTime(new Date());
        cc.setContentAuditId(auditId);
        contentCspLogicInterface.save(cc);*/
        contentCspLogicInterface.setStatus(contentId, cspId, -1, status, auditId, false);
    }

    //在contentAudit加一条数据
    public ContentAudit saveContentAudit(Long contentId) {
        ContentAudit ct = new ContentAudit();
        ct.setContentId(contentId);
        ct.setStatus(4l);
        ct.setType(4l);
        ct.setResult(1l);
        ct.setApplyTime(new Date());
        ct.setAuditAdminId(1l);
        ct.setAuditTime(new Date());
        ct = contentAuditLogicInterface.save(ct);
        return ct;
    }

    //在contentChannel表中加一条数据
    public void saveContentChannel(long contentId, long channelId) {
        ContentChannel cc = new ContentChannel();
        cc.setChannelId(channelId);
        cc.setContentId(contentId);
        contentChannelLogic.save(cc);
    }

    public Content newContent(Integer adminId, String contentId, String name, Long cspId,
                              String mediaDirector, String mediaActors,
                              Long deviceId, Long moduleId, String mediaIntro) {
        Content content = new Content();
        content.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
        content.setCreatorAdminId(adminId.longValue());
        content.setCreateTime(new Date());
        content.setModuleId(moduleId);
        content.setDeviceId(deviceId);//设备的id目前固定是“三屏互动”
        content.setContentId(contentId);
        content.setActors(mediaActors);
        content.setDirectors(mediaDirector);
        content.setName(name);
        if (mediaIntro != null && mediaIntro.length() >= 2000) {
            logger.warn("超过2K的文字介绍，已经进行了截断！");
            mediaIntro = mediaIntro.substring(0, 1990) + "...";
        }
        content.setIntro(mediaIntro);
        content.setCspId(cspId);
//        content = this.save(content);
        return content;
    }

    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId, final Long contentCspStatus, final Long channelId, final String searchValue, final PageBean pageBean) {
        String key = cspId + "_ch" + channelId + "_blurSearch_" + searchValue;
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, searchValue, pageBean);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }

    /**
     * added by mlwang， 增加频道过滤和用户类型
     * @param cspId
     * @param contentCspStatus
     * @param channelId
     * @param searchValue
     * @param pageBean
     * @param channelIdList
     * @param user
     * @return
     */
    public List<Content> list(final Long cspId,final Long contentCspStatus,final Long channelId,final String searchValue,final PageBean pageBean, final List<Long> channelIdList, final FrontUser user){
        String key = cspId + "_ch" + channelId + "_blurSearch_" + searchValue;
        if( user != null ){
            key += "_org" + user.getOrganizationId() + "_userType" + user.getTypeId();
        }

        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, searchValue, pageBean);
                long userType = (user == null)? -1: user.getTypeId();
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, searchValue, pageBean, channelIdList, userType);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }


    @SuppressWarnings("unchecked")
    public List<Content> list(final Long contentCspStatus, final String searchValue, final PageBean pageBean) {
        // 调用新增的方法
        return list(contentCspStatus, searchValue, pageBean, null, null);
    }

    /**
     * 内容列表
     * @param contentCspStatus  状态
     * @param searchValue       查询条件
     * @param pageBean          分页信息
     * @param channelIdList     频道列表
     * @param user              用户
     * @return                  内容列表
     */
    public List<Content> list(final Long contentCspStatus,final String searchValue,final PageBean pageBean, final List<Long> channelIdList, final FrontUser user){
        String key = "blurSearch_" + searchValue;
        if( user != null ){
            key += "_org" + user.getOrganizationId() + "_userType" + user.getTypeId();
        }
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //List<Content> tempResult = contentDaoInterface.list(contentCspStatus, searchValue, pageBean);
                long userType = (user == null) ? -1 : user.getTypeId();
                List<Content> tempResult = contentDaoInterface.list(contentCspStatus, searchValue, pageBean, channelIdList, userType);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listContentCount");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }

    /**
     * added by mlwang 只是增加了两个过滤条件，如果出错了，找xjliu
     * @param cspId
     * @param contentCspStatus
     * @param channelIds
     * @param contentName
     * @param directors
     * @param actors
     * @param searchValues
     * @param pageBean
     * @param channelIdList
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId,final Long contentCspStatus,final String channelIds, final String contentName, final String directors,
                              final String actors,
                              final List<ContentProperty> searchValues, final PageBean pageBean, final List<Long> channelIdList, final FrontUser user){
        String key = cspId + "_ch" + channelIds + "_name" + contentName + "_director" + directors + "_actor" + actors;
        if( user != null ){
            key += "_org" + user.getOrganizationId() + "_userType" + user.getTypeId();
        }
        if (searchValues != null) {
            for (ContentProperty cp : searchValues) {
                key += "_prop" + cp.getPropertyId() + "_v" + cp.getStringValue();
            }
        }
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        SearchResult<Content> result = (SearchResult<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //对searchValues做一个遍历，重新整理一下数据，如果是整合在主表里，要对主表进行搜索
                if (searchValues != null) {
                    for (ContentProperty cp : searchValues) {
                        Property property = propertyLogic.get(cp.getPropertyId());
                        Byte isMain = property.getIsMain();
                        if (isMain != null && 1 == isMain) {
                            cp.setExtraInt(1L);
                            cp.setExtraData(property.getColumnName());
                        } else {
                            cp.setExtraInt(0L);
                            cp.setExtraData(null);
                        }
//                        cp.setExtraData("");
                    }
                }
                String systemChannelId = "" + channelLogic.getSystemChannelId();
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelIds, contentName, directors, actors, searchValues, pageBean, systemChannelId);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
/*
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
*/
                SearchResult<Content> searchResult = new SearchResult<Content>();
                searchResult.setRowCount(pageBean.getRowCount());
                searchResult.setRows(result);
                return searchResult;
            }
        });
        Integer allCount = result.getRowCount();
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result.getRows();
    }

    /**
     * added by mlwang ，增加了频道过滤和用户类型过滤
     * @param cspId
     * @param contentCspStatus
     * @param channelId
     * @param contentName
     * @param directors
     * @param actors
     * @param searchValues
     * @param pageBean
     * @param channelIdList
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId,final Long contentCspStatus,final Long channelId,final String contentName,final String directors,
                              final String actors,
                              final List<ContentProperty> searchValues, final PageBean pageBean, final List<Long> channelIdList, final FrontUser user){
/*
        //得到栏目推荐的数量看有没有
        List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
        if (pageBean.getStartRow() == 0) {
            //说明有推荐(见识查询的条数)
            pageBean.setPageSize(pageBean.getPageSize() - recommendResult.size());
        } else {
            //如果不是第一页减去推荐的数目
            pageBean.setStartRow(pageBean.getStartRow() - recommendResult.size());
        }
*/
        String key = cspId + "_ch" + channelId + "_name" + contentName + "_director" + directors + "_actor" + actors;
        if( user != null ){
            key += "_org" + user.getOrganizationId() + "_userType" + user.getTypeId();
        }
        if (searchValues != null) {
            for (ContentProperty cp : searchValues) {
                key += "_prop" + cp.getPropertyId() + "_v" + cp.getStringValue();
            }
        }
        final String countKey = key;
        key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        SearchResult<Content> result = (SearchResult<Content>) CacheUtils.get(key, "listContent", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                //对searchValues做一个遍历，重新整理一下数据，如果是整合在主表里，要对主表进行搜索
                if (searchValues != null) {
                    for (ContentProperty cp : searchValues) {
                        Property property = propertyLogic.get(cp.getPropertyId());
                        Byte isMain = property.getIsMain();
                        if (isMain != null && 1 == isMain) {
                            cp.setExtraInt(1L);
                            cp.setExtraData(property.getColumnName());
                        } else {
                            cp.setExtraInt(0L);
                            cp.setExtraData(null);
                        }
//                        cp.setExtraData("");
                    }
                }
                String recommendContentIds = "";
                List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
                /**
                 * 数据获取的方法要仔细斟酌。我们的目的，是先把推荐的显示好，然后再显示频道的。
                 * 1、先获取推荐的数据，然后对分页数据进行调整。
                 *  1.1检查推荐数量，如果推荐数量在获取数据起始记录以后，就从起始记录开始，读到推荐最后一个。
                 *  1.1.2剩余的从普通数据中获取。
                 *
                 * 2、获取频道的列表数据
                 *  2.1.起始记录数要减去推荐记录数，每页记录数要看读取推荐后还剩余多少条。
                 *  2.2 所有记录条数加上推荐记录条数
                 *
                 */
                List<Content> result = new ArrayList<Content>();
                int start = pageBean.getStartRow();
                int limit =pageBean.getPageSize();
                for(Content c:recommendResult){
                    if(!"".equals(recommendContentIds)){
                        recommendContentIds+=",";
                    }
                    recommendContentIds+=c.getId();
                }
                while(start<recommendResult.size()&&limit>0){
                    result.add(recommendResult.get(start));
                    start++;
                    limit--;
                }

                //List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues, pageBean);
                if(limit>0){
                    PageBean tempPageBean = new PageBean();
                    start = pageBean.getStartRow()-recommendResult.size();
                    if(start<0){
                        start = 0;
                    }
                    tempPageBean.setPageSize(limit);
                    tempPageBean.setStartRow(start);
                    tempPageBean.setEndRow(start+limit);
                    tempPageBean.setOrderBy(pageBean.getOrderBy());
                    tempPageBean.setOrderDir(pageBean.getOrderDir());
                    //todo 目前还不支持将推荐的媒体从列表中剔除。剔除的话，sql脚本并不复杂，但性能会比较低
                    //
                    List<Content> tempResult = contentDaoInterface.list(
                            cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues,
                            tempPageBean, channelIdList, (user==null)?-1:user.getTypeId(),recommendContentIds
                    );
                    pageBean.setRowCount(recommendResult.size()+tempPageBean.getRowCount());
                    result.addAll(tempResult);
                }else{
                    //只是为了获取记录条数
                    contentDaoInterface.list(cspId, contentCspStatus, channelId, contentName, directors, actors,
                            searchValues, pageBean, channelIdList, (user==null)?-1:user.getTypeId(),recommendContentIds);
                    pageBean.setRowCount(recommendResult.size()+pageBean.getRowCount());
                }
                List<Content> initedContents = new ArrayList<Content>();
                for (Content c : result) {
                    initedContents.add(getCachedContent(c));
                }
                putToCache(countKey, "listContentCount", pageBean.getRowCount());
                SearchResult<Content> searchResult = new SearchResult<Content>();
                searchResult.setRowCount(pageBean.getRowCount());
                searchResult.setRows(initedContents);
                return searchResult;
            }
        });
        Integer allCount = result.getRowCount();
        pageBean.setRowCount(allCount);
        return result.getRows();
    }

    /**
     * 个人专属top10
     * @param channelIdList
     * @param user
     * @return
     */
    public List<Content> top10( List<Long> channelIdList, FrontUser user){
        return contentDaoInterface.top10(channelIdList, user == null? -1L: user.getTypeId());
    }


    public static final String channelCacheName = "channelCache";
    public static final String contentChannelCacheName = "contentChannelCache";
    public Cache getCache(String cacheName) {
        long timeToLive = AppConfigurator.getInstance().getIntConfig("cache.timeToLive", 600);//默认保存10分钟
        long timeToIdle = AppConfigurator.getInstance().getIntConfig("cache.timeToIdle", 600);
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cache = new Cache(cacheName, 1024, false, false, timeToLive, timeToIdle);
            cacheManager.addCache(cache);
        }
        return cache;
    }

    public void putToCache(Object id, String cacheName, Object result) {
        Cache cache = getCache(cacheName);
        Element ele = cache.get(id);
        if (ele != null) {
            cache.remove(id);
        }
        ele = new Element(id, result);
        cache.put(ele);
    }

    @SuppressWarnings("unchecked")
    public Object getFromCache(Object id, String cacheName) {
        Element ele = getCache(cacheName).get(id);
        if (ele != null) {
            return ele.getValue();
        }
        return null;
    }

    public Object getObjFromCache(Object objKey, String cacheName, DataInitWorker worker) {
        Object result = null;
        if (objKey != null) {
            Cache cache = getCache(cacheName);
            Element ele = cache.get(objKey);
            if (ele != null) {
                return ele.getValue();
            }
            ThreadUtils tu = ThreadUtils.getInstance();
            try {
                tu.acquire(cacheName + "_" + objKey);
                ele = cache.get(objKey);
                if (ele != null) {
                    return ele.getValue();
                }
                result = worker.init(objKey, cacheName);
                if (result != null) {
                    ele = new Element(objKey, result);
                    cache.put(ele);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tu.release(cacheName + "_" + objKey);
            }

        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Channel getContentBindChannel(Long channelId, Long contentId) {
        Channel channel = null;
        if (channelId > 0) {
            channel = (Channel) CacheUtils.get(channelId, channelCacheName, new DataInitWorker() {
                public Object init(Object objKey, String cacheName) {
                    Long channelId = com.fortune.util.StringUtils.string2long(objKey.toString(), -1);
                    if (channelId > 0) {
                        try {
                            return channelLogic.get(channelId);
                        } catch (Exception e) {
                            logger.error("无法获取频道：" + e.getMessage());
                        }
                    }
                    return null;
                }
            });
        }
        if (channel == null) {
            channel = (Channel) CacheUtils.get(contentId, contentChannelCacheName, new DataInitWorker() {
                public Object init(Object objKey, String cacheName) {
                    Long contentId = com.fortune.util.StringUtils.string2long(objKey.toString(), -1);
                    if (contentId > 0) {
                        ContentChannel cc = new ContentChannel();
                        cc.setContentId(contentId);
                        List<ContentChannel> contentChannels = contentChannelLogic.search(cc);
                        //遍历一下该结果，如果存在父子结构，则取子节点
                        List<Long> channelIds = new ArrayList<Long>();
                        for (ContentChannel cc1 : contentChannels) {
                            channelIds.add(cc1.getChannelId());
                        }
                        for (ContentChannel cc1 : contentChannels) {
                            Long channelId = cc1.getChannelId();
                            List<Channel> parents = TreeUtils.getInstance().getParents(Channel.class, channelId);
                            if (parents != null) {
                                for (int l = channelIds.size() - 1; l >= 0; l--) {
                                    long checkChannelId = channelIds.get(l);
                                    for (int index = 0; index < parents.size(); index++) {
                                        Channel parent = parents.get(index);
                                        if (parent.getId() == checkChannelId) {
                                            logger.debug("频道" + index + parent.getName() + "(" + parent.getId() + ")是否是频道“id=" +
                                                    checkChannelId + "”的祖先频道，所以移除这个频道");
                                            channelIds.remove(l);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (channelIds.size() > 0) {
                            int index = (int) Math.round(Math.random() * channelIds.size());
                            if (index < 0) index = 0;
                            if (index >= channelIds.size()) {
                                index = channelIds.size() - 1;
                            }
                            return channelLogic.get(channelIds.get(index));
                        } else {
                            logger.warn("没有找到相关的频道：" + contentId);
                        }
                    }
                    return null;
                }
            });
        }
        return channel;
    }

    public String getPropertySelectValue(final Long propertyId, final String value) {
        if (value == null) {
            return null;
        }
        String result = (String) CacheUtils.get(propertyId + "_" + value, "propertySelectCache", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                return propertyLogic.getSelectId(propertyId, value);
            }
        });
        if (result == null) {
            return value;
        }
        return result;
    }


    public Property getPropertyByCode(final String code) {
        Property property = null;
        if (code != null) {
            property = (Property) CacheUtils.get(code, "PropertyCacheName", new DataInitWorker() {
                public Object init(Object objKey, String cacheName) {
                    if (objKey != null) {
                        return propertyLogic.getByCode(objKey.toString());
                    }
                    return null;
                }
            });
        }
        return property;
    }

    @SuppressWarnings("unused")
    public void clearContentCache() {
        CacheUtils.clear(contentCacheName);
    }

    public Session getSession() {
        return this.contentDaoInterface.getHibernateSession();
    }

    public String getContentChannel(Long contentId) {
        return this.contentDaoInterface.getContentChannel(contentId);
    }

    public String getContentRecommend(Long contentId) {
        return this.contentDaoInterface.getContentRecommend(contentId);
    }

    public String getContentServiceProduct(Long contentId) {
        return this.contentDaoInterface.getContentServiceProduct(contentId);
    }

    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean) {
        return this.contentDaoInterface.list(cspId, contentCspStatus, channelId, pageBean);
    }

    public List<Object[]> list2(Long cspId, Long contentCspStatus, Long channelId, PageBean pageBean) {
        return this.contentDaoInterface.list2(cspId, contentCspStatus, channelId, pageBean);
    }

    public boolean existContentId(String contentId) {
        return this.contentDaoInterface.existContentId(contentId, null);
    }

    public List<Content> getContent(String name, String contentId, Long cspId) {
        return this.contentDaoInterface.getContent(name, contentId, cspId);
    }

    public boolean cpOfflineContent(Content content) {
        contentDaoInterface.setContentStatus(content.getId(), STATUS_CP_OFFLINE);
        contentCspLogicInterface.setStatus(content.getId(), -1, -1, STATUS_CP_OFFLINE);
        return true;
    }

    public void updateContentByContentId(Content content) {
        this.contentDaoInterface.updateContentByContentId(content);
    }

    public void removeContentByContentId(Content content) {
        this.contentDaoInterface.removeContentByContentId(content);
    }

    public Content saveContent(Content content) {
        Content content1 = this.save(content);
        if (content1 != null && content1.getId() > 0) {
            ContentCsp cc = new ContentCsp();
            cc.setContentId(content.getId());
            cc.setCspId(content.getCspId());
            cc.setStatus(0l);
            cc.setStatusTime(new Date());
            cc = contentCspLogicInterface.save(cc);
            logger.debug("保存后的值为：" + cc);
        }
        return content1;
    }

    public List<Content> getContentsOfVipChannels(long channelId) {
        List<Content> contents = new ArrayList<Content>();
        List list = contentDaoInterface.getContentsOfVipChannels(channelId);
        if (list != null && list.size() > 0) {
            for (Object aList : list) {
                Content c = new Content();
                Object[] o = (Object[]) aList;
                c.setId(Long.valueOf(o[0].toString()));

                contents.add(c);
            }
        }

        return contents;
    }

    public List<Content> getContentsOfExpired(Long cspId) {
        return contentDaoInterface.getContentsOfExpired(cspId);
    }

    public List<Content> getContentsByCspId(Long cspId) {
        return contentDaoInterface.getContentsByCspId(cspId);
    }

    public List<Content> getContentsByCspId(Long cspId,PageBean pageBean) {
        return contentDaoInterface.getContentsByCspId(cspId,pageBean);
    }

    public long getCountByCspId(Long cspId) {
        return contentDaoInterface.getCountByCspId(cspId);
    }

    public List<Content> getContentsBySpecial(PageBean pageBean) {
        return contentDaoInterface.getContentsBySpecial(pageBean);
    }

    public List<Content> getContentsOfChannelAndCp(long channelId, Long cspId, String name, String actors,
                                                   String directors, Long status, int publishStatus,PageBean pageBean) {
        List<Content> result= contentDaoInterface.getContentsOfChannelAndCp(channelId,cspId,name,actors,directors,status,publishStatus,pageBean);
        for(Content content:result){
            Long property1 = content.getProperty1();
            //在紧凑模式下，property1是频道ID，如果这个字段没有设置值，就要从ContentChannel中复制过来
            if(property1==null||property1<=0){
                ContentChannel cc = new ContentChannel();
                cc.setContentId(content.getId());
                List<ContentChannel> data = contentChannelLogic.search(cc);
                if(data!=null&&data.size()>0){
                    property1 = data.get(0).getChannelId();
                }
                content.setProperty1(property1);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args){
        try {
            ContentLogicInterface contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface");
            //List<String> response =new ArrayList<String>();
            String xmlFile = "E:/logs/190/exportSD/meta.xml";
            Long cspId = 1L;
            Long moduleId=10000L;
            String cspFilePath="";
            String file="";
            contentLogicInterface.importXmlV2(xmlFile,cspId,1,2L,moduleId,"gb2312","E:/temp/web/",cspFilePath,file,false);
            ContentChannelLogicInterface contentChannelLogicInterface =(ContentChannelLogicInterface) SpringUtils.getBean("contentChannelLogicInterface");
            ContentRecommendLogicInterface contentRecommendLogicInterface = (ContentRecommendLogicInterface)
                    SpringUtils.getBean("contentRecommendLogicInterface");
            List<ContentChannel> all = contentChannelLogicInterface.getAll();
            while(all!=null&&all.size()>0){
                List<Content> contents = new ArrayList<Content>();
                Long channelId = null;
                for(int l=all.size()-1;l>=0;l--){
                    ContentChannel cc=all.get(l);
                    if(channelId==null){
                        channelId = cc.getChannelId();
                        contents.add(new Content(cc.getContentId()));
                        all.remove(cc);
                    }else{
                        Long contentChannelId = cc.getChannelId();
                        if(channelId.equals(contentChannelId)){
                            contents.add(new Content(cc.getContentId()));
                            all.remove(cc);
                        }
                    }
                }
                if(channelId!=null&&contents.size()>0){
                    contentRecommendLogicInterface.saveContentRecommendOfChannel(contents,channelId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId, final Long contentCspStatus, final Long channelId, final String searchValue, final PageBean pageBean,final Long type) {
        String key = cspId + "_ch" + channelId + "_blurSearch_" + searchValue;
        final String countKey = key;
        if (pageBean != null) {
            key += "_start" + pageBean.getStartRow() + "_limit" + pageBean.getPageSize() + "_order" + pageBean.getOrderBy() + "_dir" + pageBean.getOrderDir();
        }
        List<Content> result = (List<Content>) CacheUtils.get(key, "listLiveContent_pl", new DataInitWorker() {
            public Object init(Object objKey, String cacheName) {
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, searchValue, pageBean,type);
                List<Content> result = new ArrayList<Content>();
                for (Content c : tempResult) {
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listLiveContent_pl", pageBean.getRowCount());
                }
                return result;
            }
        });
        Integer allCount = (Integer) getFromCache(countKey, "listLiveContent_pl");
        if (allCount != null && pageBean != null) {
            pageBean.setRowCount(allCount);
        }

        return result;
    }
*/

    @SuppressWarnings("unchecked")
    public List<Content> list(Long cspId, Long contentCspStatus, Long channelId, String searchValue, PageBean pageBean, Long type) {
        return contentDaoInterface.list(cspId, contentCspStatus, channelId, searchValue, pageBean,type);
    }



    public List<Content>  getContentByName(String homeClubName,String guestClubName,long channelId,PageBean pageBean,long cspId) {
        return contentDaoInterface.getContentByName(homeClubName,guestClubName,channelId,pageBean,cspId);
    }
    /**
     * 根据content.status状态设置在ContentCsp和ContentChannel中的状态。在微缩版中直接发布的通道
     * @param content 媒体
     * @param channelIds 频道id，可能是一个序列用逗号分号分割
     * @return 已发布的频道列表
     */
    public List<Channel> checkPublishChannels(Content content,String channelIds){
        return checkPublishChannels(content,channelIds,false);
    }

    public List<Channel> checkPublishChannels(Content content,String channelIds,boolean willCheckAuditFlag) {
        List<Channel> result=new ArrayList<Channel>();
        List<ContentChannel> contentChannels;
        if(content.getId()>0){
            contentChannels = contentChannelLogic.getContentPublishedChannels(content.getId());
        }else{
            contentChannels = new ArrayList<ContentChannel>(0);
        }
        boolean autoPublishContent=true;
        if(!"".equals(channelIds)){
            channelIds = channelIds.replaceAll(";",",");
            channelIds = channelIds.replaceAll(" ",",");
            channelIds = channelIds.replaceAll("-",",");
            for(String id:channelIds.split(",")){
                long channelId = StringUtils.string2long(id,-1);
                if(channelId>0){
                    boolean found =false;
                    for(ContentChannel cc:contentChannels){
                        if(cc.getChannelId().equals(channelId)){
                            found = true;
                            contentChannels.remove(cc);
                            break;
                        }
                    }
                    if(!found){
                        contentChannelLogic.save(new ContentChannel(-1,content.getId(),channelId));
                    }
                }
            }
        }
        //把多余的频道绑定数据删除
        if(contentChannels!=null&&contentChannels.size()>0){
            for(ContentChannel cc:contentChannels){
                contentChannelLogic.remove(cc);
            }
        }
        contentChannels = contentChannelLogic.getContentPublishedChannels(content.getId());
        for(ContentChannel cc:contentChannels){
            try {
                Channel channel = channelLogic.get(cc.getChannelId());
                //只要有一个不免审，就不自动发布，进入待审状态
                if( ! ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(channel.getAuditFlag())){
                    autoPublishContent = false;
                }
                result.add(channel);
            } catch (Exception e) {
                logger.error("无法初始化频道："+e.getMessage());
                e.printStackTrace();
            }
        }
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            //如果是紧凑模式，就要看媒体状态来设置是否上下线
            Long wantToStatus=null;
            //如果需要检查是否频道免审，就看是否自动发布内容。
            //                              如果自动发布，就尝试设置上线。
            //                              如果不是自动发布，就进入待审
            //如果不需要检查是否频道免审，就走原来的流程
            //
            if(willCheckAuditFlag){
                if(autoPublishContent){
                    wantToStatus = STATUS_WAIT_TO_ONLINE;
                }else{
                    wantToStatus = STATUS_WAITING_FOR_AUDIT;
                }
                logger.debug("将按照频道是否免审来决定是否将媒体发布上线："+getStatusString(wantToStatus));
                setContentStatus(content,wantToStatus);
            }else{
                Long status = content.getStatus();
                if(ContentLogicInterface.STATUS_CP_OFFLINE.equals(content.getStatus())){
                    logger.debug("媒体设定为下线状态，所以从ContentCsp中将其下线！");
                    contentCspLogicInterface.unPublishContent(content.getId(),content.getCspId(),-1L);
                }else if(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE.equals(status)||
                        ContentLogicInterface.STATUS_ENCODING.equals(status)){
                    logger.debug("媒体状态被设定为：" +getStatusString(status)+
                            "，所以从ContentCsp中将其状态设置为新资源，转码结束后就上线！");
                    contentCspLogicInterface.setStatus(content.getId(),content.getCspId(),-1L,ContentCspLogicInterface.STATUS_NEW);
                }else if(ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())){
                    logger.debug("媒体状态被设定为上线"+
                            "，所以从ContentCsp中将其状态设置为上线！");
                }else{
                    logger.debug("媒体状态被设定为：" +getStatusString(status)+
                            "，按照默认的约定，简版中，不认识的状态，就将ContentCsp中状态设置为上线！");
                    contentCspLogicInterface.publishContent(content.getId(),content.getCspId(),-1L);
                }
            }
        }else{
            Long status = content.getStatus();
            Long wantToStatus=null;
            if(STATUS_CP_ONLINE.equals(status)){
                wantToStatus = STATUS_WAIT_TO_ONLINE;
            }else if(STATUS_CP_OFFLINE.equals(status)){
                wantToStatus = STATUS_WAIT_TO_OFFLINE;
            }else if(STATUS_WAITING_FOR_ENCODE.equals(status)){
                wantToStatus = status;
            }else{
                logger.warn("状态未知，不设定媒体状态："+getStatusString(status));
            }
            if(wantToStatus !=null){
                logger.debug("将设置媒体状态："+getStatusString(wantToStatus));
                setContentStatus(content,wantToStatus);
            }
        }
        return result;

    }
    /**
     * 根据content.status状态设置在ContentCsp和ContentChannel中的状态。在微缩版中直接发布的通道
     * @param content 媒体
     * @param contentProperties 媒体属性
     * @return 已发布的频道列表
     */
    public List<Channel> checkPublishChannels(Content content, List<ContentProperty> contentProperties) {
        //long channelId = -1;
        String channelIds="";
        Property property = propertyLogic.getByCode("CHANNEL_ID");
        if(property!=null){
            for(ContentProperty contentProperty:contentProperties){
                if(property.getId()==contentProperty.getPropertyId()){
                    if(!"".equals(channelIds)){
                        channelIds+=",";
                    }
                    channelIds += contentProperty.getStringValue();
                }
            }
        }
        return checkPublishChannels(content,channelIds);
    }

    public List<Content> getContents(Long cspId,Long contentCspStatus,Long channelId,String searchValue,PageBean pageBean,String code,String code_1) {
        return contentDaoInterface.getContents(cspId,contentCspStatus,channelId,searchValue,pageBean,code,code_1);
    }

    public void onEncodeTaskFinished(Content content, int allTaskCount, int unFinishedCount) {
        if(unFinishedCount==0){
            logger.debug("媒体《" +content.getName()+
                    "》转码任务都已经完成，任务执行了"+allTaskCount+"个");
/*
            if(config.getBoolConfig("encoder.whenFinished.autoPublish",true)){
            }
*/
            //setContentStatus(content,STATUS_WAIT_TO_ONLINE);
            // mod by mlwang, @2014-10-23，根据所属频道设置，是自动上线还是待审
            // 有一个栏目不是免审，就要审核，逻辑有待完善

            boolean needAudit = false;
            String logs = "转码完成后自动对媒体状态的调整：";
            try {
                List<Channel> channelList = contentChannelLogic.getChannelsByContentId(content.getId());
                if(channelList != null){
                    for(Channel ch: channelList){
                        if(ch!=null){
                            Integer auditFlag = ch.getAuditFlag();
                            if(null != auditFlag){
                                needAudit = !ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(auditFlag);
                            }
                        }
                        if( needAudit ){
                            logs+="频道“" +ch.getName()+"”下媒体需要审核，不能免审，所以这个媒体将不得不进行审核才能上线："+content.getName();
                            logger.debug(logs);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logs+="检查媒体信息发布状态时发生异常错误："+e.getMessage();
                logger.error(logs);
                e.printStackTrace();
            }
            long wantToStatus =  needAudit? STATUS_WAITING_FOR_AUDIT : STATUS_WAIT_TO_ONLINE;
            try {
                logs+= "，"+setContentStatus(content,wantToStatus);
                logger.debug(logs);
                systemLogLogicInterface.saveMachineLog(logs);
            } catch (Exception e) {
                logger.error("尝试设置媒体状态到（" +getStatusString(wantToStatus)+"）时发生异常："+e.getMessage());
                e.printStackTrace();
            }

            // end of mod by mlwang
        }else if(unFinishedCount>0){
            logger.debug("媒体《" +content.getName()+
                    "》转码任务还有："+unFinishedCount+"个，共"+allTaskCount+"个");
        }else{
            logger.error("查询媒体《" +content.getName()+
                    "》转码任务未完成数量时发生错误！");
        }
    }

    public String setContentStatus(Content content,long status){
        String logInfo = "";
        if (status == ContentLogicInterface.STATUS_WAIT_TO_ONLINE || status == ContentLogicInterface.STATUS_WAIT_TO_OFFLINE) {
            Csp cp = null;
            try {
                cp = cspLogicInterface.get(content.getCspId());
            } catch (Exception e) {
                logger.error("无法获取CP信息：cspId="+content.getCspId());
                e.printStackTrace();
            }
            if(cp==null&&AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
                List<Csp> allCsp = cspLogicInterface.getAll();
                if(allCsp!=null&&allCsp.size()>0){
                    cp = allCsp.get(0);
                }
            }
            if(cp==null){
                return "无法获取CSP信息，不能自动设置Content状态：cspId="+content.getCspId();
            }
            if (status == ContentLogicInterface.STATUS_WAIT_TO_ONLINE ) {
                if (cp.getIsCpOnlineAudit() != null && cp.getIsCpOnlineAudit() == 1) {
                    ContentAudit ca = new ContentAudit();
                    ca.setContentId(content.getId());
                    ca.setType(ContentAuditLogicInterface.AUDIT_TYPE_CP_ONLINE);
                    ca.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                    ca.setApplyTime(new Date());
                    ca.setResult(ContentAuditLogicInterface.AUDIT_RESULT_WAITING);
                    ca = contentAuditLogicInterface.save(ca);
                    content.setContentAuditId(ca.getId());
                    logInfo+="进入上线待审状态！";
                } else {
                    logInfo+="免审CP，直接上线。";
                    content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);//免审直接上线
                    content.setStatusTime(new Date());
                    ContentCsp cc = new ContentCsp();
                    cc.setContentId(content.getId());
                    //cc.setStatus(ContentCspLogicInterface.STATUS_NEW);
                    List<ContentCsp> alreadyOnlineContents = contentCspLogicInterface.search(cc);
                    if(alreadyOnlineContents!=null&&alreadyOnlineContents.size()>0){
                        for(ContentCsp onlineContentCsp:alreadyOnlineContents){
                            Csp csp1 = cspLogicInterface.get(onlineContentCsp.getCspId());
                            if(ContentCspLogicInterface.STATUS_NEW.equals(onlineContentCsp.getStatus())||
                                    ContentCspLogicInterface.STATUS_ONLINE.equals(onlineContentCsp.getStatus())||
                                    AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
                                logInfo +="在SP“"+csp1.getName()+"”的上线状态已经恢复！";
                                List<Channel> channels = contentChannelLogic.getChannelsByContentId(content.getId());
                                if(channels!=null&&channels.size()>0){
                                    for(Channel channel:channels){
                                        logInfo+="已经发布到频道“"+channel.getName()+"”！";
                                    }
                                    onlineContentCsp.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                }else{
                                    logInfo +="没有发布到任何频道，所以设置为已审状态！";
                                    onlineContentCsp.setStatus(ContentCspLogicInterface.STATUS_ONLINE);
                                }
                                onlineContentCsp.setStatusTime(new Date());
                                onlineContentCsp.setContentAuditId(0L);
                                contentCspLogicInterface.save(onlineContentCsp);
                            }else{
                                logInfo +="在csp“"+csp1.getName()+"”的上线状态不是新资源状态，不进行任何处理：status="+contentCspLogicInterface.getStatusString(onlineContentCsp.getStatus());
                            }
                            //contentCspLogicInterface.setStatus(content.getId(),onlineContentCsp.getCspId(),-1,ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                        }
                    }else{
                        logInfo +="该资源没有发布到任何的CSP！";
                    }
                }
            }
            if (status==(ContentLogicInterface.STATUS_WAIT_TO_OFFLINE)) {
                if (cp.getIsCpOfflineAudit() != null && cp.getIsCpOfflineAudit() == 1) {
                    ContentAudit ca = new ContentAudit();
                    ca.setContentId(content.getId());
                    ca.setType(ContentAuditLogicInterface.AUDIT_TYPE_CP_OFFLINE);
                    ca.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);
                    ca.setApplyTime(new Date());
                    ca.setResult(ContentAuditLogicInterface.AUDIT_RESULT_WAITING);
                    ca = contentAuditLogicInterface.save(ca);
                    content.setContentAuditId(ca.getId());
                    logInfo+="进入下线待审状态";
                } else {
                    content.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);//免审直接下线
                    content.setStatusTime(new Date());
                    //cp下线, 修改sp的影片状态为0, 新片
                    logInfo+="CP免审，直接下线，所有SP上线资源已经自动下线，转为新资源状态！";
                    try {
                        HibernateUtils.executeUpdate(getSession(), "update ContentCsp cc set cc.status=" +
                                ContentCspLogicInterface.STATUS_NEW+
                                " where cc.contentId=" + content.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(status == ContentLogicInterface.STATUS_WAITING_FOR_ENCODE){
            //等待转码，此时如果媒体已经上线，则修改上线状态
            content.setStatusTime(new Date());
            //cp下线, 修改sp的影片状态为0, 新片
            logInfo+="CP内容进行转码，所有SP上线资源自动下线，转为新资源状态！";
            try {
                HibernateUtils.executeUpdate(getSession(), "update ContentCsp cc set cc.status=" +
                        ContentCspLogicInterface.STATUS_NEW+
                        " where cc.contentId=" + content.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logInfo+="将要操作的状态无法预知："+getStatusString(status);
            content.setStatus(status);
            content.setStatusTime(new Date());
        }
        save(content);
        String result = ("修改CP资源状态:《" + content.getName() + "》(" +content.getId()+
                "),将进行的操作：" + getStatusString(status)+",操作的结果是：" +
                getStatusString(content.getStatus())+
                "，附加信息："+logInfo);
        logger.debug(result);
        //清除所有缓存
        CacheUtils.clearAll();
        return result;
    }

    /**
     * 查询待审内容
     * @param channelId 频道Id
     * @param searchValue 查询条件
     * @param pageBean 分页信息
     * @return   dao返回的结果
     */
    @SuppressWarnings("unchecked")
    public List<RedexAuditContent> getUnAudtiContent(Long channelId, String searchValue, PageBean pageBean){
        List<Content> contentList = contentDaoInterface.getUnAuditContents(channelId, searchValue,pageBean);
        if(contentList == null || contentList.size() == 0) return null;

        //List<Channel> channelList = channelLogic.getAll();
        List<RedexAuditContent> unauditList = new ArrayList();

        for(Content c : contentList){
            RedexAuditContent unauditContent = new RedexAuditContent();
            unauditContent.setId(c.getId());
            unauditContent.setName(c.getName());
            unauditContent.setCreateTime(c.getCreateTime());
            unauditContent.setDeviceId(c.getDeviceId());
            unauditContent.setModuleId(c.getModuleId());
            unauditContent.setStatus(c.getStatus());
            String publisher = "未知";
            if(c.getCreatorAdminId() != null){                  
                try{
                    Admin a = adminLogic.get(c.getCreatorAdminId().intValue());
                    publisher = (a == null) ? "未知" : a.getRealname();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            unauditContent.setPublisher( publisher );

            // 根据申请上线和追集设置相应类型
            unauditContent.setAuditType(RedexAuditContent.AUDIT_TYPE_ONLINE);

            unauditContent.setChannelList(contentChannelLogic.getChannelsByContentId(c.getId()));
            unauditList.add(unauditContent);
        }

        return unauditList;
    }
    /**
     * 查询视频
     * @param channelId 所属频道
     * @param searchValue 查询条件
     * @param pageBean 分页信息
     * @return   content列表
     */
    public List<Content> getContentList(Long channelId, String searchValue, Date startTime, Date stopTime,PageBean pageBean){
        return contentDaoInterface.getContents(channelId, searchValue, startTime,stopTime,pageBean, -1);
    }

    /**
     * 查询视频内容，限定管理员所能管理的频道范围
     * @param channelId    频道Id
     * @param searchValue  查询内容
     * @param pageBean     分页信息
     * @param admin        管理员
     * @return 内容列表
     */
    public List<Content> getContentList(Long channelId, String searchValue,Date startTime,Date stopTime,PageBean pageBean, Admin admin){
        if( admin == null) return null;

        if( admin.getIsRoot() == 1) admin.setId(-1);
        return contentDaoInterface.getContents(channelId, searchValue,startTime,stopTime,pageBean,admin.getId());
    }


    public int incAllVisitCount(long contentId){
        return contentDaoInterface.incAllVisitCount(contentId);
    }

    /**
     * 获取管理员发布的异常任务
     * @param id   管理员Id
     * @return     异常任务列表
     */
    @SuppressWarnings("unchecked")
    public List<ContentAbnormal> getAbnormalContentList(Integer id){
        List abnormalList = new ArrayList();

        long taskId, clipId, contentId;
        int status;
        String title;
        // 转码失败的
        List<Object[]> transFailedList = contentDaoInterface.getTransAbnormalList(id);
        if(transFailedList != null){
            for(Object[] oa : transFailedList){
                // taskId
                taskId = StringUtils.string2long(oa[0].toString(), -1);
                // clipId
                clipId = StringUtils.string2long(oa[1].toString(), -1);
                // status
                status = StringUtils.string2int(oa[2].toString(), -1);
                // content id
                contentId = StringUtils.string2long(oa[3].toString(), -1);
                // title
                title = oa[4].toString();

                if( taskId > 0 && contentId > 0){
                    ContentAbnormal c = new ContentAbnormal();
                    c.setType(ContentAbnormal.ABNORMAL_CONTENT_TYPE_TRANS_FAILED);
                    c.setId(contentId);
                    c.setTaskId(taskId);
                    c.setTitle(title);
                    c.setMessage("未知错误");
                    switch(status){
                        case 4: case 804:
                            c.setMessage("执行错误");break;
                        case 805:
                            c.setMessage("文件不存在");break;
                        case 405:
                            c.setMessage("源文件太小");break;
                        case 500:
                            c.setMessage("格式不认识");break;
                        case 501:
                            c.setMessage("起始时间异常");break;
                        case 502:
                            c.setMessage("文件残缺");break;
                        case 504:
                            c.setMessage("输出文件错误");break;
                        case 505:
                            c.setMessage("未知错误");break;
                        case 506:
                            c.setMessage("命令行错误");break;
                        case 507:
                            c.setMessage("输出文件时间长度错误");break;
                        case 508:
                            c.setMessage("发生异常");break;
                        case 509:
                            c.setMessage("发生IO异常");break;
                        case 801:
                            c.setMessage("影片源丢失");break;
                        case 802:
                            c.setMessage("重复的任务");break;
                        case 806:
                            c.setMessage("被取消");break;
                    }

                    abnormalList.add(c);
                }
            }
        }

        // 获取审核失败任务
        List<Content> contentList = contentDaoInterface.getRejectContents(id);
        if( contentList != null ){
            for(Content c: contentList){
                ContentAbnormal a = new ContentAbnormal();
                a.setType(ContentAbnormal.ABNORMAL_CONTENT_TYPE_AUDIT_FAILED);
                a.setId(c.getId());
                a.setTaskId(-1);
                a.setTitle(c.getName());
                a.setMessage("审核未通过");
                abnormalList.add(a);
            }
        }

        return abnormalList;
    }

    /**
     * 获取最新内容列表
     * @param channelIdList 用户可以观看的栏目列表
     * @param userType      用户类型
     * @param count         获取的条数
     * @return ContentDTO对象列表
     */
    public List<ContentDTO> getNewest(List<Long> channelIdList, Long userType, int count){
        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }

        List<Content> contentList = contentDaoInterface.getRedexNewest(channelIdString, userType, count);
        List<ContentDTO> contentDTOList = new ArrayList<ContentDTO>();
        //objList中包括contentId和访问次数，根据id获取content
        for(Content content : contentList){
            contentDTOList.add(new ContentDTO(content));
        }

        return contentDTOList;
    }

    /**
     * 查询浏览频道内容
     * @param channelId     查询的频道Id，<0时忽略
     * @param searchWord    查询关键词
     * @param channelIdList 用户可以观看的栏目Id列表，列表为空或没有内容时忽略
     * @param userType       用户类型
     * @param pageBean       分页信息
     * @return  ContentDTO类型列表
     */
    public List<ContentTidyDTO> getRedexContents(Long channelId, String searchWord, List<Long> channelIdList, Long userType, PageBean pageBean){
        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }

        List<Object[]> contentList = contentDaoInterface.getRedexContents(channelId, searchWord, channelIdString, userType, pageBean);

        if(contentList == null) return null;

        List<ContentTidyDTO> tidyDTOList = new ArrayList<ContentTidyDTO>();
        for(Object[] objects : contentList){
            // 结构化成对象
            Integer id = ((Integer)objects[0]);
            if(id!=null){
                Content content = getCachedContent(id.longValue());
                tidyDTOList.add(new ContentTidyDTO(content));
            }
/*
            ContentTidyDTO dto = new ContentTidyDTO();
            dto.setId( StringUtils.string2long(objects[0].toString(), 0));  // id
            dto.setTitle( objects[1].toString() );  // 标题
            dto.setPoster( objects[2].toString() ); // 小海报
            dto.setActor( objects[3].toString() );  // 主创
            dto.setCreateTime( StringUtils.string2date(objects[4].toString())); // 创建时间
            dto.setRecommended( objects[5] != null );    // 是否被推荐

            tidyDTOList.add(dto);
*/
        }

        return tidyDTOList;
    }

    /**
     * 直播列表，逻辑和getRedexContents相同，只需要查询类型为直播的即可
     * @param channelId     查询的频道Id，<0时忽略
     * @param searchWord    查询关键词
     * @param channelIdList 用户可以观看的栏目Id列表，列表为空或没有内容时忽略
     * @param userType       用户类型
     * @param pageBean       分页信息
     * @return  ContentDTO类型列表
     */
    public List<ContentTidyDTO> getLivingList(Long channelId, String searchWord, List<Long> channelIdList, Long userType, PageBean pageBean){
        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }

        List<Object[]> contentList = contentDaoInterface.getRedexContents(channelId, searchWord, channelIdString, userType, pageBean, 1/*直播类型*/);

        if(contentList == null) return null;

        List<ContentTidyDTO> tidyDTOList = new ArrayList<ContentTidyDTO>();
        for(Object[] objects : contentList){
            // 结构化成对象
            ContentTidyDTO dto = new ContentTidyDTO();
            dto.setId( StringUtils.string2long(objects[0].toString(), 0));  // id
            dto.setTitle( objects[1].toString() );  // 标题
            dto.setPoster( objects[2].toString() ); // 小海报
            dto.setActor( objects[3].toString() );  // 主创
            dto.setCreateTime( StringUtils.string2date(objects[4].toString())); // 创建时间
            dto.setRecommended( objects[5] != null );    // 是否被推荐

            tidyDTOList.add(dto);
        }

        return tidyDTOList;
    }

    /**
     * 查询content对应的相关视频
     * @param content   content
     * @param priority  查询优先度，其实是唯一相关因素
     * @param channelIdList 搜索的频道范围
     * @param userType 用户类型
     * @return tidyDto列表
     */
    public List<ContentTidyDTO> getSimilarContents(Content content, int priority, List<Long> channelIdList, Long userType){
        if( content == null ) return null;

        Long channelId = -1L;
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(100);
        switch (priority){
            case ContentTidyDTO.SIMILARITY_PRIORITY_HOT:
                pageBean.setOrderBy("hot");
                break;
            case ContentTidyDTO.SIMILARITY_PRIORITY_NEW:
                pageBean.setOrderBy("time");
                break;
            case ContentTidyDTO.SIMILARITY_PRIORITY_SAME_CHANNEL:
                pageBean.setOrderBy("hot");
                List<Channel> channelList = contentChannelLogic.getChannelsByContentId(content.getId());
                if( channelList != null && channelList.size() > 0)
                    channelId = channelList.get(0).getId();
                break;
        }

        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }

        List<Object[]> contentList = contentDaoInterface.getRedexContents(channelId, null, channelIdString, userType, pageBean);
        // 转为ContentTidyDto对象，并计算相似度
        List<ContentTidyDTO> tidyDTOList = new ArrayList<ContentTidyDTO>();
        for(Object[] objects : contentList){
            // 结构化成对象
            long id = StringUtils.string2long(objects[0].toString(), 0);
            if( id == content.getId()) continue;
            Content c=getCachedContent(id);
            ContentTidyDTO dto = new ContentTidyDTO(c);
            dto.setRecommended( objects[5] != null );    // 是否被推荐

            // 设置相似度
            dto.setSimilarity(contentSimilarity(dto, content));

            tidyDTOList.add(dto);
        }

        // 安装相似度重新排序
        Collections.sort(tidyDTOList,new ComparatorContentTidyDTO());
        // 返回前14名
        if(tidyDTOList.size() > 21){
            tidyDTOList = tidyDTOList.subList(0, 21);
        }

        //
        // 把视频所属的栏目放进去
        for(ContentTidyDTO dto : tidyDTOList) {
            List<Channel> channelList = contentChannelLogic.getChannelsByContentId(dto.getId());
            if (channelList != null) {
                ArrayList<Long> chIdList = new ArrayList<Long>();
                for (Channel c : channelList) {
                    chIdList.add(c.getId());
                }
                dto.setChannelIdList(chIdList);
            }
        }

        return tidyDTOList;
    }

    private double contentSimilarity(ContentTidyDTO c1, Content c2){
        if(c1 == null || c2 == null) return 0.0;

        // 默认标题相似度权重为 3， 主创为1
        double titleSimilar = SimilarityUtils.getSimilarity(c1.getTitle(), c2.getName());
        double actorSimilar = SimilarityUtils.getSimilarity(c1.getTitle(), c2.getActors());

        return titleSimilar * ContentTidyDTO.POWER_TITLE + actorSimilar * ContentTidyDTO.POWER_ACTOR;
    }

    public class ComparatorContentTidyDTO implements Comparator {
        public int compare(Object arg0, Object arg1) {
            ContentTidyDTO dto0 = (ContentTidyDTO) arg0;
            ContentTidyDTO dto1 = (ContentTidyDTO) arg1;

            //根据grade排序
            return dto1.getSimilarity().compareTo(dto0.getSimilarity());
        }

    }

    /**
     * 获取前台点播视频详情
     * @param contentId 视频Id
     * @param channelId 从哪个栏目进来的
     * @return 详情包括收藏等信息
     */
    public ContentDetailDTO getContentDetail(Long contentId, Long channelId){
        Content content = contentDaoInterface.get(contentId);
        if( content == null) return null;

        ContentDetailDTO contentDetail = new ContentDetailDTO(content);
        // 获取栏目路径
        List<Channel> channelList = contentChannelLogic.getChannelsByContentId(contentDetail.getId());
        // 检查channelId是否有效，如果有效，取该栏目路径，否则取第一个
        boolean channelValid = false;

        if( channelList != null && channelId != null && channelId > 0){
            for( Channel channel : channelList ){
                if( channel.getId() == channelId ){
                    channelValid = true;
                    break;
                }
            }
        }
        if( !channelValid && channelList != null && channelList.size()>0){
            channelId = channelList.get(0).getId();
        }
        if(channelId > 0){
            List<Channel> parents = TreeUtils.getInstance().getParents(Channel.class, channelId);
            List<Channel> contentParents = new ArrayList<Channel>();
            for(int i=parents.size()-1; i>=0; i--){
                contentParents.add(parents.get(i));
            }
            contentDetail.setChannelPath(contentParents);
        }

        // 获取选集内容
        contentDetail.setEpisodeList(contentPropertyLogicInterface.getContentEpisodes(contentDetail.getId()));
        // 收藏次数
        contentDetail.setFavorite(userFavoritesLogicInterface.redexGetContentFavoriteCount(contentDetail.getId()));
        // 顶的次数/踩的次数 >=3 的记为顶
        contentDetail.setLike(userScoringLogicInterface.redexGetScoreRangeCount(contentDetail.getId(), 3, 9999));
        contentDetail.setDislike(userScoringLogicInterface.redexGetScoreRangeCount(contentDetail.getId(), 0, 2));

        // added by mlwang @2015-4-23，增加内容类型，直播/点播
        Device device = deviceLogicInterface.get(content.getDeviceId());
        if(device != null && device.getStatus().equals(DeviceLogicInterface.DEVICE_TYPE_HLS_LIVE)){
            contentDetail.setContentType(ContentDetailDTO._CONTENT_TYPE_LIVE);
        }else{
            contentDetail.setContentType(ContentDetailDTO._CONTENT_TYPE_VOD);
        }
        return contentDetail;
    }

    public List<Content> getPicContent(long channelId,long modelId) {
        return contentDaoInterface.getPicContent(channelId,modelId);
    }

    public List<Content> getContentsByChannelId(long channelId,PageBean pageBean) {
        return contentDaoInterface.getContentsByChannelId(channelId,pageBean);
    }

    public List<Content> getContentsByStatus(long status,String searchValue,Date startTime,Date stopTime,PageBean pageBean){
        return contentDaoInterface.getContentsByStatus(status,searchValue,startTime,stopTime,pageBean);
    }

    public Map<String,List<Content>>getContentsOfChannelIds(String ids,PageBean pageBean) {
        pageBean.setPageSize(2);//只需获取最新的两条媒体
        Map<String,List<Content>> map = new HashMap<String, List<Content>>();
        String[] channelIds = ids.split(",");
        for(String channelId : channelIds) {
            List<Content> contents = this.getContentsByChannelId(Long.valueOf(channelId),pageBean);
            map.put(channelId,contents);
        }

        return map;
    }

}


