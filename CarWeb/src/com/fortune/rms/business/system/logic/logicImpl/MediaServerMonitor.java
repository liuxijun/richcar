package com.fortune.rms.business.system.logic.logicImpl;

import com.fortune.common.business.base.logic.ConfigManager;
import com.fortune.rms.business.system.logic.logicInterface.LoadBalanceWorker;
import org.apache.log4j.Logger;
/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-13
 * Time: ����3:12
 * ��HLS������������
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
            logger.info("ϵͳ����������HLS��ʽ����");
            worker = new GSLB4Hls();
        }else{
            logger.info("ϵͳ������������ط�ʽ(Ϋ������)��ʽ����");
            worker = new GSLB4WeiChai();
        }
    }

    public String getHttpGslbUrl(String requestUrl,String remoateAddr){
        return worker.getHttpGslbUrl(requestUrl,remoateAddr);
    }
    public void shutdown(){
        logger.info("ϵͳ�ر�");
        worker.shutdown();
    }
}
