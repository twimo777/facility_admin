function findPassword() {
	console.log('findPassword()');
	
	let form = document.find_password_form;
	
	if (form.a_m_id.value === '') {
		alert('아이디 입력하세요');
		form.a_m_id.focus();
		
	} else if (form.a_m_name.value === '') {
		alert('이름 입력하세요');
		form.a_m_name.focus();
		
	} else if (form.a_m_mail.value === '') {
		alert('메일 주소 입력하세요');
		form.a_m_mail.focus();
		
	} else {
		form.submit();
		
	}
	
}

function findId() {
	console.log('findId()');
	
	let form = document.find_id_form;
	
	if (form.u_m_name.value === '') {
		alert('이름 입력하세요');
		form.u_m_name.focus();
		
	} else if (form.u_m_mail.value === '') {
		alert('메일 주소 입력하세요');
		form.u_m_mail.focus();
		
	} else {
		form.submit();
		
	}
	
}