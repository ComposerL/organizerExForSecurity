package com.office.myorganizeruser.plan.comment;

import org.springframework.stereotype.Service;

import com.office.myorganizeruser.plan.comment.mapper.ICommentMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CommentService {
	
	   final private ICommentMapper icommentMapper;
	   
	   public CommentService(ICommentMapper icommentMapper) {	
	      this.icommentMapper = icommentMapper;
	   }

	   // 일정 등록
	   public Object registComment(int p_ori_no, String m_id, String c_txt) {
	      log.info("registComment()");
	      
	      int result = icommentMapper.registComment(p_ori_no, m_id, c_txt);
	      
	      return result;
	   }

	public Object getComments(int p_ori_no) {
		log.info("registComment()");
		
		return icommentMapper.getComments(p_ori_no);
	}

}
