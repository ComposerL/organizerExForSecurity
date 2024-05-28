package com.office.myorganizeruser.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.office.myorganizeruser.member.mapper.IMemberMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberDetailService implements UserDetailsService{
	
	
	final private IMemberMapper iMemberMapper;
	
	public MemberDetailService(IMemberMapper iMemberMapper) {
		this.iMemberMapper = iMemberMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername()");
		
		MemberDto memberDto = iMemberMapper.getMemberById(username);
				
		return User
				.builder()
				.username(memberDto.getM_id())
				.password(memberDto.getM_pw())
				.roles(memberDto.getM_role())
				.build();
	}

}
