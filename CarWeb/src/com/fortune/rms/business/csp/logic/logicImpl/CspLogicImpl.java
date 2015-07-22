package com.fortune.rms.business.csp.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.dao.daoInterface.CspDaoInterface;
import com.fortune.rms.business.csp.logic.logicInterface.*;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.logic.logicInterface.ModuleLogicInterface;
import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.product.logic.logicImpl.ProductLogicImpl;
import com.fortune.rms.business.product.logic.logicInterface.ProductLogicInterface;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.system.logic.logicImpl.DeviceLogicImpl;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cspLogicInterface")
public class CspLogicImpl extends BaseLogicImpl<Csp>
        implements
        CspLogicInterface {
    public final static int CSP_ONLINE = 1;
    public final static int CSP_OFFLINE = 0;
    private CspDaoInterface cspDaoInterface;
    private CspDeviceLogicInterface cspDeviceLogicInterface;
    private CspCspLogicInterface cspCspLogicInterface;
    private CspProductLogicInterface cspProductLogicInterface;
    private CspModuleLogicInterface cspModuleLogicInterface;
    private CspAuditorLogicInterface cspAuditorLogicInterface;
    private AdminLogicInterface adminLogicInterface;
    private ModuleLogicInterface moduleLogicInterface;
    private DeviceLogicInterface deviceLogicInterface;
    private ProductLogicInterface productLogicInterface;


    /**
     * @param cspDaoInterface the cspDaoInterface to set
     */
    @Autowired
    public void setCspDaoInterface(CspDaoInterface cspDaoInterface) {
        this.cspDaoInterface = cspDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.cspDaoInterface;
    }

    @Autowired
    public void setModuleLogicInterface(ModuleLogicInterface moduleLogicInterface) {
        this.moduleLogicInterface = moduleLogicInterface;
    }

    @Autowired
    public void setAdminLogicInterface(AdminLogicInterface adminLogicInterface) {
        this.adminLogicInterface = adminLogicInterface;
    }

    @Autowired
    public void setCspModuleLogicInterface(CspModuleLogicInterface cspModuleLogicInterface) {
        this.cspModuleLogicInterface = cspModuleLogicInterface;
    }

    @Autowired
    public void setCspProductLogicInterface(CspProductLogicInterface cspProductLogicInterface) {
        this.cspProductLogicInterface = cspProductLogicInterface;
    }

    @Autowired
    public void setCspCspLogicInterface(CspCspLogicInterface cspCspLogicInterface) {
        this.cspCspLogicInterface = cspCspLogicInterface;
    }

    @Autowired
    public void setCspAuditorLogicInterface(CspAuditorLogicInterface cspAuditorLogicInterface) {
        this.cspAuditorLogicInterface = cspAuditorLogicInterface;
    }

    @Autowired
    public void setCspDeviceLogicInterface(CspDeviceLogicInterface cspDeviceLogicInterface) {
        this.cspDeviceLogicInterface = cspDeviceLogicInterface;
    }

    @Autowired
    public void setDeviceLogicInterface(DeviceLogicInterface deviceLogicInterface) {
        this.deviceLogicInterface = deviceLogicInterface;
    }

    @Autowired
    public void setProductLogicInterface(ProductLogicInterface productLogicInterface) {
        this.productLogicInterface = productLogicInterface;
    }

    public List<Device> getDevicesByCspId(long cspId) {
        List<Device> devices = deviceLogicInterface.getDevicesOfStatus(DeviceLogicImpl.DEVICE_ONLINE);
        if (cspId > 0) {
            List<Device> cspDevices = cspDeviceLogicInterface.getDevicesByCspId(cspId);
            if (cspDevices != null && cspDevices.size() > 0) {
                for (Device d : devices) {
                    d.setSelected(false);
                    for (Device cd : cspDevices) {
                        if (d.getId() == cd.getId()) {
                            d.setSelected(true);
                            cspDevices.remove(cd);
                            break;
                        }
                    }
                }
            }
        }
        return devices;
    }

    public void saveDeviceToCsp(List<Long> deviceIds, long cspId) {
        this.cspDeviceLogicInterface.saveDeviceToCsp(deviceIds, cspId);
    }
    public List<Csp> getCpsOfStatus(int status){
        return this.cspDaoInterface.getCpsOfStatus(status);
    }

    public boolean existSpId(String spId) {
        return this.cspDaoInterface.existSpId(spId);
    }

    public Csp getCspBySpId(String spId) {
        return this.cspDaoInterface.getCspBySpId(spId);
    }

    public List<Csp> getCpsBySpId(long spId) {
        List<Csp> csps = cspDaoInterface.getCpsOfStatus(CspLogicImpl.CSP_ONLINE);

        if (spId > 0) {
            List<Csp> spCps = cspCspLogicInterface.getCpsBySpId(spId);
            if (spCps != null && spCps.size() > 0) {
                for (Csp c : csps) {
                    c.setSelected(false);
                    for (Csp c1 : spCps) {
                        if (c.getId() == c1.getId()) {
                            c.setSelected(true);
                            spCps.remove(c1);
                            break;
                        }
                    }
                }
            }
        }

        return csps;
    }

    public List<Csp> getCspByCpCode(String cpCode) {
        List<Csp> csps = cspDaoInterface.getCspByCpCode(cpCode);
        return csps;
    }

    public void saveCspToCsp(List<Long> cspIds, long cspId) {
        this.cspCspLogicInterface.saveCspToCsp(cspIds, cspId);
    }

    public List<Product> getProductsBySpId(long spId) {
        List<Product> products = productLogicInterface.getProductsOfStatus(ProductLogicImpl.PRODUCT_ONLINE);
        if (spId > 0) {
            List<Product> cspProducts = cspProductLogicInterface.getProductsBySpId(spId);
            if(cspProducts != null && cspProducts.size()>0){
                for (Product p : products) {
                    p.setSelected(false);
                    for (Product p1 : cspProducts) {
                        if (p.getId() == p1.getId()) {
                            p.setSelected(true);
                            cspProducts.remove(p1);
                            break;
                        }
                    }
                }
            }
        }
        return products;
    }

    public void saveProductToCsp(List<Long> productIds, long cspId) {
        this.cspProductLogicInterface.saveProductToCsp(productIds, cspId);
    }

    public List<Module> getAllModuleWithCspCheck(long cspId) {
        List<Module> all = cspModuleLogicInterface.getModuleOfCsp(-1);
        List<Module> cspModules = new ArrayList<Module>();
        if (cspId > 0) {
            cspModules = cspModuleLogicInterface.getModuleOfCsp(cspId);
        }
        for (Module m : all) {
            m.setSelected(false);
            for (Module m1 : cspModules) {
                if (m.getId() == m1.getId()) {
                    m.setSelected(true);
                    cspModules.remove(m1);
                    break;
                }
            }
        }
        return all;
    }

    public Long getDefaultModuleId(long cspId) {
        return cspModuleLogicInterface.getDefaultModuleId(cspId);
    }

    public void saveModuleToCsp(List<CspModule> cspModules, String moduleIds, long cspId, long defaultModuleId) {
        this.cspModuleLogicInterface.saveModuleToCsp(cspModules, moduleIds, cspId, defaultModuleId);
    }

    public void saveAuditorToCsp(List<Admin> admin, String adminIds, long cspId) {
        this.cspAuditorLogicInterface.saveAuditorToCsp(admin, adminIds, cspId);
    }

    public List<Csp> getAllSp() {
        return this.cspDaoInterface.getAllSp();
    }

    public List<Csp> getAllCp() {
        return this.cspDaoInterface.getAllCp();
    }

    public Csp getCspByCspId(long cspId) {
        return this.cspDaoInterface.get(cspId);
    }
    public  Csp getCpBySpNmae(String name){
        return this.cspDaoInterface.getCspIdByName(name);
    }

    public Csp getSpIdByContentId(long contentId) {
        return cspDaoInterface.getSpIdByContentId(contentId);
    }

    public Csp getSpIdByProductId(long productId) {
        return cspDaoInterface.getSpIdByProductId(productId);
    }

    public void remove(Csp csp){
        cspDaoInterface.remove(csp);
    }
}
