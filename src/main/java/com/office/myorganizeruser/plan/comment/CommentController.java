package com.office.myorganizeruser.plan.comment;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/comment")
public class CommentController {
	
	   final private CommentService commentService;
	   
	   public CommentController(CommentService commentService) {
	      this.commentService = commentService;
	   
	   }
	   
	   // 댓글 등록
	   @PostMapping("/registComment")
	   public Object registComment(
	         @RequestParam("comment") String c_txt, 
	         @RequestParam("p_ori_no") int p_ori_no) {
	      log.info("registComment()");
	      
	      Authentication authentication = 
	            SecurityContextHolder.getContext().getAuthentication();
	      
	      return commentService.registComment(p_ori_no, authentication.getName(), c_txt);
	      
	   }
	   
	   @GetMapping("/getComments")
	   public Object getComments(@RequestParam("p_ori_no") int p_ori_no) {
		   	log.info("getComments()");
		   	
		   	return commentService.getComments(p_ori_no);
	   }
	   

}
