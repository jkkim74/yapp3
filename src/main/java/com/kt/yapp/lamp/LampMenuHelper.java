package com.kt.yapp.lamp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kt.yapp.domain.LampMenu;
import com.kt.yapp.service.CmsService;
import com.kt.yapp.util.YappUtil;

public class LampMenuHelper {
	private static final Logger logger = LoggerFactory.getLogger(LampMenuHelper.class);
	private boolean bInit = false;
	private static List<LampMenu> LAMP_MENU_LIST = new ArrayList<LampMenu>();
	private CmsService cmsService;
	private final String serviceType = "SV002";
	
	/**
	 * 싱글톤 처리를 위한 홀더 클래스
	 */
	private static class LazyHolder {
		private static final LampMenuHelper INSTANCE = new LampMenuHelper();
	}
	
	/**
	 * 싱글톤 패턴이므로 외부에서 호출할 수 없다.
	 */
	private LampMenuHelper() {
		
	}
	
	/**
	 * 싱글톤 객체를 가져온다.
	 * @return User 권한 도우미객체
	 */
	public static LampMenuHelper getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	/**
	 * 초기화
	 */
	public void Init(CmsService cmsService){
		if( bInit == false ) bInit = true;
		this.cmsService = cmsService;
		
		Reload();
	}
	
	/**
	 * 메뉴 정보 Reload 한다.
	 */
	public void Reload(){
		this.setLampMenuList();
	}
	
	private void setLampMenuList() {
		LampMenu reqLampMenuInfo = new LampMenu();
		reqLampMenuInfo.setLampService(serviceType);
		
		LAMP_MENU_LIST.clear();
		LAMP_MENU_LIST = cmsService.getLampMenuList(reqLampMenuInfo);
		logger.info("lamp menu list >>>" + LAMP_MENU_LIST.toString());
	}
	
	public List<LampMenu> getMenuList() {
		return LAMP_MENU_LIST;
	}
	
	public LampMenu getMenuInfo(LampMenu reqLampMenu) {
		LampMenu rtnLampMenu = null;
		
		for(LampMenu lm : LAMP_MENU_LIST) {
			if(YappUtil.isEq(lm.getLampUrl(), reqLampMenu.getLampUrl()) 
					&& YappUtil.isEq(lm.getLampService(), reqLampMenu.getLampService())) {
				rtnLampMenu = lm;
				break;
			}
		}
		
		return rtnLampMenu;
	}
	
	public boolean getLampMenuCheck(LampMenu reqLampMenu) {
		boolean rtnVal = false;
		
		for(LampMenu lm : LAMP_MENU_LIST) {
			if(YappUtil.isEq(lm.getLampUrl(), reqLampMenu.getLampUrl()) 
					&& YappUtil.isEq(lm.getLampService(), reqLampMenu.getLampService())) {
				rtnVal = true;
				break;
			}
		}
		
		return rtnVal;
	}
}
