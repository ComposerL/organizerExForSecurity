package com.office.myorganizeruser.plan;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.office.myorganizeruser.plan.util.UploadFileService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/plan")
public class PlanController {
	
	final private PlanSevice planSevice;
	final private UploadFileService uploadFileService;
	
	public PlanController(PlanSevice planSevice, UploadFileService uploadFileService) {
		this.planSevice = planSevice;
		this.uploadFileService = uploadFileService;
	}
	
	@GetMapping({"","/"})
	public String home(){
		log.info("[Organizer] home()");
		
		String nextPage = "plan/home";
		
		return nextPage;
		
	}
	
	@PostMapping("/writePlan")
	@ResponseBody
	public Object writePlan(PlanDto planDto, @RequestParam("file") MultipartFile file) {
		log.info("[Organizer] writePlan()");
		
		// save file
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String savedFileName = uploadFileService.upload(authentication.getName(), file);
		
		if(savedFileName != null) {
			planDto.setP_img_name(savedFileName);
			planDto.setM_id(authentication.getName());
			
			return planSevice.writePlan(planDto);
			
		}else {
			
			return null;
			
		}
		
	}
	
	@GetMapping("/getPlans")
	@ResponseBody
	public Object getPlans(@RequestParam("year") int year,@RequestParam("month") int month) {
		log.info("[Organizer] getPlans()");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<PlanDto> planDtos = planSevice.getPlans(authentication.getName(),year,month);
		
		return planDtos;
	}
	
	@GetMapping("/getPlan")
	@ResponseBody
	public Object getPlan(@RequestParam("p_no") int p_no) {
		log.info("[Organizer] getPlan()");
		
		PlanDto planDto = planSevice.getPlan(p_no);
		
		return planDto;
	}
	
	@DeleteMapping("/removePlan")
	@ResponseBody
	public Object removePlan(@RequestParam("p_no") int p_no) {
		log.info("[Organizer] removePlan()");
				
		return planSevice.removePlan(p_no);
	}
	
	@PutMapping("/modifyPlan")
	@ResponseBody
	public Object modifyPlan(PlanDto planDto, @RequestParam(value = "file", required = false) MultipartFile file) {
		log.info("[Organizer] modifyPlan()");
		
		if(file != null) {
			
			//savefile
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String savedFileName = uploadFileService.upload(authentication.getName(), file);
			
			if(savedFileName != null) {				
				planDto.setP_img_name(savedFileName);
				return planSevice.modifyPlan(planDto);				
			} else {				
				return null;				
			}
			
		} else {			
			return planSevice.modifyPlan(planDto);			
		}
		
	}
	
	@GetMapping("/getSearchFriends")
	@ResponseBody
	public Object getSearchFriends(@RequestParam("keyword") String keyword) {
		log.info("[Organizer] getSearchFriends()");
		
		return planSevice.getSearchFriends(keyword);
	}
	
	@PostMapping("/sharePlan")
	@ResponseBody
	public Object sharePlan(@RequestParam("p_no") int p_no, @RequestParam("m_no") int m_no, @RequestParam("m_id") String m_id) {
		log.info("[Organizer] sharePlan()");
		
		return planSevice.sharePlan(p_no,m_no,m_id);
	}
	
	
	
	
}
