<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		<link type="text/css" rel="stylesheet" href="/yapp3/res/css/layout.css?20210902">
		<script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script><!-- 210907 추가 -->
		<script type="text/javascript" src="/yapp3/res/js/common.js"></script> <!-- 210907 추가 -->
		
		<style>
			/* 리워드 없을떄 */
			/* .content{padding-top:27%; padding-bottom:15%;} */
			
			/* 리워드 있을떄 */
			/* .content{padding-top:15%; padding-bottom:15%;} */
			h2.tit + p{margin-top:3%; font-size:0.876rem}
			.coupon-ul-wrap{margin-top:13%;}
			.coupon-ul-wrap{margin-top:13%;}
		    /* .popup{display:none;} */ 
		    #cupn_use_info_yn{display:none;}
		    #cupn_etc_yn{display:none;}
		    
   	    	#cupn_popup{display:none;} /*210817 추가*/
   	    	#reward_popup{display:none;} /*210817 추가*/
   	    	#url_reward_popup{display:none;} /*230515 추가*/

   	    	#delivery_popup{display:none;} /*210812 추가*/
   	    	#ticket_offline_popup{display:none;} /*210812 추가*/
   	    	#ticket_online_popup{display:none;} /*210902 추가*/
   	    	
   	    	#ticket_gift_wait_popup{display:none;} /*210817 추가*/
   	    	#ticket_use_popup{display:none;} /*210817 추가*/
   	    	
   	    	#ticket_gift_use_error_popup{display:none;}/*210901 추가*/
   	    	#ticket_gift_win_popup{display:none;}/*210901 추가*/
   	    	#ticket_gift_non_win_popup{display:none;}/*210901 추가*/
   	    	
	   	    #ticket_gift_data_use_popup{display:none;}/*210902 추가*/
	   	    #data_use_complete_popup{display:none;}/*210903 추가*/
   	    	
   	    	#ticket_cupn_use_info_yn{display:none;}/*210906 추가*/
		    #ticket_cupn_etc_yn{display:none;}/*210906 추가*/
		       	    	
    	    #ticket_off_cupn_use_info_yn{display:none;}/*210907 추가*/
		    #ticket_off_cupn_etc_yn{display:none;}/*210907 추가*/
		    
		    #not_info_popup{display:none;}/*210907 추가*/
		   
		    #ticket_use_notice_popup{display:none;}/*210910 추가*/
		    
  	   	    #data_reward_use_popup{display:none;}/*211208 추가*/
  	   	    
  	   	    #class_reward_popup{display:none;} /*230412 추가*/
  	   	    
  	   	    #delete_class_reward_popup{display:none;} /*230413 추가*/
  	   	    
  	   	    #reward_delete_popup{display:none;} /*230413 추가*/
		    
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
		
		$(document).ready(function(){
			var hdcntrNo = '${fn:escapeXml(cntrNo)}'; 
			var hdysid = '${fn:escapeXml(ysid)}'; 
			var hdautoLogin = '${fn:escapeXml(autoLogin)}'; 
			var hdosTp = '${fn:escapeXml(osTp)}'; 
			var hdappVrsn = '${fn:escapeXml(appVrsn)}'; 
			$('[name=cntrNo]').val(hdcntrNo);
			$('[name=ysid]').val(hdysid);
			$('[name=autoLogin]').val(hdautoLogin);
			$('[name=osTp]').val(hdosTp);
			$('[name=appVrsn]').val(hdappVrsn);
			
			//중앙 팝업
			$('#rewardPopClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
				$("#cupn_etc *").remove();
				$("#cupn_use_info *").remove();
				
			});
			
			//중앙 팝업
			$('#urlRewardPopClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
				$("#cupn_etc *").remove();
				$("#cupn_use_info *").remove();
				
			});
			
			$('#classRewardPopClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
				$("#clss_cupn_etc *").remove();
				$("#clss_cupn_use_info *").remove();
			});
			
			//ticketGiftOfflineClose
			$('#ticketGiftOfflineClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			//ticketGiftOnlineClose
			$('#ticketGiftOnlineClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			
			//210910 응모권 유의사항 닫기
			$('#ticketUseNoticeClose').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			$('#cover1').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				$("#cupn_etc *").remove();
				$("#cupn_use_info *").remove();
			});
			
			//하단 팝업
			$('#cover2').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			$('#btn_cancel').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			$('#off_cover').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			$('#delivery_cover').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			$('#ticket_gift_wait_cover').click(function(){
				//scrollAble();
				//$(this).parents('.popup').hide();
			});
			
			//응모권 회차없을때 팝업 확인버튼
			$('#wait_ok').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 사용 에러 팝업 확인버튼
			$('#ticket_use_error_ok').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 사용팝업 취소버튼
			$('#ticket_cancel').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 온라인 팝업 확인 버튼
			$('#ticket_online_confirm').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 오프라인 팝업 확인 버튼
			$('#ticket_offline_confirm').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 오프라인 배송지입력 팝업 취소 버튼
			$('#delivery_cancel').click(function(){
				var element_wrap = document.getElementById("wrapPost");
				element_wrap.style.display = 'none';

				/*$("#recvName").val("");
				$("#recvMobileNo").val("");
				$("#post").val("");
				$("#addr").val("");
				$("#addrDtl").val(""); */
				
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//응모권 데이터 사용 취소 버튼
			$('#ticket_data_cancel').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//데이터 사용완료 버튼
			$('#data_use_ok').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			//211208 데이터 쿠폰 사용 취소 버튼
			$('#data_reward_cancel').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//배송지정보 입력 취소 버튼
			$('#delivery_cancel').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			//정보 미입력 팝업창 닫기
			$("#not_info_ok").click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//창 새로고침 시키기
			$("#info_refresh").click(function(){
				title ="리워드";
				console.log(evtSeq);
				var postData = {
						gb : '2'
					};
					
					if(isIOS()){
						//IOS
						window.webkit.messageHandlers.callBackHandler.postMessage(postData); 
					} else {
						//Android
						/* url = "/reward/reward";
						
						window.YDataBox.moveWebViewPage(url, evtSeq, title);
						window.close(); */
						window.YDataBox.getReload(evtSeq);
					}
			});
			
			
			//210910 응모권 상품 유의사항 팝업창 닫기
			$("#ticket_use_notice_close").click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			//배송지정보 입력 완료 버튼
			$('#delivery_complete').click(function(){
				//ajax
				var recvName=$("#recvName").val();			//사용자 이름
				var recvMobileNo=$("#recvMobileNo").val();	//사용자 전화번호
				var post=$("#post").val();					//우편번호
				var addr=$("#addr").val();					//주소
				var addrDtl=$("#addrDtl").val();			//상세 주소
				var remarks=$("#remarks").val();			//비고
		
				var giftIssueSeq = $('[name=giftIssueSeq]').val();
				
				var ticketGiftRewardInfo={		"giftIssueSeq":giftIssueSeq
									  		  ,	"recvName":recvName
									  		  ,	"recvMobileNo":recvMobileNo
									  	 	  ,	"recvPost":post
									  	 	  ,	"recvAddr":addr
									  		  ,	"recvAddrDetail":addrDtl};
				
				if(recvName.trim()=="" || recvMobileNo.trim()=="" || post.trim()=="" || addr.trim()=="" || addrDtl.trim()==""){
					scrollDisble();
					$("#not_info_popup").show();
					return false;
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
					url : "ticketGiftRewardAddressInfo",
					data : ticketGiftRewardInfo,
					success : function(data){
						if(data.resultCd=="200"){
							console.log("data : "+JSON.stringify(data));
							console.log("data.resultData : "+JSON.stringify(data.resultData));
							console.log("data.resultCd : "+data.resultCd);
							
							var delivery_info = '';
							delivery_info += recvName+'<br>';
							delivery_info += recvMobileNo+'<br>';
							delivery_info += post+'<br>';
							delivery_info += addr+'<br>';
							delivery_info += addrDtl+'<br>';
							
							$("#delivery_info").html(delivery_info);
							//210913 수정하기 주석
							//$("#addrBtnName").text('주소 수정하기');
							
							scrollAble();
							$("#delivery_popup").hide();
							
							//210913 수정하기 주석
							//$("#addrBtnName").hide();
							$("#deliveryBtn").hide();
							
						}//200 end
						else{
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#ticket_gift_use_error_popup").show();
						}
					},
					error : function(error){
						console.log(error);
					}
				}); //ajax End 
				doubleSubmitFlag = false;
				
			});
			
			//응모권 데이터 지급 로직
			$('#ticket_data_use').click(function(){
				var giftIssueSeq = $('[name=giftIssueSeq]').val();
				var uiIndex = $("#uiIndex").val();
				//var dataTest = '';

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
					url : "ticketDataUse",
					data : {giftIssueSeq : giftIssueSeq},
					success : function(data){

						if(data.resultCd=="200"){

							if( data.resultData.dataGiveYn =="Y"){

								$("#ticket"+uiIndex).addClass('grey');
								//$("#ticket"+uiIndex).attr('onclick').unbind('click');
								$("#ticket"+uiIndex).removeAttr("onclick");
								$("#regDt"+uiIndex).text(data.resultData.dataValidStartDt + " 사용 완료");
								$("#validDt"+uiIndex).text(data.resultData.validStartDt+" - "+data.resultData.validEndDt);
								
								scrollDisble();
								$("#data_use_complete_popup").show();
							}
							
						}else{
							scrollDisble();
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							$("#ticket_gift_use_error_popup").show();
							
						}
						doubleSubmitFlag = false;
					},
					error : function(error){
						console.log(error);
						doubleSubmitFlag = false;
					}
				});
			  	
				$("#ticket_gift_data_use_popup").hide();

			});
			
			//211208 데이터 쿠폰 사용 로직
			$('#data_reward_use').click(function(){
				var dataRewSeq = $('[name=dataRewSeq]').val();
				var dataUiIndex = $("#dataUiIndex").val();

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
					url : "dataRewardUse",
					data : {dataRewSeq : dataRewSeq},
					success : function(data){

						if(data.resultCd=="200"){

							if( data.resultData.dataGiveYn =="Y"){

								$("#data"+dataUiIndex).addClass('grey');
								$("#data"+dataUiIndex).removeAttr("onclick");
								$("#dataReg"+dataUiIndex).text(data.resultData.dataValidStartDt + " 사용 완료");
								
								scrollDisble();
								$("#data_use_complete_popup").show();
							}
							
						}else{
							scrollDisble();
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							$("#ticket_gift_use_error_popup").show();
							
						}
						doubleSubmitFlag = false;
					},
					error : function(error){
						console.log(error);
						doubleSubmitFlag = false;
					}
				});
				
			  	
				$("#data_reward_use_popup").hide();

			});
			
			
			$('#win_confirm').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
			});
			
			$('#non_win_confirm').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
			});
			
			
			//응모권 사용팝업 사용버튼
			$('#ticket_use').click(function(){
				scrollAble();
				$(this).parents('.popup').hide();
				
				if(doubleSubmitCheck()) return;
				$.ajax({
					type	:	"post",
					headers	:	{
									cntrNo : $('[name=cntrNo]').val(),
									ysid : $('[name=ysid]').val(),
									autoLogin : $('[name=autoLogin]').val(),
									osTp : $('[name=osTp]').val(),
									appVrsn : $('[name=appVrsn]').val()						
								},
					url		:	"ticketUse",
					data	:	{	"ticketSeq" : $('[name=ticketSeq]').val(), 
									"infYn" : $('[name=infYn]').val()
								},
					success	:	function(data){
						
						//var dataTest = data.resultCd;
						
						if(data.resultCd=="200"){
							var uiIndex = $("#uiIndex").val();

							//dataTest += ' : index : '+uiIndex;
							//$("#dataTest").text(dataTest);

							$("#ticket"+uiIndex).removeAttr("onclick");
							
							//dataTest += ' : onclick 00 : ';
							//$("#dataTest").text(dataTest);

							
						//1. 당첨시 (상품이미지 필요)
							if(data.resultData.winYn == "Y"){
								
								//애니메이션 효과??
								//이미지변환시키고 응모권사용 팝업창 닫기
								//당첨팝업 띄우기
								//새로고침?(세션날아가면 다른방법으로)
								$("#gift_name").text(data.resultData.giftName);
								
								$("#ticket"+uiIndex).attr('class','red');
								
								//dataTest += ' : hasclass : '+$("#ticket"+uiIndex).hasClass('red');
								//$("#dataTest").text(dataTest);

								$("#img"+uiIndex).attr("src", "/yapp3/res/img/win_img.png");
								
								//dataTest += ' : img : win ';
								//$("#dataTest").text(dataTest);

								$("#validDt"+uiIndex).text(data.resultData.validStartDt+" - "+data.resultData.validEndDt);
								
								//dataTest += ' : validDt : ';
								//$("#dataTest").text(dataTest);

								$("#regDt"+uiIndex).text(data.resultData.ticketGiftRegDt+" 응모");
								//dataTest += ' : regDt : ';
								//$("#dataTest").text(dataTest);

								
								//글자길어지면 자르는 처리 필요
								$("#tit"+uiIndex).text(data.resultData.giftName);
								//dataTest += ' : giftName : '+data.resultData.giftName;
								//$("#dataTest").text(dataTest);

								//온라인쿠폰일때
								if(data.resultData.giftType == "G0002"){
									//데이터일때
									if(data.resultData.dataGiveYn == "N"){
										
										//var clickEvt = dataUsePopUp(data.resultData.giftIssueSeq, uiIndex);
										//$("#ticket"+uiIndex).attr('onclick','').click(clickEvt);
										$("#ticket"+uiIndex).attr("onclick","dataUsePopUp("+data.resultData.giftIssueSeq+","+ uiIndex+")");
										
										//dataTest += ' : onclick : dataUsePopUp ';
										//$("#dataTest").text(dataTest);

									//데이터아닐때
									}else{
										//var clickEvt = ticketGiftOnlinePopUp(data.resultData.giftIssueSeq);
										//$("#ticket"+uiIndex).attr('onclick','').click(clickEvt);
										$("#ticket"+uiIndex).attr("onclick", "ticketGiftPopUp("+data.resultData.giftIssueSeq+")");

										//dataTest += ' : onclick : ticketGiftOnlinePopUp ';
										//$("#dataTest").text(dataTest);

										
									}
									
								//오프라인 상품일때
								}else if(data.resultData.giftType == "G0001"){
									//데이터일때 210929 cms에서 데이터타입 수정되면 제거
									if(data.resultData.dataGiveYn == "N"){
										$("#ticket"+uiIndex).attr("onclick","dataUsePopUp("+data.resultData.giftIssueSeq+","+ uiIndex+")");

									}else{
										
									//var clickEvt = ticketGiftOfflinePopUp(data.resultData.giftIssueSeq);
									//$("#ticket"+uiIndex).attr('onclick','').click(clickEvt);
									$("#ticket"+uiIndex).attr("onclick","ticketGiftPopUp("+data.resultData.giftIssueSeq+")");
									
									//dataTest += ' : onclick : ticketGiftOfflinePopUp ';
									//$("#dataTest").text(dataTest);
									}
								}
								
								scrollDisble();
								$("#ticket_gift_win_popup").show();
								
						//2. 꽝일시
							}else{
								$("#ticket"+uiIndex).attr('class','grey');
								
								//dataTest += ' : hasclass : '+$("#ticket"+uiIndex).hasClass('grey');
								//$("#dataTest").text(dataTest);

								$("#img"+uiIndex).attr("src", "/yapp3/res/img/lose_img.png");
								
								//dataTest += ' : img : lose ';
								//$("#dataTest").text(dataTest);

								$("#tit"+uiIndex).text("꽝");
								
								//dataTest += ' : 꽝 : ';
								//$("#dataTest").text(dataTest);

								$("#regDt"+uiIndex).text(data.resultData.ticketGiftRegDt +" 참여");
								
								//dataTest += ' : regDt : ';
								//$("#dataTest").text(dataTest);

								$("#validDt"+uiIndex).remove();
								
								//dataTest += ' : validDt : ';
								//$("#dataTest").text(dataTest);

								
								//애니메이션 효과??
								//이미지변환시키고 응모권사용 팝업창 닫기
								//당첨팝업 띄우기
								//새로고침?(세션날아가면 다른방법으로)
								scrollDisble();
								$("#ticket_gift_non_win_popup").show();
							}
						}else{
							$("#error_msg").text(data.resultMsg);	//에러 메세지
							scrollDisble();
							$("#ticket_gift_use_error_popup").show();
						}
						
						
						//$("#dataTest").text(dataTest);

					  	doubleSubmitFlag = false;
					}
				});
			});
			
			
		}); //ready end
		
		</script>
</head>
<body>
<% pageContext.setAttribute("enter", "\n"); %>
	<c:choose>
	   <c:when test="${empty rewardList and empty ticketRewardList and empty dataRewardList and empty classRewardList}">
			<div class="content" style="padding-top:27%; padding-bottom:15%;">
				<div class="layout text-center">
					<img src="/yapp3/res/img/ic_b_coupon@2x.png" alt="" width="128px;" height="128px;">
					<h2 class="tit f-yspotlightotapp">YPLAY에서<br>리워드 쿠폰을 모아보세요.</h2>
				</div>
			</div>
		</c:when>   
			
		<c:when test="${not empty rewardList or not empty ticketRewardList or not empty dataRewardList or not empty classRewardList}">
		<div class="content" style="padding-top:15%; padding-bottom:15%;">
			<div class="layout">
				<h2 class="tit f-yspotlightotapp">${fn:escapeXml(rewardCnt)} 장의 리워드 쿠폰이<br>있어요.</h2>
				<p class="f-gray">유효기간이 지나면 자동으로 사라집니다.</p>
				<div class="coupon-ul-wrap">
					<ul class="coupon-ul">
						<c:if test="${not empty rewardList }">
							<c:forEach var="list" items="${rewardList }">
								<li class="show_popup" data='<c:out value="${list.rewSeq}" escapeXml="true" />' onclick='showPopUp(<c:out value="${list.rewSeq}" escapeXml="true" />)' >
									<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
									<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
									<a href="javascript:void(0);" onfocus="blur()">
										<div class="clearfix">
											<div class="w31p img-box">
												<img src='<c:out value="${list.imgUrl}" escapeXml="true" />' alt="" width="52px;" height="50px;" >
											</div>
											<div class="w69p scroll_y">
												<p class="tit">
													<c:choose>
														<c:when test="${fn:length(list.giftName) gt 18 }">
														<c:out value="${fn:substring(list.giftName,0,17)}" escapeXml="true" />...
														</c:when>
														<c:otherwise>
															<c:out value="${list.giftName}" escapeXml="true" />
														</c:otherwise>
													</c:choose>
												</p>
												<%-- <p class="con">${fn:replace(list.giftIntro, enter, "<br/>" )}</p> --%>
												<p class="con"><c:out value="${list.issueDt}" escapeXml="true" /> 지급</p>
												<p class="con">
													<c:choose>
														<c:when test="${fn:length(list.evtTitle) gt 18 }">
														<c:out value="${fn:substring(list.evtTitle,0,17)}" escapeXml="true" />...
														</c:when>
														<c:otherwise>
															<c:out value="${list.evtTitle}" escapeXml="true" />
														</c:otherwise>
													</c:choose>
												</p>
												<p class="sub-con"><c:out value="${list.validStartDt} - ${list.validEndDt}" escapeXml="true" /> 까지</p>
											</div>
										</div>
									</a>
								</li>
							</c:forEach>
						</c:if>
						<c:if test="${not empty classRewardList }">
							<c:forEach var="list" items="${classRewardList }" >
								<li class="show_class_popup" data='<c:out value="${list.rewSeq}" escapeXml="true" />' onclick='classPopUp(<c:out value="${list.rewSeq}" escapeXml="true" />,"<c:out value="${list.classCancelYn}" escapeXml="true" />")' >
									<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
									<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
									<a href="javascript:void(0);" onfocus="blur()">
										<div class="clearfix">
											<div class="w31p img-box">
												<img src='<c:out value="${list.imgUrl}" escapeXml="true" />' alt="" width="52px;" height="50px;" >
											</div>
											<div class="w69p scroll_y">
												<p class="tit">
													<c:choose>
														<c:when test="${fn:length(list.giftName) gt 18 }">
														<c:out value="${fn:substring(list.giftName,0,17)}" escapeXml="true" />...
														</c:when>
														<c:otherwise>
															<c:out value="${list.giftName}" escapeXml="true" />
														</c:otherwise>
													</c:choose>
												</p>
												<%-- <p class="con">${fn:replace(list.giftIntro, enter, "<br/>" )}</p> --%>
												<p class="con"><c:out value="${list.issueDt}" escapeXml="true" /> 지급</p>
												<p class="con">
													<c:choose>
														<c:when test="${fn:length(list.evtTitle) gt 18 }">
														<c:out value="${fn:substring(list.evtTitle,0,17)}" escapeXml="true" />...
														</c:when>
														<c:otherwise>
															<c:out value="${list.evtTitle}" escapeXml="true" />
														</c:otherwise>
													</c:choose>
												</p>
												<p class="sub-con"><c:out value="${list.validStartDt} - ${list.validEndDt}" escapeXml="true" /> 까지</p>
											</div>
										</div>
									</a>
								</li>
							</c:forEach>
						</c:if>
						<c:if test="${not empty ticketRewardList }">
							<c:forEach var="ticketList" items="${ticketRewardList }" varStatus="status">
								<c:choose>
									<c:when test="${ticketList.winYn eq 'Y' }">
										<c:choose>
											<c:when test="${ticketList.dataGiveYn eq 'Y' }">
												<li class="grey">
													<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
													<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
													<a href="javascript:void(0);" onfocus="blur()">
														<div class="clearfix">
															<div class="w31p img-box">
																<img src="/yapp3/res/img/win_img.png" alt="" width="52px;" height="50px;" >
															</div>
															<div class="w69p scroll_y">
																<p class="tit">
																	<c:choose>
																		<c:when test="${fn:length(ticketList.ticketGiftName) gt 18 }">
																		<c:out value="${fn:substring(ticketList.ticketGiftName,0,17)}" escapeXml="true" />...
																		</c:when>
																		<c:otherwise>
																			<c:out value="${ticketList.ticketGiftName}" escapeXml="true" />
																		</c:otherwise>
																	</c:choose>
																</p>
																<%-- <p class="con">${fn:replace(ticketList.giftIntro, enter, "<br/>" )}</p> --%>
																<p class="con"><c:out value="${ticketList.dataValidStartDt }" /> 사용 완료</p>
																<p class="con">
																	<c:choose>
																		<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																		<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																		</c:when>
																		<c:otherwise>
																			<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																		</c:otherwise>
																	</c:choose>
																</p>
																<p class="sub-con"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
															</div>
														</div>
													</a>
												</li>
											</c:when>
											<c:when  test="${ticketList.dataGiveYn eq 'N' }">
												<li class="red" id="ticket${status.index }" data='<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />' onclick='dataUsePopUp(<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />,<c:out value="${status.index }" escapeXml="true" />)' >
													<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
													<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
													<a href="javascript:void(0);" onfocus="blur()">
														<div class="clearfix">
															<div class="w31p img-box">
																<img id="img${status.index }" src="/yapp3/res/img/win_img.png" alt="" width="52px;" height="50px;"  />
															</div>
															<div class="w69p scroll_y">
																<p class="tit" id="tit${status.index }">
																	<c:choose>
																		<c:when test="${fn:length(ticketList.ticketGiftName) gt 18 }">
																		<c:out value="${fn:substring(ticketList.ticketGiftName,0,17)}" escapeXml="true" />...
																		</c:when>
																		<c:otherwise>
																			<c:out value="${ticketList.ticketGiftName}" escapeXml="true" />
																		</c:otherwise>
																	</c:choose>
																</p>
																<%-- <p class="con">${fn:replace(ticketList.giftIntro, enter, "<br/>" )}</p> --%>
																<p class="con" id="regDt${status.index }"><c:out value="${ticketList.ticketGiftRegDt }" /> 응모</p>
																<p class="con">
																	<c:choose>
																		<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																		<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																		</c:when>
																		<c:otherwise>
																			<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																		</c:otherwise>
																	</c:choose>
																</p>
																<p class="sub-con" id="validDt${status.index }"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
															</div>
														</div>
													</a>
												</li>											
											</c:when>
											<c:otherwise>
												<c:if test="${ ticketList.giftType eq 'G0001'}">
													<li class="red" data='<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />' onclick='ticketGiftPopUp(<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />)' >
														<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
														<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
														<a href="javascript:void(0);" onfocus="blur()">
															<div class="clearfix">
																<div class="w31p img-box">
																	<img src="/yapp3/res/img/win_img.png" alt="" width="52px;" height="50px;" >
																</div>
																<div class="w69p scroll_y">
																	<p class="tit">
																		<c:choose>
																			<c:when test="${fn:length(ticketList.ticketGiftName) gt 18 }">
																			<c:out value="${fn:substring(ticketList.ticketGiftName,0,17)}" escapeXml="true" />...
																			</c:when>
																			<c:otherwise>
																				<c:out value="${ticketList.ticketGiftName}" escapeXml="true" />
																			</c:otherwise>
																		</c:choose>
																	</p>
																	<%-- <p class="con">${fn:replace(ticketList.giftIntro, enter, "<br/>" )}</p> --%>
																	<p class="con"><c:out value="${ticketList.ticketGiftRegDt }" /> 응모</p>
																	<p class="con">
																		<c:choose>
																			<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																			<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																			</c:when>
																			<c:otherwise>
																				<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																			</c:otherwise>
																		</c:choose>
																	</p>
																	<p class="sub-con"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
																</div>
															</div>
														</a>
													</li>
												</c:if>
												<c:if test="${ ticketList.giftType eq 'G0002'}">
													<li class="red" data='<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />' onclick='ticketGiftPopUp(<c:out value="${ticketList.giftIssueSeq}" escapeXml="true" />)' >
														<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
														<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
														<a href="javascript:void(0);" onfocus="blur()">
															<div class="clearfix">
																<div class="w31p img-box">
																	<img src="/yapp3/res/img/win_img.png" alt="" width="52px;" height="50px;" >
																</div>
																<div class="w69p scroll_y">
																	<p class="tit">
																		<c:choose>
																			<c:when test="${fn:length(ticketList.ticketGiftName) gt 18 }">
																			<c:out value="${fn:substring(ticketList.ticketGiftName,0,17)}" escapeXml="true" />...
																			</c:when>
																			<c:otherwise>
																				<c:out value="${ticketList.ticketGiftName}" escapeXml="true" />
																			</c:otherwise>
																		</c:choose>
																	</p>
																	<%-- <p class="con">${fn:replace(ticketList.giftIntro, enter, "<br/>" )}</p> --%>
																	<p class="con"><c:out value="${ticketList.ticketGiftRegDt }" /> 응모</p>
																	<p class="con">
																		<c:choose>
																			<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																			<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																			</c:when>
																			<c:otherwise>
																				<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																			</c:otherwise>
																		</c:choose>
																	</p>
																	<p class="sub-con"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
																</div>
															</div>
														</a>
													</li>
												</c:if>
											</c:otherwise>
										</c:choose>
										
									</c:when>
									<c:when test="${ticketList.winYn eq 'N' }">
										<li class="grey">
											<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<a href="javascript:void(0);" onfocus="blur()">
												<div class="clearfix">
													<div class="w31p img-box">
														<img src="/yapp3/res/img/lose_img.png" alt="" width="52px;" height="50px;" >
													</div>
													<div class="w69p scroll_y">
														<p class="tit">꽝</p>
														<p class="con"><c:out value="${ticketList.ticketGiftRegDt }" /> 참여</p>
														<p class="con">
															<c:choose>
																<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																</c:otherwise>
															</c:choose>
														</p>
													</div>
												</div>
											</a>
										</li>									
									</c:when>
									<c:otherwise>
										<c:if test="${ticketOpenYn eq 'Y' }">
											<li id="ticket${status.index }" data='<c:out value="${ticketList.ticketSeq}" escapeXml="true" />' onclick='ticketShowPopUp(<c:out value="${ticketList.ticketSeq}" escapeXml="true" />,<c:out value="${status.index }" escapeXml="true" />)' >
												<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
												<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
												<a href="javascript:void(0);" onfocus="blur()">
													<div class="clearfix">
														<div class="w31p img-box">
															<img id="img${status.index }" src="/yapp3/res/img/use_possible.png" alt="" width="52px;" height="50px;" >
														</div>
														<div class="w69p scroll_y">
															<p class="tit" id="tit${status.index }">
																<c:choose>
																	<c:when test="${fn:length(ticketList.giftName) gt 18 }">
																	<c:out value="${fn:substring(ticketList.giftName,0,17)}" escapeXml="true" />...
																	</c:when>
																	<c:otherwise>
																		<c:out value="${ticketList.giftName}" escapeXml="true" />
																	</c:otherwise>
																</c:choose>
															</p>
															<p class="con" id="regDt${status.index }"><c:out value="${ticketList.regDt }" /> 지급</p>
															<p class="con">
																<c:choose>
																	<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																	<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																	</c:when>
																	<c:otherwise>
																		<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																	</c:otherwise>
																</c:choose>															
															</p>
															<p class="sub-con" id="validDt${status.index }"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
														</div>
													</div>
												</a>
											</li>									
										</c:if>
										<c:if test="${ticketOpenYn eq 'N' }">
											<li class="grey" data='<c:out value="${ticketList.ticketSeq}" escapeXml="true" />' onclick='giftWaitPopUp()' >
												<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
												<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
												<a href="javascript:void(0);" onfocus="blur()">
													<div class="clearfix">
														<div class="w31p img-box">
															<img src='<c:out value="${ticketList.imgUrl}" escapeXml="true" />' alt="" width="52px;" height="50px;" >
														</div>
														<div class="w69p scroll_y">
															<p class="tit">
																<c:choose>
																	<c:when test="${fn:length(ticketList.giftName) gt 18 }">
																	<c:out value="${fn:substring(ticketList.giftName,0,17)}" escapeXml="true" />...
																	</c:when>
																	<c:otherwise>
																		<c:out value="${ticketList.giftName}" escapeXml="true" />
																	</c:otherwise>
																</c:choose>
															</p>
															<p class="con"><c:out value="${ticketList.regDt }" /> 지급</p>
															<p class="con">
																<c:choose>
																	<c:when test="${fn:length(ticketList.evtTitle) gt 18 }">
																	<c:out value="${fn:substring(ticketList.evtTitle,0,17)}" escapeXml="true" />...
																	</c:when>
																	<c:otherwise>
																		<c:out value="${ticketList.evtTitle}" escapeXml="true" />
																	</c:otherwise>
																</c:choose>
															</p>
															<p class="sub-con"><c:out value="${ticketList.validStartDt} - ${ticketList.validEndDt}" escapeXml="true" /> 까지</p>
														</div>
													</div>
												</a>
											</li>									
										</c:if>										
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
						<c:if test="${not empty dataRewardList }">
							<c:forEach var="dataList" items="${dataRewardList }" varStatus="dataStatus">
								<c:choose>
									<c:when test="${dataList.dataGiveYn eq 'Y' }">
										<li class="grey">
											<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<a href="javascript:void(0);" onfocus="blur()">
												<div class="clearfix">
													<div class="w31p img-box">
														<img src='<c:out value="${dataList.imgUrl}" escapeXml="true" />' alt="" width="52px;" height="50px;" >
													</div>
													<div class="w69p scroll_y">
														<p class="tit">
															<c:choose>
																<c:when test="${fn:length(dataList.giftName) gt 18 }">
																<c:out value="${fn:substring(dataList.giftName,0,17)}" escapeXml="true" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${dataList.giftName}" escapeXml="true" />
																</c:otherwise>
															</c:choose>
														</p>
														<p class="con"><c:out value="${dataList.dataValidStartDt }" /> 사용 완료</p>
														<p class="con">
															<c:choose>
																<c:when test="${fn:length(dataList.evtTitle) gt 18 }">
																<c:out value="${fn:substring(dataList.evtTitle,0,17)}" escapeXml="true" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${dataList.evtTitle}" escapeXml="true" />
																</c:otherwise>
															</c:choose>
														</p>
														<p class="sub-con"><c:out value="${dataList.validStartDt} - ${dataList.validEndDt}" escapeXml="true" /> 까지</p>
													</div>
												</div>
											</a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="red" id="data${dataStatus.index }" data='<c:out value="${dataList.dataRewSeq}" escapeXml="true" />' onclick='dataRewardUsePopUp(<c:out value="${dataList.dataRewSeq}" escapeXml="true" />,<c:out value="${dataStatus.index }" escapeXml="true" />)' >
											<div class="cir left"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<div class="cir right"><div class="half-cir-wrap"><div class="half-cir"></div></div></div>
											<a href="javascript:void(0);" onfocus="blur()">
												<div class="clearfix">
													<div class="w31p img-box">
														<img src='<c:out value="${dataList.imgUrl}" escapeXml="true" />' alt="" width="52px;" height="50px;"  />
													</div>
													<div class="w69p scroll_y">
														<p class="tit">
															<c:choose>
																<c:when test="${fn:length(dataList.giftName) gt 18 }">
																<c:out value="${fn:substring(dataList.giftName,0,17)}" escapeXml="true" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${dataList.giftName}" escapeXml="true" />
																</c:otherwise>
															</c:choose>
														</p>
														<p class="con" id="dataReg${dataStatus.index }"><c:out value="${dataList.issueDt }" /> 지급</p>
														<p class="con">
															<c:choose>
																<c:when test="${fn:length(dataList.evtTitle) gt 18 }">
																<c:out value="${fn:substring(dataList.evtTitle,0,17)}" escapeXml="true" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${dataList.evtTitle}" escapeXml="true" />
																</c:otherwise>
															</c:choose>
														</p>
														<p class="sub-con"><c:out value="${dataList.validStartDt} - ${dataList.validEndDt}" escapeXml="true" /> 까지</p>
													</div>
												</div>
											</a>
										</li>		
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</ul>
				</div>
			</div>
			<input type="hidden" name="cntrNo" value=''>
			<input type="hidden" name="ysid" value=''>
			<input type="hidden" name="autoLogin" value=''>
			<input type="hidden" name="osTp" value=''>
			<input type="hidden" name="appVrsn" value=''>
			<input type="hidden" name="ticketSeq" value=''>
			<input type="hidden" name="infYn" value=''>
			<input type="hidden" name="giftIssueSeq" value=''>
			<input type="hidden" id="uiIndex" value=''>
			<input type="hidden" name="dataRewSeq" value=''>
			<input type="hidden" id="dataUiIndex" value=''>
			<input type="hidden" id="issueSeq" name="issueSeq" value=""/>
			<input type="hidden" id="joinSeq" name="joinSeq" value=""/>
			<input type="hidden" id="classRewSeq" name="classRewSeq" value=""/>
		</div>

		<!-- 하단 고정 팝업 -->
		<div id="cupn_popup" class="popup" style="z-index:2">
			<!-- 배경 커버(블랙) -->
			<div id="cover2" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>쿠폰번호를 복사하였습니다.</p>
					<p> 쿠폰을 등록하기 위해  이동하시겠습니까?</p>
					<p id="cupn_copy" class="f-gray sub-con"></p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="btn_cancel" class="btn w50p btn-high" onfocus="blur()">취소</button>
					<a href="javascript:void(0);" onclick="land_url(this)" onfocus="blur()"><button type="button" class="btn w50p btn-high" onfocus="blur()">이동</button></a>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //하단 고정 팝업 -->
		
		<!-- 중앙 팝업 -->
		<div id="reward_popup" class="popup" style="z-index:1">
			<!-- 배경 커버(블랙) -->
			<div id="cover1" class="cover"></div>
			<button type="button" class="btn hide-btn" id="rewardPopClose" onfocus="blur()"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct">
				<div class="pop-con">
					<div class="pop-tit-con">
						<img id="cupn_img" src="" alt="" width="108px;" height="108px;">
						<p id="title" class="tit"></p>
						<p id="cupn_intro" class="con"></p>
						<p id="valid_dt" class="sub-con f-gray"></p>
					</div>
					<div class="pop-content">
						<div class="bg-skyblue box-round">
							<p id="cupn_num" class="f-bold"></p>
							<button id="foot_pop" class="btn btn-sm round-btn line-bk-btn" onfocus="blur()">쿠폰번호 복사</button>
							<p id="cupn_pw" class="f-bold"></p>
						</div>

						<div class="scroll text-left">
							<ul>
								<li id="cupn_use_info_yn">
									<h6>사용안내</h6>
									<ul id="cupn_use_info" class="ul-disc">
										
									</ul>
								</li>
								<li id="cupn_etc_yn">
									<h6>유의사항</h6>
									<ul id="cupn_etc" class="ul-disc">
										
									</ul>
								</li>
								<li id="moveEvent">
									<a href="javascript:void(0);" onfocus="blur()" onclick="javascript:goEventDetail()"><img src="/yapp3/res/img/btn_s_eventgo@2x_01.png" alt="" width="120px;" height="26px;"/></a>
								</li>
							</ul>
						</div>
						
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<a href="javascript:void(0);" onclick="land_url(this)" onfocus="blur()"><button type="button" class="btn w100p" id="regBtn">등록하러 가기</button></a>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //중앙 팝업 -->
		
		<!-- 중앙 팝업 -->
		<div id="url_reward_popup" class="popup" style="z-index:1">
			<!-- 배경 커버(블랙) -->
			<div id="cover1" class="cover"></div>
			<button type="button" class="btn hide-btn" id="urlRewardPopClose" onfocus="blur()"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct" style="padding-bottom:25px;">
				<div class="pop-con">
					<div class="pop-tit-con">
						<img id="url_cupn_img" src="" alt="" width="108px;" height="108px;">
						<p id="url_title" class="tit"></p>
						<p id="url_cupn_intro" class="con"></p>
						<p id="url_valid_dt" class="sub-con f-gray"></p>
					</div>
					<div class="pop-content">
						<div class="bg-skyblue box-round" style="padding:0%">
				
							<a href="javascript:void(0);" onclick="land_url(this)" onfocus="blur()">
								<button type="button" class="btn w100 f-bold" id="urlBtn" style="padding:10% 8%; width:100%; border-radius:1rem;">혜택 받기</button>
							</a>
							<p id="url_cupn_pw" class="f-bold"></p>
						</div>

						<div class="scroll text-left">
							<ul>
								<li id="url_cupn_use_info_yn">
									<h6>사용안내</h6>
									<ul id="url_cupn_use_info" class="ul-disc">
										
									</ul>
								</li>
								<li id="url_cupn_etc_yn">
									<h6>유의사항</h6>
									<ul id="url_cupn_etc" class="ul-disc">
										
									</ul>
								</li>
								<li id="moveEvent">
									<a href="javascript:void(0);" onfocus="blur()" onclick="javascript:goEventDetail()"><img src="/yapp3/res/img/btn_s_eventgo@2x_01.png" alt="" width="120px;" height="26px;"/></a>
								</li>
							</ul>
						</div>
					</div>
				</div>
				
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //중앙 팝업 -->
		
		
		<!-- 중앙 팝업 -->
		<div id="class_reward_popup" class="popup" style="z-index:1">
			<!-- 배경 커버(블랙) -->
			<div id="cover1" class="cover"></div>
			<button type="button" class="btn hide-btn" id="classRewardPopClose" onfocus="blur()"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct">
				<div class="pop-con">
					<div class="pop-tit-con">
						<img id="class_cupn_img" src="" alt="" width="108px;" height="108px;">
						<p id="class_title" class="tit"></p>
						<p id="class_cupn_intro" class="con"></p>
						<p id="class_valid_dt" class="sub-con f-gray"></p>
					</div>
					<div class="pop-content">
						<div class="bg-skyblue box-round">
							<p id="class_cupn_num" class="f-bold"></p>
							<button id="class_foot_pop" class="btn btn-sm round-btn line-bk-btn" onfocus="blur()">쿠폰번호 복사</button>
							<p id="class_cupn_pw" class="f-bold"></p>
						</div>

						<div class="scroll text-left">
							<ul>
								<li id="class_cupn_use_info_yn">
									<h6>사용안내</h6>
									<ul id="class_cupn_use_info" class="ul-disc">
										
									</ul>
								</li>
								<li id="class_cupn_etc_yn">
									<h6>유의사항</h6>
									<ul id="class_cupn_etc" class="ul-disc">
										
									</ul>
								</li>
								<li id="class_moveEvent">
									<a href="javascript:void(0);" onfocus="blur()" onclick="javascript:goEventDetail()"><img src="/yapp3/res/img/btn_s_eventgo@2x_01.png" alt="" width="120px;" height="26px;"/></a>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<a href="javascript:void(0);" id="deleteClassRew" onclick="deleteClassRew()" onfocus="blur()"><button type="button" class="btn w100p" id="classRegBtn">취소하기</button></a>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //중앙 팝업 -->
		
				
		<!-- 210902 응모권 온라인당첨 상세 -->
		<div id="ticket_online_popup" class="popup" style="z-index:1">
			<!-- 배경 커버(블랙) -->
			<div id="ticket_cover1" class="cover"></div>
			<button type="button" class="btn hide-btn" id="ticketGiftOnlineClose" onfocus="blur()"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct">
				<div class="pop-con">
					<div class="pop-tit-con">
						<img id="ticket_cupn_img" src="" alt="" width="108px;" height="108px;">
						<p id="ticket_title" class="tit"></p>
						<p id="ticket_cupn_intro" class="con"></p>
						<p id="ticket_valid_dt" class="sub-con f-gray"></p>
					</div>
					<div class="pop-content">
						<div class="bg-skyblue box-round">
							<p id="ticket_cupn_num" class="f-bold"></p>
							<button id="ticket_foot_pop" class="btn btn-sm round-btn line-bk-btn" onfocus="blur()">쿠폰번호 복사</button>
							<p id="ticket_cupn_pw" class="f-bold"></p>
						</div>

						<div class="scroll text-left">
							<ul>
								<li id="ticket_cupn_use_info_yn">
									<h6>사용안내</h6>
									<ul id="ticket_cupn_use_info" class="ul-disc">
										
									</ul>
								</li>
								<li id="ticket_cupn_etc_yn">
									<h6>유의사항</h6>
									<ul id="ticket_cupn_etc" class="ul-disc">
										
									</ul>
								</li>
							</ul>
						</div>
						
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<a href="javascript:void(0);" onclick="land_url(this)" onfocus="blur()"><button type="button" class="btn w100p" id="ticket_online_confirm">등록하러 가기</button></a>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //210902 응모권 온라인당첨 상세 -->
		
		<!-- 210812 응모권당첨 오프라인상품 상세 팝업 -->
		<div id="ticket_offline_popup" class="popup" style="z-index:1">
			<!-- 배경 커버(블랙) -->
			<div id="off_cover" class="cover"></div>
			<button type="button" class="btn hide-btn" id="ticketGiftOfflineClose" onfocus="blur()"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct">
				<div class="pop-con">
					<div class="pop-tit-con">
						<img id="ticket_off_cupn_img" src="" alt="" width="108px;" height="108px;">
						<p id="ticket_off_title" class="tit"></p>
						<p id="ticket_off_cupn_intro" class="con"></p>
						<p id="ticket_off_valid_dt" class="sub-con f-gray"></p>
					</div>
					<div class="pop-content">
						<div class="bg-skyblue box-round">
							<p id="delivery_info" class="text-left sub-con"></p>
							<button onclick="deliveryPop();" id="deliveryBtn" class="btn btn-sm round-btn line-bk-btn" onfocus="blur()"><span id="addrBtnName">주소 입력하기</span></button>
						</div>

						<div class="scroll text-left">
							<ul>
								<li id="ticket_off_cupn_use_info_yn">
									<h6>사용안내</h6>
									<ul id="ticket_off_cupn_use_info" class="ul-disc">
										
									</ul>
								</li>
								<li id="ticket_off_cupn_etc_yn">
									<h6>유의사항</h6>
									<ul id="ticket_off_cupn_etc" class="ul-disc">
										
									</ul>
								</li>
							</ul>
						</div>
					</div>
					<div class="pop-btn-wrap bg-darkgray">
						<button type="button" class="btn w100p" id="ticket_offline_confirm">확인</button>
					</div>					
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- // 210812 응모권당첨 오프라인상품 상세 팝업 -->
		
		<!-- 210812 응모권 오프라인당첨 배송지정보 EVE-01-POP10_배송지정보입력 (서버에 올릴땐 우편번호에 readOnly 추가)-->
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
							<li><input type="text" id="recvMobileNo" name="recvMobileNo" placeholder="전화번호 입력" maxlength="11"></li>
							<li id="wrapPost" style="display:none;border:1px solid;width:100%;height:300px;margin:5px;position:relative;clear: both;" >
								<img src="/yapp3/res/img/icon_popClose.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1;height:17px;width:17px;" onclick="foldPostCode()" alt="접기버튼">
							 </li>
							<li><input type="text" id="post" name="post" placeholder="우편번호" readOnly="readonly" onclick="execPost()" ><a href="javascript:void(0);" onclick="execPost()" onfocus="blur()" style="text-decoration: none; color: black;">우편번호 검색</a></li>
							<li><input type="text" id="addr" name="addr" placeholder="주소"></li>
							<li><input type="text" id="addrDtl" name="addrDtl" placeholder="상세주소" maxlength="80"></li>
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
		
		<!-- //210817 응모권회차 없을 때 팝업 -->
		<div id="ticket_gift_wait_popup" class="popup" style="z-index:2">
			<!-- 배경 커버(블랙) -->
			<div id="ticket_gift_wait_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>응모권 상품 준비중입니다.<br>응모권상품이 준비되면 다시 이용해주세요.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="wait_ok" class="btn w100p" onfocus="blur()">확인</button>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //210817 응모권회차 없을 때 팝업 -->
		
		<!-- //210901 응모권 사용 에러 팝업 -->
		<div id="ticket_gift_use_error_popup" class="popup" style="z-index:2">
			<!-- 배경 커버(블랙) -->
			<div id="ticket_gift_wait_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p id="error_msg">응모권 사용 중 문제가 생겼습니다.<br>잠시후, 다시 이용해주세요.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="ticket_use_error_ok" class="btn w100p" onfocus="blur()">확인</button>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //210901 응모권 사용 에러 팝업 -->
		
		<!--210901 응모권 당첨 팝업 -->
		<div id="ticket_gift_win_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="ticket_gift_win_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt con-event">
				<div class="pop-con">
					<div class="pop-content">
						<img src="/yapp3/res/img/01_win.gif" alt="" width="108px;" height="108px;">
						<p class="tit"><span class="f-red" id="gift_name"></span> 당첨되었습니다!</p>
						<p class="con f-gray">리워드를 확인해주세요.</p>
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="win_confirm" class="btn" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
		</div>
		<!-- //-210901 응모권 당첨 팝업  -->
		
		<!--210901 응모권 꽝 팝업 -->
		<div id="ticket_gift_non_win_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div id="ticket_gift_non_win_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt con-event">
				<div class="pop-con">
					<div class="pop-content">
						<img src="/yapp3/res/img/02_lose.gif" alt="">
						<p class="tit">꽝!</p>
						<p class="con f-gray">다음 기회를 노려보세요!</p>
					</div>				
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="non_win_confirm" class="btn" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
		</div>
		<!-- //-210901 응모권 꽝 팝업  -->		
		
		<!-- //210817  응모권을 사용 -->
		<div id="ticket_use_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con con-event">
					<div class="pop-content">
						<img src="/yapp3/res/img/00_ready.gif" alt="">
						<p class="tit" >응모권을 사용하시겠습니까?</p>
						<p class="con f-gray" id="dataValue">확인을 누르시면 사용이 됩니다.</p>
						<p><a href="#" onClick="ticketUseNotice();" class="sub-con f-red">*유의사항 및 당첨가능 상품보기</a></p>		
					</div>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="ticket_cancel" class="btn w50p">취소</button>
					<button type="button" id="ticket_use" class="btn w50p">확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- // 210817 응모권을 사용 -->		
		
		<!-- //210902 데이터 지급받기 -->
		<div id="ticket_gift_data_use_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>데이터 100MB 쿠폰을 사용하시겠습니까?</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="ticket_data_cancel" class="btn w50p">취소</button>
					<button type="button" id="ticket_data_use" class="btn w50p">사용</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- // 210817 응모권을 사용 -->	
		<!-- //210903 응모권 데이터 사용 완료 팝업 -->
		<div id="data_use_complete_popup" class="popup" style="z-index:2">
			<!-- 배경 커버(블랙) -->
			<div id="data_use_complete_cover" class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>데이터가 지급되었습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="data_use_ok" class="btn w100p" onfocus="blur()">확인</button>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //210903 응모권 데이터 사용 완료 팝업 -->
		<!-- 211208 데이터 쿠폰 사용 -->
		<div id="data_reward_use_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>데이터 쿠폰을 사용하시겠습니까?</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="data_reward_cancel" class="btn w50p">취소</button>
					<button type="button" id="data_reward_use" class="btn w50p">사용</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!--// 211208 데이터 쿠폰 사용 -->
		<!-- EVE-01-POP5_정보미입력 210907 -->
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
		
		<!-- 210910 유의사항 팝업 -->
		<div id="ticket_use_notice_popup" class="popup">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<button type="button" id="ticketUseNoticeClose" class="btn hide-btn"><img src="/yapp3/res/img/ic_s_close_w.png" alt=""></button>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-ct">
				<iframe src="https://ybox.kt.com/ticket_notice.html" frameborder="0" border="0" style="filter: Alpha(Opacity=65);" width="100%"></iframe>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="ticket_use_notice_close" class="btn w100p">닫기</button>
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //210910 유의사항 팝업 -->
		<!-- 23.04.13 수강 상품 취소 Popup -->
		<div id="delete_class_reward_popup" class="popup" style="z-index:4;">
		<!-- 배경 커버(블랙) -->
		<div class="cover"></div>
		<!-- //배경 커버(블랙) -->
		<!-- 팝업 내용 -->
		<div class="pop-bt">
			<div class="pop-con">
				<p>해당 수강을 취소 하시겠습니까?</p>
			</div>
				<div class="pop-btn-wrap bg-darkgray">
				<button type="button" id="btnCancel" class="btn w50p" onclick="btnDeleteClassRewCancel()" onfocus="blur()">취소</button>
				<button type="button" id="btnModify" class="btn w50p" onclick="btnDeleteClassRew()" onfocus="blur()">확인</button>
				<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
			</div>
		</div>
		<!-- //팝업 내용 -->
		</div>
		<!-- //23.04.13 수강 상품 취소 Popup End-->
		<!-- //23.04.13 수강 취소 완료 Popup -->
		<div id="reward_delete_popup" class="popup" style="z-index:4;">
			<!-- 배경 커버(블랙) -->
			<div class="cover"></div>
			<!-- //배경 커버(블랙) -->
			<!-- 팝업 내용 -->
			<div class="pop-bt">
				<div class="pop-con">
					<p>클래스 신청이 취소 되었습니다.</p>
				</div>
				<div class="pop-btn-wrap bg-darkgray">
					<button type="button" id="info_refresh" class="btn w100p" onfocus="blur()" >확인</button>
					<!-- button에 btn-high 클래스를 추가하면 아래 여백이 커집니다 -->
				</div>
			</div>
			<!-- //팝업 내용 -->
		</div>
		<!-- //23.04.13 수강 취소 완료 Popup End -->
		</c:when>
	</c:choose>	
</body>

<script type="text/javascript">
//210812 배송지 추가 시작
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
//210812 배송지 추가 끝

 	var landUrl="";
 	var cntrNo="";
	var ysid="";
	var autoLogin="";
	var osTp="";
	var appVrsn="";
	var evtSeq="";
	var evtType="";
	var evtTypeNm="";
	var rewardType = "";
	var urlId = "";

/*리워드 상세 조회 팝업 */ 
function showPopUp(rew_seq){
	var rewSeq=rew_seq;
	$.ajax({
		type : "get",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "rewardInfo",
		data : {"rewSeq":rewSeq},
		success : function(data){
			evtSeq=data.evtSeq;
			evtType=data.evtType;
			evtTypeNm=data.evtTypeNm;
			landUrl=data.landUrl;
			
			rewardType = data.rewardType;

			$("#cupn_etc").empty();
			$("#cupn_use_info").empty();
			$("#url_cupn_etc").empty();
			$("#url_cupn_use_info").empty();
			
			if(landUrl == null || landUrl == ""){
				$("#regBtn").attr("disabled", "disabled");
				
				//20210723 landUrl값이 없을시 쿠폰번호 복사 버튼 비활성화
				//$("#foot_pop").attr("disabled", "disabled");
				//20210723
				$("#foot_pop").hide();
				
			}else{
				$("#regBtn").removeAttr("disabled");
				$("#foot_pop").show();
				
			}
				//211103
				var landBtnTxt = data.landButtonText;
				
				if(!isEmpty(landBtnTxt)){
					$("#regBtn").html(landBtnTxt);
				}else{
					$("#regBtn").html("등록하러 가기");
				}
				//211103 end
			
				scrollDisble();
				
				if(rewardType == "G0002"){ //230515 리워드 타입  url일 경우 
					landUrl = data.urlId;
					$('#url_reward_popup').show();
					document.getElementById('url_cupn_img').src = data.imgUrl;
					$("#url_title").html(data.giftName);										//쿠폰 이름
					var giftIntro = data.giftIntro;
					$("#url_cupn_intro").html(giftIntro.replace(/(?:\r\n|\r|\n)/g, '<br />'));
					$("#url_valid_dt").html(data.validStartDt+" - "+data.validEndDt+" 까지");		//유효 기간 2020.03.03 - 2020.06.31 까지
					
					
					if(data.pwYn=="Y"){
						$("#url_cupn_pw").show();
						$("#url_cupn_pw").html("비밀번호 : "+data.rewPw);										//쿠폰 번호		
					}else{
						$("#url_cupn_pw").hide();
					}
					
					var cupnEtc=data.cupnEtc;
					if(cupnEtc.search("\n")==-1){
						
						if(data.cupnEtcYn=="Y"){	
							var li=$("<li>");
							$("#url_cupn_etc_yn").show();
							$("#url_cupn_etc").append(li.append(cupnEtc));						//쿠폰 사용안내
					}
						
					}else{
						
						var cupnEtcArr=cupnEtc.split("\n")
						//쿠폰유의사항 여부
						if(data.cupnEtcYn=="Y"){	
							
							for(var i in cupnEtcArr){
							var li=$("<li>");
							$("#url_cupn_etc_yn").show();
							$("#url_cupn_etc").append(li.append(cupnEtcArr[i]));						//쿠폰 사용안내
							}
						}
					}
					
					var cupnUseInfo=data.cupnUseInfo;
					var cupnUseInfoArr=cupnUseInfo.split("\n");
					//쿠폰사용안내 여부
					if(data.cupnUseInfoYn=="Y"){
						
						for(var i in cupnUseInfoArr){
						var li=$("<li>");
						$("#url_cupn_use_info_yn").show();
							$("#url_cupn_use_info").append(li.append(cupnUseInfoArr[i]));		//쿠폰 사용안내
						}
					}

					//팝업창 열기
				}else{
					$('#reward_popup').show();		
					/* $('.hide-btn').parents('.popup').show(); */
					document.getElementById('cupn_img').src=data.imgUrl;		//쿠폰 이미지
					$("#title").html(data.giftName);										//쿠폰 이름
					var giftIntro = data.giftIntro;
					$("#cupn_intro").html(giftIntro.replace(/(?:\r\n|\r|\n)/g, '<br />'));									//쿠폰 설명
					$("#valid_dt").html(data.validStartDt+" - "+data.validEndDt+" 까지");		//유효 기간 2020.03.03 - 2020.06.31 까지
					$("#cupn_num").html(data.rewId);										//쿠폰 번호
					$("#cupn_copy").html("번호 : "+data.rewId)								//쿠폰복사 팝업
				

					if(data.pwYn=="Y"){
						$("#cupn_pw").show();
						$("#cupn_pw").html("비밀번호 : "+data.rewPw);										//쿠폰 번호		
					}else{
						$("#cupn_pw").hide();
					}
					
					var cupnEtc=data.cupnEtc;
					if(cupnEtc.search("\n")==-1){
						
						if(data.cupnEtcYn=="Y"){	
							var li=$("<li>");
							$("#cupn_etc_yn").show();
							$("#cupn_etc").append(li.append(cupnEtc));						//쿠폰 사용안내
					}
						
					}else{
						
						var cupnEtcArr=cupnEtc.split("\n")
						//쿠폰유의사항 여부
						if(data.cupnEtcYn=="Y"){	
							
							for(var i in cupnEtcArr){
							var li=$("<li>");
							$("#cupn_etc_yn").show();
							$("#cupn_etc").append(li.append(cupnEtcArr[i]));						//쿠폰 사용안내
							}
						}
					}
					
					var cupnUseInfo=data.cupnUseInfo;
					var cupnUseInfoArr=cupnUseInfo.split("\n");
					//쿠폰사용안내 여부
					if(data.cupnUseInfoYn=="Y"){
						
						for(var i in cupnUseInfoArr){
						var li=$("<li>");
						$("#cupn_use_info_yn").show();
							$("#cupn_use_info").append(li.append(cupnUseInfoArr[i]));		//쿠폰 사용안내
						}
					}
				}
				
				
		},
		error : function(error){
			console.log(error);
		}
	}); //ajax End
	
}

/*클래스 리워드 상세 조회 팝업 */ 
function classPopUp(rew_seq, class_cancel_yn){
	var rewSeq=rew_seq;
	var classCancelYn=class_cancel_yn;
	$('#classRewSeq').val(rewSeq);
	
	//취소불가클래스라면
	if(class_cancel_yn=='N'){
		$('#deleteClassRew').attr("disabled", true)
		$('#classRegBtn').css("color", "#BDBDBD");
	}else{
		$('#deleteClassRew').attr("disabled", false);
		$('#classRegBtn').css("color", "#FFFFFF");
	}
	
	$.ajax({
		type : "get",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "classRewardInfo",
		data : {"rewSeq":rewSeq},
		success : function(data){
			evtSeq=data.evtSeq;
			evtType=data.evtType;
			evtTypeNm=data.evtTypeNm;
			landUrl=data.landUrl;
			
			//joinSeq값 가져오기
			$('#issueSeq').val(data.issueSeq);
			$('#joinSeq').val(data.joinSeq);
			
			$("#class_cupn_etc").empty();
			$("#class_cupn_use_info").empty();
			
			if(landUrl == null || landUrl == ""){
				$("#class_regBtn").attr("disabled", "disabled");
				
				//20210723 landUrl값이 없을시 쿠폰번호 복사 버튼 비활성화
				//$("#foot_pop").attr("disabled", "disabled");
				//20210723
				$("#class_foot_pop").hide();
				
			}else{
				$("#class_regBtn").removeAttr("disabled");
				$("#class_foot_pop").show();
				
			}
				//211103
				var landBtnTxt = data.landButtonText;
				
				if(!isEmpty(landBtnTxt)){
					$("#class_regBtn").html(landBtnTxt);
				}else{
					$("#class_regBtn").html("등록하러 가기");
				}
				//211103 end
			
				scrollDisble();
				
				$('#class_reward_popup').show();								//팝업창 열기
				/* $('.hide-btn').parents('.popup').show(); */
				document.getElementById('class_cupn_img').src=data.imgUrl;		//쿠폰 이미지
				$("#class_title").html(data.giftName);										//쿠폰 이름
				var giftIntro = data.giftIntro;
				$("#class_cupn_intro").html(giftIntro.replace(/(?:\r\n|\r|\n)/g, '<br />'));									//쿠폰 설명
				$("#class_valid_dt").html(data.validStartDt+" - "+data.validEndDt+" 까지");		//유효 기간 2020.03.03 - 2020.06.31 까지
				$("#class_cupn_num").html(data.rewId);										//쿠폰 번호
				$("#class_cupn_copy").html("번호 : "+data.rewId)								//쿠폰복사 팝업
				
				var cupnEtc=data.cupnEtc;
				if(cupnEtc.search("\n")==-1){
					
					if(data.cupnEtcYn=="Y"){	
						var li=$("<li>");
						$("#class_cupn_etc_yn").show();
						$("#class_cupn_etc").append(li.append(cupnEtc));						//쿠폰 사용안내
				}
					
				}else{
					
					var cupnEtcArr=cupnEtc.split("\n")
					//쿠폰유의사항 여부
					if(data.cupnEtcYn=="Y"){	
						
						for(var i in cupnEtcArr){
						var li=$("<li>");
						$("#class_cupn_etc_yn").show();
						$("#class_cupn_etc").append(li.append(cupnEtcArr[i]));						//쿠폰 사용안내
						}
					}
				}
				
				var cupnUseInfo=data.cupnUseInfo;
				var cupnUseInfoArr=cupnUseInfo.split("\n");
				//쿠폰사용안내 여부
				if(data.cupnUseInfoYn=="Y"){
					
					for(var i in cupnUseInfoArr){
					var li=$("<li>");
					$("#class_cupn_use_info_yn").show();
						$("#class_cupn_use_info").append(li.append(cupnUseInfoArr[i]));		//쿠폰 사용안내
					}
				}
				
		},
		error : function(error){
			console.log(error);
		}
	}); //ajax End
	
}

/* 리워드 쿠폰 복사 팝업*/
$("#foot_pop").click(function(){
	
	var cupnNum = document.getElementById('cupn_num').innerHTML;
	var t=document.createElement("textarea");
	document.body.appendChild(t);
	t.value=cupnNum;
	t.select();
	document.execCommand('copy');
	document.body.removeChild(t);
	scrollDisble();
	//$(".pop-bt").parents(".popup").show();	//팝업창 열기
	$("#cupn_popup").show();	//팝업창 열기
	
});

/* 응모권 리워드 쿠폰 복사 팝업*/
$("#ticket_foot_pop").click(function(){
	
	var cupnNum = document.getElementById('ticket_cupn_num').innerHTML;
	var t=document.createElement("textarea");
	document.body.appendChild(t);
	t.value=cupnNum;
	t.select();
	document.execCommand('copy');
	document.body.removeChild(t);
	scrollDisble();
	//$(".pop-bt").parents(".popup").show();	//팝업창 열기
	$("#cupn_popup").show();	//팝업창 열기
	
});

//이벤트 상세페이지 이동
function goEventDetail(){
	 var title="";
	 title=evtTypeNm;
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
			evtSeq : evtSeq + "",
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

function land_url(aTag){
	$(aTag).attr("href",landUrl).attr("target","_blank");
	//aTag.target="_blank";
	//aTag.href=landUrl;
}

//단말 분류
function isIOS(){
	var userAgent = navigator.userAgent.toLowerCase();
	return ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1) || (userAgent.search("ipad") > -1));
}

//210817 응모권회차가 없을때 팝업
function giftWaitPopUp(){
	scrollDisble();
	$("#ticket_gift_wait_popup").show();
}

//210817 응모권 사용 팝업
function ticketShowPopUp(ticketSeq, index){
	$('[name=ticketSeq]').val(ticketSeq);
	$("#uiIndex").val(index);
	
	//$("#dataValue").text("데이터 확인 : "+index +": 데이터 확인 2 :"+$("#uiIndex").val());

	scrollDisble();
	callingPlan();
	
}

//무한요금제 체크 211019
function callingPlan(){
	$.ajax({
		type : "post",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "callingPlan",
		success : function(data){
			if(data.resultCd=="200"){
				console.log("data : "+JSON.stringify(data));
				console.log("data.resultData : "+JSON.stringify(data.resultData));
				console.log("data.resultCd : "+data.resultCd);
				
				$('[name=infYn]').val(data.resultData);
				
				$("#ticket_use_popup").show();
				
			}//200 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
				$("#ticket_gift_use_error_popup").show();
				
			}
		},
		error : function(error){
			console.log(error);
		}
	}); //ajax End 
}

//210901 응모권 당첨 온/오프라인 상품 상세 팝업
function ticketGiftPopUp(giftIssueSeq){
	$('[name=giftIssueSeq]').val(giftIssueSeq);
	//ajax
	$.ajax({
		type : "get",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		url : "ticketRewardInfo",
		data : {"giftIssueSeq":giftIssueSeq},
		success : function(data){
			
			//오프라인
			if(data.giftType == "G0001"){
				scrollDisble();
				$("#ticket_offline_popup").show();	
				
				$("#ticket_off_cupn_etc").empty();
				$("#ticket_off_cupn_use_info").empty();
				
				document.getElementById('ticket_off_cupn_img').src=data.giftImgUrl;					//쿠폰 이미지
				$("#ticket_off_title").html(data.giftName);											//쿠폰 이름
				var giftIntro = data.giftIntro;
				$("#ticket_off_cupn_intro").html(giftIntro.replace(/(?:\r\n|\r|\n)/g, '<br />'));									//쿠폰 설명
				$("#ticket_off_valid_dt").html(data.validStartDt+" - "+data.validEndDt+" 까지");		//유효 기간 2020.03.03 - 2020.06.31 까지
				
				var delivery_info = '';
				var recvName = data.recvName;
				var recvMobileNo = data.recvMobileNo;
				var recvPost = data.recvPost;
				var recvAddr = data.recvAddr;
				var recvAddrDetail = data.recvAddrDetail;
				
				if(!isEmpty(recvName) && !isEmpty(recvMobileNo) &&  !isEmpty(recvPost) &&  !isEmpty(recvAddr) ){
					delivery_info += recvName+'<br>';
					delivery_info += recvMobileNo+'<br>';
					delivery_info += recvPost+'<br>';
					delivery_info += recvAddr+'<br>';
					delivery_info += recvAddrDetail+'<br>';
					
					$("#delivery_info").html(delivery_info);
					
					$("#recvName").val(recvName);
					$("#recvMobileNo").val(recvMobileNo);
					$("#post").val(recvPost);
					$("#addr").val(recvAddr);
					$("#addrDtl").val(recvAddrDetail);
					
					//210913 일단 수정하기는 안보이게
					/* $("#addrBtnName").text('주소 수정하기'); */
					//$("#addrBtnName").hide();
					$("#deliveryBtn").hide();
					
				}else{
					$("#delivery_info").empty();
					
					$("#deliveryBtn").show();
					$("#addrBtnName").text('주소 입력하기');
				}
				
				
				var cupnEtc=data.giftNotice;
				if(cupnEtc.search("\n")==-1){
					if(data.giftNotice != null && data.giftNotice!=""){	
						var li=$("<li>");
						$("#ticket_off_cupn_etc_yn").show();
						$("#ticket_off_cupn_etc").append(li.append(cupnEtc));						//쿠폰 사용안내
				}
					
				}else{
					var cupnEtcArr=cupnEtc.split("\n")
					//쿠폰유의사항 여부
					if(data.giftNotice != null && data.giftNotice!=""){	
						
						for(var i in cupnEtcArr){
						var li=$("<li>");
						$("#ticket_off_cupn_etc_yn").show();
						$("#ticket_off_cupn_etc").append(li.append(cupnEtcArr[i]));						//쿠폰 사용안내
						}
					}
				}
				
				var cupnUseInfo=data.giftManual;
				var cupnUseInfoArr=cupnUseInfo.split("\n");
				//쿠폰사용안내 여부
				if(data.giftManual !=null && data.giftManual != ""){
					
					for(var i in cupnUseInfoArr){
					var li=$("<li>");
					$("#ticket_off_cupn_use_info_yn").show();
						$("#ticket_off_cupn_use_info").append(li.append(cupnUseInfoArr[i]));		//쿠폰 사용안내
					}
				}
				
			//온라인
			}else if(data.giftType == "G0002"){
				
				landUrl=data.landUrl;
				if(landUrl == null || landUrl == ""){
					$("#ticket_online_confirm").attr("disabled", "disabled");
					$("#ticket_foot_pop").hide();
				}else{
					$("#ticket_online_confirm").removeAttr("disabled");
					$("#ticket_foot_pop").show();
				}
				
				scrollDisble();
				$("#ticket_online_popup").show();												//팝업창 열기
				
				$("#ticket_cupn_etc").empty();
				$("#ticket_cupn_use_info").empty();
				
				/* $('.hide-btn').parents('.popup').show(); */
				document.getElementById('ticket_cupn_img').src=data.giftImgUrl;					//쿠폰 이미지
				$("#ticket_title").html(data.giftName);											//쿠폰 이름
				var giftIntro = data.giftIntro;
				$("#ticket_cupn_intro").html(giftIntro.replace(/(?:\r\n|\r|\n)/g, '<br />'));	//쿠폰 설명
				$("#ticket_valid_dt").html(data.validStartDt+" - "+data.validEndDt+" 까지");		//유효 기간 2020.03.03 - 2020.06.31 까지
				$("#ticket_cupn_num").html(data.couponNo);										//쿠폰 번호
				$("#ticket_cupn_copy").html("번호 : "+data.couponNo)								//쿠폰복사 팝업
				
				if(data.pwYn=="Y"){
					$("#ticket_cupn_pw").show();
					$("#ticket_cupn_pw").html("비밀번호 : "+data.cupnPw);										//쿠폰 번호		
				}else{
					$("#ticket_cupn_pw").hide();
				}
				
				var cupnEtc=data.giftNotice;
				if(cupnEtc.search("\n")==-1){
					if(data.giftNotice != null && data.giftNotice!=""){	
						var li=$("<li>");
						$("#ticket_cupn_etc_yn").show();
						$("#ticket_cupn_etc").append(li.append(cupnEtc));						//쿠폰 사용안내
				}
					
				}else{
					var cupnEtcArr=cupnEtc.split("\n")
					//쿠폰유의사항 여부
					if(data.giftNotice != null && data.giftNotice!=""){	
						
						for(var i in cupnEtcArr){
						var li=$("<li>");
						$("#ticket_cupn_etc_yn").show();
						$("#ticket_cupn_etc").append(li.append(cupnEtcArr[i]));						//쿠폰 사용안내
						}
					}
				}
				
				var cupnUseInfo=data.giftManual;
				var cupnUseInfoArr=cupnUseInfo.split("\n");
				//쿠폰사용안내 여부
				if(data.giftManual !=null && data.giftManual != ""){
					
					for(var i in cupnUseInfoArr){
					var li=$("<li>");
					$("#ticket_cupn_use_info_yn").show();
						$("#ticket_cupn_use_info").append(li.append(cupnUseInfoArr[i]));		//쿠폰 사용안내
					}
				}
			}	
				
				
				
				
		},
		error : function(error){
			console.log(error);
		}
	}); //ajax End
	
}

/*수강 삭제 ajax*/
function btnDeleteClassRew(){
	var issueSeq = $('#issueSeq').val();
	var joinSeq = $('#joinSeq').val();
	var rewSeq = $('#classRewSeq').val();
	$('#btnModify').attr('disabled', true);
	
	$.ajax({
		type : "put",
		headers : {
			cntrNo : $('[name=cntrNo]').val(),
			ysid : $('[name=ysid]').val(),
			autoLogin : $('[name=autoLogin]').val(),
			osTp : $('[name=osTp]').val(),
			appVrsn : $('[name=appVrsn]').val()
		},
		data : {"issueSeq":issueSeq, "joinSeq":joinSeq, "rewSeq":rewSeq},
		dataType : "json",
		url : "deleteClassReward",
		success : function(data){
			if(data.resultCd=="200"){
				$("#reward_delete_popup").show();
				$('#class_reward_popup').hide();
				$('#btnModify').attr('disabled', false);
			}//200 end
			else{
				$("#error_msg").text(data.resultMsg);	//에러 메세지
				scrollDisble();
				$("#ticket_gift_use_error_popup").show();
				$('#btnModify').attr('disabled', false);
			}
		},
		error : function(error){
			console.log(error);
			$('#btnModify').attr('disabled', false);
		}
	}); //ajax End
}

//230413 수강 취소 팝업창 열기
function deleteClassRew(){
	$("#delete_class_reward_popup").show();
}

//230413 수강 취소 팝업창 취소
function btnDeleteClassRewCancel(){
	$("#delete_class_reward_popup").hide();
	scrollAble();
}

//210901 응모권 당첨 데이터 지급 팝업
function dataUsePopUp(giftIssueSeq, index){
	$('[name=giftIssueSeq]').val(giftIssueSeq);
	$("#uiIndex").val(index);
	
	scrollDisble();
	$("#ticket_gift_data_use_popup").show();
}

//211208 데이터쿠폰 사용 팝업
function dataRewardUsePopUp(dataRewSeq, index){
	$('[name=dataRewSeq]').val(dataRewSeq);
	$("#dataUiIndex").val(index);
	
	scrollDisble();
	$("#data_reward_use_popup").show();
}
//210907 배송지 팝업
function deliveryPop(){
	scrollDisble();

	$("#recvName").val("");
	$("#recvMobileNo").val("");
	$("#post").val("");
	$("#addr").val("");
	$("#addrDtl").val("");

	$("#delivery_popup").show();
	$("#deliveryBtn").show();
	
}

function ticketUseNotice(){
	scrollDisble();
	$("#ticket_use_notice_popup").show();
}

</script> 
</html>