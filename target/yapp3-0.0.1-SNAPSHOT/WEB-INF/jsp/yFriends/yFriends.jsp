<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0">
		<title>Y BOX</title>
		<script type="text/javascript" src="/yapp3/res/ext/jquery-3.5.1/jquery-3.5.1.js"></script>
		<script type="text/javascript" src="/yapp3/res/ext/script.js"></script>
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/reset.css">
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/font-ios.css">
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/font-android.css">
		<!-- ios, android를 붙혀 구분 -->
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/layout.css?20230128">
		<!-- loadingbar css -->
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/loadingbar.css">
		
		<script type="text/javascript" src="/yapp3/res/js/common.js?20230417"></script> <!-- 210615 추가 -->
		
		<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
		
		<style>
			a {-webkit-tap-highlight-color:rgba(0,0,0,0); }
			.content{padding-top:4%; padding-bottom:14%;}
			.popup{display: none;}
			#eventEnd{display: none;}
			/*경품선택 css  */
			.pop-con p + .radio-list-row{margin-top:6%}
			.radio-list-row{border-bottom:none; padding-bottom:0;}
			.radio-list-row img{width: 154px;; height: 154px;}
			h2.tit{font-size:1.5rem}
		</style>
		
		<script type="text/javascript">
			var chkMsg = '${fn:escapeXml(chkMsg)}';
			var chkSubMsg = '${fn:escapeXml(chkSubMsg)}';
			var endYn = '${fn:escapeXml(endYn)}';
			var evtSeq = '${fn:escapeXml(evtSeq)}';
			//경품 소진 여부
			var existGiftYn = '${fn:escapeXml(existGiftYn)}';
			var remainGiftYn = '${fn:escapeXml(remainGiftYn)}';
			var eventJoinYn = '${fn:escapeXml(eventJoinYn)}';
			var maxRoomCnt = '${fn:escapeXml(yfriendsMenuInfo.maxRoomCnt)}';
			var loadTimer;
			var currentIndex = 37;
			
			//스크립트 작성
			$(document).ready(function(){		
				
				console.log($("input:radio[name=giveaway]").eq(0));
				//첫번째 라디오 버튼에 체크
				$("input:radio[name=giveaway]").eq(0).attr("checked",true).parents('li').addClass('on');
				//온,오프라인 상품 첫번째 라디오 체크된 텍스트값
				$('#data-resualt').text($("input:radio[name=giveaway]").eq(0).siblings('label').text());
				//데이터 상품 첫번째 라디오 체크된 텍스트값
				$('#datacp-resualt').text($("input:radio[name=datacpaway]").eq(0).siblings('label').text());
				
				// 라디오 선택시 선택 라디오 해당 img opacity 조정 + 선택된 라디오로 텍스트 변경
				$('.radio-list-row input').click(function(){
					$(this).parents('li').addClass('on');
					$(this).parents('li').siblings().removeClass('on');
					$('#data-resualt').text($(this).siblings('label').text());
				});
				
				//이벤트 종료시 전체 dim처리
				var invitedFnshYn = $('[name=invitedFnshYn]').val();
				
				if(invitedFnshYn =="Y"){
					var joinCnt = '${roomInfoList[0].joinCnt}';
					
					//인원수
					if(joinCnt=="2"){
						$("#ulTag").attr("class","ybox-cir-userli list2");
					}
					if(joinCnt=="3"){
						$("#ulTag").attr("class","ybox-cir-userli list3");
					}
					if(joinCnt=="4"){
						$("#ulTag").attr("class","ybox-cir-userli list4");
					}
					if(joinCnt=="5"){
						$("#ulTag").attr("class","ybox-cir-userli list5");
					}
				}else{
					//인원수
					if(maxRoomCnt=="2"){
						$("#ulTag").attr("class","ybox-cir-userli list2");
					}
					if(maxRoomCnt=="3"){
						$("#ulTag").attr("class","ybox-cir-userli list3");
					}
					if(maxRoomCnt=="4"){
						$("#ulTag").attr("class","ybox-cir-userli list4");
					}
					if(maxRoomCnt=="5"){
						$("#ulTag").attr("class","ybox-cir-userli list5");
					}
				}
				
				var giftRcvYn = $('[name=giftRcvYn]').val();
				if(endYn == "Y"){
					if(invitedFnshYn =="Y"){
						
						if(giftRcvYn =="Y"){
							
							if(eventJoinYn == "Y"){
								scrollDisble();
								$("#eventEnd").show();
							}else{
								scrollAble();
								$("#eventEnd").hide();
							}
						}else{
								scrollDisble();
								$("#eventEnd").show();
						}
					}else{
						scrollDisble();
						$("#eventEnd").show();
					}
				}else{
					var giftFalg =$('[name=giftFalg]').val();
					if(giftFalg == "Y"){	//상품이 없을경우
						
						if(eventJoinYn == "N"){
							if(invitedFnshYn =="Y"){
								if(existGiftYn == "N"){
									scrollDisble();
									$("#eventEnd").show();
								}
							}else{
								if(remainGiftYn == "N"){	//remainGiftYn N이면 dim
									scrollDisble();
									$("#eventEnd").show();
								}
							}
						}
					}
				}
				
				//에러 메세지
				if(chkMsg != ''){
					$("#errorContent").text(chkMsg);
					if(chkSubMsg == '') {
						$("#errorContent2").text(chkSubMsg);
					}
					scrollDisble();
					$("#errorPop").show();
				}
				
				//전화번호 입력시 숫자만 입력
				$("#recvMobileNo").bind("keyup",function(event){
					var regNumber = /^[0-9]*$/;
					var temp = $("#recvMobileNo").val();
					if(!regNumber.test(temp)){
						$("#recvMobileNo").val(temp.replace(/[^0-9]/g,""));
					}
				});
				
				//에러 메세지 팝업 닫으면 close
				$("#errorPopCancel").click(function(){
					eventClose();
				});
				$("#errorPopCover").click(function(){
				});
			
				//Ybox 방만들기 버튼 클릭시 팝업창 띄우기
				$("#yBoxRoomBtn").click(function(){
					scrollDisble();
					$("#yBoxRoomPop").show();
				});
				//Ybox 방만들기 버튼 클릭시 팝업창 감추기
				$('#yBoxRoomPopCancel').click(function(){
					scrollAble();
					$("#yBoxRoomPop").hide();
				});
				//Ybox 방 삭제 팝업창 띄우기
				$("#roomOut").click(function(){
					scrollDisble();
					$("#roomDelPop").show();
				});
				//Ybox 방 삭제 팝업창 취소
				$("#roomDelPopCancel").click(function(){
					scrollAble();
					$("#roomDelPop").hide();
				});
				//에러팝업닫기
				$("#errorOk").click(function(){
					var emSeq =$('[name=emSeq]').val();
					reLoad(emSeq);
					//$("#roomErrorPopup").hide();
				});
				//에러팝업닫기
				$("#errorCover").click(function(){
				});
				//내보내기팝업닫기
				$("#banPopCancel").click(function(){
					scrollAble();
					$("#banPop").hide();
				});
				//경품신청 팝업
				$("#yBoxRoomGiftBtn").click(function(){
					scrollDisble();
					$("#giftChoicePopup").show();
				});
				//경품신청 취소버튼 클릭시 팝업닫기
				$("#giftChoiceCancel").click(function(){
					scrollAble();
					$(this).parents('.popup').hide();
				});
				//배송지팝업 취소버튼 클릭시 팝업닫기
				$("#deliveryCancel").click(function(){
					var element_wrap = document.getElementById("wrapPost");
					element_wrap.style.display = 'none';
					$("#recvName").val("");
					$("#recvMobileNo").val("");
					$("#post").val("");
					$("#addr").val("");
					$("#addrDtl").val("");
					scrollAble();
					$('#giftChoiceCancel').parents('.popup').hide();
					$(this).parents('.popup').hide();
				});
				//배송지팝업 커버 클릭시 팝업닫기
				$("#deliveryCover").click(function(){
				});
				//경품선택완료 팝업창(오프라인) 닫은 후 상세페이지 종료
				$("#giftChoice_complete_ok").click(function(){
					eventClose();
				});
				//경품선택완료 팝업창(오프라인) 커버 클릭시 상세페이지 종료
				$("#giftChoice_complete_cover").click(function(){
				});
				//선착순 경품증정 팝업창(온라인) 닫은 후 상세페이지 종료
				$("#first_gift_cancel").click(function(){
					eventClose();
				});
				//선착순 경품증정 팝업창(온라인) 커버 클릭시 상세페이지 종료
				$("#first_gift_cover").click(function(){
				});
				//랜덤제공 완료 팝업창 닫은 후 상세페이지 종료
				$("#random_complete_ok").click(function(){
					eventClose();
				});
				//랜덤제공 완료 팝업창 커버 클릭시 상세페이지 종료
				$("#random_complete_cover").click(function(){
				});
				//경품 없는 경우 팝업창 닫기
				$("#not_gift_ok").click(function(){
					scrollAble();
					$('#giftChoice_popup_cancel').parents('.popup').hide();
					$(this).parents('.popup').hide();
				});
				//경품 없는 경우 팝업창 닫기
				$("#not_gift_cover").click(function(){
				});
				//정보 미입력 팝업창 닫기
				$("#not_info_ok").click(function(){
					scrollAble();
					$(this).parents('.popup').hide();
				});
				//모집완료팝업에서 취소버튼 클릭시
				$("#finishPopCancel").click(function(){
					var emSeq =$('[name=emSeq]').val();
					reLoad(emSeq);
				});
				//모집완료팝업에서 커버 클릭시
				$("#finishPopCover").click(function(){
				});
				//경품안내 팝업 ON
				$("#finishPopOk").click(function(){
					var emSeq =$('[name=emSeq]').val();
					var rmSeq =$('[name=rmSeq]').val();
					var issueSeq =$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=issueSeq]').val();
					console.log("evtSeq : "+evtSeq);
					console.log("rmSeq : "+rmSeq);
					console.log("issueSeq : "+issueSeq);
					
					 $.ajax({
						type : "post",
						dataType : "json",
						headers : {
							ysid : $('[name=ysid]').val(),
							autoLogin : $('[name=autoLogin]').val(),
							osTp : $('[name=osTp]').val(),
							appVrsn : $('[name=appVrsn]').val()
						},
						url : "finish",
						data : {"evtSeq":evtSeq
							   ,"rmSeq":rmSeq},
						success : function(data){
							if(data.resultCd == "200") {
								scrollDisble();
								$("#giftInfoPop").show();
							} else {
								$("#errorMsg").text(data.resultMsg);
								scrollDisble();
								$("#roomErrorPopup").show();
							}
						},
						error : function(error){
							console.log(error);
						}
					}); //ajax End  
				});
				//경품안내 팝업 확인버튼 클릭시
				$("#giftInfoPopOk").click(function(){
					var emSeq =$('[name=emSeq]').val();
					reLoad(emSeq);
				});
				//경품안내 팝업 커버 클릭시
				$("#giftInfoPopCover").click(function(){
				});
			
				//리워드함으로 이동
				$("#reward_move").click(function(){
					title ="리워드";
					console.log(evtSeq);
					var postData = {
							gm : '3',
							url : "/reward/reward",
							evtSeq : evtSeq,
							title : title
						};
						
						if(isIOS()){
							//IOS
							window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
						} else {
							//Android
							url = "/reward/reward";
							
							window.YDataBox.moveWebViewPage(url, evtSeq, title);
							window.close();
						}
					
				});
				
				
				//경품신청 (Y프렌즈인 정회원일경우 데이터 쿠폰 처리 추가)
				$("#giftChoiceComplete").click(function(){
					var giftChoiceType= '${fn:escapeXml(eventDetail.giftChoiceType)}';	//경품 제공 방식 (선택,랜덤)
					var giftOfferType= '${fn:escapeXml(eventDetail.giftOfferType)}';//O0002선착순, O0003추첨, O0001상품없음
					var url = '';
					if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
						var giftType=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftType]').val();
						
						if(giftType=="G0001"){	//오프라인상품 : 배송지 입력팝업
							
							//211029
							var remarkNm="${fn:escapeXml(eventDetail.giftFormNm)}";
							if(!isEmpty(remarkNm)){
								$("#remarks").attr("placeholder", remarkNm);
							}
							//211029 end
							url = '/yapp3/event/giftOfflineJoin';
							scrollDisble();
							$("#deliveryPopup").show();
							return false;
						}
						if(giftType=="G0002"){	//온라인상품 : 배송지 입력 필요x
							var giftSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftSeq]').val();
							var issueSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=issueSeq]').val();
							
							var eventGiftJoin={};
						
								eventGiftJoin={"evtSeq":evtSeq
											  ,"giftSeq":giftSeq
											  ,"issueSeq":issueSeq
											  ,"giftType":giftType
											  };
								
								url = '/yapp3/event/giftOnlineJoin';
						}
						if(giftType=="G0004"){	//데이터상품 : 배송지 입력 필요x
							var giftSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftSeq]').val();
							var issueSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=issueSeq]').val();
							
							var eventGiftJoin={};
						
								eventGiftJoin={"evtSeq":evtSeq
											  ,"giftSeq":giftSeq
											  ,"issueSeq":issueSeq
											  ,"giftType":giftType
											  };
								
								url = '/yapp3/event/giftDataCpJoin';
						}
					}//경품 제공 방식 : 선택 end
					
					if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
						var flag=false;
						var length=$('input:hidden[name=giftType]').length;
							for(var i=0;i<length;i++){
								var giftType=$('input:hidden[name=giftType]').eq(i).val();
								if(giftType=="G0002"){//온라인
									flag=true;
								}
								if(giftType=="G0001"){//오프라인 상품이면 배송지 정보입력
									flag=false;
									scrollDisble();
									$("#deliveryPopup").show();
								return flag;
								}
								
							}//for end
						if(flag==true){
							var eventGiftJoin={"evtSeq":evtSeq
											  ,"issueSeq": -1
											  ,"giftType":giftType};
						}	
						
					}// 경품 제공 방식 : 랜덤 end
					console.log('url?');
					console.log(url);
					//경품이벤트(온라인) ajax
					  	 $.ajax({
							type : "post",
							headers : {
								cntrNo : $('[name=cntrNo]').val(),
								ysid : $('[name=ysid]').val(),
								autoLogin : $('[name=autoLogin]').val(),
								osTp : $('[name=osTp]').val(),
								appVrsn : $('[name=appVrsn]').val()
							},
							url : url,
							data : eventGiftJoin,
							success : function(data){
								if(data.resultCd=="200"){
									
									if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
										//추첨 경품일시
										if(giftOfferType=="O0003"){
											scrollDisble();
												$("#giftChoice_complete_popup").show();
										}
										//선착순 경품일시
										if(giftOfferType=="O0002"){
											
											if(giftType=="G0002" || giftType=="G0004"){	//온라인상품, 데이터 상품 
												
												$("#gift_url").attr("src",data.resultData.imgUrl);
												$("#gift_name").text(data.resultData.giftName);
												scrollDisble();
												$("#first_gift_popup").show();
											}
										}
										
									}
									
									if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
											scrollDisble();
											$("#random_complete_popup").show();
									}
									
								}else{
									console.log(data);
									$("#error_msg").text(data.resultMsg);	//에러 메세지
									scrollDisble();
									$("#not_gift_url").attr("src",data.resultData.imgUrl);
									$("#not_gift_name").text(data.resultData.giftName);
									$("#not_gift_popup").show();
								}
								
							},
							error : function(error){
								console.log(error);
							}
						}); //ajax End  
					
				});	//경품신청 End
				
				//오프라인 경품신청 ajax
				$("#deliveryComplete").click(function(){
					var multiGiftRewardChk = '${fn:escapeXml(multiGiftRewardChk)}';
					var apiUrl = '/yapp3/event/giftOfflineJoin';
					
					if(multiGiftRewardChk == 'Y'){
						apiUrl = '/yapp3/event/giftMultiRewardJoin';
					}
					
					var giftType=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftType]').val();
					var giftChoiceType = '${fn:escapeXml(eventDetail.giftChoiceType)}';	//경품 제공 방식 (선택,랜덤)C0001:선택,  C0002:랜덤
					var giftOfferType= '${fn:escapeXml(eventDetail.giftOfferType)}';//O0003선착순, O0002추첨, O0001상품없음
					
					var recvName=$("#recvName").val();			//사용자 이름
					var recvMobileNo=$("#recvMobileNo").val();	//사용자 전화번호
					var post=$("#post").val();					//우편번호
					var addr=$("#addr").val();;					//주소
					var addrDtl=$("#addrDtl").val();;			//상세 주소
					var remarks=$("#remarks").val();;			//비고

					if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
						var giftSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftSeq]').val();
						var issueSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=issueSeq]').val();
						var eventGiftJoin={"evtSeq":evtSeq
										  ,"giftSeq":giftSeq
								 	      ,"issueSeq":issueSeq
								  		  ,"recvName":recvName
								  		  ,"recvMobileNo":recvMobileNo
								  	 	  ,"post":post
								  	 	  ,"addr":addr
								  		  ,"addrDtl":addrDtl
								  		  ,"remarks":remarks
								  		  ,"giftType":giftType};
						if(recvName.trim()=="" || recvMobileNo.trim()=="" || post.trim()=="" || addr.trim()=="" || addrDtl.trim()==""){
							scrollDisble();
							$("#not_info_popup").show();
							return false;
						}
					}// 경품 제공 방식 : 선택 end
					
					if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
						var eventGiftJoin={"evtSeq":evtSeq
										  ,"issueSeq": -1				
						  		  		  ,"recvName":recvName
						  		  		  ,"recvMobileNo":recvMobileNo
						  	 	  		  ,"post":post
						  	 	  		  ,"addr":addr
						  		  		  ,"addrDtl":addrDtl
						  		  		  ,"remarks":remarks
						  		  		  ,"giftType":giftType};
					}//경품 제공 방식 : 랜덤
					
					$.ajax({
						type : "post",
						headers : {
							cntrNo : $('[name=cntrNo]').val(),
							ysid : $('[name=ysid]').val(),
							autoLogin : $('[name=autoLogin]').val(),
							osTp : $('[name=osTp]').val(),
							appVrsn : $('[name=appVrsn]').val()
						},
						url : apiUrl,
						data : eventGiftJoin,
						success : function(data){
							if(data.resultCd=="200"){
								if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
									scrollDisble();
									$("#giftChoice_complete_popup").show();
								}
								
								if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
									scrollDisble();
									$("#random_complete_popup").show();
								}
							}//200 end
							else{
								$("#error_msg").text(data.resultMsg);	//에러 메세지
								scrollDisble();
								$("#not_gift_url").attr("src",data.resultData.imgUrl);
								$("#not_gift_name").text(data.resultData.giftName);
								$("#not_gift_popup").show();
							}
						},
						error : function(error){
							console.log(error);
						}
					}); //ajax End 
					
					
				}); //오프라인 경품신청 ajax end
				
				//내보내기ajax
				$("#banPopOk").click(function(){
					var emSeq =$('[name=emSeq]').val();
					var rmSeq =$('[name=rmSeq]').val();
					var cntrNo =$('[name=joinCntrNo]').val();
					$.ajax({
					type : "post",
					headers : {
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "ban",
					data : {"rmSeq":rmSeq
						   ,"cntrNo":cntrNo},
					success : function(data){
						if(data.resultCd == "200") {
							reLoad(emSeq);
						} else {
							$("#errorMsg").text(data.resultMsg);
							scrollDisble();
							$("#roomErrorPopup").show();
						}
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End   
				});
				
				//모집완료
				$("#inviteFinishBtn").click(function(){
					scrollDisble();
					$("#finishPop").show();
				});
				
				//yBox 방만들기 ajax
				$('#yBoxRoomPopOk').click(function(){
					loadingStart(imgInterval);//로딩바 시작
					$("#yBoxRoomPopOk").attr("disabled",true);// 선택 버튼 비활성화 
					var evtSeq='${fn:escapeXml(evtSeq)}';
					var emSeq =$('[name=emSeq]').val();
					
					$.ajax({
						type : "post",
						dataType : "json",
						headers : {
							ysid : $('[name=ysid]').val(),
							autoLogin : $('[name=autoLogin]').val(),
							osTp : $('[name=osTp]').val(),
							appVrsn : $('[name=appVrsn]').val()
						},
						url : "room",
						data : {"evtSeq":evtSeq},
						success : function(data){
							if(data.resultCd == "200") {
								reLoad(emSeq);
								
							} else {
								$("#errorMsg").text(data.resultMsg);
								scrollDisble();
								$("#roomErrorPopup").show();
							}
							/* $("#yBoxRoomPopOk").attr("disabled",false);// 선택 버튼 활성화 
							
							loadingEnd();//로딩바 숨기기 */
						},
						error : function(error){
							console.log(error);
							$("#yBoxRoomPopOk").attr("disabled",false);// 선택 버튼 활성화 
							
							loadingEnd();//로딩바 숨기기
						}
					}); //ajax End 
				});
				
				//yBox 나가기 ajax
				$("#roomDelPopOk").click(function(){
						var rmSeq =$('[name=rmSeq]').val();
						var emSeq =$('[name=emSeq]').val();
						$.ajax({
							type : "post",
							headers : {
								ysid : $('[name=ysid]').val(),
								autoLogin : $('[name=autoLogin]').val(),
								osTp : $('[name=osTp]').val(),
								appVrsn : $('[name=appVrsn]').val()
							},
							url : "exit",
							data : {"rmSeq":rmSeq},
							success : function(data){
								if(data.resultCd == "200") {
									reLoad(emSeq);
								} else {
									$("#errorMsg").text(data.resultMsg);
									scrollDisble();
									$("#roomErrorPopup").show();
								}
							},
							error : function(error){
								console.log(error);
							}
						}); //ajax End 
				});
							
				$('.cover').click(function(){
					/* scrollAble();
					$(this).parents('.popup').hide(); */
				});

			});
			
			//로딩바 추가
			var imgInterval = function changeImage(){
				if(currentIndex < 72){
					currentIndex = currentIndex + 1; 
				}else{
					currentIndex = 37;
				}
				$('#loading-image').attr('src','/yapp3/res/images/loadingbar/kt_loading_000'+currentIndex+'.png');
			};
			
			//이벤트종료 dim처리시 스크롤 막기
			/* function scrollDisble(){
				$("#eventEnd").css("touch-action","none");
				$("#eventEnd").on("scroll touchmove mousewheel",function(e){
					e.preventDefault();
					e.stopPropagation();
					return false;
				});
			} */
			function scrollDisble(){
				$("body").addClass("overflow-hidden")
				//$(".overflow-hidden").css("touch-action","none");
				$(".overflow-hidden").on("scroll touchmove mousewheel",function(e){
					e.preventDefault();
					e.stopPropagation();
					return false;
				});
			}
			function scrollAble(){
				$("body").removeClass("overflow-hidden")
				//$(".overflow-hidden").css("touch-action","none");
				$(".overflow-hidden").off("scroll touchmove mousewheel");
			}
			
			//내보내기 팝업
			function ban(joinName, joinCntrNo){
				var masterCntrNo =$('[name=masterCntrNo]').val();
				var cntrNo =$('[name=cntrNo]').val();
				 if(masterCntrNo == cntrNo){
					var invitedFnshYn = $('[name=invitedFnshYn]').val();
					if(invitedFnshYn=="N"){
					$("#banUserNm").text(joinName);
					$("#banUserNmOk").text(joinName+"님을 내보내시겠습니까?");
					$("<input>").attr({type:"hidden", name:"joinCntrNo", value:joinCntrNo}).appendTo("#banPop");
					scrollDisble();
					$("#banPop").show();
					}
				} 
			}
			
			//친구추가
			function owner_member_add(evtSeq, rmSeq, themaKor){
				//모집완료시 친구추가 비활성화
				var invitedFnshYn = $('[name=invitedFnshYn]').val();
				if(invitedFnshYn=="Y"){
					return;
				}
				console.log("evtSeq : "+evtSeq);
				console.log("rmSeq : "+rmSeq);
				console.log("themaKor : "+themaKor);
				var postData = {
						gb : '1',
						rmSeq : rmSeq,
						evtSeq : evtSeq,
						themaKor : themaKor
				};
				if (isIOS()){
					window.webkit.messageHandlers.callBackHandler.postMessage(postData);
				}else{
					window.YDataBox.postInviteFriend(rmSeq, evtSeq, themaKor);
				}
				
			}
			
			//리로드
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
			
			//이벤트 상세페이지 이동
			function goEventDetail(){
				 var evtSeq='${fn:escapeXml(evtSeq)}';
				 var evtType=$('[name=evtType]').val();
				 var title=$('[name=evtTypeNm]').val();
				 /*
				 if(evtType == "G0001" || evtType == "G0002" ){
					title="이벤트";
				 }else if(evtType == "G0003"){
					title="매거진";
				 }else if(evtType == "G0004"){
					title="Y프렌즈";
				 }else if(evtType == "G0005"){
					title="출석체크";
				 }
				 */
				 var url = "/event/eventDetail";
				 var postData = {
						gm : '3',	
						url : "/event/eventDetail",
						evtSeq : evtSeq,
						title : title
					};
					if(isIOS()){
						//IOS
						window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
					} else {
						window.YDataBox.moveWebViewPage(url, evtSeq, title);
						window.close();
					}
			}
			
			
			
			//단말 분류
			function isIOS(){
				var userAgent = navigator.userAgent.toLowerCase();
				return ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1) || (userAgent.search("ipad") > -1));
			}
			
			function eventClose(){
				var postData = { gm : '4' };
				
				if (isIOS()){
					window.webkit.messageHandlers.callBackHandler.postMessage(postData);
				}else{
					window.YDataBox.closeWebViewPage();
				}	
			}
			
			function ownerMemberComplete(){
				scrollDisble();
				$("#finishPop").show();
			}
			
			
		</script>
	</head>
	<body>
		<div  id="loading-container" style="display:none;">
			<img id="loading-image"/>
		</div>
		<div class="content">
			<div class="titleWrap">
				<div class="tit-wrap clearfix">
					<h2 class="tit f-yspotlightotapp"><c:out value="${yfriendsMenuInfo.dtlTitle }" /></h2>
					<a href="javascript:void(0);" onclick="javascript:goEventDetail()" class="more-btn" style="text-decoration: none; color: black;">상세보기 
						<svg class="diagonal" style="width:20px; height:10px; border-bottom:1px solid #555555;">
							<line x1="13" y1="5" x2="20" y2="10" style="stroke:#707070; stroke-width:1"></line>
						</svg>
					</a>
				</div>
			</div>
			<div class="layout">
				<div class="y-infobox">
					<ul class="y-info-list">
						<li><span>가입 현황</span><c:choose><c:when test="${eventDetail.evtType eq 'G0004'}"><c:out value="${roomTitle.dtlTitle }" /></c:when><c:otherwise><c:out value="${yfriendsMenuInfo.menuName }" /></c:otherwise></c:choose></li>
						<li><span>혜택</span><c:choose><c:when test="${eventDetail.evtType eq 'G0004'}"><c:out value="${roomTitle.dtlRoomTitle }" /></c:when><c:otherwise><c:out value="${yfriendsMenuInfo.eventDetail }" /></c:otherwise></c:choose></li>
					</ul>
				</div>
				<div style="margin-top: 39%;">
					<div class="ybox-cir-wrap">
						<div class="ybox-cir">
							<img src="/yapp3/res/img/ic_y_yfriendbg@3x.png" alt="" >
							<p class="f-yspotlightotapp"><c:out value="${yfriendsMenuInfo.dtlRoomTitle }" /></p>
							<c:set var="img_a" value="/yapp3/res/img/ic_f_member_a@2x.png"/>
							<c:set var="img_b" value="/yapp3/res/img/ic_f_member_b@2x.png"/>
							<c:set var="img_c" value="/yapp3/res/img/ic_f_member_c@2x.png"/>
							<c:set var="img_d" value="/yapp3/res/img/ic_f_member_d@2x.png"/>
							<c:forEach var="item" items="${roomInfoList}" >
								<input type="hidden" name="emSeq" value='<c:out value="${item.emSeq }" />'/>
								<c:set var="emSeq" value="${item.emSeq }"/>
							</c:forEach>
								<ul id="ulTag" class="ybox-cir-userli">
									<c:if test="${empty roomJoinList}">
										<c:forEach begin="0" end="${yfriendsMenuInfo.maxRoomCnt - 1}" varStatus="status">
											<c:choose>
												<c:when test="${status.index eq '0' }">
													<li class="first-user on" style=""><div>
														<a href="javascript:void(0);">
															<img src="/yapp3/res/img/f-first-user@2x.png" alt="" width="75px;" height="75px;">
															<img src="/yapp3/res/img/f-first-user-owner@2x.png" alt="" id="ownerIcon" width="20px;" height="20px;">
														</a></div>
														<span>${partyName}</span></li>
												</c:when>
												<c:otherwise>
													<li><div><a href="javascript:void(0);"><img src="/yapp3/res/img/ic_y_empty@2x.png" width="75px;" height="75px;"/></a></div></li>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
									<c:if test="${!empty roomJoinList and roomInfoList[0].invitedFnshYn eq 'N'}">
										<c:forEach var="item" items="${roomJoinList}" varStatus="status" >
											<c:set var="rmSeq" value="${item.rmSeq}"/>
											<c:set var="joinFnshYn" value="${item.joinFnshYn}"/>
											<c:set var="giftRcvYn" value="${item.giftRcvYn}"/>
											<c:set var="invitedFnshYn" value="${item.invitedFnshYn}"/>
											<c:set var="currentIndex" value="${status.index}"/>
											<c:if test="${item.joinType eq 'JN001'}">
													<c:set var="masterCntrNo" value="${item.masterCntrNo}"/>
													<li class="first-user on">
														<div>
															<a href="javascript:void(0);">
																<img src="/yapp3/res/img/f-first-user@2x.png" alt="" width="75px;" height="75px;">
																<img src="/yapp3/res/img/f-first-user-owner@2x.png" alt="" id="ownerIcon" width="20px;" height="20px;">
															</a>
														</div>
														<span><c:out value="${item.masterName }" /></span>
													</li>
											</c:if>
											<c:if test="${item.joinType eq 'JN002'}">
												<c:if test="${status.index == 1 }">
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_a)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>
												</c:if>                                                                                                                                                      
												<c:if test="${status.index == 2 }">  
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_b)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                        
												</c:if>                                                                                                                                                     
												<c:if test="${status.index == 3 }">    
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_c)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                     
												</c:if>                                                                                                                                                     
												<c:if test="${status.index == 4 }">  
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_d)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                       
												</c:if>                                                                                                                                                                                  
											</c:if>
										</c:forEach>
										<c:forEach begin="${currentIndex }" end="${yfriendsMenuInfo.maxRoomCnt - 2}" varStatus="status">
													<li><div><a href="javascript:void(0);" onclick="javascript:owner_member_add('${evtSeq}', '${rmSeq}', '${yfriendsMenuInfo.menuName }')"><img src="/yapp3/res/img/ic_y_empty@2x.png" alt="" width="75px;" height="75px;"/></a></div></li>
										</c:forEach>
									</c:if>
									<c:if test="${!empty roomJoinList and roomInfoList[0].invitedFnshYn eq 'Y'}">
										<c:forEach var="item" items="${roomJoinList}" varStatus="status" >
											<c:set var="rmSeq" value="${item.rmSeq}"/>
											<c:set var="joinFnshYn" value="${item.joinFnshYn}"/>
											<c:set var="giftRcvYn" value="${item.giftRcvYn}"/>
											<c:set var="invitedFnshYn" value="${item.invitedFnshYn}"/>
											<c:set var="currentIndex" value="${status.index}"/>
											<c:if test="${item.joinType eq 'JN001'}">
													<c:set var="masterCntrNo" value="${item.masterCntrNo}"/>
													<li class="first-user on">
														<div>
															<a href="javascript:void(0);">
																<img src="/yapp3/res/img/f-first-user@2x.png" alt="" width="75px;" height="75px;">
																<img src="/yapp3/res/img/f-first-user-owner@2x.png" alt="" id="ownerIcon" width="20px;" height="20px;">
															</a>
														</div>
														<span><c:out value="${item.masterName }" /></span>
													</li>
											</c:if>
											<c:if test="${item.joinType eq 'JN002'}">
												<c:if test="${status.index == 1 }">
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_a)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>
												</c:if>                                                                                                                                                      
												<c:if test="${status.index == 2 }">  
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_b)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                        
												</c:if>                                                                                                                                                     
												<c:if test="${status.index == 3 }">    
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_c)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                     
												</c:if>                                                                                                                                                     
												<c:if test="${status.index == 4 }">  
													<li class="on"><div><a href="javascript:void(0);" onclick="javascript:ban('<c:out value="${item.joinName }" />','<c:out value="${item.joinCntrNo }" />')"><img src='${fn:escapeXml(img_d)}' alt="" width="75px;" height="75px;"></a></div><span><c:out value="${item.joinName }" /></span></li>                                                                                                                       
												</c:if>                                                                                                                                                                                  
											</c:if>
										</c:forEach>
									</c:if>
								</ul>
								<!-- 모집완료 전이면 완료하기 버튼 표시 -->
								<c:if test="${!empty enableFnshYn and enableFnshYn eq 'Y' and invitedFnshYn eq 'N' and masterCntrNo eq cntrNo}">
									<div class="f-completebtn" align="center">
											<a href="javascript:void(0);" onclick="javascript:ownerMemberComplete()">
												<span><img alt="" src="/yapp3/res/img/ic_f_complete@2x.png" width="14px" height="14px"> 모집 완료하기</span>
											</a>
									</div>
								</c:if>
								<c:if test="${invitedFnshYn eq 'Y'}">
									<div class="f-inviteComplete">
										<img alt="" src="/yapp3/res/img/f-inviteComplate@2x.png" width="100%" height="90%">
										<div class="f-completeText">모집완료</div>
									</div>
								</c:if>
						</div>
					</div>
				</div>
				
				<div class="btn-wrap bg-darkgray">
					<c:if test="${empty roomJoinList}">
						<button type="button" id="yBoxRoomBtn" class="btn w100p" onfocus="blur()">Y프렌즈 BOX 만들기</button>
					</c:if>
					<c:if test="${!empty roomJoinList}">
						<c:if test="${invitedFnshYn eq 'N'}">
						<!-- 모집완료가 아직 안된 상태 -->
							<button type="button" id="roomOut" class="btn w50p" onfocus="blur()">나가기</button>
							
							<c:if test="${joinFnshYn eq 'N'}">
								<c:if test="${enableInviteYn eq 'Y'}">
									<button type="button" id="inviteFriendBtn" class="btn w50p" onclick="javascript:owner_member_add('${fn:escapeXml(evtSeq)}', '${fn:escapeXml(rmSeq)}', '${fn:escapeXml(yfriendsMenuInfo.menuName)}')" style="color:white;" onfocus="blur()">초대하기</button>
								</c:if>
								<c:if test="${enableInviteYn eq 'N'}">
									<button type="button" id="inviteFriendBtn" class="btn w50p" onclick="javascript:owner_member_add('${fn:escapeXml(evtSeq)}', '${fn:escapeXml(rmSeq)}', '${fn:escapeXml(yfriendsMenuInfo.menuName)}')" disabled="disabled" onfocus="blur()">초대하기</button>
								</c:if>
							</c:if>
							
							
							<c:if test="${joinFnshYn eq 'Y'}">
								<c:if test="${masterCntrNo eq cntrNo }">
										<button type="button" id="inviteFinishBtn" class="btn w50p" onfocus="blur()">모집완료</button>
								</c:if>
								<c:if test="${masterCntrNo ne cntrNo }">
										<button type="button" id="inviteFriendBtn" class="btn w50p" onclick="javascript:owner_member_add('${fn:escapeXml(evtSeq)}', '${fn:escapeXml(rmSeq)}', '${fn:escapeXml(yfriendsMenuInfo.menuName)}')" disabled="disabled" onfocus="blur()">초대하기</button>
								</c:if>
							</c:if>
							
							
						</c:if>
						<c:if test="${invitedFnshYn eq 'Y'}">
							<c:if test="${eventJoinYn eq 'Y' }">
								<button type="button" id="yBoxRoomGiftBtn" class="btn w100p" disabled="disabled" onfocus="blur()">경품 신청 완료</button>
							</c:if>
							<c:if test="${eventJoinYn eq 'N' }">
								<button type="button" id="yBoxRoomGiftBtn" class="btn w100p" onfocus="blur()">경품 신청하기</button>
							</c:if>
						</c:if>
					</c:if>
						<!-- button에 bth-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<input type="hidden" name="cntrNo" value='${fn:escapeXml(cntrNo)}'>
			<input type="hidden" name="ysid" value='${fn:escapeXml(ysid)}'>
			<input type="hidden" name="autoLogin" value='${fn:escapeXml(autoLogin)}'>
			<input type="hidden" name="osTp" value='${fn:escapeXml(osTp)}'>
			<input type="hidden" name="appVrsn" value='${fn:escapeXml(appVrsn)}'>
			<input type="hidden" name="rmSeq" value='${fn:escapeXml(rmSeq)}'/>
			<input type="hidden" name="masterCntrNo" value='${fn:escapeXml(masterCntrNo)}'/>
			<input type="hidden" name="invitedFnshYn" value='${fn:escapeXml(invitedFnshYn)}'/>
			<input type="hidden" name="giftRcvYn" value='${fn:escapeXml(giftRcvYn)}'/>
			<input type="hidden" name="evtType" value='${fn:escapeXml(eventDetail.evtType)}'/>
			<input type="hidden" name="evtTypeNm" value='${fn:escapeXml(eventDetail.evtTypeNm)}'/>
			<c:if test="${empty eventGiftList}">
				<input type="hidden" name="giftFalg" value="N"/>
			</c:if>
			<c:if test="${!empty eventGiftList}">
				<input type="hidden" name="giftFalg" value="Y" />
			</c:if>
			<input type="hidden" name="multiGiftRewardChk" value='${fn:escapeXml(multiGiftRewardChk)}'>
		</div>


		<!-- SID-YFR-01-POP1_방개설_popup -->
		<div id="yBoxRoomPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends.png" alt="">
					<p><c:out value="${yfriendsMenuInfo.menuName}" />을 개설하시겠습니까?</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="yBoxRoomPopCancel" class="btn w50p" onfocus="blur()">취소</button>
					<button type="button" id="yBoxRoomPopOk" class="btn w50p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP1_방개설_popup -->

		
		<!-- SID-YFR-01-POP2_참여대상자아님_popup -->
		<div id="errorPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div id="errorPopCover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends@2x.png" alt="" width="76px;" height="76px;">
					<p id="errorContent"></p>
					<p id="errorContent2" class="f-gray sub-con"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="errorPopCancel" class="btn w50p" onfocus="blur()">닫기</button>
					<button type="button" class="btn w50p" onclick="javascript:goEventDetail()" onfocus="blur()">자세히 보기</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP2_참여대상자아님_popup -->

		
		<!-- SID-YFR-01-POP3_방초대받음_수락전 -->
		<div class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends@2x.png" alt="" width="76px;" height="76px;">
					<p><c:out value="${yfriendsMenuInfo.menuName}" />에 초대되었습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" class="btn w50p" onfocus="blur()">닫기</button>
					<button type="button" class="btn w50p" onfocus="blur()">초대 리스트 보기</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP3_방초대받음_수락전 -->
		
		<!-- SID-YFR-01-POP4_방폐쇄 -->
		<div id="roomDelPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends@2x.png" alt="" width="76px;" height="76px;">
					<p><c:out value="${yfriendsMenuInfo.menuName}" />을 나가시겠습니까?</p>
					<c:if test="${masterCntrNo eq cntrNo}">
						<p class="f-gray sub-con">방이 폐쇄되며 처음부터 다시 <br>구성원을 모집해야 합니다.</p>
					</c:if>
					<c:if test="${masterCntrNo ne cntrNo}">
					<p class="f-gray sub-con">방을 나가시는 경우 혜택을 받으실 수 없습니다. </p>
					</c:if>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="roomDelPopCancel" class="btn w50p" onfocus="blur()">취소</button>
					<button type="button" id="roomDelPopOk" class="btn w50p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP4_방폐쇄 -->
		
		<!-- SID-YFR-01-POP5_내보내기 -->
		<div id="banPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p id="banUserNm" class="user-team"></p>
					<p id="banUserNmOk"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="banPopCancel" class="btn w50p" onfocus="blur()">취소</button>
					<button type="button" id="banPopOk" class="btn w50p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP5_내보내기 -->
		
		<!-- SID-YFR-01_이벤트종료 -->
		<div id="eventEnd" class="finish">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<c:if test="${endYn eq 'Y'}">
			<p id="closeContent"><c:out value="${yfriendsMenuInfo.menuName}" /> 이벤트가<br/>종료되었습니다.</p>
			</c:if>
			<c:if test="${endYn eq 'N'}">
				<c:if test="${remainGiftYn eq 'N' or existGiftYn eq 'N'}">
					<p id="closeContent">준비된 경품 소진으로 이벤트가 <br/>조기 종료되었습니다.</p>
					<!--이벤트에 경품이 없을 경우 Y면 경품o N이면 경품x  -->
					<!--//이벤트에 경품이 없을 경우 Y면 경품o N이면 경품x  -->
				</c:if>
			</c:if>
		</div>
				
		<!-- SID-YFR-01_방 에러 팝업-->
		<div id="roomErrorPopup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="errorCover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p id="errorMsg"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="errorOk" class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		
		<!-- SID-YFR-01-POP7_데이터신청 -->
		<div id="finishPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div id="finishPopCover" class="cover"></div>
			
		
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends@2x.png" alt="" width="76px;" height="76px;">
					<p><c:out value="${yfriendsMenuInfo.menuName}" /> 모집을 완료하시겠습니까?</p>
					<p class="f-gray sub-con">모집을 완료하고 혜택을 신청합니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="finishPopCancel" class="btn w50p">취소</button>
					<button type="button" id="finishPopOk" class="btn w50p">확인</button>
					
				</div>
			</div>
		
		</div>
		<!-- //SID-YFR-01-POP7_데이터신청 -->
		
		<!-- SID-YFR-01-POP8_경품신청안내 -->
		<div id="giftInfoPop" class="popup">
			<!-- 배경 커버(블랙) -->
			<div id="giftInfoPopCover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img src="/yapp3/res/img/ic_YFriends@2x.png" alt="" width="76px;" height="76px;">
					<p>고객님께서는 선착순으로 제공하는<br><c:out value="${yfriendsMenuInfo.menuName}" />  경품을 받으실 수 있습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="giftInfoPopOk" class="btn w100p">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //SID-YFR-01-POP8_경품신청안내 -->
		
		<!-- EVE-01-POP4_경품신청완료 -->
		<div id="giftChoice_complete_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="giftChoice_complete_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>경품 신청이 완료되었습니다!</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="giftChoice_complete_ok"class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP4_경품신청완료 -->
		
		<!-- EVE-01-POP5_정보미입력 -->
		<div id="not_info_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>정보를 입력해주세요.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="not_info_ok" class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP5_정보미입력 -->
		
		<!-- EVE-01-POP6_경품증정 -->
		<div id="first_gift_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="first_gift_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img id="gift_url" src="" alt="" width="108px;" height="108px;">
					<p id="gift_name"></p>
					<p class="f-gray sub-con">
						축하합니다. 이벤트 상품은<br>리워드 함에서 확인하실 수 있습니다.
					</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="reward_move" class="btn w50p" onfocus="blur()" >리워드함</button>
					<button type="button" id="first_gift_cancel"  class="btn w50p" onfocus="blur()" >닫기</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP6_경품증정 -->
		
		<!-- EVE-01-POP6_1_경품증정 -->
		<div id="first_data_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="first_data_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img id="data_url" src="" alt="" width="108px;" height="108px;">
					<p id="data_name"></p>
					<p class="f-gray sub-con">
						축하합니다. 이벤트 상품은<br>리워드 함에서 확인하실 수 있습니다.
					</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="data_reward_move" class="btn w50p" onfocus="blur()" >리워드함</button>
					<button type="button" id="first_data_cancel"  class="btn w50p" onfocus="blur()" >닫기</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP6_1_경품증정 -->
		
		<!-- EVE-01-POP7_경품신청완료(랜덤제공시) -->
		<div id="random_complete_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="random_complete_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>이벤트 참여가 완료되었습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="random_complete_ok" class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP7_경품신청완료 -->
		
		<!-- EVE-01-POP8_경품이 없는 경우(에러 팝업) -->
		<div id="not_gift_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<img id="not_gift_url" src="" alt="" width="108px;" height="108px;">
					<p id="not_gift_name"></p>
					<p id="error_msg" class="f-gray sub-con"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="not_gift_ok" class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP8_경품이 없는 경우 -->
		
		
		<!-- EVE-01-POP9_경품선택 -->
		<div id="giftChoicePopup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<!--경품 선택  -->
					<c:if test="${eventDetail.giftChoiceType eq 'C0001'}">
						<p class="f-yspotlightotapp">받고 싶은 경품을 선택해주세요!</p>
						<div class="radio-list-row">
							<ul>
								<c:forEach var="giftList" items="${eventGiftList}" varStatus="status">
								<c:forEach var="roomInfoList" items="${roomInfoList}" varStatus="room_status">
								<c:if test="${giftList.memNum eq roomInfoList.joinCnt}">
									<c:if test="${status.index eq '0'}">
										<li class="on">
											<label for='giveaway<c:out value="${status.count}" />'><img src='<c:out value="${giftList.imgUrl}" />' alt=""></label>
											<div class="radio-custom-wrap">
												<input type="radio" name="giveaway" id='giveaway<c:out value="${status.count}" />' value='giveaway<c:out value="${status.count}" />' checked>
												<span class="radio-custom"></span>
												<input type="hidden" name="giftType" value='<c:out value="${giftList.giftType}" />'/>
												<input type="hidden" name="giftSeq" value='<c:out value="${giftList.giftSeq}" />'/>
												<input type="hidden" name="issueSeq" value='<c:out value="${giftList.issueSeq}" />'/>
												<label for='giveaway<c:out value="${status.count}" />'><c:out value="${giftList.giftName}" /></label>
											</div>
										</li>
									</c:if>
									<c:if test="${status.index ne '0'}">
										<li>
											<label for='giveaway<c:out value="${status.count}" />'><img src='<c:out value="${giftList.imgUrl}" />' alt=""></label>
											<div class="radio-custom-wrap">
												<input type="radio" name="giveaway" id='giveaway<c:out value="${status.count}" />' value='giveaway<c:out value="${status.count}" />'>
												<span class="radio-custom"></span>
												<input type="hidden" name="giftType" value='<c:out value="${giftList.giftType}" />'/>
												<input type="hidden" name="giftSeq" value='<c:out value="${giftList.giftSeq}" />'/>
												<input type="hidden" name="issueSeq" value='<c:out value="${giftList.issueSeq}" />'/>
												<label for='giveaway<c:out value="${status.count}" />'><c:out value="${giftList.giftName}" /></label>
											</div>
										</li>
									</c:if>
									</c:if>
								</c:forEach>
								</c:forEach>
							</ul>
							<p class="radio-list-resualt">경품으로 <span id="data-resualt"></span>를<br>선택하셨습니다.</p>
						</div>
					</c:if>
					<!--//경품 선택  -->
					
					<!--랜덤 제공  -->
					<c:if test="${eventDetail.giftChoiceType eq 'C0002'}">
						<div class="radio-list-row">
							<ul>
								<c:forEach var="giftList" items="${eventGiftList}" varStatus="status">
									<li class="on"><label for='giveaway<c:out value="${status.count}" />'><img
											src='<c:out value="${giftList.imgUrl}" />' alt=""></label>
										<div class="radio-custom-wrap">
											<input type="hidden" name="giftType" value='<c:out value="${giftList.giftType}" />'/>
											<label for='giveaway<c:out value="${status.count}" />'><c:out value="${giftList.giftName}" /></label>
										</div></li>
								</c:forEach>
							</ul>
							<p class="radio-list-resualt">
								랜덤 제공 방식 경품입니다.
							</p>
						</div>
					</c:if>
					<!--//랜덤 제공  -->
					
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="giftChoiceCancel" class="btn w50p" onfocus="blur()">취소</button>
					<button type="button" id="giftChoiceComplete" class="btn w50p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP9_경품선택 -->
		
		
		<!-- EVE-01-POP10_배송지정보입력 (서버에 올릴땐 우편번호에 readOnly 추가)-->
		<div id="deliveryPopup" class="popup" style="z-index:3;">
			<!-- 배경 커버(블랙) -->
			<div id="deliveryCover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div id="delivery_resize" class="pop-con" style="overflow: auto; height:390px;">
					<p class="f-yspotlightotapp">배송지 정보를 입력해 주세요.</p>
					<div class="input-list-wrap">
						<ul>
							<li><input type="text" id="recvName" name="recvName" placeholder="이름 입력" maxlength="10"></li>
							<li><input type="text" id="recvMobileNo" name="recvMobileNo" placeholder="전화번호 입력" maxlength="11"></li>
							<li><input type="text" id="post" name="post" placeholder="우편번호" readOnly="readonly" onclick="execPost()" ><a href="javascript:void(0);" onclick="execPost()" onfocus="blur()" style="text-decoration: none; color: black;">우편번호 검색</a></li>
							<li id="wrapPost" style="display:none;border:1px solid;width:100%;height:300px;margin:5px;position:relative;clear: both;" >
								<img src="/yapp3/res/img/icon_popClose.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1;height:17px;width:17px;" onclick="foldPostCode()" alt="접기버튼">
							 </li>
							<li><input type="text" id="addr" name="addr" placeholder="주소"></li>
							<li><input type="text" id="addrDtl" name="addrDtl" placeholder="상세주소" maxlength="80"></li>
							<li><input type="text" id="remarks" name="remarks" placeholder="비고" maxlength="80"></li>
						</ul>
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="deliveryCancel" class="btn w50p" onfocus="blur()" >취소</button>
					<button type="button" id="deliveryComplete" class="btn w50p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP10_배송지정보입력 -->
		

<script type="text/javascript">
var element_wrap = document.getElementById("wrapPost");
function foldPostCode(){
	element_wrap.style.display = 'none';
}
function execPost(){
	var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
	new daum.Postcode({
		oncomplete:function(data){
			//팝업에서 검색 결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
			
			//도로명 주소의 노출 규칙에 따라 주소를 조합한다.
			//내려오는 변수가 값이 없는 경우엔 공백값을 가지므로, 이를 참고하여 분기한다.
			var addr = ''; //data.roadAddress (도로명 주소 변수)
			var extraAddr = '';
			//사용자가 선택한 주소의 타입이 도로명일 경우
			if(data.userSelectedType == 'R'){
				addr = data.roadAddress;
				//범정동명이 있을경우 추가한다. (법정리는 제외)
				//법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
					extraAddr += data.bname;
				}
				//건물명이 있고, 공동주택일 경우 추가한다.
				if(data.buildingName!=='' && data.apartment ==='Y'){
					extraAddr += (extraAddr !== ''?', ' + data.buildingName:data.buildingName)
				}
				//도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if(extraAddr !==''){
					extraAddr = '('+extraAddr+')';
				}
			} else {
				addr = data.jibunAddress;
			}
			
			//우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById("post").value = data.zonecode;
			document.getElementById("addr").value = addr + extraAddr;
			document.getElementById("addrDtl").focus();
			element_wrap.style.display = 'none';
			document.body.scrollTop = currentScroll;
		},
		onresize:function(size){
			element_wrap.style.height=size.height+'px';
		},
	width:'100%',
	height:'100%'
	}).embed(element_wrap);
	element_wrap.style.display = 'block';
}
</script>

	</body>
</html>