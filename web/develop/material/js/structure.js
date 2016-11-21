$(document).ready(function(){
    // 判断图片加载状况，加载完成后回调
    isImgLoad(function(){
        // 加载完成
        $("#sidebar").width($("#logo").css("width"));
        $("#sidebar").css("top",$("#topMenu").css("height"));
        if($("#mobileTopMenu").is(":hidden")){
            $(".full.height").css("marginTop",$("#topMenu").css("height"));
        }else{
            $(".full.height").css("marginTop",$("#mobileTopMenu").css("height"));
        }
        $(".constant").width($("#logo").css("width"));
    });

    $("#menuIcon").click(function(){
        $("#mobileSidebar").sidebar('setting', 'transition', 'uncover').sidebar('toggle');
    });

    $('.ui.accordion').accordion({
        selector: {
            trigger: '.title'
        }
    });

});

function isImgLoad(callback){
    // 查找所有图片，迭代处理
    $('.image').each(function(){
        // 找到为0就将isLoad设为false，并退出each
        if(this.height === 0){
            isLoad = false;
            return false;
        }
    });
    // 为true，没有发现为0的。加载完毕
    if(isLoad){
        clearTimeout(t_img); // 清除定时器
        // 回调函数
        callback();
        // 为false，因为找到了没有加载完成的图，将调用定时器递归
    }else{
        isLoad = true;
        t_img = setTimeout(function(){
            isImgLoad(callback); // 递归扫描
        },100); // 我这里设置的是500毫秒就扫描一次，可以自己调整
    }
}
