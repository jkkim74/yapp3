$(document).ready(function() {
	$('.layerPop').hide();
	
	//$('#mask').click(function () {
	//	$(this).hide();
	//	$('.layerPop').hide();
	//});
	product_event();
	
	if(chkMsg == '' || chkMsg == null){
		if($('[name=giftEventYn]').val() == "Y"){
			onNotice('#noticePop');
		}
	}
	
});

function product_event(){ // 경품 이벤트
	var pop = $('#eventPop1');
	$(document).on("change", "#eventPop1 input[name='choice']", function(){
		$('[name=post]').val('');
		$('[name=addr]').val('');
		$('[name=addrDtl]').val('');
		var product_value = $(this).val();
		var dataSeq = $(this).attr("data-seq");
		var dataType = $(this).attr("data-type");
		var addrUseYn = $(this).attr("data-use");
		var remainCnt = $(this).attr("data-cnt");
		var totalCnt = $('#totalCnt').val();
		$('#themaGiftSeq').val(dataSeq);
		$('#giftType').val(dataType);
		$('#addrUseYn').val(addrUseYn);
		$('#remainCnt').val(remainCnt);
		
		if(addrUseYn == "Y"){
			$('#addrInfo').show();
		}else{
			$('#addrInfo').hide();
		}
		var product_text = product_value+'<br/> 경품을  선택하셨습니다.';
		if(remainCnt == 0){
			$('#btn_confirm3').prop('disabled', true);
			$('#btn_confirm3').removeClass('del_check');
			$('#btn_confirm3').addClass('btn_cancel');
			if(totalCnt > 0){
				product_text = product_value+'<br/> 경품은 소진되었습니다. <br/>다른 경품을 선택하세요.';
			}else{
				//product_text = '웰컴패키지 이벤트 종료 및 시즈널 모집은 계속됩니다. <br/>이벤트 내용을 확인해주세요.';
				product_text = '많은 성원에 힘입어 웰컴 패키지 지급 이벤트가 종료되었습니다 <br/> 더 풍성한 시즈널 혜택 모집은 계속되오니, <br/>자세한 내용은 이벤트를 확인해 주세요!';
			}
			$(pop).find('#form_user_info').hide();
		}else{
			$('#btn_confirm3').prop('disabled', false);
			$('#btn_confirm3').removeClass('btn_cancel');
			$('#btn_confirm3').addClass('del_check');
			$(pop).find('#form_user_info').show();
		}
		$(pop).find('.text').show().html(product_text);
	}); 
	$(document).on("click", "#eventPop1 .btn_cancel", function(e){
		$("form").each(function() {  
			if(this.id == "form_user_info") this.reset();
	 	});
		$('form#form_product input[name="choice"]').prop('checked', false);
		$(pop).find('.event_title').attr('class','event_title');
		$(pop).find('.event_choice').attr('id','');
		$(pop).find('.text').hide();
		$(pop).find('#form_user_info').hide();
		e.preventDefault();
		$('.layerPop').hide();
	});
}