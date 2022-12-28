package com.docmall.service;

import java.util.List;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;

public interface OrderService {

	//주문 목록
	List<CartVOList> cart_list(String mem_id);
	
	//2-1)주문 정보 저장하기 - 장바구니 구매시
	void orderSaveWithCart(OrderVO o_vo, PaymentVO p_vo); //String type 제거.
	
	//2-2)주문 정보 저장하기 - 바로 구매시
	void orderSaveDirect(OrderVO o_vo, PaymentVO p_vo, OrderDetailVO od_vo);
	
	
	//바로구매에서 보여줄 주문 내역
	CartVOList directOrder(CartVO vo);
	
	//시퀀스 가져오기
	Long getOrderSequence();
	
	//진행 주문 건수
	int getOrderProcessCount(String mem_id);
	
	//주문 내역(페이징 O, 검색 X)
	List<OrderVO> getOrderList(String mem_id, Criteria cri);
	int getOrderTotalCount(String mem_id);
	
	//주문상세정보
	List<OrderDetailProductVO> getOrderDetailList(Long odr_code);
	
}
