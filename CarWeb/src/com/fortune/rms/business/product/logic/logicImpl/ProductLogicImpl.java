package com.fortune.rms.business.product.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.common.business.base.model.Config;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.product.dao.daoInterface.ProductDaoInterface;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.util.AppConfigurator;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import com.fortune.vac.Command;
import com.fortune.vac.VacWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("productLogicInterface")
public class ProductLogicImpl extends BaseLogicImpl<Product>
        implements
        ProductLogicInterface {
    private ProductDaoInterface productDaoInterface;
    private ContentLogicInterface contentLogicInterface;
    private CspLogicInterface cspLogicInterface;

    /**
     * @param productDaoInterface the productDaoInterface to set
     */
    @Autowired
    public void setProductDaoInterface(ProductDaoInterface productDaoInterface) {
        this.productDaoInterface = productDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.productDaoInterface;
    }

    @Autowired
    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public List<Product> getAllProductOfStatus(Product product, PageBean pageBean) {
        String hql = "status=1";
        return search(product, pageBean, hql);
    }

    public List<Product> getAllProduct(Product product, PageBean pageBean) {
        String hqlStr = "";
        if (product.getName() != null) {
            hqlStr = "name like '%" + product.getName().trim() + "%'";
        }
        return search(product, pageBean, hqlStr);
    }

    public List<Product> getProductType(Long type, PageBean pageBean) {
        return productDaoInterface.getProductType(type, pageBean);
    }

    public List<Product> getProductsOfStatus(int status) {
        return productDaoInterface.getProductsOfStatus(status);
    }

    public Map<String, Object> getProductMap(Object[] o) {
        ConfigManager configurator = ConfigManager.getInstance();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("serviceProductId", Long.valueOf(o[0].toString()));
        map.put("serviceProductName", o[1].toString());
        map.put("cspId", Long.valueOf(o[2].toString()));
        Integer typeId = StringUtils.string2int(o[3].toString(), -1);
        map.put("typeId", typeId);
        if (ProductLogicInterface.TYPE_FOR_MONTH.equals(typeId.longValue())) {
            map.put("type", configurator.getConfig("system.buy.monthTypeDescription", "包月,周期为自然月"));
        } else {
            map.put("type", configurator.getConfig("system.buy.onceTypeDescription", "按次，自订购时间起24小时内有效"));
        }
        map.put("payProductNo", o[4].toString());
        if (o[5] != null) {
            map.put("mobileProduct", Integer.valueOf(o[5].toString()));
        } else {
            map.put("mobileProduct", -1);
        }
        map.put("price", Double.valueOf(o[6].toString()));
        if (o[7] != null) {
            map.put("description", o[7]);
        } else {
            map.put("description", "暂无说明");
        }
        if (o.length > 8 && o[8] != null) {
            map.put("status", StringUtils.string2int(o[8].toString(), STATUS_OK));
        } else {
            map.put("status", STATUS_OK);
        }

        if (o.length > 9 && o[9] != null) {
            map.put("costType", o[9]);
        } else {
            map.put("costType", -1);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<Map> getProductsOfCanBeBuy(long contentId) {
        Content c = contentLogicInterface.get(contentId);
        if (c != null) {
            List<Map> maps = new ArrayList<Map>();
            List<Object[]> contentProducts = productDaoInterface.getProducts(contentId);
            if (contentProducts != null) {
                for (Object aList : contentProducts) {
                    try {
                        Object[] o = (Object[]) aList;
                        Float price = Float.parseFloat(o[6].toString());
                        if (price <= 0.0f) {//价格为0的不能订购
                            continue;
                        }
                        Long status = StringUtils.string2long(o[8].toString(), STATUS_OK);
                        if (STATUS_ONLY_SUBSCRIBE == status || STATUS_OK == status) {
                            String payProductNo = o[4].toString(); //根据SQL 获取产品的支付ID
                            if (!"8018000401".equals(payProductNo) && !"8018000201".equals(payProductNo) && !"8018000501".equals(payProductNo)) {  //手机端排除2元产品包，零时办法不是长久之计
                                maps.add(getProductMap(o));
                            }
                        }
                    } catch (Exception e) {
                        logger.error("准备格式化产品时发生异常：" + e.getMessage());
                    }
                }
            }
            return maps;
        }
        return null;
    }

    public List<Map> getProductsByContentId_Pc(long contentId) {
        Content c = contentLogicInterface.get(contentId);
        if (c != null) {
            List<Map> maps = new ArrayList<Map>();
            List<Object[]> contentProducts = productDaoInterface.getProducts(contentId);
            if (contentProducts != null) {
                for (Object aList : contentProducts) {
                    try {
                        Object[] o = (Object[]) aList;
                        Float price = Float.parseFloat(o[6].toString());
                        if (price <= 0.0f) {//价格为0的不能订购
                            continue;
                        }
                        Long status = StringUtils.string2long(o[8].toString(), STATUS_OK);
                        if (STATUS_ONLY_SUBSCRIBE == status || STATUS_OK == status) {
                             maps.add(getProductMap(o));
                        }
                    } catch (Exception e) {
                        logger.error("准备格式化产品时发生异常：" + e.getMessage());
                    }
                }
            }
            return maps;
        }
        return null;
    }


    //得到用户可以购买的商品信息
    public List<Map<String, Object>> getMobileProducts() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List list = productDaoInterface.getMobileProducts();
        if (list != null && list.size() > 0) {
            for (Object aList : list) {
                Object[] o = (Object[]) aList;
                String payProductNo = o[4].toString(); //根据SQL 获取产品的支付ID
                if (!"8018000401".equals(payProductNo) && !"8018000201".equals(payProductNo) && !"8018000501".equals(payProductNo)) {  //手机端排除2元产品包，零时办法不是长久之计
                    maps.add(getProductMap(o));
                }
            }
        }
        return maps;
    }

    public List<Map<String, Object>> getMobileProducts_Pc() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List list = productDaoInterface.getMobileProducts();
        if (list != null && list.size() > 0) {
            for (Object aList : list) {
                Object[] o = (Object[]) aList;
                maps.add(getProductMap(o));
            }
        }
        return maps;
    }

    /**
     * 用户购买产品情况，返回所有产品列表，并包含用户是否已经购买
     *
     * @param userTelephone          用户号码
     * @param fixedDescriptionLength 固定的描述长度
     * @return 产品列表
     */
    public List<Map<String, Object>> getUserMobileProducts(String userTelephone, int fixedDescriptionLength) {
        List<Map<String, Object>> products = getMobileProducts();
        //Long operationType=6l;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : products) {
            // List<String> productIds=new ArrayList<String>();
            String productId = String.valueOf(map.get("payProductNo"));
            //cspId根据产品的id 查询
            //productIds.add(productId);
            if ("8018600305".equals(productId) || "8018000300".equals(productId) || "8018000301".equals(productId)) {
                logger.debug("燕赵世界大包、燕赵视界VIP体验包、燕赵视界VIP影视专享包不添加进显示列表");
                continue;
            }
            int typeId = -1;
            {
                Object type = map.get("typeId");
                if (type != null) {
                    typeId = StringUtils.string2int(type.toString(),typeId);
                    if (typeId!= TYPE_FOR_MONTH) {
                        //只有包月的才会让用户处理
                        logger.debug("只有包月的才会让用户处理，这个不是包月：typeId=" + type+","+map.get("name"));
                        continue;
                    }
                }
            }

            Csp csp = cspLogicInterface.get(Long.valueOf(map.get("cspId").toString()));
            boolean hasBuyThisProduct = false;
            if (csp != null && productId != null) {
                List<String> ids = new ArrayList<String>(1);
                ids.add(productId);
                hasBuyThisProduct = VacWorker.getInstance().checkBuy(userTelephone, ids, Command.OPERATE_TYPE_CHECK, csp.getSpId());
                //hasBuyThisProduct = true;
            }
            String description = String.valueOf(map.get("description"));
            if (fixedDescriptionLength > 0 && description.length() >= fixedDescriptionLength) {
                description = description.substring(0, fixedDescriptionLength - 4) + "...";
                map.put("description", description);
            }
            Object statusStr = map.get("status");
            if (statusStr == null) {
                statusStr = STATUS_OK + "";
            }

            int status = StringUtils.string2int(statusStr.toString(), STATUS_OK);
            if (status == STATUS_OK || status == STATUS_ONLY_DISPLAY || status == STATUS_ONLY_UN_SUBSCRIBE ||
                    status == STATUS_ONLY_SUBSCRIBE || hasBuyThisProduct) {
                /*if("8018600305".equals(productId) && !hasBuyThisProduct) {
                    //如果为华数的燕赵世界大包月产品并且没有购买，则不添加进显示列表
                }
                else if("8018000300".equals(productId) && !hasBuyThisProduct)
                {
                    //如果为联通体验包产品并且没有购买，则不添加进显示列表
                }else {
                    map.put("hasBuyThis",hasBuyThisProduct);
                    map.put("spId",csp.getSpId());

                    result.add(map);
                }*/

                {
                    if (status == STATUS_ONLY_DISPLAY && (!hasBuyThisProduct)) {//只显示的产品，如果没有购买就不放到展示列表里
                        logger.debug("只显示的产品，没有购买就不让用户看到：" + productId);
                        continue;
                    }
                    map.put("hasBuyThis", hasBuyThisProduct);
                    map.put("spId", csp == null ? "" : csp.getSpId());
                    result.add(map);
                }
            }
        }
        return result;
    }

    public List<Map<String, Object>> getUserMobileProducts(String userTelephone) {
        return getUserMobileProducts(userTelephone, 0);
    }

    /**
     * 用户购买产品情况，返回所有产品列表，并包含用户是否已经购买
     *
     * @param userTelephone          用户号码
     * @param fixedDescriptionLength 固定的描述长度
     * @return 产品列表
     */
    public List<Map<String, Object>> getUserMobileProducts_Pc(String userTelephone, int fixedDescriptionLength) {
        List<Map<String, Object>> products = getMobileProducts_Pc();
        Long operationType = 6l;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : products) {
            List<String> productIds = new ArrayList<String>();
            String productId = String.valueOf(map.get("payProductNo"));
            //cspId根据产品的id 查询
            productIds.add(productId);
            Csp csp = cspLogicInterface.get(Long.valueOf(map.get("cspId").toString()));
            boolean hasBuyThisProduct = false;
            if (csp != null) {
                hasBuyThisProduct = VacWorker.getInstance().checkBuy(userTelephone, productIds, operationType, csp.getSpId());
            }
            String description = String.valueOf(map.get("description"));
            if (fixedDescriptionLength > 0 && description.length() >= fixedDescriptionLength) {
                description = description.substring(0, fixedDescriptionLength - 4) + "...";
                map.put("description", description);
            }
            Object statusStr = map.get("status");
            if (statusStr == null) {
                statusStr = STATUS_OK + "";
            }

            int status = StringUtils.string2int(statusStr.toString(), STATUS_OK);
            if (status == STATUS_OK || status == STATUS_ONLY_DISPLAY || status == STATUS_ONLY_UN_SUBSCRIBE ||
                    status == STATUS_ONLY_SUBSCRIBE || hasBuyThisProduct) {
                /*if("8018600305".equals(productId) && !hasBuyThisProduct) {
                    //如果为华数的燕赵世界大包月产品并且没有购买，则不添加进显示列表
                }
                else if("8018000300".equals(productId) && !hasBuyThisProduct)
                {
                    //如果为联通体验包产品并且没有购买，则不添加进显示列表
                }else {
                    map.put("hasBuyThis",hasBuyThisProduct);
                    map.put("spId",csp.getSpId());

                    result.add(map);
                }*/

                if ("8018600305".equals(productId) || "8018000300".equals(productId) || "8018000301".equals(productId)) {
                    //燕赵世界大包、燕赵视界VIP体验包、燕赵视界VIP影视专享包不添加进显示列表
                    logger.debug("燕赵世界大包、燕赵视界VIP体验包、燕赵视界VIP影视专享包不添加进显示列表");
                } else {
                    map.put("hasBuyThis", hasBuyThisProduct);
                    map.put("spId", csp == null ? "" : csp.getSpId());

                    result.add(map);
                }
            }
        }
        return result;
    }

    public List<Map<String, Object>> getUserMobileProducts_Pc(String userTelephone) {
        return getUserMobileProducts_Pc(userTelephone, 0);
    }

    public Product getProductByPayId(String productId) {
        Product product = new Product();
        product.setPayProductNo(productId);
        List<Product> products = search(product);
        if (products == null || products.size() <= 0) {
            return null;
        }
        return products.get(0);
    }
}
