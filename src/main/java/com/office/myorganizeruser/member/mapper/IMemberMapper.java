package com.office.myorganizeruser.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.office.myorganizeruser.member.MemberDto;

@Mapper
public interface IMemberMapper {

	boolean isMember(String m_id);

	int memberSignUpConfirm(MemberDto memberDto);

	MemberDto memberSignInConfirm(MemberDto memberDto);

	MemberDto getMemberById(String m_id);

	int memberModifyConfirm(MemberDto memberDto);

	int memberDeleteConfirm(String m_id);

}
