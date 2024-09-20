package com.office.seoul.facility.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.office.seoul.facility.reservation.ReservationService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/statistics")
public class StatisticsController {
	
	final private ReservationService reservationService;
	
	public StatisticsController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	@GetMapping("/statistics_form")
	public String statisticsForm() {
		log.info("statisticsForm()");
		
		String nextPage = "statistics/statistics_form";
		
		return nextPage;
	}
	
	// 월별
	@GetMapping("/monthly_statistics")
	@ResponseBody
	public List<Map<String, Object>> getMonthlyStatistics() {
	    log.info("getMonthlyStatistics()");
	    return reservationService.getMonthlyStatistics();
	}
	
	// 년별
	@GetMapping("/yearly_statistics")
	@ResponseBody
	public List<Map<String, Object>> getYearlyStatistics() {
	    log.info("getYearlyStatistics()");
	    return reservationService.getYearlyStatistics();
	}

	// 시설별
	@GetMapping("/facility_statistics")
	@ResponseBody
	public List<Map<String, Object>> getFacilityStatistics() {
	    log.info("getFacilityStatistics()");
	    return reservationService.getFacilityStatistics();
	}

}
