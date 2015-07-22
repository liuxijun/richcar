package com.fortune.util.timer;

import com.fortune.rms.business.publish.model.RelatedProperty;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import com.fortune.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-13
 * Time: 18:05:02
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelated  extends TimerBase {

    public void run() {
        try {

            System.out.println("run timer:"+ StringUtils.date2string(new Date()));


            {

                hibernateUtils.executeUpdate("delete from RelatedProperty rp");

                String actorStr = "";
                List actors = hibernateUtils.findAll("select distinct c.actors from Content c ");

                for (int i=0;i<actors.size(); i++){
                    Object obj = actors.get(i);
                    if (obj==null){
                        continue;
                    }
                    String s = obj.toString();
                    String ss[] = s.split(";");
                    if (ss!=null){
                        for (int j=0; j<ss.length; j++){
                            if (actorStr.indexOf(ss[j])==-1){
                                RelatedProperty rp = new RelatedProperty();
                                rp.setPropertyId(5055l);
                                rp.setRelatedId(24279l);
                                rp.setPropertyValue(ss[j]);

                                hibernateUtils.save(rp);
                                actorStr+=ss[j];
                            }
                        }
                    }

                }

            }

            {

                hibernateUtils.executeUpdate("delete from RelatedPropertyContent rp");

                List rps = hibernateUtils.findAll("select rp from RelatedProperty rp where rp.relatedId=24279 ");

                for (int i=0;i<rps.size(); i++){
                    RelatedProperty rp = (RelatedProperty)rps.get(i);

                    List cids = hibernateUtils.findAll("select c.id,cc.cspId from Content c,ContentCsp cc where c.id=cc.contentId and c.actors like ? order by c.monthVisitCount desc",new Object[]{"%"+rp.getPropertyValue()+"%"});
                    for (int j=0;j<cids.size();j++){
                        Object objs[] = (Object[])cids.get(j);
                        long contentId = (Long)objs[0];
                        long spId = (Long)objs[1];
                        RelatedPropertyContent rpc = new RelatedPropertyContent();
                        rpc.setRelatedPropertyId(rp.getId());
                        rpc.setCspId(spId);
                        rpc.setContentId(contentId);
                        rpc.setDisplayOrder((long)(j+1));
                        hibernateUtils.save(rpc);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}