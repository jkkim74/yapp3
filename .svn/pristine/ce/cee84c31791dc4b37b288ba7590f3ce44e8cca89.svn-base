package com.kt.yapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:limitation.properties")
@Data
public class LimitationPropConfig 
{
	/** 선물받기로 적립가능 데이터 합계 (매월 선물받기로 적립 가능한 데이터 총량은 2,000MB (보너스 데이터/프로모션 데이터는 총량산정에서 제외)) */
	@Value("${LMT_DBOX_GIFT_MAX_AMT}")
	private int lmtDboxGiftMaxAmt;
	
	/** 선물받기로 보유가능 데이터 합계 (선물하기로만 데이터를 적립할 경우, 월 최대 보유량은 4,000MB (전월 이월 2,000MB 포함)) */
//	@Value("${LMT_DBOX_GIFT_KEEP_MAX_AMT}")
//	private int lmtDboxGiftKeepMaxAmt;
	
	/** 데이터 박스 기본 사이즈 */
	@Value("${LMT_DBOX_DATA_SIZE}")
	private int lmtDboxDataSize;
	
	/** 데이터 박스 꺼내기 가능 횟수 */
	@Value("${LMT_DBOX_PULL_MAX_CNT}")
	private int lmtDboxPullMaxCnt;
	
	/** 1회 선물가능 최대 용량(100MB 단위로 선택하여 선물하기 가능(최대 1,000MB)) */
	@Value("${LMT_GIFT_MAX_AMT}")
	private int lmtGiftMaxAmtOneTime;
	
	/** 월 선물가능 횟수(월 최대 20회까지 선물하기 가능) */
	@Value("${LMT_GIFT_CNT}")
	private int lmtGiftCnt;
	
	/** 선물가능 월 용량 합계(회선당 월 합계 2,000MB까지만 선물하기 가능) */
	@Value("${LMT_GIFT_AMT}")
	private int lmtGiftAmt;
	
	/** 선물하기 완료후의 최소 데이터 잔여량(선물하기 완료 후 기본제공 데이터 잔여량이 500MB 미만이 되는 용량은 선물하기 불가) */
	@Value("${LMT_GIFT_AFTER_MIN_AMT}")
	private int lmtGiftAfterMinAmt;
	
	/** 데이턱 수령 데이터 크기 (MB) */
	@Value("${LMT_DATUK_RCV_DATA_SIZE}")
	private int lmtDatukRcvDataSize;
	
}
