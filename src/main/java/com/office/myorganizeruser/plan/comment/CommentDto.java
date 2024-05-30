package com.office.myorganizeruser.plan.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	private int c_no;
	private int p_ori_no;
	private String m_id;
	private String c_txt;
	private String c_reg_date;
	private String c_mod_date;
	
}
