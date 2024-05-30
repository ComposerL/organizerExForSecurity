package com.office.myorganizeruser.plan.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.office.myorganizeruser.plan.comment.CommentDto;

@Mapper
public interface ICommentMapper {

	public int registComment(int p_ori_no, String m_id, String c_txt);

	public List<CommentDto> getComments(int p_ori_no);
	
}
