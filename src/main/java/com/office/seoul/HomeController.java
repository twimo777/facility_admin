package com.office.seoul;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.seoul.facility.FacilityService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class HomeController {
	
	private final FacilityService facilityService;
	
	public HomeController(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
    
    @GetMapping({"/", ""})
    public String home(Model model) {
        log.info("home()");
        
        String nextPage = "home";
        
        return nextPage;
    }
    
//    @GetMapping("/options")
//    @ResponseBody
//    public Map<String, Object> getOptions(	@RequestParam(value = "area", required = false) String area,
//    										@RequestParam(value = "category", required = false) String category) {
//		log.info("getOptions()");
//		
//		Map<String, Object> options = new HashMap<>();
//		options.put("areas", facilityService.getAreas());
//		options.put("categories", facilityService.getCategoriesByArea(area));
//		options.put("results", facilityService.getResults(area, category));
//		
//		log.info("area : {}", area);
//		log.info("category : {}", category);
//		
//		return options;
//	}
    
    
}
