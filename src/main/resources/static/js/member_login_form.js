function memberLoginForm() {
	console.log('memberLoginForm()');
	
	let form = document.member_login_form;
	if (form.a_m_id.value === '') {
		alert('아이디 입력하세요');
		form.a_m_id.focus();
		
	} else if (form.a_m_pw.value === '') {
		alert('비밀번호 입력하세요');
		form.a_m_pw.focus();
			
	} else {
		form.submit();
		
	}
	
}