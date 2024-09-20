function MemberModifyForm() {
	console.log('MemberModifyForm()');
	
	let form = document.member_modify_form;
	if (form.a_m_pw.value === '') {
		alert('비밀번호 입력하세요');
		form.a_m_pw.focus();
		
	} else if (form.a_m_name.value === '') {
		alert('이름 입력하세요');
		form.a_m_name.focus();
				
	} else if (form.a_m_mail.value === '') {
		alert('메일 주소 입력하세요');
		form.a_m_mail.focus();
				
	} else if (form.a_m_phone.value === '') {
		alert('전화번호 입력하세요');
		form.a_m_phone.focus();
					
	} else {
		form.submit();
		
	}
	
	
}