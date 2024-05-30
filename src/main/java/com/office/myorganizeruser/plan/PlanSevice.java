package com.office.myorganizeruser.plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.office.myorganizeruser.member.mapper.IMemberMapper;
import com.office.myorganizeruser.plan.mapper.IPlanMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PlanSevice {
	
	final static public int ALREADY_SHARED_PLAN = -1; //이미 공유된 일정
	final static public int SHARE_PLAN_FAIL = 0; //일정 공유 실패
	final static public int SHARE_PLAN_SUCCESS = 1; //일정 공유 성공

	final private IPlanMapper iPlanMapper;
	final private IMemberMapper iMemberMapper;
	
	public PlanSevice(IPlanMapper iPlanMapper, IMemberMapper iMemberMapper) {
		this.iPlanMapper = iPlanMapper;
		this.iMemberMapper = iMemberMapper;
	}

	public Object writePlan(PlanDto planDto) {
		log.info("writePlan()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		int result = iPlanMapper.writePlan(planDto);
		if(result > 0) {
			log.info("writePlan success!");
			iPlanMapper.setPOriNo(planDto.getP_no()); //mybatis에서 넣어준 pk값
			
		}else {
			log.info("writePlan fail!");
		}
		
		responseMap.put("resultWritePlan", result);
		
		return responseMap;
	}

	public List<PlanDto> getPlans(String m_id, int year, int month) {
		log.info("getPlans()");
			
		return iPlanMapper.getPlans(m_id,year,month);
	}

	public PlanDto getPlan(int p_no) {
		log.info("getPlan()");
		
		return iPlanMapper.getPlan(p_no);
	}

	public Object removePlan(int p_no) {
		log.info("removePlan()");
		
		return iPlanMapper.removePlan(p_no);
	}

	public Object modifyPlan(PlanDto planDto) {
		log.info("modifyPlan()");
		
		return iPlanMapper.modifyPlan(planDto);
	}

	public Object getSearchFriends(String keyword) {
		log.info("getSearchFriends()");
		
		return iMemberMapper.getSearchFriends(keyword);
	}

	public Object sharePlan(int p_no, int m_no, String m_id) {
		log.info("sharePlan()");
		
		PlanDto planDto = iPlanMapper.getPlan(p_no);
		
		boolean isSharedPlan = iPlanMapper.isSharedPlan(planDto.getP_ori_no(), m_id);
		
		int result = SHARE_PLAN_FAIL;
		
		if(!isSharedPlan) {
			result = iPlanMapper.sharePlan(planDto, m_id);
		}else {
			result = ALREADY_SHARED_PLAN;
		}
		
		return result;
	}
	
	
	
}
