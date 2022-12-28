package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;

public interface CartMapper {

	//장바구니 추가
	void cart_add(CartVO vo);
	
	//장바구니 목록
	List<CartVOList> cart_list(String mem_id);
	
	//장바구니 수량 변경
	void cart_qty_change(@Param("cart_code") Long cart_code, @Param("cart_amount") int cart_amount);
	
	//장바구니 삭제
	void cart_delete(Long cart_code);
	
	//장바구니 비우기
	void cart_empty(String mem_id);
	
	//장바구니 선택 삭제. 파라미터 List컬렉션 사용.
	void cart_sel_delete(List<Long> cart_code_arr);
	/*
	 DELETE FROM cart_tbl WHERE cart_code in (31, 33);
	 DELETE FROM cart_tbl WHERE cart_code = 31 OR cart_code =33; 
	 */
	
	//장바구니 사용자별 상품 개수
	int getCartProductCountByUserID(String mem_id);
	
}
