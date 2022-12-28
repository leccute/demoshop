<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!-- 이 jsp파일에는 j쿼리 사용할 수 있는 문장이 없는데, orderList.jsp에 끼어 들어가 있는 파일이라 jsp를 읽고 얘를 읽어들이는 거라서 j쿼리를 사용할 수 있는 환경
따라서, 따로 j쿼리 관련 문장을 써주지 않아도 사용 가능하다.-->

<div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">주문 상세 내역</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h4>주문상세정보</h4>
        	<table class="table table-bordered">
        		<tr>
        			<th>주문번호</th>
        			<th>주문상품/이미지</th>
        			<th>수량</th>
        			<th>주문금액</th>
        			<th>배송료</th>
					<th>비고</th>
        		</tr>
        		<c:forEach items="${odList2}" var="orderDetailVOList">
	        		<tr>
	        			<td>${orderDetailVOList.orderDetailVO.odr_code }</td>
	        			<td>
						<img src="/admin/order/displayFile?folderName=${orderDetailVOList.productVO.pdt_img_folder }&fileName=s_${orderDetailVOList.productVO.pdt_img}" class="bd-placeholder-img card-img-top">
						<a class="move" href="${orderDetailVOList.productVO.pdt_num}" ><c:out value="${orderDetailVOList.productVO.pdt_name}" /></a>
           				</td> 
	        			<td>${orderDetailVOList.orderDetailVO.odr_amount }</td>
	        			<td><span class="odr_price">${orderDetailVOList.orderDetailVO.odr_price }</san></td>
	        			<td>0원</td>
						<td>
							<input type="hidden" name="odr_code" value="${orderDetailVOList.orderDetailVO.odr_code }">
							<input type="hidden" name="pdt_num" value="${orderDetailVOList.orderDetailVO.pdt_num}">
							<button type="button" name="btnOrderDetailDel" class="btn btn-link">삭제</button></td>
	        		</tr>
        		</c:forEach>
        	</table><br>
        <h4>결제정보</h4>
        	<table class="table table-bordered">
        		<tr>
        			<th>결제방식</th>
        			<td>${paymentVO.pay_method }</td>
        			<th>결제금액</th>
        			<td><span id="pay_tot_price">${paymentVO.pay_tot_price }</span></td>
        		</tr>
        		<tr>
        			<th>입금자명</th>
        			<td>${paymentVO.pay_nobank_user }</td>
        			<th>입금계좌</th>
        			<td>${paymentVO.pay_nobank }</td>
        		</tr>
        	</table><br>
        <h4>주문정보</h4>
        	<table class="table table-bordered">
        		<tr>
        			<th>수령인</th>
        			<td>${orderVO.odr_name}</td>
        			<th>전화번호</th>
        			<td>${orderVO.odr_phone}</td>
        		</tr>
        		<tr>
        			<th>주소</th>
        			<td colspan="3">${orderVO.odr_addr} ${orderVO.odr_addr_d}<br>
        			우편번호 : ${orderVO.odr_zipcode}
        			</td>
        		</tr>
        		<tr>
        			<th>배송메모</th>
        			<td colspan="3">빈칸</td>
        		</tr>
        	</table><br>
        <h5>관리자메모</h5>
        <form action="" method="post"><!-- ajax를 이용하면 form태그 안써도 된다 -->
		<input type="hidden" name="pay_code" value="${paymentVO.pay_code}">
        <textarea name="pay_memo" class="form-control" rows="3">${paymentVO.pay_memo}</textarea>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="button" name="btnPayMemo" class="btn btn-primary">저장하기</button>
      </div>
    </div>
  </div>
  
  <script>

	//관리자 메모
	$("button[name='btnPayMemo']").on("click", function(){

		$.ajax({
			url: '/admin/order/pay_memo',
			data: {
				pay_code : $("input[name='pay_code']").val(),
				pay_memo : $("textarea[name='pay_memo']").val()
			},
			type: 'post',
			dataType: 'text',
			success: function(result){  //콜백 개념.
				if(result == 'success'){ 
					alert("관리자 메모가 저장되었습니다.");
				}
			}
		});
	});

	//개별 주문 상품 삭제  (odr_code, pdt_num)
	$("button[name='btnOrderDetailDel']").on("click", function(){
		if(!confirm("주문 상품을 삭제하시겠습니까?")) return;
		
		let odr_code = $(this).siblings("input[name='odr_code']").val();
		let pdt_num = $(this).siblings("input[name='pdt_num']").val();
		let odr_price = $(this).parent().parent().find("span.odr_price").text();

		// console.log("주문코드: " + odr_code);
		// console.log("상품 번호: " + pdt_num);

		//ajax에서는 $(this)를 쓸 수 없다. 콜백 형식이기에, 스프링으로 갔다가 돌아오면 그 정보를 갖고 있지 않다.

		let cur_tr = $(this).parent().parent(); //ajax()메소드 호출 전에 삭제할 tr태그를 미리 참조해 둬야 한다.

		 

		$.ajax({
			url: '/admin/order/orderDetailProductDelete',
			type: 'get',
			data: {
				odr_code : odr_code,
				pdt_num : pdt_num,
				odr_price : odr_price
			},
			dataType : 'text', //스프링에서 넘어 올 것 : success가 넘어올 것이다.
			success: function(data){
				if(data == 'success'){
					alert("개별 상품이 삭제되었습니다.");
					cur_tr.remove();

					let pay_tot_price = $("#pay_tot_price").text();
					$("#pay_tot_price").text(pay_tot_price - odr_price);
				}
			}
		});
	});
  </script>