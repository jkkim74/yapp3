/** 210615
 * 빈 문자열 체크 Y/N
 */
function isEmpty(str){
	if(typeof str == 'undefined' || str == null || str == ''){
		return true;
	}else{
		return false;
	}
}

/** 220128
 * 공백/엔터 대체
 */
function replaceSpace(str, replace){
	if(typeof str == 'undefined' || str == null || str == ''){
		return "";
	}

	str = str.replace(/\s*/g,replace);
	
	return str;
}

/** 230417
 * 로딩바 시작
 */
function loadingStart(changeImage){
	$('#loading-container').show();
	loadTimer = setInterval(changeImage, 100);//로딩바 추가
};

/** 230417
 * 로딩바 종료
 */
function loadingEnd(){
	clearInterval(loadTimer);//로딩바 초기화
	$('#loading-container').hide();//로딩창 숨기기
};