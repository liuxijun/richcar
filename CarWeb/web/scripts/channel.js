/*栏目js*/
var _TREE_NODE_PREFIX = "_CHANNEL_";
var _AUDIT_FREE_FUNC = "switch_channel_audit_flag";
var _ADD_CHILD_NODE_FUNC = "add_child_channel";
var _EDIT_NODE_FUNC = "edit_channel";
var _REMOVE_NODE_FUNC = "remove_channel";
var _AUTO_GENERATE_START_ID = -100;
var _DEFAULT_AUDIT_FLAG = 0;

getChannelVisitCount = function(channel){
    // 获得频道累计访问量，包括其子频道
    if( !channel ) return 0;

    var totalVisitCount = channel.visitCount;
    if( channel.children ){
        for( var i=0; i< channel.children.length; i++ ){
            totalVisitCount += getChannelVisitCount(channel.children[i]);
        }
    }

    return totalVisitCount;
};

function channelChildrenInArray( el, array ){
    /*check whether element or any of it's heir in selected channel*/
    if( el && array){
        if( in_array(el.id, array) ) return true;
        if( el.children && el.children.length > 0 ){
            for( var i=0; i<el.children.length; i++){
                var r = channelChildrenInArray(el.children[i], array);
                if( r ) return true;
            }
        }
    }

    return false;
}

function findLeafChannel(channel, list){
    if(!channel || !list) return;

    if( channel && (!channel.children || channel.children.length == 0) ){
        list.push(channel);
    }else{
        // 包含子频道
        for(var i=0; i<channel.children.length; i++){
            findLeafChannel(channel.children[i], list);
        }
    }
}

function Channel(id, name, flag, selected){
    this.id = id;
    this.name = name;
    this.auditFlag = flag;
    this.selected = selected;
    this.children = [];
    this.visitCount = 0;
}

function ChannelUtils(){
    // 自动生成的id，为负数
    this.autoChannelId = _AUTO_GENERATE_START_ID;

    this.selectAllMark = 0;  // 无全选标识，使用selectedChannel列表
                                //  1全选 2全不选
    this.openToSelected = true; // 打开到选中节点

    this.grantedChannel = []; // 授权的栏目，可选栏目
    this.grantEnabled = false;  // 是否启用授权

    this.setGrantEnabled = function(g){ this.grantEnabled = g;};
    
    // 解析的栏目列表
    this.channelList = [];

    // 选中的节点id
    this.selectedChannel = [];

    // 从服务器返回的json串，解析存在数组中
    this.initByJson = function(json){
        var channelJson = eval("(function(){return " + json + ";})()");
        function parseChildNode(n, p, a, mark){
            // 解析子节点，递归， n为要解析的json节点，c为Channel类型父节点
            if(n){
                if(n.children){
                    // add children
                    for(var i=0; i<n.children.length; i++){
                        var cn = n.children[i];
                        if(cn){
                            var selected = in_array(cn.id, a);
                            switch( mark ){
                               case 1: selected = true; break;
                               case 2: selected = false;break;
                               default: 
                            }
                            var ch = new Channel(cn.id, cn.name, cn.auditFlag, selected );
                            //console.info("add child node:" + cn.name);
                            if( cn.children && cn.children.length > 0){
                                //console.info("add sub child node:" + cn.name);
                                parseChildNode(cn, ch, a, mark);
                            }
                            if(p){
                                p.children.push(ch);
                            }
                        }
                    }
                }
            }
        }

        for(var n=0; n<channelJson.length; n++){
            var cn = channelJson[n];
            if(cn){
                var selected = in_array(cn.id, this.selectedChannel);
                switch( this.selectAllMark ){
                   case 1: selected = true; break;
                   case 2: selected = false;break;
                   default:
                }
                var ch = new Channel(cn.id, cn.name, cn.auditFlag, selected);
                //console.info("add root node:" + cn.name);
                if( cn.children && cn.children.length > 0){
                    // send selectedChannel to embeded func, or it cannot access it
                    parseChildNode(cn, ch, this.selectedChannel, this.selectAllMark);
                }
                this.channelList.push(ch);
            }
        }
    };

    this.getAllChildrenNode = function(){
        if(this.channelList.length == 0) return null;
        var _a = [];
        var parseChildren = function(node, array){
            if(node && node.children){
                for(var i=0; i<node.children.length; i++){
                    var n = node.children[i];
                    if( n.children && n.children.length > 0){
                        parseChildren(n, array);
                    }else{
                        array.push(n);
                    }
                }
            }
        };

        for(var i=0; i<this.channelList.length; i++){
            var n = this.channelList[i];
            if( n.children && n.children.length > 0){
                parseChildren(n, _a);
            }else{
                _a.push(n);
            }
        }

        return _a;
    };

    // 设置选中节点
    this.setSelectedChannel = function(channelString){
        this.selectedChannel = (""+channelString).split(",");
        //console.info("selected channel len:" + this.selectedChannel.length);
    };

    // 生成flueux树的串
    // 生成树形多选或栏目过滤树
    // 节点选中受selectedChannel和selectAllMark影响
    // 如果grantEnabled被设置，根据grantedChannel，只生成有权限节点及其父节点
    this.generateTreeData = function(){
        var chList = ( arguments.length > 0)? arguments[0] : this.channelList;
        if( chList.length == 0) return null;

        var hasGrantedInChildren = function(c, a){
            /*自己或子节点是否有在授权节点中的*/
            if( c ){
                if( in_array(c.id, a) ){
                    return true;
                }
                for( var i=0; i<c.children.length; i++){
                    var m = hasGrantedInChildren(c.children[i], a);
                    //console.info(c.children[i].name + " has children in granted:" + m);
                    if( m ) return true;
                }
                return false;
            }else{
                return false;
            }
        };

        var tree_data = {};
        var buildChildren = function(ch, prop, a, mark, useGrant, granted, openToSelected){
            // 生成节点的子节点对象
            for(var i=0; i<ch.children.length;i++){
                var c_ch = ch.children[i];

                if(useGrant && !hasGrantedInChildren(c_ch, granted)) continue;

                var ch_type = (c_ch.children && c_ch.children.length > 0)? "folder" : "item";
                prop[_TREE_NODE_PREFIX + c_ch.id] = {
                    "name" : c_ch.name,
                    "type" : ch_type,
                    "id"    : c_ch.id
                };

                //var selected = in_array(c_ch.id, a);
                var selected = channelChildrenInArray(c_ch, a);
                switch( mark ){
                   case 1: selected = true; break;
                   case 2: selected = false;break;
                   default:
                }

                if(ch_type == "item" && selected){
                    prop[_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"] = {"item-selected" : true};
                }else if(ch_type == "folder"){
                    if( openToSelected && selected ){
                        /*folder channel can't been selected, just opened*/
                        prop[_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"] = {"item-opened" : true};
                    }else{
                        prop[_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"] = {};
                    }
                    prop[_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["children"] = {};
                    buildChildren(c_ch, prop[_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["children"], a, mark, useGrant, granted,openToSelected);
                }
            }
        };

        for(var i=0; i<chList.length; i++){
            // 以属性的形式付给tree_data
            var ch = chList[i];

            if(this.grantEnabled && !hasGrantedInChildren(ch, this.grantedChannel)) continue;

            var ch_type = (ch.children && ch.children.length > 0)? "folder" : "item";
            tree_data[_TREE_NODE_PREFIX + ch.id] = {
                "name" : ch.name,
                "type" : ch_type,
                "id" : ch.id
            };
            //var selected = in_array(ch.id, this.selectedChannel);
            var selected = channelChildrenInArray(ch, this.selectedChannel);
            switch( this.selectAllMark ){
               case 1: selected = true; break;
               case 2: selected = false;break;
               default:
            }

            if(ch_type == "item" && selected){
                tree_data[_TREE_NODE_PREFIX + ch.id]["additionalParameters"] = {"item-selected" : true};
            }else if(ch_type == "folder"){
                if( this.openToSelected && selected) {
                    tree_data[_TREE_NODE_PREFIX + ch.id]["additionalParameters"] = {"item-opened" : true};
                    //console.info(ch.name + " opened ");
                }else{
                    tree_data[_TREE_NODE_PREFIX + ch.id]["additionalParameters"] = {};
                }
                tree_data[_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["children"] = {};
                buildChildren(ch, tree_data[_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["children"],
                        this.selectedChannel, this.selectAllMark, this.grantEnabled, this.grantedChannel, this.openToSelected);
            }
        }

        return new DataSourceTree({data: tree_data});
    };

    this.generateFilterTree = function(){
        // 构建一个用于过滤条件的组织树，和generateTreeData不同之处，在于如果有多个根栏目，自动创建一个名为“所有栏目的节点”
        if( this.channelList.length <= 1)  return this.generateTreeData();

        var super_channelList = [];
        var superChannel = new Channel(-1, "所有栏目", 0, false);
        for( var i=0; i<this.channelList.length; i++){
            superChannel.children.push(this.channelList[i]);
        }
        super_channelList.push(superChannel);
        return this.generateTreeData(super_channelList);
    };

    // 生成nestable数据，在栏目管理时使用
    this.generateNestableData = function(){
        var nestableChildren = function(ch){
            if( ch && ch.children && ch.children.length > 0 ){
                output += '<ol class="bb-list">';
                for(var j=0; j<ch.children.length; j++){
                    var child = ch.children[j];
                    if(child){
                        output += '<li class="bb-item" data-id="' + child.id +  '" data-name="' + encodeURI(child.name) + '" data-flag="' + child.auditFlag + '">';
                        // op button
                        output += '<div class="bb-handle"><span>' + child.name +
                                  '</span><div class="pull-right action-buttons">';
                        //if( !child.children || child.children.length == 0 ){
                            // audit free flag
                            //console.info("audit flag:" + child.auditFlag);
                            if( parseInt(child.auditFlag) == 1 ){
                                output += '<a class="blue" href="#" style="margin-right: 40px" title="取消频道免审" onclick="' +
                                          _AUDIT_FREE_FUNC + '(' + child.id + ');return false;">' +
                                      '<i class="ace-icon fa fa-flag bigger-130"></i>' +
                                      '</a>';
                            }else{
                                output += '<a class="green" href="#" style="margin-right: 40px" title="设置频道免审" onclick="' +
                                          _AUDIT_FREE_FUNC + '(' + child.id + ');return false;">' +
                                      '<i class="ace-icon fa fa-flag-o bigger-130"></i>' +
                                      '</a>';
                            }
                        //}
                        output += '<a class="green" href="#" title="添加子频道" onclick="' + _ADD_CHILD_NODE_FUNC +
                                  '(' + child.id + ');return false;">' + 
                                  '<i class="ace-icon fa fa-plus bigger-130"></i>' +
                                  '</a><a class="blue" href="#" title="编辑频道" onclick="' + _EDIT_NODE_FUNC +
                                  '(' + child.id + ');return false;">' +
                                  '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
                                  '</a>' +
                                  '<a class="red" href="#" title="删除频道" onclick="' + _REMOVE_NODE_FUNC +
                                  '(' + child.id + ');return false;">' +
                                  '<i class="ace-icon fa fa-trash-o bigger-130"></i> ' +
                                  '</a></div>';
                        
                        output += '</div>';
                        if( child.children && child.children.length > 0 ){
                            nestableChildren(child);
                        }
                        output += '</li>';
                    }
                }
                output += '</ol>';
            }
        };

        var output = '<ol class="bb-list">';
        for(var i=0; i<this.channelList.length; i++){
            var ch = this.channelList[i];
            if(ch){
                output += '<li class="bb-item" data-id="' + ch.id + '" data-name="' + encodeURI(ch.name) + '" data-flag="' + ch.auditFlag + '">';
                         // op button
                output += '<div class="bb-handle"><span>' + ch.name +
                          '</span><div class="pull-right action-buttons">';
                //if( !ch.children || ch.children.length == 0 ){
                    // audit free flag
                    //console.info("audit flag:" + child.auditFlag);
                    if( parseInt(ch.auditFlag) == 1 ){
                        output += '<a class="blue" href="#" style="margin-right: 40px" title="取消频道免审" onclick="' +
                                  _AUDIT_FREE_FUNC + '(' + ch.id + ');return false;">' +
                              '<i class="ace-icon fa fa-flag bigger-130"></i>' +
                              '</a>';
                    }else{
                        output += '<a class="green" href="#" style="margin-right: 40px" title="设置频道免审" onclick="' +
                                  _AUDIT_FREE_FUNC + '(' + ch.id + ');return false;">' +
                              '<i class="ace-icon fa fa-flag-o bigger-130"></i>' +
                              '</a>';
                    }
                //}
                output += '<a class="green" href="#" title="添加子频道" onclick="' + _ADD_CHILD_NODE_FUNC + '(' + ch.id + ')">' +
                          '<i class="ace-icon fa fa-plus bigger-130"></i>' +
                          '</a><a class="blue" href="#" title="编辑频道" onclick="' + _EDIT_NODE_FUNC +
                          '(' + ch.id + ');return false;">' +
                          '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
                          '</a>' +
                          '<a class="red" href="#" title="删除频道" onclick="' + _REMOVE_NODE_FUNC +
                          '(' + ch.id + ');return false;">' +
                          '<i class="ace-icon fa fa-trash-o bigger-130"></i> ' +
                          '</a></div>';

                output += '</div>';
                if( ch.children && ch.children.length > 0 ){
                    nestableChildren(ch);
                }
                output += '</li>';
            }
        }
        output += '</ol>';

        //console.info( output );

        return output;
    };

    this.generateNewChannel = function(pid, name){
        // 返回新节点的html代码
        var output = '<li class="bb-item" data-id="' + this.autoChannelId + '"  data-name="' + encodeURI(name) + '" data-flag="' + _DEFAULT_AUDIT_FLAG + '">' +
                     '<div class="bb-handle"><span>' + name +
                  '</span><div class="pull-right action-buttons">';
        //if( !child.children || child.children.length == 0 ){
            // audit free flag
            //console.info("audit flag:" + child.auditFlag);
            if( parseInt(_DEFAULT_AUDIT_FLAG) == 1 ){
                output += '<a class="blue" href="#" style="margin-right: 40px" title="取消频道免审" onclick="' +
                          _AUDIT_FREE_FUNC + '(' + this.autoChannelId + ');return false;">' +
                      '<i class="ace-icon fa fa-flag bigger-130"></i>' +
                      '</a>';
            }else{
                output += '<a class="green" href="#" style="margin-right: 40px" title="设置频道免审" onclick="' +
                          _AUDIT_FREE_FUNC + '(' + this.autoChannelId  + ');return false;">' +
                      '<i class="ace-icon fa fa-flag-o bigger-130"></i>' +
                      '</a>';
            }
        //}
        output += '<a class="green" href="#" title="添加子频道" onclick="' + _ADD_CHILD_NODE_FUNC +
                  '(' + this.autoChannelId + ');return false;">' +
                  '<i class="ace-icon fa fa-plus bigger-130"></i>' +
                  '</a><a class="blue" href="#" title="编辑频道" onclick="' + _EDIT_NODE_FUNC +
                  '(' + this.autoChannelId + ');return false;">' +
                  '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
                  '</a>' +
                  '<a class="red" href="#" title="删除频道" onclick="' + _REMOVE_NODE_FUNC +
                  '(' + this.autoChannelId + ');return false;">' +
                  '<i class="ace-icon fa fa-trash-o bigger-130"></i> ' +
                  '</a></div>';

        output += '</div></li>';

        this.autoChannelId--;
        return output;
    };

    this.addChild = function(pid, title){
        // 添加子节点，在channelList中添加,nestable在jsp中添加，并刷新
        var parentChannel = this.findChannel(pid);
        if(!parentChannel) return null;

        var channel = new Channel(this.autoChannelId, title, _DEFAULT_AUDIT_FLAG, false);
        parentChannel.children.push(channel);

        return channel;
    };

    this.findChannel = function(id){
        var iterateChild = function( channel, id){
            if( !channel ) return null;

            if( channel.children && channel.children.length > 0 ){
                for(var i=0; i<channel.children.length; i++){
                    var c = channel.children[i];
                    if( !c ) continue;

                    if( c.id == id ){
                        return c;
                    }else if(c.children && c.children.length > 0){
                        var cc = iterateChild( c, id );
                        if( cc ) return cc;
                    }
                }
            }

            return null;
        };

        for(var i=0; i< this.channelList.length; i++){
            var channel = this.channelList[i];
            if(channel){
                if(channel.id == id){
                    return channel;
                }else if(channel.children && channel.children.length > 0){
                    var cc = iterateChild(channel, id);
                    if( cc ) return cc;
                }
            }
        }
        return null;

    };

    this.getFirstLevelChannel = function(){
        // 获取第一层的栏目，用于过滤
        var firstLvChannelList = [];
        if( this.channelList ){
            return ( this.channelList.length > 1) ? this.channelList :
                       this.channelList[0].children;
/*
            for(var i=0; i<list.length; i++){
                firstLvChannelList.push(list[i]);
            }
*/
        }
    };

    this.getLeafChannel = function(){
        // 获取所有的叶子频道
        var leafChannelList = [];
        for(var i=0; i<this.channelList.length; i++){
            var channel = this.channelList[i];
            findLeafChannel(channel, leafChannelList);
        }

        return leafChannelList;
    };

    this.setGrantChannel = function(s){
        this.grantedChannel = (""+s).split(",");
    };


    this.resetAllVisitCount = function(){
        // 重置所有频道访问量
        var resetChannelVisitCount = function( channel ){
            // 重置频道访问量，包括其子频道
            if( !channel ) return;

            channel.visitCount = 0;
            if( channel.children ){
                for( var i=0; i< channel.children.length; i++ ){
                    resetChannelVisitCount(channel.children[i]);
                }
            }
        };

        for( var i=0; i<this.channelList.length; i++){
            resetChannelVisitCount(this.channelList[i]);
        }
    };

    this.setChannelVisitCount = function(channelId, visitCount){
        var channel = this.findChannel(channelId);
        if( channel ){
            channel.visitCount = parseInt(visitCount);
        }
    };

    // 生成栏目访问统计样式，全部展开的nestable 树，后面没有操作区域，显示访问数量
    // 基本逻辑同generateNestableData，为避免逻辑复杂化，另写的方法
    this.generateStat = function(){
        var nestableChildren = function(ch){
            if( ch && ch.children && ch.children.length > 0 ){
                output += '<ol class="bb-list">';
                for(var j=0; j<ch.children.length; j++){
                    var child = ch.children[j];
                    if(child){
                        output += '<li class="bb-item" data-id="' + child.id +  '">';
                        // op button
                        output += '<div class="bb-handle"><span>' + child.name +
                        '</span><div class="pull-right action-buttons">' +
                        numberWithCommas(getChannelVisitCount(child)) +
                        '</div>';

                        output += '</div>';
                        if( child.children && child.children.length > 0 ){
                            nestableChildren(child);
                        }
                        output += '</li>';
                    }
                }
                output += '</ol>';
            }
        };

        var output = '<ol class="bb-list">';
        for(var i=0; i<this.channelList.length; i++){
            var ch = this.channelList[i];
            if(ch){
                output += '<li class="bb-item" data-id="' + ch.id + '">';
                // op button
                output += '<div class="bb-handle"><span>' + ch.name +
                '</span><div class="pull-right action-buttons">' +
                numberWithCommas(getChannelVisitCount(ch)) + '</div>';

                output += '</div>';
                if( ch.children && ch.children.length > 0 ){
                    nestableChildren(ch);
                }
                output += '</li>';
            }
        }
        output += '</ol>';

        //console.info( output );

        return output;
    }
}