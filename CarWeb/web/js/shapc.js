$(function(){
	$(".dbug").click(function(){
		$(".menu").hide();
		$(".ls_xq").hide();
	})
	$(".foot>ul>li:last").css("background","none");
	$(".foot>ul>li>a").click(function(){
		$(this).siblings(".menu").find("li:last").css("background","none");	
		$(this).parent().siblings().children(".menu").hide();					
		$(this).siblings(".menu").toggle();
		return false;	
	})
	$(".p_list img").click(function(){
		$(this).siblings(".ls_xq").toggle(200);
		return false;
	})
	$(".main_image ul li img").bind("load",function(){
  	var dh=$(".main_image img").height();
  	$(".main_visual").css("min-height",188);
  	$(".main_visual").height(dh);
	$(".ad_font").height(dh);
	$(".banner").height(dh);	
 })


});


		$(".ms_se select").change(function(){
			var $a=$(this).val();
			$(this).siblings("span").html($a);
		})
		$(".ms_se select").change(function(){
			var $a=$(this).val();
			$(this).siblings("span").html($a);
		})
		$(".msx_se select").change(function(){
			var $a=$(this).val();
			$(this).siblings("span").html($a);
		})
		$(".msx_se select").change(function(){
			var $a=$(this).val();
			$(this).siblings("span").html($a);
		})
		

  