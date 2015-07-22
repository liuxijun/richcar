package com.fortune.rms.business.system.logic.logicInterface;

/**
 * Created by xjliu on 2014/11/25.
 * 负载均衡的接口
 */
public interface LoadBalanceWorker {
    public String getHttpGslbUrl(String requestUrl, String remoateAddr);
    public void shutdown();
}
