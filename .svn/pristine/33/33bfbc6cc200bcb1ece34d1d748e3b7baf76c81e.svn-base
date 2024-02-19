package com.kt.yapp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.yapp.common.repository.CommonDao;
import com.kt.yapp.config.LimitationPropConfig;
import com.kt.yapp.domain.DataboxPullInfo;
import com.kt.yapp.util.YappUtil;

@Service
public class DboxService 
{
	@Autowired
	private CommonDao cmnDao;
	@Autowired
	private KosService kosService;

	@Autowired
	private LimitationPropConfig limitConfig;

	/**
	 * 데이터 꺼내기 정보를 조회한다.
	 */
	public DataboxPullInfo getDboxPullInfo(String cntrNo)
	{
		DataboxPullInfo dboxPullInfo = cmnDao.selectOne("mybatis.mapper.dbox.getDboxPullInfo", YappUtil.makeParamMap("cntrNo", cntrNo));
		if ( dboxPullInfo == null )
			dboxPullInfo = new DataboxPullInfo();
		
		dboxPullInfo.setDboxMaxPullCnt(limitConfig.getLmtDboxPullMaxCnt());
		
		return dboxPullInfo;
	}
	
	/**
	 * 데이터 꺼내기 정보 목록을 조회한다.
	 */
//	public List<DataboxPullInfo> getDboxPullInfoList(String cntrNo)
//	{
//		return cmnDao.selectList("mybatis.mapper.dbox.getDboxPullInfoList", YappUtil.makeParamMap("cntrNo", cntrNo));
//	}
	
	/**
	 * 데이터 꺼내기 정보를 추가한다.
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int pullData(String cntrNo, String mobileNo, int dataAmt) throws Exception
	{
		// 데이터박스 꺼내기 정보를 추가한다.
		Map<String, Object> paramObj = YappUtil.makeParamMap(new String[]{"cntrNo", "dataAmt"}, new Object[]{cntrNo, dataAmt});
		int insCnt = cmnDao.insert("mybatis.mapper.dbox.insertDboxPullInfo", paramObj);
		
		// 데이터 박스에서 데이터 꺼내기
		if(dataAmt > 1000){
			kosService.pullRemainData(cntrNo, mobileNo, 1000);
			
			kosService.pullRemainData(cntrNo, mobileNo, dataAmt - 1000);
		}else{
			kosService.pullData(cntrNo, mobileNo, dataAmt);
		}

		return insCnt;
	}
}
