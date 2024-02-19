package com.kt.yapp.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.kt.yapp.util.YappUtil;

@Configuration
public class ApplicationConfig 
{
	/**
	 * 쿼리 로그 출력을 위한 함수
	 */
	//SIT는 주석처리. 개발은 주석을 풀어야됨.
	//운영외에는 사용하도록 처리 2023.06.13 by jk
	@Profile("!prod")
	@Bean
	public DataSource dataSource(Environment env)
	{
		//System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		//System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		return DataSourceBuilder
				.create()
				.username(YappUtil.decode(env.getProperty("spring.datasource.ucode")))
				.password(YappUtil.decode(env.getProperty("spring.datasource.pcode")))
				.driverClassName(env.getProperty("spring.datasource.driverClassName"))
				.url(env.getProperty("spring.datasource.url"))
				.build();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) 
	{
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
		return dataSourceTransactionManager;
	}
 
}
