package com.office.seoul.facility.member;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.office.seoul.facility.FacilityDto;
import com.office.seoul.facility.FacilityService;
import com.office.seoul.facility.reservation.ReservationDto;
import com.office.seoul.facility.reservation.ReservationService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {

	final private MemberService memberService;
	final private ReservationService reservationService;
	final private FacilityService facilityService;

	public MemberController(MemberService memberService, ReservationService reservationService, FacilityService facilityService) {
		this.memberService = memberService;
		this.reservationService = reservationService;
		this.facilityService = facilityService;
	}

	/*
	 * 회원가입
	 */
	@GetMapping("/create_account_form")
	public String createAccountForm() {
		log.info("createAccountForm()");

		String nextPage = "member/create_account_form";

		return nextPage;
	}

	/*
	 * 회원가입 확인
	 */
	@PostMapping("/create_account_confirm")
	public String createAccountConfirm(MemberDto memberDto, Model model) {
		log.info("createAccountConfirm()");

		String nextPage = "member/create_account_result";

		int result = memberService.createAccountConfirm(memberDto);

		boolean isSuccess = result == MemberService.INSERT_SUCCESS_AT_DB;
		model.addAttribute("isSuccess", isSuccess);

		return nextPage;
	}

	/*
	 * 로그인
	 */
	@GetMapping("/member_login_form")
	public String memberLoginForm() {
		log.info("memberLoginForm()");

		String nextPage = "/member/member_login_form";

		return nextPage;

	}

	/*
	 * 로그인 실패 
	 */
	@GetMapping("/member_login_result")
	public String memberLoginConfirm() {
		log.info("memberLoginConfirm()");

		String nextPage = "/member/member_login_result";

		return nextPage;

	}

	/*
	 * 정보수정
	 */
	@GetMapping("/member_modify_form")
	public String MemberModifyForm(Principal principal, Model model) {
		log.info("MemberModifyForm()");

		String nextPage = "/member/member_modify_form";

		// 로그인한 회원 정보 조회
		MemberDto loginedMemberDto = memberService.MemberModifyForm(principal.getName());
		log.info("MemberModifyForm() with loginedMemberDto: {}", loginedMemberDto);

		model.addAttribute("loginedMemberDto", loginedMemberDto);

		return nextPage;
	}

	/*
	 * 정보 수정 확인
	 */
	@PostMapping("/member_modify_confirm")
	public String memberModifyConfirm(MemberDto memberDto, Model model) {
		log.info("memberModifyConfirm()");

		String nextPage = "/member/member_modify_result";

		int result = memberService.memberModifyConfirm(memberDto);

		boolean isModify = result == MemberService.INSERT_SUCCESS_AT_DB;
		model.addAttribute("isModify", isModify);

		return nextPage;

	}

	/*
	 * 회원 탈퇴
	 */
	@GetMapping("/member_delete_confirm")
	public String memberDeleteConfirm(Principal principal) {
		log.info("memberDeleteConfirm()");

		String nextPage = "redirect:/member/member_logout_confirm";

		int result = memberService.memberDeleteConfirm(principal.getName());
		if (result <= 0)
			nextPage = "/member/member_delete_result";

		return nextPage;

	}

	/*
	 * 아이디 찾기
	 */
	@GetMapping("/find_id_form")
	public String findIdForm() {
		log.info("findIdForm()");

		return "member/find_id_form";
	}

	/*
	 * 아이디 찾기 확인
	 */
	@PostMapping("/find_id_confirm")
	public String findIdConfirm(MemberDto memberDto, Model model) {
		log.info("findIdConfirm()");

		String nextPage = "member/find_id_result";

		MemberDto dto = memberService.findIdConfirm(memberDto);

		if (dto != null) {
			model.addAttribute("isUpdate", true);
			model.addAttribute("name", memberDto.getA_m_name());
			model.addAttribute("userId", dto.getA_m_id());

		} else {
			model.addAttribute("isUpdate", false);

		}

		return nextPage;
	}

	/*
	 * 비밀번호 찾기
	 */
	@GetMapping("/find_password_form")
	public String findPasswordForm() {
		log.info("findPasswordForm()");

		return "member/find_password_form";
	}

	/*
	 * 비밀번호 찾기 확인
	 */
	@PostMapping("/find_password_confirm")
	public String findPasswordConfirm(MemberDto memberDto, Model model) {
		log.info("findPasswordConfirm()");

		String nextPage = "member/find_password_result";

		int result = memberService.findPasswordConfirm(memberDto);

		boolean isUpdate = result == MemberService.INSERT_SUCCESS_AT_DB;
		model.addAttribute("isUpdate", isUpdate);

		return nextPage;

	}

	// 권한 거부
	@GetMapping("/access_denied")
	public String accessDenied() {
		log.info("accessDenied()");
		
		String nextPage = "member/access_denied";
		
		return nextPage;
	}	
	
	
	@GetMapping("/listup_admins")
	public String listupAdmins(Model model) {
		log.info("listupAdmins()");
		
		String nextPage= "member/listup_admins";
		
		
		List<MemberDto> memberDtos = memberService.listupAdmins();
		
		model.addAttribute("memberDtos", memberDtos);
		
		return nextPage;
		
	}
	
	
	/*
	 * 관리자 승인
	 */
	@GetMapping("/set_admin_approval")
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		log.info("setAdminApproval()");
		
		String nextPage= "redirect:/member/listup_admins";
		
		memberService.setAdminApproval(a_m_no);
		
		return nextPage;
		
	}
	
	// 전체 예약 리스트
	@GetMapping("/listup_reservations")
	public String listupReservations(
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        Model model) {
	    
	    log.info("listupReservations(), page: {}, size: {}", page, size);
	    
	    String nextPage = "member/listup_reservations";
	    
	    // 전체 예약 수 가져오기
	    int totalReservations = reservationService.countReservations();
	    List<ReservationDto> reservationDtos = reservationService.listupReservations(page, size);
	    List<FacilityDto> facilityDtos = facilityService.getAllFacilities();

	    model.addAttribute("reservations", reservationDtos);
	    model.addAttribute("facilities", facilityDtos);
	    model.addAttribute("totalReservations", totalReservations);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", (int) Math.ceil((double) totalReservations / size));
	    
	    return nextPage;
	}
	
	
	@GetMapping("/set_reservation_approval")
	public String setReservationApproval(@RequestParam("r_no") int r_no) {
		log.info("setReservationApproval()");
		
		String nextPage= "redirect:/member/listup_reservations";
		
		reservationService.setReservationApproval(r_no);
		
		return nextPage;
		
	}
	
	
}
