var functionQueue=null;
function queueFunctions(functions,nextFunction){
    functionQueue={
        functions:functions,
        nextFunction:nextFunction
    };
    //执行所有的代码
    for(var fn=0;fn<functions.length;fn++){
        functions[fn].func.call();
    }
}
function functionDone(functionFlag){
    var allDone = true;
    if(functionQueue==null){
        return allDone;
    }
    try{
        var funList = functionQueue.functions;
        if(typeof(outDebugMsg)=='function'){
            outDebugMsg(functionFlag + '已经执行完毕');
        }
        window.status = functionFlag+' finished!';
        for(var fn=0;fn<funList.length;fn++){
            var thisFun = funList[fn];
            if(thisFun){
                if(thisFun.flag==functionFlag){
                    thisFun.done = true;
                }
            }
            if(!thisFun.done){
                allDone = false;
            }
        }


        if(allDone){
            try{
                if(typeof(outDebugMsg)=='function'){
                    var msg= ('所有进程已经执行完毕，执行最后的一步！');
                    //alert(msg);
                    outDebugMsg(msg);
                }
            }catch(e){

            }
            functionQueue.nextFunction.call();
            return true;
        }else{
            return false;
        }
    }catch(e){

    }
    return false;
}
function test(){
    queueFunctions([{func:test1,done:false,flag:'test1'},{func:test2,done:false,flag:'test2'},
        {func:test3,done:false,flag:'test3'}],allDone);
}
function test1(){
   window.setTimeout("functionDone('test1')",1000);
}
function test2(){
    window.setTimeout("functionDone('test2')",2000);
}
function test3(){
    window.setTimeout("functionDone('test3')",3000);
}

function allDone(){
   alert('allDone'); 
}

function formatPercent(baseNumber) {
	return baseNumber.toFixed(2) + " %";
}

function formatBPS(baseNumber) {
    var bpsUnits = [1073741824, 1048576, 1024, 1], bpsUnitLabels = ["Gbps", "Mbps", "Kbps", "bps"];
    return formatUnits(baseNumber, bpsUnits, bpsUnitLabels, true);

}

function formatTime(baseNumber) {
    var timeUnits = [86400, 3600, 60, 1], timeUnitLabels = ["天", "小时", "分", "秒"];
    return formatUnits(baseNumber, timeUnits, timeUnitLabels, false);
}

function formatBytes(baseNumber) {
    var sizeUnits = [1073741824, 1048576, 1024, 1], sizeUnitLabels = ["GB", "MB", "KB", "字节"];
    return formatUnits(baseNumber, sizeUnits, sizeUnitLabels, true);

}

function formatUnits(baseNumber, unitDivisors, unitLabels, singleFractional) {
    var i, unit, unitDivisor, unitLabel;

    if (baseNumber === 0) {
        return "0 " + unitLabels[unitLabels.length - 1];
    }

    if (singleFractional) {
        unit = baseNumber;
        unitLabel = unitLabels.length >= unitDivisors.length ? unitLabels[unitDivisors.length - 1] : "";
        for (i = 0; i < unitDivisors.length; i++) {
            if (baseNumber >= unitDivisors[i]) {
                unit = (baseNumber / unitDivisors[i]).toFixed(2);
                unitLabel = unitLabels.length >= i ? " " + unitLabels[i] : "";
                break;
            }
        }

        return unit + unitLabel;
    } else {
        var formattedStrings = [];
        var remainder = baseNumber;

        for (i = 0; i < unitDivisors.length; i++) {
            unitDivisor = unitDivisors[i];
            unitLabel = unitLabels.length > i ? " " + unitLabels[i] : "";

            unit = remainder / unitDivisor;
            if (i < unitDivisors.length -1) {
                unit = Math.floor(unit);
            } else {
                unit = unit.toFixed(2);
            }
            if (unit > 0) {
                remainder = remainder % unitDivisor;

                formattedStrings.push(unit + unitLabel);
            }
        }

        return formattedStrings.join(" ");
    }
}
