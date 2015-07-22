package com.fortune.rms.business.user.logic.logicImpl;

import com.fortune.rms.business.user.logic.logicInterface.UserBindingInterface;
import com.fortune.rms.business.user.model.AllResp;
import com.fortune.util.AppConfigurator;
import org.springframework.stereotype.Service;

@Service("userBindingInterface")
public class UserBindingImpl extends XmlHelper implements UserBindingInterface {
    public AllResp userBindingCheckRequest(String userId,Integer userType) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<UserBindingCheckReq><UserID>"+userId+"</UserID><UserType>"+userType+"</UserType></UserBindingCheckReq>";
        String urlStr = AppConfigurator.getInstance().getConfig("Ericsson.binding.listUrl","http://61.55.150.16/IShareFT/Fortune.Interface/FT_UserBindingCheckReq.aspx");
        return getSimpleResult(urlStr,xml);
    }
}
