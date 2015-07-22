package com.fortune.util.timer;

import com.fortune.rms.business.publish.model.RelatedProperty;
import com.fortune.rms.business.publish.model.RelatedPropertyContent;
import com.fortune.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2011-7-14
 * Time: 11:53:02
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelatedBrowse  extends TimerBase {

    public void run() {
        try {

            System.out.println("run timer:"+ StringUtils.date2string(new Date()));

            {
                hibernateUtils.executeUpdate("delete from ContentRelated cr");

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH,-1);
                Date startTime = calendar.getTime();

                List list1 = hibernateUtils.findAll("select distinct c.id,vl.contentId from Content c,VisitLog vl where vl.startTime>? and vl.userId in (select vl1.userId from VisitLog vl1 where vl1.contentId=c.id) group by c.id,vl.contentId order by count(*) desc",new Object[]{startTime});
                if (list1!=null){
                    for (int i=0; i<list1.size(); i++){
                        Object objs[] = (Object[])list1.get(i);
                        long contentId = (Long)objs[0];
                        long relatedContentId = (Long)objs[1];
                        com.fortune.rms.business.content.model.ContentRelated cr = new com.fortune.rms.business.content.model.ContentRelated();
                        cr.setContentId(contentId);
                        cr.setRelatedContentId(relatedContentId);
                        cr.setRelatedId(-1l);
                        cr.setDisplayOrder((long)i);
                        hibernateUtils.save(cr);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}