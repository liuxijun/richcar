package com.fortune.rms.web.content;

import com.fortune.common.Constants;
import com.fortune.rms.business.content.logic.logicInterface.*;
import com.fortune.rms.business.content.model.*;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.product.logic.logicInterface.ServiceProductLogicInterface;
import com.fortune.rms.business.product.model.ServiceProduct;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.logic.logicInterface.RecommendLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.rms.business.publish.model.Recommend;
import com.fortune.util.HibernateUtils;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Namespace("/content")
@ParentPackage("default")
@Action(value = "contentCsp")
public class ContentCspAction extends BaseAction<ContentCsp> {
    private static final long serialVersionUID = 3243534534534534l;



    private ChannelLogicInterface channelLogicInterface;
    private ContentCspLogicInterface contentCspLogicInterface;
    private ContentChannelLogicInterface contentChannelLogicInterface;
    private ContentServiceProductLogicInterface contentServiceProductLogicInterface;
    private ContentLogicInterface contentLogicInterface;
    private ContentRecommendLogicInterface contentRecommendLogicInterface;
    private ServiceProductLogicInterface serviceProductLogicInterface;
    private RecommendLogicInterface recommendLogicInterface;
    private Long cspId;
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setContentChannelLogicInterface(ContentChannelLogicInterface contentChannelLogicInterface) {
        this.contentChannelLogicInterface = contentChannelLogicInterface;
    }

    public void setRecommendLogicInterface(RecommendLogicInterface recommendLogicInterface) {
        this.recommendLogicInterface = recommendLogicInterface;
    }


    public void setContentServiceProductLogicInterface(ContentServiceProductLogicInterface contentServiceProductLogicInterface) {
        this.contentServiceProductLogicInterface = contentServiceProductLogicInterface;
    }

    public void setContentRecommendLogicInterface(ContentRecommendLogicInterface contentRecommendLogicInterface) {
        this.contentRecommendLogicInterface = contentRecommendLogicInterface;
    }

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public void setContentLogicInterface(ContentLogicInterface contentLogicInterface) {
        this.contentLogicInterface = contentLogicInterface;
    }

    public void setServiceProductLogicInterface(ServiceProductLogicInterface serviceProductLogicInterface) {
        this.serviceProductLogicInterface = serviceProductLogicInterface;
    }

    public void setCspId(Long cspId) {
        this.cspId=cspId;
    }

    @SuppressWarnings("unchecked")

    public ContentCspAction() {
        super(ContentCsp.class);
    }
    public String formatChannelId(List list){
        String result="";
        for(int i =0;i<list.size();i++){
            result += list.get(i)+",";
        } if (result!=""){
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

    //保存数据
    @SuppressWarnings("unchecked")
    public String publish(){
        log.debug("in Publish");
        String channels = getRequestParam("channels","");
        String serviceProducts = getRequestParam("serviceProducts","");
        String recommends = getRequestParam("recommends","");
        String contentId = getRequestParam("contentId","");
        String keyIds = getRequestParam("keyIds","");
        String userOperation="";
        if("".equals(keyIds)){
           //keyIds为空时代表对已上线资源进行频道管理，keyIds不为空时代表在已审核资源列表对资源进行上线处理。
           userOperation="修改已上线资源";
        }else{
          userOperation="对已审核资源列表中的资源进行上线处理";
        }
        //删除旧数据
        String logInfo = admin.getLogin()+"("+admin.getRealname()+")"+userOperation+"：";
        try {
            if (!"".equals(contentId)) {
                //将关联表的所有数据拼接成一个String类型的字符串传入hql
                int cspId1 = cspId.intValue();
                List<Channel> list = channelLogicInterface.getAvailableChannelOfCsp(type,cspId1);
                List result2 = new ArrayList();
                for(Channel result1:list){
                    Long channelId = result1.getId();
                    result2.add(channelId);
                }
                String formatResult=formatChannelId(result2);
                contentCspLogicInterface.deleteAllContentChannel(contentId,cspId,formatResult);
                //查询关联表的每一张表然后删除
//              contentCspLogicInterface.deleteContentChannel(contentId,csp.getId());
//              contentCspLogicInterface.deleteAvailableChannelOfCsp(contentId,csp.getId());
//              contentCspLogicInterface.deleteChannelChildOfCsp(contentId,csp.getId());
                contentCspLogicInterface.deleteContentRecommend(contentId, cspId);
                contentCspLogicInterface.deleteContentServiceProduct(contentId, cspId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断contentId个数，循环存储
        if (!"".equals(contentId)) {
            String contentIdss[] = contentId.split(",");
            String contentNames = "";
            String channelNames = "";
            String serviceProductNames = "";
            String recommendNames="";
            TreeUtils tu = TreeUtils.getInstance();
            for (String contentId1 : contentIdss) {
                long cId = StringUtils.string2long(contentId1,-1);
                if(cId<=0){
                    continue;
                }
                Content c = contentLogicInterface.get(cId);
                contentNames+="《"+c.getName()+"》,id="+c.getId()+"。";
                if (!"".equals(channels)) {
                    String channelIds[] = channels.split(",");
                    for (String channels1 : channelIds) {
                        long channelId = StringUtils.string2long(channels1,-1);
                        if(channelId<=0){
                            continue;
                        }
                        Channel channel =(Channel) tu.getObject(Channel.class,channelId);
                        if(channel==null){
                            continue;
                        }
                        channelNames +=channel.getName()+",";
                        ContentChannel cc = new ContentChannel();
                        cc.setContentId(c.getId());
                        cc.setChannelId(channelId);
                        contentChannelLogicInterface.save(cc);
                    }
                }
                if (!"".equals(serviceProducts)) {
                    String serviceProductss[] = serviceProducts.split(",");
                    for (String serviceProducts1 : serviceProductss) {
                        long serviceProductId = StringUtils.string2long(serviceProducts1,-1);
                        if(serviceProductId<=0){
                            continue;
                        }
                       ServiceProduct serviceProduct = serviceProductLogicInterface.get(serviceProductId);
                        if(serviceProduct==null){
                            continue;
                        }
                        serviceProductNames +=serviceProduct.getName()+",";
                        ContentServiceProduct csp = new ContentServiceProduct();
                        csp.setContentId(c.getId());
                        csp.setServiceProductId(serviceProductId);
                        contentServiceProductLogicInterface.save(csp);
                    }
                }
                if (!"".equals(recommends)) {
                    String recommendss[] = recommends.split(",");
                    for (String recommends1 : recommendss) {
                        long recommendId = StringUtils.string2long(recommends1,-1);
                        if(recommendId<=0){
                            continue;
                        }
                        Recommend recommend = recommendLogicInterface.get(recommendId);
                        if(recommend==null){
                            continue;
                        }
                        recommendNames += recommend.getName()+",";
                        ContentRecommend cr = new ContentRecommend();
                        cr.setContentId(c.getId());
                        cr.setRecommendId(recommendId);
                        contentRecommendLogicInterface.save(cr);
                    }
                }
            }
            if(!"".equals(channelNames)){
                channelNames=channelNames.substring(0,channelNames.length()-1);
            }
            if(!"".equals(serviceProductNames)){
                serviceProductNames = serviceProductNames.substring(0,serviceProductNames.length()-1);
            }
            logInfo +=contentNames+"发布到频道："+channelNames+"。绑定了产品：" +serviceProductNames+
                    "。绑定了推荐：" + recommendNames+
                    "。";
        }
        //已审核资源列表上线,keyIds为空的时候代表线上资源列表修改
        if(!"".equals(keyIds)){
            String keyIdss[] = keyIds.split(",");
            for(String keyIds1:keyIdss){
                long keyId = StringUtils.string2long(keyIds1,-1);
                if(keyId<=0){
                    continue;
                }
                ContentCsp contentCsp = contentCspLogicInterface.get(keyId);
                if(contentCsp.getStatus() == null || contentCsp.getStatus() != 2){
                    contentCsp.setStatus(2l);
                    contentCsp.setStatusTime(new Date());
                    contentCspLogicInterface.save(contentCsp);
                }
            }
        }
        writeSysLog(logInfo);
        return Constants.ACTION_SAVE;
    }

    private long channelId;
    private long serviceProductId;

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public void setServiceProductId(long serviceProductId) {
        this.serviceProductId = serviceProductId;
    }

    //河北媒体绑定产品
    public void bindServiceProductsOfCsp() {
        List<Content> contents = contentLogicInterface.getContentsOfVipChannels(channelId);
        List<ServiceProduct> serviceProducts = new ArrayList<ServiceProduct>();
        if(serviceProductId > 0) {
            ServiceProduct s = serviceProductLogicInterface.get(serviceProductId);
            serviceProducts.add(s);
        } else {
            serviceProducts = serviceProductLogicInterface.getServiceProductsByCspId(cspId);
        }

        if(contents != null && contents.size() > 0 && serviceProducts != null && serviceProducts.size() != 0) {
            log.debug(cspId+" 开始绑定，一共有"+contents.size()+"个媒体，每个媒体绑定"+serviceProducts+"产品！");
            for(Content c : contents) {
                 for (ServiceProduct s :serviceProducts) {
                     ContentServiceProduct csp = new ContentServiceProduct();
                     csp.setContentId(c.getId());
                     csp.setServiceProductId(s.getId());
                     contentServiceProductLogicInterface.save(csp);
                 }
            }
        }
        log.debug(cspId+" 媒体产品绑定完成！");
    }


    boolean needBind = true;
    //山西媒体绑定产品
    public void bindServiceProductsByCsp() {
       List<Content> contents = contentLogicInterface.getContentsByCspId(cspId);
        List<ServiceProduct> serviceProducts = serviceProductLogicInterface.getServiceProductsByCspId(cspId);
        if(contents != null && contents.size() > 0 && serviceProducts != null && serviceProducts.size() != 0) {
            for(Content c : contents) {
                for (ServiceProduct s :serviceProducts) {
                    List<ContentServiceProduct>  contentServiceProducts = contentServiceProductLogicInterface.getContentServiceProductsByContentIdAndCspId(c.getId(),cspId);
                    if(contentServiceProducts != null && serviceProducts.size() > 0) {
                        for(ContentServiceProduct csp :contentServiceProducts) {
                           if(csp.getServiceProductId() == s.getId()) {
                               needBind = false;
                               break;
                           }

                        }

                        if(needBind) {
                            ContentServiceProduct csp = new ContentServiceProduct();
                            csp.setContentId(c.getId());
                            csp.setServiceProductId(s.getId());
                            contentServiceProductLogicInterface.save(csp);
                        }
                    } else {
                        ContentServiceProduct csp = new ContentServiceProduct();
                        csp.setContentId(c.getId());
                        csp.setServiceProductId(s.getId());
                        contentServiceProductLogicInterface.save(csp);
                    }

                    needBind = true;
                }
            }
        }
    }

//    public String publish() {
//        List<Channel> oldChannels = null;
//        String channelss[] = null;
//        log.debug("in publish");
//        String dealMessage = "";
//        String channels = "";
//        if (keys != null) {
//            for (String aKey : this.keys) {
//                try {
//                    channels = getRequestParam("channels", "");
//                    String channelRecommend = getRequestParam("channelRecommend", "");
//                    String serviceProducts = getRequestParam("serviceProducts", "");
//                    String recommends = getRequestParam("recommends", "");
//
//                    ContentCsp contentCsp = contentCspLogicInterface.get(new Long(aKey));
//                    long contentId = contentCsp.getContentId();
//                    oldChannels = contentChannelLogicInterface.getChannelsByContentId(contentId);
//                    //删除原来的值
//                    long cspId = contentCsp.getCspId();
//                    //contentLogicInterface.exitChannel(cspId,channels);
//                    //String hql="delete from ContentChannel cc where cc.contentId=" + contentId + " and cc.channelId in (select c.id from Channel c where c.cspId=" + cspId + ") union delete from ContentChannel cc where cc.contentId="+contentId+" and cc.channelId in (select ccl.channelId from CspChannel ccl where ccl.cspId="+cspId+")";
//                    HibernateUtils.deleteAll(this.contentLogicInterface.getSession(), "");
//                    HibernateUtils.deleteAll(this.contentLogicInterface.getSession(), "delete from ContentRecommend cr where cr.contentId=" + contentId + " and cr.recommendId in (select r.id from Recommend r where r.cspId=" + cspId + ")");
//                    HibernateUtils.deleteAll(this.contentLogicInterface.getSession(), "delete from ContentServiceProduct csp where csp.contentId=" + contentId + " and csp.serviceProductId in (select sp.id from ServiceProduct sp where sp.cspId=" + cspId + ")");
//
//                    if (contentCsp != null) {
//                        //资源上线
//                        if (contentCsp.getStatus() == null || contentCsp.getStatus() != 2) {
//                            contentCsp.setStatus(2l);
//                            contentCsp.setStatusTime(new Date());
//                            contentCspLogicInterface.save(contentCsp);
//                        }
//
//
//                        if (!"".equals(channels)) {
//                            channelss = channels.split(",");
//                            for (String channels1 : channelss) {
//                                ContentChannel cc = new ContentChannel();
//                                cc.setContentId(contentId);
//                                cc.setChannelId(new Long(channels1));
//                                contentChannelLogicInterface.save(cc);
//                            }
//                        }
//
//                        //channels = ","+channels+",";
//                        if (!"".equals(channelRecommend)) {
//                            String channelRecommends[] = channelRecommend.split(",");
//                            for (String channelRecommend1 : channelRecommends) {
//                                String ss[] = channelRecommend1.split("_");
//                                String channelId = ss[0];
//                                String recommendId = ss[1];
//                                //if (channels.indexOf(","+channelId+",")>-1){
//                                ContentRecommend cr = new ContentRecommend();
//                                cr.setContentId(contentId);
//                                cr.setChannelId(new Long(channelId));
//                                cr.setRecommendId(new Long(recommendId));
//                                contentRecommendLogicInterface.save(cr);
//                                //}
//                            }
//                        }
//
//
//                        if (!"".equals(serviceProducts)) {
//                            String serviceProductss[] = serviceProducts.split(",");
//                            for (String serviceProducts1 : serviceProductss) {
//                                ContentServiceProduct csp = new ContentServiceProduct();
//                                csp.setContentId(contentId);
//                                csp.setServiceProductId(new Long(serviceProducts1));
//                                contentServiceProductLogicInterface.save(csp);
//                            }
//                        }
//
//                        if (!"".equals(recommends)) {
//                            String recommendss[] = recommends.split(",");
//                            for (String recommends1 : recommendss) {
//                                ContentRecommend cr = new ContentRecommend();
//                                cr.setContentId(contentId);
//                                cr.setRecommendId(new Long(recommends1));
//                                contentRecommendLogicInterface.save(cr);
//                            }
//                        }
//                    }
//
//                    writeSysLog("发布SP资源:contentCspId=" + aKey);
//                    dealMessage += aKey;
//                } catch (Exception e) {
//                    super.addActionError("无法修改资源状态" +
//                            ":" + aKey);
//                }
//            }
//        }
//        if ("".equals(dealMessage)) {
//            super.addActionError("没有操作任何数据");
//            setSuccess(false);
//        } else {
//            super.addActionMessage("已经成功修改资源状态(" + dealMessage + ")");
//            setSuccess(true);
//        }
//
//        super.addActionMessage("");
//        String addChannels = "";
//        if (oldChannels != null) {
//            for (Channel c : oldChannels) {
//                int count = 0;
//                for (String channels1 : channelss) {
//                    count = 0;
//                    if (valueOf(c.getId()).equals(channels1)) {
//                        count++;
//
//                    }
//                }
//                if (count == 0) {
//                    addChannels += c.getId() + ",";
//                }
//            }
//        }
//        if (!addChannels.equals("")) {
//            addChannels = addChannels.substring(0, addChannels.length() - 1);
//            channels = channels + "," + addChannels;
//        }
//
//
//        if (!"".equals(channels) && null != channels) {
//            long cspId = ((Admin) ActionContext.getContext().getSession().get("sessionOperator")).getCspId();
//            String fullFilePath = ServletActionContext.getServletContext().getRealPath("/");
//            JsUtils jsUtils = new JsUtils();
//            jsUtils.addChannelIdToRunner(channels, cspId, fullFilePath);
//        }
//        return Constants.ACTION_SAVE;
//    }

    /**
     * @param contentCspLogicInterface the contentCspLogicInterface to set
     */
    @Autowired
    public void setContentCspLogicInterface(
            ContentCspLogicInterface contentCspLogicInterface) {
        this.contentCspLogicInterface = contentCspLogicInterface;
        setBaseLogicInterface(contentCspLogicInterface);
    }
}
