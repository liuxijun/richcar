package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.system.logic.logicInterface.LoadBalanceWorker;
import org.apache.log4j.Logger;
/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-13
 * Time: 下午3:12
 * 对HLS服务器做调度
 */
public class MediaServerMonitor {
    private Logger logger = Logger.getLogger(this.getClass());
    private static MediaServerMonitor instance = new MediaServerMonitor();
    public static MediaServerMonitor getInstance(){
        return instance;
    }
    LoadBalanceWorker worker;
    private MediaServerMonitor(){
        if("hls".equals(ConfigManager.getInstance().getConfig("system.lsbMethod", "hls"))){
            logger.info("系统启动，采用HLS方式调度");
            worker = new GSLB4Hls();
        }else{
            logger.info("系统启动，采用异地方式(潍柴混合网)方式调度");
            worker = new GSLB4WeiChai();
        }
    }

    public String getHttpGslbUrl(String requestUrl,String remoateAddr){
        return worker.getHttpGslbUrl(requestUrl,remoateAddr);
    }
    public void shutdown(){
        logger.info("系统关闭");
        worker.shutdown();
    }
}
