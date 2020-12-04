package com.ujm.semweb.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import static com.google.common.base.Predicates.or;

@Configuration
@ComponentScan
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("itcps2-api-doc")
				.apiInfo(apiInfo()).select().paths(postPaths()).build();
	}
	private Predicate<String> postPaths() {
		return or(regex("/api./*"), regex("/api/*"));
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("ITCPS2 Project - CLOUD COMPUTING")
				.description("API reference for developers")
				.contact("univ.st-etienne@fr.com").license("Private License")
				.licenseUrl("univ.st-etienne@fr.com").version("3.02").build();
	}

}