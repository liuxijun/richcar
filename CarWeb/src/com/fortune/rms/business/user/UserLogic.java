package com.fortune.rms.business.user;

import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.rms.business.system.logic.logicInterface.IpRangeLogicInterface;
import com.fortune.rms.business.system.model.IpRange;
import com.uspsfot.iii.bssp.generated.BSSPExtendServiceImplServiceLocator;
import com.uspsfot.iii.bssp.generated.BSSPExtendServiceImplSoapBindingStub;
import com.uspsfot.iii.bssp.generated.QueryUserProductInfoRequest;
import com.uspsfot.iii.bssp.generated.QueryUserProductInfoResponse;
import com.fortune.util.*;
import com.huawei.itellin.spapi.service.BSSPStringBuffer;
import com.huawei.itellin.spapi.service.InteriorTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-8
 * Time: 18:51:29
 */
public class UserLogic {

    private HttpServletRequest request;
    private IpRangeLogicInterface ipRangeLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private ContentPropertyLogicInterface contentPropertyLogicInterface;
    private Logger logger = Logger.getLogger(getClass());

    public UserLogic(HttpServletRequest request) {
        this.request = request;
        try {
            ipRangeLogicInterface = (IpRangeLogicInterface) SpringUtils.getBean("ipRangeLogicInterface");
            contentLogicInterface = (ContentLogicInterface) SpringUtils.getBean("contentLogicInterface");
            contentPropertyLogicInterface = (ContentPropertyLogicInterface) SpringUtils.getBean("contentPropertyLogicInterface");
        } catch (Exception e) {
            logger.error(e);
        }
    }


    @SuppressWarnings("unused")
    public void setIpRangeLogicInterface(IpRangeLogicInterface ipRangeLogicInterface) {
        this.ipRangeLogicInterface = ipRangeLogicInterface;
    }

    @SuppressWarnings("unchecked")
    public List<IpRange> getCachedIpRanges() {
        return (List<IpRange>) CacheUtils.get("ipRangeAll", "ipRange", new DataInitWorker() {
            public Object init(Object keyId, String cacheName) {
                return ipRangeLogicInterface.search(new IpRange(),
                        new PageBean(0, 100000, "o1.ipFrom", "asc"));
            }
        });
    }
//检测河北的IP段
public boolean checkIp_hb(String ip) {
    boolean is=false;
    try {
        List<IpRange> list = ipRangeLogicInterface.getAll();
        if (list != null && list.size() != 0) {
            long longIp = StringUtils.string2IpLong(ip);
            is= this.isValidIps(longIp, list, 0, list.size() - 1);
            return is;
        }
    } catch (Exception e) {
        e.printStackTrace();
        logger.error(e);
    }
    return is;
}
    public boolean checkFree(long contentId, long spId, long contentPropertyId) {
        CacheLogic cacheLogic = new CacheLogic(request);
        Property property = contentLogicInterface.getPropertyByCode("MEDIA_TELEPLAY_FREESET");
        if (property != null) {
            int freeCount = contentPropertyLogicInterface.getContentFreeCountByCache(contentId, property.getId());
            long intValue = contentPropertyLogicInterface.getContentIntValueByCache(contentPropertyId);
            if (freeCount != -1 && intValue != -1l && freeCount >= intValue) {
                return true;
            }

        }

        List list1 = cacheLogic.getServiceProduct(contentId, spId);
        if (list1 != null && list1.size() > 0) {
            for (Object aList1 : list1) {
                ServiceProduct serviceProduct = (ServiceProduct) aList1;
                if (serviceProduct != null
                        && serviceProduct.getIsFree() != null
                        && serviceProduct.getFreeStartTime() != null
                        && serviceProduct.getFreeEndTime() != null
                        && serviceProduct.getIsFree() == 1
                        && serviceProduct.getFreeStartTime().getTime() < System.currentTimeMillis()
                        && serviceProduct.getFreeEndTime().getTime() > System.currentTimeMillis()
                        ) {
                    return true;
                }

            }
        } else {
            return true;
        }
        return false;
    }

    public boolean checkAccount(String userId, long contentId, long contentPropertyId, long spId) {
        try {
            CacheLogic cacheLogic = new CacheLogic(request);
            List list1 = cacheLogic.getServiceProduct(contentId, spId);
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(currentTime);
            String sql = "";
            if (list1 != null && list1.size() > 0) {
                for (Object aList1 : list1) {
                    ServiceProduct serviceProduct = (ServiceProduct) aList1;
                    if (serviceProduct.getType() != null && serviceProduct.getType() == 1) {
                        //包月
                        sql += " (ub.user_id='" + userId + "' and ub.service_Product_Id=" + serviceProduct.getId() + " and ub.start_time<to_date('" + time + "','yyyy-mm-dd hh24:mi:ss') and ub.end_time>to_date('" + time + "','yyyy-mm-dd hh24:mi:ss') and ub.content_Id=" + contentId + " and ub.content_property_id=" + contentPropertyId + " and ub.sp_id=" + spId + ") or ";
                    }
                    if (serviceProduct.getType() != null && serviceProduct.getType() == 2) {
                        //按次购买
                        sql += " (ub.user_id='" + userId + "' and ub.service_Product_Id=" + serviceProduct.getId() + " and ub.start_time<to_date('" + time + "','yyyy-mm-dd hh24:mi:ss') and ub.end_time>to_date('" + time + "','yyyy-mm-dd hh24:mi:ss') and ub.content_Id=" + contentId + " and ub.content_property_id=" + contentPropertyId + " and ub.sp_id=" + spId + ") or ";
                    }

                }
            }

            if (!"".equals(sql)) {
                sql = sql.substring(0, sql.length() - 4);
                sql = "select count(*) from user_buy ub where " + sql;
                HibernateUtils hibernateUtils = new HibernateUtils(request);
                int count = hibernateUtils.sqlFindCount(sql);
                if (count > 0) {
                    return true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean checkCcit(String userId, long contentId, long spId, String platformId, String SP_PRIVATE_KEY, String bsspExtendServiceAddress, String productIdss) {
        try {
            CacheLogic cacheLogic = new CacheLogic(request);
            List list1 = cacheLogic.getServiceProduct(contentId, spId);
            if (list1 != null && list1.size() > 0) {
                for (int i = 0; i < list1.size(); i++) {

                    ServiceProduct serviceProduct = (ServiceProduct) list1.get(i);
                    long serviceProductId = serviceProduct.getId();
                    if (serviceProduct != null && serviceProduct.getType() != null && serviceProduct.getType() == 1) {//包月
                        Product product = cacheLogic.getProduct(serviceProductId);
                        if (product != null && product.getPayProductNo() != null) {
                          String payproductNo = product.getPayProductNo();
                           // String payproductNo = productIdss;
                            String timeStamp = InteriorTools.generateTimeStamp();

                            BSSPStringBuffer reqSignStringBuffer = new BSSPStringBuffer();
                            reqSignStringBuffer.append(platformId);
                            reqSignStringBuffer.append(timeStamp);
                            reqSignStringBuffer.append(userId);
                            reqSignStringBuffer.append(payproductNo);
                            reqSignStringBuffer.append("");
                            reqSignStringBuffer.append("");
                            String reqSignString = reqSignStringBuffer.toString();
                            String reqSign = InteriorTools.generateSign(reqSignString, SP_PRIVATE_KEY);

                            QueryUserProductInfoRequest userProductInfo = new QueryUserProductInfoRequest();
                            userProductInfo.setSpID(platformId);
                            userProductInfo.setTimeStamp(timeStamp);
                            userProductInfo.setPseudoID(userId);
                            userProductInfo.setProductID(payproductNo);
                            userProductInfo.setSpSignData(reqSign);
                            userProductInfo.setSpUserID("");
                            userProductInfo.setRemark("");

                            BSSPExtendServiceImplServiceLocator locator = new BSSPExtendServiceImplServiceLocator();
                            BSSPExtendServiceImplSoapBindingStub _stub = new BSSPExtendServiceImplSoapBindingStub(new URL(bsspExtendServiceAddress), locator);
                            QueryUserProductInfoResponse queryUserProduct = _stub.queryUserProductInfo(userProductInfo);

                            int result = queryUserProduct.getResult();
                            //System.out.print(queryUserProduct.getResult());
                            if (result == 1) {  //购买过该产品
                                return true;
                            }
                        }
                        //加入 被赠送 的主产品
                        ServiceProductCache spc = cacheLogic.getServiceProductCache(serviceProductId);
                        list1.addAll(spc.masterServiceProduct);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return false;

    }

    public void saveUserBuy(long spId, long channelId, long contentId, long contentPropertyId, long serviceProductId, double price, String userId, String userIP) {
        try {
            CacheLogic cacheLogic = new CacheLogic(request);
            HibernateUtils hibernateUtils = new HibernateUtils(request);

            ServiceProductCache spc = cacheLogic.getServiceProductCache(serviceProductId);
            ServiceProduct sp = spc.serviceProduct;

            Calendar calendar = Calendar.getInstance();
            Date startTime = calendar.getTime();

            int validDays = 0;

            if (sp.getType() == 1) {
                calendar.add(Calendar.MONTH, 1);
            } else {
                if (sp.getValidLength() != null) {
                    validDays = sp.getValidLength().intValue();
                    if (sp.getLengthUnit() == 2) {
                        validDays = sp.getValidLength().intValue() / 24;
                        if (validDays < 1) {
                            calendar.add(Calendar.DATE, 1);
                        } else {
                            calendar.add(Calendar.DATE, validDays);
                        }
                    } else {
                        calendar.add(Calendar.DATE, validDays);
                    }
                } else {
                    calendar.add(Calendar.DATE, 1);
                }
            }
            Date endTime = calendar.getTime();

            UserBuy ub = new UserBuy();
            ub.setSpId(spId);
            ub.setChannelId(channelId);
            ub.setContentId(contentId);
            ub.setContentPropertyId(contentPropertyId);
            ub.setUserId(userId);
            ub.setUserIp(userIP);
            ub.setServiceProductId(serviceProductId);
            ub.setPrice(price);
            ub.setIsGift(0l);
            ub.setBuyTime(startTime);
            ub.setStartTime(startTime);
            ub.setEndTime(endTime);
            hibernateUtils.save(ub);

            List list1 = spc.giftServiceProduct;
            if (list1 != null && list1.size() > 0) {
                for (Object aList1 : list1) {
                    ServiceProduct serviceProduct = (ServiceProduct) aList1;
                    ub = new UserBuy();
                    ub.setSpId(spId);
                    ub.setChannelId(channelId);
                    ub.setContentId(contentId);
                    ub.setContentPropertyId(contentPropertyId);
                    ub.setUserId(userId);
                    ub.setUserIp(userIP);
                    ub.setServiceProductId(serviceProduct.getId());
                    ub.setPrice(0.0);
                    ub.setIsGift(1l);
                    ub.setBuyTime(startTime);
                    ub.setStartTime(startTime);
                    ub.setEndTime(endTime);
                    hibernateUtils.save(ub);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //截取用户ID
    public String getSubString(String parameter, String regex, int index) {
        String userId = null;
        if (parameter != null && !parameter.trim().isEmpty()) {
            //此处的regex为"|"，所以需要转译或者加上【】
            String[] userMessage = parameter.split("[" + regex + "]");
            userId = userMessage[index - 1];
        }
        return userId;
    }


    //验证IP是否合法

    public boolean checkIp(String ip) {
        try {
            List<IpRange> list = getCachedIpRanges();
            if (list != null && list.size() != 0) {
                long longIp = StringUtils.string2IpLong(ip);
                return this.isValidIp(longIp, list, 0, list.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return false;
    }

    //判断ip是否在这范围内
    public boolean isValidIps(long longIp, List<IpRange> list, int fromIndex, int toIndex) {
        boolean result=false;
        for(int i=0;i<list.size();i++){
            IpRange ir = list.get(i);
            if ((ir.getIpFrom() < longIp && longIp < ir.getIpTo()) ||
                    ir.getIpFrom() == longIp || ir.getIpTo() == longIp) {
                result = true;
                return result;
            }
        }
        return result;
    }
    public boolean isValidIp(long longIp, List<IpRange> list, int fromIndex, int toIndex) {
        boolean result=false;
        int lastIndexId=-1 ;
        while (true) {
            int stdev = toIndex - fromIndex;
            int listIndex = fromIndex + (stdev / 2);
            if(listIndex==lastIndexId){
                break;
            }
            IpRange ir = list.get(listIndex);
            if ((ir.getIpFrom() < longIp && longIp < ir.getIpTo()) ||
                    ir.getIpFrom() == longIp || ir.getIpTo() == longIp) {
                result = true;
                break;
            } else {
                if (longIp < ir.getIpFrom()) {
                    toIndex = listIndex;
                } else {
                    fromIndex = listIndex;
                }
                if(fromIndex==toIndex){
                   // break;
                }
            }
            lastIndexId = listIndex;
        }
        return result;
    }

}
