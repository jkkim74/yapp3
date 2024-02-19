package com.kt.yapp.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.yapp.domain.BigiChargBlnc;
import com.kt.yapp.domain.CallingPlan;
import com.kt.yapp.domain.DataInfo;
import com.kt.yapp.domain.DataRefill;
import com.kt.yapp.domain.WsgDataUseQnt;
import com.kt.yapp.domain.WsgGetMembershipInfo;
import com.kt.yapp.domain.WsgGetMyTimePlanSetTime;
import com.kt.yapp.em.EnumCallingCatLarge;
import com.kt.yapp.em.EnumYn;
import com.kt.yapp.exception.YappException;
import com.kt.yapp.soap.response.SoapResponse101;
import com.kt.yapp.soap.response.SoapResponse139;
import com.kt.yapp.util.KeyFixUtil;
import com.kt.yapp.util.YappUtil;

/**
 * WSG 연동 서비스
 */
@Service
public class WsgService 
{
	@Autowired
	private CommonService cmnService;
	@Autowired
	private ShubService shubService;
	@Autowired
	private KosService kosService;
	
	@Autowired
	private KeyFixUtil keyFixUtil;
	@Value("${WSG_SVC_CD}")
	private String svcCode;
	
	private static final String URL_GETMOBILETOTALUSEWEB 	= "https://appgw.olleh.com/api/bill/getMobileTotalUseWeb";
	private static final String URL_GETMEMBERSHIPINFO 		= "https://appgw.olleh.com/api/membership/getMemberShipInfoSubParty";
	private static final String URL_GETMYTIMEPLANSETTIME 	= "https://appgw.olleh.com/api/bill/getMyTimePlanSetTime";
	private static final String URL_GETEGGTOTALUSE			= "https://appgw.olleh.com/api/bill/getEggTotalUse";
	
	private static final String ERR_MSG_PREFIX = "WSG_";

	private static final Logger logger = LoggerFactory.getLogger(WsgService.class);
	
	/**
	 * 데이터 사용량 정보를 조회한다.
	 */
	public WsgDataUseQnt getMobileTotalUseWeb(String cntrNo, String mobileNo, String tarMnth, CallingPlan callingPlan) throws Exception
	{
		/////////////////////////////////////////// TODO TEST DATA
//		WsgDataUseQnt dataUser = new WsgDataUseQnt();
//		dataUser.setMyDataSize(2500);
//		dataUser.setCurMnthDataAmt(1500);
//		dataUser.setFwdMnthDataAmt(1000);
//		dataUser.setMyRmnDataAmt(2000);
//		dataUser.setDataAmt(5000);
//		if ( true ) return dataUser;
		/////////////////////////////////////////// TODO TEST DATA
		CallingPlan callingPlanTmp = new CallingPlan();
		callingPlanTmp = callingPlan;
		// 요금제 정보 조회
		if ( callingPlanTmp == null ) {
			SoapResponse139 resp139 = shubService.callFn139(mobileNo, false, true);
			callingPlanTmp = resp139.getCntrInfo().getCallingPlan();
		}

		if ( callingPlanTmp == null ) {
			throw new YappException("CHECK_MSG",cmnService.getMsg("ERR_NO_CALLING_PLAN")); //ERR_NO_CALLING_PLAN
		}

		
		//넘어는 달 말일 구하기
		String curMonth = YappUtil.getCurDate("yyyyMM");
		String curDate = "";
		if(tarMnth == null || curMonth.equals(tarMnth)){
			curDate = YappUtil.getCurDate("yyyyMMdd");
		}else{
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(tarMnth.substring(0,4)), Integer.parseInt(tarMnth.substring(0,4)) -1, 1);
			curDate = tarMnth + String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		// 요금제 구분에 따라 데이터 사용량 조회 호출
		if ( YappUtil.isEq(callingPlanTmp.getEggYn(), EnumYn.C_Y.getValue()) ){
			//return getEggTotalUse(mobileNo, callingPlanTmp.getPpCatL());
			return getEggTotalUseKos(cntrNo, mobileNo, callingPlanTmp.getPpCatL());
		}else{
			//return getNotEggTotalUseWeb(mobileNo, tarMnth);
			return getNotEggTotalUseWebKos(cntrNo, mobileNo, curDate);
		}
	}
	
	/**
	 * 알요금제 외의 LTE, 3G 데이터 사용량 정보 조회
	 */
	private WsgDataUseQnt getNotEggTotalUseWeb(String mobileNo, String tarMnth) throws Exception
	{
		// 암호화
		String encMobileNo = keyFixUtil.encode(mobileNo);
		String[][] paramArray = new String[][]{{"svcCode", svcCode}, {"ctn", encMobileNo}, {"month", tarMnth}};
		if ( tarMnth == null )
			paramArray = new String[][]{{"svcCode", svcCode}, {"ctn", encMobileNo}, {"dataCacheFlag", "0"}};
			
		String apiUrl = makeUrl(URL_GETMOBILETOTALUSEWEB, paramArray);
		logger.info("WSG URL: " + apiUrl);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> totalUse = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("WSG RETURN: " + totalUse);
		
		// 정상 리턴이 아니면 종료
		String rtnCd = YappUtil.nToStr(totalUse.get("returnCode"));
		if ( totalUse == null || totalUse.get("drctlyUseQntFreeRetvOutDTO") == null || YappUtil.isNotEq(rtnCd, EnumYn.C_1.getValue()) ) {
			logger.warn(cmnService.getMsg(ERR_MSG_PREFIX + rtnCd));
			//throw new YappException("WSG_MSG", rtnCd, cmnService.getMsg("ERR_SYS_ERROR"), "[getMobileTotalUseWeb] "+cmnService.getMsg(ERR_MSG_PREFIX + rtnCd), "");
			return new WsgDataUseQnt();
		}
		logger.info("=========================================");
		logger.info("WSG RETURN: getClass  " + totalUse.get("drctlyUseQntFreeRetvOutDTO").getClass().getName());
		logger.info("=========================================");
		// 데이터 사용량만 추출한다.
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if(totalUse.get("drctlyUseQntFreeRetvOutDTO").getClass().getName().equals("java.util.ArrayList")){
			dataList = (List<Map<String, Object>>) totalUse.get("drctlyUseQntFreeRetvOutDTO");
		}else{
			dataList.add((Map<String, Object>)totalUse.get("drctlyUseQntFreeRetvOutDTO"));
		}
		for ( Map<String, Object> data : dataList )
		{
			String unitSvcGroupCd = YappUtil.nToStr(data.get("unitSvcGroupCd"));
			if ( YappUtil.contains(unitSvcGroupCd, "PALV_T", "PACKET") )
			{
				WsgDataUseQnt useQnt = new WsgDataUseQnt();
				long totalDataAmt = this.divideVal(YappUtil.toLong(data.get("totFreeQnt")	, 0) , 2048);
				long useDataAmt = this.divideVal(YappUtil.toLong(data.get("freeUseQnt")		, 0) , 2048);
				useQnt.setUseDataAmt(useDataAmt);
				useQnt.setMyDataSize(totalDataAmt);
				
				useQnt.setFwdMnthDataAmt(	this.divideVal(YappUtil.toLong(data.get("tmonFreeQnt")	, 0), 2048));	//당월무료제공량
				useQnt.setCurMnthDataAmt(	this.divideVal(YappUtil.toLong(data.get("cfwdFreeQnt")	, 0) , 2048) - useDataAmt); // 당원이월제공량 - 당월제공사용량
				useQnt.setMyRmnDataAmt(totalDataAmt - useDataAmt);
				
				return useQnt;
			}
		}
		return new WsgDataUseQnt();
	}

	/**
	 * 알 사용량 정보를 조회한다.
	 */
	private WsgDataUseQnt getEggTotalUse(String mobileNo, String ppCatL) throws Exception
	{
		// 암호화
		String encMobileNo = keyFixUtil.encode(mobileNo);
		String apiUrl = makeUrl(URL_GETEGGTOTALUSE, new String[][]{{"svcCode", svcCode}, {"ctn", encMobileNo}, {"dataCacheFlag", "0"}});
		logger.info("WSG URL: " + apiUrl);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> eggTotalUseInfo = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("WSG RETURN: " + eggTotalUseInfo);
		
		// 정상 리턴이 아니면 종료
		String rtnCd = YappUtil.nToStr(eggTotalUseInfo.get("returnCode"));
		if ( eggTotalUseInfo == null || YappUtil.isNotEq(rtnCd, EnumYn.C_1.getValue()) ) {
			logger.warn(cmnService.getMsg(ERR_MSG_PREFIX + rtnCd));
			//throw new YappException("WSG_MSG", rtnCd, cmnService.getMsg("ERR_SYS_ERROR"), "[getEggTotalUse] "+cmnService.getMsg(ERR_MSG_PREFIX + rtnCd), "");
			return new WsgDataUseQnt();
		}
		
		// 알 -> 데이터 전환 비율 (LTE 알요금제: 20.48 = 1MB, 3G 알요금제: 40.96 = 1MB
		double chgRate = 0d;
		if ( YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0001.getCatCd()) || YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0002.getCatCd())){
			chgRate = 20.48;
		}else if ( YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0003.getCatCd()) || YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0004.getCatCd())){
			chgRate = 40.96;
		}
		
		// 월정액 알(기본 제공)
		long baseR = YappUtil.toLong(eggTotalUseInfo.get("BASE_R"), 0);
		// 이월 알
		long forwardR = YappUtil.toLong(eggTotalUseInfo.get("FORWARD_R"), 0);
		// 충전 알
		long chgR = YappUtil.toLong(eggTotalUseInfo.get("CHG_R"), 0);
		// 수신 알
		long revR = YappUtil.toLong(eggTotalUseInfo.get("REV_R"), 0);
		// 자동 상한 알
		long amaxR = YappUtil.toLong(eggTotalUseInfo.get("AMAX_R"), 0);
		
		// 전체 알
		long totalR = baseR + forwardR + chgR + revR + amaxR;
		
		// 매진엔 알
		long magicR = YappUtil.toLong(eggTotalUseInfo.get("MAGIC_R"), 0);
		// 꺼낸 데이터(MB)
		//long dataR = magicR + YappUtil.toLong(eggTotalUseInfo.get("DATA_RCV"), 0) / 1048576;
		long dataR = YappUtil.toLong(eggTotalUseInfo.get("DATA_RCV"), 0) / 1048576;
		
		WsgDataUseQnt useQnt = new WsgDataUseQnt();

		logger.info("======================================");
		logger.info("totalR" + totalR );
		logger.info("forwardR" + forwardR );
		logger.info("baseR" + baseR );
		logger.info("revR" + revR );
		logger.info("Math.round(dataR * chgRate) + magicR" + Math.round(dataR * chgRate) + magicR );
		logger.info("chgR" + chgR );
		logger.info("======================================");
		
		useQnt.setEggMyRmnDataAmt(totalR);	// 알 표시에는 '전체' 만 세팅
		useQnt.setEggFwdMnthDataAmt(forwardR);
		useQnt.setEggCurMnthDataAmt(baseR);
		useQnt.setEggRcvDataAmt(revR);
		useQnt.setEggDataAmt(Math.round(dataR * chgRate) + magicR);
		useQnt.setEggChgDataAmt(chgR);
		useQnt.setMyEggDataAmt(totalR + magicR + Math.round(dataR * chgRate));

		// 쿠폰 목록을 리스트에 추가한다.
		long cpnVal = 0;
		for ( int i = 1; i < 31; i++ )
		{
			// n번째 쿠폰 잔여량 조회
			int coupRemains = YappUtil.toInt(eggTotalUseInfo.get("COUP" + i + "REMAINS"), -1);
			if ( coupRemains < 0 )
				break;
			
			// n번째 쿠폰 사용 만료일 조회
			int coupExpireDate = YappUtil.toInt(eggTotalUseInfo.get("COUP" + i + "EXPIRE"), -1);
			if ( coupExpireDate > 0 && (coupExpireDate >= YappUtil.toInt(YappUtil.getCurDate("yyyyMMdd"))) )
				cpnVal += coupRemains / 1048576;
		}

		useQnt.setMyRmnDataAmt(		(Math.round(totalR / chgRate) + dataR + Math.round(magicR/ chgRate) + cpnVal));	// MB 표시에는 ('전체' + 꺼낸 데이터 + 쿠폰 데이터) 세팅
		useQnt.setFwdMnthDataAmt(	Math.round(forwardR / chgRate));
		useQnt.setCurMnthDataAmt(	Math.round(baseR / chgRate));
		useQnt.setDataAmt(dataR);	// 데이터 알
		useQnt.setRcvDataAmt(		Math.round(revR / chgRate));	// 받은 알
		useQnt.setChgDataAmt(		Math.round(chgR / chgRate));	// 충전 알
	
		logger.info("======================================");
		logger.info("setMyRmnDataAmt " + (Math.round(totalR / chgRate) + dataR + Math.round(magicR/ chgRate) + cpnVal));
		logger.info("setFwdMnthDataAmt " + Math.round(forwardR / chgRate) );
		logger.info("setCurMnthDataAmt " + Math.round(baseR / chgRate) );
		logger.info("setDataAmt " + dataR);
		logger.info("setRcvDataAmt " + Math.round(revR / chgRate) );
		logger.info("setChgDataAmt " + Math.round(chgR / chgRate));
		logger.info("======================================");
		
		// SMS 제공량
		useQnt.setEggSmsDataAmt(YappUtil.toLong(eggTotalUseInfo.get("FSMS_R"), 0));

		return useQnt;
	}
	
	/**
	 * 알요금제 외의 LTE, 3G 데이터 사용량 정보 조회
	 */
	private WsgDataUseQnt getNotEggTotalUseWebKos(String cntrNo, String mobileNo, String trDate) throws Exception
	{
		DataInfo dataInfo = kosService.retrieveDrctlyUseQnt(cntrNo, mobileNo, trDate);
		WsgDataUseQnt useQnt = new WsgDataUseQnt();
		if (dataInfo != null){
			long totalDataAmt = YappUtil.toLong(dataInfo.getDataAmt()	, 0);
			long useDataAmt = YappUtil.toLong(dataInfo.getUseDataAmt()	, 0);
			useQnt.setUseDataAmt(useDataAmt);
			useQnt.setMyDataSize(totalDataAmt);
			useQnt.setFwdMnthDataAmt(YappUtil.toLong(dataInfo.getDsprNextMnthDataAmt()	, 0));	//당월무료제공량
			useQnt.setCurMnthDataAmt(YappUtil.toLong(dataInfo.getDsprCurMnthDataAmt()	, 0)); // 당원이월제공량 - 당월제공사용량
			useQnt.setMyRmnDataAmt(totalDataAmt - useDataAmt);
			useQnt.setTmonFreeQnt(dataInfo.getTmonFreeQnt());
			logger.info("============================================================================");
			logger.info("총제공랻 ======> " + totalDataAmt);
			logger.info("사용량 ======> " + useDataAmt);
			logger.info("당월무료제공량 ======> " + dataInfo.getDsprNextMnthDataAmt());
			logger.info("당원이월제공량 - 당월제공사용량 ======> " + dataInfo.getDsprCurMnthDataAmt());
			logger.info("============================================================================");
		}
		return useQnt;
	}

	/**
	 * 알 사용량 정보를 조회한다.
	 */
	private WsgDataUseQnt getEggTotalUseKos(String cntrNo, String mobileNo, String ppCatL) throws Exception
	{
		
		BigiChargBlnc bigiChargBlnc = kosService.retrieveBigiChargBlnc(cntrNo, mobileNo);
		DataRefill dataRefill = kosService.retrieveDataRefill(cntrNo, mobileNo);

		// 알 -> 데이터 전환 비율 (LTE 알요금제: 20.48 = 1MB, 3G 알요금제: 40.96 = 1MB
		double chgRate = 0d;
		if ( YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0001.getCatCd()) || YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0002.getCatCd())){
			chgRate = 20.48;
		}else if ( YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0003.getCatCd()) || YappUtil.isEq(ppCatL, EnumCallingCatLarge.G0004.getCatCd())){
			chgRate = 40.96;
		}

		// 월정액 알(기본 제공)
		long baseR = YappUtil.toLong(bigiChargBlnc.getEggCurMnthDataAmt(), 0);
		// 이월 알
		long forwardR = YappUtil.toLong(bigiChargBlnc.getEggFwdMnthDataAmt(), 0);
		// 충전 알
		long chgR = YappUtil.toLong(bigiChargBlnc.getEggChgDataAmt(), 0);
		// 수신 알
		long revR = YappUtil.toLong(bigiChargBlnc.getEggRcvDataAmt(), 0);
		
		// 전체 알
		long totalR = baseR + forwardR + chgR + revR;
		
		// 데이터알
		long dataR = YappUtil.toLong(bigiChargBlnc.getEggDataAmt(), 0);
		// 데이터MB
		long dataMb = YappUtil.toLong(bigiChargBlnc.getDataAmt(), 0) / 1048576;
		// 충전데이터 잔여량 MB
		long dataRefillMb = YappUtil.toLong(dataRefill.getRefillRemainsAmt(), 0) / 1048576;
		
		WsgDataUseQnt useQnt = new WsgDataUseQnt();

		logger.info("======================================");
		logger.info("totalR : " + totalR );
		logger.info("forwardR : " + forwardR );
		logger.info("baseR : " + baseR );
		logger.info("revR : " + revR );
		logger.info("dataR : " + (dataR +  Math.round(dataMb * chgRate)));
		logger.info("chgR : " + chgR );
		logger.info("dataRefillMb : " + Math.round(dataRefillMb * chgRate) );
		logger.info("======================================");
		
		useQnt.setEggMyRmnDataAmt(totalR);	// 알 표시에는 '전체' 만 세팅
		useQnt.setEggFwdMnthDataAmt(forwardR);
		useQnt.setEggCurMnthDataAmt(baseR);
		useQnt.setEggRcvDataAmt(revR);
		useQnt.setEggDataAmt((dataR +  Math.round(dataMb * chgRate)));
		useQnt.setEggChgDataAmt(chgR);
		useQnt.setMyEggDataAmt((totalR +  dataR +  Math.round(dataMb * chgRate) +  Math.round(dataRefillMb * chgRate)));
		useQnt.setEggDataRefillAmt(Math.round(dataRefillMb * chgRate));
		
		useQnt.setMyRmnDataAmt((Math.round((totalR+dataR) / chgRate)+dataMb+dataRefillMb));	// MB 표시에는 ('전체' + 꺼낸 데이터 + 쿠폰 데이터+충전데이터) 세팅
		useQnt.setFwdMnthDataAmt(Math.round(forwardR / chgRate));
		useQnt.setCurMnthDataAmt(Math.round(baseR / chgRate));
		useQnt.setDataAmt(Math.round((dataR / chgRate)+dataMb));	// 데이터 알
		useQnt.setDataRefillAmt(dataRefillMb);// 충전데이터 잔여량
		useQnt.setRcvDataAmt(Math.round(revR / chgRate));	// 받은 알
		useQnt.setChgDataAmt(Math.round(chgR / chgRate));	// 충전 알
		useQnt.setTmonFreeQnt(0);
		logger.info("======================================");
		logger.info("setMyRmnDataAmt : " + (Math.round((totalR+dataR) / chgRate)+dataMb+dataRefillMb));
		logger.info("setFwdMnthDataAmt : " + Math.round(forwardR / chgRate) );
		logger.info("setCurMnthDataAmt : " + Math.round(baseR / chgRate) );
		logger.info("setDataAmt : " + (Math.round(dataR / chgRate)+dataMb));
		logger.info("setRcvDataAmt : " + Math.round(revR / chgRate) );
		logger.info("setChgDataAmt : " + Math.round(chgR / chgRate));
		logger.info("setDataRefillAmt : " + dataRefillMb);
		logger.info("======================================");
		
		// SMS 제공량
		useQnt.setEggSmsDataAmt(YappUtil.toLong(bigiChargBlnc.getEggSmsDataAmt(), 0));

		return useQnt;
	}
	
	/**
	 * 부가서비스 두배쓰기 설정여부를 조회한다.
	 */
	public String getVasDoubleInfo(String mobileNo) throws Exception
	{
		// 암호화
		String encMobileNo = keyFixUtil.encode(mobileNo);
		String apiUrl = makeUrl(URL_GETEGGTOTALUSE, new String[][]{{"svcCode", svcCode}, {"ctn", encMobileNo}});
		logger.info("WSG URL: " + apiUrl);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> eggTotalUseInfo = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("WSG RETURN: " + eggTotalUseInfo);
		
		// 정상 리턴이 아니면 종료
		String rtnCd = YappUtil.nToStr(eggTotalUseInfo.get("returnCode"));
		if ( eggTotalUseInfo == null || YappUtil.isNotEq(rtnCd, EnumYn.C_1.getValue()) ) {
			logger.warn(cmnService.getMsg(ERR_MSG_PREFIX + rtnCd));
			return "N";
		}
		
		// 두배쓰기 설정여부
		Object doubleYn = eggTotalUseInfo.get("UPSCALE_DATA_FLAG");
		
		return YappUtil.is1(doubleYn) ? "Y" : "N";
	}
	
	/**
	 * 무제한이 아니면 입력값을 나눈다.
	 */
	private long divideVal(long val, long divideVal)
	{
		if ( val == 999999999 )
			return 999999999;
		else
			return val / divideVal;
	}
	
	/**
	 * 멤버십 정보를 조회한다.(크레덴션 ID가 없으면 휴대폰 번호로 조회한다.)
	 */
	public WsgGetMembershipInfo getMembershipInfo(String credentialId, String mobileNo) throws Exception
	{
		// 식별정보를 조회한다.
		SoapResponse101 resp101 = null;
		if ( YappUtil.isEmpty(credentialId) )
			resp101 = shubService.callFn101WithMobileNo(mobileNo);
		else
			resp101 = shubService.callFn101(credentialId);
		
		// 암호화
		String encPartyId = keyFixUtil.encode(resp101.getPartyIdNumber1());
		String encPartyCode = keyFixUtil.encode(resp101.getPartyIdtfNumberCd());
		
		String apiUrl = makeUrl(URL_GETMEMBERSHIPINFO, new String[][]{{"svcCode", svcCode}, {"partyId", encPartyId}, {"partyCode", encPartyCode}, {"dataCacheFlag", "0"}});
		logger.info("WSG URL: " + apiUrl);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> membershipInfo = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("WSG RETURN: " + membershipInfo);

		WsgGetMembershipInfo memInfo = new WsgGetMembershipInfo();
		
		if ( membershipInfo == null || YappUtil.isNotEq(membershipInfo.get("returnCode"), EnumYn.C_1.getValue()) )
		{
			if(membershipInfo != null) {
				logger.warn(cmnService.getMsg(ERR_MSG_PREFIX + membershipInfo.get("returnCode")));
			}else{
				logger.warn(cmnService.getMsg(ERR_MSG_PREFIX));
			}
			
			return memInfo;
		}
		
		memInfo.setGrade(YappUtil.nToStr(membershipInfo.get("grade")));
		memInfo.setPoint(YappUtil.toInt(membershipInfo.get("point"), 0));
		
		return memInfo;
	}
	
	/**
	 * My Time Plan 설정 정보를 조회한다.
	 */
	public void getMyTimePlanSetTime(String svcCode, String mobileNo, String prdcCd) throws Exception
	{
		/////////////////////////////////////////// TODO TEST DATA
		WsgGetMyTimePlanSetTime dataUser = new WsgGetMyTimePlanSetTime();
/*		dataUser.setPrmtSbst("17시 ~ 20시");
		dataUser.setStHour(YappUtil.toInt("17시 ~ 20시".replaceAll("시", "").split("~")[0].trim()));
		dataUser.setEdHour(YappUtil.toInt("17시 ~ 20시".replaceAll("시", "").split("~")[1].trim()));
		
		dataUser.setPrmtSbst(prmtSbstPa);
		dataUser.setStHour(stHour);
		dataUser.setEdHour(edHour);
		
		//if ( true ) return dataUser;
*/		/////////////////////////////////////////// TODO TEST DATA
		
		// 암호화
		String encMobileNo = keyFixUtil.encode(mobileNo);
		String apiUrl = makeUrl(URL_GETMYTIMEPLANSETTIME, new String[][]{{"svcCode", svcCode}, {"ctn", encMobileNo}, {"prdc_cd", prdcCd}});
		logger.info("WSG URL: " + apiUrl);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> myTimePlanInfo = mapper.readValue(new URL(apiUrl), Map.class);
		logger.info("WSG RETURN: " + myTimePlanInfo);
//		logger.info("WSG RETURN: " + eggTotalUseInfo);
		
//		if ( myTimePlanInfo == null || YappUtil.isNotEq(myTimePlanInfo.getReturnCode(), EnumYn.C_1.getValue()) )
//			throw new YappWsgException(myTimePlanInfo.getReturnCode(), cmnService.getMsg(ERR_MSG_PREFIX + myTimePlanInfo.getReturnCode()));
		
		if ( myTimePlanInfo == null || YappUtil.isNotEq(myTimePlanInfo.get("returnCode"), EnumYn.C_1.getValue()) )
		{
			 if(myTimePlanInfo != null) {
				 logger.warn(cmnService.getMsg(ERR_MSG_PREFIX + myTimePlanInfo.get("returnCode")));
			 }else{
				 logger.warn(cmnService.getMsg(ERR_MSG_PREFIX));
			 }
			
			//throw new YappWsgException(myTimePlanInfo.get("returnCode"), cmnService.getMsg(ERR_MSG_PREFIX + myTimePlanInfo.get("returnCode"));
		}
		
		/*String prmtSbst = YappUtil.nToStr(myTimePlanInfo.getPrmtSbst());
		
		if ( prmtSbst.indexOf("시") > -1 && prmtSbst.indexOf("~") > -1 )
		{
			myTimePlanInfo.setStHour(YappUtil.toInt(prmtSbst.replaceAll("시", "").split("~")[0].trim()));
			myTimePlanInfo.setEdHour(YappUtil.toInt(prmtSbst.replaceAll("시", "").split("~")[1].trim()));
		}*/
		
		//return myTimePlanInfo;
	}
	
	/**
	 * GET 호출을 위한 URL 을 만든다.
	 */
	private String makeUrl(String url, String[] ... params)
	{
		StringBuffer sb = new StringBuffer();
		//String rtnUrl = url;
		sb.append(url);
		for ( int i = 0; i < params.length; i++ ) {
			if ( YappUtil.isEmpty(params[i][1]) )
				continue;
			
			sb.append((i == 0 ? "?" : "&") + params[i][0] + "=" + params[i][1]);
			//rtnUrl += (i == 0 ? "?" : "&") + params[i][0] + "=" + params[i][1];
		}
		//return rtnUrl;
		return sb.toString();
	}
	
	/*public static void main(String[] args) throws Exception
	{
		// TODO 메인 삭제
		String str = "07시 ~ 9시";
		WsgService ins = new WsgService();
	}*/
}
