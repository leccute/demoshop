package com.docmall.service;

import java.util.List;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;

public interface CartService {

	//장바구니 추가
	void cart_add(CartVO vo);
	
	//장바구니 목록
	List<CartVOList> cart_list(String mem_id);
	
	//장바구니 수량 변경
	void cart_qty_change(Long cart_code, int cart_amount);
	
	//장바구니 삭제
	void cart_delete(Long cart_code);
	
	//장바구니 비우기
	void cart_empty(String mem_id);
	
	//장바구니 선택 삭제
	void cart_sel_delete(List<Long> cart_code_arr);
	
	//장바구니 사용자별 상품 개수
	int getCartProductCountByUserID(String mem_id);
}
