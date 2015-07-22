package com.fortune.rms.business.train.logic.logicImpl;

import com.fortune.common.business.base.dao.BaseDaoInterface;
import com.fortune.common.business.base.logic.BaseLogicImpl;
import com.fortune.rms.business.ad.logic.logicInterface.AdLogLogicInterface;
import com.fortune.rms.business.log.logic.logicInterface.VisitLogLogicInterface;
import com.fortune.rms.business.log.model.VisitLog;
import com.fortune.rms.business.train.dao.daoInterface.TrainDaoInterface;
import com.fortune.rms.business.train.logic.logicInterface.TrainLogicInterface;
import com.fortune.rms.business.train.model.Train;
import com.fortune.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("trainLogicInterface")
public class TrainLogicImpl extends BaseLogicImpl<Train> implements TrainLogicInterface {
    private AdLogLogicInterface adLogLogicInterface;
    private VisitLogLogicInterface visitLogLogicInterface;
    private TrainDaoInterface trainDaoInterface;

    /**
     * @param trainDaoInterface the adDaoInterface to set
     */
    @Autowired
    public void setTrainDaoInterface(TrainDaoInterface trainDaoInterface) {
        this.trainDaoInterface = trainDaoInterface;
        baseDaoInterface = (BaseDaoInterface) this.trainDaoInterface;
    }

    @Autowired
    public void setAdLogLogicInterface(AdLogLogicInterface adLogLogicInterface) {
        this.adLogLogicInterface = adLogLogicInterface;
    }

    @Autowired
    public void setVisitLogLogicInterface(VisitLogLogicInterface visitLogLogicInterface) {
        this.visitLogLogicInterface = visitLogLogicInterface;
    }

    public List<String> importLog(File logFile) {
        List<String> result = new ArrayList<String>();
        if (logFile.exists()) {
            result.add("文件大小：" + StringUtils.formatBytes(logFile.length()) + ",日期：" + StringUtils.date2string(logFile.lastModified()));
            int lineCount = 0, recordCount = 0, adCount = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(logFile));
                String line = br.readLine();
                while (null != line) {
                    String[] data = line.split(" ");
                    if ("ad".equals(data[0])) {
                        if(data.length>=5){
                            long adId = StringUtils.string2long(data[1],0);
                            long adRangeId = StringUtils.string2long(data[2],0);
                            long contentId = StringUtils.string2long(data[3],0);
                            long channelId = StringUtils.string2long(data[4],0);
                            adLogLogicInterface.createAdLog(adId,adRangeId,contentId,channelId,1L);
                            adCount++;
                        }else{
                            result.add("广告日志不符合规范："+line);
                        }
                    } else if ("pl".equals(data[0])) {
                        if(data.length>=7){
                            VisitLog log = new VisitLog();
                            log.setUserIp(data[1]);
                            String url = data[2];
                            log.setContentId(StringUtils.getLongParameter(url,"contentId",0));
                            log.setChannelId(StringUtils.getLongParameter(url,"channelId",0));
                            log.setCpId(StringUtils.getLongParameter(url,"cpId",1));
                            log.setSpId(StringUtils.getLongParameter(url,"spId",1));
                            log.setUserAgent(data[3]);
                            log.setStartTime(StringUtils.string2date(data[4]));
                            log.setStartTime(StringUtils.string2date(data[5]));
                            log.setBytesSend(StringUtils.string2long(data[6],-1));
                            log.setContentPropertyId(StringUtils.getLongParameter(url,"contentPropertyId",-1));
                            log.setUrl(url);
                            visitLogLogicInterface.save(log);
                            recordCount++;
                        }else{
                            result.add("播放日志不符合规范："+line);
                        }
                    }else{
                        result.add("格式不正确："+line);
                    }
                    lineCount++;
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.add("处理了" + lineCount + "行，添加日志数据：" + recordCount + ",广告日志：" + adCount);
        } else {
            result.add("文件不存在：" + logFile.getAbsolutePath());
        }
        return result;
    }
}
