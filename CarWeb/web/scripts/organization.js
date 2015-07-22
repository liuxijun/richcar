/*栏目js*/
var _ORG_TREE_NODE_PREFIX = "_ORG_";
var _ORG_ADD_CHILD_NODE_FUNC = "add_child_org";
var _ORG_EDIT_NODE_FUNC = "edit_org";
var _ORG_REMOVE_NODE_FUNC = "remove_org";
var _ORG_GENERATE_START_ID = -423;

function organizationChildrenInArray( el, array ){
    /*check whether element or any of it's heir in selected array, return 1 if el in array, 2 if it's heir in, otherwise 0*/
    if( el && array){
        if( in_array(el.id, array) ) return 1;
        if( el.children && el.children.length > 0 ){
            for( var i=0; i<el.children.length; i++){
                var r = organizationChildrenInArray(el.children[i], array);
                if( r > 0 ) return 2;
            }
        }
    }

    return 0;
}

function Organization(id, name, channels, userCount, selected){
    this.id = id;
    this.name = name;
    this.channels = channels;
    this.userCount = userCount;
    this.selected = selected;
    this.children = [];
}

function OrganizationUtils(){
    // 自动生成的id，为负数
    this.autoOrganizationId = _ORG_GENERATE_START_ID;

    // 是否自动打开到选中节点
    this.openToSelected = true;

    // 解析的组织列表
    this.organizationList = [];

    // 选中的节点id
    this.selectedOrganization = [];

    // 从服务器返回的json串，解析存在数组中
    this.initByJson = function(json){
        //var orgJson = eval("(function(){return " + json + ";})()");
        var orgJson = JSON.parse(json);
        if(!orgJson) return;

        function parseChildNode(n, p, a){
            // 解析子节点，递归， n为要解析的json节点，p为父节点， a为选中节点列表
            if(n){
                if(n.children){
                    // add children
                    for(var i=0; i<n.children.length; i++){
                        var cn = n.children[i];
                        if(cn){
                            var org = new Organization(cn.id, cn.name,cn.channels, cn.count, in_array(cn.id, a));
                            //console.info("add child node:" + cn.name);
                            if( cn.children && cn.children.length > 0){
                                //console.info("add sub child node:" + cn.name);
                                parseChildNode(cn, org, a);
                            }
                            if(p){
                                p.children.push(org);
                            }
                        }
                    }
                }
            }
        }

        //console.info("length:" + orgJson.length);
        for(var n=0; n<orgJson.length; n++){
            var cn = orgJson[n];
            if(cn){
                var org = new Organization(cn.id, cn.name, cn.channels, cn.count, in_array(cn.id, this.selectedOrganization));
                //console.info("add root node:" + cn.name);
                if( cn.children && cn.children.length > 0){
                    // send selectedChannel to embeded func, or it cannot access it
                    parseChildNode(cn, org, this.selectedOrganization);
                }
                this.organizationList.push(org);
            }
        }
    };

    // 设置选中节点
    this.setSelectedOrganization = function(orgString){
        this.selectedOrganization = (""+orgString).split(",");

        //console.info("selected org len:" + this.selectedOrganization.length);
    };

    // 生成子节点的方法
    var buildChildren = function(org, prop, a, openToSelected){
        // 生成节点的子节点对象
        for(var i=0; i<org.children.length;i++){
            var c_ch = org.children[i];
            var org_type = (c_ch.children && c_ch.children.length > 0)? "folder" : "item";
            prop[_ORG_TREE_NODE_PREFIX + c_ch.id] = {
                "name" : c_ch.name,
                "type" : org_type,
                "id"    : c_ch.id
            };

            if(org_type == "item" && in_array(c_ch.id, a)){
                //console.info("set selected:" + c_ch.name);
                prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"] = {"item-selected" : true};
            }else if(org_type == "folder"){
                prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"] = {};
                var sel = organizationChildrenInArray(c_ch, a);
                if(sel == 1){
                    //console.info("set selected:" + c_ch.name);
                    prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["item-selected"] = true;
                }else if( sel == 2 && openToSelected){
                    prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["item-opened"] = true;
                }
                prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["children"] = {};
                buildChildren(c_ch, prop[_ORG_TREE_NODE_PREFIX + c_ch.id]["additionalParameters"]["children"], a);
            }
        }
    };

    // 生成flueux树的串
    this.generateTreeData = function(){
        //var orgList = ( arguments.length > 0)? arguments[0] : this.organizationList;
        //alert(arguments.length + "  all fuck");
        var orgList = this.organizationList;
        //console.info("generate data:" + this.channelList.length);
        if( orgList.length == 0) return null;

        var tree_data = {};

        for(var i=0; i<orgList.length; i++){
            // 以属性的形式付给tree_data
            var ch = orgList[i];
            var ch_type = (ch.children && ch.children.length > 0)? "folder" : "item";
            tree_data[_ORG_TREE_NODE_PREFIX + ch.id] = {
                "name" : ch.name,
                "type" : ch_type,
                "id" : ch.id
            };
            if(ch_type == "item" && in_array(ch.id, this.selectedOrganization)){
                tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"] = {"item-selected" : true};
            }else if(ch_type == "folder"){
                tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"] = {};
                var sel = organizationChildrenInArray(ch, this.selectedOrganization);
                if(sel == 1){
                    tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["item-selected"] = true;
                }else if(sel == 2 && this.openToSelected){
                    tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["item-opened"] = true;
                }
                tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["children"] = {};
                buildChildren(ch, tree_data[_ORG_TREE_NODE_PREFIX + ch.id]["additionalParameters"]["children"],
                    this.selectedOrganization, this.openToSelected);
            }
        }

        return new DataSourceTree({data: tree_data});
    };

    this.generateFilterTree = function(){
        // 构建一个用于过滤条件的组织树，和generateTreeData不同之处，在于如果有多个根组织，自动创建一个名为“所有组织的节点”
        if( this.organizationList.length <= 1)  return this.generateTreeData();

        var super_organizationList = [];
        var superOrg = new Organization(-1, "所有组织", "", 9999, false);
        for( var i=0; i<this.organizationList.length; i++){
            superOrg.children.push(this.organizationList[i]);
        }
        super_organizationList.push(superOrg);
        return this.generateTreeData(super_organizationList);
    };
    // 生成nestable数据
    this.generateNestableData = function(){
        var nestableChildren = function(org){
            if( org && org.children && org.children.length > 0 ){
                output += '<ol class="bb-list">';
                for(var j=0; j<org.children.length; j++){
                    var child = org.children[j];
                    if(child){
                        output += '<li class="bb-item" data-id="' + child.id +  '" data-name="' +
                        encodeURI(child.name) + '" data-channels="' + child.channels +
                        '" data-user-count="' + child.userCount + '">';
                        // op button
                        output += '<div class="bb-handle"><span>' + child.name +
                        '</span><div class="pull-right action-buttons">' +
                        '<a class="green" href="#" title="添加子组织" onclick="' + _ORG_ADD_CHILD_NODE_FUNC +
                        '(' + child.id + ');return false;">' +
                        '<i class="ace-icon fa fa-plus bigger-130"></i>' +
                        '</a><a class="blue" href="#" title="编辑组织" onclick="' + _ORG_EDIT_NODE_FUNC +
                        '(' + child.id + ');return false;">' +
                        '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
                        '</a>' +
                        '<a class="red" href="#" title="删除组织" onclick="' + _ORG_REMOVE_NODE_FUNC +
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
        for(var i=0; i<this.organizationList.length; i++){
            var org = this.organizationList[i];
            if(org){
                output += '<li class="bb-item" data-id="' + org.id + '" data-name="' +
                encodeURI(org.name) + '" data-channels="' + org.channels +
                '" data-user-count="' + org.userCount +  '">';
                // op button
                output += '<div class="bb-handle"><span>' + org.name +
                '</span><div class="pull-right action-buttons">' +
                '<a class="green" href="#" title="添加子组织" onclick="' + _ORG_ADD_CHILD_NODE_FUNC + '(' + org.id + ')">' +
                '<i class="ace-icon fa fa-plus bigger-130"></i>' +
                '</a><a class="blue" href="#" title="编辑组织" onclick="' + _ORG_EDIT_NODE_FUNC +
                '(' + org.id + ');return false;">' +
                '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
                '</a>' +
                '<a class="red" href="#" title="删除组织" onclick="' + _ORG_REMOVE_NODE_FUNC +
                '(' + org.id + ');return false;">' +
                '<i class="ace-icon fa fa-trash-o bigger-130"></i> ' +
                '</a></div>';

                output += '</div>';
                if( org.children && org.children.length > 0 ){
                    nestableChildren(org);
                }
                output += '</li>';
            }
        }
        output += '</ol>';

        //console.info( output );

        return output;
    };

    this.generateNewOrganization = function(pid, name, channels){
        // 返回新节点的html代码
        var output = '<li class="bb-item" data-id="' + this.autoOrganizationId + '"  data-name="' + encodeURI(name) + '" data-channels="' +
            channels + '" data-user-count="0">' +
            '<div class="bb-handle"><span>' + name +
            '</span><div class="pull-right action-buttons">'+
            '<a class="green" href="#" title="添加子组织" onclick="' + _ORG_ADD_CHILD_NODE_FUNC +
            '(' + this.autoOrganizationId + ');return false;">' +
            '<i class="ace-icon fa fa-plus bigger-130"></i>' +
            '</a><a class="blue" href="#" title="编辑组织" onclick="' + _ORG_EDIT_NODE_FUNC +
            '(' + this.autoOrganizationId + ');return false;">' +
            '<i class="ace-icon fa fa-pencil bigger-130"></i>' +
            '</a>' +
            '<a class="red" href="#" title="删除组织" onclick="' + _ORG_REMOVE_NODE_FUNC +
            '(' + this.autoOrganizationId + ');return false;">' +
            '<i class="ace-icon fa fa-trash-o bigger-130"></i> ' +
            '</a></div>';

        output += '</div></li>';

        this.autoOrganizationId--;
        return output;
    };

    this.addChild = function(pid, title){
        // 添加子节点，在channelList中添加,nestable在jsp中添加，并刷新
        var parentOrg = this.findOrganization(pid);
        if(!parentOrg) return null;

        var org = new Organization(this.autoOrganizationId, title, false);
        parentOrg.children.push(org);

        return org;
    };

    this.findOrganization = function(id){
        var iterateChild = function( org, id){
            if( !org ) return null;

            if( org.children && org.children.length > 0 ){
                for(var i=0; i<org.children.length; i++){
                    var c = org.children[i];
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

        for(var i=0; i< this.organizationList.length; i++){
            var org = this.organizationList[i];
            if(org){
                if(org.id == id){
                    return org;
                }else if(org.children && org.children.length > 0){
                    var cc = iterateChild(org, id);
                    if( cc ) return cc;
                }
            }
        }
        return null;

    };

    this.getFirstLevelOrg = function(){
        // 获取第一层，用于过滤
        if( this.organizationList ){
            return ( this.organizationList.length > 1) ? this.organizationList :
                this.organizationList[0].children;
            /*
             for(var i=0; i<list.length; i++){
             firstLvChannelList.push(list[i]);
             }
             */
        }
    }
}