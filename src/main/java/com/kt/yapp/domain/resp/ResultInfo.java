package com.kt.yapp.domain.resp;

import java.util.List;
import java.util.Map;

import com.kt.yapp.em.EnumRsltCd;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 서버 호출 결과 정보 클래스
 */
@Data
public class ResultInfo<T> 
{
	@ApiModelProperty(value="결과 정보 리스트")
	private List<T> resultInfoList;
	
	@ApiModelProperty(value="결과 정보 데이터")
	private T resultData;
	
	@ApiModelProperty(value="결과 추가 정보 데이터(Map)")
	private Map<String, Object> resultMap;
	
	@ApiModelProperty(value="결과상태코드", notes="200 정상, 401 인증 에러, 500 일반 에러")
	private String resultCd = EnumRsltCd.C200.getRsltCd();

	@ApiModelProperty(value="결과 상태 메세지")
	private String resultMsg = "SUCCESS";

	public ResultInfo(){
	}
	public ResultInfo(T resultData){
		setResultData(resultData);
	}
	public ResultInfo(List<T> resultDataList){
		setResultInfoList(resultDataList);
	}
}
