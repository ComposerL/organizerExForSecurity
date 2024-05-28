package com.office.myorganizeruser.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.office.myorganizeruser.member.mapper.IMemberMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberService {
	
	final static public int MEMBER_ALREADY = -1;
	final static public int MEMBER_SIGN_UP_FAIL = 0;
	final static public int MEMBER_SIGN_UP_SUCCESS = 1;
	
	final private IMemberMapper iMemberMapper;
	
	final private PasswordEncoder passwordEncoder;
	
	@Autowired
	public MemberService(IMemberMapper iMemberMapper,PasswordEncoder passwordEncoder) {
		this.iMemberMapper = iMemberMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	// 회원 가입 확인
	public int memberSignUpConfirm(MemberDto memberDto) {
		log.info("memberSignUpConfirm()");
		
		// ID존재 여부 확인
		boolean isMember = iMemberMapper.isMember(memberDto.getM_id());
		
		// 회원 가입 진행
		if(!isMember) {
			log.info("This ID dose not Exist");
			
			String encodedPW = passwordEncoder.encode(memberDto.getM_pw());
			memberDto.setM_pw(encodedPW);
			
			int result = iMemberMapper.memberSignUpConfirm(memberDto);
			
			if(result <= 0) {
				log.info("MEMBER SIGN UP FAIL");
				
				return MEMBER_SIGN_UP_FAIL;
			}else {
				log.info("MEMBER SIGN UP SUCCESS");
				
				return MEMBER_SIGN_UP_SUCCESS;
			}
			
		} else {
			log.info("This ID Exist");
			
			return MEMBER_ALREADY;
		}
		
	}
	
	// 로그인 확인
	public MemberDto memberSignInConfirm(MemberDto memberDto) {
		log.info("memberSignInConfirm()");
		
		MemberDto loginedMemberDto = iMemberMapper.memberSignInConfirm(memberDto);
		
		return loginedMemberDto;
	}
	
	// 회원 ID로 회원 정보 조회
	public MemberDto getMemberById(String m_id) {
		log.info("memberSignInConfirm()");
		
		MemberDto loginedMemberDto = iMemberMapper.getMemberById(m_id);
		
		return loginedMemberDto;
	}
	
	// 회원 정보 수정 확인
	public int memberModifyConfirm(MemberDto memberDto) {
		log.info("memberModifyConfirm()");
		
		String encodedPW = passwordEncoder.encode(memberDto.getM_pw());
		memberDto.setM_pw(encodedPW);
		
		int result = iMemberMapper.memberModifyConfirm(memberDto);
		
		return result;
	}

	public int memberDeleteConfirm(String m_id) {
		log.info("memberDeleteConfirm()");
		
		int result = iMemberMapper.memberDeleteConfirm(m_id);
		
		return result;
	}
	
}
