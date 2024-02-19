<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<meta property="og:type" content="website">
<%-- <meta property="og:title" id="meta_title" content="<c:out value="${eventDetail.evtTitle}" />"/>
<meta property="og:description" id="meta_description" content="${eventDetail.evtSmallTitle}"/>
<meta property="og:image" id="meta_image" content="${eventDetail.evtDtlTopImg}"/> --%>
<meta property="og:title" id="meta_title" content='<c:out value="${eventDetail.evtTitle}" />'/>
<meta property="og:description" id="meta_description" content='<c:out value="${eventDetail.evtSmallTitle}" />'/>
<meta property="og:image" id="meta_image" content='<c:out value="${eventDetail.evtDtlTopImg}" />'/>
<meta property="og:url" content="https://ybox.kt.com/">
<title>이벤트 열기</title>
<script type="text/javascript" src="/yapp3/res/ext/jquery-3.5.1/jquery-3.5.1.js"></script>
<style type="text/css">
@import url(http://fonts.googleapis.com/earlyaccess/notosanskr.css);
body {
  font-family: 'Noto Sans KR', sans-serif;
  margin: 0;
  background-color: #fafafa;
  padding: 0;
}
</style>
<script>
		var ios_appstoreid = "1331731129";
		var android_packagename = "com.kt.ydatabox";
		var android_marketUrl = "market://details?id=com.kt.ydatabox";   
		var android_appUrl = "yboxapp://yboxapp";	//yboxapp://eventDetail?evtSeq="+evtSeq
		var android_intent = "intent://yboxapp/#Intent;scheme=yboxapp;package=com.kyad.offerwall.appall;end";
		var android_version_suffix = "&ver=1";
		var ios_version_suffix = "&ver=1";
		var type = "7";
		var evtSeq = getParam("evtSeq");	//new URLSearchParams(location.search).get("evtSeq");
		var evtTypeNm = getParam("evtTypeNm");
	
		function isIOS(){
			var userAgent = navigator.userAgent.toLowerCase();
			return ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1) || (userAgent.search("ipad") > -1));
		}
		function isSafari(){
			var userAgent = navigator.userAgent.toLowerCase();
			return (userAgent.search("safari") > -1);
		}
	
		function go_iOS_AppStore(){
			app_store_url = "https://itunes.apple.com/kr/app/id" + ios_appstoreid + "?mt=8";
			window.top.location.href = app_store_url;
		}
	
		  function go_android_market(){
		  var url = "Intent://yboxapp?type=" + encodeURIComponent(type) + "&evtSeq=" + encodeURIComponent(evtSeq) + android_version_suffix +"&evtTypeNm=" + evtTypeNm;
		  url += "#Intent;scheme=yboxapp;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=" + android_packagename + ";end";
		  android_intent = url;
		  launch_app_or_alt_url();
		}
		
		function KTYSHARE_showToast(str) {
			if (isIOS()){
				setTimeout(function () {
					window.location.href = "yboxapp://showToast?msg=" + encodeURIComponent(str) + ios_version_suffix;
				}, 50);
				setTimeout( function() {
					go_iOS_AppStore();
				}, 500);
				return;
			}
			if (typeof yboxapp == 'undefined') {
				if(is_android_web()){
					var url = "Intent://yboxapp?msg=" + encodeURIComponent(str) + android_version_suffix;
					url += "#Intent;scheme=yboxapp;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=" + android_packagename + ";end";
					android_intent = url;
					launch_app_or_alt_url();
				} else {
					alert(str);
					return;
				}
			}
			yboxapp.showToast(str);
		}
	
		function KTYSHARE_share() {
			if (isIOS()){
				document.getElementById("googleGo").style.visibility='hidden';
				document.getElementById("appStoreGo").style.visibility='visible';
				/* setTimeout(function () {
					var target_url = "yboxapp://ios?type=" + encodeURIComponent(type) + "&evtSeq=" + encodeURIComponent(evtSeq) + ios_version_suffix +"&evtTypeNm=" + evtTypeNm;
					window.top.location.href = target_url;
				}, 50);
				setTimeout( function() {
					go_iOS_AppStore();
				}, 500);
				return; */
				//수정
				var target_url = "yboxapp://ios?type=" + encodeURIComponent(type) + "&evtSeq=" + encodeURIComponent(evtSeq) + ios_version_suffix +"&evtTypeNm=" + evtTypeNm;
				var change = false;
				
				setTimeout(function () {
					if(!change) {
						go_iOS_AppStore();
					}
				}, 3000);
				
				window.top.location.href = target_url;
				window.onblur = function(){
					change = true;
				};
				
				window.onfocus = function(){
					change = false;
				};
				return;
			}
			if (typeof yboxapp == 'undefined') {
				if(is_android_web()){
					document.getElementById("googleGo").style.visibility='visible';
					document.getElementById("appStoreGo").style.visibility='hidden';
					var url = "Intent://yboxapp?type=" + encodeURIComponent(type) + "&evtSeq=" + encodeURIComponent(evtSeq) + android_version_suffix +"&evtTypeNm=" + evtTypeNm;
					url += "#Intent;scheme=yboxapp;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=" + android_packagename + ";end";
					android_intent = url;
					launch_app_or_alt_url();
				} else {
					document.getElementById("googleGo").style.visibility='hidden';
					document.getElementById("appStoreGo").style.visibility='hidden';
					alert("스마트폰을 이용해주세요.");
				}
				return;
			}
			yboxapp.share(type, evtSeq);
		}
	
		function openInNewTab(url) {
			var win = window.open(url, '_blank');
			win.focus();
		}
	
		function is_android_web(){
			var userAgent = navigator.userAgent.toLowerCase();
			return ((typeof yboxapp == 'undefined') && (userAgent.search("android") > -1));
		}
	
		var timer;
		var heartbeat;
		var iframe_timer;
	
		function clearTimers() {
			clearTimeout(timer);
			clearTimeout(heartbeat);
			clearTimeout(iframe_timer);
		}
	
		function intervalHeartbeat() {
			if (document.webkitHidden || document.hidden) {
				clearTimers();
			}
		}
	
		function tryIframeApproach() {
			var iframe = document.createElement("iframe");
			iframe.style.border = "none";
			iframe.style.width = "1px";
			iframe.style.height = "1px";
			iframe.onload = function () {
				window.top.location.href = android_marketUrl;
			};
			iframe.src = android_appUrl;
			document.body.appendChild(iframe);
		}
	
		function tryWebkitApproach() {
			window.top.location.href = android_appUrl;
			timer = setTimeout(function () {
				window.top.location.href = android_marketUrl;
			}, 2500);
		}
	
		function launch_app_or_alt_url() {
			heartbeat = setInterval(intervalHeartbeat, 200);
			if (navigator.userAgent.match(/Chrome/)) {
				window.top.location.href = android_intent;
			} else if (navigator.userAgent.match(/Firefox/)) {
				tryWebkitApproach();
				iframe_timer = setTimeout(function () {
					tryIframeApproach();
				}, 1500);
			} else {
				tryIframeApproach();
			}
		}
		
		function getParam(search_for) {
			var query = window.location.search.substring(1);
			var parms = query.split('&');
			for (var i = 0; i < parms.length; i++) {
				var pos = parms[i].indexOf('=');
				if (pos > 0 && search_for == parms[i].substring(0, pos)) {
					return parms[i].substring(pos + 1);
					;
				}
			}
			return "";
		}
	</script>
</head>
<body onload="KTYSHARE_share()">
	<div class="app" style="position: relative; overflow: hidden;">
		<div class="app-container" style="padding: 18px;">
			<div class="app__title" style="height: calc(100vh - 195px); text-align: center; display: flex; flex-direction: column; justify-content: center;">
				<img class="icon-google" src="/yapp3/res/img/ic_YFriends@2x.png" alt="Y" style="width: 72px; margin: 0 auto;">
				<h2 style="margin-top: 60px; margin-bottom: 19px; font-size: 17.8px; font-weight: 500; line-height: 1.43; letter-spacing: -0.9px; color: #1e1e1e; margin-bottom: 19px;">Y박스 앱 설치 안내</h2>
				<h4 style="margin-top: 0; font-weight: normal; font-size: 14.2px; line-height: 1.5; letter-spacing: -0.7px; color: #636363;">요청하신 기능은 앱 설치 이후<br/>사용하실 수 있습니다.</h4>
			</div>
			<div class="app__btn-group">
				<div id="googleGo" style="visibility:hidden">
				<a href="javascript:go_android_market();">
					<button style="cursor: pointer; width: 100%; height: 50px; line-height: 1; background-color: #2bb7b3; color: #fff; margin-bottom: 6px; border: none; font-size: 16px; letter-spacing: -0.8px;">
					 <img class="icon-google" src="https://ybox.kt.com/img/icon_google.png " alt="google" style="width: 16px; margin-right: 10px; margin-bottom: -2px;">
						구글 플레이
					</button>
				</a>
				</div>
				<div id="appStoreGo" style="visibility:hidden">
				<a href="javascript:go_iOS_AppStore();">
					<button style="cursor: pointer; width: 100%; height: 50px; line-height: 1; background-color: #2bb7b3; color: #fff; border: none; font-size: 16px; letter-spacing: -0.8px;">
						<img class="icon-google" src="https://ybox.kt.com/img/icon_apple.png " alt="google" style="width: 16px; margin-right: 10px; margin-bottom: -2px;">
						앱 스토어
					</button>
				</a>
				</div>
				<p style="font-size: 10.7px; line-height: 1.5; letter-spacing: -0.5px; color: #999999; margin-top: 17px; margin-bottom: 0;">Y박스는 KT 고객만 이용 가능합니다. 요금제에 따라 일부 기능이 제한될 수 있습니다.</p>
			</div>
		</div>
	</div>
</body>
</html>
