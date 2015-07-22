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
        STATUS_CODES.put(STATUS_CP_ONLINE, "CP����");
        STATUS_CODES.put(STATUS_CP_OFFLINE, "CP����");
        STATUS_CODES.put(STATUS_WAIT_TO_ONLINE, "���ߴ���");
        STATUS_CODES.put(STATUS_WAIT_TO_OFFLINE, "���ߴ���");
        //STATUS_CODES.put(STATUS_SP_PUBLISHED,"SP�ѷ�����Ƶ��");
//        STATUS_CODES.put(STATUS_SP_ONLINE,"SP���ߣ�������");
//        STATUS_CODES.put(STATUS_SP_OFFLINE,"SP����");
        STATUS_CODES.put(STATUS_WAITING, "�ȴ���һ������");
        STATUS_CODES.put(STATUS_RECYCLE, "����վ");
        STATUS_CODES.put(STATUS_DELETE, "ɾ��");
        STATUS_CODES.put(STATUS_LOST_MEDIA_SOURCE, "Դ�ļ���ʧ");
        STATUS_CODES.put(STATUS_WAITING_FOR_ENCODE, "�ȴ�ת��");
        STATUS_CODES.put(STATUS_ENCODE_ERROR, "ת��ʧ��");
        STATUS_CODES.put(STATUS_WAITING_FOR_AUDIT, "�ȴ����");
        STATUS_CODES.put(STATUS_ENCODING, "����ת��");
        STATUS_CODES.put(STATUS_AUDIT_REJECTED, "��˱���");
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
            result = "[δ֪(" + status + ")]";
        }
        return result;
    }

    public Content initContent(Long contentId) {
        Content content = null;
        try {
            content = contentDaoInterface.get(contentId);
            return initContent(content);
        } catch (Exception e) {
            logger.error("��ʼ��Contentʱ�����쳣��id="+contentId+",err="+e.getMessage());
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
            logger.error("�����Content�ǿգ��޷�����������");
            return null;
        }
        contentId = content.getId();
        try {
            Map<String, Property> playUrlTypes = new HashMap<String, Property>();
            long moduleId = content.getModuleId();
            //�ҳ����е�ģ�����ԣ�׼������װ��
            List<Property> propertyList = (List<Property>) CacheUtils.get(moduleId,"propertiesOfModule",new com.fortune.util.DataInitWorker(){
                 public Object init(Object key,String cacheName){
                     return propertyLogic.getPropertiesOfModule((Long)key,PropertyLogicInterface.STATUS_ON,null,new PageBean(0,10000,"displayOrder","asc"));
                 }
            });
            Map<Long, Property> propertyMap = new HashMap<Long, Property>();
            //���䱣�浽���Ի����б���
            long devicePropertyId = 0;
            for (Property p : propertyList) {
                propertyMap.put(p.getId(), p);
                if (p.getRelatedTable() != null && p.getRelatedTable() == 1) {
                    devicePropertyId = p.getId();
                }
            }
            //�����ԵĿ�ѡֵ���л���
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
                    //�����豸�����뻺��
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

            //��ȡ���е���Դ��ص����Ե�ֵ
            List contentPropertyList = HibernateUtils.findAll(getSession(), "from ContentProperty cp where cp.contentId=" + content.getId() + " order by id asc");
            List<ContentProperty> outputContentProperty = new ArrayList<ContentProperty>();
            //���������б���ʼ��ֵ �����forѭ����Ҫ��ɨ����Ҳ������content���������ֵ ����һ����������contentProperty���С�
            for (Property p : propertyList) {
                String str=null;
                //�������������������Ҷ�Ӧ�������գ��ʹ�����¼Content������ȡֵ
                String fieldName = p.getColumnName();
                if (p.getIsMain() != null && p.getIsMain() == 1 && fieldName != null) {
                    if("".equals(fieldName)){
                        logger.error("�������ˣ����ԡ�" +p.getName()+
                                "����Ӧ��Content��Field����Ȼ�ǿգ�������ȥ�ˣ�");
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
                                                    logger.error("�޷��ҵ�Ƶ����channelId="+id);
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
            //����ɨ����ϣ�ɨ��contentProperty��
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
//                        logger.warn("���ԡ�" +p.getName()+
//                                "��[" +p.getCode()+
//                                "]�������У����ڴӱ�ContentProperty�з����ˣ�Ӧ���Ǹ��������ݣ�");
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
                                cp1.setDesp(p.getName());//�������������
                                cp1.setIntValue(cp.getIntValue());
                                cp1.setStringValue(str);
                                cp1.setExtraData(cp.getExtraData());
                            }
                            outputContentProperty.add(cp1);
                        }
                    }
                } else {
                    logger.error("�и������Ѿ���ɾ��������Ȼ�����������У����Ǹ����գ�Ҳ���������ݣ�" + cp.toString());
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
                        //��ǰ���psMap�б�����PropertySelect�е�value��Ӧ��code��������ɨ��contentPropertyʱ��������
                        //propertyName�С�ע�⣬����Ժ��߼����е��������ܻ������⡣
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
            logger.error("��ʼ��Content����ʱ�����쳣��" + e.getMessage());
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
                    cParameters.put("c_DOT_name", "���Ʋ���");
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
                    cParameters.put("cc_DOT_channelId", "δ֪Ƶ��");
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
                        //�ֱ���Ա�����ݺ�ӰƬ������ƥ�����Ƶ�ӰƬ������result��
                        String names = content.getActors(); //��Ա
                        //String ContentNames;
                        names = appendNames(names, content.getDirectors());//����
                        //��ȡӰƬ����ǰ4���ֽ���ƥ�����
                        String contentName = content.getName();
                        if (contentName.length() > 6) {
                            if (contentName.substring(0, 1).contains("��") || contentName.substring(0, 1).contains("��")) {
                                contentName = contentName.substring(1, 5);
                                //logger.debug("��ѯ��ӰƬ����Ϊ����������������������������������������������������������������������������������������" + contentName);
                            } else {
                                contentName = contentName.substring(0, 4);
                                //logger.debug("��ѯ��ӰƬ����Ϊ����������������������������������������������������������������������������������������" + contentName);
                            }
                        }
                        names = appendNames(names, contentName);    //ӰƬ����
                        if (names != null && !"".equals(names)) {
//                            int contentSize=4-result.size();
                            PageBean pageBean = new PageBean(0, 4, null, null);
                            for (String name : names.split(";")) {
                                List<Content> data = list(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, -1L, name, pageBean);
                                for (Content c : data) {
                                    //ֻ�в���ͬһ��Ƭ�Ӳżӽ�ȥ
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
                            //��������ݣ���Ա��ӰƬ���ƶ�δƥ�䵽�����ڵ�ǰӰ��Ƶ����������
                            List<Channel> channelList = contentChannelLogic.getChannelsByContentId(content.getId());  //�õ���ǰӰ�ӵ�Ƶ��
                            List<Content> contentList = new ArrayList<Content>();
                            if (channelList != null) {
                                for (Channel channel : channelList) {
                                    PageBean pageBean = new PageBean(0, 10, null, null);
//                                    List<Content> data = list(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, channel.getId(), pageBean);
                                    //���ѡȡ10�����ݡ�
                                    List<Object[]> data2 = list2(willCheckCspId, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED, channel.getId(), pageBean);
                                    //װ�䵽Content�
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
                                                //װ��Propertys���ԡ�
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
     * @param cspId csp��id
     * @param contentCspStatus ����״̬��һ������������߷�����2
     * @param channelId Ƶ��id
     * @param contentName ý������
     * @param directors  ����
     * @param actors  ��Ա
     * @param searchValues  ����ֵ
     * @param pageBean ��ҳ��Ϣ
     * @return                   �������
     */
    @SuppressWarnings("unchecked")
    public List<Content> list(final Long cspId, final Long contentCspStatus, final Long channelId, final String contentName,
                              final String directors, final String actors,
                              final List<ContentProperty> searchValues, final PageBean pageBean) {
        //�õ���Ŀ�Ƽ�����������û��
        List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
        if(pageBean!=null){
            if (pageBean.getStartRow() == 0) {
                //˵�����Ƽ�(��ʶ��ѯ������)
                pageBean.setPageSize(pageBean.getPageSize() - recommendResult.size());
            } else {
                //������ǵ�һҳ��ȥ�Ƽ�����Ŀ
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
                //��searchValues��һ����������������һ�����ݣ�����������������Ҫ�������������
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
                logger.debug("׼������������"+objKey);
                List<Content> tempResult = contentDaoInterface.list(cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues, pageBean);
                if (pageBean != null && pageBean.getStartRow() == 0) {
                    List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
                    for (Content c : recommendResult) {
                        result.add(getCachedContent(c.getId()));
                    }
                    pageBean.setRowCount(pageBean.getRowCount() + recommendResult.size());
                    logger.debug("������������¼����"+pageBean.getRowCount()+",��ǰ��ȡ����"+tempResult.size());
                }else{
                    logger.debug("��������pageBean=null�������������������ݲ�һ����ȷ����ǰ��ȡ����"+tempResult.size());
                }
                for (Content c : tempResult) {
                    logger.debug("��ʼ��"+c.getName());
                    result.add(getCachedContent(c.getId()));
                }
                if (pageBean != null) {
                    putToCache(countKey, "listContentCount", pageBean.getRowCount());
                }
                logger.debug("������������������������"+result.size());
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
                //��searchValues��һ����������������һ�����ݣ�����������������Ҫ�������������
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
                //channel.setName("��Ŀ¼");
                String systemChannelId = null;
                List<Channel> channels = channelLogic.search(channel);
                if (channels != null && channels.size() > 0) {
                    for (Channel c : channels) {
                        if ("��Ŀ¼".equals(c.getName()) || "��ҳ".equals(c.getName())) {
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
                if(propertyCode.startsWith("%")){//�ⲻ��Ҫ���ݿ�֧�ֵ�һ������
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
                    saveResult.add("" + propertyCode + "����ʧ��");
                    logger.error("Can't found property of code '" + propertyCode + "'");
                    continue;
                }
                Byte dataType = property.getDataType();
                if (PropertyLogicInterface.DATA_TYPE_CHECKBOX.equals(dataType) ||
                        PropertyLogicInterface.DATA_TYPE_COMBO.equals(dataType) ||
                        PropertyLogicInterface.DATA_TYPE_RADIO.equals(dataType)) {
                    String selectValue = getPropertySelectValue(property.getId(), value);
                    if (selectValue == null) {
                        //û���ҵ�����ֵ���Ǿʹ���һ��
                        String error = "�޷��ҵ���" + content.getName() + "�������ԣ�" + property.getName() + ",ֵ��" + value;
                        if (config.getBoolConfig("importXml.createSelectValueIfNotExists", true)) {
                            error += ",���������ļ��������������ֵ��";
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
                    //��������ݿ�����в������Ͳ�������ʱ���й����ݲ�����
                    Byte isMain = property.getIsMain();
                    if(isMain!=null&&isMain==1){
                        //�����Content��������ΪContent��ֵ
                        String columnName = property.getColumnName();
                        if(columnName!=null){
                            if(com.fortune.util.BeanUtils.setProperty(content,columnName,value)){
                                logger.debug(propertyCode+"->"+columnName+"���õ�����ɹ���");
                                continue;
                                //break;
                            }
                        }
                    }
                }
                //�������Content��������ΪContentProperty��ֵ
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
            //���½�ý�壬����󶨹�ϵ
            logger.debug("ý�塶" + content.getName() + "�����½�ý�壬��Ҫ���������������SP�İ󶨹�ϵ");
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
                logger.error("�޷���ȡCSP!id="+e.getMessage());
            }
        }
        if(cspId==null||cspId<=0||csp==null){
            //����ģʽ�»��Զ�����Ϊcsp���еĵ�һ��csp
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
                    //�½������������󣬾�ֱ������
                    logger.debug("�½�ý�壬����CP���������󣬾�ֱ�����ߣ�ý�壺" + content.getName() + ",csp=" + own.getName());
                    content.setStatus(STATUS_CP_ONLINE);
                }
            } catch (Exception e) {
                logger.error("�޷������й�CSP�İ󶨣�" + e.getMessage());
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
                    //�������SP�����ð�
                    continue;
                }
                if (CspLogicInterface.TRUE.equals(csp.getIsSpOnlineAudit())) {
                    defaultStatus = 0L;
                } else {
                    defaultStatus = ContentCspLogicInterface.STATUS_ONLINE;
                }
                if (isCreateNew) {
                    logger.debug("��Ӱ󶨹�ϵ��" + content.getName() + "(id=" + content.getId() + ")�󶨵�" + csp.getName() + "(id=" + csp.getId() + ")");
                    contentCspLogicInterface.setStatus(content.getId(), cspCsp.getMasterCspId(), -1, defaultStatus);
                } else {
                    logger.debug("���󶨹�ϵ��" + content.getName() + "(id=" + content.getId() + ")�󶨵�" + csp.getName() + "(id=" + csp.getId() + ")");
                    contentCspLogicInterface.checkStatus(content.getId(), cspCsp.getMasterCspId(), -1, defaultStatus);
                }
            } catch (Exception e) {
                logger.error("�޷���ȡ��ӦSP����Ϣ��" + e.getMessage());
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
            logger.error("�޷�����Content��" + content.toString() + "\n������Ϣ��" + e.getMessage());
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
                        //�ҵ��ɵ�ƥ�����ݣ����ƹ���
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
                logger.error("�޷����ҵ����ԣ�" + cp.getPropertyId() + "��������ݲ��ܵ��룺" + cp.toString());
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
            //����Ǻ���ͼƬ����Ҫ�������ͬ�������е�WEB������
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
                        logger.debug("���ƺ����ļ�" + picFile.getAbsolutePath() + " --> " + newPicFile.getAbsolutePath());
                        FileUtils.copy(picFile, newPicPath.getAbsolutePath(), FileUtils.extractFileName(newPicFile.getAbsolutePath(), File.separator));
                        //todo �ַ�����
                        if (shouldSyncPoster) {
                            JsUtils jsUtils = new JsUtils();
                            try {
                                jsUtils.saveAndPushSynFile(fileNameHere, newPicFile.getAbsolutePath(), newFileUrl, content.getCspId());
                            } catch (Exception e) {
                                e.getMessage();
                                logger.error("ͬ����Ϣ����");
                            }
                        }
                        //Ȼ�󱣴浽���ݿ�
                        cp = contentPropertyLogicInterface.save(cp);
                        if (cp == null) {
                            logger.error("�޷��������ݣ�");
                        }
                    }
                } else if (picFile.isDirectory()) {
                    logger.warn("�������������⣬url=" + picFile.getAbsolutePath());
                } else {
                    //�洢��û�У����ڱ������ң������û�У��ͱ���
                    File newPicFile = new File(webAppPath + "/" + cp.getStringValue());
                    if (!newPicFile.exists()) {
                        String errorMsg = "�����ļ�ȱʧ����" + content.getName() +
                                "���ģ�" + picFile.getAbsolutePath();
                        logger.error(errorMsg);
                        try {
                            os.write(errorMsg + "\r\n");
                        } catch (IOException e) {
                            logger.error("�޷�������������ļ���" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else if (checkMediaFile && (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                    PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType))) {
                //�����ý���ļ�
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
                        errorMsg = "ý���ļ��޷����в��ż��:��" + content.getName() +
                                "���ģ�" + mediaFile.getAbsolutePath();

                    }
                } else {
                    errorMsg = "ý���ļ�ȱʧ����" + content.getName() +
                            "����" + cp.getName() + "(" + cp.getIntValue() + ")" +
                            "��" + mediaFile.getAbsolutePath();
                    cp.setExtraInt(404L);
                }
                if (!checkOnly) {
                    cp = contentPropertyLogicInterface.save(cp);
                    if ("Media_Url_Source".equals(property.getCode())) {
                        if (cp.getExtraInt() == 200L) {
                            if (autoEncode) {
                                //�����Զ�ת����У�ֻ����Դ����ת��
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
                                saveResult.add("���������Ѿ����ת������" + taskInfo);
                            } else {
                                saveResult.add("��Ϊ����Ҫ�󣬲�����ת������" + cp.getStringValue());
                            }
                        } else {
                            saveResult.add("��Ϊ�ļ�����ȷ��������ת������" + cp.getStringValue() + "��������ڲ�����Ƶ�ļ������ڲ�����ɺ�֪ͨϵͳ����Ա�ֹ�����ת�롣");
                        }
                    }
                }
                if (errorMsg != null && !"".equals(errorMsg)) {
                    logger.error(errorMsg);
                    //saveResult.add(errorMsg);
                    try {
                        os.write(errorMsg + "\r\n");
                    } catch (IOException e) {
                        logger.error("�޷�������������ļ���" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                if (!checkOnly) {
                    cp = contentPropertyLogicInterface.save(cp);
                    if (cp == null) {
                        logger.error("�޷��������ݣ�");
                    }
                }
            }
        }
        return content;
    }

    public boolean downloadFile(Device device,String fileName,String desertFile){
        String url = device.getUrl()+"/"+fileName;
        if(url.startsWith("http://hls.")){
            //������Ҫ���ȵģ�����IP��������
            url = "http://"+device.getIp()+":"+TcpUtils.getPortFromUrl(url)+("/"+StringUtils.getClearURL(url).replace("//","/"));
        }else if(url.contains("serverIP")){
            url = url.replace("serverIP",device.getIp());
        }else if(url.contains("serverIp")){
            url = url.replace("serverIp",device.getIp());
        }
        File localPath = new File(device.getLocalPath());
        logger.debug("�������أ�"+url+",���浽��"+localPath.getAbsolutePath());
        HttpDownloadWorker httpDownloadWorker = new HttpDownloadWorker(url,
                localPath.getAbsolutePath(),desertFile,null);
        return httpDownloadWorker.download();
    }

    /**
     * ����ý����Ϣ
     * @param content Ҫ�����ý�����
     * @param contentProperties ���Խ��
     * @param oldContentProperties ��ǰ����������Խ������
     * @param saveResult �����е�������Ϣ
     * @param picPath ͼƬ·��
     * @param webAppPath web�ĸ�Ŀ¼
     * @param cspFilePath csp���ļ�ľ��¼
     * @param fileNameHere �ļ���
     * @param os �����־�ļ���
     * @param checkOnly �Ƿ��Ǽ��
     * @return ������content����
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
        //����������е�ͼƬ����Ƶ����
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
                        //�ҵ��ɵ�ƥ�����ݣ����ƹ���
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
                logger.error("�޷����ҵ����ԣ�" + cp.getPropertyId() + "��������ݲ��ܵ��룺" + cp.toString());
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
            //����Ǻ���ͼƬ����Ҫ�������ͬ�������е�WEB������
            if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType) && shouldCopyPoster) {
                //   if picExist then begin
                //       CopyFileFromNasToMasterHost81PostDir
                //       SyncFileToAllWebHost86_87_89
                //   else begin
                //       Warning
                //   end if
                File picFile = new File(picPath + "/" + cp.getStringValue());
                if(!picFile.exists()){
                    //����ļ������ڣ��ͳ��Խ������ء�
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
                        logger.debug("���ƺ����ļ�" + picFile.getAbsolutePath() + " --> " + newPicFile.getAbsolutePath());
                        FileUtils.copy(picFile, newPicPath.getAbsolutePath(), FileUtils.extractFileName(newPicFile.getAbsolutePath(), File.separator));
                        //todo �ַ�����
                        //Ȼ�󱣴浽���ݿ�
                        willSaveProperties.add(cp);
/*
                        cp = contentPropertyLogicInterface.save(cp);
                        if (cp == null) {
                            logger.error("�޷��������ݣ�");
                        }
*/
                    }
                } else if (picFile.isDirectory()) {
                    logger.warn("�������������⣬url=" + picFile.getAbsolutePath());
                } else {
                    //�洢��û�У����ڱ������ң������û�У��ͱ���
                    File newPicFile = new File(webAppPath + "/" + cp.getStringValue());
                    if (!newPicFile.exists()) {
                        String errorMsg = "�����ļ�ȱʧ����" + content.getName() +
                                "���ģ�" + picFile.getAbsolutePath();
                        logger.error(errorMsg);
                        try {
                            os.write(errorMsg + "\r\n");
                        } catch (IOException e) {
                            logger.error("�޷�������������ļ���" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } else if (checkMediaFile && (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                    PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType))) {
                //�����ý���ļ�
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
                        errorMsg = "ý���ļ��޷����в��ż��:��" + content.getName() +
                                "���ģ�" + mediaFile.getAbsolutePath();

                    }
                } else {
                    errorMsg = "ý���ļ�ȱʧ����" + content.getName() +
                            "����" + cp.getName() + "(" + cp.getIntValue() + ")" +
                            "��" + mediaFile.getAbsolutePath();
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
                        logger.error("�޷�������������ļ���" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                if (!checkOnly) {
                    willSaveProperties.add(cp);
/*
                    cp = contentPropertyLogicInterface.save(cp);
                    if (cp == null) {
                        logger.error("�޷��������ݣ�");
                    }
*/
                }
            }
        }
        try {
            if (!checkOnly) {
                willSaveProperties = contentPropertyLogicInterface.setContentFromContentProperties(content, willSaveProperties,oldContentProperties);
                content = save(content);
                //���ϵ�����ɾ��
                for(ContentProperty cp:oldContentProperties){
                    logger.debug("׼��ɾ����"+content.getName()+"����"+cp.getName()+","+cp.getStringValue());
                    contentPropertyLogicInterface.remove(cp);
                }
                for(ContentProperty cp:willSaveProperties){
                    cp.setContentId(content.getId());
                    logger.debug("׼�����桶"+content.getName()+"����"+cp.getName()+","+cp.getStringValue());
                    cp = contentPropertyLogicInterface.save(cp);
                    Property property = propertyLogic.getPropertyByCache(cp.getPropertyId());
                    Byte dataType = property.getDataType();
                    if (PropertyLogicInterface.DATA_TYPE_FLV.equals(dataType) ||
                            PropertyLogicInterface.DATA_TYPE_MP4.equals(dataType)){
                        //׼������ת��
                        if ("Media_Url_Source".equals(property.getCode())) {
                            //ԭ�����߼����Ҫ����ļ����ڲŻ����ת�룬���ں���������
                            if (autoEncode) {
                                //�����Զ�ת����У�ֻ����Դ����ת��
                                //cp.setExtraInt(ContentPropertyLogicInterface.EXTRA_INT_HAS_ENCODED);
                                List<EncoderTask> tasks = encoderTaskLogicInterface.createEncoderTasksForAllTemplate(content, cp);
                                String taskInfo = "";
                                for (EncoderTask task : tasks) {
                                    taskInfo += "" + task.getName() + ",";
                                }
                                if (!"".equals(taskInfo)) {
                                    taskInfo = taskInfo.substring(0, taskInfo.length() - 1);
                                }
                                saveResult.add("���������Ѿ����ת������" + taskInfo);
                            } else {
                                saveResult.add("��Ϊ����Ҫ�󣬲�����ת������" + cp.getStringValue());
                            }
/*
                            if (cp.getExtraInt() == 200L) {
                            } else {
                                saveResult.add("��Ϊ�ļ�����ȷ��������ת������" + cp.getStringValue() + "��������ڲ�����Ƶ�ļ������ڲ�����ɺ�֪ͨϵͳ����Ա�ֹ�����ת�롣");
                            }
*/
                        }
                    }else if (PropertyLogicInterface.DATA_TYPE_FILE_PIC.equals(dataType) && shouldCopyPoster) {
                        //׼��ͬ��
                        if (shouldSyncPoster) {
                            JsUtils jsUtils = new JsUtils();
                            try {
                                String newFileUrl = cp.getStringValue();
                                File newPicFile = new File((webAppPath + newFileUrl).replace('/', File.separatorChar));
                                jsUtils.saveAndPushSynFile(fileNameHere, newPicFile.getAbsolutePath(), newFileUrl, content.getCspId());
                            } catch (Exception e) {
                                e.getMessage();
                                logger.error("ͬ����Ϣ����");
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
            logger.error("�޷�����Content��" + content.toString() + "\n������Ϣ��" + e.getMessage());
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
                    logger.error("�޷��ҵ��豸:" + keyId + "," + e.getMessage(), e);
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
     * ���浽xmlʱ�����л���������ʱ���ռ�������ȡ����������Ϣ
     * @param clips  ���м�
     * @param episodes ����
     * @return ���
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
     * ��һ��content�б��ʽ����xml�С�
     * @param fileName xml�ļ���
     * @param contents ý���б�
     * @param moduleId ģ��id
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
                    //����������һ������
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
                            //��������Ƿ��ǲ�������
                            List<Map<String,String>> playUrlTypes =(List<Map<String,String>>) properties.get("playUrlTypes");
                            boolean isNotClips = true;
                            if(playUrlTypes!=null){
                                for(Map<String,String> type:playUrlTypes){
                                    String code = type.get("code");
                                    if(key.equals(code)){
                                        if(propertyValue instanceof List){
                                            //���ǵ㲥����
                                            clips = (List)propertyValue;
                                            isNotClips = false;
                                        }else if(propertyValue instanceof Map){
                                            clips = new ArrayList<Map<String,String>>();
                                            clips.add((Map<String,String>)propertyValue);
                                            isNotClips = false;
                                        }else{
                                            logger.error("�޷��ҵ���ȷ���������ͣ�"+key+",name="+content.getName());
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
                //�Լ���Ϊ�ؼ������Ĳ�������
                List<Map<String,String>> playUrls = new ArrayList<Map<String,String>>();
                //׼�����ӰƬ��ƵƬ����Ϣ
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
                            logger.error("������" +content.getName()+
                                    "��Ƭ����Ϣʱ�����쳣��"+e.getMessage());
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
                //���Ƶ����Ϣ
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
     * ɨ��һ��xml�ļ�������һ��ý���б�
     *
     * @param fileName xml�ļ���
     * @return ɨ������ý���б�
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> scanXml(String fileName, Long moduleId, List<String> response, String xmlFileEncoding) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String,Property> propertyMap = getPropertyMap(moduleId);
        List<Content> result = new ArrayList<Content>();
        logger.debug("����ɨ��xml�ļ���"+fileName);
        map.put("contents", result);
        //���Ʋ���ͬʱ����һ���ļ�
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
                response.add("û���ҵ����xml�ļ����������Ա��ϵ��");
                logger.error("�޷��ҵ�XML�ļ���" + xmlFile.getAbsolutePath());
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
                    response.add("���԰�Ȩ��Ϣ�����е���ӰƬ������Ϊ10�����ڣ�");
                } else {
                    response.add("�ϸ���XML�еİ�Ȩ��Ϣ���뵽ϵͳ�У�");
                }
                List allContentNodes = root.selectNodes("content");
                long cpCode = XmlUtils.getLongValue(root, "@CPCode", -1);
                map.put("cpCode", cpCode);
                int i = 0, nodeCount = allContentNodes.size();
                for (Object node : allContentNodes) {
                    i++;
                    if (i % 50 == 0) {
                        logger.debug("������" + i + "/" + nodeCount + ",�ٷֱ�" + Math.round(i * 100.0 / nodeCount) + "%...");
                    }
                    Node contentNode = (Node) node;
                    importContentId = XmlUtils.getValue(contentNode, "@ContentID", "");
                    //�ж�  ContentID �����ݿ����Ƿ����
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
                            clip.setName(XmlUtils.getValue(subContentNode, "@SubContentName", contentName + "��" + episodes + "��"));
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
                        validStartTimeStr = StringUtils.date2string(new Date(), "yyyyMMdd");//���ó�����
                        validEndTimeStr = StringUtils.date2string(new Date(System.currentTimeMillis() + 10 * 365 * 24 * 3600 * 1000L), "yyyyMMdd");//����ʮ���
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

                        String warnStr = "�Ѿ�������" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                        if (validEndTime.after(now)) {
                            warnStr = "�ٹ�" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                    "�ͻ����";
                        }
                        tempStr = "���棺����ý�塶" + content.getName() + "(id=" + content.getId() + ")" +
                                "���İ�Ȩ�������ڿ��������⣬" + warnStr +
                                "������Ѿ����ڣ��ᱻϵͳ�Զ����ߣ���ʼ����" +
                                StringUtils.date2string(validStartTime) + "->��������" +
                                StringUtils.date2string(validEndTime) + "����ǰ���ڣ�" + StringUtils.date2string(new Date()) +
                                "(xml�����ݣ�" + validStartTimeStr + "->" + validEndTimeStr + ")";
                        logger.error(tempStr);
                        response.add(tempStr);
                    }
                    if (content == null) {
                        importErrorCount++;
                        tempStr = "����ý���Ƿ����쳣�������XMLΪ��\n" + contentNode.asXML();
                        logger.error(tempStr);
                        response.add(tempStr);
                        importErrorCount++;
                    }                    //��������ϵ����ݣ��Ǿ�Ҫ�½���Ӧ����ƺ�csp��
                    List channels = contentNode.selectNodes("channels/channel");
                    if (channels == null || channels.size() <= 0) {
                        tempStr = content.getName() + "ý��û��Ƶ���ڵ�!";
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
                String resultStr = "����ѡ�е�xml�ļ���ϣ��ۼ�";
                resultStr += "ý�壺" + importContentCount + "��������" + importErrorCount + "����";
                response.add(resultStr);
                logger.debug(resultStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("�����쳣���޷�����ִ�е��������" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("ɨ��XMLʱ�����쳣��" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        map.put("endTime", new Date());
        logger.debug("���ɨ��xml�ļ���"+fileName);
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<String> importXmlV2(String xmlFileName, Long cspId, Integer adminId, Long deviceId,
                                  Long moduleId, String xmlFileEncoding, String webAppPath, String cspFilePath, String fileNameHere,
                                  boolean checkOnly) {
        //���Ʋ���ͬʱ����һ���ļ�
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
                //�ҵ������csp�󶨵ķ���������ѡ����һ��
                List<Device> devicesOfCsp = cspDeviceLogicInterface.getDeviceOfCsp(cspId);
                if (devicesOfCsp != null && devicesOfCsp.size() > 0) {
                    deviceId = devicesOfCsp.get(0).getId();
                } else {
                    deviceId = config.getLongConfig("importXml.default.deviceId", 11054546l);//Ĭ�ϵ�һ���������������豸id
                }
            }
            int importContentCount = 0;
            int importErrorCount = 0;
            List<String> result = new ArrayList<String>();
            File xmlFile = new File(xmlFileName);
            if (!xmlFile.exists()) {
                result.add("û���ҵ����xml�ļ����������Ա��ϵ��");
                logger.error("�޷��ҵ�XML�ļ���" + xmlFile.getAbsolutePath());
                return result;
            }
            String importContentId;
            String mediaName;

            Long channelId;
            Device device = null;
            try {
                device = deviceLogicInterface.get(deviceId);
            } catch (Exception e) {
                String info = "�޷��ҵ���ý���������ID=" + deviceId;
                logger.error(info);
                result.add(info);
                importErrorCount++;
            }

            File importResultFile = new File(webAppPath + "/import/logs/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss_") + cspId + "_" + xmlFile.getName() +
                    Math.round(Math.random() * 1000) + ".txt");
            File pathFile = importResultFile.getParentFile();
            if (!pathFile.exists()) {
                if (!pathFile.mkdirs()) {
                    logger.error("�޷�������־Ŀ¼����־��¼���ܻ������⣡");
                }
            }
            //�������xml��һ������
            File backupXmlFile = new File(webAppPath + "/import/backup/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss") + "_" + xmlFile.getName());
            FileUtils.copy(xmlFile, backupXmlFile.getParentFile().getAbsolutePath(), backupXmlFile.getName());
            result.add("xml�ļ��Ѿ����ݵ���" + backupXmlFile.getAbsolutePath());
            OutputStreamWriter os = null;
            String tempStr;
            boolean ignoreCopyRight = config.getBoolConfig("importXml.default.ignoreCopyRight", false);
            try {
                if (ignoreCopyRight) {
                    result.add("���԰�Ȩ��Ϣ�����е���ӰƬ������Ϊ10�����ڣ�");
                } else {
                    result.add("�ϸ���XML�еİ�Ȩ��Ϣ���뵽ϵͳ�У�");
                }
                Map<String, Object> xmlResult = scanXml(xmlFileName, moduleId, result, xmlFileEncoding);
                List<Content> contentsFromXml = (List<Content>) xmlResult.get("contents");
                os = new OutputStreamWriter(new FileOutputStream(importResultFile));
                os.write(StringUtils.date2string(startTime) +
                        "-" + "��������xml�ļ���" + xmlFile.getAbsolutePath() + "\r\n");
                Long cpCode = (Long) xmlResult.get("cpCode");

                if (cspId.equals(cpCode)) {
                    int i = 0, nodeCount = contentsFromXml.size();
                    for (Content contentFromXml : contentsFromXml) {
                        int startErrorCount = importErrorCount;
                        i++;
                        if (i % 50 == 0) {
                            logger.debug("������" + i + "/" + nodeCount + ",�ٷֱ�" + Math.round(i * 100.0 / nodeCount) + "%...");
                            os.flush();
                        }
                        importContentId = contentFromXml.getContentId();
                        //�ж�  ContentID �����ݿ����Ƿ����
                        mediaName = contentFromXml.getName();
                        List<Content> contents = getContent(mediaName, null, cspId);

                        int oldCount = contents != null ? contents.size() : 0;
                        Content content = null;
                        if (oldCount > 0) {
                            content = contents.get(0);
                            String msg = "ý�塶" + mediaName +
                                    "���ظ����룬ԭƽ̨IDΪ" + importContentId + "���ڱ�ƽ̨IDΪ" +
                                    content.getId() +
                                    "���ڱ�ƽ̨ӰƬ��Ϊ����" + content.getName() +
                                    "�������ʱ�䣺" + StringUtils.date2string(content.getCreateTime()) +
                                    "����ǰ״̬��" + getStatusString(content.getStatus()) +
                                    "�������޸��滻ԭ�����ݣ�";
                            result.add(msg);
                            logger.warn(msg);
                            //������һ�������ǵ�һ�����ݵ�����
                            //����������
                            if (oldCount > 1) {
                                for (int idx = 1; idx < oldCount; idx++) {
                                    Content c = contents.get(idx);
                                    if (ContentLogicInterface.STATUS_CP_ONLINE.equals(c.getStatus())) {
                                        msg = "ý�塶" + c.getName() +
                                                "���ظ����룬���ң��ڱ�ƽ̨Ҳ�ظ��ˣ���ԭCPƽ̨��ID" +
                                                "Ϊ" + c.getContentId() + "���ڱ�ƽ̨IDΪ" + c.getId() +
                                                "�����ʱ��Ϊ��" + StringUtils.date2string(c.getCreateTime()) +
                                                "����������󣬽��Ὣ������¼���ߣ�";
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
                                            String info = "ý�塶" + content.getName() +
                                                    "���ĺ�����" + cp.getName() + "," + cp.getIntValue() + ",xml�����ݣ�" + cp.getStringValue() + ",�ļ�λ�ã�" + picFile.getAbsolutePath() +
                                                    "��û���ҵ���";
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
                                                String info = "ý�塶" + content.getName() +
                                                        "����Ƭ�Ρ�" + cp.getName() + "," + cp.getIntValue() + "," + mediaFile.getAbsolutePath() +
                                                        "��û���ҵ���";
                                                os.write(info + "\r\n");
                                                result.add(info);
                                                importErrorCount++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (posterCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")����XML������û���κκ�����");
                            } else if (posterCanUseCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")��û���κο��ú�����");
                            }
                            if(config.getBoolConfig("importXml.default.checkMediaFile", true)){
                                if (clipCount == 0) {
                                    result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                            ")��û����XML���ҵ��κβ���Ƭ�Σ�");
                                } else if (clipCanUseCount == 0) {
                                    result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                            ")��û���κο��ò���Ƭ�Σ�");
                                }
                            }else{
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")��ϵͳ���ò����ý���ļ�����ν��֪��ý���ļ��������");
                            }
                            Date now = new Date();
                            Date validEndTime = content.getValidEndTime();
                            Date validStartTime = content.getValidStartTime();
                            if (validEndTime.before(new Date(now.getTime() + 7 * 24 * 3600 * 1000L))) {
                                importErrorCount++;

                                String warnStr = "�Ѿ�������" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                                if (validEndTime.after(now)) {
                                    warnStr = "�ٹ�" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                            "�ͻ����";
                                }
                                tempStr = "���棺����ý�塶" + content.getName() + "(id=" + content.getId() + ")" +
                                        "���İ�Ȩ�������ڿ��������⣬" + warnStr +
                                        "������Ѿ����ڣ��ᱻϵͳ�Զ����ߣ���ʼ����" +
                                        StringUtils.date2string(validStartTime) + "->��������" +
                                        StringUtils.date2string(validEndTime) + "����ǰ���ڣ�" + StringUtils.date2string(new Date());
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                            }
                            if (contentFromXml == null) {
                                importErrorCount++;
                                tempStr = "����ý���Ƿ����쳣����������Ϊ��";
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                                importErrorCount++;
                            }
                        }
                        //��������ϵ����ݣ��Ǿ�Ҫ�½���Ӧ����ƺ�csp��
                        if (!hasOldData) {
                            if (!checkOnly) {
                                ContentAudit contentAudit = saveContentAudit(content.getId());
                                saveContentCsp(content.getId(), cpCode, contentAudit.getId(), ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                            }
                        }
                        List<Channel> channels = (List<Channel>) contentFromXml.getProperties().get("channels");
                        if (channels == null || channels.size() <= 0) {
                            tempStr = content.getName() + "ý��û��Ƶ���ڵ�!";
                            result.add(tempStr);
                            os.write(tempStr + "\r\n");
                            importErrorCount++;
                            logger.error(tempStr);
                        } else {
                            for (Channel channelFromXml : channels) {
                                channelId = channelFromXml.getId();
                                if (channelId < 0) {
                                    tempStr = content.getName() + "Ƶ��IDû����ȷ����!";
                                    result.add(tempStr);
                                    os.write(tempStr + "\r\n");
                                    logger.error(tempStr);
                                    continue;
                                }
                                String channelName = channelFromXml.getName();
                                //�鿴��ǰ�û��Ƿ������Ƶ������Ȩ��
                                Channel channel = (Channel) TreeUtils.getInstance().getObject(Channel.class, channelId);
                                if (channel == null) {
                                    //Ƶ��ID���ԡ����Խ��в²⣬�ܷ��ҵ�һ��������ֻ�Ƕ������ŵ�
                                    tempStr = content.getName() + ",�޷�ͨ��ChannelID=" + channelId + "������Ƶ��������ͨ��Ƶ�������飺" + channelName + "....";
                                    channel = (Channel) CacheUtils.get(channelName, "channelByNameCache", new com.fortune.util.DataInitWorker() {
                                        public Object init(Object key, String cacheName) {
                                            Channel bean = new Channel();
                                            String searchName = key.toString();
                                            bean.setName(searchName);

                                            List<Channel> channelList = channelLogic.search(bean);
                                            for (Channel ch : channelList) {
                                                String name = ch.getName();
                                                if (name.equals(searchName) || name.startsWith(searchName + "(") ||
                                                        name.startsWith(searchName + "��")) {
                                                    return ch;
                                                }
                                            }
                                            return null;
                                        }
                                    });
                                    if (channel == null) {
                                        importErrorCount++;
                                        tempStr += "��" + content.getName() +
                                                "������ʱ���޷��ҵ�Ƶ����������ͨ��ID=" + channelId + ",����ͨ��Name=" + channelName;
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
                                    //�����root���е��룬��������Ȩ��Ϣ
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
                                    //�ж��Ƿ�ΪҶ�ӽڵ㡣
                                    if (isLeafChannel || ((!isLeafChannel) && allowPublishToUnLeafChannel)) {
                                        //��Ƶ����content������
                                        boolean shouldCreateContentChannel = true;
                                        //������ӰƬ�Ѿ����ˣ��Ǿ�Ҫ����Ƿ��Ѿ����������Ƶ�������û�У��ͷ���������Ѿ��������ͷ���
                                        if (hasOldData && contentChannelLogic.isExists(channelId, content.getId())) {
                                            shouldCreateContentChannel = false;
                                            tempStr = "ý�塶" + content.getName() + "���Ѿ����ڣ��Ѿ����ù�Ƶ���󶨣����ٷ�����" + channelName;
                                            //os.write(tempStr+"\r\n");
                                            result.add(tempStr);
                                            logger.debug(tempStr);
                                        }
                                        if (shouldCreateContentChannel) {
                                            if (hasOldData) {
                                                tempStr = "ý�塶" + content.getName() + "���Ѿ����ڣ����·�����Ƶ����" + channelName;
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
                                        tempStr = "Ƶ����" + channelName + "������Ҷ�ӽڵ㣬��������ʱ��ܾ���" + content.getName() + "������ý�嵽���Ƶ����";
                                        os.write(tempStr + "\r\n");
                                        logger.error(tempStr);
                                    }
                                } else {
                                    tempStr = "��" + content.getName() + "������ʱ��������û��Ȩ�ް󶨵�idΪ" + channelId + "����Ϊ��" + channelName + "�����Ƶ�����봴�����߹���Ա��Ƶ����Ȩ";
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
                            //û�д������ԭý��û�����ߣ����Խ������߷���
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                                content.setStatusTime(new Date());
                                content = save(content);
                                contentCspLogicInterface.setStatus(content.getId(), content.getCspId(), -1, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                result.add("ý�塶" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")��û�д����Խ���ֱ�����߷�����");
                            }
                        } else {
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                result.add("ý�塶" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")����Ϣ����" + contentErrorCount +
                                        "����������״̬��" + getStatusString(content.getStatus()) +
                                        "�������ֹ������������ݺ����ߣ�");
                            }
                        }
                        importContentCount++;
                    }
                } else {
                    tempStr = "��ǰ�û����ܲ�����xml,xml��cpCodeӦ��Ϊ" + cspId;
                    result.add(tempStr);
                    os.write(tempStr + "\r\n");
                    importErrorCount++;
                    logger.error("��ǰ��û�����Ȩ�ޣ����ܲ�����xml");
                }
                String resultStr = "����ѡ�е�xml�ļ���ϣ��ۼ�";
                if (checkOnly) {
                    resultStr += "���";
                } else {
                    resultStr += "���";
                }
                resultStr += "ý�壺" + importContentCount + "��������" + importErrorCount + "����";
                os.write(resultStr + "\r\n");
                result.add(resultStr);
                logger.debug(resultStr);
                Date stopTime = new Date();
                os.write("-����ʱ�䣺" + StringUtils.date2string(startTime) + "\n");
                os.write("-���ʱ�䣺" + StringUtils.date2string(stopTime));
            } catch (IOException e) {
                logger.error("�޷��ҵ��ļ���" + importResultFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("�����쳣���޷�����ִ�е��������" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("�޷��ر��ļ���" + importResultFile.getAbsolutePath());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("����ӰƬ��Ϣ�Ƿ����쳣��" + e.getMessage());
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
        //���Ʋ���ͬʱ����һ���ļ�
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
                //�ҵ������csp�󶨵ķ���������ѡ����һ��
                List<Device> devicesOfCsp = cspDeviceLogicInterface.getDeviceOfCsp(cspId);
                if (devicesOfCsp != null && devicesOfCsp.size() > 0) {
                    deviceId = devicesOfCsp.get(0).getId();
                } else {
                    deviceId = config.getLongConfig("importXml.default.deviceId", 11054546l);//Ĭ�ϵ�һ���������������豸id
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
                result.add("û���ҵ����xml�ļ����������Ա��ϵ��");
                logger.error("�޷��ҵ�XML�ļ���" + xmlFile.getAbsolutePath());
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
                String info = "�޷��ҵ���ý���������ID=" + deviceId;
                logger.error(info);
                result.add(info);
                importErrorCount++;
            }

            File importResultFile = new File(webAppPath + "/import/logs/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss_") + cspId + "_" + xmlFile.getName() +
                    Math.round(Math.random() * 1000) + ".txt");
            File pathFile = importResultFile.getParentFile();
            if (!pathFile.exists()) {
                if (!pathFile.mkdirs()) {
                    logger.error("�޷�������־Ŀ¼����־��¼���ܻ������⣡");
                }
            }
            //�������xml��һ������
            File backupXmlFile = new File(webAppPath + "/import/backup/" + StringUtils.date2string(startTime, "yyyyMMddHHmmss") + "_" + xmlFile.getName());
            FileUtils.copy(xmlFile, backupXmlFile.getParentFile().getAbsolutePath(), backupXmlFile.getName());
            result.add("xml�ļ��Ѿ����ݵ���" + backupXmlFile.getAbsolutePath());
            OutputStreamWriter os = null;
            String tempStr;
            boolean ignoreCopyRight = config.getBoolConfig("importXml.default.ignoreCopyRight", false);
            try {
                if (ignoreCopyRight) {
                    result.add("���԰�Ȩ��Ϣ�����е���ӰƬ������Ϊ10�����ڣ�");
                } else {
                    result.add("�ϸ���XML�еİ�Ȩ��Ϣ���뵽ϵͳ�У�");
                }
                os = new OutputStreamWriter(new FileOutputStream(importResultFile));
                os.write("-" + StringUtils.date2string(startTime) +
                        "��������xml�ļ���" + xmlFile.getAbsolutePath() + "\r\n");
                List allContentNodes = root.selectNodes("content");
                long cpCode = XmlUtils.getLongValue(root, "@CPCode", -1);

                if (cspId == cpCode) {
                    int i = 0, nodeCount = allContentNodes.size();
                    for (Object node : allContentNodes) {
                        int startErrorCount = importErrorCount;
                        i++;
                        if (i % 50 == 0) {
                            logger.debug("������" + i + "/" + nodeCount + ",�ٷֱ�" + Math.round(i * 100.0 / nodeCount) + "%...");
                            os.flush();
                        }
                        Node contentNode = (Node) node;
                        importContentId = XmlUtils.getValue(contentNode, "@ContentID", "");
                        //�ж�  ContentID �����ݿ����Ƿ����
                        mediaName = XmlUtils.getValue(contentNode, "@MediaName", "");
                        Content content = null;
                        List<Content> contents = getContent(mediaName, null, cspId);
                        int oldCount = contents != null ? contents.size() : 0;
                        if (oldCount > 0) {
                            content = contents.get(0);
                            String msg = "ý�塶" + mediaName +
                                    "���ظ����룬ԭƽ̨IDΪ" + importContentId + "���ڱ�ƽ̨IDΪ" +
                                    content.getId() +
                                    "���ڱ�ƽ̨ӰƬ��Ϊ����" + content.getName() +
                                    "�������ʱ�䣺" + StringUtils.date2string(content.getCreateTime()) +
                                    "����ǰ״̬��" + getStatusString(content.getStatus()) +
                                    "�������޸��滻ԭ�����ݣ�";
                            result.add(msg);
                            logger.warn(msg);
                            //������һ�������ǵ�һ�����ݵ�����
                            //����������
                            if (oldCount > 1) {
                                for (int idx = 1; idx < oldCount; idx++) {
                                    Content c = contents.get(idx);
                                    if (ContentLogicInterface.STATUS_CP_ONLINE.equals(c.getStatus())) {
                                        msg = "ý�塶" + c.getName() +
                                                "���ظ����룬���ң��ڱ�ƽ̨Ҳ�ظ��ˣ���ԭCPƽ̨��ID" +
                                                "Ϊ" + c.getContentId() + "���ڱ�ƽ̨IDΪ" + c.getId() +
                                                "�����ʱ��Ϊ��" + StringUtils.date2string(c.getCreateTime()) +
                                                "����������󣬽��Ὣ������¼���ߣ�";
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
                        String msg = "ý�塶" + mediaName +
                                "���ظ����룬IDΪ" + importContentId +"����������Դ������Ҫ���Ƶ��������";
                        logger.info(msg);
*/
                            //String msg = "ý�塶" + mediaName +"���ظ����룬�����IDΪ" + importContentId +"���ȴӱ�ƽ̨����ӰƬ���ݣ����������޸ĺ��滻��";
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
                            //result.add("����xml���ݿ�ʼ��ӰƬΪ��"+mediaName+"");
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
                                    clip.setName(XmlUtils.getValue(subContentNode, "@SubContentName", contentName + "��" + episodes + "��"));
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
                                validStartTimeStr = StringUtils.date2string(new Date(), "yyyyMMdd");//���ó�����
                                validEndTimeStr = StringUtils.date2string(new Date(System.currentTimeMillis() + 10 * 365 * 24 * 3600 * 1000L), "yyyyMMdd");//����ʮ���
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
                                            String info = "ý�塶" + content.getName() +
                                                    "���ĺ�����" + cp.getName() + "," + cp.getIntValue() + ",xml�����ݣ�" + cp.getStringValue() + ",�ļ�λ�ã�" + picFile.getAbsolutePath() +
                                                    "��û���ҵ���";
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
                                                String info = "ý�塶" + content.getName() +
                                                        "����Ƭ�Ρ�" + cp.getName() + "," + cp.getIntValue() + "," + mediaFile.getAbsolutePath() +
                                                        "��û���ҵ���";
                                                os.write(info + "\r\n");
                                                result.add(info);
                                                importErrorCount++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (posterCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")����XML������û���κκ�����");
                            } else if (posterCanUseCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")��û���κο��ú�����");
                            }
                            if (clipCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")��û����XML���ҵ��κβ���Ƭ�Σ�");
                            } else if (clipCanUseCount == 0) {
                                result.add("ý�塶" + content.getName() + "(id=" + content.getId() +
                                        ")��û���κο��ò���Ƭ�Σ�");
                            }
                            Date now = new Date();
                            if (validEndTime.before(new Date(now.getTime() + 7 * 24 * 3600 * 1000L))) {
                                importErrorCount++;

                                String warnStr = "�Ѿ�������" + StringUtils.formatTime((now.getTime() - validEndTime.getTime()) / 1000);
                                if (validEndTime.after(now)) {
                                    warnStr = "�ٹ�" + StringUtils.formatTime((validEndTime.getTime() - now.getTime()) / 1000) +
                                            "�ͻ����";
                                }
                                tempStr = "���棺����ý�塶" + content.getName() + "(id=" + content.getId() + ")" +
                                        "���İ�Ȩ�������ڿ��������⣬" + warnStr +
                                        "������Ѿ����ڣ��ᱻϵͳ�Զ����ߣ���ʼ����" +
                                        StringUtils.date2string(validStartTime) + "->��������" +
                                        StringUtils.date2string(validEndTime) + "����ǰ���ڣ�" + StringUtils.date2string(new Date()) +
                                        "(xml�����ݣ�" + validStartTimeStr + "->" + validEndTimeStr + ")";
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                            }
                            if (content == null) {
                                importErrorCount++;
                                tempStr = "����ý���Ƿ����쳣�������XMLΪ��\n" + contentNode.asXML();
                                logger.error(tempStr);
                                result.add(tempStr);
                                os.write(tempStr + "\r\n");
                                importErrorCount++;
                            }
                        }
                        //��������ϵ����ݣ��Ǿ�Ҫ�½���Ӧ����ƺ�csp��
                        if (!hasOldData) {
                            if (!checkOnly) {
                                ContentAudit contentAudit = saveContentAudit(content.getId());
                                saveContentCsp(content.getId(), cpCode, contentAudit.getId(), ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                            }
                        }
                        List channels = contentNode.selectNodes("channels/channel");
                        if (channels == null || channels.size() <= 0) {
                            tempStr = content.getName() + "ý��û��Ƶ���ڵ�!";
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
                                    tempStr = content.getName() + "Ƶ��IDû����ȷ���룺" + channelIdStr;
                                    result.add(tempStr);
                                    os.write(tempStr + "\r\n");
                                    logger.error(tempStr);
                                    continue;
                                }
                                String channelName = XmlUtils.getValue(channelNode, "@Name", "");
                                //�鿴��ǰ�û��Ƿ������Ƶ������Ȩ��
                                Channel channel = (Channel) TreeUtils.getInstance().getObject(Channel.class, channelId);
                                if (channel == null) {
                                    //Ƶ��ID���ԡ����Խ��в²⣬�ܷ��ҵ�һ��������ֻ�Ƕ������ŵ�
                                    tempStr = content.getName() + ",�޷�ͨ��ChannelID=" + channelId + "������Ƶ��������ͨ��Ƶ�������飺" + channelName + "....";
                                    channel = (Channel) CacheUtils.get(channelName, "channelByNameCache", new com.fortune.util.DataInitWorker() {
                                        public Object init(Object key, String cacheName) {
                                            Channel bean = new Channel();
                                            String searchName = key.toString();
                                            bean.setName(searchName);

                                            List<Channel> channelList = channelLogic.search(bean);
                                            for (Channel ch : channelList) {
                                                String name = ch.getName();
                                                if (name.equals(searchName) || name.startsWith(searchName + "(") ||
                                                        name.startsWith(searchName + "��")) {
                                                    return ch;
                                                }
                                            }
                                            return null;
                                        }
                                    });
                                    if (channel == null) {
                                        importErrorCount++;
                                        tempStr += "��" + content.getName() +
                                                "������ʱ���޷��ҵ�Ƶ����������ͨ��ID=" + channelId + ",����ͨ��Name=" + channelName;
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
                                    //�ж��Ƿ�ΪҶ�ӽڵ㡣
//                                if (channelLogic.isLeafChannel(channelId)) {
                                    boolean allowPublishToUnLeafChannel = config.getBoolConfig("importXml.allowPublishToUnLeafChannel", true);  //
                                    boolean isLeafChannel = channelLogic.isLeafChannel(channelId);
                                    if (isLeafChannel || ((!isLeafChannel) && allowPublishToUnLeafChannel)) {
                                        //��Ƶ����content������
                                        boolean shouldCreateContentChannel = true;
                                        //������ӰƬ�Ѿ����ˣ��Ǿ�Ҫ����Ƿ��Ѿ����������Ƶ�������û�У��ͷ���������Ѿ��������ͷ���
                                        if (hasOldData && contentChannelLogic.isExists(channelId, content.getId())) {
                                            shouldCreateContentChannel = false;
                                            tempStr = "ý�塶" + content.getName() + "���Ѿ����ڣ��Ѿ����ù�Ƶ���󶨣����ٷ�����" + channelName;
                                            //os.write(tempStr+"\r\n");
                                            result.add(tempStr);
                                            logger.debug(tempStr);
                                        }
                                        if (shouldCreateContentChannel) {
                                            if (hasOldData) {
                                                tempStr = "ý�塶" + content.getName() + "���Ѿ����ڣ����·�����Ƶ����" + channelName;
                                                logger.debug(tempStr);
                                                os.write(tempStr + "\r\n");
                                            }
                                            if (!checkOnly) {
                                                saveContentChannel(content.getId(), channelId);
                                            }
                                        }
                                    } else {
                                        importErrorCount++;
                                        tempStr = "Ƶ����" + channelName + "������Ҷ�ӽڵ㣬��������ʱ��ܾ���" + content.getName() + "������ý�嵽���Ƶ����";
                                        os.write(tempStr + "\r\n");
                                        logger.error(tempStr);
                                    }
                                } else {
                                    tempStr = "��" + content.getName() + "������ʱ��������û��Ȩ�ް󶨵�idΪ" + channelId + "����Ϊ��" + channelName + "�����Ƶ�����봴�����߹���Ա��Ƶ����Ȩ";
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
                            //û�д������ԭý��û�����ߣ����Խ������߷���
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);
                                content.setStatusTime(new Date());
                                content = save(content);
                                contentCspLogicInterface.setStatus(content.getId(), content.getCspId(), -1, ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                result.add("ý�塶" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")��û�д����Խ���ֱ�����߷�����");
                            }
                        } else {
                            if (!ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())) {
                                result.add("ý�塶" + content.getName() +
                                        "(id=" + content.getId() +
                                        ")����Ϣ����" + contentErrorCount +
                                        "����������״̬��" + getStatusString(content.getStatus()) +
                                        "�������ֹ������������ݺ����ߣ�");
                            }
                        }

                        importContentCount++;
                    }
                } else {
                    tempStr = "��ǰ�û����ܲ�����xml,xml��cpCodeӦ��Ϊ" + cspId;
                    result.add(tempStr);
                    os.write(tempStr + "\r\n");
                    importErrorCount++;
                    logger.error("��ǰ��û�����Ȩ�ޣ����ܲ�����xml");
                }
                String resultStr = "����ѡ�е�xml�ļ���ϣ��ۼ�";
                if (checkOnly) {
                    resultStr += "���";
                } else {
                    resultStr += "���";
                }
                resultStr += "ý�壺" + importContentCount + "��������" + importErrorCount + "����";
                os.write(resultStr + "\r\n");
                result.add(resultStr);
                logger.debug(resultStr);
                Date stopTime = new Date();
                os.write("-����ʱ�䣺" + StringUtils.date2string(startTime) + "\n");
                os.write("-���ʱ�䣺" + StringUtils.date2string(stopTime));
            } catch (IOException e) {
                logger.error("�޷��ҵ��ļ���" + importResultFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.error("�����쳣���޷�����ִ�е��������" + xmlFile.getAbsolutePath() + "," + e.getMessage());
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("�޷��ر��ļ���" + importResultFile.getAbsolutePath());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("����ӰƬ��Ϣ�Ƿ����쳣��" + e.getMessage());
            e.printStackTrace();
        } finally {
            threadUtils.release(resourceKey);
        }
        return null;
    }

    //��ӳɹ�Ҫ��һ��contentCsp�󶨹�ϵ
    public void saveContentCsp(Long contentId, Long cspId, long auditId, long status) {
/*      ���´�������ϲ����2013��6��13���޸ģ�����ֱ����ӡ���Ϊ��Ӳ����Ѿ���save�����������ˡ�����ֻ�����޸�״̬
        ContentCsp cc = new ContentCsp();
        cc.setCspId(cspId);
        cc.setContentId(contentId);
        cc.setStatus(2l);
        cc.setStatusTime(new Date());
        cc.setContentAuditId(auditId);
        contentCspLogicInterface.save(cc);*/
        contentCspLogicInterface.setStatus(contentId, cspId, -1, status, auditId, false);
    }

    //��contentAudit��һ������
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

    //��contentChannel���м�һ������
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
        content.setDeviceId(deviceId);//�豸��idĿǰ�̶��ǡ�����������
        content.setContentId(contentId);
        content.setActors(mediaActors);
        content.setDirectors(mediaDirector);
        content.setName(name);
        if (mediaIntro != null && mediaIntro.length() >= 2000) {
            logger.warn("����2K�����ֽ��ܣ��Ѿ������˽ضϣ�");
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
     * added by mlwang�� ����Ƶ�����˺��û�����
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
        // ���������ķ���
        return list(contentCspStatus, searchValue, pageBean, null, null);
    }

    /**
     * �����б�
     * @param contentCspStatus  ״̬
     * @param searchValue       ��ѯ����
     * @param pageBean          ��ҳ��Ϣ
     * @param channelIdList     Ƶ���б�
     * @param user              �û�
     * @return                  �����б�
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
     * added by mlwang ֻ������������������������������ˣ���xjliu
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
                //��searchValues��һ����������������һ�����ݣ�����������������Ҫ�������������
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
     * added by mlwang ��������Ƶ�����˺��û����͹���
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
        //�õ���Ŀ�Ƽ�����������û��
        List<Content> recommendResult = contentDaoInterface.recommendList(channelId);
        if (pageBean.getStartRow() == 0) {
            //˵�����Ƽ�(��ʶ��ѯ������)
            pageBean.setPageSize(pageBean.getPageSize() - recommendResult.size());
        } else {
            //������ǵ�һҳ��ȥ�Ƽ�����Ŀ
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
                //��searchValues��һ����������������һ�����ݣ�����������������Ҫ�������������
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
                 * ���ݻ�ȡ�ķ���Ҫ��ϸ���á����ǵ�Ŀ�ģ����Ȱ��Ƽ�����ʾ�ã�Ȼ������ʾƵ���ġ�
                 * 1���Ȼ�ȡ�Ƽ������ݣ�Ȼ��Է�ҳ���ݽ��е�����
                 *  1.1����Ƽ�����������Ƽ������ڻ�ȡ������ʼ��¼�Ժ󣬾ʹ���ʼ��¼��ʼ�������Ƽ����һ����
                 *  1.1.2ʣ��Ĵ���ͨ�����л�ȡ��
                 *
                 * 2����ȡƵ�����б�����
                 *  2.1.��ʼ��¼��Ҫ��ȥ�Ƽ���¼����ÿҳ��¼��Ҫ����ȡ�Ƽ���ʣ���������
                 *  2.2 ���м�¼���������Ƽ���¼����
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
                    //todo Ŀǰ����֧�ֽ��Ƽ���ý����б����޳����޳��Ļ���sql�ű��������ӣ������ܻ�Ƚϵ�
                    //
                    List<Content> tempResult = contentDaoInterface.list(
                            cspId, contentCspStatus, channelId, contentName, directors, actors, searchValues,
                            tempPageBean, channelIdList, (user==null)?-1:user.getTypeId(),recommendContentIds
                    );
                    pageBean.setRowCount(recommendResult.size()+tempPageBean.getRowCount());
                    result.addAll(tempResult);
                }else{
                    //ֻ��Ϊ�˻�ȡ��¼����
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
     * ����ר��top10
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
        long timeToLive = AppConfigurator.getInstance().getIntConfig("cache.timeToLive", 600);//Ĭ�ϱ���10����
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
                            logger.error("�޷���ȡƵ����" + e.getMessage());
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
                        //����һ�¸ý����������ڸ��ӽṹ����ȡ�ӽڵ�
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
                                            logger.debug("Ƶ��" + index + parent.getName() + "(" + parent.getId() + ")�Ƿ���Ƶ����id=" +
                                                    checkChannelId + "��������Ƶ���������Ƴ����Ƶ��");
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
                            logger.warn("û���ҵ���ص�Ƶ����" + contentId);
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
            logger.debug("������ֵΪ��" + cc);
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
            //�ڽ���ģʽ�£�property1��Ƶ��ID���������ֶ�û������ֵ����Ҫ��ContentChannel�и��ƹ���
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
     * ����content.status״̬������ContentCsp��ContentChannel�е�״̬����΢������ֱ�ӷ�����ͨ��
     * @param content ý��
     * @param channelIds Ƶ��id��������һ�������ö��ŷֺŷָ�
     * @return �ѷ�����Ƶ���б�
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
        //�Ѷ����Ƶ��������ɾ��
        if(contentChannels!=null&&contentChannels.size()>0){
            for(ContentChannel cc:contentChannels){
                contentChannelLogic.remove(cc);
            }
        }
        contentChannels = contentChannelLogic.getContentPublishedChannels(content.getId());
        for(ContentChannel cc:contentChannels){
            try {
                Channel channel = channelLogic.get(cc.getChannelId());
                //ֻҪ��һ�������󣬾Ͳ��Զ��������������״̬
                if( ! ChannelLogicInterface.AUDIT_FLAG_DO_NOT_NEED_AUDIT.equals(channel.getAuditFlag())){
                    autoPublishContent = false;
                }
                result.add(channel);
            } catch (Exception e) {
                logger.error("�޷���ʼ��Ƶ����"+e.getMessage());
                e.printStackTrace();
            }
        }
        if(AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
            //����ǽ���ģʽ����Ҫ��ý��״̬�������Ƿ�������
            Long wantToStatus=null;
            //�����Ҫ����Ƿ�Ƶ�����󣬾Ϳ��Ƿ��Զ��������ݡ�
            //                              ����Զ��������ͳ����������ߡ�
            //                              ��������Զ��������ͽ������
            //�������Ҫ����Ƿ�Ƶ�����󣬾���ԭ��������
            //
            if(willCheckAuditFlag){
                if(autoPublishContent){
                    wantToStatus = STATUS_WAIT_TO_ONLINE;
                }else{
                    wantToStatus = STATUS_WAITING_FOR_AUDIT;
                }
                logger.debug("������Ƶ���Ƿ������������Ƿ�ý�巢�����ߣ�"+getStatusString(wantToStatus));
                setContentStatus(content,wantToStatus);
            }else{
                Long status = content.getStatus();
                if(ContentLogicInterface.STATUS_CP_OFFLINE.equals(content.getStatus())){
                    logger.debug("ý���趨Ϊ����״̬�����Դ�ContentCsp�н������ߣ�");
                    contentCspLogicInterface.unPublishContent(content.getId(),content.getCspId(),-1L);
                }else if(ContentLogicInterface.STATUS_WAITING_FOR_ENCODE.equals(status)||
                        ContentLogicInterface.STATUS_ENCODING.equals(status)){
                    logger.debug("ý��״̬���趨Ϊ��" +getStatusString(status)+
                            "�����Դ�ContentCsp�н���״̬����Ϊ����Դ��ת�����������ߣ�");
                    contentCspLogicInterface.setStatus(content.getId(),content.getCspId(),-1L,ContentCspLogicInterface.STATUS_NEW);
                }else if(ContentLogicInterface.STATUS_CP_ONLINE.equals(content.getStatus())){
                    logger.debug("ý��״̬���趨Ϊ����"+
                            "�����Դ�ContentCsp�н���״̬����Ϊ���ߣ�");
                }else{
                    logger.debug("ý��״̬���趨Ϊ��" +getStatusString(status)+
                            "������Ĭ�ϵ�Լ��������У�����ʶ��״̬���ͽ�ContentCsp��״̬����Ϊ���ߣ�");
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
                logger.warn("״̬δ֪�����趨ý��״̬��"+getStatusString(status));
            }
            if(wantToStatus !=null){
                logger.debug("������ý��״̬��"+getStatusString(wantToStatus));
                setContentStatus(content,wantToStatus);
            }
        }
        return result;

    }
    /**
     * ����content.status״̬������ContentCsp��ContentChannel�е�״̬����΢������ֱ�ӷ�����ͨ��
     * @param content ý��
     * @param contentProperties ý������
     * @return �ѷ�����Ƶ���б�
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
            logger.debug("ý�塶" +content.getName()+
                    "��ת�������Ѿ���ɣ�����ִ����"+allTaskCount+"��");
/*
            if(config.getBoolConfig("encoder.whenFinished.autoPublish",true)){
            }
*/
            //setContentStatus(content,STATUS_WAIT_TO_ONLINE);
            // mod by mlwang, @2014-10-23����������Ƶ�����ã����Զ����߻��Ǵ���
            // ��һ����Ŀ�������󣬾�Ҫ��ˣ��߼��д�����

            boolean needAudit = false;
            String logs = "ת����ɺ��Զ���ý��״̬�ĵ�����";
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
                            logs+="Ƶ����" +ch.getName()+"����ý����Ҫ��ˣ����������������ý�彫���ò�������˲������ߣ�"+content.getName();
                            logger.debug(logs);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logs+="���ý����Ϣ����״̬ʱ�����쳣����"+e.getMessage();
                logger.error(logs);
                e.printStackTrace();
            }
            long wantToStatus =  needAudit? STATUS_WAITING_FOR_AUDIT : STATUS_WAIT_TO_ONLINE;
            try {
                logs+= "��"+setContentStatus(content,wantToStatus);
                logger.debug(logs);
                systemLogLogicInterface.saveMachineLog(logs);
            } catch (Exception e) {
                logger.error("��������ý��״̬����" +getStatusString(wantToStatus)+"��ʱ�����쳣��"+e.getMessage());
                e.printStackTrace();
            }

            // end of mod by mlwang
        }else if(unFinishedCount>0){
            logger.debug("ý�塶" +content.getName()+
                    "��ת�������У�"+unFinishedCount+"������"+allTaskCount+"��");
        }else{
            logger.error("��ѯý�塶" +content.getName()+
                    "��ת������δ�������ʱ��������");
        }
    }

    public String setContentStatus(Content content,long status){
        String logInfo = "";
        if (status == ContentLogicInterface.STATUS_WAIT_TO_ONLINE || status == ContentLogicInterface.STATUS_WAIT_TO_OFFLINE) {
            Csp cp = null;
            try {
                cp = cspLogicInterface.get(content.getCspId());
            } catch (Exception e) {
                logger.error("�޷���ȡCP��Ϣ��cspId="+content.getCspId());
                e.printStackTrace();
            }
            if(cp==null&&AppConfigurator.getInstance().getBoolConfig("system.compactMode",false)){
                List<Csp> allCsp = cspLogicInterface.getAll();
                if(allCsp!=null&&allCsp.size()>0){
                    cp = allCsp.get(0);
                }
            }
            if(cp==null){
                return "�޷���ȡCSP��Ϣ�������Զ�����Content״̬��cspId="+content.getCspId();
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
                    logInfo+="�������ߴ���״̬��";
                } else {
                    logInfo+="����CP��ֱ�����ߡ�";
                    content.setStatus(ContentLogicInterface.STATUS_CP_ONLINE);//����ֱ������
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
                                logInfo +="��SP��"+csp1.getName()+"��������״̬�Ѿ��ָ���";
                                List<Channel> channels = contentChannelLogic.getChannelsByContentId(content.getId());
                                if(channels!=null&&channels.size()>0){
                                    for(Channel channel:channels){
                                        logInfo+="�Ѿ�������Ƶ����"+channel.getName()+"����";
                                    }
                                    onlineContentCsp.setStatus(ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                                }else{
                                    logInfo +="û�з������κ�Ƶ������������Ϊ����״̬��";
                                    onlineContentCsp.setStatus(ContentCspLogicInterface.STATUS_ONLINE);
                                }
                                onlineContentCsp.setStatusTime(new Date());
                                onlineContentCsp.setContentAuditId(0L);
                                contentCspLogicInterface.save(onlineContentCsp);
                            }else{
                                logInfo +="��csp��"+csp1.getName()+"��������״̬��������Դ״̬���������κδ���status="+contentCspLogicInterface.getStatusString(onlineContentCsp.getStatus());
                            }
                            //contentCspLogicInterface.setStatus(content.getId(),onlineContentCsp.getCspId(),-1,ContentCspLogicInterface.STATUS_ONLINE_PUBLISHED);
                        }
                    }else{
                        logInfo +="����Դû�з������κε�CSP��";
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
                    logInfo+="�������ߴ���״̬";
                } else {
                    content.setStatus(ContentLogicInterface.STATUS_CP_OFFLINE);//����ֱ������
                    content.setStatusTime(new Date());
                    //cp����, �޸�sp��ӰƬ״̬Ϊ0, ��Ƭ
                    logInfo+="CP����ֱ�����ߣ�����SP������Դ�Ѿ��Զ����ߣ�תΪ����Դ״̬��";
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
            //�ȴ�ת�룬��ʱ���ý���Ѿ����ߣ����޸�����״̬
            content.setStatusTime(new Date());
            //cp����, �޸�sp��ӰƬ״̬Ϊ0, ��Ƭ
            logInfo+="CP���ݽ���ת�룬����SP������Դ�Զ����ߣ�תΪ����Դ״̬��";
            try {
                HibernateUtils.executeUpdate(getSession(), "update ContentCsp cc set cc.status=" +
                        ContentCspLogicInterface.STATUS_NEW+
                        " where cc.contentId=" + content.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logInfo+="��Ҫ������״̬�޷�Ԥ֪��"+getStatusString(status);
            content.setStatus(status);
            content.setStatusTime(new Date());
        }
        save(content);
        String result = ("�޸�CP��Դ״̬:��" + content.getName() + "��(" +content.getId()+
                "),�����еĲ�����" + getStatusString(status)+",�����Ľ���ǣ�" +
                getStatusString(content.getStatus())+
                "��������Ϣ��"+logInfo);
        logger.debug(result);
        //������л���
        CacheUtils.clearAll();
        return result;
    }

    /**
     * ��ѯ��������
     * @param channelId Ƶ��Id
     * @param searchValue ��ѯ����
     * @param pageBean ��ҳ��Ϣ
     * @return   dao���صĽ��
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
            String publisher = "δ֪";
            if(c.getCreatorAdminId() != null){                  
                try{
                    Admin a = adminLogic.get(c.getCreatorAdminId().intValue());
                    publisher = (a == null) ? "δ֪" : a.getRealname();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            unauditContent.setPublisher( publisher );

            // �����������ߺ�׷��������Ӧ����
            unauditContent.setAuditType(RedexAuditContent.AUDIT_TYPE_ONLINE);

            unauditContent.setChannelList(contentChannelLogic.getChannelsByContentId(c.getId()));
            unauditList.add(unauditContent);
        }

        return unauditList;
    }
    /**
     * ��ѯ��Ƶ
     * @param channelId ����Ƶ��
     * @param searchValue ��ѯ����
     * @param pageBean ��ҳ��Ϣ
     * @return   content�б�
     */
    public List<Content> getContentList(Long channelId, String searchValue, Date startTime, Date stopTime,PageBean pageBean){
        return contentDaoInterface.getContents(channelId, searchValue, startTime,stopTime,pageBean, -1);
    }

    /**
     * ��ѯ��Ƶ���ݣ��޶�����Ա���ܹ����Ƶ����Χ
     * @param channelId    Ƶ��Id
     * @param searchValue  ��ѯ����
     * @param pageBean     ��ҳ��Ϣ
     * @param admin        ����Ա
     * @return �����б�
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
     * ��ȡ����Ա�������쳣����
     * @param id   ����ԱId
     * @return     �쳣�����б�
     */
    @SuppressWarnings("unchecked")
    public List<ContentAbnormal> getAbnormalContentList(Integer id){
        List abnormalList = new ArrayList();

        long taskId, clipId, contentId;
        int status;
        String title;
        // ת��ʧ�ܵ�
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
                    c.setMessage("δ֪����");
                    switch(status){
                        case 4: case 804:
                            c.setMessage("ִ�д���");break;
                        case 805:
                            c.setMessage("�ļ�������");break;
                        case 405:
                            c.setMessage("Դ�ļ�̫С");break;
                        case 500:
                            c.setMessage("��ʽ����ʶ");break;
                        case 501:
                            c.setMessage("��ʼʱ���쳣");break;
                        case 502:
                            c.setMessage("�ļ���ȱ");break;
                        case 504:
                            c.setMessage("����ļ�����");break;
                        case 505:
                            c.setMessage("δ֪����");break;
                        case 506:
                            c.setMessage("�����д���");break;
                        case 507:
                            c.setMessage("����ļ�ʱ�䳤�ȴ���");break;
                        case 508:
                            c.setMessage("�����쳣");break;
                        case 509:
                            c.setMessage("����IO�쳣");break;
                        case 801:
                            c.setMessage("ӰƬԴ��ʧ");break;
                        case 802:
                            c.setMessage("�ظ�������");break;
                        case 806:
                            c.setMessage("��ȡ��");break;
                    }

                    abnormalList.add(c);
                }
            }
        }

        // ��ȡ���ʧ������
        List<Content> contentList = contentDaoInterface.getRejectContents(id);
        if( contentList != null ){
            for(Content c: contentList){
                ContentAbnormal a = new ContentAbnormal();
                a.setType(ContentAbnormal.ABNORMAL_CONTENT_TYPE_AUDIT_FAILED);
                a.setId(c.getId());
                a.setTaskId(-1);
                a.setTitle(c.getName());
                a.setMessage("���δͨ��");
                abnormalList.add(a);
            }
        }

        return abnormalList;
    }

    /**
     * ��ȡ���������б�
     * @param channelIdList �û����Թۿ�����Ŀ�б�
     * @param userType      �û�����
     * @param count         ��ȡ������
     * @return ContentDTO�����б�
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
        //objList�а���contentId�ͷ��ʴ���������id��ȡcontent
        for(Content content : contentList){
            contentDTOList.add(new ContentDTO(content));
        }

        return contentDTOList;
    }

    /**
     * ��ѯ���Ƶ������
     * @param channelId     ��ѯ��Ƶ��Id��<0ʱ����
     * @param searchWord    ��ѯ�ؼ���
     * @param channelIdList �û����Թۿ�����ĿId�б��б�Ϊ�ջ�û������ʱ����
     * @param userType       �û�����
     * @param pageBean       ��ҳ��Ϣ
     * @return  ContentDTO�����б�
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
            // �ṹ���ɶ���
            Integer id = ((Integer)objects[0]);
            if(id!=null){
                Content content = getCachedContent(id.longValue());
                tidyDTOList.add(new ContentTidyDTO(content));
            }
/*
            ContentTidyDTO dto = new ContentTidyDTO();
            dto.setId( StringUtils.string2long(objects[0].toString(), 0));  // id
            dto.setTitle( objects[1].toString() );  // ����
            dto.setPoster( objects[2].toString() ); // С����
            dto.setActor( objects[3].toString() );  // ����
            dto.setCreateTime( StringUtils.string2date(objects[4].toString())); // ����ʱ��
            dto.setRecommended( objects[5] != null );    // �Ƿ��Ƽ�

            tidyDTOList.add(dto);
*/
        }

        return tidyDTOList;
    }

    /**
     * ֱ���б��߼���getRedexContents��ͬ��ֻ��Ҫ��ѯ����Ϊֱ���ļ���
     * @param channelId     ��ѯ��Ƶ��Id��<0ʱ����
     * @param searchWord    ��ѯ�ؼ���
     * @param channelIdList �û����Թۿ�����ĿId�б��б�Ϊ�ջ�û������ʱ����
     * @param userType       �û�����
     * @param pageBean       ��ҳ��Ϣ
     * @return  ContentDTO�����б�
     */
    public List<ContentTidyDTO> getLivingList(Long channelId, String searchWord, List<Long> channelIdList, Long userType, PageBean pageBean){
        String channelIdString = "";
        if(channelIdList != null && channelIdList.size() > 0 ){
            for(Long id : channelIdList){
                channelIdString += channelIdString.isEmpty()? id : ","+id;
            }
        }

        List<Object[]> contentList = contentDaoInterface.getRedexContents(channelId, searchWord, channelIdString, userType, pageBean, 1/*ֱ������*/);

        if(contentList == null) return null;

        List<ContentTidyDTO> tidyDTOList = new ArrayList<ContentTidyDTO>();
        for(Object[] objects : contentList){
            // �ṹ���ɶ���
            ContentTidyDTO dto = new ContentTidyDTO();
            dto.setId( StringUtils.string2long(objects[0].toString(), 0));  // id
            dto.setTitle( objects[1].toString() );  // ����
            dto.setPoster( objects[2].toString() ); // С����
            dto.setActor( objects[3].toString() );  // ����
            dto.setCreateTime( StringUtils.string2date(objects[4].toString())); // ����ʱ��
            dto.setRecommended( objects[5] != null );    // �Ƿ��Ƽ�

            tidyDTOList.add(dto);
        }

        return tidyDTOList;
    }

    /**
     * ��ѯcontent��Ӧ�������Ƶ
     * @param content   content
     * @param priority  ��ѯ���ȶȣ���ʵ��Ψһ�������
     * @param channelIdList ������Ƶ����Χ
     * @param userType �û�����
     * @return tidyDto�б�
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
        // תΪContentTidyDto���󣬲��������ƶ�
        List<ContentTidyDTO> tidyDTOList = new ArrayList<ContentTidyDTO>();
        for(Object[] objects : contentList){
            // �ṹ���ɶ���
            long id = StringUtils.string2long(objects[0].toString(), 0);
            if( id == content.getId()) continue;
            Content c=getCachedContent(id);
            ContentTidyDTO dto = new ContentTidyDTO(c);
            dto.setRecommended( objects[5] != null );    // �Ƿ��Ƽ�

            // �������ƶ�
            dto.setSimilarity(contentSimilarity(dto, content));

            tidyDTOList.add(dto);
        }

        // ��װ���ƶ���������
        Collections.sort(tidyDTOList,new ComparatorContentTidyDTO());
        // ����ǰ14��
        if(tidyDTOList.size() > 21){
            tidyDTOList = tidyDTOList.subList(0, 21);
        }

        //
        // ����Ƶ��������Ŀ�Ž�ȥ
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

        // Ĭ�ϱ������ƶ�Ȩ��Ϊ 3�� ����Ϊ1
        double titleSimilar = SimilarityUtils.getSimilarity(c1.getTitle(), c2.getName());
        double actorSimilar = SimilarityUtils.getSimilarity(c1.getTitle(), c2.getActors());

        return titleSimilar * ContentTidyDTO.POWER_TITLE + actorSimilar * ContentTidyDTO.POWER_ACTOR;
    }

    public class ComparatorContentTidyDTO implements Comparator {
        public int compare(Object arg0, Object arg1) {
            ContentTidyDTO dto0 = (ContentTidyDTO) arg0;
            ContentTidyDTO dto1 = (ContentTidyDTO) arg1;

            //����grade����
            return dto1.getSimilarity().compareTo(dto0.getSimilarity());
        }

    }

    /**
     * ��ȡǰ̨�㲥��Ƶ����
     * @param contentId ��ƵId
     * @param channelId ���ĸ���Ŀ������
     * @return ��������ղص���Ϣ
     */
    public ContentDetailDTO getContentDetail(Long contentId, Long channelId){
        Content content = contentDaoInterface.get(contentId);
        if( content == null) return null;

        ContentDetailDTO contentDetail = new ContentDetailDTO(content);
        // ��ȡ��Ŀ·��
        List<Channel> channelList = contentChannelLogic.getChannelsByContentId(contentDetail.getId());
        // ���channelId�Ƿ���Ч�������Ч��ȡ����Ŀ·��������ȡ��һ��
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

        // ��ȡѡ������
        contentDetail.setEpisodeList(contentPropertyLogicInterface.getContentEpisodes(contentDetail.getId()));
        // �ղش���
        contentDetail.setFavorite(userFavoritesLogicInterface.redexGetContentFavoriteCount(contentDetail.getId()));
        // ���Ĵ���/�ȵĴ��� >=3 �ļ�Ϊ��
        contentDetail.setLike(userScoringLogicInterface.redexGetScoreRangeCount(contentDetail.getId(), 3, 9999));
        contentDetail.setDislike(userScoringLogicInterface.redexGetScoreRangeCount(contentDetail.getId(), 0, 2));

        // added by mlwang @2015-4-23�������������ͣ�ֱ��/�㲥
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
        pageBean.setPageSize(2);//ֻ���ȡ���µ�����ý��
        Map<String,List<Content>> map = new HashMap<String, List<Content>>();
        String[] channelIds = ids.split(",");
        for(String channelId : channelIds) {
            List<Content> contents = this.getContentsByChannelId(Long.valueOf(channelId),pageBean);
            map.put(channelId,contents);
        }

        return map;
    }

}


