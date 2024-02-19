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
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="/yapp3/res/css/notosanskr.css?20230713">
<style type="text/css">
body {
  font-family: "Noto Sans KR", sans-serif;
  margin: 0;
  background-color: #fafafa;
  padding: 0;
  color: #636363;
}
.regular {
  font-weight: 400
}
.medium {
  font-weight: 500
}
section.terms__item .import {
  font-size: 14px;
  font-weight: 400;
  line-height: 22px;
}
section.terms__item {
  margin-bottom: 22px;
}
section.terms__item h5 {
  font-size: 14px;
  margin-top: 0;
  margin-bottom: 6px;
  font-weight: 500;
}
section.terms__item p {
  font-size: 14px;
  font-weight: 400;
  line-height: 22px;
}
section.terms__item ol {
  font-size: 14px;
  font-weight: 400;
  line-height: 22px;
  margin: 0;
  padding-left: 15px;
}
section.terms__item ol.ol-korean {
  list-style: none;
  padding-left: 0;
}
section.terms__item ol.ol-korean li {
  padding-left: 20px;
  position: relative;
}
section.terms__item ol.ol-korean li:before {
  position: absolute;
  left: 0;
}
section.terms__item ol.ol-korean li:first-child:before {
  content: "가.";
}
section.terms__item ol.ol-korean li:nth-child(2):before {
  content: "나.";
}
section.terms__item ol.ol-korean li:nth-child(3):before {
  content: "다.";
}
section.terms__item ol.ol-korean li:nth-child(4):before {
  content: "라.";
}
section.terms__item ol.ol-korean li:nth-child(5):before {
  content: "마.";
}
section.terms__item ol.ol-korean li:nth-child(6):before {
  content: "바.";
}
section.terms__item ol.ol-korean li:nth-child(7):before {
  content: "사.";
}
section.terms__item ol.ol-korean li.import:before {
  content: "※";
}
section.terms__item ol.ol-korean2 {
  list-style: none;
  padding-left: 0;
}
section.terms__item ol.ol-korean2 li {
  padding-left: 20px;
  position: relative;
}
section.terms__item ol.ol-korean2 li:before {
  position: absolute;
  left: 0;
}
section.terms__item ol.ol-korean2 li:first-child:before {
  content: "나.";
}
section.terms__item ul {
  list-style: disc;
  padding-left: 25px;
  font-size: 14px;
  font-weight: 400;
  line-height: 22px;
  margin: 0;
  padding-left: 15px;
}
section.terms__item ul.ul-circle {
  list-style: circle;
  padding-left: 25px;
}
section.terms__item ul.ul-dash {
  list-style: none;
  padding-left: 0;
}
section.terms__item ul.ul-dash li {
  padding-left: 15px;
  position: relative;
}
section.terms__item ul.ul-dash li:before {
  content: "-";
  position: absolute;
  left: 0;
}
section.terms__item table {
  margin-left: -20px;
  border-collapse: collapse;
  margin-top: 10px;
  margin-bottom: 10px;
}
section.terms__item table tr th {
  background-color: lightgray;
}
section.terms__item table tr th, td {
  border: 1px solid;
  padding: 5px;
  text-align: left;
  vertical-align: middle;
}
section.terms__item p.sub_title {
  margin-left: -20px;
  margin-top: 15px;
  margin-bottom: -5px;
}

section.terms__item p.sub_comment {
  margin-left: -20px;
  margin-top: -10px;
  margin-bottom: 5px;
}

section.terms__item table tr th:first-child {
  width: 20%;
}

section.terms__item .ftable tr th:first-child {
  width: 50%;
}

</style>
</head>
<body>
  <div class="terms" style="position: relative; overflow: hidden;">
    <div class="terms-container" style="padding: 24px 20px;">
  고객님. Y박스 App 서비스를 이용해 주셔서 감사합니다. <br /><br />

  Y박스 App 계정이 서비스의 장기간 미이용으로 인해 휴면계정으로 전환될 예정입니다.<br />
  (전환예정일 : ${accTransDate})<br /><br />
  
  휴면계정 전환 후에는 Y박스 App에 저장중인 회원님의 모든 개인정보가 정보통신망법 제29조 제2항 및 동법 시행령 제16조에 따라 파기 또는 분리보관 됩니다.<br /><br />
  
  휴면 처리를 원치 않으실 경우 전환 예정 일자전까지 Y박스 App에 로그인하여 주시기 바랍니다.<br /><br />
  
  앱 로그인하기: <a href="https://ybox.kt.com/invite.html">https://ybox.kt.com/invite.html</a><br /><br />
  
  ※본 문자는 정보통신망법 제29조 제2항에 의거하여 문자수신에 대한 동의 여부와 관계없이 Y박스 회원님들 대상으로 보내드리는 통지 문자입니다.(발신전용)<br />
	</div>
  </div>
  
</body>
</html>
'