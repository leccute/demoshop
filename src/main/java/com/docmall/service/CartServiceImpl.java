package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.mapper.CartMapper;

import lombok.Setter;

@Service
public class CartServiceImpl implements CartService{

	//주입
	@Setter(onMethod_ = {@Autowired})
	private CartMapper cartMapper;

	@Override
	public void cart_add(CartVO vo) {
		cartMapper.cart_add(vo);
		
	}

	@Override
	public List<CartVOList> cart_list(String mem_id) {
		
		return cartMapper.cart_list(mem_id);
	}

	@Override
	public void cart_qty_change(Long cart_code, int cart_amount) {
		cartMapper.cart_qty_change(cart_code, cart_amount);
		
	}

	@Override
	public void cart_delete(Long cart_code) {
		cartMapper.cart_delete(cart_code);
		
	}

	//장바구니 비우기
	@Override
	public void cart_empty(String mem_id) {
		cartMapper.cart_empty(mem_id);
		
	}

	//장바구니 선택 삭제
	@Override
	public void cart_sel_delete(List<Long> cart_code_arr) {
		cartMapper.cart_sel_delete(cart_code_arr);
		
	}
	
	//장바구니 사용자별 상품 개수
	@Override
	public int getCartProductCountByUserID(String mem_id) {
		
		
		return cartMapper.getCartProductCountByUserID(mem_id);
	}
}
