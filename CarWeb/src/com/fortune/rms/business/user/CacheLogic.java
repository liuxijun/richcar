package com.fortune.rms.business.user;

import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.rms.business.system.model.Device;
import com.fortune.rms.business.system.model.IpRange;
import com.fortune.util.CacheUtils;
import com.fortune.util.DataInitWorker;
import com.fortune.util.HibernateUtils;
import com.fortune.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-8
 * Time: 19:00:50
 * 缓存处理类
 */
public class CacheLogic {

    private HttpServletRequest request;

    public CacheLogic(HttpServletRequest request) {
        this.request = request;
    }

/*
    public static Cache contents;
    public static Cache serviceProducts;
    public static Cache ipRanges;

    static {
        //从配置文件读取缓存的参数
        int cacheSize = Config.getIntValue("memory-cached.max-size", 5000000);
        int cacheTime = Config.getIntValue("memory-cached.validtime", 60) * 1000;
        //todo
        contents = new Cache(cacheSize, cacheTime);
        serviceProducts = new Cache(cacheSize, cacheTime);
        ipRanges = new Cache(cacheSize, cacheTime);
    }
*/

    public ContentCache getContentCache(long contentId) {
        return (ContentCache) CacheUtils.get(contentId, "userContentCache", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                ContentCache contentCache = null;
                Long contentId = StringUtils.string2long(key.toString(), -1);
                try {
                    HibernateUtils hibernateUtils = new HibernateUtils(request);

                    Content content = (Content) hibernateUtils.findByPK(Content.class, contentId);

                    Device device = null;
                    if (content != null && content.getDeviceId() != null) {
                        device = (Device) hibernateUtils.findByPK(Device.class, content.getDeviceId());
                    }

                    List list1 = hibernateUtils.findAll("select distinct sp from ServiceProduct sp,ContentServiceProduct csp where sp.id=csp.serviceProductId and csp.contentId=" + contentId);
                    HashMap<Long, List<ServiceProduct>> hm1 = new HashMap<Long, List<ServiceProduct>>();

                    for (Object obj : list1) {
                        ServiceProduct serviceProduct = (ServiceProduct) obj;
                        Long cspId = serviceProduct.getCspId();
                        if (hm1.get(cspId) == null) {
                            hm1.put(cspId, new ArrayList<ServiceProduct>());
                        }
                        List<ServiceProduct> list2 = hm1.get(cspId);
                        list2.add(serviceProduct);
                    }

                    HashMap<Long, ContentProperty> hm2 = new HashMap<Long, ContentProperty>();
                    list1 = hibernateUtils.findAll("select cp from ContentProperty cp where cp.contentId=" + contentId);
                    for (Object cp : list1) {
                        ContentProperty contentProperty = (ContentProperty) cp;
                        hm2.put(contentProperty.getId(), contentProperty);
                    }

                    contentCache = new ContentCache();
                    contentCache.content = content;
                    contentCache.device = device;
                    contentCache.servicePropertyMap = hm1;
                    contentCache.contentPropertyMap = hm2;
//                    contents.add(contentId,contentCache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return contentCache;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public ServiceProductCache getServiceProductCache(long serviceProductId) {
        return (ServiceProductCache) CacheUtils.get(serviceProductId, "serviceProductCache", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                ServiceProductCache serviceProductCache = null;
                Long serviceProductId = StringUtils.string2long(key.toString(), -1);
                try {
                    HibernateUtils hibernateUtils = new HibernateUtils(request);
                    ServiceProduct serviceProduct = (ServiceProduct) hibernateUtils.findByPK(ServiceProduct.class, serviceProductId);
                    Product product = null;
                    if (serviceProduct != null && serviceProduct.getProductId() != null) {
                        product = (Product) hibernateUtils.findByPK(Product.class, serviceProduct.getProductId());
                    }
                    List<ServiceProduct> masterServiceProducts = hibernateUtils.findAll("select distinct sp from ServiceProduct sp,ServiceProductGift spg where sp.id=spg.serviceProductId and spg.giftServiceProductId=? and spg.startTime<? and spg.endTime>?", new Object[]{serviceProductId, new Date(), new Date()});
                    List<ServiceProduct> giftServiceProducts = hibernateUtils.findAll("select distinct sp from ServiceProduct sp,ServiceProductGift spg where sp.id=spg.giftServiceProductId and spg.serviceProductId=? and spg.startTime<? and spg.endTime>?", new Object[]{serviceProductId, new Date(), new Date()});
                    serviceProductCache = new ServiceProductCache();
                    serviceProductCache.serviceProduct = serviceProduct;
                    serviceProductCache.product = product;
                    serviceProductCache.masterServiceProduct = masterServiceProducts;
                    serviceProductCache.giftServiceProduct = giftServiceProducts;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return serviceProductCache;
            }
        });
    }
    public IpRangeCache getAllIpRanges(){
        return (IpRangeCache)  CacheUtils.get("ipRangeAll", "IpRangeCache", new DataInitWorker() {
            public Object init(Object key, String cacheName) {
                IpRangeCache cachedIpRange = null;
                try {
                    HibernateUtils hibernateUtils = new HibernateUtils(request);
                    List list1 = hibernateUtils.findAll("select ir from IpRange ir order by ir.ipFrom");

                    cachedIpRange = new IpRangeCache();
                    cachedIpRange.ipRanges = list1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return cachedIpRange;
            }});
    }

    public long getAreaId(String ip) {
        try {
            long lip;
            if (ip == null || "".equals(ip)) {
                return -1;
            } else {
                lip = StringUtils.string2IpLong(ip);
            }
            IpRangeCache ipc = getAllIpRanges();
            if (ipc != null) {
                List irs = ipc.ipRanges;
                if (irs != null) {
                    for (Object obj : irs) {
                        IpRange ir = (IpRange) obj;
                        if (ir.getIpFrom() != null && ir.getIpTo() != null
                                && ir.getIpFrom() <= lip && ir.getIpTo() >= lip
                                ) {
                            return ir.getAreaId();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Content getContent(long contentId) {
        ContentCache contentCache = getContentCache(contentId);
        if (contentCache != null) {
            return contentCache.content;
        }
        return null;
    }

    @SuppressWarnings("unused")
    public ContentProperty getContentProperty(long contentId, long contentPropertyId) {
        ContentCache contentCache = getContentCache(contentId);
        if (contentCache != null) {
            HashMap hm = contentCache.contentPropertyMap;
            if (hm != null) {
                return (ContentProperty) hm.get(contentPropertyId);
            }
        }
        return null;
    }

    public String getMediaUrl(long contentId, long contentPropertyId) {
        ContentCache contentCache = getContentCache(contentId);
        if (contentCache != null ) {
            Device device = contentCache.device;
            if(device != null){
                HashMap hm = contentCache.contentPropertyMap;
                if (hm != null) {
                    ContentProperty contentProperty = (ContentProperty) hm.get(contentPropertyId);
                    if (contentProperty != null) {
                        return device.getUrl() + contentProperty.getStringValue();
                    }
                }
            }
        }
        return "";
    }

    public List<ServiceProduct> getServiceProduct(long contentId, long spId) {
        ContentCache contentCache = getContentCache(contentId);
        if (contentCache != null) {
            HashMap<Long,List<ServiceProduct>> hm = contentCache.servicePropertyMap;
            if (hm != null) {
                return hm.get(spId);
            }
        }
        return null;
    }

    public List<ServiceProduct> getAllServiceProduct(long contentId, long spId) {
        List<ServiceProduct> result = new ArrayList<ServiceProduct>();
        List list1 = getServiceProduct(contentId, spId);
        if (list1 != null) {
            for (Object obj : list1) {
                ServiceProduct sp = (ServiceProduct) obj;
                if (sp != null) {
                    ServiceProductCache spc = getServiceProductCache(sp.getId());
                    result.addAll(spc.masterServiceProduct);
                    result.add(sp);
                }
            }
        }
        return result;
    }


    public Product getProduct(long serviceProductId) {
        ServiceProductCache serviceProductCache = getServiceProductCache(serviceProductId);
        if (serviceProductCache != null) {
            return serviceProductCache.product;
        }
        return null;
    }
    public ServiceProduct getServiceProduct(long serviceProductId) {
        ServiceProductCache serviceProductCache = getServiceProductCache(serviceProductId);
        if (serviceProductCache != null) {
            return serviceProductCache.serviceProduct;
        }
        return null;
    }

    public Csp getCsp(long cspId) {
         HibernateUtils hibernateUtils = new HibernateUtils(request);
         Csp csp = null;
        try {
            csp = (Csp)hibernateUtils.findByPK(Csp.class,cspId);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
       return csp;
    }
}
