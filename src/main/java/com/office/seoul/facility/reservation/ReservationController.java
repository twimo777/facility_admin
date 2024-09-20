package com.office.seoul.facility.reservation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/reservation")
public class ReservationController {
	
	private final ReservationService reservationService;
	
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	@GetMapping({"", "/"})
	public String reservation() {
		log.info("reservation()");
		
		String nextPage = "redirect:/reservation/home";
		
		return nextPage;
	}
	
	@GetMapping("/home")
    public String home() {
        log.info("home()");
        
        String nextPage = "reservation/home";
        
        return nextPage;
    }
	
	@PostMapping("/select_time")
    public String selectTime(@RequestParam("facilityId") String facilityId,
                         @RequestParam("userId") String userId,
                         @RequestParam("selectedDate") String selectedDate,
                         Model model) {
        log.info("selectTime() - facilityId: {}, userId: {}, selectedDate: {}", facilityId, userId, selectedDate);
        
        String nextPage = "reservation/select_time";
        
        model.addAttribute("facilityId", facilityId);
        model.addAttribute("userId", userId);
        model.addAttribute("selectedDate", selectedDate);
        
        // 예약된 시간 목록과 전체 시간 목록을 가져와서 모델에 추가
        List<String> reservedTimes = reservationService.getReservedTimes(facilityId, selectedDate);
        List<String> allTimes = reservationService.getAllTimes();
        
        log.info("selectTime() - reservedTimes: {}, allTimes: {}", reservedTimes, allTimes);
        
        model.addAttribute("reservedTimes", reservedTimes);
        model.addAttribute("allTimes", allTimes);
        
        return nextPage;
    }
	
	@PostMapping("/confirm")
	public String confirm(@RequestParam("facilityId") String facilityId,
	                      @RequestParam("userId") String userId,
	                      @RequestParam("selectedDate") String selectedDate,
	                      @RequestParam("selectedTime") String selectedTime,
	                      Model model) {
	    log.info("confirm() - facilityId: {}, userId: {}, selectedDate: {}, selectedTime: {}", facilityId, userId, selectedDate, selectedTime);
	    
	    String nextPage = "reservation/confirm";

	    // 예약 정보 저장
	    boolean isSaved = reservationService.saveReservation(facilityId, userId, selectedDate, selectedTime);

	    // 모델에 추가할 데이터 설정
	    model.addAttribute("facilityId", facilityId);
	    model.addAttribute("userId", userId);
	    model.addAttribute("selectedDate", selectedDate);
	    model.addAttribute("selectedTime", selectedTime);
	    model.addAttribute("isSaved", isSaved); 

	    return nextPage; 
	}

}
