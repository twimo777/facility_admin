package com.office.seoul.facility.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberDao {

	public boolean isMember(String a_m_id);

	public int insertMember(MemberDto memberDto);

	public MemberDto selectMemberByMId(String username);

	public MemberDto getLoginedMemberByAmId(String loginedMemberID);

	public int updateMemberModify(MemberDto memberDto);

	public int deleteMemberByMId(String loginedMemberID);

	public MemberDto selectForFindPassword(@Param("a_m_id") String a_m_id,
								            @Param("a_m_name") String a_m_name,
								            @Param("a_m_mail") String a_m_mail);

	public int updatePassword(@Param("a_m_id") String a_m_id, @Param("encodedPassword") String encodedPassword);

	public MemberDto selectForFindId(@Param("a_m_name") String a_m_name, @Param("a_m_mail") String a_m_mail);

	public List<MemberDto> selectAdmins();

	public int updateAdminAccount(int a_m_no);

	public int cancelAdminApproval(int a_m_no);

}
