<%--
  Created by IntelliJ IDEA.
  User: 91336575
  Date: 2023-05-15
  Time: 오후 2:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalabel=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>OM</title>
    <link type="text/css" rel="stylesheet" href="/yapp3/res/css/onmasi.css?20230622">
    <script type="text/javascript" src="/yapp3/res/ext/jquery-3.5.1/jquery-3.5.1.js"></script>
    <script type="text/javascript" src="${omStatUrl}"></script>
    <script type="text/javascript">
        const cmpId = "${omReqInfo.caVal1}";// 캠페인아이디
        const cmpName = "${omReqInfo.caVal2}";// 캠페인 이름
        const cmpZoneCode = "${omReqInfo.caVal3}";// 캠페인 ZoneCode
        const statCd = "${omReqInfo.statCd}";

        $(document).ready(function (){
            $("#om_move").click(function(e){

                //e.preventDefault()
                $(this).attr("href","${jsonBanner.urlNm}").attr("target","_blank");
                const campStatCd = '^YBOX' + statCd + '^클릭';
                try {
                    console.log(cmpId, cmpName, cmpZoneCode, 'CL', campStatCd);
                    trgt.campaignClickLog(cmpId, cmpName, cmpZoneCode, 'CL', campStatCd);
                }catch (e) {
                    console.log(e);
                }
            });
            window.YBoxOnmasInterface.onReady();//앱에 배너 오픈시점을 INFO 함..
        });

        // 앱의 스와이프 무한 로딩 문제로 별도의 메소드로 뺌. 2023.07.07
        const initOmBanner = function () {
            ot.a.apVal6 = '${appVrsn}';		// App인경우 앱버전
            ot.u.ktId = '${encloginUserId}';	// ktId  (inisafenet 암호한 값)
            ot.u.gaId = '${gaId}';	// GoogleID 대체 할 수 있는 유니크한 값

            // 온라인 마케팅 캠페인 노출 로그 수집 함수 호출
            // calVal1 : 캠페인코드, calVal2 : 캠페인명, caVal3 : 캠페인 위치 관련 zoneCode
            try {
                console.log("before static sending : ",cmpId, cmpName, cmpZoneCode);
                trgt.campaignViewLog(cmpId, cmpName, cmpZoneCode);
            } catch (e) {
                console.log(e);
            }
        }

    </script>
</head>
<body>
<div class="swiper mySwiper">
    <div class="swiper-wrapper">
        <a href="javascript:void(0);" id="om_move">
            <div class="swiper-slide">
                <img src="${jsonBanner.imageFileName}">
                <div class="f_box">
                    <p>${jsonBanner.title}</p>
                    <p class="sub">${jsonBanner.subTitle}</p>
                </div>
            </div>
        </a>
    </div>
</div>
</body>
</html>