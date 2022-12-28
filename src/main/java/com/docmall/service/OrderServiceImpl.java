package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.CartMapper;
import com.docmall.mapper.OrderMapper;

import lombok.Setter;

@Service
public class OrderServiceImpl implements OrderService{

	//Mapper 주입
	@Setter(onMethod_ = {@Autowired})
	private OrderMapper orderMapper;
	
	//장바구니 주입. 장바구니 목록 메소드 가져다 쓰려고.(얘도 매퍼니까 OrderMapper 작업 할 필요 없다.)
	@Setter(onMethod_ = {@Autowired})
	private CartMapper cartMapper;

	//주문목록.
	@Override
	public List<CartVOList> cart_list(String mem_id) {
		
		return cartMapper.cart_list(mem_id);
	}
	
	//서비스의 메소드 안에서 하나라도 에러가 발생하면, 다른 메소드 호출도 롤백처리 된다. 메소드들을 일부러 에러내서 트랜젝션이 되는지 반드시 확인해야 한다.
	//장바구니에서 주문하기 또는 상품리스트에서 바로 구매.(공통으로 사용하는 메소드) 
	@Transactional
	@Override
	public void orderSaveWithCart(OrderVO o_vo, PaymentVO p_vo) { //String type 제거
		
		//1)주문정보 저장하기(주문자와 배송지 정보)
		orderMapper.orderSave(o_vo); //odr_code필드에 시퀀스 값이 존재.
		  
		//2-1)주문상세 저장하기(주문상품 정보 - 장바구니 카트 내역을 넣어야 한다)
		orderMapper.orderDetailSave(o_vo.getOdr_code(), o_vo.getMem_id());  
		
		//3)결제정보 저장하기
		p_vo.setOdr_code(o_vo.getOdr_code());
		orderMapper.paymentSave(p_vo);  
		
		//4)장바구니 비우기 
		cartMapper.cart_empty(o_vo.getMem_id());
		
		//밑에 새로운 메소드를 이용하지 않고, 위에 4)에 if문을 써서 if(type.equal("cart")){}일 때만 진행하라고 하면 되네,,, ->근데 2번도 변경이라.
	}
	
	//상품리스트에서 바로구매 진행
	@Transactional
	@Override
	public void orderSaveDirect(OrderVO o_vo, PaymentVO p_vo, OrderDetailVO od_vo) { //OrderDetailVO 추가, String type 삭제
		//1)주문정보 저장하기(주문자와 배송지 정보)
		orderMapper.orderSave(o_vo); //odr_code필드에 시퀀스 값이 존재.
		  
		//2-2)주문상세 저장하기(주문상품 정보 - 바로 구매 내역만 들어가게 하고 싶다)
		orderMapper.orderDirectDetailSave(od_vo);  
		
		//3)결제정보 저장하기
		p_vo.setOdr_code(o_vo.getOdr_code());
		orderMapper.paymentSave(p_vo);  
		
	}
	
	
	//바로구매에서 보여줄 주문 내역
	@Override
	public CartVOList directOrder(CartVO vo) {
		
		return orderMapper.directOrder(vo);
	}

	//시퀀스 가져오기
	@Override
	public Long getOrderSequence() {
		
		return orderMapper.getOrderSequence();
	}

	//진행 주문 건수
	@Override
	public int getOrderProcessCount(String mem_id) {
		
		return orderMapper.getOrderProcessCount(mem_id);
	}

	//주문 내역(페이징 O, 검색 X)
	@Override
	public List<OrderVO> getOrderList(String mem_id, Criteria cri) {
		
		return orderMapper.getOrderList(mem_id, cri);
	}
	//주문 내역(페이징 O, 검색 X). 총 데이터 수
	@Override
	public int getOrderTotalCount(String mem_id) {
		
		return orderMapper.getOrderTotalCount(mem_id);
	}

	//주문상세정보
	@Override
	public List<OrderDetailProductVO> getOrderDetailList(Long odr_code) {
		
		return orderMapper.getOrderDetailList(odr_code);
	}


	
	
}
