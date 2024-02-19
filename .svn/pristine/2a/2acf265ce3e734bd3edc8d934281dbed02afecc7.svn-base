<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">

		<meta property="og:title" content="${eventDetail.evtTitle}"/>
		<meta property="og:description" content="${eventDetail.evtSmallTitle}"/>
		<meta property="og:image" content="${eventDetail.evtDtlTopImg}"/>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, touch-action: pan-y;">
		<title>Y BOX</title>
		<script type="text/javascript" src="/yapp3/res/ext/jquery-3.5.1/jquery-3.5.1.js"></script>
		<!-- <script type="text/javascript" src="/yapp3/res/ext/slick-1.8.1/slick.min.js"></script> -->
		<script type="text/javascript" src="/yapp3/res/ext/script.js"></script>
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/reset.css">
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/font-ios.css">
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/font-android.css">
		<!-- ios, android를 붙혀 구분 -->
		<!-- <link rel="stylesheet" href="/yapp3/res/css/slick.css"> -->
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/layout.css?20230130">
		
		<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>
		<script src="https://developers.kakao.com/sdk/js/kakao.min.js"></script>
		
		<link type="text/css" rel="stylesheet" href="/yapp3/res/ext/swiperjs/swiper-bundle.min.css">
		<script type="text/javascript" src="/yapp3/res/ext/swiperjs/swiper-bundle.min.js"></script>
		<script type="text/javascript" src="/yapp3/res/js/common.js"></script> <!-- 210615 추가 -->
		
		<style>
			a:hover, .btn:hover {text-decoration: none; color: white;}
			.content{padding-bottom:35%;}
			.layout{padding-top:4%;}
			.layout p{font-size:0.815rem; color:#7D7D7D;}
			.layout p.font_b{font-size:1.1rem; color:#7D7D7D;}
			.layout p.font_m{font-size:0.925rem; color:#7D7D7D;}
			.layout p + p{margin-top:6%;}
			.layout .tit{color:#111; font-size:0.876rem;}
			.layout .tit + p{margin-top:2%;}
			.layout .tit img{vertical-align:top; padding-top:0.25rem; margin-right:2%;}

			.layout div,
			.layout .layout-div{margin-top:8%;}
			.layout div:first-child,
			.layout .layout-div:first-child{margin-top:0 !important;}
			
			.iframebox {position: relative; width: 100%; padding-bottom: 56.25%;}
			.iframebox iframe {position: absolute; left:50%; transform:translatex(-50%); width: 100%; height: 100%;}
			
			.row-list li{width:50%; text-align: center;}
			.row-list li p{color:#111;}

			.y-info-list li{color:#111;}
			.y-info-list li span{color:#111;}

			.disc-ul + .bg-skyblue{margin-top:8%; position:relative; z-index:-1;}
			.bg-skyblue .disc-ul{padding:4% 5%;}
			.bg-skyblue .disc-ul li:before{background-color:#111;}
			.bg-skyblue .disc-ul li p{color:#111}​
			
			/* 공유 */
			h2.tit + p{margin-top:3%; font-size:0.876rem}
			.coupon-ul-wrap{margin-top:13%;}
			.inline-block-list li{margin-left:4%;}
			.inline-block-list li:first-child{margin-left:0;}
		    #notLogin{display:none;}
		    #notLogin2{display:none;}
		    #notTarget{display:none;}
		    #notTarget2{display:none;}
		    #notTextLimit{display:none;}
		    #replyLimit{display:none;}
	    	#share_popup{display:none;}
	    	#AddComment{display:none;}
	    	#ModifyComment{display:none;}
	    	#reply_modify_popup{display:none;}
	    	#DeleteComment{display:none;}
	    	#reply_delete_popup{display:none;}
	    	#giftChoice_popup{display:none;}
	    	#giftChoice_complete_popup{display:none;}
	    	#delivery_popup{display:none;}
	    	#not_info_popup{display:none;}
	    	#not_vote_popup{display:none;}
	    	#only_vote_popup{display:none;}
	    	#complete_vote_popup{display:none;}
	    	#first_gift_popup{display:none;}
	    	#first_ticket_gift_popup{display:none;}
	    	#random_complete_popup{display:none;}
	    	#not_gift_popup{display:none;}
	    	#attend_check_popup{display:none;}
	    	#only_attend_popup{display:none;}
	    	#YcanvasClass{display:none;}
	    	#ApplyYcanvasClassPopup{display:none;}
	    	#ycanvas_complete_popup{display:none;}
	    	#not_class_limit_popup{display:none;}
	    	
	    	.swiper-div {
				padding-left: 8%;
   				padding-right: 8%;
   				padding-top: 3%;
   				padding-bottom: 3%;
			 }
	    	
	    	.swiper-container { 
				 width: 100%; 
				 height: 100%; 
				 z-index: 0;
			 } 
			  
			 .swiper-slide { 
				 text-align: center; 
				 font-size: 18px; 
				 background: #fff; 
				  
				 /* Center slide text vertically */ 
				 display: -webkit-box; 
				 display: -ms-flexbox; 
				 display: -webkit-flex; 
				 display: flex; 
				 -webkit-box-pack: center; 
				 -ms-flex-pack: center; 
				 -webkit-justify-content: center; 
				 justify-content: center; 
				 -webkit-box-align: center; 
				 -ms-flex-align: center; 
				 -webkit-align-items: center; 
				 align-items: center; 
			 } 
			 
			 .swiper-container-horizontal > .swiper-scrollbar { 
				 position: relative; 
				 bottom: 0px; 
			 } 
			 
			.layout .link_box {
			    padding: 15px 15px;
			    border: 1px solid #aaa;
			}
			.layout div, .layout .layout-div {
			    margin-top: 8%;
			}
			a, a:hover, .btn:hover {
			    text-decoration: none;
			    color: inherit;
			}
			.link_box img {
			    width: 65px;
			    height: 65px;
			}
			img {
			    max-width: 100%;
			}
			.link_box dl {
			    display: inline-block;
			    margin-left: 10px;
			    width: calc(100% - 120px);
			    vertical-align: top;
			}
			.link_box dl dt {
			    margin-top: 20px;
			    font-size: 0.876rem;
			}
			.link_box dl dd {
			    margin-top: 10px;
			    font-size: 0.815rem;
			    color: #7D7D7D;
			}
			a{
				-webkit-tap-highlight-color: transparent;
			}
			 
			 /*210614*/
			.check-wrap{padding:0 5%;} /* 좌우여백 수정 : 221118 */
			.check-inner {font-size: 0;}
			.check-inner li {display: inline-block; /* width: calc(50% - 10px); 투표 수치 여부에 따라 적용하므로 주석처리함*/ vertical-align: top;}
			.check-inner li:nth-child(n + 3) {margin-top: 25px;}
			.check-inner li:nth-child(2n) {margin-left: 20px;}
			.check-inner li.active .img-area {position: relative;}
			.check-inner li.active .img-area::after {content: ""; position: absolute; top: 0; left:0; width: calc(100% - 4px); height: calc(100% - 4px); border: 2px solid #54b8b3; border-radius: 10px; background-color: rgba(0,0,0,0.2);}
			.check-inner li .img-area {text-align: center; width: 100%;}
			.check-inner li .img-area img {position: relative; width: 100%; height: auto; border-radius: 10px;}
			.check-inner li dl {margin-top: 3px; width: 100%; text-align: center;}
			.check-inner .radio-custom-wrap {padding-left: 16px;}
			.check-inner .radio-custom-wrap .radio-custom {top:2px;}
			.check-inner .graph {position: relative; margin-top: -5px; width: 100%; height: 10px; background-color: #f3f3f3; border-radius: 5px; overflow: hidden;}
			.check-inner .graph span {position: absolute; height: 10px; background-color: #54b8b3;}
			
			/*20220922*/			
			.vote-card { position: relative; box-shadow: 0 2px 2px rgb(0 0 0 / 20%); padding: 3px; border-radius:5px; overflow:hidden; /* aspect-ratio: 5/8; 투표 수치 여부에 따라 적용하므로 주석처리함*/} 
			#vote-group { width: 100%; position: absolute; bottom: 8px; height: 30px; left: 0; padding-left: 5px; padding-right: 5px; }
			#vote-container { position: relative; height: 25px; margin-top: 3px; }
			#vote-cont1 { position: absolute; left: 1%; font-size:0.876rem; color: #595959;}
			#vote-cont2 { position: absolute; right: 1%; font-size:0.876rem; color: #595959;}
			
			/*20230413*/	
			.campus-card {height: 100px; display: flex; align-items: center; justify-content:space-between;}
			.class-info {width:70vw; display: flex; align-items: center; justify-content:flex-start;}
			.campus-card .img-area, #class_deadline {width: 88px; height: 70px; border-radius: 8px; position:relative;}
			.campus-card .img-area img {width:100%; height: 70px; object-fit: cover; border-radius:8px;}
			.class-start, .class-title {font-size:13px; color:#4b4b4b;}
			.campus-title-area {width:60%; padding-left: 10px;}
			.campus-title-area > div:last-child {font-size:12px; color:#959595;}
			.campus-card > .btn-area > button {width:46px; height:23px; background-color:#00b5b6; border-radius:4px; border:none; font-size:11px; color:#ffffff;}
			.campus-card > .btn-area > .join {background-color:#525252;}
			.campus-card > .btn-area > .full {background-color:#c1c1c1;}
			.class-inner hr:last-child { display:none; }
			#class_deadline {display:flex; align-items:center; justify-content: center; position:absolute; top:0;}
			#deadline_image {width:56px; height: 17px; border-radius:0; filter:brightness(1); object-fit:contain;}
			
			
			/*확대 modal 팝업*/
			.modal-popup{
				position: relative;
				display: block;
				width: 100%;
				height: 100%;
				position: fixed;
				left: 0px;
				top: 0px;
				z-index: 2;
				cursor:hard;
				background-color: black;
				text-align: end;
			}
			
			.popup-close{
				display: block;
				position: absolute;
				right : 15px;
				top : 15px;
				z-index: 2000;
			}
		</style>
		
		
<script type="text/javascript">
var doubleSubmitFlag = false;
function doubleSubmitCheck(){
	if(doubleSubmitFlag){
		return doubleSubmitFlag;
	}else{
		doubleSubmitFlag = true;
		return false;
	}
}

//스크립트 작성
$(document).ready(function(){
	
	//EVE-01-POP2_이벤트 공유하기 팝업
	$("#share_btn").click(function(){
		scrollDisble();
		$("#share_popup").show();
	});
	//EVE-01-POP2_이벤트 공유하기 팝업 닫기
	$("#share_hide_btn").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	$("#limit_hide_btn").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	$("#target_hide_btn").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	$("#target_hide_btn2").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	$("#target_link_btn").click(function(){
		scrollAble();
		if(targetBtnUrl.length >0){
			window.open(targetBtnUrl);
		}else{
			window.open('https://kt.com');
		}
	});

	var evtSeq="${evtSeq}";
	var linkType="${eventDetail.linkType}";
	var loginYn="${loginYn}";
	var targetYn="${targetYn}";
	var targetCode="${targetCode}";
	var memStatus="${memStatus}";
	var linkUrl="${eventDetail.linkUrl}";
	var evtType="${eventDetail.evtType}";
	var eventJoinYn="${eventJoinYn}";
	var endYn="${eventDetail.endYn}";
	var progressType="${eventDetail.progressType}";	//이벤트상태(P:시작전,I:진행중,E:종료)
	var cntrNo="${cntrNo}";
	var ysid="";
	var autoLogin="";
	var osTp="";
	var appVrsn="${appVrsn}";
	var url="";
	var title="";
	var soldoutYn="${soldoutYn}";
	var voteYn="${voteYn}"; //210610 투표하기 상태(Y:투표,N:미투표)
	var voteRateYn = "${voteRateYn}"; //230106 투표수치 표시여부 (Y:표시, N:미표시)
	var voteItem="${voteItem}"; //210615 사용자 투표대상
	var eventTicketHaveYn="${eventTicketHaveYn}"; //210820 응모권보유이벤트 여부
	var eventTicketJoinYn="${eventTicketJoinYn}"; //210820 응모권 보유 이벤트 참여 여부
	var addrFormYn="${eventDetail.giftAddrYn}"; //210820 응모권 보유 이벤트 참여 여부
	var targetMem="${eventDetail.targetMem}"; //이벤트 참여 여부 정회원(G0001), 준회원(G0002), 전체(G0003))
	var replyYn="${eventDetail.replyYn}";
	var replyEndSeq = 0;
	var replyCnt = "${replyCnt}";
	var likeCnt = "${likeCnt}";
	var likeYn = "${likeYn}";
	var targetBtnYn="${eventDetail.targetButtonYn}";
	var targetBtnUrl="${eventDetail.targetButtonUrl}";
	var targetBtnNm="${eventDetail.targetButtonNm}";
	var eventGiftList = "${eventGiftList}";
	var assoAddrYn="${assoAddrYn}";//Y:주소입력, N주소 미입력
	
	if(replyYn=="Y") {
		$("#replyList").text(replyCnt);
		$("#replyList").css({
			"background-image": "url(/yapp3/res/images/ic_message.png)",
			"color":"#111"
		});
	}else{
		$("#replyList").css({
			"background-image": "url(/yapp3/res/images/ic_message_gray.png)",
			"color":"transparent"
		});
	}
	
	if(likeYn == "Y"){
		$("#likeBtn").css('background-image', 'url(/yapp3/res/images/ic_heart_fill.png)');
	}else{
		$("#likeBtn").css('background-image', 'url(/yapp3/res/images/ic_heart.png)');
	}
	
	$("#likeBtn").text(likeCnt);
	 
	//230116 투표이벤트 css 적용
	  if(evtType=="G0008"){
		 if(voteRateYn == "N"){ //투표수치 미표시일 경우
				$(".check-inner dl").css("margin-bottom", "25px");
				$(".check-inner").css("display", "grid");
				$(".check-inner").css("grid-template-columns", "1fr 1fr");
				$(".check-inner").css("grid-auto-rows", "1fr");
				$(".check-inner").css("gap", "25px 20px");
				
				$(".vote-card").css("margin", '0px');
			}else {
				$(".vote-card").css("aspect-ratio", '5/8');
				$(".vote-card").css("width", 'calc(50% - 10px)');
			}
	 }
	
	if(progressType=="P"){	//이벤트 진행예정
		$("#evt_move").children().text("이벤트 예정").attr("disabled",true);
		$("#check_attendance_btn").attr("disabled",true);
	}
	
	if(progressType=="I"){//이벤트 진행중
		
		if(loginYn=="Y"){	//로그인시 ::추후에 Y로 변경해야 함
		
		if(targetYn=="Y"){
			
			if(eventJoinYn=="N"){			//이벤트 참여하지 않은 경우
				
				if(evtType=="G0001" || evtType=="G0003" || evtType=="G0004" || evtType=="G0006"){		//일반 이벤트 (G0001), 매거진 (G0003), Y프렌즈 (G0004), Y소식 (G0006)
					//하단 이벤트 이벤트
					$("#evt_move").click(function(){
						
						var newTypeYn = "N";
						var standardVrsn = "3.0.0";
						
						if(!isEmpty(appVrsn) && compareVersion(appVrsn, standardVrsn)){
							//앱버전이 존재하고 3.0.0보다 크면 (>) Y
							newTypeYn = "Y";
						}
						
						//이벤트 응모권 보유안함/ 응모권 보유한이벤트 참여햇을때
						if(eventTicketHaveYn == "N" || eventTicketJoinYn == "Y" || (eventTicketJoinYn =="N" && soldoutYn == "Y")){
							
							if(newTypeYn == "N"){
								//3.0.1 이전 사용자
								
								if(linkType =="L0001"){
									
									if(linkUrl.indexOf("reward")=="1"){
										title="리워드";
									}else{
										title="${yfriendsMenuName}";
									}
									
									console.log("title : "+title);
									
									var postData = {
										gm : '3',	
										url : linkUrl,
										evtSeq : evtSeq,
										title : title,
									};
									
									if(isIOS()){
										//IOS
										window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
									} else {
										//Android
										url = linkUrl;
										window.YDataBox.moveWebViewPage(url, evtSeq, title);
										window.close();
									}
								}
								
								if(linkType=="L0002"){	//외부 링크
									//
									$.ajax({
											type : "post",
											headers : {
												ysid : $('[name=ysid]').val(),
												autoLogin : $('[name=autoLogin]').val(),
												osTp : $('[name=osTp]').val(),
												appVrsn : $('[name=appVrsn]').val()
											},
											url : "/yapp3/cms/event/linkappl",
											data : {"evtSeq":evtSeq},
											success : function(data){
												
											},
											error : function(error){
												console.log(error);
											}
									}); //ajax End 
									
									$("#evt_move").attr("href",linkUrl).attr("target","_blank");
								
								}
								
							}else{
								//3.0.1 이후 사용자
								
								if(linkUrl.indexOf("reward")=="1" || linkUrl.indexOf("yFriends")=="1"){
									
									if(linkUrl.indexOf("reward")=="1"){
										title="리워드";
									}else{
										title="${yfriendsMenuName}";
									}
									
									console.log("title : "+title);
									
									var postData = {
										gm : '3',	
										url : linkUrl,
										evtSeq : evtSeq,
										title : title,
									};
									
									if(isIOS()){
										//IOS
										window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
									} else {
										//Android
										url = linkUrl;
										window.YDataBox.moveWebViewPage(url, evtSeq, title);
										window.close();
									}
									
								}else{
									
									if(linkType !="L0002"){
										
										if(linkUrl.indexOf("reward")=="1"){
											title="리워드";
										}else{
											title="${yfriendsMenuName}";
										}
										
										console.log("title : "+title);
										
										var linkTp;
										
										if(linkType == "L0001"){
											linkTp = "inWeb";
										}else{
											linkTp = linkType;
										}
										
										var postData = {
											url : linkUrl,
											linkType : linkTp,
											title : title,
										};
										
										if(isIOS()){
											//IOS
											window.webkit.messageHandlers.moveLandingPage.postMessage(postData); 
										} else {
											//Android
											url = linkUrl;
											window.YDataBox.moveLandingPage(linkTp, url, title);
											window.close();
										}
									}
									
									if(linkType=="L0002"){	//외부 링크
										//
										$.ajax({
												type : "post",
												headers : {
													ysid : $('[name=ysid]').val(),
													autoLogin : $('[name=autoLogin]').val(),
													osTp : $('[name=osTp]').val(),
													appVrsn : $('[name=appVrsn]').val()
												},
												url : "/yapp3/cms/event/linkappl",
												data : {"evtSeq":evtSeq},
												success : function(data){
													
												},
												error : function(error){
													console.log(error);
												}
										}); //ajax End 
										
										$("#evt_move").attr("href",linkUrl).attr("target","_blank");
									
									}
								}
								
								
							}
							
						//응모권 보유한 이벤트 참여 전
						}else{
							
							//응모권 지급 로직
							if(doubleSubmitCheck()) return;
							
							var giftSeq=$('input:hidden[name=giftSeq]').val();
							var issueSeq=$('input:hidden[name=issueSeq]').val();
							var giftChoiceType="${eventDetail.giftChoiceType}";	//경품 제공 방식 (선택,랜덤)
							var giftOfferType="${eventDetail.giftOfferType}";//O0003추첨, O0002선착순, O0001상품없음

							var eventGiftJoin={};
								eventGiftJoin={"evtSeq":evtSeq
											  ,"giftSeq":giftSeq
											  ,"issueSeq":issueSeq
											  };
								
							//20210811 응모권 경품 타입 추가 시작
						  	$.ajax({
								type : "post",
								headers : {
									cntrNo : $('[name=cntrNo]').val(),
									ysid : $('[name=ysid]').val(),
									autoLogin : $('[name=autoLogin]').val(),
									osTp : $('[name=osTp]').val(),
									appVrsn : $('[name=appVrsn]').val()
								},
								url : "giftTicketRewardJoin",
								data : eventGiftJoin,
								success : function(data){
									
									console.log("data : "+JSON.stringify(data));
									console.log("resultData : "+JSON.stringify(data.resultData));
						
									if(data.resultCd=="200"){
										
										if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
											//선착순/무제한 경품일시
											if(giftOfferType=="O0002"){
												
												//응모권 이미지 위치 정해지면 수정
												$("#ticket_gift_url").attr("src",data.resultData.imgUrl);
												$("#ticket_gift_name").text(data.resultData.giftName);
												
												scrollDisble();
												$("#first_ticket_gift_popup").show();
												eventTicketJoinYn = "Y";
												
												if(newTypeYn == "N"){
													//3.0.1 이전 사용자
													
													if(linkType=="L0001"){
														if(linkUrl.indexOf("reward")=="1"){
															title="리워드";
														}
														else{
															title="${yfriendsMenuName}";
														}
														console.log("title : "+title);
														
														var postData = {
															gm : '3',	
															url : linkUrl,
															evtSeq : evtSeq,
															title : title,
														};
														
														if(isIOS()){
															//IOS
															window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
														} else {
															//Android
															url = linkUrl;
															window.YDataBox.moveWebViewPage(url, evtSeq, title);
															window.close();
														}
													}
													
													if(linkType=="L0002"){	//외부 링크
														//
														$.ajax({
																type : "post",
																headers : {
																	ysid : $('[name=ysid]').val(),
																	autoLogin : $('[name=autoLogin]').val(),
																	osTp : $('[name=osTp]').val(),
																	appVrsn : $('[name=appVrsn]').val()
																},
																url : "/yapp3/cms/event/linkappl",
																data : {"evtSeq":evtSeq},
																success : function(data){
																	
																},
																error : function(error){
																	console.log(error);
																}
														}); //ajax End 
													
														$("#link_go").attr("href",linkUrl).attr("target","_blank");
													}
													
												}else{
													//3.0.1 이후 사용자
													
													if(linkType !="L0002"){
														
														if(linkUrl.indexOf("reward")=="1"){
															title="리워드";
														}else{
															title="${yfriendsMenuName}";
														}
														
														console.log("title : "+title);
														
														var linkTp;
														
														if(linkType == "L0001"){
															linkTp = "inWeb";
														}else{
															linkTp = linkType;
														}
														
														var postData = {
															url : linkUrl,
															linkType : linkTp,
															title : title,
														};
														
														if(isIOS()){
															//IOS
															window.webkit.messageHandlers.moveLandingPage.postMessage(postData); 
														} else {
															//Android
															url = linkUrl;
															window.YDataBox.moveLandingPage(linkTp, url, title);
															window.close();
														}
													}
													
													if(linkType=="L0002"){	//외부 링크
														//
														$.ajax({
																type : "post",
																headers : {
																	ysid : $('[name=ysid]').val(),
																	autoLogin : $('[name=autoLogin]').val(),
																	osTp : $('[name=osTp]').val(),
																	appVrsn : $('[name=appVrsn]').val()
																},
																url : "/yapp3/cms/event/linkappl",
																data : {"evtSeq":evtSeq},
																success : function(data){
																	
																},
																error : function(error){
																	console.log(error);
																}
														}); //ajax End 
														
														$("#link_go").attr("href",linkUrl).attr("target","_blank");
													
													}
												}
											}
										}
									}else{
										$("#error_msg").text(data.resultMsg);	//에러 메세지
										scrollDisble();
										$("#not_gift_popup").show();
										eventTicketJoinYn = "Y";
										
										
										if(newTypeYn == "N"){
											//3.0.1 이전 사용자
											
											if(linkType=="L0001"){
												if(linkUrl.indexOf("reward")=="1"){
													title="리워드";
												}
												else{
													title="${yfriendsMenuName}";
												}
												console.log("title : "+title);
												
												var postData = {
													gm : '3',	
													url : linkUrl,
													evtSeq : evtSeq,
													title : title,
												};
												
												if(isIOS()){
													//IOS
													window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
												} else {
													//Android
													url = linkUrl;
													window.YDataBox.moveWebViewPage(url, evtSeq, title);
													window.close();
												}
											}
											
											if(linkType=="L0002"){	//외부 링크
												//
												$.ajax({
														type : "post",
														headers : {
															ysid : $('[name=ysid]').val(),
															autoLogin : $('[name=autoLogin]').val(),
															osTp : $('[name=osTp]').val(),
															appVrsn : $('[name=appVrsn]').val()
														},
														url : "/yapp3/cms/event/linkappl",
														data : {"evtSeq":evtSeq},
														success : function(data){
															
														},
														error : function(error){
															console.log(error);
														}
												}); //ajax End 
											
												$("#soldout_link_go").attr("href",linkUrl).attr("target","_blank");
											}
											
										}else{
											//3.0.1 이후 사용자
											
											if(linkType !="L0002"){
												
												if(linkUrl.indexOf("reward")=="1"){
													title="리워드";
												}else{
													title="${yfriendsMenuName}";
												}
												
												console.log("title : "+title);
												
												var linkTp;
												
												if(linkType == "L0001"){
													linkTp = "inWeb";
												}else{
													linkTp = linkType;
												}
												
												var postData = {
													url : linkUrl,
													linkType : linkTp,
													title : title,
												};
												
												if(isIOS()){
													//IOS
													window.webkit.messageHandlers.moveLandingPage.postMessage(postData); 
												} else {
													//Android
													url = linkUrl;
													window.YDataBox.moveLandingPage(linkTp, url, title);
													window.close();
												}
											}
											
											if(linkType=="L0002"){	//외부 링크
												//
												$.ajax({
														type : "post",
														headers : {
															ysid : $('[name=ysid]').val(),
															autoLogin : $('[name=autoLogin]').val(),
															osTp : $('[name=osTp]').val(),
															appVrsn : $('[name=appVrsn]').val()
														},
														url : "/yapp3/cms/event/linkappl",
														data : {"evtSeq":evtSeq},
														success : function(data){
															
														},
														error : function(error){
															console.log(error);
														}
												}); //ajax End 
												
												$("#soldout_link_go").attr("href",linkUrl).attr("target","_blank");
											
											}
										}
									}
								},
								error : function(error){
									console.log(error);
								}
							}); //ajax End  
							//20210811 응모권 경품 타입 추가 끝
						  	doubleSubmitFlag = false;
						}
					});
					
				}//일반 이벤트 (G0001), 매거진 (G0003), Y프렌즈 (G0004), Y소식 (G0006) if end
				

				if(evtType=="G0011"){//Y캔버스 이벤트

					$("#evt_move").click(function(){
						scrollDisble();
						$("#YcanvasClass").show();
					});
				
				}//경품이벤트 if end
				
				if(evtType=="G0002"){		//경품 이벤트
					
					//210924 선착순 경품 소진시
					if(soldoutYn == "Y"){
						$("#evt_move").children().text("경품이 소진되었습니다.").attr("disabled",true);
					}else{
						$("#evt_move").click(function(){
							
							var giftType = "${eventGiftList[0].giftType}";	//G0001오프라인, G0002온라인
							var giftSize = "${fn: length(eventGiftList)}";
							var rewardType = "${eventGiftList[0].rewardType}";	//G0001난수번호, G0002url랜딩
							var giftOfferType="${eventDetail.giftOfferType}";	//O0003추첨, O0002선착순, O0001상품없음
							
				
							if(giftType == "G0002" && giftSize == "1" && rewardType == "G0002" && giftOfferType=="O0002"){ //230512 URL 온라인경품이 1개, 선착순인 경우 경품 선택 화면없이 바로 지급
					
							
								var giftChoiceType="${eventDetail.giftChoiceType}";	//경품 제공 방식 (선택,랜덤)
								var giftSeq="${eventGiftList[0].giftSeq}";
								var issueSeq="${eventGiftList[0].issueSeq}";
								
								var eventGiftJoin={};
								
								eventGiftJoin={"evtSeq":evtSeq
											  ,"giftSeq":giftSeq
											  ,"issueSeq":issueSeq
											  };
				
								if(doubleSubmitCheck()) return;
			
							  	$.ajax({
									type : "post",
									headers : {
										cntrNo : $('[name=cntrNo]').val(),
										ysid : $('[name=ysid]').val(),
										autoLogin : $('[name=autoLogin]').val(),
										osTp : $('[name=osTp]').val(),
										appVrsn : $('[name=appVrsn]').val()
									},
									url : "giftRewardJoin",
									data : eventGiftJoin,
									success : function(data){
										
										console.log("data : "+JSON.stringify(data));
										console.log("resultData : "+JSON.stringify(data.resultData));
							
										if(data.resultCd=="200"){
											
									
											$("#gift_url").attr("src",data.resultData.imgUrl);
											$("#gift_name").text(data.resultData.giftName);
											scrollDisble();
											$("#first_gift_popup").show();
												
										
										}else{
											$("#error_msg").text(data.resultMsg);	//에러 메세지
											scrollDisble();
											$("#not_gift_popup").show();
										}
										
									},
									error : function(error){
										console.log(error);
									}
								}); //ajax End  
							
						}else {
							scrollDisble();
							$("#giftChoice_popup").show();
							}
							
						});
					}
				
				}//경품이벤트 if end
		
				
				//210609 투표이벤트 start
				if(evtType=="G0008"){ 
					
					var giftOfferType="${eventDetail.giftOfferType}";//O0003추첨, O0002선착순, O0001상품없음
					
					//투표는 완료 이제 경품 등록할 차례
					if(voteYn == "Y"){	
						//220926 경품없을시 추가
						
						if(giftOfferType == "O0001"){
							
							$("#evt_move").children().text("참여 완료").attr("disabled",true);
							$("#evt_move").css('display', 'block');
							$("#vote_gift").css('display', 'none');
							$('input[name="voteaway"]').each(function(){
								$(this).prop('disabled', true);
								$(this).trigger('change');
							});
							
						}else{
							
							$("#evt_move").children().text("경품 신청하기");
							$("#evt_move").click(function(){
								scrollDisble();
								$("#giftChoice_popup").show();
								
							});
						}

					//투표하기 AJAX
					}else{
						
						
						
						$("#vote_gift").click(function(){
							scrollDisble();
							$("#giftChoice_popup").show();
							
						});
						
 						$("#evt_move").click(function(){
							var voteChkSeq=$("input:radio[name=voteaway]:checked").siblings('input:hidden[name=voteItemSeq]').val();
							var voteSeq=$("#voteSeq").val();
							if(isEmpty(voteChkSeq)){
								scrollDisble();
								$("#not_vote_popup").show();
								return false;
							}
							
							var voteData = { 
												"evtSeq":evtSeq,
												"voteItemSeq":voteChkSeq,
												"voteSeq":voteSeq
											}
							if(doubleSubmitCheck()) return;
							$.ajax({
								type : "post",
								headers : {
									cntrNo : $('[name=cntrNo]').val(),
									ysid : $('[name=ysid]').val(),
									autoLogin : $('[name=autoLogin]').val(),
									osTp : $('[name=osTp]').val(),
									appVrsn : $('[name=appVrsn]').val()
								},
								url : "eventVote",
								data : voteData, //이벤트순번, 투표대상순번 -- 동운
								success : function(data){
									if(data.resultCd=="200"){
										//voteYn = "Y";
										
   										$('input[name="voteaway"]').each(function(){
											$(this).prop('disabled', true);
											$(this).trigger('change');
										}); 
 										
 										$.each(data.resultInfoList, function(idx, obj){
											$('input[name="voteaway"]').each(function(){
												if(obj.voteItemSeq == $(this).val() ){
													$("#votePercent"+obj.voteItemSeq).css('width',obj.votePercent+'%'); // 동운
													$("#votePercentLetter"+obj.voteItemSeq).text(obj.votePercent+'%'); // 동운
													
													let res = obj.voteItemCnt.toString().replace( /\B(?=(\d{3})+(?!\d))/g, ',');
													$("#voteGetLetter"+obj.voteItemSeq).text( res + ' 표'); // 동운
												}
											});
										});  
 										
 										if(giftOfferType == "O0001"){
 											
 											$("#evt_move").children().text("참여 완료").attr("disabled",true);
 											$("#evt_move").css('display', 'block');
 											$("#vote_gift").css('display', 'none');
 											$('input[name="voteaway"]').each(function(){
 												$(this).prop('disabled', true);
 												$(this).trigger('change');
 											});
 											
 											$("#complete_vote_popup").show();
 											
 										}else{
 											
 											$("#evt_move").css('display', 'none');
 	 										$("#vote_gift").css('display', 'block');
 	 										
 	 										$("#only_vote_popup").show();
 										}

									}//200 end
									else{
										$("#error_msg").text(data.resultMsg);	//에러 메세지
										scrollDisble();
										$("#not_gift_popup").show();
									}
								},	//success end
								error : function(error){
									console.log(error);
								}
							});	//ajax end
							
							doubleSubmitFlag = false;
						}); 
					}
				} //210609 투표이벤트 end
				
			}//이벤트 참여 x if end
			
			if(eventJoinYn=="Y"){
				
				//210615 투표하기
				//220926 보완
				if(evtType=="G0008"){
					
					if(voteYn == "Y"){	
						
						$("#evt_move").children().text("참여 완료").attr("disabled",true);
						$("#evt_move").css('display', 'block');
						$("#vote_gift").css('display', 'none');
						$('input[name="voteaway"]').each(function(){
							$(this).prop('disabled', true);
							$(this).trigger('change');
						});

					//투표하기 AJAX
					}else{
						
						$("#vote_gift").click(function(){
							scrollDisble();
							$("#giftChoice_popup").show();
							
						});
						
 						$("#evt_move").click(function(){
							var voteChkSeq=$("input:radio[name=voteaway]:checked").siblings('input:hidden[name=voteItemSeq]').val();
							var voteSeq=$("#voteSeq").val();
							if(isEmpty(voteChkSeq)){
								scrollDisble();
								$("#not_vote_popup").show();
								return false;
							}
							
							var voteData = { 
												"evtSeq":evtSeq,
												"voteItemSeq":voteChkSeq,
												"voteSeq":voteSeq
											}
							if(doubleSubmitCheck()) return;
							$.ajax({
								type : "post",
								headers : {
									cntrNo : $('[name=cntrNo]').val(),
									ysid : $('[name=ysid]').val(),
									autoLogin : $('[name=autoLogin]').val(),
									osTp : $('[name=osTp]').val(),
									appVrsn : $('[name=appVrsn]').val()
								},
								url : "eventVote",
								data : voteData, //이벤트순번, 투표대상순번
								success : function(data){
									if(data.resultCd=="200"){
										//voteYn = "Y";
										
   										$('input[name="voteaway"]').each(function(){
											$(this).prop('disabled', true);
											$(this).trigger('change');
										}); 

 										$.each(data.resultInfoList, function(idx, obj){
											$('input[name="voteaway"]').each(function(){
												if(obj.voteItemSeq == $(this).val()){
													$("#votePercent"+obj.voteItemSeq).css('width',obj.votePercent+'%'); // -- 동운
													$("#votePercentLetter"+obj.voteItemSeq).text(obj.votePercent+'%');  // 동운													
													
													let res = obj.voteItemCnt.toString().replace( /\B(?=(\d{3}) + (?!\d))/g, ','); 
													$("#voteGetLetter"+obj.voteItemSeq).text( res + ' 표'); // 동운
												}
											});
										});  
 										
 										$("#evt_move").children().text("참여 완료").attr("disabled",true);
											$("#evt_move").css('display', 'block');
											$("#vote_gift").css('display', 'none');
											$('input[name="voteaway"]').each(function(){
												$(this).prop('disabled', true);
												$(this).trigger('change');
											});
											
											$("#complete_vote_popup").show();

									}//200 end
									else{
										$("#error_msg").text(data.resultMsg);	//에러 메세지
										scrollDisble();
										$("#not_gift_popup").show();
									}
								},	//success end
								error : function(error){
									console.log(error);
								}
							});	//ajax end
							
							doubleSubmitFlag = false;
						}); 
					}
					
				}else{
					
					$("#evt_move").children().text("참여 완료").attr("disabled",true);
					
				}
				
				
			}//이벤트 참여시 if end
			
			if(evtType=="G0005" || evtType=="G0010"){
				//출석하기 버튼 클릭시   eventGiftList
				 $("#check_attendance_btn").click(function(){
					var attendGiftYn=$("[name=attendGiftYn]").val();
					  if(attendGiftYn=="N"){
						 if(doubleSubmitCheck()) return;
					   	 $.ajax({
							type : "post",
							headers : {
								cntrNo : $('[name=cntrNo]').val(),
								ysid : $('[name=ysid]').val(),
								autoLogin : $('[name=autoLogin]').val(),
								osTp : $('[name=osTp]').val(),
								appVrsn : $('[name=appVrsn]').val()
							},
							url : "attendJoin",
							data : {"evtSeq":evtSeq},
							success : function(data){
								console.log("data : "+data);
								console.log("data : "+JSON.stringify(data));
								if(data.resultCd=="200"){
									scrollDisble();
								$("#attend_check_popup").show();
								}
								else{
									$("#error_msg").text(JSON.stringify(data.resultMsg));	//에러 메세지
									scrollDisble();
									$("#not_gift_popup").show();
								}
								
							},
							error : function(error){
								console.log(error);
							}
						}); //ajax End
						
					} else if(soldoutYn == "Y"){
						if(doubleSubmitCheck()) return;
					   	 $.ajax({
							type : "post",
							headers : {
								cntrNo : $('[name=cntrNo]').val(),
								ysid : $('[name=ysid]').val(),
								autoLogin : $('[name=autoLogin]').val(),
								osTp : $('[name=osTp]').val(),
								appVrsn : $('[name=appVrsn]').val()
							},
							url : "attendJoin",
							data : {"evtSeq":evtSeq},
							success : function(data){
								console.log("data : "+data);
								console.log("data : "+JSON.stringify(data));
								if(data.resultCd=="200"){
									scrollDisble();
									$("#only_attend_popup").show();
								}
								else{
									$("#error_msg").text(JSON.stringify(data.resultMsg));	//에러 메세지
									scrollDisble();
									$("#not_gift_popup").show();
								}
								
							},
							error : function(error){
								console.log(error);
							}
						}); //ajax End
						//경품이 매진되었을경우
						
					}
					 //출석체크(경품있는경우) 
					 else{
						    scrollDisble();
							$("#giftChoice_popup").show();
					}   
				});
				
			}//출석이벤트 if end 
			
		}//응모대상 if end
		
		}//로그인시 if end
		
	}//이벤트 진행중 if end
	
	if(targetYn=="N"){
		
		//출석체크 이벤트 시작시 팝업 처리
		if(evtType=="G0005"){
			if(loginYn=="Y"){
				$("#notTarget").show();
				$("#check_attendance_btn").click(function(){
					$("#notTarget").show();
				});
			}else{
				scrollDisble();
				$("#notLogin").show();	
			}
		}
		
		$("#evt_move").click(function(){
			
				scrollDisble();
				
				if(targetBtnYn == 'Y'){
					if(targetBtnNm.length > 0){
						$("#target_link_btn").text(targetBtnNm);
					}
					$("#notTarget2").show();
				}else{
					$("#notTarget").show();
				}
			});
	}
	
	if(progressType=="E"){	//이벤트 종료시
		$("#evt_move").children().text("이벤트 종료").attr("disabled",true);
		$("#check_attendance_btn").attr("disabled",true);
	}//이벤트 종료시 if end
				
	//로그인안된경우
 	if(loginYn=="N"){
 		
 		//댓글입력 부분
 		$("#replyText").attr("placeholder", "로그인 후 이용 가능합니다.");
 		$("#replyText").attr("readonly", true);
 		$("#replyWrite").attr("disabled",true);
 		$("#replyText").on("focus", function() {
 			scrollDisble();
 			//$("#notLogin2").show();
 			login_move();
 			$("#replyText").blur();
 		});
 		
 		//팝업 처리
 		if(evtType == "G0002" || evtType == "G0004" || evtType == "G0005" || evtType == "G0008"){
 			$("#evt_move").click(function(){
 				scrollDisble();
 				$("#notLogin").show();
 			});
 		}else{
 			
			$("#evt_move").click(function(){
 				scrollDisble();
 				$("#notLogin").show();
 			});
	 			
 			/* if(linkType=="L0001"){
 	 			$("#evt_move").click(function(){
 	 				scrollDisble();
 	 				$("#notLogin").show();
 	 			});
 	 		}else{
 		 		$("#evt_move").click(function(){
 	 				$.ajax({
 	 					type : "post",
 	 					url : "/yapp3/cms/event/linkapplNoLogin",
 	 					data : {"evtSeq":evtSeq},
 	 					success : function(data){
 	 						
 	 					},
 	 					error : function(error){
 	 						console.log(error);
 	 					}
 	 				}); //ajax End 
 	 			});
 	 			
 		 		$("#evt_move").attr("href",linkUrl).attr("target","_blank");
 				
 	 		} */
 		}

		
		//출석체크하기 로그인 안된경우 팝업
		$("#check_attendance_btn").click(function(){
			scrollDisble();
			$("#notLogin").show();	
		});
		
		
		//댓글 입력 이벤트 처리
		/* $("#replyText").keyup(function (e) {
			
	 		let contents = $(this).val();
	 		var writeBtnYn = 'N';
	 		
	 		if(contents.length == 0 || contents == '') {
	 			$("#replyLimit").text('(0/100)');
	 			$("#replyWrite").attr("disabled",true);
	 			$("#replyLimit").hide();
	 			writeBtnYn = 'N';
	 		}else{
	 			$("#replyLimit").text('('+contents.length+'/100)');
	 			if(writeBtnYn == 'N'){
	 				$("#replyWrite").attr("disabled",false);
	 				$("#replyLimit").show();
		 			writeBtnYn = 'Y';
	 			}
	 		}
	 		
	 		if(contents.length > 100){
	 			$(this).val($(this).val().substring(0,100));
	 			$("#replyLimit").text('(100/100)');
	 			//$("#notTextLimit").show();
	 		}
		});
		
		$("#replyText").on("blur", function() {

			$("#replyLimit").hide();
			$("#replyModify").hide();
			$("#replyWrite").attr("disabled",true);
			$("#replyWrite").show();
			$("#replyText").css('height', '36px').css('height', $("#replyText").scrollHeight);
			var tar_height = $("#replyText").parents('.pop-btn-wrap').outerHeight();
			$("#replyText").parents('.popup').find('.pop-con').css('padding-bottom', tar_height);
 		}); */
		
	}else{
		
		//댓글 입력 이벤트 처리
		$("#replyText").keyup(function (e) {
			
	 		let contents = $(this).val();
	 		var writeBtnYn = 'N';
	 		
	 		if(contents.length == 0 || contents == '') {
	 			$("#replyLimit").text('(0/100)');
	 			$("#replyWrite").attr("disabled",true);
	 			$("#replyLimit").hide();
	 			writeBtnYn = 'N';
	 		}else{
	 			$("#replyLimit").text('('+contents.length+'/100)');
	 			if(writeBtnYn == 'N'){
	 				$("#replyWrite").attr("disabled",false);
	 				$("#replyLimit").show();
		 			writeBtnYn = 'Y';
	 			}
	 		}
	 		
	 		if(contents.length > 100){
	 			$(this).val($(this).val().substring(0,100));
	 			$("#replyLimit").text('(100/100)');
	 			//$("#notTextLimit").show();
	 		}
		});
		
		$("#replyText").on("focus", function() {
			
	 		let contents = $(this).val();
	 		var writeBtnYn = 'N';
	 		
	 		if(contents.length == 0 || contents == '') {
	 			$("#replyLimit").text('(0/100)');
	 			$("#replyWrite").attr("disabled",true);
	 			$("#replyLimit").hide();
	 			writeBtnYn = 'N';
	 		}else{
	 			$("#replyLimit").text('('+contents.length+'/100)');
	 			if(writeBtnYn == 'N'){
	 				$("#replyWrite").attr("disabled",false);
	 				$("#replyLimit").show();
		 			writeBtnYn = 'Y';
	 			}
	 		}
	 		
	 		if(contents.length > 100){
	 			$(this).val($(this).val().substring(0,100));
	 			$("#replyLimit").text('(100/100)');
	 			//$("#notTextLimit").show();
	 		}
 		});
		
		$("#replyText").on("blur", function() {
			
			if($("#replyModify").css('display') == "block"){
				$("#replyText").val("");			
			}
			
			$("#replyLimit").hide();
			$("#replyModify").hide();
			$("#replyWrite").attr("disabled",true);
			$("#replyWrite").show();
			$("#replyText").css('height', '36px').css('height', $("#replyText").scrollHeight);
			var tar_height = $("#replyText").parents('.pop-btn-wrap').outerHeight();
			$("#replyText").parents('.popup').find('.pop-con').css('padding-bottom', tar_height);
 		});
	}
	
	//로그인안된경우 로그인 페이지 이동
	 $("#login_move").click(function(){
			login_move();
	}); 
	//로그인안된경우 로그인 페이지 이동
	 $("#login2_move").click(function(){
			login_move();
	}); 
	//출석 체크 이벤트
	//로그인안된경우  팝업 닫기
	$('#login_hide_btn').click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	//로그인안된경우  팝업 닫기
	$('#login2_hide_btn').click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	//댓글 수정 완료  팝업 닫기
	$('#reply_modify_ok').click(function(){
		//scrollAble();
		$(this).parents('.popup').hide();
		getReplyList(0, "N");
	});
	//댓글 삭제 완료  팝업 닫기
	$('#reply_delete_ok').click(function(){
		//scrollAble();
		$(this).parents('.popup').hide();
		getReplyList(0, "N");
	});
	//출석체크 이벤트 하단 버튼 클릭시 상세페이지 종료
	$("#attend_close").click(function(){
		window.open('','_self').close();
	});
	//경품선택 팝업창 닫기
	$('#giftChoice_popup_cancel').click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	//배송지 팝업 닫기
	$("#delivery_cancel").click(function(){
		var element_wrap = document.getElementById("wrapPost");
		element_wrap.style.display = 'none';
		$("#recvName").val("");
		$("#recvMobileNo").val("");
		$("#post").val("");
		$("#addr").val("");
		$("#addrDtl").val("");
		scrollAble();
		$('#giftChoice_popup_cancel').parents('.popup').hide();
		$(this).parents('.popup').hide();
	});
	//배송지 팝업 닫기
	$("#delivery_cover").click(function(){
	});
	//정보 미입력 팝업창 닫기
	$("#not_info_ok").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	
	//210615 투표대상 미선택 팝업창 닫기
	$("#not_vote_ok").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	
	//210706 투표완료 팝업창 닫기
	$("#only_vote_ok").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
	});
	
	//210706 투표완료 팝업창 닫기
	$("#complete_vote_ok").click(function(){
		//scrollAble();
		//$(this).parents('.popup').hide();
		$("#complete_vote_popup").hide();
	});
	
	//리워드함으로 이동
	$("#reward_move").click(function(){
		
		title ="리워드";
		
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
	
	//210820 외부링크로
	$("#link_go").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
		
	});
	
	//210915 응모권보유이벤트 경품소진시 외부링크로
	$("#soldout_link_go").click(function(){
		scrollAble();
		$(this).parents('.popup').hide();
		
	});
	
	
	//선착순 경품증정 팝업창(온라인) 닫은 후 상세페이지 종료
	$("#first_gift_cancel").click(function(){
		eventClose();
	});
	//선착순 경품증정 팝업창(온라인) 커버 클릭시 상세페이지 종료
	$("#first_gift_cover").click(function(){
	});
	//경품선택완료 팝업창(오프라인) 닫은 후 상세페이지 종료
	$("#giftChoice_complete_ok").click(function(){
		eventClose();
	});
	//경품선택완료 팝업창(오프라인) 커버 클릭시 상세페이지 종료
	$("#giftChoice_complete_cover").click(function(){
	});
	//경품소진 팝업창커버 클릭시 닫은 후 상세페이지 종료
	$("#onlyattend_complete_ok").click(function(){
		eventClose();
	});
	//경품소진 팝업창커버 클릭시 상세페이지 종료
	$("#onlyattend_complete_cover").click(function(){
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
		//if(evtType!='G0008'){
		//	location.reload();
		//}else{
			scrollAble();
			$(this).parents('.popup').hide();
		//}
	});
	//경품 없는 경우 팝업창 닫기
	$("#not_gift_cover").click(function(){
	});
	
	//출석체크 팝업창 확인 버튼 클릭시 리로드
	$("#attend_check_ok").click(function(){
		location.reload();
	});
	//출석체크 팝업창 배경 클릭시 리로드
	$("#attend_check_cover").click(function(){
	});
	
	$('.cover').click(function(){
	});
	
	//Y캔버스 완료 팝업창 닫은 후 상세페이지 종료
	$("#ycanvas_complete_ok").click(function(){
		var title ="리워드";
		var evtSeq="${evtSeq}";
		
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
				//window.YDataBox.getReload(evtSeq);
		}
	});
	
	//Y캔버스 완료 팝업창 닫은 후 상세페이지 종료
	$("#ycanvas_complete_cancel").click(function(){
		$('#ycanvas_complete_popup').hide();
	});
	
	
	//첫번째 라디오 버튼에 체크
	$("input:radio[name=giveaway]").eq(0).attr("checked",true).parents('li').addClass('on');
	//첫번째 라디오 체크된 텍스트값
	$('#data-resualt').text($("input:radio[name=giveaway]").eq(0).siblings('label').text());
	// 라디오 선택시 선택 라디오 해당 img opacity 조정 + 선택된 라디오로 텍스트 변경
	$('.radio-list-row input').click(function(){
		$(this).parents('li').addClass('on');
		$(this).parents('li').siblings().removeClass('on');
		$('#data-resualt').text($(this).siblings('label').text());
	});	
	
	//210616
	$('.check-wrap input').click(function(){
		$(this).parents('li').addClass('active');
		$(this).parents('li').siblings().removeClass('active');
	});	
	
	//경품선택 확인 버튼 클릭시 
	$("#giftChoice_complete").click(function(){
		var evtType="${eventDetail.evtType}";
		var giftChoiceType="${eventDetail.giftChoiceType}";	//경품 제공 방식 (선택,랜덤)
		var giftOfferType="${eventDetail.giftOfferType}";//O0003추첨, O0002선착순, O0001상품없음

		//준회원일경우, 주소 받는 경우
		if(memStatus=="G0003" && assoAddrYn == "Y"){
			var giftType = "${eventGiftList[0].giftType}";	//G0001오프라인, G0002온라인
			//타이틀 변경
			if(giftType=="G0002" || giftType == "G0003"){
				$(".f-yspotlightotapp").text("정보를 입력해 주세요");
			}else{
				$(".f-yspotlightotapp").text("배송지 정보를 입력해 주세요");
			}
			//고객정보 입력
			var remarkNm="${eventDetail.giftFormNm}";
			if(!isEmpty(remarkNm)){
				$("#remarks").attr("placeholder", remarkNm);
			}
		
			scrollDisble();
			$("#delivery_popup").show();
			
			return false;
		}
		
		if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
			var giftType=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftType]').val();
			if(giftType=="G0001"){	//오프라인상품 : 배송지 입력팝업
				//타이틀 변경
				$(".f-yspotlightotapp").text("배송지 정보를 입력해 주세요");
				//210701
				var remarkNm="${eventDetail.giftFormNm}";
				if(!isEmpty(remarkNm)){
					$("#remarks").attr("placeholder", remarkNm);
				}
				//210701 end
				
				scrollDisble();
				$("#delivery_popup").show();
				
				return false;
			}
			
			if(giftType=="G0002" || giftType == "G0003" ){	//온라인상품 || 응모권상품 : 배송지 입력 필요x
				var giftSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftSeq]').val();
				var issueSeq=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=issueSeq]').val();
				
				var eventGiftJoin={};
			
					eventGiftJoin={"evtSeq":evtSeq
								  ,"giftSeq":giftSeq
								  ,"issueSeq":issueSeq
								  };
			}
		}//경품 제공 방식 : 선택 end
		
		if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
			var flag=false;
			var length=$('input:hidden[name=giftType]').length;
				for(var i=0;i<length;i++){
					var giftType=$('input:hidden[name=giftType]').eq(i).val();
					if(giftType=="G0002" || giftType=="G0003"){//온라인 || 응모권
						flag=true;
					}
					if(giftType=="G0001"){//오프라인 상품이면 배송지 정보입력
						//타이틀 변경
						$(".f-yspotlightotapp").text("배송지 정보를 입력해 주세요");
						//210701
						var remarkNm="${eventDetail.giftFormNm}";
						if(!isEmpty(remarkNm)){
							$("#remarks").attr("placeholder", remarkNm);
						}
						//210701 end
						flag=false;
						scrollDisble();
						$("#delivery_popup").show();
						
					return flag;
					}
					
				}//for end
			if(flag==true){
				var eventGiftJoin={"evtSeq":evtSeq
								  ,"issueSeq": -1};
			}	
		}// 경품 제공 방식 : 랜덤
		
		//경품이벤트(온라인) ajax
		if(evtType=='G0002' || evtType=='G0008'/*210611 투표하기 추가*/){
			if(doubleSubmitCheck()) return;
			
			if(giftType=="G0002"){
			  	$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "giftRewardJoin",
					data : eventGiftJoin,
					success : function(data){
						
						console.log("data : "+JSON.stringify(data));
						console.log("resultData : "+JSON.stringify(data.resultData));
			
						if(data.resultCd=="200"){
							
							if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
								//추첨 경품일시
								if(giftOfferType=="O0003"){
									    scrollDisble();
										$("#giftChoice_complete_popup").show();
								}
								//선착순/무제한 경품일시
								if(giftOfferType=="O0002"){
									if(giftType=="G0002"){	//온라인상품 : G0002
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
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#not_gift_popup").show();
						}
						
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End  
			
			//20210811 응모권 경품 타입 추가 시작
			}else if(giftType == "G0003"){
			  	$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "giftTicketRewardJoin",
					data : eventGiftJoin,
					success : function(data){
						
						console.log("data : "+JSON.stringify(data));
						console.log("resultData : "+JSON.stringify(data.resultData));
			
						if(data.resultCd=="200"){
							
							if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
								//추첨 경품일시
								if(giftOfferType=="O0003"){
									    scrollDisble();
										$("#giftChoice_complete_popup").show();
								}
								//선착순/무제한 경품일시
								if(giftOfferType=="O0002"){
									
									//응모권 이미지 위치 정해지면 수정
									$("#gift_url").attr("src",data.resultData.imgUrl);
									$("#gift_name").text(data.resultData.giftName);
									
									scrollDisble();
									$("#first_gift_popup").show();
								}
								
							}
							if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
									scrollDisble();
									$("#random_complete_popup").show();
							}
							
						}else{
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#not_gift_popup").show();
						}
						
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End  
			}
			//20210811 응모권 경품 타입 추가 끝
			
		  	doubleSubmitFlag = false;
		}
		
		//출석 이벤트(리워드 경품)ajax
		if(evtType=="G0005" || evtType=="G0010"){
			if(doubleSubmitCheck()) return;
			
			if(giftType=="G0002"){
				$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "attendRewardJoin",
					data : eventGiftJoin,
					success : function(data){
							console.log("data : "+JSON.stringify(data));
							console.log("data.resultData : "+JSON.stringify(data.resultData));
							console.log("data.resultCd : "+data.resultCd);
						//성공시
						if(data.resultCd=="200"){
							if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
								
								//경품소진되었을때 출석체크만 되고 팝업띄워줌
								if(data.resultData.onlyAttend == "Y"){
									scrollDisble();
									$("#only_attend_popup").show();
								}else{
									//선착순/무제한 경품일시
									if(giftOfferType=="O0002"){
										$("#gift_url").attr("src",data.resultData.imgUrl);
										$("#gift_name").text(data.resultData.giftName);
										scrollDisble();
										$("#first_gift_popup").show();
									}
									
									//추첨 경품일시
									if(giftOfferType=="O0003"){
										scrollDisble();
										$("#giftChoice_complete_popup").show();
									}
								}
								
							}
							
							if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
								scrollDisble();
								$("#random_complete_popup").show();
							}
						}//성공시 end
						else{
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#not_gift_popup").show();
						}
					},	//success end
					error : function(error){
						console.log(error);
					}
				}); //ajax End
			
			//20210811 응모권 경품 타입 추가 시작
			}else if(giftType=="G0003"){
				$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "attendTicketRewardJoin",
					data : eventGiftJoin,
					success : function(data){
							console.log("data : "+JSON.stringify(data));
							console.log("data.resultData : "+JSON.stringify(data.resultData));
							console.log("data.resultCd : "+data.resultCd);
						//성공시
						if(data.resultCd=="200"){
							if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
								
								//경품소진되었을때 출석체크만 되고 팝업띄워줌
								if(data.resultData.onlyAttend == "Y"){
									scrollDisble();
									$("#only_attend_popup").show();
								}else{
									//선착순/무제한 경품일시
									if(giftOfferType=="O0002"){
										
										//응모권이미지 어디에 적재할지 정해지면 사용
										$("#gift_url").attr("src",data.resultData.imgUrl);
										$("#gift_name").text(data.resultData.giftName);
										
										scrollDisble();
										$("#first_gift_popup").show();
									}
									
									//추첨 경품일시
									if(giftOfferType=="O0003"){
										scrollDisble();
										$("#giftChoice_complete_popup").show();
									}
								}
								
							}
							
							if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
								scrollDisble();
								$("#random_complete_popup").show();
							}
						}//성공시 end
						else{
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#not_gift_popup").show();
						}
					},	//success end
					error : function(error){
						console.log(error);
					}
				}); //ajax End
			}
			//20210811 응모권 경품 타입 추가 끝

		}
		
	});
	
	//전화번호 입력시 숫자만 입력
	$("#recvMobileNo").bind("keyup",function(event){
		var regNumber = /^[0-9]*$/;
		var temp = $("#recvMobileNo").val();
		if(!regNumber.test(temp)){
			$("#recvMobileNo").val(temp.replace(/[^0-9]/g,""));
		}
	});
	//오프라인 상품 : 배송지 입력후 ajax 
	$("#delivery_complete").click(function(){
		
		var evtType="${eventDetail.evtType}";
		var giftChoiceType="${eventDetail.giftChoiceType}";	//경품 제공 방식 (선택,랜덤)C0001:선택,  C0002:랜덤
		var giftOfferType="${eventDetail.giftOfferType}";//O0003선착순, O0002추첨, O0001상품없음
		
		var recvName=$("#recvName").val();			//사용자 이름
		var recvMobileNo=$("#recvMobileNo").val();	//사용자 전화번호
		var post=$("#post").val();					//우편번호
		var addr=$("#addr").val();;					//주소
		var addrDtl=$("#addrDtl").val();;			//상세 주소
		var remarks=$("#remarks").val();;			//비고
		
		var addrFormYn="${eventDetail.giftAddrYn}";

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
					  		  ,"remarks":remarks};
			
			if(addrFormYn=="Y"){
				
				if(recvName.trim()=="" || recvMobileNo.trim()=="" || post.trim()=="" || addr.trim()=="" || addrDtl.trim()==""){
					scrollDisble();
					$("#not_info_popup").show();
					return false;
				}
			}else{
				
				if(recvName.trim()=="" || recvMobileNo.trim()==""){
					scrollDisble();
					$("#not_info_popup").show();
					return false;
				}
			}
		}
		
		if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
			var eventGiftJoin={"evtSeq":evtSeq
							  ,"issueSeq": -1				
			  		  		  ,"recvName":recvName
			  		  		  ,"recvMobileNo":recvMobileNo
			  	 	  		  ,"post":post
			  	 	  		  ,"addr":addr
			  		  		  ,"addrDtl":addrDtl
			  		  		  ,"remarks":remarks};
		}

		//준회원일 경우 온라인, 오프라인 상품 전부 해당 자리에서 처리
		if(memStatus=="G0003" && assoAddrYn=="Y"){
			var giftType=$("input:radio[name=giveaway]:checked").siblings('input:hidden[name=giftType]').val();
			//오프라인상품
			if(giftType=="G0001"){
				if(doubleSubmitCheck()) return;
				$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "giftOfflineJoin",
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
							$("#not_gift_popup").show();
						}
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End 
				doubleSubmitFlag = false;
			//온라인상품 || 응모권상품
			}else if(giftType=="G0002" || giftType == "G0003"){
				if(doubleSubmitCheck()) return;
				$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "giftRewardJoin",
					data : eventGiftJoin,
					success : function(data){
						
						console.log("data : "+JSON.stringify(data));
						console.log("resultData : "+JSON.stringify(data.resultData));
			
						if(data.resultCd=="200"){
							
							if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
								//추첨 경품일시
								if(giftOfferType=="O0003"){
									    scrollDisble();
										$("#giftChoice_complete_popup").show();
								}
								//선착순/무제한 경품일시
								if(giftOfferType=="O0002"){
									if(giftType=="G0002"){	//온라인상품 : G0002
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
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#not_gift_popup").show();
						}
						
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End
				doubleSubmitFlag = false;
			}
			return;
		}else{
			//경품이벤트(오프라인) ajax
			if(evtType=='G0002' || evtType=='G0008' /*210614 투표하기 추가*/){
				if(doubleSubmitCheck()) return;
				$.ajax({
					type : "post",
					headers : {
						cntrNo : $('[name=cntrNo]').val(),
						ysid : $('[name=ysid]').val(),
						autoLogin : $('[name=autoLogin]').val(),
						osTp : $('[name=osTp]').val(),
						appVrsn : $('[name=appVrsn]').val()
					},
					url : "giftOfflineJoin",
					data : eventGiftJoin,
					success : function(data){
						if(data.resultCd=="200"){
							console.log("data : "+JSON.stringify(data));
							console.log("data.resultData : "+JSON.stringify(data.resultData));
							console.log("data.resultCd : "+data.resultCd);
						
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
							$("#not_gift_popup").show();
						}
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End 
				doubleSubmitFlag = false;
			}
		}
		
		//출석 이벤트(오프라인)ajax
		if(evtType=="G0005" || evtType=="G0010"){
			if(doubleSubmitCheck()) return;
			$.ajax({
				type : "post",
				headers : {
					cntrNo : $('[name=cntrNo]').val(),
					ysid : $('[name=ysid]').val(),
					autoLogin : $('[name=autoLogin]').val(),
					osTp : $('[name=osTp]').val(),
					appVrsn : $('[name=appVrsn]').val()
				},
				url : "attendOfflineJoin",
				data : eventGiftJoin,
				success : function(data){
					console.log("data : "+JSON.stringify(data));
					console.log("data.resultData : "+JSON.stringify(data.resultData));
					console.log("data.resultCd : "+data.resultCd);
				
					if(data.resultCd=="200"){
							//경품소진되었을때 출석체크만 되고 팝업띄워줌
							if(data.resultData.onlyAttend == "Y"){
								scrollDisble();
								$("#only_attend_popup").show();
							}else{
								if(giftChoiceType=="C0001"){	//경품 제공 방식 : 선택
									scrollDisble();
									$("#giftChoice_complete_popup").show();
								}
								
								if(giftChoiceType=="C0002"){	//경품 제공 방식 : 랜덤
									scrollDisble();
									$("#random_complete_popup").show();
								}
							}
					}//200 end
					else{
						$("#error_msg").text(data.resultMsg);	//에러 메세지
						scrollDisble();
						$("#not_gift_popup").show();
					}
				},	//success end
				error : function(error){
					console.log(error);
				}
			});	//ajax end
		}
	});	//오프라인 상품 : 배송지 입력후 ajax end
	
	
	//221114 댓글 불러오기
	$('#replyList').click(function(){
		
		var replyYn="${eventDetail.replyYn}";
		
		if(replyYn == "Y"){
			scrollDisble();
			
			$("#AddComment").show();
			
			getAdminReplyList("Y");
		}else{
			return;
		}
	});	
	
	
	//221114 댓글 더보기
	$('#replyMore').click(function(){
		
		var replyEndSeq = $('#replyMore').data.replyEndSeq;
		
		getReplyList(replyEndSeq, "N");
		
	});	
	
	
	//221119 댓글 달기
	$("#replyWrite").on("mousedown", function(event) {
		event.preventDefault();
		}).on("click", function() {
			
			if(loginYn=="Y"){
				writeReply();

			}else{
				scrollDisble();
				$("#notLogin2").show();
			}
		});
	
	//221119 댓글 수정
	$("#replyModify").on("mousedown", function(event) {
		event.preventDefault();
		}).on("click", function() {
			
			if(loginYn=="Y"){
				$("#ModifyComment").data.contents=$("#replyText").val();
				$("#ModifyComment").show();

			}else{
				scrollDisble();
				$("#notLogin2").show();
			}
		});
	
	
	$('.slider').slick({
		slidesToShow: 1,
		slidesToScroll: 1,
		autoplay: false,
		arrows:true,
		infinite: false,
		dots: true,
		arrows:false
	});
	// 슬라이더 하단 너비 구하기
	$(".slide-wrap").each(function(index){
		var sliderNavigationWidth = 100 / Number( $(this).find('.slick-dots > li').length );
		$(this).find('.slick-dots > li').css("width", sliderNavigationWidth + "%" );
		if($(this).find('.slick-dots > li').length == 1){
			$(this).find('.slick-dots').css("display", "none");
		}
	});
	//var sliderNavigationWidth = 100 / Number( $('.slick-dots > li').length );
	//$('.slick-dots > li').css("width", sliderNavigationWidth + "%" );
});	//ready End

var writeClicked = false;

function writeReply() {
	
	var evtSeq="${evtSeq}";
	var contents = $("#replyText").val();

	var reply = {
			"evtSeq":evtSeq,
			"contents":contents
	};
	
	if(!writeClicked){
		
		writeClicked = true;
		
		$.ajax({
			type : "post",
			headers : {
				ysid : $('[name=ysid]').val(),
				autoLogin : $('[name=autoLogin]').val(),
				osTp : $('[name=osTp]').val(),
				appVrsn : $('[name=appVrsn]').val()
			},
			url : "eventReplyWrite",
			data : reply,
			success : function(data){
					console.log("data : "+JSON.stringify(data));
					console.log("data.resultData : "+JSON.stringify(data.resultData));
					console.log("data.resultCd : "+data.resultCd);
				//성공시
				if(data.resultCd=="200"){
		
					$("#replyText").val("");
					$("#replyText").blur();
					getReplyList(0, "Y");
					
				}//성공시 end
				else{
					$("#error_msg").text(data.resultMsg);	//에러 메세지
					scrollDisble();
				}
			},	//success end
			error : function(error){
				console.log(error);
			}
		}); //ajax End
	}
	
	
}

function getReplyList(replyEndSeq, scrollTopYn){

	var evtSeq="${evtSeq}";
	var loginYn="${loginYn}";
	var memStatus="${memStatus}"
	var cntrNo="${cntrNo}";
	var replyUL = $(".comment-wrap");
	var adminReplyUL = document.querySelector('#admin-reply-box').innerHTML;
	
	$.ajax({
		type : "get",
		url : "eventReplyList",
		data : {"evtSeq":evtSeq, "endSeq":replyEndSeq},
		success : function(data){

			//성공시
			if(data.resultCd=="200"){

				var str = "";
				
				var list = data.resultInfoList;
				
				if ((list == null || list.length == 0) && (adminReplyUL == null || adminReplyUL == '')){
					$("#replyMore").hide();
					
					str += "<div class='comment-none-item'>";
					str += "<img class='img' src=\"/yapp3/res/images/ic_y_comment_none.png\" alt='댓글없음'>";
					str += "<dl><dt>등록된 댓글이 없어요.</dt><dd>처음으로 댓글을 달아보세요.</dd></dl></div>";
					replyUL.html(str);
					return;
					
					}else if (list.length>30){
						$("#replyMore").show();
						$("#replyMore").data.replyEndSeq = list[29].replySeq;
					}else if (list.length<31){
						$("#replyMore").hide();
					}
					
				
				for(var i=0, len = list.length || 0; i<len; i++){
					if(i>=30){
						break;
					}
					
					var contents = replaceBrTag(list[i].contents);
					var blockYn = list[i].blockYn;
					
					if(blockYn == "Y"){
						contents = "관리자에 의해 숨김 처리된 댓글입니다.";
					}
					
					if(loginYn == "Y" && blockYn != "Y"){
						
						if(memStatus == "G0003"){

							if("Y" == list[i].meYn){
								str += "<div class='comment-item manager' data-replySeq='"+list[i].replySeq+"'><div class='info'><div class='col'>";
								str += "<span class='category'>작성자</span>"
								str += "<strong class='name'>"+list[i].name+"</strong>";
								str += "<span class='date'>"+displayTime(list[i].regDt)+"</span></div>";
								str += "<div class='col'>";
								str += "<button type='button' class='btn-gray' onclick='replyModify("+list[i].replySeq+",\""+contents+"\")'>수정</button>";									
								str += "<button type='button' class='btn-gray' onclick='replyDelete("+list[i].replySeq+")'>삭제</button></div></div>";								
								str += "<div class='comment'>";
								str += contents+"</div></div>";
							}else{
								str += "<div class='comment-item'><div class='info'><div class='col'>";
								str += "<strong class='name'>"
								str += list[i].name+"</strong>";
								str += "<span class='date'>";
								str += displayTime(list[i].regDt)+"</span></div></div>";
								str += "<div class='comment'>";
								str += contents+"</div></div>";
							}
						}else{
							
							if("Y" == list[i].meYn){
								str += "<div class='comment-item manager' data-replySeq='"+list[i].replySeq+"'><div class='info'><div class='col'>";
								str += "<span class='category'>작성자</span>"
								str += "<strong class='name'>"+list[i].name+"</strong>";
								str += "<span class='date'>"+displayTime(list[i].regDt)+"</span></div>";
								str += "<div class='col'>";
								str += "<button type='button' class='btn-gray' onclick='replyModify("+list[i].replySeq+",\""+contents+"\")'>수정</button>";									
								str += "<button type='button' class='btn-gray' onclick='replyDelete("+list[i].replySeq+")'>삭제</button></div></div>";								
								str += "<div class='comment'>";
								str += contents+"</div></div>";
							}else{
								str += "<div class='comment-item'><div class='info'><div class='col'>";
								str += "<strong class='name'>"
								str += list[i].name+"</strong>";
								str += "<span class='date'>";
								str += displayTime(list[i].regDt)+"</span></div></div>";
								str += "<div class='comment'>";
								str += contents+"</div></div>";
							}
						}
						
					}else{
						/* str += "<div class='comment-item manager' data-replySeq='"+list[i].replySeq+"'><div class='info'><div class='col'>";
						str += "<span class='category'>작성자</span>"
						str += "<strong class='name'>01099999999</strong>";
						str += "<span class='date'>"+displayTime(list[i].regDt)+"</span></div>";
						str += "<div class='col'>";
						str += "<button type='button' class='btn-gray' onclick='replyModify("+list[i].replySeq+",\""+contents+"\")'>수정</button>";									
						str += "<button type='button' class='btn-gray' onclick='replyDelete("+list[i].replySeq+")'>삭제</button></div></div>";								
						str += "<div class='comment'>";
						str += contents+"</div></div>"; */
						
						str += "<div class='comment-item'><div class='info'><div class='col'>";
						str += "<strong class='name'>"
						str += list[i].name+"</strong>";
						str += "<span class='date'>";
						str += displayTime(list[i].regDt)+"</span></div></div>";
						str += "<div class='comment'>";
						str += contents+"</div></div>";
						
					}
				}

				if(replyEndSeq == 0){
					replyUL.html(str);
				}else{
					replyUL.append(str);
				}
				
				if(scrollTopYn == "Y"){
					$(".pop-con").scrollTop(0);
				}
				
				writeClicked = false;
				
			}//성공시 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
			}
		},	//success end
		error : function(error){
			console.log(error);
		}
	}); //ajax End
}

//댓글 수정
function replyModify(replySeq, contents){
	
	con = unreplaceBrTag(contents);

	$("#ModifyComment").data.replySeq=replySeq;
	$("#replyText").focus();
	$("#replyText").val(con);
	
	$("#replyLimit").text('('+con.length+'/100)');
	$("#replyWrite").hide();
	$("#replyModify").show();
	$("#replyLimit").show();
}


//댓글 삭제
function replyDelete(replySeq){
	
	$("#DeleteComment").data.replySeq=replySeq;
	$("#DeleteComment").show();
	
}

//230320 운영자 댓글 리스트 조회
function getAdminReplyList(scrollTopYn){

	var evtSeq="${evtSeq}";
	var replyUL = document.querySelector('.comment-wrap').innerHTML;
	var adminReplyUL = $("#admin-reply-box");
	
	$.ajax({
		type : "get",
		url : "eventAdminReplyList",
		data : {"evtSeq":evtSeq},
		success : function(data){
			//성공시
			if(data.resultCd=="200"){

				var str = "";
				
				var list = data.resultInfoList;
				
				if(list == null || list.length == 0){
					adminReplyUL.html(str);
				}
				
				for(var i=0, len = list.length || 0; i<len; i++){
			
					var contents = replaceBrTag(list[i].contents);
					var blockYn = list[i].blockYn;
					
					if(blockYn == "Y"){
						contents = "관리자에 의해 숨김 처리된 댓글입니다.";
					}
					
					str += "<div class='comment-item admin-comment'><div class='info'><div class='col'>";
					str += "<span class='category'>운영자</span>"
					str += "<strong class='name'>"
					str += list[i].name+"</strong>";
					str += "<span class='date'>";
					str += displayTime(list[i].regDt)+"</span></div></div>";
					str += "<div class='comment'>";
					str += contents+"</div></div>";
					
					adminReplyUL.html(str);
				}
				
				getReplyList(0, "Y");
				
				if(replyUL != ''){
					$("#admin-reply-box .comment-item:last-child").css("margin-bottom", "1.25rem");
				}	
				
							
			}//성공시 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
			}
		},	//success end
		error : function(error){
			console.log(error);
		}
	}); //ajax End
}

function btnDelete() {
	
	$("#DeleteComment").css("display", "none");
	
	var replySeq = $("#DeleteComment").data.replySeq;
	
	$.ajax({
		type : "post",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "eventReplyModify",
		data : {"replySeq":replySeq, "delYn":'Y'},
		success : function(data){
			console.log("data : "+JSON.stringify(data));
			console.log("data.resultData : "+JSON.stringify(data.resultData));
			console.log("data.resultCd : "+data.resultCd);
		
			if(data.resultCd=="200"){
				$("#reply_delete_popup").show();
			}//200 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
			}
		},	//success end
		error : function(error){
			console.log(error);
		}
	});	//ajax end
}

function btnModify() {
	
	$("#ModifyComment").css("display", "none");
	
	var replySeq = $("#ModifyComment").data.replySeq;
	var contents = $("#ModifyComment").data.contents;
	
	$.ajax({
		type : "post",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "eventReplyModify",
		data : {"replySeq":replySeq, "contents":contents},
		success : function(data){
			console.log("data : "+JSON.stringify(data));
			console.log("data.resultData : "+JSON.stringify(data.resultData));
			console.log("data.resultCd : "+data.resultCd);
		
			if(data.resultCd=="200"){
				$("#replyText").val("");
				$("#reply_modify_popup").show();
			}//200 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
			}
		},	//success end
		error : function(error){
			console.log(error);
		}
	});	//ajax end
}

function btnModifyCancel() {
	$("#ModifyComment").css("display", "none");
}

function btnDeleteCancel() {
	$("#DeleteComment").css("display", "none");
}

function getIconCountInfo() {
	
	var evtSeq="${evtSeq}";
	var replyYn="${eventDetail.replyYn}";
	
	$.ajax({
		type : "get",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "eventIconCount",
		data : {"evtSeq":evtSeq},
		success : function(data){
			console.log("data : "+JSON.stringify(data));
			console.log("data.resultData : "+JSON.stringify(data.resultData));
			console.log("data.resultCd : "+data.resultCd);
		
			if(data.resultCd=="200"){

				var replyCnt = data.resultData.replyCnt;
				var likeCnt = data.resultData.likeCnt;
				var likeSeq = data.resultData.likeSeq;
				var likeYn = data.resultData.likeYn;
				
				if(replyYn=="Y") {
					$("#replyList").text(replyCnt);
					$("#replyList").css({
						"background-image": "url(/yapp3/res/images/ic_message.png)",
						"color":"#111"
					});
				}else{
					$("#replyList").css({
						"background-image": "url(/yapp3/res/images/ic_message_gray.png)",
						"color":"transparent"
					});
				}
				
				if(likeYn == "Y"){
					$("#likeBtn").css('background-image', 'url(/yapp3/res/images/ic_heart_fill.png)');
				}else{
					$("#likeBtn").css('background-image', 'url(/yapp3/res/images/ic_heart.png)');
				}
				
				$("#likeBtn").text(likeCnt);
				$("#replyWrite").attr("disabled",true);
				
				likeClicked = false;
				
			}//200 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
			}
		},	//success end
		error : function(error){
			console.log(error);
		}
	});	//ajax end
	
}

//카카오톡 공유하기
//Kakao.init('6786153c7093f6e5667ab1e7436de0ad');
Kakao.init('8f93b450f7cbc600605aa40c59c7e7fa');
function sendLink(){
	var evtSeq="${evtSeq}";
	var host = window.location.host;
	var imgUrl="${eventDetail.evtDtlTopImg}";
	var evtTitle=$("#evtTitle").val();
	var evtSmallTitle="${eventDetail.evtSmallTitle}";
	var evtTypeNm="${eventDetail.evtTypeNm}";
	var currentUrl="https://"+host+"/yapp3/event/evtDetail?evtSeq="+evtSeq+"&evtTypeNm="+evtTypeNm;	//yboxapp://eventDetail?evtSeq="+evtSeq
	//var encodeUrl=encodeURIComponent(currentUrl);
 	Kakao.Link.sendDefault({		// Kakao.Link.sendDefault : 직접 만든 버튼으로 메시지 보내기
		objectType : "feed",
		installTalk : true,
		content : {
			title : evtTitle,
			description : evtSmallTitle,
			imageUrl : imgUrl,
			link : {
				mobileWebUrl : currentUrl,		
				webUrl : currentUrl		
				//androidExecParams : "test",
			}
		},
		buttons : [
			{
				title : "자세히 보기",
				link : {
					mobileWebUrl : currentUrl,	
					webUrl : currentUrl		
				}
			}
		]
	}); 
}

//sns 공유하기
function shareAct(a){
	var evtSeq="${evtSeq}";
	var evtTypeNm="${eventDetail.evtTypeNm}";
	var snsCode = $(a).attr("id");
	var host = window.location.host;
	var currentUrl = "https://"+host+"/yapp3/event/evtDetail?evtSeq="+evtSeq+"&evtTypeNm="+evtTypeNm;	//yboxapp://eventDetail?evtSeq="+evtSeq
	//var encodeUrl=encodeURIComponent(currentUrl);
	switch(snsCode){
		
		/* case "kakaotalk" :
			//카카오톡
			currentUrl = "kakaotalk";
		break; */
		
		case "line" : 
			currentUrl = "https://social-plugins.line.me/lineit/share?url="+currentUrl;
		break;
		
		case "facebook" : 
			currentUrl = "http://www.facebook.com/sharer/sharer.php?u="+currentUrl;
		break;
	}
	window.open(currentUrl,'','scrollbars=yes');
}

//단말 분류
function isIOS(){
	var userAgent = navigator.userAgent.toLowerCase();
	
	return ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1) || (userAgent.search("ipad") > -1));
}

//로그인안된경우 로그인 페이지 이동
function login_move(){
		var postData = { gm : '1' };
		
		if (isIOS()){
			window.webkit.messageHandlers.callBackHandler.postMessage(postData);
		}else{
			window.YDataBox.moveLoginPage();
		}	
}

//배송지 입력후 이벤트 상세 페이지 닫기
function eventClose(){
		var postData = { gm : '4' };
		
		if (isIOS()){
			window.webkit.messageHandlers.callBackHandler.postMessage(postData);
		}else{
			window.YDataBox.closeWebViewPage();
		}	
}

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

var likeClicked = false;

function clickLike(){
	
	var loginYn="${loginYn}";
	
	if(loginYn=="N"){
		login_move();
		
	}else{
		
		if(!likeClicked){
			
			likeClicked = true;
			
			var evtSeq="${evtSeq}";
			
			$.ajax({
				type : "post",
				headers : {
					ysid : $('[name=ysid]').val(),
					autoLogin : $('[name=autoLogin]').val(),
					osTp : $('[name=osTp]').val(),
					appVrsn : $('[name=appVrsn]').val()
				},
				url : "eventLikeWrite",
				data : {"evtSeq":evtSeq},
				success : function(data){
						console.log("data : "+JSON.stringify(data));
						console.log("data.resultData : "+JSON.stringify(data.resultData));
						console.log("data.resultCd : "+data.resultCd);
					//성공시
					if(data.resultCd=="200"){
						getIconCountInfo();
						
					}//성공시 end
					else{
						$("#error_msg").text(data.resultMsg);	//에러 메세지
						scrollDisble();
					}
				},	//success end
				error : function(error){
					console.log(error);
				}
			}); //ajax End
		}		
	}	
}

function cancelComment(){
	$("#replyText").val("");
	$("#AddComment").hide();
	getIconCountInfo();
	scrollAble();
	
}

function cancelClass(){
	$("#YcanvasClass").hide();
	scrollAble();
}

function btnApplyCancel(){
	$("#ApplyYcanvasClassPopup").hide();
	scrollAble();
}

//개행처리
function replaceBrTag(str) {
	if(str == undefined || str == null){
		return;
	}
	
	str = str.replace(/\r\n/ig, '<br>');
	str = str.replace(/\\n/ig, '<br>');
	str = str.replace(/\n/ig, '<br>');
	
	return str;
}

//개행풀기
function unreplaceBrTag(str) {
	if(str == undefined || str == null){
		return;
	}
	
	str = str.replace(/<br>/ig, '\n');
	str = str.replace(/<\/br>/ig, '\n');
	str = str.replace(/<br \>/ig, '\n');
	
	return str;
}

//댓글 시간 표기
function displayTime(date) {
	
	var dateObj = new Date(date);
	
	var yy = dateObj.getFullYear();
	var mm = dateObj.getMonth()+1;
	var dd = dateObj.getDate();
	var hh = dateObj.getHours();
	var mi = dateObj.getMinutes();
	
	return [yy, '/', (mm>9?'':'0')+mm, '/',(dd>9?'':'0')+dd,' ', (hh>9?'':'0') + hh, ':',(mi>9?'':'0')+mi].join('');
	
}

//버전 비교
function compareVersion(verA, verB) {
	
	var result = false;
	
	verA = verA.split( '.');
	verB = verB.split( '.');
	
	console.log("verA : ", verA);
	console.log("verB : ", verB);
	
	const length = Math.max( verA.length, verB.length );
	
	for ( var i =0; i< length; i++){
		
		var a =verA[i] ? parseInt(verA[i], 10) : 0;
		var b =verB[i] ? parseInt(verB[i], 10) : 0;
		
		if (a>b){
			result = true;
			break;
		}
	}
	console.log("compareVersion : "+result);
	return result;
}

//수강 상품 신청 팝업 실행
function applyYcanvasPopup(clsSeq, issueSeq, rewSeq, imgUrl, giftSeq, maxClassCnt, clsTitle){
	//비활성화 되어있는 버튼 갯수 확인하여 신청 불가 처리 및 팝업 문구
	var len = $('button[id^=ycanvas_btn_]:contains("신청완료")').length;

	if(maxClassCnt < len){
		$('#ycanvas_alert_text').text('해당 클래스를 신청 하실 수 없습니다. ');
		$('#ycanvas_alert_text').append('<br/>Y캠퍼스 클래스 신청은 ');
		$('#ycanvas_alert_text').append('<br/>최대 '+maxClassCnt+'개 가능합니다. ');
		$('#ycanvas_alert_text').append('<br/>리워드를 확인해주세요.');
		$('#not_class_limit_popup').show();
		return;
	}else{
		$('#ycanvas_complete_text').text(clsTitle + '이 신청되었습니다.');
		$('#ycanvas_complete_text').append('<br/>Y캠퍼스 클래스 신청은 최대 '+maxClassCnt+'개 가능합니다.');
	}
	
	$('#ycanvas_complete_text').text(clsTitle + '이 신청되었습니다.');
	$('#ycanvas_complete_text').append('<br/>Y캠퍼스 클래스 신청은 최대 '+maxClassCnt+'개 가능합니다.');
	
	$('#clsSeq').val(clsSeq);
	$('#clsIssueSeq').val(issueSeq);
	$('#clsRewSeq').val(rewSeq);
	$('#imgUrl').val(imgUrl);
	$('#giftSeq').val(giftSeq);
	$('#maxClassCnt').val(maxClassCnt);
	$('#clsTitle').val(clsTitle);
	$('#ApplyYcanvasClassPopup').show();
	scrollAble();
}

//수강 상품 정보 apply ajax
function btnApply(){
	
	var evtSeq="${evtSeq}";
	var clsSeq=$('#clsSeq').val();
	var clsIssueSeq=$('#clsIssueSeq').val();
	var clsRewSeq=$('#clsRewSeq').val();
	var imgUrl=$('#imgUrl').val();
	var maxClassCnt=$('#maxClassCnt').val();
	var giftSeq=$('#giftSeq').val();
	$('#ycanvas_cmpl_img').attr("src", imgUrl);

	//수강 신청 팝업 확인버튼 비활성화 처리
	$('#btnApply').attr('disabled', true);
	
	$.ajax({
		type : "post",
		headers : {
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "yCanvasRewardJoin",
		data : {"evtSeq":evtSeq,"clsSeq":clsSeq,"issueSeq":clsIssueSeq,"rewSeq":clsRewSeq,"giftSeq":giftSeq,"maxClassCnt":maxClassCnt},
		success : function(data){
			//성공시
			if(data.resultCd=="200"){
				//Y캔버스 상품 DOM 변경
				$.each(data.resultData.ycanvasItem, function(idx, item){
					//상품 수량이 없을경우
					if(item.issueCnt>=item.giftCnt){
						//소진이미지로 표출
						$("#ycanvas_img_"+item.clsSeq).attr("src", item.imgUrl);
						$("#ycanvas_img_"+item.clsSeq).css({"filter": "brightness(0.35)"});
						$("#ycanvas_img_"+item.clsSeq).after('<div id="class_deadline"><img id="deadline_image" src="/yapp3/res/img/y_class_deadline.png" alt=""></div>');
						
					}
					//상품수량 변경
					$("#ycanvas_gift_cnt_"+item.clsSeq).text("("+item.issueCnt+"/"+item.giftCnt+")");
					//버튼 문구 변경
					//$("#ycanvas_btn_"+clsSeq).text("신청완료");
					//완료 팝업
					$('#ycanvas_complete_popup').show();
					btnApplyCancel();
				});
				//버튼 비활성화 처리
				$("#ycanvas_btn_"+clsSeq).css({"display":"none"});
				$("#ycanvas_btn_"+clsSeq).after('<button id="ycanvas_btn_${clsSeq}" type="button" style="color:#ffffff !important" class="btn-upload-comment join" disabled="disabled">신청완료</button>');

				$('#btnApply').attr('disabled', false); 
			}//성공시 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
				$('#ApplyYcanvasClassPopup').hide();
				$('#not_gift_popup').show();
				//수강 신청 팝업 확인버튼 활성화 처리
				$('#btnApply').attr('disabled', false);
			}
		},	//success end
		error : function(error){
			console.log(error);
			//수강 신청 팝업 확인버튼 활성화 처리
			$('#btnApply').attr('disabled', false);
		}
	}); //ajax End
}
</script>
</head>
<body>

<div class="content">
		<div class="eve-visual" style="background-image:url(${eventDetail.evtDtlTopImg})">
			<div class="clearfix">
				<a href="javascript:void(0);" class="round-line-btn btn-white btn-sm" onfocus="blur()">${eventDetail.evtTypeNm}</a>
				<a href="javascript:void(0);" id="share_btn" class="btn" onfocus="blur()"><img src="/yapp3/res/img/ic_s_share.png" alt="공유하기"></a>
			</div>
			<div class="tit-wrap clearfix">
				<% pageContext.setAttribute("enter", "\n"); %>
				<c:set var="evtTitle" value="${fn:replace(eventDetail.evtTitle, enter, ' ' )}"/>
				<input type="hidden" id="evtTitle" value="${evtTitle}"/>
				<h2 id="h2Tit" class="tit f-yspotlightotapp" style="color:${eventDetail.btitleColorCode };">${fn:replace(eventDetail.evtTitle, enter, "<br/>" )}</h2>
				<p style="color:${eventDetail.stitleColorCode };">${eventDetail.evtSmallTitle}</p>
				<p class="sub-txt">${eventDetail.evtStartDt} - ${eventDetail.evtEndDt}</p>
			</div>
		</div>
			<!--일반 이벤트 (G0001), 매거진 (G0003), Y소식 (G0006)  -->
			<c:if test = "${eventDetail.evtType eq 'G0001' or eventDetail.evtType eq 'G0003' or eventDetail.evtType eq 'G0006' }">
				<c:set var="listIdx" value="0"/>
				<c:forEach var="list" items="${eventContentList}" varStatus="listStatus">
					<div class="layout">
						<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
								<!--텍스트(대)  -->
								<p id="big_txt" class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>	
								<!--텍스트(대)  -->
						</c:if>
					
						<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
								<!--텍스트(중)  -->
								<p class="font_m layout-div" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(중)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
								<div class="layout-div">
									<!--텍스트(소)  -->
									<p style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
									<!--텍스트(소)  -->
								</div>
						</c:if>
		
						<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
							<div class="line"></div>
						</c:if>
		
					</div>
					<!-- 멀티 이미지  -->
					<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
					<div class="swiper-div"> 
						<div class="swiper-container swiper-wrap"> 
				 			<div class="swiper-wrapper"> 
				 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
				 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
					 				<img src="${item }" style="margin-bottom:3%;" alt="">
					 			</div>
				 			</c:forTokens>
							</div>
							<div class="swiper-scrollbar"></div> 
						</div>
					</div>
					<c:set var="listIdx" value="${listIdx + 1}"/>
					</c:if> 
					<!-- 멀티 이미지  -->
					
					<!-- s:자세히보기 링크 추가 -->						
					<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
						<div class="layout">
							<div class="link_box layout-div">
								<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
									<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
									<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
									<dl>
										<dt>${fn:split(list.conDtl,'|')[2]}</dt>
										<dd>자세히 보러가기</dd>
									</dl>
								</a>
							</div>
						</div>
					</c:if>					
					<!-- e:자세히보기 링크 추가 -->					
					
					<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
					<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
						<div class="row-scroll-wrap">
							<ul class="row-scroll">
									<li class="iframebox">
										<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
											<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
										</c:if>
										<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
											<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
											<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
										</c:if>
									</li>
							</ul>
						</div>
					</c:if>
					<!-- //0824 영상 -->
					
					<!-- 외부 HTML  -->
					<c:if test="${list.conType eq 'C0002'}">
						${list.conDtl}
					</c:if>
					<!-- //외부 HTML  -->
				</c:forEach>	
			</c:if>
			<!--// 일반 이벤트 (G0001), 매거진 (G0003), Y소식 (G0006)  -->


			<!--경품 이벤트(G0002), Y프렌즈 (G0004)  -->
			<c:if test = "${eventDetail.evtType eq 'G0002' or eventDetail.evtType eq 'G0004'  }">
				<c:set var="listIdx" value="0"/>
				<c:forEach var="list" items="${eventContentList}">
					<div class="layout">
						<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
								<div>
								<!--텍스트(대)  -->
									<p class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(대)  -->
								</div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
								<!--텍스트(중)  -->
								<p class="font_m layout-div" style="color:${list.colorCode };" >${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(중)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
							<div>
								<!--텍스트(소)  -->
								<p style="color:${list.colorCode };" >${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(소)  -->
							</div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
							<div class="line"></div>
						</c:if>
						
					</div>
						<!-- 멀티 이미지  -->
						<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
						<div class="swiper-div"> 
							<div class="swiper-container swiper-wrap"> 
				 				<div class="swiper-wrapper"> 
					 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
					 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
						 				<img src="${item }" style="margin-bottom:3%;" alt="">
						 			</div>
					 			</c:forTokens>
								</div>
								<div class="swiper-scrollbar"></div> 
							</div>
						</div>
						<c:set var="listIdx" value="${listIdx + 1}"/>
						</c:if>
						<!-- // 멀티 이미지  --> 
						
						<!-- s:자세히보기 링크 추가 -->						
						<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
							<div class="layout">
								<div class="link_box layout-div">
									<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
										<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
										<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
										<dl>
											<dt>${fn:split(list.conDtl,'|')[2]}</dt>
											<dd>자세히 보러가기</dd>
										</dl>
									</a>
								</div>
							</div>
						</c:if>						
						<!-- e:자세히보기 링크 추가 -->						
						
						<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
						<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
							<div class="row-scroll-wrap">
								<ul class="row-scroll">
										<li class="iframebox">
											<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
											<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
												<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
										</li>
								</ul>
							</div>
						</c:if>
						<!-- //0824 영상 -->
						<!-- 외부 HTML  -->
						<c:if test="${list.conType eq 'C0002'}">
							${list.conDtl}
						</c:if>
					<!-- //외부 HTML  -->
				</c:forEach>
					<!--경품 목록  -->
						<div class="row-scroll-wrap">
						<!-- 경품이 3개이하면  overflow-hidden-->
						<c:if test="${fn: length(eventGiftList) lt '3'}">
							<c:set var="scroll" value="row-scroll scrollbar-none overflow-hidden" />
						</c:if>
						<!-- //경품이 3개이하면  overflow-hidden-->
						
						<!-- 경품이 3개이상이면 scroll ON -->
						<c:if test="${fn: length(eventGiftList) gt '2'}">
							<c:set var="scroll" value="row-scroll scrollbar-none" />
						</c:if>
						<!-- //경품이 3개이상이면 scroll ON -->
							<ul class="${scroll }">
							<c:forEach var="giftList" items="${eventGiftList}">
								<li class="w50p">
									<img src="${giftList.imgUrl}" alt="" width="154px;" height="154px;">
									<p>${giftList.giftName}</p>
									<p class="sub-txt">${giftList.giftCnt}개</p>
								</li>
							</c:forEach>
							</ul>
						</div>
						
					<div class="layout">
					
						<c:if test="${eventDetail.giftOfferType eq 'O0003' }">
							<div class="line"></div>
							<div>
								<ul class="y-info-list">
									<li><span>당첨자 발표일</span>${eventDetail.etrOpenDt} (${eventDetail.etrOpenDweek}) ${eventDetail.etrOpenTime}</li>
								</ul>
							</div>
						</c:if>
					</div>
					<!--//경품 목록  -->
					
					<!--  <c:if test="${ eventGiftList != null and fn:length( eventGiftList ) > 0 }">  -->
						<div class="layout">
							<div class="line"></div>
							<p class="tit layout-div">경품 안내</p>
							<p class="layout-div">
								경품으로 지급된 상품이 개인 실수로 삭제한 경우 복구가 불가능합니다. 이벤트 경품은 부득이한 경우 동등한 가치를 가진 다른 경품으로 대체될 수 있습니다. 경품은 타인에게 양도할 수 없으며, 현금이나 기타 경품으로 요구할 수 없습니다.
							</p>
						</div>
					<!--</c:if>-->
					
					
			</c:if>
			<!-- 경품 이벤트(G0002), Y프렌즈 (G0004) end -->

			<!-- 출석 체크 이벤트(G0005) -->
			<c:if test = "${eventDetail.evtType eq 'G0005'  }">
				<c:set var="listIdx" value="0"/>
				<c:forEach var="list" items="${eventContentList}">
					<div class="layout">
						<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
								<!--텍스트(대)  -->
								<p id="big_txt" class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>	
								<!--텍스트(대)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
								<!--텍스트(중)  -->
								<p class="font_m layout-div" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(중)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
								<div class="layout-div">
									<!--텍스트(소)  -->
									<p style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
									<!--텍스트(소)  -->
								</div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
							<div class="line"></div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0007' and list.conType eq 'C0001'}">
							<div class="eve-chk-wrap">
							
							<!--${list.conDtl} 외부 html???  -->
								
								<ul id="giftYN_ul" class="bg-skyblue">
									<c:forEach var="list" items="${eventAttendList }">
										<li>
											<c:if test="${list.giftYn eq 'Y' and list.attendYn eq 'Y'}">
												<c:if test="${list.winYn eq 'Y'}">
													<img src="/yapp3/res/img/check-attendance_final@2x.png" alt="" style="width:48px; height:48px;">
												</c:if>
												<c:if test="${list.winYn eq 'N'}">
													<img src="/yapp3/res/img/check-attendance-soldout@2x.png" alt="" style="width:48px; height:48px;">
												</c:if>
											</c:if>
											<c:if test="${list.giftYn eq 'Y' and list.attendYn eq 'N'}">
												<c:if test="${list.soldoutYn eq 'Y'}">
													<img src="/yapp3/res/img/check-attendance-soldout_dis@2x.png" alt="" style="width:48px; height:48px;">
												</c:if>
												<c:if test="${list.soldoutYn eq 'N'}">
													<img src="/yapp3/res/img/check-attendance_final_dis@2x.png" alt="" style="width:48px; height:48px;">
												</c:if>
											</c:if>
											
											<c:if test="${list.giftYn eq 'N' and list.attendYn eq 'Y'}">
												<img src="/yapp3/res/img/check-attendance@2x.png" alt="" style="width:48px; height:48px;">
											</c:if>
											<c:if test="${list.giftYn eq 'N' and list.attendYn eq 'N'}">
												<img src="/yapp3/res/img/check-attendance_dis@2x.png" alt="" style="width:48px; height:48px;">
											</c:if>
											<p>${list.giftDay }일</p>
										</li>
									</c:forEach>
								</ul>
								<c:if test="${nowAttendYn eq 'N' }">
									<button type="button" id="check_attendance_btn" class="btn w100p bg-darkgray" onfocus="blur()">출석체크하기</button>
								</c:if>
								
								<c:if test="${nowAttendYn eq 'Y' }">
									<button type="button" id="check_attendance_btn" class="btn w100p bg-darkgray" disabled="disabled" onfocus="blur()">출석체크 완료</button>
								</c:if>
							</div>	
						</c:if>
					</div>
					
						<!-- 멀티 이미지  -->
						<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
						<div class="swiper-div"> 
							<div class="swiper-container swiper-wrap"> 
					 			<div class="swiper-wrapper"> 
					 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
					 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
						 				<img src="${item }" style="margin-bottom:3%;" alt="">
						 			</div>
					 			</c:forTokens>
								</div>
								<div class="swiper-scrollbar"></div> 
							</div>
						</div>
						<c:set var="listIdx" value="${listIdx + 1}"/>
						</c:if>
						<!-- //멀티 이미지  -->
						 
						<!-- s:자세히보기 링크 추가 -->						
						<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
							<div class="layout">
								<div class="link_box layout-div">
									<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
										<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
										<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
										<dl>
											<dt>${fn:split(list.conDtl,'|')[2]}</dt>
											<dd>자세히 보러가기</dd>
										</dl>
									</a>
								</div>
							</div>
						</c:if>						
						<!-- e:자세히보기 링크 추가 -->						
						
						<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
						<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
							<div class="row-scroll-wrap">
								<ul class="row-scroll">
										<li class="iframebox">
											<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
											<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
												<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
										</li>
								</ul>
							</div>
						</c:if>
						<!-- //0824 영상 -->
						
						<!-- 외부 HTML  -->
						<c:if test="${list.conType eq 'C0002'}">
							${list.conDtl}
						</c:if>
						<!-- //외부 HTML  -->
				</c:forEach>
				
				<!--경품 목록  -->
					<div class="row-scroll-wrap">
						<!-- 경품이 3개이하면  overflow-hidden-->
						<c:if test="${fn: length(eventAllGiftList) lt '3'}">
							<c:set var="scroll" value="row-scroll scrollbar-none overflow-hidden" />
						</c:if>
						<!-- //경품이 3개이하면  overflow-hidden-->
						
						<!-- 경품이 3개이상이면 scroll ON -->
						<c:if test="${fn: length(eventAllGiftList) gt '2'}">
							<c:set var="scroll" value="row-scroll scrollbar-none" />
						</c:if>
						<!-- //경품이 3개이상이면 scroll ON -->
						<ul class="${scroll }">
						<c:forEach var="giftList" items="${eventAllGiftList}">
							<li class="w50p">
								<img src="${giftList.imgUrl}" alt="" width="154px;" height="154px;">
								<p>${giftList.giftName}</p>
								<p class="sub-txt">${giftList.giftCnt}개</p>
							</li>
						</c:forEach>
						</ul>
					</div>
					
				<div class="layout">
				
					<c:if test="${eventDetail.giftOfferType eq 'O0003' }">
						<div class="line"></div>
						<div>
							<ul class="y-info-list">
								<li><span>당첨자 발표일</span>${eventDetail.etrOpenDt} (${eventDetail.etrOpenDweek}) ${eventDetail.etrOpenTime}</li>
							</ul>
						</div>
					</c:if>
				</div>
				<!--//경품 목록  -->
		
				<div class="layout">
					<div class="line"></div>
					<p class="tit layout-div">경품 안내</p>
					<p class="layout-div">
						경품으로 지급된 상품이 개인 실수로 삭제한 경우 복구가 불가능합니다. 이벤트 경품은 부득이한 경우 동등한 가치를 가진 다른 경품으로 대체될 수 있습니다. 경품은 타인에게 양도할 수 없으며, 현금이나 기타 경품으로 요구할 수 없습니다.
					</p>
				</div>
			</c:if>
	<!-- 출석 체크 이벤트 end -->
	
	<!-- 210819 응모권 출석 체크 이벤트(G0010) -->
			<c:if test = "${eventDetail.evtType eq 'G0010'  }">
				<c:set var="listIdx" value="0"/>
				<c:forEach var="list" items="${eventContentList}">
					<div class="layout">
						<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
								<!--텍스트(대)  -->
								<p id="big_txt" class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>	
								<!--텍스트(대)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
								<!--텍스트(중)  -->
								<p class="font_m layout-div" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
								<!--텍스트(중)  -->
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
								<div class="layout-div">
									<!--텍스트(소)  -->
									<p style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
									<!--텍스트(소)  -->
								</div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
							<div class="line"></div>
						</c:if>
						
						<c:if test="${list.conDtlType eq 'C0007' and list.conType eq 'C0001'}">
							<div class="ticket-eve-chk-wrap">
							<!--${list.conDtl} 외부 html???  -->
								<ul id="giftYN_ul" class="bg-skyblue">
									<c:forEach var="list" items="${eventAttendList }">
										<li>
											<c:if test="${list.giftYn eq 'Y' and list.attendYn eq 'Y'}">
												<c:if test="${list.winYn eq 'Y'}">
													<img src="/yapp3/res/img/check-attendance_final@2x.png" alt="">
												</c:if>
												<c:if test="${list.winYn eq 'N'}">
													<img src="/yapp3/res/img/check-attendance-soldout@2x.png" alt="">
												</c:if>
											</c:if>
											<c:if test="${list.giftYn eq 'Y' and list.attendYn eq 'N'}">
												<c:if test="${list.soldoutYn eq 'Y'}">
													<img src="/yapp3/res/img/check-attendance-soldout_dis@2x.png" alt="">
												</c:if>
												<c:if test="${list.soldoutYn eq 'N'}">
													<img src="/yapp3/res/img/check-attendance_final_dis@2x.png" alt="">
												</c:if>
											</c:if>
											<c:if test="${list.giftYn eq 'N' and list.attendYn eq 'Y'}">
												<img src="/yapp3/res/img/check-attendance@2x.png" alt="">
											</c:if>
											<c:if test="${list.giftYn eq 'N' and list.attendYn eq 'N'}">
												<img src="/yapp3/res/img/check-attendance_dis@2x.png" alt="">
											</c:if>
											<p>${list.giftDay }일</p>
										</li>
									</c:forEach>
								</ul>
								<c:if test="${nowAttendYn eq 'N' }">
									<button type="button" id="check_attendance_btn" class="btn w100p bg-darkgray" onfocus="blur()">출석체크하기</button>
								</c:if>
								
								<c:if test="${nowAttendYn eq 'Y' }">
									<button type="button" id="check_attendance_btn" class="btn w100p bg-darkgray" disabled="disabled" onfocus="blur()">출석체크 완료</button>
								</c:if>
							</div>	
						</c:if>
					</div>
					
						<!-- 멀티 이미지  -->
						<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
						<div class="swiper-div"> 
							<div class="swiper-container swiper-wrap"> 
					 			<div class="swiper-wrapper"> 
					 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
					 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
						 				<img src="${item }" style="margin-bottom:3%;" alt="">
						 			</div>
					 			</c:forTokens>
								</div>
								<div class="swiper-scrollbar"></div> 
							</div>
						</div>
						<c:set var="listIdx" value="${listIdx + 1}"/>
						</c:if>
						<!-- //멀티 이미지  -->
						 
						<!-- s:자세히보기 링크 추가 -->						
						<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
							<div class="layout">
								<div class="link_box layout-div">
									<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
										<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
										<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
										<dl>
											<dt>${fn:split(list.conDtl,'|')[2]}</dt>
											<dd>자세히 보러가기</dd>
										</dl>
									</a>
								</div>
							</div>
						</c:if>						
						<!-- e:자세히보기 링크 추가 -->						
						
						<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
						<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
							<div class="row-scroll-wrap">
								<ul class="row-scroll">
										<li class="iframebox">
											<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
											<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
												<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
												<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
											</c:if>
										</li>
								</ul>
							</div>
						</c:if>
						<!-- //0824 영상 -->
						
						<!-- 외부 HTML  -->
						<c:if test="${list.conType eq 'C0002'}">
							${list.conDtl}
						</c:if>
						<!-- //외부 HTML  -->
				</c:forEach>
				
				<!--경품 목록  -->
					<div class="row-scroll-wrap">
						<c:set var="cnt" value="999999"/>
						<!-- 경품이 3개이하면  overflow-hidden-->
						<c:if test="${fn: length(eventAllGiftList) lt '3'}">
							<c:set var="scroll" value="row-scroll scrollbar-none overflow-hidden" />
						</c:if>
						<!-- //경품이 3개이하면  overflow-hidden-->
						
						<!-- 경품이 3개이상이면 scroll ON -->
						<c:if test="${fn: length(eventAllGiftList) gt '2'}">
							<c:set var="scroll" value="row-scroll scrollbar-none" />
						</c:if>
						<!-- //경품이 3개이상이면 scroll ON -->
						<ul class="${scroll }">
						<c:forEach var="giftList" items="${eventAllGiftList}">
							<li class="w50p">
								<img src="${giftList.imgUrl}" alt="" width="154px;" height="154px;">
									<p>${giftList.ticketGiftName}</p>
								<p class="sub-txt"><c:if test="${giftList.giftCnt lt cnt }">${giftList.giftCnt}개</c:if><c:if test="${giftList.giftCnt ge cnt }">무제한</c:if></p>
							</li>
						</c:forEach>
						</ul>
					</div>
					
				<div class="layout">
				
					<c:if test="${eventDetail.giftOfferType eq 'O0003' }">
						<div class="line"></div>
						<div>
							<ul class="y-info-list">
								<li><span>당첨자 발표일</span>${eventDetail.etrOpenDt} (${eventDetail.etrOpenDweek}) ${eventDetail.etrOpenTime}</li>
							</ul>
						</div>
					</c:if>
				</div>
				<!--//경품 목록  -->
		
				<div class="layout">
					<div class="line"></div>
					<p class="tit layout-div">경품 안내</p>
					<p class="layout-div">
						경품으로 지급된 상품이 개인 실수로 삭제한 경우 복구가 불가능합니다. 이벤트 경품은 부득이한 경우 동등한 가치를 가진 다른 경품으로 대체될 수 있습니다. 경품은 타인에게 양도할 수 없으며, 현금이나 기타 경품으로 요구할 수 없습니다.
					</p>
				</div>
			</c:if>
	<!-- 응모권 출석 체크 이벤트 end -->
	
	<!-- 20210608 투표하기 이벤트 start -->
	<c:if test = "${eventDetail.evtType eq 'G0008' }">
		<c:set var="listIdx" value="0"/>
		<c:forEach var="list" items="${eventContentList}">
			<div class="layout">
				<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
						<!--텍스트(대)  -->
						<p id="big_txt" class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>	
						<!--텍스트(대)  -->
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
						<!--텍스트(중)  -->
						<p class="font_m layout-div" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
						<!--텍스트(중)  -->
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
						<div class="layout-div">
							<!--텍스트(소)  -->
							<p style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
							<!--텍스트(소)  -->
						</div>
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
					<div class="line"></div>
				</c:if>
				
			</div>
			
				<!-- 멀티 이미지  -->
				<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
				<div class="swiper-div"> 
					<div class="swiper-container swiper-wrap"> 
			 			<div class="swiper-wrapper"> 
			 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
			 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
				 				<img src="${item }" style="margin-bottom:3%;" alt="">
				 			</div>
			 			</c:forTokens>
						</div>
						<div class="swiper-scrollbar"></div> 
					</div>
				</div>
				<c:set var="listIdx" value="${listIdx + 1}"/>
				</c:if>
				<!-- //멀티 이미지  -->
				 
				<!-- s:자세히보기 링크 추가 -->						
				<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
					<div class="layout">
						<div class="link_box layout-div">
							<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
								<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
								<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
								<dl>
									<dt>${fn:split(list.conDtl,'|')[2]}</dt>
									<dd>자세히 보러가기</dd>
								</dl>
							</a>
						</div>
					</div>
				</c:if>						
				<!-- e:자세히보기 링크 추가 -->						
				
				<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
				<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
					<div class="row-scroll-wrap">
						<ul class="row-scroll">
								<li class="iframebox">
									<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
										<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
									</c:if>
									<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
										<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
										<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
									</c:if>
								</li>
						</ul>
					</div>
				</c:if>
				<!-- //0824 영상 -->
				
				<!-- 외부 HTML  -->
				<c:if test="${list.conType eq 'C0002'}">
					${list.conDtl}
				</c:if>
				<!-- //외부 HTML  -->
				
				<!-- 210609 투표 -->
				<c:if test="${list.conDtlType eq 'C0009' and list.conType eq 'C0001'}">
					<div class="check-wrap">
						<ul class="check-inner">
							<c:forEach var="list" items="${voteItemList}" varStatus="status">
								<li class="vote-card">
									<div class="img-area">
										<label for="voteaway${status.count }"><img src="${list.itemImage}" alt=""></label>
									</div>
									
									<dl>
										<dt>
											<div class="radio-custom-wrap" style="maring-top: 4px;">
												<input type="radio" id="voteaway${status.count }" name="voteaway" value="${list.voteItemSeq}" <c:if test="${list.voteItemSeq eq voteItem }">checked="checked"</c:if><c:if test="${voteYn eq 'Y' }">disabled</c:if>>
												<em class="radio-custom"></em>
												<input type="hidden" name="voteItemSeq" value="${list.voteItemSeq}"/>
												<label style="font-weight: bold;" for="voteaway${status.count }"><c:out value="${list.itemText}" /></label>
												<c:if test="${status.count eq 1 }"><input type="hidden" id="voteSeq" name="voteSeq" value="${list.voteSeq }"/></c:if>
											</div>
										</dt>
										<!-- <dd></dd> -->
									</dl>
									
									<div id="vote-group">
										
										<div id="vote-container">
											<!-- 230106 투표수치 표시여부 적용 -->
											<c:if test="${voteRateYn eq 'Y'}">
												<div id="vote-cont1">
													<label id="votePercentLetter${list.voteItemSeq }" style="font-size:0.876rem; color: #737373;" for="votepercent${status.count }"><c:out value="${list.votePercent}" />%</label>
												</div>
												<div id="vote-cont2">
													<label id="voteGetLetter${list.voteItemSeq }" style="font-size:0.876rem; color: #737373;" for="voteitemcnt${status.count }"> <fmt:formatNumber value="${list.voteItemCnt}" pattern="#,###"/>&nbsp;표</label>		
												</div>
											</c:if>								
										</div> 
										
										<div class="graph">
											<span id="votePercent${list.voteItemSeq }" style="width: <c:out value="${list.votePercent }" default="0" />%;"></span>
										</div>
									
									</div> 
									
								</li>
							</c:forEach>
						</ul>
					</div>
					<!-- 투표대상 리스트 -->
				</c:if>
				<!-- //210609 투표 -->
		</c:forEach>
		
		<!--경품 목록  -->
		<div class="row-scroll-wrap">
			<!-- 경품이 3개이하면  overflow-hidden-->
			<c:if test="${fn: length(eventAllGiftList) lt '3'}">
				<c:set var="scroll" value="row-scroll scrollbar-none overflow-hidden" />
			</c:if>
			<!-- //경품이 3개이하면  overflow-hidden-->
			
			<!-- 경품이 3개이상이면 scroll ON -->
			<c:if test="${fn: length(eventAllGiftList) gt '2'}">
				<c:set var="scroll" value="row-scroll scrollbar-none" />
			</c:if>
			<!-- //경품이 3개이상이면 scroll ON -->
			<ul class="${scroll }">
			<c:forEach var="giftList" items="${eventAllGiftList}">
				<li class="w50p">
					<img src="${giftList.imgUrl}" alt="" width="154px;" height="154px;">
					<p>${giftList.giftName}</p>
					<p class="sub-txt">${giftList.giftCnt}개</p>
				</li>
			</c:forEach>
			</ul>
		</div>
		<div class="layout">
		
			<c:if test="${eventDetail.giftOfferType eq 'O0003' }">
				<div class="line"></div>
				<div>
					<ul class="y-info-list">
						<li><span>당첨자 발표일</span>${eventDetail.etrOpenDt} (${eventDetail.etrOpenDweek}) ${eventDetail.etrOpenTime}</li>
					</ul>
				</div>
			</c:if>
		</div>
		<!--//경품 목록  -->
		
		<c:if test="${ eventAllGiftList != null and fn: length(eventAllGiftList) > 0}">
		
			<div class="layout">
				<div class="line"></div>
				<p class="tit layout-div">경품 안내</p>
				<p class="layout-div">
					경품으로 지급된 상품이 개인 실수로 삭제한 경우 복구가 불가능합니다. 이벤트 경품은 부득이한 경우 동등한 가치를 가진 다른 경품으로 대체될 수 있습니다. 경품은 타인에게 양도할 수 없으며, 현금이나 기타 경품으로 요구할 수 없습니다.
				</p>
			</div>
		
		</c:if>
	</c:if>
	<!-- 20210608 투표하기 이벤트 end -->
	
	<!-- 20230331 Y캔버스 이벤트 start -->
	<c:if test = "${eventDetail.evtType eq 'G0011' }">
		<c:set var="listIdx" value="0"/>
		<c:forEach var="list" items="${eventContentList}">
			<div class="layout">
				<c:if test="${list.conDtlType eq 'C0001' and list.conType eq 'C0001'}">
						<!--텍스트(대)  -->
						<p id="big_txt" class="font_b" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>	
						<!--텍스트(대)  -->
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0002' and list.conType eq 'C0001'}">
						<!--텍스트(중)  -->
						<p class="font_m layout-div" style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
						<!--텍스트(중)  -->
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0003' and list.conType eq 'C0001'}">
						<div class="layout-div">
							<!--텍스트(소)  -->
							<p style="color:${list.colorCode };">${fn:replace(list.conDtl, enter, '<br/>' )}</p>
							<!--텍스트(소)  -->
						</div>
				</c:if>
				
				<c:if test="${list.conDtlType eq 'C0006' and list.conType eq 'C0001'}">
					<div class="line"></div>
				</c:if>
				
			</div>		
			<!-- 멀티 이미지  -->
				<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
				<div class="swiper-div"> 
					<div class="swiper-container swiper-wrap"> 
			 			<div class="swiper-wrapper"> 
			 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
			 				<div class="swiper-slide" onclick="openImageModal('${listIdx}','${status.index }');">
				 				<img src="${item }" style="margin-bottom:3%;" alt="">
				 			</div>
			 			</c:forTokens>
						</div>
						<div class="swiper-scrollbar"></div> 
					</div>
				</div>
				<c:set var="listIdx" value="${listIdx + 1}"/>
				</c:if>
				<!-- //멀티 이미지  -->
				 
				<!-- s:자세히보기 링크 추가 -->						
				<c:if test="${list.conDtlType eq 'C0008' and list.conType eq 'C0001'}">	
					<div class="layout">
						<div class="link_box layout-div">
							<a href="${fn:split(list.conDtl,'|')[0]}" target="_blank">				
								<!-- <img src="/yapp3/res/img/url_link.png" alt=""> -->
								<img src="${fn:split(list.conDtl,'|')[1]}" alt="">						
								<dl>
									<dt>${fn:split(list.conDtl,'|')[2]}</dt>
									<dd>자세히 보러가기</dd>
								</dl>
							</a>
						</div>
					</div>
				</c:if>						
				<!-- e:자세히보기 링크 추가 -->						
				
				<!-- 0824 영상 : .row-scroll-wrap와 맨하단.btn-wrap와 맨하단.btn-wrap만 layout 밖에 위치해있습니다 -->
				<c:if test="${list.conDtlType eq 'C0005' and list.conType eq 'C0001'}">
					<div class="row-scroll-wrap">
						<ul class="row-scroll">
								<li class="iframebox">
									<c:if test="${fn: indexOf(list.conDtl,'youtube') eq -1}">
										<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
									</c:if>
									<c:if test="${fn: indexOf(list.conDtl,'youtube') ne -1}">
										<%-- <iframe width="100%" height="auto" src="${list.conDtl}?playsinline=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe> --%>
										<iframe width="100%" height="auto" src="${list.conDtl}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="allowfullscreen"></iframe>
									</c:if>
								</li>
						</ul>
					</div>
				</c:if>
				<!-- //0824 영상 -->
				
				<!-- 외부 HTML  -->
				<c:if test="${list.conType eq 'C0002'}">
					${list.conDtl}
				</c:if>
				<!-- //외부 HTML  -->	
			</c:forEach>
			</c:if>


<!-- 유의 사항 -->
	<c:if test="${eventDetail.evtNoteYn eq 'Y'}">
		<div class="layout">
			<div>
				<p class="tit"><img src="/yapp3/res/img/ic_s_notice.png" alt="">유의사항</p>
				<ul class="disc-ul">
					<c:forTokens var="item" items="${eventDetail.evtNote}" delims="
					">
						<li><p>${item}</p></li>
					</c:forTokens>
				</ul>
			</div>
		</div>
	</c:if>
<!-- //유의 사항 -->

<!-- 경품이벤트 안내 문구  -->
<c:set var="flag" value="false"/>
<!-- 경품 이벤트, Y프렌즈, 출석체크 이벤트  -->
<c:if test = "${eventDetail.evtType eq 'G0002' or eventDetail.evtType eq 'G0004' or eventDetail.evtType eq 'G0005' or eventDetail.evtType eq 'G0008' }">
	<c:forEach var="list" items="${eventGiftList }">
		<!--배송용 상품이 있으면 안내문구 표시  -->
		<c:if test="${list.giftType eq 'G0001' and flag eq false }">
			<div class="layout">
				<div class="bg-skyblue">
					<ul class="disc-ul">
						<li><p>결과 발표는 입력하신 문자로 전달드립니다. 주소입력이 틀린 경우 발생되는 배송 사고는 책임지지 않습니다.</p></li>
					</ul>
				</div>
			</div>
		<c:set var="flag" value="true"/>
		</c:if>
		<!--//배송용 상품이 있으면 안내문구 표시  -->
	</c:forEach>
</c:if>
<!-- //경품이벤트 안내 문구  -->

<!-- 하단 버튼  -->
	<div class="btn-wrap bg-darkgray">
		<!-- 22.10.27 minjae.jeon Start -->
		<div class="service-btn-area">
			<button type="button" id="likeBtn" class="btn-sv btn-heart" onclick="clickLike()">0</button>
			<button type="button" id="replyList" class="btn-sv btn-comment" style="color:transparent;">0</button>
		</div>
		<!-- // 22.10.27 minjae.jeon End -->
		
		<!--출석 체크 이벤트가 아니라면  -->
		<c:if test = "${eventDetail.evtType ne 'G0005' and eventDetail.evtType ne 'G0010' }">
			<a href="javascript:void(0);" id="evt_move" onfocus="blur()">
				<button type="button"  class="btn w100p" onfocus="blur()">${eventDetail.bottomButtonName}</button>
			</a>
		</c:if>
		<c:if test = "${eventDetail.evtType eq 'G0005' or eventDetail.evtType eq 'G0010' }">
			<a href="javascript:void(0);" id="attend_close" onfocus="blur()">
				<button type="button"  class="btn w100p" onfocus="blur()">닫기</button>
			</a>
		</c:if>
		<c:if test = "${eventDetail.evtType eq 'G0008'  }">
			<a href="javascript:void(0);" id="vote_gift" onfocus="blur()" style="display:none;" >
				<button type="button"  class="btn w100p" onfocus="blur()">경품 신청하기</button>
			</a>
		</c:if>		
		<!-- button에 bth-high 클래스를 추가하면 아래 여백이 커집니다 -->
	</div>
<!-- //하단 버튼  -->

	<input type="hidden" name="cntrNo" value="${cntrNo}">
	<input type="hidden" name="ysid" value="${ysid}">
	<input type="hidden" name="autoLogin" value="${autoLogin}">
	<input type="hidden" name="osTp" value="${osTp}">
	<input type="hidden" name="appVrsn" value="${appVrsn}">
	<!-- <script async src='//www.instagram.com/embed.js' /> -->

</div>

<!-- 23.04.05 Y캠버스 Popup -->
<div id="YcanvasClass" class="popup pop-add-comment" style="z-index:3;">
	<!-- 배경 커버(블랙) -->
	<div class="cover"></div>
	<!-- //배경 커버(블랙) -->
	<!-- 팝업 내용 -->
	<div class="pop-bt">
		<div class="pop-header">
			<h2>Y클래스</h2>
		</div>
		<div class="pop-con">
			<div class="campus-wrap">
			<ul class="class-inner">
							<c:set var="clsSeq" value=""/>
							<c:forEach var="list" items="${canvasItemList}" varStatus="status">
							<li class="campus-card">
								<div class="class-info">
									<div class="img-area">
										<c:choose>
											<c:when test="${list.classValidYn eq false}">
												<img id="ycanvas_img_${list.clsSeq}" src="${list.imgUrl}" alt="" style="filter:brightness(0.35);">
												<div id="class_deadline"><img id="deadline_image" src="/yapp3/res/img/y_class_deadline.png" alt=""></div>
											</c:when>
											<c:otherwise>
												<img id="ycanvas_img_${list.clsSeq}" src="${list.imgUrl}" alt="">
											</c:otherwise>
										</c:choose>
										<%-- <img id="ycanvas_img_${list.clsSeq}" src="${list.imgUrl}" alt="">
										<div id="class_deadline"><img id="deadline_image" src="/yapp3/res/img/y_class_deadline.png" alt=""></div> --%>
									</div>
									<div class="campus-title-area">
										<div class="class-start">${list.classSchd}</div>
										<div class="class-title" style="padding-bottom:3px;">${list.clsTitle}</div>
										<div id="ycanvas_gift_cnt_${list.clsSeq}">(${list.issueCnt}/${list.giftCnt})</div>
									</div>
								</div>
									<div class="btn-area">
										<c:choose>
											<c:when test="${list.classValidYn eq false}">
												<button id="ycanvas_btn_${list.clsSeq}" type="button" style="color:#ffffff !important" class="btn-upload-comment full" onClick="applyYcanvasPopup('${list.clsSeq}','${list.issueSeq}','${list.rewSeq}','${list.imgUrl}','${list.giftSeq}','${list.maxClassCnt}','${list.clsTitle}')" disabled="disabled">마감</button>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${list.classJoinYn eq 'N'}">
														<button id="ycanvas_btn_${list.clsSeq}" type="button" style="color:#ffffff !important" class="btn-upload-comment join" onClick="applyYcanvasPopup('${list.clsSeq}','${list.issueSeq}','${list.rewSeq}','${list.imgUrl}','${list.giftSeq}','${list.maxClassCnt}','${list.clsTitle}')" disabled="disabled">신청완료</button>
													</c:when>
													<c:otherwise>
														<button id="ycanvas_btn_${list.clsSeq}" type="button" class="btn-upload-comment" onClick="applyYcanvasPopup('${list.clsSeq}','${list.issueSeq}','${list.rewSeq}','${list.imgUrl}','${list.giftSeq}','${list.maxClassCnt}','${list.clsTitle}')">신청</button>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</div>
								</li>
							<c:set var="clsSeq" value="${list.clsSeq}"/>
							<hr style="background:#e5e5e5; height:1px; border:0;">
							</c:forEach>
						</ul>
					</div> 
		</div>
		<div class="btn-pop-prev" onclick="cancelClass()">이전</div>
	</div>
	<!-- //팝업 내용 -->
</div>
<!-- 23.04.05 Y캠버스 Popup End-->

<!-- 23.04.05 Y캠버스 신청 Yn 팝업-->
	<div id="ApplyYcanvasClassPopup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>해당 수강을 신청하시겠습니까?</p>
			</div>
				<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="btnCancel" class="btn w50p" onclick="btnApplyCancel()" onfocus="blur()">취소</button>
				<button type="button" id="btnApply" class="btn w50p" onclick="btnApply()" onfocus="blur()">확인</button>
				<input type="hidden" id="clsSeq" name="clsSeq" value=""/>
				<input type="hidden" id="clsIssueSeq" name="clsIssueSeq" value=""/>
				<input type="hidden" id="clsRewSeq" name="clsRewSeq" value=""/>
				<input type="hidden" id="imgUrl" name="imgUrl" value=""/>
				<input type="hidden" id="giftSeq" name="giftSeq" value=""/>
				<input type="hidden" id="maxClassCnt" name="maxClassCnt" value=""/>
				<input type="hidden" id="clsTitle" name="clsTitle" value=""/>
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
<!-- // 23.04.05 Y캠버스 신청 Yn 팝업 End -->

<!-- 23.04.06 Y캠버스 신청 완료 Yn 팝업  -->
	<div id="ycanvas_complete_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div id="random_complete_cover" class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<img id="ycanvas_cmpl_img" src="" alt="" width="108px;" height="108px;">
				<p id="ycanvas_complete_text"></p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="ycanvas_complete_ok" class="btn w50p" onfocus="blur()" >신청확인하기</button>
				<button type="button" id="ycanvas_complete_cancel" class="btn w50p" onclick="btnModifyCancel()" onfocus="blur()">계속신청하기</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
<!-- //E23.04.06 Y캠버스 신청 완료 Yn 팝업 End -->

<!-- E23.04.06 Y캠버스 정원초과 팝업의 경우 -->
	<div id="not_class_limit_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p id="ycanvas_alert_text"></p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="limit_hide_btn" class="btn w100p" onfocus="blur()">확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
<!--//E23.04.06 Y캠버스 정원초과 팝업 End -->

<!-- 22.10.27 minjae.jeon : 댓글 Popup -->
<div id="AddComment" class="popup pop-add-comment" style="z-index:3;">
	<!-- 배경 커버(블랙) -->
	<div class="cover"></div>
	<!-- //배경 커버(블랙) -->
	<!-- 팝업 내용 -->
	<div class="pop-bt">
		<div class="pop-header">
			<h2>댓글</h2>
		</div>
		<div class="pop-con">
			<div id="admin-reply-box"></div>
			<div class="comment-wrap" id="reply-box"></div>
			<button class="btn w100p" id="replyMore" style="display:none; padding:1rem"><span>더보기</span></button>
		</div>
		<div class="pop-btn-wrap">
			<div class="comment-ip">
				<textarea name="replyText" id="replyText" maxlength="100" placeholder="댓글달기"></textarea>
				<button type="button" id="replyWrite" class="btn-upload-comment" disabled="disabled">게시</button>
				<button type="button" id="replyModify" class="btn-upload-comment" style="display:none;">수정</button>
			</div>  
			<div id="replyLimit" style="text-align:end; padding:0.5rem;">(0/100)</div>
		</div>
		<div class="btn-pop-prev" onclick="cancelComment()">이전</div>
	</div>
	<!-- //팝업 내용 -->
</div>

	<script type="text/javascript">
	$.each($('.comment-ip textarea'), function() {
		var offset = this.offsetHeight - this.clientHeight;
		var resizeTextarea = function(el) {
			$(el).css('height', '36px').css('height', el.scrollHeight);
		};
		$(this).on('keyup', function(){ 
		resizeTextarea(this);
		var tar_height = $(this).parents('.pop-btn-wrap').outerHeight();
		console.log(tar_height);
		$(this).parents('.popup').find('.pop-con').css('padding-bottom', tar_height);
		});
	});  
	</script>
	<!-- // 22.10.27 minjae.jeon : 댓글 Popup -->

	<!-- 22.10.27 minjae.jeon : 댓글 수정 Popup -->
	<div id="ModifyComment" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>해당 댓글을 수정하시겠습니까?</p>
			</div>
				<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="btnCancel" class="btn w50p" onclick="btnModifyCancel()" onfocus="blur()">취소</button>
				<button type="button" id="btnModify" class="btn w50p" onclick="btnModify()" onfocus="blur()">확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- // 22.10.27 minjae.jeon : 댓글 수정 Popup -->
	
	<!-- EVE-01-POP7_댓글 수정완료 -->
	<div id="reply_modify_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>수정 되었습니다.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="reply_modify_ok" class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP7_댓글 수정완료 -->



	<!-- 22.10.27 minjae.jeon : 댓글 삭제 Popup -->
	<div id="DeleteComment" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>해당 댓글을 삭제하시겠습니까?</p>
			</div>
				<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="btnCancel" class="btn w50p" onclick="btnDeleteCancel()" onfocus="blur()">취소</button>
				<button type="button" id="btnDelete" class="btn w50p" onclick="btnDelete()" onfocus="blur()">확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- // 22.10.27 minjae.jeon : 댓글 삭제 Popup -->
	
	<!-- EVE-01-POP7_댓글 삭제완료 -->
	<div id="reply_delete_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>삭제 되었습니다.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="reply_delete_ok" class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP7_댓글 삭제완료 -->

<!-- EVE-01-POP1_응모_로그인안된경우 -->
		<div id="notLogin" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>이벤트 참여는 로그인 후에 가능합니다.<br>로그인 페이지로 이동하시겠습니까?</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="login_hide_btn" class="btn w50p" onfocus="blur()" >취소</button>
					<button type="button" id="login_move"  class="btn w50p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP1_응모_로그인안된경우 -->	

<!-- EVE-01-POP1_댓글_로그인안된경우 -->
		<div id="notLogin2" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>댓글 등록은 로그인 후에 가능합니다.<br>로그인 페이지로 이동하시겠습니까?</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="login2_hide_btn" class="btn w50p" onfocus="blur()" >취소</button>
					<button type="button" id="login2_move"  class="btn w50p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP1_댓글_로그인안된경우 -->	

<!-- EVE-01-POP1_댓글 글자수 초과의 경우 -->
		<div id="notTextLimit" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>100자를 초과하여 입력 할 수 없습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="limit_hide_btn" class="btn w100p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP1_댓글 글자수 초과의 경우 -->

<!-- EVE-01-POP1_이벤트 대상이 아닌경우 -->
		<div id="notTarget" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>이벤트 대상자가 아닙니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="target_hide_btn" class="btn w100p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP1_이벤트 대상이 아닌경우 -->

<!-- EVE-01-POP1_이벤트 대상이 아닌경우 -->
		<div id="notTarget2" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>이벤트 대상자가 아닙니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="target_link_btn" class="btn w50p" onfocus="blur()">바로가기</button>
					<button type="button" id="target_hide_btn2" class="btn w50p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP1_이벤트 대상이 아닌경우 -->					
		
<!-- //EVE-01-POP2_이벤트 공유하기 팝업  -->
		<div id="share_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<ul class="inline-block-list">
						<li><a href="javascript:void(0);" id="kakaotalk" onclick="sendLink();" onfocus="blur()"><img src="/yapp3/res/img/ic_share_kakao.png" alt="카카오톡 공유" width="55px;" height="55px;"></a></li>
						<li><a href="javascript:void(0);" id="line" onclick="shareAct(this);" onfocus="blur()"><img src="/yapp3/res/img/ic_share_line.png" alt="라인 공유" width="55px;" height="55px;"></a></li>
						<li><a href="javascript:void(0);" id="facebook" onclick="shareAct(this);" onfocus="blur()"><img src="/yapp3/res/img/ic_share_facebook.png" alt="페이스북 공유" width="55px;" height="55px;"></a></li>
					</ul>
					<p>SNS로 이벤트를 공유해보세요.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="share_hide_btn" class="btn w100p" onfocus="blur()">닫기</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
<!-- //EVE-01-POP2_이벤트 공유하기 팝업 -->

<!-- EVE-01-POP9_경품선택 -->
	<div id="giftChoice_popup" class="popup">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
			<!--경품 선택  -->
				<c:if test="${eventDetail.giftChoiceType eq 'C0001'}">
					<c:if test="${!empty eventGiftList }">
						<input type="hidden" name="attendGiftYn" value="Y"/>
					</c:if>
					<c:if test="${empty eventGiftList }">
						<input type="hidden" name="attendGiftYn" value="N"/>
					</c:if>
					<p class="f-yspotlightotapp">받고 싶은 경품을 선택해주세요!</p>
					<div class="radio-list-row">
						<ul>
						<c:forEach var="giftList" items="${eventGiftList}" varStatus="status">
							<li>
								<label for="giveaway${status.count }"><img src="${giftList.imgUrl}" alt="" width="154px;" height="154px;"></label>
									<div class="radio-custom-wrap">
										<input type="radio" name="giveaway" id="giveaway${status.count }"
											value="giveaway${status.count }"> <span class="radio-custom"></span>
										<input type="hidden" name="giftType" value="${giftList.giftType}"/>
										<input type="hidden" name="giftSeq" value="${giftList.giftSeq}"/>
										<input type="hidden" name="issueSeq" value="${giftList.issueSeq}"/>
									<label for="giveaway${status.count }">${giftList.giftName}</label>
									</div>
							</li>
						</c:forEach>
						</ul>
						<p class="radio-list-resualt">
							경품으로 <span id="data-resualt"></span>를 선택하셨습니다.
						</p>
					</div>
				</c:if>
			<!--//경품 선택  -->
				<!--랜덤 제공  -->
				<c:if test="${eventDetail.giftChoiceType eq 'C0002'}">
					<div class="radio-list-row">
						<ul>
							<c:forEach var="giftList" items="${eventGiftList}" varStatus="status">
								<li class="on"><label for="giveaway${status.count }"><img
										src="${giftList.imgUrl}" alt="" width="154px;" height="154px;"></label>
									<div class="radio-custom-wrap">
										<input type="hidden" name="giftType" value="${giftList.giftType}"/>
										<label for="giveaway${status.count }">${giftList.giftName}</label>
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
				<button type="button" id="giftChoice_popup_cancel" class="btn w50p" onfocus="blur()" >취소</button>
				<button type="button" id="giftChoice_complete" class="btn w50p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP9_경품선택 -->

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
					축하합니다. 이벤트 상품은<br>리워드에서 확인하실 수 있습니다.
				</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="reward_move" class="btn w50p" onfocus="blur()" >리워드</button>
				<button type="button" id="first_gift_cancel"  class="btn w50p" onfocus="blur()" >닫기</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP6_경품증정 -->
	
	<!-- EVE-01-POP 13_일반/매거진/Y소식 응모권경품증정 -->
	<div id="first_ticket_gift_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div id="first_gift_cover" class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<img id="ticket_gift_url" src="" alt="" width="108px;" height="108px;">
				<p id="ticket_gift_name"></p>
				<p class="f-gray sub-con">
					축하합니다. 이벤트 상품은<br>리워드에서 확인하실 수 있습니다.
				</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<a href="javascript:void(0);" id="link_go" onfocus="blur()">
					<button type="button" class="btn w100p" onfocus="blur()" >확인</button>
				</a>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
	<!--// EVE-01-POP 13_일반/매거진/Y소식 응모권경품증정 -->
	</div>
	<!-- //EVE-01-POP13_경품증정 -->
	
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
			<div id="not_gift_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p id="error_msg"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<c:if test="${eventTicketHaveYn eq 'Y' }">
						<a href="javascript:void(0);" id="soldout_link_go" onfocus="blur()">
							<button type="button" class="btn w100p" onfocus="blur()" >확인</button>
						</a>
					</c:if>
					<c:if test="${eventTicketHaveYn eq 'N' }">
						<button type="button" id="not_gift_ok" class="btn w100p" onfocus="blur()" >확인</button>
					</c:if>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP8_경품이 없는 경우 -->


		<!-- EVE-01-POP10_배송지정보입력 (서버에 올릴땐 우편번호에 readOnly 추가)-->
		<div id="delivery_popup" class="popup" style="z-index:3;">
			<!-- 배경 커버(블랙) -->
			<div id="delivery_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div id="delivery_resize" class="pop-con" style="overflow: auto; height:390px;">
					<p class="f-yspotlightotapp">배송지 정보를 입력해 주세요.</p>
					<div class="input-list-wrap">
						<ul>
							<li><input type="text" id="recvName" name="recvName" placeholder="이름 입력" maxlength="10"></li>
							<li><input type="text" id="recvMobileNo" name="recvMobileNo" placeholder="전화번호 입력" maxlength="11" value="<c:if test = "${not empty assoUserMobileNo}">${assoUserMobileNo}</c:if>"></li>
							<li id="wrapPost" style="display:none;border:1px solid;width:100%;height:300px;margin:5px;position:relative;clear: both;" >
								<img src="/yapp3/res/img/icon_popClose.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1;height:17px;width:17px;" onclick="foldPostCode()" alt="접기버튼">
							</li>
							 
							<c:if test = "${eventDetail.giftAddrYn eq 'Y' }">
							<li><input type="text" id="post" name="post" placeholder="우편번호" readOnly="readonly" onclick="execPost()" ><a href="javascript:void(0);" onclick="execPost()" onfocus="blur()" style="text-decoration: none; color: black;">우편번호 검색</a></li>
							<li><input type="text" id="addr" name="addr" placeholder="주소"></li>
							<li><input type="text" id="addrDtl" name="addrDtl" placeholder="상세주소" maxlength="80"></li>
							</c:if>		
							 
							<li><input type="text" id="remarks" name="remarks" placeholder="비고" maxlength="80"></li>
						</ul>
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="delivery_cancel" class="btn w50p" onfocus="blur()" >취소</button>
					<button type="button" id="delivery_complete" class="btn w50p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-01-POP10_배송지정보입력 -->
		
		
		<!-- EVE-04-POP1_출석체크이벤트 -->
		<div id="attend_check_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div id="attend_check_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>출석체크 완료!</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="attend_check_ok" class="btn w100p" onfocus="blur()">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //EVE-04-POP1_출석체크이벤트 -->
		
		<!-- EVE-01-POP11_상품이 없어 출석체크만완료 -->
	<div id="only_attend_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div id="onlyattend_complete_cover" class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<img src="/yapp3/res/img/nogift_attend.png" alt="" width="108px;" height="108px;">
				<p>경품이 소진되었습니다.<br>다음 기회를 이용해 주세요.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="onlyattend_complete_ok"class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP11_상품이 없어 출석체크만완료-->

	<!-- 210615 EVE-01-POP12_투표대상 미선택 -->
	<div id="not_vote_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>투표 대상을 선택해주세요.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="not_vote_ok" class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP12_투표대상 미선택 -->
	
	<!-- 210706 EVE-01-POP13_투표대상 완료 -->
	<div id="only_vote_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>투표가 완료되었습니다.<br>경품을 신청해주세요.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="only_vote_ok" class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP13_투표대상 완료 -->
	
	<!-- 220926 EVE-01-POP13_투표대상 완료 -->
	<div id="complete_vote_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>투표가 완료되었습니다.</p>
			</div>
			<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="complete_vote_ok" class="btn w100p" onfocus="blur()" >확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
	</div>
	<!-- //EVE-01-POP13_투표대상 완료 -->
	
	<%-- <c:if test = "${eventDetail.evtType eq 'G0001' or eventDetail.evtType eq 'G0003' or eventDetail.evtType eq 'G0006' }"> --%>
	<!-- 이미지 모달창 팝업 -->
		<c:set var="listIdx" value="0"/>
		<c:forEach var="list" items="${eventContentList}" varStatus="modal">
		<!-- 멀티 이미지  -->
		<c:if test="${list.conDtlType eq 'C0004' and list.conType eq 'C0001'}">
			<div id="modal-popup${listIdx}" class="modal-popup">
			<img src="/yapp3/res/img/icon_popClose.png" class="popup-close" id="jsCloseBtn" onclick="closeImageModal('${listIdx}','')"/> 
				<div class="swiper-container modal-wrap"> 
		 			<div class="swiper-wrapper"> 
		 			<c:forTokens var="item" items="${list.conDtl}" delims="|" varStatus="status">
		 				<div class="swiper-slide" style="background-color : black;" onclick="openImageModal('${listIdx}','${status.index }');">
		 					<div class="swiper-zoom-container">
			 					<img name="imgViewer${status.index }" src="${item }" style="margin-bottom:3%;" alt="">
			 				</div>
			 			</div>
		 			</c:forTokens>
					</div>
					<div class="swiper-scrollbar"></div> 
				</div>
			</div>
			<c:set var="listIdx" value="${listIdx + 1}"/>
		</c:if> 
		</c:forEach>
		<!-- 멀티 이미지  -->
	<%-- </c:if> --%>
	<!-- 이미지 모달창 팝업 End-->
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

//이미지 슬라이더
  var swiper = new Swiper('.swiper-container', { 
		 scrollbar: { 
		 el: '.swiper-scrollbar', 
		 hide: false, 
		 }, 
		 centeredSlides: true,
		 slideToClickedSlide: true,
		 //loopedSlides: swiperMainNum.length
  }); 
  
  //확대 메인 이미지 슬라이더
  var mainSwiper = new Swiper('.swiper-wrap', { 
		 scrollbar: { 
		 el: '.swiper-scrollbar', 
		 hide: false, 
		 }, 
		 centeredSlides: true,
		 slideToClickedSlide: true,
  }); 
  //확대 팝업 슬라이더
  var popupSwiper = new Swiper('.modal-wrap', { 
	  	 scrollbar: { 
		 el: '.swiper-scrollbar', 
		 hide: false, 
		 }, 
		 centeredSlides: true,
		 slideToClickedSlide: true,
		 zoom: {
			 maxRatio : 3,
			 minRatio : 1
		 }
  });

var modalOpen = false;

function openImageModal(listIdx, elIdx){
	  
	  if(modalOpen){
			document.querySelector("#modal-popup"+listIdx+"").style.display = 'none';
			modalOpen = true;
	 	}else{
			document.querySelector("#modal-popup"+listIdx+"").style.display = 'block';
			modalOpen = false;
	 	}
 }
 
 function closeImageModal(listIdx, elIdx){

	  if(elIdx=='' || elIdx == undefined){
		  document.querySelector("#modal-popup"+listIdx+"").style.display = 'none';
		  modalOpen = false;
		  return;
	  } 
 }

 $('div[id^=modal-popup]').css('display','none');
</script>


</body>
</html>