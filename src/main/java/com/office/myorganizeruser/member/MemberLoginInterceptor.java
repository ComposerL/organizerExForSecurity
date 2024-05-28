package com.office.myorganizeruser.member;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MemberLoginInterceptor implements HandlerInterceptor{ //세션체크 인터셉터

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession(false); // false 넣는 이유는??
		
		if(session != null) {
			
			Object obj = session.getAttribute("loginedMemberId");
			
			if(obj != null) {
				return true;
				
			}
			
		}
		
		response.sendRedirect(request.getContextPath() + "/member/memberSignIn");
		return false;
		
	}
	
}
