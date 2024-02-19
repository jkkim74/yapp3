package com.kt.yapp.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter 
{
	private static final String BASE_PACKAGE = "com.kt.yapp";
	
	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(PathSelectors.any()).build();
		
		docket.globalResponseMessage(RequestMethod.GET, new ArrayList<ResponseMessage>());
		docket.globalResponseMessage(RequestMethod.POST, new ArrayList<ResponseMessage>());
		
		return docket;
	}
}
