package com.office.seoul.facility.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberDetailService implements UserDetailsService {
	
	final private IMemberDao iMemberDao;
	
	public MemberDetailService(IMemberDao iMemberDao) {
		this.iMemberDao = iMemberDao;
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MemberDto selectedMemberDto = 
				iMemberDao.selectMemberByMId(username);
		
		
		 if (selectedMemberDto.getA_m_approval() == 0) {
	            throw new UsernameNotFoundException("관리자 승인 대기중입니다.");
		 }
		
		if (selectedMemberDto != null) {
			log.info("getA_m_role : {}", selectedMemberDto.getA_m_role());
			return User.builder()
					.username(selectedMemberDto.getA_m_id())
					.password(selectedMemberDto.getA_m_pw())
					.roles(selectedMemberDto.getA_m_role())
					.build();
			
		}
		 	 
		return null;
		 
	}
	
	
}


