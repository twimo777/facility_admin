package com.office.seoul.facility.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
	
	private int r_no;
	private String SVCID;
	private String u_m_id;			// 예약자 이름
	private String r_reserve_date;	// 예약 날짜
	private String r_reg_date;
	private String r_mod_date;
	private String r_use_state;		// 예약 상태
	private String r_use_time;		// 예약 시간

}
