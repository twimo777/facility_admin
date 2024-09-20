package com.office.seoul.facility;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.seoul.facility.reservation.ReservationService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/facility")
public class FacilityController {
	
	@Value("${google-api-key}")
	private String apiKey;
	
	private final FacilityService facilityService;
	private final ReservationService reservationService;
	
	public FacilityController(FacilityService facilityService, ReservationService reservationService) {
		this.facilityService = facilityService;
		this.reservationService = reservationService;
	}
	
	@GetMapping({"", "/"})
	public String facility() {
		log.info("facility()");
		
		String nextPage = "redirect:/facility/home";
		
		return nextPage;
	}
	
	@GetMapping("/home")
	public String home(@RequestParam(value = "type", required = false) String type, Model model) {
		log.info("home()");
		
		String nextPage = "facility/home";
		
		List<FacilityDto> facilityDto = facilityService.home(type);
		
		model.addAttribute("facilityDto", facilityDto);
		model.addAttribute("selectedType", type);
		
		return nextPage;
	}
	
	@GetMapping("/page")
	@ResponseBody
    public Map<String, Object> getFacilities(	@RequestParam(value = "page", defaultValue = "1") int page, 
            									@RequestParam(value = "size", defaultValue = "6") int size,
            									@RequestParam(value = "type", required = false) String type) {
		
		log.info("Request Params - page: {}, size: {}, type: {}", page, size, type);
		
	    Map<String, Object> response = facilityService.getFacilities(page, size, type);
	    
	    return response;
	    
    }
	
	@GetMapping("/detailView/{id}")
	public String detailView(@PathVariable("id") String id, Model model, Principal principal) {
		log.info("detailView() with id: {}", id);
		
		String nextPage = "facility/detail_view";
		
		String loginedMemberDto = null;
	    if (principal != null) {
	        loginedMemberDto = principal.getName();
	    }
	    
	    FacilityDto facilityDto = facilityService.getFacilityById(id);
	    if (facilityDto != null) {
	    	
	    	LocalDate today = LocalDate.now();
	        LocalDate endDate = today.plusMonths(2);
//	        Map<String, Boolean> reservationStatus = reservationService.getReservationStatus(id, today, endDate);
	        Map<String, String> reservationStatus = reservationService.getReservationStatus(id, today, endDate);
	        log.info("reservationStatus : {}", reservationStatus);
	        if (reservationStatus == null) {
	            reservationStatus = new HashMap<>(); // null 방지
	        }
	    	
	        model.addAttribute("facilityDto", facilityDto);
	        model.addAttribute("loginedMemberDto", loginedMemberDto);
	        model.addAttribute("reservationStatus", reservationStatus);
	        model.addAttribute("startDate", today.toString());
	        model.addAttribute("endDate", endDate.toString());
	        model.addAttribute("apiKey", apiKey);
	        
	    } else {
	        model.addAttribute("error", "Facility not found");
	    }
	    
	    return nextPage; 
	}
	
	@GetMapping("/options")
    @ResponseBody
    public Map<String, Object> getOptions(@RequestParam(value = "area", required = false) String area,
                                           @RequestParam(value = "category", required = false) String category) {
        return facilityService.getOptions(area, category);
    }
	
	@GetMapping("/quickReserved")
	@ResponseBody
	public Map<String, String> quickReserved(@RequestParam(value = "svcnm") String svcnm) {
	    log.info("quickReserved() with svcnm: {}", svcnm);

	    // svcnm으로 시설 ID 조회
	    String facilityId = facilityService.getFacilityIdBySvcnm(svcnm);

	    log.info("svcnm: {}", svcnm);
	    log.info("facilityId: {}", facilityId);

	    Map<String, String> response = new HashMap<>();
	    if (facilityId != null) {
	        response.put("redirectUrl", "/facility/detailView/" + facilityId);
	    } else {
	        response.put("redirectUrl", "/");
	    }

	    return response;
	}

}