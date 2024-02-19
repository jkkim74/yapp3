$(function(){
	ContentH();
	friendList();
});

$(window).resize(function(){
	ContentH();
});

function ContentH(){
	var winH = $(window).height(),
			header = $('.header').outerHeight(),
			guide = $('.yteenGuide').outerHeight(),
			btm = $('.btm').outerHeight(),
			val1 = winH - header - guide - btm;
	console.log(header,guide,btm);
	$('body').css('padding-top', header+guide+'px');
	$('.yteenContent').css('height', val1+'px');
}

function friendList(){
	var top = $('.yteenContent .topArea'),
			circle = $('.yteenContent .circleArea'),
			btm = $('.yteenContent .btmArea'),
			btmWrap = $('.btm');
	
	$('.yteenContent .btnPlus').click(function(){
		circle.slideDown(150);
		circle.find('.user').fadeIn(600);
		btmWrap.fadeIn(150);
		top.slideUp(150);
		btm.slideUp(150);
	});
}

$(document).ready(function(){  

	$(window).on('scroll', function() {

		var thisScroll = $(this).scrollTop();

		if (thisScroll > 1) {
			$('.listTop_info').slideDown(200);
			//$('.info_title').slideUp(200);
			//$('.content').css('padding-top', 100);
		}
		else {
			$('.listTop_info').slideUp(200);
			//$('.info_title').slideDown(200);
			//$('.content').css('padding-top', 20);
		}
		/*if (thisScroll > 1) {
			// $('.info_title').css('display', 'none');
			// $('.listTop_info').css('display', 'block');
			$('.info_title').slideUp(200);
			$('.listTop_info').slideDown(200);
			$('.content').css('padding-top', 100);
		}
	
		if (thisScroll < 1) {
			$('.info_title').slideDown(200);
			$('.listTop_info').slideUp(200);
			$('.content').css('padding-top', 20);
		}*/
	});

	listTop_info();
	owner_member();
});


function listTop_info(){
	var listTop_info = $('.container .listTop_info');
	var listTop_info_p = $('.container .listTop_info p');
	var listTop_infoBtn = $('.container .listTop_info .btn_info');

	$('.listTop_info .btn_info').on('click', function() { 

		if ($(listTop_info).hasClass('open')) {
			$(listTop_info).removeClass('open'); //공지열림
			$(listTop_info).addClass('close'); //공지닫침
			$(listTop_info_p).animate({height: '36px'},150, 'swing');
			$(listTop_infoBtn).removeClass('close'); //닫침
			$(listTop_infoBtn).addClass('open'); //열림
		} else {
			$(listTop_info).removeClass('close'); //공지열림
			$(listTop_info).addClass('open'); //공지닫침 
			$(listTop_info_p).animate({height: '85px'},150, 'swing');
			$(listTop_infoBtn).removeClass('open'); //열림
			$(listTop_infoBtn).addClass('close'); //닫침
		}
	//console.log (listTop_info_H)
	});

};

function owner_member(){
	var owner_memberBtn = $('.section.owner .popApp');

	$(owner_memberBtn).on('click', function() { 
		var owner_member = $(this).prev().find('.inner_member');
		var owner_icon_crown = $(this).prev().find('.icon_crown');
	
		if ($(this).hasClass('open')) {
			$(owner_member).slideDown(150); //리스트열림
			$(owner_icon_crown).hide(); //방장아이콘숨김
			$(this).removeClass('open'); //닫침
			$(this).addClass('close'); //열림
		} else {
			$(owner_member).slideUp(150); //리스트닫침
			$(owner_icon_crown).show(); //방장아이콘보임
			$(this).removeClass('close'); //열림
			$(this).addClass('open'); //닫침
		}
	
		//console.log (listTop_info_H)
	});
};

//확인
function onBtn_conFirm(target){
	onAllClose(target);
}

//확인2
function onBtn_conFirm5(target){
	$(target).attr('class', 'layerPop alert');

	$('#mask').hide();
	$('.layerPop').hide();
	$('body').removeClass('popActive');
	$(target).hide();
}
//방개설
function onBtn_conFirm1(target){
		var layerType = $('[name=layerType]').val();
		var layerValue = $('[name=layerValue]').val();
		var themaSeq = $('[name=themaSeq]').val();
		var emSeq = $('[name=emSeq]').val();
		var rmSeq = $('[name=rmSeq]').val();
		

		$.ajax({
			type : "POST",
			dataType : "json",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "/yapp/yFriends/room",
			data : {
				themaSeq : themaSeq,
				emSeq : emSeq
			}, // 전송할 데이터
			error : function(){
				onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', '시스템에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.');
			},
			success : function(data){
				console.log("통신데이터 값 : " + data.resultCd);
				if(data.resultCd == "200") {
					reLoad(emSeq);
					//location.reload();
				} else {
					onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', data.resultMsg);
				}
			}
		});

		onAllClose(target);
}

//모집완료 신청
function onBtn_conFirm2(target){
		var layerType = $('[name=layerType]').val();
		var layerValue = $('[name=layerValue]').val();
		var themaSeq = $('[name=themaSeq]').val();
		var emSeq = $('[name=emSeq]').val();
		var rmSeq = $('[name=rmSeq]').val();

		$.ajax({
			type : "POST",
			dataType : "json",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "/yapp/yFriends/finish",
			data : {
				emSeq : emSeq,
				rmSeq : rmSeq
			}, // 전송할 데이터
			error : function(){
				onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', '시스템에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.');
			},
			success : function(data){
				console.log("통신데이터 값 : " + data.resultCd);
				if(data.resultCd == "200") {
					reLoad(emSeq);
					//location.reload();
				} else {
					onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', data.resultMsg);
				}
			}
		});

		onAllClose(target);
}

//경품신청
function onBtn_conFirm3(target){
		var layerType = $('[name=layerType]').val();
		var layerValue = $('[name=layerValue]').val();
		var themaSeq = $('[name=themaSeq]').val();
		var emSeq = $('[name=emSeq]').val();
		var rmSeq = $('[name=rmSeq]').val();
		var addrUseYn = $('[name=addrUseYn]').val();
		
		var themaGiftSeq = $('[name=themaGiftSeq]').val();
		var giftType = $('[name=giftType]').val();
		var recvName = $('[name=recvName]').val();
		var recvMobileNo = $('[name=recvMobileNo]').val();
		var post = $('[name=post]').val();
		var addr = $('[name=addr]').val();
		var addrDtl = $('[name=addrDtl]').val();

		if(themaGiftSeq == "" || themaGiftSeq == null){
			alert("경품을 선택해주세요.");
			//onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop3', "경품을 선택해주세요.");
			return false;
		}

		if(addrUseYn == "Y"){
			if(post == "" || post == null){
				alert("우편번호를 입력해주세요.");
				//onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop3', "우편번호를 입력해주세요.");
				return false;
			}

			if(addr == "" || addr == null){
				alert("주소를 입력해주세요.");
				//onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop3', "주소를 입력해주세요.");
				return false;
			}

			if(addrDtl == "" || addrDtl == null){
				alert("주소 상세를 입력해주세요.");
				//onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop3', "주소 상세를 입력해주세요.");
				return false;
			}
		}

		$.ajax({
			type : "POST",
			dataType : "json",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "/yapp/yFriends/roomGift",
			data : {
				themaSeq : themaSeq,
				emSeq : emSeq,
				rmSeq : rmSeq,
				themaGiftSeq : themaGiftSeq,
				giftType : giftType,
				recvName : recvName,
				recvMobileNo : recvMobileNo,
				addr : addr,
				addrDtl : addrDtl
			}, // 전송할 데이터
			error : function(){
				onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', '시스템에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.');
			},
			success : function(data){
				console.log("통신데이터 값 : " + data.resultCd);
				if(data.resultCd == "200") {
					reLoad(emSeq);
					//location.reload();
				} else {
					onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', data.resultMsg);
				}
			}
		});

		onAllClose(target);
}

//방탈퇴
function onBtn_conFirm4(target){
		var layerType = $('[name=layerType]').val();
		var layerValue = $('[name=layerValue]').val();
		var themaSeq = $('[name=themaSeq]').val();
		var emSeq = $('[name=emSeq]').val();
		var rmSeq = $('[name=rmSeq]').val();
		
		var themaGiftSeq = $('[name=themaGiftSeq]').val();
		var recvName = $('[name=recvName]').val();
		var recvMobileNo = $('[name=recvMobileNo]').val();
		var addr = $('[name=addr]').val();
		var addrDtl = $('[name=addrDtl]').val();
		
		$.ajax({
			type : "POST",
			dataType : "json",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "/yapp/yFriends/exit",
			data : {
				rmSeq : rmSeq,
				emSeq : emSeq
			}, // 전송할 데이터
			error : function(){
				onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', '시스템에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.');
			},
			success : function(data){
				console.log("통신데이터 값 : " + data.resultCd);
				if(data.resultCd == "200") {
					reLoad(emSeq);
					//location.reload();
				} else {
					onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', data.resultMsg);
				}
			}
		});

		onAllClose(target);
}

// yFriends 오류처리
function onBtn_conFirm6(target){
	var postData = { gb : '3' };
	
	if (isIOS()){
		window.webkit.messageHandlers.callBackHandler.postMessage(postData);
	}else{
		window.YDataBox.getLoginView();
	}
	onAllClose(target);
}

//친구추가
function owner_member_add(emSeq, rmSeq, themaKor){

	var postData = {
			gb : '1',
			rmSeq : rmSeq,
			emSeq : emSeq,
			themaKor : themaKor
	};

	if (isIOS()){
		window.webkit.messageHandlers.callBackHandler.postMessage(postData);
	}else{7
		window.YDataBox.postInviteFriend(rmSeq, emSeq, themaKor);
	}
	onAllClose();
}

function reLoad(emSeq){
	var postData = {
			gb : '2',
			rmSeq : '',
			emSeq : emSeq,
			themaKor : ''
	};

	if (isIOS()){
		window.webkit.messageHandlers.callBackHandler.postMessage(postData);
	}else{
		window.YDataBox.getReload(emSeq);
	}
}

function onPopup(themaSeq, emSeq, rmSeq, thema, id, themaKor){
		$('[name=layerType]').val(id);
		$('[name=layerValue]').val(thema);
		$('[name=themaSeq]').val(themaSeq);
		$('[name=emSeq]').val(emSeq);
		$('[name=rmSeq]').val(rmSeq);

		var maskHeight = $(document).height();
		var maskWidth = $(document).width();

		$('#mask').css({'width':maskWidth,'height':maskHeight});

		$('#mask').fadeIn(200);
		$('#mask').fadeTo("fast", 0.5);

		var winH = $(window).height();
		var winW = $(window).width();
		var scrollTop = $(window).scrollTop();
		$(id+"Param").html(themaKor);
		console.log(winH);
		console.log(winW);
		console.log(winH/2-($(id).height()+135)/2);
		console.log(winW/2-$(id).width()/2);
		$(id).css('top', winH/2-($(id).height()+135)/2);
		$(id).css('left', winW/2-$(id).width()/2);

		//console.log(($(id).height()+135)/2)
		console.log(id);
		$(".layerPop").hide();
		$('body').addClass('popActive');
		$(id).fadeIn(100); //페이드인 속도..숫자가 작으면 작을수록 빨라집니다.
		if(id =='#layerPop1'){
			$(id).addClass(thema);
		}
}

function onPopup2(joinSeq, themaSeq, emSeq, rmSeq, dataid, id){
		$('[name=layerType]').val(id);
		$('[name=layerValue]').val(dataid);
		$('[name=themaSeq]').val(themaSeq);
		$('[name=emSeq]').val(emSeq);
		$('[name=rmSeq]').val(rmSeq);

		$.ajax({
			type : "GET",
			dataType : "json",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "/yapp/yFriends/themaGift",
			data : {
				themaSeq : themaSeq,
				joinSeq : joinSeq,
				emSeq : emSeq
			}, // 전송할 데이터
			error : function(){
				onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', '시스템에 에러가 발생했습니다. 잠시 후 다시 시도해주세요.');
			},
			success : function(data){
				console.log("통신데이터 값 : " + data.resultCd);
				
				if(data.resultCd == "200") {
					var dataListHtml = "";
					var totalCnt = 0;
					for(var i=0; i<data.resultInfoList.length; i++) {
						var remainCnthtml = "";

						if(data.resultInfoList[i].remainCnt == 0){
							remainCnthtml = '<span style="color:#bdbdbd;">'+data.resultInfoList[i].giftName +'<br> <br><font style="color:red;">잔여량 : 소진</font></span>';
						}else{
							var numberFormat = String(data.resultInfoList[i].remainCnt).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
							//remainCnthtml = '<span>'+data.resultInfoList[i].giftName +'<br> <br>잔여량 : <fmt:formatNumber value="'+data.resultInfoList[i].remainCnt+'" pattern="#,###"/></span>';
							remainCnthtml = '<span>'+data.resultInfoList[i].giftName +'<br> <br>잔여량 : '+numberFormat+'</span>';
							totalCnt++;
						}
						
						dataListHtml +='<label class="chk">'
							+'<input type="radio" name="choice" value="'+data.resultInfoList[i].giftName+'" data-seq="'+data.resultInfoList[i].themaGiftSeq+'" data-type="'+data.resultInfoList[i].giftType+'" data-use="'+data.resultInfoList[i].addrUseYn+'" data-cnt="'+data.resultInfoList[i].remainCnt+'"/>'
							+'<i><img src="'+data.resultInfoList[i].imgUrl1+'" width="80%"></i>'
							//+'<span>'+data.resultInfoList[i].giftName + remainCnthtml
							+remainCnthtml+'</label>';
					}

					$('#totalCnt').val(totalCnt);
					$('#form_product').html(dataListHtml);

					$('#recvName').val(data.resultData.recvName);
					$('#recvMobileNo').val(data.resultData.recvMobileNo);
					
					$(id).css('top', 0);
					$(id).css('left', 0);
					$(id).fadeIn(100); //페이드인 속도..숫자가 작으면 작을수록 빨라집니다.
					$('body').addClass('popActive');

					if(totalCnt < 1){
						//많은 성원에 힘입어 웰컴 패키지 지급 이벤트가 종료되었습니다. 더 풍성한 시즈널 혜택 모집은 계속되오니, 자세한 내용은 이벤트를 확인해 주세요!
						//웰컴패키지 이벤트 종료 및 시즈널 모집은 계속됩니다. <br/>이벤트 내용을 확인해주세요.
						$('#eventTxt').show().html('많은 성원에 힘입어 웰컴 패키지 지급 이벤트가 종료되었습니다 <br/> 더 풍성한 시즈널 혜택 모집은 계속되오니, <br/>자세한 내용은 이벤트를 확인해 주세요!');
						$('#btn_confirm3').prop('disabled', true);
					}

					$(id).find('.event_title').addClass('group-'+dataid); // title 변경
					$(id).find('.event_choice').attr('id',dataid); // radio 변경
				} else {
					onPopup(themaSeq, emSeq, rmSeq, '', '#errorPop1', data.resultMsg);
				}
			}
		});
}

function onHelp(id){
	var maskHeight = $(document).height();
	var maskWidth = $(document).width();

	$('#mask').css({'width':maskWidth,'height':maskHeight});
	$('#mask').fadeIn(200);
	$('#mask').fadeTo("fast", 0.5);

	$(id).fadeIn(100); //페이드인 속도..숫자가 작으면 작을수록 빨라집니다.
}


function onNotice(id){
	var maskHeight = $(document).height();
	var maskWidth = $(document).width();

	$('#mask').css({'width':maskWidth,'height':maskHeight});

	$('#mask').fadeIn(200);
	$('#mask').fadeTo("fast", 0.5);

	var winH = $(window).height();
	var winW = $(window).width();
	var scrollTop = $(window).scrollTop();
	console.log(winH);
	console.log(winW);
	console.log(winH/2-($(id).height()+135)/2);
	console.log(winW/2-$(id).width()/2);
	$(id).css('top', winH/2-($(id).height()+135)/2);
	$(id).css('left', winW/2-$(id).width()/2);

	console.log(id);
	$(id).fadeIn(100); //페이드인 속도..숫자가 작으면 작을수록 빨라집니다.
}

function isIOS(){
	var userAgent = navigator.userAgent.toLowerCase();
	return ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1) || (userAgent.search("ipad") > -1));
}

function onAllClose(target){
	
	$('[name=layerType]').val('');
	$('[name=layerValue]').val('');
	$('[name=themaSeq]').val('');
	$('[name=emSeq]').val('');
	$('[name=rmSeq]').val('');

	if(target == '#eventPop1'){
		$(target).attr('class', 'layerPop event');
	}else if(target == '#eventPop2'){
		$(target).attr('class', 'layerPop info');
	}else{
		$(target).attr('class', 'layerPop alert');
	}
	$('#mask').hide();
	$('.layerPop').hide();
	$('body').removeClass('popActive');
	if(target=='#errorPop2' || target=='#errorPop4'){
		window.open("about:blank","_self").close();		
	}else if(target == '#eventPop1'){
		$('[name=themaGiftSeq]').val('');
		$('[name=giftType]').val('');
		$('[name=addrUseYn]').val('');
		$('[name=recvName]').val('');
		$('[name=recvMobileNo]').val('');
		$('[name=post]').val('');
		$('[name=addr]').val('');
		$('[name=addrDtl]').val('');
	}
}