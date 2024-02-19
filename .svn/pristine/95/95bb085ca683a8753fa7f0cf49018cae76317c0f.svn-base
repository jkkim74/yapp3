package com.kt.yapp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kt.yapp.lamp.LampMenuHelper;
import com.kt.yapp.service.CmsService;

@Component
public class StartUpApplication implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(StartUpApplication.class);
	@Autowired private CmsService cmsService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			// 메뉴정보 loading
			LampMenuHelper.getInstance().Init(cmsService);
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}