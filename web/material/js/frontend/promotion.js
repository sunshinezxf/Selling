$("#goods_num").on("input propertychange",function(){
	var num=$("#goods_num").val();
	if(num>=2){
		$("#purchase").text("买1赠1");
	}
});