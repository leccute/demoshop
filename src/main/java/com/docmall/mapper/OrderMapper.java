package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;

public interface OrderMapper {

	//1)주문 정보 저장하기 
	void orderSave(OrderVO vo); //odr_code null 상태
	
	//2-1)주문상세 저장하기. 장바구니 테이블의 데이터를 참조해서, 주문 상세 테이블에 저장한다.
	void orderDetailSave(@Param("odr_code") Long odr_code, @Param("mem_id") String mem_id);
	
	//2-2)바로주문에서 주문상세 저장하기(장바구니 사용 안함. 방법은 여러 갠데, 카트 테이블에 일단 저장이 되게끔 하고, 그걸 이용할 수도 있다.)
	void orderDirectDetailSave(OrderDetailVO vo);
	
	//3)결제정보 저장하기
	void paymentSave(PaymentVO vo);
	
	//바로구매에서 보여줄 주문 내역
	CartVOList directOrder(CartVO vo);
	
	//시퀀스 가져오기
	Long getOrderSequence();
	
	//진행 주문 건수
	int getOrderProcessCount(String mem_id);
	
	//주문 내역(페이징 O, 검색 X)
	List<OrderVO> getOrderList(@Param("mem_id") String mem_id, @Param("cri") Criteria cri);
	int getOrderTotalCount(String mem_id);
	
	//주문상세정보
	List<OrderDetailProductVO> getOrderDetailList(Long odr_code);
}
