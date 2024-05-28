function memberSignupForm() {
   console.log('memberSignupForm()');
   
   let form = document.member_signup_form;
   if (form.m_id.value === '') {
      alert('INPUT NEW MEMBER ID!!');
      form.m_id.focus();
      
   } else if (form.m_pw.value === '') {
      alert('INPUT NEW MEMBER PW!!');
      form.m_pw.focus();
      
   } else if (form.m_mail.value === '') {
      alert('INPUT NEW MEMBER MAIL!!');
      form.m_mail.focus();
      
   } else if (form.m_phone.value === '') {
      alert('INPUT NEW MEMBER PHONE!!');
      form.m_phone.focus();
      
   } else {
      form.submit();
      
   }
   
}