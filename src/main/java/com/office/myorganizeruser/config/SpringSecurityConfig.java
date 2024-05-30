package com.office.myorganizeruser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.office.myorganizeruser.member.MemberAccessDeniedHandler;
import com.office.myorganizeruser.member.mapper.IMemberMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	final private IMemberMapper iMemberMapper;
	
	public SpringSecurityConfig(IMemberMapper iMemberMapper) {
		this.iMemberMapper = iMemberMapper;
	}
	
	
	@Bean PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");
		return new BCryptPasswordEncoder();
	}
	
	@Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		//필터 등록
		
		http
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							"/css/**",
							"/img/**",
							"/js/**",
							"/",
							"/member/memberSignUp",
							"/member/memberSignUpConfirm",
							"/member/memberSignIn",
							"/member/memberSignInConfirm",
							"/member/memberSignInResult").permitAll()
					.requestMatchers("/plan/**").hasRole("USER_APPROVED")
					.anyRequest().authenticated()
					);
		
		http
			.exceptionHandling(exceptionConfig -> exceptionConfig
//					.accessDeniedPage("/member/accessDeniedPage") // 기본 제공해주는 객체 사용할 때
					.accessDeniedHandler(new MemberAccessDeniedHandler()) //accessDeniedHandler 객체를 따로 만들어서 사용할 때
					);
		
		http
			.formLogin(login -> login
					.loginPage("/member/memberSignIn")
					.loginProcessingUrl("/member/memberSignInConfirm")
					.usernameParameter("m_id")
					.passwordParameter("m_pw")
					.successHandler((request, response, authentication) -> {
						log.info("MEMBER SIGN IN SUCCESS HANDLER");
						
						String targetURL = "/member/memberSignInResult?logined=true";
						
						RequestCache requestCache = new HttpSessionRequestCache(); //어떤 페이지로 가다가 막혔는지 확인해서 보내주는 객체
						SavedRequest savedRequest = requestCache.getRequest(request, response);
						if(savedRequest != null) {
							targetURL = savedRequest.getRedirectUrl();
							requestCache.removeRequest(request, response);
							
						}
						
						response.sendRedirect(targetURL);
					})
					.failureHandler((request, response, exception) -> {
						log.info("MEMBER SIGN IN FAIL HANDLER");
						log.info("exceptio: {}",exception.getMessage());
						response.sendRedirect("/member/memberSignInResult?logined=false");
					})
					);
		
		http
			.logout(logout -> logout
					.logoutUrl("/member/memberSignOutConfirm")
					.logoutSuccessHandler((request,response,authentication) -> {
						log.info("MEMBER SIGN OUT SUCCESS");
						response.sendRedirect("/");
					})
					);
		
		return http.build();
	}
	
}
