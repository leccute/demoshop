<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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

    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
   
    <script id="reviewTemplate" type="text/x-handlebars-template">
      {{#each .}}
        <p>{{rv_content}}</p>
        <div class="row">
          <div class="col-md-3">{{idThreeDisplay mem_id}}</div>
          <div class="col-md-9">{{prettifyDate rv_date_reg}}</div>
        </div>
        <div class="row">
          <div class="col-md-3">{{displayStar rv_score}}
            <input type="hidden" name="rv_score" value="{{rv_score}}">
          </div>
          <div class="col-md-9">{{authorityview mem_id rv_num}}</div>
        </div>
        <hr>
      {{/each}}
    </script>

    <!-- Favicons -->
<link rel="apple-touch-icon" href="/docs/4.6/assets/img/favicons/apple-touch-icon.png" sizes="180x180">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon-32x32.png" sizes="32x32" type="image/png">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon-16x16.png" sizes="16x16" type="image/png">
<link rel="manifest" href="/docs/4.6/assets/img/favicons/manifest.json">
<link rel="mask-icon" href="/docs/4.6/assets/img/favicons/safari-pinned-tab.svg" color="#563d7c">
<link rel="icon" href="/docs/4.6/assets/img/favicons/favicon.ico">
<meta name="msapplication-config" content="/docs/4.6/assets/img/favicons/browserconfig.xml">
<meta name="theme-color" content="#563d7c">


    <style>

      p#star_rv_score a.rv_score {
        font-size: 22px;
        text-decoration: none;
        color: lightgray;
      }

      p#star_rv_score a.rv_score.on{
        color: black;
      }

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
	<script>
      let msg = '${msg}';
      if(msg != '') {
        alert(msg);
      }

    </script>
    
   
  </head>
  <body>
    
<%@include file="/WEB-INF/views/include/header.jsp" %>
<%@include file="/WEB-INF/views/include/categoryMenu.jsp" %>

<div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
  <h1 class="display-4">${cate_name}</h1>
</div>

<div class="container">
  
  <div class="row mb-3">
    <div class="col-md-12">
      <div class="card mb-3" style="max-width: 540px;">
        <div class="row no-gutters">
          <div class="col-md-4">
            <img src="/product/displayFile?folderName=${productVO.pdt_img_folder }&fileName=s_${productVO.pdt_img}" class="bd-placeholder-img card-img-top" width="100%" height="225">
          </div>
          <div class="col-md-8">
            <div class="card-body">
              <h3 class="card-title" style="text-align: center;">${productVO.pdt_name}</h3>
              <input type="hidden" name="pdt_num" value="${productVO.pdt_num}">
              <input type="number" name="odr_amount" value="1">
              <input type="hidden" name="pdt_num" value="${productVO.pdt_num}">
              <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.
                그리고 얘는 완전 최고다 어디까지 쓸 수 있을까 랄랄랄라
              </p> 
            </div>
            <button type="button" name="btnDirectOrder" class="btn btn-sm btn-outline-secondary">바로 구매</button>
            <button type="button" name="btnCart" class="btn btn-sm btn-outline-secondary">장바구니</button>
          </div>
        </div>
      </div>
    
    </div>
   </div>
      

   <div class="row">
     <div class="col-md-12">
        <div id="tabs">
          <ul>
            <li><a href="#product_desc">상품설명</a></li>
            <li><a href="#product_qna">상품후기</a></li>
          </ul>
          
          <div id="product_desc">
            상품설명<br>
            ${productVO.pdt_detail}
          </div>
          <div id="product_qna" class="border border-primary">
            <h5>상품후기</h5>
            <div class="form-group row">
              <div class="col-md-12">
                <p id="star_rv_score">
                  평점: 
                  <a class="rv_score" href="#">★</a>
                  <a class="rv_score" href="#">★</a>
                  <a class="rv_score" href="#">★</a>
                  <a class="rv_score" href="#">★</a>
                  <a class="rv_score" href="#">★</a>
                </p>
              </div>
            </div>
            
            <div class="row">
              <div class="col-md-12">
                <textarea rows="3" cols="80" class="form-control" id="rv_content"></textarea>
                <button type="button" id="btn_review_write" class="btn btn-link">등록</button>
                <button type="button" id="btn_review_edit" class="btn btn-link" style="display: none;">수정</button>
              </div>
            </div>
            
            <!--상품 후기 삽입 위치-->
            <div class="row" id="reviewList">
              <div class="col-md-12" id="reviewItem">
              </div>
            </div>

            <!--댓글 페이징 삽입 위치-->
            <div class="row" id="reviewPaging">
              <div class="col-md-12" id="reviewPagingItem">
                <nav aria-label="Page navigation example">
                  <ul class="pagination">

                  </ul>
                </nav>
              </div>
            </div>

          </div>
        </div>
     </div>

   </div>

   
  <%@include file="/WEB-INF/views/include/footer.jsp" %>
</div>

<script>

$(document).ready(function(){
  //상품설명, 상품후기 Tap
  $("#tabs").tabs();
  
  //장바구니 클릭
  $("button[name='btnCart']").on("click", function(){
    $.ajax({
      url: '/cart/cart_add',
      data: {
        pdt_num: $("input[name='pdt_num']").val(),
        cart_amount: $("input[name='odr_amount']").val()
      }, //자바스크립트 오브젝트 구문 (CartVO 클래스의 필드값이 전부 필요한데, 하나는 pdt_num으로 다른 하나는 시퀀스로.)
      success: function(result){
        if(result == "success"){
          alert("장바구니에 추가됨");
          if(confirm("장바구니로 이동하시겠습니까?")){
            location.href = "/cart/cart_list";
          }
        }
      }
    });
  });

  //바로 구매 (상품 1개, 바로 구매 버튼 1개)
  $("button[name='btnDirectOrder']").on("click", function(){
    //주문 작성 페이지
    //상품코드, 수량:기본값 1
    let pdt_num = $("input[name='pdt_num']").val();
    //$(this).parent().find("input[name='pdt_num']").val(); 위 아래 둘 다 가능하다. 
    let odr_amount = $("input[name='odr_amount']").val();

    let url = "/order/orderListInfo?pdt_num=" + pdt_num + "&odr_amount=" +odr_amount + "&type=direct";

    console.log("주문작성 URL: " + url);

    location.href = url;

  });


  //평점 별 클릭
  $("p#star_rv_score a.rv_score").on("click", function(e){
    e.preventDefault();

    //선택시 p태그 입장에서 자식 a태그의 class="on" 모두 제거하라
    $(this).parent().children().removeClass("on"); //클래스 초기화(선택했다가, 다시 바꿀 때 필요)
    $(this).addClass("on").prevAll("a").addClass("on"); //a태그에 on클래스를 줘라, 이전 a태그들에도 on클래스 줘라
  });

  //상품후기 (리뷰) 수정하기
  $("#btn_review_write").on("click", function(){
    let rv_score = 0;
    let rv_content = $("#rv_content").val();

    //each: 선택자만큼 반복
    $("p#star_rv_score a.rv_score").each(function(index, item){
      if($(this).attr("class") == "rv_score on"){
        rv_score += 1;
      }
    });

    if(rv_score == 0){
      alert("별 평점을 선택해주세요.");
      return;
    }

    if(rv_content == ""){
      alert("상품 후기를 입력하세요.");
      return;
    }

    let review_data = {
                pdt_num: $("input[name='pdt_num']").val(), 
                rv_content: rv_content, 
                rv_score: rv_score
    };

    $.ajax({
      url: '/review/new',
      headers: {
        "Content-Type" : "application/json", 
        "X-HTTP-Method-Override" : "POST"
      },
      type: 'POST',
      dataType: 'text',  //text, xml, json 이 올 수 있다.
      data: JSON.stringify(review_data),  //json문자열로 전환시켜주는 메소드.(RestController는 json포맷으로 데이터를 주고 받는다.)
      success : function(result){
        if(result == 'success'){
          alert("상품 후기가 등록됨.");
          
          url = "/review/list/" +  $("input[name='pdt_num']").val() + "/" + reviewPage;
          getPage(url);

          //초기화
          $("p#star_rv_score a.rv_score").removeClass("on");
          $("#rv_content").val("");
          /*
          $("#btn_review_edit").parent().find("input[name='rv_num']").remove();
          $("#btn_review_edit").hide();
          $("#btn_review_write").show();
          */
        }
      }
    });

  });
}); //ready 이벤트 끝.

//상품 댓글(후기) 목록 요청
let reviewPage = 1; //상품후기 페이지 번호
let url = "/review/list/" +  $("input[name='pdt_num']").val() + "/" + reviewPage;

console.log(url);

getPage(url);

function getPage(pageInfo){
  $.getJSON(pageInfo, function(data){
    
    //console.log("댓글 목록: " + data.list);
    //console.log("댓글 페이지 정보: " + data.pageMaker);
    printReviewList(data.list, $("div#reviewList div#reviewItem"), $("#reviewTemplate"));
    printrivewPaging(data.pageMaker, $("div#reviewPaging ul.pagination"));
  });
}

//1)상품 후기 목록 출력하는 함수(핸들바 안쓰면 for문으로 하나하나 추가해야 한다.)
let printReviewList = function(reviewData, target, templateObj){
  
  let template = Handlebars.compile(templateObj.html());

  let reviewHtml = template(reviewData); //댓글 목록 데이터와 템플릿 결합

  target.children().remove();
  target.append(reviewHtml);
}

//상품 후기 등록일 날짜 출력. 자바스크립트 날짜객체 https://www.w3schools.com/js/js_dates.asp
//사용자 정의 Helper 함수. 템플릿에서 호출
Handlebars.registerHelper("prettifyDate", function(timeValue){
  
  let dateObj = new Date(timeValue);
  let year = dateObj.getFullYear();
  let month = dateObj.getMonth();
  let date = dateObj.getDate();
  let hour = dateObj.getHours();
  let minute = dateObj.getMinutes();

  return year + "/" + month + "/" + date + " " + hour + ":" + minute;
});

//숫자로 저장된 숫자를 다시 별표시로 바꿔서 출력하기
Handlebars.registerHelper("displayStar", function(rating){
  let starStr = "";
  
  switch(rating){
    case 1: 
      starStr = "★☆☆☆☆";
      break;
    case 2:
      starStr = "★★☆☆☆";
      break;
    case 3:
      starStr = "★★★☆☆";
      break;
    case 4:
      starStr = "★★★★☆";
      break;
    case 5:
      starStr = "★★★★★";
      break;
  }

  return starStr;
});

//아이디 3글자만 보이기
Handlebars.registerHelper("idThreeDisplay", function(userid){
  return userid.substring(0, 3) + "*******";
});

//댓글 작성자와 로그인이 동일한 경우 수정, 삭제 버튼 표시
Handlebars.registerHelper("authorityview", function(mem_id, rv_num){

  let str = "";
  let login_id = '${sessionScope.loginStatus.mem_id}';
  if(mem_id == login_id){
    str += "<button type='button' class='btn btn-link' name='review_edit' data-rv_num='" + rv_num + "'>[edit]</button>";
    str += "<button type='button' class='btn btn-link' name='review_delete' data-rv_num='" + rv_num + "'>[delete]</button>";
  }

  return new Handlebars.SafeString(str);
});


//2)상품 후기 페이징 출력하는 함수
let printrivewPaging = function(pageMaker, target){ //성격상 핸들바를 쓸 수 없다.
  let pagingStr = "";

  //이전표시. pageMaker.startPage - 1
  if(pageMaker.prev){
    pagingStr += "<li class='page-item'><a class='page-link' href='" + (pageMaker.startPage - 1) + "' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
  }             

  //페이지 번호 표시
  for(let i = pageMaker.startPage; i <= pageMaker.endPage; i++){
    let classStr = pageMaker.cri.pageNum == i ? "active" : "";
    pagingStr += "<li class='page-item " + classStr + "'><a class='page-link' href='" + i + "'>"+ i +"</a></li>";
  }


  //다음표시. pageMaker.endPage + 1
  if(pageMaker.next){
    pagingStr += "<li class='page-item'><a class='page-link' href='" + (pageMaker.endPage + 1) + "' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
  }

  target.children().remove();
  target.append(pagingStr);
}


</script>
<script>

  $(document).ready(function(){
    
    //이전, 페이지번호, 다음 클릭
    $("nav ul.pagination").on("click", "a.page-link", function(e){
      e.preventDefault();

      reviewPage = $(this).attr("href"); //현재 선택한 페이지번호(전역변수로 빼놨었다.)

      //컨트롤러 메소드에 xml이나 json에 대한 명시적인 성격이 없었고, getPage()자체가 getJson성격이기 때문에,
      //원래대로라면 url에 ".json"을 붙이지 않아도 댓글 페이지가 불러져야 하는데, 무슨 문제가 있는지 작동하지 않았다.
      //이럴 땐, 명시적으로 url에 ".json"을 붙여주니 해결이 됐다.
      url = "/review/list/" +  $("input[name='pdt_num']").val() + "/" + reviewPage + ".json";

      console.log("url : " + url);

      getPage(url);
    });

    
    //상품 후기 삭제하기  $("div#reviewList") 이렇게만 썼었구만.
    $("div#reviewList div#reviewItem").on("click", "button[name='review_delete']", function(e){
     
      //e.preventDefault(); //이거 안하면, 밑에 console.log조차 안 찍힌다,, 나도 되네 
      //원래 성격이 button이고 submit이나 a태그 같은 애가 아니라, 굳이 안해도 되는 거 아는데, 아깐 왜 안떴담???

      //let rv_num = $(this).attr("data-rv_num"); 
      let rv_num = $(this).data("rv_num");      //아 "rv_num"가 아니라, rv_num 이렇게 써서 안된 거였나,, 
      

      if(!confirm("상품후기 " + rv_num +"번을 삭제하시겠습니까?")) return;

      $.ajax({
        url: '/review/delete/' + rv_num,
        type: 'delete',
        headers: {
          "Content-Type" : "application/json", 
          "X-HTTP-Method-Override" : "DELETE"
        },
        dataType: 'text', //컨트롤러에서 String 타입이기 때문에, text.(스프링에서 넘어오는 타입)
        success: function(result){ //에러 처리하는 거 error였나?
          if(result == "success"){
            alert("댓글 삭제 완료");

            url = "/review/list/" +  $("input[name='pdt_num']").val() + "/" + reviewPage + ".json";
            getPage(url);
          }
        }
      });
      
    });

    //상품후기 수정폼. review_edit
    $("div#reviewList div#reviewItem").on("click", "button[name='review_edit']", function(e){
      
      $("#btn_review_write").hide();
      $("#btn_review_edit").show();
      

      let rv_score = $(this).parent().parent().find("input[name='rv_score']").val();
      let rv_num = $(this).data("rv_num");
      let rv_content = $(this).parent().parent().prev().prev().html(); //p태그에 이름이나 클래스 붙여서 prev말고 다른 걸로 찾아보기

      console.log("별 평점: " + rv_score);
      console.log("상품 후기 번호: " + rv_num);
      console.log("후기 내용: " + rv_content);

      $("textarea#rv_content").val(rv_content);
      
      $("textarea#rv_content").parent().find("input[name='rv_num']").remove();
      $("textarea#rv_content").parent().append("<input type='text' name='rv_num' readonly value='"+ rv_num +"'>");
      
      $("p#star_rv_score a.rv_score").each(function(index, item){
        if(index < rv_score){
          $(item).addClass("on");
        }else{
          $(item).removeClass("on");
        }
      });
    });

    //상품후기 수정하기
    $("#btn_review_edit").on("click", function(){
      let rv_score = 0;
      let rv_content = $("#rv_content").val();

      //each: 선택자만큼 반복
      $("p#star_rv_score a.rv_score").each(function(index, item){
        if($(this).attr("class") == "rv_score on"){
          rv_score += 1;
        }
      });

      if(rv_score == 0){
        alert("별 평점을 선택해주세요.");
        return;
      }

      if(rv_content == ""){
        alert("상품 후기를 입력하세요.");
        return;
      }

      //자바스크립트 오브젝트 문법
      let review_data = {
                  rv_num: $("input[name='rv_num']").val(), 
                  rv_content: rv_content, 
                  rv_score: rv_score
      };

      $.ajax({
        url: '/review/modify',
        headers: {
          "Content-Type" : "application/json", 
          "X-HTTP-Method-Override" : "PATCH"
        },
        type: 'patch',
        dataType: 'text',  //text, xml, json 이 올 수 있다.
        data: JSON.stringify(review_data),  //json문자열로 전환시켜주는 메소드.(RestController는 json포맷으로 데이터를 주고 받는다.)
        success : function(result){
          if(result == 'success'){
            alert("상품 후기가 수정됨.");
            getPage(url);

            //초기화
            $("p#star_rv_score a.rv_score").removeClass("on");
            $("#rv_content").val("");
            $("#btn_review_edit").parent().find("input[name='rv_num']").remove();
            $("#btn_review_edit").hide();
            $("#btn_review_write").show();
          }
        }
      });

      

    });

  });
</script>

    
  </body>
</html>
