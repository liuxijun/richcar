package com.fortune.test.Testor;

import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.util.SortUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-11-4
 * Time: 17:13:49
 * ≤‚ ‘≈≈–Ú
 */
public class TestSort {
    List<ContentProperty> ps = new ArrayList<ContentProperty>();
    Logger logger = Logger.getLogger(this.getClass());
    public void test(){
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs03.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs12.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs04.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs13.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs05.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs14.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs06.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs15.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs07.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs16.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs08.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs17.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs09.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs18.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs10.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs19.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs20.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs01.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs02.wmv","",""));
        ps.add(new ContentProperty(-1,-1L,-1L,-1L,"/dianshiju/jdfs/jdfs11.wmv","",""));
        ps = SortUtils.sortArray(ps,"stringValue","asc");
        String v="";
        for(int i=0;i<ps.size();i++){
            ContentProperty cp = ps.get(i);
            String cValue = cp.getStringValue();
            if(i>0){
                logger.debug("'"+v+"' compare '"+cValue+"' = "+v.compareTo(cValue));
            }
            v = cValue;
        }
    }
    public static void main(String[] arg){
        TestSort ts = new TestSort();
        ts.test();
    }
}
