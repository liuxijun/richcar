package com.fortune.rms.web.csp;

import com.fortune.common.Constants;

import com.fortune.common.business.security.model.Admin;

import com.fortune.rms.business.csp.logic.logicInterface.CspChannelLogicInterface;
import com.fortune.rms.business.csp.logic.logicInterface.CspLogicInterface;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.csp.model.CspAuditor;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.product.model.UserBuy;
import com.fortune.rms.business.system.model.Device;
import com.fortune.util.HibernateUtils;
import com.fortune.util.JsonUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
@Namespace("/csp")
@ParentPackage("default")
@Results({
        @Result(name = "cspChannelList", location = "/csp/cspList.jsp")
})
@Action(value="csp")
public class CspAction extends BaseAction<Csp> {
    private CspLogicInterface cspLogicInterface;
    private CspChannelLogicInterface cspChannelLogicInterface;
    private List<Long> deviceIds;
    private String deviceIdsString;
    private List<Long> cpIds;
    private String cpIdsString;
    private List<Long> productIds;
    private String productIdsString;
    private String moduleIds;
    private String moduleIdsString;
    private List<Admin> admins;
    private String adminIdAndCspIdString;
    private List<CspAuditor> cspAuditors;
    private List<CspModule> cspModules;
    private String adminIds;
    private Admin admin;
    private Module module;
    private Long defaultModuleId;
    private String moduleIdAndCspIdString;
    private UserBuy userBuy;
    private long cspId;
    private String chooseChannel;


    public CspAction() {
        super(Csp.class);
    }

    @Autowired
    public void setCspLogicInterface(CspLogicInterface cspLogicInterface) {
        this.cspLogicInterface = cspLogicInterface;
        setBaseLogicInterface(cspLogicInterface);
    }
    @Autowired
    public void setCspChannelLogicInterface(CspChannelLogicInterface cspChannelLogicInterface) {
        this.cspChannelLogicInterface = cspChannelLogicInterface;
    }

    /**
     * ��ȡ����Sp��Ϣ
     */
    public String searchAllSp(){
        List<Csp> objs = cspLogicInterface.getAllSp();
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("objs", objs, "totalCount", objs.size()));
        return null;
    }
    /**
     * ��ȡ����Cp��Ϣ
     */
    public String searchAllCp(){
        List<Csp> objs = cspLogicInterface.getAllCp();
        JsonUtils jsonUtils = new JsonUtils();
        directOut(jsonUtils.getListJson("objs",objs,"totalCount",objs.size()));
        return null;
    }

    /**
     * �鿴Csp�󶨵��豸
     */
    public String searchDevicesByCspId(){
        //��ȡ��CSP�Ļ�����Ϣ
        super.view();
        //�жϸ�Csp�Ķ���������Ƿ����
        if(obj != null && obj.getId() > 0){
            List<Device> devices = cspLogicInterface.getDevicesByCspId(obj.getId());
            if(devices != null && devices.size()>0){
               obj.setDevices(devices);
            }
        }
        return Constants.ACTION_VIEW;
    }

    public void setChooseChannel(String chooseChannel) {
        this.chooseChannel = chooseChannel;
    }

    /**
     * ��ȡSp�󶨵�Cp��Ϣ
     * @return
     */
    public String searchCpsBySpId() {
        //��ȡ��SP�Ļ�����Ϣ
        super.view();
        //�жϸ�SP�Ķ���������Ƿ����
        if (obj != null && obj.getId() > 0) {
            List<Csp> csps = cspLogicInterface.getCpsBySpId(obj.getId());
            if(csps != null && csps.size()>0){
                obj.setCsps(csps);
            }
        }
        return Constants.ACTION_VIEW;
    }
    /**
     * ��ȡSp����Ʒ����Ϣ
     */
    public String searchProductBySpId() {
        //��ȡ��SP�Ļ�����Ϣ
        super.view();
        //�жϸ�SP�Ķ���������Ƿ����
        if (obj != null && obj.getId() > 0) {
            List<Product> products = cspLogicInterface.getProductsBySpId(obj.getId());
            if(products != null && products.size()>0){
                obj.setProducts(products);
            }
        }
        return Constants.ACTION_VIEW;
    }
    /**
     * ��ȡCsp��ģ�����Ϣ
     */
    public String viewAndModule() {
        String result = super.view();
        if (obj != null && obj.getId() > 0) {
            obj.setModules(cspLogicInterface.getAllModuleWithCspCheck(obj.getId()));
            obj.setDefaultModuleId(cspLogicInterface.getDefaultModuleId(obj.getId()));
        }
        return result;
    }

    /**
     * ����Csp��Ϣ
     */
    public String save() {
        super.save();
        writeSysLog("����CSP�����ƣ�"+obj.getName()+",CSP��Id:"+obj.getId());
        return Constants.ACTION_SAVE;
    }
    /**
     * ����豸��Csp
     */
    public String saveDeviceToCsp() {
        if (obj != null) {
            try {
                cspLogicInterface.saveDeviceToCsp(deviceIds, obj.getId());
                writeSysLog("CSP���豸��"+deviceIds+",CSP��Id"+obj.getId());
            } catch (Exception e) {
                setSuccess(false);
                addActionError("�������ʱ��������" + e.getMessage());
            }
        }
        return Constants.ACTION_SAVE;
    }
    /**
     * ��Csp
     */
    public String saveCspToCsp() {
        if (obj != null) {
            try {
                cspLogicInterface.saveCspToCsp(cpIds, obj.getId());
                writeSysLog("��CP��ID��"+cpIds+",CSP��Id"+obj.getId());
            } catch (Exception e) {
                setSuccess(false);
                addActionError("�������ʱ��������:" + e.getMessage());
            }
        }
        return Constants.ACTION_SAVE;
    }
    /**
     * ������Ʒ��Csp
     */
    public String saveProductToCsp() {
        if (obj != null) {
            try {
                cspLogicInterface.saveProductToCsp(productIds, obj.getId());
                writeSysLog("�����Ʒ��ID��"+productIds+",CSP��Id"+obj.getId());
            } catch (Exception e) {
                setSuccess(false);
                addActionError("�������ʱ��������:" + e.getMessage());
            }
        }
        return Constants.ACTION_SAVE;
    }
    /**
     * ����ģ�嵽Csp
     */
    public String saveModuleToCsp() {
        if (obj != null) {
            try {
                    if(defaultModuleId==null){
                       defaultModuleId=Long.parseLong("0");
                    }
                    cspLogicInterface.saveModuleToCsp(cspModules,moduleIds,keyId,defaultModuleId);
              writeSysLog("����ģ�壺"+defaultModuleId+",CSP��Id:"+keyId);
            } catch (Exception e) {
                setSuccess(false);
                addActionError("�������ʱ��������:" + e.getMessage());
            }
        }
        return Constants.ACTION_SAVE;
    }
    //����ѡ�е�Ƶ����Ϣ��cspId һ�𱣴�
    public  String saveCspToChannel(){
        //�õ�ѡ�е��ַ���  �� cspId
        //String chooseChannel = getRequestParam("chooseChannel", "");
        String Id=getRequestParam("keyId","");
        long CspId=Long.parseLong(Id);
        boolean saveCspChannel=false;
        saveCspChannel= cspChannelLogicInterface.saveCspChannel(chooseChannel,CspId);
        if(saveCspChannel){
            setSuccess(true);
        }  else {
            setSuccess(false);
        }
        return "cspChannelList";
    }
    /**
     * ��������˵�Csp
     */
    public String saveAuditorToCsp() {
        cspLogicInterface.saveAuditorToCsp(admins,adminIds,keyId);
        writeSysLog("��������˵�ID��" + adminIds+",CSP��Id:"+keyId);
        super.addActionMessage("�ɹ��������ݣ�");
        return Constants.ACTION_SAVE;
    }


    public List<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getDeviceIdsString() {
        return deviceIdsString;
    }

    public void setDeviceIdsString(String deviceIdsString) {
        this.deviceIdsString = deviceIdsString;
        if (deviceIdsString != null) {
            String[] ids = deviceIdsString.split(",");
            deviceIds = new ArrayList<Long>();
            if (ids != null) {
                for (String id : ids) {
                    deviceIds.add(Long.parseLong(id));
                }
            }
        }
    }

    public List<Long> getCpIds() {
        return cpIds;
    }

    public void setCpIds(List<Long> cpIds) {
        this.cpIds = cpIds;
    }

    public String getCpIdsString() {
        return cpIdsString;
    }

    public void setCpIdsString(String cpIdsString) {
        this.cpIdsString = cpIdsString;
        if (cpIdsString != null) {
            String[] ids = cpIdsString.split(",");
            cpIds = new ArrayList<Long>();
            if (ids != null) {
                for (String id : ids) {
                    cpIds.add(Long.parseLong(id));
                }
            }
        }

    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;

    }

    public String getProductIdsString() {
        return productIdsString;
    }

    public void setProductIdsString(String productIdsString) {
        this.productIdsString = productIdsString;
        if (productIdsString != null) {
            String[] ids = productIdsString.split(",");
            productIds = new ArrayList<Long>();
            if (ids != null) {
                for (String id : ids) {
                    productIds.add(Long.parseLong(id));
                }
            }
        }
    }

    public String getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(String moduleIds) {
        this.moduleIds = moduleIds;
    }



    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public String getAdminIdAndCspIdString() {
        return adminIdAndCspIdString;
    }

    public void setAdminIdAndCspIdString(String adminIdAndCspIdString) {
        long keyId = getRequestIntParam("keyId",0);
        this.adminIdAndCspIdString = adminIdAndCspIdString;
        if (adminIdAndCspIdString != null) {
            String[] adminIdAndCspIdArray = adminIdAndCspIdString.split(",");
            if (adminIdAndCspIdArray.length != 0) {
                admins = new ArrayList();
                for (int i = 0; i < adminIdAndCspIdArray.length; i++) {
                     boolean selected=false;
                    Admin admin1 = new Admin();
                    String[] adminIdAndCspId = adminIdAndCspIdArray[i].split("_");
                    if (adminIdAndCspId.length != 0) {
                       if(adminIdAndCspId.length>1){
                           selected=true;
                       }
                       CspAuditor cspAuditor = new CspAuditor();
                       cspAuditor.setCspId(keyId);
                        for (int j = 0; j < adminIdAndCspId.length; j++) {
                            if (j == 0) {
                                cspAuditor.setAdminId(Long.parseLong(adminIdAndCspId[j]));
                                if(adminIds==null||adminIds==""){
                                   adminIds=adminIdAndCspId[j];
                                }else{
                                    adminIds+=","+adminIdAndCspId[j];
                                }
                            } else {
                                switch (Integer.parseInt(adminIdAndCspId[j].toString())) {
                                    case 1:
                                        cspAuditor.setSpOnline(Long.parseLong("1"));
                                        break;
                                    case 2:
                                        cspAuditor.setSpOffline(Long.parseLong("1"));
                                        break;
                                    case 3:
                                        cspAuditor.setCpOnline(Long.parseLong("1"));
                                        break;
                                    case 4:
                                        cspAuditor.setCpOffline(Long.parseLong("1"));
                                        break;
                                }

                            }

                        }
                         admin1.setCspAuditor(cspAuditor);
                    }
                    if(selected){
                      admins.add(admin1);  
                    }

                }

            }

        }
    }

    public List<CspAuditor> getCspAuditors() {
        return cspAuditors;
    }

    public void setCspAuditors(List<CspAuditor> cspAuditors) {
        this.cspAuditors = cspAuditors;
    }

    public String getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(String adminIds) {
        this.adminIds = adminIds;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public String getCspBySp(){
        try{
            long spId = getRequestIntParam("spId",-1);

            if (objs==null){
                objs = new ArrayList();
            }
            Csp sp = cspLogicInterface.get(spId);
            if (sp!=null && sp.getIsCp()!=null && sp.getIsCp()==1){
                objs.add(sp);
            }

            objs.addAll(HibernateUtils.findAll(this.baseLogicInterface.getSession(),
                    "from Csp c where c.id in (select cc.cspId from CspCsp cc where cc.masterCspId="+spId+")"));


        }catch(Exception e){
            e.printStackTrace();
        }
        return Constants.ACTION_LIST;
    }

    public String getCspByAuditAdmin() {
        try{
            long adminId = getRequestIntParam("adminId",-1);
            String auditType = getRequestParam("auditType","");
            String cspType = getRequestParam("cspType",""); 
            if (objs==null){
                objs = new ArrayList();
            }

            objs=HibernateUtils.findAll(this.baseLogicInterface.getSession(),
                    "from Csp c where c."+cspType+"=1 and c.id in (select ca.cspId from CspAuditor ca where ca."+auditType+"=1 and ca.adminId="+adminId+")");

        }catch(Exception e){
            e.printStackTrace();
        }
        return Constants.ACTION_LIST;
    }


    public void setDefaultModuleId(Long defaultModuleId) {
        this.defaultModuleId = defaultModuleId;
    }


    public List<CspModule> getCspModules() {
        return cspModules;
    }

    public void setCspModules(List<CspModule> cspModules) {
        this.cspModules = cspModules;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getModuleIdAndCspIdString() {
        return moduleIdAndCspIdString;
    }

    public void setModuleIdAndCspIdString(String moduleIdAndCspIdString) {
         long keyId = getRequestIntParam("keyId",0);
        this.moduleIdAndCspIdString = moduleIdAndCspIdString;
        if(moduleIdAndCspIdString!=null){
           String[] moduleIdAndCspIdArray =moduleIdAndCspIdString.split(",");
           if(moduleIdAndCspIdArray.length!=0){
                cspModules = new ArrayList<CspModule>();
                 for(int i=0;i<moduleIdAndCspIdArray.length;i++){
                     String[] moduleIdAndCspId = moduleIdAndCspIdArray[i].split("_");
                     if(moduleIdAndCspId.length!=0){
                            String moduleId = moduleIdAndCspId[0];
                         if(moduleIds==null||moduleIds==""){
                            moduleIds = moduleId;
                         }else{
                            moduleIds+=","+moduleId;
                         }

                         if(!moduleIdAndCspId[1].equals("0")){
                             CspModule cspModule =new CspModule();
                             cspModule.setCspId(keyId);
                             cspModule.setModuleId(Long.parseLong(moduleIdAndCspId[0]));
                             if(defaultModuleId!=null){
                                if(defaultModuleId.toString().equals(moduleId)){
                                   cspModule.setIsDefault(Long.parseLong("1"));
                                }

                          }
                             cspModules.add(cspModule);
                         }
                     }

                 }
           }
        }
    }

    public String getModuleIdsString() {
        return moduleIdsString;
    }

    public void setModuleIdsString(String moduleIdsString) {
        this.moduleIdsString = moduleIdsString;
    }

    public UserBuy getUserBuy() {
        return userBuy;
    }

    public void setUserBuy(UserBuy userBuy) {
        this.userBuy = userBuy;
    }


    public String searchCspName(){
          obj = cspLogicInterface.get(cspId);
          return Constants.ACTION_VIEW;
    }

    public long getCspId() {
        return cspId;
    }

    public void setCspId(long cspId) {

        this.cspId = cspId;
    }
}
