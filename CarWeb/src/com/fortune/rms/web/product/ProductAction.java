package com.fortune.rms.web.product;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.util.JsonUtils;
import com.fortune.common.Constants;
import com.fortune.vac.VacWorker;
import org.apache.struts2.convention.annotation.*;

import java.util.List;
import java.util.Map;

@Namespace("/product")
@ParentPackage("default")
@Results({
        @Result(name = "listProductType",location = "/product/jsonProduct.jsp")
})
@Action(value="product")
public class ProductAction extends BaseAction<Product> {
	private static final long serialVersionUID = 3243534534534534l;
	private ProductLogicInterface productLogicInterface;
    private List<Product> products;
    private String userTelephone;
    private UserLogicInterface userLogicInterface;


	@SuppressWarnings("unchecked")
	public ProductAction() {
		super(Product.class);
	}
	/**
	 * @param productLogicInterface the productLogicInterface to set
	 */
	public void setProductLogicInterface(
			ProductLogicInterface productLogicInterface) {
		this.productLogicInterface = productLogicInterface;
		setBaseLogicInterface(productLogicInterface);
	}
    public void setUserLogicInterface(UserLogicInterface userLogicInterface) {
        this.userLogicInterface = userLogicInterface;
    }
    public  String list(){
        objs = productLogicInterface.getAllProduct(obj,pageBean);
        return Constants.ACTION_LIST;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getAllProductOfStatus(){
        objs = productLogicInterface.getAllProductOfStatus(obj,pageBean);
        return Constants.ACTION_LIST;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String listProductType(){
        products=productLogicInterface.getProductType(obj.getType(),pageBean);
        return "listProductType";
    }


      public String getListJson() {
         return JsonUtils.getListJsonString("objs", products, "totalCount", pageBean.getRowCount());
    }
    public void getUserMobileProducts(){
        String productId;
        String result;
        Double buyPrice;
        int totalCount=0;
//        Long operationType=6l;
        String description;
//        String hasBuyThis="";// 是否已经订购！
        List<Map<String,Object>> maps =productLogicInterface.getUserMobileProducts(userTelephone);
        if(maps.size()>0){
            result="{\"success\":\"true\",\"objs\":[";
            for (Map map:maps) {
//                List<String> productIds=new ArrayList<String>();
                productId=String.valueOf(map.get("payProductNo"));
                //cspId根据产品的id 查询
//                productIds.add(productId);
                //过掉10元不然客户购买10元的只能退
                buyPrice= Double.valueOf(map.get("price").toString());
                //进行四舍五入操作：
//                int prices=Integer.parseInt(new java.text.DecimalFormat("0").format(buyPrice));
                description=String.valueOf(map.get("description"));
                //获取信息状态
                int status= Integer.parseInt(map.get("status").toString());
                if(description.length()>=130){
                    description=description.substring(0,126)+"...";
                }
                //状态3  是可退不可定的（会显示退订 不会显示订购）
                    ++totalCount;
                    result += "{" +
                            "\"id\":\"" + productId+ "\"," +
                            "\"name\":\"" + map.get("serviceProductName").toString()+ "\"," +
                            "\"price\":\"" + buyPrice+ "\"," +
                            "\"payProductNo\":\"" + String.valueOf(map.get("payProductNo")) + "\"," +
                            "\"spId\":\"" +  String.valueOf(map.get("spId")) + "\"," +
                            "\"description\":\"" + description + "\"," +
                            "\"typeId\":\"" + String.valueOf(map.get("typeId")) + "\"," +
                            "\"type\":\"" + String.valueOf(map.get("type")) + "\"," +
                            "\"status\":\"" + status + "\"," +
                            "\"costType\":\"" + String.valueOf(map.get("costType")) + "\"," +
                            "\"hasBuyThis\":\"" + String.valueOf(map.get("hasBuyThis")) + "\"" +
                            "},";
            }
            result = result.substring(0, result.length() - 1);
            result +="],\"totalCount\":\"" + totalCount + "\"}";
        }
        else {
            result = "{ \"totalCount\":\"" + maps.size() + "\",\"success\":\"false\",\"objs\":[]}";
        }
        directOut(result);
    }

    public void getUserMobileProducts_Pc(){
        String productId;
        String result;
        Double buyPrice;
        int totalCount=0;
//        Long operationType=6l;
        String description;
//        String hasBuyThis="";// 是否已经订购！
        List<Map<String,Object>> maps =productLogicInterface.getUserMobileProducts_Pc(userTelephone);
        if(maps.size()>0){
            result="{\"success\":\"true\",\"objs\":[";
            for (Map map:maps) {
//                List<String> productIds=new ArrayList<String>();
                productId=String.valueOf(map.get("payProductNo"));
                //cspId根据产品的id 查询
//                productIds.add(productId);
                //过掉10元不然客户购买10元的只能退
                buyPrice= Double.valueOf(map.get("price").toString());
                //进行四舍五入操作：
//                int prices=Integer.parseInt(new java.text.DecimalFormat("0").format(buyPrice));
                description=String.valueOf(map.get("description"));
                //获取信息状态
                int status= Integer.parseInt(map.get("status").toString());
                if(description.length()>=130){
                    description=description.substring(0,126)+"...";
                }
                //状态3  是可退不可定的（会显示退订 不会显示订购）
                ++totalCount;
                result += "{" +
                        "\"id\":\"" + productId+ "\"," +
                        "\"name\":\"" + map.get("serviceProductName").toString()+ "\"," +
                        "\"price\":\"" + buyPrice+ "\"," +
                        "\"payProductNo\":\"" + String.valueOf(map.get("payProductNo")) + "\"," +
                        "\"spId\":\"" +  String.valueOf(map.get("spId")) + "\"," +
                        "\"description\":\"" + description + "\"," +
                        "\"type\":\"" + String.valueOf(map.get("type")) + "\"," +
                        "\"status\":\"" + status + "\"," +
                        "\"costType\":\"" + String.valueOf(map.get("costType")) + "\"," +
                        "\"hasBuyThis\":\"" + String.valueOf(map.get("hasBuyThis")) + "\"" +
                        "},";
            }
            result = result.substring(0, result.length() - 1);
            result +="],\"totalCount\":\"" + totalCount + "\"}";
        }
        else {
            result = "{ \"totalCount\":\"" + maps.size() + "\",\"success\":\"false\",\"objs\":[]}";
        }
        directOut(result);
    }

    private String productId;
    private String spId;
    private String verifyCode;
    private Long operationType;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public String buyMessage(){
        if(operationType==1){
            //AppConfigurator config = AppConfigurator.getInstance();
            if(verifyCode==null|| !userLogicInterface.verifyCodeForBuy(userTelephone, verifyCode)){
                //验证码不对
                String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"验证码错误:"+verifyCode+",请重新订购！\",\"objs\":[]}";
                directOut(result);
            }else{
                if(spId!=null&&!spId.equals("null")){
//                        Long operationLong=(long) operationType;
                    int operateOrder=VacWorker.getInstance().operateOrder(userTelephone,productId,operationType,spId);
                    if(operateOrder==0||operateOrder==1200){
                        getUserMobileProducts();
                    } else{
                        String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                        directOut(result);
                    }
                } else {
                    String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                    directOut(result);
                }
            }
        }  else {
            if(spId!=null&&!spId.equals("null")){
//                Long operationLong=(long) operationType;
                int operateOrder=VacWorker.getInstance().operateOrder(userTelephone,productId,operationType,spId);
                if(operateOrder==0||operateOrder==1200){
                    getUserMobileProducts();
                } else{
                    String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"退订失败，请稍后再次尝试退订！\",\"objs\":[]}";
                    directOut(result);
                }
            } else {
                String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"退订失败，请稍后再次尝试退订！\",\"objs\":[]}";
                directOut(result);
            }
        }
//        Long operationType=(long)6;

        return null;
    }

    //手机、PC产品要求不断变更，暂时不宜进行整合！
    public String buyMessage_Pc(){
        if(operationType==1){
            //AppConfigurator config = AppConfigurator.getInstance();
            if(ConfigManager.getInstance().getConfig("needCheckVerifyCode", false)){
                if(verifyCode==null|| !userLogicInterface.verifyCodeForBuy(userTelephone, verifyCode)){

                    //验证码不对
                    String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"验证码错误:"+verifyCode+",请重新订购！\",\"objs\":[]}";
                    directOut(result);
                }else{
                    if(spId!=null&&!spId.equals("null")){
                        int operateOrder=VacWorker.getInstance().operateOrder(userTelephone,productId,operationType,spId);
                        if(operateOrder==0||operateOrder==1200){
                            getUserMobileProducts_Pc();
                        } else{
                            String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                            directOut(result);
                        }
                    } else {
                        String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                        directOut(result);
                    }
                }
            }
        }  else {
            if(spId!=null&&!spId.equals("null")){
                int operateOrder=VacWorker.getInstance().operateOrder(userTelephone,productId,operationType,spId);
                if(operateOrder==0||operateOrder==1200){
                    getUserMobileProducts();
                } else{
                    String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"退订失败，请稍后再次尝试退订！\",\"objs\":[]}";
                    directOut(result);
                }
            } else {
                String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"退订失败，请稍后再次尝试退订！\",\"objs\":[]}";
                directOut(result);
            }
        }
//        Long operationType=(long)6;

        return null;
    }
}
