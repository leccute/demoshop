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
        			<th>주문상품/이미지</th>
        			<th>수량</th>
        			<th>주문금액</th>
        			<th>배송료</th>
					<th>비고</th>
        		</tr>
        		<c:forEach items="${odList}" var="orderDetailVOList">
	        		<tr>
	        			<td>
						<img src="/order/displayFile?folderName=${orderDetailVOList.productVO.pdt_img_folder }&fileName=s_${orderDetailVOList.productVO.pdt_img}" class="bd-placeholder-img card-img-top">
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
      </div>
      <div class="modal-footer">
        <button type="button" name="btnClose" class="btn btn-secondary" data-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
  
  <script>
	$(document).ready(function(){

		//상품 이름 클릭
		$("a.move").on("click", function(e){
			e.preventDefault();
			let pdt_num = $(this).attr("href");

			location.href = "/product/productDetail?pdt_num=" + pdt_num;
		});

		$("button[name='btnClose']").on("click", function(){
			//$("#orderDetailModal").modal('close'); 모달 상자 정보는 orderList.jsp에 있어서 꺼지지 않는다. 
		});
	});
	
  </script>