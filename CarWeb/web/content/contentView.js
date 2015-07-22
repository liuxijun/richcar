var properties = [];// 所有属性的集合
var propertySelects = [];//属性的可选项
var formObjects = [];//表单控件

var copyIcon = "/images/content/copy.jpg";
var deleteIcon = "/images/content/delete.jpg";
var uploadIcon = "/images/content/upload.jpg";
var seeIcon = "/images/content/see.jpg";

function Property(id, code, name, dataType, isMultiLine, isNull, maxSize, relatedTable, displayOrder, count) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.dataType = dataType;
    this.isMultiLine = isMultiLine;
    this.isNull = isNull;
    this.maxSize = maxSize;
    this.relatedTable = relatedTable;
    this.displayOrder = displayOrder;
    this.count = count;
}

function PropertySelect(id, code, name, propertyId, displayOrder) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.propertyId = propertyId;
    this.displayOrder = displayOrder;
}

function ContentProperty(id,contentId,subContentId,name,propertyId,intValue,stringValue,extraData,desp){
    this.id = id;
    this.contentId = contentId;
    this.subContentId = subContentId;
    this.name = name;
    this.propertyId = propertyId;
    this.intValue = intValue;
    this.stringValue = stringValue;
    this.extraData = extraData;
    this.desp = desp;
}

function FormObject(id, property, obj) {
    this.id = id;
    this.property = property;
    this.obj = obj;
}

//得到集合中的对象
function getElement(theArray, theElementId) {
    if (theArray == null) {
        alert("no any attribute here");
        return null;
    }
    var i;
    var result;
    for (i = 0; i < theArray.length; i++) {
        result = theArray[i];
        if (result.id == theElementId) {
            return result;
        }
    }
    return null;
}
//添加集合的对象
function addElement(theArray, theElement) {
    if (theArray == null) {
        theArray = new Array;
    }
    theArray[theArray.length] = theElement;
}
//删除集合中的对象
function delElement(theArray, theElementId) {
    if (theArray == null) {
        alert("no any attribute here");
        return false;
    }
    var i;
    var result;
    var k;
    for (i = 0; i < theArray.length; i++) {
        result = theArray[i];
        if (result.id == theElementId) {
            k = i;
        }
    }
    var m;
    for (m = k; m < theArray.length - 1; m++) {
        theArray[m] = theArray[m + 1];
    }
    if (theArray.length > 1) {
        theArray.length = theArray.length - 1;
    }
    return true;
}

//得到集合中的对象
function getPropertyNo(pid, no) {
    fno = 0;
    for (i = 0; i < properties.length; i++) {
        if (properties[i].id != pid) {
            fno += properties[i].count;
        } else {
            fno += no;
            return (fno - 1);
        }
    }
    return 0;
}

FortuneBaseSingleFormItem=function(p,form1,no,pno){
    this.id=p.id+'_'+ pno;
    this.fieldLabel=p.name;
    this.name = p.id;
    this.width = 220;
    this.allowBlank = true;
    this.grow = false;
    this.xtype="textfield";
};
FortuneBaseMultiFormItem=function(p,form1,no,pno){
   if(p&&form1&&no&&pno){}
};
function addFormItem(p, form1, no) {
    var pno = Math.floor(Math.random() * 10000);

    var inputWidth = 220;
    var labelWidth = 150;
    switch (p.dataType) {
        //todo 1
        case 1:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id ,
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id ,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }
            break;
        //todo 2
        case 2:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextArea({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id ,
                            width:      '320',
                            allowBlank:  true,
                            grow:         false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textarea',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id ,
                                            width:       320,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }



            break;
        //todo 3
        case 3:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id ,
                            width:       inputWidth,
                            allowBlank: true,
                            //vtype:       'number',
                            grow:        false
                        })
                        ));

            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id ,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            //vtype:       'number',
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]

                        })
                        ));

            }

            break;
        //todo 4
        case 4:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.DateField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id ,
                            format:'Y-m-d',
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'datefield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            format:'Y-m-d',
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }
            break;

        //todo 5
        case 5:

            var arrayStoreData = [];

            for (var i5 = 0; i5 < propertySelects.length; i5++) {
                if (p.id == propertySelects[i5].propertyId) {
                    arrayStoreData[arrayStoreData.length] = [propertySelects[i5].id,propertySelects[i5].name];
                }
            }

            arrayStore = new Ext.data.ArrayStore({
                fields: [
                    {
                        name: 'value'
                    },
                    {
                        name: 'display'
                    }
                ],
                data : arrayStoreData
            });
            //arrayStore.loadData(arrayStoreData);

            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.ComboBox({
                            id:   p.id + '_' + pno,
                            hiddenName: p.id,
                            fieldLabel: p.name,
                            width: inputWidth ,
                            triggerAction: 'all',
                            emptyText:'请选择...',
                            store: arrayStore,
                            valueField: 'value',
                            displayField: 'display',
                            //value:'1',
                            //mode: 'remote',
                            mode:'local',
                            loadingText:'加载中...',
                            selectOnFocus:true,
                            editable: false,
                            typeAheadDelay:1000,
                            //pageSize:5,
                            forceSection: true,
                            typeAhead: false,
                            allowBlank:true,
                            listeners:{
                                select: function(combo, record, index) {
                                    //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                    if(combo||record||index){}
                                }
                            }
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'combo',
                                            id:   p.id + '_' + pno,
                                            hiddenName: p.id ,
                                            fieldLabel: p.name,
                                            width: inputWidth,
                                            triggerAction: 'all',
                                            emptyText:'请选择...',
                                            store: arrayStore,
                                            valueField: 'value',
                                            displayField: 'display',
                                            //value:'1',
                                            //mode: 'remote',
                                            mode:'local',
                                            loadingText:'加载中...',
                                            selectOnFocus:true,
                                            editable: false,
                                            typeAheadDelay:1000,
                                            //pageSize:5,
                                            forceSection: true,
                                            typeAhead: false,
                                            allowBlank:true,
                                            listeners:{
                                                select: function(combo, record, index) {
                                                    if(combo&&record&&index){}
                                                    //alert(index +":"+ record.get("channelId") +":"+ record.get("name"));
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));
            }
            break;

        //todo 6
        case 6:

            var items = new Array();
            for (var i6 = 0; i6 < propertySelects.length; i6++) {
                if (p.id == propertySelects[i6].propertyId) {
                    items[items.length] = {name:p.id, boxLabel:propertySelects[i6].name,inputValue:propertySelects[i6].id};
                }
            }

            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.RadioGroup({
                            id:   p.id + '_' + pno,
                            name: p.id  ,
                            fieldLabel: p.name,
                            xtype:'radiogroup',
                            columns: [70,70,70,70,70],
                            //width:180,
                            items:items
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            id:   p.id + '_' + pno,
                                            name: p.id ,
                                            fieldLabel: p.name,
                                            xtype:'radiogroup',
                                            columns: [70,70,70,70,70],
                                            //width:180,
                                            items:items
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));
            }

            break;

        //todo 7  checkboxgroup
        case 7:

            var items7 = new Array();
            for (var i7 = 0; i7 < propertySelects.length; i7++) {
                if (p.id == propertySelects[i7].propertyId) {
                    items7[items7.length] = {id:p.id + '_' + propertySelects[i7].id,name:p.id,boxLabel:propertySelects[i7].name,inputValue:propertySelects[i7].id};
                }
            }

            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.CheckboxGroup({
                            id:   p.id + '_' + pno,
                            name: p.id ,
                            fieldLabel: p.name,
                            xtype:'checkboxgroup',
                            columns: [85,85,85,85,85],
                      //      width:180,
                            items:items7
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            id:   p.id + '_' + pno,
                                            name: p.id,
                                            fieldLabel: p.name,
                                            xtype:'checkboxgroup',
                                            columns: [85,85,85,85,85],
                                            //width:180,
                                            items:items7
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));
            }

            break;
        //todo 8 file wmv
        case 8:
        case 9:
        case 10:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:7},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'hidden',
                                            id:   p.id + '_' + pno + "_extra",
                                            name: p.id + "_extra"
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_desp",
                                            fieldLabel: "第",
                                            labelAlign:'right',
                                            name:        p.id + "_desp",
                                            width:       60,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_bandwidth",
                                            fieldLabel: "码流",
                                            name:        p.id + "_bandwidth",
                                            width:       40,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_length",
                                            fieldLabel: "时长",
                                            name:        p.id + "_length",
                                            width:       40,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: uploadIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '上传',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    fno = this.pid + "_" + this.pno;

                                                    getFtpList(fno)
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: seeIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '查看',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {

                                                    fno = this.pid + "_" + this.pno;
                                                    viewWmv();

                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:9},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'hidden',
                                            id:   p.id + '_' + pno + "_extra",
                                            name: p.id + "_extra"
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    labelAlign:'right',
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_desp",
                                            fieldLabel: "集",
                                            name:        p.id + "_desp",
                                            width:       30,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_bandwidth",
                                            fieldLabel: "码流",
                                            name:        p.id + "_bandwidth",
                                            width:       40,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: 28,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno + "_length",
                                            fieldLabel: "时长",
                                            name:        p.id + "_length",
                                            width:       40,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                } ,
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: uploadIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '上传',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    fno = this.pid + "_" + this.pno;

                                                    getFtpList(fno)
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: seeIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '查看',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {

                                                    fno = this.pid + "_" + this.pno;
                                                    viewWmv();

                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }
            break;

        //todo 9
        case 1009:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id,
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id ,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]

                        })
                        ));

            }
            break;

        //todo 10
        case 1010:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id,
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]

                        })
                        ));

            }
            break;

        //todo 11 file photo
        case 11:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: uploadIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '上传',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    fno = this.pid + "_" + this.pno;

                                                    uploadFile();

                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: seeIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '查看',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {

                                                    fno = this.pid + "_" + this.pno;

                                                    viewPhoto();

                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:5},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                } ,
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: uploadIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '上传',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    fno = this.pid + "_" + this.pno;

                                                    uploadFile();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: seeIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '查看',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    fno = this.pid + "_" + this.pno;

                                                    viewPhoto();
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }
            break;

        //todo 12
        case 12:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id ,
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id ,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        })
                        ));

            }
            break;
        //todo 13
        case 13:
            if (p.isMultiLine == 0) {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.form.TextField({
                            id:   p.id + '_' + pno,
                            fieldLabel: p.name,
                            name:        p.id,
                            width:       inputWidth,
                            allowBlank: true,
                            grow:        false
                        })
                        ));
            } else {
                addElement(formObjects, new FormObject(p.id + '_' + pno, p,
                        new Ext.Panel({
                            width:'100%',
                            layout:'table',
                            layoutConfig: {columns:3},
                            baseCls: 'x-plain',
                            items:[
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    labelWidth: labelWidth,
                                    items:[
                                        {
                                            xtype: 'textfield',
                                            id:   p.id + '_' + pno,
                                            fieldLabel: p.name,
                                            name:        p.id,
                                            width:       inputWidth,
                                            allowBlank:  true,
                                            grow:        false
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            xtype: 'button',
                                            icon: copyIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '复制',
                                            width: 22,
                                            heigth: 22,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    pp.count = pp.count + 1;
                                                    addFormItem(pp, form1, getPropertyNo(pp.id, pp.count));
                                                    form1.doLayout();
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    layout:'form',
                                    baseCls: 'x-plain',
                                    items:[
                                        {
                                            pid : p.id,
                                            pno : pno,
                                            xtype: 'button',
                                            icon: deleteIcon,
                                            cls: "x-btn-text-icon",
                                            tooltip: '删除',
                                            width: 20,
                                            heigth: 20,
                                            listeners:{
                                                "click":function()
                                                {
                                                    var pp = getElement(properties, this.pid);
                                                    if (pp.count > 1) {
                                                        pp.count = pp.count - 1;
                                                        //查找form控件
                                                        var formObject = getElement(formObjects, this.pid + "_" + this.pno);
                                                        //删除控件中的子控件
                                                        formObject.obj.removeAll();
                                                        //在form中删除控件
                                                        form1.remove(formObject.obj);
                                                        //重新排列页面
                                                        form1.doLayout();
                                                        //删除在缓存中控件
                                                        delElement(formObjects, this.pid + "_" + this.pno);
                                                    }
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]

                        })
                        ));

            }
            break;

    }

    form1.insert(no, formObjects[formObjects.length - 1].obj);
    return p.id + "_" + pno;

}

var fno = '';

// todo uploadFile
function uploadFile() {
    var winUploadForm = new Ext.FormPanel({
        style: {'margin-left':'35px','margin-top': '0px','margin-right':'0px','margin-bottom':'0px'},
        id:'winUploadForm',
        width: '90%',

        labelWidth: 50,
        frame:true,
        //layout:'column',
        layout:'form',
        layoutConfig: {columns:1},
        baseCls: 'x-plain',
        waitMsgTarget: true,
        loadMask:{msg:'正在加载数据，请稍侯……'},
        fileUpload:true,
        items: [
            {
                xtype: 'label',
                labelSeparator : '',
                fieldLabel: '　'
            },
            {
                name:'uploadFileLocal',
                xtype:'hidden'
            },
            {
                name: 'uploadFile',
                xtype: 'fileuploadfield',
                emptyText: '选择文件',
                fieldLabel: '文件',
                buttonText: '浏览...',
                allowBlank:false,
                width:150,
                listeners:{
                    "fileselected":function(btn, value)
                    {
                        winUploadForm.getForm().findField('uploadFileLocal').setValue(value);
                    }
                }
            },
            {
                xtype: 'label',
                labelSeparator : '',
                fieldLabel: '　'
            },
            {
                style: {'margin-left':'65px'},
                text: '保　存',
                xtype:'button',
                minWidth:80,
                listeners:{
                    "click":function()
                    {
                        if (winUploadForm.getForm().isValid()) {
                            winUploadForm.getForm().submit({
                                url: '/content/content!uploadFile.action', //处理修改后台地址
                                method: 'post',
                                waitMsg: '正在处理数据，请稍后……',
                                success: function (form, returnMsg) {
                                    var serverData = Ext.util.JSON.decode(returnMsg.response.responseText);
                                    Ext.Msg.alert("提示", "操作成功！");

                                    viewForm.getForm().findField(fno).setValue(serverData.msg);
                                    winUploadForm.getForm().findField('uploadFile').setValue('');
                                    winUpload.hide();
                                },
                                failure: function (form, returnMsg) {
                                    Ext.MessageBox.show({
                                        title: '提示',
                                        msg: '操作失败，原因是:' + returnMsg.result.error,
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.ERROR
                                    });
                                },
                                scope: this
                            });
                        }
                    }
                }
            }

        ]
    });

    var winUpload = new Ext.Window({
        title:"上传文件",
        width:300,
        height:150,
        closeAction:"hide",
        closable:true,

        items:[
            winUploadForm
        ]
    });
    winUpload.show();
}

//todo viewPhoto
function viewPhoto() {
    var winPhoto = new Ext.Window({
        title:"显示图片",
        width:300,
        height:300,
        closeAction:"close",
        closable:true
    });
    winPhoto.html = '<img src=' + viewForm.getForm().findField(fno).getValue() + ' border="0"/>';
    winPhoto.show();
}

//todo getFtpList
var ftpUrl = "/";
function getFtpList() {
    var pageSize = 20;
    var dataFtpStore = new Ext.data.JsonStore({
        method:'POST',
        remoteSort: true,
        url:"/content/content!getFtpList.action",
        totalProperty:"totalCount",
        root:'objs',
        fields:[
            {
                name:'name'
            },
            {
                name:'type'
            },
            {
                name:'size'
            },
            {
                name:'date'
            }
        ]
    });

    var winFtp = new Ext.Window({
        title:"文件列表",
        width:514,
        height:483,
        closeAction:"hide",
        closable:true,

        //html:'<h1>Hello,easyjf open source</h1>',
        items:[
            new Ext.grid.GridPanel({
                title:"位置: " + ftpUrl ,
                width:500,
                height:450,
                store: dataFtpStore,

                loadMask:{msg:'正在加载数据，请稍侯……'},
                iconCls:'icon-grid',
                //viewConfig: { forceFit: true },

                listeners:{
                    "cellclick":function(grid, rowIndex, columnIndex)
                    {
                        if(columnIndex){

                        }
                        //alert(grid.getStore().getAt(rowIndex).get('name'));
                        if (grid.getStore().getAt(rowIndex).get('type') == 1) {

                            if (grid.getStore().getAt(rowIndex).get('name') == ".") {
                                ftpUrl = "/";
                            } else if (grid.getStore().getAt(rowIndex).get('name') == "..") {
                                ftpUrl = ftpUrl.substring(0, ftpUrl.lastIndexOf("/", ftpUrl.length - 2) + 1);
                            } else {
                                ftpUrl += grid.getStore().getAt(rowIndex).get('name') + "/";
                            }

                            grid.setTitle("位置: " + ftpUrl);

                            dataFtpStore.baseParams = {
                                url: ftpUrl,
                                deviceId: deviceId
                            };
                            dataFtpStore.load({params:{start:0, limit:pageSize }});
                        } else {
                            viewForm.getForm().findField(fno).setValue(ftpUrl + grid.getStore().getAt(rowIndex).get("name"));
                            winFtp.hide();
                        }
                    }
                },

                columns: [
                    {
                        header: "文件名",
                        dataIndex: 'name',
                        width: 185,
                        sortable: true,
                        align:'center'
                    },
                    {
                        header: "类型",
                        dataIndex: 'type',
                        width: 75,
                        sortable: true,
                        align:'center',
                        renderer:function(val, p, row) {
                            if(p&&row){}
                            if (val == '1') {
                                return '&lt;DIR&gt;'
                            } else {
                                return '&lt;FILE&gt;'
                            }

                        }
                    },
                    {
                        header: "大小",
                        dataIndex: 'size',
                        width: 60,
                        sortable: true,
                        align:'center'
                    },
                    {
                        header: "日期",
                        dataIndex: 'date',
                        width: 130,
                        sortable: true,
                        align:'center'
                    }
                ],

                tbar:[
                    new Ext.Toolbar.TextItem('工具栏：'),//文本
                    {
                        pressed:true,
                        text:'根目录',
                        handler:
                                function() {
                                    ftpUrl = "/";

                                    var grid = winFtp.items.items[0];
                                    grid.setTitle("位置: " + ftpUrl);

                                    dataFtpStore.baseParams = {
                                        url: ftpUrl,
                                        deviceId: deviceId
                                    } ;
                                    dataFtpStore.load({params:{start:0, limit:pageSize }});
                                    //alert(1);
                                }
                    },
                    {
                        xtype:"tbseparator"
                    },//加上这句，后面的就显示到右边去了
                    {
                        pressed:true,
                        text:'上级目录',
                        handler:
                                function() {

                                    ftpUrl = ftpUrl.substring(0, ftpUrl.lastIndexOf("/", ftpUrl.length - 2) + 1);

                                    var grid = winFtp.items.items[0];
                                    grid.setTitle("位置: " + ftpUrl);

                                    dataFtpStore.baseParams = {
                                        url: ftpUrl,
                                        deviceId: deviceId
                                    };
                                    dataFtpStore.load({params:{start:0, limit:pageSize }});
                                    //alert(1);
                                }
                    }

                    //{xtype:"tbfill"},
                ] ,

                bbar:new Ext.PagingToolbar({
                    pageSize: pageSize,
                    store: dataFtpStore,
                    displayInfo: true,
                    displayMsg: '结果数据 {0} - {1} of {2}',
                    emptyMsg: "没有数据"
                })

            })
        ]

    });

    var deviceId = 0;
    for (var i = 0; i < properties.length; i++) {
        if (properties[i].relatedTable == '1') {
            deviceId = properties[i].id;
            break;
        }
    }
    deviceId = viewForm.getForm().findField('' + deviceId).getValue();
    if (deviceId == '') {
        Ext.Msg.alert("提示", "请先选择设备");
        return;
    }

    dataFtpStore.baseParams = {
        url: ftpUrl,
        deviceId:deviceId
    };
    dataFtpStore.load({params:{start:0, limit:pageSize }});
    winFtp.show();

}

//todo viewWmv
function viewWmv() {

    var deviceId = 0;
    for (var i = 0; i < properties.length; i++) {
        if (properties[i].relatedTable == '1') {
            deviceId = properties[i].id;
            break;
        }
    }
    deviceId = viewForm.getForm().findField('' + deviceId).getValue();

    if (deviceId == '') {
        Ext.Msg.alert("提示", "请先选择设备");
        return;
    }

    var remoteRequestStore = new Ext.data.JsonStore({
        method:'POST',
        url:"/system/device!getDeviceRegUrl.action"
    });
    remoteRequestStore.reload({
        params:{deviceId: deviceId ,url: viewForm.getForm().findField(fno).getValue() },
        callback :
                function(records, options, success) {
                    if(records&&options&&success){}
                    var returnData = this.reader.jsonData;
                    if (returnData.success) {
                        var playUrl = returnData.msg;
                        var winMedia = new Ext.Window({
                            title:"视频播放",
                            width:500,
                            height:420,
                            closeAction:"close",
                            closable:true,
                            listeners:{
                                "close":function() {
                                    if (program.currentMedia.duration > 0) {
                                        //                                        viewForm.getForm().findField('clipWidth').setValue( program.currentMedia.imageSourceWidth );
                                        //                                        viewForm.getForm().findField('clipHeight').setValue( program.currentMedia.imageSourceHeight );
                                        viewForm.getForm().findField(fno + '_bandwidth').setValue(Math.floor(program.currentMedia.getItemInfo("Bitrate") / 1000));
                                        viewForm.getForm().findField(fno + '_length').setValue(Math.floor(program.currentMedia.duration));
                                    }
                                }
                            },
                            html:
                                    '<object id="program" codeBase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2"' +
                                    '          type="application/x-oleobject"                                                      ' +
                                    '          width="486" height="388"                                                            ' +
                                    '          standby="Loading Microsoft Windows Media Player components..."                      ' +
                                    '          classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6">                               ' +
                                    '          <PARAM NAME="uiMode" VALUE="full"/>                                                           ' +
                                    '          <PARAM NAME="AUTOSIZE" VALUE="true"/>                                                         ' +
                                    '          <PARAM NAME="stretchToFit" VALUE="true"/>                                                    ' +
                                    '          <PARAM NAME="url" VALUE="' + playUrl + '"/>     '
                        });
                        winMedia.show();
                    } else {
                        Ext.MessageBox.alert('发生异常', '服务器返回错误信息：' + returnData.error);
                    }

                }
    });

}





