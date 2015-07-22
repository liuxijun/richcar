var liveUtils = {
    TASK_TYPE_ONE_TIME : 1,
    TASK_TYPE_LOOP: 2,
    TASK_TYPE_ALL_DAY: 3,
    TASK_PAGE_SIZE: 20,
    TASK_MODE_LIVE : 1,
    TASK_MODE_RECORD : 2,
    TASK_MODE_LIVERECORD : 3,
    TEMPLATE_TYPE_RECORD : "file",
    TEMPLATE_TYPE_LIVE : "ts_over_http",
    task_page_index: -1,
    task_page_count : 0,
    dataInitProgress : 0,      // 基础数据加载进度
    LIVE_INIT_MIN_PROGRESS: 4,// 装载直播最低要求
    itemId : -1,    // 正在编辑的直播或录制Id
    taskId: -1,     // 选用的任务的Id
    sourceList: [], /*信号源*/
    templateList : [],/*配置*/
    taskList: [], /*已有任务列表*/
    task : null, /*当前被选中的task*/
    itemLoading: false, // 是否正在加载直播或录制信息

    // 获取一个任务的详情
    getTask: function(id, callback){
        $.ajax({
            type: "POST",
            url: "/live/getTask.action",
            //dataType: "json",
            dataType: "text",
            data: {"taskId":id},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(response.task && callback){
                    var task = liveUtils.parseTask(response.task);
                    if(task){
                        callback(task);
                    }
                }
            }
        });
    },

    /**
     * 已有任务列表
     * pageNo，页码
     * searchValue 查询关键字
     * callback 回调函数参数taskList(Array) totalCount(int)
     * callback_fail 失败回调函数 message
     */
    getTaskList : function(pageNo, searchValue, callback, callback_fail){
        $.ajax({
            type: "POST",
            url: "/live/taskList.action",
            //dataType: "json",
            dataType: "text",
            data: {"searchValue":searchValue,
                "pageBean.pageNo": pageNo,
                "pageBean.pageSize": liveUtils.TASK_PAGE_SIZE
            },
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(parseInt(response.total) < 0){
                    // 访问统一转码失败
                    if(callback_fail){
                        callback_fail(response.message);
                    }
                }else if(callback){
                    liveUtils.task_page_index = pageNo;
                    liveUtils.task_page_count = Math.ceil(response.total/liveUtils.TASK_PAGE_SIZE);

                    var tasks = response.tasks, task;
                    liveUtils.taskList = [];
                    for(var i=0; i<tasks.length; i++){
                        task = liveUtils.parseTask(tasks[i]);
                        if(task) liveUtils.addTask(task);
                    }
                    callback(liveUtils.taskList, response.total);
                }
            }
        }).fail(function(){
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });
    },

    // 解析任务数据
    parseTask : function(o){
        if(!o) return null;

        // 先解析时间设置
        var type = o["taskType"],
            taskTime = new TaskTime(type, moment(o["onlineDate"], "YYYY-MM-DD").locale("zh-Cn"),
                moment(o["offlineDate"], "YYYY-MM-DD").locale("zh-Cn"),
                moment(o["startTime"], "YYYY-MM-DD HH:mm:ss").locale("zh-Cn"),
                moment(o["stopTime"], "YYYY-MM-DD HH:mm:ss").locale("zh-Cn"), o["weeks"]),
            deserts = o["deserts"], templates = [], serverAddress = "";
        if(deserts){
            for(var i=0; i<deserts.length; i++){
                // 从选定配置里选择第一个解析地址，在服务器上匹配直播服务器
                if(i ==0){
                    serverAddress = liveUtils.parseAddress(deserts[0]["url"]);
                }
                templates.push( new TaskTemplate(
                    deserts[i]["id"],deserts[i]["templateId"],deserts[i]["name"],deserts[i]["url"],deserts[i]["desertProtocol"]
                ));
            }
        }

        //console.info("server address:" + serverAddress);
        return new Task(o["id"], o["name"], o["source"], taskTime, templates,  o["streamName"], serverAddress, o["file"]);
    },

    // 将任务添加的列表，检查是否存在
    addTask : function(task){
        if(!task) return;

        liveUtils.taskList = liveUtils.taskList || [];
        for(var i=0; i<liveUtils.taskList.length; i++){
            if(liveUtils.taskList[i].id == task.id){
                // 替换原来的task
                liveUtils.taskList[i] = task;
                return;
            }
        }

        liveUtils.taskList.push(task);
    },

    /*
     * 获取信号源列表
     * callback - 成功后的回调
     * callback_fail - 失败后的回调
     * */
    getSourceList : function(callback, callback_fail){
        $.ajax({
            type: "POST",
            url: "/live/sourceList.action",
            //dataType: "json",
            dataType: "text",
            data: {},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(response.failed){
                    // 访问统一转码失败
                    if(callback_fail){
                        callback_fail(response.message);
                    }
                }else if(callback){
                    var sources = response.sources;
                    liveUtils.sourceList = [];
                    for(var i=0; i<sources.length; i++){
                        liveUtils.sourceList.push( {"id": sources[i].id, "title": sources[i].name, "url":sources[i].url} );
                    }

                    callback(liveUtils.sourceList);
                }
            }
        }).fail(function(){
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });
    },

    /*
     * 获取配置模板列表
     * callback - 成功后的回调
     * callback_fail - 失败后的回调
     * */
    getTemplateList : function(callback, callback_fail){
        $.ajax({
            type: "POST",
            url: "/live/templateList.action",
            //dataType: "json",
            dataType: "text",
            data: {},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(response.failed){
                    // 访问统一转码失败
                    if(callback_fail){
                        callback_fail(response.message);
                    }
                }else if(callback){
                    var templates = response.templates;
                    liveUtils.templateList = [];
                    for(var i=0; i<templates.length; i++){
                        liveUtils.templateList.push( {"id": templates[i].id, "title": templates[i].name,
                            "vbandwidth":templates[i].vbandwidth, "abandwidth":templates[i].abandwidth,
                            "vcodec":templates[i].vcodec, "acodec":templates[i].acodec,
                            "type": templates[i].type, "size": templates[i].size} );
                    }

                    callback(liveUtils.templateList);
                }
            }
        }).fail(function(){
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });
    },

    // 根据Id获取template对象
    getTemplateById : function(id){
        for(var i=0; i<liveUtils.templateList.length; i++){
            if(liveUtils.templateList[i].id == id) return liveUtils.templateList[i];
        }

        return null;
    },

    /*根据源的url获取id*/
    getSourceIdByURL : function(url){
        if(!liveUtils.sourceList) return -1;

        var sourceURL;
        for(var i= 0; i<liveUtils.sourceList.length; i++){
            sourceURL = liveUtils.sourceList[i].url;

            if(sourceURL === url || sourceURL.replace(/&amp;/g, '&') === url){
                return liveUtils.sourceList[i].id;
            }
        }

        return -1;
    },

    /*根据Id从taskList中找到任务*/
    getTaskById: function(id){
        if(!liveUtils.taskList && !liveUtils.task) return null;

        if(liveUtils.task && liveUtils.task.id == id) return liveUtils.task;

        for(var i=0; i<liveUtils.taskList.length; i++){
            if(liveUtils.taskList[i].id == id) return liveUtils.taskList[i];
        }

        return null;
    },

    /**
     * 保存直播到数据库
     * live 直播信息，包括直播描述和栏目等信息，不包括任务信息，任务信息应从taskList中寻找
     * callback 保存成功后的回调
     * callback_fail 保存失败的回调
     */
    saveLive : function(live, callback, callback_fail) {
        if (!live) return;

        var task = liveUtils.getTaskById(live.taskId);
        if(!task) return;

        // 把栏目列表转换为逗号分隔的id串
        var i,liveChannels = "", recordChannels = "";
        if(live.serializedChannel){
            for(i=0; i<live.serializedChannel.length; i++){
                liveChannels += (i==0)? live.serializedChannel[i]["id"] : ","+live.serializedChannel[i]["id"];
            }
        }
        if(live.recordChannel){
            for(i=0; i<live.recordChannel.length; i++){
                recordChannels += (i==0)? live.recordChannel[i]["id"] : ","+live.recordChannel[i]["id"];
            }
        }
        var moduleId= 503754003;
        var moduleObj = document.getElementById("liveModule");
        if(moduleObj!=null){
            moduleId = moduleObj.value;
        }
        $.ajax({
            type: "POST",
            url: "/live/live!saveLive.action",
            dataType: "text",
            data: {
                "obj.id": liveUtils.itemId,
                "obj.taskId": liveUtils.taskId,
                "obj.title": live.title,
                "obj.actor": live.actor,
                "obj.intro": live.intro,
                "obj.autoControl": live.autoControl? 1: 0,
                "obj.foreshow": live.foreshow? 1: 0,
                "obj.needRecord": live.needRecord? 1: 0,
                "obj.userTypes": live.userTypes,
                "obj.recordChannels": recordChannels,
                "liveChannels": liveChannels,
                "obj.type": task.time.type,
                "obj.startTime": moment(task.time.startTime).format("YYYY-MM-DD HH:mm:ss"),
                "obj.endTime": moment(task.time.endTime).format("YYYY-MM-DD HH:mm:ss"),
                "obj.startDate": moment(task.time.startDate).format("YYYY-MM-DD HH:mm:ss"),
                "obj.endDate": moment(task.time.endDate).format("YYYY-MM-DD HH:mm:ss"),
                "obj.weekDay": task.time.weekDay,
                "obj.moduleId":moduleId,
                "posterData": live.poster,
                "liveChannel" : task.streamName,
                "serverIp": task.serverIp
            },
            success: function (msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if (response.failed) {
                    // 访问统一转码失败
                    if (callback_fail) {
                        callback_fail(response.message);
                    }
                } else if (callback) {
                    callback();
                }
            }
        }).fail(function () {
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });

    },

    /**
     * 保存录制到数据库
     * record 录制信息，包括录制描述和栏目等信息，不包括任务信息，任务信息应从taskList中寻找
     * callback 保存成功后的回调
     * callback_fail 保存失败的回调
     */
    saveRecord : function(record, callback, callback_fail) {
        if (!record) return;

        var task = liveUtils.getTaskById(record.taskId);
        if(!task) return;

        // 把栏目列表转换为逗号分隔的id串
        var i, recordChannels = "";
        if(record.serializedChannel){
            for(i=0; i<record.serializedChannel.length; i++){
                recordChannels += (i==0)? record.serializedChannel[i]["id"] : ","+record.serializedChannel[i]["id"];
            }
        }

        $.ajax({
            type: "POST",
            url: "/live/live!saveRecord.action",
            dataType: "text",
            data: {
                "obj.id": liveUtils.itemId,
                "obj.taskId": liveUtils.taskId,
                "obj.title": record.title,
                "obj.actor": record.actor,
                "obj.intro": record.intro,
                "obj.autoControl":  1,
                "obj.foreshow": 0,
                "obj.needRecord": 1,
                "obj.userTypes": record.userTypes,
                "obj.recordChannels": recordChannels,
                "liveChannels": "",
                "obj.type": task.time.type,
                "obj.startTime": moment(task.time.startTime).format("YYYY-MM-DD HH:mm:ss"),
                "obj.endTime": moment(task.time.endTime).format("YYYY-MM-DD HH:mm:ss"),
                "obj.startDate": moment(task.time.startDate).format("YYYY-MM-DD HH:mm:ss"),
                "obj.endDate": moment(task.time.endDate).format("YYYY-MM-DD HH:mm:ss"),
                "obj.weekDay": task.time.weekDay,
                "posterData": record.poster,
                "liveChannel" : task.streamName,
                "serverIp": task.serverIp
            },
            success: function (msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if (response.failed) {
                    // 访问统一转码失败
                    if (callback_fail) {
                        callback_fail(response.message);
                    }
                } else if (callback) {
                    callback();
                }
            }
        }).fail(function () {
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });

    },

    /**
     * 加载直播信息
     * id 直播Id
     * callback 加载成功后的回调
     */
    loadLive: function(id, callback){
        if(liveUtils.itemLoading) return;

        liveUtils.itemLoading = true;
        $.ajax({
            type: "POST",
            url: "/live/live!loadLive.action",
            //dataType: "json",
            dataType: "text",
            data: {"obj.id": id},
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                if(callback){
                    // 给回调的直播对象包括以下字段
                    //id, title, taskId, actor, intro, poster, foreshow, autoControl, userTypes, channels, record, recordChannels
                    var channelList = [];
                    for(var i=0; i<response.data.channelList.length; i++){
                        channelList.push(response.data.channelList[i].channelId);
                    }
                    callback({
                        "id" : response.data["id"], "taskId": response.data["taskId"], "title": response.data["title"],
                        "actor": response.data["actor"],
                        "intro": response.data["intro"], "poster": response.data["poster"], "foreshow": response.data["foreshow"],
                        "autoControl": response.data["autoControl"],
                        "userTypes": response.data["userTypes"].split(","),
                        "channels": channelList, "record": response.data["needRecord"],
                        "recordChannels": response.data["recordChannels"].split(",")
                    });
                }
            }
        })
    },

    /*
     * 保存任务信息，在新建任务或修改已有任务时调用
     * task 要保存的任务信息
     * callback 保存成功后的回调，参数为taskId，新生成的任务Id或原id
     * callback_fail 失败时的回调
     * mode 1 - 直播 2 - 录制 3 - 直播+录制
     * */
    saveTask : function(task, callback, callback_fail, mode){
        if(!task) return;

        //console.info("source:" + task.source);
        //console.info("formatted end time:" + moment(task.time.endTime, ["YYYY-MM-DD HH:mm", "HH:mm"]).format("YYYY-MM-DD HH:mm:ss"));
        //return;
        var templates = [];
        for(var i=0; i<task.template.length; i++){
            templates.push(task.template[i].templateId);
        }

        // task中的source可能是Id，也可能是源的url
        if(!$.isNumeric(task.source)){
            task.source = liveUtils.getSourceIdByURL(task.source);
        }
        /*
         * 如果成功，更新taskList中对应的记录
         * */
        $.ajax({
            type: "POST",
            url: "/live/synchroTask.action",
            //dataType: "json",
            dataType: "text",
            data: {"task.id": task.id, "task.title": task.title,
                "task.sourceId": task.source,
                "task.templateIds": templates.join(","),
                "task.streamName": task.streamName,
                "task.serverIp": task.serverIp,
                "task.type": task.time.type,
                "task.startTime": moment(task.time.startTime, ["YYYY-MM-DD HH:mm", "HH:mm"]).format("YYYY-MM-DD HH:mm:ss"),
                "task.endTime": moment(task.time.endTime, ["YYYY-MM-DD HH:mm", "HH:mm"]).format("YYYY-MM-DD HH:mm:ss"),
                "task.startDate": moment(task.time.startDate, ["YYYY-MM-DD"]).format("YYYY-MM-DD HH:mm:ss"),
                "task.endDate": moment(task.time.endDate, ["YYYY-MM-DD"]).format("YYYY-MM-DD HH:mm:ss"),
                "task.weekDay": task.time.weekDay,
                "task.filePath": task.filePath,
                "task.needLive": true,
                "task.needRecord": (mode != liveUtils.TASK_MODE_LIVE)
            },
            success: function(msg) {
                var response = eval("(function(){return " + msg + ";})()");
                // 解析返回结果，如果成功，获取保存后的task信息，更新本地数据
                if(response.failed){
                    callback_fail(response.message);
                }else if(callback){
                    // 解析返回的id
                    task.id = response["taskId"];
                    task.serverIp = task.serverIp || response["serverIp"];
                    task.streamName = response["streamName"];
                    if(parseInt(task.id) > 0) {
                        liveUtils.addTask(task);
                        callback(task.id);
                    }else{
                        callback_fail(response.message);
                    }
                }
            }
        }).fail(function () {
            callback_fail("和服务器交互失败，请重新登录后尝试！");
        });
    },

    /*从url中解析服务器地址*/
    parseAddress : function(url){
        var l = document.createElement("a");
        if(url.indexOf("://") < 0){
            url = "http://" + url;
        }
        l.href = url;
        /* IE doesn't populate all link properties when setting .href with a relative URL,
         however .href will return an absolute URL which then can be used on itself
         to populate these additional fields.*/
        if(l.host == ""){
            l.href = l.href
        }
        //console.info(l.hostname);
        return l.hostname;
    }
};

// 任务其中time为TaskTime类型, template为TaskTemplate数组
function Task(id, title, source, time, template, streamName, serverIp, filePath){
    this.id = id;
    this.title = title;
    this.source = source; // 源url/id
    this.streamName = streamName; // 智能流频道名称
    this.serverIp = serverIp;         // 直播服务器ip
    this.time = time;
    this.template = template || [];
    this.filePath = filePath; // 文件保存路径
    this.isLive = function(){ // 配置是否包含直播
        for(var i=0; i<this.template.length; i++){
            if(this.template[i].type === liveUtils.TEMPLATE_TYPE_LIVE) return true;
        }
        return false;
    };
    this.isRecord = function(){ // 配置是否包含录制
        for(var i=0; i<this.template.length; i++){
            if(this.template[i].type === liveUtils.TEMPLATE_TYPE_RECORD) return true;
        }
        return false;
    }
}

// 任务时间
function TaskTime(type, startDate, endDate, startTime, endTime, weekDay){
    this.type = type;
    this.startDate = startDate; // 开始日期
    this.endDate = endDate;     // 结束日期
    this.startTime = startTime; // 开始时间，type为3时无效
    this.endTime = endTime;     // 结束时间，type为3时无效
    this.weekDay = weekDay;     // 星期重复，transcenter为周日开始1,0,0,1,0,0,1
    this.weekDayActive = function(day){
        if(type != liveUtils.TASK_TYPE_LOOP || day > 6 || day < 0) return false;

        var a = this.weekDay.split(",");
        if(day > a.length) return false;
        return parseInt(a[day]) > 0;
    }
}

// 任务选用的模板
function TaskTemplate(id, templateId, name, url, type){
    this.id = id;
    this.templateId = templateId;
    this.name = name;
    this.url = url;
    this.type = type;
}


