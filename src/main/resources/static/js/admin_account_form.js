function createAccountForm() {
	console.log('createAccountForm()');
	
	let form = document.create_account_form;
	if (form.a_m_id.value === '') {
		alert('새로운 관리자 아이디 입력하세요');
		form.a_m_id.focus();
		
	} else if (form.a_m_pw.value === '') {
		alert('새로운 관리자 비밀번호 입력하세요');
		form.a_m_pw.focus();
			
	} else if (form.a_m_name.value === '') {
		alert('새로운 관리자 이름을 입력하세요');
		form.a_m_name.focus();
				
	} else if (form.a_m_gender.value === '') {
		alert('관리자 성별을 선택하세요');
		form.a_m_gender.focus();
					
	} else if (form.a_m_mail.value === '') {
		alert('새로운 관리자 메일 주소 입력하세요');
		form.a_m_mail.focus();
				
	} else if (form.a_m_phone.value === '') {
		alert('새로운 관리자 전화번호 입력하세요');
		form.a_m_phone.focus();
					
	} else {
		form.submit();
		
	}
	
}



function adminLoginForm() {
	console.log('adminLoginForm()');
	
	let form = document.admin_login_form;
	if (form.a_m_id.value === '') {
		alert('관리자 아이디를 입력하세요');
		form.a_m_id.focus();
		
	} else if (form.a_m_pw.value === '') {
		alert('비밀번호 입력하세요');
		form.a_m_pw.focus();
			
	} else {
		form.submit();
		
	}
	
}

