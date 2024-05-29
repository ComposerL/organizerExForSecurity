package com.office.myorganizeruser.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {
	
	
	final private MemberService memberService;
	
	@Autowired //@Resource @Inject 비슷한 기능 @Autowired는 생성자 필드 다 사용 가능 하지만 데이터 무결성을 지키기 위해 요렇게 쓴다.
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	// 회원 가입 양식
	@GetMapping("/memberSignUp")
	public String memberSignUp() {
		log.info("memberSignUp()");
		
		String nextPage = "member/signUp";
		
		return nextPage;
	}
	
	
	// 회원 가입 확인
	@PostMapping("/memberSignUpConfirm")
	public String memberSignUpConfirm(MemberDto memberDto) {
		log.info("memberSignUpConfirm()");
		
		String nextPage = "member/signUp_ok";
		
		int result = memberService.memberSignUpConfirm(memberDto);
		
		if(result == MemberService.MEMBER_ALREADY || result == MemberService.MEMBER_SIGN_UP_FAIL) {
			nextPage = "member/signUp_ng";
		}
		
		return nextPage;
	}
	
	// 회원 로그인 양식
	@GetMapping("/memberSignIn")
	public String memberSignIn() {
		log.info("memberSignIn()");
		
		String nextPage = "member/signIn";
		
		return nextPage;
	}
	
	// 회원 로그인 확인(시큐리티 적용 전)
//	@PostMapping("/memberSignInConfirm")
//	public String memberSignInConfirm(MemberDto memberDto, HttpSession session) {
//		log.info("memberSignInConfirm()");
//		
//		String nextPage = "member/signIn_ok";
//		
//		MemberDto loginedMemberDto = memberService.memberSignInConfirm(memberDto);
//		
//		if(loginedMemberDto != null) {
//			session.setAttribute("loginedMemberId", loginedMemberDto.getM_id());
//			session.setMaxInactiveInterval(60 * 30);
//		}else {
//			nextPage = "member/signIn_ng";
//		}
//		
//		return nextPage;
//	}
	// 회원 로그인 확인(시큐리티 적용 후)
	@GetMapping("/memberSignInResult")
	public String memberSignInResult(@RequestParam( value = "logined", required = false) Boolean logined) {		
		log.info("memberSignInResult()");
		
		String nextPage = "member/signIn_ok";
		
		if(!logined) {
			nextPage = "member/signIn_ng";
		}
		
		return nextPage;
	}
	
	
	// 회원 정보 수정 양식
	@GetMapping("/memberModify")
	public String memberModify(HttpSession session, Model model) {
		log.info("memberModify()");
		
		String nextPage = "member/modify";
		
		//시큐리티 적용 전 session 사용
//		Object obj = session.getAttribute("loginedMemberId");
		
//		if(obj == null) {
//			return "redirect:/member/memberSignIn";
//		}
		
//		String loginedMemberId = String.valueOf(obj);
		
		//시큐리티 적용 후 SecurityContextHolder 사용
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		MemberDto loginedMemberDto = memberService.getMemberById(authentication.getName());
		model.addAttribute("loginedMemberDto",loginedMemberDto);
		
		return nextPage;
	}
	
	
	// 회원 정보 수정 확인
	@PostMapping("/memberModifyConfirm")
	public String memberModifyConfirm(MemberDto memberDto, HttpSession session) {
		log.info("memberModifyConfirm()");
		
		String nextPage = "member/modify_ok";
		
		Object obj = session.getAttribute("loginedMemberId");
//		if(obj == null) {
//			return "redirect:/member/memberSignIn";
//		}
		
		int result = memberService.memberModifyConfirm(memberDto);
		
		if(result <= 0) {
			nextPage = "member/modify_ng";
		}
		
		return nextPage;
	}
	
	//회원 로그아웃 (시큐리티 적용 전)
//	@GetMapping("/memberSignOutConfirm")
//	public String memberSignOutConfirm(HttpSession session) {
//		log.info("memberSignOutConfirm()");
//		
//		String nextPage = "redirect:/";
//		
//		session.invalidate();
//		
//		return nextPage;
//	}
	
	// 회원 정보 삭제 확인
	@GetMapping("/memberDeleteConfirm")
	public String memberDeleteConfirm(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		log.info("memberDeleteConfirm()");
		
		String nextPage = "member/delete_ok";
		
//		Object obj = session.getAttribute("loginedMemberId");
//		if(obj == null) { //인터셉터로 세션체크하면 주석처리
//			return "redirect:/member/memberSignIn";
//		}
		
//		String loginedMemberId = String.valueOf(obj);
//		int result = memberService.memberDeleteConfirm(loginedMemberId);
//		
//		if(result <= 0) {
//			nextPage = "member/delete_ng";
//		}
		
//		session.invalidate(); // 세션 무효화 = 객체가 사라지는게 아니라 비즈니스 로직이 끝나고 난 후 다시 리퀘스트가 오는 순간에 GC가 수거해가고 새로운 세션객체가 생성된다.
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //인증 되어있는 객체 반환		
		
		int result = memberService.memberDeleteConfirm(authentication.getName());
		
		if(result <= 0) {
			
			nextPage = "member/delete_ng";
			
		}else {
			
			new SecurityContextLogoutHandler().logout(request, response, authentication); //정보 삭제 후 로그아웃 처리
			
		}
		
		return nextPage;
	}	
	
	@GetMapping("/accessDeniedPage")
	public String accessDeniedPage() {
		log.info("accessDeniedPage()");
		
		
		return "member/access_denied_page";
	}
	
	
}
