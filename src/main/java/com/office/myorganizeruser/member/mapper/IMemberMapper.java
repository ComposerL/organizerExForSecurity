package com.office.myorganizeruser.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.office.myorganizeruser.member.MemberDto;

@Mapper
public interface IMemberMapper {

	public boolean isMember(String m_id);

	public int memberSignUpConfirm(MemberDto memberDto);

	public MemberDto memberSignInConfirm(MemberDto memberDto);

	public MemberDto getMemberById(String m_id);

	public int memberModifyConfirm(MemberDto memberDto);

	public int memberDeleteConfirm(String m_id);

	public List<MemberDto> getSearchFriends(String keyword);

}
