<%@ page import="net.sf.json.JSONObject" %><%@ page import="net.sf.json.JSONArray" %><%@ page import="com.fortune.cars.business.cars.model.CarItem" %><%@ page import="java.util.List" %><%@ page import="java.util.ArrayList" %><%@ page import="com.fortune.util.JsonUtils" %><%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/9/3
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%!
    private List<CarItem> items = null;
    public List<CarItem> initItems(){
        items = new ArrayList<CarItem>();
        items.add(new CarItem("id",null,false,"hidden","-1"));
        items.add(new CarItem("createTime","创建时间",false,"hidden",null));
        items.add(new CarItem("password","登录口令",false,"hidden",null));
        items.add(new CarItem("creator","创建者",false,"hidden",null));
        items.add(new CarItem("sn","档案编号",false,null,null));
        items.add(new CarItem("carNo","车牌号码",false,null,null));
        items.add(new CarItem("userId","客户名称",false,null,null));
        items.add(new CarItem("phone","联系方式",false,null,null));
        items.add(new CarItem("product","车辆品牌",false,null,null));
        items.add(new CarItem("productType","车辆型号",false,null,null));
        items.add(new CarItem("productHometown","产    地",false,null,null));
        items.add(new CarItem("salesCompany","销售单位",false,null,null));
        items.add(new CarItem("vinCode","VIN码",false,null,null));
        items.add(new CarItem("engineCode","发动机号",false,null,null));
        items.add(new CarItem("enineType","发动机型号",false,null,null));
        items.add(new CarItem("gearbox","变速箱类型",false,"select",null));
        items.add(new CarItem("lengthWidthHeight","长宽高",false,null,null));
        items.add(new CarItem("carColor","车身颜色",false,null,null));
        items.add(new CarItem("innerColor","内饰颜色",false,null,null));
        items.add(new CarItem("gasType","燃油种类",false,"select",null));
        items.add(new CarItem("emissionType","排放标准",false,"select",null));
        items.add(new CarItem("emission","排    量",false,"select",null));
        items.add(new CarItem("tyreType","轮胎规格",false,null,null));
        items.add(new CarItem("carType","车辆用途",false,"select",null));
        items.add(new CarItem("mileage","行驶里程",false,null,null));
        items.add(new CarItem("maintainTimes","保养次数",false,null,null));
        items.add(new CarItem("motStime","车检起始",false,"date",null));
        items.add(new CarItem("motEtime","车检截至",false,"date",null));
        items.add(new CarItem("productionDate","出厂日期",false,"date",null));
        items.add(new CarItem("insureStime","保险起始",false,"date",null));
        items.add(new CarItem("insureEtime","保险截至",false,"date",null));
        items.add(new CarItem("insureType","险种",false,null,null));
        items.add(new CarItem("insureCompany","保险公司",false,null,null));
        items.add(new CarItem("carPictureTop","顶部照片",false,"image","/upload/car/top.jpg"));
        items.add(new CarItem("carPictureLeft","左侧照片",false,"image","/upload/car/left.jpg"));
        items.add(new CarItem("carPictureFront","前面照片",false,"image","/upload/car/front.jpg"));
        items.add(new CarItem("carPictureBottom","底部照片",false,"image","/upload/car/bottom.jpg"));
        items.add(new CarItem("carPictureRight","右侧照片",false,"image","/upload/car/right.jpg"));
        items.add(new CarItem("carPictureBack","后面照片",false,"image","/upload/car/back.jpg"));
        return items;
    }
    String itemString = "[\n" +
            "        {name:\"obj.id\",type:\"hidden\",value:\"-1\"},\n" +
            "        {fieldLabel:\"创建时间\",type:\"hidden\",name:\"obj.createTime\",readOnly:\"viewReadOnly\"},\n" +
            "          {fieldLabel:\"登录口令\",type:\"hidden\",name:\"obj.password\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"创建者\",name:\"obj.creator\",type:\"hidden\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"档案编号\",name:\"obj.sn\",readOnly:\"viewReadOnly\",allowBlank:false},\n" +
            "        {fieldLabel:\"车牌号码\",name:\"obj.carNo\",readOnly:\"viewReadOnly\",allowBlank:false},\n" +
            "        {fieldLabel:\"客户名称\",name:\"obj.userId\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"联系方式\",name:\"obj.phone\",readOnly:\"viewReadOnly\",allowBlank:false},\n" +
            "        {fieldLabel:\"车辆品牌\",name:\"obj.product\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"车辆型号\",name:\"obj.productType\",readOnly:\"viewReadOnly\",parentStoreCode:\"product\"},\n" +
            "        {fieldLabel:\"产    地\",name:\"obj.productHometown\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"销售单位\",name:\"obj.salesCompany\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"VIN码\",name:\"obj.vinCode\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"发动机号\",name:\"obj.engineCode\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"发动机型号\",name:\"obj.enineType\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"变速箱类型\",name:\"obj.gearbox\",readOnly:\"viewReadOnly\",type:\"select\"},\n" +
            "        {fieldLabel:\"长宽高\",name:\"obj.lengthWidthHeight\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"车身颜色\",name:\"obj.carColor\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"内饰颜色\",name:\"obj.innerColor\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"燃油种类\",name:\"obj.gasType\",readOnly:\"viewReadOnly\",type:\"select\"},\n" +
            "        {fieldLabel:\"排放标准\",name:\"obj.emissionType\",readOnly:\"viewReadOnly\",type:\"select\"},\n" +
            "        {fieldLabel:\"排    量\",name:\"obj.emission\",readOnly:\"viewReadOnly\",type:\"select\"},\n" +
            "        {fieldLabel:\"轮胎规格\",name:\"obj.tyreType\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"车辆用途\",name:\"obj.carType\",readOnly:\"viewReadOnly\",type:\"select\"},\n" +
            "        {fieldLabel:\"行驶里程\",name:\"obj.mileage\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"保养次数\",name:\"obj.maintainTimes\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"车检起始\",name:\"obj.motStime\",readOnly:\"viewReadOnly\",type:\"date\"},\n" +
            "        {fieldLabel:\"车检截至\",name:\"obj.motEtime\",readOnly:\"viewReadOnly\",type:\"date\"},\n" +
            "        {fieldLabel:\"出厂日期\",name:\"obj.productionDate\",readOnly:\"viewReadOnly\",type:\"date\"},\n" +
            "        {fieldLabel:\"保险起始\",name:\"obj.insureStime\",readOnly:\"viewReadOnly\",type:\"date\"},\n" +
            "        {fieldLabel:\"保险截至\",name:\"obj.insureEtime\",readOnly:\"viewReadOnly\",type:\"date\"},\n" +
            "        {fieldLabel:\"险种\",name:\"obj.insureType\",readOnly:\"viewReadOnly\"},\n" +
            "        {fieldLabel:\"保险公司\",name:\"obj.insureCompany\",readOnly:\"viewReadOnly\"},\n" +
            "        {type:\"newLine\"},\n" +
            "        {fieldLabel:\"顶部照片\",name:\"obj.carPictureTop\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/top.jpg\"},\n" +
            "        {fieldLabel:\"左侧照片\",name:\"obj.carPictureLeft\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/left.jpg\"},\n" +
            "        {fieldLabel:\"前面照片\",name:\"obj.carPictureFront\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/front.jpg\"},\n" +
            "        {fieldLabel:\"底部照片\",name:\"obj.carPictureBottom\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/bottom.jpg\"},\n" +
            "        {fieldLabel:\"右侧照片\",name:\"obj.carPictureRight\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/right.jpg\"},\n" +
            "        {fieldLabel:\"后面照片\",name:\"obj.carPictureBack\",readOnly:\"viewReadOnly\",type:\"image\",value:\"/upload/car/back.jpg\"}]\n";
    //JSONObject object = JSONObject.fromObject(items);
    public String getItemArrayString(String items){
        JSONArray array = JSONArray.fromObject(items);
        int i=0,l=array.size();
        String result = "List<CarItem> items = new ArrayList<CarItem>();\r\n";
        for(;i<l;i++){
            JSONObject obj = array.getJSONObject(i);
            CarItem item =(CarItem) JSONObject.toBean(obj,CarItem.class);
            if("newLine".equals(item.getType())){
                continue;
            }
            result +=("items.add(new CarItem(\""+item.getName()+
                    "\",\""+item.getFieldLabel()+"\","+item.isAllowBlank()+",\""+item.getType()+
                    "\",\""+item.getValue()+"\"));\r\n").replace("obj.","").replace("\"null\"","null");
        }
        return result;
    }
    public String getJsonOfCarItems(){
        if(items==null||items.size()==0){
            initItems();
        }
        return JsonUtils.getJsonString(items);
    }
%>