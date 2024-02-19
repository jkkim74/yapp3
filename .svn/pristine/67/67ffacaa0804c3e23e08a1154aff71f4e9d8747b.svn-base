package com.kt.yapp.service;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.domain.EventGiftJoin;
import com.kt.yapp.domain.TicketGiftIssueInfoData;
import com.kt.yapp.domain.TicketGiftRewardData;
import com.kt.yapp.domain.TicketRewardData;
import com.kt.yapp.exception.YappRuntimeException;
import com.kt.yapp.util.YappUtil;

@Service
public class TicketService {

	private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

	@Autowired
	private CommonDao cmnDao;
	
	@Autowired
	private CommonService cmnService;
	
	/**
	 * 사용자의 응모권 발급 정보를 등록한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertEventTicket(EventGiftJoin paramObj)
	{
		return cmnDao.insert("mybatis.mapper.ticket.insertEventTicket", paramObj);
	}
	
	/**
	 * 응모권상품발행건수 조회 210818
	 */
	public int getTicketGiftIssueCnt(int round)
	{
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftIssueCnt", round);
	}
	
	/**
	 * 응모권꽝 발행건수 조회 210827
	 */
	public int getTicketGiftBlankCnt(int round)
	{
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftBlankCnt", round);
	}
	
	/**
	 * 응모권 당첨순번 조회 210910
	 */
	public int getTicketWinNum(int round)
	{
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketWinNum", round);
	}
	
	/**
	 * 210818 응모권당첨상품정보 조회
	 * @param round
	 * @param winNum
	 * @return
	 */
	public TicketGiftRewardData getTicketGiftRewardDetail(int round, int winNum)
	{
		TicketGiftRewardData paramObj = new TicketGiftRewardData();
		paramObj.setRound(round);
		paramObj.setWinNum(winNum);
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftRewardDetail", paramObj);
	}
	
	/**
	 * 20210902 응모권리워드정보 조회
	 * @param ticketSeq
	 * @return
	 */
	public TicketRewardData getTicketRewardInfo(int ticketSeq)
	{
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketRewardInfo", ticketSeq);
	}
	
	
	/**
	 * 210818 상품소진여부 조회
	 * @param round
	 * @return
	 */
	public List<TicketGiftRewardData> getTicketGiftTotal(int round)
	{
		return cmnDao.selectList("mybatis.mapper.ticket.getTicketGiftTotal", round);
	}
	
	/**
	 * 210818 미발행 쿠폰 정보 조회
	 * @param round, giftCode
	 * @return
	 */
	public TicketGiftRewardData getTicketGiftCouponInfo(int round, String giftCode){
		Map<String, Object> paramObj = 
				YappUtil.makeParamMap(new String[]{"round", "giftCode"}, new Object[]{round, giftCode});
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftCouponInfo", paramObj);
	}
	
	/**
	 * 210818 응모권당첨상품리워드테이블 등록 
	 * @param paramObj
	 * @return gift_issue_seq
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int insertTicketGiftRewardInfo(TicketGiftRewardData paramObj){
		return cmnDao.insert("mybatis.mapper.ticket.insertTicketGiftRewardInfo", paramObj);
	}
	
	/**
	 * 210830 응모권당첨상품리워드테이블 당첨순번 UPDATE
	 * @param paramObj
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateTicketUseGift(TicketGiftRewardData paramObj){
		logger.info("updateTicketUseGift paramObj : "+paramObj);

		int rtnVal = 0;
		
		//응모권 상품 리워드 테이블 update 210830 응모권당첨상품리워드테이블 당첨순번 UPDATE
		rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardInfo", paramObj);
		logger.info("1 rtnVal : "+rtnVal);

		//210923 쿠폰테이블 update
		if(!YappUtil.isEmpty(paramObj.getCouponNo())){
			rtnVal += cmnDao.update("mybatis.mapper.ticket.updateGiftCouponIssueInfo", paramObj);
			logger.info("2 rtnVal : "+rtnVal);

		}
		//
		
		//tb_ticket_reward_info gift_issue_seq, win_yn update
		//응모권리워드 테이블 update
		//210901 응모권 리워드 정보를 업데이트한다.
		rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", paramObj);
		logger.info("3 rtnVal : "+rtnVal);

		
		//tb_ticket_gift_issue_info 발행건수 update
		//발행건수 update
		if(paramObj.getTicketGiftSeq() != 0){
			rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftIssueInfo", paramObj);
			
			logger.info("4 rtnVal : "+rtnVal);

		}
		
		return rtnVal;
	}
	
	/**
	 * 210818 응모권발행상품정보 조회
	 * @param round
	 * @param giftCode
	 * @return
	 */
	public TicketGiftIssueInfoData getTicketGiftIssueInfo(int round, String giftCode)
	{
		TicketGiftRewardData paramObj = new TicketGiftRewardData();
		paramObj.setRound(round);
		paramObj.setGiftCode(giftCode);
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftIssueInfo", paramObj);
	}
	
	/**
	 * 210818 응모권발행상품정보 조회
	 * @param ticketGiftSeq
	 * @return
	 */
	public TicketGiftIssueInfoData getTicketGiftIssueInfo(int ticketGiftSeq)
	{
		TicketGiftRewardData paramObj = new TicketGiftRewardData();
		paramObj.setTicketGiftSeq(ticketGiftSeq);
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftIssueInfo", paramObj);
	}
	
	/**
	 * 210903 응모권당첨상품리워드테이블 데이터지급여부 UPDATE.
	 * @param paramObj
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateTicketGiftDataGiveInfo(TicketGiftRewardData paramObj){
		return cmnDao.update("mybatis.mapper.ticket.updateTicketGiftDataGiveInfo", paramObj);
	}
	
	/**
	 * 210903 응모권상품리워드 정보 조회
	 * @param round
	 * @param giftCode
	 * @return
	 */
	public TicketGiftRewardData getTicketGiftRewardIssueDetail(int giftIssueSeq)
	{
		return cmnDao.selectOne("mybatis.mapper.ticket.getTicketGiftRewardIssueDetail", giftIssueSeq);
	}
	
	/**
	 * 210907 응모권당첨상품리워드테이블 주소정보 UPDATE.
	 * @param paramObj
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int updateTicketGiftRewardAddressInfo(TicketGiftRewardData paramObj){
		return cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAddressInfo", paramObj);
	}
	
	/**
	 * 220104 응모권당첨 로직
	 * @param paramObj
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public TicketRewardData upsertTicketWinNum(TicketGiftRewardData paramObj, String infYn){
		TicketRewardData resultData = new TicketRewardData();

		String chkMsg = "";
		TicketGiftRewardData firstTicketGiftReward = new TicketGiftRewardData();
		int rtnVal = 0;
		
		//순번있으면 UPDATE / 없으면 INSERT
		//응모권 상품 리워드 테이블
		try{
			firstTicketGiftReward = cmnDao.selectOne("mybatis.mapper.ticket.upsertTicketWinNum",paramObj);
			firstTicketGiftReward.setTicketSeq(paramObj.getTicketSeq());
			
		}catch (RuntimeException e) {
			logger.info("upsertTicketWinNum insert/update error");
			//응모권 사용자가 많습니다. 잠시후 다시 이용해주세요.
			chkMsg = cmnService.getMsg("TICKET_ERROR_TRAFFIC");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
		
		//INSERT/UPDATE 실패
		}catch(Exception e){
			logger.info("upsertTicketWinNum insert/update error");
			//응모권 사용자가 많습니다. 잠시후 다시 이용해주세요.
			chkMsg = cmnService.getMsg("TICKET_ERROR_TRAFFIC");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
		}

		if(YappUtil.isEmpty(firstTicketGiftReward)){
			//error
			//응모권 사용자가 많습니다. 잠시후 다시 이용해주세요.
			chkMsg = cmnService.getMsg("TICKET_ERROR_TRAFFIC");
			throw new YappRuntimeException("CHECK_MSG", chkMsg);
			
		//INSERT/UPDATE 성공 
		}else{
			
			logger.info("upsertTicketWinNum 쿼리 성공!");

			//응모권 상품 정보 조회
			//1~3등 당첨여부(TICKET_GIFT_SEQ >0 ) + 온라인쿠폰여부/ 뽑기진행여부(TICKET_GIFT_SEQ -1일때 체크위해)
			//TB_TICKET_GIFT_INFO (응모권상품테이블) 상품코드/상품타입조회(온라인 쿠폰 구별위해)
			TicketGiftIssueInfoData giftTypeCodeData = cmnDao.selectOne("mybatis.mapper.ticket.getGiftCodeType", firstTicketGiftReward.getTicketGiftSeq());

			//INSERT 성공시 (4~6등 뽑기진행)
			//3분의 1확률 당첨시 TICKET_GIFT_SEQ -1  셋팅 뽑기 진행 후, TICKET_GIFT_SEQ UPDATE
			if(YappUtil.isEmpty(giftTypeCodeData)){
				logger.info("뽑기 진행!");

				//GC004  온라인 쿠폰 / GC005 데이터 쿠폰 / GC006 꽝
				//상품코드 배열
				List<String> giftCodeList = new ArrayList<String>();
				giftCodeList.add("GC004"); //온라인 쿠폰
				giftCodeList.add("GC005"); //데이터 쿠폰
				giftCodeList.add("GC006"); //꽝 
				
				//뽑기 진행
				SecureRandom tmpRandom = new SecureRandom();
				int randomInt = tmpRandom.nextInt(3);
				
				//배열에서 랜덤뽑기 3분의 1 확률
				String giftCode = giftCodeList.get(randomInt);
				logger.info("당첨! randomInt : "+randomInt);
				logger.info("당첨! giftCode : "+giftCode);

				//온라인쿠폰 당첨시, 로직
				if(giftCode.equals("GC004")){
				
					//TB_GIFT_COUPON_INFO 상품 쿠폰 정보 테이블  발행여부 update
					firstTicketGiftReward.setGiftCode(giftCode);
					TicketGiftRewardData couponData = cmnDao.selectOne("mybatis.mapper.ticket.getCouponNo", firstTicketGiftReward);
					
					logger.info("4등 온라인쿠폰 당첨 couponData : "+couponData);
					
					//온라인쿠폰 소진시, 꽝처리
					if(YappUtil.isEmpty(couponData)){
						firstTicketGiftReward.setWinYn("N");
						firstTicketGiftReward.setGiftCode("GC006");
						
						logger.info("온라인쿠폰 꽝 rtnVa 0 : "+rtnVal);

						
						//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 꽝 업데이트 ticket_gift_seq 0으로  update
						rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
						logger.info("온라인쿠폰 꽝 rtnVa 1 : "+rtnVal);
						//ticket_reward_info 응모권 리워드테이블 update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
						logger.info("온라인쿠폰 꽝 rtnVa 2 : "+rtnVal);
						if(rtnVal != 2){
							//error
							//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
							chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
							
						}else{
							logger.info("온라인쿠폰 꽝 성공 : ");
							String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
							logger.info("온라인쿠폰 꽝 성공 ticktModDt : "+ticktModDt);
							// 지급로직이후 팝업창에 쓰일 데이터 return
							resultData.setWinYn(firstTicketGiftReward.getWinYn());
							resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용 날짜
							
						}
						
					//온라인 쿠폰 발행
					}else{
						int ticketGiftSeq= getTicketGiftIssueInfo(firstTicketGiftReward.getRound(), giftCode).getTicketGiftSeq();

						firstTicketGiftReward.setCouponNo(couponData.getCouponNo());
						firstTicketGiftReward.setWinYn("Y");
						firstTicketGiftReward.setGiftCode(giftCode);
						firstTicketGiftReward.setTicketGiftSeq(ticketGiftSeq);
						logger.info("온라인쿠폰  rtnVa 0 : "+rtnVal);

						//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 응모권상품시퀀스 업데이트 ticket_gift_seq update
						rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
						logger.info("온라인쿠폰 rtnVa 1 : "+rtnVal);

						//ticket_reward_info 응모권 리워드테이블 update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
						logger.info("온라인쿠폰  rtnVa 2 : "+rtnVal);
						
						//응모권 상품 발행 정보 테이블 발행건수 업데이트
						//TB_TICKET_GIFT_ISSUE_INFO ISSUE_CNT update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftIssueInfo", ticketGiftSeq);
						logger.info("온라인쿠폰 rtnVa 3 : "+rtnVal);

						//update error
						if(rtnVal != 3){
							//error
							//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
							chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
							
						//update 성공
						}else{
							logger.info("온라인쿠폰 성공 : ");
							TicketGiftIssueInfoData giftTypeCodeData2 = cmnDao.selectOne("mybatis.mapper.ticket.getGiftCodeType", ticketGiftSeq);

							String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
							logger.info("온라인쿠폰 성공 ticktModDt : "+ticktModDt);
							resultData.setGiftName(giftTypeCodeData2.getGiftName());
							resultData.setGiftType(giftTypeCodeData2.getGiftType());
							resultData.setValidStartDt(giftTypeCodeData2.getValidStartDt());
							resultData.setValidEndDt(giftTypeCodeData2.getValidEndDt());
							resultData.setWinYn(firstTicketGiftReward.getWinYn());
							resultData.setGiftIssueSeq(firstTicketGiftReward.getGiftIssueSeq());
							resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용날짜
							
						}
					}
					
				//데이터쿠폰 당첨시, 로직
				}else if(giftCode.equals("GC005")){

					//무제한 요금제가 아닐때
					//준회원도 패스
					if(!infYn.equals("Y")&&paramObj.getCntrNo()!=null){
						//데이터쿠폰 소진 체크
						int ticketGiftSeq = getTicketGiftIssueInfo(firstTicketGiftReward.getRound(), giftCode).getTicketGiftSeq();
						
						int soldOutYn = cmnDao.selectOne("mybatis.mapper.ticket.getSoldOutYn", ticketGiftSeq);
						
						logger.info("데이터쿠폰 당첨! 상품 시퀀스 ticketGiftSeq : "+ticketGiftSeq);
						logger.info("데이터쿠폰 소진 여부 soldOutYn : "+soldOutYn);

						//데이터쿠폰 지급
						if(soldOutYn > 0){

							firstTicketGiftReward.setGiftCode(giftCode);
							firstTicketGiftReward.setWinYn("Y");
							firstTicketGiftReward.setDataGiveYn("N");
							firstTicketGiftReward.setTicketGiftSeq(ticketGiftSeq);
							logger.info("데이터쿠폰 rtnVa 0 : "+rtnVal);

							//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 쿠폰번호 업데이트 couponNo update
							rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
							logger.info("데이터쿠폰 rtnVa 1 : "+rtnVal);
							//ticket_reward_info 응모권 리워드테이블 update
							rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
							logger.info("데이터쿠폰 rtnVa2 : "+rtnVal);
							
							//응모권 상품 발행 정보 테이블 발행건수 업데이트
							//TB_TICKET_GIFT_ISSUE_INFO ISSUE_CNT update
							rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftIssueInfo", ticketGiftSeq);
							logger.info("데이터쿠폰 rtnVa3 : "+rtnVal);

							if(rtnVal !=3){
								//error
								//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
								chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
								throw new YappRuntimeException("CHECK_MSG", chkMsg);
								
							//update 성공
							}else{
								logger.info("update success : ");
								TicketGiftIssueInfoData giftTypeCodeData2 = cmnDao.selectOne("mybatis.mapper.ticket.getGiftCodeType", ticketGiftSeq);

								String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
								logger.info("데이터쿠폰 update success ticktModDt : "+ticktModDt);
								
								resultData.setGiftName(giftTypeCodeData2.getGiftName());
								resultData.setGiftType(giftTypeCodeData2.getGiftType());
								resultData.setValidStartDt(giftTypeCodeData2.getValidStartDt());
								resultData.setValidEndDt(giftTypeCodeData2.getValidEndDt());
								resultData.setWinYn(firstTicketGiftReward.getWinYn());
								resultData.setGiftIssueSeq(firstTicketGiftReward.getGiftIssueSeq());
								resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용날짜
								resultData.setDataGiveYn(firstTicketGiftReward.getDataGiveYn()); //데이터 지급여부
								
							}
							
						//소진시, 꽝
						}else{
							firstTicketGiftReward.setWinYn("N");
							firstTicketGiftReward.setGiftCode("GC006");
							
							logger.info("꽝 rtnVal 0 : "+rtnVal);

							//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 꽝 업데이트 ticket_gift_seq 0으로  update
							rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
							logger.info("꽝 rtnVal 1 : "+rtnVal);

							//ticket_reward_info 응모권 리워드테이블 update
							rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
							logger.info("꽝 rtnVal 2: "+rtnVal);

							if(rtnVal != 2){
								//error
								//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
								chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
								throw new YappRuntimeException("CHECK_MSG", chkMsg);
								
							}else{
								logger.info("꽝 : ");

								String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
								logger.info("꽝 ticktModDt : "+ticktModDt);

								// 지급로직이후 팝업창에 쓰일 데이터 return
								resultData.setWinYn(firstTicketGiftReward.getWinYn());
								resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용 날짜
								
							}
						}
						
					//무제한 요금제일경우, 꽝처리
					}else{
						firstTicketGiftReward.setWinYn("N");
						firstTicketGiftReward.setGiftCode("GC006");
						
						//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 꽝 업데이트 ticket_gift_seq 0으로  update
						rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
						
						//ticket_reward_info 응모권 리워드테이블 update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);

						if(rtnVal != 2){
							//error
							//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
							chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
							
						}else{
							String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
							
							// 지급로직이후 팝업창에 쓰일 데이터 return
							resultData.setWinYn(firstTicketGiftReward.getWinYn());
							resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용 날짜
							
						}
					}
					
				//꽝 당첨시, 로직
				}else{
					firstTicketGiftReward.setWinYn("N");
					firstTicketGiftReward.setGiftCode(giftCode);
					
					//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 꽝 업데이트 ticket_gift_seq 0으로  update
					//rtnVal 업데이트 성공시 1 리턴
					rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
					
					//ticket_reward_info 응모권 리워드테이블 update
					//rtnVal 업데이트 성공시 1 리턴
					rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);

					if(rtnVal != 2){
						//error
						//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
						chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
						
					}else{
						String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
						
						// 지급로직이후 팝업창에 쓰일 데이터 return
						resultData.setWinYn(firstTicketGiftReward.getWinYn());
						resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용 날짜
						
					}
					
				}
				
			//UPDATE 성공시(1~3등 당첨 진행)
			}else{
				
				//1,2,3 당첨시
				// 온라인 쿠폰일시 쿠폰 테이블에서 쿠폰번호 update
				//온라인 쿠폰 일때
				if(giftTypeCodeData.getGiftType().equals("G0002")){

					//TB_GIFT_COUPON_INFO 상품 쿠폰 정보 테이블  발행여부 update
					TicketGiftRewardData couponData = cmnDao.selectOne("mybatis.mapper.ticket.getCouponNo", giftTypeCodeData);
					
					logger.info("1~3당첨 온라인쿠폰 당첨 couponData : "+couponData);

					//쿠폰테이블에 유효한 쿠폰(조건에 맞는)이 없을때, 에러
					if(YappUtil.isEmpty(couponData)){
						//error
						//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
						chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
						
					//쿠폰 번호을 응모권 상품 리워드 테이블 UPDATE
					}else{
						firstTicketGiftReward.setCouponNo(couponData.getCouponNo());
						firstTicketGiftReward.setGiftCode(giftTypeCodeData.getGiftCode());
						firstTicketGiftReward.setWinYn("Y");
						logger.info("온라인쿠폰 1~3등  rtnVal 0 : "+rtnVal);

						//TB_TICKET_GIFT_REWARD_INFO 응모권 상품 리워드 테이블 쿠폰번호 업데이트 couponNo update
						rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketGiftRewardAll", firstTicketGiftReward);
						logger.info("온라인쿠폰 1~3등  rtnVal 1 : "+rtnVal);

						//ticket_reward_info 응모권 리워드테이블 update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
						logger.info("온라인쿠폰 1~3등  rtnVal 2 : "+rtnVal);

						//응모권 상품 발행 정보 테이블 발행건수 업데이트
						//TB_TICKET_GIFT_ISSUE_INFO ISSUE_CNT update
						rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftIssueInfo", firstTicketGiftReward);
						logger.info("온라인쿠폰 1~3등  rtnVal 3 : "+rtnVal);

						//update error
						if(rtnVal != 3){
							//error
							//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
							chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
							throw new YappRuntimeException("CHECK_MSG", chkMsg);
							
						//update 성공
						}else{
							logger.info("온라인쿠폰 1~3등  ");

							String ticktModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
							logger.info("온라인쿠폰 1~3등  ticktModDt 0 : "+ticktModDt);

							resultData.setGiftName(giftTypeCodeData.getGiftName());
							resultData.setGiftType(giftTypeCodeData.getGiftType());
							resultData.setValidStartDt(giftTypeCodeData.getValidStartDt());
							resultData.setValidEndDt(giftTypeCodeData.getValidEndDt());
							resultData.setWinYn(firstTicketGiftReward.getWinYn());
							resultData.setGiftIssueSeq(firstTicketGiftReward.getGiftIssueSeq());
							resultData.setTicketGiftRegDt(ticktModDt); //응모권 사용 날짜
							
						}
						
					}
			
				//온라인 쿠폰 아닐때
				}else{
					firstTicketGiftReward.setWinYn("Y");
					
					logger.info("1~3당첨 오프라인상품 당첨! ");
					
					//ticket_reward_info 응모권 리워드테이블 update
					rtnVal = cmnDao.update("mybatis.mapper.ticket.updateTicketReward", firstTicketGiftReward);
					logger.info("오프라인상품 1~3등  rtnVal 1 : "+rtnVal);

					//응모권 상품 발행 정보 테이블 발행건수 업데이트
					//TB_TICKET_GIFT_ISSUE_INFO ISSUE_CNT update
					rtnVal += cmnDao.update("mybatis.mapper.ticket.updateTicketGiftIssueInfo", firstTicketGiftReward);
					logger.info("오프라인상품 1~3등  rtnVal 2 : "+rtnVal);

					//update error
					if(rtnVal != 2){
						//error
						//응모권 사용 중 문제가 발생하였습니다. 잠시후 다시 이용해주세요.
						chkMsg = cmnService.getMsg("CHK_TICKET_USE_ERROR");
						throw new YappRuntimeException("CHECK_MSG", chkMsg);
						
					//update 성공
					}else{
						logger.info("오프라인상품 1~3등 : ");

						String ticktGiftModDt = getTicketRewardInfo(firstTicketGiftReward.getTicketSeq()).getModDt();
						logger.info("오프라인상품 1~3등  ticktGiftModDt : "+ticktGiftModDt);

						resultData.setGiftName(giftTypeCodeData.getGiftName());
						resultData.setGiftType(giftTypeCodeData.getGiftType());
						resultData.setValidStartDt(giftTypeCodeData.getValidStartDt());
						resultData.setValidEndDt(giftTypeCodeData.getValidEndDt());
						resultData.setWinYn(firstTicketGiftReward.getWinYn());
						resultData.setGiftIssueSeq(firstTicketGiftReward.getGiftIssueSeq());
						resultData.setTicketGiftRegDt(ticktGiftModDt);
						
					}
				}
				
			}
			
		}
		
		return resultData;
	}
}
