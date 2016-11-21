$(document).ready(function(){
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