package com.office.myorganizeruser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.office.myorganizeruser.member.MemberLoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
//	@Override //인터셉터 설정 시 
//	public void addInterceptors(InterceptorRegistry registry) { //인터셉터 역할
//		
//		registry.addInterceptor(new MemberLoginInterceptor())
//		.excludePathPatterns("/css/**","/img/**","/js/.**")
//		.addPathPatterns(
//				"/member/memberModify",
//				"/member/memberModifyConfirm",
//				"/member/memberDeleteConfirm"
//				);
//		
//	}
	
}
