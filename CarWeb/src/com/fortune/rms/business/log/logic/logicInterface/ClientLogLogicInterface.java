package com.fortune.rms.business.log.logic.logicInterface;

import com.fortune.common.business.base.logic.BaseLogicInterface;
import com.fortune.rms.business.log.model.ClientLog;

import java.util.Date;
import java.util.List;


public interface ClientLogLogicInterface extends BaseLogicInterface<ClientLog> {
    public static final Integer DOWNLOAD_TYPE_INHE_PLAYER=1;
    public static final Integer DOWNLOAD_TYPE_WOXIN=2;
    public static final Integer DOWNLOAD_TYPE_WOXUAN=3;
    public static final Long STATUS_DOWNLOAD=1L;
    public static final Long STATUS_ACTIVITY=2L;
    public boolean checkPhoneCode(String phoneCode);
    public ClientLog saveLog(String userId, String ip, String userAgent, String clientVersion, String phoneCode,
                             int type, int areaId, Date time);
}
