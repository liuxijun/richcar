package com.fortune.rms.business.system.logic.logicInterface;

/**
 * Created by xjliu on 2014/11/25.
 * ���ؾ���Ľӿ�
 */
public interface LoadBalanceWorker {
    public String getHttpGslbUrl(String requestUrl, String remoateAddr);
    public void shutdown();
}
