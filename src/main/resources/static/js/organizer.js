// --------------- 현재 ---------------
let current_year; //지금 보고있는 년도
let current_month; //지금 보고있는 월
let current_date; //지금 보고있는 일
let current_day; //지금 보고있는 요일

// --------------- 오늘 ---------------
let today = new Date();
let today_year = today.getFullYear(); //오늘 년도
let today_month = today.getMonth(); //오늘 월 (0 ~ 11)
let today_date = today.getDate(); //오늘 일
let today_day = today.getDay(); //오늘 요일 (0 ~ 6)


$(document).ready(() => {
	console.log("[ORGANIZER] DOCUMENT READY!");
	
	// 현재 보고있는 날짜 설정
	setCurrentCalender(today_year,today_month,today_date,today_day);
	
	// 현재 보고있는 캘린더 UI (<select>)
	setCurrentYearAndMonthSelectUI();
	
	// 현재 보고있는 캘린더 UI (<tr>)
	addCalenderTr();
	
	// 현재 당의 일정 출력
	ajaxGetCurrentMonthPlans();
	
	// 이벤트 핸들러 초기화
	eventInit();
	
});

const setCurrentCalender = (year, month, date ,day) => {
	console.log("[ORGANIZER] setCurrentCalender() Called!!");
	
	current_year = year;
	current_month = month;
	current_date = date;
	current_day = day;
	
}

const setCurrentYearAndMonthSelectUI = () => {
	console.log("[ORGANIZER] setCurrentYearAndMonthSelectUI() Called!!");
	
	$('#article_wrap select[name="c_year"]').val(current_year).prop('selected', true);
	$('#article_wrap select[name="c_month"]').val(current_month + 1).prop('selected', true);
	
}

const addCalenderTr = () => {
	console.log("[ORGANIZER] addCalenderTr() Called!!");
	
	// 현재 보고있는 월의 첫날
	let thisCalenderStart = new Date(current_year, current_month, 1);
	let thisCalenderStart_year = thisCalenderStart.getFullYear(); // 보고 있는 달의 연도
	let thisCalenderStart_month = thisCalenderStart.getMonth(); // 보고 있는 달의 월
	let thisCalenderStart_date = thisCalenderStart.getDate(); // 보고 있는 달의 첫 날
	let thisCalenderStart_day = thisCalenderStart.getDay(); // 보고 있는 달의 첫 요일
	
	// 현재 보고있는 월의 마지막 날
	let thisCalenderEnd = new Date(current_year, current_month + 1, 0);
	let thisCalenderEnd_year = thisCalenderEnd.getFullYear(); //이번달의 년도
	let thisCalenderEnd_month = thisCalenderEnd.getMonth(); //이번달의 월
	let thisCalenderEnd_date = thisCalenderEnd.getDate(); //이번달의 마지막 일
	let thisCalenderEnd_day = thisCalenderEnd.getDay(); //이번달의 마지막 요일
	
	//캘린더 날짜 데이터 설정
	let dates = Array();
	let dateCnt = 1;
	
	for(let i = 0; i < 42; i++){
		
		if(i < thisCalenderStart_day || dateCnt > thisCalenderEnd_date){
			dates[i] = 0;
		}else {			
			dates[i] = dateCnt;
			dateCnt++;
		}
		
	}
	
	let appendTag = ``;
	let dateIndex = 0;
	
	for(let i = 0; i < 6; i++){
		
		if(i >= 4 && dates[dateIndex] == 0){
			break;
		}
		
		appendTag = `<tr>`;
		for(let j = 0; j < 7; j++){
			
			if(dates[dateIndex] !== 0){
				
				let $calender_td_template = $("#calender_td_template").clone();
				let $calender_td = $($calender_td_template.html());
				
				$calender_td.find('div.date').text(dates[dateIndex]);
				$calender_td.find('div.plan_wrap').attr('id',`date_${dates[dateIndex]}`);
				appendTag += $calender_td.prop('outerHTML');
			} else {
				appendTag += "<td></td>"
			}
			
			dateIndex ++;
			
		}//두번째 for문 끝	
		appendTag += `</tr>`;
		$('#table_calender tbody').append(appendTag);
	}//첫번째 for문 끝
	
}

// 기존 tr 제거
const removeCalenderTr = () => {
	console.log("[ORGANIZER] removeCalenderTr() Called!!");
	
	$('#table_calender tbody tr').remove();
	
}

// 이벤트 핸들러 초기화
const eventInit = () => {
	console.log("[ORGANIZER] eventInit() Called!!");
	
	// 캘린더에서 이전달 버튼(btn_pre) 클릭시
	$(document).on('click','#article_wrap a.btn_pre',() => {
		console.log("[ORGANIZER] btn_pre Clicked!!");
		
		setPreMonth();
	});
	
	// 캘린더에서 다음달 버튼(btn_next) 클릭시
	$(document).on('click','#article_wrap a.btn_next',() => {
		console.log("[ORGANIZER] btn_next Clicked!!");
		
		setNextMonth();
	});
	
	// 캘린더에서 년도(c_year) 변경 시
	$(document).on('change','#article_wrap select[name="c_year"]',() => {
		console.log("[ORGANIZER] c_year Changed!!");
		setMonthBySelectChanged();
	});
	
	// 캘린더에서 월(c_month) 변경 시
	$(document).on('change','#article_wrap select[name="c_month"]',() => {
		console.log("[ORGANIZER] c_month Changed!!");
		setMonthBySelectChanged();
	});
	
	// 캘린더에서 일정 등록 버튼 클릭 시
	$(document).on('click','#article_wrap a.write_plan',(e) => {
		console.log("[ORGANIZER] write_plan Clicked!!");
		
		let year = current_year;
		let month = current_month + 1;
		let date = $(e.currentTarget).parent('div').siblings('div.date').text();
		
		$('#write_plan').data({
			'ori_year': year,
			'ori_month': month,
			'ori_date': date
		});
		
		showWritePlanView(year,month,date);
		
	});
	
	// 일정등록 모달에서 write버튼 클릭 시
	$(document).on('click','#write_plan input[value="write"]',() => {
		console.log("[ORGANIZER] write Clicked!!");
		
		let year = $("#write_plan select[name='wp_year']").val();
		let month = $("#write_plan select[name='wp_month']").val();
		let date = $("#write_plan select[name='wp_date']").val();
		let title = $("#write_plan input[name='wp_title']").val();
		let body = $("#write_plan input[name='wp_body']").val();		
		let file = $("#write_plan input[name='wp_file']").val();
		
		if(title === ''){
			alert("input plan title");
			$("#write_plan input[name='wp_title']").focus();
		}else if(body === ''){
			alert("input plan body");
			$("#write_plan input[name='wp_body']").focus();
		}else if(file === ''){
			alert("select plan img file");
			$("#write_plan input[name='wp_file']").focus();
		}else {
			let inputFile = $("#write_plan input[name='wp_file']"); // 배열
			let files = inputFile[0].files; // 이것도 배열
			//files[0]
			ajaxWritePlan(year,month,date,title,body,files[0]);
		}
		
	});
	
	// 일정 등록 모달에서 리셋 버튼 클릭 시
	$(document).on('click','#write_plan input[value="reset"]',() => {
		console.log("[ORGANIZER] reset Clicked!!");
		
		let ori_year = $('#write_plan').data('ori_year');
		let ori_month = $('#write_plan').data('ori_month');
		let ori_date = $('#write_plan').data('ori_date');
		
		showWritePlanView(ori_year,ori_month,ori_date);
		
	});
	
	// 일정등록 모달에서 취소버튼 클릭 시
	$(document).on('click','#write_plan input[value="cancel"]',() => {
		console.log("[ORGANIZER] cancel Clicked!!");
		
		hideWritePlanView();
		
	});
	
	// 디테일 뷰 - 캘린더에서 일정을 클릭 시
	$(document).on('click','#table_calender ul.plan li a',(e) => {
		console.log("[ORGANIZER] plan title Clicked!!");
		
		ajaxGetPlan($(e.target).data('p_no'));
		
	});
	
	// 디테일 뷰 - 디테일 모달 삭제 버튼 클릭 시
	$(document).on('click','#detail_plan input[value="delete"]',() => {
		console.log("[ORGANIZER] delete btn Clicked!!");
		
		ajaxRemovePlan($('#detail_plan').data('plan').p_no);
		
	});
	
	// 디테일 뷰 - 디테일 모달 수정 버튼 클릭 시
	$(document).on('click','#detail_plan input[value="modify"]',() => {
		console.log("[ORGANIZER] modify btn Clicked!!");
		
		let year = $('#detail_plan select[name="dp_year"]').val();
		let month = $('#detail_plan select[name="dp_month"]').val();
		let date = $('#detail_plan select[name="dp_date"]').val();
		let title = $('#detail_plan input[name="dp_title"]').val();
		let body = $('#detail_plan input[name="dp_body"]').val();
		
		if(title === ''){
			alert("input plan title");
			$('#detail_plan input[name="dp_title"]').focus();
		}else if(body === ''){
			alert("input plan body");
			$('#detail_plan input[name="dp_body"]').focus();
		}else{
			let inputFile = $('#detail_plan input[name="dp_file"]');
			let files = inputFile[0].files;
			ajaxModifyPlan($('#detail_plan').data('plan').p_no, year, month, date ,title, body,files[0]);
		}
		
	});
	
	// 디테일 뷰 - 디테일 모달 닫기 버튼 클릭 시
	$(document).on('click','#detail_plan input[value="close"]',() => {
		console.log("[ORGANIZER] close btn Clicked!!");
		
		hideDetailPlanView();
		
	});
	
	// 공유 대상(ID) 검색
	$(document).on('keyup','#detail_plan input[name="search_friend"]',(e) => {
		console.log("[ORGANIZER] input search_friend keyup!!");
		
		let keyword = $(e.target).val();
		
		if(keyword === ''){
			$('#detail_plan div.list_friend').hide();
			
		}else {
			$('#detail_plan div.list_friend').show();
			ajaxSearchFriends(keyword);
		}
		
	});
	
	// 일정 공유
	$(document).on('click','#detail_plan div.list_friend ul li a',(e) => {
		console.log("[ORGANIZER] friend ID Clicked!!");
		
		let result = confirm(`${e.target.text}님께 일정을 공유하시겠습니까?`);
		if(result){
			ajaxSharePlan($('#detail_plan').data('plan').p_no, $(e.target).data('m_no'), $(e.target).text());
			$('#detail_plan div.list_friend').hide();
			$('#detail_plan input[name="search_friend"]').val('');
		}
		
	});
	
	// 디테일 뷰 - 댓글 등록
    $(document).on('click', '#detail_plan div.input_comment a', () => {
      console.log("[PLAN] DETAIL_PLAN INPUT_COMMENT A CLICK HANDLER");
      
      let comment = $('#detail_plan input[name="comment"]').val();
      let p_ori_no = $('#detail_plan').data('plan').p_ori_no;
      
      if (comment.trim() !== null) {
         ajaxRegistComment(comment.trim(), p_ori_no);
      }
      
   })
	
}//Event END

const setPreMonth = () => {
	console.log("[ORGANIZER] setPreMonth() Called!!");
	
	if($('select[name="c_year"]').val() === "2024" && $('select[name="c_month"]').val() === "1"){
		alert("2024년 1월 이전은 설정할 수 없습니다.");
		return false;
	}
	
	let temp_year = current_year;
	let temp_month = current_month - 1;
	if(temp_month <= -1){
		temp_year = temp_year - 1;
		temp_month = 11;
	}
	
	//데이터(년, 월)
	let preCalender = new Date(temp_year, temp_month, 1);
	setCurrentCalender(preCalender.getFullYear(), preCalender.getMonth(), preCalender.getDate(), preCalender.getDay());
	
	setCurrentYearAndMonthSelectUI(); //현재 보고있는 캘린더 UI(<select>)
	removeCalenderTr(); // 기존 <tr> UI 제거
	addCalenderTr(); // 새로운 <tr> UI 생성
	
}

const setNextMonth = () => {
	console.log("[ORGANIZER] setNextMonth() Called!!");
	
	if($('select[name="c_year"]').val() === "2030" && $('select[name="c_month"]').val() === "12"){
		alert("2030년 12월 이후은 설정할 수 없습니다.");
		return false;
	}
	
	let temp_year = current_year;
	let temp_month = current_month + 1;
	if(temp_month >= 12){
		temp_year = temp_year + 1;
		temp_month = 0;
	}
	
	//데이터(년, 월)
	let preCalender = new Date(temp_year, temp_month, 1);
	setCurrentCalender(preCalender.getFullYear(), preCalender.getMonth(), preCalender.getDate(), preCalender.getDay());
	
	setCurrentYearAndMonthSelectUI(); //현재 보고있는 캘린더 UI(<select>)
	removeCalenderTr(); // 기존 <tr> UI 제거
	addCalenderTr(); // 새로운 <tr> UI 생성
	
}

//년 또는 월 변경시
const setMonthBySelectChanged = () => {
	console.log("[ORGANIZER] setMonthBySelectChanged() Changed!!");
	
	let temp_year = $('#article_wrap select[name="c_year"]').val();
	let temp_month = $('#article_wrap select[name="c_month"]').val() - 1;
	
	//데이터(년, 월)
	let selectedCalender = new Date(temp_year, temp_month, 1);
	setCurrentCalender(selectedCalender.getFullYear(), selectedCalender.getMonth(), selectedCalender.getDate(), selectedCalender.getDay());
	
	removeCalenderTr(); // 기존 <tr> UI 제거
	addCalenderTr(); // 새로운 <tr> UI 생성
	
}

const showWritePlanView = (year,month,date) => {
	console.log("[ORGANIZER] showWritePlanView() Called!!");
	
	$('#write_plan select[name="wp_year"]').val(year).prop('selected', true);
	$('#write_plan select[name="wp_month"]').val(month).prop('selected', true);
	
	setSelectDateOptions(year,month,'wp_date');
	$('#write_plan select[name="wp_date"]').val(date).prop('selected', true);
	
	$('#write_plan').show();
	
}

const hideWritePlanView = () => {
	console.log("[ORGANIZER] hideWritePlanView() Called!!");
	
	$('#write_plan input[name="wp_title"]').val('');
	$('#write_plan input[name="wp_body"]').val('');
	$('#write_plan input[name="wp_file"]').val('');
	
	$('#write_plan').hide();
	
}

const setSelectDateOptions = (year,month,select_name) => {
	console.log("[ORGANIZER] setSelectDateOptions() Called!!");
	
	// 데이터
	let last = new Date(year,month,0);
	
	//UI
	$(`select[name="${select_name}"]`).children().remove();
	for(let i = 1; i <= last.getDate(); i++){
		$(`select[name="${select_name}"]`).append(`<option value="${i}">${i}</option>`);
	}
	
}

const showDetailPlanView = (plan) => {
	console.log("[ORGANIZER] showDetailPlanView() Called!!");
	
	if (plan.p_ori_owner_id !== plan.m_id) {      
      $('#detail_plan select[name="dp_year"]').attr('disabled', true);
      $('#detail_plan select[name="dp_month"]').attr('disabled', true);
      $('#detail_plan select[name="dp_date"]').attr('disabled', true);
      $('#detail_plan input[name="dp_title"]').attr('disabled', true);
      $('#detail_plan input[name="dp_body"]').attr('disabled', true);
      $('#detail_plan input[name="dp_file"]').css('display', 'none');
      $('#detail_plan input[value="modify"]').css('display', 'none');
      $('#detail_plan input[value="delete"]').css('display', 'none');
      
   } else {
      $('#detail_plan select[name="dp_year"]').attr('disabled', false);
      $('#detail_plan select[name="dp_month"]').attr('disabled', false);
      $('#detail_plan select[name="dp_date"]').attr('disabled', false);
      $('#detail_plan input[name="dp_title"]').attr('disabled', false);
      $('#detail_plan input[name="dp_body"]').attr('disabled', false);
      $('#detail_plan input[name="dp_file"]').css('display', 'inline-block');
      $('#detail_plan input[value="modify"]').css('display', 'inline-block');
      $('#detail_plan input[value="delete"]').css('display', 'inline-block');
      
   }
   
   // 년 /월 <select>
   $('#detail_plan select[name="dp_year"]').val(plan.p_year).prop('selected',true);
   $('#detail_plan select[name="dp_month"]').val(plan.p_month).prop('selected',true);
	
   // 일 <select>
   setSelectDateOptions(plan.p_year,plan.p_month, 'dp_date');
   $('#detail_plan select[name="dp_date"]').val(plan.p_date).prop('selected',true);
   
   // 일정<input>
   $('#detail_plan input[name="dp_no"]').val(plan.p_no);
   $('#detail_plan input[name="dp_title"]').val(plan.p_title);
   $('#detail_plan input[name="dp_body"]').val(plan.p_body);
   
   // 이미지 src
   let uploadImgURI = `/planUploadImg/${plan.p_ori_owner_id}/${plan.p_img_name}`;
   $('#detail_plan img.plan_img').attr('src',uploadImgURI);
   
   // 코멘트 <div>
   ajaxGetComments(plan.p_ori_no);
   
   
   // data plan set
   $('#detail_plan').data('plan',plan);
   
   // 일정 상세 모달 show
   $('#detail_plan').show();
   
}

const hideDetailPlanView = () => {
	console.log("[ORGANIZER] hideDetailPlanView() Called!!");
	
	$('#detail_plan input[name="dp_title"]').val('');
	$('#detail_plan input[name="dp_body"]').val('');
	$('#detail_plan input[name="dp_file"]').val('');
	
	$('#detail_plan').hide();
	
}

const listUpComments = (comments) => {
	console.log("[ORGANIZER] listUpComments() Called!!");
	
	$('#detail_plan div.list_comment ul li').remove();
	
	for(let i = 0; i < comments.length; i++){
		
		let appendTag = '';
		appendTag += '<li>';
		appendTag += comments[i].c_txt;
		appendTag += '</li>';
		
		$('#detail_plan div.list_comment ul').append(appendTag);
	}
	
}


//ajax function
const ajaxWritePlan = (year,month,date,title,body,file) => {
	console.log("[ORGANIZER] ajaxWritePlan() Called!!");
	
	let formData = new FormData();
	formData.append("p_year",year);
	formData.append("p_month",month);
	formData.append("p_date",date);
	formData.append("p_title",title);
	formData.append("p_body",body);
	formData.append("file",file);
	
	$.ajax({
		url: '/plan/writePlan',
		method: 'POST',
		processData: false,
		contentType: false,
		data: formData,
		success: (data) => {
			console.log("[ORGANIZER] ajaxWritePlan() success!!");
						
			if(data === null || data.resultWritePlan <= 0){
				alert("일정 등록에 문제가 발생했습니다.");
			}else {
				alert("일정 등록에 성공했습니다..");
				removeCalenderTr();
				addCalenderTr();
				ajaxGetCurrentMonthPlans();
			}
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxWritePlan() error!!");
			alert("일정 등록에 문제가 error 발생했습니다.");
		},
		complete: () => {
			console.log("[ORGANIZER] ajaxWritePlan() complete!!");
			
			hideWritePlanView();
		}
	});
	
}

const ajaxGetCurrentMonthPlans = () => {
	console.log("[ORGANIZER] ajaxGetCurrentMonthPlans() Called!!");
	
	$.ajax({
		url: `/plan/getPlans?year=${current_year}&month=${current_month + 1}`,
		type: 'GET',
		data: {},
		success: (data) => {
			console.log("[ORGANIZER] ajaxGetCurrentMonthPlans() success!!");
			console.log("data ----->",data);
			
			for(let i = 0; i < data.length; i++){
				let appendTag = "<li>";
					appendTag += `<a href="#none" data-p_no='${data[i].p_no}'>${data[i].p_title}</a>`;
					appendTag += "</li>";
					
				$(`#date_${data[i].p_date} ul.plan`).append(appendTag);
			}
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxGetCurrentMonthPlans() error!!");
		}
	});
	
}

const ajaxGetPlan = (no) => {
	console.log("[ORGANIZER] ajaxGetPlan() Called!!");
	
	$.ajax({
		url: `/plan/getPlan?p_no=${no}`,
		type: 'GET',
		data: {},
		success: (data) => {
			console.log("[ORGANIZER] ajaxGetPlan() success!!");
			
			showDetailPlanView(data);
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxGetPlan() error!!");
		}
	});
	
}

const ajaxRemovePlan = (no) => {
	console.log("[ORGANIZER] ajaxRemovePlan() Called!!");
	
	let formData = new FormData();
	formData.append("p_no",no);
	
	$.ajax({
		url: '/plan/removePlan',
		method: 'DELETE',
		processData: false,
		contentType: false,
		data: formData,
		success: (data) => {
			console.log("[ORGANIZER] ajaxRemovePlan() success!!");
			
			if(data > 0){
				alert("일정 삭제 성공!");
				removeCalenderTr();
				addCalenderTr();
				ajaxGetCurrentMonthPlans();
			}else {
				alert("일정 삭제에 문제가 발생했습니다!");
			}
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxRemovePlan() error!!");
			alert("일정 삭제에 문제가(error) 발생했습니다.");
		},
		complete: () => {
			console.log("[ORGANIZER] ajaxRemovePlan() complete!!");
			
			hideDetailPlanView();
		}
	});
	
}

const ajaxModifyPlan = (no,year,month,date,title,body,file) => {
	console.log("[ORGANIZER] ajaxModifyPlan() Called!!");
	
	let formData = new FormData();
	formData.append("p_no",no);
	formData.append("p_year",year);
	formData.append("p_month",month);
	formData.append("p_date",date);
	formData.append("p_title",title);
	formData.append("p_body",body);
	
	
	if(file !== undefined){
		formData.append("file",file);
	}
	
	$.ajax({
		url: '/plan/modifyPlan',
		method: 'PUT',
		processData: false,
		contentType: false,
		data: formData,
		success: (data) => {
			console.log("[ORGANIZER] ajaxModifyPlan() success!!");
			
			if(data === null || data <= 0){
				alert("일정 수정에 문제가 발생했습니다.");
			}else{
				alert("일정 수정 성공!");
				removeCalenderTr();
				addCalenderTr();
				ajaxGetCurrentMonthPlans();
			}			
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxModifyPlan() error!!");
			alert("일정 수정에 문제가(error) 발생했습니다.");
		},
		complete: () => {
			console.log("[ORGANIZER] ajaxModifyPlan() complete!!");
			
			hideDetailPlanView();
			
		}
	});
	
}

const ajaxSearchFriends = (keyword) => {
	console.log("[ORGANIZER] ajaxSearchFriends() Called!!");
	
	$.ajax({
		url: `/plan/getSearchFriends?keyword=${keyword}`,
		type: 'GET',
		data: {},
		success: (data) => {
			console.log("[ORGANIZER] ajaxSearchFriends() success!!");
			
			$('#detail_plan li.share_reply div.list_friend ul').children().remove();
			
			for(let i = 0; i < data.length; i++){
				let appendTag = '<li>';
				appendTag += `<a href="#none" data-m_no=${data[i].m_no}>${data[i].m_id}</a>`;
				appendTag += '</li>';
				
				$('#detail_plan li.share_reply div.list_friend ul').append(appendTag);
			}
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxSearchFriends() error!!");
		}
	});
	
}

const ajaxSharePlan = (p_no, m_no, m_id) => {
	console.log("[ORGANIZER] ajaxSharePlan() Called!!");
	
	let formData = new FormData();
	formData.append("p_no",p_no);
	formData.append("m_no",m_no);
	formData.append("m_id",m_id);
	
	$.ajax({
		url: '/plan/sharePlan',
		method: 'POST',
		processData: false,
		contentType: false,
		data: formData,
		success: (data) => {
			console.log("[ORGANIZER] ajaxSharePlan() success!!");
			
			switch(data) {
				case -1:
					alert('이미 공유된 일정 입니다.');
					break;
				case 0:
					alert('일정 공유에 실패 했습니다.');
					break;
				case 1:
					alert('일정 공유에 성공 했습니다.');
					break;
			}
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxSharePlan() error!!");
			alert("일정 공유에 문제가 error 발생했습니다.");
		}
		
	});
	
}

const ajaxRegistComment = (comment, p_ori_no) => {
   console.log('[PLAN] ajaxRegistComment()');
   
   let formData = new FormData();
   formData.append("comment", comment);
   formData.append("p_ori_no", p_ori_no);
   
   $.ajax({
      url: '/comment/registComment',
      method: 'POST',
      processData: false,
      contentType: false,
      data: formData,
      success: (data) => {
         console.log('[PLAN] AJAX COMMUNICATION SUCCESS!! - ajaxRegistComment');
         
         if (data > 0) {
            alert('댓글이 정상 등록되었습니다.');
            
         } else {
            alert('댓글 등록에 실패했습니다. ');
            
         }
         
      },
      error: (error) => {
         console.log('[PLAN] AJAX COMMUNICATION ERROR!! - ajaxRegistComment');
         
      },
      complete: (data) => {
         console.log('[PLAN] AJAX COMMUNICATION COMPLETE!! - ajaxRegistComment');
         ajaxGetComments(p_ori_no);
      }
   });
   
}

const ajaxGetComments = (p_ori_no) => {
	console.log('[PLAN] ajaxGetComments()');
	
	$.ajax({
		url: `/comment/getComments?p_ori_no=${p_ori_no}`,
		type: 'GET',
		data: {},
		success: (data) => {
			console.log("[ORGANIZER] ajaxGetComments() success!!");
			listUpComments(data);
			
		},
		error: (error) => {
			console.log("[ORGANIZER] ajaxGetComments() error!!");
		}
	});
	
}



















