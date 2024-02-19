/*package com.kt.yapp.config;



import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.kt.yapp.util.YappUtil;

@Configuration
public class DataSourceConfiguration 
{
	static {
	}
	@Bean
	public DataSource dataSource(Environment env) 
	{
		return DataSourceBuilder
				.create()
				.username(YappUtil.decode(env.getProperty("spring.datasource.username")))
				.password(YappUtil.decode(env.getProperty("spring.datasource.password")))
				.driverClassName(env.getProperty("spring.datasource.driverClassName"))
				.url(env.getProperty("spring.datasource.url"))
				.build();
	}
}
*/