package com.docmall.service;

import java.util.List;

import com.docmall.domain.AdOrderDetailVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;

public interface AdOrderService {

	//검색기능 추가한 페이징
	List<OrderVO> getOrderList(Criteria cri, String odr_status, String startDate, String endDate);
	
	//총 데이터 갯수
	int getOrderTotalCount(Criteria cri, String odr_status, String startDate, String endDate);
	
	//주문 상태 변경
	int orderStatusChange (String odr_status, Long odr_code);
	
	//진행 상태 카운트
	int orderStatusCount(String odr_status);
	
	//주문상세정보1. 
	List<AdOrderDetailVOList> getOrderDetailList1(Long odr_code);
	//주문상세정보2.
	List<OrderDetailProductVO> getOrderDetailList2(Long odr_code);
	
	//결제정보. PaymentVO
	PaymentVO getPaymentVO (Long odr_code);
	
	//주문정보. OrderVO
	OrderVO getOrderVO (Long odr_code);
	
	//관리자 메모
	void pay_memo(String pay_memo, Integer pay_code);
	
	//주문 삭제
	void orderInfoDelete(Long odr_code);
	
	//주문 상품 개별 삭제(여기서 int odr_price 얘는 왜 받아올까??? 트랜잭션 때문에?)
	void orderDetailProductDelete(Long odr_code, Integer pdt_num, int odr_price);
	
	
}
