var timers={}

function registerTimer(id, start, end){
    timers[id]=[new Date(start),new Date(end)];
    console.log("Added new timer:"+id+" --> "+timers[id]);
}

function e2(x){
    if(x<10){
        return "0"+x
    }else{
        return ""+x
    }
}

function parseNum(f){
    var nano=f%1000
    f=(f-nano)/1000
    var sec=f%60
    f=(f-sec)/60
    var min=f%60
    f=(f-min)/60
    var hour=f%24
    f=(f-hour)/24
    var day=f
    if(day!=0){
        return day+"D "+e2(hour)+":"+e2(min)+":"+e2(sec)
    }else{
        return e2(hour)+":"+e2(min)+":"+e2(sec)
    }
}

function timerTick(){
    var now=new Date()
    for(var i in timers){
        var v=timers[i]
        var st=v[0]
        var ed=v[1]
        if(now<st){
            $('.'+i).html(parseNum(st-now))
        }else if(now<ed){
            $('.'+i).html(parseNum(ed-now)+" remaining")
        }else{
            $('.'+i).html("Ended "+parseNum(now-ed)+" ago")
        }
    }
}

setInterval(timerTick,1000)
