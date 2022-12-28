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
  
 <div class="row">
 	<div class="col-md-12">
	 	<div class="box">
			<div class="box-header">
			<h3 class="box-title">장바구니 목록</h3>
			</div>
			
			<div class="box-body no-padding">
				<table class="table table-striped" id="cartlist">
					<tbody>
					
					<tr>
						<th style="width: 2%"><input type="checkbox" id="checkAll"></th>
						<th style="width: 12%">이미지</th>
						<th style="width: 13%">상품이름</th>
						<th style="width: 13%">판매가</th>
						<th style="width: 10%">수량</th>
						<th style="width: 10%">적립금</th>
						<th style="width: 10%">배송구분</th>
						<th style="width: 10%">배송비</th>
						<th style="width: 10%">합계</th>
						<th>선택</th>
					</tr>
					
					<c:forEach items="${cartList}" var="cartListVO">
					<c:set var="sales_price" value="${cartListVO.sales_price }" />
					<tr>
						<td><input type="checkbox" class="check" value="${cartListVO.cart_code}"></td>
						<td>
              <a href="${cartListVO.pdt_num}" class="proDetail">
              <img src="/cart/displayFile?folderName=${cartListVO.pdt_img_folder}&fileName=s_${cartListVO.pdt_img}">
            </a></td>
						<td>${cartListVO.pdt_name}</td>
            <!-- ￦ 얘를 pattern에 넣어 뒀더니, withoutcommas 사용자 정의 함수를 만들어도 string이 나와서 NaN이 나왔다. 
              /'원'도 없어지니 이것도 처리가 필요하겠다/처리:1128.txt 파일.-->
						<td>
              <p style="text-decoration: line-through;"><fmt:formatNumber type="number" pattern="￦#,###" value="${cartListVO.pdt_price}" /></p>
              <span class="pdt_price"><fmt:formatNumber type="number" pattern="￦#,###" value="${cartListVO.pdt_dis_price}" /></span>원
            </td>
						<td>
							<input type="number" min="1" name="cart_amount" value="${cartListVO.cart_amount}" style="width:50px;"><br>
							<button type="button" name="btnQtyChange" class="btn btn-link">변경</button>
						</td>
						<td>포인트</td>
						<td>기본배송</td>
						<td>무료</td>
						<td>
							<span class="sales_price">
							<fmt:formatNumber type="number" pattern="￦#,###" value="${cartListVO.sales_price}" />원
							</span>
						</td>
						<td>
							<input type="hidden" name="cart_code" value="${cartListVO.cart_code}">
							<button type="button" name="btnDelete" class="btn btn-link">Delete</button>
						</td>
					</tr>
					<c:set var="sum_price" value="${sum_price + sales_price}" />
					</c:forEach>
				</tbody>
				<tfoot>
          <tr>
            <c:if test="${empty cartList}">
              <td colspan="10" style="text-align: center;">장바구니가 비었습니다. </td>
            </c:if>
            
            <c:if test="${!empty cartList}">
              <td colspan="10" style="text-align: right;">총금액: ￦<span id="sum_price"><fmt:formatNumber type="number" pattern="#,###" value="${sum_price}" /></span></td>
            </c:if>
          </tr>
				</tfoot>
			  </table>
  <c:if test="${!empty cartList}">
    <div class="box-footer">
      <div class="row">
        <div class="col-md-12">
          <button type="button" name="btnSelectDel" class="btn btn-link">선택 삭제</button>
          <button type="button" name="btnCartEmpty" class="btn btn-link">장바구니 비우기</button>
          <button type="button" name="btnOrder" class="btn btn-link">주문하기</button><br>
        </div>
      </div>
    </div>
  </c:if>
			</div>
			
		</div>
 	</div>
 </div>
      
  <%@include file="/WEB-INF/views/include/footer.jsp" %>
</div>

<script>

$(document).ready(function(){
  //장바구니 클릭
  $("button[name='btnCart']").on("click", function(){
    $.ajax({
      url: '/cart/cart_add',
      data: {
        pdt_num: $(this).parent().find("input[name='pdt_num']").val(),
        cart_amount: 1
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

  //상품 이미지 클릭
  $("a.proDetail").on("click", function(e){
    e.preventDefault();

    let pdt_num = $(this).attr("href");

    location.href = "/product/productDetail?pdt_num=" + pdt_num;
  });

  //수량변경 클릭
  let btnQtyChange;
  $("button[name='btnQtyChange']").on("click", function(){
    
	  btnQtyChange = $(this); //변경 태그(버튼), 상대적인 위치를 참조.(동적인 만큼, 확정하기 힘들기 때문.)
	  
    $.ajax({
      url: '/cart/cart_qty_change',
      data: {
        cart_code: $(this).parent().parent().find("input[name='cart_code']").val(),
        cart_amount: $(this).siblings("input[name='cart_amount']").val()
      }, //자바스크립트 오브젝트 구문 (CartVO 클래스의 필드값이 전부 필요한데, 하나는 pdt_num으로 다른 하나는 시퀀스로.)
      beforeSend: function(xhr){ //주소 요청 전에 필요한 내용들.
        xhr.setRequestHeader("AJAX", "true");
      },
      success: function(result){
        if(result == "success"){
          alert("장바구니 수량이 변경됨");
          
          //연산할 때는 콤마 제거, 화면에 표시할 때는 콤마 찍기
          //sales_price 변동
          let pdt_price = parseInt($.WithoutCommas(btnQtyChange.parent().parent().find("span.pdt_price").text()));
          let cart_amount = parseInt(btnQtyChange.siblings("input[name='cart_amount']").val());
          btnQtyChange.parent().parent().find("span.sales_price").text($.numberWithCommas(pdt_dis_price*cart_amount));
          
          console.log(pdt_dis_price);
          console.log(cart_amount);
          
          //sum_price 가격 변동
          sum_price();

          // location.href = "/cart/cart_list"; 나는 이렇게 했었는데, 강사님이 다른 방식으로 하심. NaN나오는데, 왜일까.
        }
      },
      error: function(xhr, status, error){
        alert(status);
        alert("로그인이 필요한 내용입니다.");
        location.href = "/member/login";
      }
    });
  });

  //장바구니 삭제
  $("button[name='btnDelete']").on("click", function(){
    if(!confirm("항목을 삭제하시겠습니까?")) return;

    let current_tr = $(this).parent().parent(); //ajax구문이 실행되기 전 삭제행을 참조해야 한다.

    $.ajax({
      url: '/cart/cart_delete',
      data: {
        cart_code: $(this).siblings("input[name='cart_code']").val()
      },
      success: function(result){
        if(result == "success"){
          alert("장바구니 삭제됨");
          current_tr.remove();

          //sum_price 가격 변동
          sum_price();
        }
      }
    });
  });

  //사용자 정의 함수
  //콤마 제거하기. 연산할 때 사용.
  $.WithoutCommas = function(x){    
    let a = x.toString().replace(",", "");
    let b = a.toString().replace("￦", "")
   return b;
  }

  //3자리 마다 콤마 찍기. 표시할 때
  $.numberWithCommas = function(x){
   let a = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return a + "원";
  }

  //장바구니 비우기 클릭
  $("button[name='btnCartEmpty']").on("click", function(){
    if(!confirm("장바구니를 비우시겠습니까?")) return;
    location.href = "/cart/cart_empty";
  });

  //제목행 체크박스 선택
  let isCheck = true;
  $("#checkAll").on("click", function(){
   
    //데이터 행 체크박스.         this = #checkAll
    $(".check").prop("checked", this.checked);

    isCheck = this.checked;
  });

  //데이터행 체크박스 선택
  $(".check").on("click", function(){
    //제목행 체크박스
    $("#checkAll").prop("checked", this.checked); //데이터행 체크하면, 제목행 체크 되어라.(하나만 해도)

    //데이터 행의 체크박스 상태를 확인.
    $(".check").each(function(){              //모든 항목을 다 볼 때까지 반복하고,
      if(!$(this).is(":checked")){            //체크가 안된 게 있으면,
        $("#checkAll").prop("checked", false);    //"#checkAll"의 checked 상태를 false로 돌려라
      }
    });
  });

  //선택삭제
  $("button[name='btnSelectDel']").on("click", function(){
    //선택된 게 하나는 있어야 한다.
    if($(".check:checked").length == 0){ //선택된 데이터 체크박스
      alert("삭제할 상품을 체크하세요."); 
      return;
    }

    if(!confirm("선택한 상품을 삭제하시겠습니까?")) return;

    //삭제할 장바구니 코드
    let cart_code_arr = [];

    //선택한 체크박스
    $(".check:checked").each(function(){
      cart_code_arr.push($(this).val()); //push만 공부하지 말고, JS 배열에 관한 내용 다 공부하기 (w3schools에서 관련 내용 전부.)
    });

    console.log("삭제할 장바구니 코드: " + cart_code_arr);

    $.ajax({
      url: '/cart/cart_sel_delete',
      type: 'post',
      dataType: 'text',
      data: {cart_code_arr : cart_code_arr},
      success: function(result){
        if(result == "success"){
          alert("선택한 상품이 삭제되었습니다.");
          location.href = "/cart/cart_list";
        }
      }
    });
  });

  //주문하기
  $("button[name='btnOrder']").on("click", function(){
    location.href="/order/orderListInfo?type=cart";
  });

});  //ready() 끝.


//sum_price 가격 변동 (jquery 함수가 안먹어서, 순수 자바스크립트 문법으로 함수를 만들었다.)
function sum_price(){
  let sum_price = 0;
    $("table#cartlist span.sales_price").each(function(index, item){ //선택자 반복적으로. 장바구니에 2 데이터 있으므로, 2	번 반복
      sum_price += parseInt($.WithoutCommas($(item).text()));
    });
    //btnQtyChange.parents("table#cartlist").find("span#sum_price").text($.numberWithCommas(sum_price));
    //굳이 parents를 찾을 필요 없이 직접 하면 되는 것.(c:forEach 바깥에 있기 때문에, 직접하면 된다.)
    
    $("span#sum_price").text($.numberWithCommas(sum_price));

}
</script>

    
  </body>
</html>
