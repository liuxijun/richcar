package com.fortune.rms.business.csp.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.rms.business.csp.model.Csp;
import com.fortune.rms.business.csp.model.CspModule;
import com.fortune.rms.business.module.model.Module;
import com.fortune.rms.business.product.model.Product;
import com.fortune.rms.business.system.model.Device;

import java.util.List;

public interface CspLogicInterface extends BaseLogicInterface<Csp> {
    public final static Long MUST_AUDIT=1L;
    public static final Long TYPE_IS_SP=1l;
    public static final Long TRUE=1l;
    public List<Device> getDevicesByCspId(long cspId);
     public void saveDeviceToCsp(List<Long> deviceIds, long cspId);
     public List<Csp> getCpsBySpId(long spId);
     public List<Csp> getCspByCpCode(String cpCode);
     public List<Csp> getCpsOfStatus(int status);
     public boolean existSpId(String spId);
     public Csp getCspBySpId(String spId);
     public void saveCspToCsp(List<Long> cspIds, long cspId);
     public List<Product> getProductsBySpId(long spId);
     public void saveProductToCsp(List<Long> productIds, long cspId);
     public List<Module> getAllModuleWithCspCheck(long cspId);
     public Long getDefaultModuleId(long cspId);
     public void saveModuleToCsp(List<CspModule> cspModules, String moduleIds, long cspId, long defaultModuleId);

    public void saveAuditorToCsp(List<Admin> admin, String adminIds, long cspId);

    public List<Csp> getAllSp();
     public List<Csp> getAllCp();

     public Csp getCspByCspId(long cspId);
     public Csp getCpBySpNmae(String name);
    public Csp getSpIdByContentId(long contentId);
    public Csp getSpIdByProductId(long productId);
}
