package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.system.logic.logicInterface.LoadBalanceWorker;

/**
 * Created by xjliu on 2014/11/25
 * 潍柴的调度方式.
 */
public class GSLB4WeiChai implements LoadBalanceWorker {
    private String gslbPolicy;
    public GSLB4WeiChai(){
        gslbPolicy = ConfigManager.getInstance().getConfig("system.gslb.policy",
                "120.27.29.191->default;10.0.66.11->119.191.61.30,123.133.64.102,123.168.22.6,219.146.118.238,172.17.,10.0.");
    }
    public String doGslb(String gslbUrl,String userIp){
        if(gslbPolicy!=null){
            String[] policies = gslbPolicy.split(";");
            String defaultHost = null;
            boolean urlSeted = false;
            for(String policy:policies){
                String[] hostAndIps = policy.split("->");
                if(hostAndIps.length>=2){
                    String[] ips = hostAndIps[1].split(",");
                    for(String ip:ips){
                        //支持子网方式
                        if(userIp.contains(ip)){
                            gslbUrl = gslbUrl.replace("hls.weichai.com",GSLB4Hls.getRandomOf(hostAndIps[0].split(",")));
                            urlSeted = true;
                            break;
                        }else if(ip.equals("default")){
                            defaultHost = hostAndIps[0];
                        }
                    }
                }
            }
            if(!urlSeted){
                if(defaultHost==null){
                    defaultHost = "120.27.29.191";//潍柴
                }
                gslbUrl = gslbUrl.replace("hls.weichai.com",GSLB4Hls.getRandomOf(defaultHost.split(",")));
            }
        }
        return gslbUrl;
    }
    public String getHttpGslbUrl(String requestUrl, String remoteAddr) {
        if(requestUrl.contains("hls.weichai.com")){
            return doGslb(requestUrl,remoteAddr);
        }else{
            return requestUrl;
        }
    }
    public void shutdown(){

    }
}
