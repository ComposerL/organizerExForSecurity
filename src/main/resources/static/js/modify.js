function memberModifyForm() {
   console.log('memberModifyForm()');
   
   let form = document.member_modify_form;
   if (form.m_pw.value === '') {
      alert('INPUT MEMBER PW!!');
      form.m_pw.focus();
      
   } else if (form.m_mail.value === '') {
      alert('INPUT MEMBER MAIL!!');
      form.m_mail.focus();
      
   } else if (form.m_phone.value === '') {
      alert('INPUT MEMBER PHONE!!');
      form.m_phone.focus();
      
   } else {
      form.submit();
      
   }
   
}

function memberDeleteConfirm() {
   console.log('memberDeleteConfirm()');
   
   result = confirm('Are you sure you want to withdraw?');
   
   if (result) {
      location.href='/member/memberDeleteConfirm';
   }
   
}