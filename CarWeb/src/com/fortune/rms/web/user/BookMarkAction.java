package com.fortune.rms.web.user;

import com.fortune.rms.business.content.model.Content;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.frontuser.model.FrontUser;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.BookMarkLogicInterface;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.rms.business.user.model.BookMark;
import com.fortune.rms.business.user.model.HistoryDTO;
import com.fortune.threeScreen.common.Constants;
import com.fortune.util.AppConfigurator;
import com.fortune.vac.VacWorker;
import com.fortune.util.PageBean;
import com.fortune.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;


import java.util.*;

@Namespace("/web")
@ParentPackage("threeScreen")
@Results({
        @Result(name = "add", location = "/common/threeScreenData.jsp"),
        @Result(name = "list", location = "/common/threeScreenData.jsp"),
        @Result(name = "delete", location = "/common/threeScreenData.jsp")
})
//@Action(value="bookMark-*")
@Action(value = "bookMark")
@SuppressWarnings("unused")
public class BookMarkAction extends UnicomAction<BookMark> {
    private static final long serialVersionUID = 3243534534534534l;
    private BookMarkLogicInterface bookMarkLogicInterface;
    private ProductLogicInterface productLogicInterface;
    //private ServiceProductLogicInterface serviceProductLogicInterface;
    private UserLogicInterface userLogicInterface;
    private CspLogicInterface cspLogicInterface;
    private BookMark bookMark;

    public BookMarkAction() {
        super(BookMark.class);
    }


    public BookMark getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
    }

    public BookMarkLogicInterface getBookMarkLogicInterface() {
        return bookMarkLogicInterface;
    }

    public void setBookMarkLogicInterface(BookMarkLogicInterface bookMarkLogicInterface) {
        this.bookMarkLogicInterface = bookMarkLogicInterface;
    }

    public void setProductLogicInterface(ProductLogicInterface productLogicInterface) {
        this.productLogicInterface = productLogicInterface;
    }

/*
    public void setServiceProductLogicInterface(ServiceProductLogicInterface serviceProductLogicInterface) {
        this.serviceProductLogicInterface = serviceProductLogicInterface;
    }

*/
    public void setUserLogicInterface(UserLogicInterface userLogicInterface) {
        this.userLogicInterface = userLogicInterface;
    }

    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
    }

    public List<BookMark> bookmarks;
    public JSONObject jsonList;

    public JSONObject getJsonList() {
        return jsonList;
    }

    public void setJsonList(JSONObject jsonList) {
        this.jsonList = jsonList;
    }

    public String saveBookmark() {
        obj =  bookMarkLogicInterface.saveBookmark(obj.getUserId(), obj.getContentId(), obj.getSubContentId(), obj.getBookMarkValue());
        boolean success = obj == null;
        setSuccess(success);
        if (!success) {
            addActionError("保存播放记录失败，有些参数无法获取！");
        }
        return "view";
    }

    public String add() {
        int userType = Constants.USER_TYPE_PC;
        if (userId == null) {
            userId = playUserId;
        }
        AllResp allResp = bookMarkLogicInterface.addBookMark(userId, userType, contentId, subContentId,
                subContentName, subContentType, serviceType, bookMarkType, bookMarkValue);
        jsonList = JSONObject.fromObject(allResp);
        log.debug("媒体[" + subContentName + "|" + contentId + "]书签[" + bookMarkValue + "|" +
                StringUtils.formatTime(StringUtils.string2long(bookMarkValue, 0)) +
                "]添加调用结果：" + jsonList.toString());
        return "add";
    }

    public String list() {
        int userType = Constants.USER_TYPE_PC;
        AllResp allBookMarkResp = bookMarkLogicInterface.getAllBookMark(userId, userType, bookMarkType);
        jsonList = JSONObject.fromObject(allBookMarkResp);
        log.debug("用户[" + userId + "]书签列表：" + jsonList.toString());
        return "list";
    }

    public String delete() {
        int userType = Constants.USER_TYPE_PC;
        AllResp allResp = bookMarkLogicInterface.deleteBookMark(userId, userType,
                operationType, bookMarkType, bookMarkIdList);
        jsonList = JSONObject.fromObject(allResp);
        log.debug("用户[" + userId + "]书签[" + bookMarkIdList +
                "]删除调用结果：" + jsonList.toString());
        return "delete";
    }

    private String userTelephone;
    private Long removeContentId;
    private String productId;
    private String spId;
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public Long getRemoveContentId() {
        return removeContentId;
    }

    public void setRemoveContentId(Long removeContentId) {
        this.removeContentId = removeContentId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    //删除播放记录
    public String removeBookMark() {
        if (bookMarkLogicInterface.removeBookMark(userTelephone, removeContentId)) {
            bookMarkOfUser();
        }
        return null;
    }

    //查询播放记录
    public String getBookMarkOfUser() {
        bookMarkOfUser();
        return null;
    }

    //购买产品
    public String buyMessage() {
        if (operationType == 1) {
            AppConfigurator config = AppConfigurator.getInstance();
            if (config.getBoolConfig("needCheckVerifyCode", false)) {
                if (verifyCode == null || !userLogicInterface.verifyCodeForBuy(userTelephone, verifyCode)) {

                    //验证码不对
                    String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"验证码错误:" + verifyCode + ",请重新订购！\",\"objs\":[]}";
                    directOut(result);
                } else {
                    if (spId != null && !"null".equals(spId)) {
                        Long operationLong = (long) operationType;
                        int operateOrder = VacWorker.getInstance().operateOrder(userTelephone, productId, operationLong, spId);
                        if (operateOrder == 0 || operateOrder == 1200) {
                            IsBuyMessage();
                        } else {
                            String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                            directOut(result);
                        }
                    } else {
                        String result = "{ \"totalCount\":\"" + 0 + "\",\"success\":\"false\",\"error\":\"订购失败，请稍后再次尝试订购！\",\"objs\":[]}";
                        directOut(result);
                    }
                }
            }
        } else {
            if (spId != null && !spId.equals("null")) {
                Long operationLong = (long) operationType;
                int operateOrder = VacWorker.getInstance().operateOrder(userTelephone, productId, operationLong, spId);
                if (operateOrder == 0 || operateOrder == 1200) {
                    IsBuyMessage();
                } else {
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

    /**
     * 保存播放历史，obj.contentId传递内容obj.subContentId传episode
     * @return SUCCESS
     */
    @Action(value = "addHis")
    public String redexAddHistory(){
        if (session.get(com.fortune.common.Constants.SESSION_FRONT_USER) != null) {
            FrontUser user = (FrontUser) session.get(com.fortune.common.Constants.SESSION_FRONT_USER);
            // 不知道什么意思
            obj.setBookMarkValue("1");
            // 如果episode为null，设置为1
            if(obj.getSubContentId() == null) obj.setSubContentId("1");
            obj = bookMarkLogicInterface.saveBookmark(user.getUserId(), obj.getContentId(), obj.getSubContentId(), obj.getBookMarkValue());
            setSuccess(obj!=null);
        }else{
            setSuccess(false);
            addActionError("登录已过期，请重新登录");
        }
        return SUCCESS;
    }

    @Action(value = "updateBookmark")
    public String updateBookmark(){
        if (session.get(com.fortune.common.Constants.SESSION_FRONT_USER) != null) {
            FrontUser user = (FrontUser) session.get(com.fortune.common.Constants.SESSION_FRONT_USER);
            // 不知道什么意思
            // 如果episode为null，设置为1
            if(obj.getSubContentId() == null) obj.setSubContentId("1");
            log.debug("用户"+user.getName()+"("+user.getUserId()+")要保存播放记录：id="+obj.getId()+
                    ",contentId="+obj.getContentId()+",subContentId="+obj.getSubContentId()+
                    ",value="+obj.getBookMarkValue());
            if(obj.getId()<=0){
                obj = bookMarkLogicInterface.saveBookmark(user.getUserId(), obj.getContentId(), obj.getSubContentId(), obj.getBookMarkValue());
                setSuccess(obj!=null);
            }else{
                setSuccess(1==bookMarkLogicInterface.updateBookmark(obj.getId(),user.getUserId(), obj.getContentId(), obj.getSubContentId(), obj.getBookMarkValue()));
            }
        }else{
            setSuccess(false);
            addActionError("登录已过期，请重新登录");
        }
        return SUCCESS;
    }

    /**
     * ajax调用，返回json格式当前用户的观看历史，页码信息在pageBean中
     */
    @Action(value = "historyList")
    public void redexGetHistoryList(){
        if (obj.getUserId() == null && session.get(com.fortune.common.Constants.SESSION_FRONT_USER) != null) {
            FrontUser user = (FrontUser) session.get(com.fortune.common.Constants.SESSION_FRONT_USER);
            obj.setUserId(user.getUserId());
        }
        if(pageBean == null) pageBean = new PageBean();
        List<HistoryDTO> historyList = bookMarkLogicInterface.redexGetHistoryList(obj.getUserId(), pageBean);

        directOut(com.fortune.util.JsonUtils.getListJsonString("history", historyList, "totalCount", pageBean.getRowCount()));
    }

    //查询可以绑定的产品信息   Long operationType 1.订购 2.退订 3.鉴权
    public String checkBuyMessage() {
//        String phoneNumber="";
        IsBuyMessage();
        return null;
    }

    public void IsBuyMessage() {
//        VacWorker vacWorker=new VacWorker();
        String productId = "";
        String result;
        Double buyPrice;
        int totalCount = 0;
        Long operationType = 6l;
        String description;
        boolean whetherBuy = false;
        List<Map<String, Object>> maps = productLogicInterface.getMobileProducts();
        if (maps.size() > 0) {
            result = "{\"success\":\"true\",\"objs\":[";
            for (Map map : maps) {
                List<String> productIds = new ArrayList<String>();
                productId = String.valueOf(map.get("payProductNo"));
                //cspId根据产品的id 查询
                productIds.add(productId);
//                ServiceProduct serviceProduct =servicePro
// ductLogicInterface.getServiceProductBySpId(String.valueOf(product.getId()));
                //判断是否购买
//                log.debug("userId--->"+userTelephone+"  productId--->"+productId+"  operationType--->"+operationType+"  cspId--->"+String.valueOf(map.get("cspId")));
                Csp csp = cspLogicInterface.get(Long.valueOf(map.get("cspId").toString()));
                if (csp != null) {
                    whetherBuy = VacWorker.getInstance().checkBuy(userTelephone, productIds, operationType, csp.getSpId());
                }
                //过掉10元不然客户购买10元的只能退
                buyPrice = Double.valueOf(map.get("price").toString());
                //进行四舍五入操作：
                int prices = Integer.parseInt(new java.text.DecimalFormat("0").format(buyPrice));
                description = String.valueOf(map.get("description"));
                //获取信息状态
                int status = Integer.parseInt(map.get("status").toString());
                if (description.length() >= 60) {
                    description = description.substring(0, 56) + "...";
                }
                //状态3  是可退不可定的（会显示退订 不会显示订购）
                if (status != 3 || whetherBuy) {
                    ++totalCount;
                    result += "{" +
                            "\"id\":\"" + productId + "\"," +
                            "\"name\":\"" + map.get("serviceProductName").toString() + "\"," +
                            "\"price\":\"" + buyPrice + "\"," +
                            "\"payProductNo\":\"" + String.valueOf(map.get("payProductNo")) + "\"," +
                            "\"spId\":\"" + csp.getSpId() + "\"," +
                            "\"description\":\"" + description + "\"," +
                            "\"type\":\"" + String.valueOf(map.get("type")) + "\"," +
                            "\"status\":\"" + status + "\"," +
                            "\"whetherBuy\":\"" + whetherBuy + "\"" +
                            "},";
                }
            }
            result = result.substring(0, result.length() - 1);
            result += "],\"totalCount\":\"" + totalCount + "\"}";
        } else {
            result = "{ \"totalCount\":\"" + maps.size() + "\",\"success\":\"false\",\"objs\":[]}";
        }
        directOut(result);
    }


    public void bookMarkOfUser() {
        String formatTimeL = "";
        int count = bookMarkLogicInterface.getBookMarkCountOfUser(userTelephone);
        List<Content> list = bookMarkLogicInterface.getBookMarkOfUser(userTelephone, new PageBean(0, count, "o1.id", "asc"));
        if (count > 10) {
            int count1 = count - 10;
            for (int i = 0; i < count1; i++) {
                list.remove(list.get(0));
            }
        }
        String result = "{ \"totalCount\":\"" + list.size() + "\",\"success\":\"true\",\"objs\":[";
        if (list.size() >= 1) {
            for (Content content : list) {
                Map<String, Object> properties = content.getProperties();
                BookMark bookMark = content.getBookMark();
                String bookMarkId = bookMark.getSubContentId();
                Long id = content.getId();
                String name = content.getName();
                String moveActors = content.getActors();
                String moveLength = (String) properties.get("MEDIA_LENGTH");
                //当前播放的时间
                String moveNowTime = bookMark.getBookMarkValue();
                if (moveNowTime == null) {
                    moveNowTime = "";
                }
                if (moveActors == null) {
                    moveActors = "无";
                }
                if (name == null) {
                    name = "";
                }
                if (name.length() > 7) {
                    name = name.substring(0, 7) + "..";
                }
                String postUrl = (String) properties.get("PC_MEDIA_POSTER_BIG");
                if (postUrl == null || "".equals(postUrl.trim())) {
                    postUrl = (String) properties.get("PC_MEDIA_POSTER_HORIZONTAL_BIG");
                }
                if (postUrl != null && !"".equals(postUrl.trim())) {
                } else {
                    postUrl = "";
                }
                String formatTime = StringUtils.formatTime(StringUtils.string2int(moveNowTime, 0)); //存放时间的格式不统一
                if (StringUtils.string2int(moveLength, 0) > 180) {
                    formatTimeL = StringUtils.formatTime(StringUtils.string2int(moveLength, 0));
                } else {
                    formatTimeL = StringUtils.formatTime(StringUtils.string2int(moveLength, 0) * 60);
                }
                result += "{" +
                        "\"id\":\"" + id + "\"," +
                        "\"bookMarkId\":\"" + bookMarkId + "\"," +
                        "\"name\":\"" + name + "\"," +
                        "\"moveActors\":\"" + moveActors + "\"," +
                        "\"moveNowTime\":\"" + moveNowTime + "\"," +
                        "\"moveLength\":\"" + moveLength + "\"," +
                        "\"formatTime\":\"" + formatTime + "\"," +
                        "\"formatTimeL\":\"" + formatTimeL + "\"," +
                        "\"postUrl\":\"" + postUrl + "\"" +
                        "},";
            }
            result = result.substring(0, result.length() - 1);
        }
        result += "]}";
        directOut(result);
    }

    private String contentId;
    private String subContentId;
    private String subContentName;
    private Integer subContentType;
    private Integer serviceType;
    private Integer bookMarkType;
    private String bookMarkValue;
    private Integer operationType;
    private String bookMarkIdList;
    private String playUserId;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getSubContentId() {
        return subContentId;
    }

    public void setSubContentId(String subContentId) {
        this.subContentId = subContentId;
    }

    public String getSubContentName() {
        return subContentName;
    }

    public void setSubContentName(String subContentName) {
        this.subContentName = subContentName;
    }

    public Integer getSubContentType() {
        return subContentType;
    }

    public void setSubContentType(Integer subContentType) {
        this.subContentType = subContentType;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getBookMarkType() {
        return bookMarkType;
    }

    public void setBookMarkType(Integer bookMarkType) {
        this.bookMarkType = bookMarkType;
    }

    public String getBookMarkValue() {
        return bookMarkValue;
    }

    public void setBookMarkValue(String bookMarkValue) {
        this.bookMarkValue = bookMarkValue;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getBookMarkIdList() {
        return bookMarkIdList;
    }

    public void setBookMarkIdList(String bookMarkIdList) {
        this.bookMarkIdList = bookMarkIdList;
    }

    public String getPlayUserId() {
        return playUserId;
    }

    public void setPlayUserId(String playUserId) {
        this.playUserId = playUserId;
    }
}
