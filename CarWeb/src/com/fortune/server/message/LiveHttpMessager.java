package com.fortune.server.message;

import com.fortune.util.XmlUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xjliu on 14-6-18.
 * 和HLS服务器通信
 */
public class LiveHttpMessager extends ServerMessager{
    public List<Map<String,String>> getLives(String ip,int port,String pwd){
        List<Map<String,String>> channels = new ArrayList<Map<String,String>>();
        String result = postToHost("http://"+ip+":"+port+"/ep-get/channel?password="+pwd,"");
        //用xml解析器解析
        if(result!=null){
            result = result.replaceAll("&nbsp;","--");
            result = result.replaceAll("&amp;","--");
            result = result.replaceAll("&","-");
            result = result.replaceAll("<br>","<br/>");
            result = result.replaceAll("border=1","border=\"1\"");
        }
        Element root = XmlUtils.getRootFromXmlStr(result);
        if(root==null){
            logger.error("无法解释文本内容："+result);
        }else{
            List dataArray = root.selectNodes("/html/body/table/tr");
            //第一行是表格的头，跳过
            if(dataArray!=null&&dataArray.size()>1){
                Node firstLine =(Node) dataArray.get(0);
                List<String> fields = new ArrayList<String>();
                if(firstLine!=null){
                    List children = firstLine.selectNodes("td");
                    for(Object child:children){
                        fields.add(((Node)child).getText());
                    }
                }
                for(int i=1;i<dataArray.size();i++){
                    Node node = (Node) dataArray.get(i);
                    if(node!=null){
                        List children = node.selectNodes("td");
                        if(children!=null&&children.size()>=10){
                            Map<String,String> channel = new HashMap<String,String>();
                            for(int j=0;j<fields.size();j++){
                                channel.put(fields.get(j),((Node)children.get(j)).getText().trim());
                            }
                            channels.add(channel);
                        }
                    }
                }
            }
        }
        return channels;
    }
}
