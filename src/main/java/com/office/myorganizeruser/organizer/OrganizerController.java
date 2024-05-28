package com.office.myorganizeruser.organizer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/organizer")
public class OrganizerController {
	
	@GetMapping({"","/"})
	public String home(){
		log.info("Organizer home()");
		
		String nextPage = "organizer/home";
		
		return nextPage;
		
	}
	
}
