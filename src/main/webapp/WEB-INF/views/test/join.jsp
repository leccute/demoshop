<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
    <meta name="generator" content="Hugo 0.101.0">
    <title>Pricing example · Bootstrap v4.6</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.6/examples/pricing/">

    
<%@include file="/WEB-INF/views/include/common.jsp" %>
    



    <!-- Favicons 
<link rel="apple-touch-icon" href="/docs/4.6/assets/img/favicons/apple-touch-icon.png" sizes="180x180">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon-32x32.png" sizes="32x32" type="image/png">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon-16x16.png" sizes="16x16" type="image/png">
<link rel="manifest" href="/docs/4.6/assets/img/favicons/manifest.json">
<link rel="mask-icon" href="/docs/4.6/assets/img/favicons/safari-pinned-tab.svg" color="#563d7c">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon.ico">
<meta name="msapplication-config" content="/docs/4.6/assets/img/favicons/browserconfig.xml">
-->
<meta name="theme-color" content="#563d7c">


    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>

    
    
  </head>
  <body>
    
<%@include file="/WEB-INF/views/include/header.jsp" %>



<div class="container">
  <h3>기본정보</h3>
  <div class="mb-3 text-center">
	  <form action="#" method="post">
		  <div class="form-group row">
		    <label for="id" class="col-sm-2 col-form-label">아이디</label>
		    <div class="col-sm-5">
		      <input type="text" class="form-control" id="id" name="id"> (영문 소문자, 숫자4~16자)
        </div>
		  </div>
		  <div class="form-group row">
		    <label for="password" class="col-sm-2 col-form-label">비밀번호</label>
		    <div class="col-sm-7">
		      <input type="password" class="form-control" id="password" name="password">영문/최소 1개의 숫자 또는 특수문자('~!@^()-_+[]{}\:,.<>/)조합으로 8~16자리.
		    </div>
		  </div>
		  <div class="form-group row">
		    <label for="password_2" class="col-sm-2 col-form-label">비밀번호확인</label>
		    <div class="col-sm-3">
		      <input type="password" class="form-control" id="password_2">
		    </div>
		  </div>
		  <div class="form-group row">
		    <label for="name" class="col-sm-2 col-form-label">이름</label>
		    <div class="col-sm-3">
		      <input type="text" class="form-control" id="name" name="name">
		    </div>
		  </div>
		  <div class="form-group row">
		    <label for="email" class="col-sm-2 col-form-label">이메일</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="email" name="email">@<input type="text" class="form-control" id="mem_email_sub" name="mem_email">
		    </div>
		  </div>
		  <div class="form-group row">
		  	이메일 수신여부
        <div class="col-sm-5" >
          <input name="email_accept" type="radio" value="Y">수신
          <input name="email_accept" type="radio" value="N">수신안함
        </div>
		  </div>
		  <div class="form-group row">
		    <label for="passwdConfirm_q" class="col-sm-2 col-form-label">비밀번호 확인시 질문</label>
		    <div class="col-sm-5">
		      <select name="passwdConfirm">
            <option>========선택==========</option>
            <option>========선택==========</option>
          </select>
		    </div>
      </div>
        <div class="form-group row"> 
          <label for="passwdConfirm_a" class="col-sm-2 col-form-label">비밀번호 확인시 답변</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="passwdConfirm_a" name="passwdConfirm_a">
          </div>
        </div>
		  <div class="form-group row">
		  	성별
        <div class="col-sm-5">
            <input name="gender" type="radio" value="Y">남자
            <input name="gender" type="radio" value="N">여자
        </div>
		  </div>
      <div class="form-group row">
		  	생년월일
        <div class="col-sm-5">
		      <select id="year">
            <option>1900</option>
          </select>년
          <select id="month">
            <option>01</option>
          </select>월
          <select id="day">
            <option>11</option>
          </select>일
		    </div>
		  </div>

		  <div class="form-group row">
			  <div class="col-sm-12 text-center">
			  	<button type="button" class="btn btn-dark" id="btnJoin">회원가입</button>
			  </div>			
		  </div>
	 </form>
  </div>


  <!--  footer.jsp -->
  <%@include file="/WEB-INF/views/include/footer.jsp" %>
</div>


  <!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
</div>

<script>

  $(document).ready(function(){

    //생년월일
    let birth = function () {
        for (var i = 1900; i < 2023; i++) {
            $('#year').append('<option value="' + i + '">' + i + '</option>');
        }
        for (var i = 1; i < 13; i++) {
            $('#month').append('<option value="' + i + '">' + i + '</option>');
        }
        for (var i = 1; i < 32; i++) {
            $('#day').append('<option value="' + i + '">' + i + '</option>');
        }
    }

    birth();

    //비밀번호 유효성
    function chkPW(){

      var pw = $("#password").val();
      var num = pw.search(/[0-9]/g);
      var eng = pw.search(/[a-z]/ig);
      var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

      if($("#password").val() != ''){
        if(pw.length < 8 || pw.length > 16){
          alert("비밀번호를 8자리 ~ 16자리 이내로 입력해주세요.");
          return false;
        }else if(pw.search(/\s/) != -1){
          alert("비밀번호는 공백 없이 입력해주세요.");
          return false;
        }else if( (num < 0 && eng < 0) || (eng < 0 && spe < 0) ){
          alert("비밀번호를 영문/최소 1개의 숫자 또는 특수문자를 혼합하여 입력해주세요.");
          return false;
        }else {
          console.log("통과");	 
        }
      }else{
        alert("비밀번호를 입력해주세요.");
      }

    }

    //아이디 유효성
    function idCheck(){
      if($("#id").val() != ''){

      var id = $("#id").val();
      var num = id.search(/[0-9]/g);
      var eng = id.search(/[a-z]/ig);

      if(id.length < 4 || id.length > 16){
          alert("아이디를 4자리 ~ 16자리 이내로 입력해주세요.");
          return false;
        }else if(id.search(/\s/) != -1){
          alert("아이디는 공백 없이 입력해주세요.");
          return false;
        }else if((num < 0 && eng < 0)){
          alert("아이디를 영문/숫자로 입력해주세요.");
          return false;
        }else {
          console.log("통과");	 
        }

      }else{
        alert("아이디를 입력해주세요.");
      }
  }

 
    $("#btnJoin").on("click", function(){
      //아이디
      idCheck()
      //비밀번호
      chkPW();
      //비밀번호 확인
      let password = $("#password").val();
      let password2 = $("#password_2").val();
      
      if(password2 != password){
        alert("비밀번호가 일치하지 않습니다.");
        return false;
      }
      //이름
      if($("#name").val() == ''){
        alert("이름을 입력해 주세요.");
      }
      //이메일
      if($("#email").val() == ''){
        alert("이메일을 입력해 주세요.");
      }
      if($("#mem_email_sub").val() == ''){
        alert("이메일을 입력해 주세요.");
      }
      //이메일수신체크
      let accept = $("input[name='email_accept']").val();
      console.log("이메일" + accept);
      if(accept == ''){
        alert("이메일 수신 체크해주세요.");
      }
      
      //비밀번호 질문
      if($("#passwdConfirm_a").val() == ''){
        alert("비밀번호 질문 답변을 입력해 주세요.");
      }

    });


  });

</script>


  </body>
</html>
