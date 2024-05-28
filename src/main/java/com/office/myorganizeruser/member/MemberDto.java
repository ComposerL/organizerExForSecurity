package com.office.myorganizeruser.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //생성자 오버로딩 자동 생성 아웃라인에서 확인
@NoArgsConstructor // 디폴트 생성자 자동 생성
public class MemberDto {

	private int m_no;
	private String m_id;
	private String m_pw;
	private String m_mail;
	private String m_phone;
	private String m_role;
	private String m_reg_date;
	private String m_mod_date;
	
}
