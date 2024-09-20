package com.office.seoul.facility.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	
	private int a_m_no;
	private int a_m_approval;
	private String a_m_id;
	private String a_m_pw;
	private String a_m_name;
	private String a_m_gender;
	private String a_m_mail;
	private String a_m_phone;
	private String a_m_reg_date;
	private String a_m_mod_date;
	private String a_m_role;
	
}
