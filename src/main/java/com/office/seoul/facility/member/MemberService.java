package com.office.seoul.facility.member;

import java.util.List;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberService {
	
	final static public int ID_EXISTS 					= -2;
	final static public int DB_COMMUNICATION_TROUBLE 	= -1;
	final static public int INSERT_FAIL_AT_DB 			= 0;
	final static public int INSERT_SUCCESS_AT_DB 		= 1;
	
	final private IMemberDao iMemberDao;
	final private PasswordEncoder passwordEncoder;
	final private JavaMailSenderImpl javaMailSenderImpl;
	
	
	public MemberService(IMemberDao iMemberDao, PasswordEncoder passwordEncoder, JavaMailSenderImpl javaMailSenderImpl) {
		this.iMemberDao = iMemberDao;
		this.passwordEncoder = passwordEncoder;
		this.javaMailSenderImpl = javaMailSenderImpl;
	
	}

	public int createAccountConfirm(MemberDto memberDto) {
		log.info("createAccountConfirm()");
		
		boolean isMember = iMemberDao.isMember(memberDto.getA_m_id());
		if (!isMember) {
			memberDto.setA_m_pw(passwordEncoder.encode(memberDto.getA_m_pw()));
			int result = iMemberDao.insertMember(memberDto);
			
			switch (result) {
			case DB_COMMUNICATION_TROUBLE:
				log.info("DATABASE COMMUNICATION TROUBLE");
				break;

			case INSERT_FAIL_AT_DB:
				log.info("INSERT FAIL AT DATABASE");
				break;
				
			case INSERT_SUCCESS_AT_DB:
				log.info("INSERT SUCCESS AT DATABASE");
				break;
			}
			
			return result;
			
		} else {
			return ID_EXISTS;
			
		}
	}

	public MemberDto MemberModifyForm(String loginedMemberID) {
		log.info("MemberModifyForm()");
			
		return iMemberDao.getLoginedMemberByAmId(loginedMemberID);
	}

	public int memberModifyConfirm(MemberDto memberDto) {
		log.info("memberModifyConfirm()");
		
		if (memberDto.getA_m_pw() != null && !memberDto.getA_m_pw().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(memberDto.getA_m_pw());
	        memberDto.setA_m_pw(encodedPassword);
		}
		
		int result = iMemberDao.updateMemberModify(memberDto);
		
		switch (result) {
		case INSERT_FAIL_AT_DB:
			log.info("회원 정보수정 실패");
			break;
			
		case INSERT_SUCCESS_AT_DB:
			log.info("회원 정보수정 성공");
			break;
		}
		
		return result;
	}

	public int memberDeleteConfirm(String loginedMemberID) {
		log.info("memberDeleteConfirm()");
			
		return iMemberDao.deleteMemberByMId(loginedMemberID);
	}

	public MemberDto findIdConfirm(MemberDto memberDto) {
		log.info("sendNewPasswordByMail()");
		
		MemberDto dto = iMemberDao.selectForFindId(memberDto.getA_m_name(), 
	            								memberDto.getA_m_mail());
		
		if (dto != null) {
	        return dto;
	        
	    } else {
	        return null;
	        
	    }
	}

	public int findPasswordConfirm(MemberDto memberDto) {
		log.info("findPasswordConfirm()");
	    
	    MemberDto dto = iMemberDao.selectForFindPassword(
										            memberDto.getA_m_id(),
										            memberDto.getA_m_name(),
										            memberDto.getA_m_mail());
		
		int result = 0;
		if (dto != null) {
			
			final String newPassword = createNewPassword();
			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode(newPassword);
            result = iMemberDao.updatePassword(memberDto.getA_m_id(), encodedPassword);
			if (result > 0) 
				sendNewPasswordByMail(memberDto.getA_m_mail(), newPassword);
			
		}

		return result;

	}

	private String createNewPassword() {
	    log.info("createNewPassword()");
	    
	    // UUID를 생성하고 문자열로 변환
	    String uuid = UUID.randomUUID().toString();
	    
	    // UUID에서 하이픈을 제거
	    String password = uuid.replace("-", "");

	    // 비밀번호 길이 조절
	    return password.substring(0, 8);
	}

	private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
		log.info("sendNewPasswordByMail()");
		
		final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo(toMailAddr);
				mimeMessageHelper.setSubject("[공공예약서비스] 새 비밀번호 안내입니다.");
				mimeMessageHelper.setText("새 비밀번호 : " + newPassword, true);
				
			}
			
		};
		
		javaMailSenderImpl.send(mimeMessagePreparator);
		
	}

	public List<MemberDto> listupAdmins() {
		log.info("listupAdmins()");
		
		List<MemberDto> memberDtos = iMemberDao.selectAdmins();
		
		
		return memberDtos;
	}

	public void setAdminApproval(int a_m_no) {
		log.info("setAdminApproval()");
		
		int result = iMemberDao.updateAdminAccount(a_m_no);
		if (result > 0) {
			log.info(a_m_no + " APPROVAL SUCCESS!!");
			
		} else {
			log.info(a_m_no + " APPROVAL FAIL!!");
			
		}
		
	}

	public void cancelAdminApproval(int a_m_no) {
		log.info("cancelAdminApproval()");
		int result = iMemberDao.cancelAdminApproval(a_m_no);
		if (result > 0) {
			log.info(a_m_no + " CANCEL APPROVAL SUCCESS!!");
			
		} else {
			log.info(a_m_no + " CANCEL APPROVAL FAIL!!");
			
		}
	}

	
}
