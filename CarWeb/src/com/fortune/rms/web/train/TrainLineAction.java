package com.fortune.rms.web.train;

/**
 * Created by xjliu on 14-6-12.
 *
 */
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.train.logic.logicInterface.TrainLineLogicInterface;
import com.fortune.rms.business.train.logic.logicInterface.TrainLogicInterface;
import com.fortune.rms.business.train.model.Train;
import com.fortune.rms.business.train.model.TrainLine;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Namespace("/train")
@ParentPackage("default")
@Action(value="trainLine")
@Results({
        @Result(name="getNodes",location = "/train/trainLineGetNodes.jsp")
})

public class TrainLineAction extends BaseAction<TrainLine> {
    private static final long serialVersionUID = 5743534534533534l;
    private TrainLineLogicInterface trainLineLogicInterface;
    private TrainLogicInterface trainLogicInterface;
    @SuppressWarnings("unchecked")
    public TrainLineAction() {
        super(TrainLine.class);
    }
    /**
     * @param trainLineLogicInterface the trainLineLogicInterface to set
     */
    @Autowired
    public void setTrainLineLogicInterface(TrainLineLogicInterface trainLineLogicInterface) {
        this.trainLineLogicInterface = trainLineLogicInterface;
        setBaseLogicInterface(trainLineLogicInterface);
    }

    @Autowired
    public void setTrainLogicInterface(TrainLogicInterface trainLogicInterface) {
        this.trainLogicInterface = trainLogicInterface;
    }

    private String selectMode;
    private String parentId;
    @SuppressWarnings("unchecked")
    public String getNodes(){
        TreeUtils tu = TreeUtils.getInstance();
        tu.initCache(TrainLine.class);
        if(parentId==null||"".equals(parentId)){
            parentId = "-1";
        }
        log.debug("׼����ѯparentId="+parentId+"�������ӽڵ�");
        objs = tu.getSonOf(TrainLine.class, parentId,"");
        if(objs==null||objs.size()==0){
            if("train".equals(selectMode)){
                Train bean = new Train();
                bean.setTrainLineId(StringUtils.string2int(parentId,Integer.MAX_VALUE));
                List<Train> trains = trainLogicInterface.search(bean);
                if(trains!=null){
                    objs = new ArrayList<TrainLine>(trains.size());
                    for(Train train:trains){
                        TrainLine line = new TrainLine();
                        line.setId(train.getId());
                        line.setName(train.getName());
                        line.setLeaf(true);
                        line.setType(1000);
                        objs.add(line);
                    }
                }
            }
        }else{
            for(TrainLine trainLine :objs){
                //�����Ϊ��ѡ�����յĻ𳵣����趨������Ҷ�ӣ�����Ļ𳵲���Ҷ��
                if("train".equals(selectMode)){
                    trainLine.setLeaf(false);
                }else{
                    List childs = tu.getSonOf(TrainLine.class,trainLine.getId(),"");
                    if(childs!=null&&childs.size()>0){
                        trainLine.setLeaf(false);
                    }else{
                        trainLine.setLeaf(true);
                    }
                }
            }
        }
        if(objs==null){
            objs = new ArrayList<TrainLine>();
        }
        return "getNodes";
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(String selectMode) {
        this.selectMode = selectMode;
    }
}
