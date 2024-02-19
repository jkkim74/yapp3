package com.kt.yapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	//	System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	//	System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		return application.sources(YappApplication.class);
	}

}
