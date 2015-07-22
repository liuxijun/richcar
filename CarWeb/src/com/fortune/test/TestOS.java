package com.fortune.test;

import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.content.logic.logicInterface.ContentPropertyLogicInterface;
import com.fortune.rms.business.content.model.ContentProperty;
import com.fortune.rms.business.module.logic.logicInterface.PropertyLogicInterface;
import com.fortune.rms.business.module.model.Property;
import com.fortune.rms.business.user.logic.logicInterface.UserLogicInterface;
import com.fortune.util.JsonUtils;
import com.fortune.util.SpringUtils;
import com.fortune.util.XmlUtils;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liupeng
 * Date: 12-7-9
 * Time: ����10:38
 *
 */
public class TestOS {
    public static void outPutXmlTag(String parentTag,Element node){
        List attributes = node.attributes();
        System.out.println(parentTag+"{");
        for(Object a:attributes){
            Node attribute = (Node) a;
            System.out.println(parentTag+"["+attribute.getName()+"]="+attribute.getText());
        }
        
        List childs = node.elements();
        for(Object n:childs){
            Element child = (Element) n;
            outPutXmlTag("\t"+parentTag+"."+child.getName(),child);
        }
        System.out.println(parentTag+"}");
        String str="<div id=\"flashObj\" style=\"width: 640px;height: 480px;background-color: #000000\">\n" +
                "    <object class='play' classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='640' height='480' id='player' align='middle'>\n" +
                "        <param name='movie' value='../../swfs/player.swf?url=getPlayUrl.jsp&pid=1&uid=2'/>\n" +
                "        <param name='quality' value='high'/>\n" +
                "        <param name='bgcolor' value='#000000' />\n" +
                "        <param name='play' value='true' />\n" +
                "        <param name='allowFullScreen' value='true' />\n" +
                "        <param name='loop' value='true' />\n" +
                "        <param name='wmode' value='window' />\n" +
                "        <param name='scale' value='showall' />\n" +
                "        <param name='menu' value='true' />\n" +
                "        <param name='devicefont' value='false' />\n" +
                "        <param name='salign' value='' />\n" +
                "        <param name='allowScriptAccess' value='sameDomain'/> &lt;!&ndash;[if !IE]>&ndash;&gt;\n" +
                "        <object type='application/x-shockwave-flash' data='../../swfs/player.swf?url=getPlayUrl.jsp&pid=1&uid=2'\n" +
                "                width='640' height='480' id='player1'>\n" +
                "            <param name='movie' value='../../swfs/player.swf?url=mediaUrl.xml&pid=1&uid=2'/>\n" +
                "            <param name='quality' value='high' />\n" +
                "            <param name='bgcolor' value='#000000' />\n" +
                "            <param name='play' value='true' />\n" +
                "            <param name='loop' value='true' />\n" +
                "            <param name='wmode' value='window' />\n" +
                "            <param name='scale' value='showall' />\n" +
                "            <param name='menu' value='true' />\n" +
                "            <param name='devicefont' value='false' />\n" +
                "            <param name='salign' value='' />\n" +
                "            <param name='allowFullScreen' value='true' />\n" +
                "            <param name='allowScriptAccess' value='sameDomain' />\n" +
                "        </object> &lt;!&ndash;<![endif]&ndash;&gt;\n" +
                "    </object>\n";
    }
    public static void main(String args[]){
        try {
            XmlUtils.getRootFromXmlFile("E:/temp/1.xml");
            XmlUtils.getRootFromXmlFile("E:/temp/2.xml");
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}
