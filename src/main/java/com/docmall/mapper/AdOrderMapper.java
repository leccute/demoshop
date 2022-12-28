package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.AdOrderDetailVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;

public interface AdOrderMapper {

	//검색기능 추가한 페이징
	List<OrderVO> getOrderList(@Param("cri") Criteria cri, 
							@Param("odr_status") String odr_status,
							@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	//총 데이터 갯수
	int getOrderTotalCount(@Param("cri") Criteria cri, 
						@Param("odr_status") String odr_status,
						@Param("startDate") String startDate, @Param("endDate") String endDate);
	
	//주문 상태 변경
	int orderStatusChange (@Param("odr_status") String odr_status, @Param("odr_code") Long odr_code);
	
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
	void pay_memo(@Param("pay_memo") String pay_memo, @Param("pay_code") Integer pay_code);
	
	//주문삭제
	void orderDelete(Long odr_code);
	void orderDetailDelete(Long odr_code);
	void orderPaymentDelete(Long odr_code);
	
	//주문 상세 개별 상품 삭제 기능
	//1)주문 상품 개별 삭제
	void orderDetailProductDelete(@Param("odr_code") Long odr_code, @Param("pdt_num") Integer pdt_num);
	//2)주문 상품 개별 삭제 후 주문 금액 업데이트(금액 차감)
	void orderTotalPriceChange(@Param("odr_code") Long odr_code, @Param("odr_price") int odr_price);
}
