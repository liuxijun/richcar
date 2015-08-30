var carFriend={
    menus:{
        richNav:{//
            text:'虞乔导航',
            desp:'',
            doRender:function(){

            }
        },
        carProduct:{
            text:'汽车产品',
            desp:'',
            doRender:function(){

            }
        },
        service:{
            text:'服务质量',
            desp:'',
            doRender:function(){
                utils.openDialog({
                    href: "#serviceDialog",
                    dialog: "MessageServiceDialog",
                    transition:'fade',
                    callback: function () {
                        $("#btnOk").unbind("click").bind("click", function () {
                            $("#MessageServiceDialog").dialog("close");
                        });
                    }
                });
            }
        },
        repairProcess:{
            text:'维修进度',
            desp:'',
            doRender:function(){
                utils.grid({
                    header:[
                        {name:'id',title:'序号',align:'center',width:'10%'},
                        {name:'name',title:'项目',align:'center',width:'80%'},
                        {name:'process',title:'进度',align:'center',width:'10%'}
                    ],
                    store:{
                        data:[
                            {id:'1',process:'90%',name:'机油机滤更换'},
                            {id:'2',name:'动力系统检查',process:'95%'},
                            {id:'3',name:'四轮定位',process:'0%'}
                        ]
                    },
                    renderTo:'mainBody'
                });
            }
        },
        repairSolution:{
            text:'维修方案',
            desp:'',
            doRender:function(){

            }
        },
        maintenance:{
            text:'养护方案',
            desp:'',
            doRender:function(){

            }
        },
        carInfo:{
            text:'车辆信息',
            desp:'',
            doRender:function(){

            }
        }
    },
    renderHome:function(){
        var result = "";
        var menus = carFriend.menus;
        //var i= 0,l=menus.length;
        //alert("准备渲染");
        for(var menuId in menus){
            if(menus.hasOwnProperty(menuId)){
                var menu = menus[menuId];
                result +='<div id="homeMenu_'+menuId+'" class="homeMenu homeMenu_'+menuId+'"' +
                    ' onclick="carFriend.clickOnMenu(\''+menuId+'\')">';
                result +='<h1>'+menu['text']+'</h1><span>' +menu['desp']+'</span>';
                result+='</div>';
            }
        }
        $("#mainBody").html(result);
    },
    scale:1,
    selectedMenu:null,
    selectedMenuClsName:'homeMenuMouseOver',
    selectMenu:function(){
        $("."+carFriend.selectedMenuClsName).removeClass(carFriend.selectedMenuClsName);
        if(carFriend.selectedMenu!=null){
            var menu = carFriend.menus[carFriend.selectedMenu];
            if(menu!=null){
                menu.doRender();
            }
        }
    },
    clickOnMenu:function(menuId){
        var clsName = carFriend.selectedMenuClsName;
        $("."+clsName).removeClass(clsName);
        $("#homeMenu_"+menuId).addClass(clsName);
        carFriend.selectedMenu = menuId;
        window.setTimeout("carFriend.selectMenu('" +menuId+"')",500);
    }
};
